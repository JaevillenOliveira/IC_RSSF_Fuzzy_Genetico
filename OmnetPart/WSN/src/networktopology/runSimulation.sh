#!/bin/bash

#Starting Matlab Server
path_to_server="../../../../MatlabPart/Matlab_Bridge/Sever.m"
run_command="run('Server.m')"
gnome-terminal --working-directory=IC/MatlabPart/Matlab_Bridge/ -- sudo /usr/local/MATLAB/R2018b/bin/matlab -nodisplay -nosplash -nodesktop -r ${run_command} &


for i in $(seq 1 10)
do	
sudo .././WSN -u Cmdenv -f wsn.ini -n ../../simulations:..:../../../../inet4/src:../../../../inet4/examples:../../../../inet4/tutorials:../../../../inet4/showcases -c wsnSc1T${i} --vector-recording=false
done


#gnome-terminal -x sh -c
