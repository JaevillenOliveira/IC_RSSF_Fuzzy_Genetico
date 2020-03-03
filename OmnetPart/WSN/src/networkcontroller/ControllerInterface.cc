/*
 * cMatlabInterface.cc
 *
 *  Created on: Jul 18, 2019
 *      Author: jaevillen
 */

#include <string.h>
#include <omnetpp.h>
#include <stdio.h>
#include <iostream>
#include "ControllerInterface.h"
#include "../management/ap/ApReport.h"


using namespace std;
using std::endl;
using namespace omnetpp;

#define ON                        1
#define OFF                       0

Define_Module(ControllerInterface);
Register_Class(ControllerInterface::ApInfo);

ControllerInterface::~ControllerInterface() {
    cancelAndDelete(apSortingTimer);
}

void ControllerInterface::initialize()
{
    this->getSimulation()->getSystemModule()->subscribe("reportReadySignal", this);
    apSortingTimer = new cMessage("apSortingTimer");
    scheduleAt(simTime() +  2, apSortingTimer);
    p = new OMNeTPipe("localhost", 18638);

};

void ControllerInterface::handleMessage(cMessage *msg)
{
    if (msg == apSortingTimer) {
        if(!aplist.empty()){
            vec.clear();
            std::copy(aplist.begin(),aplist.end(),std::back_inserter<std::vector<pair>>(vec)); //copies items from map to vector

            std::sort(vec.begin(), vec.end(), [](const pair& l, const pair& r) { //sorts vector by throughput averages
                if (l.second.getCurrentReport().getThroughput() != r.second.getCurrentReport().getThroughput())
                    return l.second.getCurrentReport().getThroughput() < r.second.getCurrentReport().getThroughput();
            });
//            for (auto const &pair: vec) {
//                cout <<  pair.first;
//                printf("\n");
//            }
            for (auto it = vec.begin(); it != vec.end(); ++it) {
                apAnalisys(it->first, it->second);
            }
            scheduleAt(simTime() + 10, apSortingTimer);
        }
    }
}

ControllerInterface::ApInfo *ControllerInterface::lookupAp(int id) {
    auto it = aplist.find(id);
    return it == aplist.end() ? nullptr : &(it->second);
}

simsignal_t ControllerInterface::concatRegister(std::string s, std::string s1){
        s += s1;
        int n = s.length();
        char char_array[n + 1];
        strcpy(char_array, s.c_str());
        const char *signalName = char_array;
        simsignal_t signalID = registerSignal(signalName);
        return signalID;
}

void ControllerInterface::receiveSignal (cComponent *source, simsignal_t signalID, cObject *obj, cObject *details)
{
    Enter_Method_Silent();
    ApReport *report;
    ApInfo *ap = lookupAp(source->getId());
    if(!ap){
        printf("\n %s %i %s ", "AP", source->getId(), "now is in the list ");
        ap = &aplist[source->getId()];
        ap->setControlSignalID(concatRegister(s, std::to_string(source->getId())));
        ap->setOff(false);
    }
    report = static_cast<ApReport*> (obj);
    ap->setCurrentReport(*report);

    printf("\n");
    cout << "ID: " << source->getId() << ("    ");
    cout << "Number of sensors: " << report->getNumberOfSta() << ("    ");
    cout << "Number of Neighbors: " << report->getNumberOfNeighbours() << ("    ");
    cout << "RSSI: " << report->getRssiMean() << ("    ");
    cout << "Throughput: " << report->getThroughput();
}

void ControllerInterface::apAnalisys (const int id, ApInfo &ap){

    std::string s = "packet" + std::to_string(packetsCount++);
    int n = s.length();
    char char_array[n + 1];
    strcpy(char_array, s.c_str());
    char *packetName = char_array;

    OMNeTPk* pk = new OMNeTPk(packetName);
    pk->addVal("sensors", FLOAT(ap.getCurrentReport().getNumberOfSta()), TYPE_FLOAT);
    pk->addVal("neighbors", FLOAT(ap.getCurrentReport().getNumberOfNeighbours()), TYPE_FLOAT);
    pk->addVal("rssi", FLOAT(ap.getCurrentReport().getRssiMean()), TYPE_FLOAT);
    pk->addVal("throughput", FLOAT(ap.getCurrentReport().getThroughput()), TYPE_FLOAT);
    p->sendPk(*pk);

    OMNeTPk* rpk = p->recvPk();
    char* rName = rpk->getName(0);
    void* r = rpk->getVal(rpk->getName(0));
    float resp = *(float *)&r;
    cout << rName << "  " << resp << endl;


    int numberApsOff = 0;
    for (auto it = aplist.begin(); it != aplist.end(); ++it) {
        if(it->second.isOff())
            numberApsOff++;
    }
    cout << "numberOff " << numberApsOff;
    if(resp <= 50 && ap.isOff()){
        ApInfo *apinfo = lookupAp(id);
        apinfo->setOff(false);
        restartAp(id, &ap);

    } else if (resp > 50 && !ap.isOff() && numberApsOff < (aplist.size()/2)){
        ApInfo *apinfo = lookupAp(id);
        apinfo->setOff(true);
        shutdownAp(id, &ap);
        cout << "Shutting down ap" << id << endl ;

    }
}

void ControllerInterface::shutdownAp (const int id, ApInfo *ap){
    printf("\n %s %i ", "Shutting Down AP", id);
    this->emit(ap->getControlSignalID(),true, nullptr);
}

void ControllerInterface::restartAp (const int id, ApInfo *ap){
    printf("\n %s %i", "Restarting AP", id);
    this->emit(ap->getControlSignalID(),false, nullptr);
}









