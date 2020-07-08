/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ag;

import java.io.BufferedWriter;
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
    private StringTokenizer st;

    
    /**
     *
     * @param name the name of the problem
     * @param numberOfObjectives   
     * @param numberOfVariables
     * @param upperLimits
     * @param lowerLimits
     * @param numberOfSets
     * @param setType
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
 

    @Override
    public void evaluate(DoubleSolution s) {
        try {
            System.out.println("It's here");
            this.writeTriangSolution(s);
            Runtime.getRuntime().exec("/home/jaevillen/IC/OmnetPart/WSN/src/networktopology/runSimulation.sh");
        } catch (IOException ex) {
            Logger.getLogger(Problemfz.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
    private void writeTriangSolution(DoubleSolution s) throws IOException{
        this.bw = new BufferedWriter(new FileWriter("/home/jaevillen/IC/Buffer/TempSolution.txt"));
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
     * Create a solution with the specified number of variables, and respecting the problem's parameters 
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
    
    private double [] createTriangularSets(int arraySize, int numFcnPoints, int index){
        double [] sets = new double [arraySize];
        
        for(int i = 0; i < arraySize; i+=numFcnPoints){  
            for(int j = 0; j < numFcnPoints; j++){
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
    
    // Calculates a number to pass to the random generator as a way to generate a value between two limits
    public double generateRdmPoint(double min, double max){
        return (min + this.rdm.nextDouble()*(max - min));
    }
    
    /**
     * Makes a String specifying the constraints of one set's point: the extreme values of the range
     * @param inferiorLimit
     * @param superiorLimit
     * @return the string  specifying the constraints
     */
    public String constraintsToStr(double inferiorLimit, double superiorLimit){
        return (String.valueOf(inferiorLimit)+" "+String.valueOf(superiorLimit));
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
    
    public int getNumberOfSets() {
        return numberOfSets;
    }

    public void setNumberOfSets(int numberOfSets) {
        this.numberOfSets = numberOfSets;
    }


    public SetShape getSetshape() {
        return setShape;
    }

    public void setSetshape(SetShape Setshape) {
        this.setShape = Setshape;
    }
}
