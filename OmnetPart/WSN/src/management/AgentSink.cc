/*
 * AgentSta.cc
 *
 *  Created on: Sep 6, 2019
 *      Author: jaevillen
 */

#include "AgentSink.h"
#include "inet/networklayer/common/L3AddressResolver.h"
#include <stdio.h>


using namespace std;

Define_Module(AgentSink);

void AgentSink::processScanConfirm(Ieee80211Prim_ScanConfirm *resp)
{
    // choose best AP
    int bssIndex;
    bssIndex = chooseBSS(resp);

    if (bssIndex == -1) {
        EV << "No (suitable) AP found, continue scanning\n";
        emit(dropConfirmSignal, PR_SCAN_CONFIRM);
        sendScanRequest();
        return;
    }

    dumpAPList(resp);
    emit(acceptConfirmSignal, PR_SCAN_CONFIRM);

    const Ieee80211Prim_BssDescription& bssDesc = resp->getBssList(bssIndex);
    EV << "Chosen AP address=" << bssDesc.getBSSID() << " from list, starting authentication\n";
    sendAuthenticateRequest(bssDesc.getBSSID());
}

//CHANGED BY JAEVILLEN
//always chooses the AP that was previously set because each sink should connect with one specific AP
int AgentSink::chooseBSS(Ieee80211Prim_ScanConfirm *resp)
{
    if (resp->getBssListArraySize() == 0)
        return -1;

    // here, just choose the one with the greatest receive power
    // TODO and which supports a good data rate we support
    L3AddressResolver addressResolver;
    int bestIndex = -1;
    for (size_t i = 0; i < resp->getBssListArraySize(); i++)
        if(resp->getBssList(i).getBSSID().equals(addressResolver.resolve(par("assocApAddress"), L3AddressResolver::ADDR_MAC).toMac())){
            bestIndex = i;
        }
    return bestIndex;
}
