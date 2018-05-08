/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DS;

import AS.AnnStd;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Diego
 */
public class Demo {

    public static void main(String[] args) throws IOException {
//        DgStd obj = new DgStd();
        DgStd1 obj = new DgStd1();

        int qtdAlg = 3; //Quantidade de algoritmos de resolução de entidades não supervisionados utilizados no processo

        File gs = new File("./src/csv/datasets", "cd_gold.csv");

        obj.setGs(gs);

        obj.setDedup(true);
//        obj.setDedup(false);

        obj.setTamBaseOrig(9763);

        File[] resultados = new File[qtdAlg];
        for (int i = 0; i < resultados.length; ++i) {
            int index = i + 1;
            resultados[i] = new File("./src/csv/resultsDedup_Demo", "resultado" + index + ".csv");
        }

        //Padronização dos arquivos
        File[] resultadosPadr = new File[qtdAlg];

        for (int i = 0; i < resultadosPadr.length; ++i) {
            resultadosPadr[i] = obj.padronizaCsvFile(resultados[i]);
        }

        List<String> aux = new ArrayList<String>();

        //Geração do conjunto de pares divergentes
        for (int i = 1; i <= 1; i++) {

            obj.setPermutacao(i);
            obj.limpaTudo();
            System.out.println("Iteração " + i);

            int cont = 0;

            while (aux.size() < qtdAlg) {

                aux.add(Integer.toString(cont));
                
                obj.setQtdAlg(qtdAlg);

                if (aux.size() == qtdAlg - 1) { //Gerar estatísticas só na última iteração
                    obj.setGeraEst(true);
                }

                obj.comparaConjuntos(resultadosPadr[cont]);

                cont++;

            }

//            obj.contabilizaEstatDA(obj.getHistoricoDA());
//            obj.contabilizaEstatNAODA(obj.getHistoricoNAODA());
//            
//            obj.filtraDivergencias_NEW(obj.getEstatDA(), obj.getEstatNAODA());
            //Impressão dos algoritmos utilizados
            Iterator it = aux.iterator();

            while (it.hasNext()) {
                System.out.print(it.next() + ", ");
            }

            aux.clear();

        }

//        obj.remDupDiverg();
        java.awt.Toolkit.getDefaultToolkit().beep();

        
        
        //ADICIONAR PAUSA PARA  DAR PROSEGUIMENTO À PRÓXIMA ETAPA
        //2ª etapa Geração do arquivo de similaridades
        
        JOptionPane.showMessageDialog(null, "Conjunto de divergências gerado!");
        
        {
            File vetorSimilaridade = new File("./src/csv/conjuntosDS/vetorSimilaridades", "vetorSimilaridades-DEMO.csv");
            VetorSim objVS = new VetorSim("cd", "pk", "cd_gold", "disc1_id", "disc2_id", "cd_result", vetorSimilaridade);
            File diverg = new File("./src/csv/conjuntosDS/", "NAO_DA.csv"); //Esse arquivo tem que possuir todas as divergências
            
            File dir = new File("./src/csv/conjuntosDS/conjuntosDiverg/");

            try {
                objVS.geraVetor(diverg); //Para gerar o vetor base dos demais
                
                vetorSimilaridade = new File("./src/csv/conjuntosDS/vetorSimilaridades", "vetorSimilaridades-14-03.csv");
                
                System.out.println("Gerando vetores de similaridade...");
                
                
                if (dir.isDirectory()) {
                    File[] divergs = dir.listFiles();

                    for (File arq : divergs) {

                        String nome = arq.getName();

                        System.out.println("Nome do arquivo: " + nome);

                        if (nome.contains("diverg") && !nome.contains("_NEW")) {
                            objVS.geraVetorMenor(arq, vetorSimilaridade);
                            arq.delete(); //Exclui o arquivo depois de gerar os vetores de similaridade
                        }

                    }
                }
                
                JOptionPane.showMessageDialog(null, "Vetores de similaridades gerados!");

            } catch (IOException ex) {
                Logger.getLogger(ExecVetorSim.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //ADICIONAR PAUSA PARA  DAR PROSEGUIMENTO À PRÓXIMA ETAPA
        //3ª Python2: Executar a abordagem de AA para gerar o conjunto de treinamento e teste (to classify)        
        {
            
        }

    }

}
