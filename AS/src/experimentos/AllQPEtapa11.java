/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experimentos;

import DS.VetorSimEstat11;
import java.io.IOException;

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
//    int vQtdAlgB, vQtdAlgM, vQtdAlgR, vQtdAlgAll, vQtdAlgLot;
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

    }

    /**
     * Executa todos os experimentos para deduplicação.
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void rodaExpDedup() throws IOException, InterruptedException {

        //Para qp1
        if (okqp1) {
//            int[] vetor = {10, 15, 20}; //Poderia passar direto nos argumentos assim: new int[]{10, 15, 20}
            Etapa1Experimento qp1 = new Etapa1Experimento(gsGeral, baseGeral, "qp1", qtdMaxGeral, tamBase1Geral, qtdObs, vQtdAlgGeral);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp1.setvQtdAlg(getvQtdAlgGeral());
                qp1.setQtdObservacoes(1); //Basta uma vez                
                qp1.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral, chavePrimaria, gsGeral, goldId1, goldId2, ';', "qp1", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgGeral(vetorBKP);
            qp1.setvQtdAlg(getvQtdAlgGeral());
            qp1.setQtdObservacoes(qtdObs);
            qp1.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp2  - Bons
        if (okqp2b) {
            int[] vetor2 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp2b = new Etapa1Experimento(gsGeral, baseGeral, "qp2b", qtdMaxB, tamBase1Geral, qtdObs, vQtdAlgB);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp2b.setvQtdAlg(getvQtdAlgGeral());
                qp2b.setQtdObservacoes(1); //Basta uma vez                
                qp2b.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral, chavePrimaria, gsGeral, goldId1, goldId2, ';', "qp2b", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgB(vetorBKP);
            qp2b.setvQtdAlg(getvQtdAlgB());
            qp2b.setQtdObservacoes(qtdObs);
            qp2b.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores

        } //Para qp2  - Médios
        if (okqp2m) {
            int vetor3 = 10; //Uma única quantidade a ser definida
            Etapa1Experimento qp2m = new Etapa1Experimento(gsGeral, baseGeral, "qp2m", qtdMaxM, tamBase1Geral, qtdObs, vQtdAlgM);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp2m.setvQtdAlg(getvQtdAlgGeral());
                qp2m.setQtdObservacoes(1); //Basta uma vez                
                qp2m.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral, chavePrimaria, gsGeral, goldId1, goldId2, ';', "qp2m", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgM(vetorBKP);
            qp2m.setvQtdAlg(getvQtdAlgM());
            qp2m.setQtdObservacoes(qtdObs);
            qp2m.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp2  - Ruins
        if (okqp2r) {
            int[] vetor4 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp2r = new Etapa1Experimento(gsGeral, baseGeral, "qp2r", qtdMaxR, tamBase1Geral, qtdObs, vQtdAlgR);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp2r.setvQtdAlg(getvQtdAlgGeral());
                qp2r.setQtdObservacoes(1); //Basta uma vez                
                qp2r.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral, chavePrimaria, gsGeral, goldId1, goldId2, ';', "qp2r", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgR(vetorBKP);
            qp2r.setvQtdAlg(getvQtdAlgR());
            qp2r.setQtdObservacoes(qtdObs);
            qp2r.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp3  - Todos
        if (okqp3all) {
            int[] vetor5 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp3All = new Etapa1Experimento(gsGeral, baseGeral, "qp3all", qtdMaxAll, tamBase1Geral, qtdObs, vQtdAlgAll);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp3All.setvQtdAlg(getvQtdAlgGeral());
                qp3All.setQtdObservacoes(1); //Basta uma vez                
                qp3All.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral, chavePrimaria, gsGeral, goldId1, goldId2, ';', "qp3all", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgAll(vetorBKP);
            qp3All.setvQtdAlg(getvQtdAlgAll());
            qp3All.setQtdObservacoes(qtdObs);
            qp3All.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp3  - Parte
        if (okqp3lot) {
            int[] vetor6 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp3Lot = new Etapa1Experimento(gsGeral, baseGeral, "qp3lot", qtdMaxLot, tamBase1Geral, qtdObs, vQtdAlgLot);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp3Lot.setvQtdAlg(getvQtdAlgGeral());
                qp3Lot.setQtdObservacoes(1); //Basta uma vez                
                qp3Lot.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral, chavePrimaria, gsGeral, goldId1, goldId2, ';', "qp3lot", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgLot(vetorBKP);
            qp3Lot.setvQtdAlg(getvQtdAlgLot());
            qp3Lot.setQtdObservacoes(qtdObs);
            qp3Lot.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores

        }

        //Para qp5 - 1% 
        if (okqp5one) {
            int[] vetor8 = {10, 15, 20}; //Uma única quantidade a ser definida
            //A quantidade de elementos da baseGeral de dados aqui é diferente
            Etapa1Experimento qp5o = new Etapa1Experimento(gsOne, baseOne, "qp5o", qtdMaxGeral, tamBase1One, qtdObs, vQtdAlgGeral);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp5o.setvQtdAlg(getvQtdAlgGeral());
                qp5o.setQtdObservacoes(1); //Basta uma vez                
                qp5o.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseOne, chavePrimaria, gsOne, goldId1, goldId2, ';', "qp5o", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgGeral(vetorBKP);
            qp5o.setvQtdAlg(getvQtdAlgGeral());
            qp5o.setQtdObservacoes(qtdObs);
            qp5o.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp5 - 3% 
        if (okqp5three) {
            int[] vetor9 = {10, 15, 20}; //Uma única quantidade a ser definida9
            //A quantidade de elementos da baseGeral de dados aqui é diferente
            Etapa1Experimento qp5t = new Etapa1Experimento(gsThree, baseThree, "qp5t", qtdMaxGeral, tamBase1Three, qtdObs, vQtdAlgGeral);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp5t.setvQtdAlg(getvQtdAlgGeral());
                qp5t.setQtdObservacoes(1); //Basta uma vez                
                qp5t.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseThree, chavePrimaria, gsThree, goldId1, goldId2, ';', "qp5t", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgGeral(vetorBKP);
            qp5t.setvQtdAlg(getvQtdAlgGeral());
            qp5t.setQtdObservacoes(qtdObs);
            qp5t.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores

        }

        //Para qp5 - 5% 
        if (okqp5five) {
            int[] vetor10 = {10, 15, 20}; //Uma única quantidade a ser definida
            //A quantidade de elementos da baseGeral de dados aqui é diferente
            Etapa1Experimento qp5f = new Etapa1Experimento(gsFive, baseFive, "qp5f", qtdMaxGeral, tamBase1Five, qtdObs, vQtdAlgGeral);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp5f.setvQtdAlg(getvQtdAlgGeral());
                qp5f.setQtdObservacoes(1); //Basta uma vez                
                qp5f.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseFive, chavePrimaria, gsFive, goldId1, goldId2, ';', "qp5f", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgGeral(vetorBKP);
            qp5f.setvQtdAlg(getvQtdAlgGeral());
            qp5f.setQtdObservacoes(qtdObs);
            qp5f.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores

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

        //Para qp1
        if (okqp1) {
//            int[] vetor = {10, 15, 20}; //Poderia passar direto nos argumentos assim: new int[]{10, 15, 20}
            Etapa1Experimento qp1 = new Etapa1Experimento(gsGeral, baseGeral, "qp1", qtdMaxGeral, tamBase1Geral, qtdObs, vQtdAlgGeral);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp1.setvQtdAlg(getvQtdAlgGeral());
                qp1.setQtdObservacoes(1); //Basta uma vez                
                qp1.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral, chavePrimaria, gsGeral, goldId1, goldId2, ';', "qp1", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgGeral(vetorBKP);
            qp1.setvQtdAlg(getvQtdAlgGeral());
            qp1.setQtdObservacoes(qtdObs);
            qp1.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp2  - Bons
        if (okqp2b) {
            int[] vetor2 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp2b = new Etapa1Experimento(gsGeral, baseGeral, "qp2b", qtdMaxB, tamBase1Geral, qtdObs, vQtdAlgB);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp2b.setvQtdAlg(getvQtdAlgGeral());
                qp2b.setQtdObservacoes(1); //Basta uma vez                
                qp2b.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral, chavePrimaria, gsGeral, goldId1, goldId2, ';', "qp2b", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgB(vetorBKP);
            qp2b.setvQtdAlg(getvQtdAlgB());
            qp2b.setQtdObservacoes(qtdObs);
            qp2b.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores

        } //Para qp2  - Médios
        if (okqp2m) {
            int vetor3 = 10; //Uma única quantidade a ser definida
            Etapa1Experimento qp2m = new Etapa1Experimento(gsGeral, baseGeral, "qp2m", qtdMaxM, tamBase1Geral, qtdObs, vQtdAlgM);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp2m.setvQtdAlg(getvQtdAlgGeral());
                qp2m.setQtdObservacoes(1); //Basta uma vez                
                qp2m.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral, chavePrimaria, gsGeral, goldId1, goldId2, ';', "qp2m", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgM(vetorBKP);
            qp2m.setvQtdAlg(getvQtdAlgM());
            qp2m.setQtdObservacoes(qtdObs);
            qp2m.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp2  - Ruins
        if (okqp2r) {
            int[] vetor4 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp2r = new Etapa1Experimento(gsGeral, baseGeral, "qp2r", qtdMaxR, tamBase1Geral, qtdObs, vQtdAlgR);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp2r.setvQtdAlg(getvQtdAlgGeral());
                qp2r.setQtdObservacoes(1); //Basta uma vez                
                qp2r.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral, chavePrimaria, gsGeral, goldId1, goldId2, ';', "qp2r", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgR(vetorBKP);
            qp2r.setvQtdAlg(getvQtdAlgR());
            qp2r.setQtdObservacoes(qtdObs);
            qp2r.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp3  - Todos
        if (okqp3all) {
            int[] vetor5 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp3All = new Etapa1Experimento(gsGeral, baseGeral, "qp3all", qtdMaxAll, tamBase1Geral, qtdObs, vQtdAlgAll);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp3All.setvQtdAlg(getvQtdAlgGeral());
                qp3All.setQtdObservacoes(1); //Basta uma vez                
                qp3All.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral, chavePrimaria, gsGeral, goldId1, goldId2, ';', "qp3all", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgAll(vetorBKP);
            qp3All.setvQtdAlg(getvQtdAlgAll());
            qp3All.setQtdObservacoes(qtdObs);
            qp3All.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp3  - Parte
        if (okqp3lot) {
            int[] vetor6 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp3Lot = new Etapa1Experimento(gsGeral, baseGeral, "qp3lot", qtdMaxLot, tamBase1Geral, qtdObs, vQtdAlgLot);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp3Lot.setvQtdAlg(getvQtdAlgGeral());
                qp3Lot.setQtdObservacoes(1); //Basta uma vez                
                qp3Lot.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral, chavePrimaria, gsGeral, goldId1, goldId2, ';', "qp3lot", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgLot(vetorBKP);
            qp3Lot.setvQtdAlg(getvQtdAlgLot());
            qp3Lot.setQtdObservacoes(qtdObs);
            qp3Lot.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores

        }

        //Para qp5 - 1% 
        if (okqp5one) {
            int[] vetor8 = {10, 15, 20}; //Uma única quantidade a ser definida
            //A quantidade de elementos da baseGeral de dados aqui é diferente
            Etapa1Experimento qp5o = new Etapa1Experimento(gsOne, baseOne, "qp5o", qtdMaxGeral, tamBase1One, qtdObs, vQtdAlgGeral);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp5o.setvQtdAlg(getvQtdAlgGeral());
                qp5o.setQtdObservacoes(1); //Basta uma vez                
                qp5o.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseOne, chavePrimaria, gsOne, goldId1, goldId2, ';', "qp5o", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgGeral(vetorBKP);
            qp5o.setvQtdAlg(getvQtdAlgGeral());
            qp5o.setQtdObservacoes(qtdObs);
            qp5o.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp5 - 3% 
        if (okqp5three) {
            int[] vetor9 = {10, 15, 20}; //Uma única quantidade a ser definida9
            //A quantidade de elementos da baseGeral de dados aqui é diferente
            Etapa1Experimento qp5t = new Etapa1Experimento(gsThree, baseThree, "qp5t", qtdMaxGeral, tamBase1Three, qtdObs, vQtdAlgGeral);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp5t.setvQtdAlg(getvQtdAlgGeral());
                qp5t.setQtdObservacoes(1); //Basta uma vez                
                qp5t.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseThree, chavePrimaria, gsThree, goldId1, goldId2, ';', "qp5t", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgGeral(vetorBKP);
            qp5t.setvQtdAlg(getvQtdAlgGeral());
            qp5t.setQtdObservacoes(qtdObs);
            qp5t.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores

        }

        //Para qp5 - 5% 
        if (okqp5five) {
            int[] vetor10 = {10, 15, 20}; //Uma única quantidade a ser definida
            //A quantidade de elementos da baseGeral de dados aqui é diferente
            Etapa1Experimento qp5f = new Etapa1Experimento(gsFive, baseFive, "qp5f", qtdMaxGeral, tamBase1Five, qtdObs, vQtdAlgGeral);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp5f.setvQtdAlg(getvQtdAlgGeral());
                qp5f.setQtdObservacoes(1); //Basta uma vez                
                qp5f.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseFive, chavePrimaria, gsFive, goldId1, goldId2, ';', "qp5f", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgGeral(vetorBKP);
            qp5f.setvQtdAlg(getvQtdAlgGeral());
            qp5f.setQtdObservacoes(qtdObs);
            qp5f.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores

        }

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
            int[] vetor = {10, 15, 20}; //Poderia passar direto nos argumentos assim: new int[]{10, 15, 20}
            Etapa1Experimento qp1 = new Etapa1Experimento(gsGeral, baseGeral1, baseGeral2, "qp1", qtdMaxGeral, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgGeral);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp1.setvQtdAlg(getvQtdAlgGeral());
                qp1.setQtdObservacoes(1); //Basta uma vez                
                qp1.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral1, baseGeral2, chavePrimaria, chavePrimaria2, gsGeral, goldId1, goldId2, ';', "qp1", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgGeral(vetorBKP);
            qp1.setvQtdAlg(getvQtdAlgGeral());
            qp1.setQtdObservacoes(qtdObs);
            qp1.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp2  - Bons
        if (okqp2b) {
            int[] vetor2 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp2b = new Etapa1Experimento(gsGeral, baseGeral1, baseGeral2, "qp2b", qtdMaxB, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgB);
            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp2b.setvQtdAlg(getvQtdAlgGeral());
                qp2b.setQtdObservacoes(1); //Basta uma vez                
                qp2b.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral1, baseGeral2, chavePrimaria, chavePrimaria2, gsGeral, goldId1, goldId2, ';', "qp2b", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgB(vetorBKP);
            qp2b.setvQtdAlg(getvQtdAlgB());
            qp2b.setQtdObservacoes(qtdObs);
            qp2b.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp2  - Médios
        if (okqp2m) {
            int vetor3 = 10; //Uma única quantidade a ser definida
            Etapa1Experimento qp2m = new Etapa1Experimento(gsGeral, baseGeral1, baseGeral2, "qp2m", qtdMaxM, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgM);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp2m.setvQtdAlg(getvQtdAlgGeral());
                qp2m.setQtdObservacoes(1); //Basta uma vez                
                qp2m.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral1, baseGeral2, chavePrimaria, chavePrimaria2, gsGeral, goldId1, goldId2, ';', "qp2m", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgM(vetorBKP);
            qp2m.setvQtdAlg(getvQtdAlgM());
            qp2m.setQtdObservacoes(qtdObs);
            qp2m.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp2  - Ruins
        if (okqp2r) {
            int[] vetor4 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp2r = new Etapa1Experimento(gsGeral, baseGeral1, baseGeral2, "qp2r", qtdMaxR, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgR);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp2r.setvQtdAlg(getvQtdAlgGeral());
                qp2r.setQtdObservacoes(1); //Basta uma vez                
                qp2r.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral1, baseGeral2, chavePrimaria, chavePrimaria2, gsGeral, goldId1, goldId2, ';', "qp2r", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgR(vetorBKP);
            qp2r.setvQtdAlg(getvQtdAlgR());
            qp2r.setQtdObservacoes(qtdObs);
            qp2r.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp3  - Todos
        if (okqp3all) {
            int[] vetor5 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp3All = new Etapa1Experimento(gsGeral, baseGeral1, baseGeral2, "qp3all", qtdMaxAll, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgAll);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp3All.setvQtdAlg(getvQtdAlgGeral());
                qp3All.setQtdObservacoes(1); //Basta uma vez                
                qp3All.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral1, baseGeral2, chavePrimaria, chavePrimaria2, gsGeral, goldId1, goldId2, ';', "qp3all", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgAll(vetorBKP);
            qp3All.setvQtdAlg(getvQtdAlgAll());
            qp3All.setQtdObservacoes(qtdObs);
            qp3All.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp3  - Parte
        if (okqp3lot) {
            int[] vetor6 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp3Lot = new Etapa1Experimento(gsGeral, baseGeral1, baseGeral2, "qp3Lot", qtdMaxLot, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgLot);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp3Lot.setvQtdAlg(getvQtdAlgGeral());
                qp3Lot.setQtdObservacoes(1); //Basta uma vez                
                qp3Lot.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral1, baseGeral2, chavePrimaria, chavePrimaria2, gsGeral, goldId1, goldId2, ';', "qp3lot", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgLot(vetorBKP);
            qp3Lot.setvQtdAlg(getvQtdAlgLot());
            qp3Lot.setQtdObservacoes(qtdObs);
            qp3Lot.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp5 - 1% 
        if (okqp5one) {
            int[] vetor8 = {10, 15, 20}; //Uma única quantidade a ser definida
            //A quantidade de elementos da baseGeral de dados aqui é diferente
            Etapa1Experimento qp5o = new Etapa1Experimento(gsOne, baseOne1, baseOne2, "qp5o", qtdMaxGeral, tamBase1One, tamBase2One, qtdObs, vQtdAlgGeral);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp5o.setvQtdAlg(getvQtdAlgGeral());
                qp5o.setQtdObservacoes(1); //Basta uma vez                
                qp5o.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseOne1, baseOne2, chavePrimaria, chavePrimaria2, gsOne, goldId1, goldId2, ';', "qp5o", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgGeral(vetorBKP);
            qp5o.setvQtdAlg(getvQtdAlgGeral());
            qp5o.setQtdObservacoes(qtdObs);
            qp5o.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp5 - 3% 
        if (okqp5three) {
            int[] vetor9 = {10, 15, 20}; //Uma única quantidade a ser definida9
            //A quantidade de elementos da baseGeral de dados aqui é diferente
            Etapa1Experimento qp5t = new Etapa1Experimento(gsThree, baseThree1, baseThree2, "qp5t", qtdMaxGeral, tamBase1Three, tamBase2Three, qtdObs, vQtdAlgGeral);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp5t.setvQtdAlg(getvQtdAlgGeral());
                qp5t.setQtdObservacoes(1); //Basta uma vez                
                qp5t.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseThree1, baseThree2, chavePrimaria, chavePrimaria2, gsThree, goldId1, goldId2, ';', "qp5t", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgGeral(vetorBKP);
            qp5t.setvQtdAlg(getvQtdAlgGeral());
            qp5t.setQtdObservacoes(qtdObs);
            qp5t.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp5 - 5% 
        if (okqp5five) {
            int[] vetor10 = {10, 15, 20}; //Uma única quantidade a ser definida
            //A quantidade de elementos da baseGeral de dados aqui é diferente
            Etapa1Experimento qp5f = new Etapa1Experimento(gsFive, baseFive1, baseFive2, "qp5f", qtdMaxGeral, tamBase1Five, tamBase2Five, qtdObs, vQtdAlgGeral);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp5f.setvQtdAlg(getvQtdAlgGeral());
                qp5f.setQtdObservacoes(1); //Basta uma vez                
                qp5f.executa(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseFive1, baseFive2, chavePrimaria, chavePrimaria2, gsFive, goldId1, goldId2, ';', "qp5f", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgGeral(vetorBKP);
            qp5f.setvQtdAlg(getvQtdAlgGeral());
            qp5f.setQtdObservacoes(qtdObs);
            qp5f.executa();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
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

        //Para qp1
        if (okqp1) {
            int[] vetor = {10, 15, 20}; //Poderia passar direto nos argumentos assim: new int[]{10, 15, 20}
            Etapa1Experimento qp1 = new Etapa1Experimento(gsGeral, baseGeral1, baseGeral2, "qp1", qtdMaxGeral, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgGeral);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp1.setvQtdAlg(getvQtdAlgGeral());
                qp1.setQtdObservacoes(1); //Basta uma vez                
                qp1.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral1, baseGeral2, chavePrimaria, chavePrimaria2, gsGeral, goldId1, goldId2, ';', "qp1", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgGeral(vetorBKP);
            qp1.setvQtdAlg(getvQtdAlgGeral());
            qp1.setQtdObservacoes(qtdObs);
            qp1.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp2  - Bons
        if (okqp2b) {
            int[] vetor2 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp2b = new Etapa1Experimento(gsGeral, baseGeral1, baseGeral2, "qp2b", qtdMaxB, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgB);
            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp2b.setvQtdAlg(getvQtdAlgGeral());
                qp2b.setQtdObservacoes(1); //Basta uma vez                
                qp2b.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral1, baseGeral2, chavePrimaria, chavePrimaria2, gsGeral, goldId1, goldId2, ';', "qp2b", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgB(vetorBKP);
            qp2b.setvQtdAlg(getvQtdAlgB());
            qp2b.setQtdObservacoes(qtdObs);
            qp2b.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp2  - Médios
        if (okqp2m) {
            int vetor3 = 10; //Uma única quantidade a ser definida
            Etapa1Experimento qp2m = new Etapa1Experimento(gsGeral, baseGeral1, baseGeral2, "qp2m", qtdMaxM, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgM);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp2m.setvQtdAlg(getvQtdAlgGeral());
                qp2m.setQtdObservacoes(1); //Basta uma vez                
                qp2m.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral1, baseGeral2, chavePrimaria, chavePrimaria2, gsGeral, goldId1, goldId2, ';', "qp2m", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgM(vetorBKP);
            qp2m.setvQtdAlg(getvQtdAlgM());
            qp2m.setQtdObservacoes(qtdObs);
            qp2m.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp2  - Ruins
        if (okqp2r) {
            int[] vetor4 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp2r = new Etapa1Experimento(gsGeral, baseGeral1, baseGeral2, "qp2r", qtdMaxR, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgR);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp2r.setvQtdAlg(getvQtdAlgGeral());
                qp2r.setQtdObservacoes(1); //Basta uma vez                
                qp2r.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral1, baseGeral2, chavePrimaria, chavePrimaria2, gsGeral, goldId1, goldId2, ';', "qp2r", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgR(vetorBKP);
            qp2r.setvQtdAlg(getvQtdAlgR());
            qp2r.setQtdObservacoes(qtdObs);
            qp2r.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp3  - Todos
        if (okqp3all) {
            int[] vetor5 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp3All = new Etapa1Experimento(gsGeral, baseGeral1, baseGeral2, "qp3all", qtdMaxAll, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgAll);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp3All.setvQtdAlg(getvQtdAlgGeral());
                qp3All.setQtdObservacoes(1); //Basta uma vez                
                qp3All.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral1, baseGeral2, chavePrimaria, chavePrimaria2, gsGeral, goldId1, goldId2, ';', "qp3all", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgAll(vetorBKP);
            qp3All.setvQtdAlg(getvQtdAlgAll());
            qp3All.setQtdObservacoes(qtdObs);
            qp3All.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp3  - Parte
        if (okqp3lot) {
            int[] vetor6 = {10}; //Uma única quantidade a ser definida
            Etapa1Experimento qp3Lot = new Etapa1Experimento(gsGeral, baseGeral1, baseGeral2, "qp3Lot", qtdMaxLot, tamBase1Geral, tamBase2Geral, qtdObs, vQtdAlgLot);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp3Lot.setvQtdAlg(getvQtdAlgGeral());
                qp3Lot.setQtdObservacoes(1); //Basta uma vez                
                qp3Lot.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseGeral1, baseGeral2, chavePrimaria, chavePrimaria2, gsGeral, goldId1, goldId2, ';', "qp3lot", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgLot(vetorBKP);
            qp3Lot.setvQtdAlg(getvQtdAlgLot());
            qp3Lot.setQtdObservacoes(qtdObs);
            qp3Lot.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp5 - 1% 
        if (okqp5one) {
            int[] vetor8 = {10, 15, 20}; //Uma única quantidade a ser definida
            //A quantidade de elementos da baseGeral de dados aqui é diferente
            Etapa1Experimento qp5o = new Etapa1Experimento(gsOne, baseOne1, baseOne2, "qp5o", qtdMaxGeral, tamBase1One, tamBase2One, qtdObs, vQtdAlgGeral);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp5o.setvQtdAlg(getvQtdAlgGeral());
                qp5o.setQtdObservacoes(1); //Basta uma vez                
                qp5o.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseOne1, baseOne2, chavePrimaria, chavePrimaria2, gsOne, goldId1, goldId2, ';', "qp5o", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgGeral(vetorBKP);
            qp5o.setvQtdAlg(getvQtdAlgGeral());
            qp5o.setQtdObservacoes(qtdObs);
            qp5o.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp5 - 3% 
        if (okqp5three) {
            int[] vetor9 = {10, 15, 20}; //Uma única quantidade a ser definida9
            //A quantidade de elementos da baseGeral de dados aqui é diferente
            Etapa1Experimento qp5t = new Etapa1Experimento(gsThree, baseThree1, baseThree2, "qp5t", qtdMaxGeral, tamBase1Three, tamBase2Three, qtdObs, vQtdAlgGeral);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp5t.setvQtdAlg(getvQtdAlgGeral());
                qp5t.setQtdObservacoes(1); //Basta uma vez                
                qp5t.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseThree1, baseThree2, chavePrimaria, chavePrimaria2, gsThree, goldId1, goldId2, ';', "qp5t", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgGeral(vetorBKP);
            qp5t.setvQtdAlg(getvQtdAlgGeral());
            qp5t.setQtdObservacoes(qtdObs);
            qp5t.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
        }

        //Para qp5 - 5% 
        if (okqp5five) {
            int[] vetor10 = {10, 15, 20}; //Uma única quantidade a ser definida
            //A quantidade de elementos da baseGeral de dados aqui é diferente
            Etapa1Experimento qp5f = new Etapa1Experimento(gsFive, baseFive1, baseFive2, "qp5f", qtdMaxGeral, tamBase1Five, tamBase2Five, qtdObs, vQtdAlgGeral);

            int[] vetorBKP = getvQtdAlgGeral();

            //Gera o arquivo de divergências com todos os algoritmos (NAO_DA)
            if (geraVetor) {
                setvQtdAlgGeral(new int[]{qtdMaxGeral});
                qp5f.setvQtdAlg(getvQtdAlgGeral());
                qp5f.setQtdObservacoes(1); //Basta uma vez                
                qp5f.executa2(); //Gera os arquivos de divergências com todos os algoritmos (NAO_DA)
            }

            //Mesma ideia do construtor
            objVet.setAllVarDedup(baseFive1, baseFive2, chavePrimaria, chavePrimaria2, gsFive, goldId1, goldId2, ';', "qp5f", geraVetor);

            //Retornando às quantidades de matchers passadas como parâmetros para geração dos vetores de similaridade menores
            setvQtdAlgGeral(vetorBKP);
            qp5f.setvQtdAlg(getvQtdAlgGeral());
            qp5f.setQtdObservacoes(qtdObs);
            qp5f.executa2();

            objVet.executaGerVetMenor(); //Para gerar os vetores menores
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

    public void setParamVetorSim(String chavePrimaria, String goldId1, String goldId2, char separator, String chavePrimariaQP5, String goldId1QP5, String goldId2QP5, char separatorQP5, boolean geraVetor) {
        this.chavePrimaria = chavePrimaria;
        this.chavePrimariaQP5 = chavePrimariaQP5;
        this.goldId1 = goldId1;
        this.goldId2 = goldId2;
        this.goldId1QP5 = goldId1QP5;
        this.goldId2QP5 = goldId2QP5;
        this.separator = separator;
        this.separatorQP5 = separatorQP5;
        this.geraVetor = geraVetor;
    }

    public void setParamVetorSim(String chavePrimaria1, String chavePrimaria2, String goldId1, String goldId2, char separator, String chavePrimaria1QP5, String chavePrimaria2QP5, String goldId1QP5, String goldId2QP5, char separatorQP5,  boolean geraVetor) {
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

}
