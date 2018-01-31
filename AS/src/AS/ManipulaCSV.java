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
public class ManipulaCSV {

    int vp, fp, iteracao;
    File estatisticas;
    File DA;
    File divergencias;
    File DM;
    File NDM;
    File gs;
    FileWriter escreveEstat;

    public ManipulaCSV() {
        vp = 0;
        fp = 0;
        iteracao = 0;

        estatisticas = new File("./src/csv/", "estatisticas.csv");

        if (!estatisticas.exists()) {
            System.out.println("Não existe arquivo estatisticas.csv.");

            try {
                estatisticas.createNewFile();
                try {
                    escreveEstat = new FileWriter(estatisticas, true); //O parâmetro true faz com que as informações não sejam sobreescritas
                    BufferedWriter bwEstat = new BufferedWriter(escreveEstat);

                    bwEstat.write("Precision;Recall;F1;VP;FP;Iteração\n");
                    bwEstat.flush();
                    bwEstat.close();

                } catch (IOException ex) {
                    System.out.println("Não foi possível escrever o cabeçalho no arquivo estatisticas.csv.");
                }

            } catch (IOException ex) {
                System.out.println("Não foi possível criar arquivo estatisticas.csv.");
            }
        }

    }

    public File padronizaCsvFile(File arq) throws IOException {

        File arqPadr = null;

        try {
            BufferedReader brArq = new BufferedReader(new FileReader(arq.getPath()));

            String Str = "";
            String Str2 = "";
            int index_c1 = 0;
            int index_c2 = 0;
            int cont = 0;
            String[] linhaAtual;

            String diretorio = arq.getParent();
            String nome = arq.getName();
            nome = nome.substring(0, nome.indexOf('.'));

            FileWriter escreveArqPadr = new FileWriter(diretorio + "\\" + nome + "_NEW.csv", false);
            arqPadr = new File(diretorio + "\\" + nome + "_NEW.csv");

            while ((Str = brArq.readLine()) != null) {

                if (Str.contains("First Object")) {
                    System.out.println("Contém First Object");
                    System.out.println(Str);
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

            escreveArqPadr.flush();
            escreveArqPadr.close();
            brArq.close();

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return arqPadr;
    }

    public void comparaComGS(File arqResult) {
        //Já deve receber o arqResult padronizado
        //Adicionar um teste para saber se está padronizado ou não, para poder tratar o arquivo
        vp = 0;
        fp = 0;

        String Str;
        String elemento1;
        String elemento2;
        String[] linhaAtual;
        boolean existe = false;

        try {
            BufferedReader brResult = new BufferedReader(new FileReader(arqResult.getPath()));

            while ((Str = brResult.readLine()) != null) {

                linhaAtual = Str.split(";", 2);

                elemento1 = linhaAtual[0];
                elemento2 = linhaAtual[1];

                existe = buscaGabarito(elemento1, elemento2, gs);

                if (existe) {
                    vp++;
                } else {
                    fp++;
                }

            }

            brResult.close();
            calculaMetricas(vp, fp, gs);

        } catch (FileNotFoundException ex) {
            System.out.println("Não foi possível encontrar o arquivo " + arqResult.getName());
        } catch (IOException ex) {
            Logger.getLogger(ManipulaCSV.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean buscaGabarito(String elemento1, String elemento2, File gs) {
        //O gabarito tem de estar sem aspas
        String Str;
        String elementoGS1;
        String elementoGS2;
        String[] linhaAtual;
        boolean existe = false;

        try {
            BufferedReader brGS = new BufferedReader(new FileReader(gs.getPath()));

            while ((Str = brGS.readLine()) != null) {

                linhaAtual = Str.split(";", 2);

                elementoGS1 = linhaAtual[0];
                elementoGS2 = linhaAtual[1];

                if ((elemento1.equals(elementoGS1)) && (elemento2.equals(elementoGS2))) {

                    existe = true;
                    break;
                }
            }
            brGS.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Não foi possível encontrar o arquivo " + gs.getName());
        } catch (IOException ex) {
            Logger.getLogger(ManipulaCSV.class.getName()).log(Level.SEVERE, null, ex);
        }

        return existe;
    }

    public void calculaMetricas(int vp, int fp, File gs) {

        try {

            LineNumberReader linhaLeitura;
            linhaLeitura = new LineNumberReader(new FileReader(gs.getPath()));
            linhaLeitura.skip(gs.length());

            int qtdLinha = linhaLeitura.getLineNumber() + 1;

            double precision = getPrecision(vp, fp);
            double recall = getRecall(vp, qtdLinha);
            double f1 = getF1(precision, recall);

            try {
                escreveEstat = new FileWriter(estatisticas, true);
                BufferedWriter bwEstat = new BufferedWriter(escreveEstat);

                bwEstat.append(Double.toString(precision));
                bwEstat.append(";");
                bwEstat.append(Double.toString(recall));
                bwEstat.append(";");
                bwEstat.append(Double.toString(f1));
                bwEstat.append(";");
                bwEstat.append(Integer.toString(vp));
                bwEstat.append(";");
                bwEstat.append(Integer.toString(fp));
                bwEstat.append(";");
                bwEstat.append(Integer.toString(iteracao));
                bwEstat.append("\n");

                bwEstat.flush();
                bwEstat.close();

                iteracao++;
                vp = 0;
                fp = 0;

            } catch (FileNotFoundException ex) {
                System.out.println("Não foi possível encontrar o arquivo " + gs.getName());
            }

        } catch (FileNotFoundException ex) {
            System.out.println("Não foi possível encontrar o arquivo " + estatisticas.getName());
        } catch (IOException ex) {
            Logger.getLogger(ManipulaCSV.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public double getPrecision(int vp, int fp) {
        return (double) vp / (vp + fp);
    }

    public double getRecall(int vp, int tamGS) {
        return (double) fp / tamGS;
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

    public void comparaConjuntos(File arqResult) {

        String Str;
        String[] linhaAtual;
        boolean baseline = false;
//        arqResult é o resultado da atual iteração
        DA = new File("./src/csv/", "DA.csv");
        FileWriter escreveDupAuto;

//        File aux = new File("./src/csv/", "auxiliar.csv");
        File aux;

        if (!DA.exists()) {

            try {
                DA.createNewFile();

                BufferedReader brArqResult = new BufferedReader(new FileReader(arqResult.getPath()));

                escreveDupAuto = new FileWriter(DA);
                BufferedWriter bwDupAuto = new BufferedWriter(escreveDupAuto);

                //Copiando do primeiro arquivo
                while ((Str = brArqResult.readLine()) != null) {

                    linhaAtual = Str.split(";", 2);
                    bwDupAuto.write(linhaAtual[0] + ";" + linhaAtual[1] + "\n");

                }

                baseline = true;
                comparaComGS(DA);

                brArqResult.close();

                bwDupAuto.flush();
                bwDupAuto.close();

            } catch (IOException ex) {
                System.out.println("Não foi possível criar arquivo " + DA.getName());
            }
        }

        if (baseline == false) {

            aux = juntaArquivos(DA, arqResult); //Isso é feito para que se possa ver a união e intersecção do resultado atual com o DA

            atualizaDA(aux); //DA deve ficar apenas com a intersecção

            filtraDivergencias(aux); //NAO_DA deve ficar apenas com aquilo que não for intersecção com DA

            geraDM_NDM(); //Separação daquilo que não é DA em D_M e ND_M
        }
    }

    public File juntaArquivos(File arqDA, File arq2) {

        File juncao = new File("./src/csv/", "juncao.csv");
        FileWriter escreveJuncao;

        String Str;
        String[] linhaAtual;

        if (!juncao.exists()) {
            System.out.println("Não existe arquivo juncao.csv.");
            try {
                juncao.createNewFile();
            } catch (FileNotFoundException ex) {

                System.out.println("Não foi possível encontrar o arquivo " + juncao.getName());
            } catch (IOException ex) {
                System.out.println("Não foi possível criar o arquivo " + juncao.getName());
            }
        }

        //concatenação
        try {

            BufferedReader brDA = new BufferedReader(new FileReader(arqDA.getPath()));
            BufferedReader brArq2 = new BufferedReader(new FileReader(arq2.getPath()));

            escreveJuncao = new FileWriter(juncao);
            BufferedWriter bwJuncao = new BufferedWriter(escreveJuncao);

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

            brDA.close();
            brArq2.close();

            bwJuncao.flush();
            bwJuncao.close();

        } catch (FileNotFoundException ex) {

            Logger.getLogger(ManipulaCSV.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ManipulaCSV.class.getName()).log(Level.SEVERE, null, ex);
        }

        return juncao;
    }

    public File atualizaDA(File arqDuplicatas) {

        String Str;
        String Str2;
        String[] linhaAtual1;
        String[] linhaAtual2;
        List<String> dupEncontradas = new ArrayList<String>();
        boolean existe = false;
        boolean existe2 = false;
        int cont = 0;

        FileWriter escreveNovoDA;

        try {

            BufferedReader brArqDup = new BufferedReader(new FileReader(arqDuplicatas.getPath()));
            BufferedReader brArqDup2;

            escreveNovoDA = new FileWriter(DA); //Desta forma sobrescreve
            BufferedWriter bwDA = new BufferedWriter(escreveNovoDA);

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

                    if ((elemento1.equals(elementoA)) && (elemento2.equals(elementoB))) {
                        cont++;
                    }

                    if (cont >= 2) {

                        for (int x = 0; x < dupEncontradas.size(); x++) {
                            if (dupEncontradas.get(x).equals(elemento1 + ";" + elemento2)) {
                                existe2 = true; //Só é verdadeiro se a duplicada já tiver sido encontrada antes
                                break;
                            }

                        }

                        if (existe2 == false) { //Se não existe ocorrência anterior, adiciona a DA e à lista de duplicadas encontradas
                            bwDA.write(elemento1 + ";" + elemento2 + "\n");
                            dupEncontradas.add(elemento1 + ";" + elemento2);
                        }
                    }
                    existe2 = false;
                }
                brArqDup2.close();

                cont = 0;

            }
            brArqDup.close();

            bwDA.flush();
            bwDA.close();

        } catch (IOException ex) {
            Logger.getLogger(ManipulaCSV.class.getName()).log(Level.SEVERE, null, ex);
        }

        return DA;
    }

    //Para gerar DN e NM       
    public File filtraDivergencias(File arqDA) {

        String Str;
        String[] linhaAtual1;
        String[] linhaAtual2;
        boolean existe = false;
        int cont = 0;

        divergencias = new File("./src/csv/", "NAO_DA.csv");
        FileWriter escreveDiverg;

        if (!divergencias.exists()) {
            System.out.println("Não existe arquivo NAO_DA.csv.");

            try {
                divergencias.createNewFile();

            } catch (FileNotFoundException ex) {

                Logger.getLogger(ManipulaCSV.class
                        .getName()).log(Level.SEVERE, null, ex);

            } catch (IOException ex) {
                Logger.getLogger(ManipulaCSV.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        }

        try {

            BufferedReader brDA = new BufferedReader(new FileReader(arqDA.getPath()));

            escreveDiverg = new FileWriter(divergencias);
            BufferedWriter bwDiverg = new BufferedWriter(escreveDiverg);

            String elemento1;
            String elemento2;
            String elementoA;
            String elementoB;

            while ((Str = brDA.readLine()) != null) {

                existe = false;
                cont = 0;
                BufferedReader brDA2 = new BufferedReader(new FileReader(arqDA.getPath()));

                linhaAtual1 = Str.split(";", 2);

                elemento1 = linhaAtual1[0];
                elemento2 = linhaAtual1[1];

                while ((Str = brDA2.readLine()) != null) {

                    linhaAtual2 = Str.split(";", 2);

                    elementoA = linhaAtual2[0];
                    elementoB = linhaAtual2[1];

                    if ((elemento1.equals(elementoA)) && (elemento2.equals(elementoB))) {

                        cont++;
                    }

                    if (cont >= 2) {
                        existe = true;
                        break;
                    }
                }

                brDA2.close();

                if (existe == false) {
                    bwDiverg.write(elemento1 + ";" + elemento2 + "\n");
                }
            }
            brDA.close();

            bwDiverg.flush();
            bwDiverg.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManipulaCSV.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(ManipulaCSV.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return divergencias;
    }

    private void geraDM_NDM() {
        vp = 0;
        fp = 0;

        String Str = "";
        String elemento1 = "";
        String elemento2 = "";
        String[] linhaAtual;
        boolean existe = false;

        DM = new File("./src/csv/", "D_M.csv");
        FileWriter escreveDM;
        NDM = new File("./src/csv/", "ND_M.csv");
        FileWriter escreveNDM;

        if (!DM.exists()) {
            System.out.println("Não existe arquivo " + DM.getName());

            try {
                DM.createNewFile();

            } catch (FileNotFoundException ex) {

                Logger.getLogger(ManipulaCSV.class
                        .getName()).log(Level.SEVERE, null, ex);

            } catch (IOException ex) {
                Logger.getLogger(ManipulaCSV.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        }

        if (!NDM.exists()) {
            System.out.println("Não existe arquivo " + NDM.getName());

            try {
                NDM.createNewFile();

            } catch (FileNotFoundException ex) {

                Logger.getLogger(ManipulaCSV.class
                        .getName()).log(Level.SEVERE, null, ex);

            } catch (IOException ex) {
                Logger.getLogger(ManipulaCSV.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        }

        try {
            BufferedReader brDiverg = new BufferedReader(new FileReader(divergencias.getPath()));

            escreveDM = new FileWriter(DM);
            BufferedWriter bwDM = new BufferedWriter(escreveDM);

            escreveNDM = new FileWriter(NDM);
            BufferedWriter brNDM = new BufferedWriter(escreveNDM);

            while ((Str = brDiverg.readLine()) != null) {

                linhaAtual = Str.split(";", 2);

                elemento1 = linhaAtual[0];
                elemento2 = linhaAtual[1];

                existe = buscaGabarito(elemento1, elemento2, gs);

                if (existe) {
                    bwDM.write(elemento1 + ";" + elemento2 + "\n");
                } else {
                    brNDM.write(elemento1 + ";" + elemento2 + "\n");
                }

            }

            brDiverg.close();

            bwDM.flush();
            bwDM.close();

            brNDM.flush();
            brNDM.close();

//            calculaMetricas(vp, fp, gs);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManipulaCSV.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(ManipulaCSV.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

}
