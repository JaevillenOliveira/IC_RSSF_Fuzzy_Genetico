/*
 * UdpSinkAdap.cc
 *
 *  Created on: Sep 5, 2019
 *      Author: jaevillen
 */


#include "UdpSinkAdap.h"

#include "inet/common/ModuleAccess.h"
#include "inet/common/packet/Packet.h"
#include "inet/networklayer/common/L3AddressResolver.h"
#include "inet/transportlayer/contract/udp/UdpControlInfo_m.h"

#include <stdio.h>

using namespace inet;
using namespace std;

Define_Module(UdpSinkAdap);

UdpSinkAdap::~UdpSinkAdap()
{
    cancelAndDelete(selfMsg);
}

// sends a report informing the current throughput of the sink/AP
void UdpSinkAdap::sendReport(){
    this->th.setThroughput(rcvdThCount*8 / this->getSimulation()->getSimTime());
    emit(thReportSignalID, &th);
}

/*listens for signals from the controller to turn off or to restart the AP;
 *when the AP is turned off the sink has to put a 0 on the count of packets that calculates the throughput and the socket has to close*/
void UdpSinkAdap::receiveSignal(cComponent *source, simsignal_t signalID, bool b,cObject *details) {
    Enter_Method_Silent();
    if (signalID == resetSignalID) {
        if (b == true){
            selfMsg->setKind(STOP);
            scheduleAt(this->getSimulation()->getSimTime(), selfMsg);
            cancelAndDelete(selfMsg);
        }else{
            selfMsg = new cMessage("UDPSinkTimer");
            selfMsg->setKind(START);
            scheduleAt(this->getSimulation()->getSimTime(), selfMsg);
        }

    }
}
void UdpSinkAdap::processStart()
{
    socket.setOutputGate(gate("socketOut"));
    socket.setReuseAddress(true);
    if(!socket.isOpen()){
        socket.bind(localPort);
        setSocketOptions();
    }
    if (stopTime >= SIMTIME_ZERO) {
        selfMsg->setKind(STOP);
        scheduleAt(stopTime, selfMsg);
    }
    else{
        //LINES ADDED BY JAEVILLEN
        //make the IDs for listening the controller
        thReportSignalID = concatRegister(s, this->getParentModule()->getName()); //

        s_resetSink += std::to_string(this->getId()); //
        int n = s_resetSink.length(); //
        char char_reset[n + 1]; //
        strcpy(char_reset, s_resetSink.c_str()); //
        const char *resetSignalName = char_reset; //
        resetSignalID = registerSignal(resetSignalName); //
        this->getSimulation()->getSystemModule()->subscribe(resetSignalID,this); //

        selfMsg->setKind(SEND);
        scheduleAt(simTime() + uniform(0, 0.4), selfMsg);
    }
}

//make IDs for signals based on its names
simsignal_t UdpSinkAdap::concatRegister(std::string s, std::string s1){
        s += s1;
        int n = s.length();
        char char_array[n + 1];
        strcpy(char_array, s.c_str());
        const char *signalName = char_array;
        simsignal_t signalID = registerSignal(signalName);
        return signalID;
}

void UdpSinkAdap::handleMessageWhenUp(cMessage *msg)
{
    if (msg->isSelfMessage()) {
        ASSERT(msg == selfMsg);
        switch (selfMsg->getKind()) {
            case START:
                processStart();
                break;

            case STOP:
                processStop();
                rcvdThCount = 0;// ADDED BY JAEVILLEN; counts the received packets to calculate the throughput
                cancelAndDelete(selfMsg);
                break;

            case SEND:
                sendReport();
                selfMsg->setKind(SEND);
                scheduleAt(simTime() + uniform(0, 0.4), selfMsg);
                break;

        default:
                throw cRuntimeError("Invalid kind %d in self message", (int)selfMsg->getKind());
        }
    }
    else if (msg->arrivedOn("socketIn"))
        socket.processMessage(msg);
    else
        throw cRuntimeError("Unknown incoming gate: '%s'", msg->getArrivalGate()->getFullName());
}

void UdpSinkAdap::socketDataArrived(UdpSocket *socket, Packet *packet)
{
    // process incoming packet
    processPacket(packet);
}

void UdpSinkAdap::processPacket(Packet *pk)
{
    EV_INFO << "Received packet: " << UdpSocket::getReceivedPacketInfo(pk) << endl;
    emit(packetReceivedSignal, pk);

    std::string d = pk->getTotalLength().str();
    std::string e;
    int s = d.size();
    for(int i = 0; i < s - 2; i++){
        e += d[i];
    }
    rcvdThCount += std::stod(e); //ADDED BY JAEVILLEN
    numReceived++;

    delete pk;
}

void UdpSinkAdap::setSocketOptions()
{
    bool receiveBroadcast = par("receiveBroadcast");
    if (receiveBroadcast)
        socket.setBroadcast(true);

    MulticastGroupList mgl = getModuleFromPar<IInterfaceTable>(par("interfaceTableModule"), this)->collectMulticastGroups();
    socket.joinLocalMulticastGroups(mgl);

    // join multicastGroup
    const char *groupAddr = par("multicastGroup");
    multicastGroup = L3AddressResolver().resolve(groupAddr);
    if (!multicastGroup.isUnspecified()) {
        if (!multicastGroup.isMulticast())
            throw cRuntimeError("Wrong multicastGroup setting: not a multicast address: %s", groupAddr);
        socket.joinMulticastGroup(multicastGroup);
    }
    socket.setCallback(this);
}

void UdpSinkAdap::processStop()
{
    if (!multicastGroup.isUnspecified())
        socket.leaveMulticastGroup(multicastGroup); // FIXME should be done by socket.close()
    socket.setReuseAddress(true);
    socket.close();
}

void UdpSinkAdap::handleStopOperation(LifecycleOperation *operation)
{
    cancelEvent(selfMsg);
    if (!multicastGroup.isUnspecified())
        socket.leaveMulticastGroup(multicastGroup); // FIXME should be done by socket.close()
    socket.close();
    delayActiveOperationFinish(par("stopOperationTimeout"));
}

void UdpSinkAdap::handleCrashOperation(LifecycleOperation *operation)
{
    cancelEvent(selfMsg);
    if (operation->getRootModule() != getContainingNode(this)) {     // closes socket when the application crashed only
        if (!multicastGroup.isUnspecified())
            socket.leaveMulticastGroup(multicastGroup); // FIXME should be done by socket.close()
        socket.destroy();    //TODO  in real operating systems, program crash detected by OS and OS closes sockets of crashed programs.
    }
}


