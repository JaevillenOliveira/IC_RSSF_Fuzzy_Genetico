 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Operators;


import ag.FzArrayDoubleSolution;
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

    /** Constructor
    * @param probability
    * @param randomGenerator 
    */
    public FzSetsMutation(double probability, Random randomGenerator) {
        if (probability < 0) {
          throw new JMetalException("Mutation probability is negative: " + mutationProbability);
        }

        this.mutationProbability = probability;
        this.randomGenerator = randomGenerator;
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
    private void doMutation(double probability, FzArrayDoubleSolution solution) {
        for (int i = 0; i < solution.getNumberOfVariables(); i++) { 
            if (randomGenerator.nextDouble() <= probability) {
                for(int j = 0; j < 3; j++){ //Number of sets in each variable
                    if (randomGenerator.nextDouble() <= probability) {
                        double [][] mutatedSets = (double[][]) solution.getVariableValue(i);
                        double inf, sup;
                        String id;
                        switch (j) {
                            case 0: // In the first set only the third point can be changed
                                {
                                    id = String.valueOf(i)+String.valueOf(j)+String.valueOf(2);
                                    System.out.println(solution.getAttributes());
                                    this.st = new StringTokenizer((String) solution.getAttribute(id));
                                    inf = Double.parseDouble(st.nextToken());
                                    sup = Double.parseDouble(st.nextToken());
                                    mutatedSets[0][2] = this.generateMutatedValue(inf, sup);
                                    break;
                                }
                            case 1:
                                for(int k = 0; k < 3; k++){//Number of points in the second set
                                    if (randomGenerator.nextDouble() <= probability) {
                                        id = String.valueOf(i)+String.valueOf(j)+String.valueOf(k);
                                        this.st = new StringTokenizer((String) solution.getAttribute(id));
                                        inf = Double.parseDouble(st.nextToken());
                                        sup = Double.parseDouble(st.nextToken());
                                        mutatedSets[1][k] = this.generateMutatedValue(inf, sup);
                                    }
                                }   break;
                            case 2: // In the third set only the first point can be changed
                                {
                                    id = String.valueOf(i)+String.valueOf(j)+String.valueOf(0);
                                    this.st = new StringTokenizer((String) solution.getAttribute(id));
                                    inf = Double.parseDouble(st.nextToken());
                                    sup = Double.parseDouble(st.nextToken());
                                    mutatedSets[2][0] = this.generateMutatedValue(inf, sup);
                                    break;
                                }
                            default:
                                break;
                        }

                        solution.setVariableValue(i, mutatedSets);    
                    }
                }
            }
        }
    }
  
    /*
    * Generates a random value within a specific range 
    */
    private double generateMutatedValue(double inf, double sup){
        return (inf + (sup - inf) * randomGenerator.nextDouble());
    }
}


