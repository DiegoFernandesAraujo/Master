/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dedupalgorithms;

import dude.algorithm.Algorithm;
import dude.output.CSVOutput;
import dude.output.DuDeOutput;
import dude.output.statisticoutput.CSVStatisticOutput;
import dude.output.statisticoutput.SimpleStatisticOutput;
import dude.output.statisticoutput.StatisticOutput;
import dude.postprocessor.NaiveTransitiveClosureGenerator;
import dude.postprocessor.StatisticComponent;
import dude.similarityfunction.aggregators.Average;
import dude.similarityfunction.aggregators.Maximum;
import dude.similarityfunction.contentbased.impl.simmetrics.JaroDistanceFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.LevenshteinDistanceFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.SmithWatermanFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.SmithWatermanFunction;
import dude.util.GoldStandard;
import dude.util.data.DuDeObjectPair;
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
public class Alg4 extends DedupAlg {

    FileWriter escreveResult;
    File estatisticasCSV;
    File estatisticasTXT;

    public Alg4(String baseDados1, String chavePrimaria, String gold, String goldId1, String goldId2, String result, int ordem) {
        super(baseDados1, chavePrimaria, gold, goldId1, goldId2, result);

        estatisticasCSV = new File("./src/csv/resultsDedup/estatisticas", "estatisticasDedup" + ordem + ".csv");
        estatisticasTXT = new File("./src/csv/resultsDedup/estatisticas", "estatisticasDedup" + ordem + ".txt");

        try {
            this.escreveResult = new FileWriter(new File("./src/csv/resultsDedup", "resultado" + ordem + ".csv"));

        } catch (IOException ex) {
            Logger.getLogger(Alg4.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    File resultado = new File("./src/csv/datasets", "resultado.csv");
    @Override
    public void executaDedupAlg() throws IOException {

        GoldStandard goldStandard = getGS();

        Algorithm algorithm = getAlg();
        algorithm.enableInMemoryProcessing();

        LevenshteinDistanceFunction similarityFunc = new LevenshteinDistanceFunction("title");
        JaroDistanceFunction similarityFunc2 = new JaroDistanceFunction("artist");
        LevenshteinDistanceFunction similarityFunc3 = new LevenshteinDistanceFunction("title");
        LevenshteinDistanceFunction similarityFunc4 = new LevenshteinDistanceFunction("track01");
        LevenshteinDistanceFunction similarityFunc5 = new LevenshteinDistanceFunction("track02");
        
        Maximum max = new Maximum();
        max.add(similarityFunc);
        max.add(similarityFunc2);
        
        Average avg = new Average();
        avg.add(max);
        avg.add(similarityFunc3);
        
        Average avg2 = new Average();
        avg2.add(similarityFunc4);
        avg2.add(similarityFunc5);
        

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

        int cont = 0;
        
        //Gerando o fecho transitivo
        for (DuDeObjectPair pair : algorithm) {
            
            cont++;
            final double similarity = avg.getSimilarity(pair);
            final double similarity2 = avg2.getSimilarity(pair);
            
            if ( (similarity >= 0.9) && (similarity2 >= 0.9) ) {
                fechoTrans.add(pair);

            } else {
                statistic.addNonDuplicate(pair);
            }
            
            if(cont % 10 == 0){
                System.out.print(".");
                if(cont % 100 == 0){
                    System.out.println("");
                }
            }
            
        }

        
        for (DuDeObjectPair pair : fechoTrans) {

            statistic.addDuplicate(pair);
            output.write(pair);

        }

        statistic.setEndTime();

        statisticOutputCSV.writeStatistics();
        statisticOutputTXT.writeStatistics();

        algorithm.cleanUp();
        goldStandard.close();

    }

    public static void main(String[] args) {
        Alg4 obj1 = new Alg4("cd", "pk", "cd_gold", "disc1_id", "disc2_id", "cd_result", 3);
        try {
            obj1.executaDedupAlg();
        } catch (IOException ex) {
            Logger.getLogger(Alg4.class.getName()).log(Level.SEVERE, null, ex);
        }
        java.awt.Toolkit.getDefaultToolkit().beep();
    }

}
