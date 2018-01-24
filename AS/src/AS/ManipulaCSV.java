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

/**
 *
 * @author Diego
 */
public class ManipulaCSV {

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

    public void padronizaCsvFile(File arquivo) throws IOException {
        
        try {
            BufferedReader arquivoCSV = new BufferedReader(new FileReader(arquivo.getPath()));

            String Str, Str2 = "";
            int index_c1, index_c2;
            String[] TableLine;

//            System.out.println("arquivo.getParent() + arquivo.getName() + \"NEW.csv\" = " + arquivo.getParent() + " " + arquivo.getName() + "NEW.csv");
            
            int cont;
            String diretorio = arquivo.getParent();
            String nome = arquivo.getName();
            nome = nome.substring(0, nome.indexOf('.'));

            FileWriter newCSV = new FileWriter(diretorio + "\\" + nome + "_NEW.csv");

            //Essa estrutura do looping while é clássica para ler cada linha
            //do arquivo
            while ((Str = arquivoCSV.readLine()) != null) {
                //Aqui usamos o método split que divide a linha lida em um array de String
                //passando como parametro o divisor ";".
//                    TableLine = Str.split(";");
                TableLine = Str.split(";", 2); //Nesse caso considera apenas as duas primeiras colunas (as que interessam)
                cont = 0;

                //O foreach é usadao para imprimir cada célula do array de String.
                for (String cell : TableLine) {
                    cont++;
//                        System.out.print(cell + " ");

                    index_c1 = cell.indexOf('[');
                    index_c2 = cell.indexOf(']');

//                        System.out.println("c1 = " + index_c1 + " c2 = " + index_c2);
                    Str2 = cell.substring(index_c1 + 1, index_c2);
                    
//                    System.out.print(Str2 + " ");

                    newCSV.append(Str2);
                    
                    if (cont == 1) {
                        newCSV.append(';');
                    } else {
                        newCSV.append('\n');
                    }
                    
                    
                }
//                    System.out.println("cont = " + cont);
//                newCSV.append('\n');
//                System.out.println("\n");
            }
            
            newCSV.flush();
            newCSV.close();
            //Fechamos o buffer
            arquivoCSV.close();

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

}
