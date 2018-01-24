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
//        File arquivo = new File("../","resultado1.csv");
        
        try {
            System.out.println("/  -> " + new File("/").getCanonicalPath());
            System.out.println(".. -> " + new File("..").getCanonicalPath());
            System.out.println(".  -> " + new File(".").getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

//        obj.readCsvFile();
//        obj.padronizaCsvFile(arquivo);
        
        
        
        
        
    }
    
}
