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
public class AnnealingStandard {
    public static void main(String [] args) throws IOException{
        ManipulaCSV obj = new ManipulaCSV();
//        File arquivo = new File("D:\\Pesquisa\\Desenvolvimento\\resultado1.csv");
//        File arquivo = new File("H:\\Meu Drive\\UFCG\\Pesquisa\\Desenvolvimento\\resultado1.csv");

//        File arquivo = new File("./src/csv/","resultado1.csv");
        File resultado = new File("./src/csv/","resultTestCD.csv");
        File gs = new File("./src/csv/","cd_gold.csv");

        /* Para retornar o path do projeto
        try {

            System.out.println(".. -> " + new File("..").getCanonicalPath());
            System.out.println(".  -> " + new File(".").getCanonicalPath());
            System.out.println(System.getProperty("user.dir"));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
*/
//        obj.readCsvFile();
        obj.setGs(gs);
        obj.comparaComGS(obj.padronizaCsvFile(resultado));
//        obj.comparaComGS(obj.padronizaCsvFile(resultado));
//           obj.padronizaCsvFile(resultado);
        System.out.println("Iteração " + obj.getIteracao());
        
        
        
        
        
        
        
    }
    
}
