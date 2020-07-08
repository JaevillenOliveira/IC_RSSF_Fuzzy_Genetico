/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ag;

/**
 *
 * @author jaevillen
 */
public enum SetShape {
        
    TRIANGULAR(3), TRAPEZOIDAL(4);
     
    private final int numPoints;
    SetShape(int option){
        numPoints = option;
    }
    public int getNumPoints(){
        return numPoints;
    }
    
}
