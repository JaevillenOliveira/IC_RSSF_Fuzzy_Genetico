import pandas as pd
import glob
import matplotlib.pyplot as plt

files = glob.glob("/home/jaevillen/IC/OmnetPart/WSN/src/networktopology/FuzzyEntries/*.txt")

for file in files:
    th = []
    plt.cla()
    plt.gca()
    with open(file, 'r') as reader:
        line = reader.readline()
        line = reader.readline()
        while line != '':  # The EOF char is an empty string
            th.append(float((line.split())[-1]))
            line = reader.readline()

        plt.style.use('ggplot')
        plt.hist(th, bins=10)
        plt.savefig(file+'.png')

