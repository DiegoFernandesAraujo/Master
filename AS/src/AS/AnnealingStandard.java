/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AS;

import java.io.IOException;

/**
 *
 * @author Diego
 */
public class AnnealingStandard {
    public static void main(String [] args) throws IOException{
        ManipulaCSV obj = new ManipulaCSV();
        obj.readCsvFile();
    }
    
}
