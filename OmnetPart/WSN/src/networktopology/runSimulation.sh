#!/bin/bash

export PATH=$HOME/anaconda3/bin:$PATH
export PATH=$HOME/omnetpp-5.5.1/bin:$PATH

gnome-terminal --working-directory=IC/MatlabPart/Matlab_Bridge/ -- /usr/local/MATLAB/R2018b/bin/matlab -nodisplay -nosplash -nodesktop -r "run('/home/jaevillen/IC/MatlabPart/Matlab_Bridge/ReadFuzzySolution.m')"

sleep 12

cd ~
cd IC/OmnetPart/WSN/src/networktopology

if [ $@ -eq "1" ]
then
    source runFLC.sh; run_tests 1
    rm /home/jaevillen/IC/MatlabPart/Matlab_Bridge/TempSolution.fis 
    cd results
    scavetool x wsnSc1T* -o wsn1.csv
    python -c "import Filter; Filter.dump_consumption_into_file('1')"

elif [ $@ == "2" ]
then
    source runFLC.sh; run_tests 2
    rm /home/jaevillen/IC/MatlabPart/Matlab_Bridge/TempSolution.fis 
    cd results
    scavetool x wsnSc2T* -o wsn2.csv
    python -c "import Filter; Filter.dump_consumption_into_file('2')"

elif [ $@ == "3" ]
then
    source runFLC.sh; run_tests 3
    rm /home/jaevillen/IC/MatlabPart/Matlab_Bridge/TempSolution.fis 
    cd results
    scavetool x wsnSc3T* -o wsn3.csv
    python -c "import Filter; Filter.dump_consumption_into_file('3')"

else
    echo "Scenario $@ doesn't exist"

fi


