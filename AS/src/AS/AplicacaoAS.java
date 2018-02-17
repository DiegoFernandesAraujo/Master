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

        File resultado0 = new File("./src/csv/resultsDedup", "resultTestMaiorCD0_NEW.csv");
        File resultado1 = new File("./src/csv/resultsDedup", "resultTestMaiorCD1.csv");
        File resultado2 = new File("./src/csv/resultsDedup", "resultTestMaiorCD2.csv");
        //Copiado diretamente do gold standard:
        File resultado3 = new File("./src/csv/resultsDedup", "resultTestMaiorCD3.csv");
        File resultado4 = new File("./src/csv/resultsDedup", "resultTestMaiorCD4.csv");
        File resultado5 = new File("./src/csv/resultsDedup", "resultTestMaiorCD5.csv");
        File resultado10000 = new File("./src/csv/resultsDedup", "resultado10000.csv");

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
        obj.setTamBaseOrig(9763);
        
        obj.setPermutacao(0);
        
        obj.limpaTudo(); //Só utilizar após a execução de um conjunto de permutações
        obj.comparaConjuntos(resultado0);
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado1));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado2));
        obj.comparaConjuntos(resultado3);
        obj.comparaConjuntos(resultado4);
        obj.comparaConjuntos(resultado5);
        
        obj.setPermutacao(1);
        
        obj.limpaTudo(); //Só utilizar após a execução de um conjunto de permutações
        obj.comparaConjuntos(resultado0);
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado1));
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado2));
        obj.comparaConjuntos(resultado3);
        obj.comparaConjuntos(resultado4);
        obj.comparaConjuntos(resultado5);
        
        obj.setPermutacao(10001);
        obj.limpaTudo(); //Só utilizar após a execução de um conjunto de permutações
        obj.comparaConjuntos(obj.padronizaCsvFile(resultado10000));

    }

}
