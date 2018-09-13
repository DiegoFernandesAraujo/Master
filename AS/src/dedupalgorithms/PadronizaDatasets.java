/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dedupalgorithms;

import AS.AnnStd;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diego
 */
public class PadronizaDatasets {

    public Map<String, String> geraMapa(File arq) throws IOException {

        Map<String, String> mapaPadr = new HashMap<String, String>();

        BufferedReader brArq = null;
        FileWriter escreveArqPadr = null;
        BufferedWriter bwArqPadr = null;
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
             System.out.println("nome: " + nome);
            nome = nome.replace("_ids", "");
            System.out.println("nome: " + nome);
            nome = nome.substring(0, nome.indexOf('.'));
            
            escreveArqPadr = new FileWriter(diretorio + "/" + nome + "_NEW.csv", false);
            bwArqPadr = new BufferedWriter(escreveArqPadr);

            //Isso aqui ajeita depois
//            escreveArqPadr = new FileWriter(diretorio + "/" + nome + "_NEW.csv", false);
            while ((Str = brArq.readLine()) != null) {

                linhaAtual = Str.split(",", 3);

//                System.out.println(linhaAtual[0] + " - " + linhaAtual[1] + " - " + linhaAtual[2]);

                if (cont == 0) {
//                    System.out.println(Str);
                    bwArqPadr.append(linhaAtual[0]);
                    bwArqPadr.append(',');
                    bwArqPadr.append(linhaAtual[2]);
                    bwArqPadr.append('\n');
                } else {
                    //Nesse caso considera apenas as duas primeiras colunas (as que interessam)

                    //O id anterior textual é a chave e o id numérico é o valor.
                    mapaPadr.put(linhaAtual[1], linhaAtual[0]);
//                System.out.println(linhaAtual[1] + " - " + linhaAtual[0]);

                    bwArqPadr.append(linhaAtual[0]);
                    bwArqPadr.append(',');
                    bwArqPadr.append(linhaAtual[2]);
                    bwArqPadr.append('\n');

                }
                cont++;
                bwArqPadr.flush();
            }

//            for (Map.Entry<String, String> par : mapaPadr.entrySet()) {
//                System.out.println(par.getKey() + " - "+ par.getValue());
//            }
//            System.out.println("Tamanho do mapa: " + mapaPadr.size());
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            escreveArqPadr.flush();
            escreveArqPadr.close();
            brArq.close();
        }
        return mapaPadr;
    }

    public File geraIds(File arq) throws IOException {

        BufferedReader brArq = null;
        FileWriter escreveArqPadr = null;
        BufferedWriter bwArqPadr = null;
        File arqPadr = null;
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
            arqPadr = new File(diretorio + "/" + nome + "_ids.csv");

            escreveArqPadr = new FileWriter(arqPadr, false);
            bwArqPadr = new BufferedWriter(escreveArqPadr);

            //Isso aqui ajeita depois
//            escreveArqPadr = new FileWriter(diretorio + "/" + nome + "_NEW.csv", false);
            while ((Str = brArq.readLine()) != null) {

                linhaAtual = Str.split(",", 3);

                if (cont == 0) {
//                    System.out.println(Str);
                    bwArqPadr.append("num");
                    bwArqPadr.append(',');
                    bwArqPadr.append(Str);
                    bwArqPadr.append('\n');
                } else {
                    //Nesse caso considera apenas as duas primeiras colunas (as que interessam)

                    bwArqPadr.append(Integer.toString(cont));
                    bwArqPadr.append(',');
                    bwArqPadr.append(Str);
                    bwArqPadr.append('\n');

                }
                cont++;
                bwArqPadr.flush();
            }

//            for (Map.Entry<String, String> par : mapaPadr.entrySet()) {
//                System.out.println(par.getKey() + " - "+ par.getValue());
//            }
//            System.out.println("Tamanho do mapa: " + mapaPadr.size());
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            escreveArqPadr.flush();
            escreveArqPadr.close();
            brArq.close();
        }
        return arqPadr;
    }

    public void padronizaGS(Map<String, String> mapa, File gs) throws IOException {

        BufferedReader brArq = null;
        FileWriter escreveNewGS = null;
        BufferedWriter bwNewGS = null;
        int cont = 0;

        try {

            String diretorio = gs.getParent();
            String nome = gs.getName();
            System.out.println("nome: " + nome);
            nome = nome.replace("_ids", "");
            System.out.println("nome: " + nome);
            nome = nome.substring(0, nome.indexOf('.'));
            System.out.println("nome: " + nome);

            escreveNewGS = new FileWriter(diretorio + "/" + nome + "_NEW.csv", false);
            bwNewGS = new BufferedWriter(escreveNewGS);
            brArq = new BufferedReader(new FileReader(gs.getPath()));

//            System.out.println(gs.getPath());
            String Str = "";
            String[] linhaAtual;

            while ((Str = brArq.readLine()) != null) {

                Str = Str.replace("\"", "");

                linhaAtual = Str.split(",", 2); //Nesse caso considera apenas as duas primeiras colunas (as que interessam)

                if (cont == 0) {
                    bwNewGS.append(linhaAtual[0]);
                    bwNewGS.append(',');
                    bwNewGS.append(linhaAtual[1]);
                    bwNewGS.append('\n');
                } else {

//                linhaAtual[0] = linhaAtual[0].replace("\"","");
//                
//                System.out.print("Str: " + Str + "-->");
//                System.out.println(linhaAtual[0] + " - " + linhaAtual[1]);
                    //O id anterior textual é a chave e o id numérico é o valor.
//                System.out.println("linhaAtual[0]: " + linhaAtual[0]);
                    if (mapa.containsKey(linhaAtual[0])) {

//                        System.out.println(mapa.get(linhaAtual[0]) + " - " + linhaAtual[1]);
                        bwNewGS.append(mapa.get(linhaAtual[0]));
                        bwNewGS.append(',');
                        bwNewGS.append(linhaAtual[1]);
                        bwNewGS.append('\n');

                    }
                }
                cont++;
                bwNewGS.flush();
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            escreveNewGS.flush();
            escreveNewGS.close();
            brArq.close();
        }

    }

    public static void main(String[] args) throws IOException {
        PadronizaDatasets obj = new PadronizaDatasets();
        File dblp2Origin = new File("./src/csv/datasets/gera", "DBLP2.csv");
        File dblp2 = obj.geraIds(dblp2Origin);
        File gs = new File("./src/csv/datasets/gera", "DBLP2-ACM_perfectMapping.csv");
        obj.padronizaGS(obj.geraMapa(dblp2), gs);

    }

}
