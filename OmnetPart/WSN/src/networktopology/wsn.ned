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
        wlan[*].mgmt.typename = default("MgmtApSF");
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
        wlan[*].mgmt.typename = default("MgmtSensorSH");
        wlan[*].agent.typename = default("AgentSensor");
}

module ControllerInterface
{
    parameters:
        bool fuzzyControlled = default(true);
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
        sensor1: Sensor {
            @display("p=1045.6687,636.07495;is=s");
        }
        sensor2: Sensor {
            @display("p=1455.2625,915.56244;is=s");
        }
        ap1: myAccesspoint {
            @display("p=954.1125,1161.3187;r=791,,,1");
        }
        sink1: WirelessHost {
            @display("p=1103.4937,1166.1375;is=s");
        }
        sink2: WirelessHost {
            @display("p=1643.1937,1166.1375;is=s");
        }
        ap2: myAccesspoint {
            @display("p=1493.8125,1132.4062;r=791,,,1");
        }
        ap3: myAccesspoint {
            @display("p=2052.7874,1161.3187;r=791,,,1");
        }
        sink3: WirelessHost {
            @display("p=2202.1687,1166.1375;is=s");
        }
        sensor3: Sensor {
            @display("p=1821.4874,1648.0125;is=s");
        }
        sensor4: Sensor {
            @display("p=1961.2312,737.26874;is=s");
        }
        sensor5: Sensor {
            @display("p=1098.6749,1435.9874;is=s");
        }
        sensor6: Sensor {
            @display("p=1532.3624,1382.9812;is=s");
        }
        sensor7: Sensor {
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
        sensor1: Sensor {
            @display("p=801.8936,1242.935;is=s");
        }
        sensor2: Sensor {
            @display("p=1676.6866,641.5149;is=s");
        }
        sensor3: Sensor {
            @display("p=1764.1659,1250.225;is=s");
        }
        sensor4: Sensor {
            @display("p=1596.4972,2114.083;is=s");
        }
        sensor5: Sensor {
            @display("p=1341.3492,1250.225;is=s");
        }
        sensor6: Sensor {
            @display("p=674.31964,2391.1008;is=s");
        }
        sensor7: Sensor {
            @display("p=390.01187,1242.935;is=s");
        }
        ap1: myAccesspoint {
            @display("p=568.07825,2183.2026;r=791,#AD7FA8,,1;i=,#5C3566");
        }
        ap2: myAccesspoint {
            @display("p=1202.9893,2183.2026;r=791,#AD7FA8,,1;i=,#5C3566");
        }
        ap3: myAccesspoint {
            @display("p=1878.7424,2186.9155;r=791,#AD7FA8,,1;i=,#5C3566");
        }
        sink1: WirelessHost {
            @display("p=720.30835,2190.6284;is=s");
        }
        sink2: WirelessHost {
            @display("p=1344.0806,2186.9155;is=s");
        }
        sink3: WirelessHost {
            @display("p=2030.9725,2198.0544;is=s");
        }
        sensor8: Sensor {
            @display("p=1020.59186,739.9291;is=s");
        }
        sensor9: Sensor {
            @display("p=1629.302,1629.302;is=s");
        }
        sensor10: Sensor {
            @display("p=907.5978,1629.302;is=s");
        }
        sensor11: Sensor {
            @display("p=1250.225,2423.9058;is=s");
        }
        sensor12: Sensor {
            @display("p=2310.9116,1705.8464;is=s");
        }
        ap4: myAccesspoint {
            @display("p=568.07825,1552.0046;r=791,#EF2929,,1;i=,#A40000");
        }
        sink4: WirelessHost {
            @display("p=720.30835,1563.1434;is=s");
        }
        ap5: myAccesspoint {
            @display("p=1251.2573,1552.0046;r=791,#EF2929,,1;i=,#A40000");
        }
        sink5: WirelessHost {
            @display("p=1403.4874,1555.7175;is=s");
        }
        ap6: myAccesspoint {
            @display("p=1915.8717,1555.7175;r=791,#EF2929,,1;i=,#A40000");
        }
        sink6: WirelessHost {
            @display("p=2056.9631,1526.014;is=s");
        }
        ap7: myAccesspoint {
            @display("p=561.3255,929.4676;r=791,#AD7FA8,,1;i=,#5C3566");
        }
        sink7: WirelessHost {
            @display("p=718.05927,936.7575;is=s");
        }
        ap8: myAccesspoint {
            @display("p=1250.225,929.4676;r=791,#AD7FA8,,1;i=,#5C3566");
        }
        sink8: WirelessHost {
            @display("p=1403.3138,933.11255;is=s");
        }
        ap9: myAccesspoint {
            @display("p=1939.1245,929.4676;r=791,#AD7FA8,,1;i=,#5C3566");
        }
        sink9: WirelessHost {
            @display("p=2092.2134,933.11255;is=s");
        }
}











