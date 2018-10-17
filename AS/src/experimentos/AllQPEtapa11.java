/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experimentos;

import DS.VetorSimEstat11;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.JOptionPane;

/**
 * Executa os experimentos para todas as questões de pesquisa.
 *
 * @author Diego
 */
public class AllQPEtapa11 {

    String baseGeral;
    String baseGeral1;
    String baseGeral2;
    String gsGeral;
    String baseOne;
    String baseOne1;
    String baseOne2;
    String gsOne;
    String baseThree;
    String baseThree1;
    String baseThree2;
    String gsThree;
    String baseFive1;
    String baseFive2;
    String baseFive;
    String gsFive;
    String goldId1QP5;
    String goldId2QP5;
    char separatorQP5;

    String chavePrimaria;
    String chavePrimaria2;
    String chavePrimariaQP5;
    String chavePrimaria2QP5;
    String goldId1;
    String goldId2;
    char separator;
    boolean geraVetor;

    VetorSimEstat11 objVet;

    int qtdMaxGeral, qtdMaxB, qtdMaxM, qtdMaxR, qtdMaxAll, qtdMaxLot;
    int tamBase1Geral, tamBase1One, tamBase1Three, tamBase1Five, tamBase2Geral, tamBase2One, tamBase2Three, tamBase2Five;
    int qtdObs;
    int[] vQtdAlgB, vQtdAlgM, vQtdAlgR, vQtdAlgAll, vQtdAlgLot;
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
    private boolean geraVetorQP5;

    File dirDivergqp1, dirDivergqp4, dirDivergqp6, dirDivergqp7;
    File dirEstatqp1, dirEstatqp4, dirEstatqp6, dirEstatqp7;

    public AllQPEtapa11() {

    }

    /**
     * Construtor utilizado para realizar <b>todos</b> experimentos com
     * deduplicação.
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
    public AllQPEtapa11(String baseGeral, String gsGeral, String baseOne, String gsOne, String baseThree, String gsThree, String baseFive, String gsFive, int qtdMaxGeral, int qtdMaxB, int qtdMaxM, int qtdMaxR, int qtdMaxAll, int qtdMaxLot, int tamBase1Geral, int tamBase1One, int tamBase1Three, int tamBaseFive, int qtdObs, int[] vQtdAlgB, int[] vQtdAlgM, int[] vQtdAlgR, int[] vQtdAlgAll, int[] vQtdAlgLot, VetorSimEstat11 obj) throws IOException, InterruptedException {
        this.baseGeral = baseGeral;
        this.gsGeral = gsGeral;
        this.baseOne = baseOne;
        this.gsOne = gsOne;
        this.baseThree = baseThree;
        this.gsThree = gsThree;
        this.baseFive = baseFive;
        this.gsFive = gsFive;
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
        this.objVet = obj;

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
    public AllQPEtapa11(String baseGeral, String gsGeral, String baseOne, String gsOne, String baseThree, String gsThree, String baseFive, String gsFive, int qtdMaxGeral, int qtdMaxB, int qtdMaxM, int qtdMaxR, int qtdMaxAll, int qtdMaxLot, int tamBase1Geral, int tamBase1One, int tamBase1Three, int tamBaseFive, int qtdObs, int[] vQtdAlgB, int[] vQtdAlgM, int[] vQtdAlgR, int[] vQtdAlgAll, int[] vQtdAlgLot, boolean okqp1, boolean okqp2b, boolean okqp2m, boolean okqp2r, boolean okqp3all, boolean okqp3lot, boolean okqp5one, boolean okqp5three, boolean okqp5five, VetorSimEstat11 obj) throws IOException, InterruptedException {
        this.baseGeral = baseGeral;
        this.gsGeral = gsGeral;
        this.baseOne = baseOne;
        this.gsOne = gsOne;
        this.baseThree = baseThree;
        this.gsThree = gsThree;
        this.baseFive = baseFive;
        this.gsFive = gsFive;
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

        this.objVet = obj;

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
    public AllQPEtapa11(String baseGeral, String gsGeral, String baseOne, String gsOne, String baseThree, String gsThree, String baseFive, String gsFive, int qtdMaxGeral, int qtdMaxB, int qtdMaxM, int qtdMaxR, int qtdMaxAll, int qtdMaxLot, int tamBase1Geral, int tamBase1One, int tamBase1Three, int tamBaseFive, int qtdObs, boolean okqp1, boolean okqp2b, boolean okqp2m, boolean okqp2r, boolean okqp3all, boolean okqp3lot, boolean okqp5one, boolean okqp5three, boolean okqp5five, VetorSimEstat11 obj) throws IOException, InterruptedException {
        this.baseGeral = baseGeral;
        this.gsGeral = gsGeral;
        this.baseOne = baseOne;
        this.gsOne = gsOne;
        this.baseThree = baseThree;
        this.gsThree = gsThree;
        this.baseFive = baseFive;
        this.gsFive = gsFive;
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

        this.objVet = obj;

        iniciaArq();

        System.out.println("Quantidade de observações a serem geradas: " + qtdObs);

        System.out.println("Executando qp1: " + okqp1 + " - qp2b: " + okqp2b + " - qp2m: " + okqp2m + " - qp2r: " + okqp2r + " - qp3all: " + okqp3all + " - qp3lot: " + okqp3lot + " - qp5one :" + okqp5one + " - qp5three: " + okqp5three + " - qp5five: " + okqp5five);

    }

    /**
     * Construtor utilizado para realizar <b>todos</b> experimentos com
     * <i>record linkage</i>.
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
    public AllQPEtapa11(String baseGeral1, String baseGeral2, String gsGeral, String baseOne1, String baseOne2, String gsOne, String baseThree1, String baseThree2, String gsThree, String baseFive1, String baseFive2, String gsFive, int qtdMaxGeral, int qtdMaxB, int qtdMaxM, int qtdMaxR, int qtdMaxAll, int qtdMaxLot, int tamBase1Geral, int tamBase1One, int tamBase1Three, int tamBase1Five, int tamBase2Geral, int tamBase2One, int tamBase2Three, int tamBase2Five, int qtdObs, int[] vQtdAlgB, int[] vQtdAlgM, int[] vQtdAlgR, int[] vQtdAlgAll, int[] vQtdAlgLot) throws IOException, InterruptedException {

        this.baseGeral1 = baseGeral1;
        this.baseGeral2 = baseGeral2;
        this.gsGeral = gsGeral;
        this.baseOne1 = baseOne1;
        this.baseOne2 = baseOne2;
        this.gsOne = gsOne;
        this.baseThree1 = baseThree1;
        this.baseThree2 = baseThree2;
        this.gsThree = gsThree;
        this.baseFive1 = baseFive1;
        this.baseFive2 = baseFive2;
        this.gsFive = gsFive;

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
     * @param baseGeral1
     * @param baseGeral2
     * @param gsGeral
     * @param baseOne1
     * @param base1
     * @param qtdMaxGeral
     * @param gsOne
     * @param baseThree1
     * @param baseThree2
     * @param baseFive1
     * @param baseOne2
     * @param baseFive2
     * @param base2
     * @param qtdMaxB
     * @param qtdMaxM
     * @param qtdMaxR
     * @param qtdMaxAll
     * @param gsThree
     * @param qtdMaxLot
     * @param tamBase1Geral
     * @param tamBase1One
     * @param gsFive
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
    public AllQPEtapa11(String baseGeral1, String baseGeral2, String gsGeral, String baseOne1, String baseOne2, String gsOne, String baseThree1, String baseThree2, String gsThree, String baseFive1, String baseFive2, String gsFive, int qtdMaxGeral, int qtdMaxB, int qtdMaxM, int qtdMaxR, int qtdMaxAll, int qtdMaxLot, int tamBase1Geral, int tamBase1One, int tamBase1Three, int tamBase1Five, int tamBase2Geral, int tamBase2One, int tamBase2Three, int tamBase2Five, int qtdObs, int[] vQtdAlgB, int[] vQtdAlgM, int[] vQtdAlgR, int[] vQtdAlgAll, int[] vQtdAlgLot, boolean okqp1, boolean okqp2b, boolean okqp2m, boolean okqp2r, boolean okqp3all, boolean okqp3lot, boolean okqp5one, boolean okqp5three, boolean okqp5five) throws IOException, InterruptedException {

        this.baseGeral1 = baseGeral1;
        this.baseGeral2 = baseGeral2;
        this.gsGeral = gsGeral;
        this.baseOne1 = baseOne1;
        this.baseOne2 = baseOne2;
        this.gsOne = gsOne;
        this.baseThree1 = baseThree1;
        this.baseThree2 = baseThree2;
        this.gsThree = gsThree;
        this.baseFive1 = baseFive1;
        this.baseFive2 = baseFive2;
        this.gsFive = gsFive;

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

        iniciaArq();

        System.out.println("Quantidade de observações a serem geradas: " + qtdObs);

        System.out.println("Executando qp1: " + okqp1 + " - qp2b: " + okqp2b + " - qp2m: " + okqp2m + " - qp2r: " + okqp2r + " - qp3all: " + okqp3all + " - qp3lot: " + okqp3lot + " - qp5one :" + okqp5one + " - qp5three: " + okqp5three + " - qp5five: " + okqp5five);

    }

    /**
     * Executa todos os experimentos para deduplicação.
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void rodaExpDedup() throws IOException, InterruptedException {

        //Apenas para gerar o vetor de divergências maior! IRÁ SERVIR PARA QP1 INDEPENDENTEMENTE DE okp1 SER true. QP1 é o carro chefe!
        Etapa1Experimento qp1 = new Etapa1Experimento(gsGeral, baseGeral, "qp1", qtdMaxGeral, tamBase1Geral, qtdObs, vQtdAlgGeral);

        //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
        if (geraVetor) {
            qp1.setvQtdAlg(new int[]{qtdMaxGeral});
            qp1.setQtdObservacoes(1); //Basta uma vez                
            qp1.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            qp1.limpaEstatisticas(baseGeral, "qp1");
            JOptionPane.showMessageDialog(null, "Olhe a pasta lá, papae!");
            limpaDivergBase(dirDivergqp1);
            JOptionPane.showMessageDialog(null, "Olhe a pasta agora, papae!");
        }

        //Se geraVetor == true, gera o vetor de similaridades maior
        objVet.setAllVarDedup(baseGeral, chavePrimaria, gsGeral, goldId1, goldId2, ';', "qp1", geraVetor);
        geraVetor = false;

        //Para qp1
        if (okqp1) {
//            int[] vetor = {10, 15, 20}; //Poderia passar direto nos argumentos assim: new int[]{10, 15, 20}
//            Etapa1Experimento qp1 = new Etapa1Experimento(gsGeral, baseGeral, "qp1", qtdMaxGeral, tamBase1Geral, qtdObs, vQtdAlgGeral);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
//            if (geraVetor) {
//                qp1.setvQtdAlg(new int[]{qtdMaxGeral});
//                qp1.setQtdObservacoes(1); //Basta uma vez                
//                qp1.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
//                qp1.limpaEstatisticas(baseGeral, "qp1");
//            }
//
//            //Mesma ideia do construtor
//            //Se geraVetor == true, gera o vetor de similaridades maior
//            objVet.setAllVarDedup(baseGeral, chavePrimaria, gsGeral, goldId1, goldId2, ';', "qp1", geraVetor);
            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp1.setvQtdAlg(getvQtdAlgGeral());
            qp1.setQtdObservacoes(qtdObs);
            qp1.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores

            copia(dirDivergqp1, dirDivergqp4);
            copia(dirDivergqp1, dirDivergqp6);
            copia(dirDivergqp1, dirDivergqp7);
            copia(dirEstatqp1, dirEstatqp4);
            copia(dirEstatqp1, dirEstatqp6);
            copia(dirEstatqp1, dirEstatqp7);

        }

        //Para qp2  - Bons
        if (okqp2b) {
//            int[] vetor2 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp2b = new Etapa1Experimento(gsGeral, baseGeral, "qp2b", qtdMaxB, tamBase1Geral, qtdObs, vQtdAlgB);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
//            if (geraVetor) {
//                qp2b.setvQtdAlg(new int[]{qtdMaxB});
//                qp2b.setQtdObservacoes(1); //Basta uma vez                
//                qp2b.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
//                qp2b.limpaEstatisticas(baseGeral, "qp2b");
//            }
            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral, chavePrimaria, gsGeral, goldId1, goldId2, ';', "qp2b", geraVetor);

            int itensComb = (int) (qtdMaxB * 0.8); //80% da quantidade máxima de matchers bons.
//            System.out.println("qtdMaxB: " + qtdMaxB);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp2b.setvQtdAlg(new int[]{itensComb});

            //Validando se a quantidade de Observações definida inicialmente (1000) pode ser alcançada pela quantidade
            //de matchers disponíveis. Se não for possível, a quantidade de observações é definida a partir do cálculo
            //de combinações possíveis de n-2 matchers.
            if (calcQtdObs(qtdMaxB, itensComb) < qtdObs) { //Esse cálculo foi fito baseado nas 1000 iterações clássicas
                qp2b.setQtdObservacoes(calcQtdObs(qtdMaxB, itensComb));
            } else {
                qp2b.setQtdObservacoes(qtdObs);
            }

            qp2b.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores

        } //Para qp2  - Médios
        if (okqp2m) {
//            int vetor3 = 10; //Uma única quantidade a ser definida
            Etapa1Experimento qp2m = new Etapa1Experimento(gsGeral, baseGeral, "qp2m", qtdMaxM, tamBase1Geral, qtdObs, vQtdAlgM);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
//            if (geraVetor) {
//                qp2m.setvQtdAlg(new int[]{qtdMaxGeral});
//                qp2m.setQtdObservacoes(1); //Basta uma vez                
//                qp2m.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
//            }
            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral, chavePrimaria, gsGeral, goldId1, goldId2, ';', "qp2m", geraVetor);

            int itensComb = (int) (qtdMaxM * 0.8); //80% da quantidade máxima de matchers médios.
//            System.out.println("qtdMaxM: " + qtdMaxM);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp2m.setvQtdAlg(new int[]{itensComb});

            //Validando se a quantidade de Observações definida inicialmente (1000) pode ser alcançada pela quantidade
            //de matchers disponíveis. Se não for possível, a quantidade de observações é definida a partir do cálculo
            //de combinações possíveis de n-2 matchers.
            if (calcQtdObs(qtdMaxM, itensComb) < qtdObs) {
                qp2m.setQtdObservacoes(calcQtdObs(qtdMaxM, itensComb));
            } else {
                qp2m.setQtdObservacoes(qtdObs);
            }

            qp2m.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp2  - Ruins
        if (okqp2r) {
//            int[] vetor4 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp2r = new Etapa1Experimento(gsGeral, baseGeral, "qp2r", qtdMaxR, tamBase1Geral, qtdObs, vQtdAlgR);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
//            if (geraVetor) {
//                qp2r.setvQtdAlg(new int[]{qtdMaxGeral});
//                qp2r.setQtdObservacoes(1); //Basta uma vez                
//                qp2r.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
//            }
            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral, chavePrimaria, gsGeral, goldId1, goldId2, ';', "qp2r", geraVetor);

            int itensComb = (int) (qtdMaxR * 0.8); //80% da quantidade máxima de matchers ruins.
//            System.out.println("qtdMaxR: " + qtdMaxR);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp2r.setvQtdAlg(new int[]{itensComb});

            //Validando se a quantidade de Observações definida inicialmente (1000) pode ser alcançada pela quantidade
            //de matchers disponíveis. Se não for possível, a quantidade de observações é definida a partir do cálculo
            //de combinações possíveis de n-2 matchers.
            if (calcQtdObs(qtdMaxR, itensComb) < qtdObs) {
                qp2r.setQtdObservacoes(calcQtdObs(qtdMaxR, itensComb));
            } else {
                qp2r.setQtdObservacoes(qtdObs);
            }

            qp2r.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp3  - Todos
        if (okqp3all) {

            int esteQtdMaxAll = qtdMaxAll;
//            int[] vetor5 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp3All = new Etapa1Experimento(gsGeral, baseGeral, "qp3all", esteQtdMaxAll, tamBase1Geral, qtdObs, vQtdAlgAll);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
//            if (geraVetor) {
//                qp3All.setvQtdAlg(new int[]{qtdMaxGeral});
//                qp3All.setQtdObservacoes(1); //Basta uma vez                
//                qp3All.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
//            }
            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral, chavePrimaria, gsGeral, goldId1, goldId2, ';', "qp3all", geraVetor);

            int itensComb = (int) (esteQtdMaxAll * 0.8); //80% da quantidade máxima de matchers bons.
//            System.out.println("esteQtdMaxAll: " + esteQtdMaxAll);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp3All.setvQtdAlg(new int[]{itensComb});

            //Validando se a quantidade de Observações definida inicialmente (1000) pode ser alcançada pela quantidade
            //de matchers disponíveis. Se não for possível, a quantidade de observações é definida a partir do cálculo
            //de combinações possíveis de n-2 matchers.
            if (calcQtdObs(esteQtdMaxAll, itensComb) < qtdObs) {
                qp3All.setQtdObservacoes(calcQtdObs(esteQtdMaxAll, itensComb));
            } else {
                qp3All.setQtdObservacoes(qtdObs);
            }

            qp3All.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp3  - Parte
        if (okqp3lot) {
//            int[] vetor6 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp3Lot = new Etapa1Experimento(gsGeral, baseGeral, "qp3lot", qtdMaxAll, tamBase1Geral, qtdObs, vQtdAlgLot);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
//            if (geraVetor) {
//                qp3Lot.setvQtdAlg(new int[]{qtdMaxGeral});
//                qp3Lot.setQtdObservacoes(1); //Basta uma vez                
//                qp3Lot.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
//            }
            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral, chavePrimaria, gsGeral, goldId1, goldId2, ';', "qp3lot", geraVetor);

            int itensComb = (int) qtdMaxAll / 2; //Metade da quantidade máxima de matchers bons.
//            System.out.println("qtdMaxLot: " + qtdMaxLot);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp3Lot.setvQtdAlg(new int[]{itensComb});

            //Validando se a quantidade de Observações definida inicialmente (1000) pode ser alcançada pela quantidade
            //de matchers disponíveis. Se não for possível, a quantidade de observações é definida a partir do cálculo
            //de combinações possíveis de n-2 matchers.
            if (calcQtdObs(qtdMaxLot, itensComb) < qtdObs) {
                qp3Lot.setQtdObservacoes(calcQtdObs(qtdMaxLot, itensComb));
            } else {
                qp3Lot.setQtdObservacoes(qtdObs);
            }

            qp3Lot.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores

        }

        //Para qp5 - 1% 
        if (okqp5one) {
//            int[] vetor8 = {10, 15, 20}; //Uma única quantidade a ser definida
            //A quantidade de elementos da baseGeral de dados aqui é diferente
            Etapa1Experimento qp5o = new Etapa1Experimento(gsOne, baseOne, "qp5o", qtdMaxGeral, tamBase1One, qtdObs, vQtdAlgGeral);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetorQP5) {
                qp5o.setvQtdAlg(new int[]{qtdMaxGeral});
                qp5o.setQtdObservacoes(1); //Basta uma vez                
                qp5o.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
                qp5o.limpaEstatisticas(baseOne, "qp5o");
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseOne, chavePrimaria, gsOne, goldId1, goldId2, ';', "qp5o", geraVetorQP5);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp5o.setvQtdAlg(getvQtdAlgGeral());
            qp5o.setQtdObservacoes(qtdObs);
            qp5o.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp5 - 3% 
        if (okqp5three) {
//            int[] vetor9 = {10, 15, 20}; //Uma única quantidade a ser definida9
            //A quantidade de elementos da baseGeral de dados aqui é diferente
            Etapa1Experimento qp5t = new Etapa1Experimento(gsThree, baseThree, "qp5t", qtdMaxGeral, tamBase1Three, qtdObs, vQtdAlgGeral);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetorQP5) {
                qp5t.setvQtdAlg(new int[]{qtdMaxGeral});
                qp5t.setQtdObservacoes(1); //Basta uma vez                
                qp5t.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
                qp5t.limpaEstatisticas(baseOne, "qp5t");
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseThree, chavePrimaria, gsThree, goldId1, goldId2, ';', "qp5t", geraVetorQP5);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp5t.setvQtdAlg(getvQtdAlgGeral());
            qp5t.setQtdObservacoes(qtdObs);
            qp5t.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores

        }

        //Para qp5 - 5% 
        if (okqp5five) {
//            int[] vetor10 = {10, 15, 20}; //Uma única quantidade a ser definida
            //A quantidade de elementos da baseGeral de dados aqui é diferente
            Etapa1Experimento qp5f = new Etapa1Experimento(gsFive, baseFive, "qp5f", qtdMaxGeral, tamBase1Five, qtdObs, vQtdAlgGeral);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetorQP5) {
                qp5f.setvQtdAlg(new int[]{qtdMaxGeral});
                qp5f.setQtdObservacoes(1); //Basta uma vez                
                qp5f.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
                qp5f.limpaEstatisticas(baseOne, "qp5f");
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseFive, chavePrimaria, gsFive, goldId1, goldId2, ';', "qp5f", geraVetorQP5);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp5f.setvQtdAlg(getvQtdAlgGeral());
            qp5f.setQtdObservacoes(qtdObs);
            qp5f.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores

        }

        System.out.println("Quantidade de observações geradas: " + qtdObs);

        System.out.println("QPs executaas - qp1: " + okqp1 + " - qp2b: " + okqp2b + " - qp2m: " + okqp2m + " - qp2r: " + okqp2r + " - qp3all: " + okqp3all + " - qp3lot: " + okqp3lot + " - qp5one :" + okqp5one + " - qp5three: " + okqp5three + " - qp5five: " + okqp5five);

    }

    /**
     * Executa todos os experimentos para deduplicação a partir de arquivo com
     * lista de sequências aleatórias de <i>matchers</i>.
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void rodaExpDedupArq() throws IOException, InterruptedException {

        //Para qp1
        if (okqp1) {
//            int[] vetor = {10, 15, 20}; //Poderia passar direto nos argumentos assim: new int[]{10, 15, 20}
            Etapa1Experimento qp1 = new Etapa1Experimento(gsGeral, baseGeral, "qp1", qtdMaxGeral, tamBase1Geral, qtdObs, vQtdAlgGeral);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                qp1.setvQtdAlg(new int[]{qtdMaxGeral});
                qp1.setQtdObservacoes(1); //Basta uma vez                
                qp1.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            //Se geraVetor == true, gera o vetor de similaridades maior
            objVet.setAllVarDedup(baseGeral, chavePrimaria, gsGeral, goldId1, goldId2, ';', "qp1", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp1.setvQtdAlg(getvQtdAlgGeral());
            qp1.setQtdObservacoes(qtdObs);
            qp1.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp2  - Bons
        if (okqp2b) {
//            int[] vetor2 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp2b = new Etapa1Experimento(gsGeral, baseGeral, "qp2b", qtdMaxB, tamBase1Geral, qtdObs, vQtdAlgB);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                qp2b.setvQtdAlg(new int[]{qtdMaxGeral});
                qp2b.setQtdObservacoes(1); //Basta uma vez                
                qp2b.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral, chavePrimaria, gsGeral, goldId1, goldId2, ';', "qp2b", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp2b.setvQtdAlg(getvQtdAlgB());
            qp2b.setQtdObservacoes(qtdObs);
            qp2b.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores

        } //Para qp2  - Médios
        if (okqp2m) {
//            int vetor3 = 10; //Uma única quantidade a ser definida
            Etapa1Experimento qp2m = new Etapa1Experimento(gsGeral, baseGeral, "qp2m", qtdMaxM, tamBase1Geral, qtdObs, vQtdAlgM);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                qp2m.setvQtdAlg(new int[]{qtdMaxGeral});
                qp2m.setQtdObservacoes(1); //Basta uma vez                
                qp2m.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral, chavePrimaria, gsGeral, goldId1, goldId2, ';', "qp2m", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp2m.setvQtdAlg(getvQtdAlgM());
            qp2m.setQtdObservacoes(qtdObs);
            qp2m.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp2  - Ruins
        if (okqp2r) {
//            int[] vetor4 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp2r = new Etapa1Experimento(gsGeral, baseGeral, "qp2r", qtdMaxR, tamBase1Geral, qtdObs, vQtdAlgR);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                qp2r.setvQtdAlg(new int[]{qtdMaxGeral});
                qp2r.setQtdObservacoes(1); //Basta uma vez                
                qp2r.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral, chavePrimaria, gsGeral, goldId1, goldId2, ';', "qp2r", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp2r.setvQtdAlg(getvQtdAlgR());
            qp2r.setQtdObservacoes(qtdObs);
            qp2r.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp3  - Todos
        if (okqp3all) {
//            int[] vetor5 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp3All = new Etapa1Experimento(gsGeral, baseGeral, "qp3all", qtdMaxAll, tamBase1Geral, qtdObs, vQtdAlgAll);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                qp3All.setvQtdAlg(new int[]{qtdMaxGeral});
                qp3All.setQtdObservacoes(1); //Basta uma vez                
                qp3All.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral, chavePrimaria, gsGeral, goldId1, goldId2, ';', "qp3all", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp3All.setvQtdAlg(getvQtdAlgAll());
            qp3All.setQtdObservacoes(qtdObs);
            qp3All.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp3  - Parte
        if (okqp3lot) {
//            int[] vetor6 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp3Lot = new Etapa1Experimento(gsGeral, baseGeral, "qp3lot", qtdMaxLot, tamBase1Geral, qtdObs, vQtdAlgLot);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                qp3Lot.setvQtdAlg(new int[]{qtdMaxGeral});
                qp3Lot.setQtdObservacoes(1); //Basta uma vez                
                qp3Lot.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral, chavePrimaria, gsGeral, goldId1, goldId2, ';', "qp3lot", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp3Lot.setvQtdAlg(getvQtdAlgLot());
            qp3Lot.setQtdObservacoes(qtdObs);
            qp3Lot.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores

        }

        //Para qp5 - 1% 
        if (okqp5one) {
//            int[] vetor8 = {10, 15, 20}; //Uma única quantidade a ser definida
            //A quantidade de elementos da baseGeral de dados aqui é diferente
            Etapa1Experimento qp5o = new Etapa1Experimento(gsOne, baseOne, "qp5o", qtdMaxGeral, tamBase1One, qtdObs, vQtdAlgGeral);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                qp5o.setvQtdAlg(new int[]{qtdMaxGeral});
                qp5o.setQtdObservacoes(1); //Basta uma vez                
                qp5o.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseOne, chavePrimaria, gsOne, goldId1, goldId2, ';', "qp5o", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp5o.setvQtdAlg(getvQtdAlgGeral());
            qp5o.setQtdObservacoes(qtdObs);
            qp5o.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp5 - 3% 
        if (okqp5three) {
//            int[] vetor9 = {10, 15, 20}; //Uma única quantidade a ser definida9
            //A quantidade de elementos da baseGeral de dados aqui é diferente
            Etapa1Experimento qp5t = new Etapa1Experimento(gsThree, baseThree, "qp5t", qtdMaxGeral, tamBase1Three, qtdObs, vQtdAlgGeral);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                qp5t.setvQtdAlg(new int[]{qtdMaxGeral});
                qp5t.setQtdObservacoes(1); //Basta uma vez                
                qp5t.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseThree, chavePrimaria, gsThree, goldId1, goldId2, ';', "qp5t", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp5t.setvQtdAlg(getvQtdAlgGeral());
            qp5t.setQtdObservacoes(qtdObs);
            qp5t.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores

        }

        //Para qp5 - 5% 
        if (okqp5five) {
//            int[] vetor10 = {10, 15, 20}; //Uma única quantidade a ser definida
            //A quantidade de elementos da baseGeral de dados aqui é diferente
            Etapa1Experimento qp5f = new Etapa1Experimento(gsFive, baseFive, "qp5f", qtdMaxGeral, tamBase1Five, qtdObs, vQtdAlgGeral);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                qp5f.setvQtdAlg(new int[]{qtdMaxGeral});
                qp5f.setQtdObservacoes(1); //Basta uma vez                
                qp5f.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseFive, chavePrimaria, gsFive, goldId1, goldId2, ';', "qp5f", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp5f.setvQtdAlg(getvQtdAlgGeral());
            qp5f.setQtdObservacoes(qtdObs);
            qp5f.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores

        }

        System.out.println("Quantidade de observações geradas: " + qtdObs);

        System.out.println("QPs executaas - qp1: " + okqp1 + " - qp2b: " + okqp2b + " - qp2m: " + okqp2m + " - qp2r: " + okqp2r + " - qp3all: " + okqp3all + " - qp3lot: " + okqp3lot + " - qp5one :" + okqp5one + " - qp5three: " + okqp5three + " - qp5five: " + okqp5five);

    }

    /**
     * Executa todos os experimentos para <i>record linkage</i>.
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void rodaExpRecLink() throws IOException, InterruptedException {

        //Para qp1
        if (okqp1) {
//            int[] vetor = {10, 15, 20}; //Poderia passar direto nos argumentos assim: new int[]{10, 15, 20}
            Etapa1Experimento qp1 = new Etapa1Experimento(gsGeral, baseGeral1, baseGeral2, "qp1", qtdMaxGeral, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgGeral);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                qp1.setvQtdAlg(new int[]{qtdMaxGeral});
                qp1.setQtdObservacoes(1); //Basta uma vez                
                qp1.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral1, baseGeral2, chavePrimaria, chavePrimaria2, gsGeral, goldId1, goldId2, ';', "qp1", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp1.setvQtdAlg(getvQtdAlgGeral());
            qp1.setQtdObservacoes(qtdObs);
            qp1.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp2  - Bons
        if (okqp2b) {
//            int[] vetor2 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp2b = new Etapa1Experimento(gsGeral, baseGeral1, baseGeral2, "qp2b", qtdMaxB, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgB);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                qp2b.setvQtdAlg(new int[]{qtdMaxGeral});
                qp2b.setQtdObservacoes(1); //Basta uma vez                
                qp2b.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral1, baseGeral2, chavePrimaria, chavePrimaria2, gsGeral, goldId1, goldId2, ';', "qp2b", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp2b.setvQtdAlg(getvQtdAlgB());
            qp2b.setQtdObservacoes(qtdObs);
            qp2b.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp2  - Médios
        if (okqp2m) {
            int vetor3 = 10; //Uma única quantidade a ser definida
            Etapa1Experimento qp2m = new Etapa1Experimento(gsGeral, baseGeral1, baseGeral2, "qp2m", qtdMaxM, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgM);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                qp2m.setvQtdAlg(new int[]{qtdMaxGeral});
                qp2m.setQtdObservacoes(1); //Basta uma vez                
                qp2m.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral1, baseGeral2, chavePrimaria, chavePrimaria2, gsGeral, goldId1, goldId2, ';', "qp2m", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp2m.setvQtdAlg(getvQtdAlgM());
            qp2m.setQtdObservacoes(qtdObs);
            qp2m.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp2  - Ruins
        if (okqp2r) {
//            int[] vetor4 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp2r = new Etapa1Experimento(gsGeral, baseGeral1, baseGeral2, "qp2r", qtdMaxR, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgR);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                qp2r.setvQtdAlg(new int[]{qtdMaxGeral});
                qp2r.setQtdObservacoes(1); //Basta uma vez                
                qp2r.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral1, baseGeral2, chavePrimaria, chavePrimaria2, gsGeral, goldId1, goldId2, ';', "qp2r", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp2r.setvQtdAlg(getvQtdAlgR());
            qp2r.setQtdObservacoes(qtdObs);
            qp2r.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp3  - Todos
        if (okqp3all) {
//            int[] vetor5 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp3All = new Etapa1Experimento(gsGeral, baseGeral1, baseGeral2, "qp3all", qtdMaxAll, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgAll);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                qp3All.setvQtdAlg(new int[]{qtdMaxGeral});
                qp3All.setQtdObservacoes(1); //Basta uma vez                
                qp3All.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral1, baseGeral2, chavePrimaria, chavePrimaria2, gsGeral, goldId1, goldId2, ';', "qp3all", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp3All.setvQtdAlg(getvQtdAlgAll());
            qp3All.setQtdObservacoes(qtdObs);
            qp3All.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp3  - Parte
        if (okqp3lot) {
//            int[] vetor6 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp3Lot = new Etapa1Experimento(gsGeral, baseGeral1, baseGeral2, "qp3Lot", qtdMaxLot, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgLot);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                qp3Lot.setvQtdAlg(new int[]{qtdMaxGeral});
                qp3Lot.setQtdObservacoes(1); //Basta uma vez                
                qp3Lot.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral1, baseGeral2, chavePrimaria, chavePrimaria2, gsGeral, goldId1, goldId2, ';', "qp3lot", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp3Lot.setvQtdAlg(getvQtdAlgLot());
            qp3Lot.setQtdObservacoes(qtdObs);
            qp3Lot.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp5 - 1% 
        if (okqp5one) {
//            int[] vetor8 = {10, 15, 20}; //Uma única quantidade a ser definida
            //A quantidade de elementos da baseGeral de dados aqui é diferente
            Etapa1Experimento qp5o = new Etapa1Experimento(gsOne, baseOne1, baseOne2, "qp5o", qtdMaxGeral, tamBase1One, tamBase2One, qtdObs, vQtdAlgGeral);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                qp5o.setvQtdAlg(new int[]{qtdMaxGeral});
                qp5o.setQtdObservacoes(1); //Basta uma vez                
                qp5o.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseOne1, baseOne2, chavePrimaria, chavePrimaria2, gsOne, goldId1, goldId2, ';', "qp5o", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp5o.setvQtdAlg(getvQtdAlgGeral());
            qp5o.setQtdObservacoes(qtdObs);
            qp5o.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp5 - 3% 
        if (okqp5three) {
//            int[] vetor9 = {10, 15, 20}; //Uma única quantidade a ser definida9
            //A quantidade de elementos da baseGeral de dados aqui é diferente
            Etapa1Experimento qp5t = new Etapa1Experimento(gsThree, baseThree1, baseThree2, "qp5t", qtdMaxGeral, tamBase1Three, tamBase2Three, qtdObs, vQtdAlgGeral);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                qp5t.setvQtdAlg(new int[]{qtdMaxGeral});
                qp5t.setQtdObservacoes(1); //Basta uma vez                
                qp5t.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseThree1, baseThree2, chavePrimaria, chavePrimaria2, gsThree, goldId1, goldId2, ';', "qp5t", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp5t.setvQtdAlg(getvQtdAlgGeral());
            qp5t.setQtdObservacoes(qtdObs);
            qp5t.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp5 - 5% 
        if (okqp5five) {
//            int[] vetor10 = {10, 15, 20}; //Uma única quantidade a ser definida
            //A quantidade de elementos da baseGeral de dados aqui é diferente
            Etapa1Experimento qp5f = new Etapa1Experimento(gsFive, baseFive1, baseFive2, "qp5f", qtdMaxGeral, tamBase1Five, tamBase2Five, qtdObs, vQtdAlgGeral);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                qp5f.setvQtdAlg(new int[]{qtdMaxGeral});
                qp5f.setQtdObservacoes(1); //Basta uma vez                
                qp5f.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseFive1, baseFive2, chavePrimaria, chavePrimaria2, gsFive, goldId1, goldId2, ';', "qp5f", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp5f.setvQtdAlg(getvQtdAlgGeral());
            qp5f.setQtdObservacoes(qtdObs);
            qp5f.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        System.out.println("Quantidade de observações geradas: " + qtdObs);

        System.out.println("QPs executaas - qp1: " + okqp1 + " - qp2b: " + okqp2b + " - qp2m: " + okqp2m + " - qp2r: " + okqp2r + " - qp3all: " + okqp3all + " - qp3lot: " + okqp3lot + " - qp5one :" + okqp5one + " - qp5three: " + okqp5three + " - qp5five: " + okqp5five);

    }

    /**
     * Executa todos os experimentos para <i>record linkage</i> a partir de
     * arquivo com lista de sequências aleatórias de <i>matchers</i>.
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void rodaExpRecLinkArq() throws IOException, InterruptedException {

        //Para qp1
        if (okqp1) {
//            int[] vetor = {10, 15, 20}; //Poderia passar direto nos argumentos assim: new int[]{10, 15, 20}
            Etapa1Experimento qp1 = new Etapa1Experimento(gsGeral, baseGeral1, baseGeral2, "qp1", qtdMaxGeral, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgGeral);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                qp1.setvQtdAlg(new int[]{qtdMaxGeral});
                qp1.setQtdObservacoes(1); //Basta uma vez                
                qp1.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral1, baseGeral2, chavePrimaria, chavePrimaria2, gsGeral, goldId1, goldId2, ';', "qp1", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp1.setvQtdAlg(getvQtdAlgGeral());
            qp1.setQtdObservacoes(qtdObs);
            qp1.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp2  - Bons
        if (okqp2b) {
//            int[] vetor2 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp2b = new Etapa1Experimento(gsGeral, baseGeral1, baseGeral2, "qp2b", qtdMaxB, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgB);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                qp2b.setvQtdAlg(new int[]{qtdMaxGeral});
                qp2b.setQtdObservacoes(1); //Basta uma vez                
                qp2b.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral1, baseGeral2, chavePrimaria, chavePrimaria2, gsGeral, goldId1, goldId2, ';', "qp2b", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp2b.setvQtdAlg(getvQtdAlgB());
            qp2b.setQtdObservacoes(qtdObs);
            qp2b.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp2  - Médios
        if (okqp2m) {
            int vetor3 = 10; //Uma única quantidade a ser definida
            Etapa1Experimento qp2m = new Etapa1Experimento(gsGeral, baseGeral1, baseGeral2, "qp2m", qtdMaxM, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgM);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                qp2m.setvQtdAlg(new int[]{qtdMaxGeral});
                qp2m.setQtdObservacoes(1); //Basta uma vez                
                qp2m.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral1, baseGeral2, chavePrimaria, chavePrimaria2, gsGeral, goldId1, goldId2, ';', "qp2m", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp2m.setvQtdAlg(getvQtdAlgM());
            qp2m.setQtdObservacoes(qtdObs);
            qp2m.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp2  - Ruins
        if (okqp2r) {
//            int[] vetor4 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp2r = new Etapa1Experimento(gsGeral, baseGeral1, baseGeral2, "qp2r", qtdMaxR, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgR);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                qp2r.setvQtdAlg(new int[]{qtdMaxGeral});
                qp2r.setQtdObservacoes(1); //Basta uma vez                
                qp2r.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral1, baseGeral2, chavePrimaria, chavePrimaria2, gsGeral, goldId1, goldId2, ';', "qp2r", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp2r.setvQtdAlg(getvQtdAlgR());
            qp2r.setQtdObservacoes(qtdObs);
            qp2r.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp3  - Todos
        if (okqp3all) {
//            int[] vetor5 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp3All = new Etapa1Experimento(gsGeral, baseGeral1, baseGeral2, "qp3all", qtdMaxAll, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgAll);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                qp3All.setvQtdAlg(new int[]{qtdMaxGeral});
                qp3All.setQtdObservacoes(1); //Basta uma vez                
                qp3All.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral1, baseGeral2, chavePrimaria, chavePrimaria2, gsGeral, goldId1, goldId2, ';', "qp3all", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp3All.setvQtdAlg(getvQtdAlgAll());
            qp3All.setQtdObservacoes(qtdObs);
            qp3All.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp3  - Parte
        if (okqp3lot) {
//            int[] vetor6 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp3Lot = new Etapa1Experimento(gsGeral, baseGeral1, baseGeral2, "qp3Lot", qtdMaxLot, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgLot);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                qp3Lot.setvQtdAlg(new int[]{qtdMaxGeral});
                qp3Lot.setQtdObservacoes(1); //Basta uma vez                
                qp3Lot.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral1, baseGeral2, chavePrimaria, chavePrimaria2, gsGeral, goldId1, goldId2, ';', "qp3lot", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp3Lot.setvQtdAlg(getvQtdAlgLot());
            qp3Lot.setQtdObservacoes(qtdObs);
            qp3Lot.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp5 - 1% 
        if (okqp5one) {
//            int[] vetor8 = {10, 15, 20}; //Uma única quantidade a ser definida
            //A quantidade de elementos da baseGeral de dados aqui é diferente
            Etapa1Experimento qp5o = new Etapa1Experimento(gsOne, baseOne1, baseOne2, "qp5o", qtdMaxGeral, tamBase1One, tamBase2One, qtdObs, vQtdAlgGeral);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                qp5o.setvQtdAlg(new int[]{qtdMaxGeral});
                qp5o.setQtdObservacoes(1); //Basta uma vez                
                qp5o.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseOne1, baseOne2, chavePrimaria, chavePrimaria2, gsOne, goldId1, goldId2, ';', "qp5o", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp5o.setvQtdAlg(getvQtdAlgGeral());
            qp5o.setQtdObservacoes(qtdObs);
            qp5o.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp5 - 3% 
        if (okqp5three) {
//            int[] vetor9 = {10, 15, 20}; //Uma única quantidade a ser definida9
            //A quantidade de elementos da baseGeral de dados aqui é diferente
            Etapa1Experimento qp5t = new Etapa1Experimento(gsThree, baseThree1, baseThree2, "qp5t", qtdMaxGeral, tamBase1Three, tamBase2Three, qtdObs, vQtdAlgGeral);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                qp5t.setvQtdAlg(new int[]{qtdMaxGeral});
                qp5t.setQtdObservacoes(1); //Basta uma vez                
                qp5t.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseThree1, baseThree2, chavePrimaria, chavePrimaria2, gsThree, goldId1, goldId2, ';', "qp5t", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp5t.setvQtdAlg(getvQtdAlgGeral());
            qp5t.setQtdObservacoes(qtdObs);
            qp5t.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp5 - 5% 
        if (okqp5five) {
//            int[] vetor10 = {10, 15, 20}; //Uma única quantidade a ser definida
            //A quantidade de elementos da baseGeral de dados aqui é diferente
            Etapa1Experimento qp5f = new Etapa1Experimento(gsFive, baseFive1, baseFive2, "qp5f", qtdMaxGeral, tamBase1Five, tamBase2Five, qtdObs, vQtdAlgGeral);

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                qp5f.setvQtdAlg(new int[]{qtdMaxGeral});
                qp5f.setQtdObservacoes(1); //Basta uma vez                
                qp5f.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseFive1, baseFive2, chavePrimaria, chavePrimaria2, gsFive, goldId1, goldId2, ';', "qp5f", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            qp5f.setvQtdAlg(getvQtdAlgGeral());
            qp5f.setQtdObservacoes(qtdObs);
            qp5f.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        System.out.println("Quantidade de observações geradas: " + qtdObs);

        System.out.println("QPs executaas - qp1: " + okqp1 + " - qp2b: " + okqp2b + " - qp2m: " + okqp2m + " - qp2r: " + okqp2r + " - qp3all: " + okqp3all + " - qp3lot: " + okqp3lot + " - qp5one :" + okqp5one + " - qp5three: " + okqp5three + " - qp5five: " + okqp5five);

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

    public String getBaseGeral() {
        return baseGeral;
    }

    public void setBaseGeral(String base) {
        this.baseGeral = base;
    }

    public String getBaseOne() {
        return baseOne;
    }

    public void setBaseOne(String baseOne) {
        this.baseOne = baseOne;
    }

    public String getBaseThree() {
        return baseThree;
    }

    public void setBaseThree(String baseThree) {
        this.baseThree = baseThree;
    }

    public String getBaseFive() {
        return baseFive;
    }

    public void setBaseFive(String baseFive) {
        this.baseFive = baseFive;
    }

    public void setParamVetorSim(String chavePrimaria, String goldId1, String goldId2, char separator, String chavePrimariaQP5, String goldId1QP5, String goldId2QP5, char separatorQP5, boolean geraVetor, boolean geraVetorQP5) {
        this.chavePrimaria = chavePrimaria;
        this.chavePrimariaQP5 = chavePrimariaQP5;
        this.goldId1 = goldId1;
        this.goldId2 = goldId2;
        this.goldId1QP5 = goldId1QP5;
        this.goldId2QP5 = goldId2QP5;
        this.separator = separator;
        this.separatorQP5 = separatorQP5;
        this.geraVetor = geraVetor;
        this.geraVetorQP5 = geraVetorQP5;
    }

    public void setParamVetorSim(String chavePrimaria1, String chavePrimaria2, String goldId1, String goldId2, char separator, String chavePrimaria1QP5, String chavePrimaria2QP5, String goldId1QP5, String goldId2QP5, char separatorQP5, boolean geraVetor, boolean geraVetorQP5) {
        this.chavePrimaria = chavePrimaria1;
        this.chavePrimaria2 = chavePrimaria2;
        this.chavePrimariaQP5 = chavePrimaria1QP5;
        this.chavePrimaria2QP5 = chavePrimariaQP5;
        this.goldId1 = goldId1;
        this.goldId2 = goldId2;
        this.goldId1QP5 = goldId1QP5;
        this.goldId2QP5 = goldId2QP5;
        this.separator = separator;
        this.separatorQP5 = separatorQP5;
        this.geraVetor = geraVetor;
        this.geraVetorQP5 = geraVetorQP5;
    }

    public int getQtdObs() {
        return qtdObs;
    }

    public void setQtdObs(int qtdObs) {
        this.qtdObs = qtdObs;
    }

    public int[] getvQtdAlgB() {
        return vQtdAlgB;
    }

    public void setvQtdAlgB(int[] vQtdAlgB) {
        this.vQtdAlgB = vQtdAlgB;
    }

    public int[] getvQtdAlgM() {
        return vQtdAlgM;
    }

    public void setvQtdAlgM(int[] vQtdAlgM) {
        this.vQtdAlgM = vQtdAlgM;
    }

    public int[] getvQtdAlgR() {
        return vQtdAlgR;
    }

    public void setvQtdAlgR(int[] vQtdAlgR) {
        this.vQtdAlgR = vQtdAlgR;
    }

    public int[] getvQtdAlgAll() {
        return vQtdAlgAll;
    }

    public void setvQtdAlgAll(int[] vQtdAlgAll) {
        this.vQtdAlgAll = vQtdAlgAll;
    }

    public int[] getvQtdAlgLot() {
        return vQtdAlgLot;
    }

    public void setvQtdAlgLot(int[] vQtdAlgLot) {
        this.vQtdAlgLot = vQtdAlgLot;
    }

    public int[] getvQtdAlgGeral() {
        return vQtdAlgGeral;
    }

    public void setvQtdAlgGeral(int[] vQtdAlgGeral) {
        this.vQtdAlgGeral = vQtdAlgGeral;
    }

    // Copia todos os arquivos de um diretório para o diretório destino
    // Se o diretório destino não existir, ele sera criado automaticamente
    public boolean copia(File srcDir, File dstDir) {
        try {
            if (srcDir.isDirectory()) {
                if (!dstDir.exists()) {
                    dstDir.mkdir();
                }
                String[] children = srcDir.list();
                for (int i = 0; i < children.length; i++) {
                    copia(new File(srcDir, children[i]), new File(dstDir, children[i]));
                }
            } else {
                InputStream in = new FileInputStream(srcDir);
                OutputStream out = new FileOutputStream(dstDir);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }
        } catch (IOException ioex) {
            ioex.printStackTrace();
            return false;
        }
        return true;
    }

    public int calcQtdObs(int itensTotal, int itensComb) {
        //Quantidade de observações corresponde à quantidade de combinações possíveis
        int qtdObservacoes = (int) (fact(itensTotal) / (fact(itensTotal - itensComb) * fact(itensComb)));

        System.out.println("Quantidade de combinações para " + (itensComb) + ": " + qtdObservacoes + ".");

        return qtdObservacoes;
    }

    public static long fact(final int n) {
        if (n < 0) {
            System.err.println("No negative numbers");
            return 0;
        }
        return (n < 2) ? 1 : n * fact(n - 1);
    }

    public void limpaDivergBase(File dir) {

        if (dir.isDirectory()) {
            File[] sun = dir.listFiles();
            for (File toDelete : sun) {

                if (toDelete.getName().contains(Integer.toString(qtdMaxGeral))) {

                    toDelete.delete();
                }
            }
        }

    }

    public void iniciaArq() {
        dirDivergqp1 = new File("./src/csv/conjuntosDS/conjuntosDivergAA/" + baseGeral + "/qp1/");
        dirDivergqp4 = new File("./src/csv/conjuntosDS/conjuntosDivergAA/" + baseGeral + "/qp4/");
        dirDivergqp6 = new File("./src/csv/conjuntosDS/conjuntosDivergAA/" + baseGeral + "/qp6/");
        dirDivergqp7 = new File("./src/csv/conjuntosDS/conjuntosDivergAA/" + baseGeral + "/qp7/");

        dirEstatqp1 = new File("./src/csv/estatisticas/" + baseGeral + "/qp1/");
        dirEstatqp4 = new File("./src/csv/estatisticas/" + baseGeral + "/qp4/");
        dirEstatqp6 = new File("./src/csv/estatisticas/" + baseGeral + "/qp6/");
        dirEstatqp7 = new File("./src/csv/estatisticas/" + baseGeral + "/qp7/");
    }

    public static void main(String[] args) {
        AllQPEtapa11 obj = new AllQPEtapa11();
        System.out.println(obj.calcQtdObs(6, 4));
    }

}
