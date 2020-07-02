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
            sol.setVariableValue(i, this.createSets(i, sol));
            //System.out.println(sol.getAttribute(String.valueOf(i)+String.valueOf(0)+String.valueOf(2))); 
        } 
        return sol;
    }
    
    /**
     *
     * @param variableIndex
     * @param sol
     * @return
     */
    public double [][] createSets(int variableIndex, ThreeDArrayDoubleSolution sol){
        double [][] sets = new double [3][3]; //For this problem is already known that the variables have three triangular shaped sets
        Map<String,Double> limits = this.getVariablesLimits(variableIndex);
        
        /*
            SET 1
        */
        //Variable index, set, point (A, B, or C)
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

        //System.out.println((String) map.get(String.valueOf(variableIndex)+String.valueOf(1)+String.valueOf(2)));
        return sets;
    }
    
    // Calculates a number to pass to the random generator as a way to generate a value between two limits
    private double generateRdmPoint(double min, double max){
        return (min + this.rdm.nextDouble()*(max - min));
    }
    
    
    public String constraintsToStr(double inferiorLimit, double superiorLimit){
        return (String.valueOf(inferiorLimit)+" "+String.valueOf(superiorLimit));
    }
    
    public Map getVariablesLimits(int variableIndex){
        Map <String,Double> limits = new HashMap<String,Double>();
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
