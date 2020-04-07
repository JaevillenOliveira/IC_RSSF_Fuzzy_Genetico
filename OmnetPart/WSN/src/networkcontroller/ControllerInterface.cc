/*
 * cMatlabInterface.cc
 *
 *  Created on: Jul 18, 2019
 *      Author: jaevillen
 */

#include <string.h>
#include <omnetpp.h>
#include <stdio.h>
#include <random>
#include <iostream>
#include "ControllerInterface.h"
#include "../management/ap/ApReport.h"

Define_Module(ControllerInterface);

Register_Class(ControllerInterface::ApInfo);

ControllerInterface::~ControllerInterface() {
    cancelAndDelete(apSortingTimer);
    std::string s = "endingPacket";
    int n = s.length();
    char char_array[n + 1];
    strcpy(char_array, s.c_str());
    char *packetName = char_array;

    OMNeTPk* pk = new OMNeTPk(packetName);
    p->sendPk(*pk);
}

void ControllerInterface::initialize()
{
    this->getSimulation()->getSystemModule()->subscribe("reportReadySignal", this);
    fuzzyControlled = par("fuzzyControlled");
    randomOff = par("randomOff");
    if(fuzzyControlled){
        apSortingTimer = new cMessage("apSortingTimer");
        scheduleAt(simTime() +  2, apSortingTimer);
        p = new OMNeTPipe("localhost", 18638);
    }else if(randomOff){
        randomOffTimer = new cMessage("randomOffTimer");
        scheduleAt(simTime() +  2, randomOffTimer);
    }
};

void ControllerInterface::chooseRandomAp(){
    if(!aplist.empty()){
        vec.clear();
        std::copy(aplist.begin(),aplist.end(),std::back_inserter<std::vector<pair>>(vec)); //copies items from map to vector
    }
    std::random_device rd;  //Will be used to obtain a seed for the random number engine
    std::mt19937 gen(rd()); //Standard mersenne_twister_engine seeded with rd()
    std::uniform_int_distribution<> dis(0, vec.size() - 1);

    int apToTurnOff = dis(gen);

    cout << "Vec size" << vec.size() << endl;
    cout << "apToTurnOff" << apToTurnOff << endl;

    auto ap = vec.at(apToTurnOff);
    int id = ap.first;
    ApInfo apinfo = ap.second;

    int numberApsOff = 0;
    for (auto it = aplist.begin(); it != aplist.end(); ++it) {
        if(it->second.isOff())
            numberApsOff++;
    }
    cout << "APs OFF:  " << numberApsOff << endl;
    if (!apinfo.isOff() && numberApsOff < (aplist.size()/2)){
        shutdownAp(id, &apinfo);
        scheduleAt(simTime() +  2, randomOffTimer);

    }else if(apinfo.isOff() && numberApsOff < (aplist.size()/2)){
        scheduleAt(simTime() +  2, randomOffTimer);
    }
}

void ControllerInterface::sortApByThroughput(){
    if(!aplist.empty()){
         vec.clear();
         std::copy(aplist.begin(),aplist.end(),std::back_inserter<std::vector<pair>>(vec)); //copies items from map to vector

         std::sort(vec.begin(), vec.end(), [](const pair& l, const pair& r) { //sorts vector by throughput averages
             if (l.second.getCurrentReport().getThroughput() != r.second.getCurrentReport().getThroughput())
                 return l.second.getCurrentReport().getThroughput() < r.second.getCurrentReport().getThroughput();
         });
         for (auto it = vec.begin(); it != vec.end(); ++it) {
             apAnalisys(it->first, it->second);
         }
    }
}

void ControllerInterface::handleMessage(cMessage *msg)
{
    if (msg == apSortingTimer) {
        sortApByThroughput();
        scheduleAt(simTime() + 10, apSortingTimer);
    }else if(msg == randomOffTimer){
        chooseRandomAp();
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
        restartAp(id, &ap);
    } else if (resp > 50 && !ap.isOff() && numberApsOff < (aplist.size()/2)){
        shutdownAp(id, &ap);
    }
}

void ControllerInterface::shutdownAp (const int id, ApInfo *ap){
    printf("\n %s %i ", "Shutting Down AP", id);
    ApInfo *apinfo = lookupAp(id);
    apinfo->setOff(true);
    this->emit(ap->getControlSignalID(),true, nullptr);
}

void ControllerInterface::restartAp (const int id, ApInfo *ap){
    printf("\n %s %i", "Restarting AP", id);
    ApInfo *apinfo = lookupAp(id);
    apinfo->setOff(false);
    this->emit(ap->getControlSignalID(),false, nullptr);
}









