/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dedupalgorithms.cds;

import dedupalgorithms.DedupAlg;
import dude.algorithm.Algorithm;
import dude.datasource.CSVSource;
import dude.output.CSVOutput;
import dude.output.DuDeOutput;
import dude.output.statisticoutput.CSVStatisticOutput;
import dude.output.statisticoutput.SimpleStatisticOutput;
import dude.output.statisticoutput.StatisticOutput;
import dude.postprocessor.NaiveTransitiveClosureGenerator;
import dude.postprocessor.StatisticComponent;
import dude.similarityfunction.aggregators.Average;
import dude.similarityfunction.aggregators.Maximum;
import dude.similarityfunction.aggregators.Minimum;
import dude.similarityfunction.contentbased.impl.SoundExFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.JaroDistanceFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.LevenshteinDistanceFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.MongeElkanFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.SmithWatermanFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.SmithWatermanFunction;
import dude.util.GoldStandard;
import dude.util.data.DuDeObjectPair;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.simmetrics.metrics.functions.AffineGap;

/**
 *
 * @author Diego
 */
public class Alg31 extends DedupAlg {

    String rotulo;
    double a, b, c, d, e, f;
    int index_c1, index_c2, index_c3;
    String dir = "resultsDedup/cds";

    FileWriter escreveResult;
    File estatisticasCSV;
    File estatisticasTXT;

    public Alg31(String baseDados1, String chavePrimaria, String gold, String goldId1, String goldId2, int ordem) {
        super(baseDados1, chavePrimaria, gold, goldId1, goldId2, ';');

        dir = "resultsDedup/" + baseDados1;
        
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
            Logger.getLogger(Alg31.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    File resultado = new File("./src/csv/datasets", "resultado.csv");
    @Override
    public void executaDedupAlg() throws IOException {

        GoldStandard goldStandard = getGS();

        Algorithm algorithm = getAlg();
        algorithm.enableInMemoryProcessing();

        SoundExFunction similarityFunc = new SoundExFunction("artist");
        LevenshteinDistanceFunction similarityFunc2 = new LevenshteinDistanceFunction("track01");
        LevenshteinDistanceFunction similarityFunc3 = new LevenshteinDistanceFunction("track02");
        LevenshteinDistanceFunction similarityFunc4 = new LevenshteinDistanceFunction("track01");
        LevenshteinDistanceFunction similarityFunc5 = new LevenshteinDistanceFunction("track02");

        Average avg = new Average();
        avg.add(similarityFunc2);
        avg.add(similarityFunc3);
        avg.add(similarityFunc4);
        avg.add(similarityFunc5);

//        DuDeOutput output = new JsonOutput(System.out);
        DuDeOutput output = new CSVOutput(escreveResult);
        StatisticComponent statistic = new StatisticComponent(goldStandard, algorithm);

        StatisticOutput statisticOutputCSV;
        StatisticOutput statisticOutputTXT;

        statisticOutputCSV = new CSVStatisticOutput(estatisticasCSV, statistic, ';');
        statisticOutputTXT = new SimpleStatisticOutput(estatisticasTXT, statistic);

//        statisticOutput = new SimpleStatisticOutput(System.out, statistic);
        statistic.setStartTime();

        NaiveTransitiveClosureGenerator fechoTrans = new NaiveTransitiveClosureGenerator();

        //Gerando o fecho transitivo
        for (DuDeObjectPair pair : algorithm) {

            final double similarity = similarityFunc.getSimilarity(pair);
            final double similarity2 = avg.getSimilarity(pair);

            //Baseado em ALg14
            if ((similarity >= 0.75) && (similarity2 >= 0.7)) {
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
            //output.write(pair);

            try {

                a = similarityFunc.getSimilarity(pair);
                b = avg.getSimilarity(pair);
//                c = avg.getSimilarity(pair);
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
        Alg31 obj1 = new Alg31("cd", "pk", "cd_gold", "disc1_id", "disc2_id", 31);
        try {
            obj1.executaDedupAlg();
        } catch (IOException ex) {
            Logger.getLogger(Alg31.class.getName()).log(Level.SEVERE, null, ex);
        }
        java.awt.Toolkit.getDefaultToolkit().beep();
    }

}
