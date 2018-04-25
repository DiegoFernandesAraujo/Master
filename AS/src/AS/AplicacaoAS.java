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

    int tamBase = 0;
    int tamBase2 = 0;

    
    public int getTamBase() {
        return tamBase;
    }

    public void setTamBase(int tamBase) {
        this.tamBase = tamBase;
    }
        
    public int getTamBase2() {
        return tamBase2;
    }

    public void setTamBase2(int tamBase2) {
        this.tamBase2 = tamBase2;
    }

    public static void main(String[] args) throws IOException {
        
//        new AplicacaoAS().setTamBase(9763);
//        
        AnnStd obj = new AnnStd();
        long seed = 500;
        int qtdAlg = 23; //Quantidade de algoritmos de resolução de entidades utilizados

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

        File gs = new File("./src/csv/datasets", "cd_gold.csv");

        /* Para retornar o path do projeto
        try {

            System.out.println(".. -> " + new File("..").getCanonicalPath());
            System.out.println(".  -> " + new File(".").getCanonicalPath());
            System.out.println(System.getProperty("user.dir"));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
         */
        obj.setGs(gs);
//        obj.setDedup(true);
        obj.setDedup(false);
        
//        obj.setTamBaseOrig(9763); //Necessário!
        obj.setTamBaseOrig(9763, 9762); //Necessário!

        
//        obj.setTamBaseOrig(new AplicacaoAS().getTamBase()); //Necessário!
//      OR 
//      obj.setTamBaseOrig(new AplicacaoAS().getTamBase(), new AplicacaoAS().getTamBase2()); //Necessário!

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
