/*
 * MgmtSensorHH.cc
 *
 *  Created on: Sep 3, 2019
 *      Author: jaevillen
 */

#include "MgmtSensorHH.h"

#include "inet/common/INETUtils.h"
#include "inet/common/ModuleAccess.h"
#include "inet/common/ProtocolTag_m.h"
#include "inet/common/Simsignals.h"
#include "inet/common/packet/Message.h"
#include "inet/linklayer/common/InterfaceTag_m.h"
#include "inet/linklayer/common/MacAddressTag_m.h"
#include "inet/linklayer/common/UserPriorityTag_m.h"
#include "inet/linklayer/ieee80211/mac/Ieee80211SubtypeTag_m.h"
#include "inet/linklayer/ieee80211/mgmt/Ieee80211MgmtSta.h"
#include "inet/networklayer/common/InterfaceEntry.h"
#include "inet/physicallayer/common/packetlevel/SignalTag_m.h"
#include "inet/physicallayer/contract/packetlevel/IRadioMedium.h"
#include "inet/physicallayer/contract/packetlevel/RadioControlInfo_m.h"
#include "inet/physicallayer/ieee80211/packetlevel/Ieee80211ControlInfo_m.h"

#include <stdio.h>

using namespace inet;
using namespace std;
using namespace ieee80211;
using namespace physicallayer;

Define_Module(MgmtSensorHH);

#define MK_BEACON_TIMEOUT         6
#define MAX_BEACONS_MISSED        3.5  // beacon lost timeout, in beacon intervals (doesn't need to be integer)
#define MK_ASSOC_TIMEOUT          2

void MgmtSensorHH::handleCommand(int msgkind, cObject *ctrl)
{
    if (auto cmd = dynamic_cast<Ieee80211Prim_ScanRequest *>(ctrl))
        processScanCommand(cmd);
    else if (auto cmd = dynamic_cast<Ieee80211Prim_AuthenticateRequest *>(ctrl))
        processAuthenticateCommand(cmd);
    else if (auto cmd = dynamic_cast<Ieee80211Prim_DeauthenticateRequest *>(ctrl))
        processDeauthenticateCommand(cmd);
    else if (auto cmd = dynamic_cast<Ieee80211Prim_AssociateRequest *>(ctrl))
        processAssociateCommand(cmd);
    else if (auto cmd = dynamic_cast<Ieee80211Prim_ReassociateRequest *>(ctrl))
        processReassociateCommand(cmd);
    else if (auto cmd = dynamic_cast<Ieee80211Prim_DisassociateRequest *>(ctrl))
        processDisassociateCommand(cmd);
    else if (ctrl)
        throw cRuntimeError("handleCommand(): unrecognized control info class `%s'", ctrl->getClassName());
    else
        throw cRuntimeError("handleCommand(): control info is nullptr");
    delete ctrl;
}

void MgmtSensorHH::processScanCommand(Ieee80211Prim_ScanRequest *ctrl)
{
    EV << "Received Scan Request from agent, clearing AP list and starting scanning...\n";

    if (isScanning)
       throw cRuntimeError("processScanCommand: scanning already in progress");

    if (mib->bssStationData.isAssociated){
        //disassociate(); COMMENTED BY JAEVILLEN
        //ADDED BY JAEVILLEN
        /*In the original code the station doesn't notify the AP that it's breaking the association
         * and, therefore the AP can't register the correct number of sensors that are associated */
        Ieee80211Prim_DisassociateRequest *req = new Ieee80211Prim_DisassociateRequest();
        req->setAddress(assocAP.address);
        req->setReasonCode(RC_UNSPECIFIED);
        processDisassociateCommand(req);
    }

    if (assocTimeoutMsg) {
        EV << "Cancelling ongoing association process\n";
        delete cancelEvent(assocTimeoutMsg);
        assocTimeoutMsg = nullptr;
    }

    // clear existing AP list (and cancel any pending authentications) -- we want to start with a clean page
    clearAPList();

    // fill in scanning state
    ASSERT(ctrl->getBSSType() == BSSTYPE_INFRASTRUCTURE);
    scanning.bssid = ctrl->getBSSID().isUnspecified() ? MacAddress::BROADCAST_ADDRESS : ctrl->getBSSID();
    scanning.ssid = ctrl->getSSID();
    scanning.activeScan = ctrl->getActiveScan();
    scanning.probeDelay = ctrl->getProbeDelay();
    scanning.channelList.clear();
    scanning.minChannelTime = ctrl->getMinChannelTime();
    scanning.maxChannelTime = ctrl->getMaxChannelTime();
    ASSERT(scanning.minChannelTime <= scanning.maxChannelTime);

    // channel list to scan (default: all channels)
    for (size_t i = 0; i < ctrl->getChannelListArraySize(); i++)
        scanning.channelList.push_back(ctrl->getChannelList(i));
    if (scanning.channelList.empty())
        for (int i = 0; i < numChannels; i++)
            scanning.channelList.push_back(i);

    // start scanning
    if (scanning.activeScan)
        host->subscribe(IRadio::receptionStateChangedSignal, this);
    scanning.currentChannelIndex = -1;    // so we'll start with index==0
    isScanning = true;
    scanNextChannel();

}

void MgmtSensorHH::startAssociation(ApInfo *ap, simtime_t timeout)
{
    if (mib->bssStationData.isAssociated ||assocTimeoutMsg)
        throw cRuntimeError("startAssociation: already associated or association currently in progress");
    if (!ap->isAuthenticated)
        throw cRuntimeError("startAssociation: not yet authenticated with AP address=", ap->address.str().c_str());

    // switch to that channel
    changeChannel(ap->channel);

    // create and send association request
    const auto& body = makeShared<Ieee80211AssociationRequestFrame>();

    //XXX set the following too?
    // string SSID
    // Ieee80211SupportedRatesElement supportedRates;

    body->setChunkLength(B(2 + 2 + strlen(body->getSSID()) + 2 + body->getSupportedRates().numRates + 2));
    sendManagementFrame("Assoc", body, ST_ASSOCIATIONREQUEST, ap->address);

    // schedule timeout
    ASSERT(assocTimeoutMsg == nullptr);
    assocTimeoutMsg = new cMessage("assocTimeout", MK_ASSOC_TIMEOUT);
    assocTimeoutMsg->setContextPointer(ap);
    scheduleAt(simTime() + timeout, assocTimeoutMsg);
}


void MgmtSensorHH::handleAssociationResponseFrame(Packet *packet, const Ptr<const Ieee80211MgmtHeader>& header)
{
    EV << "Received Association Response frame\n";

    if (!assocTimeoutMsg) {
        EV << "No association in progress, ignoring frame\n";
        delete packet;
        return;
    }

    // extract frame contents
    const auto& responseBody = packet->peekData<Ieee80211AssociationResponseFrame>();
    MacAddress address = header->getTransmitterAddress();
    int statusCode = responseBody->getStatusCode();
    //XXX short aid;
    //XXX Ieee80211SupportedRatesElement supportedRates;
    delete packet;

    // look up AP data structure
    ApInfo *ap = lookupAP(address);
    if (!ap)
        throw cRuntimeError("handleAssociationResponseFrame: AP not known: address=%s", address.str().c_str());

    if (mib->bssStationData.isAssociated) {
        EV << "Breaking existing association with AP address=" << assocAP.address << "\n";
        mib->bssStationData.isAssociated = false;
        delete cancelEvent(assocAP.beaconTimeoutMsg);
        assocAP.beaconTimeoutMsg = nullptr;
        assocAP = AssociatedApInfo();
    }

    delete cancelEvent(assocTimeoutMsg);
    assocTimeoutMsg = nullptr;

    if (statusCode != SC_SUCCESSFUL) {
        EV << "Association failed with AP address=" << ap->address << "\n";
    }
    else {
        EV << "Association successful, AP address=" << ap->address << "\n";

        // change our state to "associated"
        mib->bssData.bssid = ap->address;
        mib->bssStationData.isAssociated = true;
        (ApInfo&)assocAP = (*ap);

        emit(l2AssociatedSignal, myIface, ap);

        assocAP.beaconTimeoutMsg = new cMessage("beaconTimeout", MK_BEACON_TIMEOUT);
        scheduleAt(simTime() + MAX_BEACONS_MISSED * assocAP.beaconInterval, assocAP.beaconTimeoutMsg);

        //ADDED BY JAEVILLEN
        this->getParentModule()->getParentModule()->par("sinkAddress").setStringValue(responseBody->getSinkAddress());
    }

    // report back to agent
    sendAssociationConfirm(ap, statusCodeToPrimResultCode(statusCode));
}

void MgmtSensorHH::processDisassociateCommand(Ieee80211Prim_DisassociateRequest *ctrl)
{
    const MacAddress& address = ctrl->getAddress();

    if (mib->bssStationData.isAssociated && address == assocAP.address) {
        disassociate();
    }
    else if (assocTimeoutMsg) {
        // pending association
        delete cancelEvent(assocTimeoutMsg);
        assocTimeoutMsg = nullptr;
    }
    // create and send disassociation request
    const auto& body = makeShared<Ieee80211DisassociationFrame>();
    body->setReasonCode(ctrl->getReasonCode());
    sendManagementFrame("Disass", body, ST_DISASSOCIATION, address);
}

void MgmtSensorHH::disassociate()
{
    EV << "Disassociating from AP address=" << assocAP.address << "\n";
    ASSERT(mib->bssStationData.isAssociated);
    mib->bssStationData.isAssociated = false;
    if (assocAP.beaconTimeoutMsg) {
        delete cancelEvent(assocAP.beaconTimeoutMsg);
        assocAP.beaconTimeoutMsg = nullptr;
    }
    assocAP = AssociatedApInfo();    // clear it
}











