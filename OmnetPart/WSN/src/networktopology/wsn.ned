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

network OneToOne
{
    parameters:
        @display("bgb=3851.3052,2294.019;bgg=100,1,grey95");
        @figure[title](type=label; pos=0,-1; anchor=sw; color=darkblue);
        @signal[packetReceived](type=inet::Packet);
        @signal[packetSent](type=inet::Packet);
        //@signal[handoverDelay](type=simtime_t);
        //@statistic[handoverTime](title="handoverTime"; source=handoverDelay; unit=s; record=mean; interpolationmode=none);
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
            @display("p=563.7937,1233.6;is=s");
        }
        ap1: myAccesspoint {
            @display("p=949.2937,1161.3187;r=791,#CC0000,,1;i=,#CC0000");
        }
        sink1: WirelessHost {
            @display("p=1156.5,1166.1375;is=s");
        }
}

network Sc1
{
    parameters:
        @display("bgb=4413.9746,2293.7249,#F3F3F3;bgg=100,1,grey95");
        @figure[title](type=label; pos=0,-1; anchor=sw; color=darkblue);
        @signal[packetReceived](type=inet::Packet);
        @signal[packetSent](type=inet::Packet);
        @statistic[packetReceived](title="packets received"; source=(packetReceived); record=count,"sum(packetBytes)","vector(packetBytes)"; interpolationmode=none);
        @statistic[packetSent](title="packets sent"; source=(packetSent); record=count,"sum(packetBytes)","vector(packetBytes)"; interpolationmode=none);
        @statistic[packetLoss](title="packet loss"; source=(count(packetSent)-count(packetReceived)); record=last; interpolationmode=none);

    submodules:
        controller: ControllerInterface {
            @display("p=4144.125,207.20624;i=block/telnet");
        }
        visualizer: IntegratedCanvasVisualizer {
            @display("p=3787.5374,395.13748");
        }
        configurator: Ipv4NetworkConfigurator {
            @display("p=3363.4873,207.20624");
        }
        radioMedium: Ieee80211ScalarRadioMedium {
            @display("p=2973.1687,375.8625");
        }
        ss1: Sensor {
            @display("p=1045.6687,636.07495;is=s");
        }
        ss2: Sensor {
            @display("p=1455.2625,915.56244;is=s");
        }
        ap1: myAccesspoint {
            @display("p=949.2937,1161.3187;r=791,#A40000,,1;i=,#A40000");
        }
        sink1: WirelessHost {
            @display("p=1137.225,1117.95;is=vs;i=block/sink");
        }
        ss3: Sensor {
            @display("p=1787.7562,1628.7374;is=s");
        }
        ss4: Sensor {
            @display("p=1961.2312,737.26874;is=s");
        }
        ss5: Sensor {
            @display("p=910.7437,1435.9874;is=s");
        }
        ss6: Sensor {
            @display("p=1493.8125,1435.9874;is=s");
        }
        ss7: Sensor {
            @display("p=1286.6062,1729.9312;is=s");
        }
        ap2: myAccesspoint {
            @display("p=1493.8125,1161.3187;r=791,#A40000,,1;i=,#A40000");
        }
        sink2: WirelessHost {
            @display("p=1696.2,1084.2188;is=vs;i=block/sink");
        }
        ap3: myAccesspoint {
            @display("p=2052.7874,1161.3187;r=791,#A40000,,1;i=,#A40000");
        }
        sink3: WirelessHost {
            @display("p=2240.7188,1117.95;is=vs;i=block/sink");
        }
}

network Sc2
{
    parameters:
        @display("bgb=5001.92,3098.48,#F3F3F3;bgg=100,1,grey95");
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
            @display("p=4730.88,184.8;i=device/terminal");
        }
        visualizer: IntegratedCanvasVisualizer {
            @display("p=4318.16,554.4");
        }
        configurator: Ipv4NetworkConfigurator {
            @display("p=3917.76,184.8");
        }
        radioMedium: Ieee80211ScalarRadioMedium {
            @display("p=3431.12,468.16");
        }
        ss1: Sensor {
            @display("p=1322.0309,1288.8616;is=s");
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
            @display("p=905.0462,1288.8616;is=s");
        }
        ap1: myAccesspoint {
            @display("p=985.60004,2179.6924;r=791,#B94343,,1;i=,#B94343");
        }
        ap2: myAccesspoint {
            @display("p=1620.554,2179.6924;r=791,#B94343,,1;i=,#B94343");
        }
        ap3: myAccesspoint {
            @display("p=2298.154,2184.431;r=791,#B94343,,1;i=,#B94343");
        }
        sink1: WirelessHost {
            @display("p=1184.6155,2113.354;is=vs;i=block/sink");
        }
        sink2: WirelessHost {
            @display("p=1852.7385,2184.431;is=vs;i=block/sink");
        }
        sink3: WirelessHost {
            @display("p=2482.954,2151.2617;is=vs;i=block/sink");
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
            @display("p=985.60004,1549.477;r=791,#B94343,,1;i=,#B94343");
        }
        sink4: WirelessHost {
            @display("p=1184.6155,1525.7847;is=vs;i=block/sink");
        }
        ap5: myAccesspoint {
            @display("p=1672.677,1549.477;r=791,#B94343,,1;i=,#B94343");
        }
        sink5: WirelessHost {
            @display("p=1895.3848,1525.7847;is=vs;i=block/sink");
        }
        ap6: myAccesspoint {
            @display("p=2336.0618,1554.2155;r=791,#B94343,,1;i=,#B94343");
        }
        sink6: WirelessHost {
            @display("p=2563.5078,1483.1385;is=vs;i=block/sink");
        }
        ap7: myAccesspoint {
            @display("p=980.86163,928.7385;r=791,#B94343,,1;i=,#B94343");
        }
        sink7: WirelessHost {
            @display("p=1184.6155,895.5693;is=vs;i=block/sink");
        }
        ap8: myAccesspoint {
            @display("p=1667.9386,928.7385;r=791,#B94343,,1;i=,#B94343");
        }
        sink8: WirelessHost {
            @display("p=1852.7385,895.5693;is=vs;i=block/sink");
        }
        ap9: myAccesspoint {
            @display("p=2355.0156,928.7385;r=791,#B94343,,1;i=,#B94343");
        }
        sink9: WirelessHost {
            @display("p=2591.9387,895.5693;is=vs;i=block/sink");
        }
}


network Sc3
{
    parameters:
        @display("bgb=6603.52,3985.52,#F3F3F3;bgg=100,1,grey95");
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
            @display("p=973.28,2519.44;r=791,#888A85,,1;i=,#2E3436");
        }
        ap2: myAccesspoint {
            @display("p=1657.04,2519.44;r=791,#888A85,,1;i=,#2E3436");
        }
        ap3: myAccesspoint {
            @display("p=2346.96,2544.08;r=791,#888A85,,1;i=,#2E3436");
        }
        ap4: myAccesspoint {
            @display("p=3049.2,2556.4;r=791,#888A85,,1;i=,#2E3436");
        }
        ap5: myAccesspoint {
            @display("p=3751.44,2544.08;r=791,#888A85,,1;i=,#2E3436");
        }
        ap6: myAccesspoint {
            @display("p=973.28,1823.36;r=791,#888A85,,1;i=,#2E3436");
        }
        ap7: myAccesspoint {
            @display("p=1657.04,1878.8;r=791,#888A85,,1;i=,#2E3436");
        }
        ap8: myAccesspoint {
            @display("p=2346.96,1897.28;r=791,#888A85,,1;i=,#2E3436");
        }
        ap9: myAccesspoint {
            @display("p=3049.2,1878.8;r=791,#888A85,,1;i=,#2E3436");
        }
        ap10: myAccesspoint {
            @display("p=3751.44,1878.8;r=791,#888A85,,1;i=,#2E3436");
        }
        ap11: myAccesspoint {
            @display("p=973.28,1250.48;r=791,#888A85,,1;i=,#2E3436");
        }
        ap12: myAccesspoint {
            @display("p=1657.04,1250.48;r=791,#888A85,,1;i=,#2E3436");
        }
        configurator: Ipv4NetworkConfigurator {
            @display("p=5784.24,394.24");
        }
        controller: ControllerInterface {
            @display("p=6184.64,154;i=device/terminal");
        }
        radioMedium: Ieee80211ScalarRadioMedium {
            @display("p=4851.246,291.49118");
        }
        sink1: WirelessHost {
            @display("p=1213.52,2439.36;is=vs;i=block/sink,#E9B96E");
        }
        sink2: WirelessHost {
            @display("p=1915.76,2457.84;is=vs;i=block/sink,#E9B96E");
        }
        sink3: WirelessHost {
            @display("p=2599.52,2457.84;is=vs;i=block/sink,#E9B96E");
        }
        sink4: WirelessHost {
            @display("p=3295.6,2464;is=vs;i=block/sink,#E9B96E");
        }
        sink5: WirelessHost {
            @display("p=4022.48,2439.36;is=vs;i=block/sink,#E9B96E");
        }
        sink6: WirelessHost {
            @display("p=1213.52,1829.52;is=vs;i=block/sink,#E9B96E");
        }
        sink7: WirelessHost {
            @display("p=1915.76,1804.88;is=vs;i=block/sink,#E9B96E");
        }
        sink8: WirelessHost {
            @display("p=2599.52,1829.52;is=vs;i=block/sink,#E9B96E");
        }
        sink9: WirelessHost {
            @display("p=3295.6,1829.52;is=vs;i=block/sink,#E9B96E");
        }
        sink10: WirelessHost {
            @display("p=4022.48,1804.88;is=vs;i=block/sink,#E9B96E");
        }
        sink11: WirelessHost {
            @display("p=1305.92,1207.36;is=vs;i=block/sink");
        }
        sink12: WirelessHost {
            @display("p=1928.0801,1195.04;is=vs;i=block/sink,#E9B96E");
        }
        ss1: Sensor {
            @display("p=1305.92,1601.6;is=s");
        }
        ss2: Sensor {
            @display("p=2679.6,948.64;is=s");
        }
        ss3: Sensor {
            @display("p=3449.6,1533.84;is=s");
        }
        ss4: Sensor {
            @display("p=2186.8,2223.76;is=s");
        }
        ss5: Sensor {
            @display("p=2679.6,1601.6;is=s");
        }
        ss6: Sensor {
            @display("p=2679.6,2926;is=s");
        }
        ss7: Sensor {
            @display("p=1065.68,948.64;is=s");
        }
        ss8: Sensor {
            @display("p=1213.52,2852.08;is=s");
        }
        ss9: Sensor {
            @display("p=1607.76,2205.28;is=s");
        }
        ss10: Sensor {
            @display("p=3400.32,2205.28;is=s");
        }
        ss11: Sensor {
            @display("p=1706.3201,893.2;is=s");
        }
        ss12: Sensor {
            @display("p=3400.32,2852.08;is=s");
        }
        ss13: Sensor {
            @display("p=4139.52,2168.32;is=s");
        }
        ss14: Sensor {
            @display("p=4244.24,1601.6;is=s");
        }
        ss15: Sensor {
            @display("p=708.4,1527.68;is=s");
        }
        ss16: Sensor {
            @display("p=3498.8801,893.2;is=s");
        }
        ss17: Sensor {
            @display("p=1915.76,1527.68;is=s");
        }
        ss18: Sensor {
            @display("p=924,2205.28;is=s");
        }
        ss19: Sensor {
            @display("p=2753.52,2223.76;is=s");
        }
        ss20: Sensor {
            @display("p=1995.84,2852.08;is=s");
        }
        visualizer: IntegratedCanvasVisualizer {
            @display("p=5359.2,154");
        }
        ap13: myAccesspoint {
            @display("p=2365.44,1287.4401;r=791,#888A85,,1;i=,#2E3436");
        }
        sink13: WirelessHost {
            @display("p=2636.48,1207.36;is=vs;i=block/sink,#E9B96E");
        }
        ap14: myAccesspoint {
            @display("p=3049.2,1330.56;r=791,#888A85,,1;i=,#2E3436");
        }
        sink14: WirelessHost {
            @display("p=3332.56,1207.36;is=vs;i=block/sink,#E9B96E");
        }
        ap15: myAccesspoint {
            @display("p=3751.44,1330.56;r=791,#888A85,,1;i=,#2E3436");
        }
        sink15: WirelessHost {
            @display("p=4022.48,1256.64;is=vs;i=block/sink,#E9B96E");
        }
}










