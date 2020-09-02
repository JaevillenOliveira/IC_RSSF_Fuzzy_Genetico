import pandas as pd
import matplotlib.pyplot as plt
import json

from Filters import ao_simulation_filter as fao
from Filters import ro_simulation_filter as fro
from Filters import flc_simulation_filter as fflc
from Filters import flcag_simulation_filter as fflcag

wsn1 = pd.read_csv('wsn1.csv', usecols=['run', 'type', 'module', 'name', 'value', 'mean'])
wsn2 = pd.read_csv('wsn2.csv', usecols=['run', 'type', 'module', 'name', 'value', 'mean'])

wsn = [wsn1, wsn2]
run = ['wsnSc1T', 'wsnSc2T']


for i in range(1, 3):
	fresult_fao = fao(wsn[i-1], run[i-1], str(i)) ##Only needed to be executed once,the GA doesn't interfere here
	fresult_fro = fro(wsn[i-1], run[i-1], str(i)) ##Only needed to be executed once,the GA doesn't interfere here
	fresult_fflc = fflc(wsn[i-1], run[i-1], str(i)) 
	fresult_fflcag = fflcag(wsn[i-1], run[i-1], str(i)) 

	#####
	# HERE JUST NEED TO EXECUTE AT THE END, WHEN HAVE THE IDEAL SOLUTION 
	####

	results = pd.DataFrame(index=['T1', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'T8', 'T9', 'T10'])
	results.insert(0, "PacketsSent", fresult_fao[0])  
	results.insert(1, "PacketsSentRO", fresult_fro[0])  
	results.insert(2, "PacketsSentFLC", fresult_fflc[0])  
	results.insert(3, "PacketsSentFLCAG", fresult_fflcag[0]) 

	
	results.insert(4, "PacketsReceived", fresult_fao[1])  
	results.insert(5, "PacketsReceivedRO", fresult_fro[1])
	results.insert(6, "PacketsReceivedFLC", fresult_fflc[1])  
	results.insert(7, "PacketsReceivedFLCAG", fresult_fflcag[1])  
	  
	
	results.insert(8, "PacketsLost(%)", fresult_fao[2])  
	results.insert(9, "PacketsLostRO(%)", fresult_fro[2])
	results.insert(10, "PacketsLostFLC(%)", fresult_fflc[2])  
	results.insert(11, "PacketsLostFLCAG(%)", fresult_fflcag[2]) 
	  
	
	results.insert(12, "Jitter", fresult_fao[3])  
	results.insert(13, "JitterRO", fresult_fro[3]) 
	results.insert(14, "JitterFLC", fresult_fflc[3])  
	results.insert(15, "JitterFLCAG", fresult_fflcag[3])  
	 
	
	results.insert(16, "Latency", fresult_fao[4])  
	results.insert(17, "LatencyRO", fresult_fro[4]) 
	results.insert(18, "LatencyFLC", fresult_fflc[4]) 
	results.insert(19, "LatencyFLCAG", fresult_fflcag[4])  
	 
	
	results.insert(20, "PowerConsumed(W)", fresult_fao[5])  
	results.insert(21, "PowerConsumedRO(W)", fresult_fro[5])
	results.insert(22, "PowerConsumedFLC(W)", fresult_fflc[5])  
	results.insert(23, "PowerConsumedFLCAG(W)", fresult_fflcag[5])  
	
	minConsump = []
	if(i == 2):
		for b in (fresult_fao[5]):
			minConsump.append(b/9*5)
	elif(i ==1):
		for b in (fresult_fao[5]):
			minConsump.append(b/3*2)
	
	#results.insert(24, "Min Consump Possible", minConsump)  
	#results.insert(25, "diff", sum(fresult_fflcag[5]) - sum(minConsump))  
	  

#	power_consumption_dict = {'T'+str(i+1) : (fresult_fflc[5])[i] for i in range(0, len(fresult_fflc[5])) }
#	y = json.dumps(power_consumption_dict)
#	with open('/home/jaevillen/IC/Buffer/power_consumption_sc'+str(i)+'.txt', 'w') as outfile:
#		json.dump(y, outfile)

	results.to_csv('outSc'+str(i)+'.csv')

	ax = plt.cla() 
	ax = plt.gca()

	results.plot(kind='line',y='PacketsLost(%)',label='Always On', ax=ax)
	results.plot(kind='line',y='PacketsLostRO(%)', label='Random OFF', color='green', ax=ax)
	results.plot(kind='line',y='PacketsLostFLC(%)', label='Fuzzy Controlled', color='red', ax=ax)
	results.plot(kind='line',y='PacketsLostFLCAG(%)', label='GA Fuzzy Controlled', color='black', ax=ax)
	plt.ylabel('Perda de pacotes (%)')
	plt.title('Perda de pacotes Cenario'+str(i))
	plt.savefig('Sc'+str(i)+'PacketsLossGraphic.png')
	
	ax = plt.cla() 
	ax = plt.gca()

	results.plot(kind='line',y='Jitter', label='Always On',ax=ax)
	results.plot(kind='line',y='JitterRO', label='Random OFF', color='green', ax=ax)
	results.plot(kind='line',y='JitterFLC', label='Fuzzy Controlled', color='red', ax=ax)
	results.plot(kind='line',y='JitterFLCAG', label='GA Fuzzy Controlled', color='black', ax=ax)
	plt.ylabel('Jitter (s)')
	plt.title('Jitter Cenario'+str(i))
	plt.savefig('Sc'+str(i)+'JitterGraphic.png')
	
	ax = plt.cla() 
	ax = plt.gca()

	results.plot(kind='line',y='Latency', label='Always On',ax=ax)
	results.plot(kind='line',y='LatencyRO', label='Random OFF', color='green', ax=ax)
	results.plot(kind='line',y='LatencyFLC', label='Fuzzy Controlled', color='red', ax=ax)
	results.plot(kind='line',y='LatencyFLCAG', label='GA Fuzzy Controlled', color='black', ax=ax)
	plt.ylabel('Latencia (s)')
	plt.title('Latencia Cenario'+str(i))	
	plt.savefig('Sc'+str(i)+'LatencyGraphic.png')

	ax = plt.cla() 
	ax = plt.gca()

	results.plot(kind='line',y='PowerConsumed(W)', label='Always On',ax=ax)
	results.plot(kind='line',y='PowerConsumedRO(W)', label='Random OFF', color='green', ax=ax)
	results.plot(kind='line',y='PowerConsumedFLC(W)', label='Fuzzy Controlled', color='red', ax=ax)
	results.plot(kind='line',y='PowerConsumedFLCAG(W)', label='GA Fuzzy Controlled', color='black', ax=ax)
	plt.ylabel('Potencia consumida (W)')
	plt.title('Potencia consumida Cenario'+str(i))	
	plt.savefig('Sc'+str(i)+'PowerConsumptionGraphic.png')



