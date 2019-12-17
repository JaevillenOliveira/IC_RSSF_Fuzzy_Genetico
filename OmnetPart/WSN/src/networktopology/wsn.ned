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

package wsn.networktopology;

import inet.networklayer.configurator.ipv4.Ipv4NetworkConfigurator;
import inet.node.inet.StandardHost;
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
        @signal[handoverDelayTime](type=long);
        @statistic[handoverDelayTime](title="handover time"; source=handoverDelayTime; unit=s; record=histogram,vector; interpolationmode=none);

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
        wlan[*].mgmt.typename = default("MgmtSensor");
        wlan[*].agent.typename = default("AgentSensor");
}

simple MatlabInterface
{
    @class(ControllerInterface);
}

network wsn
{
    parameters:
        @display("bgb=3851.3052,2294.019;bgg=100,1,grey95");
        @figure[title](type=label; pos=0,-1; anchor=sw; color=darkblue);
        @signal[packetReceived](type=inet::Packet);
        @signal[packetSent](type=inet::Packet);
        @signal[handoverDelay](type=simtime_t);
        @statistic[packetReceived](title="packets received"; source=(packetReceived); record=count,"sum(packetBytes)","vector(packetBytes)"; interpolationmode=none);
        @statistic[packetSent](title="packets sent"; source=(packetSent); record=count,"sum(packetBytes)","vector(packetBytes)"; interpolationmode=none);
        @statistic[packetLoss](title="packet loss"; source=(count(packetSent)-count(packetReceived)); record=last; interpolationmode=none);
        @statistic[throughput](title="throughput"; unit=bps; source="throughput(packetReceived)"; record=vector);
//        @statistic[endToEndDelay](title="end-to-end delay"; source="dataAge(packetReceived)"; unit=s; record=histogram,vector; interpolationmode=none);
 //       @statistic[jitter](title="jitter"; source="jitter(packetReceived)"; unit=s; record=histogram,vector; interpolationmode=none);
        @statistic[handoverTime](title="handover time"; source=handoverDelay; unit=s; record=histogram,vector; interpolationmode=none);

    submodules:
        interface: MatlabInterface {
            @display("p=3093.6375,159.01874");
        }
        visualizer: IntegratedCanvasVisualizer {
            @display("p=3575.5125,997.4812");
        }
        configurator: Ipv4NetworkConfigurator {
            @display("p=3575.5125,159.01874");
        }
        radioMedium: Ieee80211ScalarRadioMedium {
            @display("p=3575.5125,703.5375");
        }
        sensor1: Sensor {
            @display("p=954.1125,732.44995;is=s");
        }
        sensor2: Sensor {
            @display("p=1416.7124,862.5562;is=s");
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
            @display("p=1917.8624,1599.825;is=s");
        }
        sensor4: Sensor {
            @display("p=1917.8624,708.3562;is=s");
        }
        sensor5: Sensor {
            @display("p=1098.6749,1498.6312;is=s");
        }
        sensor6: Sensor {
            @display("p=1527.5437,1440.8062;is=s");
        }
        sensor7: Sensor {
            @display("p=1344.4312,1797.3937;is=s");
        }
}
