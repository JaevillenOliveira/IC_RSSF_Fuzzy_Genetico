#!/bin/bash
	
for i in $(seq 1 10)
do	

gnome-terminal --working-directory=IC/MatlabPart/Matlab_Bridge/ -- /usr/local/MATLAB/R2018b/bin/matlab -nodisplay -nosplash -nodesktop -r "run('/home/jaevillen/IC/MatlabPart/Matlab_Bridge/Server.m')" &

sleep 20

.././WSN -u Cmdenv -f wsnSc3.ini -n ../../simulations:..:../../../../inet4/src:../../../../inet4/examples:../../../../inet4/tutorials:../../../../inet4/showcases -c wsnSc3T${i} -r '$opMode="fuzzyControlled"' --vector-recording=false

done
