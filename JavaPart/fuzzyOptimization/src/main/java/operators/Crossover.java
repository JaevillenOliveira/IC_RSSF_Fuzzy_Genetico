/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operators;

import org.uma.jmetal.operator.impl.crossover.BLXAlphaCrossover;

/**
 *
 * @author jaevillen
 */
public class Crossover extends BLXAlphaCrossover{
    
    public Crossover(double crossoverProbability) {
        super(crossoverProbability);
    }
    
}
