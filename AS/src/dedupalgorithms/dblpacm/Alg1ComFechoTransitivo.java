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
public class Alg1ComFechoTransitivo extends DedupAlg {

    String rotulo;
    double a, b, c, d, e, f;
    int index_c1, index_c2, index_c3;
    String dir = "resultsDedup/dblpacm";
    
    FileWriter escreveResult;
    File estatisticasCSV;
    File estatisticasTXT;
    File estatisticasCSVSemFecho;
    File estatisticasTXTSemFecho;

    public Alg1ComFechoTransitivo(String baseDados1, String baseDados2, String chavePrimaria1, String chavePrimaria2, String gold, String goldId1, String goldId2, int ordem) {
        super(baseDados1, baseDados2, chavePrimaria1, chavePrimaria2, gold, goldId1, goldId2, ',');

        dir = "resultsDedup/" + baseDados1 + "-" + baseDados2;

        estatisticasCSV = new File("./src/csv/" + dir + "/estatisticas", "estatisticasDedup" + ordem + ".csv");
        estatisticasTXT = new File("./src/csv/" + dir + "/estatisticas", "estatisticasDedup" + ordem + ".txt");

        estatisticasCSVSemFecho = new File("./src/csv/" + dir + "/estatisticas", "estatisticasDedupSemFecho" + ordem + ".csv");
        estatisticasTXTSemFecho = new File("./src/csv/" + dir + "/estatisticas", "estatisticasDedupSemFecho" + ordem + ".txt");

        if (estatisticasTXT.exists() | estatisticasCSV.exists()) {
            System.out.println("Já existem resultados para esse algoritmo!");
            java.awt.Toolkit.getDefaultToolkit().beep();
            System.exit(0);
        }
        try {
            this.escreveResult = new FileWriter(new File("./src/csv/" + dir, "resultado" + ordem + ".csv"));

        } catch (IOException ex) {
            Logger.getLogger(Alg1ComFechoTransitivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    File resultado = new File("./src/csv/datasets", "resultado.csv");
    @Override
    public void executaDedupAlg() throws IOException {

        GoldStandard goldStandard = getGS();

        Algorithm algorithm = getAlg();
        algorithm.enableInMemoryProcessing();

        EuclideanDistanceFunction similarityFunc = new EuclideanDistanceFunction("title");
        EuclideanDistanceFunction similarityFunc2 = new EuclideanDistanceFunction("authors");
        EuclideanDistanceFunction similarityFunc3 = new EuclideanDistanceFunction("venue");
        NeedlemanWunschFunction similarityFunc4 = new NeedlemanWunschFunction("year");

//        similarityFunc.ignoreCapitalization();
//        similarityFunc2.ignoreCapitalization();
//        similarityFunc3.ignoreCapitalization();
//        similarityFunc4.ignoreCapitalization();
//        AbsoluteNumberDiffFunction similarityFunc5 = new AbsoluteNumberDiffFunction(10, "year");
//        DuDeOutput output = new JsonOutput(System.out);
//        DuDeOutput output = new CSVOutput(escreveResult);

        StatisticComponent statistic = new StatisticComponent(goldStandard, algorithm);

        StatisticComponent statisticSemFecho = new StatisticComponent(goldStandard, algorithm);

        StatisticOutput statisticOutputCSV;
        StatisticOutput statisticOutputTXT;

        StatisticOutput statisticOutputCSVSemFecho;
        StatisticOutput statisticOutputTXTSemFecho;

        statisticOutputCSV = new CSVStatisticOutput(estatisticasCSV, statistic, ';');
        statisticOutputTXT = new SimpleStatisticOutput(estatisticasTXT, statistic);

        statisticOutputCSVSemFecho = new CSVStatisticOutput(estatisticasCSVSemFecho, statisticSemFecho, ';');
        statisticOutputTXTSemFecho = new SimpleStatisticOutput(estatisticasTXTSemFecho, statisticSemFecho);

//        statisticOutput = new SimpleStatisticOutput(System.out, statistic);
        statistic.setStartTime();
        statisticSemFecho.setStartTime();

        NaiveTransitiveClosureGenerator fechoTrans = new NaiveTransitiveClosureGenerator();

//        Vector<DuDeObjectPair> pairs = new Vector<DuDeObjectPair>();
        //Gerando o fecho transitivo
        for (DuDeObjectPair pair : algorithm) {

            final double similarity = similarityFunc.getSimilarity(pair);
            final double similarity2 = similarityFunc2.getSimilarity(pair);
            final double similarity3 = similarityFunc3.getSimilarity(pair);
            final double similarity4 = similarityFunc4.getSimilarity(pair);

//            System.out.println(pair.getFirstElement().getAttributeValue("year").toString() + "-" + pair.getSecondElement().getAttributeValue("year").toString());
//            System.out.println(similarityFunc5.calculateSimilarity(pair.getFirstElement().getAttributeValue("year"), pair.getSecondElement().getAttributeValue("year")));
//            System.out.println(similarityFunc5.getSimilarity(pair));
//            System.out.println("");
//            System.out.println(similarity + "," + similarity2 + "," + similarity3 + "," + (similarity4));
//            System.out.println(similarity2 /*&& (similarity4 >= 0.85)*/);
            if ((similarity >= 0.75) && (similarity2 >= 0.75) && (similarity3 >= 0.5) && (similarity4 >= 0.75)) {
//            if ((similarity >= 0.35) && (similarity2 >= 0.35) && (similarity3 >= 0.35) && (similarity4 >= 0.35)) {
//            if ((similarity >= 0.9) && (similarity2 >= 0.9) && (similarity3 >= 0.9) /*&& (similarity4 >= 0.85)*/) {
                fechoTrans.add(pair);
                statisticSemFecho.addDuplicate(pair);
//                pairs.add(pair);
//                statistic.addDuplicate(pair);
//                output.write(pair);
//                System.out.println("Existe no GS: " + existeNoGS(pair));
//                System.out.println("Elemento 1: " + pair.getFirstElement() + " - " + "Elemento 2: " + pair.getSecondElement());
//                System.out.println("Similaridade: " + pair.getSimilarity());
//                System.out.println("");
//
            } else {
                try {
                    statistic.addNonDuplicate(pair);
                    statisticSemFecho.addNonDuplicate(pair);
                } catch (ExtractionFailedException ex) {
                    Logger.getLogger(Alg1ComFechoTransitivo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

//        NaiveTransitiveClosureGenerator transClosGenerator = new NaiveTransitiveClosureGenerator();
//        transClosGenerator.add(pairs);
//        Collection<Collection<DuDeObject>> duplicates = (Collection<Collection<DuDeObject>>) transClosGenerator.getTransitiveClosures();
//        //iterate over each cluster to generate duplicate pairs
//        Iterator<Collection<DuDeObject>> it1 = duplicates.iterator();
//        while (it1.hasNext()) {
//            Collection<DuDeObject> cluster = it1.next(); //take cluster
//            Iterator<DuDeObject> it2 = cluster.iterator();
//            List<DuDeObject> li = new LinkedList<DuDeObject>();
//            while (it2.hasNext()) {
//                li.add(it2.next());
//            }
//            //generate Duplicate pairs from transitive Closure to add to statisticComponent
//            for (int i = 0; i < li.size(); i++) {
//                for (int j = i + 1; j < li.size(); j++) {
//                    DuDeObjectPair pair = new DuDeObjectPair(li.get(i), li.get(j));
//                    statistic.addDuplicate(pair);
//                    output.write(pair);
//                }
//            }
//        }
        BufferedWriter bwSim = null;

//        File sim = new File("./src/csv/resultsDedup/similaridade.csv");
//        escreveSim = new FileWriter(sim, true);
        bwSim = new BufferedWriter(escreveResult);

//        bwSim.write("First Object;Second Object;duplicata;similaridade\n");
        bwSim.write("First Object;Second Object;similaridade\n");

        for (DuDeObjectPair pair : fechoTrans) {

            statistic.addDuplicate(pair);
//            output.write(pair);

            try {

                a = similarityFunc.getSimilarity(pair);
                b = similarityFunc2.getSimilarity(pair);
                c = similarityFunc3.getSimilarity(pair);
                d = similarityFunc4.getSimilarity(pair);
//                e = similarityFunc2.getSimilarity(pair);
//                f = similarityFunc2.getSimilarity(pair);

                final double simNorm = (a + b + c + d) / 4;
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
        statisticSemFecho.setEndTime();

        statisticOutputCSV.writeStatistics();
        statisticOutputTXT.writeStatistics();
        
        statisticOutputCSVSemFecho.writeStatistics();
        statisticOutputTXTSemFecho.writeStatistics();

        algorithm.cleanUp();
        goldStandard.close();

    }

    public static void main(String[] args) {
//        Alg1ComFechoTransitivo obj1 = new Alg1ComFechoTransitivo("DBLP2", "ACM", "num", "id", "DBLP2-ACM_perfectMapping_NEW", "idDBLP", "idACM", 1);

        Alg1ComFechoTransitivo obj1 = new Alg1ComFechoTransitivo("DBLP2", "ACM", "id", "id", "DBLP2-ACM_perfectMapping", "idDBLP", "idACM", 1);
//        Alg1ComFechoTransitivo obj1 = new Alg1ComFechoTransitivo( "ACM", "DBLP2", "id", "id", "DBLP2-ACM_perfectMapping", "idACM", "idDBLP",  1);
//        Alg1ComFechoTransitivo obj1 = new Alg1ComFechoTransitivo( "ACM", "DBLP2", "id", "id", "DBLP2-ACM_perfectMapping", "idDBLP", "idACM",  1);
//        Alg1ComFechoTransitivo obj1 = new Alg1ComFechoTransitivo("DBLP2", "ACM", "id", "id", "DBLP2-ACM_perfectMapping", "idACM", "idDBLP", 1);
//        Alg1ComFechoTransitivo obj1 = new Alg1ComFechoTransitivo("DBLP2_NEW", "ACM", "num", "id", "DBLP2-ACM_perfectMapping_NEW", "idDBLP", "idACM", 1);
//        Alg1ComFechoTransitivo obj1 = new Alg1ComFechoTransitivo("DBLP2_NEW", "ACM", "num", "id", "DBLP2-ACM_perfectMapping_NEW", "idACM", "idDBLP", 1);

        try {
            obj1.executaDedupAlg();
        } catch (IOException ex) {
            Logger.getLogger(Alg10.class.getName()).log(Level.SEVERE, null, ex);
        }
        java.awt.Toolkit.getDefaultToolkit().beep();
    }

}
