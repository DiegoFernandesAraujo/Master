/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AS;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Diego
 */
public class AplicacaoAS {

    public static void main(String[] args) throws IOException {
        AnnStd obj = new AnnStd();

        File resultado1 = new File("./src/csv/resultsDedup", "resultado1.csv");
        File resultado2 = new File("./src/csv/resultsDedup", "resultado2.csv");
        File resultado3 = new File("./src/csv/resultsDedup", "resultado3.csv");
        File resultado4 = new File("./src/csv/resultsDedup", "resultado4.csv");
        File resultado5 = new File("./src/csv/resultsDedup", "resultado5.csv");
        File resultado6 = new File("./src/csv/resultsDedup", "resultado6.csv");
        File resultado7 = new File("./src/csv/resultsDedup", "resultado7.csv");
        File resultado8 = new File("./src/csv/resultsDedup", "resultado8.csv");
        File resultado9 = new File("./src/csv/resultsDedup", "resultado9.csv");
        File resultado10 = new File("./src/csv/resultsDedup", "resultado10.csv");
        File resultado11 = new File("./src/csv/resultsDedup", "resultado11.csv");
        File resultado12 = new File("./src/csv/resultsDedup", "resultado12.csv");
        File resultado13 = new File("./src/csv/resultsDedup", "resultado13.csv");
        File resultado14 = new File("./src/csv/resultsDedup", "resultado14.csv");
        File resultado15 = new File("./src/csv/resultsDedup", "resultado15.csv");
        File resultado16 = new File("./src/csv/resultsDedup", "resultado16.csv");
        File resultado17 = new File("./src/csv/resultsDedup", "resultado17.csv");
        File resultado18 = new File("./src/csv/resultsDedup", "resultado18.csv");
        File resultado19 = new File("./src/csv/resultsDedup", "resultado19.csv");
        File resultado20 = new File("./src/csv/resultsDedup", "resultado20.csv");
        File resultado21 = new File("./src/csv/resultsDedup", "resultado21.csv");
        File resultado22 = new File("./src/csv/resultsDedup", "resultado22.csv");
        File resultado23 = new File("./src/csv/resultsDedup", "resultado23.csv");

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

        obj.setPermutacao(0);

        obj.limpaTudo(); //Só utilizar após a execução de um conjunto de permutações
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado1));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado2));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado3));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado4));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado5));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado6));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado7));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado8));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado9));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado10));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado11));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado12));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado13));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado14));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado15));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado16));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado17));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado18));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado19));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado20));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado21));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado22));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado23));

        obj.setPermutacao(1);

        obj.limpaTudo(); //Só utilizar após a execução de um conjunto de permutações
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado23));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado22));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado21));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado20));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado19));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado18));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado17));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado16));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado15));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado14));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado13));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado12));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado11));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado10));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado9));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado8));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado7));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado6));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado5));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado4));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado3));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado2));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado1));

    }

}
