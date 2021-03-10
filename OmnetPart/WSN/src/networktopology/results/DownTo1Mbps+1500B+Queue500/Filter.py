import pandas as pd
import json

from Filters import ao_simulation_filter as fao
from Filters import ro_simulation_filter as fro
from Filters import flc_simulation_filter as fflc
from Filters import flcag_simulation_filter as fflcag
from Filters import flc_power_comsump_filter as pf

import Plot as plt

def results_from_wsn_ao(wsn_file, wl, run_name, scenario):	
	fresult_fao = fao(wsn_file, wl, run_name, scenario) 

	results = pd.DataFrame(index=['T1', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'T8', 'T9', 'T10'])
	results["PacketsSent"] = fresult_fao[0]
	results["PacketsReceived"] = fresult_fao[1]
	results["PacketsLost(%)"] = fresult_fao[2]
	results["Jitter"] = fresult_fao[3]
	results["Latency"] = fresult_fao[4]
	results["PowerConsumed(W)"] = fresult_fao[5]
	results["Th/Wl(%)"] = fresult_fao[6]
 
	return results

def results_from_wsn_ro(wsn_file, wl, run_name, scenario):	
	fresult_fro = fro(wsn_file, wl, run_name, scenario)

	results = pd.DataFrame(index=['T1', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'T8', 'T9', 'T10'])
	results["PacketsSentRO"] = fresult_fro[0]
	results["PacketsReceivedRO"] = fresult_fro[1]
	results["PacketsLostRO(%)"] = fresult_fro[2]
	results["JitterRO"] = fresult_fro[3]
	results["LatencyRO"] = fresult_fro[4]
	results["PowerConsumedRO(W)"] = fresult_fro[5]
	results["Th/WlRO(%)"] = fresult_fro[6]
 
	return results

def results_from_wsn_flc(wsn_file, wl, run_name, scenario):	
	fresult_fflc = fflc(wsn_file, wl, run_name, scenario) 

	results = pd.DataFrame(index=['T1', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'T8', 'T9', 'T10'])
	results["PacketsSentFLC"] = fresult_fflc[0]
	results["PacketsReceivedFLC"] = fresult_fflc[1]
	results["PacketsLostFLC(%)"] = fresult_fflc[2]
	results["JitterFLC"] = fresult_fflc[3]
	results["LatencyFLC"] = fresult_fflc[4]
	results["PowerConsumedFLC(W)"] = fresult_fflc[5]
	results["Th/WlFLC(%)"] = fresult_fflc[6]
 
	return results

def results_from_wsn_flc_ag(wsn_file, wl, run_name, scenario):	
	fresult_fflcag = fflcag(wsn_file, wl, run_name, scenario) 

	results = pd.DataFrame(index=['T1', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'T8', 'T9', 'T10'])
	results["PacketsSentFLCAG"] = fresult_fflcag[0]
	results["PacketsReceivedFLCAG"] = fresult_fflcag[1]
	results["PacketsLostFLCAG(%)"] = fresult_fflcag[2]
	results["JitterFLCAG"] = fresult_fflcag[3]
	results["LatencyFLCAG"] = fresult_fflcag[4]
	results["PowerConsumedFLCAG(W)"] = fresult_fflcag[5]
	results["Th/WlFLCAG(%)"] = fresult_fflcag[6]
 
	return results

def plot_ao_graphics(results, scenario):
	plt.plot_packet_loss_ao(results, scenario)
	plt.plot_jitter_ao(results, scenario)
	plt.plot_latency_ao(results, scenario)
	plt.plot_consumed_power_ao(results, scenario)
	plt.plot_th_wl_ao(results, scenario)
 
def plot_all_graphics(results, scenario):
	plt.plot_packet_loss(results, scenario)
	plt.plot_jitter(results, scenario)
	plt.plot_latency(results, scenario)
	plt.plot_consumed_power(results, scenario)
	plt.plot_th_wl(results, scenario)
 
def getResults (scenario, wl):
    wsn = pd.read_csv('wsn'+scenario+'.csv', usecols=['run', 'type', 'module', 'name', 'value', 'mean'])
    
    ao = results_from_wsn_ao(wsn, wl, 'wsnSc'+scenario+'T', scenario)
    ro = results_from_wsn_ro(wsn, wl, 'wsnSc'+scenario+'T', scenario)
    flc = results_from_wsn_flc(wsn, wl, 'wsnSc'+scenario+'T', scenario)
    # flcag = results_from_wsn_ao(wsn, wl, ''wsnSc'+scenario+'T', scenario)
    
    results = result = pd.concat([ao, ro, flc], axis=1)
    results.to_csv('outSc'+scenario+'.csv')
    plot_all_graphics(results, scenario)

def dump_consumption_into_file(scenario):
	wsn = pd.read_csv('wsn'+scenario+'.csv', usecols=['run', 'type', 'module', 'name', 'value', 'mean'])
	fresult_pf = pf(wsn, 'wsnSc'+scenario+'T', scenario) 
	power_consumption_dict = {'T'+str(i+1) : fresult_pf[i] for i in range(0, len(fresult_pf)) }
	y = json.dumps(power_consumption_dict)
	with open('/home/jaevillen/IC/Buffer/power_consumption_sc'+scenario+'.txt', 'w') as outfile:
		json.dump(y, outfile)

#dump_consumption_into_file('1')
#dump_consumption_into_file('2')
#dump_consumption_into_file('3')


wl1 = [8084,8332,18726,16976,20520,37304,44414,33888,20272,23900]
wl2 = [12246,16812,30510,25632,41960,38620,57684,37584,56326,45240]
wl3 = [14494,21464,37020,42616,43920,40600,87340,41328,94598,67900]

getResults ('1', wl1)
getResults ('2', wl2)
getResults ('3', wl3)


