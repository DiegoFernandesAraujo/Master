/*
 * Classe utilizada para gerar o conjunto de vetores de similaridades 
 * que será base para o conjunto treinamento dos algoritmos de AA.
 *
 * Atenção especial para a comparação dos IDs.
 * Se conseguir recuperar o texto de cada ID normalmente, deverá ser feito um método
 * para desconsiderar seus colchetes ([[ e ]]) para comparar com o conjunto de divergências gerado antes.
 */
package experimentos;

import DS.DgStd1;
import DS.ExecVetorSimCDs;
import dedupalgorithms.*;
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

/**
 * Método geraVetorMaior() gera os vetores de similaridades juntamente com as
 * estatísticas (méd, mín, máx) necessárias para a execução da abordagem AA
 * baseada em monotonicidade.
 *
 * @author Diego
 */
public abstract class VetorSimEstat11 extends DedupAlg {

    File vetorSimilaridade;
    File dirDiverg;

    String baseDados1;
    String baseDados2;
    String chavePrimaria;
    String chavePrimaria1;
    String chavePrimaria2;
    String gold;
    String goldId1;
    String goldId2;
    String idBaseDados;
    char separator;
    String qp;
    Map<String, String> mapVetorMaior;

    public boolean isGerVetMaior() {
        return geraVetorMaior;
    }

    public void setGerVetMaior(boolean geraVetorMaior) {
        this.geraVetorMaior = geraVetorMaior;
    }

    boolean geraVetorMaior;

    public VetorSimEstat11() {

    }

    public VetorSimEstat11(String baseDados1, String chavePrimaria, String gold, String goldId1, String goldId2, char separator, String qp) {
        super(baseDados1, chavePrimaria, gold, goldId1, goldId2, separator);

    }

    public void setAllVarDedup(String baseDados1, String chavePrimaria, String gold, String goldId1, String goldId2, char separator, String qp, boolean geraVetor) {
        this.baseDados1 = baseDados1;
        this.chavePrimaria = chavePrimaria;
        this.gold = gold;
        this.goldId1 = goldId1;
        this.goldId2 = goldId2;
        this.separator = separator;
        this.qp = qp;
//        geraVetorMaior = geraVetorMaior;
        super.setAllVarDedup(baseDados1, chavePrimaria, gold, goldId1, goldId2, separator);

//        vetorSimilaridade = new File("./src/csv/conjuntosDS/vetorSimilaridades", "vetorSimilaridades-" + baseDados1 + "-" + qp + ".csv");
        vetorSimilaridade = new File("./src/csv/conjuntosDS/vetorSimilaridades", "vetorSimilaridades-" + baseDados1 + ".csv");
        dirDiverg = new File("./src/csv/conjuntosDS/conjuntosDivergAA/" + baseDados1 + "/" + qp + "/");

        if (!vetorSimilaridade.exists()) {
            System.out.println("Não existe arquivo vetorSimilaridade.csv.");

            try {
                vetorSimilaridade.createNewFile();

            } catch (IOException ex) {
                System.out.println("Não foi possível criar arquivo vetorSimilaridade.csv.");
            }

        }
        setGerVetMaior(geraVetor);

        exeGerVetMaior();
    }

    //XML!
    public void setAllVarDedup(String baseDados1, String chavePrimaria, String gold, String goldId1, String goldId2, char separator, String qp, boolean geraVetor, String xml) {
        this.baseDados1 = baseDados1;
        this.chavePrimaria = chavePrimaria;
        this.gold = gold;
        this.goldId1 = goldId1;
        this.goldId2 = goldId2;
        this.separator = separator;
        this.qp = qp;
//        geraVetorMaior = geraVetorMaior;
        super.setAllVarDedup(baseDados1, chavePrimaria, gold, goldId1, goldId2, separator, xml);

//        vetorSimilaridade = new File("./src/csv/conjuntosDS/vetorSimilaridades", "vetorSimilaridades-" + baseDados1 + "-" + qp + ".csv");
        vetorSimilaridade = new File("./src/csv/conjuntosDS/vetorSimilaridades", "vetorSimilaridades-" + baseDados1 + ".csv");
        dirDiverg = new File("./src/csv/conjuntosDS/conjuntosDivergAA/" + baseDados1 + "/" + qp + "/");

        if (!vetorSimilaridade.exists()) {
            System.out.println("Não existe arquivo vetorSimilaridade.csv.");

            try {
                vetorSimilaridade.createNewFile();

            } catch (IOException ex) {
                System.out.println("Não foi possível criar arquivo vetorSimilaridade.csv.");
            }

        }
        setGerVetMaior(geraVetor);

        exeGerVetMaior();
    }

    public VetorSimEstat11(String baseDados1, String baseDados2, String chavePrimaria1, String chavePrimaria2, String gold, String goldId1, String goldId2, char separator, String qp) {
        super(baseDados1, baseDados2, chavePrimaria1, chavePrimaria2, gold, goldId1, goldId2, separator);
    }

    public void setAllVarDedup(String baseDados1, String baseDados2, String chavePrimaria1, String chavePrimaria2, String gold, String goldId1, String goldId2, char separator, String qp, boolean geraVetor) {

        this.baseDados1 = baseDados1;
        this.baseDados2 = baseDados2;
        this.chavePrimaria1 = chavePrimaria1;
        this.chavePrimaria2 = chavePrimaria2;
        this.gold = gold;
        this.goldId1 = goldId1;
        this.goldId2 = goldId2;
        this.separator = separator;
        this.qp = qp;
        super.setAllVarDedup(baseDados1, baseDados2, chavePrimaria1, chavePrimaria2, gold, goldId1, goldId2, separator);

//        vetorSimilaridade = new File("./src/csv/conjuntosDS/vetorSimilaridades", "vetorSimilaridades-" + baseDados1 + "-" + baseDados2 + "-" + qp + ".csv");
        vetorSimilaridade = new File("./src/csv/conjuntosDS/vetorSimilaridades", "vetorSimilaridades-" + baseDados1 + "-" + baseDados2 + ".csv");
        dirDiverg = new File("./src/csv/conjuntosDS/conjuntosDivergAA/" + baseDados1 + "-" + baseDados2 + "/" + qp + "/");

        if (!vetorSimilaridade.exists()) {
            System.out.println("Não existe arquivo vetorSimilaridade.csv.");

            try {
                vetorSimilaridade.createNewFile();

            } catch (IOException ex) {
                System.out.println("Não foi possível criar arquivo vetorSimilaridade.csv.");
            }

        }
        setGerVetMaior(geraVetor);

        exeGerVetMaior();
    }

    /**
     * A ser sobreescrito para cada base
     *
     * @param arqDiverg
     * @throws IOException
     */
    public abstract void geraVetorMaior(File arqDivergMaior) throws IOException;

    public void exeGerVetMaior() {

        if (geraVetorMaior) {//Dar um jeito de conseguir o conjunto de todas divergências antes!
            try {
                geraVetorMaior(getFileDiverg()); //Para gerar o vetor base dos demais'
            } catch (IOException ex) {
                Logger.getLogger(VetorSimEstat11.class.getName()).log(Level.SEVERE, null, ex);
            }
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
//            JOptionPane.showMessageDialog(null, "Vetor de similaridades vazio! Especifique outro arquivo!");
            System.out.println("Vetor de similaridades vazio! Especifique outro arquivo!");
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

//                        System.out.println("elementoVetorSim1: " + elementoVetorSim1 + " - " + "elementoVetorSim2: " + elementoVetorSim2);
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
            System.out.println("Não foi possível encontrar o arquivo " + arqDiverg.getName() + " em geraVetorMenor()");
        } catch (IOException ex) {
            Logger.getLogger(VetorSimEstat11.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            bwVetorMenor.flush();
            bwVetorMenor.close();

            brDiverg.close();
            brVetorSim.close();
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
    public void geraVetorMenor2(File arqDiverg) throws IOException {

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

                if (linha++ == 0) {
                    if (mapVetorMaior.containsKey("elemento1;elemento2")){
                        bwVetorMenor.write("elemento1;elemento2;qtdAlg;min;max;med;duplicata;" + mapVetorMaior.get("elemento1;elemento2") + "\n"); //NOVO
                        
                    }
                }

//                System.out.println("elementoDiverg1: " + elementoDiverg1 + " - " + "elementoDiverg2: " + elementoDiverg2);
                if (mapVetorMaior.containsKey(elementoDiverg1 + ";" + elementoDiverg2) || mapVetorMaior.containsKey(elementoDiverg2 + ";" + elementoDiverg1)) {

                    bwVetorMenor.append(str);
                    bwVetorMenor.append(';');
                    try {

                        bwVetorMenor.append(mapVetorMaior.get(elementoDiverg1 + ";" + elementoDiverg2) + "\n");
                    } catch (Exception ex) {
                        bwVetorMenor.append(mapVetorMaior.get(elementoDiverg2 + ";" + elementoDiverg1) + "\n");
                    }
                    bwVetorMenor.flush();

                }

            }
        } catch (FileNotFoundException ex) {
            System.out.println("Não foi possível encontrar o arquivo " + arqDiverg.getName() + " em geraVetorMenor()");
        } catch (IOException ex) {
            Logger.getLogger(VetorSimEstat11.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            bwVetorMenor.flush();
            bwVetorMenor.close();

            brDiverg.close();

        }
    }

    public void populaMapVetorMaior() {

//        System.out.println("populaMapArqResult!");
        //Limpando valores anteriores
        try {
            mapVetorMaior.clear();
        } catch (NullPointerException ex) {
            System.out.println("ERRO NO CLEAR!");
            Logger.getLogger(DgStd1.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            System.out.println(e);
        }

        String str;
        String[] linhaAtual1;
        String elementoA1;
        String elementoA2;

        BufferedReader brVetorMaior = null;

        //Armazenando valores do arquivo atual no mapa
        try {

            brVetorMaior = new BufferedReader(new FileReader(getVetorSimilaridade().getPath()));
//            System.out.println(getArqResult().getName());

//            int linha = 0;
            while ((str = brVetorMaior.readLine()) != null) {

//                if (linha > 0) {
                linhaAtual1 = str.split(";", 3);

                elementoA1 = linhaAtual1[0];
                elementoA2 = linhaAtual1[1];
//                System.out.println(elementoA1 + ";" + elementoA2 + " - " + linhaAtual1[2]);

//                mapVetorMaior.put(elementoA1 + ";" + elementoA2, elementoA1 + ";" + elementoA2 + ";" + linhaAtual1[2]);
                mapVetorMaior.put(elementoA1 + ";" + elementoA2, linhaAtual1[2]);
//                }
//                linha++;
            }

//            while ((str = brHistDA.readLine()) != null) {
//
//                linhaAtual = str.split(";", 4);
//
//                elemento1 = linhaAtual[0];
//                elemento2 = linhaAtual[1];
//
//                mapVetorMaior.put(elemento1 + ";" + elemento2, linhaAtual[2]);
            brVetorMaior.close();
        } catch (NullPointerException ex) {
            Logger.getLogger(DgStd1.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(DgStd1.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            brVetorMaior = null;
            elementoA1 = null;
            elementoA2 = null;
//            int cont = 0;
//            for (Map.Entry<String, String> entry : mapVetorMaior.entrySet()) {
//
//                System.out.println(++cont + " " + entry.getKey() + " - " + entry.getValue());
//                System.out.println(entry.getKey());
        }
    }

    public void executaGerVetMenor() {

        mapVetorMaior = new HashMap<String, String>();
        populaMapVetorMaior();

        try {

            if (getDirDiverg().isDirectory()) {
                File[] divergs = getDirDiverg().listFiles();

                for (File arq : divergs) {

                    String nome = arq.getName();

                    System.out.println("Nome do arquivo: " + nome);

                    if (nome.contains("diverg") && !nome.contains("_NEW")) {
//                        geraVetorMenor(arq, getVetorSimilaridade());
                        geraVetorMenor2(arq);
                        arq.delete(); //Exclui o arquivo depois de gerar os vetores de similaridade
                    }

                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ExecVetorSimCDs.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public File getVetorSimilaridade() {
        return vetorSimilaridade;
    }

    public void setVetorSimilaridade(File vetorSimilaridade) {
        this.vetorSimilaridade = vetorSimilaridade;
    }

    public File getDirDiverg() {
        return dirDiverg;
    }

    public File getFileDiverg() {
        File diverg = new File("./src/csv/conjuntosDS/", "NAO_DA.csv"); //Esse arquivo tem que possuir todas as divergências
        return diverg;
    }

}
