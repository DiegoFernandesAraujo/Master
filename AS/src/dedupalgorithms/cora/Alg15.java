/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dedupalgorithms.cora;

import dedupalgorithms.DedupAlg;
import dude.algorithm.Algorithm;
import dude.output.statisticoutput.CSVStatisticOutput;
import dude.output.statisticoutput.SimpleStatisticOutput;
import dude.output.statisticoutput.StatisticOutput;
import dude.postprocessor.NaiveTransitiveClosureGenerator;
import dude.postprocessor.StatisticComponent;
import dude.similarityfunction.contentbased.calculationstrategy.ArrayConversionStrategy;
import dude.similarityfunction.contentbased.calculationstrategy.AverageArrayArrayStrategy;
import dude.similarityfunction.contentbased.impl.simmetrics.LevenshteinDistanceFunction;
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
public class Alg15 extends DedupAlg {

    String rotulo;
    double a, b, c, d, e, f;
    int index_c1, index_c2, index_c3;
    String dir;

    FileWriter escreveResult;
    File estatisticasCSV;
    File estatisticasTXT;

    public Alg15(String baseDados1, String chavePrimaria, String gold, String goldId1, String goldId2, int ordem) {
        super(baseDados1, chavePrimaria, gold, goldId1, goldId2, ';', "XML");

        dir = "resultsDedup/" + baseDados1;

        File dirEstat = new File("./src/csv/" + dir + "/estatisticas");

        try {
            if (!dirEstat.exists()) {
                dirEstat.mkdirs();
                System.out.println("Diretório " + dirEstat.getAbsoluteFile() + " criado!");
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            estatisticasCSV = new File("./src/csv/" + dir + "/estatisticas", "estatisticasDedup" + ordem + ".csv");
            estatisticasTXT = new File("./src/csv/" + dir + "/estatisticas", "estatisticasDedup" + ordem + ".txt");
        }

        if (estatisticasTXT.exists() | estatisticasCSV.exists()) {
            System.out.println("Já existem resultados para esse algoritmo!");
            java.awt.Toolkit.getDefaultToolkit().beep();
            System.exit(0);
        }

        try {

            this.escreveResult = new FileWriter(new File("./src/csv/" + dir, "resultado" + ordem + ".csv"));

        } catch (IOException ex) {
            Logger.getLogger(Alg15.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    int contPrint = 0;

    @Override
    public void executaDedupAlg() throws IOException {

        GoldStandard goldStandard = getGS();

        Algorithm algorithm = getAlg();
        algorithm.enableInMemoryProcessing();

        NeedlemanWunschFunction similarityFunc = new NeedlemanWunschFunction("author");
        NeedlemanWunschFunction similarityFunc2 = new NeedlemanWunschFunction("title");
        LevenshteinDistanceFunction similarityFunc3 = new LevenshteinDistanceFunction("journal");
        LevenshteinDistanceFunction similarityFunc4 = new LevenshteinDistanceFunction("volume");
        LevenshteinDistanceFunction similarityFunc5 = new LevenshteinDistanceFunction("pages");
        LevenshteinDistanceFunction similarityFunc6 = new LevenshteinDistanceFunction("date");

        StatisticComponent statistic = new StatisticComponent(goldStandard, algorithm);

        StatisticOutput statisticOutputCSV;
        StatisticOutput statisticOutputTXT;

        statisticOutputCSV = new CSVStatisticOutput(estatisticasCSV, statistic, ';');
        statisticOutputTXT = new SimpleStatisticOutput(estatisticasTXT, statistic);

        statistic.setStartTime();

        NaiveTransitiveClosureGenerator fechoTrans = new NaiveTransitiveClosureGenerator();

        //Gerando o fecho transitivo
        for (DuDeObjectPair pair : algorithm) {
            double similarity = 0;
            double similarity2 = 0;
            double similarity3 = 0;
            double similarity4 = 0;
            double similarity5 = 0;
            double similarity6 = 0;

            try {
                similarity = similarityFunc.getSimilarity(pair);
                similarity2 = similarityFunc2.getSimilarity(pair);
                similarity3 = similarityFunc3.getSimilarity(pair);
                similarity4 = similarityFunc4.getSimilarity(pair);
                similarity5 = similarityFunc5.getSimilarity(pair);
                similarity6 = similarityFunc6.getSimilarity(pair);
            } catch (IllegalArgumentException e) {
                // ignore invalid values
//                System.out.print(".");
//                if (++contPrint >= 20) {
//                    contPrint = 0;
//                    System.out.println("");
//                }

                continue;
            }

            if ((similarity >= 0.6) && (similarity2 >= 0.7) && (similarity3 >= 0.2)) {
                fechoTrans.add(pair);
            } else {
                statistic.addNonDuplicate(pair);
            }
        }

        BufferedWriter bwSim = null;

        bwSim = new BufferedWriter(escreveResult);

        bwSim.write("First Object;Second Object;similaridade\n");

        for (DuDeObjectPair pair : fechoTrans) {

            statistic.addDuplicate(pair);

            try {

                try {
                    a = similarityFunc.getSimilarity(pair);
                    b = similarityFunc2.getSimilarity(pair);
                    c = similarityFunc3.getSimilarity(pair);
                    d = similarityFunc4.getSimilarity(pair);
                    e = similarityFunc5.getSimilarity(pair);
                } catch (IllegalArgumentException e) {
                    // ignore invalid values
//                    System.out.println("Values will be ignored: " + e.getMessage());
                    continue;
                }
//                f = similarityFunc2.getSimilarity(pair);

                final double simNorm = (a + b + c + d + e) / 5;
                String elemento1 = pair.getFirstElement().toString();
                String elemento2 = pair.getSecondElement().toString();

                index_c1 = elemento1.indexOf('[');
                index_c2 = elemento1.indexOf(']');
                index_c3 = elemento1.indexOf(']', index_c2) + 1;

                elemento1 = elemento1.substring(index_c1 + 1, index_c3);

                index_c1 = elemento2.indexOf('[');
                index_c2 = elemento2.indexOf(']');
                index_c3 = elemento2.indexOf(']', index_c2) + 1;

                elemento2 = elemento2.substring(index_c1 + 1, index_c3);

                bwSim.append(elemento1);
                bwSim.append(';');
                bwSim.append(elemento2);
                bwSim.append(';');
                bwSim.append(Double.toString(simNorm));
                bwSim.append('\n');
                bwSim.flush();

            } catch (IOException ex) {
                System.out.println("ERRO!");
            }

        }

        bwSim.close(); //Fecha arquivo

        statistic.setEndTime();

        statisticOutputCSV.writeStatistics();
        statisticOutputTXT.writeStatistics();
        System.out.println("");
        System.out.printf("Recall: %.2f %n", (statisticOutputTXT.getStatistics().getRecall()));
        System.out.printf("Precision: %.2f %n", (statisticOutputTXT.getStatistics().getPrecision()));
        System.out.printf("F1: %.2f %n", (statisticOutputTXT.getStatistics().getFMeasure()));
        System.out.println("");
        System.out.println("True positives: " + statisticOutputTXT.getStatistics().getTruePositives());
        System.out.println("False positives: " + statisticOutputTXT.getStatistics().getFalsePositives());
        System.out.println("False negatives: " + statisticOutputTXT.getStatistics().getFalseNegatives());

        algorithm.cleanUp();
        goldStandard.close();

    }

    public static void main(String[] args) {
        Alg15 obj1 = new Alg15("CORA", "id", "cora_gold", "id1", "id2", 15);
        try {
            obj1.executaDedupAlg();
        } catch (IOException ex) {
            Logger.getLogger(Alg15.class.getName()).log(Level.SEVERE, null, ex);
        }
        java.awt.Toolkit.getDefaultToolkit().beep();
    }

}
