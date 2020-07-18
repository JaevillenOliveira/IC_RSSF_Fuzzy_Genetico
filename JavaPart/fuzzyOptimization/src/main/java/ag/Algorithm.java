/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ag;

import ag.problem.Problemfz;
import ag.solution.FzArrayDoubleSolution;
import operators.FzSetsBLXAlphaCrossover;
import operators.FzSetsMutation;
import operators.NullOperator;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import operators.Crossover;
import operators.Mutation;
import org.uma.jmetal.algorithm.impl.AbstractGeneticAlgorithm;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.solutionattribute.Ranking;


/**
 *
 * @author jaevillen
 */
public class Algorithm extends AbstractGeneticAlgorithm{
    
    private int iterations;
    private int maxIterations;
    private SequentialSolutionListEvaluator evaluator;
      
    public Algorithm(Problemfz problem, int maxPopulationSize, int maxIterations) throws FileNotFoundException, IOException {
        super(problem);         
        this.setMaxPopulationSize(maxPopulationSize-1);
        this.iterations = 0; 
        this.maxIterations = maxIterations;
        this.crossoverOperator = new Crossover(0.7);//new FzSetsBLXAlphaCrossover(0.7, problem);
        this.mutationOperator = new Mutation(0.7);   //new FzSetsMutation(0.7, new Random(), problem);
        this.evaluator = new SequentialSolutionListEvaluator();
    }

    
    public void writePopulation(List solutionList, Problemfz pfz) throws IOException{   
        BufferedWriter bw = new BufferedWriter(new FileWriter("/home/jaevillen/IC/Buffer/TestingReproduction.txt")); 
        Iterator it = solutionList.iterator();
        int subjectCounter = 1;
        while(it.hasNext()){
            FzArrayDoubleSolution sol = (FzArrayDoubleSolution) it.next();
            bw.write("Individuo" + " " + ++subjectCounter);
            int v = pfz.getNumberOfSets() * 3;
            int index = 0;
            for(int i = 0; i < sol.getNumberOfVariables(); i+=v){
                bw.newLine();
                for (int j = 0; j < v; j++){
                    double point = sol.getVariableValue(index++);
                    bw.write(point + " ");
                }
            }
            bw.newLine();
        }
        bw.close();
    }
    
    public void run(FzArrayDoubleSolution modelSolution) throws IOException{
        List<FzArrayDoubleSolution> offspringPopulation;
        List<FzArrayDoubleSolution> matingPopulation;
        
        
        population = this.createInitialPopulation();//This inherited method creates 'maxPopulationSize' new subjects
        population.add(0, modelSolution);
        setMaxPopulationSize(this.maxPopulationSize+1);  //It's declared twice because one subject is already saved in the file, therefore it just needs to create maxPopulationSize -1 new subjects
        population = evaluatePopulation(this.getPopulation());
        initProgress();
        
        while(!this.isStoppingConditionReached()){
            matingPopulation = selection(population);
            offspringPopulation = this.reproduction(matingPopulation);
            offspringPopulation = evaluatePopulation(offspringPopulation);
            population = replacement(population, offspringPopulation);
            updateProgress();
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
        return iterations >= maxIterations;
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

//        Ranking ranking = computeRanking(jointPopulation);
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
