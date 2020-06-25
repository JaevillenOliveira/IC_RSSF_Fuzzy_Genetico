/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Operators;

import org.uma.jmetal.operator.impl.crossover.NPointCrossover;

/**
 *
 * @author jaevillen
 */
public class Crossover extends NPointCrossover{
    
    public Crossover(double probability, int crossovers) {
        super(probability, crossovers);
    }
    
}
