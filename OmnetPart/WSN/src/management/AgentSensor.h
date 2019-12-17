/*
 * AgentSensor.h
 *
 *  Created on: Sep 25, 2019
 *      Author: jaevillen
 */

#ifndef MANAGEMENT_AGENTSENSOR_H_
#define MANAGEMENT_AGENTSENSOR_H_

#include "inet/linklayer/ieee80211/mgmt/Ieee80211AgentSta.h"

using namespace inet;

using namespace ieee80211;

class AgentSensor : public Ieee80211AgentSta
{
protected:
    //CREATED BY JAEVILLEN: BEGIN
    std::string s_handover = "handoverFrom";
    simsignal_t handoverSignalID = -1;
    //CREATED BY JAEVILLEN: END


protected:
    virtual void handleMessage(cMessage *msg) override;
    virtual void handleResponse(cMessage *msg) override;
    virtual void receiveSignal(cComponent *source, simsignal_t signalID, bool b, cObject *details) override;
    virtual void receiveSignal(cComponent *source, simsignal_t signalID, cObject *obj, cObject *details) override;
    simsignal_t concatRegister(std::string s, std::string s1);
    virtual void lprocessAssociateConfirm(Ieee80211Prim_AssociateConfirm *resp, cMessage *msg);
};



#endif /* MANAGEMENT_AGENTSENSOR_H_ */
