/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ag;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.impl.ArrayDoubleSolution;

/**
 *
 * @author jaevillen
 */
public class FzArrayDoubleSolution extends ArrayDoubleSolution implements Cloneable{

    public FzArrayDoubleSolution(DoubleProblem problem) {
        super(problem);
    }


    @Override
    public String getVariableValueString(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public org.uma.jmetal.solution.Solution copy() {
        try {
            return (Solution) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(FzArrayDoubleSolution.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Map getAttributes() {
        return this.attributes;
    }
   
}
