import pandas as pd
import matplotlib.pyplot as plt
import json

from Filters import ao_simulation_filter as fao
from Filters import ro_simulation_filter as fro
from Filters import flc_simulation_filter as fflc

wsn3 = pd.read_csv('wsn3.csv', usecols=['run', 'type', 'module', 'name', 'value', 'mean'])

wsn = wsn3
run = 'wsnSc3T'


fresult_fao = fao(wsn[i-1], run[i-1], str(i)) ##Only needed to be executed once,the GA doesn't interfere here
#fresult_fro = fro(wsn[i-1], run[i-1], str(i)) ##Only needed to be executed once,the GA doesn't interfere here
#fresult_fflc = fflc(wsn[i-1], run[i-1], str(i)) 

#####
# HERE JUST NEED TO EXECUTE AT THE END, WHEN HAVE THE IDEAL SOLUTION 
####

results = pd.DataFrame(index=['T1', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'T8', 'T9', 'T10'])
results.insert(0, "PacketsSent", fresult_fao[0])  
results.insert(1, "PacketsReceived", fresult_fao[1])  
results.insert(2, "PacketsLost(%)", fresult_fao[2])  
results.insert(3, "Jitter", fresult_fao[3])  
results.insert(4, "Latency", fresult_fao[4])  
results.insert(5, "PowerConsumed(W)", fresult_fao[5])  

results.to_csv('outSc'+str(i)+'.csv')

#	ax = plt.cla() 
#	ax = plt.gca()

#	results.plot(kind='line',y='PacketsLost(%)',label='Always On', ax=ax)
#	results.plot(kind='line',y='PacketsLostRO(%)', label='Random OFF', color='green', ax=ax)
#	results.plot(kind='line',y='PacketsLostFLC(%)', label='Fuzzy Controlled', color='red', ax=ax)
#	plt.ylabel('Perda de pacotes (%)')
#	plt.title('Perda de pacotes Cenário'+str(i))
#	plt.savefig('DataOut/Sc'+str(i)+'PacketsLossGraphic.png')

#	ax = plt.cla() 
#	ax = plt.gca()

#	results.plot(kind='line',y='Jitter', label='Always On',ax=ax)
#	results.plot(kind='line',y='JitterRO', label='Random OFF', color='green', ax=ax)
#	results.plot(kind='line',y='JitterFLC', label='Fuzzy Controlled', color='red', ax=ax)
#	plt.ylabel('Jitter (s)')
#	plt.title('Jitter Cenário'+str(i))
#	plt.savefig('DataOut/Sc'+str(i)+'JitterGraphic.png')

#	ax = plt.cla() 
#	ax = plt.gca()

#	results.plot(kind='line',y='Latency', label='Always On',ax=ax)
#	results.plot(kind='line',y='LatencyRO', label='Random OFF', color='green', ax=ax)
#	results.plot(kind='line',y='LatencyFLC', label='Fuzzy Controlled', color='red', ax=ax)
#	plt.ylabel('Latência (s)')
#	plt.title('Latência Cenário'+str(i))	
#	plt.savefig('DataOut/Sc'+str(i)+'LatencyGraphic.png')

#	ax = plt.cla() 
#	ax = plt.gca()

#	results.plot(kind='line',y='PowerConsumed(W)', label='Always On',ax=ax)
#	results.plot(kind='line',y='PowerConsumedRO(W)', label='Random OFF', color='green', ax=ax)
#	results.plot(kind='line',y='PowerConsumedFLC(W)', label='Fuzzy Controlled', color='red', ax=ax)
#	plt.ylabel('Potência consumida (W)')
#	plt.title('Potência consumida Cenário'+str(i))	
#	plt.savefig('DataOut/Sc'+str(i)+'PowerConsumptionGraphic.png')



