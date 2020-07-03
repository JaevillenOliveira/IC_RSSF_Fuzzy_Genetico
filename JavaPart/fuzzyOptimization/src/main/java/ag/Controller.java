package ag;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jaevillen
 */
public class Controller {
    
    private BufferedReader br;
    private BufferedWriter bw;
    private StringTokenizer st;
    private Algorithm ga;
    private Problemfz pfz;

    public Controller() throws FileNotFoundException, IOException {
        this.br = this.br = new BufferedReader(new FileReader("/home/jaevillen/IC/Buffer/ConfigFile.txt"));
        this.bw = new BufferedWriter(new FileWriter("/home/jaevillen/IC/Buffer/ConfigFile.txt", true)); 
    }
    
     /**
     * Reads the problem's parameters from the file
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
        this.pfz = new Problemfz(name, objectives, numberOfVariables, upperLimits, lowerLimits);
        this.ga = new Algorithm(this.pfz, sizeOfPopulation);
    }
    
    /**
     * Reads the first subject of the file (the original one) the puts in the list of solutions
     * @throws IOException
     */
    public void readSubject() throws IOException{     
        ThreeDArrayDoubleSolution sol = new ThreeDArrayDoubleSolution((Problemfz) this.ga.getProblem());
        String subjectId = br.readLine(); //Is the name of the subject ("Individuo" + index)
        for(int i = 0; i < 5; i++){ // Reads the five variables of the subject (including the output)         
            Map<String,Double> limits = this.pfz.getVariablesLimits(i);
            this.st = new StringTokenizer(br.readLine());
            double [][] sets = new double [st.countTokens()/3][3]; // The sets are triangular, therefore contains three nodes 
           
            for(int j = 0; st.hasMoreTokens(); j++){
                for (int k = 0; k < 3; k++){
                   sets[j][k] = Double.parseDouble(st.nextToken());
                } 
            }
            sol.setVariableValue(i, sets);
            
            for(int j = 0; j < 3; j++){
                switch(j){
                    case 0:
                        sol.setAttribute(String.valueOf(i)+"0"+"2",this.pfz.constraintsToStr(limits.get("MiddleOfFirstHalf"), limits.get("Middle")));
                        break;
                    case 1:
                        double pointALimit = (sets[0][2] + limits.get("Min"))/2;
                        double pointCLimit = (limits.get("Max") + sets[2][0])/2;    
                        sol.setAttribute(String.valueOf(i)+"1"+"0", this.pfz.constraintsToStr(limits.get("Min"), pointALimit));
                        sol.setAttribute(String.valueOf(i)+"1"+"2", this.pfz.constraintsToStr(pointCLimit, limits.get("Max")));
                        sol.setAttribute(String.valueOf(i)+"1"+"1", this.pfz.constraintsToStr(pointALimit, pointCLimit));
                        break;
                    case 2:
                        sol.setAttribute(String.valueOf(i)+"2"+"0", this.pfz.constraintsToStr(limits.get("Middle"), limits.get("MiddleOfSecondHalf")));
                        break;
                }
            }
        }      
        this.ga.getPopulation().add(0, sol);    
        this.br.close();
    }
    
    /**
     * Writes the new population in the file   
     * @param solutionList the new population to write
     * @throws IOException
     */
    public void writePopulation(List solutionList) throws IOException{          
        Iterator it = solutionList.iterator();
        int subjectCounter = 1;
        while(it.hasNext()){
            ThreeDArrayDoubleSolution sol = (ThreeDArrayDoubleSolution) it.next();
            this.bw.write("Individuo" + " " + ++subjectCounter);
            for(int i = 0; i < sol.getNumberOfVariables(); i++){
                this.bw.newLine();
                double [][] sets = (double[][]) sol.getVariableValue(i);
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
    
    public Algorithm getGa() {
        return ga;
    }   
}
