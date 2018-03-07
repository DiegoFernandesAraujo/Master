/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DS;

import dedupalgorithms.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diego
 */
public class ExecVetorSim {
    public static void main(String [] args){
//        VetorSim obj1 = new VetorSim("cd3", "pk", "cd_gold", "disc1_id", "disc2_id", "cd_result");
//        VetorSim obj1 = new VetorSim("cd4", "pk", "cd_gold", "disc1_id", "disc2_id", "cd_result");
//        VetorSim obj1 = new VetorSim("cd - Copy", "pk", "cd_gold", "disc1_id", "disc2_id", "cd_result");
        VetorSim obj1 = new VetorSim("cd", "pk", "cd_gold", "disc1_id", "disc2_id", "cd_result");
        File diverg = new File("./src/csv/conjuntosDS", "NAO_DA.csv");
        try {
            obj1.geraVetor(diverg);
        } catch (IOException ex) {
            Logger.getLogger(ExecVetorSim.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
