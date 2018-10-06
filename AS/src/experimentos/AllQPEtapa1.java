/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experimentos;

import experimentos.Etapa1Experimento;

import java.io.File;
import java.io.IOException;

/**
 * Executa os experimentos para todas as questões de pesquisa.
 *
 * @author Diego
 */
public class AllQPEtapa1 {

    String gsGeral = null;
    String gsOne = null;
    String gsThree = null;
    String gsFive = null;
    String base = null;
    int qtdMaxGeral, qtdMaxB, qtdMaxM, qtdMaxR, qtdMaxAll, qtdMaxLot;
    int tamBase1Geral, tamBase1One, tamBase1Three, tamBase1Five, tamBase2Geral, tamBase2One, tamBase2Three, tamBase2Five;
    int qtdObs;
    int vQtdAlgB, vQtdAlgM, vQtdAlgR, vQtdAlgAll, vQtdAlgLot;
    int[] vQtdAlgGeral;
    boolean okqp1 = true;
    boolean okqp2b = true;
    boolean okqp2m = true;
    boolean okqp2r = true;
    boolean okqp3all = true;
    boolean okqp3lot = true;
    boolean okqp5one = true;
    boolean okqp5three = true;
    boolean okqp5five = true;

    /**
     * Construtor utilizado para realizar <b>todos</b> experimentos com deduplicação.
     * 
     * @param base
     * @param qtdMaxGeral
     * @param qtdMaxB
     * @param qtdMaxM
     * @param qtdMaxR
     * @param qtdMaxAll
     * @param qtdMaxLot
     * @param tamBase1Geral
     * @param tamBase1One
     * @param tamBase1Three
     * @param tamBaseFive
     * @param qtdObs
     * @param vQtdAlgB
     * @param vQtdAlgM
     * @param vQtdAlgR
     * @param vQtdAlgAll
     * @param vQtdAlgLot
     * @throws IOException
     * @throws InterruptedException
     */
    public AllQPEtapa1(String base, int qtdMaxGeral, int qtdMaxB, int qtdMaxM, int qtdMaxR, int qtdMaxAll, int qtdMaxLot, int tamBase1Geral, int tamBase1One, int tamBase1Three, int tamBaseFive, int qtdObs, int vQtdAlgB, int vQtdAlgM, int vQtdAlgR, int vQtdAlgAll, int vQtdAlgLot) throws IOException, InterruptedException {
        this.base = base;
        this.qtdMaxGeral = qtdMaxGeral;
        this.qtdMaxB = qtdMaxB;
        this.qtdMaxM = qtdMaxM;
        this.qtdMaxR = qtdMaxR;
        this.qtdMaxAll = qtdMaxAll;
        this.qtdMaxLot = qtdMaxLot;
        this.tamBase1Geral = tamBase1Geral;
        this.tamBase1One = tamBase1One;
        this.tamBase1Three = tamBase1Three;
        this.tamBase1Five = tamBaseFive;
        this.qtdObs = qtdObs;
        this.vQtdAlgB = vQtdAlgB;
        this.vQtdAlgM = vQtdAlgM;
        this.vQtdAlgR = vQtdAlgR;
        this.vQtdAlgAll = vQtdAlgAll;
        this.vQtdAlgLot = vQtdAlgLot;
        this.vQtdAlgGeral = new int[]{10, 15, 20};

    }

    /**
     * Construtor utilizado para realizar experimentos com deduplicação.
     *
     * @param base
     * @param qtdMaxGeral
     * @param qtdMaxB
     * @param qtdMaxM
     * @param qtdMaxR
     * @param qtdMaxAll
     * @param qtdMaxLot
     * @param tamBase1Geral
     * @param tamBase1One
     * @param tamBase1Three
     * @param tamBaseFive
     * @param qtdObs
     * @param vQtdAlgB
     * @param vQtdAlgM
     * @param vQtdAlgR
     * @param vQtdAlgAll
     * @param vQtdAlgLot
     * @param okqp1
     * @param okqp2b
     * @param okqp2m
     * @param okqp2r
     * @param okqp3all
     * @param okqp3lot
     * @param okqp5five
     * @param okqp5three
     * @param okqp5one
     * @throws IOException
     * @throws InterruptedException
     */
    public AllQPEtapa1(String base, int qtdMaxGeral, int qtdMaxB, int qtdMaxM, int qtdMaxR, int qtdMaxAll, int qtdMaxLot, int tamBase1Geral, int tamBase1One, int tamBase1Three, int tamBaseFive, int qtdObs, int vQtdAlgB, int vQtdAlgM, int vQtdAlgR, int vQtdAlgAll, int vQtdAlgLot,  boolean okqp1, boolean okqp2b, boolean okqp2m, boolean okqp2r, boolean okqp3all, boolean okqp3lot, boolean okqp5one, boolean okqp5three, boolean okqp5five) throws IOException, InterruptedException {
        this.base = base;
        this.qtdMaxGeral = qtdMaxGeral;
        this.qtdMaxB = qtdMaxB;
        this.qtdMaxM = qtdMaxM;
        this.qtdMaxR = qtdMaxR;
        this.qtdMaxAll = qtdMaxAll;
        this.qtdMaxLot = qtdMaxLot;
        this.tamBase1Geral = tamBase1Geral;
        this.tamBase1One = tamBase1One;
        this.tamBase1Three = tamBase1Three;
        this.tamBase1Five = tamBaseFive;
        this.qtdObs = qtdObs;
        this.vQtdAlgB = vQtdAlgB;
        this.vQtdAlgM = vQtdAlgM;
        this.vQtdAlgR = vQtdAlgR;
        this.vQtdAlgAll = vQtdAlgAll;
        this.vQtdAlgLot = vQtdAlgLot;
        this.vQtdAlgGeral = new int[]{10, 15, 20};

        this.okqp1 = okqp1;
        this.okqp2b = okqp2b;
        this.okqp2m = okqp2m;
        this.okqp2r = okqp2r;
        this.okqp3all = okqp3all;
        this.okqp3lot = okqp3lot;
        this.okqp5one = okqp5one;
        this.okqp5three = okqp5three;
        this.okqp5five = okqp5five;

    }

    /**
     * Construtor utilizado para realizar <b>todos</b> experimentos com <i>record
     * linkage</i>.
     *
     * @param base1
     * @param qtdMaxGeral
     * @param base2
     * @param qtdMaxB
     * @param qtdMaxM
     * @param qtdMaxR
     * @param qtdMaxAll
     * @param qtdMaxLot
     * @param tamBase1Geral
     * @param tamBase1One
     * @param tamBase1Three
     * @param tamBase1Five
     * @param tamBase2Geral
     * @param tamBase2One
     * @param tamBase2Three
     * @param tamBase2Five
     * @param qtdObs
     * @param vQtdAlgB
     * @param vQtdAlgM
     * @param vQtdAlgR
     * @param vQtdAlgAll
     * @param vQtdAlgLot
     * @throws IOException
     * @throws InterruptedException
     */
    public AllQPEtapa1(String base1, String base2, int qtdMaxGeral, int qtdMaxB, int qtdMaxM, int qtdMaxR, int qtdMaxAll, int qtdMaxLot, int tamBase1Geral, int tamBase1One, int tamBase1Three, int tamBase1Five, int tamBase2Geral, int tamBase2One, int tamBase2Three, int tamBase2Five, int qtdObs, int vQtdAlgB, int vQtdAlgM, int vQtdAlgR, int vQtdAlgAll, int vQtdAlgLot) throws IOException, InterruptedException {
        this.base = base1 + "-" + base2;
        this.qtdMaxGeral = qtdMaxGeral;
        this.qtdMaxB = qtdMaxB;
        this.qtdMaxM = qtdMaxM;
        this.qtdMaxR = qtdMaxR;
        this.qtdMaxAll = qtdMaxAll;
        this.qtdMaxLot = qtdMaxLot;
        this.tamBase1Geral = tamBase1Geral;
        this.tamBase1One = tamBase1One;
        this.tamBase1Three = tamBase1Three;
        this.tamBase1Five = tamBase1Five;
        this.tamBase2Geral = tamBase2Geral;
        this.tamBase2One = tamBase2One;
        this.tamBase2Three = tamBase2Three;
        this.tamBase2Five = tamBase2Five;
        this.qtdObs = qtdObs;
        this.vQtdAlgB = vQtdAlgB;
        this.vQtdAlgM = vQtdAlgM;
        this.vQtdAlgR = vQtdAlgR;
        this.vQtdAlgAll = vQtdAlgAll;
        this.vQtdAlgLot = vQtdAlgLot;
        this.vQtdAlgGeral = new int[]{10, 12, 20};

    }

    /**
     * Construtor utilizado para realizar experimentos com <i>record
     * linkage</i>.
     *
     * @param base1
     * @param qtdMaxGeral
     * @param base2
     * @param qtdMaxB
     * @param qtdMaxM
     * @param qtdMaxR
     * @param qtdMaxAll
     * @param qtdMaxLot
     * @param tamBase1Geral
     * @param tamBase1One
     * @param tamBase1Three
     * @param tamBase1Five
     * @param tamBase2Geral
     * @param tamBase2One
     * @param tamBase2Three
     * @param tamBase2Five
     * @param qtdObs
     * @param vQtdAlgB
     * @param vQtdAlgM
     * @param vQtdAlgR
     * @param vQtdAlgAll
     * @param vQtdAlgLot
     * @param okqp3all
     * @param okqp2b
     * @param okqp2m
     * @param okqp1
     * @param okqp5three
     * @param okqp5one
     * @param okqp2r
     * @param okqp3lot
     * @param okqp5five
     * @throws IOException
     * @throws InterruptedException
     */
    public AllQPEtapa1(String base1, String base2, int qtdMaxGeral, int qtdMaxB, int qtdMaxM, int qtdMaxR, int qtdMaxAll, int qtdMaxLot, int tamBase1Geral, int tamBase1One, int tamBase1Three, int tamBase1Five, int tamBase2Geral, int tamBase2One, int tamBase2Three, int tamBase2Five, int qtdObs, int vQtdAlgB, int vQtdAlgM, int vQtdAlgR, int vQtdAlgAll, int vQtdAlgLot,  boolean okqp1, boolean okqp2b, boolean okqp2m, boolean okqp2r, boolean okqp3all, boolean okqp3lot, boolean okqp5one, boolean okqp5three, boolean okqp5five) throws IOException, InterruptedException {
        this.base = base1 + "-" + base2;
        this.qtdMaxGeral = qtdMaxGeral;
        this.qtdMaxB = qtdMaxB;
        this.qtdMaxM = qtdMaxM;
        this.qtdMaxR = qtdMaxR;
        this.qtdMaxAll = qtdMaxAll;
        this.qtdMaxLot = qtdMaxLot;
        this.tamBase1Geral = tamBase1Geral;
        this.tamBase1One = tamBase1One;
        this.tamBase1Three = tamBase1Three;
        this.tamBase1Five = tamBase1Five;
        this.tamBase2Geral = tamBase2Geral;
        this.tamBase2One = tamBase2One;
        this.tamBase2Three = tamBase2Three;
        this.tamBase2Five = tamBase2Five;
        this.qtdObs = qtdObs;
        this.vQtdAlgB = vQtdAlgB;
        this.vQtdAlgM = vQtdAlgM;
        this.vQtdAlgR = vQtdAlgR;
        this.vQtdAlgAll = vQtdAlgAll;
        this.vQtdAlgLot = vQtdAlgLot;
        this.vQtdAlgGeral = new int[]{10, 12, 20};

        this.okqp1 = okqp1;
        this.okqp2b = okqp2b;
        this.okqp2m = okqp2m;
        this.okqp2r = okqp2r;
        this.okqp3all = okqp3all;
        this.okqp3lot = okqp3lot;
        this.okqp5one = okqp5one;
        this.okqp5three = okqp5three;
        this.okqp5five = okqp5five;

    }

    /**
     * Executa todos os experimentos para deduplicação.
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void rodaExpDedup() throws IOException, InterruptedException {

        //Para QP1
        if (okqp1) {
            int[] vetor = {10, 15, 20}; //Poderia passar direto nos argumentos assim: new int[]{10, 15, 20}
            Etapa1Experimento qp1 = new Etapa1Experimento(gsGeral, base, "QP1", qtdMaxGeral, tamBase1Geral, qtdObs, vQtdAlgGeral);
            qp1.executa();
        }

        //Para QP2  - Bons
        if (okqp2b) {
            int[] vetor2 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp2b = new Etapa1Experimento(gsGeral, base, "QP2B", qtdMaxB, tamBase1Geral, qtdObs, vQtdAlgB);
            qp2b.executa(); //Quando já se tem o arquivo com as listas sequenciais aleatórias de matchers
        }

        //Para QP2  - Médios
        if (okqp2m) {
            int vetor3 = 10; //Uma única quantidade a ser definida
            Etapa1Experimento qp2m = new Etapa1Experimento(gsGeral, base, "QP2M", qtdMaxM, tamBase1Geral, qtdObs, vQtdAlgM);
            qp2m.executa(); //Quando já se tem o arquivo com as listas sequenciais aleatórias de matchers
        }

        //Para QP2  - Ruins
        if (okqp2r) {
            int[] vetor4 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp2r = new Etapa1Experimento(gsGeral, base, "QP2R", qtdMaxR, tamBase1Geral, qtdObs, vQtdAlgR);
            qp2r.executa(); //Quando já se tem o arquivo com as listas sequenciais aleatórias de matchers
        }

        //Para QP3  - Todos
        if (okqp3all) {
            int[] vetor5 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp3All = new Etapa1Experimento(gsGeral, base, "QP3All", qtdMaxAll, tamBase1Geral, qtdObs, vQtdAlgAll);
            qp3All.executa(); //Quando já se tem o arquivo com as listas sequenciais aleatórias de matchers
        }

        //Para QP3  - Parte
        if (okqp3lot) {
            int[] vetor6 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp3Lot = new Etapa1Experimento(gsGeral, base, "QP3Lot", qtdMaxLot, tamBase1Geral, qtdObs, vQtdAlgLot);
            qp3Lot.executa(); //Quando já se tem o arquivo com as listas sequenciais aleatórias de matchers
        }

        //Para QP5 - 1% 
        if (okqp5one) {
            int[] vetor8 = {10, 15, 20}; //Uma única quantidade a ser definida
            //A quantidade de elementos da base de dados aqui é diferente
            Etapa1Experimento qp5o = new Etapa1Experimento(gsOne, base, "QP5O", qtdMaxGeral, tamBase1One, qtdObs, vQtdAlgGeral);
            qp5o.executa(); //Quando já se tem o arquivo com as listas sequenciais aleatórias de matchers
        }

        //Para QP5 - 3% 
        if (okqp5three) {
            int[] vetor9 = {10, 15, 20}; //Uma única quantidade a ser definida9
            //A quantidade de elementos da base de dados aqui é diferente
            Etapa1Experimento qp5t = new Etapa1Experimento(gsThree, base, "QP5T", qtdMaxGeral, tamBase1Three, qtdObs, vQtdAlgGeral);
            qp5t.executa(); //Quando já se tem o arquivo com as listas sequenciais aleatórias de matchers
        }

        //Para QP5 - 5% 
        if (okqp5five) {
            int[] vetor10 = {10, 15, 20}; //Uma única quantidade a ser definida
            //A quantidade de elementos da base de dados aqui é diferente
            Etapa1Experimento qp5f = new Etapa1Experimento(gsFive, base, "QP5F", qtdMaxGeral, tamBase1Five, qtdObs, vQtdAlgGeral);
            qp5f.executa(); //Quando já se tem o arquivo com as listas sequenciais aleatórias de matchers
        }

    }

    /**
     * Executa todos os experimentos para deduplicação a partir de arquivo com
     * lista de sequências aleatórias de <i>matchers</i>.
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void rodaExpDedupArq() throws IOException, InterruptedException {

        //Para QP1
        if (okqp1) {
            int[] vetor = {10, 15, 20}; //Poderia passar direto nos argumentos assim: new int[]{10, 15, 20}
            Etapa1Experimento qp1 = new Etapa1Experimento(gsGeral, base, "QP1", qtdMaxGeral, tamBase1Geral, qtdObs, vQtdAlgGeral);
            qp1.executa2();
        }

        //Para QP2  - Bons
        if (okqp2b) {
            int[] vetor2 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp2b = new Etapa1Experimento(gsGeral, base, "QP2B", qtdMaxB, tamBase1Geral, qtdObs, vQtdAlgB);
            qp2b.executa2(); //Quando já se tem o arquivo com as listas sequenciais aleatórias de matchers
        }

        //Para QP2  - Médios
        if (okqp2m) {
            int vetor3 = 10; //Uma única quantidade a ser definida
            Etapa1Experimento qp2m = new Etapa1Experimento(gsGeral, base, "QP2M", qtdMaxM, tamBase1Geral, qtdObs, vQtdAlgM);
            qp2m.executa2(); //Quando já se tem o arquivo com as listas sequenciais aleatórias de matchers
        }

        //Para QP2  - Ruins
        if (okqp2r) {
            int[] vetor4 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp2r = new Etapa1Experimento(gsGeral, base, "QP2R", qtdMaxR, tamBase1Geral, qtdObs, vQtdAlgR);
            qp2r.executa2(); //Quando já se tem o arquivo com as listas sequenciais aleatórias de matchers
        }

        //Para QP3  - Todos
        if (okqp3all) {
            int[] vetor5 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp3All = new Etapa1Experimento(gsGeral, base, "QP3All", qtdMaxAll, tamBase1Geral, qtdObs, vQtdAlgAll);
            qp3All.executa2(); //Quando já se tem o arquivo com as listas sequenciais aleatórias de matchers
        }

        //Para QP3  - Parte
        if (okqp3lot) {
            int[] vetor6 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp3Lot = new Etapa1Experimento(gsGeral, base, "QP3Lot", qtdMaxLot, tamBase1Geral, qtdObs, vQtdAlgLot);
            qp3Lot.executa2(); //Quando já se tem o arquivo com as listas sequenciais aleatórias de matchers
        }

        //Para QP5 - 1% 
        if (okqp5one) {
            int[] vetor8 = {10, 15, 20}; //Uma única quantidade a ser definida
            //A quantidade de elementos da base de dados aqui é diferente
            Etapa1Experimento qp5o = new Etapa1Experimento(gsOne, base, "QP5O", qtdMaxGeral, tamBase1One, qtdObs, vQtdAlgGeral);
            qp5o.executa2(); //Quando já se tem o arquivo com as listas sequenciais aleatórias de matchers
        }

        //Para QP5 - 3% 
        if (okqp5three) {
            int[] vetor9 = {10, 15, 20}; //Uma única quantidade a ser definida9
            //A quantidade de elementos da base de dados aqui é diferente
            Etapa1Experimento qp5t = new Etapa1Experimento(gsThree, base, "QP5T", qtdMaxGeral, tamBase1Three, qtdObs, vQtdAlgGeral);
            qp5t.executa2(); //Quando já se tem o arquivo com as listas sequenciais aleatórias de matchers
        }

        //Para QP5 - 5% 
        if (okqp5five) {
            int[] vetor10 = {10, 15, 20}; //Uma única quantidade a ser definida
            //A quantidade de elementos da base de dados aqui é diferente
            Etapa1Experimento qp5f = new Etapa1Experimento(gsFive, base, "QP5F", qtdMaxGeral, tamBase1Five, qtdObs, vQtdAlgGeral);
            qp5f.executa2(); //Quando já se tem o arquivo com as listas sequenciais aleatórias de matchers
        }

    }

    /**
     * Executa todos os experimentos para <i>record linkage</i>.
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void rodaExpRecLink() throws IOException, InterruptedException {

        //Para QP1
        if (okqp1) {
            int[] vetor = {10, 15, 20}; //Poderia passar direto nos argumentos assim: new int[]{10, 15, 20}
            Etapa1Experimento qp1 = new Etapa1Experimento(gsGeral, base, "QP1", qtdMaxGeral, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgGeral);
            qp1.executa();
        }

        //Para QP2  - Bons
        if (okqp2b) {
            int[] vetor2 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp2b = new Etapa1Experimento(gsGeral, base, "QP2B", qtdMaxB, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgB);
            qp2b.executa(); //Quando já se tem o arquivo com as listas sequenciais aleatórias de matchers
        }

        //Para QP2  - Médios
        if (okqp2m) {
            int vetor3 = 10; //Uma única quantidade a ser definida
            Etapa1Experimento qp2m = new Etapa1Experimento(gsGeral, base, "QP2M", qtdMaxM, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgM);
            qp2m.executa(); //Quando já se tem o arquivo com as listas sequenciais aleatórias de matchers
        }

        //Para QP2  - Ruins
        if (okqp2r) {
            int[] vetor4 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp2r = new Etapa1Experimento(gsGeral, base, "QP2R", qtdMaxR, tamBase1Geral, tamBase2Geral, tamBase2Geral, qtdObs, vQtdAlgR);
            qp2r.executa(); //Quando já se tem o arquivo com as listas sequenciais aleatórias de matchers
        }

        //Para QP3  - Todos
        if (okqp3all) {
            int[] vetor5 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp3All = new Etapa1Experimento(gsGeral, base, "QP3All", qtdMaxAll, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgAll);
            qp3All.executa(); //Quando já se tem o arquivo com as listas sequenciais aleatórias de matchers
        }

        //Para QP3  - Parte
        if (okqp3lot) {
            int[] vetor6 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp3Lot = new Etapa1Experimento(gsGeral, base, "QP3Lot", qtdMaxLot, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgLot);
            qp3Lot.executa(); //Quando já se tem o arquivo com as listas sequenciais aleatórias de matchers
        }

        //Para QP5 - 1% 
        if (okqp5one) {
            int[] vetor8 = {10, 15, 20}; //Uma única quantidade a ser definida
            //A quantidade de elementos da base de dados aqui é diferente
            Etapa1Experimento qp5o = new Etapa1Experimento(gsOne, base, "QP5O", qtdMaxGeral, tamBase1One, tamBase2One, qtdObs, vQtdAlgGeral);
            qp5o.executa(); //Quando já se tem o arquivo com as listas sequenciais aleatórias de matchers
        }

        //Para QP5 - 3% 
        if (okqp5three) {
            int[] vetor9 = {10, 15, 20}; //Uma única quantidade a ser definida9
            //A quantidade de elementos da base de dados aqui é diferente
            Etapa1Experimento qp5t = new Etapa1Experimento(gsThree, base, "QP5T", qtdMaxGeral, tamBase1Three, tamBase2Three, qtdObs, vQtdAlgGeral);
            qp5t.executa(); //Quando já se tem o arquivo com as listas sequenciais aleatórias de matchers
        }

        //Para QP5 - 5% 
        if (okqp5five) {
            int[] vetor10 = {10, 15, 20}; //Uma única quantidade a ser definida
            //A quantidade de elementos da base de dados aqui é diferente
            Etapa1Experimento qp5f = new Etapa1Experimento(gsFive, base, "QP5F", qtdMaxGeral, tamBase1Five, tamBase2Five, qtdObs, vQtdAlgGeral);
            qp5f.executa(); //Quando já se tem o arquivo com as listas sequenciais aleatórias de matchers
        }

    }

    /**
     * Executa todos os experimentos para <i>record linkage</i> a partir de
     * arquivo com lista de sequências aleatórias de <i>matchers</i>.
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void rodaExpRecLinkArq() throws IOException, InterruptedException {

        //Para QP1
        if (okqp1) {
            int[] vetor = {10, 15, 20}; //Poderia passar direto nos argumentos assim: new int[]{10, 15, 20}
            Etapa1Experimento qp1 = new Etapa1Experimento(gsGeral, base, "QP1", qtdMaxGeral, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgGeral);
            qp1.executa2();
        }

        //Para QP2  - Bons
        if (okqp2b) {
            int[] vetor2 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp2b = new Etapa1Experimento(gsGeral, base, "QP2B", qtdMaxB, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgB);
            qp2b.executa2(); //Quando já se tem o arquivo com as listas sequenciais aleatórias de matchers
        }

        //Para QP2  - Médios
        if (okqp2m) {
            int vetor3 = 10; //Uma única quantidade a ser definida
            Etapa1Experimento qp2m = new Etapa1Experimento(gsGeral, base, "QP2M", qtdMaxM, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgM);
            qp2m.executa2(); //Quando já se tem o arquivo com as listas sequenciais aleatórias de matchers
        }

        //Para QP2  - Ruins
        if (okqp2r) {
            int[] vetor4 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp2r = new Etapa1Experimento(gsGeral, base, "QP2R", qtdMaxR, tamBase1Geral, tamBase2Geral, tamBase2Geral, qtdObs, vQtdAlgR);
            qp2r.executa2(); //Quando já se tem o arquivo com as listas sequenciais aleatórias de matchers
        }

        //Para QP3  - Todos
        if (okqp3all) {
            int[] vetor5 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp3All = new Etapa1Experimento(gsGeral, base, "QP3All", qtdMaxAll, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgAll);
            qp3All.executa2(); //Quando já se tem o arquivo com as listas sequenciais aleatórias de matchers
        }

        //Para QP3  - Parte
        if (okqp3lot) {
            int[] vetor6 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp3Lot = new Etapa1Experimento(gsGeral, base, "QP3Lot", qtdMaxLot, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgLot);
            qp3Lot.executa2(); //Quando já se tem o arquivo com as listas sequenciais aleatórias de matchers
        }

        //Para QP5 - 1% 
        if (okqp5one) {
            int[] vetor8 = {10, 15, 20}; //Uma única quantidade a ser definida
            //A quantidade de elementos da base de dados aqui é diferente
            Etapa1Experimento qp5o = new Etapa1Experimento(gsOne, base, "QP5O", qtdMaxGeral, tamBase1One, tamBase2One, qtdObs, vQtdAlgGeral);
            qp5o.executa2(); //Quando já se tem o arquivo com as listas sequenciais aleatórias de matchers
        }

        //Para QP5 - 3% 
        if (okqp5three) {
            int[] vetor9 = {10, 15, 20}; //Uma única quantidade a ser definida9
            //A quantidade de elementos da base de dados aqui é diferente
            Etapa1Experimento qp5t = new Etapa1Experimento(gsThree, base, "QP5T", qtdMaxGeral, tamBase1Three, tamBase2Three, qtdObs, vQtdAlgGeral);
            qp5t.executa2(); //Quando já se tem o arquivo com as listas sequenciais aleatórias de matchers
        }

        //Para QP5 - 5% 
        if (okqp5five) {
            int[] vetor10 = {10, 15, 20}; //Uma única quantidade a ser definida
            //A quantidade de elementos da base de dados aqui é diferente
            Etapa1Experimento qp5f = new Etapa1Experimento(gsFive, base, "QP5F", qtdMaxGeral, tamBase1Five, tamBase2Five, qtdObs, vQtdAlgGeral);
            qp5f.executa2(); //Quando já se tem o arquivo com as listas sequenciais aleatórias de matchers
        }

    }
    
    /**
     *
     * @param gsGeral
     */
    public void setGsGeral(String gsGeral) {
        this.gsGeral = gsGeral;
    }

    /**
     *
     * @param gsOne
     */
    public void setGsOne(String gsOne) {
        this.gsOne = gsOne;
    }
    
    /**
     *
     * @param gsThree
     */
    public void setGsThree(String gsThree) {
        this.gsThree = gsThree;
    }

    /**
     *
     * @param gsFive
     */
    public void setGsFive(String gsFive) {
        this.gsFive = gsFive;
    }

    

}
