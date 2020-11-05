package wsn.networktopology;
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License
// along with this program.  If not, see http://www.gnu.org/licenses/.
// 

import inet.networklayer.configurator.ipv4.Ipv4NetworkConfigurator;
import inet.node.inet.WirelessHost;
import inet.node.wireless.AccessPoint;
import inet.physicallayer.ieee80211.packetlevel.Ieee80211ScalarRadioMedium;
import inet.power.contract.IEnergyGenerator;
import inet.power.contract.IEnergyManagement;
import inet.power.contract.IEnergyStorage;
import inet.visualizer.integrated.IntegratedCanvasVisualizer;


module myAccesspoint extends AccessPoint
{
    parameters:
        @networkNode();
        @labels(node,ethernet-node,wireless-node);
        wlan[*].mgmt.typename = default("MgmtAp");
        mobility.subjectModule = default("^");
        *.mobilityModule = default(exists(mobility) ? absPath(".mobility") : "");
        *.energySourceModule = default(exists(energyStorage) ? absPath(".energyStorage") : "");
        *.energyGeneratorModule = default(exists(energyGenerator) ? absPath(".energyGenerator") : "");
    submodules:
        energyStorage: <default("")> like IEnergyStorage if typename != "" {
            parameters:
                @display("p=307,480;is=s");
        }
        energyManagement: <default("")> like IEnergyManagement if typename != "" {
            parameters:
                @display("p=212,457;is=s");
        }
        energyGenerator: <default("")> like IEnergyGenerator if typename != "" {
            parameters:
                @display("p=117,480;is=s");
        }
}

module Sensor extends WirelessHost
{
    parameters:
        string sinkAddress;
        wlan[*].mgmt.typename = default("MgmtSensorHH");
        wlan[*].agent.typename = default("AgentSensor");
    submodules:
}

module ControllerInterface
{
    parameters:
        string opMode = default("AlwaysON");
        int tNumber;
        int scNumber;
        @class(ControllerInterface);
}


network Sc1
{
    parameters:
        @display("bgb=3851.3052,2294.019;bgg=100,1,grey95");
        @figure[title](type=label; pos=0,-1; anchor=sw; color=darkblue);
        @signal[packetReceived](type=inet::Packet);
        @signal[packetSent](type=inet::Packet);
        @signal[handoverDelay](type=simtime_t);
        @statistic[handoverTime](title="handoverTime"; source=handoverDelay; unit=s; record=mean; interpolationmode=none);
        @statistic[packetReceived](title="packets received"; source=(packetReceived); record=count,"sum(packetBytes)","vector(packetBytes)"; interpolationmode=none);
        @statistic[packetSent](title="packets sent"; source=(packetSent); record=count,"sum(packetBytes)","vector(packetBytes)"; interpolationmode=none);
        @statistic[packetLoss](title="packet loss"; source=(count(packetSent)-count(packetReceived)); record=last; interpolationmode=none);
        //@statistic[throughput](title="throughput"; unit=bps; source="throughput(packetReceived)"; record=vector);

    submodules:
        controller: ControllerInterface {
            @display("p=3093.6375,159.01874");
        }
        visualizer: IntegratedCanvasVisualizer {
            @display("p=3575.5125,997.4812");
        }
        configurator: Ipv4NetworkConfigurator {
            @display("p=3575.5125,159.01874");
        }
        radioMedium: Ieee80211ScalarRadioMedium {
            @display("p=3339.3936,544.51874");
        }
        ss1: Sensor {
            @display("p=1045.6687,636.07495;is=s");
        }
        ss2: Sensor {
            @display("p=1455.2625,915.56244;is=s");
        }
        ap1: myAccesspoint {
            @display("p=1493.8125,1161.3187;r=791,#CC0000,,1;i=,#CC0000");
        }
        sink1: WirelessHost {
            @display("p=1638.375,1185.4125;is=s");
        }
        ss3: Sensor {
            @display("p=1821.4874,1648.0125;is=s");
        }
        ss4: Sensor {
            @display("p=1961.2312,737.26874;is=s");
        }
        ss5: Sensor {
            @display("p=1098.6749,1435.9874;is=s");
        }
        ss6: Sensor {
            @display("p=1532.3624,1382.9812;is=s");
        }
        ss7: Sensor {
            @display("p=1344.4312,1778.1187;is=s");
        }
}

network Sc2
{
    parameters:
        @display("bgb=3851.3052,4000.00;bgg=100,1,grey95");
        @figure[title](type=label; pos=0,-1; anchor=sw; color=darkblue);
        @signal[packetReceived](type=inet::Packet);
        @signal[packetSent](type=inet::Packet);
        @signal[handoverDelay](type=simtime_t);
        @statistic[handoverTime](title="handoverTime"; source=handoverDelay; unit=s; record=mean; interpolationmode=none);
        @statistic[packetReceived](title="packets received"; source=(packetReceived); record=count,"sum(packetBytes)","vector(packetBytes)"; interpolationmode=none);
        @statistic[packetSent](title="packets sent"; source=(packetSent); record=count,"sum(packetBytes)","vector(packetBytes)"; interpolationmode=none);
        @statistic[packetLoss](title="packet loss"; source=(count(packetSent)-count(packetReceived)); record=last; interpolationmode=none);
        //@statistic[throughput](title="throughput"; unit=bps; source="throughput(packetReceived)"; record=vector);

    submodules:
        controller: ControllerInterface {
            @display("p=3093.6375,159.01874");
        }
        visualizer: IntegratedCanvasVisualizer {
            @display("p=3575.5125,997.4812");
        }
        configurator: Ipv4NetworkConfigurator {
            @display("p=3575.5125,159.01874");
        }
        radioMedium: Ieee80211ScalarRadioMedium {
            @display("p=3587.0156,559.1385");
        }
        ss1: Sensor {
            @display("p=1222.5232,1241.477;is=s");
        }
        ss2: Sensor {
            @display("p=2094.4001,639.6923;is=s");
        }
        ss3: Sensor {
            @display("p=2184.431,1246.2155;is=s");
        }
        ss4: Sensor {
            @display("p=2013.8463,2113.354;is=s");
        }
        ss5: Sensor {
            @display("p=1762.7078,1246.2155;is=s");
        }
        ss6: Sensor {
            @display("p=1094.5847,2388.1848;is=s");
        }
        ss7: Sensor {
            @display("p=390.01187,1242.935;is=s");
        }
        ap1: myAccesspoint {
            @display("p=985.60004,2179.6924;r=791,#CC0000,,1;i=,#CC0000");
        }
        ap2: myAccesspoint {
            @display("p=1620.554,2179.6924;r=791,#CC0000,,1;i=,#CC0000");
        }
        ap3: myAccesspoint {
            @display("p=2298.154,2184.431;r=791,#CC0000,,1;i=,#CC0000");
        }
        sink1: WirelessHost {
            @display("p=1141.9694,2189.1694;is=vs;i=block/sink");
        }
        sink2: WirelessHost {
            @display("p=1762.7078,2184.431;is=vs;i=block/sink");
        }
        sink3: WirelessHost {
            @display("p=2445.0464,2193.908;is=vs;i=block/sink");
        }
        ss8: Sensor {
            @display("p=1440.4924,739.2001;is=s");
        }
        ss9: Sensor {
            @display("p=2047.0155,1625.2924;is=s");
        }
        ss10: Sensor {
            @display("p=1322.0309,1625.2924;is=s");
        }
        ss11: Sensor {
            @display("p=1667.9386,2421.354;is=s");
        }
        ss12: Sensor {
            @display("p=2729.354,1705.8463;is=s");
        }
        ap4: myAccesspoint {
            @display("p=985.60004,1549.477;r=791,#E9B96E,,1;i=,#E9B96E");
        }
        sink4: WirelessHost {
            @display("p=1141.9694,1558.954;is=vs;i=block/sink");
        }
        ap5: myAccesspoint {
            @display("p=1672.677,1549.477;r=791,#E9B96E,,1;i=,#E9B96E");
        }
        sink5: WirelessHost {
            @display("p=1824.3079,1554.2155;is=vs;i=block/sink");
        }
        ap6: myAccesspoint {
            @display("p=2336.0618,1554.2155;r=791,#E9B96E,,1;i=,#E9B96E");
        }
        sink6: WirelessHost {
            @display("p=2478.2156,1525.7847;is=vs;i=block/sink");
        }
        ap7: myAccesspoint {
            @display("p=980.86163,928.7385;r=791,#AD7FA8,,1;i=,#5C3566");
        }
        sink7: WirelessHost {
            @display("p=1137.2308,933.477;is=vs;i=block/sink");
        }
        ap8: myAccesspoint {
            @display("p=1667.9386,928.7385;r=791,#AD7FA8,,1;i=,#5C3566");
        }
        sink8: WirelessHost {
            @display("p=1824.3079,928.7385;is=vs;i=block/sink");
        }
        ap9: myAccesspoint {
            @display("p=2355.0156,928.7385;r=791,#AD7FA8,,1;i=,#5C3566");
        }
        sink9: WirelessHost {
            @display("p=2511.3848,928.7385;is=vs;i=block/sink");
        }
}


network Sc3
{
    parameters:
        @display("bgb=5957.9517,3995.992;bgg=100,1,grey95");
        @figure[title](type=label; pos=0,-1; anchor=sw; color=darkblue);
        @signal[packetReceived](type=inet::Packet);
        @signal[packetSent](type=inet::Packet);
        @signal[handoverDelay](type=simtime_t);
        @statistic[handoverTime](title="handoverTime"; source=handoverDelay; unit=s; record=mean; interpolationmode=none);
        @statistic[packetReceived](title="packets received"; source=(packetReceived); record=count,"sum(packetBytes)","vector(packetBytes)"; interpolationmode=none);
        @statistic[packetSent](title="packets sent"; source=(packetSent); record=count,"sum(packetBytes)","vector(packetBytes)"; interpolationmode=none);
        @statistic[packetLoss](title="packet loss"; source=(count(packetSent)-count(packetReceived)); record=last; interpolationmode=none);
        @statistic[throughput](title="throughput"; unit=bps; source="throughput(packetReceived)"; record=vector);

    submodules:
        ap1: myAccesspoint {
            @display("p=1445.2308,2345.5386;r=791,#CC0000,,1;i=,#CC0000");
        }
        ap2: myAccesspoint {
            @display("p=2065.9695,2350.277;r=791,#CC0000,,1;i=,#CC0000");
        }
        ap3: myAccesspoint {
            @display("p=2690.688,2354.352;r=791,#CC0000,,1;i=,#CC0000");
        }
        ap4: myAccesspoint {
            @display("p=3269.5386,2345.5386;r=791,#CC0000,,1;i=,#CC0000");
        }
        ap5: myAccesspoint {
            @display("p=1483.1385,1952.2463;r=791,#E9B96E,,1;i=,#E9B96E");
        }
        ap6: myAccesspoint {
            @display("p=2065.9695,1952.2463;r=791,#E9B96E,,1;i=,#E9B96E");
        }
        ap7: myAccesspoint {
            @display("p=2686.7078,1952.2463;r=791,#E9B96E,,1;i=,#E9B96E");
        }
        ap8: myAccesspoint {
            @display("p=3269.5386,1952.2463;r=791,#E9B96E,,1;i=,#E9B96E");
        }
        ap9: myAccesspoint {
            @display("p=1521.0463,1402.5847;r=791,#AD7FA8,,1;i=,#AD7FA8");
        }
        ap10: myAccesspoint {
            @display("p=2065.9695,1402.5847;r=791,#AD7FA8,,1;i=,#AD7FA8");
        }
        ap11: myAccesspoint {
            @display("p=2648.8003,1402.5847;r=791,#AD7FA8,,1;i=,#5C3566");
        }
        ap12: myAccesspoint {
            @display("p=3245.8464,1402.5847;r=791,#AD7FA8,,1;i=,#5C3566");
        }
        configurator: Ipv4NetworkConfigurator {
            @display("p=5741.736,192.192");
        }
        controller: ControllerInterface {
            @display("p=5261.256,192.192");
        }
        radioMedium: Ieee80211ScalarRadioMedium {
            @display("p=5749.7437,592.592");
        }
        sink1: WirelessHost {
            @display("p=1596.8617,2350.277;is=vs;i=block/sink,#E9B96E");
        }
        sink2: WirelessHost {
            @display("p=2203.3848,2355.0156;is=vs;i=block/sink,#E9B96E");
        }
        sink3: WirelessHost {
            @display("p=2852.554,2312.3694;is=vs;i=block/sink,#E9B96E");
        }
        sink4: WirelessHost {
            @display("p=3421.1694,2364.4924;is=vs;i=block/sink,#E9B96E");
        }
        sink5: WirelessHost {
            @display("p=1634.7693,1961.7233;is=vs;i=block/sink,#E9B96E");
        }
        sink6: WirelessHost {
            @display("p=2198.6462,1919.077;is=vs;i=block/sink,#E9B96E");
        }
        sink7: WirelessHost {
            @display("p=2828.8618,1876.4309;is=vs;i=block/sink,#E9B96E");
        }
        sink8: WirelessHost {
            @display("p=3421.1694,1862.2155;is=vs;i=block/sink,#E9B96E");
        }
        sink9: WirelessHost {
            @display("p=1672.677,1364.677;is=vs;i=block/sink,#E9B96E");
        }
        sink10: WirelessHost {
            @display("p=2212.8616,1284.1232;is=vs;i=block/sink,#E9B96E");
        }
        sink11: WirelessHost {
            @display("p=2800.431,1326.7693;is=vs;i=block/sink");
        }
        sink12: WirelessHost {
            @display("p=3383.2617,1326.7693;is=vs;i=block/sink,#E9B96E");
        }
        ss1: Sensor {
            @display("p=1601.6001,1606.3386;is=s");
        }
        ss2: Sensor {
            @display("p=2373.9695,900.30774;is=s");
        }
        ss3: Sensor {
            @display("p=2440.3079,1563.6924;is=s");
        }
        ss4: Sensor {
            @display("p=2410.408,2274.272;is=s");
        }
        ss5: Sensor {
            @display("p=1852.7385,2478.2156;is=s");
        }
        ss6: Sensor {
            @display("p=1483.1385,2625.108;is=s");
        }
        ss7: Sensor {
            @display("p=1201.2,1409.408;is=s");
        }
        ss8: Sensor {
            @display("p=1810.0924,1032.9847;is=s");
        }
        ss9: Sensor {
            @display("p=2442.44,1793.792;is=s");
        }
        ss10: Sensor {
            @display("p=1743.7539,1933.2925;is=s");
        }
        ss11: Sensor {
            @display("p=2151.2617,2696.1848;is=s");
        }
        ss12: Sensor {
            @display("p=3070.5232,2084.923;is=s");
        }
        ss13: Sensor {
            @display("p=3615.4463,2141.7847;is=s");
        }
        ss14: Sensor {
            @display("p=3615.4463,1563.6924;is=s");
        }
        ss15: Sensor {
            @display("p=3658.0925,2696.1848;is=s");
        }
        ss16: Sensor {
            @display("p=3421.1694,961.9078;is=s");
        }
        ss17: Sensor {
            @display("p=3364.3079,1506.8308;is=s");
        }
        ss18: Sensor {
            @display("p=1089.8462,2137.0464;is=s");
        }
        ss19: Sensor {
            @display("p=2994.708,2596.677;is=s");
        }
        ss20: Sensor {
            @display("p=2577.7231,2795.6924;is=s");
        }
        ss21: Sensor {
            @display("p=1592.1232,805.5385;is=s");
        }
        ss22: Sensor {
            @display("p=2065.9695,1663.2001;is=s");
        }
        ss23: Sensor {
            @display("p=1198.8308,2525.6;is=s");
        }
        ss24: Sensor {
            @display("p=2482.954,1265.1693;is=s");
        }
        ss25: Sensor {
            @display("p=1919.077,2179.6924;is=s");
        }
        ss26: Sensor {
            @display("p=2871.5078,1606.3386;is=s");
        }
        ss27: Sensor {
            @display("p=3004.1848,900.30774;is=s");
        }
        ss28: Sensor {
            @display("p=3928.1848,2137.0464;is=s");
        }
        ss29: Sensor {
            @display("p=2264.9849,2080.1848;is=s");
        }
        ss30: Sensor {
            @display("p=2686.7078,2080.1848;is=s");
        }
        visualizer: IntegratedCanvasVisualizer {
            @display("p=5741.736,1033.032");
        }
}










