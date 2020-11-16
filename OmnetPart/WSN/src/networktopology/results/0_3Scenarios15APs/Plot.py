import matplotlib.pyplot as plt

def plot_packet_loss(results, scenario):
	ax = plt.cla() 
	ax = plt.gca()

	results.plot(kind='line',y='PacketsLost(%)',label='Always On', ax=ax)
	results.plot(kind='line',y='PacketsLostRO(%)', label='Random OFF', color='green', ax=ax)
	results.plot(kind='line',y='PacketsLostFLC(%)', label='Fuzzy Controlled', color='red', ax=ax)
	plt.ylabel('Perda de pacotes (%)')
	plt.title('Perda de pacotes Cenário'+scenario)
	plt.savefig('Sc'+scenario+'PacketsLossGraphic.png')
	
def plot_jitter(results, scenario):
	ax = plt.cla() 
	ax = plt.gca()

	results.plot(kind='line',y='Jitter', label='Always On',ax=ax)
	results.plot(kind='line',y='JitterRO', label='Random OFF', color='green', ax=ax)
	results.plot(kind='line',y='JitterFLC', label='Fuzzy Controlled', color='red', ax=ax)
	plt.ylabel('Jitter (s)')
	plt.title('Jitter Cenário'+scenario)
	plt.savefig('Sc'+scenario+'JitterGraphic.png')
	
def plot_latency(results, scenario):
	ax = plt.cla() 
	ax = plt.gca()

	results.plot(kind='line',y='Latency', label='Always On',ax=ax)
	results.plot(kind='line',y='LatencyRO', label='Random OFF', color='green', ax=ax)
	results.plot(kind='line',y='LatencyFLC', label='Fuzzy Controlled', color='red', ax=ax)
	plt.ylabel('Latência (s)')
	plt.title('Latência Cenário'+scenario)	
	plt.savefig('Sc'+scenario+'LatencyGraphic.png')
	
def plot_consumed_power(results, scenario):
	ax = plt.cla() 
	ax = plt.gca()

	results.plot(kind='line',y='PowerConsumed(W)', label='Always On',ax=ax)
	results.plot(kind='line',y='PowerConsumedRO(W)', label='Random OFF', color='green', ax=ax)
	results.plot(kind='line',y='PowerConsumedFLC(W)', label='Fuzzy Controlled', color='red', ax=ax)
	plt.ylabel('Potência consumida (W)')
	plt.title('Potência consumida Cenário'+scenario)	
	plt.savefig('Sc'+scenario+'PowerConsumptionGraphic.png')

