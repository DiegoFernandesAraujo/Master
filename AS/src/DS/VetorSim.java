/*
 * Classe utilizada para gerar o conjunto de vetores de similaridades 
 * que será base para o conjunto treinamento dos algoritmos de AA.
 * Atenção especial para a comparação dos IDs.
 * Se conseguir recuperar o texto de cada ID normalmente, deverá ser feito um método
 * para desconsiderar seus colchetes ([[ e ]]) para comparar com o conjunto de divergências gerado antes.
 */

package DS;

import dedupalgorithms.*;
import dude.algorithm.Algorithm;
import dude.datasource.CSVSource;
import dude.output.CSVOutput;
import dude.output.DuDeOutput;
import dude.output.JsonOutput;
import dude.output.statisticoutput.CSVStatisticOutput;
import dude.output.statisticoutput.SimpleStatisticOutput;
import dude.output.statisticoutput.StatisticOutput;
import dude.postprocessor.StatisticComponent;
import dude.similarityfunction.contentbased.impl.simmetrics.LevenshteinDistanceFunction;
import dude.util.GoldStandard;
import dude.util.data.DuDeObjectPair;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diego
 */
public class VetorSim extends DedupAlg {

    FileWriter arquivoCSV;

    public VetorSim(String baseDados1, String chavePrimaria, String gold, String goldId1, String goldId2, String result) {
        super(baseDados1, chavePrimaria, gold, goldId1, goldId2, result);

        try {
            this.arquivoCSV = new FileWriter(new File("./src/csv/xxxx", "vetorDeSimilaridades.csv"));

        } catch (IOException ex) {
            Logger.getLogger(VetorSim.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    File resultado = new File("./src/csv/datasets", "resultado.csv");
    @Override
    public void executaDedupAlg() throws IOException {

        GoldStandard goldStandard = getGS();

        Algorithm algorithm = getAlg();
        algorithm.enableInMemoryProcessing();

        LevenshteinDistanceFunction similarityFunc = new LevenshteinDistanceFunction("title");
        LevenshteinDistanceFunction similarityFunc2 = new LevenshteinDistanceFunction("artist");
        LevenshteinDistanceFunction similarityFunc3 = new LevenshteinDistanceFunction("track01");
        LevenshteinDistanceFunction similarityFunc4 = new LevenshteinDistanceFunction("track02");
        LevenshteinDistanceFunction similarityFunc5 = new LevenshteinDistanceFunction("track03");

//        QUANDO TIVER COM O CONJUNTO DE DIVERGÊNCIAS ACUMULADAS:
//        para cada par divergente faça{
//            for (DuDeObjectPair pair : algorithm) {
//              se par[1] == pair.getFirstElement() && par[2] == pair.getSecondElement()
//              
//                  final double similarity = similarityFunc.getSimilarity(pair);
//                  a =  Double.toString(similarityFunc.getSimilarity(pair));
//                  b = Double.toString(similarityFunc2.getSimilarity(pair));
//                  c = Double.toString(similarityFunc3.getSimilarity(pair));
//                  d = Double.toString(similarityFunc4.getSimilarity(pair));
//                  e = Double.toString(similarityFunc5.getSimilarity(pair));
//                  rotulo =  Boolean.toString(statistic.isDuplicate(pair));
//                  arquivoCSV.append(a, b, c, d, e...)
//                  break;
//            }
//        }
//        arquivoCSV.flush();
//        arquivoCSV.close();
//        
//       ESSA IMPRESSÃO FUNCIONOU A PRINCÍPIO. UTILIZÁ-LA PARA VER COMO OS IDENTIFICADORES SÃO RECUPERADOS
//                System.out.print(pair.getFirstElement() + " " + pair.getSecondElement());
//                System.out.println("");
//            }
    }

}
