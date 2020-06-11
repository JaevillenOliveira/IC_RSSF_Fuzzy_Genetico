/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ag;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.uma.jmetal.problem.impl.AbstractGenericProblem;

/**
 * 
 * @author jaevillen
 */
public final class Problem extends AbstractGenericProblem{

    private ArrayList <Solution> solutions;
    private Integer [] lowerLimits;
    private Integer [] upperLimits;
    private int numberOfIndividuos;
    private Random rdm;
    
    /**
     *
     * @param name the name of the problem
     * @param numberOfObjectives   
     * @param numberOfVariables
     * @param numberOfIndividuos
     * @param upperLimits
     * @param lowerLimits
     */
    public Problem(String name, int numberOfObjectives, int numberOfVariables, int numberOfIndividuos, Integer [] upperLimits, Integer [] lowerLimits) {
        this.setName(name);
        this.setNumberOfObjectives(numberOfObjectives);
        this.setNumberOfVariables(numberOfVariables);
        this.setNumberOfIndividuos(numberOfIndividuos);
        this.setUpperLimits(upperLimits);
        this.setLowerLimits(lowerLimits);
        this.rdm = new Random();
        this.solutions = new ArrayList <Solution> ();
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
     *
     * @return
     */
    @Override
    public Object createSolution() {
        Solution sol = new Solution(this);
        for(int i = 0; i < this.getNumberOfVariables(); i++){
            sol.setVariableValue(i, this.createSets(i));
        } 
        return sol;
    }
    
    /**
     *
     * @param numberOfSets
     * @param variableIndex
     * @return
     */
    public int [][] createSets(int variableIndex){
        int [][] sets = new int [3][3];
        int min = this.getLowerLimits()[variableIndex];
        int max = this.getUpperLimits()[variableIndex];
        int mean = (min+max)/2;
        
        int tempLimit = this.calculateLimitToSeed((min+max)/2, max);
        
        sets[0][0] = min;
        sets[0][1] = min;
        sets[0][2] = this.rdm.nextInt(tempLimit) + min;
        
        sets[1][0] = this.rdm.nextInt(tempLimit) + min;
        sets[1][2] = this.rdm.nextInt(tempLimit) + mean;
        int tempInBetween = this.calculateLimitToSeed(sets[1][0], sets[1][2]);
        sets[1][1] = this.rdm.nextInt(tempInBetween) + sets[1][0];
        
        sets[2][0] = this.rdm.nextInt(tempLimit) + mean;
        sets[2][1] = max;
        sets[2][2] =  max;
       
        return sets;
    }
    
    private int calculateLimitToSeed(int min, int max){
        if (min < 0 && max < 0){
            return ((max - min) - 1);
        }
        return ((max - min) + 1);
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
    
    /**
     *
     * @return
     */
    public List<Solution> getSolutions() {
        return solutions;
    }

    /**
     *
     * @param solutions
     */
    public void setSolutions(ArrayList<Solution> solutions) {
        this.solutions = solutions;
    }

    /**
     *
     * @return
     */
    public int getNumberOfIndividuoss() {
        return numberOfIndividuos;
    }

    /**
     *
     * @param numberOfIndividuos
     */
    public void setNumberOfIndividuos(int numberOfIndividuos) {
        this.numberOfIndividuos = numberOfIndividuos;
    }
    

    
}
