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

package wsn.ToPrint;

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
        @display("bgb=4732.012,2293.7249,white,,0;bgg=100,1,white");
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
            @display("p=4510.3496,1238.4187");
        }
        visualizer: IntegratedCanvasVisualizer {
            @display("p=4447.706,872.1937");
        }
        configurator: Ipv4NetworkConfigurator {
            @display("p=4510.3496,159.01874");
        }
        radioMedium: Ieee80211ScalarRadioMedium {
            @display("p=4447.706,505.96872");
        }
        s1: Sensor {
            @display("p=1407.075,636.07495;is=s");
        }
        s2: Sensor {
            @display("p=1816.6687,915.56244;is=s");
        }
        AP1: myAccesspoint {
            @display("p=1310.7,1161.3187;r=791,#888A85,,1;i=,#22113F");
        }

        s3: Sensor {
            @display("p=2182.8936,1648.0125;is=s");
        }
        s4: Sensor {
            @display("p=2322.6375,737.26874;is=s");
        }
        s5: Sensor {
            @display("p=1460.0812,1435.9874;is=s");
        }
        s6: Sensor {
            @display("p=1927.4999,1435.9874;is=s");
        }
        s7: Sensor {
            @display("p=1705.8374,1749.2062;is=s");
        }
        AP2: myAccesspoint {
            @display("p=1855.2186,1161.3187;r=791,#888A85,,1;i=,#22113F");
        }

        AP3: myAccesspoint {
            @display("p=2414.1936,1161.3187;r=791,#888A85,,1;i=,#22113F");
        }

}

network Sc2
{
    parameters:
        @display("bgb=7431.424,3995.992,white,,0;bgg=100,1,white");
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
            @display("p=5813.8076,288.288");
        }
        visualizer: IntegratedCanvasVisualizer {
            @display("p=6438.4316,1169.168");
        }
        configurator: Ipv4NetworkConfigurator {
            @display("p=6294.2876,288.288");
        }
        radioMedium: Ieee80211ScalarRadioMedium {
            @display("p=6446.44,728.72797");
        }
        s1: Sensor {
            @display("p=1881.88,1297.296;is=s");
        }
        s2: Sensor {
            @display("p=2754.752,688.688;is=s");
        }
        s3: Sensor {
            @display("p=2842.8398,1297.296;is=s");
        }
        s4: Sensor {
            @display("p=2674.6719,2162.16;is=s");
        }
        s5: Sensor {
            @display("p=2426.4238,1297.296;is=s");
        }
        s6: Sensor {
            @display("p=1829.52,2470.16;is=s");
        }
        s7: Sensor {
            @display("p=1049.048,1297.296;is=s");
        }
        AP1: myAccesspoint {
            @display("p=1649.648,2234.232;r=791,#888A85,,1;i=,#22113F");
        }
        AP2: myAccesspoint {
            @display("p=2282.28,2234.232;r=791,#888A85,,1;i=,#22113F");
        }
        AP3: myAccesspoint {
            @display("p=2954.952,2234.232;r=791,#888A85,,1;i=,#22113F");
        }
        s8: Sensor {
            @display("p=2098.096,792.792;is=s");
        }
        s9: Sensor {
            @display("p=2706.7039,1673.672;is=s");
        }
        s10: Sensor {
            @display("p=1985.9839,1673.672;is=s");
        }
        s11: Sensor {
            @display("p=2328.48,2525.6;is=s");
        }
        s12: Sensor {
            @display("p=3387.3838,1761.76;is=s");
        }
        AP4: myAccesspoint {
            @display("p=1649.648,1601.6;r=791,#888A85,,1;i=,#22113F");
        }
        AP5: myAccesspoint {
            @display("p=2330.328,1601.6;r=791,#888A85,,1;i=,#22113F");
        }
        AP6: myAccesspoint {
            @display("p=2994.992,1609.6079;r=791,#888A85,,1;i=,#22113F");
        }
        AP7: myAccesspoint {
            @display("p=1641.64,976.97595;r=791,#888A85,,1;i=,#22113F");
        }
        AP8: myAccesspoint {
            @display("p=2330.328,976.97595;r=791,#888A85,,1;i=,#22113F");
        }
        AP9: myAccesspoint {
            @display("p=3019.0159,976.97595;r=791,#888A85,,1;i=,#22113F");
        }
}


network Sc3
{
    parameters:
        @display("bgb=5957.9517,3995.992,white,,0;bgg=100,1,white");
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
        AP1: myAccesspoint {
            @display("p=1544.2292,2337.877;r=791,#888A85,,1;i=,#22113F");
        }
        AP2: myAccesspoint {
            @display("p=2104.0894,2350.1816;r=791,#888A85,,1;i=,#22113F");
        }
        AP3: myAccesspoint {
            @display("p=2676.254,2337.877;r=791,#888A85,,1;i=,#22113F");
        }
        AP4: myAccesspoint {
            @display("p=3229.9617,2337.877;r=791,#888A85,,1;i=,#22113F");
        }
        AP5: myAccesspoint {
            @display("p=3752.9077,2337.877;r=791,#888A85,,1;i=,#22113F");
        }
        AP6: myAccesspoint {
            @display("p=1544.2292,1925.6724;r=791,#888A85,,1;i=,#22113F");
        }
        AP7: myAccesspoint {
            @display("p=2134.8508,1925.6724;r=791,#888A85,,1;i=,#22113F");
        }
        AP8: myAccesspoint {
            @display("p=2670.1016,1913.3678;r=791,#888A85,,1;i=,#22113F");
        }
        AP9: myAccesspoint {
            @display("p=3229.9617,1925.6724;r=791,#888A85,,1;i=,#22113F");
        }
        AP10: myAccesspoint {
            @display("p=3752.9077,1937.977;r=791,#888A85,,1;i=,#22113F");
        }
        AP11: myAccesspoint {
            @display("p=1544.2292,1427.3354;r=791,#888A85,,1;i=,#22113F");
        }
        AP12: myAccesspoint {
            @display("p=2134.8508,1427.3354;r=791,#888A85,,1;i=,#22113F");
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
        s1: Sensor {
            @display("p=1667.2754,1679.5801;is=s");
        }
        s2: Sensor {
            @display("p=2373.9695,900.30774;is=s");
        }
        s3: Sensor {
            @display("p=2994.708,1705.8463;is=s");
        }
        s4: Sensor {
            @display("p=2410.408,2274.272;is=s");
        }
        s5: Sensor {
            @display("p=1739.0155,1895.3848;is=s");
        }
        s6: Sensor {
            @display("p=1810.0924,2653.5386;is=s");
        }
        s7: Sensor {
            @display("p=3562.1863,1261.2231;is=s");
        }
        s8: Sensor {
            @display("p=1810.0924,1032.9847;is=s");
        }
        s9: Sensor {
            @display("p=2442.44,1793.792;is=s");
        }
        s10: Sensor {
            @display("p=2942.5847,2137.0464;is=s");
        }
        s11: Sensor {
            @display("p=2293.4155,2696.1848;is=s");
        }
        s12: Sensor {
            @display("p=3122.6462,2895.2002;is=s");
        }
        s13: Sensor {
            @display("p=3615.4463,2141.7847;is=s");
        }
        s14: Sensor {
            @display("p=3615.4463,1791.1385;is=s");
        }
        s15: Sensor {
            @display("p=3658.0925,2696.1848;is=s");
        }
        s16: Sensor {
            @display("p=3421.1694,961.9078;is=s");
        }
        s17: Sensor {
            @display("p=1151.4463,1554.2155;is=s");
        }
        s18: Sensor {
            @display("p=1089.8462,2137.0464;is=s");
        }
        s19: Sensor {
            @display("p=2994.708,2596.677;is=s");
        }
        s20: Sensor {
            @display("p=2577.7231,2795.6924;is=s");
        }
        s21: Sensor {
            @display("p=1599.6001,898.23694;is=s");
        }
        s22: Sensor {
            @display("p=2011.8047,1704.1893;is=s");
        }
        s23: Sensor {
            @display("p=1198.8308,2525.6;is=s");
        }
        s24: Sensor {
            @display("p=2482.954,1265.1693;is=s");
        }
        s25: Sensor {
            @display("p=1919.077,2179.6924;is=s");
        }
        visualizer: IntegratedCanvasVisualizer {
            @display("p=5741.736,1033.032");
        }
        AP13: myAccesspoint {
            @display("p=2670.1016,1408.8785;r=791,#888A85,,1;i=,#22113F");
        }
        AP14: myAccesspoint {
            @display("p=3229.9617,1427.3354;r=791,#888A85,,1;i=,#22113F");
        }
        AP15: myAccesspoint {
            @display("p=3752.9077,1415.0308;r=791,#888A85,,1;i=,#22113F");
        }
}




