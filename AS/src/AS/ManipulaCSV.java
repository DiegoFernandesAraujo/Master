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

/**
 *
 * @author Diego
 */
public class ManipulaCSV {

    int vp, fp, iteracao;
    File estat;
    FileWriter estatisticas;

    public ManipulaCSV() {

        estat = new File("./src/csv/", "estatisticas.csv");

        if (!estat.exists()) {
            System.out.println("Não existe arquivo estatisticas.csv.");

            try {
                estat.createNewFile();
                try {
                    estatisticas = new FileWriter(estat, true);
                    BufferedWriter StrW = new BufferedWriter(estatisticas);

//            StrW.append("Precision");
//            StrW.append(";");
//            StrW.append("Recall");
//            StrW.append(";");
//            StrW.append("F1");
//            StrW.append(";");
//            StrW.append("Iteracao");
//            StrW.append("\n");
                    StrW.write("Precision;Recall;F1;Iteração\n");
                    StrW.flush();
                    StrW.close();

                } catch (IOException ex) {
                    System.out.println("Não foi possível escrever o cabeçalho no arquivo estatisticas.csv.");
                }

            } catch (IOException ex) {
                System.out.println("Não foi possível criar arquivo estatisticas.csv.");
            }
        }

    }

    public void readCsvFile() throws java.io.IOException {

        //A estrutura try-catch é usada pois o objeto BufferedWriter exige que as
        //excessões sejam tratadas
        try {

            //Criação de um buffer para a ler de uma stream
            BufferedReader StrR = new BufferedReader(new FileReader("D:\\Pesquisa\\Desenvolvimento\\resultado1.csv"));

            //NÃO FUNCIONA!
//              BufferedReader StrR = new BufferedReader(new FileReader("./csv/cd.csv"));  
            System.out.println(StrR.readLine());
            String Str;
            String[] TableLine;
            //Essa estrutura do looping while é clássica para ler cada linha
            //do arquivo
            while ((Str = StrR.readLine()) != null) {
                //Aqui usamos o método split que divide a linha lida em um array de String
                //passando como parametro o divisor ";".
                TableLine = Str.split(";");

                //O foreach é usadao para imprimir cada célula do array de String.
                for (String cell : TableLine) {
                    System.out.print(cell + " ");
                }
                System.out.println("\n");
            }
            //Fechamos o buffer
            StrR.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void createCsvFile() throws java.io.IOException {
        //A estrutura try-catch é usada pois o objeto BufferedWriter exige que as
        //excessões sejam tratadas
        try {

            //Criação de um buffer para a escrita em uma stream
            BufferedWriter StrW = new BufferedWriter(new FileWriter("C:\\tabela.csv"));

            //Escrita dos dados da tabela
            StrW.write("Nome;Telefone;Idade\n");
            StrW.write("Juliana;6783-8490;23\n");
            StrW.write("Tatiana;6743-7480;45\n");
            StrW.write("Janice;6909-9380;21");

            //Fechamos o buffer
            StrW.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public File padronizaCsvFile(File arquivo) throws IOException {

        File returnCSV = null;

        try {
            BufferedReader arquivoCSV = new BufferedReader(new FileReader(arquivo.getPath()));

            String Str, Str2 = "";
            int index_c1, index_c2, cont;
            String[] TableLine;

            String diretorio = arquivo.getParent();
            String nome = arquivo.getName();
            nome = nome.substring(0, nome.indexOf('.'));

            FileWriter newCSV = new FileWriter(diretorio + "\\" + nome + "_NEW.csv", true);
            returnCSV = new File(diretorio + "\\" + nome + "_NEW.csv");

            //Essa estrutura do looping while é clássica para ler cada linha
            //do arquivo
            while ((Str = arquivoCSV.readLine()) != null) {

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

                //O foreach é usado para imprimir cada célula do array de String.
                for (String cell : TableLine) {

                    cont++;

                    index_c1 = cell.indexOf('[');
                    index_c2 = cell.indexOf(']');

                    Str2 = cell.substring(index_c1 + 1, index_c2);

                    newCSV.append(Str2);

                    if (cont == 1) {
                        newCSV.append(';');
                    } else {
                        newCSV.append('\n');
                    }

                }
//                System.out.println("Comprimento do array = " + TableLine.length);
            }

            newCSV.flush();
            newCSV.close();
            //Fechamos o buffer
            arquivoCSV.close();

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return returnCSV;
    }

    public void comparaComGS(File arqResult, File gs) {
        //Já deve receber o arqResult padronizado
        vp = 0;
        fp = 0;

        String Str, elemento1, elemento2 = "";
        String[] TableLine;
        boolean existe = false;

        try {
            BufferedReader StrR = new BufferedReader(new FileReader(arqResult.getPath()));

            while ((Str = StrR.readLine()) != null) {

                TableLine = Str.split(";", 2);

                elemento1 = TableLine[0];
                elemento2 = TableLine[1];

                System.out.println("e1: " + elemento1 + " e2: " + elemento2);

                existe = buscaGabarito(elemento1, elemento2, gs);

                if (existe) {
//                    System.out.println("Existe! vp++");
                    vp++;
                } else {
                    fp++;
                }

            }

            StrR.close();
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
            BufferedReader StrR = new BufferedReader(new FileReader(gs.getPath()));

            while ((Str = StrR.readLine()) != null) {

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
            StrR.close();
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
                BufferedWriter StrW = new BufferedWriter(estatisticas);
//                System.out.println(estatisticas.);

//                StrW.append(Double.toString(precision));
//                StrW.append(";");
//                StrW.append(Double.toString(recall));
//                StrW.append(";");
//                StrW.append(Double.toString(f1));
//                StrW.append(";");
//                StrW.append(Double.toString(iteracao));
//                StrW.append("\n");
                StrW.write(Double.toString(precision) + ";" + Double.toString(recall) + ";" + Double.toString(f1) + ";" + Double.toString(iteracao) + "\n");
//                estatisticas.flush();
//                estatisticas.close();
                StrW.flush();
                StrW.close();
                iteracao++;

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

}
