/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ag;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 *
 * @author jaevillen
 */
public class GA {
    
    private Problem problem;
    private FileReader fr;
    private BufferedReader br;
    private StringTokenizer st;

    public GA() throws FileNotFoundException {
        this.fr = new FileReader("/home/jaevillen/IC/Buffer/ConfigFile.txt");            
        this.br = new BufferedReader(this.fr);    
        
    }
    
    public void readProblem() throws IOException{
        String name = br.readLine(); 
        int objectives = 0;
        int numberOfVariables = 0;
        int sizeOfPopulation = 0;
        
        this.st = new StringTokenizer(br.readLine());
        while(st.hasMoreTokens()){
            objectives = Integer.parseInt(st.nextToken());
            numberOfVariables = Integer.parseInt(st.nextToken());
            sizeOfPopulation = Integer.parseInt(st.nextToken());
        }
        
        Integer [] upperLimits = new Integer [numberOfVariables];
        Integer [] lowerLimits = new Integer [numberOfVariables];
        
        this.st = new StringTokenizer(br.readLine());
        for(int i = 0; st.hasMoreTokens(); i++){
            upperLimits[i] = Integer.parseInt(st.nextToken());
        }
        
        this.st = new StringTokenizer(br.readLine());
        for(int i = 0; st.hasMoreTokens(); i++){
            lowerLimits[i] = Integer.parseInt(st.nextToken());
        }
        
        this.problem = new Problem(name, objectives, numberOfVariables, sizeOfPopulation, upperLimits, lowerLimits);
    }
    
    public void readIndividuo() throws IOException{     
        Solution sol = new Solution(this.problem);
        String individuoId = br.readLine();
        for(int i = 0; i < 5; i++){          
            this.st = new StringTokenizer(br.readLine());
            int [][] sets = new int [st.countTokens()/3][3];  
           
            for(int j = 0; st.hasMoreTokens(); j++){
                for (int k = 0; k < 3; k++){
                   sets[j][k] = Integer.parseInt(st.nextToken());
                } 
            }
            sol.setVariableValue(i, sets);
        }      
        this.problem.getSolutions().add(sol);
    }
    
    public void createPopulation(){
        for (int i = 1; i < this.problem.getNumberOfIndividuoss(); i++){
            Solution sol = (Solution) this.problem.createSolution();
            this.problem.getSolutions().add(sol);
        }
        System.out.println(this.problem.getSolutions());
    }
    
    
    
    
    
    

}
