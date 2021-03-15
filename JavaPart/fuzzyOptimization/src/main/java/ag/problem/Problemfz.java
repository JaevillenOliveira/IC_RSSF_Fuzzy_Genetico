 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ag.problem;

import ag.solution.FzArrayDoubleSolution;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

/**
 * 
 * @author jaevillen
 */
public final class Problemfz extends AbstractDoubleProblem{

    private int numberOfSets;
    private SetShape setShape;
    private Random rdm;
    private BufferedWriter bw;
    private BufferedReader br;
    private StringTokenizer st;
    private int scenarioId;
    private String simFilePath;
    private String bufferPath;
    
    /**
     *
     * @param name the name of the problem
     * @param numberOfObjectives   
     * @param numberOfVariables
     * @param upperLimits
     * @param lowerLimits
     * @param numberOfSets
     * @param setShape
     */
    public Problemfz(String name, int numberOfObjectives, int numberOfVariables, 
                        ArrayList<Double> upperLimits, ArrayList<Double> lowerLimits, 
                        int numberOfSets, SetShape setShape, int scenarioId, String bufferPath, String simFilePath) {
        this.setName(name);
        this.setNumberOfObjectives(numberOfObjectives);
        this.setNumberOfVariables(numberOfVariables);
        this.setLowerLimit(lowerLimits);
        this.setUpperLimit(upperLimits);
        this.numberOfSets = numberOfSets;
        this.setShape = setShape;
        this.rdm = new Random();
        this.scenarioId = scenarioId;
        this.bufferPath = bufferPath;
        this.simFilePath = simFilePath;
    }
 
    /**
     * Evaluates the solutions 
     * @param s the solution to be evaluated
     */
    @Override
    public void evaluate(DoubleSolution s) {
        try {
            this.writeSolution(s, this.bufferPath+"TempSolution.txt");
            
            ProcessBuilder processBuilder = new ProcessBuilder(this.simFilePath, String.valueOf(this.scenarioId));
            //processBuilder.inheritIO();
            processBuilder.redirectOutput(new File(this.bufferPath+"log.txt"));
            Process process = processBuilder.start();
            process.waitFor(); //Waits for the simulation to finish
            
            double energyConsumed = 0;
            energyConsumed += this.getEnergyConsumed(this.bufferPath+"power_consumption_sc"+String.valueOf(this.scenarioId)+".txt");
            
            System.out.println("Subject evaluated: "+String.valueOf(energyConsumed));
            s.setObjective(0, energyConsumed);       
            System.out.println("Evaluated " + s.getObjective(0));
//            System.exit(0);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(Problemfz.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private double getEnergyConsumed(String filePath) throws FileNotFoundException, IOException{
        this.br = new BufferedReader(new FileReader(filePath));
        this.st = new StringTokenizer(br.readLine().replaceAll("[{,\\\\:\"}]", ""));
        double energyConsumed = 0;
        while(st.hasMoreTokens()){
            st.nextToken(); //Reads the title
            energyConsumed += Double.parseDouble(st.nextToken());
        }
        return energyConsumed;
    }

    /**
     * Writes a singular solution into a file 
     * 
     * @param s the solution
     * @param filePath the file path
     * @throws IOException
     */

    public void writeSolution(DoubleSolution s, String filePath) throws IOException{
        this.bw = new BufferedWriter(new FileWriter(filePath));
        int v = this.getNumberOfSets() * this.getSetshape().getNumPoints();
        int index = 0;
        for(int i = 0; i < s.getNumberOfVariables(); i+=v){
            this.bw.newLine();
            for (int j = 0; j < v; j++){
                double point = s.getVariableValue(index++);
                this.bw.write(point + " ");
            }
        }
        this.bw.close();
    }


    /**
     * Creates a solution with the specified number of variables, and respecting the problem's parameters.
     * Each variable of a Solution is a point of a set of that Fuzzy Variable.
     * -FuzzyVariables (Three triangular sets in each one - Low, Medium, High)
     *    -RSSI
     *    -NumberOfNeighbors
     *    -NumberOfSources
     *    -Throughput
     *    -SwitchPercentual
     * 
     * In this case the Solution Object will have 45 variables, 
     * as each fuzzy variable has 9 points (3 sets, each one with 3 points) 
     * So: 5 Fuzzy Variables * 3 Sets * 3 Points
     * 
     * @return the new solution
     */

    @Override
    public DoubleSolution createSolution() {
        FzArrayDoubleSolution sol = new FzArrayDoubleSolution(this);
        int j = 0;
        switch(this.setShape){
            case TRIANGULAR:
                int arraySize = this.numberOfSets * this.setShape.getNumPoints();
                for(int i = 0; i < this.getNumberOfVariables(); i+=arraySize){
                    double [] setsPoints = this.createSets(arraySize,this.setShape.getNumPoints(), i);
                    for(double d : setsPoints){
                        sol.setVariableValue(j++, d);
                    }
                } 
                return sol; 
            default:
                break;
        }
        return null;
    }
    

    /*
    * Creates Sets for one Fuzzy Variable respecting the limits defined for each point
    */
    private double [] createSets(int arraySize, int numFcnPoints, int index){
        double [] sets = new double [arraySize];
        
        for(int i = 0; i < arraySize; i+=numFcnPoints){  
            for(int j = 0; j < numFcnPoints; j++){
                //This conditional treats the case of having three or more variables crossing each other
                if(i >= (numFcnPoints*2) && this.getLowerBound(index+i+j) < sets[i+j-numFcnPoints-1]){
                    double lowerBound = sets[i+j-numFcnPoints-1];
                    sets [i+j] = this.generateRdmPoint(lowerBound, this.getUpperBound(index+i+j));
                }else{
                    sets [i+j] = this.generateRdmPoint(this.getLowerBound(index+i+j), this.getUpperBound(index+i+j));
                }
            }
        }
        return sets;
    }


    /**
     * Calculates a random value in a specific range
     * @param min
     * @param max
     * @return
     */
    public double generateRdmPoint(double min, double max){
        return (min + this.rdm.nextDouble()*(max - min));
    }
    
    public int getNumberOfSets() {
        return numberOfSets;
    }

    public SetShape getSetshape() {
        return setShape;
    }
}
