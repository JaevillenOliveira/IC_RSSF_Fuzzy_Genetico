/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operators;

import org.uma.jmetal.operator.impl.selection.RankingAndCrowdingSelection;

/**
 *
 * @author jaevillen
 */
public class Selection extends RankingAndCrowdingSelection{

    public Selection(int solutionsToSelect) {
        super(solutionsToSelect);
    }


    
}
