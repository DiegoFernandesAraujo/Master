/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AS;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Diego
 */
public class AplicacaoAS {

    public static void main(String[] args) throws IOException {
        AnnStd obj = new AnnStd();

        //CONFIGURAÇÃO DOS DADOS REFERENTES AO EXPERIMENTO
        int qtdAlg = 23; //Quantidade de algoritmos de resolução de entidades não supervisionados utilizados no processo

        File gs = new File("./src/csv/datasets", "cd_gold.csv");

        obj.setGs(gs);

        obj.setDedup(true);
//        obj.setDedup(false);

        obj.setTamBaseOrig(9763); //Necessário!
//        obj.setTamBaseOrig2(9763); //Necessário!

        //CONFIGURAÇÃO DOS DADOS REFERENTES AO EXPERIMENTO

        long seed = 500;

        File[] resultados = new File[qtdAlg];
        for (int i = 0; i < resultados.length; ++i) {
            int index = i + 1;
            resultados[i] = new File("./src/csv/resultsDedup", "resultado" + index + ".csv");
        }

        System.out.println("resultados.length: " + resultados.length);

        //Padronização dos arquivos
        File[] resultadosPadr = new File[qtdAlg];

        for (int i = 0; i < resultadosPadr.length; ++i) {
            resultadosPadr[i] = obj.padronizaCsvFile(resultados[i]);
        }

        /* Para retornar o path do projeto
        try {

            System.out.println(".. -> " + new File("..").getCanonicalPath());
            System.out.println(".  -> " + new File(".").getCanonicalPath());
            System.out.println(System.getProperty("user.dir"));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
         */
        List<String> aux = new ArrayList<String>();
        Random gerador = new Random(seed);

        //1000 experimentos aleatórios
        for (int i = 1; i <= 1000; i++) {

            obj.setPermutacao(i);
            obj.limpaTudo();
            System.out.println("Iteração " + i);

            while (aux.size() < qtdAlg) {

                int randomNum = gerador.nextInt(resultados.length);

                if (!aux.contains(Integer.toString(randomNum))) {

                    aux.add(Integer.toString(randomNum));
                    obj.comparaConjuntos(resultadosPadr[randomNum]);

                }

            }

            aux.clear();

        }

        java.awt.Toolkit.getDefaultToolkit().beep();

    }

}
