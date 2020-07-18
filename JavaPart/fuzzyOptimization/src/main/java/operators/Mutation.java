/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operators;

import org.uma.jmetal.operator.impl.mutation.SimpleRandomMutation;

/**
 *
 * @author jaevillen
 */
public class Mutation extends SimpleRandomMutation{
    
    public Mutation(double probability) {
        super(probability);
    }
    
}
