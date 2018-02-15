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
import dude.similarityfunction.contentbased.impl.simmetrics.LevenshteinDistanceFunction;
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

/**
 *
 * @author Diego
 */
public class VetorSim extends DedupAlg {

    File vetorSimilaridade = new File("./src/csv/conjuntosDS", "vetorSimilaridades.csv");
    FileWriter escreveArqVetor;
    BufferedWriter bwArqVetor = null;
    String a, b, c, d, e, rotulo;

    public VetorSim(String baseDados1, String chavePrimaria, String gold, String goldId1, String goldId2, String result) {
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
    //    File resultado = new File("./src/csv/datasets", "resultado.csv");
    //  Era o método executaDedupAlg()

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

        StatisticOutput statisticOutput = new SimpleStatisticOutput(System.out, statistic);

        LevenshteinDistanceFunction similarityFunc = new LevenshteinDistanceFunction("title");
        LevenshteinDistanceFunction similarityFunc2 = new LevenshteinDistanceFunction("artist");
        LevenshteinDistanceFunction similarityFunc3 = new LevenshteinDistanceFunction("track01");
        LevenshteinDistanceFunction similarityFunc4 = new LevenshteinDistanceFunction("track02");
        LevenshteinDistanceFunction similarityFunc5 = new LevenshteinDistanceFunction("track03");

        //Escrita do cabeçalho
        try {
            escreveArqVetor = new FileWriter(vetorSimilaridade, true); //O parâmetro true faz com que as informações não sejam sobreescritas

            bwArqVetor = new BufferedWriter(escreveArqVetor);
            bwArqVetor.write("id;elemento1;elemento2;title;artist;track01;track02;track03;duplicata\n");

        } catch (IOException ex) {
            System.out.println("Não foi possível escrever o cabeçalho no arquivo vetorSimilaridade.csv.");
        } finally {
            bwArqVetor.flush();
            bwArqVetor.close();

        }

        String elemento1T = null;
        String elemento2T = null;

        String[] parAtual;
        parAtual = null;
        int index_c1 = 0;
        int index_c2 = 0;

        int cont = 0;
        String Str2 = null;

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

//                System.out.println("Buscando elemento1: " + elemento1 + " - elemento2: " + elemento2);
                for (DuDeObjectPair pair : algorithm) {

//                    String par = pair.getReference().toString();
////                    System.out.println(par);
//
//                    parAtual = par.split(";", 2); //Nesse caso considera apenas as duas primeiras colunas (as que interessam)
//
//                    for (String cell : parAtual) {
////
//                        cont++;
//
//                        index_c1 = cell.indexOf('[');
//                        index_c2 = cell.indexOf(']');
//
//                        if (cont == 1) {
//
//                            elemento1T = cell.substring(index_c1 + 2, index_c2);
//                        } else {
//                            elemento2T = cell.substring(index_c1 + 1, index_c2);
//                        }
//
//                    }
//                    
//                    cont = 0;
//
//                    System.out.println("elemento1T: " + elemento1T);
//
//                    System.out.println("elemento2T: " + elemento2T);
//                    System.out.println(pair.getSecondElement().toString());
//                    System.out.println(pair.getSecondElement());
//                    if (pair.getFirstElement().toString().contains(elemento1) && pair.getSecondElement().toString().contains(elemento2)) {
                    if ( (pair.getFirstElement().toString().contains(elemento1) && pair.getSecondElement().toString().contains(elemento2)) 
                            || (pair.getFirstElement().toString().contains(elemento2) && pair.getSecondElement().toString().contains(elemento1))) {
//                    if ((pair.getFirstElement().toString().contains("6858")) && (pair.getSecondElement().toString().equals("[cd.[10230]]"))) {
//                    if (pair.getFirstElement().toString().contains("2004") && pair.getSecondElement().toString().contains("3710")) {
//                    if (elemento1T.equals(elemento1) && elemento2T.equals(elemento2))  {

//                        System.out.println(pair.getReference().toString());
                        id++;

                        a = Double.toString(similarityFunc.getSimilarity(pair));
                        b = Double.toString(similarityFunc2.getSimilarity(pair));
                        c = Double.toString(similarityFunc3.getSimilarity(pair));
                        d = Double.toString(similarityFunc4.getSimilarity(pair));
                        e = Double.toString(similarityFunc5.getSimilarity(pair));
                        rotulo = Boolean.toString(statistic.isDuplicate(pair));

//                        System.out.println("a: " + a);
                        try {
//                            System.out.println("Entrou no try");
//                        escreveArqVetor.write(Integer.toString(id) + ";" + elemento1 + ";" + elemento2 + ";" + a + ";" + b + ";" + c + ";" + d + ";" + e + ";" + rotulo + "\n");
                            bwArqVetor.append(Integer.toString(id));
                            bwArqVetor.append(';');
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
                            bwArqVetor.append(rotulo);
                            bwArqVetor.append('\n');
                            System.out.println("Escreveu!");

                        } catch (IOException ex) {
                            System.out.println("Não foi possível escrever o cabeçalho no arquivo vetorSimilaridade.csv.");
                        }

//                        System.out.print(pair.getFirstElement().toString() + " " + pair.getSecondElement().toString());
                        System.out.println("");
//                        new Thread().sleep(10000);
                        break;
                    }

                }
//                cont--;
                System.out.println("Saiu do for " + id);
                System.out.println("**************************");
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } finally {
//            escreveArqVetor.flush();
//            escreveArqVetor.close();

            bwArqVetor.flush();
            bwArqVetor.close();

            brDiverg.close();
        }
    }

}
