/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simdedupalgorithms;

import dude.algorithm.Algorithm;
import dude.datasource.CSVSource;
import dude.output.CSVOutput;
import dude.output.DuDeOutput;
import dude.output.JsonOutput;
import dude.output.statisticoutput.CSVStatisticOutput;
import dude.output.statisticoutput.SimpleStatisticOutput;
import dude.output.statisticoutput.StatisticOutput;
import dude.postprocessor.NaiveTransitiveClosureGenerator;
import dude.postprocessor.StatisticComponent;
import dude.postprocessor.WarshallTransitiveClosureGenerator;
import dude.preprocessor.Preprocessor;
import dude.similarityfunction.SimilarityFunction;
import dude.similarityfunction.aggregators.Aggregator;
import dude.similarityfunction.aggregators.Average;
import dude.similarityfunction.contentbased.impl.simmetrics.LevenshteinDistanceFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.SimmetricsFunction;
import dude.util.GoldStandard;
import dude.util.data.DuDeObjectPair;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diego
 */
public class TesteSimilaridade extends DedupAlg {

    FileWriter escreveResult;
    File estatisticasCSV;
    File estatisticasTXT;

    String rotulo, Str2;
    double a, b, c, d, e, f;
    int index_c1, index_c2, index_c3;

    public TesteSimilaridade(String baseDados1, String chavePrimaria, String gold, String goldId1, String goldId2, String result, int ordem) {
        super(baseDados1, chavePrimaria, gold, goldId1, goldId2, result);

        estatisticasCSV = new File("./src/csv/resultsDedup/estatisticas", "estatisticasDedup" + ordem + ".csv");
        estatisticasTXT = new File("./src/csv/resultsDedup/estatisticas", "estatisticasDedup" + ordem + ".txt");

        try {
            this.escreveResult = new FileWriter(new File("./src/csv/resultsDedup", "resultado" + ordem + ".csv"));

        } catch (IOException ex) {
            Logger.getLogger(TesteSimilaridade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    File resultado = new File("./src/csv/datasets", "resultado.csv");
    @Override
    public void executaDedupAlg() throws IOException {

        GoldStandard goldStandard = getGS();

        Algorithm algorithm = getAlg();
        algorithm.enableInMemoryProcessing();

        LevenshteinDistanceFunction similarityFunc = new LevenshteinDistanceFunction("artist");
        LevenshteinDistanceFunction similarityFunc2 = new LevenshteinDistanceFunction("title");
//        LevenshteinDistanceFunction similarityFunc3 = new LevenshteinDistanceFunction("track01");
//        LevenshteinDistanceFunction similarityFunc4 = new LevenshteinDistanceFunction("track02");

//        DuDeOutput output = new CSVOutput(escreveResult);
        StatisticComponent statistic = new StatisticComponent(goldStandard, algorithm);

        StatisticOutput statisticOutputCSV;
        StatisticOutput statisticOutputTXT;

        statisticOutputCSV = new CSVStatisticOutput(estatisticasCSV, statistic, ';');
        statisticOutputTXT = new SimpleStatisticOutput(estatisticasTXT, statistic);

//        statisticOutput = new SimpleStatisticOutput(System.out, statistic);
        statistic.setStartTime();

        NaiveTransitiveClosureGenerator fechoTrans = new NaiveTransitiveClosureGenerator();

        int cont = 0;

        //Gerando o fecho transitivo
        for (DuDeObjectPair pair : algorithm) {
            final double similarity = similarityFunc.getSimilarity(pair);
            final double similarity2 = similarityFunc2.getSimilarity(pair);
//            final double similarity3 = similarityFunc3.getSimilarity(pair);
//            final double similarity4 = similarityFunc4.getSimilarity(pair);

            if ((similarity >= 0.9) && (similarity2 >= 0.9)) /*&& (similarity3 >= 0.9) && (similarity4 >= 0.9))*/ {
                fechoTrans.add(pair);
                System.out.println(++cont);

            } else {
                statistic.addNonDuplicate(pair);
            }

            if (cont > 5) {
                break;
            }
        }

//        FileWriter escreveSim;
        BufferedWriter bwSim = null;

//        File sim = new File("./src/csv/resultsDedup/similaridade.csv");
//        escreveSim = new FileWriter(sim, true);
        bwSim = new BufferedWriter(escreveResult);

//        bwSim.write("First Object;Second Object;duplicata;similaridade\n");
        bwSim.write("First Object;Second Object;similaridade\n");

        for (DuDeObjectPair pair : fechoTrans) {

            statistic.addDuplicate(pair);

//            if (statistic.isDuplicate(pair)) {
//                rotulo = "1.0";
//            } else {
//                rotulo = "0.0";
//            }
            try {

                a = similarityFunc.getSimilarity(pair);
                b = similarityFunc2.getSimilarity(pair);
//                c = similarityFunc3.getSimilarity(pair);
//                d = similarityFunc4.getSimilarity(pair);
//                e = similarityFunc2.getSimilarity(pair);
//                f = similarityFunc2.getSimilarity(pair);

                final double simNorm = (a + b) / 2;
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

//                bwSim.append(pair.getFirstElement().toString());
                bwSim.append(elemento1);
                bwSim.append(';');
                bwSim.append(elemento2);
//                bwSim.append(pair.getSecondElement().toString());
                bwSim.append(';');
//                bwSim.append(rotulo);
//                bwSim.append(';');
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

        algorithm.cleanUp();

        goldStandard.close();

    }

    public static void main(String[] args) {
        TesteSimilaridade obj1 = new TesteSimilaridade("cd", "pk", "cd_gold", "disc1_id", "disc2_id", "cd_result", 24);
        try {
            obj1.executaDedupAlg();

        } catch (IOException ex) {
            Logger.getLogger(TesteSimilaridade.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        java.awt.Toolkit.getDefaultToolkit().beep();
    }

}
