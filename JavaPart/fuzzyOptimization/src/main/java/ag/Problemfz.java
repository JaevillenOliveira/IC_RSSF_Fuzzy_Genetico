/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.uma.jmetal.problem.impl.AbstractGenericProblem;

/**
 * 
 * @author jaevillen
 */
public final class Problemfz extends AbstractGenericProblem{

    private Integer [] lowerLimits;
    private Integer [] upperLimits;
    private Random rdm;
    
    /**
     *
     * @param name the name of the problem
     * @param numberOfObjectives   
     * @param numberOfVariables
     * @param upperLimits
     * @param lowerLimits
     */
    public Problemfz(String name, int numberOfObjectives, int numberOfVariables, Integer [] upperLimits, Integer [] lowerLimits) {
        this.setName(name);
        this.setNumberOfObjectives(numberOfObjectives);
        this.setNumberOfVariables(numberOfVariables);
        this.setUpperLimits(upperLimits);
        this.setLowerLimits(lowerLimits);
        this.rdm = new Random();
        
    }
 
    /**
     *
     * @param s
     */
    @Override
    public void evaluate(Object s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    /**
     * Create a solution with the specified number of variables, and respecting the problem's parameters 
     * @return the new solution
     */
    @Override
    public Object createSolution() {
        ThreeDArrayDoubleSolution sol = new ThreeDArrayDoubleSolution(this);
        for(int i = 0; i < this.getNumberOfVariables(); i++){
            sol.setVariableValue(i, this.createSets(i, sol));
            //System.out.println(sol.getAttribute(String.valueOf(i)+String.valueOf(0)+String.valueOf(2))); 
        } 
        return sol;
    }

    /*
    
    * This method is specific for a problem with fuzzy variables with three triangular sets.
    
    * Creates sets dinamically, respecting the range permitted for each point of each set, so that
    * the sets keep their interpretability.
    
    * This also put the limits for each point of each set into a hash map, and its keys are 
    * strings specifying the 'variableIndex' (0, 1, 2, 3 or 4), 'set' (0, 1 or 2), 'pointOfSet' (0, 1 or 2). 
    
    */
    
    private double [][] createSets(int variableIndex, ThreeDArrayDoubleSolution sol){
        double [][] sets = new double [3][3]; //For this problem is already known that the variables have three triangular shaped sets
        Map<String,Double> limits = this.getVariablesLimits(variableIndex);
        
        /*
            SET 1
        */
        //Key: VariableIndex, set, point; Value: InferiorLimit, SuperiorLimit
        sol.setAttribute(String.valueOf(variableIndex)+"0"+"2",this.constraintsToStr(limits.get("MiddleOfFirstHalf"), limits.get("Middle")));
      
        sets[0][0] = limits.get("Min");
        sets[0][1] = limits.get("Min");
        sets[0][2] = this.generateRdmPoint(limits.get("MiddleOfFirstHalf"), limits.get("Middle"));
       

        /*
            SET 3
        */
        sol.setAttribute(String.valueOf(variableIndex)+"2"+"0", this.constraintsToStr(limits.get("Middle"), limits.get("MiddleOfSecondHalf")));

        sets[2][0] = this.generateRdmPoint(limits.get("Middle"), limits.get("MiddleOfSecondHalf")); 
        sets[2][1] = limits.get("Max");
        sets[2][2] = limits.get("Max");

        /*
            SET 2
        */
              
        double pointALimit = (sets[0][2] + limits.get("Min"))/2;
        sol.setAttribute(String.valueOf(variableIndex)+"1"+"0", this.constraintsToStr(limits.get("Min"), pointALimit));               
        sets[1][0] = this.generateRdmPoint(limits.get("Min"), pointALimit);
        
        double pointCLimit = (limits.get("Max") + sets[2][0])/2;    
        sol.setAttribute(String.valueOf(variableIndex)+"1"+"2", this.constraintsToStr(pointCLimit, limits.get("Max")));
        sets[1][2] = this.generateRdmPoint(pointCLimit, limits.get("Max"));
        
        sol.setAttribute(String.valueOf(variableIndex)+"1"+"1", this.constraintsToStr(pointALimit, pointCLimit));
        sets[1][1] = this.generateRdmPoint(pointALimit, pointCLimit);

        return sets;
    }
    
    // Calculates a number to pass to the random generator as a way to generate a value between two limits
    private double generateRdmPoint(double min, double max){
        return (min + this.rdm.nextDouble()*(max - min));
    }
    
    /**
     * Makes a String specifying the constraints of one set's point: the xtreme values of the range
     * @param inferiorLimit
     * @param superiorLimit
     * @return the string  specifying the constraints
     */
    public String constraintsToStr(double inferiorLimit, double superiorLimit){
        return (String.valueOf(inferiorLimit)+" "+String.valueOf(superiorLimit));
    }
    
    /**
     * Calculates values (as the middle point, and others) from the variables's universe and puts them 
     * into a hash map
     * @param variableIndex the variable's index
     * @return a hash map with the calculated values
     * 
     * @see Keys of the hash map: 
     *  @Min the lower limit of the universe
     *  @Max the higher limit of the universe
     *  @Middle the middle of the universe
     *  @MiddleOfFirstHalf the middle value between the @Min and the @Middle
     *  @MiddleOfSecondHalf the middle value between the @Middle and the @Max
     * 
     */
    public Map getVariablesLimits(int variableIndex){
        Map <String,Double> limits = new HashMap<>();
        double min = this.getLowerLimits()[variableIndex]; //Variable's Lower limit (min)
        double max = this.getUpperLimits()[variableIndex]; //Variable's Upper limit
        double middle = (min+max)/2; //Variable's middle value
        double middleOfFirstHalf = (min+middle)/2; 
        double middleOfSecondHalf = (middle+max)/2; 
        
        limits.put("Min", min);
        limits.put("Max", max);
        limits.put("Middle",middle);
        limits.put("MiddleOfFirstHalf",middleOfFirstHalf);
        limits.put("MiddleOfSecondHalf",middleOfSecondHalf);
        
        return limits;
    }

    public Integer [] getLowerLimits() {
        return lowerLimits;
    }

    public Integer [] getUpperLimits() {
        return upperLimits;
    }
    
    public void setLowerLimits(Integer[] lowerLimits) {
        this.lowerLimits = lowerLimits;
    }

    public void setUpperLimits(Integer[] upperLimits) {
        this.upperLimits = upperLimits;
    }
}
