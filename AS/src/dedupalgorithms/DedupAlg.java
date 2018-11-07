/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dedupalgorithms;

import dude.algorithm.Algorithm;
import dude.algorithm.duplicatedetection.NaiveDuplicateDetection;
import dude.algorithm.recordlinkage.NaiveRecordLinkage;
import dude.datasource.CSVSource;
import dude.datasource.XMLSource;
import dude.util.GoldStandard;
import dude.util.data.DuDeObjectPair;
import experimentos.ExperimentosCDs1;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diego
 */
public class DedupAlg {

    private String result; //O modificador protected permite que as subclasses tenham acesso aos membros da superclasse
    private String baseDados1;
    private String baseDados2;
    private String chavePrimaria;
    private String chavePrimaria1;
    private String chavePrimaria2;
    private String gold;
    private String goldId1;
    private String goldId2;
    private String idBaseDados;
    private char separator;

    private Algorithm algorithm;
    private CSVSource source1;
    private CSVSource source2;
    private XMLSource sourceXML1;
    private GoldStandard goldStandard;

    String dir = null;

    FileWriter escreveResult;

    File estatisticasCSV;
    File estatisticasTXT;

    int ordem = 0;

    public DedupAlg() {

    }

    /**
     * Construtor utilizado para quando o projeto de herança não estava bem
     * definido. Pode ser utilizado com cd.
     *
     * @param baseDados1
     * @param chavePrimaria
     * @param gold
     * @param goldId1
     * @param goldId2
     * @param separator
     */
    @Deprecated
    public DedupAlg(String baseDados1, String chavePrimaria, String gold, String goldId1, String goldId2, char separator) {
//    public DedupAlg(String baseDados1, String gold, String goldId1, String goldId2, String result) {

        this.result = baseDados1;
        this.baseDados1 = baseDados1;
        this.chavePrimaria = chavePrimaria;
        this.gold = gold;
        this.goldId1 = goldId1;
        this.goldId2 = goldId2;
        this.idBaseDados = idBaseDados;
        this.separator = separator;
        deduplication(); //Não era final antes
    }
    
        /**
     * Construtor utilizado para quando o projeto de herança não estava bem
     * definido. Pode ser utilizado com cd.
     *
     * @param baseDados1
     * @param chavePrimaria
     * @param gold
     * @param goldId1
     * @param goldId2
     * @param separator
     */
    @Deprecated
    public DedupAlg(String baseDados1, String chavePrimaria, String gold, String goldId1, String goldId2, char separator, String xml) {
//    public DedupAlg(String baseDados1, String gold, String goldId1, String goldId2, String result) {

        this.result = baseDados1;
        this.baseDados1 = baseDados1;
        this.chavePrimaria = chavePrimaria;
        this.gold = gold;
        this.goldId1 = goldId1;
        this.goldId2 = goldId2;
        this.idBaseDados = idBaseDados;
        this.separator = separator;
        deduplicationXML(); //Não era final antes
    }


    /**
     * Construtor utilizado para quando o projeto de herança não estava bem
     * definido. Pode ser utilizado com dblp-acm.
     *
     * @param baseDados1
     * @param baseDados2
     * @param chavePrimaria1
     * @param chavePrimaria2
     * @param gold
     * @param goldId1
     * @param goldId2
     * @param separator
     */
    public DedupAlg(String baseDados1, String baseDados2, String chavePrimaria1, String chavePrimaria2, String gold, String goldId1, String goldId2, char separator) {
//    public DedupAlg(String baseDados1, String baseDados2, String gold, String goldId1, String goldId2, String result) {

        this.baseDados1 = baseDados1;
        this.baseDados2 = baseDados2;
        this.chavePrimaria1 = chavePrimaria1;
        this.chavePrimaria2 = chavePrimaria2;
        this.gold = gold;
        this.goldId1 = goldId1;
        this.goldId2 = goldId2;
        this.idBaseDados = idBaseDados;
        this.result = baseDados1 + baseDados2;
        this.separator = separator;
        System.out.println("baseDados1: " + baseDados1);
        System.out.println("baseDados2: " + baseDados2);
        System.out.println("chavePrimaria1" + chavePrimaria1);
        System.out.println("chavePrimaria2" + chavePrimaria2);
        System.out.println("gold" + gold);
        System.out.println("goldId1" + goldId1);
        System.out.println("goldId2" + goldId2);
        System.out.println("separator" + separator);
        
        recordLinkage();
    }

    /**
     * Construtor com projeto de herança reformulado.
     *
     * @param baseDados1
     * @param chavePrimaria
     * @param gold
     * @param goldId1
     * @param goldId2
     * @param separator
     * @param dir
     * @param ordem
     */
    public DedupAlg(String baseDados1, String chavePrimaria, String gold, String goldId1, String goldId2, char separator, String dir, int ordem) {
//    public DedupAlg(String baseDados1, String gold, String goldId1, String goldId2, String result) {

        this.result = baseDados1;
        this.baseDados1 = baseDados1;
        this.chavePrimaria = chavePrimaria;
        this.gold = gold;
        this.goldId1 = goldId1;
        this.goldId2 = goldId2;
        this.idBaseDados = idBaseDados;
        this.separator = separator;
        this.ordem = ordem;
        this.setUpDiretorios(dir); //Não era final antes
        deduplication();
    }

    /**
     * Construtor utilizado para quando o projeto de herança não estava bem
     * definido. Pode ser utilizado com dblp-acm.
     *
     * @param baseDados1
     * @param baseDados2
     * @param chavePrimaria1
     * @param chavePrimaria2
     * @param gold
     * @param goldId1
     * @param goldId2
     * @param separator
     */
    @Deprecated
    public DedupAlg(String baseDados1, String baseDados2, String chavePrimaria1, String chavePrimaria2, String gold, String goldId1, String goldId2, char separator, String dir, int ordem) {
//    public DedupAlg(String baseDados1, String baseDados2, String gold, String goldId1, String goldId2, String result) {

        System.out.println("baseDados2= " + baseDados2);
        System.out.println("Entrei no 2");

        this.baseDados1 = baseDados1;
        this.baseDados2 = baseDados2;
        this.chavePrimaria1 = chavePrimaria1;
        this.chavePrimaria2 = chavePrimaria2;
        this.gold = gold;
        this.goldId1 = goldId1;
        this.goldId2 = goldId2;
        this.idBaseDados = idBaseDados;
        this.result = baseDados1 + baseDados2;
        this.ordem = ordem;
        this.setUpDiretorios(dir); //Não era final antes
        recordLinkage();
    }

    /**
     *
     */
    public final void deduplication() {
//        String literalGS = baseDados1;
        try {
            source1 = new CSVSource(baseDados1, new File("./src/csv/datasets", baseDados1 + ".csv"));

            //Vejamos se funcionam essas 2 linhas:
            source1.withQuoteCharacter('"');
//            source1.withSeparatorCharacter(';');
            source1.withSeparatorCharacter(getSeparator());

            source1.enableHeader();

            source1.addIdAttributes(chavePrimaria);

            File file = new File("./src/csv/datasets", baseDados1 + ".csv");
            System.out.println(file.getAbsolutePath());
            System.out.println(file.getPath());
            System.out.println(file.getName());

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DedupAlg.class.getName()).log(Level.SEVERE, null, ex);
        }

        algorithm = new NaiveDuplicateDetection();

        algorithm.addDataSource(source1);

        setGoldStandard(baseDados1);

    }
    
    /**
     *
     */
    public final void deduplicationXML() {
//        String literalGS = baseDados1;
        try {
            sourceXML1 = new XMLSource(baseDados1, new File("./src/csv/datasets", baseDados1 + ".xml"), baseDados1);

            //Vejamos se funcionam essas 2 linhas:
//            source1.withQuoteCharacter('"');
////            source1.withSeparatorCharacter(';');
//            source1.withSeparatorCharacter(getSeparator());
//
//            source1.enableHeader();

            sourceXML1.addIdAttributes(chavePrimaria);

            File file = new File("./src/csv/datasets", baseDados1 + ".xml");
            System.out.println(file.getAbsolutePath());
            System.out.println(file.getPath());
            System.out.println(file.getName());

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DedupAlg.class.getName()).log(Level.SEVERE, null, ex);
        }

        algorithm = new NaiveDuplicateDetection();

        algorithm.addDataSource(sourceXML1);

        setGoldStandard(baseDados1);

    }

    /**
     *
     */
    final void recordLinkage() {
//        String literalGS = baseDados1 + baseDados2;
        try {

            source1 = new CSVSource(baseDados1, new File("./src/csv/datasets", baseDados1 + ".csv"));
            source2 = new CSVSource(baseDados2, new File("./src/csv/datasets", baseDados2 + ".csv"));
//            source2 = new CSVSource(baseDados1, new File("./src/csv/datasets", baseDados1 + ".csv"));
//            source1 = new CSVSource(baseDados2, new File("./src/csv/datasets", baseDados2 + ".csv"));

            source1.withQuoteCharacter('"');
            source2.withQuoteCharacter('"');

            source1.withSeparatorCharacter(getSeparator());
            source2.withSeparatorCharacter(getSeparator());

            source1.enableHeader();
            source2.enableHeader();

            source1.addIdAttributes(chavePrimaria1);
            source2.addIdAttributes(chavePrimaria2);

//            source1.addIdAttributes(chavePrimaria2);
//            source2.addIdAttributes(chavePrimaria1);
            File file = new File("./src/csv/datasets", baseDados1 + ".csv");
            System.out.println(file.getAbsolutePath());
            System.out.println(file.getPath());
            System.out.println(file.getName());

            File file2 = new File("./src/csv/datasets", baseDados2 + ".csv");
            System.out.println(file2.getAbsolutePath());
            System.out.println(file2.getPath());
            System.out.println(file2.getName());

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DedupAlg.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        algorithm = new NaiveRecordLinkage();

        algorithm.addDataSource(source1);
        algorithm.addDataSource(source2);

        setGoldStandard(baseDados1, baseDados2);

    }

    /**
     *
     * @param literalGS
     */
    public void setGoldStandard(String literalGS) {
        CSVSource goldStandardSource = null;
        try {
            goldStandardSource = new CSVSource("goldstandard", new File("./src/csv/datasets", gold + ".csv"));
            goldStandardSource.enableHeader();

            File file = new File("./src/csv/datasets", gold + ".csv");
            System.out.println(file.getAbsolutePath());
            System.out.println(file.getPath());
            System.out.println(file.getName());

            goldStandardSource.enableHeader();

            goldStandardSource.withQuoteCharacter('"');
            goldStandardSource.withSeparatorCharacter(getSeparator());
//            goldStandardSource.withSeparatorCharacter(';');

            goldStandard = new GoldStandard(goldStandardSource);

//            goldStandard.setFirstElementsObjectIdAttributes(getGoldId1());
//            goldStandard.setSecondElementsObjectIdAttributes(getGoldId2());

            goldStandard.setFirstElementsObjectIdAttributes("id1");
            goldStandard.setSecondElementsObjectIdAttributes("id2");
            
            goldStandard.setSourceIdLiteral(literalGS);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DedupAlg.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    

    //Para record linkage
    /**
     *
     * @param literalGS1
     * @param literalGS2
     */
    public void setGoldStandard(String literalGS1, String literalGS2) {
        CSVSource goldStandardSource = null;
        try {
            goldStandardSource = new CSVSource("goldstandard", new File("./src/csv/datasets", gold + ".csv"));
            goldStandardSource.enableHeader();

            File file = new File("./src/csv/datasets", gold + ".csv");
            System.out.println(file.getAbsolutePath());
            System.out.println(file.getPath());
            System.out.println(file.getName());

            goldStandardSource.enableHeader();

            goldStandardSource.withQuoteCharacter('"');
            goldStandardSource.withSeparatorCharacter(getSeparator());
//            goldStandardSource.withSeparatorCharacter(';');

            goldStandard = new GoldStandard(goldStandardSource);

            goldStandard.setFirstElementsObjectIdAttributes(getGoldId1());
            goldStandard.setSecondElementsObjectIdAttributes(getGoldId2());

//            goldStandard.setSourceIdLiteral(literalGS);
            goldStandard.setFirstElementsSourceIdLiteral(literalGS1);
            goldStandard.setSecondElementsSourceIdLiteral(literalGS2);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DedupAlg.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @param par
     * @return
     */
    public boolean existeNoGS(DuDeObjectPair par) {
        return getGS().contains(par);
    }

    /**
     *
     * @return
     */
    public char getSeparator() {
        return separator;
    }

    /**
     *
     * @return
     */
    public Algorithm getAlg() {
        return algorithm;
    }

    /**
     *
     * @return
     */
    public GoldStandard getGS() {
        return goldStandard;
    }

    /**
     *
     * @return
     */
    public String getResult() {
        return result;
    }

    /**
     *
     * @return
     */
    public String getGold() {
        return gold;
    }

    /**
     *
     * @return
     */
    public String getGoldId1() {
        System.out.println("goldId1: " + goldId1);
        return goldId1;
    }

    /**
     *
     * @return
     */
    public String getGoldId2() {
        System.out.println("goldId2: " + goldId2);
        return goldId2;
    }

    /**
     *
     * @return
     */
    public String getIdBaseDados() {
        return idBaseDados;
    }

    /**
     *
     * @throws Exception
     */
    public void executaDedupAlg() throws Exception {

    }

    /**
     *
     * @return
     */
    public int getOrdem() {
        return ordem;
    }

    /**
     *
     * @param ordem
     */
    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    /**
     *
     * @return
     */
    public FileWriter getEscreveResult() {
        return escreveResult;
    }

    /**
     *
     * @param escreveResult
     */
    public void setEscreveResult(FileWriter escreveResult) {
        this.escreveResult = escreveResult;
    }

    /**
     *
     * @return
     */
    public String getDir() {
        return dir;
    }

    /**
     *
     * @return
     */
    public File getEstatisticasCSV() {
        return estatisticasCSV;
    }

    /**
     *
     * @param estatisticasCSV
     */
    public void setEstatisticasCSV(File estatisticasCSV) {
        this.estatisticasCSV = estatisticasCSV;
    }

    /**
     *
     * @param dir
     */
    public void setDir(String dir) {
        this.dir = dir;
    }

    /**
     *
     * @return
     */
    public File getEstatisticasTXT() {
        return estatisticasTXT;
    }

    /**
     *
     * @param estatisticasTXT
     */
    public void setEstatisticasTXT(File estatisticasTXT) {
        this.estatisticasTXT = estatisticasTXT;
    }

    /**
     *
     * @param dir
     */
    final void setUpDiretorios(String dir) {

        this.dir = dir;
        this.estatisticasCSV = new File("./src/csv/" + getDir() + "/estatisticas", "estatisticasDedup" + getOrdem() + ".csv");
        this.estatisticasTXT = new File("./src/csv/" + getDir() + "/estatisticas", "estatisticasDedup" + getOrdem() + ".txt");
//        estatisticasCSV = new File("./src/csv/resultsDedup/estatisticas", "estatisticasDedup" + ordem + ".csv");
//        estatisticasTXT = new File("./src/csv/resultsDedup/estatisticas", "estatisticasDedup" + ordem + ".txt");
        if (this.estatisticasTXT.exists() | this.estatisticasCSV.exists()) {
            System.out.println("Já existem resultados para esse algoritmo!");
            java.awt.Toolkit.getDefaultToolkit().beep();
            System.exit(0);
        }
        try {
            this.escreveResult = new FileWriter(new File("./src/csv/" + getDir(), "resultado" + getOrdem() + ".csv"));
            System.out.println("Chamou escreveResult;");
//            this.escreveResult = new FileWriter(new File("./src/csv/resultsDedup", "resultado" + ordem + ".csv"));

        } catch (IOException ex) {
            Logger.getLogger(DedupAlg.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setAllVarDedup(String baseDados1, String chavePrimaria, String gold, String goldId1, String goldId2, char separator) {
        this.baseDados1 = baseDados1;
        this.chavePrimaria = chavePrimaria;
        this.gold = gold;
        this.goldId1 = goldId1;
        this.goldId2 = goldId2;
        this.separator = separator;
        deduplication(); //Não era final antes
    }

    public void setAllVarDedup(String baseDados1, String baseDados2, String chavePrimaria1, String chavePrimaria2, String gold, String goldId1, String goldId2, char separator) {

        this.baseDados1 = baseDados1;
        this.baseDados2 = baseDados2;
        this.chavePrimaria1 = chavePrimaria1;
        this.chavePrimaria2 = chavePrimaria2;
        this.gold = gold;
        this.goldId1 = goldId1;
        this.goldId2 = goldId2;
        this.separator = separator;
        
        System.out.println("baseDados1: " + baseDados1);
        System.out.println("baseDados2: " + baseDados2);
        System.out.println("chavePrimaria1: " + chavePrimaria1);
        System.out.println("chavePrimaria2: " + chavePrimaria2);
        System.out.println("gold: " + gold);
        System.out.println("goldId1: " + goldId1);
        System.out.println("goldId2: " + goldId2);
        System.out.println("separator: " + separator);
        recordLinkage();
    }

}
