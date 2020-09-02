import pandas as pd
import json

from Filters import ao_simulation_filter as fao
from Filters import ro_simulation_filter as fro
from Filters import flc_simulation_filter as fflc

import Plot

def results_from_wsn(wsn_file, run_name, scenario):	
	fresult_fao = fao(wsn_file, run_name, scenario) 
	fresult_fro = fro(wsn_file, run_name, scenario)
	fresult_fflc = fflc(wsn_file, run_name, scenario) 

	results = pd.DataFrame(index=['T1', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'T8', 'T9', 'T10'])
	results.insert(0, "PacketsSent", fresult_fao[0])  
	results.insert(1, "PacketsSentFLC", fresult_fflc[0])  
	results.insert(2, "PacketsSentRO", fresult_fro[0])  
	results.insert(3, "PacketsReceived", fresult_fao[1])  
	results.insert(4, "PacketsReceivedFLC", fresult_fflc[1])  
	results.insert(5, "PacketsReceivedRO", fresult_fro[1])  
	results.insert(6, "PacketsLost(%)", fresult_fao[2])  
	results.insert(7, "PacketsLostFLC(%)", fresult_fflc[2])  
	results.insert(8, "PacketsLostRO(%)", fresult_fro[2])  
	results.insert(9, "Jitter", fresult_fao[3])  
	results.insert(10, "JitterFLC", fresult_fflc[3])  
	results.insert(11, "JitterRO", fresult_fro[3])  
	results.insert(12, "Latency", fresult_fao[4])  
	results.insert(13, "LatencyFLC", fresult_fflc[4])  
	results.insert(14, "LatencyRO", fresult_fro[4])  
	results.insert(15, "PowerConsumed(W)", fresult_fao[5])  
	results.insert(16, "PowerConsumedFLC(W)", fresult_fflc[5])  
	results.insert(17, "PowerConsumedRO(W)", fresult_fro[5])  

	results.to_csv('outSc'+scenario+'.csv')
	#dump_consumption_into_file(scenario)

def results_from_wsn_ao(wsn_file, run_name, scenario):	
	fresult_fao = fao(wsn_file, run_name, scenario) 

	results = pd.DataFrame(index=['T1', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'T8', 'T9', 'T10'])
	results.insert(0, "PacketsSent", fresult_fao[0])  
	results.insert(1, "PacketsReceived", fresult_fao[1])  
	#results.insert(2, "PacketsLost(%)", fresult_fao[2])  
	results.insert(2, "Jitter", fresult_fao[3])  
	results.insert(3, "Latency", fresult_fao[4])  
	results.insert(4, "PowerConsumed(W)", fresult_fao[5])  

	results.to_csv('outSc'+scenario+'.csv')
	#dump_consumption_into_file(scenario)
	
	
def dump_consumption_into_file(scenario):
	power_consumption_dict = {'T'+str(i+1) : (fresult_fflc[5])[i] for i in range(0, len(fresult_fflc[5])) }
	y = json.dumps(power_consumption_dict)
	with open('/home/jaevillen/IC/Buffer/power_consumption_sc'+scenario+'.txt', 'w') as outfile:
		json.dump(y, outfile)

def call_func_wsn1():
	wsn1 = pd.read_csv('wsn1.csv', usecols=['run', 'type', 'module', 'name', 'value', 'mean'])
	results_from_wsn(wsn1,'wsnSc1T', '1')

def call_func_wsn2():
	wsn2 = pd.read_csv('wsn2.csv', usecols=['run', 'type', 'module', 'name', 'value', 'mean'])
	results_from_wsn(wsn2,'wsnSc2T', '2')
	
def call_func_wsn3():
	wsn3 = pd.read_csv('wsn3.csv', usecols=['run', 'type', 'module', 'name', 'value', 'mean'])
	results_from_wsn_ao(wsn3,'wsnSc3T', '3')



#call_func_wsn1()
#call_func_wsn2()
call_func_wsn3()

