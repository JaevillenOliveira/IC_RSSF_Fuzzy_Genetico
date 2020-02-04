/*
 * MgmtApSF.cc
 *
 *  Created on: Feb 4, 2020
 *      Author: jaevillen
 */


#include "MgmtApSF.h"
#include <iostream>

using namespace std;

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
        if(this->staList.size() == 1){ //the AP only turns off when all its sensors are connected with another AP
            handoverDelayTime = this->getSimulation()->getSimTime() - handoverDelayTime;
            this->stop();
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
