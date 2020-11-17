#!/bin/bash

export PATH=$HOME/anaconda3/bin:$PATH
export PATH=$HOME/omnetpp-5.5.1/bin:$PATH


if [ $@ -eq "1" ]
then
    gnome-terminal --working-directory=IC/MatlabPart/Matlab_Bridge/ -- /usr/local/MATLAB/R2018b/bin/matlab -nodisplay -nosplash -nodesktop -r "run('/home/jaevillen/IC/MatlabPart/Matlab_Bridge/ReadFuzzySolutionSc1.m')"

    sleep 12

    cd ~
    cd IC/OmnetPart/WSN/src/networktopology
    ./runSc1FLC.sh
    rm /home/jaevillen/IC/MatlabPart/Matlab_Bridge/TempSolution.fis 
    cd results
    scavetool x wsnSc1T* -o wsn1.csv
    python -c "import Filter; Filter.dump_consumption_into_file('1')"

elif [ $@ == "2" ]
then
    gnome-terminal --working-directory=IC/MatlabPart/Matlab_Bridge/ -- /usr/local/MATLAB/R2018b/bin/matlab -nodisplay -nosplash -nodesktop -r "run('/home/jaevillen/IC/MatlabPart/Matlab_Bridge/ReadFuzzySolutionSc2.m')"

    sleep 12

    cd ~
    cd IC/OmnetPart/WSN/src/networktopology
    ./runSc2FLC.sh
    rm /home/jaevillen/IC/MatlabPart/Matlab_Bridge/TempSolution.fis 
    cd results
    scavetool x wsnSc2T* -o wsn2.csv
    python -c "import Filter; Filter.dump_consumption_into_file('2')"

elif [ $@ == "3" ]
then
    gnome-terminal --working-directory=IC/MatlabPart/Matlab_Bridge/ -- /usr/local/MATLAB/R2018b/bin/matlab -nodisplay -nosplash -nodesktop -r "run('/home/jaevillen/IC/MatlabPart/Matlab_Bridge/ReadFuzzySolutionSc3.m')"

    sleep 12

    cd ~
    cd IC/OmnetPart/WSN/src/networktopology
    ./runSc3FLC.sh
    rm /home/jaevillen/IC/MatlabPart/Matlab_Bridge/TempSolution.fis 
    cd results
    scavetool x wsnSc3T* -o wsn3.csv
    python -c "import Filter; Filter.dump_consumption_into_file('3')"

else
    echo "Scenario $@ doesn't exist"

fi


