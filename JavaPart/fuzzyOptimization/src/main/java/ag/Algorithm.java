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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.uma.jmetal.algorithm.impl.AbstractGeneticAlgorithm;
import org.uma.jmetal.util.evaluator.impl.MultithreadedSolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.solutionattribute.Ranking;


/**
 *
 * @author jaevillen
 */
public class Algorithm extends AbstractGeneticAlgorithm{
    
    private int iterations;
    private SequentialSolutionListEvaluator evaluator;
      
    public Algorithm(Problemfz problem, int maxPopulationSize) throws FileNotFoundException, IOException {
        super(problem);         
        this.setMaxPopulationSize(maxPopulationSize-1);
        this.iterations = 0; 
        this.crossoverOperator = new Crossover(0.7, 5);
        this.mutationOperator = new FzSetsMutation(0.7, new Random(), problem);
        this.evaluator = new SequentialSolutionListEvaluator();
    }
    
    public void execute(FzArrayDoubleSolution modelSolution){
        List p = this.createInitialPopulation();//This inherited method creates 'maxPopulationSize' new subjects
        p.add(0, modelSolution);
        this.setPopulation(p);
        //It's declared twice because one subject is already saved in the file, therefore it just needs to create maxPopulationSize -1 new subjects
        this.setMaxPopulationSize(this.maxPopulationSize+1);  
        this.evaluatePopulation(this.getPopulation());
        this.initProgress();
        while(!this.isStoppingConditionReached()){
            this.updateProgress();
            p = this.selection(this.getPopulation());
            p = this.reproduction(p);
            p = this.evaluatePopulation(p);
            //this.replacement(this.getPopulation(), p);

        }
    }
    
    @Override
    protected void initProgress() {
        this.iterations = 1;
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
    protected List replacement(List population, List offspringPopulation) {
        List jointPopulation = new ArrayList<>();
        jointPopulation.addAll(population);
        jointPopulation.addAll(offspringPopulation);

//    Ranking ranking = computeRanking(jointPopulation);
//
//    return crowdingDistanceSelection(ranking);
        return jointPopulation;
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
