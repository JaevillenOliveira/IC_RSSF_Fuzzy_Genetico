#!/bin/bash
	
function run_sim_ao() {
	.././WSN -u Cmdenv -f wsnSc${1}.ini -n ../../simulations:..:../../../../inet4/src:../../../../inet4/examples:../../../../inet4/tutorials:../../../../inet4/showcases -c wsnSc${1}T${2} -r '$opMode="alwaysON"' --vector-recording=false
	
}

function run_sima_ro() {
	.././WSN -u Cmdenv -f wsnSc${1}.ini -n ../../simulations:..:../../../../inet4/src:../../../../inet4/examples:../../../../inet4/tutorials:../../../../inet4/showcases -c wsnSc${1}T${2} -r '$opMode="randomOFF"' --vector-recording=false
	
}

function run_tests() { 
	for i in $(seq 1 10)
	do	
		run_sim_ao $1 $i
		run_sim_ro $1 $i
		
	done
}

run_tests 1 # Run scenario 1
#run_tests 2 # Run scenario 2
#run_tests 3 # Run scenario 3


