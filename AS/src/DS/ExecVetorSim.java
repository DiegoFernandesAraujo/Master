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
        VetorSim obj = new VetorSim("cd", "pk", "cd_gold", "disc1_id", "disc2_id", "cd_result");
//        VetorSim1 obj = new VetorSim1("cd", "pk", "cd_gold", "disc1_id", "disc2_id", "cd_result");
//        VetorSim2 obj = new VetorSim2("cd", "pk", "cd_gold", "disc1_id", "disc2_id", "cd_result");
//        File diverg = new File("./src/csv/conjuntosDS/", "NAO_DA.csv"); //Esse arquivo tem que possuir todas as divergências
        File diverg = new File("./src/csv/conjuntosDS/conjuntosDiverg/", "diverg1.csv"); //Esse arquivo tem que possuir todas as divergências
        File vetorSim = new File("./src/csv/conjuntosDS/conjuntosDiverg/", "vetorSimilaridades-07-03.csv"); //Esse arquivo tem que possuir todas as divergências
        try {
//            obj.geraVetor(diverg);
            obj.geraVetorMenor(diverg, vetorSim);
        } catch (IOException ex) {
            Logger.getLogger(ExecVetorSim.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
