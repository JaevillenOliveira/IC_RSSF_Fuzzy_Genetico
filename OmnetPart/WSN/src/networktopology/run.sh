#!/bin/bash


export PATH=$HOME/anaconda3/bin:$PATH
export PATH=$HOME/omnetpp-5.5.1/bin:$PATH

./runSc1FLC.sh
./runSc2FLC.sh
./runSc3FLC.sh

#./runSc1RandomOFF.sh
#./runSc2RandomOFF.sh
#./runSc3RandomOFF.sh

#./runSc1AlwaysON.sh
#./runSc2AlwaysON.sh
#./runSc3AlwaysON.sh
