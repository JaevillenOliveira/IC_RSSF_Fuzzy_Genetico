import pandas as pd
import matplotlib.pyplot as plt
import json

from Filters import ao_simulation_filter as fao
from Filters import ro_simulation_filter as fro
from Filters import flc_simulation_filter as fflc

wsn1 = pd.read_csv('wsn1.csv', usecols=['run', 'type', 'module', 'name', 'value', 'mean'])
wsn2 = pd.read_csv('wsn2.csv', usecols=['run', 'type', 'module', 'name', 'value', 'mean'])

wsn = [wsn1, wsn2]
run = ['wsnSc1T', 'wsnSc2T']


for i in range(1, 3):
	fresult_fao = fao(wsn[i-1], run[i-1], str(i)) ##Only needed to be executed once,the GA doesn't interfere here
	fresult_fro = fro(wsn[i-1], run[i-1], str(i)) ##Only needed to be executed once,the GA doesn't interfere here
	fresult_fflc = fflc(wsn[i-1], run[i-1], str(i)) 

	#####
	# HERE JUST NEED TO EXECUTE AT THE END, WHEN HAVE THE IDEAL SOLUTION 
	####

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

	power_consumption_dict = {'T'+str(i+1) : (fresult_fflc[5])[i] for i in range(0, len(fresult_fflc[5])) }
	y = json.dumps(power_consumption_dict)
	with open('/home/jaevillen/IC/Buffer/power_consumption_sc'+str(i)+'.txt', 'w') as outfile:
		json.dump(y, outfile)

	results.to_csv('outSc'+str(i)+'.csv')

	ax = plt.cla() 
	ax = plt.gca()

	results.plot(kind='line',y='PacketsLost(%)',label='Always On', ax=ax)
	results.plot(kind='line',y='PacketsLostRO(%)', label='Random OFF', color='green', ax=ax)
	results.plot(kind='line',y='PacketsLostFLC(%)', label='Fuzzy Controlled', color='red', ax=ax)
	plt.ylabel('Perda de pacotes (%)')
	plt.title('Perda de pacotes Cenário'+str(i))
	plt.savefig('DataOut/Sc'+str(i)+'PacketsLossGraphic.png')
	
	ax = plt.cla() 
	ax = plt.gca()

	results.plot(kind='line',y='Jitter', label='Always On',ax=ax)
	results.plot(kind='line',y='JitterRO', label='Random OFF', color='green', ax=ax)
	results.plot(kind='line',y='JitterFLC', label='Fuzzy Controlled', color='red', ax=ax)
	plt.ylabel('Jitter (s)')
	plt.title('Jitter Cenário'+str(i))
	plt.savefig('DataOut/Sc'+str(i)+'JitterGraphic.png')
	
	ax = plt.cla() 
	ax = plt.gca()

	results.plot(kind='line',y='Latency', label='Always On',ax=ax)
	results.plot(kind='line',y='LatencyRO', label='Random OFF', color='green', ax=ax)
	results.plot(kind='line',y='LatencyFLC', label='Fuzzy Controlled', color='red', ax=ax)
	plt.ylabel('Latência (s)')
	plt.title('Latência Cenário'+str(i))	
	plt.savefig('DataOut/Sc'+str(i)+'LatencyGraphic.png')

	ax = plt.cla() 
	ax = plt.gca()

	results.plot(kind='line',y='PowerConsumed(W)', label='Always On',ax=ax)
	results.plot(kind='line',y='PowerConsumedRO(W)', label='Random OFF', color='green', ax=ax)
	results.plot(kind='line',y='PowerConsumedFLC(W)', label='Fuzzy Controlled', color='red', ax=ax)
	plt.ylabel('Potência consumida (W)')
	plt.title('Potência consumida Cenário'+str(i))	
	plt.savefig('DataOut/Sc'+str(i)+'PowerConsumptionGraphic.png')



