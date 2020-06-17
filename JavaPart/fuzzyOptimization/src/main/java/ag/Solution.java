/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ag;

import java.util.Map;
import java.util.Random;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.impl.AbstractGenericSolution;

/**
 *
 * @author jaevillen
 */
public class Solution extends AbstractGenericSolution{
    
    public Solution(Problem problem) {
        super(problem);
    }

    @Override
    public String getVariableValueString(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public org.uma.jmetal.solution.Solution copy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map getAttributes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
   
}
