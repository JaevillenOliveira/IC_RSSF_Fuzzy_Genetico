/*
 * MgmtSensorSH.h
 *
 *  Created on: 9 de jan de 2020
 *      Author: jaevillen
 */

#ifndef MANAGEMENT_MGMTSENSORSH_H_
#define MANAGEMENT_MGMTSENSORSH_H_

#include "MgmtSensorHH.h"

class MgmtSensorSH : public MgmtSensorHH{
    virtual void handleAssociationResponseFrame(Packet *packet, const Ptr<const Ieee80211MgmtHeader>& header) override;
    virtual void processScanCommand(Ieee80211Prim_ScanRequest *ctrl)override;
    virtual void startAssociation(ApInfo *ap, simtime_t timeout) override;
    virtual void handleCommand(int msgkind, cObject *ctrl) override;
    virtual void processDisassociateCommand(Ieee80211Prim_DisassociateRequest *ctrl)override;
    virtual void sendDisassociationConfirm();
};



#endif /* MANAGEMENT_MGMTSENSORSH_H_ */
