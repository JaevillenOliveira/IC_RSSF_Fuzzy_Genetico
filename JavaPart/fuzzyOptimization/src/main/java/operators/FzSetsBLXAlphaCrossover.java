/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operators;

import ag.solution.FzArrayDoubleSolution;
import ag.problem.Problemfz;
import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.solution.util.RepairDoubleSolution;
import org.uma.jmetal.solution.util.RepairDoubleSolutionAtBounds;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;

/**
 * This class allows to apply a BLX-alpha crossover operator to two parent solutions.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 * Changed by @author jaevillen
 * 
 */
@SuppressWarnings("serial")
public class FzSetsBLXAlphaCrossover implements CrossoverOperator {
    private static final double DEFAULT_ALPHA = 0.5;

    private double crossoverProbability;
    private double alpha;
    private Problemfz pfz; //This was added because to 
    private RepairDoubleSolution solutionRepair ;
    private RandomGenerator<Double> randomGenerator ;
    
    
    /** Constructor
     * @param crossoverProbability
     * @param pfz //Added bY Me
     */
    public FzSetsBLXAlphaCrossover(double crossoverProbability, Problemfz pfz) {
      this (crossoverProbability, DEFAULT_ALPHA, new RepairDoubleSolutionAtBounds(), pfz) ;
    }

    /** Constructor
     * @param crossoverProbability
     * @param alpha */
    public FzSetsBLXAlphaCrossover(double crossoverProbability, double alpha, Problemfz pfz) {
      this (crossoverProbability, alpha, new RepairDoubleSolutionAtBounds(), pfz) ;
    }

    /** Constructor
     * @param crossoverProbability
     * @param alpha
     * @param solutionRepair */
    public FzSetsBLXAlphaCrossover(double crossoverProbability, double alpha, RepairDoubleSolution solutionRepair, Problemfz pfz) {
            this(crossoverProbability, alpha, solutionRepair, () -> JMetalRandom.getInstance().nextDouble(), pfz);
    }

    /** Constructor
     * @param crossoverProbability
     * @param alpha
     * @param solutionRepair
     * @param randomGenerator */
    public FzSetsBLXAlphaCrossover(double crossoverProbability, double alpha, RepairDoubleSolution solutionRepair, RandomGenerator<Double> randomGenerator, Problemfz pfz) {
      if (crossoverProbability < 0) {
        throw new JMetalException("Crossover probability is negative: " + crossoverProbability) ;
      } else if (alpha < 0) {
        throw new JMetalException("Alpha is negative: " + alpha);
      }

      this.crossoverProbability = crossoverProbability ;
      this.alpha = alpha ;
      this.randomGenerator = randomGenerator ;
      this.solutionRepair = solutionRepair ;
      this.pfz = pfz;
    }

    /* Getters */
     public double getCrossoverProbability() {
       return crossoverProbability;
     }

     public double getAlpha() {
       return alpha;
     }

     /* Setters */
     public void setCrossoverProbability(double crossoverProbability) {
       this.crossoverProbability = crossoverProbability;
     }

     public void setAlpha(double alpha) {
       this.alpha = alpha;
     }

     /** Execute() method
     * @param solutions
     * @return  */
     @Override
     public Object execute(Object solutions) {
       if (null == solutions) {
         throw new JMetalException("Null parameter") ;
       } else if (((List)solutions).size() != 2) {
         throw new JMetalException("There must be two parents instead of " + ((List)solutions).size()) ;
       }

       return doCrossover(crossoverProbability, ((List<FzArrayDoubleSolution>)solutions).get(0), ((List<FzArrayDoubleSolution>)solutions).get(1)) ;
     }

     /** doCrossover method
     * @param probability
     * @param parent1
     * @param parent2
     * @return  */
    public List<FzArrayDoubleSolution> doCrossover(
         double probability, FzArrayDoubleSolution parent1, FzArrayDoubleSolution parent2) {
        List<FzArrayDoubleSolution> offspring = new ArrayList<>(2);

        offspring.add((FzArrayDoubleSolution) parent1.copy()) ;
        offspring.add((FzArrayDoubleSolution) parent2.copy()) ;

        int i;
        double random;
        double valueY1;
        double valueY2;
        double valueX1;
        double valueX2;
        double upperBound;
        double lowerBound;
        int numOfPoints = this.pfz.getSetshape().getNumPoints();
        int numFzVarPoints = this.pfz.getNumberOfSets()*numOfPoints;

        if (randomGenerator.getRandomValue() <= probability) {
            for (i = 0; i < parent1.getNumberOfVariables(); i+= numFzVarPoints) {
                for(int j = 0; j < numFzVarPoints; j+=numOfPoints){
                    for(int k = 0; k < numOfPoints; k++){
                        upperBound = parent1.getUpperBound(i+j+k);
                        lowerBound = parent1.getLowerBound(i+j+k);
                        valueX1 = parent1.getVariableValue(i+j+k);
                        valueX2 = parent2.getVariableValue(i+j+k);

                        double max;
                        double min;
                        double range;

                        if (valueX2 > valueX1) {
                          max = valueX2;
                          min = valueX1;
                        } else {
                          max = valueX1;
                          min = valueX2;
                        }
                    
                        range = max - min;

                        double minRange;
                        double maxRange;

                        minRange = min - range * alpha;
                        maxRange = max + range * alpha;
                        
                        double infLimit1 = lowerBound;
                        double infLimit2 = lowerBound;

                        
                        if(j >= (numOfPoints*2)){
                            infLimit1 = parent1.getVariableValue(i+j+k-numOfPoints-1);
                            infLimit2 = parent2.getVariableValue(i+j+k-numOfPoints-1);
                            
                            random = randomGenerator.getRandomValue();
                            if(minRange < infLimit1){
                                valueY1 = infLimit1 + random * (maxRange - infLimit1);
                            }else{
                               valueY1 = minRange + random * (maxRange - minRange);
                            }

                            random = randomGenerator.getRandomValue();
                            if(minRange < infLimit2){
                                valueY2 = infLimit2 + random * (maxRange - infLimit2);
                            }else{
                                valueY2 = minRange + random * (maxRange - minRange);  
                            }

                        }else{
                            random = randomGenerator.getRandomValue();
                            valueY1 = minRange + random * (maxRange - minRange);
                            
                            random = randomGenerator.getRandomValue();
                            valueY2 = minRange + random * (maxRange - minRange);  
                        }
                        
                        valueY1 = solutionRepair.repairSolutionVariableValue(valueY1, infLimit1, upperBound) ;
                        valueY2 = solutionRepair.repairSolutionVariableValue(valueY2, infLimit2, upperBound) ;
                        
                        offspring.get(0).setVariableValue(i+j+k, valueY1);
                        offspring.get(1).setVariableValue(i+j+k, valueY2);
                    }
                }
            }
        }
        return offspring;
    }
    

    @Override
     public int getNumberOfRequiredParents() {
       return 2 ;
     }

    @Override
     public int getNumberOfGeneratedChildren() {
       return 2 ;
     }


}