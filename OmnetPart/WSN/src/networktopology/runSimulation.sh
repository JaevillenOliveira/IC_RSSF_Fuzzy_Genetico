#!/bin/bash
gnome-terminal -x sh -c sudo /usr/local/MATLAB/R2018b/bin/matlab -nodisplay -nosplash -nodesktop -r "run('../../../../MatlabPart/Matlab_Bridge/Sever.m')"; bash

for i in $(seq 1 10)
do	
.././WSN -u Cmdenv -f wsn.ini -n ../../simulations:..:../../../../inet4/src:../../../../inet4/examples:../../../../inet4/tutorials:../../../../inet4/showcases -c wsnSc1T${i} --vector-recording=false
done



