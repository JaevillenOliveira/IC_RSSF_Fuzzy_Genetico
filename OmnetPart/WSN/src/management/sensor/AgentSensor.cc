/*
 * AgentSensor.cc
 *
 *  Created on: Sep 25, 2019
 *      Author: jaevillen
 */


#include "inet/common/INETUtils.h"
#include "inet/common/ModuleAccess.h"
#include "inet/common/Simsignals.h"
#include "AgentSensor.h"
#include "inet/linklayer/ieee80211/mgmt/Ieee80211Primitives_m.h"

#include <string.h>
#include <stdio.h>
#include <iostream>

using namespace inet;
using namespace ieee80211;
using namespace std;

Define_Module(AgentSensor);

void AgentSensor::handleMessage(cMessage *msg)
{
    if (msg->isSelfMessage())
        handleTimer(msg);
    else{
        handleResponse(msg);
    }
}

void AgentSensor::handleResponse(cMessage *msg)
{
    cObject *ctrl = msg->removeControlInfo();

    EV << "Processing confirmation from mgmt: " << ctrl->getClassName() << "\n";

    if (auto ptr = dynamic_cast<Ieee80211Prim_ScanConfirm *>(ctrl))
        processScanConfirm(ptr);
    else if (auto ptr = dynamic_cast<Ieee80211Prim_AuthenticateConfirm *>(ctrl))
        processAuthenticateConfirm(ptr);
    else if (auto ptr = dynamic_cast<Ieee80211Prim_AssociateConfirm *>(ctrl))
        lprocessAssociateConfirm(ptr, msg);
    else if (auto ptr = dynamic_cast<Ieee80211Prim_ReassociateConfirm *>(ctrl))
        processReassociateConfirm(ptr);
    else if (auto ptr = dynamic_cast<Ieee80211PrimConfirm *>(ctrl))
        processDisassociateConfirm(ptr);
    else if (ctrl)
        throw cRuntimeError("handleResponse(): unrecognized control info class `%s'", ctrl->getClassName());
    else
        throw cRuntimeError("handleResponse(): control info is nullptr");
    delete msg; //moved from top to here - JAEVILLEN
    delete ctrl;

}
void AgentSensor::receiveSignal(cComponent *source, simsignal_t signalID, cObject *obj, cObject *details)
{
    Enter_Method_Silent();
    printSignalBanner(signalID, obj, details);

    if (signalID == l2BeaconLostSignal) {
        //XXX should check details if it's about this NIC
        EV << "beacon lost, starting scanning again\n";
        getContainingNode(this)->bubble("Beacon lost!");
        sendDisassociateRequest(this->prevAP,RC_UNSPECIFIED);
        emit(l2DisassociatedSignal, myIface);
    }
}

//WROTE BY JAEVILLEN
//listens for signals to disconnect from current AP
void AgentSensor::receiveSignal(cComponent *source, simsignal_t signalID, bool b, cObject *details)
{
    Enter_Method_Silent();
    printSignalBanner(signalID, b, details);

    if (signalID == handoverSignalID) {
        EV << "handover, starting scanning again\n";
        getContainingNode(this)->bubble("Handover!");
        sendScanRequest();
    }
}


//WROTE BY JAEVILLEN
//makes signal IDs based on its names
simsignal_t AgentSensor::concatRegister(std::string s, std::string s1){
        s += s1;
        int n = s.length();
        char char_array[n + 1];
        strcpy(char_array, s.c_str());
        const char *signalName = char_array;
        simsignal_t signalID = registerSignal(signalName);
        return signalID;
}

//CHANGED BY JAEVILLEN
void AgentSensor::lprocessAssociateConfirm(Ieee80211Prim_AssociateConfirm *resp, cMessage *msg)
{
    if (resp->getResultCode() != PRC_SUCCESS) {
        EV << "Association error\n";
        emit(dropConfirmSignal, PR_ASSOCIATE_CONFIRM);

        // try scanning again, maybe we'll have better luck next time, possibly with a different AP
        EV << "Going back to scanning\n";
        sendScanRequest();
    }
    else {
        EV << "Association successful\n";
        emit(acceptConfirmSignal, PR_ASSOCIATE_CONFIRM);
        // we are happy!
        getContainingNode(this)->bubble("Associated with AP");
        if (prevAP.isUnspecified() || prevAP != resp->getAddress()) {
            emit(l2AssociatedNewApSignal, myIface);    //XXX detail: InterfaceEntry?
            prevAP = resp->getAddress();

            //registers a new handover signal ID because it's associating to a new AP

           if(this->handoverSignalID != -1 && this->getSimulation()->getSystemModule()->isSubscribed(handoverSignalID, this)){
               this->getSimulation()->getSystemModule()->unsubscribe(handoverSignalID, this);
           }
           handoverSignalID = concatRegister(s_handover, msg->getSenderModule()->getParentModule()->getParentModule()->getAncestorPar("sinkAddress").stdstringValue());
           this->getSimulation()->getSystemModule()->subscribe(handoverSignalID, this);
        }
        else
            emit(l2AssociatedOldApSignal, myIface);
    }
}

void AgentSensor::processDisassociateConfirm(Ieee80211PrimConfirm *resp)
{
    sendScanRequest();
}

