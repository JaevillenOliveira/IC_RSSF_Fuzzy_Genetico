/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ag;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;


/**
 *
 * @author jaevillen
 */
public class GA {
    
    private Problem problem;
    private BufferedReader br;
    private BufferedWriter bw;
    private StringTokenizer st;
    private int individuoCount;

    /**
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public GA() throws FileNotFoundException, IOException {         
        this.br = new BufferedReader(new FileReader("/home/jaevillen/IC/Buffer/ConfigFile.txt"));  
        this.bw = new BufferedWriter(new FileWriter("/home/jaevillen/IC/Buffer/ConfigFile.txt", true)); 
        this.individuoCount = 0;        
    }
    
    /**
     *
     * @throws IOException
     */
    public void readProblem() throws IOException{
        String name = br.readLine(); 
        int objectives = 0;
        int numberOfVariables = 0;
        int sizeOfPopulation = 0;
        
        this.st = new StringTokenizer(br.readLine());
        /*The first line contains the number of objectives, the number of variables, and the size of the population
          that must be created
        */
        while(st.hasMoreTokens()){
            objectives = Integer.parseInt(st.nextToken());
            numberOfVariables = Integer.parseInt(st.nextToken());
            sizeOfPopulation = Integer.parseInt(st.nextToken());
        }
        
        Integer [] upperLimits = new Integer [numberOfVariables];
        Integer [] lowerLimits = new Integer [numberOfVariables];
        
        this.st = new StringTokenizer(br.readLine());
        /* 
        The next line contains the upper limits of all the variables in the following order: RSSI, 
        number of neighbors, number of sources, throughput and the output.
        These limits will be stored in an array, in which, each index will correspond to one variable.
        */
        for(int i = 0; st.hasMoreTokens(); i++){
            upperLimits[i] = Integer.parseInt(st.nextToken());
        }
        
        /*
        This line contains the lower limits of the variables in the same order as the upper limits
        */
        this.st = new StringTokenizer(br.readLine());
        for(int i = 0; st.hasMoreTokens(); i++){
            lowerLimits[i] = Integer.parseInt(st.nextToken());
        }
        
        //Creates a Problem object with the information read
        this.problem = new Problem(name, objectives, numberOfVariables, sizeOfPopulation, upperLimits, lowerLimits);
    }
    
    /**
     * Reads the first subject of the file (the original one)
     * @throws IOException
     */
    public void readSubject() throws IOException{     
        Solution sol = new Solution(this.problem);
        String subjectId = br.readLine(); //Is the name of the subject ("Individuo" + index)
        for(int i = 0; i < 5; i++){ // Reads the five variables of the subject (including the output)         
            this.st = new StringTokenizer(br.readLine());
            int [][] sets = new int [st.countTokens()/3][3]; // The sets are triangular, therefore contains three nodes 
           
            for(int j = 0; st.hasMoreTokens(); j++){
                for (int k = 0; k < 3; k++){
                   sets[j][k] = Integer.parseInt(st.nextToken());
                } 
            }
            sol.setVariableValue(i, sets);
        }      
        this.problem.getSolutions().add(sol);
        this.individuoCount++;
        this.br.close();
    }
    
    /**
     *
     * @return
     */
    public List<Solution> createPopulation(){
        ArrayList <Solution> newList = new ArrayList <> ();
        for (int i = 1; i < this.problem.getNumberOfIndividuoss(); i++){
            Solution sol = (Solution) this.problem.createSolution();
            newList.add(sol);
        }
        this.problem.getSolutions().addAll(newList);
        return newList;
    }
    
    /**
     *
     * @param solutionList
     * @throws IOException
     */
    public void writePopulation(List <Solution> solutionList) throws IOException{          
        Iterator<Solution> it = solutionList.iterator();
        while(it.hasNext()){
            Solution sol = it.next();
            this.bw.write("Individuo" + " " + ++this.individuoCount);
            for(int i = 0; i < sol.getNumberOfVariables(); i++){
                this.bw.newLine();
                int [][] sets = (int[][]) sol.getVariableValue(i);
                for(int j = 0; j < 3; j++){
                    for (int k = 0; k < 3; k++){
                        this.bw.write(sets[j][k] + " ");
                    }
                }
            }
            this.bw.newLine();
        }
        this.bw.close();
    }
}
