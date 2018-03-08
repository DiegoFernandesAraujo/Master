/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DS;

import AS.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Diego
 */
public class AplicacaoDS {

    public static void main(String[] args) throws IOException {
        DgStd obj = new DgStd();

        File[] resultados = new File[23];
        for (int i = 0; i < resultados.length; ++i) {
            int index = i + 1;
            resultados[i] = new File("./src/csv/resultsDedup", "resultado" + index + ".csv");
        }

        System.out.println("resultados.length: " + resultados.length);

        //Padronização dos arquivos
        File[] resultadosPadr = new File[23];

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
        obj.setTamBaseOrig(9763); //Necessário!

        List<String> aux = new ArrayList<String>();
        Random gerador = new Random();

        //1000 experimentos aleatórios
//        for (int i = 1; i <= 1000; i++) {
        for (int i = 1; i <= 1; i++) {

            obj.setPermutacao(i);
            obj.limpaTudo();
            System.out.println("Iteração " + i);

            int cont = 0;

            while (aux.size() < 23) {

//                int randomNum = gerador.nextInt(resultados.length);
//
//                if (!aux.contains(Integer.toString(randomNum))) {
//
//                    aux.add(Integer.toString(randomNum));
//                    obj.comparaConjuntos(resultadosPadr[randomNum]);
//
//                }
                aux.add(Integer.toString(cont));
                obj.comparaConjuntos(resultadosPadr[cont]);
                
                cont++;

            }

            aux.clear();

        }

//        obj.remDupDiverg();
        java.awt.Toolkit.getDefaultToolkit().beep();
        

    }

}
