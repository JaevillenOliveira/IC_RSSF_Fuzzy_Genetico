/*
 * MgmtSensor.h
 *
 *  Created on: Sep 3, 2019
 *      Author: jaevillen
 */

#ifndef MANAGEMENT_MGMTSENSOR_H_
#define MANAGEMENT_MGMTSENSOR_H_

#include "inet/linklayer/ieee80211/mgmt/Ieee80211MgmtSta.h"
#include "inet/common/INETDefs.h"
#include "inet/linklayer/ieee80211/mgmt/Ieee80211MgmtBase.h"
#include "inet/linklayer/ieee80211/mgmt/Ieee80211Primitives_m.h"

using namespace inet;

class InterfaceEntry;

using namespace ieee80211;

class MgmtSensor : public Ieee80211MgmtSta
{
protected:
    std::string s_handover = "handoverFromAp";
    simsignal_t handoverSignalID;

protected:

    virtual void handleAssociationResponseFrame(Packet *packet, const Ptr<const Ieee80211MgmtHeader>& header) override;
    virtual void handleCommand(int msgkind, cObject *ctrl) override;
    virtual void processScanCommand(Ieee80211Prim_ScanRequest *ctrl);
    virtual void startAssociation(ApInfo *ap, simtime_t timeout) override;
    virtual void processDisassociateCommand(Ieee80211Prim_DisassociateRequest *ctrl)override;
    virtual void disassociate()override;
};

#endif /* MANAGEMENT_MGMTSENSOR_H_ */
