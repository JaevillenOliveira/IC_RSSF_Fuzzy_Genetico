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

package wsn.throughput_test;

import inet.networklayer.configurator.ipv4.Ipv4NetworkConfigurator;
import inet.node.inet.WirelessHost;
import inet.node.wireless.AccessPoint;
import inet.physicallayer.ieee80211.packetlevel.Ieee80211ScalarRadioMedium;
import inet.power.contract.IEnergyGenerator;
import inet.power.contract.IEnergyManagement;
import inet.power.contract.IEnergyStorage;
import inet.visualizer.integrated.IntegratedCanvasVisualizer;
import inet.linklayer.ieee80211.mgmt.IIeee80211Agent;



network test
{
    parameters:
        @display("bgb=3851.3052,2294.019;bgg=100,1,grey95");
        @figure[title](type=label; pos=0,-1; anchor=sw; color=darkblue);
        @signal[packetReceived](type=inet::Packet);
        @signal[packetSent](type=inet::Packet);
        @statistic[packetReceived](title="packets received"; source=(packetReceived); record=count,"sum(packetBytes)","vector(packetBytes)"; interpolationmode=none);
        @statistic[packetSent](title="packets sent"; source=(packetSent); record=count,"sum(packetBytes)","vector(packetBytes)"; interpolationmode=none);
        @statistic[packetLoss](title="packet loss"; source=(count(packetSent)-count(packetReceived)); record=last; interpolationmode=none);

    submodules:
        visualizer: IntegratedCanvasVisualizer {
            @display("p=3594.7874,824.0062");
        }
        configurator: Ipv4NetworkConfigurator {
            @display("p=3594.7874,202.3875");
        }
        radioMedium: Ieee80211ScalarRadioMedium {
            @display("p=3594.7874,544.51874");
        }
        ss1: WirelessHost {
            @display("p=1031.2124,896.2875;is=s");
        }
        ap1: AccessPoint {
            @display("p=1701.0187,1161.3187;r=791,#CC0000,,1;i=,#CC0000");
        }
        sink1: WirelessHost {
            @display("p=1850.3999,1142.0437;is=s");
        }
}







