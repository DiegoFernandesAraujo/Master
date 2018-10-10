/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DS;

import dude.algorithm.Algorithm;
import dude.similarityfunction.contentbased.impl.simmetrics.LevenshteinDistanceFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.MongeElkanFunction;
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
import javax.swing.JOptionPane;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Soundex;

/**
 *
 * @author Diego
 */
public class ExecVetorSim1 extends VetorSimEstat1 { //Mudar o nome para geraVetorCDS depois

    public ExecVetorSim1(String baseDados1, String chavePrimaria, String gold, String goldId1, String goldId2, String result, File vetor, char separator, File diverg, boolean geraVetor) throws IOException {
        super(baseDados1, chavePrimaria, gold, goldId1, goldId2, result, vetor, separator);

        if (geraVetor) {
            geraVetor(diverg); //Para gerar o vetor base dos demais
        }
    }

    /**
     * A ser sobreescrito para cada base
     *
     * @param arqDiverg
     * @return
     * @throws IOException
     */
    @Override
    public File geraVetor(File arqDiverg) throws IOException {

        String str;
        String elemento1;
        String elemento2;
        String[] linhaAtual;
        BufferedReader brDiverg = null;

        Algorithm algorithm = getAlg();
        algorithm.enableInMemoryProcessing();

        //Utilizando-se funções de similaridade que apresentaram bons resultados para gerar os vetores de similaridade.
        MongeElkanFunction similarityFunc = new MongeElkanFunction("title");
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
//            bwArqVetor.write("elemento1;elemento2;title;artist;track01;track02;track03;track10;track11\n"); //ERA ASSIM
            bwArqVetor.write("elemento1;elemento2;qtdAlg;min;max;med;duplicata;title;artist;track01;track02;track03;track10;track11\n");

        } catch (IOException ex) {
            System.out.println("Não foi possível escrever o cabeçalho no arquivo vetorSimilaridade.csv.");
        } finally {
            bwArqVetor.flush();
            bwArqVetor.close();
            escreveArqVetor = null;
            bwArqVetor = null;
        }

        //Escrita dos valores de similaridade
        try {
            brDiverg = new BufferedReader(new FileReader(arqDiverg.getPath()));
            escreveArqVetor = new FileWriter(vetorSimilaridade, true); //O parâmetro true faz com que as informações não sejam sobreescritas
            bwArqVetor = new BufferedWriter(escreveArqVetor);
            int id = 0;

            String firstSoundex = null;
            String secondSoundex = null;

            while ((str = brDiverg.readLine()) != null) {

                linhaAtual = str.split(";", 3);

                elemento1 = linhaAtual[0];
                elemento2 = linhaAtual[1];

                if (elemento1.contains("elemento1")) {
                    continue;
                }

                System.out.println("Buscando elemento1: " + elemento1 + " - elemento2: " + elemento2);
                for (DuDeObjectPair pair : algorithm) {

                    firstSoundex = null;
                    secondSoundex = null;

                    firstSoundex = pair.getFirstElement().getAttributeValues("artist").toString();
                    secondSoundex = pair.getSecondElement().getAttributeValues("artist").toString();

                    //Fecho transitivo
                    if ((pair.getFirstElement().toString().contains(elemento1) && pair.getSecondElement().toString().contains(elemento2))
                            || (pair.getFirstElement().toString().contains(elemento2) && pair.getSecondElement().toString().contains(elemento1))) {

                        id++;

//                        System.out.println(firstSoundex + "-" + secondSoundex);
                        a = Double.toString(similarityFunc.getSimilarity(pair));
                        b = Double.toString(similarityFunc2.getSimilarity(firstSoundex, secondSoundex));
                        c = Double.toString(similarityFunc3.getSimilarity(pair));
                        d = Double.toString(similarityFunc4.getSimilarity(pair));
                        e = Double.toString(similarityFunc5.getSimilarity(pair));
                        f = Double.toString(similarityFunc6.getSimilarity(pair));
                        g = Double.toString(similarityFunc7.getSimilarity(pair));

                        try {
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
            escreveArqVetor = null;
            bwArqVetor = null;
            brDiverg.close();
            brDiverg = null;
        }

        return vetorSimilaridade;
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {
//        File vetorSim = new File("./src/csv/conjuntosDS/vetorSimilaridades", "vetorSimilaridadesCDs.csv");
        File vetorSim = new File("./src/csv/conjuntosDS/vetorSimilaridades", "vetorSimilaridades-25-06.csv");
        File diverg = new File("./src/csv/conjuntosDS/", "NAO_DA.csv"); //Esse arquivo tem que possuir todas as divergências
        File dir = new File("./src/csv/conjuntosDS/conjuntosDivergAA/cds/QP1/");

        //Para gerar o vetor de similaridade geral antes de gerar os vetores de similaridade específicos
//        VetorSimEstat1 obj = new ExecVetorSim1("cd", "pk", "cd_gold", "disc1_id", "disc2_id", "cd_result", vetorSim, ';', diverg, true);
        //Para gerar os vetores de similaridade específicos apenas
        VetorSimEstat1 obj = new ExecVetorSim1("cd", "pk", "cd_gold", "disc1_id", "disc2_id", "cd_result", vetorSim, ';', diverg, false);

        try {

            if (dir.isDirectory()) {
                File[] divergs = dir.listFiles();

                for (File arq : divergs) {

                    String nome = arq.getName();

                    System.out.println("Nome do arquivo: " + nome);

                    if (nome.contains("diverg") && !nome.contains("_NEW")) {
                        obj.geraVetorMenor(arq, vetorSim);
                        arq.delete(); //Exclui o arquivo depois de gerar os vetores de similaridade
                    }

                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ExecVetorSim1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
