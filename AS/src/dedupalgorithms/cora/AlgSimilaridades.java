/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dedupalgorithms.cora;

import dedupalgorithms.DedupAlg;
import dude.algorithm.Algorithm;
import dude.output.CSVOutput;
import dude.output.DuDeOutput;
import dude.output.statisticoutput.CSVStatisticOutput;
import dude.output.statisticoutput.SimpleStatisticOutput;
import dude.output.statisticoutput.StatisticOutput;
import dude.postprocessor.NaiveTransitiveClosureGenerator;
import dude.postprocessor.StatisticComponent;
import dude.similarityfunction.contentbased.calculationstrategy.ArrayConversionStrategy;
import dude.similarityfunction.contentbased.calculationstrategy.AverageArrayArrayStrategy;
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
import uk.ac.shef.wit.simmetrics.similaritymetrics.Soundex;

/**
 *
 * @author Diego
 */
public class AlgSimilaridades extends DedupAlg {

    FileWriter escreveArqPadr = null;
    BufferedWriter bwArqPadr = null;

    FileWriter escreveResult;
    File estatisticasCSV;
    File estatisticasTXT;
    String dir;

    public AlgSimilaridades(String baseDados1, String chavePrimaria, String gold, String goldId1, String goldId2, int ordem) {
        super(baseDados1, chavePrimaria, gold, goldId1, goldId2, ';', "XML");

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
            Logger.getLogger(AlgSimilaridades.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    File resultado = new File("./src/csv/datasets", "resultado.csv");
    @Override
    public void executaDedupAlg() throws IOException {

        String firstSoundex = null;
        String secondSoundex = null;

        String firstSoundex1 = null;
        String secondSoundex1 = null;

        String firstSoundex2 = null;
        String secondSoundex2 = null;

        String firstSoundex3 = null;
        String secondSoundex3 = null;

        String firstSoundex4 = null;
        String secondSoundex4 = null;

        String firstSoundex5 = null;
        String secondSoundex5 = null;

        GoldStandard goldStandard = getGS();

        Algorithm algorithm = getAlg();
        algorithm.enableInMemoryProcessing();

        Soundex similarityFuncSOUNDEX = new Soundex();

        LevenshteinDistanceFunction similarityFuncA = new LevenshteinDistanceFunction("author");
        similarityFuncA.setCompareArrayArrayStrategy(new AverageArrayArrayStrategy());
        similarityFuncA.setCompareArrayAtomicStrategy(new ArrayConversionStrategy());

        CosineSimilarityFunction similarityFuncB = new CosineSimilarityFunction("author");
        similarityFuncB.setCompareArrayArrayStrategy(new AverageArrayArrayStrategy());
        similarityFuncB.setCompareArrayAtomicStrategy(new ArrayConversionStrategy());

        DiceCoefficientFunction similarityFuncC = new DiceCoefficientFunction("author");
        similarityFuncC.setCompareArrayArrayStrategy(new AverageArrayArrayStrategy());
        similarityFuncC.setCompareArrayAtomicStrategy(new ArrayConversionStrategy());

        EuclideanDistanceFunction similarityFuncD = new EuclideanDistanceFunction("author");
        similarityFuncD.setCompareArrayArrayStrategy(new AverageArrayArrayStrategy());
        similarityFuncD.setCompareArrayAtomicStrategy(new ArrayConversionStrategy());

        JaccardSimilarityFunction similarityFuncE = new JaccardSimilarityFunction("author");
        similarityFuncE.setCompareArrayArrayStrategy(new AverageArrayArrayStrategy());
        similarityFuncE.setCompareArrayAtomicStrategy(new ArrayConversionStrategy());

        JaroDistanceFunction similarityFuncF = new JaroDistanceFunction("author");
        similarityFuncF.setCompareArrayArrayStrategy(new AverageArrayArrayStrategy());
        similarityFuncF.setCompareArrayAtomicStrategy(new ArrayConversionStrategy());

        JaroWinklerFunction similarityFuncG = new JaroWinklerFunction("author");
        similarityFuncG.setCompareArrayArrayStrategy(new AverageArrayArrayStrategy());
        similarityFuncG.setCompareArrayAtomicStrategy(new ArrayConversionStrategy());

        MatchingCoefficientFunction similarityFuncH = new MatchingCoefficientFunction("author");
        similarityFuncH.setCompareArrayArrayStrategy(new AverageArrayArrayStrategy());
        similarityFuncH.setCompareArrayAtomicStrategy(new ArrayConversionStrategy());

        MongeElkanFunction similarityFuncI = new MongeElkanFunction("author");
        similarityFuncI.setCompareArrayArrayStrategy(new AverageArrayArrayStrategy());
        similarityFuncI.setCompareArrayAtomicStrategy(new ArrayConversionStrategy());

        NeedlemanWunschFunction similarityFuncJ = new NeedlemanWunschFunction("author");
        similarityFuncJ.setCompareArrayArrayStrategy(new AverageArrayArrayStrategy());
        similarityFuncJ.setCompareArrayAtomicStrategy(new ArrayConversionStrategy());

        OverlapCoefficientFunction similarityFuncK = new OverlapCoefficientFunction("author");
        similarityFuncK.setCompareArrayArrayStrategy(new AverageArrayArrayStrategy());
        similarityFuncK.setCompareArrayAtomicStrategy(new ArrayConversionStrategy());

        SmithWatermanFunction similarityFuncL = new SmithWatermanFunction("author");
        similarityFuncL.setCompareArrayArrayStrategy(new AverageArrayArrayStrategy());
        similarityFuncL.setCompareArrayAtomicStrategy(new ArrayConversionStrategy());

        SmithWatermanGotohFunction similarityFuncM = new SmithWatermanGotohFunction("author");
        similarityFuncM.setCompareArrayArrayStrategy(new AverageArrayArrayStrategy());
        similarityFuncM.setCompareArrayAtomicStrategy(new ArrayConversionStrategy());

        LevenshteinDistanceFunction similarityFunc1A = new LevenshteinDistanceFunction("title");

        CosineSimilarityFunction similarityFunc1B = new CosineSimilarityFunction("title");

        DiceCoefficientFunction similarityFunc1C = new DiceCoefficientFunction("title");

        EuclideanDistanceFunction similarityFunc1D = new EuclideanDistanceFunction("title");

        JaccardSimilarityFunction similarityFunc1E = new JaccardSimilarityFunction("title");

        JaroDistanceFunction similarityFunc1F = new JaroDistanceFunction("title");

        JaroWinklerFunction similarityFunc1G = new JaroWinklerFunction("title");

        MatchingCoefficientFunction similarityFunc1H = new MatchingCoefficientFunction("title");

        MongeElkanFunction similarityFunc1I = new MongeElkanFunction("title");

        NeedlemanWunschFunction similarityFunc1J = new NeedlemanWunschFunction("title");

        OverlapCoefficientFunction similarityFunc1K = new OverlapCoefficientFunction("title");

        SmithWatermanFunction similarityFunc1L = new SmithWatermanFunction("title");

        SmithWatermanGotohFunction similarityFunc1M = new SmithWatermanGotohFunction("title");

        LevenshteinDistanceFunction similarityFunc2A = new LevenshteinDistanceFunction("journal");

        CosineSimilarityFunction similarityFunc2B = new CosineSimilarityFunction("journal");

        DiceCoefficientFunction similarityFunc2C = new DiceCoefficientFunction("journal");

        EuclideanDistanceFunction similarityFunc2D = new EuclideanDistanceFunction("journal");

        JaccardSimilarityFunction similarityFunc2E = new JaccardSimilarityFunction("journal");

        JaroDistanceFunction similarityFunc2F = new JaroDistanceFunction("journal");

        JaroWinklerFunction similarityFunc2G = new JaroWinklerFunction("journal");

        MatchingCoefficientFunction similarityFunc2H = new MatchingCoefficientFunction("journal");

        MongeElkanFunction similarityFunc2I = new MongeElkanFunction("journal");

        NeedlemanWunschFunction similarityFunc2J = new NeedlemanWunschFunction("journal");

        OverlapCoefficientFunction similarityFunc2K = new OverlapCoefficientFunction("journal");

        SmithWatermanFunction similarityFunc2L = new SmithWatermanFunction("journal");

        SmithWatermanGotohFunction similarityFunc2M = new SmithWatermanGotohFunction("journal");

        LevenshteinDistanceFunction similarityFunc3A = new LevenshteinDistanceFunction("volume");

        CosineSimilarityFunction similarityFunc3B = new CosineSimilarityFunction("volume");

        DiceCoefficientFunction similarityFunc3C = new DiceCoefficientFunction("volume");

        EuclideanDistanceFunction similarityFunc3D = new EuclideanDistanceFunction("volume");

        JaccardSimilarityFunction similarityFunc3E = new JaccardSimilarityFunction("volume");

        JaroDistanceFunction similarityFunc3F = new JaroDistanceFunction("volume");

        JaroWinklerFunction similarityFunc3G = new JaroWinklerFunction("volume");

        MatchingCoefficientFunction similarityFunc3H = new MatchingCoefficientFunction("volume");

        MongeElkanFunction similarityFunc3I = new MongeElkanFunction("volume");

        NeedlemanWunschFunction similarityFunc3J = new NeedlemanWunschFunction("volume");

        OverlapCoefficientFunction similarityFunc3K = new OverlapCoefficientFunction("volume");

        SmithWatermanFunction similarityFunc3L = new SmithWatermanFunction("volume");

        SmithWatermanGotohFunction similarityFunc3M = new SmithWatermanGotohFunction("volume");

        LevenshteinDistanceFunction similarityFunc4A = new LevenshteinDistanceFunction("pages");

        CosineSimilarityFunction similarityFunc4B = new CosineSimilarityFunction("pages");

        DiceCoefficientFunction similarityFunc4C = new DiceCoefficientFunction("pages");

        EuclideanDistanceFunction similarityFunc4D = new EuclideanDistanceFunction("pages");

        JaccardSimilarityFunction similarityFunc4E = new JaccardSimilarityFunction("pages");

        JaroDistanceFunction similarityFunc4F = new JaroDistanceFunction("pages");

        JaroWinklerFunction similarityFunc4G = new JaroWinklerFunction("pages");

        MatchingCoefficientFunction similarityFunc4H = new MatchingCoefficientFunction("pages");

        MongeElkanFunction similarityFunc4I = new MongeElkanFunction("pages");

        NeedlemanWunschFunction similarityFunc4J = new NeedlemanWunschFunction("pages");

        OverlapCoefficientFunction similarityFunc4K = new OverlapCoefficientFunction("pages");

        SmithWatermanFunction similarityFunc4L = new SmithWatermanFunction("pages");

        SmithWatermanGotohFunction similarityFunc4M = new SmithWatermanGotohFunction("pages");

        LevenshteinDistanceFunction similarityFunc5A = new LevenshteinDistanceFunction("date");

        CosineSimilarityFunction similarityFunc5B = new CosineSimilarityFunction("date");

        DiceCoefficientFunction similarityFunc5C = new DiceCoefficientFunction("date");

        EuclideanDistanceFunction similarityFunc5D = new EuclideanDistanceFunction("date");

        JaccardSimilarityFunction similarityFunc5E = new JaccardSimilarityFunction("date");

        JaroDistanceFunction similarityFunc5F = new JaroDistanceFunction("date");

        JaroWinklerFunction similarityFunc5G = new JaroWinklerFunction("date");

        MatchingCoefficientFunction similarityFunc5H = new MatchingCoefficientFunction("date");

        MongeElkanFunction similarityFunc5I = new MongeElkanFunction("date");

        NeedlemanWunschFunction similarityFunc5J = new NeedlemanWunschFunction("date");

        OverlapCoefficientFunction similarityFunc5K = new OverlapCoefficientFunction("date");

        SmithWatermanFunction similarityFunc5L = new SmithWatermanFunction("date");

        SmithWatermanGotohFunction similarityFunc5M = new SmithWatermanGotohFunction("date");

        DuDeOutput output = new CSVOutput(escreveResult);

        StatisticComponent statistic = new StatisticComponent(goldStandard, algorithm);

        StatisticOutput statisticOutputCSV;
        StatisticOutput statisticOutputTXT;

        statisticOutputCSV = new CSVStatisticOutput(estatisticasCSV, statistic, ';');
        statisticOutputTXT = new SimpleStatisticOutput(estatisticasTXT, statistic);

        statistic.setStartTime();

        NaiveTransitiveClosureGenerator fechoTrans = new NaiveTransitiveClosureGenerator();
        try {

            escreveArqPadr = new FileWriter("./src/csv/" + dir + "/similaridades.csv", false);
            System.out.println("./src/csv/" + dir + "/similaridades.csv");
            bwArqPadr = new BufferedWriter(escreveArqPadr);

            System.out.println("Tá funcionando!");

            int cont = 0;
            int contPrint = 0;

            //Gerando o fecho transitivo
            for (DuDeObjectPair pair : algorithm) {

                if (++cont > 10000) {
                    break;
                }
//                try {
//                    firstSoundex = pair.getFirstElement().getAttributeValues("author").toString();
//                    secondSoundex = pair.getSecondElement().getAttributeValues("author").toString();
//
//                    firstSoundex1 = pair.getFirstElement().getAttributeValues("title").toString();
//                    secondSoundex1 = pair.getSecondElement().getAttributeValues("title").toString();
//
//                    firstSoundex2 = pair.getFirstElement().getAttributeValues("journal").toString();
//                    secondSoundex2 = pair.getSecondElement().getAttributeValues("journal").toString();
//
//                    firstSoundex3 = pair.getFirstElement().getAttributeValues("volume").toString();
//                    secondSoundex3 = pair.getSecondElement().getAttributeValues("volume").toString();
//
//                    firstSoundex4 = pair.getFirstElement().getAttributeValues("pages").toString();
//                    secondSoundex4 = pair.getSecondElement().getAttributeValues("pages").toString();
//
//                    firstSoundex5 = pair.getFirstElement().getAttributeValues("date").toString();
//                    secondSoundex5 = pair.getSecondElement().getAttributeValues("date").toString();
//                } catch (Exception e) {
//                    System.out.println("ANTES");
//                    System.out.println("MSG: " + e.getMessage());
//                    System.out.println("DEPOIS");
//                }

                double similarityA = 0;
                double similarityB = 0;
                double similarityC = 0;
                double similarityD = 0;
                double similarityE = 0;
                double similarityF = 0;
                double similarityG = 0;
                double similarityH = 0;
                double similarityI = 0;
                double similarityJ = 0;
                double similarityK = 0;
                double similarityL = 0;
                double similarityM = 0;
                String similarityN = null;
                double similarity1A = 0;
                double similarity1B = 0;
                double similarity1C = 0;
                double similarity1D = 0;
                double similarity1E = 0;
                double similarity1F = 0;
                double similarity1G = 0;
                double similarity1H = 0;
                double similarity1I = 0;
                double similarity1J = 0;
                double similarity1K = 0;
                double similarity1L = 0;
                double similarity1M = 0;
                String similarity1N = null;
                double similarity2A = 0;
                double similarity2B = 0;
                double similarity2C = 0;
                double similarity2D = 0;
                double similarity2E = 0;
                double similarity2F = 0;
                double similarity2G = 0;
                double similarity2H = 0;
                double similarity2I = 0;
                double similarity2J = 0;
                double similarity2K = 0;
                double similarity2L = 0;
                double similarity2M = 0;
                String similarity2N = null;
                double similarity3A = 0;
                double similarity3B = 0;
                double similarity3C = 0;
                double similarity3D = 0;
                double similarity3E = 0;
                double similarity3F = 0;
                double similarity3G = 0;
                double similarity3H = 0;
                double similarity3I = 0;
                double similarity3J = 0;
                double similarity3K = 0;
                double similarity3L = 0;
                double similarity3M = 0;
                String similarity3N = null;
                double similarity4A = 0;
                double similarity4B = 0;
                double similarity4C = 0;
                double similarity4D = 0;
                double similarity4E = 0;
                double similarity4F = 0;
                double similarity4G = 0;
                double similarity4H = 0;
                double similarity4I = 0;
                double similarity4J = 0;
                double similarity4K = 0;
                double similarity4L = 0;
                double similarity4M = 0;
                String similarity4N = null;
                double similarity5A = 0;
                double similarity5B = 0;
                double similarity5C = 0;
                double similarity5D = 0;
                double similarity5E = 0;
                double similarity5F = 0;
                double similarity5G = 0;
                double similarity5H = 0;
                double similarity5I = 0;
                double similarity5J = 0;
                double similarity5K = 0;
                double similarity5L = 0;
                double similarity5M = 0;
                String similarity5N = null;

                try {
                    similarityA = similarityFuncA.getSimilarity(pair);
                    similarityB = similarityFuncB.getSimilarity(pair);
                    similarityC = similarityFuncC.getSimilarity(pair);
                    similarityD = similarityFuncD.getSimilarity(pair);
                    similarityE = similarityFuncE.getSimilarity(pair);
                    similarityF = similarityFuncF.getSimilarity(pair);
                    similarityG = similarityFuncG.getSimilarity(pair);
                    similarityH = similarityFuncH.getSimilarity(pair);
                    similarityI = similarityFuncI.getSimilarity(pair);
                    similarityJ = similarityFuncJ.getSimilarity(pair);
                    similarityK = similarityFuncK.getSimilarity(pair);
                    similarityL = similarityFuncL.getSimilarity(pair);
                    similarityM = similarityFuncM.getSimilarity(pair);
//                    similarityN = Double.toString(similarityFuncSOUNDEX.getSimilarity(firstSoundex, secondSoundex));

                    similarity1A = similarityFunc1A.getSimilarity(pair);
                    similarity1B = similarityFunc1B.getSimilarity(pair);
                    similarity1C = similarityFunc1C.getSimilarity(pair);
                    similarity1D = similarityFunc1D.getSimilarity(pair);
                    similarity1E = similarityFunc1E.getSimilarity(pair);
                    similarity1F = similarityFunc1F.getSimilarity(pair);
                    similarity1G = similarityFunc1G.getSimilarity(pair);
                    similarity1H = similarityFunc1H.getSimilarity(pair);
                    similarity1I = similarityFunc1I.getSimilarity(pair);
                    similarity1J = similarityFunc1J.getSimilarity(pair);
                    similarity1K = similarityFunc1K.getSimilarity(pair);
                    similarity1L = similarityFunc1L.getSimilarity(pair);
                    similarity1M = similarityFunc1M.getSimilarity(pair);
//                    similarity1N = Double.toString(similarityFuncSOUNDEX.getSimilarity(firstSoundex1, secondSoundex1));

                    similarity2A = similarityFunc2A.getSimilarity(pair);
                    similarity2B = similarityFunc2B.getSimilarity(pair);
                    similarity2C = similarityFunc2C.getSimilarity(pair);
                    similarity2D = similarityFunc2D.getSimilarity(pair);
                    similarity2E = similarityFunc2E.getSimilarity(pair);
                    similarity2F = similarityFunc2F.getSimilarity(pair);
                    similarity2G = similarityFunc2G.getSimilarity(pair);
                    similarity2H = similarityFunc2H.getSimilarity(pair);
                    similarity2I = similarityFunc2I.getSimilarity(pair);
                    similarity2J = similarityFunc2J.getSimilarity(pair);
                    similarity2K = similarityFunc2K.getSimilarity(pair);
                    similarity2L = similarityFunc2L.getSimilarity(pair);
                    similarity2M = similarityFunc2M.getSimilarity(pair);
//                    similarity2N = Double.toString(similarityFuncSOUNDEX.getSimilarity(firstSoundex2, secondSoundex2));

                    similarity3A = similarityFunc3A.getSimilarity(pair);
                    similarity3B = similarityFunc3B.getSimilarity(pair);
                    similarity3C = similarityFunc3C.getSimilarity(pair);
                    similarity3D = similarityFunc3D.getSimilarity(pair);
                    similarity3E = similarityFunc3E.getSimilarity(pair);
                    similarity3F = similarityFunc3F.getSimilarity(pair);
                    similarity3G = similarityFunc3G.getSimilarity(pair);
                    similarity3H = similarityFunc3H.getSimilarity(pair);
                    similarity3I = similarityFunc3I.getSimilarity(pair);
                    similarity3J = similarityFunc3J.getSimilarity(pair);
                    similarity3K = similarityFunc3K.getSimilarity(pair);
                    similarity3L = similarityFunc3L.getSimilarity(pair);
                    similarity3M = similarityFunc3M.getSimilarity(pair);
//                    similarity3N = Double.toString(similarityFuncSOUNDEX.getSimilarity(firstSoundex3, secondSoundex3));

                    similarity4A = similarityFunc4A.getSimilarity(pair);
                    similarity4B = similarityFunc4B.getSimilarity(pair);
                    similarity4C = similarityFunc4C.getSimilarity(pair);
                    similarity4D = similarityFunc4D.getSimilarity(pair);
                    similarity4E = similarityFunc4E.getSimilarity(pair);
                    similarity4F = similarityFunc4F.getSimilarity(pair);
                    similarity4G = similarityFunc4G.getSimilarity(pair);
                    similarity4H = similarityFunc4H.getSimilarity(pair);
                    similarity4I = similarityFunc4I.getSimilarity(pair);
                    similarity4J = similarityFunc4J.getSimilarity(pair);
                    similarity4K = similarityFunc4K.getSimilarity(pair);
                    similarity4L = similarityFunc4L.getSimilarity(pair);
                    similarity4M = similarityFunc4M.getSimilarity(pair);
//                    similarity4N = Double.toString(similarityFuncSOUNDEX.getSimilarity(firstSoundex4, secondSoundex4));

                    similarity5A = similarityFunc5A.getSimilarity(pair);
                    similarity5B = similarityFunc5B.getSimilarity(pair);
                    similarity5C = similarityFunc5C.getSimilarity(pair);
                    similarity5D = similarityFunc5D.getSimilarity(pair);
                    similarity5E = similarityFunc5E.getSimilarity(pair);
                    similarity5F = similarityFunc5F.getSimilarity(pair);
                    similarity5G = similarityFunc5G.getSimilarity(pair);
                    similarity5H = similarityFunc5H.getSimilarity(pair);
                    similarity5I = similarityFunc5I.getSimilarity(pair);
                    similarity5J = similarityFunc5J.getSimilarity(pair);
                    similarity5K = similarityFunc5K.getSimilarity(pair);
                    similarity5L = similarityFunc5L.getSimilarity(pair);
                    similarity5M = similarityFunc5M.getSimilarity(pair);
//                    similarity5N = Double.toString(similarityFuncSOUNDEX.getSimilarity(firstSoundex5, secondSoundex5));
                } catch (IllegalArgumentException e) {
                    // ignore invalid values
                    System.out.print(".");
                    if (++contPrint >= 20) {
                        contPrint = 0;
                        System.out.println("");
                    }

                    continue;
                }

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
//                bwArqPadr.append(',');
//                bwArqPadr.append(similarityN);
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
//                bwArqPadr.append(',');
//                bwArqPadr.append(similarity1N);
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
//                bwArqPadr.append(',');
//                bwArqPadr.append(similarity2N);
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
//                bwArqPadr.append(',');
//                bwArqPadr.append(similarity3N);
                bwArqPadr.append(',');
                bwArqPadr.append('-');
                bwArqPadr.append(',');

                bwArqPadr.append(Double.toString(similarity4A));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity4B));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity4C));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity4D));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity4E));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity4F));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity4G));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity4H));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity4I));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity4J));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity4K));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity4L));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity4M));
                bwArqPadr.append(',');
//                bwArqPadr.append(similarity4N);
//                bwArqPadr.append(',');
                bwArqPadr.append('-');
                bwArqPadr.append(',');

                bwArqPadr.append(Double.toString(similarity5A));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity5B));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity5C));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity5D));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity5E));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity5F));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity5G));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity5H));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity5I));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity5J));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity5K));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity5L));
                bwArqPadr.append(',');
                bwArqPadr.append(Double.toString(similarity5M));
//                bwArqPadr.append(',');
//                bwArqPadr.append(similarity5N);
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
//                    Logger.getLogger(Alg1ComFechoTransitivo.class.getName()).log(Level.SEVERE, null, ex);
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
//        Alg1ComFechoTransitivo obj1 = new Alg1ComFechoTransitivo("DBLP2", "ACM", "num", "id", "DBLP2-ACM_perfectMapping_NEW", "idDBLP", "idACM", 1);
        AlgSimilaridades obj1 = new AlgSimilaridades("CORA", "id", "cora_gold", "id1", "id2", 1000);
        try {
            obj1.executaDedupAlg();
        } catch (IOException ex) {
            Logger.getLogger(AlgSimilaridades.class.getName()).log(Level.SEVERE, null, ex);
        }
        java.awt.Toolkit.getDefaultToolkit().beep();
    }

}
