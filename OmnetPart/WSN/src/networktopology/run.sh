#!/bin/bash

export PATH=$HOME/anaconda3/bin:$PATH
export PATH=$HOME/omnetpp-5.5.1/bin:$PATH

#./runSc1RandomOFF.sh
#./runSc2RandomOFF.sh
#./runSc3RandomOFF.sh

#./runSc1AlwaysON.sh
#./runSc2AlwaysON.sh
#./runSc3AlwaysON.sh

./runSc1FLC.sh
./runSc2FLC.sh
./runSc3FLC.sh

cd results
scavetool x wsnSc1T* -o wsn1.csv
scavetool x wsnSc2T* -o wsn2.csv
scavetool x wsnSc3T* -o wsn3.csv

#python -c "import Filter; Filter.call_func_wsn1()"
#python -c "import Filter; Filter.call_func_wsn2()"
#python -c "import Filter; Filter.call_func_wsn2()"
