/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DS;

import dedupalgorithms.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diego
 */
public class ExecVetorSim {
    public static void main(String [] args){
        VetorSim obj1 = new VetorSim("cd3", "pk", "cd_gold", "disc1_id", "disc2_id", "cd_result");
        try {
            obj1.executaDedupAlg();
        } catch (IOException ex) {
            Logger.getLogger(ExecVetorSim.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
