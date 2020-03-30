#!/bin/bash

#Starting Matlab Server

for i in $(seq 10 10)
do	

gnome-terminal --working-directory=IC/MatlabPart/Matlab_Bridge/ -- /usr/local/MATLAB/R2018b/bin/matlab -nodisplay -nosplash -nodesktop -r "run('Server.m')" &

sleep 20

.././WSN -u Cmdenv -f wsnSc1.ini -n ../../simulations:..:../../../../inet4/src:../../../../inet4/examples:../../../../inet4/tutorials:../../../../inet4/showcases -c wsnSc1T${i} --vector-recording=false

done
gnome-terminal --working-directory=IC/OmnetPart/WSN/src/networktopology/results/ scavetool x *.sca -o wsn.csv
