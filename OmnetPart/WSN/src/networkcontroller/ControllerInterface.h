/*
 * ControllerInterface.h
 *
 *  Created on: Aug 27, 2019
 *      Author: jaevillen
 */

#ifndef NETWORKCONTROLLER_CONTROLLERINTERFACE_H_
#define NETWORKCONTROLLER_CONTROLLERINTERFACE_H_

#include <map>
#include "../management/ap/ApReport.h"
#include "../Matlab_Bridge/OMNeTPk.h"
#include "../Matlab_Bridge/OMNeTPipe.h"



class ControllerInterface : public cSimpleModule,  protected cListener{

    public:

        class ApInfo : public cObject{
           ApReport currentReport;
            simsignal_t controlSignalID;
            bool Off;

            public:
                const ApReport& getCurrentReport() const { return currentReport; }
                void setCurrentReport(const ApReport& currentReport) { this->currentReport = currentReport; }
                bool isOff() const { return Off;  }
                void setOff(bool o) { this->Off = o; }
                const simsignal_t& getControlSignalID() const { return controlSignalID;}
                void setControlSignalID(const simsignal_t& controlSignalID) { this->controlSignalID = controlSignalID; }
        };
//        struct ThCompare {
//            bool operator()(const MacAddress& u1, const MacAddress& u2) const { return u1.compareTo(u2) < 0; }
//        };
        typedef std::map<int, ApInfo> ApList;
        typedef std::pair<int,ApInfo> pair;


    protected:
        cMessage *apSortingTimer = nullptr;
        cMessage *stopTimer = nullptr;
        ApList aplist;
        std::vector<pair> vec;
        std::string s = "turnOnOffap";
        OMNeTPipe* p;
        int packetsCount = 1;
        bool fuzzyControlled;

    public:
        ControllerInterface() {}
        virtual ~ControllerInterface();
    protected:
        virtual void initialize() override;
        virtual void receiveSignal (cComponent *source, simsignal_t signalID, cObject *obj, cObject *details) override;
        virtual void handleMessage(cMessage *msg)override;
        virtual simsignal_t concatRegister(std::string s, std::string s1);
        virtual ApInfo *lookupAp(int id);
        virtual void apAnalisys (const int id, ApInfo &ap);
        virtual void shutdownAp (const int id, ApInfo *ap);
        virtual void restartAp (const int id, ApInfo *ap);
};


#endif /* NETWORKCONTROLLER_CONTROLLERINTERFACE_H_ */
