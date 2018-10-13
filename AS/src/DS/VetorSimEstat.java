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
import dude.similarityfunction.contentbased.impl.simmetrics.LevenshteinDistanceFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.MongeElkanFunction;
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
import uk.ac.shef.wit.simmetrics.similaritymetrics.Soundex;

/**
 * Método geraVetor() gera os vetores de similaridades juntamente com as
 * estatísticas (méd, mín, máx) necessárias para a execução da abordagem AA
 * baseada em monotonicidade.
 *
 * @author Diego
 */
public class VetorSimEstat extends DedupAlg {

    File vetorSimilaridade = new File("./src/csv/conjuntosDS/vetorSimilaridades", "vetorSimilaridades-24-06.csv");
    FileWriter escreveArqVetor;
    BufferedWriter bwArqVetor = null;
    String a, b, c, d, e, f, g, rotulo;

    /**
     * Não utilizado
     * @param baseDados1
     * @param chavePrimaria
     * @param gold
     * @param goldId1
     * @param goldId2
     * @param result
     */
    public VetorSimEstat(String baseDados1, String chavePrimaria, String gold, String goldId1, String goldId2, String result, char separator) {
        super(baseDados1, chavePrimaria, gold, goldId1, goldId2, separator);

        if (!vetorSimilaridade.exists()) {
            System.out.println("Não existe arquivo vetorSimilaridade.csv.");

            try {
                vetorSimilaridade.createNewFile();

            } catch (IOException ex) {
                System.out.println("Não foi possível criar arquivo vetorSimilaridade.csv.");
            }

        }
    }
    
    public VetorSimEstat(String baseDados1, String chavePrimaria, String gold, String goldId1, String goldId2, String result, File vetor, char separator) {
        super(baseDados1, chavePrimaria, gold, goldId1, goldId2, separator);
        
        vetorSimilaridade = vetor;
    
        if (!vetorSimilaridade.exists()) {
            System.out.println("Não existe arquivo vetorSimilaridade.csv.");

            try {
                vetorSimilaridade.createNewFile();

            } catch (IOException ex) {
                System.out.println("Não foi possível criar arquivo vetorSimilaridade.csv.");
            }

        }
    }

    /**
     * A ser sobreescrito para cada base
     * @param arqDiverg
     * @throws IOException
     */
    public void geraVetor(File arqDiverg) throws IOException {

        String Str;
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
        }

        //Escrita dos valores de similaridade
        try {
            brDiverg = new BufferedReader(new FileReader(arqDiverg.getPath()));
            escreveArqVetor = new FileWriter(vetorSimilaridade, true); //O parâmetro true faz com que as informações não sejam sobreescritas
            bwArqVetor = new BufferedWriter(escreveArqVetor);
            int id = 0;

            while ((Str = brDiverg.readLine()) != null) {

                linhaAtual = Str.split(";", 3);

                elemento1 = linhaAtual[0];
                elemento2 = linhaAtual[1];

                if (elemento1.contains("elemento1")) {
                    continue;
                }

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
                        b = Double.toString(similarityFunc2.getSimilarity(firstSoundex, secondSoundex));
                        c = Double.toString(similarityFunc3.getSimilarity(pair));
                        d = Double.toString(similarityFunc4.getSimilarity(pair));
                        e = Double.toString(similarityFunc5.getSimilarity(pair));
                        f = Double.toString(similarityFunc6.getSimilarity(pair));
                        g = Double.toString(similarityFunc7.getSimilarity(pair));

//                        if (statistic.isDuplicate(pair)) {
//                            rotulo = "1.0";
//                        } else {
//                            rotulo = "0.0";
//                        }

                        try {
//                            bwArqVetor.append(Integer.toString(id));
//                            bwArqVetor.append(';');
//                            bwArqVetor.append(Str);
//                            bwArqVetor.append(';');
//                            bwArqVetor.append(rotulo);
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

    //A partir do vetor de similaridades geral cria um vetor menor dado o par de possíveis duplicatas
    //existente no arquivo de divergências informado
    /**
     *
     * @param arqDiverg
     * @param vetorSim
     * @throws IOException
     */
    public void geraVetorMenor(File arqDiverg, File vetorSim) throws IOException {

//O gabarito tem de estar sem aspas
        String Str;
        String Str2;
        String elementoDiverg1;
        String elementoDiverg2;
        String elementoVetorSim1;
        String elementoVetorSim2;
        String[] linhaAtual;
        String[] linhaAtualVetor;

        File vetorMenor = null;
        BufferedReader brDiverg = null;
        BufferedReader brVetorSim = null;
        BufferedWriter bwVetorMenor = null;
        FileWriter escreveVetorMenor;

        try {
            brDiverg = new BufferedReader(new FileReader(arqDiverg.getPath()));

            String diretorio = arqDiverg.getParent();
            String nome = arqDiverg.getName();
            nome = nome.substring(0, nome.indexOf('.'));

            escreveVetorMenor = new FileWriter(diretorio + "/" + nome + "_NEW.csv", false);
            vetorMenor = new File(diretorio + "/" + nome + "_NEW.csv");
            bwVetorMenor = new BufferedWriter(escreveVetorMenor);

//            bwVetorMenor.write("elemento1;elemento2;title;artist;track01;track02;track03;duplicata\n");
            //Ordem para o algoritmo de Peter Christen
            bwVetorMenor.write("elemento1;elemento2;qtdAlg;min;max;med;duplicata;title;artist;track01;track02;track03;track10;track11\n");
            //Construir a string acima a partir do cabeçalho de NAO_DA2 e do vetorMaior
            
            
            while ((Str = brDiverg.readLine()) != null) {
                
                if (Str.contains("elemento1")) {
                    System.out.println("Entrei no Str.contains");
                    continue;
                    
                }

                linhaAtual = Str.split(";");

                elementoDiverg1 = linhaAtual[0];
                elementoDiverg2 = linhaAtual[1];

                brVetorSim = new BufferedReader(new FileReader(vetorSim.getPath()));

//                System.out.println("elementoDiverg1: " + elementoDiverg1 + " - " + "elementoDiverg2: " + elementoDiverg2);
                while ((Str2 = brVetorSim.readLine()) != null) {

//                    linhaAtualVetor = Str2.split(";");
                    linhaAtualVetor = Str2.split(";", 3);
                    int cont = 0;

                    elementoVetorSim1 = linhaAtualVetor[0];
                    elementoVetorSim2 = linhaAtualVetor[1];
                    
//                    System.out.println("elementoVetorSim1: " + elementoVetorSim1 + " - " + "elementoVetorSim2: " + elementoVetorSim2);

//                    System.out.println("elementoVetorSim1: " + elementoVetorSim1 + " - " + "elementoVetorSim2: " + elementoVetorSim2);
                    if (((elementoVetorSim1.equals(elementoDiverg1)) && (elementoVetorSim2.equals(elementoDiverg2))) || ((elementoVetorSim1.equals(elementoDiverg2)) && ((elementoVetorSim2.equals(elementoDiverg1))))) {

                        System.out.println("elementoVetorSim1: " + elementoVetorSim1 + " - " + "elementoVetorSim2: " + elementoVetorSim2);
//                        System.out.println(linhaAtualVetor.length);
//                        for (String valor : linhaAtualVetor) {
//                            bwVetorMenor.append(valor);
                        bwVetorMenor.append(Str);
                        bwVetorMenor.append(';');
                        bwVetorMenor.append(linhaAtualVetor[2]);
                        
                        if(cont++ == 0){
                            System.out.println("Str: " + Str);
                            System.out.println("linhaAtualVetor[2]: " + linhaAtualVetor[2]);
                        }

//                            if (cont < linhaAtualVetor.length - 1) {
//                                bwVetorMenor.append(';');
//                            }
//                            cont++;
//                        }
                        bwVetorMenor.append('\n');
                        bwVetorMenor.flush();
                        break;

                    }
                }

                brVetorSim.close();
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Não foi possível encontrar o arquivo " + arqDiverg.getName() + " em buscaGabarito()");
        } catch (IOException ex) {
            Logger.getLogger(VetorSimEstat.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            bwVetorMenor.flush();
            bwVetorMenor.close();

            brDiverg.close();
            brVetorSim.close();
        }
    }

}
