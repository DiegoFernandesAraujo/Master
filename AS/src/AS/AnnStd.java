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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author Diego
 */
public class AnnStd {

    int tp, fp, tn, fn, iteracao, permutacao, tamBaseOrig, tamBaseOrig2, qtdAlg;
    boolean dedup = false;

    File estatisticas;
    File DA;
    File divergencias;
    File DM;
    File NDM;
    File gs;
    File arqResult;
    File DADM;
    FileWriter escreveEstat;
    Map<String, String> mapGS;
    Map<String, String> mapArqResult;
    String sepGS;

    public AnnStd(File gabarito) {

        this.gs = gabarito;

        System.out.println("Gabarito: " + gs);

        mapGS = new HashMap<String, String>();
        mapArqResult = new HashMap<String, String>();

        populaMapGS();

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

    public AnnStd(File gabarito, String base, String experimento) {

        this.gs = gabarito;

        mapGS = new HashMap<String, String>();
        mapArqResult = new HashMap<String, String>();

        populaMapGS();

        tp = 0;
        fp = 0;
        iteracao = 0;

        File dirEstat = new File("./src/csv/estatisticas/" + base + "/" + experimento);

        try {
            if (!dirEstat.exists()) {
                dirEstat.mkdirs();
                System.out.println("Diretório " + dirEstat.getAbsoluteFile() + " criado!");
            }
        } catch (Exception e) {
            System.out.println(e);
        }

//        File divergToAA = new File("./src/csv/conjuntosDS/conjuntosDiverg/", "diverg(" + getQtdAlg() + ")" + permutacao + ".csv");
        estatisticas = new File(dirEstat, "estatisticas.csv");
//        File divergToAA = new File("./src/csv/conjuntosDS/conjuntosDiverg-DEMO/", "diverg(" + getQtdAlg() + ")" + permutacao + ".csv");

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

    public AnnStd(File gabarito, String base, String experimento, String sepGS) {

        this.sepGS = sepGS;

        this.gs = gabarito;

        mapGS = new HashMap<String, String>();
        mapArqResult = new HashMap<String, String>();

        populaMapGS();

        tp = 0;
        fp = 0;
        iteracao = 0;

        File dirEstat = new File("./src/csv/estatisticas/" + base + "/" + experimento);

        try {
            if (!dirEstat.exists()) {
                dirEstat.mkdirs();
                System.out.println("Diretório " + dirEstat.getAbsoluteFile() + " criado!");
            }
        } catch (Exception e) {
            System.out.println(e);
        }

//        File divergToAA = new File("./src/csv/conjuntosDS/conjuntosDiverg/", "diverg(" + getQtdAlg() + ")" + permutacao + ".csv");
        estatisticas = new File(dirEstat, "estatisticas.csv");
//        File divergToAA = new File("./src/csv/conjuntosDS/conjuntosDiverg-DEMO/", "diverg(" + getQtdAlg() + ")" + permutacao + ".csv");

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

            escreveArqPadr = new FileWriter(diretorio + "/" + nome + "_NEW.csv", false);
            arqPadr = new File(diretorio + "/" + nome + "_NEW.csv");

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
//                }
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

    //Utilizado para computar os tp e fp
    public void comparaComGS(File arqResult) throws IOException {

        //System.out.println(new Throwable().getStackTrace()[0]);
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

//                existe = buscaGabarito(elemento1, elemento2, gs);
                existe = buscaGabarito2(elemento1, elemento2);

                if (existe) {
                    tp++;
                } else {
                    fp++;
                }

                Str = null;

                linhaAtual = null;

                elemento1 = null;
                elemento2 = null;

            }

//            gravaEstatisticas(tp, fp, gs, arqResult);
        } catch (FileNotFoundException ex) {
            System.out.println("Não foi possível encontrar o arquivo " + arqResult.getName());

        } catch (IOException ex) {
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);
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

//                linhaAtual = Str.split(";", 2);
                linhaAtual = Str.split(sepGS, 2);

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
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            brGS.close();
        }

        return existe;
    }

    //Utilizado para dizer se determinado par existe no gabarito
    private boolean buscaGabarito2(String elemento1, String elemento2) throws IOException {
        //O gabarito tem de estar sem aspas
        boolean existe = false;

        //Verificando se os pares existentes no arquivo já estão em DA
        if (mapGS.containsKey(elemento1 + ";" + elemento2) || mapGS.containsKey(elemento2 + ";" + elemento1)) {

            elemento1 = null;
            elemento2 = null;
            existe = true;
        }

        return existe;

    }

    public void comparaConjuntos(File arqResult) throws IOException {

        setArqResult(arqResult);

        populaMapArqResult();

        //Colocar o getArqResult nas invocações abaixo!!!! ATENÇÃO!!
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
            System.out.println("Não existe arquivo DA.csv em conjuntosAS.");
            BufferedReader brArqResult = null;
            BufferedWriter bwDupAuto = null;
            try {
                DA.createNewFile();
                //new Thread().sleep(50);

                brArqResult = new BufferedReader(new FileReader(arqResult.getPath()));

                escreveDupAuto = new FileWriter(DA);
                bwDupAuto = new BufferedWriter(escreveDupAuto);

                //Copiando do primeiro arquivo
                while ((Str = brArqResult.readLine()) != null) {

//                    linhaAtual = Str.split(";", 2);
//                    bwDupAuto.write(linhaAtual[0] + ";" + linhaAtual[1] + "\n");
                    linhaAtual = Str.split(";", 3);
//                    bwDupAuto.write(linhaAtual[0] + ";" + linhaAtual[1] + ";" + linhaAtual[2] + "\n");
//                    System.out.println(linhaAtual[0] + "-" + linhaAtual[1] + "-" + linhaAtual[2]);
                    bwDupAuto.write(linhaAtual[0] + ";" + linhaAtual[1] + "\n");

                }

                //DM e NDM são criados obrigatoriamente quando DA é criado
                if (!DM.exists()) {
                    System.out.println("Não existe arquivo DM.csv em conjuntosAS.");

                    try {
                        DM.createNewFile();
                        //new Thread().sleep(50);

                    } catch (FileNotFoundException ex) {

                        Logger.getLogger(AnnStd.class
                                .getName()).log(Level.SEVERE, null, ex);

                    } catch (IOException ex) {
                        Logger.getLogger(AnnStd.class
                                .getName()).log(Level.SEVERE, null, ex);

                    }

                }

                if (!NDM.exists()) {
                    System.out.println("Não existe arquivo NDM.csv em conjuntosAS.");

                    try {
                        NDM.createNewFile();
                        //new Thread().sleep(50);

                    } catch (FileNotFoundException ex) {

                        Logger.getLogger(AnnStd.class
                                .getName()).log(Level.SEVERE, null, ex);

                    } catch (IOException ex) {
                        Logger.getLogger(AnnStd.class
                                .getName()).log(Level.SEVERE, null, ex);

                    }

                }

                //Arquivo para cálculo de precision, recall e f-measure do Annealing todo
                if (!DADM.exists()) {
                    System.out.println("Não existe arquivo DADM.csv em conjuntosAS.");

                    try {
                        DADM.createNewFile();
                        //new Thread().sleep(50);

                    } catch (FileNotFoundException ex) {

                        Logger.getLogger(AnnStd.class
                                .getName()).log(Level.SEVERE, null, ex);

                    } catch (IOException ex) {
                        Logger.getLogger(AnnStd.class
                                .getName()).log(Level.SEVERE, null, ex);

                    }

                }

                baseline = true;

            } catch (IOException ex) {
                System.out.println("Não foi possível criar arquivo " + DA.getName());

            } finally {

                brArqResult.close();
                bwDupAuto.flush(); //Alterei DA aqui!!!!!
                bwDupAuto.close();

                //Juntar DA + DM para calcular precision, recall e f-measure para o TODO, O ANNEALING!
                //Como é o baseline poderia ser calculado apenas com DA mesmo
//                juntaDADM(DA, DM);
//
//                comparaComGS(DADM);
//                System.exit(0);
                comparaComGS(DA);

            }

        }

        if (baseline == false) {

            aux = juntaArquivos(DA, arqResult); //Isso é feito para que se possa ver a união e intersecção do resultado atual com o DA

//            atualizaDA(aux); //DA deve ficar apenas com a intersecção
            atualizaDA2(arqResult); //DA deve ficar apenas com a intersecção

//            filtraDivergencias(aux); //NAO_DA deve ficar apenas com aquilo que não for intersecção com DA
            filtraDivergenciasHash(aux); //NAO_DA deve ficar apenas com aquilo que não for intersecção com DA

//            atualizaDM_NDM(); //Separação daquilo que não é DA em D_M e ND_M. 
            atualizaDM_NDM2(); //Separação daquilo que não é DA em D_M e ND_M. 
//            JOptionPane.showMessageDialog(null, "Veja DM e NDM, bixim!\n" + iteracao + "\n" + getArqResult().getName());

            juntaDADM(DA, DM);

            comparaComGS(DADM);
        }
    }

    public File juntaArquivos(File arqDA, File arq2) throws IOException {

        //System.out.println(new Throwable().getStackTrace()[0]);

        File juncao = new File("./src/csv/conjuntosAS", "juncao.csv");
        FileWriter escreveJuncao;

        String Str;
        String[] linhaAtual;

        if (!juncao.exists()) {
            System.out.println("Não existe arquivo juncao.csv em conjuntosAS.");
            try {
                juncao.createNewFile();
                //new Thread().sleep(50);

            } catch (FileNotFoundException ex) {

                System.out.println("Não foi possível encontrar o arquivo " + juncao.getName());
            } catch (IOException ex) {
                System.out.println("Não foi possível criar o arquivo " + juncao.getName());

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

                linhaAtual = Str.split(";", 3);
                bwJuncao.write(linhaAtual[0] + ";" + linhaAtual[1] + "\n");

            }

        } catch (FileNotFoundException ex) {

            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            brDA.close();
            brArq2.close();

            bwJuncao.flush();
            bwJuncao.close();

        }

        return juncao;
    }

    public void juntaDADM(File arqDA, File arqDM) throws IOException {

        //System.out.println(new Throwable().getStackTrace()[0]);

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

            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            bwDA.flush();
            bwDA.close();
        }

        return DA;
    }

    public File atualizaDA2(File arqDuplicatas) throws IOException {

        //System.out.println(new Throwable().getStackTrace()[0]);

        String str;
        String[] linhaAtual1;
        String elemento1;
        String elemento2;

        FileWriter escreveNovoDA;
        BufferedReader brArqDup = null;
        BufferedWriter bwDA = null;
        Map<String, String> mapResult = new HashMap<String, String>();
        Map<String, String> mapDA = new HashMap<String, String>();

        //Armazenando valores do arquivo atual no mapa
        try {

            brArqDup = new BufferedReader(new FileReader(arqDuplicatas.getPath()));

            while ((str = brArqDup.readLine()) != null) {

                linhaAtual1 = str.split(";", 3);

                elemento1 = linhaAtual1[0];
                elemento2 = linhaAtual1[1];
//                System.out.println("tamanho: " + linhaAtual1.length);

                mapResult.put(elemento1 + ";" + elemento2, elemento1 + ";" + elemento2);

            }
            brArqDup.close();

        } catch (IOException ex) {
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            brArqDup = null;
            elemento1 = null;
            elemento2 = null;
//            System.out.println(arqDuplicatas.getName());
//            for (Map.Entry<String, String> entry : mapResult.entrySet()) {
//
////                System.out.println(entry.getKey() + " - " + entry.getValue());
//                System.out.println(entry.getKey());
//
//            }
        }

        //Verificando se os pares existentes no arquivo já estão em DA
        try {

//            System.out.println("Entrou no try");
            brArqDup = new BufferedReader(new FileReader(DA.getPath()));

            while ((str = brArqDup.readLine()) != null) {

                linhaAtual1 = str.split(";", 3);

                elemento1 = linhaAtual1[0];
                elemento2 = linhaAtual1[1];

//                System.out.println("tamanho: " + linhaAtual1.length);
//                System.out.println("Buscando: " + elemento1 + ";" + elemento2);
                if (mapResult.containsKey(elemento1 + ";" + elemento2)) {
//                    System.out.println("Achei: " + elemento1 + ";" + elemento2);
                    mapDA.put(elemento1 + ";" + elemento2, elemento1 + ";" + elemento2);
                    continue;
                }

//                System.out.println("Buscando: " + elemento2 + ";" + elemento1);
                if (mapResult.containsKey(elemento2 + ";" + elemento1)) {
//                    System.out.println("Achei: " + elemento2 + ";" + elemento1);
                    mapDA.put(elemento2 + ";" + elemento1, elemento2 + ";" + elemento1 + ";" + linhaAtual1[2]);
                    continue;
                }

            }
            brArqDup.close();

        } catch (IOException ex) {
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            brArqDup = null;
            elemento1 = null;
            elemento2 = null;
        }

        //Atualizando DA
        try {

            escreveNovoDA = new FileWriter(DA); //Desta forma sobrescreve
            bwDA = new BufferedWriter(escreveNovoDA);

            for (Map.Entry<String, String> entry : mapDA.entrySet()) {

                bwDA.write(entry.getValue() + "\n");

            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            bwDA.flush();
            bwDA.close();
            escreveNovoDA = null;
            bwDA = null;
        }

        mapResult.clear();
        mapResult = null;
        mapDA.clear();
        mapDA = null;

//        JOptionPane.showMessageDialog(null, "Veja o arquivo DATeste!");
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
            System.out.println("Não existe arquivo NAO_DA.csv em conjuntosAS.");

            try {
                divergencias.createNewFile();
                //new Thread().sleep(50);

            } catch (FileNotFoundException ex) {

                Logger.getLogger(AnnStd.class
                        .getName()).log(Level.SEVERE, null, ex);

            } catch (IOException ex) {
                Logger.getLogger(AnnStd.class
                        .getName()).log(Level.SEVERE, null, ex);

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

    public File filtraDivergenciasHash(File arqDA) throws IOException {

        //System.out.println(new Throwable().getStackTrace()[0]);

        //arqDA contém a junção do que está em DA com o último resultado (com dados repetidos, inclusive)
        HashMap<String, Boolean> numIndex = new HashMap<String, Boolean>();
        HashMap<String, Boolean> abandoned = new HashMap<String, Boolean>();

        String Str;
        String[] linhaAtual1;

        divergencias = new File("./src/csv/conjuntosAS", "NAO_DA.csv");

        if (!divergencias.exists()) {
            System.out.println("Não existe arquivo NAO_DA.csv em conjuntosAS.");

            try {
                divergencias.createNewFile();
                //new Thread().sleep(50);

            } catch (FileNotFoundException ex) {

                Logger.getLogger(AnnStd.class
                        .getName()).log(Level.SEVERE, null, ex);

            } catch (IOException ex) {
                Logger.getLogger(AnnStd.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        }

        BufferedReader brDA = null;
        FileWriter escreveDiverg = null;
        BufferedWriter bwDiverg = null;

        String elemento1 = null;
        String elemento2 = null;

        try {

            brDA = new BufferedReader(new FileReader(arqDA.getPath()));

            escreveDiverg = new FileWriter(divergencias); //Dessa forma sobrescreve
            bwDiverg = new BufferedWriter(escreveDiverg);

            while ((Str = brDA.readLine()) != null) {

                linhaAtual1 = Str.split(";", 2);

                elemento1 = linhaAtual1[0];
                elemento2 = linhaAtual1[1];

                try {
                    // check if already abandoned and skip this iteration
                    if ((abandoned.get(elemento1 + ";" + elemento2) != null) || (abandoned.get(elemento2 + ";" + elemento1) != null)) {
                        continue;
                    }
                } catch (Exception e) {

                }

                boolean isInIndex;
                try {
                    // check if it is already indexed
                    isInIndex = ((numIndex.get(elemento1 + ";" + elemento2) != null) || (numIndex.get(elemento2 + ";" + elemento1) != null));
                } catch (Exception e) {
                    // if not, we found it the first time
                    isInIndex = false;
                }

                if (isInIndex == false) {
                    //so we put it to the index
                    numIndex.put(elemento1 + ";" + elemento2, true);
                } else {
                    // if it appeared, we abandon it
                    numIndex.remove(elemento1 + ";" + elemento2);
                    abandoned.put(elemento1 + ";" + elemento2, true);

                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            brDA.close();
        }

        try {
            for (Map.Entry<String, Boolean> entry : numIndex.entrySet()) {

                linhaAtual1 = entry.getKey().split(";", 2);

                elemento1 = linhaAtual1[0];
                elemento2 = linhaAtual1[1];

                bwDiverg.write(elemento1 + ";" + elemento2 + "\n");

            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            bwDiverg.flush();
            bwDiverg.close();
//            System.out.println(entry.getKey());
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
        int cont = 0;

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

//                existeDM_NDM = buscaDM_NDM(elemento1, elemento2);
                existeDM_NDM = buscaDM_NDM2(elemento1, elemento2);

                //Só entra se já não existir em DM ou NDM
                if (!existeDM_NDM) {

//                    if(cont == 1){
//                        System.out.println(elemento1 + ";" + elemento2 + "\n");   
//                        cont++;
//                    }
//                    existeGS = buscaGabarito(elemento1, elemento2, gs);
                    existeGS = buscaGabarito2(elemento1, elemento2);

                    //Aqui são simuladas as inspeções manuais
                    //Se a divergência existe no gabarito, então é adicionada a DM
                    if (existeGS) {
                        bwDM.write(elemento1 + ";" + elemento2 + "\n");
                        bwDM.flush();

                    } else {
                        brNDM.write(elemento1 + ";" + elemento2 + "\n");
                        brNDM.flush();

                    }

//                    if(elemento1.equals("253332") && elemento2.equals("conf/sigmod/KornJF97")){
//                        JOptionPane.showMessageDialog(null, "Olhe o DM ou NDM!");
//                        cont++;
//                    }
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

            bwDM.close();

            brNDM.close();
        }
    }

    private void atualizaDM_NDM2() throws IOException {

        //System.out.println(new Throwable().getStackTrace()[0]);
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
        BufferedWriter bwNDM = null;

        String str;

        Map<String, String> mapDM = new HashMap<String, String>();
        Map<String, String> mapNDM = new HashMap<String, String>();

        BufferedReader brDM = null;
        BufferedReader brNDM = null;

        //Armazenando valores do arquivo atual no mapa
        try {

            brDM = new BufferedReader(new FileReader(DM.getPath()));

            while ((str = brDM.readLine()) != null) {

                mapDM.put(str, str);
            }
            brDM.close();

        } catch (IOException ex) {
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            brDM = null;
        }

        //Armazenando valores do arquivo atual no mapa
        try {

            brNDM = new BufferedReader(new FileReader(NDM.getPath()));

            while ((str = brNDM.readLine()) != null) {

                mapNDM.put(str, str);
            }
            brNDM.close();

        } catch (IOException ex) {
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            brNDM = null;
        }

        try {
            brDiverg = new BufferedReader(new FileReader(divergencias.getPath()));

            escreveDM = new FileWriter(DM, true); //Sem sobrescrever
            bwDM = new BufferedWriter(escreveDM);

            escreveNDM = new FileWriter(NDM, true); //Sem sobrescrever
            bwNDM = new BufferedWriter(escreveNDM);

            while ((Str = brDiverg.readLine()) != null) {

                linhaAtual = Str.split(";", 2);

                elemento1 = linhaAtual[0];
                elemento2 = linhaAtual[1];

//                existeDM_NDM = buscaDM_NDM(elemento1, elemento2);
                existeDM_NDM = ((mapDM.containsKey(elemento1 + ";" + elemento2) || mapDM.containsKey(elemento2 + ";" + elemento1))
                        || (mapNDM.containsKey(elemento1 + ";" + elemento2) || mapNDM.containsKey(elemento2 + ";" + elemento1)));

//                existeDM_NDM = buscaDM_NDM2(elemento1, elemento2);
                //Só entra se já não existir em DM ou NDM
                if (!existeDM_NDM) {

                    existeGS = buscaGabarito2(elemento1, elemento2);

                    //Aqui são simuladas as inspeções manuais
                    //Se a divergência existe no gabarito, então é adicionada a DM
                    if (existeGS) {

                        mapDM.put(elemento1 + ";" + elemento2, elemento1 + ";" + elemento2);

//                        bwDM.write(elemento1 + ";" + elemento2 + "\n");
//                        bwDM.flush();
                    } else {

                        mapNDM.put(elemento1 + ";" + elemento2, elemento1 + ";" + elemento2);
//                        bwNDM.write(elemento1 + ";" + elemento2 + "\n");
//                        bwNDM.flush();

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

            bwDM.close();

            bwNDM.close();
        }

        //Escrevendo os mapas nos arquivos
        try {

            escreveDM = new FileWriter(DM); //Sem sobrescrever
            bwDM = new BufferedWriter(escreveDM);

            escreveNDM = new FileWriter(NDM); //Sem sobrescrever
            bwNDM = new BufferedWriter(escreveNDM);

            for (Map.Entry<String, String> entry : mapDM.entrySet()) {

                bwDM.write(entry.getValue() + "\n");
//                bwDM.flush();

            }

            for (Map.Entry<String, String> entry : mapNDM.entrySet()) {

                bwNDM.write(entry.getValue() + "\n");
//                bwNDM.flush();

            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(AnnStdMap.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(AnnStdMap.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            bwDM.flush();
            bwDM.close();
            escreveDM = null;
            bwDM = null;

            bwNDM.flush();
            bwNDM.close();
            escreveNDM = null;
            bwNDM = null;
        }

        elemento1 = null;
        elemento2 = null;
        mapDM.clear();
        mapDM = null;
        mapNDM.clear();
        mapNDM = null;
    }

    public void gravaEstatisticas(int tp, int fp, File gs, File arqResult) {

        try {

            double precision = getPrecision(tp, fp);
//            int fn = getFN(arqResult);
            int fn = getFN2(arqResult);
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

                bwEstat.append("AS");
                bwEstat.append(";");
                bwEstat.append("only");
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
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(AnnStd.class
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

    public int getInspManuais() throws IOException {

        return getTamNDM() + getTamDM();
    }

    public int getQtdAlg() {
        return qtdAlg;
    }

    public void setQtdAlg(int qtdAlg) {
        this.qtdAlg = qtdAlg;
    }

    public File getArqResult() {
        return arqResult;
    }

    public void setArqResult(File arqResult) {
        this.arqResult = null;
        this.arqResult = arqResult;
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
        int contExiste = 0;
        int contNaoExiste = 0;

        BufferedReader brGS = null;
        try {

            if (gs.getPath().contains(".csv")) {

                brGS = new BufferedReader(new FileReader(gs.getPath()));

            } else {

                brGS = new BufferedReader(new FileReader(gs.getPath() + ".csv"));
            }

            while ((Str = brGS.readLine()) != null) {

//                linhaAtual = Str.split(";", 2);
                linhaAtual = Str.split(sepGS, 2);

                elementoGS1 = linhaAtual[0];
                elementoGS2 = linhaAtual[1];

                //Passando o arqResult para procurar os elementos do gabarito nele.
                //O que estiver no gabarito e não estiver em arqResult é, portanto, um falso negativo
//                existe = buscaFN(elementoGS1, elementoGS2, arqResult);
                existe = buscaFN2(elementoGS1, elementoGS2, arqResult);

//                if (existe) {
//                    contExiste++;
//                }
                //Só entra se a variável existe for falsa
                if (!existe) {
//                    contNaoExiste++;
                    fn++;
                }

            }
            fn--; //Reduzindo um do título do gabarito

        } catch (FileNotFoundException ex) {
            System.out.println("Não foi possível encontrar o arquivo " + gs.getName() + " em getFN()");

        } catch (IOException ex) {
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            brGS.close();

            if (getArqResult().getName().contains("23")) {
//                System.out.println("Quantidade de vezes que existe foi verdadeiro: " + contExiste);
//                System.out.println("Quantidade de vezes que existe foi falso: " + contNaoExiste);
//                System.out.println("fn: " + fn);
            }

        }

        return fn;
    }

    public int getFN2(File arqResult) throws IOException {
        //Já deve receber o arqResult padronizado
        //Adicionar um teste para saber se está padronizado ou não, para poder tratar o arquivo

        String str;

        BufferedReader brArq = null;

        Map<String, String> map = new HashMap<String, String>();

        //Armazenando valores do arquivo atual no mapa
        try {

            brArq = new BufferedReader(new FileReader(arqResult.getPath()));
            while ((str = brArq.readLine()) != null) {

                map.put(str, str);
            }
            brArq.close();

        } catch (IOException ex) {
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            brArq = null;
        }

        ///////////////////////
        tp = 0;
        fp = 0;

        String elementoGS1;
        String elementoGS2;
        String[] linhaAtual;
        boolean existe = false;

        for (Map.Entry<String, String> entry : mapGS.entrySet()) {

            existe = false;
            linhaAtual = entry.getValue().split(";", 2);

            elementoGS1 = linhaAtual[0];
            elementoGS2 = linhaAtual[1];
//            System.out.println("entry.getValue(): " + entry.getValue());
//            System.out.println("elementoGS1: " + elementoGS1 + " - elementoGS2: " + elementoGS2);

            //Passando o arqResult para procurar os elementos do gabarito nele.
            //O que estiver no gabarito e não estiver em arqResult é, portanto, um falso negativo
//                existe = buscaFN(elementoGS1, elementoGS2, arqResult);
//                existe = buscaFN2(elementoGS1, elementoGS2, arqResult);
            if (map.containsKey(elementoGS1 + ";" + elementoGS2) || map.containsKey(elementoGS2 + ";" + elementoGS1)) {

                existe = true;

            }

//                if (existe) {
//                    contExiste++;
//                }
            //Só entra se a variável existe for falsa
            if (!existe) {
//                    contNaoExiste++;
                fn++;
            }

        }
//        fn--; //Reduzindo um do título do gabarito --> Não necessário com mapa
        map.clear();
        return fn;
    }

    private boolean buscaFN(String elementoGS1, String elementoGS2, File arqResult) throws IOException {
        String str;
        String elemento1;
        String elemento2;
        String[] linhaAtual;
        boolean existe = false;

        BufferedReader brArqResult = null;
        try {
            brArqResult = new BufferedReader(new FileReader(arqResult.getPath()));

            while ((str = brArqResult.readLine()) != null) {

                linhaAtual = str.split(";", 2);

                elemento1 = linhaAtual[0];
                elemento2 = linhaAtual[1];

                //Para admitir o fecho transitivo:
                if (((elemento1.equals(elementoGS1)) && (elemento2.equals(elementoGS2))) || ((elemento1.equals(elementoGS2)) && ((elemento2.equals(elementoGS1))))) {

                    existe = true;

                    if (getArqResult().getName().contains("23")) {
//                System.out.println(getArqResult().getName() + " contém " + elemento1 + ";" + elemento2);
                        System.out.println(elemento1 + ";" + elemento2);
                    }

                    break;
                } else {
                    if (getArqResult().getName().contains("23")) {
//                System.out.println(getArqResult().getName() + " contém " + elemento1 + ";" + elemento2);
                        System.out.println(elemento1 + ";" + elemento2);
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Não foi possível encontrar o arquivo " + gs.getName() + " em buscaFN()");

        } catch (IOException ex) {
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            brArqResult.close();
        }

        return existe;
    }

    private boolean buscaFN2(String elemento1, String elemento2, File arq) throws IOException {
        //O gabarito tem de estar sem aspas

        String str;

        BufferedReader brArq = null;

        Map<String, String> map = new HashMap<String, String>();

        //Armazenando valores do arquivo atual no mapa
        try {

            brArq = new BufferedReader(new FileReader(arq.getPath()));
            while ((str = brArq.readLine()) != null) {

                map.put(str, str);
            }
            brArq.close();

        } catch (IOException ex) {
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            brArq = null;
        }

        boolean existe = false;

        int cont = 0;

        //Verificando se os pares existentes no arquivo já estão em DA
        if (map.containsKey(elemento1 + ";" + elemento2) || map.containsKey(elemento2 + ";" + elemento1)) {

            existe = true;

        } else {
        }

        elemento1 = null;
        elemento2 = null;
        map.clear();
        map = null;

        return existe;

    }

    private boolean buscaDM_NDM(String elemento1, String elemento2) throws IOException {
        String Str;
        String StrAnt = null;
        String elementoDM_NDM1;
        String elementoDM_NDM2;
        String[] linhaAtual;
        boolean existe = false;

        BufferedReader brDM = null;
        try {
            brDM = new BufferedReader(new FileReader(DM.getPath()));

            while ((Str = brDM.readLine()) != null) {

                try {
                    linhaAtual = Str.split(";", 2);

                    elementoDM_NDM1 = linhaAtual[0];
                    elementoDM_NDM2 = linhaAtual[1];

                    if (((elemento1.equals(elementoDM_NDM1)) && (elemento2.equals(elementoDM_NDM2))) || ((elemento1.equals(elementoDM_NDM2)) && ((elemento2.equals(elementoDM_NDM1))))) {

                        return existe = true;

                    }
                } catch (Exception e) {
                    System.out.println("str: " + Str + " - StrAnt: " + StrAnt);
                }
                StrAnt = Str;
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Não foi possível encontrar o arquivo " + gs.getName() + " em buscaGabarito()");

        } catch (IOException ex) {
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            brNDM.close();
        }

        return existe;
    }

    private boolean buscaDM_NDM2(String elemento1, String elemento2) throws IOException {

        boolean existe = false;

        String str;

        Map<String, String> mapDM = new HashMap<String, String>();
        Map<String, String> mapNDM = new HashMap<String, String>();

        BufferedReader brDM = null;
        BufferedReader brNDM = null;

        //Armazenando valores do arquivo atual no mapa
        try {

            brDM = new BufferedReader(new FileReader(DM.getPath()));

            while ((str = brDM.readLine()) != null) {

                mapDM.put(str, str);
            }
            brDM.close();

        } catch (IOException ex) {
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            brDM = null;
        }

        //Armazenando valores do arquivo atual no mapa
        try {

            brNDM = new BufferedReader(new FileReader(NDM.getPath()));

            while ((str = brNDM.readLine()) != null) {

                mapNDM.put(str, str);
            }
            brNDM.close();

        } catch (IOException ex) {
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            brNDM = null;
        }

//        ***********************
        //Verificando se os pares existentes no arquivo já estão em DA
        if ((mapDM.containsKey(elemento1 + ";" + elemento2) || mapDM.containsKey(elemento2 + ";" + elemento1))
                || (mapNDM.containsKey(elemento1 + ";" + elemento2) || mapNDM.containsKey(elemento2 + ";" + elemento1))) {

            existe = true;

        }

        elemento1 = null;
        elemento2 = null;
        mapDM.clear();
        mapDM = null;
        mapNDM.clear();
        mapNDM = null;

        return existe;
    }

    public void setIteracao(int iteracao) {
        this.iteracao = iteracao;
    }

    /**
     *
     * @return
     */
    public int getTamBaseOrig() {
        return tamBaseOrig;
    }

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

    public void populaMapGS() {

        String str;
        String[] linhaAtual1;
        String elemento1;
        String elemento2;

        BufferedReader brGS = null;

        //Armazenando valores do arquivo atual no mapa
        try {

            if (gs.getPath().contains(".csv")) {

                brGS = new BufferedReader(new FileReader(gs.getPath()));

            } else {

                brGS = new BufferedReader(new FileReader(gs.getPath() + ".csv"));
            }

            int linha = 0;

            while ((str = brGS.readLine()) != null) {

                if (linha > 0) {
//                    linhaAtual1 = str.split(";", 2);
                    linhaAtual1 = str.split(sepGS, 2);
                    try {

                        elemento1 = linhaAtual1[0];
                        elemento2 = linhaAtual1[1];

                        mapGS.put(elemento1 + ";" + elemento2, elemento1 + ";" + elemento2);

                    } catch (Exception e) {
                        Logger.getLogger(AnnStd.class
                                .getName()).log(Level.SEVERE, null, e);
                    }
                }
                linha++;
            }
            brGS.close();

        } catch (IOException ex) {
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            brGS = null;
            elemento1 = null;
            elemento2 = null;
//            int cont = 0;
//            for (Map.Entry<String, String> entry : mapGS.entrySet()) {
//
//                System.out.println(++cont + " " + entry.getKey() + " - " + entry.getValue());
////                System.out.println(entry.getKey());
//            }
        }

    }

    public void populaMapArqResult() {

//        System.out.println("populaMapArqResult!");
        //Limpando valores anteriores
        try {
            mapArqResult.clear();
        } catch (Exception e) {
            System.out.println(e);
        }

        String str;
        String[] linhaAtual1;
        String elementoA1;
        String elementoA2;

        BufferedReader brArqResult = null;

        //Armazenando valores do arquivo atual no mapa
        try {

            brArqResult = new BufferedReader(new FileReader(getArqResult().getPath()));
//            System.out.println(getArqResult().getName());

//            int linha = 0;
            while ((str = brArqResult.readLine()) != null) {

//                if (linha > 0) {
                linhaAtual1 = str.split(";", 3);

                elementoA1 = linhaAtual1[0];
                elementoA2 = linhaAtual1[1];

                mapArqResult.put(elementoA1 + ";" + elementoA2, elementoA1 + ";" + elementoA2 + ";" + linhaAtual1[2]);
//                }
//                linha++;
            }
            brArqResult.close();

        } catch (IOException ex) {
            Logger.getLogger(AnnStd.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            brArqResult = null;
            elementoA1 = null;
            elementoA2 = null;
//            int cont = 0;
//            for (Map.Entry<String, String> entry : mapArqResult.entrySet()) {
//
////                System.out.println(++cont + " " + entry.getKey() + " - " + entry.getValue());
//                System.out.println(entry.getKey());
//            }
        }

    }

//      Para imprimir todos os métodos desta classe
//    public static void main(String args[]) {
//        System.out.println("");
//        try {
//            Class c = AnnStd.class;
//            Method[] m = c.getDeclaredMethods();
//            for (int i = 0; i < m.length; i++) {
//                System.out.println(m[i].toString());
//            }
//        } catch (Throwable e) {
//            System.err.println(e);
//        }
//    }
}
