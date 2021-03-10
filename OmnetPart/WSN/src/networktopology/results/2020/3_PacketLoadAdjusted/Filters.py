import pandas as pd
import matplotlib.pyplot as plt


def ao_simulation_filter(wsn, wl, run, scenario):

    totalPkSentCount = []
    totalPkReceivedCount = []
    packetLoss = []
    consumedPower = []
    latencyMean = []
    jitterMean = []
    th_wl = []

    for i in range(1, 11):
        packetSentCount = wsn[(wsn.run.str.startswith('wsnSc'+scenario+'T'+str(i)+'-2')) & (wsn.type=='scalar') & 
        (wsn.module.str.startswith("Sc"+scenario+".ss")) & (wsn.module.str.endswith("udp")) & 
        (wsn.name=='packetSent:count')]   

        packetReceivedCount = wsn[(wsn.run.str.startswith('wsnSc'+scenario+'T'+str(i)+'-2'))& (wsn.type=='scalar') & 
        (wsn.module.str.startswith('Sc'+scenario+'.sink')) & 
         (wsn.module.str.endswith("udp")) & (wsn.name=='packetReceived:count')]

        totalPkSentCount.append(sum(packetSentCount.value))
        totalPkReceivedCount.append(sum(packetReceivedCount.value))
        packetLoss.append((totalPkSentCount[i-1] - totalPkReceivedCount[i-1]) * 100 / totalPkSentCount[i-1])


        residualEnergy = wsn[(wsn.run.str.startswith('wsnSc'+scenario+'T'+str(i)+'-2')) & (wsn.type=='scalar') & 
        (wsn.name=='residualEnergyCapacity:last') & 
        (wsn.module.str.endswith('energyStorage'))] 

        if(int(scenario) == 1):
            spentEnergyJ = 3000 - sum(residualEnergy.value)
        elif(int(scenario) == 2):
            spentEnergyJ = 9000 - sum(residualEnergy.value)
        elif(int(scenario) == 3):
            spentEnergyJ = 15000 - sum(residualEnergy.value)
            
        consumedPower.append(spentEnergyJ / 240)

        latency = wsn[(wsn.run.str.startswith('wsnSc'+scenario+'T'+str(i)+'-2')) & (wsn.type=='histogram') & 
        (wsn.name=='endToEndDelay:histogram')] 

        latencyMean.append(latency["mean"].mean())
     
        jitter = wsn[(wsn.run.str.startswith('wsnSc'+scenario+'T'+str(i)+'-2')) & (wsn.type=='histogram') & 
        (wsn.name=='jitter:histogram')] 
        
        jitterMean.append(jitter["mean"].mean())   
        
        th_wl.append(totalPkSentCount[i-1] * 100 / wl[i-1])

    return [totalPkSentCount, totalPkReceivedCount, packetLoss, jitterMean, latencyMean, consumedPower, th_wl];



def ro_simulation_filter(wsn, wl, run, scenario):
    totalPkSentCountRO = []
    totalPkReceivedCountRO = []
    packetLossRO = []
    consumedPowerRO = []
    latencyMeanRO = []
    jitterMeanRO = []
    th_wlRO = []

    for i in range(1, 11):
        packetSentCountRO = wsn[(wsn.run.str.startswith('wsnSc'+scenario+'T'+str(i)+'-1')) & (wsn.type=='scalar') & 
        (wsn.module.str.startswith('Sc'+scenario+'.ss')) & (wsn.module.str.endswith("udp")) & 
        (wsn.name=='packetSent:count')]   

        packetReceivedCountRO = wsn[(wsn.run.str.startswith('wsnSc'+scenario+'T'+str(i)+'-1'))& (wsn.type=='scalar') & 
        (wsn.module.str.startswith('Sc'+scenario+'.sink')) & 
        (wsn.module.str.endswith("udp")) & (wsn.name=='packetReceived:count')]

        totalPkSentCountRO.append(sum(packetSentCountRO.value))
        totalPkReceivedCountRO.append(sum(packetReceivedCountRO.value))
        packetLossRO.append((totalPkSentCountRO[i-1] - totalPkReceivedCountRO[i-1]) * 100 / totalPkSentCountRO[i-1])

        residualEnergyRO = wsn[(wsn.run.str.startswith('wsnSc'+scenario+'T'+str(i)+'-1')) & (wsn.type=='scalar') & 
        (wsn.name=='residualEnergyCapacity:last') & 
        (wsn.module.str.endswith('energyStorage'))] 

        if(int(scenario) == 1):
            spentEnergyJRO = 3000 - sum(residualEnergyRO.value)
        elif(int(scenario) == 2):
            spentEnergyJRO = 9000 - sum(residualEnergyRO.value)
        elif(int(scenario) == 3):
            spentEnergyJRO = 15000 - sum(residualEnergyRO.value)
        consumedPowerRO.append(spentEnergyJRO / 240)

        latencyRO = wsn[(wsn.run.str.startswith('wsnSc'+scenario+'T'+str(i)+'-1')) & (wsn.type=='histogram') & 
        (wsn.name=='endToEndDelay:histogram')] 

        latencyMeanRO.append(latencyRO["mean"].mean())
        
        jitterRO = wsn[(wsn.run.str.startswith('wsnSc'+scenario+'T'+str(i)+'-1')) & (wsn.type=='histogram') & 
        (wsn.name=='jitter:histogram')] 
        
        jitterMeanRO.append(jitterRO["mean"].mean())
        
        th_wlRO.append(totalPkSentCountRO[i-1] * 100 / wl[i-1])

    return [totalPkSentCountRO, totalPkReceivedCountRO, packetLossRO, jitterMeanRO, latencyMeanRO, consumedPowerRO, th_wlRO];


def flc_simulation_filter(wsn, wl, run, scenario):

    totalPkSentCountFLC = []
    totalPkReceivedCountFLC = []
    packetLossFLC = []
    consumedPowerFLC = []
    latencyMeanFLC = []
    jitterMeanFLC = []
    th_wlFLC = []

    for i in range(1, 11):
        packetSentCountFLC = wsn[(wsn.run.str.startswith('wsnSc'+scenario+'T'+str(i)+'-0')) & (wsn.type=='scalar') & 
        (wsn.module.str.startswith('Sc'+scenario+'.ss')) & 
        (wsn.module.str.endswith("udp")) & (wsn.name=='packetSent:count')] 

        packetReceivedCountFLC = wsn[(wsn.run.str.startswith('wsnSc'+scenario+'T'+str(i)+'-0'))& (wsn.type=='scalar') & 
        (wsn.module.str.startswith('Sc'+scenario+'.sink')) & 
        (wsn.module.str.endswith("udp")) & (wsn.name=='packetReceived:count')]

        totalPkSentCountFLC.append(sum(packetSentCountFLC.value))
        totalPkReceivedCountFLC.append(sum(packetReceivedCountFLC.value))
        packetLossFLC.append((totalPkSentCountFLC[i-1] - totalPkReceivedCountFLC[i-1]) * 100 / totalPkSentCountFLC[i-1])


        residualEnergyFLC = wsn[(wsn.run.str.startswith('wsnSc'+scenario+'T'+str(i)+'-0')) & (wsn.type=='scalar') & 
        (wsn.name=='residualEnergyCapacity:last') & 
        (wsn.module.str.endswith('energyStorage'))] 

        if(int(scenario) == 1):
            spentEnergyJFLC = 3000 - sum(residualEnergyFLC.value)
        elif(int(scenario) == 2):
            spentEnergyJFLC = 9000 - sum(residualEnergyFLC.value)
        elif(int(scenario) == 3):
            spentEnergyJFLC = 15000 - sum(residualEnergyFLC.value)
        consumedPowerFLC.append(spentEnergyJFLC / 240)

        latencyFLC = wsn[(wsn.run.str.startswith('wsnSc'+scenario+'T'+str(i)+'-0')) & (wsn.type=='histogram') & 
        (wsn.name=='endToEndDelay:histogram')] 

        latencyMeanFLC.append(latencyFLC["mean"].mean())

        jitterFLC = wsn[(wsn.run.str.startswith('wsnSc'+scenario+'T'+str(i)+'-0')) & 
        (wsn.type=='histogram') & (wsn.name=='jitter:histogram')] 

        jitterMeanFLC.append(jitterFLC["mean"].mean()) 
        
        th_wlFLC.append(totalPkSentCountFLC[i-1] * 100 / wl[i-1])

    return [totalPkSentCountFLC, totalPkReceivedCountFLC, packetLossFLC, jitterMeanFLC, latencyMeanFLC, consumedPowerFLC, th_wlFLC];
     

