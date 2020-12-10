import matplotlib.pyplot as plt

def plot_packet_loss(results, scenario):
	ax = plt.cla() 
	ax = plt.gca()

	results.plot(kind='line',y='PacketsLost(%)',label='Always On', ax=ax)
	results.plot(kind='line',y='PacketsLostRO(%)', label='Random OFF', color='green', ax=ax)
	results.plot(kind='line',y='PacketsLostFLC(%)', label='Fuzzy Controlled', color='red', ax=ax)
	results.plot(kind='line',y='PacketsLostFLCAG(%)', label='GA Fuzzy Controlled', color='black', ax=ax)
	plt.ylabel('Perda de pacotes (%)')
	plt.title('Perda de pacotes Cenário'+scenario)
	plt.savefig('Sc'+scenario+'PacketsLoss.png')
	
def plot_th_wl(results, scenario):
	ax = plt.cla() 
	ax = plt.gca()

	results.plot(kind='line',y='Th/Wl(%)', label='Always On',ax=ax)
	results.plot(kind='line',y='Th/WlRO(%)', label='Random OFF', color='green', ax=ax)
	results.plot(kind='line',y='Th/WlFLC(%)', label='Fuzzy Controlled', color='red', ax=ax)
	results.plot(kind='line',y='Th/WlFLCAG(%)', label='GA Fuzzy Controlled', color='black', ax=ax)

	plt.ylabel('Th/Wl percentage (%)')
	plt.title('Th/Wl percentage Cenário'+scenario)	
	plt.savefig('Sc'+scenario+'Th_Wl.png')

	
def plot_jitter(results, scenario):
	ax = plt.cla() 
	ax = plt.gca()

	results.plot(kind='line',y='Jitter', label='Always On',ax=ax)
	results.plot(kind='line',y='JitterRO', label='Random OFF', color='green', ax=ax)
	results.plot(kind='line',y='JitterFLC', label='Fuzzy Controlled', color='red', ax=ax)
	results.plot(kind='line',y='JitterFLCAG', label='GA Fuzzy Controlled', color='black', ax=ax)
	plt.ylabel('Jitter (s)')
	plt.title('Jitter Cenário'+scenario)
	plt.savefig('Sc'+scenario+'Jitter.png')
	
def plot_latency(results, scenario):
	ax = plt.cla() 
	ax = plt.gca()

	results.plot(kind='line',y='Latency', label='Always On',ax=ax)
	results.plot(kind='line',y='LatencyRO', label='Random OFF', color='green', ax=ax)
	results.plot(kind='line',y='LatencyFLC', label='Fuzzy Controlled', color='red', ax=ax)
	results.plot(kind='line',y='LatencyFLCAG', label='GA Fuzzy Controlled', color='black', ax=ax)
	plt.ylabel('Latência (s)')
	plt.title('Latência Cenário'+scenario)	
	plt.savefig('Sc'+scenario+'Latency.png')
	
def plot_consumed_power(results, scenario):
	ax = plt.cla() 
	ax = plt.gca()

	results.plot(kind='line',y='PowerConsumed(W)', label='Always On',ax=ax)
	results.plot(kind='line',y='PowerConsumedRO(W)', label='Random OFF', color='green', ax=ax)
	results.plot(kind='line',y='PowerConsumedFLC(W)', label='Fuzzy Controlled', color='red', ax=ax)
	results.plot(kind='line',y='PowerConsumedFLCAG(W)', label='GA Fuzzy Controlled', color='black', ax=ax)
	plt.ylabel('Potência consumida (W)')
	plt.title('Potência consumida Cenário'+scenario)	
	plt.savefig('Sc'+scenario+'PowerConsumption.png')

