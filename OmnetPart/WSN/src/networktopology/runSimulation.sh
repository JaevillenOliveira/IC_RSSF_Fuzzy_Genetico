#!/bin/bash

export PATH=$HOME/anaconda3/bin:$PATH
export PATH=$HOME/omnetpp-5.5.1/bin:$PATH

gnome-terminal --working-directory=IC/MatlabPart/Matlab_Bridge/ -- /usr/local/MATLAB/R2019b/bin/matlab -nodisplay -nosplash -nodesktop -r "run('/home/jaevillen/IC/MatlabPart/Matlab_Bridge/ReadFuzzySolution.m')"

sleep 12

cd ~
cd IC/OmnetPart/WSN/src/networktopology
#./runSc1FLC.sh
#./runSc2FLC.sh
./runSc3FLC.sh

rm /home/jaevillen/IC/MatlabPart/Matlab_Bridge/TempSolution.fis

cd results
./scave.sh

python Filter.py

# Call GA
