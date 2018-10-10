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

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        File vetorSim = new File("./src/csv/conjuntosDS/vetorSimilaridades", "vetorSimilaridades-25-06.csv");
//        File vetorSim = new File("./src/csv/conjuntosDS/conjuntosDiverg/", "vetorSimilaridades-10-05.csv"); //Esse arquivo tem que possuir todas as divergências

//        VetorSim obj = new VetorSim("cd", "pk", "cd_gold", "disc1_id", "disc2_id", "cd_result");
//        VetorSim obj = new VetorSim("cd", "pk", "cd_gold", "disc1_id", "disc2_id", "cd_result", vetorSim);
        VetorSimEstat obj = new VetorSimEstat("cd", "pk", "cd_gold", "disc1_id", "disc2_id", "cd_result", vetorSim, ';');

//        VetorSim1 obj = new VetorSim1("cd", "pk", "cd_gold", "disc1_id", "disc2_id", "cd_result");
//        VetorSim2 obj = new VetorSim2("cd", "pk", "cd_gold", "disc1_id", "disc2_id", "cd_result");
        File diverg = new File("./src/csv/conjuntosDS/", "NAO_DA.csv"); //Esse arquivo tem que possuir todas as divergências
//        File diverg = new File("./src/csv/conjuntosDS/conjuntosDivergAA/cds/QP1/", "diverg(10)1.csv"); //Esse arquivo tem que possuir todas as divergências

//        File dir = new File("./src/csv/conjuntosDS/conjuntosDiverg/");
        File dir = new File("./src/csv/conjuntosDS/conjuntosDivergAA/cds/QP1/");

        try {
//            obj.geraVetor(diverg); //Para gerar o vetor base dos demais

            //Para gerar os vetores de similaridade específicos
            if (dir.isDirectory()) {
                File[] divergs = dir.listFiles();

                for (File arq : divergs) {

                    String nome = arq.getName();

                    System.out.println("Nome do arquivo: " + nome);

                    if (nome.contains("diverg") && !nome.contains("_NEW")) {
                        obj.geraVetorMenor(arq, vetorSim);
                        arq.delete(); //Exclui o arquivo depois de gerar os vetores de similaridade
                    }

                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ExecVetorSim.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
