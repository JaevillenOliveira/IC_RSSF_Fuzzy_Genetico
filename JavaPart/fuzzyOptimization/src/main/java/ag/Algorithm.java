/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ag;

import ag.problem.Problemfz;
import ag.solution.FzArrayDoubleSolution;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import operators.Crossover;
import operators.Mutation;
import operators.Selection;
import org.uma.jmetal.algorithm.impl.AbstractGeneticAlgorithm;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

/**
 *
 * @author jaevillen
 */
public class Algorithm extends AbstractGeneticAlgorithm{
    
    private int iterations;
    private int maxIterations;
    private SequentialSolutionListEvaluator evaluator;
      
    /**
     *
     * @param problem
     * @param maxPopulationSize
     * @param maxIterations
     * @throws FileNotFoundException
     * @throws IOException
     */
    public Algorithm(Problemfz problem, int maxPopulationSize, int maxIterations) throws FileNotFoundException, IOException {
        super(problem);         
        this.setMaxPopulationSize(maxPopulationSize-1);
        this.iterations = 0; 
        this.maxIterations = maxIterations;
        this.selectionOperator = new Selection(6);
        this.crossoverOperator = new Crossover(0.7);//new FzSetsBLXAlphaCrossover(0.7, problem);
        this.mutationOperator = new Mutation(0.4);   //new FzSetsMutation(0.7, new Random(), problem);
        this.evaluator = new SequentialSolutionListEvaluator();
    }

    /**
     * Writes the entire population into a file
     * @param filePath
     * @throws IOException
     */
    public void writePopulation(String filePath) throws IOException{   
        BufferedWriter bw = new BufferedWriter(new FileWriter(filePath)); 
        Iterator it = this.population.iterator();
        int subjectCounter = 0;
        while(it.hasNext()){
            FzArrayDoubleSolution sol = (FzArrayDoubleSolution) it.next();
            bw.write("Individuo "+ subjectCounter++ + " Energy "+ sol.getObjective(0));
            int v = ((Problemfz) this.problem).getNumberOfSets() * ((Problemfz) this.problem).getSetshape().getNumPoints();
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
    
    private void logEvolution(List solutionList) throws IOException{   
        BufferedWriter bw = new BufferedWriter(new FileWriter("/home/jaevillen/IC/Buffer/EvolutionLog.txt", true)); 
        Iterator it = solutionList.iterator();
        int subjectCounter = 0;
        bw.write("GENERATION " + this.iterations);
        bw.newLine();
        while(it.hasNext()){
            FzArrayDoubleSolution sol = (FzArrayDoubleSolution) it.next();
            System.out.println(sol.getObjective(0));
            bw.write("Individuo "+ subjectCounter++ + " Energy "+sol.getObjective(0));
            bw.newLine();
        }
        bw.close();
    }
    
    /*
        This method was overwritten because there were a problem with the original one:
        If the selection operator were to return ONE best solution, this method would return a list
        (in the size of the total number of solutions) filled with the same assumed best solution;
        If selection operator were to return a list ranked by the best solutions, this method would 
        return a list (in the size of the total number of solutions) filled with a bunch of lists 
        containing the ranked solutions.
    
     * @param population
     * @return
     */

    @Override
    protected List selection(List population) {
        return (List) selectionOperator.execute(population);
    }
    
    /**
     *
     * @param modelSolution
     * @throws IOException
     */
    public void run(FzArrayDoubleSolution modelSolution) throws IOException{
        List<FzArrayDoubleSolution> offspringPopulation;
        List<FzArrayDoubleSolution> matingPopulation;

        population = this.createInitialPopulation();//This inherited method creates 'maxPopulationSize' new subjects
        population.add(0, modelSolution);
        setMaxPopulationSize(this.maxPopulationSize+1);  //It's declared twice because one subject is already saved in the file, therefore it just needs to create maxPopulationSize -1 new subjects
        population = evaluatePopulation(this.getPopulation());
        this.logEvolution(population);
        System.out.println("GENERATION "+this.iterations);
        initProgress();
        
        while(!this.isStoppingConditionReached()){
            matingPopulation = selection(population);            
            offspringPopulation = this.reproduction(matingPopulation);
            offspringPopulation = evaluatePopulation(offspringPopulation);
            population = replacement(population, offspringPopulation);
            this.logEvolution(population);
            System.out.println("GENERATION "+this.iterations);
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

    /**
     *
     * @param population
     * @param offspringPopulation
     * @return
     */
    @Override
    protected List replacement(List population, List offspringPopulation) {
        List jointPopulation = new ArrayList<>();
        jointPopulation.addAll(population);
        jointPopulation.addAll(offspringPopulation);

        return (new Selection(this.maxPopulationSize)).execute(jointPopulation);
    }

    @Override
    public Object getResult() {
       return this.population.get(0);
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
