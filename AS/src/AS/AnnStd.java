/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AS;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

/**
 *
 * @author Diego
 */
public class AnnStd {

    int tp, fp, tn, fn, iteracao, permutacao, tamBaseOrig;

    File estatisticas;
    File DA;
    File divergencias;
    File DM;
    File NDM;
    File gs;
    File DADM;
    FileWriter escreveEstat;

    public AnnStd() {
        tp = 0;
        fp = 0;
        iteracao = 0;

        estatisticas = new File("./src/csv/", "estatisticas.csv");

        if (!estatisticas.exists()) {
            System.out.println("Não existe arquivo estatisticas.csv.");

            try {
                estatisticas.createNewFile();
                BufferedWriter bwEstat = null;
                try {
                    escreveEstat = new FileWriter(estatisticas, true); //O parâmetro true faz com que as informações não sejam sobreescritas
                    bwEstat = new BufferedWriter(escreveEstat);

                    bwEstat.write("permutacao;iteracao;inspecoesManuais;precision;recall;f-measure;da;dm;ndm;tp;fp;tn;fn\n");

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

                linhaAtual = Str.split(";", 2); //Nesse caso considera apenas as duas primeiras colunas (as que interessam)
                cont = 0;

                for (String cell : linhaAtual) {

                    cont++;

                    index_c1 = cell.indexOf('[');
                    index_c2 = cell.indexOf(']');

                    Str2 = cell.substring(index_c1 + 1, index_c2);

                    escreveArqPadr.append(Str2);

                    if (cont == 1) {
                        escreveArqPadr.append(';');
                    } else {
                        escreveArqPadr.append('\n');
                    }

                }
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

    public void comparaComGS(File arqResult) throws IOException {
        //Já deve receber o arqResult padronizado
        //Adicionar um teste para saber se está padronizado ou não, para poder tratar o arquivo

        tp = 0;
        fp = 0;

        String Str;
        String elemento1;
        String elemento2;
        String[] linhaAtual;
        boolean existe = false;

        BufferedReader brResult = null;
        try {
            brResult = new BufferedReader(new FileReader(arqResult.getPath()));

            while ((Str = brResult.readLine()) != null) {

                linhaAtual = Str.split(";", 2);

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
            Logger.getLogger(AnnStd.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            brResult.close();
            gravaEstatisticas(tp, fp, gs, arqResult);
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

                linhaAtual = Str.split(";", 2);

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
            Logger.getLogger(AnnStd.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            brGS.close();
        }

        return existe;
    }

    public void comparaConjuntos(File arqResult) throws IOException {

        String Str;
        String[] linhaAtual;
        boolean baseline = false;
        DA = new File("./src/csv/conjuntosAS", "DA.csv");
        DADM = new File("./src/csv/conjuntosAS", "DADM.csv");
        DM = new File("./src/csv/conjuntosAS", "DM.csv");
        NDM = new File("./src/csv/conjuntosAS", "NDM.csv");
        FileWriter escreveDupAuto;

        File aux;

        if (!arqResult.exists()) {
            System.out.println("Arquivo " + arqResult.getName() + " não existe!");
            System.exit(0);
        }

        if (!DA.exists()) {
            BufferedReader brArqResult = null;
            BufferedWriter bwDupAuto = null;
            try {
                DA.createNewFile();
                new Thread().sleep(50);

                brArqResult = new BufferedReader(new FileReader(arqResult.getPath()));

                escreveDupAuto = new FileWriter(DA);
                bwDupAuto = new BufferedWriter(escreveDupAuto);

                //Copiando do primeiro arquivo
                while ((Str = brArqResult.readLine()) != null) {

                    linhaAtual = Str.split(";", 2);
                    bwDupAuto.write(linhaAtual[0] + ";" + linhaAtual[1] + "\n");

                }

                //DM e NDM são criados obrigatoriamente quando DA é criado
                if (!DM.exists()) {
                    System.out.println("Não existe arquivo " + DM.getName());

                    try {
                        DM.createNewFile();
                        new Thread().sleep(50);

                    } catch (FileNotFoundException ex) {

                        Logger.getLogger(AnnStd.class
                                .getName()).log(Level.SEVERE, null, ex);

                    } catch (IOException ex) {
                        Logger.getLogger(AnnStd.class
                                .getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(AnnStd.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

                if (!NDM.exists()) {
                    System.out.println("Não existe arquivo " + NDM.getName());

                    try {
                        NDM.createNewFile();
                        new Thread().sleep(50);

                    } catch (FileNotFoundException ex) {

                        Logger.getLogger(AnnStd.class
                                .getName()).log(Level.SEVERE, null, ex);

                    } catch (IOException ex) {
                        Logger.getLogger(AnnStd.class
                                .getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(AnnStd.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

                //Arquivo para cálculo de precision, recall e f-measure do Annealing todo
                if (!DADM.exists()) {
                    System.out.println("Não existe arquivo " + DADM.getName());

                    try {
                        DADM.createNewFile();
                        new Thread().sleep(50);

                    } catch (FileNotFoundException ex) {

                        Logger.getLogger(AnnStd.class
                                .getName()).log(Level.SEVERE, null, ex);

                    } catch (IOException ex) {
                        Logger.getLogger(AnnStd.class
                                .getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(AnnStd.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

                baseline = true;

            } catch (IOException ex) {
                System.out.println("Não foi possível criar arquivo " + DA.getName());
            } catch (InterruptedException ex) {
                Logger.getLogger(AnnStd.class.getName()).log(Level.SEVERE, null, ex);
            } finally {

                brArqResult.close();
                bwDupAuto.flush(); //Alterei DA aqui!!!!!
                bwDupAuto.close();

                //Juntar DA + DM para calcular precision, recall e f-measure para o TODO, O ANNEALING!
                //Como é o baseline poderia ser calculado apenas com DA mesmo
                juntaDADM(DA, DM);

                comparaComGS(DADM);

            }

        }

        if (baseline == false) {

            aux = juntaArquivos(DA, arqResult); //Isso é feito para que se possa ver a união e intersecção do resultado atual com o DA

            atualizaDA(aux); //DA deve ficar apenas com a intersecção

            filtraDivergencias(aux); //NAO_DA deve ficar apenas com aquilo que não for intersecção com DA

            atualizaDM_NDM(); //Separação daquilo que não é DA em D_M e ND_M. 

            juntaDADM(DA, DM);

            comparaComGS(DADM);
        }
    }

    public File juntaArquivos(File arqDA, File arq2) throws IOException {

        File juncao = new File("./src/csv/conjuntosAS", "juncao.csv");
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
                Logger.getLogger(AnnStd.class.getName()).log(Level.SEVERE, null, ex);
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

                linhaAtual = Str.split(";", 2);
                bwJuncao.write(linhaAtual[0] + ";" + linhaAtual[1] + "\n");

            }
            //Copiando do segundo arquivo
            while ((Str = brArq2.readLine()) != null) {

                linhaAtual = Str.split(";", 2);
                bwJuncao.write(linhaAtual[0] + ";" + linhaAtual[1] + "\n");

            }

        } catch (FileNotFoundException ex) {

            Logger.getLogger(AnnStd.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AnnStd.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            brDA.close();
            brArq2.close();

            bwJuncao.flush();
            bwJuncao.close();

        }

        return juncao;
    }

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

                linhaAtual = Str.split(";", 2);
                bwJuncao.write(linhaAtual[0] + ";" + linhaAtual[1] + "\n");

            }
            //Copiando do segundo arquivo
            while ((Str = brDM.readLine()) != null) {

                linhaAtual = Str.split(";", 2);
                bwJuncao.write(linhaAtual[0] + ";" + linhaAtual[1] + "\n");

            }

        } catch (FileNotFoundException ex) {

            Logger.getLogger(AnnStd.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AnnStd.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            brDA.close();
            brDM.close();

            bwJuncao.flush();
            bwJuncao.close();
        }

//        return DADM;
    }

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

                linhaAtual1 = Str.split(";", 2);

                elemento1 = linhaAtual1[0];
                elemento2 = linhaAtual1[1];

                while ((Str2 = brArqDup2.readLine()) != null) {

                    linhaAtual2 = Str2.split(";", 2);

                    elementoA = linhaAtual2[0];
                    elementoB = linhaAtual2[1];

                    if (((elemento1.equals(elementoA)) && (elemento2.equals(elementoB))) || ((elemento1.equals(elementoB)) && ((elemento2.equals(elementoA))))) {
                        cont++;
                    }

                    if (cont >= 2) {

                        for (int x = 0; x < dupEncontradas.size(); x++) {
                            if (dupEncontradas.get(x).equals(elemento1 + ";" + elemento2)) {
                                existe = true; //Só é verdadeiro se a duplicada já tiver sido encontrada antes
                                break;
                            }

                        }

                        if (existe == false) { //Se não existe ocorrência anterior, adiciona a DA e à lista de duplicadas encontradas
                            bwDA.write(elemento1 + ";" + elemento2 + "\n");
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
            Logger.getLogger(AnnStd.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            bwDA.flush();
            bwDA.close();
        }

        return DA;
    }

    //Para gerar DN e NM       
    public File filtraDivergencias(File arqDA) throws IOException {

        String Str;
        String[] linhaAtual1;
        String[] linhaAtual2;
        boolean jaExistia = false;
        int cont = 0;

        divergencias = new File("./src/csv/conjuntosAS", "NAO_DA.csv");

        if (!divergencias.exists()) {
            System.out.println("Não existe arquivo NAO_DA.csv.");

            try {
                divergencias.createNewFile();
                new Thread().sleep(50);

            } catch (FileNotFoundException ex) {

                Logger.getLogger(AnnStd.class
                        .getName()).log(Level.SEVERE, null, ex);

            } catch (IOException ex) {
                Logger.getLogger(AnnStd.class
                        .getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(AnnStd.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        BufferedReader brDA = null;
        FileWriter escreveDiverg = null;
        BufferedWriter bwDiverg = null;

        try {

            brDA = new BufferedReader(new FileReader(arqDA.getPath()));

            escreveDiverg = new FileWriter(divergencias); //Dessa forma sobrescreve
            bwDiverg = new BufferedWriter(escreveDiverg);

            String elemento1;
            String elemento2;
            String elementoA;
            String elementoB;

            while ((Str = brDA.readLine()) != null) {

                jaExistia = false;
                cont = 0;
                BufferedReader brDA2 = new BufferedReader(new FileReader(arqDA.getPath()));

                linhaAtual1 = Str.split(";", 2);

                elemento1 = linhaAtual1[0];
                elemento2 = linhaAtual1[1];

                while ((Str = brDA2.readLine()) != null) {

                    linhaAtual2 = Str.split(";", 2);

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

                if (jaExistia == false) {
                    bwDiverg.write(elemento1 + ";" + elemento2 + "\n");
                }
            }
            brDA.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            bwDiverg.flush();
            bwDiverg.close();

        }

        return divergencias;
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

                linhaAtual = Str.split(";", 2);

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
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            brDiverg.close();

            bwDM.flush();
            bwDM.close();

            brNDM.flush();
            brNDM.close();
        }
    }

    public void gravaEstatisticas(int tp, int fp, File gs, File arqResult) {

        try {

            double precision = getPrecision(tp, fp);
            int fn = getFN(arqResult);
            int tn = getTN(tp, fp, fn, tamBaseOrig);
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
            Logger.getLogger(AnnStd.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setPermutacao(int permutacao) {
        this.permutacao = permutacao;
    }

    public double getPrecision(int tp, int fp) {
        return (double) tp / (tp + fp);
    }

    public double getRecall(int tp, int fn) {

        return (double) tp / (tp + fn);
    }

    public double getF1(double precision, double recall) {
        return 2 * recall * precision / (recall + precision);
    }

    public void fechaExecucao() {
//        TODO
    }

    public int getIteracao() {
        return iteracao;
    }

    public void setGs(File gs) {
        this.gs = gs;
    }

    public int getTamDA() throws IOException {

        int tamDA = 0;

        LineNumberReader linhaLeitura1 = null;

        try {
            linhaLeitura1 = new LineNumberReader(new FileReader(DA.getPath()));
            linhaLeitura1.skip(DA.length());
            tamDA = linhaLeitura1.getLineNumber();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(AnnStd.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AnnStd.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            linhaLeitura1.close();
        }

        return tamDA;

    }

    public int getTamDM() throws IOException {
        int tamDM = 0;
        LineNumberReader linhaLeitura1 = null;
        try {
            linhaLeitura1 = new LineNumberReader(new FileReader(DM.getPath()));
            linhaLeitura1.skip(DM.length());
            tamDM = linhaLeitura1.getLineNumber();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(AnnStd.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AnnStd.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            linhaLeitura1.close();
        }

        return tamDM;

    }

    public int getTamNDM() throws IOException {
        int tamNDM = 0;
        LineNumberReader linhaLeitura1 = null;
        try {
            linhaLeitura1 = new LineNumberReader(new FileReader(NDM.getPath()));
            linhaLeitura1.skip(NDM.length());
            tamNDM = linhaLeitura1.getLineNumber();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AnnStd.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AnnStd.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            linhaLeitura1.close();
        }

        return tamNDM;

    }

    public int getTN(int tp, int fp, int fn, int tamBase) throws IOException {

        tn = (((tamBase--) * (tamBase))) / 2 - tp - fp - fn;

        return tn;

    }

    public int getInspManuais() throws IOException {

        return getTamNDM() + getTamDM();
    }

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

                linhaAtual = Str.split(";", 2);

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
            Logger.getLogger(AnnStd.class.getName()).log(Level.SEVERE, null, ex);
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

                linhaAtual = Str.split(";", 2);

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
            Logger.getLogger(AnnStd.class.getName()).log(Level.SEVERE, null, ex);
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

                linhaAtual = Str.split(";", 2);

                elementoDM_NDM1 = linhaAtual[0];
                elementoDM_NDM2 = linhaAtual[1];

                if (((elemento1.equals(elementoDM_NDM1)) && (elemento2.equals(elementoDM_NDM2))) || ((elemento1.equals(elementoDM_NDM2)) && ((elemento2.equals(elementoDM_NDM1))))) {

                    return existe = true;

                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Não foi possível encontrar o arquivo " + gs.getName() + " em buscaGabarito()");
        } catch (IOException ex) {
            Logger.getLogger(AnnStd.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            brDM.close();

        }

        BufferedReader brNDM = null;
        try {
            brNDM = new BufferedReader(new FileReader(NDM.getPath()));

            while ((Str = brNDM.readLine()) != null) {

                linhaAtual = Str.split(";", 2);

                elementoDM_NDM1 = linhaAtual[0];
                elementoDM_NDM2 = linhaAtual[1];

                if (((elemento1.equals(elementoDM_NDM1)) && (elemento2.equals(elementoDM_NDM2))) || ((elemento1.equals(elementoDM_NDM2)) && ((elemento2.equals(elementoDM_NDM1))))) {

                    return existe = true;

                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Não foi possível encontrar o arquivo " + gs.getName() + " em buscaGabarito()");
        } catch (IOException ex) {
            Logger.getLogger(AnnStd.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            brNDM.close();
        }

        return existe;
    }

    public void setIteracao(int iteracao) {
        this.iteracao = iteracao;
    }

    public void setTamBaseOrig(int tamBaseOrig) {
        this.tamBaseOrig = tamBaseOrig;
    }

    public void limpaTudo() {

        File dir = new File("./src/csv/conjuntosAS");

        if (dir.isDirectory()) {
            File[] sun = dir.listFiles();
            for (File toDelete : sun) {
                toDelete.delete();
            }
        }

        tp = 0;
        fp = 0;
        tn = 0;
        fn = 0;
        iteracao = 0;

    }

}
