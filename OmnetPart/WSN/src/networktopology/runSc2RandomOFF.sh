#!/bin/bash
	
for i in $(seq 1 10)
do	
.././WSN -u Cmdenv -f wsnSc2.ini -n ../../simulations:..:../../../../inet4/src:../../../../inet4/examples:../../../../inet4/tutorials:../../../../inet4/showcases -c wsnSc2T${i} -r '$opMode="randomOFF"' --vector-recording=false

done
