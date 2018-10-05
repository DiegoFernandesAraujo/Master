/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experimentos;

import experimentos.QP;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Diego
 */
public class TestaQP {
    
    public static void main(String[] args) throws IOException, InterruptedException {
//        File gs = new File("./src/csv/datasets", "cd_gold.csv");
    

    //Para deduplicação
    
        int [] vetor = {10};
        
        QP obj = new QP("cd_gold.csv", "cds", "QP1", 23, 9763, vetor, 1);
        obj.executa();
    }
    
}
