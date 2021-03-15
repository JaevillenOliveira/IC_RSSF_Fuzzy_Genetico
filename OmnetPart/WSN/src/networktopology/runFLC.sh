#!/bin/bash

function start_server() {
	gnome-terminal --working-directory=IC/MatlabPart/Matlab_Bridge/ -- /usr/local/MATLAB/R2018b/bin/matlab -nodisplay -nosplash -nodesktop -r "run('/home/jaevillen/IC/MatlabPart/Matlab_Bridge/Server.m')" &
	
}

function run_sim() {
	.././WSN -u Cmdenv -f wsnSc${1}.ini -n ../../simulations:..:../../../../inet4/src:../../../../inet4/examples:../../../../inet4/tutorials:../../../../inet4/showcases -c wsnSc${1}T${2} -r '$opMode="fuzzyControlled"' --vector-recording=false
	
}

function run_tests() {     
	#start_server
	#sleep 20        
	             
	for i in $(seq 1 10) 
	do
		loop_count=0
		start_server
	    sleep 5
		run_sim $1 $i 
		status=$?
		
		echo "Sc${1}T${i}, Loop Count ${loop_count}: ${status}" >> log.txt;
		
		while [ $status == 3 ] 
		do
			echo "LOOP ${loop_count}"; 
			if [ $loop_count -eq 6 ]
			then
				start_server
				sleep 20

			elif [ $loop_count -gt 6 ]
			then
				echo "Sc${1}T${i}, Loop Count ${loop_count}: 5" >> log.txt
				exit
			fi

			sleep 5
			run_sim $1 $i
			status=$?
			
			((loop_count++)) 
			echo "LOOP ${loop_count}";
			echo "Sc${1}T${i}, Loop Count ${loop_count}: ${status}" >> log.txt

		done
	done
}


#run_tests 1 # Run scenario 1
#run_tests 2 # Run scenario 2
#run_tests 3 # Run scenario 3




