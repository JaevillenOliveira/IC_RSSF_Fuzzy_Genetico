/*
 * UdpApp.cc
 *
 *  Created on: Sep 3, 2019
 *      Author: jaevillen
 */

#include "UdpApp.h"
#include "inet/networklayer/common/L3AddressResolver.h"
#include "inet/applications/base/ApplicationPacket_m.h"
#include "inet/applications/udpapp/UdpBasicApp.h"
#include "inet/common/ModuleAccess.h"
#include "inet/common/TagBase_m.h"
#include "inet/common/TimeTag_m.h"
#include "inet/common/lifecycle/ModuleOperations.h"
#include "inet/common/packet/Packet.h"
#include "inet/networklayer/common/FragmentationTag_m.h"
#include "inet/transportlayer/contract/udp/UdpControlInfo_m.h"
#include <stdio.h>

using namespace inet;
using namespace std;

Define_Module(UdpApp);

UdpApp::~UdpApp()
{
    cancelAndDelete(selfMsg);
}

L3Address UdpApp::chooseDestAddr()
{
    //the destination of the packets must be the sink attached to the AP in which the sensor is currently associated
    const char *destAddrs = this->getParentModule()->par("sinkAddress");//Line changed by Jaevillen

    L3Address result;
    L3AddressResolver().tryResolve(destAddrs, result);

    return result;
}

void UdpApp::sendPacket()
{
    std::ostringstream str;
    str << packetName << "-" << numSent;
    Packet *packet = new Packet(str.str().c_str());
    if(dontFragment)
        packet->addTagIfAbsent<FragmentationReq>()->setDontFragment(true);
    const auto& payload = makeShared<ApplicationPacket>();
    payload->setChunkLength(B(par("messageLength")));
    payload->setSequenceNumber(numSent);
    payload->addTag<CreationTimeTag>()->setCreationTime(simTime());
    packet->insertAtBack(payload);
    L3Address destAddr = chooseDestAddr();
    emit(packetSentSignal, packet);
    socket.sendTo(packet, destAddr, destPort);
    numSent++;
}

void UdpApp::processStart()
{
    socket.setOutputGate(gate("socketOut"));
    const char *localAddress = par("localAddress");
    socket.bind(*localAddress ? L3AddressResolver().resolve(localAddress) : L3Address(), localPort);
    setSocketOptions();

    //the destination of the packets must be the sink attached to the AP in which the sensor is currently associated
    const char *destAddrs = this->getParentModule()->par("sinkAddress");//Line changed by Jaevillen
    cStringTokenizer tokenizer(destAddrs);
    const char *token;

    while ((token = tokenizer.nextToken()) != nullptr) {
        destAddressStr.push_back(token);
        L3Address result;
        L3AddressResolver().tryResolve(token, result);
        if (result.isUnspecified())
            EV_ERROR << "cannot resolve destination address: " << token << endl;
        destAddresses.push_back(result);
    }

    if (!destAddresses.empty()) {
           selfMsg->setKind(SEND);
           processSend();
       }
    else {
        if (stopTime >= SIMTIME_ZERO) {
            selfMsg->setKind(STOP);
            scheduleAt(stopTime, selfMsg);
        }
    }
}

void UdpApp::processSend()
{
    sendPacket();

//    simtime_t d = simTime() + par("sendInterval");
    simtime_t d = simTime() + poisson(par("sendInterval"));
    if (stopTime < SIMTIME_ZERO || d < stopTime) {
        selfMsg->setKind(SEND);
        scheduleAt(d, selfMsg);
    }
    else {
        selfMsg->setKind(STOP);
        scheduleAt(stopTime, selfMsg);
    }
}

void UdpApp::handleMessageWhenUp(cMessage *msg)
{
    if (msg->isSelfMessage()) {
        ASSERT(msg == selfMsg);
        switch (selfMsg->getKind()) {
            case START:
                processStart();
                break;

            case SEND:
                processSend();
                break;

            case STOP:
                processStop();
                break;

            default:
                throw cRuntimeError("Invalid kind %d in self message", (int)selfMsg->getKind());
        }
    }
    else
        socket.processMessage(msg);
}
