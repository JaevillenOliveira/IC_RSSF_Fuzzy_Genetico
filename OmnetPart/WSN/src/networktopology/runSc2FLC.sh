#!/bin/bash
	
function onError {
    if [$errorCount -ge 3 ]
    then
        echo "Error in T $i"
        exit
    else
        echo "erro"
        ((errorCount=errorCount+1))
        sleep 5
        ((i=i-1))
    
    fi
}


errorCount = 0

for i in $(seq 1 10)
do	

gnome-terminal --working-directory=IC/MatlabPart/Matlab_Bridge/ -- /usr/local/MATLAB/R2018b/bin/matlab -nodisplay -nosplash -nodesktop -r "run('/home/jaevillen/IC/MatlabPart/Matlab_Bridge/Server.m')" &

sleep 23

.././WSN -u Cmdenv -f wsnSc2.ini -n ../../simulations:..:../../../../inet4/src:../../../../inet4/examples:../../../../inet4/tutorials:../../../../inet4/showcases -c wsnSc2T${i} -r '$opMode="fuzzyControlled"' --vector-recording=false

trap onError ERR

done



