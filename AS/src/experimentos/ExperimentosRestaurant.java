/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experimentos;

import DS.DgStd1;
import dude.algorithm.Algorithm;
import dude.similarityfunction.contentbased.impl.simmetrics.JaroDistanceFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.LevenshteinDistanceFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.MongeElkanFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.NeedlemanWunschFunction;
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
public class ExperimentosRestaurant extends VetorSimEstat11 {

    FileWriter escreveArqVetor;
    BufferedWriter bwArqVetor = null;
    String a, b, c, d, e, f, g, rotulo;

    public ExperimentosRestaurant() {

    }

    public ExperimentosRestaurant(String baseDados1, String chavePrimaria, String gold, String goldId1, String goldId2, char separator, String qp, boolean geraVetor) {
        super(baseDados1, chavePrimaria, gold, goldId1, goldId2, separator, qp);

        if (geraVetor) {//Dar um jeito de conseguir o conjunto de todas divergências antes!
            try {
                geraVetorMaior(getFileDiverg()); //Para gerar o vetor base dos demais'
            } catch (IOException ex) {
                Logger.getLogger(ExperimentosRestaurant.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    ExperimentosRestaurant(String baseDados1, String chavePrimaria, String gold, String goldId1, String goldId2, char separator, String qp) {
        super(baseDados1, chavePrimaria, gold, goldId1, goldId2, separator, qp);
    }

    ExperimentosRestaurant(String baseGeral, String chavePrimaria, String gsGeral, String goldId1, String goldId2, char c, boolean geraVetor, String qp2r) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * A ser sobreescrito para cada base
     *
     * @param arqDiverg
     * @return
     * @throws IOException
     */
    @Override
    public void geraVetorMaior(File arqDiverg) throws IOException {

        System.out.println("Olha eu aqui, papae!");

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
        JaroDistanceFunction similarityFunc = new JaroDistanceFunction("name");
        JaroDistanceFunction similarityFunc2 = new JaroDistanceFunction("addr");
        NeedlemanWunschFunction similarityFunc3 = new NeedlemanWunschFunction("city");
        LevenshteinDistanceFunction similarityFunc4 = new LevenshteinDistanceFunction("phone");
        NeedlemanWunschFunction similarityFunc5 = new NeedlemanWunschFunction("type");

        //Escrita do cabeçalho
        try {
            escreveArqVetor = new FileWriter(getVetorSimilaridade(), false); //O parâmetro false faz com que as informações sejam sobreescritas

            bwArqVetor = new BufferedWriter(escreveArqVetor);
            bwArqVetor.write("elemento1;elemento2;name;addr;city;phone;type\n"); //ERA ASSIM

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

//                firstSoundex = pair.getFirstElement().getAttributeValues("artist").toString();
//                secondSoundex = pair.getSecondElement().getAttributeValues("artist").toString();

                elemento1Par = pair.getFirstElement().toString();
                elemento2Par = pair.getSecondElement().toString();
                elemento1Par = elemento1Par.replaceAll("\\D", ""); //Exclui tudo que não for número
                elemento2Par = elemento2Par.replaceAll("\\D", ""); //Exclui tudo que não for número

                //Fecho transitivo
                if (mapDivergsMaior.containsKey(elemento1Par + ";" + elemento2Par) || mapDivergsMaior.containsKey(elemento2Par + ";" + elemento1Par)) {

                    id++;

//                    if (id > 20) {
//                        break;
//                    }
//                        System.out.println(firstSoundex + "-" + secondSoundex);
                    a = Double.toString(similarityFunc.getSimilarity(pair));
                    b = Double.toString(similarityFunc2.getSimilarity(pair));
                    c = Double.toString(similarityFunc3.getSimilarity(pair));
                    d = Double.toString(similarityFunc4.getSimilarity(pair));
                    e = Double.toString(similarityFunc5.getSimilarity(pair));
                    
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

        VetorSimEstat11 obj;
        obj = new ExperimentosRestaurant();

//        AllQPEtapa11 expCds = new AllQPEtapa11("cd", "cd_gold", null, null, null, null, null, null, 23, 0, 0, 0, 0, 0, 9763, 0, 0, 0, 1000, null, null, null, null, null, true, false, false, false, false, false, false, false, false, obj);
//        expCds.setParamVetorSim("pk", "disc1_id", "disc2_id", ';', null, null, null, ',', false, false);
//        expCds.rodaExpDedup();
//        AllQPEtapa11 expCdsQP2b = new AllQPEtapa11("cd", "cd_gold", null, null, null, null, null, null, 23, 10, 0, 0, 0, 0, 9763, 0, 0, 0, 1000, new int[]{10}, null, null, null, null, false, true, false, false, false, false, false, false, false, obj);
//        AllQPEtapa11 expCdsQP2b = new AllQPEtapa11("cd", "cd_gold", null, null, null, null, null, null, 23, 7, 10, 6, 7, 7, 9763, 0, 0, 0, 10, true, true, true, true, true, true, false, false, false, obj);
//        expCdsQP2b.setParamVetorSim("pk", "disc1_id", "disc2_id", ';', null, null, null, ',', false, false);
//        expCdsQP2b.rodaExpDedup();
//        AllQPEtapa11 expGeraVetorMaior = new AllQPEtapa11("cd", "cd_gold", null, null, null, null, null, null, 31, 16, 0, 15, 16, 16, 9763, 0, 0, 0, 1, true, true, false, true, true, true, false, false, false, obj);
//        expGeraVetorMaior.setParamVetorSim("pk", "disc1_id", "disc2_id", ';', null, null, null, ',', false, false);
//        expGeraVetorMaior.rodaExpDedup();

//        AllQPEtapa11 expCdsQP1e2e3 = new AllQPEtapa11("cd", "cd_gold", null, null, null, null, null, null, 31, 16, 0, 15, 16, 16, 9763, 0, 0, 0, 1000, false, true, false, true, true, true, false, false, false, obj);
//        expCdsQP1e2e3.setParamVetorSim("pk", "disc1_id", "disc2_id", ';', null, null, null, ',', true, false);
//        expCdsQP1e2e3.rodaExpDedup();

//        AllQPEtapa11 expRest = new AllQPEtapa11("restaurant", "restaurant_gold", null, null, null, null, null, null, 37, 18, 0, 19, 18, 18, 9763, 0, 0, 0, 500, true, true, false, true, true, true, false, false, false, obj);
//        expRest.setParamVetorSim("id", "id_1", "id_2", ',', null, null, null, ',', true, false);
//        expRest.rodaExpDedup();
        
        AllQPEtapa11 expQP1 = new AllQPEtapa11("restaurant", "restaurant_gold", null, null, null, null, null, null, 37, 18, 0, 19, 18, 18, 864, 0, 0, 0, 500, true, true, false, true, true, true, false, false, false, obj);
        expQP1.setParamVetorSim("id", "id_1", "id_2", ',', null, null, null, ',', true, false);
        expQP1.rodaExpDedup();

//        AllQPEtapa11 expCdsMenosQP5 = new AllQPEtapa11("cd", "cd_gold", "cdO", "cdO_gold", "cdT", "cdT_gold", null, null, 23, 10, 15, 6, 10, 10, 9763, 9763, 9763, 0, 1, true, true, true, true, true, true, true, true, false, obj);
//        expCdsMenosQP5.setParamVetorSim("pk", "disc1_id", "disc2_id", ';', null, null, null, ',', false, false);
//        expCdsMenosQP5.rodaExpDedup();
    }

}
