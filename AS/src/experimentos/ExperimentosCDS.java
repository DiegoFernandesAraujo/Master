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
        //Com o construtor dessa forma realiza apenas o experimento para QP1
        AllQPEtapa1 expCds = new AllQPEtapa1("cds", "cd_gold.csv", null, null, null, null, null, null, 23, 0, 0, 0, 0, 0, 9763, 0, 0, 0, 1, 0, 0, 0, 0, 0, true, false, false, false, false, false, false, false, false);
//        expCds.setGsGeral("cd_gold.csv");
        expCds.rodaExpDedup();
    }
    
}
