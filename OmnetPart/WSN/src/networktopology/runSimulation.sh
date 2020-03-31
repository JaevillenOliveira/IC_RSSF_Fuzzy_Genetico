#!/bin/bash

#Starting Matlab Server

for j in $(seq 2 2)
do
	for i in $(seq 5 10)
	do	

	.././WSN -u Cmdenv -f wsnSc${j}.ini -n ../../simulations:..:../../../../inet4/src:../../../../inet4/examples:../../../../inet4/tutorials:../../../../inet4/showcases -c wsnSc${j}T${i} -r '$fuzzyCtrl=false' --vector-recording=false

	done

	for i in $(seq 5 10)
	do	

	gnome-terminal --working-directory=IC/MatlabPart/Matlab_Bridge/ -- /usr/local/MATLAB/R2018b/bin/matlab -nodisplay -nosplash -nodesktop -r "run('Server.m')" &

	sleep 20

	.././WSN -u Cmdenv -f wsnSc${j}.ini -n ../../simulations:..:../../../../inet4/src:../../../../inet4/examples:../../../../inet4/tutorials:../../../../inet4/showcases -c wsnSc${j}T${i} -r '$fuzzyCtrl=true' --vector-recording=false

	done

done

gnome-terminal --working-directory=IC/OmnetPart/WSN/src/networktopology/results/ scavetool x *.sca -o wsn.csv
