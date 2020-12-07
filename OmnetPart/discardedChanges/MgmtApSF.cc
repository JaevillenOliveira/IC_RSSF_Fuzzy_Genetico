/*
 * MgmtApSF.cc
 *
 * This implementation is about a soft handover: when an access point receives the signal to turn off it waits until all of its sensors
 * are disconnected before it really turns off.
 *
 *  Created on: Feb 4, 2020
 *      Author: jaevillen
 */

#include "inet/linklayer/ieee80211/mac/Ieee80211SubtypeTag_m.h"

using namespace inet;
using namespace std;

#include "MgmtApSF.h"
#include <iostream>

Define_Module(MgmtApSF);

#define MK_BEACON_TIMEOUT         6

MgmtApSF::~MgmtApSF() {
    cancelAndDelete(beaconTimer);
    cancelAndDelete(reportTimer); //ADDED BY JAEVILLEN
}

void MgmtApSF::handleTimer(cMessage *msg) {
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
        cout << "HOW MANY SENSORS?  " << this->staList.size() << endl;
        if(this->staList.size() == 1){ //the AP only turns off when all its sensors are connected with another AP
            this->stop();
            handoverDelayTime = this->getSimulation()->getSimTime() - handoverDelayTime;
            cout << "   ";
            cout << "handover: ";
            cout << handoverDelayTime;
            emit(handoverDelay, handoverDelayTime);
        }else
            scheduleAt(simTime() + 0.05, handoverTimer);
    } else {
        throw cRuntimeError("internal error: unrecognized timer '%s'", msg->getName());
    }
}
//listens for the signal to turn off --from the controller
void MgmtApSF::receiveSignal(cComponent *source, simsignal_t signalID, bool b,cObject *details) {
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

void MgmtApSF::handleAssociationRequestFrame(Packet *packet,const Ptr<const Ieee80211MgmtHeader>& header) {
    EV << "Processing AssociationRequest frame\n";

    // "11.3.2 AP association procedures"
    StaInfo *sta = lookupSenderSTA(header);
    if (!sta || mib->bssAccessPointData.stations[sta->address] == Ieee80211Mib::NOT_AUTHENTICATED) {
        cout << "NOT AUTHENTICATED!!!!!!!!!!!!!";
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

void MgmtApSF::handleAssociationResponseFrame(Packet *packet,const Ptr<const Ieee80211MgmtHeader>& header) {
    dropManagementFrame(packet);
}


