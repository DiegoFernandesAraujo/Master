/*
 * Classe utilizada para gerar o conjunto de vetores de similaridades 
 * que será base para o conjunto treinamento dos algoritmos de AA.
 *
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
import dude.similarityfunction.contentbased.impl.SoundExFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.LevenshteinDistanceFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.MongeElkanFunction;
import dude.similarityfunction.contentbased.util.SoundEx;
import dude.util.GoldStandard;
import dude.util.data.DuDeObjectPair;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.shef.wit.simmetrics.similaritymetrics.MongeElkan;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Soundex;

/**
 *
 * @author Diego
 */
public class VetorSim2 extends DedupAlg {

    File vetorSimilaridade = new File("./src/csv/conjuntosDS/vetorSimilaridades", "vetorSimilaridades-14-03.csv");
    FileWriter escreveArqVetor;
    BufferedWriter bwArqVetor = null;
    String a, b, c, d, e, f, g, rotulo;

    public VetorSim2(String baseDados1, String chavePrimaria, String gold, String goldId1, String goldId2, String result) {
        super(baseDados1, chavePrimaria, gold, goldId1, goldId2, result);

        if (!vetorSimilaridade.exists()) {
            System.out.println("Não existe arquivo vetorSimilaridade.csv.");

            try {
                vetorSimilaridade.createNewFile();

            } catch (IOException ex) {
                System.out.println("Não foi possível criar arquivo vetorSimilaridade.csv.");
            }

        }
    }

    public void geraVetor(File arqDiverg) throws IOException {

        String Str;
        String elemento1;
        String elemento2;
        String[] linhaAtual;
        BufferedReader brDiverg = null;

        GoldStandard goldStandard = getGS();

        Algorithm algorithm = getAlg();
        algorithm.enableInMemoryProcessing();

        StatisticComponent statistic = new StatisticComponent(goldStandard, algorithm);

        //Utilizando-se funções de similaridade que apresentaram bons resultados para gerar os vetores de similaridade.
        MongeElkanFunction similarityFunc = new MongeElkanFunction("title");
//        SoundExFunction similarityFunc2 = new SoundExFunction("artist");
        Soundex similarityFunc2 = new Soundex();
        LevenshteinDistanceFunction similarityFunc3 = new LevenshteinDistanceFunction("track01");
        LevenshteinDistanceFunction similarityFunc4 = new LevenshteinDistanceFunction("track02");
        LevenshteinDistanceFunction similarityFunc5 = new LevenshteinDistanceFunction("track03");
        LevenshteinDistanceFunction similarityFunc6 = new LevenshteinDistanceFunction("track10");
        LevenshteinDistanceFunction similarityFunc7 = new LevenshteinDistanceFunction("track11");
        
        //Escrita do cabeçalho
        try {
            escreveArqVetor = new FileWriter(vetorSimilaridade, false); //O parâmetro false faz com que as informações sejam sobreescritas

            bwArqVetor = new BufferedWriter(escreveArqVetor);
            bwArqVetor.write("id;elemento1;elemento2;title;artist;track01;track02;track03;duplicata\n");

        } catch (IOException ex) {
            System.out.println("Não foi possível escrever o cabeçalho no arquivo vetorSimilaridade.csv.");
        } finally {
            bwArqVetor.flush();
            bwArqVetor.close();
       }

        //Escrita dos valores de similaridade
        try {
            brDiverg = new BufferedReader(new FileReader(arqDiverg.getPath()));
            escreveArqVetor = new FileWriter(vetorSimilaridade, true); //O parâmetro true faz com que as informações não sejam sobreescritas
            bwArqVetor = new BufferedWriter(escreveArqVetor);
            int id = 0;

            while ((Str = brDiverg.readLine()) != null) {

                linhaAtual = Str.split(";", 2);

                elemento1 = linhaAtual[0];
                elemento2 = linhaAtual[1];

                System.out.println("Buscando elemento1: " + elemento1 + " - elemento2: " + elemento2);
                for (DuDeObjectPair pair : algorithm) {

                    String firstSoundex = pair.getFirstElement().getAttributeValues("artist").toString();
                    String secondSoundex = pair.getSecondElement().getAttributeValues("artist").toString();
                    

                    //Fecho transitivo
                    if ((pair.getFirstElement().toString().contains(elemento1) && pair.getSecondElement().toString().contains(elemento2))
                            || (pair.getFirstElement().toString().contains(elemento2) && pair.getSecondElement().toString().contains(elemento1))) {

                        id++;
                        
                        System.out.println(firstSoundex + "-" + secondSoundex);

                        a = Double.toString(similarityFunc.getSimilarity(pair));
                        b = Double.toString(similarityFunc2.getSimilarity(firstSoundex,secondSoundex));
                        c = Double.toString(similarityFunc3.getSimilarity(pair));
                        d = Double.toString(similarityFunc4.getSimilarity(pair));
                        e = Double.toString(similarityFunc5.getSimilarity(pair));
                        f = Double.toString(similarityFunc4.getSimilarity(pair));
                        g = Double.toString(similarityFunc5.getSimilarity(pair));
                        rotulo = Boolean.toString(statistic.isDuplicate(pair));

                        try {
//                            bwArqVetor.append(Integer.toString(id));
//                            bwArqVetor.append(';');
                            bwArqVetor.append(elemento1);
                            bwArqVetor.append(';');
                            bwArqVetor.append(elemento2);
                            bwArqVetor.append(';');
                            bwArqVetor.append(a);
                            bwArqVetor.append(';');
                            bwArqVetor.append(b);
                            bwArqVetor.append(';');
                            bwArqVetor.append(c);
                            bwArqVetor.append(';');
                            bwArqVetor.append(d);
                            bwArqVetor.append(';');
                            bwArqVetor.append(e);
                            bwArqVetor.append(';');
                            bwArqVetor.append(f);
                            bwArqVetor.append(';');
                            bwArqVetor.append(g);
                            bwArqVetor.append(';');
                            bwArqVetor.append(rotulo);
                            bwArqVetor.append('\n');
                            bwArqVetor.flush();

                        } catch (IOException ex) {
                            System.out.println("Não foi possível escrever o cabeçalho no arquivo vetorSimilaridade.csv.");
                        }

                        System.out.print(pair.getFirstElement().toString() + " " + pair.getSecondElement().toString());
                        System.out.println(" - id: " + id);
                        break;
                    }

                }

//                if (id > 20) {
//                    break;
//                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } finally {

            bwArqVetor.flush();
            bwArqVetor.close();

            brDiverg.close();
        }
    }

}
