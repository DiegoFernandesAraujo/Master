/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dedupalgorithms.dblpacm;

import dedupalgorithms.cds.*;
import dedupalgorithms.DedupAlg;
import dude.algorithm.Algorithm;
import dude.output.CSVOutput;
import dude.output.DuDeOutput;
import dude.output.statisticoutput.CSVStatisticOutput;
import dude.output.statisticoutput.SimpleStatisticOutput;
import dude.output.statisticoutput.StatisticOutput;
import dude.postprocessor.NaiveTransitiveClosureGenerator;
import dude.postprocessor.StatisticComponent;
import dude.similarityfunction.contentbased.impl.simmetrics.CosineSimilarityFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.DiceCoefficientFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.EuclideanDistanceFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.JaccardSimilarityFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.JaroDistanceFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.JaroWinklerFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.LevenshteinDistanceFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.MatchingCoefficientFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.MongeElkanFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.NeedlemanWunschFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.OverlapCoefficientFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.SmithWatermanFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.SmithWatermanGotohFunction;
import dude.util.GoldStandard;
import dude.util.data.DuDeObjectPair;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diego
 */
public class AlgTeste extends DedupAlg {

    FileWriter escreveArqPadr = null;
    BufferedWriter bwArqPadr = null;

    FileWriter escreveResult;
    File estatisticasCSV;
    File estatisticasTXT;
    String dir = "resultsDedup/dblpacm";

    public AlgTeste(String baseDados1, String baseDados2, String chavePrimaria1, String chavePrimaria2, String gold, String goldId1, String goldId2, int ordem) {
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

        LevenshteinDistanceFunction similarityFuncA = new LevenshteinDistanceFunction("title");

        CosineSimilarityFunction similarityFuncB = new CosineSimilarityFunction("title");

        DiceCoefficientFunction similarityFuncC = new DiceCoefficientFunction("title");

        EuclideanDistanceFunction similarityFuncD = new EuclideanDistanceFunction("title");

        JaccardSimilarityFunction similarityFuncE = new JaccardSimilarityFunction("title");

        JaroDistanceFunction similarityFuncF = new JaroDistanceFunction("title");

        JaroWinklerFunction similarityFuncG = new JaroWinklerFunction("title");

        MatchingCoefficientFunction similarityFuncH = new MatchingCoefficientFunction("title");

        MongeElkanFunction similarityFuncI = new MongeElkanFunction("title");

        NeedlemanWunschFunction similarityFuncJ = new NeedlemanWunschFunction("title");

        OverlapCoefficientFunction similarityFuncK = new OverlapCoefficientFunction("title");

        SmithWatermanFunction similarityFuncL = new SmithWatermanFunction("title");

        SmithWatermanGotohFunction similarityFuncM = new SmithWatermanGotohFunction("title");

        LevenshteinDistanceFunction similarityFunc1A = new LevenshteinDistanceFunction("authors");

        CosineSimilarityFunction similarityFunc1B = new CosineSimilarityFunction("authors");

        DiceCoefficientFunction similarityFunc1C = new DiceCoefficientFunction("authors");

        EuclideanDistanceFunction similarityFunc1D = new EuclideanDistanceFunction("authors");

        JaccardSimilarityFunction similarityFunc1E = new JaccardSimilarityFunction("authors");

        JaroDistanceFunction similarityFunc1F = new JaroDistanceFunction("authors");

        JaroWinklerFunction similarityFunc1G = new JaroWinklerFunction("authors");

        MatchingCoefficientFunction similarityFunc1H = new MatchingCoefficientFunction("authors");

        MongeElkanFunction similarityFunc1I = new MongeElkanFunction("authors");

        NeedlemanWunschFunction similarityFunc1J = new NeedlemanWunschFunction("authors");

        OverlapCoefficientFunction similarityFunc1K = new OverlapCoefficientFunction("authors");

        SmithWatermanFunction similarityFunc1L = new SmithWatermanFunction("authors");

        SmithWatermanGotohFunction similarityFunc1M = new SmithWatermanGotohFunction("authors");

        LevenshteinDistanceFunction similarityFunc2A = new LevenshteinDistanceFunction("venue");

        CosineSimilarityFunction similarityFunc2B = new CosineSimilarityFunction("venue");

        DiceCoefficientFunction similarityFunc2C = new DiceCoefficientFunction("venue");

        EuclideanDistanceFunction similarityFunc2D = new EuclideanDistanceFunction("venue");

        JaccardSimilarityFunction similarityFunc2E = new JaccardSimilarityFunction("venue");

        JaroDistanceFunction similarityFunc2F = new JaroDistanceFunction("venue");

        JaroWinklerFunction similarityFunc2G = new JaroWinklerFunction("venue");

        MatchingCoefficientFunction similarityFunc2H = new MatchingCoefficientFunction("venue");

        MongeElkanFunction similarityFunc2I = new MongeElkanFunction("venue");

        NeedlemanWunschFunction similarityFunc2J = new NeedlemanWunschFunction("venue");

        OverlapCoefficientFunction similarityFunc2K = new OverlapCoefficientFunction("venue");

        SmithWatermanFunction similarityFunc2L = new SmithWatermanFunction("venue");

        SmithWatermanGotohFunction similarityFunc2M = new SmithWatermanGotohFunction("venue");

        LevenshteinDistanceFunction similarityFunc3A = new LevenshteinDistanceFunction("year");

        CosineSimilarityFunction similarityFunc3B = new CosineSimilarityFunction("year");

        DiceCoefficientFunction similarityFunc3C = new DiceCoefficientFunction("year");

        EuclideanDistanceFunction similarityFunc3D = new EuclideanDistanceFunction("year");

        JaccardSimilarityFunction similarityFunc3E = new JaccardSimilarityFunction("year");

        JaroDistanceFunction similarityFunc3F = new JaroDistanceFunction("year");

        JaroWinklerFunction similarityFunc3G = new JaroWinklerFunction("year");

        MatchingCoefficientFunction similarityFunc3H = new MatchingCoefficientFunction("year");

        MongeElkanFunction similarityFunc3I = new MongeElkanFunction("year");

        NeedlemanWunschFunction similarityFunc3J = new NeedlemanWunschFunction("year");

        OverlapCoefficientFunction similarityFunc3K = new OverlapCoefficientFunction("year");

        SmithWatermanFunction similarityFunc3L = new SmithWatermanFunction("year");

        SmithWatermanGotohFunction similarityFunc3M = new SmithWatermanGotohFunction("year");

//        similarityFunc.ignoreCapitalization();
//        similarityFunc2.ignoreCapitalization();
//        similarityFunc3.ignoreCapitalization();
//        similarityFunc4.ignoreCapitalization();
//        AbsoluteNumberDiffFunction similarityFunc5 = new AbsoluteNumberDiffFunction(10, "year");
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
        try {

            escreveArqPadr = new FileWriter("./src/csv/" + dir + "similaridades.csv", false);
            bwArqPadr = new BufferedWriter(escreveArqPadr);
            
            System.out.println("Tá funcionando!");

            //Gerando o fecho transitivo
            for (DuDeObjectPair pair : algorithm) {

                final double similarityA = similarityFuncA.getSimilarity(pair);
                final double similarityB = similarityFuncB.getSimilarity(pair);
                final double similarityC = similarityFuncC.getSimilarity(pair);
                final double similarityD = similarityFuncD.getSimilarity(pair);
                final double similarityE = similarityFuncE.getSimilarity(pair);
                final double similarityF = similarityFuncF.getSimilarity(pair);
                final double similarityG = similarityFuncG.getSimilarity(pair);
                final double similarityH = similarityFuncH.getSimilarity(pair);
                final double similarityI = similarityFuncI.getSimilarity(pair);
                final double similarityJ = similarityFuncJ.getSimilarity(pair);
                final double similarityK = similarityFuncK.getSimilarity(pair);
                final double similarityL = similarityFuncL.getSimilarity(pair);
                final double similarityM = similarityFuncM.getSimilarity(pair);

                final double similarity1A = similarityFunc1A.getSimilarity(pair);
                final double similarity1B = similarityFunc1B.getSimilarity(pair);
                final double similarity1C = similarityFunc1C.getSimilarity(pair);
                final double similarity1D = similarityFunc1D.getSimilarity(pair);
                final double similarity1E = similarityFunc1E.getSimilarity(pair);
                final double similarity1F = similarityFunc1F.getSimilarity(pair);
                final double similarity1G = similarityFunc1G.getSimilarity(pair);
                final double similarity1H = similarityFunc1H.getSimilarity(pair);
                final double similarity1I = similarityFunc1I.getSimilarity(pair);
                final double similarity1J = similarityFunc1J.getSimilarity(pair);
                final double similarity1K = similarityFunc1K.getSimilarity(pair);
                final double similarity1L = similarityFunc1L.getSimilarity(pair);
                final double similarity1M = similarityFunc1M.getSimilarity(pair);

                final double similarity2A = similarityFunc2A.getSimilarity(pair);
                final double similarity2B = similarityFunc2B.getSimilarity(pair);
                final double similarity2C = similarityFunc2C.getSimilarity(pair);
                final double similarity2D = similarityFunc2D.getSimilarity(pair);
                final double similarity2E = similarityFunc2E.getSimilarity(pair);
                final double similarity2F = similarityFunc2F.getSimilarity(pair);
                final double similarity2G = similarityFunc2G.getSimilarity(pair);
                final double similarity2H = similarityFunc2H.getSimilarity(pair);
                final double similarity2I = similarityFunc2I.getSimilarity(pair);
                final double similarity2J = similarityFunc2J.getSimilarity(pair);
                final double similarity2K = similarityFunc2K.getSimilarity(pair);
                final double similarity2L = similarityFunc2L.getSimilarity(pair);
                final double similarity2M = similarityFunc2M.getSimilarity(pair);

                final double similarity3A = similarityFunc3A.getSimilarity(pair);
                final double similarity3B = similarityFunc3B.getSimilarity(pair);
                final double similarity3C = similarityFunc3C.getSimilarity(pair);
                final double similarity3D = similarityFunc3D.getSimilarity(pair);
                final double similarity3E = similarityFunc3E.getSimilarity(pair);
                final double similarity3F = similarityFunc3F.getSimilarity(pair);
                final double similarity3G = similarityFunc3G.getSimilarity(pair);
                final double similarity3H = similarityFunc3H.getSimilarity(pair);
                final double similarity3I = similarityFunc3I.getSimilarity(pair);
                final double similarity3J = similarityFunc3J.getSimilarity(pair);
                final double similarity3K = similarityFunc3K.getSimilarity(pair);
                final double similarity3L = similarityFunc3L.getSimilarity(pair);
                final double similarity3M = similarityFunc3M.getSimilarity(pair);

//            System.out.println(pair.getFirstElement().getAttributeValue("year").toString() + "-" + pair.getSecondElement().getAttributeValue("year").toString());
//            System.out.println(similarityFunc5.calculateSimilarity(pair.getFirstElement().getAttributeValue("year"), pair.getSecondElement().getAttributeValue("year")));
//            System.out.println(similarityFunc5.getSimilarity(pair));
//            System.out.println("");
//                System.out.println(similarity + "," + similarity2 + "," + similarity3 + "," + (similarity4));
//            System.out.println(similarity2 /*&& (similarity4 >= 0.85)*/);
                bwArqPadr.append(Double.toString(similarityA));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarityB));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarityC));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarityD));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarityE));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarityF));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarityG));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarityH));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarityI));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarityJ));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarityK));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarityL));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarityM));
                bwArqPadr.append(',');
                bwArqPadr.append('-');
                bwArqPadr.append(',');

                bwArqPadr.append(Double.toString(similarity1A));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity1B));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity1C));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity1D));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity1E));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity1F));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity1G));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity1H));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity1I));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity1J));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity1K));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity1L));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity1M));
                bwArqPadr.append(',');
                bwArqPadr.append('-');
                bwArqPadr.append(',');

                bwArqPadr.append(Double.toString(similarity2A));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity2B));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity2C));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity2D));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity2E));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity2F));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity2G));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity2H));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity2I));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity2J));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity2K));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity2L));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity2M));
                bwArqPadr.append(',');
                bwArqPadr.append('-');
                bwArqPadr.append(',');

                bwArqPadr.append(Double.toString(similarity3A));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity3B));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity3C));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity3D));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity3E));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity3F));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity3G));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity3H));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity3I));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity3J));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity3K));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity3L));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity3M));
                bwArqPadr.append('\n');

//            if ((similarity >= 0.9) && (similarity2 >= 0.9) && (similarity3 >= 0.9) && (similarity4 >= 0.75)) {
////            if ((similarity >= 0.9) && (similarity2 >= 0.9) && (similarity3 >= 0.9) /*&& (similarity4 >= 0.85)*/) {
//                fechoTrans.add(pair);
//                System.out.println("Elemento 1: " + pair.getFirstElement() + " - " + "Elemento 2: " + pair.getSecondElement());
//                System.out.println("Similaridade: " + pair.getSimilarity());
//                System.out.println("");
//
//            } else {
//                try {
//                    statistic.addNonDuplicate(pair);
//                } catch (ExtractionFailedException ex) {
//                    Logger.getLogger(Alg1.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
                bwArqPadr.flush();
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            escreveArqPadr.flush();
            escreveArqPadr.close();
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
        AlgTeste obj1 = new AlgTeste("DBLP2", "ACM", "id", "id", "DBLP2-ACM_perfectMapping", "idDBLP", "idACM", 1);
        try {
            obj1.executaDedupAlg();
        } catch (IOException ex) {
            Logger.getLogger(Alg10.class.getName()).log(Level.SEVERE, null, ex);
        }
        java.awt.Toolkit.getDefaultToolkit().beep();
    }

}
