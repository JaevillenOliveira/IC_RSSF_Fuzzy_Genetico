/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ag;

import Operators.Crossover;
import Operators.FzSetsMutation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import org.uma.jmetal.algorithm.impl.AbstractGeneticAlgorithm;
import org.uma.jmetal.util.evaluator.impl.MultithreadedSolutionListEvaluator;


/**
 *
 * @author jaevillen
 */
public class Algorithm extends AbstractGeneticAlgorithm{
    
    private int iterations;
    private MultithreadedSolutionListEvaluator evaluator;
      
    public Algorithm(Problemfz problem, int maxPopulationSize) throws FileNotFoundException, IOException {
        super(problem);         
        this.setMaxPopulationSize(maxPopulationSize-1);
        this.setPopulation(this.createInitialPopulation());//This inherited method creates 'maxPopulationSize' new subjects
        //It's declared twice because one subject is already saved in the file, therefore it just needs to create maxPopulationSize -1 new subjects
        this.setMaxPopulationSize(maxPopulationSize);  
        this.iterations = 0;
        this.crossoverOperator = new Crossover(0.7, 5);
        this.mutationOperator = new FzSetsMutation(0.7, new Random());
        this.evaluator = new MultithreadedSolutionListEvaluator(4, problem);
    }
    
    @Override
    protected void initProgress() {
        this.iterations = 1; 
        //this.reproduction(this.getPopulation());
    }

    @Override
    protected void updateProgress() {
        this.iterations++;
    }

    @Override
    protected boolean isStoppingConditionReached() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected List evaluatePopulation(List list) {
        return this.evaluator.evaluate(list, problem);
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
