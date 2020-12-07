/*
 * MgmtApSF.h
 *
 *  Created on: Feb 4, 2020
 *      Author: jaevillen
 */

#ifndef MANAGEMENT_MGMTAPSF_H_
#define MANAGEMENT_MGMTAPSF_H_

#include "MgmtAp.h"

/**
 * Used in 802.11 infrastructure mode: handles management frames for
 * an access point (AP). See corresponding NED file for a detailed description.
 *
 * @author Andras Varga
 */

using namespace inet;
using namespace ieee80211;

class MgmtApSF: public MgmtAp{

public:
    MgmtApSF() {}
    virtual ~MgmtApSF();

protected:
    virtual void handleAssociationResponseFrame(Packet *packet,const Ptr<const Ieee80211MgmtHeader>& header) override;
    virtual void handleAssociationRequestFrame(Packet *packet,const Ptr<const Ieee80211MgmtHeader>& header) override;
    virtual void handleTimer(cMessage *msg) override;
    virtual void receiveSignal(cComponent *source, simsignal_t signalID, bool b,cObject *details) override;
};


#endif /* MANAGEMENT_MGMTAPSF_H_ */
