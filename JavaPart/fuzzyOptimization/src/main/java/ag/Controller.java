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
    private double [] modelSubject = null;

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
        int objectives = 0, numberOfVariables = 0, numberofSets = 0, sizeOfPopulation = 0;
        SetShape setsType = null;

        this.st = new StringTokenizer(br.readLine());
        /*The first line contains the number of objectives, the number of variables, and the size of the population
          that must be created
        */
        while(st.hasMoreTokens()){
            objectives = Integer.parseInt(st.nextToken());
            numberOfVariables = Integer.parseInt(st.nextToken());
            numberofSets = Integer.parseInt(st.nextToken());
            switch(st.nextToken()){
                case "triangular":
                    setsType = SetShape.TRIANGULAR;
                    break;
                default:
                    break;
            }
            
            sizeOfPopulation = Integer.parseInt(st.nextToken());
        }

        ArrayList <Double> upperLimits = new ArrayList ();
        ArrayList <Double> lowerLimits = new ArrayList ();

        switch(setsType){
            case TRIANGULAR:
                this.modelSubject = this.readModel(numberOfVariables, numberofSets, 3);
                for(int i = 0; i < modelSubject.length; i+=3){  
                    double [] constraints = this.calculatelimitsTriangularSetsConstraints(modelSubject [i], modelSubject [i+1], modelSubject [i+2]);
                    lowerLimits.add(constraints[0]);
                    upperLimits.add(constraints[1]);
                    
                    lowerLimits.add(constraints[2]);
                    upperLimits.add(constraints[3]);
                    
                    lowerLimits.add(constraints[4]);
                    upperLimits.add(constraints[5]);
                }
                //Creates a Problem object with the information read
                this.pfz = new Problemfz(name, objectives, numberOfVariables*numberofSets*3, upperLimits, lowerLimits, numberofSets, setsType);
                break;
            default:
                break;
        }       
        this.ga = new Algorithm(this.pfz, sizeOfPopulation);
    }
    
    public FzArrayDoubleSolution createModelSolution(){
        FzArrayDoubleSolution fzs = new FzArrayDoubleSolution(this.pfz);
        int cnt = 0;
        for(double d : modelSubject){
            fzs.setVariableValue(cnt++, d);
        }
        return fzs;
    }
    
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
     * Reads the first subject of the file (the original one) the puts in the list of solutions
     * @param numberOfVariables
     * @param numberofSets
     * @param numberOfPoints
     * @return 
     * @throws IOException
     */
    public double [] readModel(int numberOfVariables, int numberofSets, int numberOfPoints) throws IOException{     
        double [] model = new double [numberOfVariables*numberofSets*numberOfPoints];
        br.readLine();
        br.readLine();
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
    
    /**
     * Writes the new population in the file   
     * @param solutionList the new population to write
     * @throws IOException
     */
    public void writePopulation(List solutionList) throws IOException{          
        Iterator it = solutionList.iterator();
        int subjectCounter = 1;
        while(it.hasNext()){
            FzArrayDoubleSolution sol = (FzArrayDoubleSolution) it.next();
            this.bw.write("Individuo" + " " + ++subjectCounter);
            int v = this.pfz.getNumberOfSets() * 3;
            int index = 0;
            for(int i = 0; i < sol.getNumberOfVariables(); i+=v){
                this.bw.newLine();
                for (int j = 0; j < v; j++){
                    double point = sol.getVariableValue(index++);
                    this.bw.write(point + " ");
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
