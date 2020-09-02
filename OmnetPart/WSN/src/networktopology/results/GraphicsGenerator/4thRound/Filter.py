import pandas as pd
import matplotlib.pyplot as plt
import json

from Filters import ao_simulation_filter as fao
from Filters import ro_simulation_filter as fro
from Filters import flc_simulation_filter as fflc
from Filters import flcag_simulation_filter as fflcag


for i in range(1, 3):
	final = pd.read_csv('outSc'+str(i)+'.csv', 
	usecols=['PacketsLostFLC(%)', 'PacketsLostFLCAG(%)', 'JitterFLC', 'JitterFLCAG', 'LatencyFLC', 'LatencyFLCAG', 		 'PowerConsumedFLC(W)', 'PowerConsumedFLCAG(W)'])

	ticks = ['T1', 'T2', 'T3', 'T4','T5','T6','T7','T8','T9','T10']

	ax = plt.cla() 
	ax = plt.gca()
	
	final.plot(kind='line',y='PacketsLostFLC(%)', label='Uniform Distribution', ax=ax)
	final.plot(kind='line',y='PacketsLostFLCAG(%)', label='AG Distribution', color='red', ax=ax)
	plt.title('Packet Loss Sc'+str(i))
	ax.set_xticklabels(ticks)
	plt.savefig('Sc'+str(i)+'PacketsLossGraphic.png')

	ax = plt.cla() 
	ax = plt.gca()

	final.plot(kind='line',y='JitterFLC', label='Uniform Distribution', ax=ax)
	final.plot(kind='line',y='JitterFLCAG',  label='AG Distribution',color='red', ax=ax)
	ax.set_xticklabels(ticks)
	plt.title('Jitter Sc'+str(i))
	plt.savefig('Sc'+str(i)+'JitterGraphic.png')

	ax = plt.cla() 
	ax = plt.gca()
	
	final.plot(kind='line',y='LatencyFLC', label='Uniform Distribution',ax=ax)
	final.plot(kind='line',y='LatencyFLCAG', label='AG Distribution',color='red', ax=ax)
	ax.set_xticklabels(ticks)
	plt.title('Latency Sc'+str(i))	
	plt.savefig('Sc'+str(i)+'LatencyGraphic.png')

	ax = plt.cla() 
	ax = plt.gca()
	
	final.plot(kind='line',y='PowerConsumedFLC(W)', label='Uniform Distribution',ax=ax)
	final.plot(kind='line',y='PowerConsumedFLCAG(W)', label='AG Distribution',color='red', ax=ax)
	ax.set_xticklabels(ticks)	
	plt.title('Power Consumption Sc'+str(i))	
	plt.savefig('Sc'+str(i)+'PowerConsumptionGraphic.png')

