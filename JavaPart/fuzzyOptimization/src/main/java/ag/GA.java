/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ag;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.algorithm.impl.AbstractGeneticAlgorithm;


/**
 *
 * @author jaevillen
 */
public class GA extends AbstractGeneticAlgorithm{
      

    private int sizeOfIntinialPopulation;
    private int subjectCount;
    private ArrayList <ArrayIntSolution> solutions;

    public GA(Problem problem, int sizeOfIntinialPopulation) throws FileNotFoundException, IOException {
        super(problem);         
        this.subjectCount = 0; 
        this.solutions = new ArrayList <> ();
        this.setSzeOfIntinialPopulation(sizeOfIntinialPopulation);
    }
    
    /**
     *
     * @return
     */
    public List<ArrayIntSolution> createPopulation(){
        ArrayList <ArrayIntSolution> newList = new ArrayList <> ();
        for (int i = 1; i < this.getSizeOfIntinialPopulation(); i++){
            ArrayIntSolution sol = (ArrayIntSolution) this.problem.createSolution();
            newList.add(sol);
        }
        this.getSolutions().addAll(newList);
        return newList;
    }
   
    public List<ArrayIntSolution> getSolutions() {
        return solutions;
    }

    public void setSolutions(ArrayList<ArrayIntSolution> solutions) {
        this.solutions = solutions;
    }
    
    public int getSizeOfIntinialPopulation() {
        return sizeOfIntinialPopulation;
    }

    public void setSzeOfIntinialPopulation(int sizeOfIntinialPopulation) {
        this.sizeOfIntinialPopulation = sizeOfIntinialPopulation;
    }
    
    public int getSubjectCount() {
        return subjectCount;
    }

    public void setSubjectCount(int subjectCount) {
        this.subjectCount = subjectCount;
    }

    @Override
    protected void initProgress() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
