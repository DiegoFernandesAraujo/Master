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
import javax.swing.JOptionPane;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Soundex;

/**
 * Método geraVetor() gera os vetores de similaridades juntamente com as
 * estatísticas (méd, mín, máx) necessárias para a execução da abordagem AA
 * baseada em monotonicidade.
 *
 * @author Diego
 */
public abstract class VetorSimEstat1 extends DedupAlg {

    File vetorSimilaridade = null;

    FileWriter escreveArqVetor;
    BufferedWriter bwArqVetor = null;
    String a, b, c, d, e, f, g, rotulo;

    public VetorSimEstat1(String baseDados1, String chavePrimaria, String gold, String goldId1, String goldId2, String result, File vetor, char separator) {
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

    public File getVetorSimilaridade() {
        return vetorSimilaridade;
    }

    public void setVetorSimilaridade(File vetorSimilaridade) {
        this.vetorSimilaridade = vetorSimilaridade;
    }

    /**
     * A ser sobreescrito para cada base
     *
     * @param arqDiverg
     * @throws IOException
     */
    public abstract File geraVetor(File arq) throws IOException;

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
        String str;
        String str2;
        String elementoDiverg1;
        String elementoDiverg2;
        String elementoVetorSim1;
        String elementoVetorSim2;
        String[] linhaAtual;
        String[] linhaAtualVetor;

        BufferedReader brDiverg = null;
        BufferedReader brVetorSim = null;
        BufferedWriter bwVetorMenor = null;
        FileWriter escreveVetorMenor;
        int linha = 0;
        
        if (vetorSim.length() == 0) {
            JOptionPane.showMessageDialog(null, "Vetor de similaridades vazio! Especifique outro arquivo!");
            System.exit(0);
        }

        try {
            brDiverg = new BufferedReader(new FileReader(arqDiverg.getPath()));

            String diretorio = arqDiverg.getParent();
            String nome = arqDiverg.getName();
            nome = nome.substring(0, nome.indexOf('.'));

            escreveVetorMenor = new FileWriter(diretorio + "/" + nome + "_NEW.csv", false);
            bwVetorMenor = new BufferedWriter(escreveVetorMenor);

//            bwVetorMenor.write("elemento1;elemento2;title;artist;track01;track02;track03;duplicata\n");
            //Ordem para o algoritmo de Peter Christen
            //ANTERIOR
//            bwVetorMenor.write("elemento1;elemento2;qtdAlg;min;max;med;duplicata;title;artist;track01;track02;track03;track10;track11\n");
            //Construir a string acima a partir do cabeçalho de NAO_DA2 e do vetorMaior
            while ((str = brDiverg.readLine()) != null) {

                if (str.contains("elemento1")) {
//                    System.out.println("Entrei no Str.contains");
                    continue;

                }

                linhaAtual = str.split(";");

                elementoDiverg1 = linhaAtual[0];
                elementoDiverg2 = linhaAtual[1];

                brVetorSim = new BufferedReader(new FileReader(vetorSim.getPath()));

//                System.out.println("elementoDiverg1: " + elementoDiverg1 + " - " + "elementoDiverg2: " + elementoDiverg2);
                while ((str2 = brVetorSim.readLine()) != null) {

//                    linhaAtualVetor = Str2.split(";");
                    linhaAtualVetor = str2.split(";", 3);

                    if (linha++ == 0) {
                        bwVetorMenor.write("elemento1;elemento2;qtdAlg;min;max;med;duplicata;" + linhaAtualVetor[2] + "\n"); //NOVO
                    }

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
                        bwVetorMenor.append(str);
                        bwVetorMenor.append(';');
                        bwVetorMenor.append(linhaAtualVetor[2]);

//                        if (cont++ == 0) {
//                            System.out.println("Str: " + str);
//                            System.out.println("linhaAtualVetor[2]: " + linhaAtualVetor[2]);
//                        }
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
            Logger.getLogger(VetorSimEstat1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            bwVetorMenor.flush();
            bwVetorMenor.close();

            brDiverg.close();
            brVetorSim.close();
        }
    }

}
