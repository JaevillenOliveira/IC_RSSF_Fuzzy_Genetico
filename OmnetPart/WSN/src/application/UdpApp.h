/*
 * UdpApp.h
 *
 *  Created on: Sep 3, 2019
 *      Author: jaevillen
 */

#ifndef APPLICATION_UDPAPP_H_
#define APPLICATION_UDPAPP_H_

#include "inet/applications/udpapp/UdpBasicApp.h"

using namespace inet;

/**
 * UDP application. See NED for more info.
 */
class INET_API UdpApp : public UdpBasicApp
{
  protected:
    cPoisson poissonVar;

  protected:

    virtual void processStart()override;
    virtual L3Address chooseDestAddr()override;
    virtual void sendPacket()override;
    virtual void processSend()override;
    virtual void handleMessageWhenUp(cMessage *msg) override;


  public:
    UdpApp() {}
    ~UdpApp();
};


#endif /* APPLICATION_UDPAPP_H_ */
