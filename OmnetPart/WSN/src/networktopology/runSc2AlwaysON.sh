#!/bin/bash
	
for i in $(seq 9 9)
do	
.././WSN -u Cmdenv -f wsnSc2.ini -n ../../simulations:..:../../../../inet4/src:../../../../inet4/examples:../../../../inet4/tutorials:../../../../inet4/showcases -c wsnSc2T${i} -r '$opMode="alwaysON"' --vector-recording=false

done
