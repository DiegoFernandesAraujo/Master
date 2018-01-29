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
    File D_A;
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
                    StrW.write("Precision;Recall;F1;VP;FP;Iteração\n");
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
    
    public File padronizaCsvFile(File arquivo) throws IOException {

        File returnCSV = null;

        
        try {
            BufferedReader arquivoCSV = new BufferedReader(new FileReader(arquivo.getPath()));

            String Str = "";
            String Str2 = "";
            int index_c1 = 0;
            int index_c2 = 0;
            int cont = 0;
            String[] TableLine;

            String diretorio = arquivo.getParent();
            String nome = arquivo.getName();
            nome = nome.substring(0, nome.indexOf('.'));
            
            if(arquivo.exists()){
                System.out.println(arquivo.getName() + " existe!");
            }
//            arquivo.
//            arquivo.createNewFile();
//            
            FileWriter newCSV = new FileWriter(diretorio + "\\" + nome + "_NEW.csv", false);
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
//                System.out.println("TableLine[0] = " + TableLine[0] + " TableLine[1] = " + TableLine[1]);
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
    
    public void comparaConjuntos(File arqResult) {
        
        D_A = new File("./src/csv/", "D_A.csv");
        
        if (!D_A.exists()) {

            try {
                estat.createNewFile();
                D_A = arqResult;
//                    estatisticas = new FileWriter(estat, true);
//                    BufferedWriter StrW = new BufferedWriter(estatisticas);

//                    StrW.write("Precision;Recall;F1;VP;FP;Iteração\n");
//                    StrW.flush();
//                    StrW.close();
            } catch (IOException ex) {
                System.out.println("Não foi possível criar arquivo D_A.csv.");
            }
            comparaComGS(D_A);
        }else{
            //TODO
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

                StrW.append(Double.toString(precision));
                StrW.append(";");
                StrW.append(Double.toString(recall));
                StrW.append(";");
                StrW.append(Double.toString(f1));
                StrW.append(";");
                StrW.append(Integer.toString(vp));
                StrW.append(";");
                StrW.append(Integer.toString(fp));
                StrW.append(";");
                StrW.append(Integer.toString(iteracao));
                StrW.append("\n");
//                StrW.write(Double.toString(precision) + ";" + Double.toString(recall) + ";" + Double.toString(f1) + ";" + Double.toString(iteracao) + "\n");
//                estatisticas.flush();
//                estatisticas.close();
                StrW.flush();
                StrW.close();
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
    
    public void fechaExecucao(){
//        TODO
    }
    
    public int getIteracao(){
        return iteracao;
    }
    
    public void setGs(File gs){
        this.gs = gs;
    }
    
}
