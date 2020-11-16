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

package wsn.a;

import inet.applications.ethernet.EtherAppClient;
import inet.linklayer.ieee80211.Ieee80211Interface;
import inet.mobility.static.StationaryMobility;
import inet.networklayer.common.InterfaceTable;
import inet.physicallayer.ieee80211.packetlevel.Ieee80211ScalarRadioMedium;
import inet.visualizer.contract.IIntegratedVisualizer;
import inet.node.inet.WirelessHost;
import inet.node.wireless.AccessPoint;
import inet.networklayer.configurator.ipv4.Ipv4NetworkConfigurator;
import inet.visualizer.integrated.IntegratedCanvasVisualizer;
import inet.power.contract.IEnergyGenerator;
import inet.power.contract.IEnergyManagement;
import inet.power.contract.IEnergyStorage;

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

module ControllerInterface
{
    parameters:
        string opMode = default("AlwaysON");
        int tNumber;
        int scNumber;
        @class(ControllerInterface);
}

module Sensor extends WirelessHost
{
    parameters:
        string sinkAddress;
        wlan[*].mgmt.typename = default("MgmtSensorHH");
        wlan[*].agent.typename = default("AgentSensor");
    submodules:
}

network tst
{
    @display("bgb=4682.004,2254.1194");
    @signal[packetReceived](type=inet::Packet);
    @signal[packetSent](type=inet::Packet);
    @statistic[packetReceived](title="packets received"; source=(packetReceived); record=count,"sum(packetBytes)","vector(packetBytes)"; interpolationmode=none);
    @statistic[packetSent](title="packets sent"; source=(packetSent); record=count,"sum(packetBytes)","vector(packetBytes)"; interpolationmode=none);
    @statistic[packetLoss](title="packet loss"; source=(count(packetSent)-count(packetReceived)); record=last; interpolationmode=none);
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
        ap1: myAccesspoint {
            @display("p=1042.5906,1197.0485;r=791,#CC0000,,1;i=,#CC0000");
        }
        sink1: WirelessHost {
            @display("p=1153.6072,1177.7412;is=s");
        }
        ss7: Sensor {
            @display("p=1344.4312,1778.1187;is=s");
        }

        ss6: Sensor {
            @display("p=1532.3624,1382.9812;is=s");
        }
        ss5: Sensor {
            @display("p=1098.6749,1435.9874;is=s");
        }
        ss4: Sensor {
            @display("p=1961.2312,737.26874;is=s");
        }
        ss3: Sensor {
            @display("p=1821.4874,1648.0125;is=s");
        }
        ss2: Sensor {
            @display("p=1455.2625,915.56244;is=s");
        }
        ss1: Sensor {
            @display("p=1045.6687,636.07495;is=s");
        }
        ap2: myAccesspoint {
            @display("p=1602.5004,1197.0485;r=791,#CC0000,,1;i=,#CC0000");
        }
        sink2: WirelessHost {
            @display("p=1713.517,1177.7412;is=s");
        }
        ap3: myAccesspoint {
            @display("p=2143.103,1197.0485;r=791,#CC0000,,1;i=,#CC0000");
        }
        sink3: WirelessHost {
            @display("p=2254.1194,1177.7412;is=s");
        }
}
