/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experimentos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diego
 */
public class GeradoraGabarito {

    public void geraGab(File base) throws IOException {

        File gabarito = new File(base.getParent(), base.getName() + "_gold.csv");
        FileWriter escreveGabarito;

        String str;
        String[] linhaAtual;

        if (!gabarito.exists()) {
            System.out.println("Não existe arquivo " + gabarito.getName());
            try {
                gabarito.createNewFile();
                //new Thread().sleep(50);

            } catch (FileNotFoundException ex) {

                System.out.println("Não foi possível encontrar o arquivo " + gabarito.getName());
            } catch (IOException ex) {
                System.out.println("Não foi possível criar o arquivo " + gabarito.getName());
            }
        }

        //concatenação
        BufferedReader brBase = null;
        BufferedWriter bwGabarito = null;
        try {

            brBase = new BufferedReader(new FileReader(base.getPath()));
            escreveGabarito = new FileWriter(gabarito);
            bwGabarito = new BufferedWriter(escreveGabarito);
            String originalAtual = null;

            //Copiando do primeiro arquivo
            while ((str = brBase.readLine()) != null) {

                linhaAtual = str.split(",", 3);

                if (linhaAtual[0].equals("rec_id")) {
                    bwGabarito.write("rec_id1" + "," + "rec_id2");
                    bwGabarito.newLine();

                    continue;
                } else {

                    if (linhaAtual[0].contains("org")) {
                        originalAtual = linhaAtual[0];
                    } else {
                        bwGabarito.write(originalAtual + "," + linhaAtual[0]);
                        bwGabarito.newLine();
                        bwGabarito.flush();
                    }
                }

            }

        } catch (FileNotFoundException ex) {

            Logger.getLogger(GeradoraGabarito.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GeradoraGabarito.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            brBase.close();
            bwGabarito.flush();
            bwGabarito.close();

        }

    }

    public static void main(String[] args) throws IOException {
        GeradoraGabarito obj = new GeradoraGabarito();
        File base = new File("c:/Users/Diego/Desktop/mydata.csv");
        obj.geraGab(base);
    }

}
