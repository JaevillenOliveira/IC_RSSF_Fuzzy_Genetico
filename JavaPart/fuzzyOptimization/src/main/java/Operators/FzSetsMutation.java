 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Operators;


import ag.FzArrayDoubleSolution;
import ag.Problemfz;
import java.util.Random;
import java.util.StringTokenizer;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
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
        switch(this.problem.getSetType()){
            case "triangular":
                numFzVarPoints = this.problem.getNumberOfSets()*3;
                numSetsPoints = 3;
                break;
            default:
                break;
        }

        for (int i = 0; i < s.getNumberOfVariables(); i+=numFzVarPoints) { 
            for(int j = 0; j < numFzVarPoints; j+=numSetsPoints){  
                for(int k = 0; k < numSetsPoints; k++){ 
                    if (this.randomGenerator.nextDouble() <= probability) {
                        if(j >= 6 && s.getLowerBound(i+j+k) < s.getVariableValue(i+j+k-4)){
                            double lowerBound = s.getVariableValue(i+j+k-4);
                            s.setVariableValue(i+j+k, this.problem.generateRdmPoint(lowerBound, s.getUpperBound(i+j+k)));
                        }else{
                            s.setVariableValue(i+j+k, this.problem.generateRdmPoint(s.getLowerBound(i+j+k), s.getUpperBound(i+j+k)));
                        }
                    }
                }
            }
        }
    }
                    
//                double mutatedSets = (double) s.getVariableValue(i);
//                    double inf, sup;
//                    String id;
//                    switch (j) {
//                        case 0: // In the first set only the third point can be changed
//                            {
//                                id = String.valueOf(i)+String.valueOf(j)+String.valueOf(2);
//                                System.out.println(s.getAttributes());
//                                this.st = new StringTokenizer((String) s.getAttribute(id));
//                                inf = Double.parseDouble(st.nextToken());
//                                sup = Double.parseDouble(st.nextToken());
//                                mutatedSets[0][2] = this.generateMutatedValue(inf, sup);
//                                break;
//                            }
//                        case 1:
//                            for(int k = 0; k < 3; k++){//Number of points in the second set
//                                if (randomGenerator.nextDouble() <= probability) {
//                                    id = String.valueOf(i)+String.valueOf(j)+String.valueOf(k);
//                                    this.st = new StringTokenizer((String) s.getAttribute(id));
//                                    inf = Double.parseDouble(st.nextToken());
//                                    sup = Double.parseDouble(st.nextToken());
//                                    mutatedSets[1][k] = this.generateMutatedValue(inf, sup);
//                                }
//                            }   break;
//                        case 2: // In the third set only the first point can be changed
//                            {
//                                id = String.valueOf(i)+String.valueOf(j)+String.valueOf(0);
//                                this.st = new StringTokenizer((String) s.getAttribute(id));
//                                inf = Double.parseDouble(st.nextToken());
//                                sup = Double.parseDouble(st.nextToken());
//                                mutatedSets[2][0] = this.generateMutatedValue(inf, sup);
//                                break;
//                            }
//                        default:
//                            break;
//                    }
//
//                    s.setVariableValue(i, mutatedSets);    
//                }
//            }
//        }
}


