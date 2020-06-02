import pandas as pd
import matplotlib.pyplot as plt

from Filters import ao_simulation_filter as fao
from Filters import ro_simulation_filter as fro
from Filters import flc_simulation_filter as fflc

wsn1 = pd.read_csv('wsn1.csv', usecols=['run', 'type', 'module', 'name', 'value', 'mean'])
wsn2 = pd.read_csv('wsn2.csv', usecols=['run', 'type', 'module', 'name', 'value', 'mean'])

wsn = [wsn1, wsn2]
run = ['wsnSc1T', 'wsnSc2T']


for i in range(1, 3):
	fresult_fao = fao(wsn[i-1], run[i-1], str(i)) 
	fresult_fro = fro(wsn[i-1], run[i-1], str(i)) 
	fresult_fflc = fflc(wsn[i-1], run[i-1], str(i)) 

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

	results.to_csv('outSc'+str(i)+'.csv')

	#ax = plt.gca()

	# results.plot(kind='line',y='PacketsLost(%)', ax=ax)
	# results.plot(kind='line',y='PacketsLostFLC(%)', color='red', ax=ax)
	# results.plot(kind='line',y='PacketsLostRO(%)', color='green', ax=ax)
	# plt.title('Packet Loss Sc'+str(i))
	# plt.savefig('DataOut/Sc'+str(i)+'PacketsLossGraphic.png')

	# results.plot(kind='line',y='Jitter', ax=ax)
	# results.plot(kind='line',y='JitterFLC', color='red', ax=ax)
	# results.plot(kind='line',y='JitterRO', color='green', ax=ax)
	# plt.title('Jitter Sc2')
	# plt.savefig('DataOut/Sc'+str(i)+'JitterGraphic.png')

	# results.plot(kind='line',y='Latency', ax=ax)
	# results.plot(kind='line',y='LatencyFLC', color='red', ax=ax)
	# results.plot(kind='line',y='LatencyRO', color='green', ax=ax)
	# plt.title('Latency Sc2')
	# plt.savefig('DataOut/Sc'+str(i)+'LatencyGraphic.png')

	# results.plot(kind='line',y='PowerConsumed(W)', ax=ax)
	# results.plot(kind='line',y='PowerConsumedFLC(W)', color='red', ax=ax)
	# results.plot(kind='line',y='PowerConsumedRO(W)', color='green', ax=ax)
	# plt.title('Power Consumption Sc2')
	# plt.savefig('DataOut/Sc'+str(i)+'PowerConsumptionGraphic.png')



