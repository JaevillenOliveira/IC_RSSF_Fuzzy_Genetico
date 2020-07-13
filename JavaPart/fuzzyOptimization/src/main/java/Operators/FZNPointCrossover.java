/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Operators;

import org.apache.commons.lang3.ArrayUtils;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.operator.CrossoverOperator;

/**
 * Created by FlapKap on 23-03-2017.
 */
@SuppressWarnings("serial")
public class FZNPointCrossover implements CrossoverOperator {
    private final JMetalRandom randomNumberGenerator = JMetalRandom.getInstance();
    private double probability;
    private int crossovers;

    public FZNPointCrossover(double probability, int crossovers) {
      if (probability < 0.0) throw new JMetalException("Probability can't be negative");
      if (crossovers < 1) throw new JMetalException("Number of crossovers is less than one");
      this.probability = probability;
      this.crossovers = crossovers;
    }

    public FZNPointCrossover(int crossovers) {
      this.crossovers = crossovers;
      this.probability = 1.0;
    }

    public double getCrossoverProbability() {
      return probability;
    }
    
    @Override
    public Object execute(Object source) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public List<Solution> execute(List<Solution> s) {
      if (getNumberOfRequiredParents() != s.size()) {
        throw new JMetalException("Point Crossover requires + " + getNumberOfRequiredParents() + " parents, but got " + s.size());
      }
      if (randomNumberGenerator.nextDouble() < probability) {
        return doCrossover(s);
      } else {
        return s;
      }
    }

    private List<Solution> doCrossover(List<Solution> s) {
      Solution mom = s.get(0);
      Solution dad = s.get(1);

      if (mom.getNumberOfVariables() != dad.getNumberOfVariables()) {
        throw new JMetalException("The 2 parents doesn't have the same number of variables");
      }
      if (mom.getNumberOfVariables() < crossovers) {
        throw new JMetalException("The number of crossovers is higher than the number of variables");
      }

      int[] crossoverPoints = new int[crossovers];
      for (int i = 0; i < crossoverPoints.length; i++) {
        crossoverPoints[i] = randomNumberGenerator.nextInt(0, mom.getNumberOfVariables() - 1);
      }
      Solution girl = mom.copy();
      Solution boy = dad.copy();
      boolean swap = false;

      for (int i = 0; i < mom.getNumberOfVariables(); i++) {
        if (swap) {
          boy.setVariableValue(i, mom.getVariableValue(i));
          girl.setVariableValue(i, dad.getVariableValue(i));

        }

        if (ArrayUtils.contains(crossoverPoints, i)) {
          swap = !swap;
        }
      }
      List<Solution> result = new ArrayList<>();
      result.add(girl);
      result.add(boy);
      return result;
    }

    public int getNumberOfRequiredParents() {
      return 2;
    }

    public int getNumberOfGeneratedChildren() {
      return 2;
    }

}
