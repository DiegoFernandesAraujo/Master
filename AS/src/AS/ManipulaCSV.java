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

/**
 *
 * @author Diego
 */
public class ManipulaCSV {

    int vp, fp, iteracao;
    File estat;
    File D_A;
    File D_M;
    File ND_M;
    File gs;
    FileWriter estatisticas;

    public ManipulaCSV() {

        vp = 0;
        fp = 0;
        iteracao = 0;

        estat = new File("./src/csv/", "estatisticas.csv");

        if (!estat.exists()) {
            System.out.println("Não existe arquivo estatisticas.csv.");

            try {
                estat.createNewFile();
                try {
                    estatisticas = new FileWriter(estat, true); //O parâmetro true faz com que as informações não sejam sobreescritas
                    BufferedWriter bwEstat = new BufferedWriter(estatisticas);

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

    public File padronizaCsvFile(File arquivo) throws IOException {

        File returnCSV = null;

        try {
            BufferedReader brArquivo = new BufferedReader(new FileReader(arquivo.getPath()));

            String Str = "";
            String Str2 = "";
            int index_c1 = 0;
            int index_c2 = 0;
            int cont = 0;
            String[] TableLine;

            String diretorio = arquivo.getParent();
            String nome = arquivo.getName();
            nome = nome.substring(0, nome.indexOf('.'));

//            if (arquivo.exists()) {
//                System.out.println(arquivo.getName() + " existe!");
//            }
//            arquivo.
//            arquivo.createNewFile();
//            
            FileWriter brNovoArquivo = new FileWriter(diretorio + "\\" + nome + "_NEW.csv", false);
            returnCSV = new File(diretorio + "\\" + nome + "_NEW.csv");

            //Essa estrutura do looping while é clássica para ler cada linha
            //do arquivo
            while ((Str = brArquivo.readLine()) != null) {

                if (Str.contains("First Object")) {
                    System.out.println("Contém First Object");
                    System.out.println(Str);
                    continue;
                }
                //Aqui usamos o método split que divide a linha lida em um array de String
                //passando como parametro o divisor ";".
//                    TableLine = Str.split(";");

                TableLine = Str.split(";", 2); //Nesse caso considera apenas as duas primeiras colunas (as que interessam)
                cont = 0;
//                System.out.println("TableLine[0] = " + TableLine[0] + " TableLine[1] = " + TableLine[1]);
                //O foreach é usado para imprimir cada célula do array de String.
                for (String cell : TableLine) {

                    cont++;

                    index_c1 = cell.indexOf('[');
                    index_c2 = cell.indexOf(']');

                    Str2 = cell.substring(index_c1 + 1, index_c2);

                    brNovoArquivo.append(Str2);

                    if (cont == 1) {
                        brNovoArquivo.append(';');
                    } else {
                        brNovoArquivo.append('\n');
                    }

                }
//                System.out.println("Comprimento do array = " + TableLine.length);
            }

            brNovoArquivo.flush();
            brNovoArquivo.close();
            //Fechamos o buffer
            brArquivo.close();

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return returnCSV;
    }

    public void comparaComGS(File arqResult) {
        //Já deve receber o arqResult padronizado
        vp = 0;
        fp = 0;

        String Str = "";
        String elemento1 = "";
        String elemento2 = "";
        String[] TableLine;
        boolean existe = false;

        try {
            BufferedReader brResult = new BufferedReader(new FileReader(arqResult.getPath()));

            while ((Str = brResult.readLine()) != null) {

                TableLine = Str.split(";", 2);

                elemento1 = TableLine[0];
                elemento2 = TableLine[1];

//                System.out.println("e1: " + elemento1 + " e2: " + elemento2);
                existe = buscaGabarito(elemento1, elemento2, gs);

                if (existe) {
//                    System.out.println("Existe! vp++");
                    vp++;
                } else {
                    fp++;
                }

            }

            brResult.close();
            calculaMetricas(vp, fp, gs);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManipulaCSV.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ManipulaCSV.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean buscaGabarito(String elemento1, String elemento2, File gs) {
        //O gabarito tem de estar sem aspas
        String Str, elementoGS1, elementoGS2 = "";
        String[] TableLine;
        boolean existe = false;

        try {
            BufferedReader brGS = new BufferedReader(new FileReader(gs.getPath()));

            while ((Str = brGS.readLine()) != null) {

                TableLine = Str.split(";", 2);

                elementoGS1 = TableLine[0];
                elementoGS2 = TableLine[1];

//                if(elemento1.equals("10230") && elemento2.equals("10383")){
//                    System.out.println("gs1: " + elementoGS1 + " gs2: " + elementoGS2);
//                }
//                System.out.println("gs1: " + elementoGS1 + " gs2: " + elementoGS2);
                if ((elemento1.equals(elementoGS1)) && (elemento2.equals(elementoGS2))) {
//                    System.out.println("Existe em buscaGabarito.");
                    existe = true;
                    break;
                }
            }
            brGS.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManipulaCSV.class.getName()).log(Level.SEVERE, null, ex);
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
//            int qtdLinha = linhaLeitura.getLineNumber() + 1;
            int qtdLinha = linhaLeitura.getLineNumber() + 1;

            double precision = getPrecision(vp, fp);
            double recall = getRecall(vp, qtdLinha);
            double f1 = getF1(precision, recall);

            try {
                estatisticas = new FileWriter(estat, true);
                BufferedWriter bwEstat = new BufferedWriter(estatisticas);
//                System.out.println(estatisticas.);

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
//                StrW.write(Double.toString(precision) + ";" + Double.toString(recall) + ";" + Double.toString(f1) + ";" + Double.toString(iteracao) + "\n");
//                estatisticas.flush();
//                estatisticas.close();
                bwEstat.flush();
                bwEstat.close();
                iteracao++;
                vp = 0;
                fp = 0;

            } catch (FileNotFoundException ex) {
                Logger.getLogger(ManipulaCSV.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManipulaCSV.class.getName()).log(Level.SEVERE, null, ex);
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
        String[] TableLine;
        boolean baseline = false;
//        arqResult é o resultado da atual iteração
        D_A = new File("./src/csv/", "D_A.csv");
        FileWriter escreveD_A;

        File NAO_D_A = new File("./src/csv/", "NAO_D_A.csv");
        File aux = new File("./src/csv/", "auxiliar.csv");

        if (!D_A.exists()) {

            try {
                D_A.createNewFile();

                System.out.println("CRIANDO D_A");

                BufferedReader brArqResult = new BufferedReader(new FileReader(arqResult.getPath()));

                escreveD_A = new FileWriter(D_A);
                BufferedWriter bwD_A = new BufferedWriter(escreveD_A);

                //Copiando do primeiro arquivo
                while ((Str = brArqResult.readLine()) != null) {

                    TableLine = Str.split(";", 2);
//                    System.out.println(TableLine[0] + ";" + TableLine[1]);
                    bwD_A.write(TableLine[0] + ";" + TableLine[1] + "\n");

                }

                baseline = true;
                comparaComGS(D_A);
                brArqResult.close();

                bwD_A.flush();
                bwD_A.close();

            } catch (IOException ex) {
                System.out.println("Não foi possível criar arquivo D_A.csv.");
            }
        }
//            Desnecessário, acho
//            if (!aux1.exists()) {
//
//                try {
//                    aux1.createNewFile();
//
//                } catch (IOException ex) {
//                    Logger.getLogger(ManipulaCSV.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//            } else {
//                try {

        if (baseline == false) {
            aux = juntaArquivos(D_A, arqResult); //Isso é feito para que se possa ver a união e intersecção do resultado atual com o D_A

            atualizaD_A(aux); //D_A deve ficar apenas com a intersecção

//        NAO_D_A = removeDuplicatas(aux); //NAO_D_A deve ficar apenas com aquilo que não for intersecção com D_A
//        geraDM_NDM(NAO_D_A); //Separação daquilo que não é D_A em D_M e ND_M
//                } catch (Exception e) {
//                    System.out.println("Exceção...");
//                }
//            }
        }
    }
    //OK

    public File juntaArquivos(File arquivoD_A, File arquivo2) {

        File juncao = new File("./src/csv/", "juncao.csv");
        FileWriter escreveJuncao;

        String Str;
        String[] TableLine;

        if (!juncao.exists()) {
            System.out.println("Não existe arquivo juncao.csv.");
            try {
                juncao.createNewFile();
            } catch (FileNotFoundException ex) {

                Logger.getLogger(ManipulaCSV.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ManipulaCSV.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        System.out.println("Existe arquivo juncao.csv.");

        //concatenação
        try {

            BufferedReader brD_A = new BufferedReader(new FileReader(arquivoD_A.getPath()));
            BufferedReader brArq2 = new BufferedReader(new FileReader(arquivo2.getPath()));

            escreveJuncao = new FileWriter(juncao);
            BufferedWriter bwJuncao = new BufferedWriter(escreveJuncao);

            //Copiando do primeiro arquivo
            while ((Str = brD_A.readLine()) != null) {

                TableLine = Str.split(";", 2);
//                System.out.println(TableLine[0] + ";" + TableLine[1]);
                bwJuncao.write(TableLine[0] + ";" + TableLine[1] + "\n");

            }
            //Copiando do segundo arquivo
            while ((Str = brArq2.readLine()) != null) {

//                System.out.println("Entrei no segundo");
                TableLine = Str.split(";", 2);
//                System.out.println(TableLine[0] + ";" + TableLine[1]);
                bwJuncao.write(TableLine[0] + ";" + TableLine[1] + "\n");

            }
            
            brD_A.close();
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

    public File atualizaD_A(File arqDuplicatas) {
        System.out.println("Tamanho de D_A antes da atualização: " + D_A.length());

        String Str;
        String Str2;
        String[] TableLine1;
        String[] TableLine2;
        int cont = 0;
        int cont1 = 0;
        boolean existe = false;

        FileWriter duplicatas;

        try {

            BufferedReader brArqDup = new BufferedReader(new FileReader(arqDuplicatas.getPath()));
            BufferedReader brArqDup2;
            
            
//            duplicatas = new FileWriter(D_A, false); //Tem que apagar antes
            duplicatas = new FileWriter(D_A); //Tem que apagar antes
            BufferedWriter bwD_A = new BufferedWriter(duplicatas);

            String elemento1;
            String elemento2;
            String elementoA;
            String elementoB;
//            Collection lista = new ArrayList();

            while ((Str = brArqDup.readLine()) != null) {
                brArqDup2 = new BufferedReader(new FileReader(arqDuplicatas.getPath()));
                TableLine1 = Str.split(";", 2);

                elemento1 = TableLine1[0];
                elemento2 = TableLine1[1];
                

//                lista.add(TableLine1[0] + ";" + TableLine1[1]);
//            }
//
//            Collection lista2 = new LinkedHashSet(lista);
                while ((Str2 = brArqDup2.readLine()) != null) {
//                    System.out.println(cont1++);
                    TableLine2 = Str2.split(";", 2);

                    elementoA = TableLine2[0];
                    elementoB = TableLine2[1];

//                if(elemento1.equals("10230") && elemento2.equals("10383")){
//                    System.out.println("gs1: " + elementoGS1 + " gs2: " + elementoGS2);
//                }
//                System.out.println("gs1: " + elementoGS1 + " gs2: " + elementoGS2);
                    //Se os elementos forem iguais adiciona ao new_D_A
                    if ((elemento1.equals(elementoA)) && (elemento2.equals(elementoB))) {
//                    System.out.println("Existe em buscaGabarito.");
                        //copia uma delas para aux2 e depois remove de aux
                        //COPIA
//                        System.out.println(elemento1 + " ; " + elemento2);
//                        System.out.println(elementoA + " ; " + elementoB);
//                        System.out.println("");
                        //DELETE!!!
                        cont++;
//                        StrW2.write(TableLine1[0] + ";" + TableLine1[1] + "\n");
                        //DELETE!!!

                    }

                    if (cont >= 2) {
//                        System.out.println("Maior ou igual a dois");
                        System.out.println(cont1++);
                        bwD_A.write(TableLine1[0] + ";" + TableLine1[1] + "\n");
                        break;
                    }
                }
                brArqDup2.close();
//                System.out.println("break");
                
                cont = 0;

            }
            brArqDup.close();
            

            bwD_A.flush();
            bwD_A.close();

        } catch (IOException ex) {
            Logger.getLogger(ManipulaCSV.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println(
                "Tamanho de D_A depois da atualização: " + D_A.length());
        return D_A;
    }
//Para gerar DN e NM

    public File removeDuplicatas(File arqDA) {

        String Str;
        String[] TableLine1;
        String[] TableLine2;
        boolean existe = false;
        int cont = 0;

        File new_NAO_D_A = new File("./src/csv/", "new_NAO_D_A.csv");
        FileWriter naoDuplicatas;

        if (!new_NAO_D_A.exists()) {
            System.out.println("Não existe arquivo new_NAO_D_A.csv.");

            try {
                new_NAO_D_A.createNewFile();

            } catch (FileNotFoundException ex) {

                Logger.getLogger(ManipulaCSV.class
                        .getName()).log(Level.SEVERE, null, ex);

            } catch (IOException ex) {
                Logger.getLogger(ManipulaCSV.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        }

        try {

            BufferedReader brD_A = new BufferedReader(new FileReader(arqDA.getPath()));
            BufferedReader brD_A2 = new BufferedReader(new FileReader(arqDA.getPath()));
            
            
            naoDuplicatas = new FileWriter(new_NAO_D_A);
            BufferedWriter bwN_D_A = new BufferedWriter(naoDuplicatas);

            String elemento1;
            String elemento2;
            String elementoA;
            String elementoB;
//            Collection lista = new ArrayList();

            while ((Str = brD_A.readLine()) != null) {

                existe = false;

                TableLine1 = Str.split(";", 2);

                elemento1 = TableLine1[0];
                elemento2 = TableLine1[1];

//                lista.add(TableLine1[0] + ";" + TableLine1[1]);
//            }
//
//            Collection lista2 = new LinkedHashSet(lista);
                while ((Str = brD_A2.readLine()) != null) {

                    TableLine2 = Str.split(";", 2);

                    elementoA = TableLine2[0];
                    elementoB = TableLine2[1];

//                if(elemento1.equals("10230") && elemento2.equals("10383")){
//                    System.out.println("gs1: " + elementoGS1 + " gs2: " + elementoGS2);
//                }
//                System.out.println("gs1: " + elementoGS1 + " gs2: " + elementoGS2);
                    if ((elemento1.equals(elementoA)) && (elemento2.equals(elementoB))) {
//                    System.out.println("Existe em buscaGabarito.");
                        //copia uma delas para aux2 e depois remove de aux
                        //COPIA
                        cont++;
//                        StrW2.write(TableLine1[0] + ";" + TableLine1[1] + "\n");
                        //DELETE!!!

                    }

                    if (cont >= 2) {
                        existe = true;
                        break;
                    }
                }

                if (existe == false) {
                    bwN_D_A.write(TableLine1[0] + ";" + TableLine1[1] + "\n");
                    cont = 0;
                }
            }
//            for (Object item : lista2) {
//                
//                /* ESCREVE A LISTA NO ARQUIVO...
//             * OBSERVE O TYPECAST FEITO POIS O 
//             * FOR ESTÁ PASSANDO UM "Object"
//                 */
//                StrW2.write((String) item);
//                StrW2.newLine();
//            }
            brD_A.close();
            brD_A2.close();

            bwN_D_A.flush();
            bwN_D_A.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManipulaCSV.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(ManipulaCSV.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return new_NAO_D_A;
    }

    private void geraDM_NDM(File naoDup) {
        vp = 0;
        fp = 0;

        String Str = "";
        String elemento1 = "";
        String elemento2 = "";
        String[] TableLine;
        boolean existe = false;

        FileWriter naoDuplicatas;

        File duplicatasManual = new File("./src/csv/", "D_M.csv");
        FileWriter dupM;
        File naoDuplicatasManual = new File("./src/csv/", "ND_M.csv");

        FileWriter naoDupM;

        if (!duplicatasManual.exists()) {
            System.out.println("Não existe arquivo duplicatasManual.csv.");

            try {
                duplicatasManual.createNewFile();

            } catch (FileNotFoundException ex) {

                Logger.getLogger(ManipulaCSV.class
                        .getName()).log(Level.SEVERE, null, ex);

            } catch (IOException ex) {
                Logger.getLogger(ManipulaCSV.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        }

        if (!naoDuplicatasManual.exists()) {
            System.out.println("Não existe arquivo duplicatasManual.csv.");

            try {
                naoDuplicatasManual.createNewFile();

            } catch (FileNotFoundException ex) {

                Logger.getLogger(ManipulaCSV.class
                        .getName()).log(Level.SEVERE, null, ex);

            } catch (IOException ex) {
                Logger.getLogger(ManipulaCSV.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        }

        try {
            BufferedReader brNaoDup = new BufferedReader(new FileReader(naoDup.getPath()));

            dupM = new FileWriter(duplicatasManual);
            BufferedWriter bwDup = new BufferedWriter(dupM);

            naoDupM = new FileWriter(naoDuplicatasManual);
            BufferedWriter bwNaoDUp = new BufferedWriter(naoDupM);

            while ((Str = brNaoDup.readLine()) != null) {

                TableLine = Str.split(";", 2);

                elemento1 = TableLine[0];
                elemento2 = TableLine[1];

//                System.out.println("e1: " + elemento1 + " e2: " + elemento2);
                existe = buscaGabarito(elemento1, elemento2, gs);

                if (existe) {
//                    System.out.println("Existe! vp++");
                    bwDup.write(elemento1 + ";" + elemento2 + "\n");
//                    vp++;
                } else {
//                    fp++;
                    bwNaoDUp.write(elemento1 + ";" + elemento2 + "\n");
                }

            }

            brNaoDup.close();

            bwDup.flush();
            bwDup.close();

            bwNaoDUp.flush();
            bwNaoDUp.close();

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
