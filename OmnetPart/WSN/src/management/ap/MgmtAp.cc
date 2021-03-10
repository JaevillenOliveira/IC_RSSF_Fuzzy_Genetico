/*
 * MgmtAp.cc
 *
 *  Created on: Aug 20, 2019
 *      Author: jaevillen
 */

#include "inet/common/ModuleAccess.h"
#include "inet/common/Simsignals.h"
#include "inet/linklayer/common/MacAddressTag_m.h"

#ifdef WITH_ETHERNET
#include "inet/linklayer/ethernet/EtherFrame_m.h"
#endif // ifdef WITH_ETHERNET

#include "inet/linklayer/ieee80211/mac/Ieee80211Frame_m.h"
#include "inet/linklayer/ieee80211/mac/Ieee80211SubtypeTag_m.h"
#include "MgmtAp.h"
#include "ApReport.h"
#include "inet/physicallayer/ieee80211/packetlevel/Ieee80211Radio.h"
#include "inet/physicallayer/common/packetlevel/SignalTag_m.h"
#include "inet/networklayer/common/L3AddressResolver.h"

#include <string.h>
#include <stdio.h>
#include <iostream>
#include <cmath>

using namespace inet;
using namespace ieee80211;
using namespace std;
using namespace physicallayer;

Define_Module(MgmtAp);
Register_Class(MgmtAp::NotificationInfoSta);

#define MK_BEACON_TIMEOUT         6
#define MAX_BEACONS_MISSED        3.5

static std::ostream& operator<<(std::ostream& os, const MgmtAp::StaInfo& sta) {
    os << "address:" << sta.address;
    return os;
}

MgmtAp::~MgmtAp() {
    cancelAndDelete(beaconTimer);
    cancelAndDelete(reportTimer); //ADDED BY JAEVILLEN
}

void MgmtAp::initialize(int stage) {
    Ieee80211MgmtApBase::initialize(stage);
    if (stage == INITSTAGE_LOCAL) {
        // read params and init vars
        ssid = par("ssid").stdstringValue();
        beaconInterval = par("beaconInterval");
        numAuthSteps = par("numAuthSteps");

        //ADDED BY JAEVILLEN:BEGIN
        networkNode = findContainingNode(this);
        nodeStatus = dynamic_cast<NodeStatus *>(networkNode->getSubmodule("status"));
        lifecycleOperationTimer = new cMessage("lifecycleOperation");
        //ADDED BY JAEVILLEN:END

        if (numAuthSteps != 2 && numAuthSteps != 4)
            throw cRuntimeError("parameter 'numAuthSteps' (number of frames exchanged during authentication) must be 2 or 4, not %d", numAuthSteps);
        channelNumber = -1; // value will arrive from physical layer in receiveChangeNotification()
        WATCH(ssid);
        WATCH(channelNumber);
        WATCH(beaconInterval);
        WATCH(numAuthSteps);
        WATCH_MAP(staList);

        //TBD fill in supportedRates

        // subscribe for notifications
        radioModule = getModuleFromPar<cModule>(par("radioModule"),this);
        radioModule->subscribe(Ieee80211Radio::radioChannelChangedSignal, this);

        //ADDED BY JAEVILLEN: BEGIN; registers control signals
        turnOnOffSignalID = concatRegister(s_turnOnOff,std::to_string(this->getId()));
        this->getSimulation()->getSystemModule()->subscribe(turnOnOffSignalID,this);

        thReportSignalID = concatRegister(s_thReport,par("sinkAddress").stdstringValue());
        this->getSimulation()->getSystemModule()->subscribe(thReportSignalID, this);

        handoverSignalID = concatRegister(s_handover, par("sinkAddress").stdstringValue());

        on = true;
        //ADDED BY JAEVILLEN:END

        // start beacon timer (randomize startup time)
        beaconTimer = new cMessage("beaconTimer");
        reportTimer = new cMessage("reportTimer");//ADDED BY JAEVILLEN
        handoverTimer = new cMessage("handoverTimer");//ADDED BY JAEVILLEN
    }
}

//CREATED BY JAEVILLEN
//makes signal IDs based on its names
simsignal_t MgmtAp::concatRegister(std::string s, std::string s1){
        s += s1;
        int n = s.length();
        char char_array[n + 1];
        strcpy(char_array, s.c_str());
        const char *signalName = char_array;
        simsignal_t signalID = registerSignal(signalName);
        return signalID;
}


void MgmtAp::handleTimer(cMessage *msg) {
    if (msg == beaconTimer) {
        sendBeacon();
        scheduleAt(simTime() + beaconInterval, beaconTimer);
    } else if (msg == reportTimer) { //ADDED BY JAEVILLEN
        sendReport();
        scheduleAt(simTime() + 0.5, reportTimer);
    } else if (msg->getKind() == MK_BEACON_TIMEOUT) {//ADDED BY JAEVILLEN; manage the list of active neighbors
        ApInfo *ap = static_cast<ApInfo*>(msg->getControlInfo());
        beaconLost(ap);
    }else if(msg == handoverTimer){//ADDED BY JAEVILLEN
        this->stop();
    } else {
        throw cRuntimeError("internal error: unrecognized timer '%s'", msg->getName());
    }
}

void MgmtAp::handleCommand(int msgkind, cObject *ctrl) {
    throw cRuntimeError("handleCommand(): no commands supported");
}

void MgmtAp::receiveSignal(cComponent *source, simsignal_t signalID, long value,cObject *details) {
    Enter_Method_Silent();
    if (signalID == Ieee80211Radio::radioChannelChangedSignal) {
        EV << "updating channel number\n";
        channelNumber = value;
    }
}

MgmtAp::StaInfo *MgmtAp::lookupSenderSTA(const Ptr<const Ieee80211MgmtHeader>& header) {
    auto it = staList.find(header->getTransmitterAddress());
    return it == staList.end() ? nullptr : &(it->second);
}

void MgmtAp::sendManagementFrame(const char *name,const Ptr<Ieee80211MgmtFrame>& body, int subtype,const MacAddress& destAddr) {
    auto packet = new Packet(name);
    packet->addTagIfAbsent<MacAddressReq>()->setDestAddress(destAddr);
    packet->addTagIfAbsent<Ieee80211SubtypeReq>()->setSubtype(subtype);
    packet->insertAtBack(body);
    sendDown(packet);
}

void MgmtAp::sendBeacon() {
    if (on){
        EV << "Sending beacon\n";
        const auto& body = makeShared<Ieee80211BeaconFrame>();
        body->setSSID(ssid.c_str());
        body->setSupportedRates(supportedRates);
        body->setBeaconInterval(beaconInterval);
        body->setChannelNumber(channelNumber);
        body->setChunkLength(B(8 + 2 + 2 + (2 + ssid.length()) + (2 + supportedRates.numRates)));
        sendManagementFrame("Beacon", body, ST_BEACON, MacAddress::BROADCAST_ADDRESS);
    }

}

//ADDED BY JAEVILLEN: BEGIN


void MgmtAp::executeNodeOperation(bool b)
{
    if (b && nodeStatus->getState() == NodeStatus::UP) {
        LifecycleOperation::StringMap params;
        ModuleStopOperation *operation = new ModuleStopOperation();
        operation->initialize(networkNode, params);
        lifecycleController.initiateOperation(operation);

    }
    else if (!b && nodeStatus->getState() == NodeStatus::DOWN) {
        LifecycleOperation::StringMap params;
        ModuleStartOperation *operation = new ModuleStartOperation();
        operation->initialize(networkNode, params);
        lifecycleController.initiateOperation(operation);
    }
}

//listens for the signal to turn off --from the controller
void MgmtAp::receiveSignal(cComponent *source, simsignal_t signalID, bool b,cObject *details) {
    Enter_Method_Silent();
    if (signalID == turnOnOffSignalID) {
        if (b == true){
            this->on = false;
            this->handoverDelayTime = this->getSimulation()->getSimTime();
            emit(handoverSignalID, true);
            scheduleAt(simTime(),handoverTimer);
        }else{
            this->restart();
            this->on = true;
        }
    }
}

//listens for the signal of new throughput report --from the sink
void MgmtAp::receiveSignal(cComponent *source, simsignal_t signalID, cObject *obj,cObject *details) {
    Enter_Method_Silent();
    if (signalID == thReportSignalID) {
        th = static_cast<ThroughputReport*> (obj);
        if(udpSinkID == -1){
            udpSinkID = source->getId();
            resetSinkSignalID = concatRegister(s_resetSink, std::to_string(udpSinkID));
        }
    }
}

//sends reports to the controller informing the number of sensors, neighbors, throughput and rssi mean
void MgmtAp::sendReport() {
    EV << "Sending Report\n";
    ApReport reportNotif;
    reportNotif.setNumberOfSta(this->countAssocSta()-1);
    reportNotif.setNumberOfNeighbours(apList.size());
    reportNotif.setRssiMean(calculateRssiMean());
    if(th != nullptr){
        reportNotif.setThroughput((this->th->getThroughput()*100)/1300000);
    }else
        reportNotif.setThroughput(0.0);
    emit(reportReadySignalID, &reportNotif);
}

int MgmtAp::countAssocSta(){
    int counter = 0;
    for (std::map<MacAddress, StaInfo, MacCompare>::iterator it = staList.begin(); it != staList.end(); ++it) {
        if(mib->bssAccessPointData.stations[it->first] == Ieee80211Mib::ASSOCIATED)
            counter++;
    }
    return counter;
}

//calculates the rssi mean based on the power of the transmissions from the sensors
double MgmtAp::calculateRssiMean() {
    StaInfo sta;
    double rssiMean = 0.0;
    if (!staList.empty()) {
        for (std::map<MacAddress, StaInfo, MacCompare>::iterator it = staList.begin(); it != staList.end(); ++it) {
            sta = it->second;
            rssiMean = rssiMean + sta.rssi;
        }
        rssiMean = rssiMean / (staList.size());
        return rssiMean;
    }
    return 0;
}

double MgmtAp::calculateThroughput() {

}

//manages the active neighbors
void MgmtAp::beaconLost(ApInfo *ap) {
    cancelEvent(ap->beaconTimeoutMsg);
    apList.erase(ap->address);
}

MgmtAp::ApInfo *MgmtAp::lookupAP(const MacAddress& address) {
    auto it = apList.find(address);
    return it == apList.end() ? nullptr : &(it->second);
}

void MgmtAp::storeAPInfo(Packet *packet,const Ptr<const Ieee80211MgmtHeader>& header,const Ptr<const Ieee80211BeaconFrame>& body) {
    auto address = header->getTransmitterAddress();
    ApInfo *ap = lookupAP(address);
    if (ap) {
        EV << "AP address=" << address << ", SSID=" << body->getSSID() << " already in our AP list, refreshing the info\n";
        ASSERT(ap->beaconTimeoutMsg != nullptr);
        cancelEvent(ap->beaconTimeoutMsg);
        scheduleAt(simTime() + MAX_BEACONS_MISSED * ap->beaconInterval,ap->beaconTimeoutMsg);

    } else {
        EV << "Inserting AP address=" << address << ", SSID=" << body->getSSID()<< " into our AP list\n";
        ap = &apList[address];
        ap->beaconInterval = body->getBeaconInterval();
        ap->beaconTimeoutMsg = new cMessage("beaconTimeout", MK_BEACON_TIMEOUT);
        ap->beaconTimeoutMsg->setControlInfo(ap);
        scheduleAt(simTime() + MAX_BEACONS_MISSED * ap->beaconInterval, ap->beaconTimeoutMsg);
    }
    ap->channel = body->getChannelNumber();
    ap->address = address;
    ap->ssid = body->getSSID();
    ap->supportedRates = body->getSupportedRates();
    ap->beaconInterval = body->getBeaconInterval();
}

//ADDED BY JAEVILLEN: END

//METHOD CHANGED BY JAEVILLEN
void MgmtAp::handleBeaconFrame(Packet *packet,const Ptr<const Ieee80211MgmtHeader>& header) {
    EV << "Received Beacon frame\n";
    const auto& beaconBody = packet->peekData<Ieee80211BeaconFrame>();
    storeAPInfo(packet, header, beaconBody);

    delete packet;


// Original code
//     dropManagementFrame(packet);
}

void MgmtAp::handleAuthenticationFrame(Packet *packet,const Ptr<const Ieee80211MgmtHeader>& header) {
    const auto& requestBody = packet->peekData<Ieee80211AuthenticationFrame>();
    int frameAuthSeq = requestBody->getSequenceNumber();
    EV << "Processing Authentication frame, seqNum=" << frameAuthSeq << "\n";

    // create STA entry if needed
    StaInfo *sta = lookupSenderSTA(header);
    if (!sta) {
        MacAddress staAddress = header->getTransmitterAddress();
        sta = &staList[staAddress];   // this implicitly creates a new entry
        sta->address = staAddress;
        mib->bssAccessPointData.stations[staAddress] = Ieee80211Mib::NOT_AUTHENTICATED;
        sta->authSeqExpected = 1;
    }
    //ADDED BY JAEVILLEN: BEGIN; gets the power of transmission from the packets received to calculate the rssi
    SignalPowerInd * signalPowerInd;
    signalPowerInd = packet->getTag<SignalPowerInd>();
    if (signalPowerInd != nullptr) {
        double power = signalPowerInd->getPower().get();
        sta->rssi = (10 * log10(1000 * power));
    }
    //END


    // reset authentication status, when starting a new auth sequence
    // The statements below are added because the L2 handover time was greater than before when
    // a STA wants to re-connect to an AP with which it was associated before. When the STA wants to
    // associate again with the previous AP, then since the AP is already having an entry of the STA
    // because of old association, and thus it is expecting an authentication frame number 3 but it
    // receives authentication frame number 1 from STA, which will cause the AP to return an Auth-Error
    // making the MN STA to start the handover process all over again.
    if (frameAuthSeq == 1) {
        if (mib->bssAccessPointData.stations[sta->address] == Ieee80211Mib::ASSOCIATED)
            sendDisAssocNotification(sta->address);
        mib->bssAccessPointData.stations[sta->address] = Ieee80211Mib::NOT_AUTHENTICATED;
        sta->authSeqExpected = 1;
    }

    // check authentication sequence number is OK
    if (frameAuthSeq != sta->authSeqExpected) {
        // wrong sequence number: send error and return
        EV << "Wrong sequence number, " << sta->authSeqExpected << " expected\n";
        const auto& body = makeShared<Ieee80211AuthenticationFrame>();
        body->setStatusCode(SC_AUTH_OUT_OF_SEQ);
        sendManagementFrame("Auth-ERROR", body, ST_AUTHENTICATION,header->getTransmitterAddress());
        delete packet;
        sta->authSeqExpected = 1;    // go back to start square
        return;
    }

    // station is authenticated if it made it through the required number of steps
    bool isLast = (frameAuthSeq + 1 == numAuthSteps);

    // send OK response (we don't model the cryptography part, just assume
    // successful authentication every time)
    EV << "Sending Authentication frame, seqNum=" << (frameAuthSeq + 1) << "\n";
    const auto& body = makeShared<Ieee80211AuthenticationFrame>();
    body->setSequenceNumber(frameAuthSeq + 1);
    body->setStatusCode(SC_SUCCESSFUL);
    body->setIsLast(isLast);
    // XXX frame length could be increased to account for challenge text length etc.
    sendManagementFrame(isLast ? "Auth-OK" : "Auth", body, ST_AUTHENTICATION, header->getTransmitterAddress());

    delete packet;

    // update status
    if (isLast) {
        if (mib->bssAccessPointData.stations[sta->address] == Ieee80211Mib::ASSOCIATED)
            sendDisAssocNotification(sta->address);
        mib->bssAccessPointData.stations[sta->address] = Ieee80211Mib::AUTHENTICATED; // XXX only when ACK of this frame arrives
        EV << "STA authenticated\n";
    } else {
        sta->authSeqExpected += 2;
        EV << "Expecting Authentication frame " << sta->authSeqExpected << "\n";
    }
}

void MgmtAp::handleDeauthenticationFrame(Packet *packet,const Ptr<const Ieee80211MgmtHeader>& header) {
    EV << "Processing Deauthentication frame\n";

    StaInfo *sta = lookupSenderSTA(header);
    delete packet;

    if (sta) {
        // mark STA as not authenticated; alternatively, it could also be removed from staList
        if (mib->bssAccessPointData.stations[sta->address] == Ieee80211Mib::ASSOCIATED)
            sendDisAssocNotification(sta->address);
        mib->bssAccessPointData.stations[sta->address] =  Ieee80211Mib::NOT_AUTHENTICATED;
        sta->authSeqExpected = 1;
    }
}

void MgmtAp::handleAssociationRequestFrame(Packet *packet,const Ptr<const Ieee80211MgmtHeader>& header) {
    EV << "Processing AssociationRequest frame\n";

    // "11.3.2 AP association procedures"
    StaInfo *sta = lookupSenderSTA(header);
    if (!sta || mib->bssAccessPointData.stations[sta->address] == Ieee80211Mib::NOT_AUTHENTICATED) {
        // STA not authenticated: send error and return
        const auto& body = makeShared<Ieee80211DeauthenticationFrame>();
        body->setReasonCode(RC_NONAUTH_ASS_REQUEST);
        sendManagementFrame("Deauth", body, ST_DEAUTHENTICATION, header->getTransmitterAddress());
        delete packet;
        return;
    }

    delete packet;

    // mark STA as associated
    if (mib->bssAccessPointData.stations[sta->address] != Ieee80211Mib::ASSOCIATED)
        sendAssocNotification(sta->address);
    mib->bssAccessPointData.stations[sta->address] = Ieee80211Mib::ASSOCIATED; // XXX this should only take place when MAC receives the ACK for the response

    // send OK response
    const auto& body = makeShared<Ieee80211AssociationResponseFrame>();
    body->setStatusCode(SC_SUCCESSFUL);
    body->setAid(0);    //XXX
    body->setSupportedRates(supportedRates);
    body->setChunkLength(B(2 + 2 + 2 + body->getSupportedRates().numRates + 2));
    body->setSinkAddress(this->par("sinkAddress").stdstringValue()); //LINE INSERTED BY JAEVILLEN
    sendManagementFrame("AssocResp-OK", body, ST_ASSOCIATIONRESPONSE,sta->address);
}

void MgmtAp::handleAssociationResponseFrame(Packet *packet,const Ptr<const Ieee80211MgmtHeader>& header) {
    dropManagementFrame(packet);
}

void MgmtAp::handleReassociationRequestFrame(Packet *packet,const Ptr<const Ieee80211MgmtHeader>& header) {
    EV << "Processing ReassociationRequest frame\n";

    // "11.3.4 AP reassociation procedures" -- almost the same as AssociationRequest processing
    StaInfo *sta = lookupSenderSTA(header);
    if (!sta || mib->bssAccessPointData.stations[sta->address] == Ieee80211Mib::NOT_AUTHENTICATED) {
        // STA not authenticated: send error and return
        const auto& body = makeShared<Ieee80211DeauthenticationFrame>();
        body->setReasonCode(RC_NONAUTH_ASS_REQUEST);
        sendManagementFrame("Deauth", body, ST_DEAUTHENTICATION, header->getTransmitterAddress());
        delete packet;
        return;
    }

    delete packet;

    // mark STA as associated
    mib->bssAccessPointData.stations[sta->address] =
            Ieee80211Mib::ASSOCIATED; // XXX this should only take place when MAC receives the ACK for the response

    // send OK response
    const auto& body = makeShared<Ieee80211ReassociationResponseFrame>();
    body->setStatusCode(SC_SUCCESSFUL);
    body->setAid(0);    //XXX
    body->setSupportedRates(supportedRates);
    body->setChunkLength( B(2 + (2 + ssid.length()) + (2 + supportedRates.numRates) + 6));
    sendManagementFrame("ReassocResp-OK", body, ST_REASSOCIATIONRESPONSE,sta->address);

}

void MgmtAp::handleReassociationResponseFrame(Packet *packet,const Ptr<const Ieee80211MgmtHeader>& header) {
    dropManagementFrame(packet);
}

void MgmtAp::handleDisassociationFrame(Packet *packet, const Ptr<const Ieee80211MgmtHeader>& header) {
    StaInfo *sta = lookupSenderSTA(header);
    delete packet;
    printf("Disassociating");

    if (sta) {
        if (mib->bssAccessPointData.stations[sta->address] == Ieee80211Mib::ASSOCIATED)
            sendDisAssocNotification(sta->address);
        mib->bssAccessPointData.stations[sta->address] = Ieee80211Mib::AUTHENTICATED;

    }
}

void MgmtAp::handleProbeRequestFrame(Packet *packet,const Ptr<const Ieee80211MgmtHeader>& header) {
    EV << "Processing ProbeRequest frame\n";

    const auto& requestBody =  packet->peekData<Ieee80211ProbeRequestFrame>();
    if (strcmp(requestBody->getSSID(), "") != 0 && strcmp(requestBody->getSSID(), ssid.c_str()) != 0) {
        EV << "SSID `" << requestBody->getSSID() << "' does not match, ignoring frame\n";
        dropManagementFrame(packet);
        return;
    }

    MacAddress staAddress = header->getTransmitterAddress();
    delete packet;

    EV << "Sending ProbeResponse frame\n";
    const auto& body = makeShared<Ieee80211ProbeResponseFrame>();
    body->setSSID(ssid.c_str());
    body->setSupportedRates(supportedRates);
    body->setBeaconInterval(beaconInterval);
    body->setChannelNumber(channelNumber);
    body->setChunkLength(B(8 + 2 + 2 + (2 + ssid.length()) + (2 + supportedRates.numRates)));
    sendManagementFrame("ProbeResp", body, ST_PROBERESPONSE, staAddress);
}

void MgmtAp::handleProbeResponseFrame(Packet *packet,const Ptr<const Ieee80211MgmtHeader>& header) {
    dropManagementFrame(packet);
}

void MgmtAp::sendAssocNotification(const MacAddress& addr) {
    NotificationInfoSta notif;
    notif.setApAddress(mib->address);
    notif.setStaAddress(addr);
    emit(l2ApAssociatedSignal, &notif);
}

void MgmtAp::sendDisAssocNotification(const MacAddress& addr) {
    NotificationInfoSta notif;
    notif.setApAddress(mib->address);
    notif.setStaAddress(addr);
    emit(l2ApDisassociatedSignal, &notif);
}

void MgmtAp::start() {
    Ieee80211MgmtApBase::start();
    scheduleAt(simTime() + uniform(0, beaconInterval), beaconTimer);
    scheduleAt(simTime() + uniform(0.5, 0.5), reportTimer);//ADDED BY JAEVILLEN
}

//ADDED BY JAEVILLEN
void MgmtAp::restart() {
    this->executeNodeOperation(false);
    Ieee80211MgmtApBase::start();
//    scheduleAt(simTime() + uniform(0, beaconInterval), beaconTimer);
//    scheduleAt(simTime() + uniform(0.5, 0.5), reportTimer);
    emit(resetSinkSignalID, false, nullptr);
}

void MgmtAp::stop() {
    cancelEvent(beaconTimer);
    cancelEvent(reportTimer);
    staList.clear();
    ApInfo ap;
    for (std::map<MacAddress, ApInfo, MacCompare>::iterator it = apList.begin();
        it != apList.end(); ++it) {
        ap = it->second;
        cancelEvent(ap.beaconTimeoutMsg);

    }
    apList.clear();
    this->th = nullptr; //clear the throughput counter
    emit(resetSinkSignalID, true, nullptr);
    this->executeNodeOperation(true);
    Ieee80211MgmtApBase::stop();
}
