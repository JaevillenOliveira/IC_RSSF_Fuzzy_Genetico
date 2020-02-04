/*
 * UdpSinkAdap.h
 *
 *  Created on: Sep 5, 2019
 *      Author: jaevillen
 */

#ifndef APPLICATION_UDPSINKADAP_H_
#define APPLICATION_UDPSINKADAP_H_

#include "inet/common/INETDefs.h"
#include "Throughput.h"
#include "inet/applications/base/ApplicationBase.h"
#include "inet/transportlayer/contract/udp/UdpSocket.h"
#include "inet/applications/udpapp/UdpSink.h"
using namespace inet;

/**
 * Consumes and prints packets received from the Udp module. See NED for more info.
 */
class  UdpSinkAdap : public UdpSink,  protected cListener
{
    UdpSocket socket;
    //CREATED BY JAEVILLEN: BEGIN
    ThroughputReport th;
    double rcvdThCount = 0;
    std::string s = "thReport";
    std::string s_resetSink = "resetSink";
    simsignal_t thReportSignalID;
    simsignal_t resetSignalID;
    //CREATED BY JAEVILLEN: END

    enum SelfMsgKinds { START = 1,SEND /*CREATED BY JAEVILLEN*/, STOP };

  public:
    UdpSinkAdap() {}
    virtual ~UdpSinkAdap();

  protected:
    virtual void sendReport();
    virtual void processStart() override;
    virtual void processStop()override;
    virtual void setSocketOptions();
    virtual simsignal_t concatRegister(std::string s, std::string s1);
    virtual void processPacket(Packet *msg)override;
    virtual void handleMessageWhenUp(cMessage *msg) override;
    virtual void socketDataArrived(UdpSocket *socket, Packet *packet) override;
    virtual void handleStopOperation(LifecycleOperation *operation) override;
    virtual void handleCrashOperation(LifecycleOperation *operation) override;
    virtual void receiveSignal(cComponent *source, simsignal_t signalID, bool b,cObject *details) override;

};


#endif /* APPLICATION_UDPSINKADAP_H_ */
