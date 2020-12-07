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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import operators.Crossover;
import operators.FzTournamentSelection;
import operators.Mutation;
import operators.Selection;
import org.uma.jmetal.algorithm.impl.AbstractGeneticAlgorithm;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

/**
 *
 * @author jaevillen
 */
public class Algorithm extends AbstractGeneticAlgorithm implements Serializable {
    
    private int iterations;
    private int noChangeCounter;
    private double bestFit;
    private SequentialSolutionListEvaluator evaluator;
      
    /**
     *
     * @param problem
     * @param maxPopulationSize
     * @throws FileNotFoundException
     * @throws IOException
     */
    public Algorithm(Problemfz problem, int maxPopulationSize) throws FileNotFoundException, IOException {
        super(problem);         
        this.setMaxPopulationSize(maxPopulationSize-1);
        this.iterations = 0; 
        this.selectionOperator = new FzTournamentSelection(3);
        this.crossoverOperator = new Crossover(0.7);//new FzSetsBLXAlphaCrossover(0.7, problem);
        this.mutationOperator = new Mutation(0.2);   //new FzSetsMutation(0.7, new Random(), problem);
        this.evaluator = new SequentialSolutionListEvaluator();
    }
    
    private void saveExecState(String path, Object objToSave) throws FileNotFoundException, IOException{
        ObjectOutputStream stateObj = new ObjectOutputStream(new FileOutputStream(path));
        stateObj.writeObject(objToSave);
        System.out.println("Object Saved to "+path);
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
    
    private void writeBestSolution(String filePath) throws IOException{   
        BufferedWriter bw = new BufferedWriter(new FileWriter(filePath)); 
        FzArrayDoubleSolution sol = (FzArrayDoubleSolution) getResult();
        bw.write("Best Individuo | Energy "+ sol.getObjective(0));
        int v = ((Problemfz) this.problem).getNumberOfSets() * ((Problemfz) this.problem).getSetshape().getNumPoints();
        int index = 0;
        for(int i = 0; i < sol.getNumberOfVariables(); i+=v){
            bw.newLine();
            for (int j = 0; j < v; j++){
                double point = sol.getVariableValue(index++);
                bw.write(point + " ");
            }
        }
        bw.close();
    }
    
    private void logEvolution(List solutionList, int scenario) throws IOException{   
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("/home/jaevillen/IC/Buffer/EvolutionLogSc"+String.valueOf(scenario)+".txt", true))) {
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
        }
    }
    
    private void updateLogs(Object population, int scenario) throws IOException{
        //this.saveExecState("/home/jaevillen/IC/Buffer/ExecState", population);
        this.writePopulation("/home/jaevillen/IC/Buffer/FittestGenerationSc"+String.valueOf(scenario)+".txt");
        this.logEvolution((List) population, scenario);
        this.writeBestSolution("/home/jaevillen/IC/Buffer/BestSolutionSc"+String.valueOf(scenario)+".txt");
        
    }
    
    /**
     *
     * @param modelSolution
     * @throws IOException
     */
    public void run(FzArrayDoubleSolution modelSolution, int scenario) throws IOException{
        List<FzArrayDoubleSolution> offspringPopulation;
        List<FzArrayDoubleSolution> matingPopulation;
        
        System.out.println("Starting Scenario "+String.valueOf(scenario));
        System.out.println("GENERATION "+this.iterations+ " NoChangeCounter "+this.noChangeCounter);
        
        population = this.createInitialPopulation();//This inherited method creates 'maxPopulationSize' new subjects
        population.add(0, modelSolution);
        setMaxPopulationSize(this.maxPopulationSize+1);  //It's declared twice because one subject is already saved in the file, therefore it just needs to create maxPopulationSize -1 new subjects
        population = evaluatePopulation(population);
        
        this.updateLogs(population, scenario);
        
        initProgress();
        while(!this.isStoppingConditionReached()){
            System.out.println("GENERATION "+this.iterations+ " NoChangeCounter "+this.noChangeCounter);
            matingPopulation = selection(population); 
            offspringPopulation = this.reproduction(matingPopulation);
            offspringPopulation = evaluatePopulation(offspringPopulation);
            population = replacement(population, offspringPopulation);
            
            this.updateLogs(population, scenario);

            updateProgress();
        }
    }
    
    public void runRecovery(List<FzArrayDoubleSolution> lastPop, int iterations, int noChangeCntr, int scenario) throws IOException{
        List<FzArrayDoubleSolution> offspringPopulation;
        List<FzArrayDoubleSolution> matingPopulation;
        
        System.out.println("Retaking Scenario "+String.valueOf(scenario));
        
        population = lastPop;//This inherited method creates 'maxPopulationSize' new subjects
        setMaxPopulationSize(this.maxPopulationSize+1);  //It's declared twice because one subject is already saved in the file, therefore it just needs to create maxPopulationSize -1 new subjects 

        retakeProgress(iterations, noChangeCntr);
        
        while(!this.isStoppingConditionReached()){
            System.out.println("GENERATION "+this.iterations+ " NoChangeCounter "+this.noChangeCounter);
            matingPopulation = selection(population); 
            offspringPopulation = this.reproduction(matingPopulation);
            offspringPopulation = evaluatePopulation(offspringPopulation);
            population = replacement(population, offspringPopulation);
            
            this.updateLogs(population, scenario);

            updateProgress();
        }
    }
    
    protected void retakeProgress(int iter, int noChangeCntr){
        this.iterations = iter;
        this.noChangeCounter = noChangeCntr;
    }
 
    @Override
    protected void initProgress() {
        this.iterations = 1;
        this.noChangeCounter = 0;
    }

    @Override
    protected void updateProgress() {
        this.iterations++;
        double newFit = (double)((FzArrayDoubleSolution) population.get(0)).getObjective(0);
        if(this.bestFit > newFit){
            this.bestFit = newFit;
            this.noChangeCounter = 0;
        }else{
            this.noChangeCounter++;
        }
    }

    @Override
    protected boolean isStoppingConditionReached() {
        return noChangeCounter >= 10 || this.iterations >= 50;
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