#!/bin/bash

#./runSc1AlwaysON.sh
#./runSc2AlwaysON.sh

#./runSc1RandomOFF.sh
#./runSc2RandomOFF.sh

gnome-terminal --working-directory=IC/MatlabPart/Matlab_Bridge/ -- /usr/local/MATLAB/R2019b/bin/matlab -nodisplay -nosplash -nodesktop -r "run('/home/jaevillen/IC/MatlabPart/Matlab_Bridge/ReadFuzzySolution.m')"

cd ~
cd IC/OmnetPart/WSN/src/networktopology
./runSc1FLC.sh
#./runSc2FLC.sh

rm /home/jaevillen/IC/MatlabPart/Matlab_Bridge/TempSolution.fis

#cd results
#./scave.sh

#python Filter.py

# Call GA
