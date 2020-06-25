/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ag;

import Operators.Crossover;
import Operators.Mutation;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import org.uma.jmetal.algorithm.impl.AbstractGeneticAlgorithm;


/**
 *
 * @author jaevillen
 */
public class Algorithm extends AbstractGeneticAlgorithm{
      
    public Algorithm(Problem problem, int maxPopulationSize) throws FileNotFoundException, IOException {
        super(problem);         
        this.setMaxPopulationSize(maxPopulationSize - 1);
        this.setPopulation(this.createInitialPopulation());
        //this.setMaxPopulationSize(maxPopulationSize); //It's declared two times beacause one subject is already created and saved in the file
    }
    
    @Override
    protected void initProgress() {
        this.crossoverOperator = new Crossover(0.7, 5);
        this.mutationOperator = new Mutation();
        this.reproduction(this.getPopulation());
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void updateProgress() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected boolean isStoppingConditionReached() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected List evaluatePopulation(List list) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected List replacement(List list, List list1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getResult() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getDescription() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
