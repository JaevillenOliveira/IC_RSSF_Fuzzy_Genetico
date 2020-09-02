import matplotlib.pyplot as plt

def plot_packet_loss(results):
	ax = plt.cla() 
	ax = plt.gca()

	results.plot(kind='line',y='PacketsLost(%)',label='Always On', ax=ax)
	results.plot(kind='line',y='PacketsLostRO(%)', label='Random OFF', color='green', ax=ax)
	results.plot(kind='line',y='PacketsLostFLC(%)', label='Fuzzy Controlled', color='red', ax=ax)
	plt.ylabel('Perda de pacotes (%)')
	plt.title('Perda de pacotes Cenário'+str(i))
	plt.savefig('DataOut/Sc'+str(i)+'PacketsLossGraphic.png')
	
def plot_jitter(results):
	ax = plt.cla() 
	ax = plt.gca()

	results.plot(kind='line',y='Jitter', label='Always On',ax=ax)
	results.plot(kind='line',y='JitterRO', label='Random OFF', color='green', ax=ax)
	results.plot(kind='line',y='JitterFLC', label='Fuzzy Controlled', color='red', ax=ax)
	plt.ylabel('Jitter (s)')
	plt.title('Jitter Cenário'+str(i))
	plt.savefig('DataOut/Sc'+str(i)+'JitterGraphic.png')
	
def plot_latency(results):
	ax = plt.cla() 
	ax = plt.gca()

	results.plot(kind='line',y='Latency', label='Always On',ax=ax)
	results.plot(kind='line',y='LatencyRO', label='Random OFF', color='green', ax=ax)
	results.plot(kind='line',y='LatencyFLC', label='Fuzzy Controlled', color='red', ax=ax)
	plt.ylabel('Latência (s)')
	plt.title('Latência Cenário'+str(i))	
	plt.savefig('DataOut/Sc'+str(i)+'LatencyGraphic.png')
	
def plot_consumed_power(results):
	ax = plt.cla() 
	ax = plt.gca()

	results.plot(kind='line',y='PowerConsumed(W)', label='Always On',ax=ax)
	results.plot(kind='line',y='PowerConsumedRO(W)', label='Random OFF', color='green', ax=ax)
	results.plot(kind='line',y='PowerConsumedFLC(W)', label='Fuzzy Controlled', color='red', ax=ax)
	plt.ylabel('Potência consumida (W)')
	plt.title('Potência consumida Cenário'+str(i))	
	plt.savefig('DataOut/Sc'+str(i)+'PowerConsumptionGraphic.png')

