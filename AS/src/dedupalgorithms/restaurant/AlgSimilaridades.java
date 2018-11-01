/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dedupalgorithms.restaurant;

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
        super(baseDados1, chavePrimaria, gold, goldId1, goldId2, ',');

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

        GoldStandard goldStandard = getGS();

        Algorithm algorithm = getAlg();
        algorithm.enableInMemoryProcessing();
        
        Soundex similarityFuncSOUNDEX = new Soundex();

        LevenshteinDistanceFunction similarityFuncA = new LevenshteinDistanceFunction("name");

        CosineSimilarityFunction similarityFuncB = new CosineSimilarityFunction("name");

        DiceCoefficientFunction similarityFuncC = new DiceCoefficientFunction("name");

        EuclideanDistanceFunction similarityFuncD = new EuclideanDistanceFunction("name");

        JaccardSimilarityFunction similarityFuncE = new JaccardSimilarityFunction("name");

        JaroDistanceFunction similarityFuncF = new JaroDistanceFunction("name");

        JaroWinklerFunction similarityFuncG = new JaroWinklerFunction("name");

        MatchingCoefficientFunction similarityFuncH = new MatchingCoefficientFunction("name");

        MongeElkanFunction similarityFuncI = new MongeElkanFunction("name");

        NeedlemanWunschFunction similarityFuncJ = new NeedlemanWunschFunction("name");

        OverlapCoefficientFunction similarityFuncK = new OverlapCoefficientFunction("name");

        SmithWatermanFunction similarityFuncL = new SmithWatermanFunction("name");

        SmithWatermanGotohFunction similarityFuncM = new SmithWatermanGotohFunction("name");

        LevenshteinDistanceFunction similarityFunc1A = new LevenshteinDistanceFunction("addr");

        CosineSimilarityFunction similarityFunc1B = new CosineSimilarityFunction("addr");

        DiceCoefficientFunction similarityFunc1C = new DiceCoefficientFunction("addr");

        EuclideanDistanceFunction similarityFunc1D = new EuclideanDistanceFunction("addr");

        JaccardSimilarityFunction similarityFunc1E = new JaccardSimilarityFunction("addr");

        JaroDistanceFunction similarityFunc1F = new JaroDistanceFunction("addr");

        JaroWinklerFunction similarityFunc1G = new JaroWinklerFunction("addr");

        MatchingCoefficientFunction similarityFunc1H = new MatchingCoefficientFunction("addr");

        MongeElkanFunction similarityFunc1I = new MongeElkanFunction("addr");

        NeedlemanWunschFunction similarityFunc1J = new NeedlemanWunschFunction("addr");

        OverlapCoefficientFunction similarityFunc1K = new OverlapCoefficientFunction("addr");

        SmithWatermanFunction similarityFunc1L = new SmithWatermanFunction("addr");

        SmithWatermanGotohFunction similarityFunc1M = new SmithWatermanGotohFunction("addr");

        LevenshteinDistanceFunction similarityFunc2A = new LevenshteinDistanceFunction("city");

        CosineSimilarityFunction similarityFunc2B = new CosineSimilarityFunction("city");

        DiceCoefficientFunction similarityFunc2C = new DiceCoefficientFunction("city");

        EuclideanDistanceFunction similarityFunc2D = new EuclideanDistanceFunction("city");

        JaccardSimilarityFunction similarityFunc2E = new JaccardSimilarityFunction("city");

        JaroDistanceFunction similarityFunc2F = new JaroDistanceFunction("city");

        JaroWinklerFunction similarityFunc2G = new JaroWinklerFunction("city");

        MatchingCoefficientFunction similarityFunc2H = new MatchingCoefficientFunction("city");

        MongeElkanFunction similarityFunc2I = new MongeElkanFunction("city");

        NeedlemanWunschFunction similarityFunc2J = new NeedlemanWunschFunction("city");

        OverlapCoefficientFunction similarityFunc2K = new OverlapCoefficientFunction("city");

        SmithWatermanFunction similarityFunc2L = new SmithWatermanFunction("city");

        SmithWatermanGotohFunction similarityFunc2M = new SmithWatermanGotohFunction("city");

        LevenshteinDistanceFunction similarityFunc3A = new LevenshteinDistanceFunction("phone");

        CosineSimilarityFunction similarityFunc3B = new CosineSimilarityFunction("phone");

        DiceCoefficientFunction similarityFunc3C = new DiceCoefficientFunction("phone");

        EuclideanDistanceFunction similarityFunc3D = new EuclideanDistanceFunction("phone");

        JaccardSimilarityFunction similarityFunc3E = new JaccardSimilarityFunction("phone");

        JaroDistanceFunction similarityFunc3F = new JaroDistanceFunction("phone");

        JaroWinklerFunction similarityFunc3G = new JaroWinklerFunction("phone");

        MatchingCoefficientFunction similarityFunc3H = new MatchingCoefficientFunction("phone");

        MongeElkanFunction similarityFunc3I = new MongeElkanFunction("phone");

        NeedlemanWunschFunction similarityFunc3J = new NeedlemanWunschFunction("phone");

        OverlapCoefficientFunction similarityFunc3K = new OverlapCoefficientFunction("phone");

        SmithWatermanFunction similarityFunc3L = new SmithWatermanFunction("phone");

        SmithWatermanGotohFunction similarityFunc3M = new SmithWatermanGotohFunction("phone");

        LevenshteinDistanceFunction similarityFunc4A = new LevenshteinDistanceFunction("type");

        CosineSimilarityFunction similarityFunc4B = new CosineSimilarityFunction("type");

        DiceCoefficientFunction similarityFunc4C = new DiceCoefficientFunction("type");

        EuclideanDistanceFunction similarityFunc4D = new EuclideanDistanceFunction("type");

        JaccardSimilarityFunction similarityFunc4E = new JaccardSimilarityFunction("type");

        JaroDistanceFunction similarityFunc4F = new JaroDistanceFunction("type");

        JaroWinklerFunction similarityFunc4G = new JaroWinklerFunction("type");

        MatchingCoefficientFunction similarityFunc4H = new MatchingCoefficientFunction("type");

        MongeElkanFunction similarityFunc4I = new MongeElkanFunction("type");

        NeedlemanWunschFunction similarityFunc4J = new NeedlemanWunschFunction("type");

        OverlapCoefficientFunction similarityFunc4K = new OverlapCoefficientFunction("type");

        SmithWatermanFunction similarityFunc4L = new SmithWatermanFunction("type");

        SmithWatermanGotohFunction similarityFunc4M = new SmithWatermanGotohFunction("type");

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

            escreveArqPadr = new FileWriter("./src/csv/" + dir + "/similaridades.csv", false);
            System.out.println("./src/csv/" + dir + "/similaridades.csv");
            bwArqPadr = new BufferedWriter(escreveArqPadr);

            System.out.println("Tá funcionando!");

            //Gerando o fecho transitivo
            for (DuDeObjectPair pair : algorithm) {

                firstSoundex = pair.getFirstElement().getAttributeValues("name").toString();
                secondSoundex = pair.getSecondElement().getAttributeValues("name").toString();
                
                firstSoundex1 = pair.getFirstElement().getAttributeValues("addr").toString();
                secondSoundex1 = pair.getSecondElement().getAttributeValues("addr").toString();
                
                firstSoundex2 = pair.getFirstElement().getAttributeValues("city").toString();
                secondSoundex2 = pair.getSecondElement().getAttributeValues("city").toString();
                
                firstSoundex3 = pair.getFirstElement().getAttributeValues("phone").toString();
                secondSoundex3 = pair.getSecondElement().getAttributeValues("phone").toString();
                
                firstSoundex4 = pair.getFirstElement().getAttributeValues("type").toString();
                secondSoundex4 = pair.getSecondElement().getAttributeValues("type").toString();

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
                final String similarityN = Double.toString(similarityFuncSOUNDEX.getSimilarity(firstSoundex, secondSoundex));

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
                final String similarity1N = Double.toString(similarityFuncSOUNDEX.getSimilarity(firstSoundex1, secondSoundex1));

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
                final String similarity2N = Double.toString(similarityFuncSOUNDEX.getSimilarity(firstSoundex2, secondSoundex2));

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
                final String similarity3N = Double.toString(similarityFuncSOUNDEX.getSimilarity(firstSoundex3, secondSoundex3));

                final double similarity4A = similarityFunc4A.getSimilarity(pair);
                final double similarity4B = similarityFunc4B.getSimilarity(pair);
                final double similarity4C = similarityFunc4C.getSimilarity(pair);
                final double similarity4D = similarityFunc4D.getSimilarity(pair);
                final double similarity4E = similarityFunc4E.getSimilarity(pair);
                final double similarity4F = similarityFunc4F.getSimilarity(pair);
                final double similarity4G = similarityFunc4G.getSimilarity(pair);
                final double similarity4H = similarityFunc4H.getSimilarity(pair);
                final double similarity4I = similarityFunc4I.getSimilarity(pair);
                final double similarity4J = similarityFunc4J.getSimilarity(pair);
                final double similarity4K = similarityFunc4K.getSimilarity(pair);
                final double similarity4L = similarityFunc4L.getSimilarity(pair);
                final double similarity4M = similarityFunc4M.getSimilarity(pair);
                final String similarity4N = Double.toString(similarityFuncSOUNDEX.getSimilarity(firstSoundex4, secondSoundex4));

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
                bwArqPadr.append(similarityN);
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
                bwArqPadr.append(similarity1N);
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
                bwArqPadr.append(similarity2N);
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
                bwArqPadr.append(',');
                bwArqPadr.append(similarity3N);
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
                bwArqPadr.append(similarity4N);
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
        AlgSimilaridades obj1 = new AlgSimilaridades("restaurant", "id", "restaurant_gold", "id_1", "id_2", 1000);
        try {
            obj1.executaDedupAlg();
        } catch (IOException ex) {
            Logger.getLogger(AlgSimilaridades.class.getName()).log(Level.SEVERE, null, ex);
        }
        java.awt.Toolkit.getDefaultToolkit().beep();
    }

}
