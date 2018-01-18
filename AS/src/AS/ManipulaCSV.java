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

/**
 *
 * @author Diego
 */
public class ManipulaCSV {

    public void readCsvFile() throws java.io.IOException {

        //A estrutura try-catch é usada pois o objeto BufferedWriter exige que as
        //excessões sejam tratadas
        try {

            try ( //Criação de um buffer para a ler de uma stream
//            BufferedReader StrR = new BufferedReader(new FileReader("c:\\tabela.csv"));
                    
              //NÃO FUNCIONA!
              BufferedReader StrR = new BufferedReader(new FileReader("./csv/cd.csv"))) { 
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
            }
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


}
