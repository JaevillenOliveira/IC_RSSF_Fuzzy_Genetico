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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
    public Problemfz(String name, int numberOfObjectives, int numberOfVariables, ArrayList<Double> upperLimits, ArrayList<Double> lowerLimits, int numberOfSets, SetShape setShape) {
        this.setName(name);
        this.setNumberOfObjectives(numberOfObjectives);
        this.setNumberOfVariables(numberOfVariables);
        this.setLowerLimit(lowerLimits);
        this.setUpperLimit(upperLimits);
        this.setNumberOfSets(numberOfSets);
        this.setSetshape(setShape);
        this.rdm = new Random();
        
    }
 
    /**
     *
     * @param s
     */
    @Override
    public void evaluate(DoubleSolution s) {
        try {
            System.out.println("It's here");
            this.writeTriangSolution(s, "/home/jaevillen/IC/Buffer/TempSolution.txt");
            ProcessBuilder processBuilder = new ProcessBuilder("/home/jaevillen/IC/OmnetPart/WSN/src/networktopology/runSimulation.sh");
            processBuilder.inheritIO();
            Process process = processBuilder.start();
            process.waitFor();
            
            System.out.println("It has finished");
            HashMap resultSc1 = this.readSolutionResult("/home/jaevillen/IC/Buffer/power_consumption_sc1.txt");
            HashMap resultSc2 = this.readSolutionResult("/home/jaevillen/IC/Buffer/power_consumption_sc2.txt");
            Iterator it = resultSc1.values().iterator();
            while(it.hasNext()){
                System.out.println(it.next());
            }
            
            System.exit(0);
        } catch (IOException ex) {
            Logger.getLogger(Problemfz.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Problemfz.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private HashMap readSolutionResult(String filePath) throws FileNotFoundException, IOException{
        HashMap <String, Double> energyConsump = new HashMap();
        this.br = this.br = new BufferedReader(new FileReader(filePath));
        this.st = new StringTokenizer(br.readLine().replaceAll("[{\\\\:\"}]", ""));
        while(st.hasMoreTokens()){
            energyConsump.put(st.nextToken(), Double.parseDouble(st.nextToken()));
        }

        return energyConsump;
    }
    
    /*
    * Writes a singular solution into a file that will be read by the fuzzy controller (matlab)
    * to be used in a simulation (Omnet++)
    */
    private void writeTriangSolution(DoubleSolution s, String filePath) throws IOException{
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
                    double [] setsPoints = this.createTriangularSets(arraySize,this.setShape.getNumPoints(), i);
                    for(double d : setsPoints){
                        sol.setVariableValue(j, d);
                        j++;
                    }
                    //System.out.println(sol.getAttribute(String.valueOf(i)+String.valueOf(0)+String.valueOf(2))); 
                } 
                return sol; 
            default:
                break;
        }
        return null;
    }
    

    /*
    
    * This method is specific for a problem with fuzzy variables with three triangular sets.
    
    * Creates sets dinamically, respecting the range permitted for each point of each set, so that
    * the sets keep their interpretability.
    
    * This also put the limits for each point of each set into a hash map, and its keys are 
    * strings specifying the 'variableIndex' (0, 1, 2, 3 or 4), 'set' (0, 1 or 2), 'pointOfSet' (0, 1 or 2). 
    
    */
    
    /*
    * Creates Triangular Sets for one Fuzzy Variable
    */
    private double [] createTriangularSets(int arraySize, int numFcnPoints, int index){
        double [] sets = new double [arraySize];
        
        for(int i = 0; i < arraySize; i+=numFcnPoints){  
            for(int j = 0; j < numFcnPoints; j++){
                //This conditional treats the case of having three or more variables crossing each other
                if(i >= (numFcnPoints*2) && this.getLowerBound(index+i+j) < sets[i-numFcnPoints-1]){
                    double lowerBound = sets[i-numFcnPoints-1];
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
    
    
//    /**
//     * Calculates values (as the middle point, and others) from the variables' universe and puts them 
//     * into a hash map
//     * @param variableIndex the variable's index
//     * @return a hash map with the calculated values
//     * 
//     * @see Keys of the hash map: 
//     *  @Min the lower limit of the universe
//     *  @Max the higher limit of the universe
//     *  @Middle the middle of the universe
//     *  @MiddleOfFirstHalf the middle value between the @Min and the @Middle
//     *  @MiddleOfSecondHalf the middle value between the @Middle and the @Max
//     * 
//     */
//    public Map getVariablesLimits(int variableIndex){
//        Map <String,Double> limits = new HashMap<>();
//        double min = this.getLowerLimits()[variableIndex]; //Variable's Lower limit (min)
//        double max = this.getUpperLimits()[variableIndex]; //Variable's Upper limit
//        double middle = (min+max)/2; //Variable's middle value
//        double middleOfFirstHalf = (min+middle)/2; 
//        double middleOfSecondHalf = (middle+max)/2; 
//        
//        limits.put("Min", min);
//        limits.put("Max", max);
//        limits.put("Middle",middle);
//        limits.put("MiddleOfFirstHalf",middleOfFirstHalf);
//        limits.put("MiddleOfSecondHalf",middleOfSecondHalf);
//        
//        return limits;
//    }

    /**
     *
     * @return
     */
    
    public int getNumberOfSets() {
        return numberOfSets;
    }

    /**
     *
     * @param numberOfSets
     */
    public void setNumberOfSets(int numberOfSets) {
        this.numberOfSets = numberOfSets;
    }

    /**
     *
     * @return
     */
    public SetShape getSetshape() {
        return setShape;
    }

    /**
     *
     * @param Setshape
     */
    public void setSetshape(SetShape Setshape) {
        this.setShape = Setshape;
    }
}
