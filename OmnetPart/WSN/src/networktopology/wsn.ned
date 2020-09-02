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
}

module ControllerInterface
{
    parameters:
        string opMode = default("AlwaysON");
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
        @statistic[throughput](title="throughput"; unit=bps; source="throughput(packetReceived)"; record=vector);

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
            @display("p=954.1125,1161.3187;r=791,#CC0000,,1;i=,#CC0000");
        }
        sink1: WirelessHost {
            @display("p=1098.6749,1185.4125;is=s");
        }
        sink2: WirelessHost {
            @display("p=1643.1937,1166.1375;is=s");
        }
        ap2: myAccesspoint {
            @display("p=1493.8125,1132.4062;r=791,#E9B96E,,1;i=,#E9B96E");
        }
        ap3: myAccesspoint {
            @display("p=2052.7874,1161.3187;r=791,#CC0000,,1;i=,#CC0000");
        }
        sink3: WirelessHost {
            @display("p=2202.1687,1166.1375;is=s");
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
        @statistic[throughput](title="throughput"; unit=bps; source="throughput(packetReceived)"; record=vector);

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
        controller: ControllerInterface {
            @display("p=5261.256,192.192");
        }
        visualizer: IntegratedCanvasVisualizer {
            @display("p=5741.736,1033.032");
        }
        configurator: Ipv4NetworkConfigurator {
            @display("p=5741.736,192.192");
        }
        radioMedium: Ieee80211ScalarRadioMedium {
            @display("p=5749.7437,592.592");
        }
        ss1: Sensor {
            @display("p=1634.7693,1464.1847;is=s");
        }
        ss2: Sensor {
            @display("p=2490.488,808.808;is=s");
        }
        ss3: Sensor {
            @display("p=2578.576,1417.416;is=s");
        }
        ss4: Sensor {
            @display("p=2410.408,2274.272;is=s");
        }
        ss5: Sensor {
            @display("p=2154.1519,1417.416;is=s");
        }
        ss6: Sensor {
            @display("p=1483.1385,2625.108;is=s");
        }
        ss7: Sensor {
            @display("p=1201.2,1409.408;is=s");
        }
        ap1: myAccesspoint {
            @display("p=1377.376,2346.344;r=791,#CC0000,,1;i=,#CC0000");
        }
        ap2: myAccesspoint {
            @display("p=2018.016,2346.344;r=791,#CC0000,,1;i=,#CC0000");
        }
        ap3: myAccesspoint {
            @display("p=2690.688,2354.352;r=791,#CC0000,,1;i=,#CC0000");
        }
        sink1: WirelessHost {
            @display("p=1529.528,2354.352;is=vs;i=block/sink,#E9B96E");
        }
        sink2: WirelessHost {
            @display("p=2154.1519,2354.352;is=vs;i=block/sink,#E9B96E");
        }
        sink3: WirelessHost {
            @display("p=2852.554,2312.3694;is=vs;i=block/sink,#E9B96E");
        }
        ss8: Sensor {
            @display("p=1833.8319,904.904;is=s");
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
        ap4: myAccesspoint {
            @display("p=3269.5386,2345.5386;r=791,#CC0000,,1;i=,#CC0000");
        }
        sink4: WirelessHost {
            @display("p=3421.1694,2364.4924;is=vs;i=block/sink,#E9B96E");
        }
        ap5: myAccesspoint {
            @display("p=3890.277,2345.5386;r=791,#CC0000,,1;i=,#CC0000");
        }
        sink5: WirelessHost {
            @display("p=4041.908,2350.277;is=vs;i=block/sink,#E9B96E");
        }
        ap6: myAccesspoint {
            @display("p=1345.7231,1810.0924;r=791,#E9B96E,,1;i=,#E9B96E");
        }
        sink6: WirelessHost {
            @display("p=1483.1385,1776.9232;is=vs;i=block/sink,#E9B96E");
        }
        ap7: myAccesspoint {
            @display("p=2065.9695,1786.4001;r=791,#E9B96E,,1;i=,#E9B96E");
        }
        sink7: WirelessHost {
            @display("p=2212.8616,1710.5847;is=vs;i=block/sink,#E9B96E");
        }
        ap8: myAccesspoint {
            @display("p=2776.7385,1786.4001;r=791,#E9B96E,,1;i=,#E9B96E");
        }
        sink8: WirelessHost {
            @display("p=2928.3694,1696.3694;is=vs;i=block/sink,#E9B96E");
        }
        ap9: myAccesspoint {
            @display("p=3302.708,1810.0924;r=791,#E9B96E,,1;i=,#E9B96E");
        }
        sink9: WirelessHost {
            @display("p=3454.3386,1776.9232;is=vs;i=block/sink,#E9B96E");
        }
        ap10: myAccesspoint {
            @display("p=3890.277,1852.7385;r=791,#E9B96E,,1;i=,#E9B96E");
        }
        sink10: WirelessHost {
            @display("p=4041.908,1734.277;is=vs;i=block/sink,#E9B96E");
        }
        ap11: myAccesspoint {
            @display("p=1374.1539,1250.954;r=791,#AD7FA8,,1;i=,#5C3566");
        }
        sink11: WirelessHost {
            @display("p=1525.7847,1175.1385;is=vs;i=block/sink");
        }
        ap12: myAccesspoint {
            @display("p=2013.8463,1250.954;r=791,#AD7FA8,,1;i=,#5C3566");
        }
        sink12: WirelessHost {
            @display("p=2151.2617,1175.1385;is=vs;i=block/sink,#E9B96E");
        }
        ap13: myAccesspoint {
            @display("p=2724.6155,1269.9078;r=791,#AD7FA8,,1;i=,#5C3566");
        }
        sink13: WirelessHost {
            @display("p=2904.677,1232.0001;is=vs;i=block/sink,#E9B96E");
        }
        ap14: myAccesspoint {
            @display("p=3302.708,1288.8616;r=791,#AD7FA8,,1;i=,#5C3566");
        }
        sink14: WirelessHost {
            @display("p=3454.3386,1232.0001;is=vs;i=block/sink,#E9B96E");
        }
        ap15: myAccesspoint {
            @display("p=3833.4155,1307.8154;r=791,#AD7FA8,,1;i=,#5C3566");
        }
        sink15: WirelessHost {
            @display("p=3980.3079,1232.0001;is=vs;i=block/sink,#E9B96E");
        }
        ss13: Sensor {
            @display("p=3615.4463,2141.7847;is=s");
        }
        ss14: Sensor {
            @display("p=3615.4463,1563.6924;is=s");
        }
        ss15: Sensor {
            @display("p=3743.3848,2753.0464;is=s");
        }
        ss16: Sensor {
            @display("p=3421.1694,961.9078;is=s");
        }
        ss17: Sensor {
            @display("p=4198.2773,961.9078;is=s");
        }
        ss18: Sensor {
            @display("p=1089.8462,2137.0464;is=s");
        }
        ss19: Sensor {
            @display("p=2994.708,2596.677;is=s");
        }
        ss20: Sensor {
            @display("p=4354.6465,2179.6924;is=s");
        }
}










