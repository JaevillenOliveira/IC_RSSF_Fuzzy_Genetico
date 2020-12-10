#!/bin/bash

readWriteFiles() {
	genFile="/home/jaevillen/IC/Buffer/FittestGenerationSc${1}.txt"
	tempFile="/home/jaevillen/IC/Buffer/TempSolution.txt"
	
 
 	for i in $(seq 1 $2) # $2 is the number of subjects to read and write
	do
		read line # Reads the header of the subject
		echo "$line" 
		
		true > $tempFile # Clears the Temp file
		
		for j in $(seq 1 $3) # Goes by each variable of the subject
		do 
			read line
			echo "$line" >> $tempFile # Write the variables into the temp file
			
		done
		
		./runSimulation.sh $1
		exit  

	done < $genFile
}


# Script Params: scenarioNumber, numberOfSubjects, numberOfFuzzyVariables

readWriteFiles $@
