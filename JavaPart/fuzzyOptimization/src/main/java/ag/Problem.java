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
            List list = this.createSets(i);
            sol.setVariableValue(i, list.get(0));
            sol.getAttributes().putAll((Map) list.get(1));
            
        } 
        return sol;
    }
    
    /**
     *
     * @param variableIndex
     * @param map
     * @return
     */
    public List createSets(int variableIndex){
        double [][] sets = new double [3][3]; //For this problem is already known that the variables have three triangular shaped sets
        int min = this.getLowerLimits()[variableIndex]; //Variable's Lower limit
        int max = this.getUpperLimits()[variableIndex]; //Variable's Upper limit
        int middle = (min+max)/2; //Variable's middle value
        int middleOfFirstHalf = (min+middle)/2; 
        int middleOfSecondHalf = (middle+max)/2; 
        Map map = new HashMap();
        
        /*
            SET 1
        */
        //Variable index, set, point (A, B, or C)
        map.put(String.valueOf(variableIndex)+"0"+"2",this.limitsToStr(middleOfFirstHalf, middle));
      
        sets[0][0] = min;
        sets[0][1] = min;
        sets[0][2] = this.generateRdmPoint(middleOfFirstHalf, middle);
       

        /*
            SET 3
        */
        map.put(String.valueOf(variableIndex)+"2"+"0", this.limitsToStr(middle, middleOfSecondHalf));

        sets[2][0] = this.generateRdmPoint(middle, middleOfSecondHalf); 
        sets[2][1] = max;
        sets[2][2] = max;

        /*
            SET 2
        */
              
        double pointALimit = (sets[0][2] + min)/2;
        map.put(String.valueOf(variableIndex)+"1"+"0", this.limitsToStr(min, pointALimit));               
        sets[1][0] = this.generateRdmPoint(min, pointALimit);
        
        double pointCLimit = (max + sets[2][0])/2;    
        map.put(String.valueOf(variableIndex)+"1"+"2", this.limitsToStr(pointCLimit, max));
        sets[1][2] = this.generateRdmPoint(pointCLimit, max);
        
        map.put(String.valueOf(variableIndex)+"1"+"1", this.limitsToStr(pointALimit, pointCLimit));
        sets[1][1] = this.generateRdmPoint(pointALimit, pointCLimit);

        //System.out.println((String) map.get(String.valueOf(variableIndex)+String.valueOf(1)+String.valueOf(2)));
        List list = new ArrayList();
        
        list.add(sets);
        list.add(map);
        return list;
    }
    
    // Calculates a number to pass to the random generator as a way to generate a value between two limits
    private double generateRdmPoint(double min, double max){
        return (min + this.rdm.nextDouble()*(max - min));
    }
    
    private String limitsToStr(double inferiorLimit, double superiorLimit){
        return (String.valueOf(inferiorLimit)+" "+String.valueOf(superiorLimit));
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
