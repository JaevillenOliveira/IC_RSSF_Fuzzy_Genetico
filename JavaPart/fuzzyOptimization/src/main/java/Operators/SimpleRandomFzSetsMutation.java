/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Operators;


import ag.ThreeDArrayDoubleSolution;
import java.util.Random;
import java.util.StringTokenizer;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.util.JMetalException;


/**
 * 
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 * Changed by @author jaevillen
 * 
 */

@SuppressWarnings("serial")
public class SimpleRandomFzSetsMutation implements MutationOperator<ThreeDArrayDoubleSolution> {
  private double mutationProbability;
  private Random randomGenerator;
  private StringTokenizer st;

  /** Constructor
     * @param probability
     * @param randomGenerator 
     */
  public SimpleRandomFzSetsMutation(double probability, Random randomGenerator) {
    if (probability < 0) {
      throw new JMetalException("Mutation probability is negative: " + mutationProbability);
    }

    this.mutationProbability = probability;
    this.randomGenerator = randomGenerator;
  }

  /* Getters */
  public double getMutationProbability() {
    return mutationProbability;
  }

  /* Setters */
  public void setMutationProbability(double mutationProbability) {
    this.mutationProbability = mutationProbability;
  }

  /** Execute() method
     * @param solution */
  public ThreeDArrayDoubleSolution execute(ThreeDArrayDoubleSolution solution) throws JMetalException {
    if (null == solution) {
      throw new JMetalException("Null parameter");
    }

    doMutation(mutationProbability, solution);

    return solution;
  }

  /** Implements the mutation operation */
  private void doMutation(double probability, ThreeDArrayDoubleSolution solution) {
    for (int i = 0; i < solution.getNumberOfVariables(); i++) {
        if (randomGenerator.nextDouble() <= probability) {
            for(int j = 0; j < 3; j++){ //Number of sets in each variable
                if (randomGenerator.nextDouble() <= probability) {
                    double [][] mutatedSets = (double[][]) solution.getVariableValue(i);
                    double inf, sup;
                    String id;
                    switch (j) {
                        case 0:
                            {
                                id = String.valueOf(i)+String.valueOf(j)+String.valueOf(2);
                                System.out.println(solution);
                                this.st = new StringTokenizer((String) solution.getAttribute(id));
                                inf = Double.parseDouble(st.nextToken());
                                sup = Double.parseDouble(st.nextToken());
                                mutatedSets[0][2] = this.generateMutatedValue(inf, sup);
                                break;
                            }
                        case 1:
                            for(int k = 0; k < 3; k++){//Number of points in each set
                                if (randomGenerator.nextDouble() <= probability) {
                                    id = String.valueOf(i)+String.valueOf(j)+String.valueOf(k);
                                    this.st = new StringTokenizer((String) solution.getAttribute(id));
                                    inf = Double.parseDouble(st.nextToken());
                                    sup = Double.parseDouble(st.nextToken());
                                    mutatedSets[1][k] = this.generateMutatedValue(inf, sup);
                                }
                            }   break;
                        case 2:
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
  
    private double generateMutatedValue(double inf, double sup){
        return (inf + (sup - inf) * randomGenerator.nextDouble());
    }
}


