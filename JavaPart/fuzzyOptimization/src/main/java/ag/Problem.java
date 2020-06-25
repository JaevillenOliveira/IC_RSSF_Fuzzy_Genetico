/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ag;

import java.util.Random;
import org.uma.jmetal.problem.impl.AbstractGenericProblem;
import org.uma.jmetal.solution.impl.AbstractGenericSolution;

/**
 * 
 * @author jaevillen
 */
public final class Problem extends AbstractGenericProblem{

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
    public Problem(String name, int numberOfObjectives, int numberOfVariables, Integer [] upperLimits, Integer [] lowerLimits) {
        this.setName(name);
        this.setNumberOfObjectives(numberOfObjectives);
        this.setNumberOfVariables(numberOfVariables);
        this.setUpperLimits(upperLimits);
        this.setLowerLimits(lowerLimits);
        this.rdm = new Random();
        
    }
    
    @Override
    public void evaluate(Object s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    /**
     *
     * @return
     */
    @Override
    public Object createSolution() {
        ThreeDArrayDoubleSolution sol = new ThreeDArrayDoubleSolution(this);
        for(int i = 0; i < this.getNumberOfVariables(); i++){
            sol.setVariableValue(i, this.createSets(i));
        } 
        return sol;
    }
    
    /**
     *
     * @param variableIndex
     * @return
     */
    public double [][] createSets(int variableIndex){
        double [][] sets = new double [3][3]; //For this problem is already known that the variables have three triangular shaped sets
        int min = this.getLowerLimits()[variableIndex]; //Variable's Lower limit
        int max = this.getUpperLimits()[variableIndex]; //Variable's Upper limit
        int middle = (min+max)/2; //Variable's middle value
        int middleOfFirstHalf = (min+middle)/2; 
        int middleOfSecondHalf = (middle+max)/2; 
        
        /*
            SET 1
        */
        
        double tempLimit = this.calculateLimitToSeed(middleOfFirstHalf, middle);
        
        sets[0][0] = min;
        sets[0][1] = min;
        sets[0][2] = this.rdm.nextDouble()*tempLimit + middleOfFirstHalf;

        /*
            SET 3
        */
        tempLimit = this.calculateLimitToSeed(middle, middleOfSecondHalf); 
        
        sets[2][0] = this.rdm.nextDouble()*tempLimit + middle;
        sets[2][1] = max;
        sets[2][2] = max;

        /*
            SET 2
        */

        double pointALimit = (sets[0][2] + min)/2;
        tempLimit = this.calculateLimitToSeed(min, pointALimit);                
        sets[1][0] = this.rdm.nextDouble()*tempLimit + min;
        
        double pointCLimit = (max + sets[2][0])/2;    
        tempLimit = this.calculateLimitToSeed(pointCLimit, max);
        sets[1][2] = this.rdm.nextDouble()*tempLimit + pointCLimit;
        
        tempLimit = this.calculateLimitToSeed(pointALimit, pointCLimit);
        sets[1][1] = this.rdm.nextDouble()*tempLimit + pointALimit;

        return sets;
    }
    
    // Calculates a number to pass to the random generator as a way to generate a value between two limits
    private double calculateLimitToSeed(double min, double max){
        return (max - min);
    }
    
    /**
     *
     * @return
     */
    public Integer [] getLowerLimits() {
        return lowerLimits;
    }

    private void setLowerLimits(Integer [] lowerLimits) {
        this.lowerLimits = lowerLimits;
    }

    /**
     *
     * @return
     */
    public Integer [] getUpperLimits() {
        return upperLimits;
    }

    /**
     *
     * @param upperLimits
     */
    public void setUpperLimits(Integer [] upperLimits) {
        this.upperLimits = upperLimits;
    }
}
