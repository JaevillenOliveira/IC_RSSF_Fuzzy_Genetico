/*
 * MgmtAp.h
 *
 *  Created on: Aug 20, 2019
 *      Author: jaevillen
 */

#ifndef MANAGEMENT_MGMTAP_H_
#define MANAGEMENT_MGMTAP_H_

#include <map>
#include "../application/Throughput.h"

#include "inet/common/INETDefs.h"
#include "inet/linklayer/ieee80211/mgmt/Ieee80211MgmtApBase.h"
#include "inet/physicallayer/common/packetlevel/SignalTag_m.h"

/**
 * Used in 802.11 infrastructure mode: handles management frames for
 * an access point (AP). See corresponding NED file for a detailed description.
 *
 * @author Andras Varga
 */
using namespace inet;
using namespace ieee80211;

class MgmtAp: public Ieee80211MgmtApBase, protected cListener {
public:
    /** Describes a STA */
    struct StaInfo {
        MacAddress address;
        int authSeqExpected; // when NOT_AUTHENTICATED: transaction sequence number of next expected auth frame
        //int consecFailedTrans;  //XXX
        //double expiry;          //XXX association should expire after a while if STA is silent?

        //ADDED BY JAEVILLEN
        double rssi;
    };

    //ADDED BY JAEVILLEN
    struct ApInfo: public cObject {
        int channel;
        MacAddress address;    // alias bssid
        std::string ssid;
        Ieee80211SupportedRatesElement supportedRates;
        simtime_t beaconInterval;
        double rxPower;

        bool isAuthenticated;
        int authSeqExpected;    // valid while authenticating; values: 1,3,5...
        cMessage *authTimeoutMsg; // if non-nullptr: authentication is in progress

        int receiveSequence;
        cMessage *beaconTimeoutMsg;

        ApInfo() {
            channel = -1;
            beaconInterval = rxPower = 0;
            authSeqExpected = -1;
            isAuthenticated = false;
            authTimeoutMsg = nullptr;

            receiveSequence = 0;
            beaconTimeoutMsg = nullptr;
        }
    };

    class NotificationInfoSta: public cObject {
        MacAddress apAddress;
        MacAddress staAddress;

        public:
            void setApAddress(const MacAddress& a) {apAddress = a;}
            void setStaAddress(const MacAddress& a) { staAddress = a;}
            const MacAddress& getApAddress() const { return apAddress; }
            const MacAddress& getStaAddress() const {  return staAddress; }
    };


    struct MacCompare {
        bool operator()(const MacAddress& u1, const MacAddress& u2) const { return u1.compareTo(u2) < 0; }
    };
    typedef std::map<MacAddress, StaInfo, MacCompare> StaList;
    typedef std::map<MacAddress, ApInfo, MacCompare> NeighborsAPsList;

protected:
    // configuration
    std::string ssid;
    int channelNumber = -1;
    simtime_t beaconInterval;
    int numAuthSteps = 0;
    Ieee80211SupportedRatesElement supportedRates;

    // state
    StaList staList;    ///< list of STAs

    cMessage *beaconTimer = nullptr;

    //ADDED BY JAEVILLEN:BEGIN
    NeighborsAPsList apList;
    cMessage *reportTimer = nullptr;
    cMessage *handoverTimer = nullptr;
    std::string s_turnOnOff = "turnOnOffap";
    std::string s_thReport = "thReport";
    std::string s_resetSink = "resetSink";
    std::string s_handover = "handoverFrom";
    simsignal_t turnOnOffSignalID;
    simsignal_t thReportSignalID;
    simsignal_t resetSinkSignalID;
    simsignal_t handoverSignalID;
    simsignal_t reportReadySignalID = registerSignal("reportReadySignal");
    simsignal_t handoverDelay = registerSignal("handoverDelay");
    simtime_t handoverDelayTime;
    ThroughputReport *th = nullptr;
    int udpSinkID = -1;
    int state = 1;
    //ADDED BY JAEVILLEN:END



public:
    MgmtAp() {}
    virtual ~MgmtAp();

protected:
    virtual int numInitStages() const override {return NUM_INIT_STAGES; }
    virtual void initialize(int) override;

    /** Implements abstract Ieee80211MgmtBase method */
    virtual void handleTimer(cMessage *msg) override;

    /** Implements abstract Ieee80211MgmtBase method -- throws an error (no commands supported) */
    virtual void handleCommand(int msgkind, cObject *ctrl) override;

    /** Called by the signal handler whenever a change occurs we're interested in */
    virtual void receiveSignal(cComponent *source, simsignal_t signalID,long value, cObject *details) override;

    /** Utility function: return sender STA's entry from our STA list, or nullptr if not in there */
    virtual StaInfo *lookupSenderSTA(const Ptr<const Ieee80211MgmtHeader>& header);

    /** Utility function: set fields in the given frame and send it out to the address */
    virtual void sendManagementFrame(const char *name,const Ptr<Ieee80211MgmtFrame>& body, int subtype,const MacAddress& destAddr);

    /** Utility function: creates and sends a beacon frame */
    virtual void sendBeacon();

    //ADDED BY JAEVILLEN: BEGIN

    simsignal_t concatRegister(std::string s, std::string s1);

    /**Used to send reports to the controller*/
    virtual void sendReport();
    virtual int countAssocSta();
    virtual void beaconLost(ApInfo *ap);
    virtual ApInfo *lookupAP(const MacAddress& address);
    virtual void storeAPInfo(Packet *packet,const Ptr<const Ieee80211MgmtHeader>& header,const Ptr<const Ieee80211BeaconFrame>& body);
    virtual double calculateRssiMean();
    virtual double calculateThroughput();
    virtual void receiveSignal(cComponent *source, simsignal_t signalID, bool b,cObject *details) override;
    virtual void receiveSignal(cComponent *source, simsignal_t signalID, cObject *obj,cObject *details) override;
    virtual void restart() ;
    //ADDED BY JAEVILLEN: END

    /** @name Processing of different frame types */
    //@{
    virtual void handleAuthenticationFrame(Packet *packet,const Ptr<const Ieee80211MgmtHeader>& header) override;
    virtual void handleDeauthenticationFrame(Packet *packet,const Ptr<const Ieee80211MgmtHeader>& header) override;
    virtual void handleAssociationRequestFrame(Packet *packet,const Ptr<const Ieee80211MgmtHeader>& header) override;
    virtual void handleAssociationResponseFrame(Packet *packet,const Ptr<const Ieee80211MgmtHeader>& header) override;
    virtual void handleReassociationRequestFrame(Packet *packet,const Ptr<const Ieee80211MgmtHeader>& header) override;
    virtual void handleReassociationResponseFrame(Packet *packet,const Ptr<const Ieee80211MgmtHeader>& header) override;
    virtual void handleDisassociationFrame(Packet *packet,const Ptr<const Ieee80211MgmtHeader>& header) override;
    virtual void handleBeaconFrame(Packet *packet,const Ptr<const Ieee80211MgmtHeader>& header) override;
    virtual void handleProbeRequestFrame(Packet *packet,const Ptr<const Ieee80211MgmtHeader>& header) override;
    virtual void handleProbeResponseFrame(Packet *packet,const Ptr<const Ieee80211MgmtHeader>& header) override;
    //@}

    void sendAssocNotification(const MacAddress& addr);

    void sendDisAssocNotification(const MacAddress& addr);

    /** lifecycle support */
    //@{
protected:
    virtual void start() override;
    virtual void stop() override;
    //@}
};

#endif /* MANAGEMENT_MGMTAP_H_ */
