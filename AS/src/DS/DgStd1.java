/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DS;

import AS.*;
import static AS.AplicacaoASDS.geraOrdAlg;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.LineNumberReader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Diego Em relação à classe DgStd possui o método copiaArqDiverg(), um
 * método para manter salvo o arquivo de divergências gerado
 */
public class DgStd1 {

    int tp, fp, tn, fn, iteracao, permutacao, tamBaseOrig, tamBaseOrig2, qtdAlg;
    boolean dedup = false;

    boolean geraEst = false;

    File estatisticas;
    File DA;
    File historicoDA;
    File historicoNAODA;

    File divergencias, divergencias2;
    File estatDA, estatNAODA, estatNAODAIncr;

    File DM;
    File NDM;
    File gs;
    File DADM;
    FileWriter escreveEstat;

    /**
     *
     */
    public DgStd1() {
        tp = 0;
        fp = 0;
        iteracao = 0;

        estatisticas = new File("./src/csv/", "estatisticaInicialDS.csv");
//        estatisticas = new File("./src/csv/", "estatisticaInicialDS-DEMO.csv"); 

        if (!estatisticas.exists()) {
            System.out.println("Não existe arquivo estatisticas.csv.");

            try {
                estatisticas.createNewFile();
                BufferedWriter bwEstat = null;
                try {
                    escreveEstat = new FileWriter(estatisticas, true); //O parâmetro true faz com que as informações não sejam sobreescritas
                    bwEstat = new BufferedWriter(escreveEstat);

                    bwEstat.write("abordagem;etapa;algoritmosUtilizados;permutacao;iteracao;inspecoesManuais;precision;recall;f-measure;da;dm;ndm;tp;fp;tn;fn\n");

                } catch (IOException ex) {
                    System.out.println("Não foi possível escrever o cabeçalho no arquivo estatisticas.csv.");
                } finally {
                    bwEstat.flush();
                    bwEstat.close();

                }

            } catch (IOException ex) {
                System.out.println("Não foi possível criar arquivo estatisticas.csv.");
            }
        }

    }

    /**
     *
     * @param arq
     * @return
     * @throws IOException
     */
    public File padronizaCsvFile(File arq) throws IOException {

        File arqPadr = null;
        BufferedReader brArq = null;
        FileWriter escreveArqPadr = null;
        try {
            brArq = new BufferedReader(new FileReader(arq.getPath()));

            String Str = "";
            String Str2 = "";
            int index_c1 = 0;
            int index_c2 = 0;
            int cont = 0;
            String[] linhaAtual;

            String diretorio = arq.getParent();
            String nome = arq.getName();
            nome = nome.substring(0, nome.indexOf('.'));

            escreveArqPadr = new FileWriter(diretorio + "\\" + nome + "_NEW.csv", false);
            arqPadr = new File(diretorio + "\\" + nome + "_NEW.csv");

            while ((Str = brArq.readLine()) != null) {

                if (Str.contains("First Object")) {
                    continue;
                }
                //Aqui usamos o método split que divide a linha lida em um array de String
                //passando como parametro o divisor ";".

//                linhaAtual = Str.split(";", 2); //Nesse caso considera apenas as duas primeiras colunas (as que interessam)
//                cont = 0;
//
//                for (String cell : linhaAtual) {
//
//                    cont++;
//
//                    index_c1 = cell.indexOf('[');
//                    index_c2 = cell.indexOf(']');
//
//                    Str2 = cell.substring(index_c1 + 1, index_c2);
//
//                    escreveArqPadr.append(Str2);
//
//                    if (cont == 1) {
//                        escreveArqPadr.append(';');
//                    } else {
//                        escreveArqPadr.append('\n');
//                    }
//
//                }//FIM DO FOR APRIMORADO
                //Considerando valores de similaridade além dos identificadores dos pares.
                linhaAtual = Str.split(";", 3); //Nesse caso considera apenas as duas primeiras colunas (as que interessam)
                cont = 0;

//                System.out.println("PADRONIZA_CSV_NO_DS");
                for (int i = 0; i < linhaAtual.length; i++) {

                    String cell = linhaAtual[i];
                    cont++;
//                    System.out.println("cell: " + cell);

                    if (i <= 1) {

                        index_c1 = cell.indexOf('[');
                        index_c2 = cell.indexOf(']');

                        Str2 = cell.substring(index_c1 + 1, index_c2);
//                        System.out.println("Str2: " + Str2);
                    } else {
                        Str2 = cell;

//                        System.out.println("Str2 = cell: " + Str2);
                    }

                    escreveArqPadr.append(Str2);
                    escreveArqPadr.append(';');

                    if (cont > 2) {
                        escreveArqPadr.append('\n');
                    }
                }//FIM DO FOR
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            escreveArqPadr.flush();
            escreveArqPadr.close();
            brArq.close();
        }
        return arqPadr;
    }

    /**
     *
     * @param arqResult
     * @throws IOException
     */
    public void comparaComGS(File arqResult) throws IOException {
        //Já deve receber o arqResult padronizado
        //Adicionar um teste para saber se está padronizado ou não, para poder tratar o arquivo

        tp = 0;
        fp = 0;
//        int cont = 0;

        String Str;
        String elemento1;
        String elemento2;
        String[] linhaAtual;
        boolean existe = false;

        BufferedReader brResult = null;
        try {
            brResult = new BufferedReader(new FileReader(arqResult.getPath()));

            while ((Str = brResult.readLine()) != null) {

                linhaAtual = Str.split(";", 3);
//                System.out.println(Str);
//                cont++;
                

                elemento1 = linhaAtual[0];
                elemento2 = linhaAtual[1];

                existe = buscaGabarito(elemento1, elemento2, gs);

                if (existe) {
                    tp++;
                } else {
                    fp++;
                }

            }

//            gravaEstatisticas(tp, fp, gs, arqResult);
        } catch (FileNotFoundException ex) {
            System.out.println("Não foi possível encontrar o arquivo " + arqResult.getName());
        } catch (IOException ex) {
            Logger.getLogger(DgStd1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            brResult.close();
            gravaEstatisticas(tp, fp, gs, arqResult);
//            System.out.println("cont: " + cont);
        }
    }

    private boolean buscaGabarito(String elemento1, String elemento2, File gs) throws IOException {
        //O gabarito tem de estar sem aspas
        String Str;
        String elementoGS1;
        String elementoGS2;
        String[] linhaAtual;
        boolean existe = false;

        BufferedReader brGS = null;
        try {
            brGS = new BufferedReader(new FileReader(gs.getPath()));

            while ((Str = brGS.readLine()) != null) {

                linhaAtual = Str.split(";", 3);

                elementoGS1 = linhaAtual[0];
                elementoGS2 = linhaAtual[1];

                if (((elemento1.equals(elementoGS1)) && (elemento2.equals(elementoGS2))) || ((elemento1.equals(elementoGS2)) && ((elemento2.equals(elementoGS1))))) {

                    existe = true;
                    break;
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Não foi possível encontrar o arquivo " + gs.getName() + " em buscaGabarito()");
        } catch (IOException ex) {
            Logger.getLogger(DgStd1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            brGS.close();
        }

        return existe;
    }

    /**
     *
     * @param arqResult
     * @throws IOException
     */
    public void comparaConjuntos(File arqResult) throws IOException {

        String Str;
        String[] linhaAtual;
        boolean baseline = false;
        DA = new File("./src/csv/conjuntosDS", "DA.csv");
        historicoDA = new File("./src/csv/conjuntosDS", "historicoDA.csv");
        DADM = new File("./src/csv/conjuntosDS", "DADM.csv");
        DM = new File("./src/csv/conjuntosDS", "DM.csv");
        NDM = new File("./src/csv/conjuntosDS", "NDM.csv");
        FileWriter escreveDupAuto;
        FileWriter escreveHist;

        File aux;

        if (!arqResult.exists()) {
            System.out.println("Arquivo " + arqResult.getName() + " não existe!");
            System.exit(0);
        }

        if (!DA.exists()) {

            BufferedReader brArqResult = null;
            BufferedWriter bwDupAuto = null;
            BufferedWriter bwHist = null;
            System.out.println("Não existe arquivo " + DA.getName());
            try {
                DA.createNewFile();
                historicoDA.createNewFile();
                new Thread().sleep(50);

                brArqResult = new BufferedReader(new FileReader(arqResult.getPath()));

                escreveDupAuto = new FileWriter(DA); //Dessa forma sobreescreve
                bwDupAuto = new BufferedWriter(escreveDupAuto);

                escreveHist = new FileWriter(historicoDA, true); //Dessa forma não sobreescreve
                bwHist = new BufferedWriter(escreveHist);

                //Copiando do primeiro arquivo
                System.out.println("Primeiro DA");

                while ((Str = brArqResult.readLine()) != null) {

                    linhaAtual = Str.split(";", 4);
//                    System.out.println("Str: " + Str);
                    bwDupAuto.write(linhaAtual[0] + ";" + linhaAtual[1] + ";" + linhaAtual[2] + "\n");

//                    System.out.println("linhaAtual[1]: " + linhaAtual[1]);
//                    System.out.println("linhaAtual[2]: " + linhaAtual[2]);
//
//                    System.out.println(linhaAtual[0] + ";" + linhaAtual[1] + ";" + linhaAtual[2]);
                    bwHist.write(linhaAtual[0] + ";" + linhaAtual[1] + ";" + linhaAtual[2] + "\n");

                }

                //DM e NDM são criados obrigatoriamente quando DA é criado
                if (!DM.exists()) {
                    System.out.println("Não existe arquivo " + DM.getName() + " em conjuntosDS");

                    try {
                        DM.createNewFile();
                        new Thread().sleep(50);

                    } catch (FileNotFoundException ex) {

                        Logger.getLogger(DgStd1.class
                                .getName()).log(Level.SEVERE, null, ex);

                    } catch (IOException ex) {
                        Logger.getLogger(DgStd1.class
                                .getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(DgStd1.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

                if (!NDM.exists()) {
                    System.out.println("Não existe arquivo " + NDM.getName());

                    try {
                        NDM.createNewFile();
                        new Thread().sleep(50);

                    } catch (FileNotFoundException ex) {

                        Logger.getLogger(DgStd1.class
                                .getName()).log(Level.SEVERE, null, ex);

                    } catch (IOException ex) {
                        Logger.getLogger(DgStd1.class
                                .getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(DgStd1.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

                //Arquivo para cálculo de precision, recall e f-measure do Annealing todo
                if (!DADM.exists()) {
                    System.out.println("Não existe arquivo " + DADM.getName());

                    try {
                        DADM.createNewFile();
                        new Thread().sleep(50);

                    } catch (FileNotFoundException ex) {

                        Logger.getLogger(DgStd1.class
                                .getName()).log(Level.SEVERE, null, ex);

                    } catch (IOException ex) {
                        Logger.getLogger(DgStd1.class
                                .getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(DgStd1.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

                baseline = true;

            } catch (IOException ex) {
                System.out.println("Não foi possível criar arquivo " + DA.getName());
            } catch (InterruptedException ex) {
                Logger.getLogger(DgStd1.class.getName()).log(Level.SEVERE, null, ex);
            } finally {

//                System.out.println("Entrou no baseline true!");
                brArqResult.close();
                bwDupAuto.flush(); //Alterei DA aqui!!!!!
                bwDupAuto.close();

                bwHist.flush();
                bwHist.close();

//                JOptionPane.showMessageDialog(null, "Veja o arquivo historicoDA!");
                //Juntar DA + DM para calcular precision, recall e f-measure para o TODO, O ANNEALING!
                //Como é o baseline poderia ser calculado apenas com DA mesmo
//                juntaDADM(DA, DM);
//
//                comparaComGS(DADM);
//                comparaComGS(DA); //Não está comentado em DgStd
            }

        }

        if (baseline == false) {

//            System.out.println("Entrou no baseline false!");
            aux = juntaArquivos(DA, arqResult); //Isso é feito para que se possa ver a união e intersecção do resultado atual com o DA

            atualizaDA(aux); //DA deve ficar apenas com a intersecção

            //ATENÇÃO! Apenas para a abordagem de AA
            {
                atualizaHistDA(arqResult);

                atualizaHistNAODA(aux);
            }

//            JOptionPane.showMessageDialog(null, "Veja o arquivo historicoDA!");
            /*
             *ATENÇÃO! Como as estatísticas só são gravadas quando chamadas de dentro do método comparaComGS,
             *esta etapa só está gravando as estatísticas do primeiro algoritmo de deduplicação.
             *Creio que não faz sentido gravar as estatísticas de cada iteração, 
             *a não ser que seja a quantidade de divergências.
             */
            //!!!!!!!!!!!!!!!
            //TRATAR divergencias2 em filtraDivergencias e, assim, não haverá mais necessidade de remDupDiverg!!!!!!
            //!!!!!!!!!!!!!!!
//            contabilizaEstatDA(aux);
            remDupDiverg(filtraDivergencias(aux)); //NAO_DA deve ficar apenas com aquilo que não for intersecção com DA
            copiaArqDiverg(); //Salva o conjunto de divergências atual em diretório específico

            //ATENÇÃO! Os arquivos DN e NDM devem ser povoados a partir do algoritmo de AA
//            atualizaDM_NDM(); //Separação daquilo que não é DA em D_M e ND_M. 
//
//            juntaDADM(DA, DM);
//
            if (geraEst) {
                comparaComGS(DA);
                setGeraEst(false);
            }
        }
    }

    /**
     *
     * @param arqDA
     * @param arq2
     * @return
     * @throws IOException
     */
    public File juntaArquivos(File arqDA, File arq2) throws IOException {

        File juncao = new File("./src/csv/conjuntosDS", "juncao.csv");
        FileWriter escreveJuncao;

        String Str;
        String[] linhaAtual;

        if (!juncao.exists()) {
            System.out.println("Não existe arquivo juncao.csv.");
            try {
                juncao.createNewFile();
                new Thread().sleep(50);

            } catch (FileNotFoundException ex) {

                System.out.println("Não foi possível encontrar o arquivo " + juncao.getName());
            } catch (IOException ex) {
                System.out.println("Não foi possível criar o arquivo " + juncao.getName());
            } catch (InterruptedException ex) {
                Logger.getLogger(DgStd1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //concatenação
        BufferedReader brDA = null;
        BufferedReader brArq2 = null;
        BufferedWriter bwJuncao = null;
        try {

            brDA = new BufferedReader(new FileReader(arqDA.getPath()));
            brArq2 = new BufferedReader(new FileReader(arq2.getPath()));

            escreveJuncao = new FileWriter(juncao);
            bwJuncao = new BufferedWriter(escreveJuncao);

            //Copiando do primeiro arquivo
            while ((Str = brDA.readLine()) != null) {

                linhaAtual = Str.split(";", 3);
//                bwJuncao.write(linhaAtual[0] + ";" + linhaAtual[1] + "\n");
                bwJuncao.write(linhaAtual[0] + ";" + linhaAtual[1] + ";" + linhaAtual[2] + "\n");

            }
            //Copiando do segundo arquivo
            while ((Str = brArq2.readLine()) != null) {

                linhaAtual = Str.split(";", 3);
//                bwJuncao.write(linhaAtual[0] + ";" + linhaAtual[1] + "\n");
                bwJuncao.write(linhaAtual[0] + ";" + linhaAtual[1] + ";" + linhaAtual[2] + "\n");

            }

        } catch (FileNotFoundException ex) {

            Logger.getLogger(DgStd1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DgStd1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            brDA.close();
            brArq2.close();

            bwJuncao.flush();
            bwJuncao.close();

        }

        return juncao;
    }

    /**
     *
     * @param arqDA
     * @param arqDM
     * @throws IOException
     */
    public void juntaDADM(File arqDA, File arqDM) throws IOException {

        FileWriter escreveDADM;

        String Str;
        String[] linhaAtual;

        BufferedReader brDA = null;
        BufferedReader brDM = null;
        BufferedWriter bwJuncao = null;

        try {

            brDA = new BufferedReader(new FileReader(arqDA.getPath()));
            brDM = new BufferedReader(new FileReader(arqDM.getPath()));

            escreveDADM = new FileWriter(DADM); //Dessa forma sobrescreve
            bwJuncao = new BufferedWriter(escreveDADM);

            //Copiando do primeiro arquivo
            while ((Str = brDA.readLine()) != null) {

                linhaAtual = Str.split(";", 3);
                bwJuncao.write(linhaAtual[0] + ";" + linhaAtual[1] + "\n");

            }
            //Copiando do segundo arquivo
            while ((Str = brDM.readLine()) != null) {

                linhaAtual = Str.split(";", 3);
                bwJuncao.write(linhaAtual[0] + ";" + linhaAtual[1] + "\n");

            }

        } catch (FileNotFoundException ex) {

            Logger.getLogger(DgStd1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DgStd1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            brDA.close();
            brDM.close();

            bwJuncao.flush();
            bwJuncao.close();
        }

//        return DADM;
    }

    /**
     *
     * @param arqDuplicatas
     * @return
     * @throws IOException
     */
    public File atualizaDA(File arqDuplicatas) throws IOException {

        String Str;
        String Str2;
        String[] linhaAtual1;
        String[] linhaAtual2;
        List<String> dupEncontradas = new ArrayList<String>();
        boolean existe = false;
        int cont = 0;

        FileWriter escreveNovoDA;

        BufferedReader brArqDup = null;
        BufferedReader brArqDup2 = null;
        BufferedWriter bwDA = null;

        try {

            brArqDup = new BufferedReader(new FileReader(arqDuplicatas.getPath()));

            escreveNovoDA = new FileWriter(DA); //Desta forma sobrescreve
            bwDA = new BufferedWriter(escreveNovoDA);

            String elemento1;
            String elemento2;
            String elementoA;
            String elementoB;

            while ((Str = brArqDup.readLine()) != null) {

                brArqDup2 = new BufferedReader(new FileReader(arqDuplicatas.getPath()));

                linhaAtual1 = Str.split(";", 3);

                elemento1 = linhaAtual1[0];
                elemento2 = linhaAtual1[1];

                while ((Str2 = brArqDup2.readLine()) != null) {

                    linhaAtual2 = Str2.split(";", 3);

                    elementoA = linhaAtual2[0];
                    elementoB = linhaAtual2[1];

                    if (((elemento1.equals(elementoA)) && (elemento2.equals(elementoB))) || ((elemento1.equals(elementoB)) && ((elemento2.equals(elementoA))))) {
                        cont++; //A ideia aqui é encontrar elementos iguais em DA e arqResult
                    }

                    //Quando o mesmo par é encontrado duas vezes é sinal que existe ocorrência dele em DA e no arqResult
                    if (cont >= 2) { //Se o par já tiver sido encontrado uma vez nesse arquivo de resultados, 
                        //a busca por esse elemento pode parar

                        for (int x = 0; x < dupEncontradas.size(); x++) {
                            if (dupEncontradas.get(x).equals(elemento1 + ";" + elemento2)) {
                                existe = true; //Só é verdadeiro se a duplicada já tiver sido encontrada antes
                                break;
                            }

                        }

                        if (existe == false) { //Se não existe ocorrência anterior, adiciona a DA e à lista de duplicadas encontradas
                            bwDA.write(elemento1 + ";" + elemento2 + ";" + linhaAtual1[2] + "\n");
                            dupEncontradas.add(elemento1 + ";" + elemento2);
                        }
                    }
                    existe = false;
                }
                brArqDup2.close();

                cont = 0;

            }
            brArqDup.close();

        } catch (IOException ex) {
            Logger.getLogger(DgStd1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            bwDA.flush();
            bwDA.close();
        }

        return DA;
    }

    /**
     * Armazena no arquivo historicoDA todas as ocorrências dos pares originais
     * do arquivo DA, mesmo que tenham sido removidos deste durante o processo.
     *
     * @param arqResult recebe o conjunto de duplicatas da atual iteração.
     * @return
     * @throws java.io.IOException
     */
    public File atualizaHistDA(File arqResult) throws IOException {

        String Str;
        String Str2;
        String[] linhaAtual1;
        String[] linhaAtual2;
        List<String> dupEncontradas = new ArrayList<String>();
        boolean existe = false;
        int cont = 0;

        FileWriter escreveHist;

        BufferedReader brArqResult = null;
        BufferedReader brHist = null;
        BufferedWriter bwHist = null;

        try {

            brArqResult = new BufferedReader(new FileReader(arqResult.getPath()));

            escreveHist = new FileWriter(historicoDA, true); //Dessa forma não sobreescreve
            bwHist = new BufferedWriter(escreveHist);

            String elemento1;
            String elemento2;
            String similaridade;
            String elementoA;
            String elementoB;

            while ((Str = brArqResult.readLine()) != null) {

                brHist = new BufferedReader(new FileReader(historicoDA.getPath()));

                linhaAtual1 = Str.split(";", 4);

                elemento1 = linhaAtual1[0];
                elemento2 = linhaAtual1[1];
                similaridade = linhaAtual1[2];

//                System.out.println("similaridade no HISTÓRICO: " + similaridade);
                while ((Str2 = brHist.readLine()) != null) {

                    linhaAtual2 = Str2.split(";", 3);

                    elementoA = linhaAtual2[0];
                    elementoB = linhaAtual2[1];

                    if (((elemento1.equals(elementoA)) && (elemento2.equals(elementoB))) || ((elemento1.equals(elementoB)) && ((elemento2.equals(elementoA))))) {
                        bwHist.write(linhaAtual1[0] + ";" + linhaAtual1[1] + ";" + linhaAtual1[2] + "\n");
                        break;
                    }

                }
                brHist.close();

            }
            brArqResult.close();

        } catch (IOException ex) {
            Logger.getLogger(DgStd1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            bwHist.flush();
            bwHist.close();
        }

        return historicoDA;
    }

    /**
     * Armazena no arquivo historicoNAO_DA todas as ocorrências dos pares que
     * apresentaram alguma divergência com o arquivo DA.
     * <b>Importante:</b> Aqueles pares que tenham pertencido ao conjunto DA
     * anteriormente terão seu histórico considerado a partir da sua primeira
     * divergência.
     *
     * @param arqDA
     * @return
     * @throws IOException
     */
    public File atualizaHistNAODA(File arqDA) throws IOException {
        //arqDA contém a junção do que está em DA com o último resultado (com dados repetidos, inclusive)

        ArrayList<String> listaElementos = new ArrayList<String>();

        String Str;
        String[] linhaAtual1;
        String[] linhaAtual2;
        boolean jaExistia = false;
        int cont = 0;

        historicoNAODA = new File("./src/csv/conjuntosDS", "historicoNAO_DA.csv");
//        divergencias2 = new File("./src/csv/conjuntosDS", "NAO_DA2.csv");

        if (!historicoNAODA.exists()) {
            System.out.println("Não existe arquivo NAO_DA.csv.");

            try {
                historicoNAODA.createNewFile();
//                divergencias2.createNewFile();
                new Thread().sleep(50);

            } catch (FileNotFoundException ex) {

                Logger.getLogger(DgStd1.class
                        .getName()).log(Level.SEVERE, null, ex);

            } catch (IOException ex) {
                Logger.getLogger(DgStd1.class
                        .getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(DgStd1.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        BufferedReader brDA = null;
        FileWriter escreveDiverg = null;
        BufferedWriter bwDiverg = null;

//        FileWriter escreveHistNAODA = null;
//        BufferedWriter bwHistNAODA = null;
        try {

            brDA = new BufferedReader(new FileReader(arqDA.getPath()));

            escreveDiverg = new FileWriter(historicoNAODA, true); //Dessa forma NÃO sobrescreve
            bwDiverg = new BufferedWriter(escreveDiverg);

//            escreveHistNAODA = new FileWriter(historicoNAODA, true); //Dessa forma NÃO sobrescreve
//            bwHistNAODA = new BufferedWriter(escreveHistNAODA);
//            escreveDiverg2 = new FileWriter(divergencias2, true); //Dessa forma NÃO sobrescreve
//            bwDiverg2 = new BufferedWriter(escreveDiverg2);
            String elemento1;
            String elemento2;
            String elementoA;
            String elementoB;

            while ((Str = brDA.readLine()) != null) {

                jaExistia = false;
                cont = 0;
                BufferedReader brDA2 = new BufferedReader(new FileReader(arqDA.getPath()));

                linhaAtual1 = Str.split(";", 3);

                elemento1 = linhaAtual1[0];
                elemento2 = linhaAtual1[1];

                while ((Str = brDA2.readLine()) != null) {

                    linhaAtual2 = Str.split(";", 3);

                    elementoA = linhaAtual2[0];
                    elementoB = linhaAtual2[1];

                    if (((elemento1.equals(elementoA)) && (elemento2.equals(elementoB))) || ((elemento1.equals(elementoB)) && ((elemento2.equals(elementoA))))) {

                        cont++;
                    }

                    if (cont >= 2) {
                        jaExistia = true; //Se já existia o par de elementos em DA não deve ser considerada divergência
                        break;
                    }
                }

                brDA2.close();

                if (jaExistia == false) {//NAO_DA é atualizado aqui
                    //ATENÇÃO! Colocar aqui a busca pelo par atual dentro do arquivo NAO_DA de forma que caso ele exista não seja inserido novamente
                    //Isso está sendo feito com o método removeDup()
                    bwDiverg.write(elemento1 + ";" + elemento2 + ";" + linhaAtual1[2] + "\n");
//                    bwDiverg2.write(elemento1 + ";" + elemento2 + ";" + ";" +   "teste\n");

                }
            }
            brDA.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DgStd1.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(DgStd1.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            bwDiverg.flush();
            bwDiverg.close();

//            bwDiverg2.flush();
//            bwDiverg2.close();
        }

        return historicoNAODA;
    }

    public File filtraDivergencias_NEW(File estatDA, File estatNAO_DA) throws IOException {

        String Str;
        String Str2;
        String[] linhaAtual1;
        String[] linhaAtual2;
        List<String> dupEncontradas = new ArrayList<String>();
        boolean existe = false;
        int cont = 0;

//        divergencias2 = new File("./src/csv/conjuntosDS", "NAO_DA2.csv");
        FileWriter escreveDiverg;

        BufferedReader brEstatDA = null;
        BufferedReader brEstatNAO_DA = null;
        BufferedWriter bwDiverg = null;

        try {

//            brEstatDA = new BufferedReader(new FileReader(estatDA.getPath()));
            brEstatNAO_DA = new BufferedReader(new FileReader(estatNAO_DA.getPath()));

            escreveDiverg = new FileWriter(divergencias2, true); //Dessa forma não sobreescreve
            bwDiverg = new BufferedWriter(escreveDiverg);

            String elemento1NAO_DA;
            String elemento2NAO_DA;
            String similaridade;
            String elemento1DA;
            String elemento2DA;

//            while ((Str = brEstatDA.readLine()) != null) {
            bwDiverg.write("elemento1" + ";" + "elemento2" + ";" + "qtdAlg" + ";" + "min" + ";" + "max" + ";" + "med" + "\n");

            while ((Str = brEstatNAO_DA.readLine()) != null) {

                if (Str.contains("elemento1")) {
                    continue;
                }

                existe = false;

//                brEstatNAO_DA = new BufferedReader(new FileReader(estatNAO_DA.getPath()));
                brEstatDA = new BufferedReader(new FileReader(estatDA.getPath()));

                linhaAtual1 = Str.split(";", 6);

                elemento1NAO_DA = linhaAtual1[0];
                elemento2NAO_DA = linhaAtual1[1];
                similaridade = linhaAtual1[2];

//                while ((Str2 = brEstatNAO_DA.readLine()) != null) {
                while ((Str2 = brEstatDA.readLine()) != null) {

                    if (Str2.contains("First Object")) {
                        continue;
                    }

                    linhaAtual2 = Str2.split(";", 6);

                    elemento1DA = linhaAtual2[0];
                    elemento2DA = linhaAtual2[1];

                    if (((elemento1NAO_DA.equals(elemento1DA)) && (elemento2NAO_DA.equals(elemento2DA))) || ((elemento1NAO_DA.equals(elemento2DA)) && ((elemento2NAO_DA.equals(elemento1DA))))) {
                        bwDiverg.write(linhaAtual2[0] + ";" + linhaAtual2[1] + ";" + linhaAtual2[2] + ";" + linhaAtual2[3] + ";" + linhaAtual2[4] + ";" + linhaAtual2[5] + "\n");
                        existe = true;
                        break;
                    }

                }
                brEstatDA.close();

                if (existe == false) {
                    bwDiverg.write(linhaAtual1[0] + ";" + linhaAtual1[1] + ";" + linhaAtual1[2] + ";" + linhaAtual1[3] + ";" + linhaAtual1[4] + ";" + linhaAtual1[5] + "\n");
                }

            }
            brEstatNAO_DA.close();

        } catch (IOException ex) {
            Logger.getLogger(DgStd1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            bwDiverg.flush();
            bwDiverg.close();
        }

        return historicoDA;
    }

    //Para gerar DN e NDM       
    /**
     *
     * @param arqDA
     * @return
     * @throws IOException
     */
    public File filtraDivergencias(File arqDA) throws IOException {
        //arqDA contém a junção do que está em DA com o último resultado (com dados repetidos, inclusive)

        ArrayList<String> listaElementos = new ArrayList<String>();

        String Str;
        String[] linhaAtual1;
        String[] linhaAtual2;
        boolean jaExistia = false;
        int cont = 0;

        divergencias = new File("./src/csv/conjuntosDS", "NAO_DA.csv");
        divergencias2 = new File("./src/csv/conjuntosDS", "NAO_DA2.csv");

        if (!divergencias.exists()) {
            System.out.println("Não existe arquivo NAO_DA.csv.");

            try {
                divergencias.createNewFile();
                divergencias2.createNewFile();
                new Thread().sleep(50);

            } catch (FileNotFoundException ex) {

                Logger.getLogger(DgStd1.class
                        .getName()).log(Level.SEVERE, null, ex);

            } catch (IOException ex) {
                Logger.getLogger(DgStd1.class
                        .getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(DgStd1.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        BufferedReader brDA = null;
        FileWriter escreveDiverg = null;
        BufferedWriter bwDiverg = null;

//        FileWriter escreveHistNAODA = null;
//        BufferedWriter bwHistNAODA = null;
        try {

            brDA = new BufferedReader(new FileReader(arqDA.getPath()));

            escreveDiverg = new FileWriter(divergencias, true); //Dessa forma NÃO sobrescreve
            bwDiverg = new BufferedWriter(escreveDiverg);

//            escreveHistNAODA = new FileWriter(historicoNAODA, true); //Dessa forma NÃO sobrescreve
//            bwHistNAODA = new BufferedWriter(escreveHistNAODA);
//            escreveDiverg2 = new FileWriter(divergencias2, true); //Dessa forma NÃO sobrescreve
//            bwDiverg2 = new BufferedWriter(escreveDiverg2);
            String elemento1;
            String elemento2;
            String elementoA;
            String elementoB;

            while ((Str = brDA.readLine()) != null) {

                jaExistia = false;
                cont = 0;
                BufferedReader brDA2 = new BufferedReader(new FileReader(arqDA.getPath()));

                linhaAtual1 = Str.split(";", 3);

                elemento1 = linhaAtual1[0];
                elemento2 = linhaAtual1[1];

                while ((Str = brDA2.readLine()) != null) {

                    linhaAtual2 = Str.split(";", 3);

                    elementoA = linhaAtual2[0];
                    elementoB = linhaAtual2[1];

                    if (((elemento1.equals(elementoA)) && (elemento2.equals(elementoB))) || ((elemento1.equals(elementoB)) && ((elemento2.equals(elementoA))))) {

                        cont++;
                    }

                    if (cont >= 2) {
                        jaExistia = true; //Se já existia o par de elementos em DA não deve ser considerada divergência
                        break;
                    }
                }

                brDA2.close();

                if (jaExistia == false) {//NAO_DA é atualizado aqui
                    //ATENÇÃO! Colocar aqui a busca pelo par atual dentro do arquivo NAO_DA de forma que caso ele exista não seja inserido novamente
                    //Isso está sendo feito com o método removeDup()
                    bwDiverg.write(elemento1 + ";" + elemento2 + "\n");
//                    bwDiverg2.write(elemento1 + ";" + elemento2 + ";" + ";" +   "teste\n");

                }
            }
            brDA.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DgStd1.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(DgStd1.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            bwDiverg.flush();
            bwDiverg.close();

//            bwDiverg2.flush();
//            bwDiverg2.close();
        }

        return divergencias;
    }

    /**
     *
     * @param arqHistDA
     * @throws IOException
     */
    public void contabilizaEstatDA(File arqHistDA) throws IOException {
        //arqDA contém a junção do que está em DA com o último resultado (com dados repetidos, inclusive)

        ArrayList<String> listaElementos = new ArrayList<String>();

        String Str;
        String[] linhaAtual1;
        String[] linhaAtual2;
        boolean jaExistia = false;
        int qtdAlg = 0;
        double max, min, med, soma;

//        divergencias2 = new File("./src/csv/conjuntosDS", "NAO_DA2.csv");
        estatDA = new File("./src/csv/conjuntosDS", "estatDA.csv");

        if (!estatDA.exists()) {
            System.out.println("Não existe arquivo NAO_DA.csv.");

            try {
                estatDA.createNewFile();
                new Thread().sleep(50);

            } catch (FileNotFoundException ex) {

                Logger.getLogger(DgStd1.class
                        .getName()).log(Level.SEVERE, null, ex);

            } catch (IOException ex) {
                Logger.getLogger(DgStd1.class
                        .getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(DgStd1.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        BufferedReader brHistDA = null;

        FileWriter escreveDiverg2 = null;
        BufferedWriter bwDiverg2 = null;

        try {

            brHistDA = new BufferedReader(new FileReader(arqHistDA.getPath()));

            escreveDiverg2 = new FileWriter(estatDA, true); //Dessa forma NÃO sobrescreve
            bwDiverg2 = new BufferedWriter(escreveDiverg2);

            String elemento1;
            String elemento2;
            String similaridade;
            String elementoA;
            String elementoB;

            bwDiverg2.write("elemento1" + ";" + "elemento2" + ";" + "qtdAlg" + ";" + "min" + ";" + "max" + ";" + "med" + "\n");

            while ((Str = brHistDA.readLine()) != null) {

                jaExistia = false;
                qtdAlg = 0;
                BufferedReader brHistDA2 = new BufferedReader(new FileReader(arqHistDA.getPath()));

                linhaAtual1 = Str.split(";", 4);

                elemento1 = linhaAtual1[0];
                elemento2 = linhaAtual1[1];

                min = 10.0;
                max = 0.0;
                soma = 0.0;

                //BUSCA NA LISTA DE EXISTENTES
                //SE EXISTE, continue;
                //NOVO
                String par = null;
                par = elemento1 + elemento2;

                if (listaElementos.contains(par)) {
                    System.out.println("Contém " + par);
                    jaExistia = true;
                    continue; //Dessa forma permite que pesquise por um determinado par apenas na sua primeira aparição
//                    break;
                } else {
                    listaElementos.add(par);
                    //Esse else teria que englobar o while abaixo?! (IMPORTANTE!)

                    //FIM_NOVO
                    while ((Str = brHistDA2.readLine()) != null) {

                        linhaAtual2 = Str.split(";", 3);

                        elementoA = linhaAtual2[0];
                        elementoB = linhaAtual2[1];
                        similaridade = linhaAtual2[2];

                        if (((elemento1.equals(elementoA)) && (elemento2.equals(elementoB))) || ((elemento1.equals(elementoB)) && ((elemento2.equals(elementoA))))) {

                            qtdAlg++; //Coletando as estatísticas...

                            System.out.println("qtdAlg: " + qtdAlg);

                            min = min(qtdAlg, similaridade, min);
                            max = max(qtdAlg, similaridade, max);

                            System.out.println("max: " + max);

                            soma = soma + Double.parseDouble(similaridade);

                        }
//
//                    if (cont >= 2) {
//                        jaExistia = true; //Se já existia o par de elementos em DA não deve ser considerada divergência
////                        break;
//                    }
                    }

                    med = soma / qtdAlg;

//                    bwDiverg2.write(elemento1 + ";" + elemento2 + ";" + cont + "\n");
                    bwDiverg2.write(elemento1 + ";" + elemento2 + ";" + qtdAlg + ";" + min + ";" + max + ";" + med + "\n");
                }
                brHistDA2.close();

//                if (jaExistia == false) {//NAO_DA é atualizado aqui
                //ATENÇÃO! Colocar aqui a busca pelo par atual dentro do arquivo NAO_DA de forma que caso ele exista não seja inserido novamente
                //Isso está sendo feito com o método removeDup()
//                    bwDiverg.write(elemento1 + ";" + elemento2 + "\n");
//                bwDiverg2.write(elemento1 + ";" + elemento2 + ";" + qtdAlg + ";" + Double.toString(min) + ";" + Double.toString(max) + "\n");
//                }
            }
            brHistDA.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DgStd1.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(DgStd1.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
//            bwDiverg.flush();
//            bwDiverg.close();

            bwDiverg2.flush();
            bwDiverg2.close();

        }

//        return divergencias;
    }

    /**
     *
     * @param arqHistNAODA
     * @throws IOException
     */
    public void contabilizaEstatNAODA(File arqHistNAODA) throws IOException {
        //arqDA contém a junção do que está em DA com o último resultado (com dados repetidos, inclusive)

        ArrayList<String> listaElementos = new ArrayList<String>();

        String Str;
        String[] linhaAtual1;
        String[] linhaAtual2;
        boolean jaExistia = false;
        int qtdAlg = 0;
        double max, min, med, soma;

//        divergencias2 = new File("./src/csv/conjuntosDS", "NAO_DA2.csv");
        estatNAODA = new File("./src/csv/conjuntosDS", "estatNAO_DA.csv");

        if (!estatNAODA.exists()) {
            System.out.println("Não existe arquivo NAO_DA.csv.");

            try {
                estatNAODA.createNewFile();
                new Thread().sleep(50);

            } catch (FileNotFoundException ex) {

                Logger.getLogger(DgStd1.class
                        .getName()).log(Level.SEVERE, null, ex);

            } catch (IOException ex) {
                Logger.getLogger(DgStd1.class
                        .getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(DgStd1.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        BufferedReader brHistDA = null;

        FileWriter escreveDiverg2 = null;
        BufferedWriter bwDiverg2 = null;

        try {

            brHistDA = new BufferedReader(new FileReader(arqHistNAODA.getPath()));

            escreveDiverg2 = new FileWriter(estatNAODA, true); //Dessa forma NÃO sobrescreve
            bwDiverg2 = new BufferedWriter(escreveDiverg2);

            String elemento1;
            String elemento2;
            String similaridade;
            String elementoA;
            String elementoB;

            bwDiverg2.write("elemento1" + ";" + "elemento2" + ";" + "qtdAlg" + ";" + "min" + ";" + "max" + ";" + "med" + "\n");

            while ((Str = brHistDA.readLine()) != null) {

                jaExistia = false;
                qtdAlg = 0;
                min = 10.0;
                max = 0.0;
                soma = 0.0;

                BufferedReader brHistDA2 = new BufferedReader(new FileReader(arqHistNAODA.getPath()));

                linhaAtual1 = Str.split(";", 3);

                elemento1 = linhaAtual1[0];
                elemento2 = linhaAtual1[1];

                //BUSCA NA LISTA DE EXISTENTES
                //SE EXISTE, continue;
                //NOVO
                String par = null;
                par = elemento1 + elemento2;

                if (listaElementos.contains(par)) {
                    System.out.println("Contém " + par);
                    jaExistia = true;
                    continue; //Dessa forma permite que pesquise por um determinado par apenas na sua primeira aparição
//                    break;
                } else {
                    listaElementos.add(par);
                    //Esse else teria que englobar o while abaixo?! (IMPORTANTE!)

                    //FIM_NOVO
                    while ((Str = brHistDA2.readLine()) != null) {

                        linhaAtual2 = Str.split(";", 4);

                        elementoA = linhaAtual2[0];
                        elementoB = linhaAtual2[1];
                        similaridade = linhaAtual2[2];

                        if (((elemento1.equals(elementoA)) && (elemento2.equals(elementoB))) || ((elemento1.equals(elementoB)) && ((elemento2.equals(elementoA))))) {

                            qtdAlg++; //Coletando as estatísticas...
                            System.out.println("qtdAlg: " + qtdAlg);

                            min = min(qtdAlg, similaridade, min);
                            max = max(qtdAlg, similaridade, max);

                            System.out.println("max: " + max);

                            soma = soma + Double.parseDouble(similaridade);

                        }
//
//                    if (cont >= 2) {
//                        jaExistia = true; //Se já existia o par de elementos em DA não deve ser considerada divergência
////                        break;
//                    }
                    }

//                    bwDiverg2.write(elemento1 + ";" + elemento2 + ";" + cont + "\n");
                    med = soma / qtdAlg;

//                    bwDiverg2.write(elemento1 + ";" + elemento2 + ";" + cont + "\n");
                    bwDiverg2.write(elemento1 + ";" + elemento2 + ";" + qtdAlg + ";" + min + ";" + max + ";" + med + "\n");
                }
                brHistDA2.close();

//                if (jaExistia == false) {//NAO_DA é atualizado aqui
                //ATENÇÃO! Colocar aqui a busca pelo par atual dentro do arquivo NAO_DA de forma que caso ele exista não seja inserido novamente
                //Isso está sendo feito com o método removeDup()
//                    bwDiverg.write(elemento1 + ";" + elemento2 + "\n");
//                bwDiverg2.write(elemento1 + ";" + elemento2 + ";" + qtdAlg + "\n");
//                med = soma / qtdAlg;
//
////                    bwDiverg2.write(elemento1 + ";" + elemento2 + ";" + cont + "\n");
//                bwDiverg2.write(elemento1 + ";" + elemento2 + ";" + qtdAlg + ";" + min + ";" + max + ";" + med + "\n");
//                }
            }
            brHistDA.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DgStd1.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(DgStd1.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
//            bwDiverg.flush();
//            bwDiverg.close();

            bwDiverg2.flush();
            bwDiverg2.close();

        }

//        return divergencias;
    }

//Remove a duplicidade dos registros no conjunto de pares divergentes acumulados
    /**
     *
     * @param divergencias
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void remDupDiverg(File divergencias) throws IOException {

        String line = "";

//        File divergencias = new File("./src/csv/conjuntosDS", "NAO_DA.csv");
        BufferedReader brDiverg = null;
        Collection lista = null;

        try {
            brDiverg = new BufferedReader(new FileReader(divergencias));

            lista = new ArrayList();

            while ((line = brDiverg.readLine()) != null) {
                lista.add(line);

            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DgStd1.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(DgStd1.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            brDiverg.close();
        }

        //Tratamento da lista sem repetições
        BufferedWriter bwDiverg = null;
        Collection lista2 = new LinkedHashSet(lista); //Aqui é removida a duplicidade

        try {//        File arquivo = new File("./src/csv/conjuntosDS", "NAO_DA2.csv");
            bwDiverg = new BufferedWriter(new FileWriter(divergencias));
            for (Object item : lista2) {
                bwDiverg.write((String) item);
                bwDiverg.newLine();
            }
            bwDiverg.flush();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DgStd1.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(DgStd1.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            bwDiverg.flush();
            bwDiverg.close();
        }

    }

    private void atualizaDM_NDM() throws IOException {
        tp = 0;
        fp = 0;

        String Str = "";
        String elemento1 = "";
        String elemento2 = "";
        String[] linhaAtual;
        boolean existeGS = false;
        boolean existeDM_NDM = false;

        FileWriter escreveDM = null;
        FileWriter escreveNDM = null;
        BufferedReader brDiverg = null;
        BufferedWriter bwDM = null;
        BufferedWriter brNDM = null;

        try {
            brDiverg = new BufferedReader(new FileReader(divergencias.getPath()));

            escreveDM = new FileWriter(DM, true); //Sem sobrescrever
            bwDM = new BufferedWriter(escreveDM);

            escreveNDM = new FileWriter(NDM, true); //Sem sobrescrever
            brNDM = new BufferedWriter(escreveNDM);

            while ((Str = brDiverg.readLine()) != null) {

                linhaAtual = Str.split(";", 3);

                elemento1 = linhaAtual[0];
                elemento2 = linhaAtual[1];

                existeDM_NDM = buscaDM_NDM(elemento1, elemento2);

                //Só entra se já não existir em DM ou NDM
                if (!existeDM_NDM) {

                    existeGS = buscaGabarito(elemento1, elemento2, gs);

                    //Aqui são simuladas as inspeções manuais
                    //Se a divergência existe no gabarito, então é adicionada a DM
                    if (existeGS) {
                        bwDM.write(elemento1 + ";" + elemento2 + "\n");
                    } else {
                        brNDM.write(elemento1 + ";" + elemento2 + "\n");

                    }

                }

            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DgStd1.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(DgStd1.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            brDiverg.close();

            bwDM.flush();
            bwDM.close();

            brNDM.flush();
            brNDM.close();
        }
    }

    public void incrementaEstatNAO_DA() throws IOException {
        tp = 0;
        fp = 0;

        String Str = "";
        String elemento1 = "";
        String elemento2 = "";
        String[] linhaAtual;
        boolean existeGS = false;
        boolean existe = false;

        FileWriter escreveEstatNaoDAIncr = null;
        FileWriter escreveNDM = null;
        BufferedReader brEstatNaoDA = null;
        BufferedWriter bwEstatNaoDAIncr = null;
        BufferedWriter brNDM = null;

        estatNAODAIncr = new File("./src/csv/conjuntosDS", "estatNAODAIncr.csv");

        if (!estatNAODAIncr.exists()) {
            System.out.println("Não existe arquivo estatNAODAIncr.csv.");

            try {
                estatNAODAIncr.createNewFile();

                try {
                    brEstatNaoDA = new BufferedReader(new FileReader(estatNAODA.getPath()));

                    escreveEstatNaoDAIncr = new FileWriter(estatNAODAIncr, true); //Sem sobrescrever

                    escreveEstatNaoDAIncr.write("elemento1" + ";" + "elemento2" + ";" + "qtdAlg" + ";" + "min" + ";" + "max" + ";" + "med" + ";" + "duplicata" + "\n");

                    bwEstatNaoDAIncr = new BufferedWriter(escreveEstatNaoDAIncr);

//                    escreveNDM = new FileWriter(NDM, true); //Sem sobrescrever
//                    brNDM = new BufferedWriter(escreveNDM);
                    while ((Str = brEstatNaoDA.readLine()) != null) {

                        if (Str.contains("elemento1")) {
                            continue;
                        }

                        linhaAtual = Str.split(";", 6);

                        elemento1 = linhaAtual[0];
                        elemento2 = linhaAtual[1];

                        existe = buscaGabarito(elemento1, elemento2, gs);

                        //Só entra se já não existir em DM ou NDM
                        if (existe) {
//                            bwEstatNaoDAIncr.write(linhaAtual[0] + ";" + linhaAtual[1] + ";" + linhaAtual[2] + ";" + linhaAtual[3] + ";" + linhaAtual[4] + ";" + linhaAtual[5] + true + "\n");
                            bwEstatNaoDAIncr.write(Str + ";" + "1.0" + "\n");
                        } else {
//                            bwEstatNaoDAIncr.write(linhaAtual[0] + ";" + linhaAtual[1] + ";" + linhaAtual[2] + ";" + linhaAtual[3] + ";" + linhaAtual[4] + ";" + linhaAtual[5] + false + "\n");
                            bwEstatNaoDAIncr.write(Str + ";" + "0.0" + "\n");

                        }

                    }

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(DgStd1.class
                            .getName()).log(Level.SEVERE, null, ex);

                } catch (IOException ex) {
                    Logger.getLogger(DgStd1.class
                            .getName()).log(Level.SEVERE, null, ex);
                } finally {
                    brEstatNaoDA.close();

                    bwEstatNaoDAIncr.flush();
                    bwEstatNaoDAIncr.close();

                }

            } catch (IOException ex) {
                System.out.println("Não foi possível criar arquivo estatisticas.csv.");
            }
        }
    }

    /**
     *
     * @param tp
     * @param fp
     * @param gs
     * @param arqResult
     */
    public void gravaEstatisticas(int tp, int fp, File gs, File arqResult) {

        try {

            double precision = getPrecision(tp, fp);
            int fn = getFN(arqResult);
            int tn = getTN(tp, fp, fn);
            double recall = getRecall(tp, fn);
            double f1 = getF1(precision, recall);
            int inspecoes = getInspManuais();
            int tamDA = getTamDA();
            int tamDM = getTamDM();
            int tamNDM = getTamNDM();

            BufferedWriter bwEstat = null;

            try {
                escreveEstat = new FileWriter(estatisticas, true);
                bwEstat = new BufferedWriter(escreveEstat);

                bwEstat.append("DS");
                bwEstat.append(";");
                bwEstat.append("1 - acm diverg");
                bwEstat.append(";");
                bwEstat.append(Integer.toString(getQtdAlg()));
                bwEstat.append(";");
                bwEstat.append(Integer.toString(permutacao));
                bwEstat.append(";");
                bwEstat.append(Integer.toString(iteracao));
                bwEstat.append(";");
                bwEstat.append(Integer.toString(inspecoes));
                bwEstat.append(";");
                bwEstat.append(Double.toString(precision));
                bwEstat.append(";");
                bwEstat.append(Double.toString(recall));
                bwEstat.append(";");
                bwEstat.append(Double.toString(f1));
                bwEstat.append(";");
                bwEstat.append(Integer.toString(tamDA));
                bwEstat.append(";");
                bwEstat.append(Integer.toString(tamDM));
                bwEstat.append(";");
                bwEstat.append(Integer.toString(tamNDM));
                bwEstat.append(";");
                bwEstat.append(Integer.toString(tp));
                bwEstat.append(";");
                bwEstat.append(Integer.toString(fp));
                bwEstat.append(";");
                bwEstat.append(Integer.toString(tn));
                bwEstat.append(";");
                bwEstat.append(Integer.toString(fn));
                bwEstat.append("\n");

            } catch (FileNotFoundException ex) {
                System.out.println("Não foi possível encontrar o arquivo " + estatisticas.getName() + " em gravaEstatisticas()");
            } finally {
                bwEstat.flush();
                bwEstat.close();

                iteracao++;
                tp = 0;
                fp = 0;
                this.fn = 0;
                this.tn = 0;
            }

        } catch (FileNotFoundException ex) {
            System.out.println("Não foi possível encontrar o arquivo " + estatisticas.getName());

        } catch (IOException ex) {
            Logger.getLogger(DgStd1.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param permutacao
     */
    public void setPermutacao(int permutacao) {
        this.permutacao = permutacao;
    }

    /**
     *
     * @param tp
     * @param fp
     * @return
     */
    public double getPrecision(int tp, int fp) {
        return (double) tp / (tp + fp);
    }

    /**
     *
     * @param tp
     * @param fn
     * @return
     */
    public double getRecall(int tp, int fn) {

        return (double) tp / (tp + fn);
    }

    /**
     *
     * @param precision
     * @param recall
     * @return
     */
    public double getF1(double precision, double recall) {
        return 2 * recall * precision / (recall + precision);
    }

    /**
     *
     */
    public void fechaExecucao() {
//        TODO
    }

    /**
     *
     * @return
     */
    public File getHistoricoDA() {
        return historicoDA;
    }

    /**
     *
     * @return
     */
    public File getHistoricoNAODA() {
        return historicoNAODA;
    }

    public File getEstatDA() {
        return estatDA;
    }

    public File getEstatNAODA() {
        return estatNAODA;
    }

    /**
     *
     * @return
     */
    public int getIteracao() {
        return iteracao;
    }

    /**
     *
     * @param gs
     */
    public void setGs(File gs) {
        this.gs = gs;
    }

    /**
     *
     * @return
     */
    public int getTamBaseOrig() {
        return tamBaseOrig;
    }

    /**
     *
     * @param tamBaseOrig
     */
    public void setTamBaseOrig(int tamBaseOrig) {
        this.tamBaseOrig = tamBaseOrig;
    }

    /**
     *
     * @return
     */
    public int getTamBaseOrig2() {
        return tamBaseOrig2;
    }

    /**
     *
     * @param tamBaseOrig2
     */
    public void setTamBaseOrig2(int tamBaseOrig2) {
        this.tamBaseOrig2 = tamBaseOrig2;
    }

    /**
     *
     * @return
     */
    public boolean isDedup() {
        return dedup;
    }

    /**
     *
     * @param dedup
     */
    public void setDedup(boolean dedup) {
        this.dedup = dedup;
    }

    /**
     *
     * @return @throws IOException
     */
    public int getTamDA() throws IOException {

        int tamDA = 0;

        LineNumberReader linhaLeitura1 = null;

        try {
            linhaLeitura1 = new LineNumberReader(new FileReader(DA.getPath()));
            linhaLeitura1.skip(DA.length());
            tamDA = linhaLeitura1.getLineNumber();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DgStd1.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(DgStd1.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            linhaLeitura1.close();
        }

        return tamDA;

    }

    /**
     *
     * @return @throws IOException
     */
    public int getTamDM() throws IOException {
        int tamDM = 0;
        LineNumberReader linhaLeitura1 = null;
        try {
            linhaLeitura1 = new LineNumberReader(new FileReader(DM.getPath()));
            linhaLeitura1.skip(DM.length());
            tamDM = linhaLeitura1.getLineNumber();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DgStd1.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(DgStd1.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            linhaLeitura1.close();
        }

        return tamDM;

    }

    /**
     *
     * @return @throws IOException
     */
    public int getTamNDM() throws IOException {
        int tamNDM = 0;
        LineNumberReader linhaLeitura1 = null;
        try {
            linhaLeitura1 = new LineNumberReader(new FileReader(NDM.getPath()));
            linhaLeitura1.skip(NDM.length());
            tamNDM = linhaLeitura1.getLineNumber();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DgStd1.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(DgStd1.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            linhaLeitura1.close();
        }

        return tamNDM;

    }

    /**
     *
     * @param tp
     * @param fp
     * @param fn
     * @return
     * @throws IOException
     */
    public int getTN(int tp, int fp, int fn) throws IOException {

        int tamBase = getTamBaseOrig();

        if (isDedup()) {

            tn = (((tamBase--) * (tamBase))) / 2 - tp - fp - fn;

            return tn;

        } else {

            int tamBase2 = getTamBaseOrig2();

            tn = (tamBase * tamBase2) - tp - fp - fn;

            return tn;
        }

    }

    /**
     *
     * @return @throws IOException
     */
    public int getInspManuais() throws IOException {

        return getTamNDM() + getTamDM();
    }

    /**
     *
     * @return
     */
    public int getQtdAlg() {
        return qtdAlg;
    }

    /**
     *
     * @param qtdAlg
     */
    public void setQtdAlg(int qtdAlg) {
        this.qtdAlg = qtdAlg;
    }

    /**
     *
     * @param geraEst
     */
    public void setGeraEst(boolean geraEst) {
        this.geraEst = geraEst;
    }

    /**
     *
     * @param arqResult
     * @return
     * @throws IOException
     */
    public int getFN(File arqResult) throws IOException {
        //Já deve receber o arqResult padronizado
        //Adicionar um teste para saber se está padronizado ou não, para poder tratar o arquivo

        tp = 0;
        fp = 0;

        String Str;
        String elementoGS1;
        String elementoGS2;
        String[] linhaAtual;
        boolean existe = false;

        BufferedReader brGS = null;
        try {
            brGS = new BufferedReader(new FileReader(gs.getPath()));

            while ((Str = brGS.readLine()) != null) {

                linhaAtual = Str.split(";", 3);

                elementoGS1 = linhaAtual[0];
                elementoGS2 = linhaAtual[1];

                //Passando o arqResult para procurar os elementos do gabarito nele.
                //O que estiver no gabarito e não estiver em arqResult é, portanto, um falso negativo
                existe = buscaFN(elementoGS1, elementoGS2, arqResult);

                //Só entra se a variável existe for falsa
                if (!existe) {
                    fn++;
                }

            }
            fn--; //Reduzindo um do título do gabarito

        } catch (FileNotFoundException ex) {
            System.out.println("Não foi possível encontrar o arquivo " + gs.getName() + " em getFN()");

        } catch (IOException ex) {
            Logger.getLogger(DgStd1.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            brGS.close();
        }

        return fn;
    }

    private boolean buscaFN(String elementoGS1, String elementoGS2, File arqResult) throws IOException {
        String Str;
        String elemento1;
        String elemento2;
        String[] linhaAtual;
        boolean existe = false;

        BufferedReader brArqResult = null;
        try {
            brArqResult = new BufferedReader(new FileReader(arqResult.getPath()));

            while ((Str = brArqResult.readLine()) != null) {

                linhaAtual = Str.split(";", 3);

                elemento1 = linhaAtual[0];
                elemento2 = linhaAtual[1];

                //Para admitir o fecho transitivo:
                if (((elemento1.equals(elementoGS1)) && (elemento2.equals(elementoGS2))) || ((elemento1.equals(elementoGS2)) && ((elemento2.equals(elementoGS1))))) {

                    existe = true;
                    break;
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Não foi possível encontrar o arquivo " + gs.getName() + " em buscaFN()");

        } catch (IOException ex) {
            Logger.getLogger(DgStd1.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            brArqResult.close();
        }

        return existe;
    }

    private boolean buscaDM_NDM(String elemento1, String elemento2) throws IOException {
        String Str;
        String elementoDM_NDM1;
        String elementoDM_NDM2;
        String[] linhaAtual;
        boolean existe = false;

        BufferedReader brDM = null;
        try {
            brDM = new BufferedReader(new FileReader(DM.getPath()));

            while ((Str = brDM.readLine()) != null) {

                linhaAtual = Str.split(";", 3);

                elementoDM_NDM1 = linhaAtual[0];
                elementoDM_NDM2 = linhaAtual[1];

                if (((elemento1.equals(elementoDM_NDM1)) && (elemento2.equals(elementoDM_NDM2))) || ((elemento1.equals(elementoDM_NDM2)) && ((elemento2.equals(elementoDM_NDM1))))) {

                    return existe = true;

                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Não foi possível encontrar o arquivo " + gs.getName() + " em buscaGabarito()");

        } catch (IOException ex) {
            Logger.getLogger(DgStd1.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            brDM.close();

        }

        BufferedReader brNDM = null;
        try {
            brNDM = new BufferedReader(new FileReader(NDM.getPath()));

            while ((Str = brNDM.readLine()) != null) {

                linhaAtual = Str.split(";", 3);

                elementoDM_NDM1 = linhaAtual[0];
                elementoDM_NDM2 = linhaAtual[1];

                if (((elemento1.equals(elementoDM_NDM1)) && (elemento2.equals(elementoDM_NDM2))) || ((elemento1.equals(elementoDM_NDM2)) && ((elemento2.equals(elementoDM_NDM1))))) {

                    return existe = true;

                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Não foi possível encontrar o arquivo " + gs.getName() + " em buscaGabarito()");

        } catch (IOException ex) {
            Logger.getLogger(DgStd1.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            brNDM.close();
        }

        return existe;
    }

    /**
     *
     * @param iteracao
     */
    public void setIteracao(int iteracao) {
        this.iteracao = iteracao;
    }

    /**
     *
     */
    public void limpaTudo() {

        File dir = new File("./src/csv/conjuntosDS");

        if (dir.isDirectory()) {
            File[] sun = dir.listFiles();
            for (File toDelete : sun) {
                if (!toDelete.isDirectory()) {
                    toDelete.delete();
                }
            }
        }

//        System.out.println("limpaTudo DS"); 
        tp = 0;
        fp = 0;
        tn = 0;
        fn = 0;
        iteracao = 0;

    }

    /**
     *
     * @throws IOException
     */
    public void copiaArqDiverg() throws IOException {

        File divergToAA = new File("./src/csv/conjuntosDS/conjuntosDiverg/", "diverg(" + getQtdAlg() + ")" + permutacao + ".csv");
//        File divergToAA = new File("./src/csv/conjuntosDS/conjuntosDiverg-DEMO/", "diverg(" + getQtdAlg() + ")" + permutacao + ".csv");

        if (divergToAA.exists()) {
            divergToAA.delete();
        }
        FileChannel sourceChannel = null;
        FileChannel destinationChannel = null;
        try {
            sourceChannel = new FileInputStream(divergencias).getChannel();
            destinationChannel = new FileOutputStream(divergToAA).getChannel();
            sourceChannel.transferTo(0, sourceChannel.size(),
                    destinationChannel);
        } finally {
            if (sourceChannel != null && sourceChannel.isOpen()) {
                sourceChannel.close();
            }
            if (destinationChannel != null && destinationChannel.isOpen()) {
                destinationChannel.close();
            }
        }
    }

    /**
     *
     * @param qtdAlg
     * @param sim
     * @param atualMin
     * @return
     */
    public double min(int qtdAlg, String sim, double atualMin) {
//        double minimo = 1.0;
        double minimo = 1;
        double similaridade = Double.parseDouble(sim);

        if (qtdAlg == 1) {
//            if (similaridade <= 1.0) {
            if (similaridade <= 10) {
                minimo = similaridade;
            } else {
                minimo = atualMin;
            }
        } else {
            if (similaridade <= atualMin) {

                minimo = similaridade;
            } else {
                minimo = atualMin;
            }
        }
        return minimo;

    }

    /**
     *
     * @param qtdAlg
     * @param sim
     * @param atualMax
     * @return
     */
    public double max(int qtdAlg, String sim, double atualMax) {
//        double minimo = 1.0;
        double maximo = 0;
        double similaridade = Double.parseDouble(sim);

        if (qtdAlg == 1) {
//            if (similaridade <= 1.0) {
            if (similaridade >= 0.0) {
                maximo = similaridade;
            }
//            else {
//                maximo = atualMax;
//            }
        } else {
            if (similaridade >= atualMax) {
                maximo = similaridade;
                System.out.println("Entrei aqui: maximo = similaridade; com qtdAlg igual a " + qtdAlg);
                System.out.println("similaridade igual a " + similaridade);
                System.out.println("maximo igual a " + maximo);
            } else {
                maximo = atualMax;
            }
        }
        return maximo;

    }
}
