/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experimentos;

import DS.DgStd1;
import DS.VetorSimEstat1;
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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Soundex;

/**
 *
 * @author Diego
 */
public class ExperimentosCDs extends VetorSimEstat1 {

    FileWriter escreveArqVetor;
    BufferedWriter bwArqVetor = null;
    String a, b, c, d, e, f, g, rotulo;

    public ExperimentosCDs(String baseDados1, String chavePrimaria, String gold, String goldId1, String goldId2, char separator, boolean geraVetor, String qp) {
        super(baseDados1, chavePrimaria, gold, goldId1, goldId2, separator, qp);

        if (geraVetor) {
            try {
                geraVetor(getFileDiverg()); //Para gerar o vetor base dos demais'
            } catch (IOException ex) {
                Logger.getLogger(ExperimentosCDs.class.getName()).log(Level.SEVERE, null, ex);
            }
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
    public void geraVetor(File arqDiverg) throws IOException {

        String str;
        String elemento1;
        String elemento2;
        String elemento1Par;
        String elemento2Par;

        String[] linhaAtual;
        BufferedReader brDiverg = null;
        Map<String, String> mapDivergsMaior = new HashMap<String, String>();

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
            escreveArqVetor = new FileWriter(getVetorSimilaridade(), false); //O parâmetro false faz com que as informações sejam sobreescritas

            bwArqVetor = new BufferedWriter(escreveArqVetor);
            bwArqVetor.write("elemento1;elemento2;title;artist;track01;track02;track03;track10;track11\n"); //ERA ASSIM

        } catch (IOException ex) {
            System.out.println("Não foi possível escrever o cabeçalho no arquivo vetorSimilaridade.csv.");
        } finally {
            bwArqVetor.flush();
            bwArqVetor.close();
            escreveArqVetor = null;
            bwArqVetor = null;
        }

        //Salvando dados no mapa
        try {

            brDiverg = new BufferedReader(new FileReader(arqDiverg.getPath()));

            while ((str = brDiverg.readLine()) != null) {

                linhaAtual = str.split(";", 3);

                elemento1 = linhaAtual[0];
                elemento2 = linhaAtual[1];

                if (elemento1.contains("elemento1")) {
                    continue;
                }

                mapDivergsMaior.put(elemento1 + ";" + elemento2, elemento1 + ";" + elemento2);

            }
            brDiverg.close();

        } catch (IOException ex) {
            Logger.getLogger(DgStd1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            brDiverg = null;
            elemento1 = null;
            elemento2 = null;
        }

//        System.out.println("mapDivergsMaior");
//
//        for (Map.Entry<String, String> entry : mapDivergsMaior.entrySet()) {
//
//            System.out.println(entry.getKey());
//
//        }
        //Escrita dos valores de similaridade
        try {
            escreveArqVetor = new FileWriter(getVetorSimilaridade(), true); //O parâmetro true faz com que as informações não sejam sobreescritas
            bwArqVetor = new BufferedWriter(escreveArqVetor);

            int id = 0;

            String firstSoundex = null;
            String secondSoundex = null;

            for (DuDeObjectPair pair : algorithm) {

                firstSoundex = null;
                secondSoundex = null;

                firstSoundex = pair.getFirstElement().getAttributeValues("artist").toString();
                secondSoundex = pair.getSecondElement().getAttributeValues("artist").toString();

                elemento1Par = pair.getFirstElement().toString();
                elemento2Par = pair.getSecondElement().toString();
                elemento1Par = elemento1Par.replaceAll("\\D", "");
                elemento2Par = elemento2Par.replaceAll("\\D", "");

                //Fecho transitivo
                if (mapDivergsMaior.containsKey(elemento1Par + ";" + elemento2Par) || mapDivergsMaior.containsKey(elemento2Par + ";" + elemento1Par)) {

                    id++;

//                    if (id > 20) {
//                        break;
//                    }
//                        System.out.println(firstSoundex + "-" + secondSoundex);
                    a = Double.toString(similarityFunc.getSimilarity(pair));
                    b = Double.toString(similarityFunc2.getSimilarity(firstSoundex, secondSoundex));
                    c = Double.toString(similarityFunc3.getSimilarity(pair));
                    d = Double.toString(similarityFunc4.getSimilarity(pair));
                    e = Double.toString(similarityFunc5.getSimilarity(pair));
                    f = Double.toString(similarityFunc6.getSimilarity(pair));
                    g = Double.toString(similarityFunc7.getSimilarity(pair));

                    try {
                        bwArqVetor.append(elemento1Par);
                        bwArqVetor.append(';');
                        bwArqVetor.append(elemento2Par);
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

                    System.out.print(elemento1Par + " " + elemento2Par);
                    System.out.println(" - id: " + id);

                    if (id == mapDivergsMaior.size()) { //Se já encontrou todas as divergências.
                        break;
                    }

                }

                elemento1Par = null;
                elemento2Par = null;

            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } finally {

            bwArqVetor.flush();
            bwArqVetor.close();
            escreveArqVetor = null;
            bwArqVetor = null;

        }

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        //Com o construtor dessa forma realiza apenas o experimento para QP1
        AllQPEtapa1 expCds = new AllQPEtapa1("cd", "cd_gold", null, null, null, null, null, null, 23, 0, 0, 0, 0, 0, 9763, 0, 0, 0, 1, 0, 0, 0, 0, 0, true, false, false, false, false, false, false, false, false);
        expCds.setParamVetorSim("pk", "cd_gold", "disc1_id", "disc2_id", ';', true);
//        expCds.setGsGeral("cd_gold.csv");
        expCds.rodaExpDedup(true);
    }

}
