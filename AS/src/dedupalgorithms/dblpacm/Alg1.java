/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class Alg1 extends DedupAlg {

    FileWriter escreveResult;
    File estatisticasCSV;
    File estatisticasTXT;
    String dir = "resultsDedup/dblpacm";

    public Alg1(String baseDados1, String baseDados2, String chavePrimaria1, String chavePrimaria2, String gold, String goldId1, String goldId2, int ordem) {
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
            Logger.getLogger(Alg1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    File resultado = new File("./src/csv/datasets", "resultado.csv");
    @Override
    public void executaDedupAlg() throws IOException {

        GoldStandard goldStandard = getGS();

        Algorithm algorithm = getAlg();
        algorithm.enableInMemoryProcessing();

        LevenshteinDistanceFunction similarityFunc = new LevenshteinDistanceFunction("title");
        
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
            
            if ((similarity >= 0.8)) {
                fechoTrans.add(pair);

            } else {
                try{
                statistic.addNonDuplicate(pair);
                }catch(ExtractionFailedException ex){
//                    System.out.println(pair.getFirstElement());
//                    System.out.println(pair.getSecondElement());
                    Logger.getLogger(Alg1.class.getName()).log(Level.SEVERE, null, ex);
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
//        Alg1 obj1 = new Alg1("DBLP2", "ACM", "num", "id", "DBLP2-ACM_perfectMapping_NEW", "idDBLP", "idACM", 1);
        Alg1 obj1 = new Alg1("DBLP2", "ACM", "id", "id", "DBLP2-ACM_perfectMapping", "idDBLP", "idACM", 1);
        try {
            obj1.executaDedupAlg();
        } catch (IOException ex) {
            Logger.getLogger(Alg10.class.getName()).log(Level.SEVERE, null, ex);
        }
        java.awt.Toolkit.getDefaultToolkit().beep();
    }

}
