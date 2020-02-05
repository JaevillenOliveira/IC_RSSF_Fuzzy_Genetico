/*
 * AgentSta.h
 *
 *  Created on: Sep 6, 2019
 *      Author: jaevillen
 */

#ifndef MANAGEMENT_AGENTSINK_H_
#define MANAGEMENT_AGENTSINK_H_

#include <vector>

#include "inet/common/INETDefs.h"
#include "inet/linklayer/ieee80211/mgmt/Ieee80211Primitives_m.h"
#include "inet/networklayer/common/InterfaceTable.h"
#include "inet/linklayer/ieee80211/mgmt/Ieee80211AgentSta.h"

using namespace inet;

using namespace ieee80211;


class AgentSink : public Ieee80211AgentSta
{
public:
    AgentSink() {}

 protected:
    virtual int chooseBSS(Ieee80211Prim_ScanConfirm *resp)override;
    virtual void processScanConfirm(Ieee80211Prim_ScanConfirm *resp)override;
};


#endif /* MANAGEMENT_AGENTSINK_H_ */
