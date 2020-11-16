import pandas as pd
import json

from Filters import ao_simulation_filter as fao
from Filters import ro_simulation_filter as fro
from Filters import flc_simulation_filter as fflc

import Plot as plt

def results_from_wsn(wsn_file, wl, run_name, scenario):	
	fresult_fao = fao(wsn_file, wl, run_name, scenario) 
	fresult_fro = fro(wsn_file, wl, run_name, scenario)
	fresult_fflc = fflc(wsn_file, wl, run_name, scenario) 

	results = pd.DataFrame(index=['T1', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'T8', 'T9', 'T10'])
	
	results["PacketsSent"] = fresult_fao[0]
	results["PacketsSentFLC"] = fresult_fflc[0]
	results["PacketsSentRO"] = fresult_fro[0]
	
	results["PacketsReceived"] = fresult_fao[1]
	results["PacketsReceivedFLC"] =fresult_fflc[1]
	results["PacketsReceivedRO"] = fresult_fro[1]
	
	results["PacketsLost(%)"] = fresult_fao[2]
	results["PacketsLostFLC(%)"] = fresult_fflc[2]
	results["PacketsLostRO(%)"] = fresult_fro[2]
	
	results["Jitter"] = fresult_fao[3]
	results["JitterFLC"] = fresult_fflc[3]
	results["JitterRO"] = fresult_fro[3]
	
	results["Latency"] = fresult_fao[4]
	results["LatencyFLC"] = fresult_fflc[4]
	results["LatencyRO"] = fresult_fro[4]
	
	results["PowerConsumed(W)"] = fresult_fao[5]
	results["PowerConsumedFLC(W)"] = fresult_fflc[5]
	results["PowerConsumedRO(W)"] = fresult_fro[5]
	
	results["Th/Wl(%)"] = fresult_fao[6]
	results["Th/WlFLC(%)"] = fresult_fflc[6]
	results["Th/WlRO(%)"] = fresult_fro[6]

	results.to_csv('outSc'+scenario+'.csv')
	plot_all_graphics(results, scenario)
		

def results_from_wsn_ao(wsn_file, run_name, scenario):	
	fresult_fao = fao(wsn_file, run_name, scenario) 

	results = pd.DataFrame(index=['T1', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'T8', 'T9', 'T10'])
	results.insert(0, "PacketsSent", fresult_fao[0])  
	results.insert(1, "PacketsReceived", fresult_fao[1])  
	results.insert(2, "PacketsLost(%)", fresult_fao[2])  
	results.insert(3, "Jitter", fresult_fao[3])  
	results.insert(4, "Latency", fresult_fao[4])  
	results.insert(5, "PowerConsumed(W)", fresult_fao[5])  

	results.to_csv('outSc'+scenario+'.csv')
	plot_all_graphics(results, scenario)
	

def call_func_wsn1():
	wsn1 = pd.read_csv('wsn1.csv', usecols=['run', 'type', 'module', 'name', 'value', 'mean'])
	wl1 = [10324, 10572, 31446, 40976,8520,241304,482414,481888,322272,403900]

	results_from_wsn(wsn1, wl1, 'wsnSc1T', '1')

def call_func_wsn2():
	wsn2 = pd.read_csv('wsn2.csv', usecols=['run', 'type', 'module', 'name', 'value', 'mean'])
	wl2 = [15606,21292,51710,61632,161960,242620,641684,485584,962326,805240]

	results_from_wsn(wsn2, wl2, 'wsnSc2T', '2')
	
def call_func_wsn3():
	wsn3 = pd.read_csv('wsn3.csv', usecols=['run', 'type', 'module', 'name', 'value', 'mean'])
	wl3 = [26176,37628,83192,143264,284410,405264,1284168,971184,2245734,1610500]

	results_from_wsn(wsn3, wl3, 'wsnSc3T', '3')

def plot_all_graphics(results, scenario):
	plt.plot_packet_loss(results, scenario)
	plt.plot_jitter(results, scenario)
	plt.plot_latency(results, scenario)
	plt.plot_consumed_power(results, scenario)
	plt.plot_th_wl(results, scenario)
	
def dump_consumption_into_file(scenario):
	wsn = pd.read_csv('wsn'+scenario+'.csv', usecols=['run', 'type', 'module', 'name', 'value', 'mean'])
	fresult_fflc = fflc(wsn, 'wsnSc'+scenario+'T', scenario) 
	power_consumption_dict = {'T'+str(i+1) : (fresult_fflc[5])[i] for i in range(0, len(fresult_fflc[5])) }
	y = json.dumps(power_consumption_dict)
	with open('/home/jaevillen/IC/Buffer/power_consumption_sc'+scenario+'.txt', 'w') as outfile:
		json.dump(y, outfile)

#dump_consumption_into_file('1')
#dump_consumption_into_file('2')
#dump_consumption_into_file('3')

call_func_wsn1()
call_func_wsn2()
call_func_wsn3()



