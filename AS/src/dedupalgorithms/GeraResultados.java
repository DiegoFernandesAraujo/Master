/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dedupalgorithms;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diego
 */
public class GeraResultados {
    public static void main(String [] args){
        DedupAlg1 obj1 = new DedupAlg1("cd", "pk", "cd_gold", "disc1_id", "disc2_id", "cd_result", 2000);
        try {
            obj1.executaDedupAlg();
        } catch (IOException ex) {
            Logger.getLogger(GeraResultados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
