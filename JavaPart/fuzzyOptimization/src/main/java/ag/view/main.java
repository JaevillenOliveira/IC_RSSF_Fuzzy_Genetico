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
            String bufferPath = "/home/jaevillen/IC/Buffer/";
            String configFilePath = "/home/jaevillen/IC/Buffer/ConfigFile.txt";
            String simFilePath = "/home/jaevillen/IC/OmnetPart/WSN/src/networktopology/runSimulation.sh";
            for(int i = 1; i <=3; i++){
                ctr = new Controller(i,configFilePath);
                pfz = ctr.readProblem(bufferPath, simFilePath);
                ga = new Algorithm(pfz, ctr.getSizeOfPopulation(), bufferPath);
                ga.run(ctr.createModelSolution(pfz), i);
                
                //String genPath = "/home/jaevillen/IC/Buffer/FittestGenerationSc"+String.valueOf(i)+".txt";
                //ga.runRecovery(ctr.readPopulation(genPath, pfz), 3, 1, i);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }



    
}
