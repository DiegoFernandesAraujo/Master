/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experimentos;

import java.io.IOException;

/**
 *
 * @author Diego
 */
public class ExperimentosCDS {
    public static void main(String[] args) throws IOException, InterruptedException {
        AllQPEtapa1 obj = new AllQPEtapa1("cds", 23, 0, 0, 0, 0, 0, 9763, 0, 0, 0, 1, 0, 0, 0, 0, 0, true, false, false, false, false, false, false, false, false);
        obj.setGsGeral("cd_gold.csv");
        obj.rodaExpDedup();
    }
    
}
