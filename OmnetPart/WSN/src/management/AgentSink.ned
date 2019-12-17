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

package wsn.management;

import inet.linklayer.ieee80211.mgmt.IIeee80211Agent;

simple AgentSink like IIeee80211Agent
{
    parameters:
        string assocApAddress;
        string interfaceTableModule;   // The path to the InterfaceTable module
        string mibModule;
        bool activeScan = default(true);  // selects between active and passive scanning
        string channelsToScan = default("");  // list of channel numbers to scan (space delimited); empty means all channels
        double probeDelay @unit(s) = default(100ms); // delay before sending a probe request during active scanning
        double minChannelTime @unit(s) = default(150ms); // min interval to spend on a channel during active scanning
        double maxChannelTime @unit(s) = default(300ms); // channel time for passive scanning, and max channel time for active scanning
        double authenticationTimeout @unit(s) = default(5s); // timeout for the authentication procedure
        double associationTimeout @unit(s) = default(5s); // timeout for the association procedure
        double startingTime @unit(s) = default(-1s); // agent starting time.
        string defaultSsid = default("SSID"); // default ssid
        @display("i=block/control");
        @signal[sentRequest](type=long); // ENUM! ~Ieee80211PrimRequestCode
        @signal[acceptConfirm](type=long); // ENUM! ~Ieee80211PrimConfirmCode
        @signal[dropConfirm](type=long); // ENUM! ~Ieee80211PrimConfirmCode
        @signal[l2AssociatedNewAp](type=inet::InterfaceEntry);
        @signal[l2AssociatedOldAp](type=inet::InterfaceEntry);
        @signal[l2Disassociated](type=inet::InterfaceEntry);
        @statistic[sentRequest](title="sent requests"; record=count,vector); // ENUM! ~Ieee80211PrimRequestCode
        @statistic[acceptConfirm](title="accepted confirms"; record=count,vector); // ENUM! ~Ieee80211PrimConfirmCode
        @statistic[dropConfirm](title="dropped confirms"; record=count,vector); // ENUM! ~Ieee80211PrimConfirmCode
    gates:
        input mgmtIn;
        output mgmtOut;
}

