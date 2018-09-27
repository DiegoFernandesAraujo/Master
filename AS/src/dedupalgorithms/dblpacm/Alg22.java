/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//Não faz sentido aplicar fecho transitivo no caso de record linkage, pois se está buscando duplicatas entre as duas bases envolvidas apenas.
package dedupalgorithms.dblpacm;

import dedupalgorithms.cds.*;
import dedupalgorithms.DedupAlg;
import dude.algorithm.Algorithm;
import dude.exceptions.ExtractionFailedException;
import dude.output.CSVOutput;
import dude.output.DuDeOutput;
import dude.output.statisticoutput.CSVStatisticOutput;
import dude.output.statisticoutput.SimpleStatisticOutput;
import dude.output.statisticoutput.StatisticOutput;
import dude.postprocessor.NaiveTransitiveClosureGenerator;
import dude.postprocessor.StatisticComponent;
import dude.similarityfunction.contentbased.impl.simmetrics.EuclideanDistanceFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.NeedlemanWunschFunction;
import dude.util.GoldStandard;
import dude.util.data.DuDeObjectPair;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diego
 */
public class Alg22 extends DedupAlg {

    String rotulo;
    double a, b, c, d, e, f;
    int index_c1, index_c2, index_c3;
    String dir = "resultsDedup/dblpacm";

    FileWriter escreveResult;
    File estatisticasCSV;
    File estatisticasTXT;
    File estatisticasCSVSemFecho;
    File estatisticasTXTSemFecho;

    public Alg22(String baseDados1, String baseDados2, String chavePrimaria1, String chavePrimaria2, String gold, String goldId1, String goldId2, int ordem) {
        super(baseDados1, baseDados2, chavePrimaria1, chavePrimaria2, gold, goldId1, goldId2, ',');

        estatisticasCSV = new File("./src/csv/" + dir + "/estatisticas", "estatisticasDedup" + ordem + ".csv");
        estatisticasTXT = new File("./src/csv/" + dir + "/estatisticas", "estatisticasDedup" + ordem + ".txt");

        if (estatisticasTXT.exists() | estatisticasCSV.exists()) {
            System.out.println("Já existem resultados para esse algoritmo!");
            java.awt.Toolkit.getDefaultToolkit().beep();
            System.exit(0);
        }
        try {
            this.escreveResult = new FileWriter(new File("./src/csv/" + dir, "resultado" + ordem + ".csv"));

        } catch (IOException ex) {
            Logger.getLogger(Alg8.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    File resultado = new File("./src/csv/datasets", "resultado.csv");
    @Override
    public void executaDedupAlg() throws IOException {

        GoldStandard goldStandard = getGS();

        Algorithm algorithm = getAlg();
        algorithm.enableInMemoryProcessing();

        NeedlemanWunschFunction similarityFunc = new NeedlemanWunschFunction("title");
        NeedlemanWunschFunction similarityFunc2 = new NeedlemanWunschFunction("authors");
        NeedlemanWunschFunction similarityFunc3 = new NeedlemanWunschFunction("year");

        StatisticComponent statistic = new StatisticComponent(goldStandard, algorithm);

        StatisticOutput statisticOutputCSV;
        StatisticOutput statisticOutputTXT;

        statisticOutputCSV = new CSVStatisticOutput(estatisticasCSV, statistic, ';');
        statisticOutputTXT = new SimpleStatisticOutput(estatisticasTXT, statistic);

        statistic.setStartTime();

        BufferedWriter bwSim = null;

        bwSim = new BufferedWriter(escreveResult);

        bwSim.write("First Object;Second Object;similaridade\n");

        //Gerando o fecho transitivo
        for (DuDeObjectPair pair : algorithm) {

            final double similarity = similarityFunc.getSimilarity(pair);
            final double similarity2 = similarityFunc2.getSimilarity(pair);
            final double similarity3 = similarityFunc3.getSimilarity(pair);

            if ((similarity >= 0.7) && (similarity3 >= 0.6)) { //Testar esse!
//            if ((similarity >= 0.35) && (similarity2 >= 0.35) && (similarity3 >= 0.35) && (similarity4 >= 0.35)) {
//            if ((similarity >= 0.9) && (similarity2 >= 0.9) && (similarity3 >= 0.9) /*&& (similarity4 >= 0.85)*/) {
                statistic.addDuplicate(pair);
//                output.write(pair);
//                System.out.println("Existe no GS: " + existeNoGS(pair));
//                System.out.println("Elemento 1: " + pair.getFirstElement() + " - " + "Elemento 2: " + pair.getSecondElement());
//                System.out.println("Similaridade: " + pair.getSimilarity());

//                try {
//
//                    a = similarity;
//                    b = similarity2;
//                    c = similarity3;
////                    d = similarity4;
////                e = similarityFunc2.getSimilarity(pair);
////                f = similarityFunc2.getSimilarity(pair);
//
//                    final double simNorm = (a + b + c) / 3;
//                    String elemento1 = pair.getFirstElement().toString();
//                    String elemento2 = pair.getSecondElement().toString();
//
//                    index_c1 = elemento1.indexOf('[');
//                    index_c2 = elemento1.indexOf(']');
//                    index_c3 = elemento1.indexOf(']', index_c2) + 1;
//
//                    elemento1 = elemento1.substring(index_c1 + 1, index_c3);
//
//                    index_c1 = elemento2.indexOf('[');
//                    index_c2 = elemento2.indexOf(']');
//                    index_c3 = elemento2.indexOf(']', index_c2) + 1;
//
//                    elemento2 = elemento2.substring(index_c1 + 1, index_c3);
//
//                    bwSim.append(elemento1);
//                    bwSim.append(';');
//                    bwSim.append(elemento2);
//                    bwSim.append(';');
//                    bwSim.append(Double.toString(simNorm));
//                    bwSim.append('\n');
//                    bwSim.flush();
//
//                } catch (IOException ex) {
//                    System.out.println("ERRO!");
//                }
//                System.out.println("");
//
            } else {
                try {
                    statistic.addNonDuplicate(pair);
                } catch (ExtractionFailedException ex) {
                    Logger.getLogger(Alg8.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

        bwSim.close(); //Fecha arquivo

        statistic.setEndTime();

        statisticOutputCSV.writeStatistics();
        statisticOutputTXT.writeStatistics();

        algorithm.cleanUp();
        goldStandard.close();

    }

    public static void main(String[] args) {
//        Alg1 obj1 = new Alg1("DBLP2", "ACM", "num", "id", "DBLP2-ACM_perfectMapping_NEW", "idDBLP", "idACM", 1);

        Alg22 obj1 = new Alg22("DBLP2", "ACM", "id", "id", "DBLP2-ACM_perfectMapping", "idDBLP", "idACM", 22);
//        Alg1 obj1 = new Alg1("DBLP2_NEW", "ACM", "num", "id", "DBLP2-ACM_perfectMapping_NEW", "idDBLP", "idACM", 1);

        try {
            obj1.executaDedupAlg();
        } catch (IOException ex) {
            Logger.getLogger(Alg10.class.getName()).log(Level.SEVERE, null, ex);
        }
        java.awt.Toolkit.getDefaultToolkit().beep();
    }

}
