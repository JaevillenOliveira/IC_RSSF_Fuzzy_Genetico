package ag;

import ag.problem.SetShape;
import ag.problem.Problemfz;
import ag.solution.FzArrayDoubleSolution;
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
    private int sizeOfPopulation;
    private double [] modelSubject = null;

    /**
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public Controller() throws FileNotFoundException, IOException {
        this.br = new BufferedReader(new FileReader("/home/jaevillen/IC/Buffer/ConfigFile.txt"));
        this.bw = new BufferedWriter(new FileWriter("/home/jaevillen/IC/Buffer/ConfigFile.txt", true)); 
    }
    
     /**
     * Reads the problem's parameters from the configuration file
     * @return the Problem object 
     * @throws IOException
     */
    public Problemfz readProblem() throws IOException{
        String name = br.readLine(); 
        int objectives = 0, numberOfVariables = 0, numberofSets = 0;
        SetShape setsType = null;
        
        /*The first line contains the number of objectives, the number of fuzzy variables, number of sets of each fuzzy variable,
          the shape of the sets, and the size of the initial population, the number of iterations.
        */
        this.st = new StringTokenizer(br.readLine());

        while(st.hasMoreTokens()){
            objectives = Integer.parseInt(st.nextToken()); //Reads the number of objectives
            numberOfVariables = Integer.parseInt(st.nextToken());
            numberofSets = Integer.parseInt(st.nextToken()); //Reads the number of sets that each variable has
            switch(st.nextToken()){ // Reads the shape of the sets (for now is implemented only for triangular sets)
                case "triangular":
                    setsType = SetShape.TRIANGULAR; //The set's shape defines, among other things, the number of points of the function
                    break;
                default:
                    break;
            }
            
            this.sizeOfPopulation = Integer.parseInt(st.nextToken()); // Reads the size of the initial population
        }

        ArrayList <Double> upperLimits = new ArrayList ();
        ArrayList <Double> lowerLimits = new ArrayList ();

        /*
        * The constraints for each point of each set of the variables will be calculated based on the 
        * model solution, that must be an uniform distributed configuration
        */
        switch(setsType){
            case TRIANGULAR:
                this.modelSubject = this.readModel(numberOfVariables, numberofSets, setsType.getNumPoints()); // Reads the model configuration from a file
                for(int i = 0; i < modelSubject.length; i+=setsType.getNumPoints()){  
                    double [] constraints = this.calculatelimitsTriangularSetsConstraints(modelSubject [i], modelSubject [i+1], modelSubject [i+2]); 
                    /*
                    * The lower and upper limits of the variables will be the constraints for each point of each set
                    */
                    lowerLimits.add(constraints[0]);
                    upperLimits.add(constraints[1]);
                    
                    lowerLimits.add(constraints[2]);
                    upperLimits.add(constraints[3]);
                    
                    lowerLimits.add(constraints[4]);
                    upperLimits.add(constraints[5]);
                }
                /*Creates a Problem object with the information read
                 * For this problem the Variables of a Solution will be the points of each set of each fuzzy variable
                */
                return new Problemfz(name, objectives, numberOfVariables*numberofSets*3, upperLimits, lowerLimits, numberofSets, setsType);
            default:
                break;
        }   
        return null;
    }
    
    /**
     * Uses the array containing the points of the model solution, read from a file, to create 
     * a Solution Object.
     * @param pfz the Problem
     * @return a FzArrayDoubleSolution as the model Solution
     */
    public FzArrayDoubleSolution createModelSolution(Problemfz pfz){
        FzArrayDoubleSolution fzs = new FzArrayDoubleSolution(pfz);
        int cnt = 0;
        for(double d : modelSubject){
            fzs.setVariableValue(cnt++, d);
        }
        return fzs;
    }
    
    /*
    * Calculates contraints for each point of each fuzy set 
    * based on the formula suggested by Cordón et all (2001). 
    */
    private double [] calculatelimitsTriangularSetsConstraints(double A, double B, double C){
        double [] limitPoints = new double [6]; 
        if(A == B){
            limitPoints[0] = A;  //AL
            limitPoints[1] = A; //AR

            limitPoints[2] = B; //BL
            limitPoints[3] = B; //BR
            
            limitPoints[4] = C - ((C-B)/2); //CL
            limitPoints[5] = C + ((C-B)/2); //CR
        }else if(B == C){
            limitPoints[0] = A - ((B-A)/2); //AL
            limitPoints[1] = A + ((B-A)/2); //AR

            limitPoints[2] = B; //BL
            limitPoints[3] = B; //BR

            limitPoints[4] = C; //CL
            limitPoints[5] = C; //CR
        }else{
            limitPoints[0] = A - ((B-A)/2); //AL
            if(limitPoints[0] < A)
                limitPoints[0] = A;
            limitPoints[1] = A + ((B-A)/2); //AR

            limitPoints[2] = B - ((B-A)/2); //BL
            limitPoints[3] = B + ((C-B)/2); //BR

            limitPoints[4] = C - ((C-B)/2); //CL
            limitPoints[5] = C + ((C-B)/2); //CR
            if(limitPoints[5] > C)
                limitPoints[5] = C;
        }
        return limitPoints;
    }
    
    /**
     * Reads the first subject from the file (the original one)
     * @param numberOfVariables
     * @param numberofSets
     * @param numberOfPoints
     * @return an array containing the points
     * @throws IOException
     */
    public double [] readModel(int numberOfVariables, int numberofSets, int numberOfPoints) throws IOException{     
        double [] model = new double [numberOfVariables*numberofSets*numberOfPoints];
        br.readLine(); //Is higher limits of the fuzzy variables (this is used only by the matlab script)
        br.readLine(); //Is lower limits of the fuzzy variables (this is used only by the matlab script)
        br.readLine(); //Is the name of the subject ("Individuo" + index)
        
        int index = 0;
        for(int i = 0; i < numberOfVariables; i++){ // Reads the five variables of the subject (including the output)         
            this.st = new StringTokenizer(br.readLine());    
            while(st.hasMoreTokens()){
                model[index++] = Double.parseDouble(st.nextToken());
            }
        }         
        this.br.close();
        return model;
    }

    public int getSizeOfPopulation() {
        return sizeOfPopulation;
    }
}