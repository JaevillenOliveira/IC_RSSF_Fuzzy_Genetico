 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operators;


import ag.solution.FzArrayDoubleSolution;
import ag.problem.Problemfz;
import java.util.Random;
import java.util.StringTokenizer;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.util.JMetalException;


/**
 * This class has a mutation algorithm that works specifically for this problem. 
 * It mutates the points of the sets of each variable, which each of them have, exactly, three triangular sets.
 * 
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 * Changed by @author jaevillen
 * 
 */

@SuppressWarnings("serial")
public class FzSetsMutation implements MutationOperator<FzArrayDoubleSolution> {
    private double mutationProbability;
    private Random randomGenerator;
    private StringTokenizer st;
    private Problemfz problem;

    /** Constructor
    * @param probability
    * @param randomGenerator 
    */
    public FzSetsMutation(double probability, Random randomGenerator, Problemfz problem) {
        if (probability < 0) {
          throw new JMetalException("Mutation probability is negative: " + mutationProbability);
        }

        this.mutationProbability = probability;
        this.randomGenerator = randomGenerator;
        this.problem = problem;
    }

  /* Getters */

    /**
     *
     * @return the mutation probability
     */

    public double getMutationProbability() {
        return mutationProbability;
    }

    /* Setters */

    /**
     * Set the mutation probability
     * @param mutationProbability
     */

    public void setMutationProbability(double mutationProbability) {
        this.mutationProbability = mutationProbability;
    }

    /** Execute() method
     * @param solution
     * @return the solution mutated
     */
    @Override
    public FzArrayDoubleSolution execute(FzArrayDoubleSolution solution) throws JMetalException {
        if (null == solution) {
            throw new JMetalException("Null parameter");
        }

        doMutation(mutationProbability, solution);

        return solution;
    }

    /** Implements the mutation operation for fuzzy variables with three triangular sets*/
    private void doMutation(double probability, FzArrayDoubleSolution s) {

        int numFzVarPoints = 0,numSetsPoints = 0;
        numSetsPoints = this.problem.getSetshape().getNumPoints();
        numFzVarPoints = this.problem.getNumberOfSets()*numSetsPoints;

        for (int i = 0; i < s.getNumberOfVariables(); i+=numFzVarPoints) { 
            if (this.randomGenerator.nextDouble() <= probability) {
                for(int j = 0; j < numFzVarPoints; j+=numSetsPoints){  
                    for(int k = 0; k < numSetsPoints; k++){ 
                        if(j >= (numSetsPoints*2) && s.getLowerBound(i+j+k) < s.getVariableValue(i+j+k-numSetsPoints-1)){
                            double lowerBound = s.getVariableValue(i+j+k-numSetsPoints-1);
                            s.setVariableValue(i+j+k,this.problem.generateRdmPoint(lowerBound, s.getUpperBound(i+j+k)));
                        }else{
                            s.setVariableValue(i+j+k, this.problem.generateRdmPoint(s.getLowerBound(i+j+k), s.getUpperBound(i+j+k)));
                        }
                    }
                }
            }
        }
       
    }
}


