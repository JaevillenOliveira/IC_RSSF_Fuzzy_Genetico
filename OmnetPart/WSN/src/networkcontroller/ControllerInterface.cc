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
#include <fstream>

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
    MyFile.close();
}

void ControllerInterface::initialize()
{
    try {
        this->getSimulation()->getSystemModule()->subscribe("reportReadySignal", this);
        opMode = par("opMode").stdstringValue();
        if(opMode == "fuzzyControlled"){
            apSortingTimer = new cMessage("apSortingTimer");
            scheduleAt(simTime() +  2, apSortingTimer);
            p = new OMNeTPipe("localhost", 18638);
            tNumber = std::to_string(par("tNumber").intValue());
            scNumber = std::to_string(par("scNumber").intValue());
            MyFile.open("/home/jaevillen/IC/OmnetPart/WSN/src/networktopology/FuzzyEntries/Sc"+scNumber+"T"+tNumber+".txt");
            MyFile << "ID  RSSI  Neighbors  Sources  Throughput" << endl;
        }else if(opMode == "randomOFF"){
            randomOffTimer = new cMessage("randomOffTimer");
            scheduleAt(simTime() +  2, randomOffTimer);
        }
        numberApsOff = 0;
    } catch(...){
        exit (3);
    }
};

void ControllerInterface::randomOff(const int id, ApInfo &ap){

    int turnOffPerc = rand() % 101;
    if(turnOffPerc >= 50 && !ap.isOff() && numberApsOff < (aplist.size()/2)){
        shutdownAp(id, &ap);
        numberApsOff++;
    }
}

std::vector<std::pair<int, ControllerInterface::ApInfo>> ControllerInterface::sortApByThroughput(){
    if(!aplist.empty()){
         vec.clear();
         std::copy(aplist.begin(),aplist.end(),std::back_inserter<std::vector<pair>>(vec)); //copies items from map to vector

         std::sort(vec.begin(), vec.end(), [](const pair& l, const pair& r) { //sorts vector by throughput averages
             if (l.second.getCurrentReport().getThroughput() != r.second.getCurrentReport().getThroughput())
                 return l.second.getCurrentReport().getThroughput() < r.second.getCurrentReport().getThroughput();
         });
         return vec;
    }
}

void ControllerInterface::handleMessage(cMessage *msg)
{
    if (msg == apSortingTimer) {
        std::vector<pair> vec = sortApByThroughput();

        bool changes = false;
        for (auto it = vec.begin(); it != vec.end() && !changes; ++it) {
          changes = apAnalisys(it->first, it->second);
        }
        if(numberApsOff < (aplist.size()/2)){
            scheduleAt(simTime() + 3, apSortingTimer);
        }
    }else if(msg == randomOffTimer){
        std::vector<pair> vec = sortApByThroughput();
        for (auto it = vec.begin(); it != vec.end(); ++it) {
            randomOff(it->first, it->second);
        }
        if(numberApsOff < (aplist.size()/2)){
            scheduleAt(simTime() + 10, randomOffTimer);
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

    MyFile << source->getId() << (" ");
    MyFile << report->getRssiMean() << (" ");
    MyFile << report->getNumberOfNeighbours() << (" ");
    MyFile << report->getNumberOfSta() << (" ");
    MyFile << report->getThroughput() << endl;

    printf("\n");
    cout << "ID: " << source->getId() << ("    ");
    cout << "Number of sensors: " << report->getNumberOfSta() << ("    ");
    cout << "Number of Neighbors: " << report->getNumberOfNeighbours() << ("    ");
    cout << "RSSI: " << report->getRssiMean() << ("    ");
    cout << "Throughput: " << report->getThroughput();
}

bool ControllerInterface::apAnalisys (const int id, ApInfo &ap){

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

    cout << "listSize " << aplist.size() << endl;
    cout << "numberOff " << numberApsOff << endl;
    if(resp <= 50 && ap.isOff()){
        restartAp(id, &ap);
        numberApsOff--;
        return true; // Means that a change was made in the network topology
    } else if (resp > 50 && !ap.isOff() && numberApsOff < (aplist.size()/2)){
        shutdownAp(id, &ap);
        numberApsOff++;
        return true;// Means that a change was made in the network topology
    }
    return false;// Means that no change was made in the network topology
}

void ControllerInterface::shutdownAp (const int id, ApInfo *ap){
    printf("\n %s %i ", "Shutting Down AP", id, "\n");
    ApInfo *apinfo = lookupAp(id);
    apinfo->setOff(true);
    this->emit(ap->getControlSignalID(),true, nullptr);
}

void ControllerInterface::restartAp (const int id, ApInfo *ap){
    printf("\n %s %i", "Restarting AP", id, "\n");
    ApInfo *apinfo = lookupAp(id);
    apinfo->setOff(false);
    this->emit(ap->getControlSignalID(),false, nullptr);
}









