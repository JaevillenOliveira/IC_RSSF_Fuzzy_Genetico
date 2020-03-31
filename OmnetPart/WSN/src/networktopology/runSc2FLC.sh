#!/bin/bash
	
for i in $(seq 1 10)
do	

gnome-terminal --working-directory=IC/MatlabPart/Matlab_Bridge/ -- /usr/local/MATLAB/R2018b/bin/matlab -nodisplay -nosplash -nodesktop -r "run('Server.m')" &

sleep 20

.././WSN -u Cmdenv -f wsnSc2.ini -n ../../simulations:..:../../../../inet4/src:../../../../inet4/examples:../../../../inet4/tutorials:../../../../inet4/showcases -c wsnSc2T${i} -r '$fuzzyCtrl=true' --vector-recording=false

done
