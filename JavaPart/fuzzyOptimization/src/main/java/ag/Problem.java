/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ag;

import java.util.List;
import org.uma.jmetal.problem.impl.AbstractGenericProblem;

/**
 *
 * @author jaevillen
 */
public class Problem extends AbstractGenericProblem{

    private List <Solution> solutions;
    private List <Double> lowerLimits;
    private List <Double> upperLimits;
    
    public Problem(String name, int numberOfConstraints, int numberOfObjectives, int numberOfVariables, List <Double> upperLimits, List <Double> lowerLimits) {
        this.setName(name);
        this.setNumberOfConstraints(numberOfConstraints);
        this.setNumberOfObjectives(numberOfObjectives);
        this.setNumberOfVariables(numberOfVariables);
        this.setUpperLimits(upperLimits);
        this.setLowerLimits(lowerLimits);
    }
    
    @Override
    public void evaluate(Object s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object createSolution() {
        Solution sol = new Solution(this);
        for(int i = 0; i < this.getNumberOfVariables(); i++){
            
        }
 
       return sol;
    }

    public List<Double> getLowerLimits() {
        return lowerLimits;
    }

    private void setLowerLimits(List<Double> lowerLimits) {
        this.lowerLimits = lowerLimits;
    }

    public List<Double> getUpperLimits() {
        return upperLimits;
    }

    private void setUpperLimits(List<Double> upperLimits) {
        this.upperLimits = upperLimits;
    }
    

    
}
