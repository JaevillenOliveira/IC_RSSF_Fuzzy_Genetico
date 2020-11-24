/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ag.view;

import ag.Algorithm;
import ag.Controller;
import ag.problem.Problemfz;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jaevillen
 */
public class main {
    
    private static Controller ctr;
    private static Algorithm ga;
    private static Problemfz pfz;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            for(int i = 2; i <=3; i++){
                ctr = new Controller(i);
                pfz = ctr.readProblem();
                ga = new Algorithm(pfz, ctr.getSizeOfPopulation());
                ga.run(ctr.createModelSolution(pfz), i);
                //pfz.writeSolution((DoubleSolution) ga.getResult(), "/home/jaevillen/IC/Buffer/BestSolution.txt");
                //ga.writePopulation("/home/jaevillen/IC/Buffer/FittestGeneration.txt");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }



    
}
