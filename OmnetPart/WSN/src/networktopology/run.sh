#!/bin/bash

export PATH=$HOME/anaconda3/bin:$PATH
export PATH=$HOME/omnetpp-5.5.1/bin:$PATH

#source runAO_RO.sh; run_tests 3
#source runAO_RO.sh; run_tests 2
#source runAO_RO.sh; run_tests 3

source runFLC.sh; run_tests 1
#source runFLC.sh; run_tests 2
#source runFLC.sh; run_tests 3

cd results
scavetool x wsnSc1T* -o wsn1.csv
scavetool x wsnSc2T* -o wsn2.csv
scavetool x wsnSc3T* -o wsn3.csv

#python -c "import Filter; Filter.call_func_wsn1()"
#python -c "import Filter; Filter.call_func_wsn2()"
#python -c "import Filter; Filter.call_func_wsn2()"
