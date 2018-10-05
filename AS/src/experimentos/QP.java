/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//
package experimentos;

import AS.*;
import DS.DgStd1;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Superclasse para Questões de pesquisa
 *
 * @author Diego
 */
public class QP {

    File gs = null;
    String base = null; //"cd", "dblp-acm", "cd1%"...
    String qp = null; //Questão de pesquisa
    File algSort = null;
    int qtdAlg; //Quantidade de algoritmos de resolução de entidades não supervisionados utilizados no processo
    int tamBase1, tamBase2, qtdObservacoes; //Quantidade de observações a serem geradas para os experimentos (ANTES ERAM 1000)
    boolean isDedup;
    int[] vQtdAlg = null; //{10, 15, 20};//, 25}; - Quantidades de algoritmos para geração das observações

    //Para deduplicação
    /**
     *
     * @param gabarito Nome do arquivo que armazena o gabarito (<i>gold
     * standard</i>).
     * @param base Nome do diretório onde ficarão armazenados os arquivos
     * resultantes dos experimentos. Também nome do diretório onde ficam armazenados os <i>matchers</i> para cada base. Ex.: "cd". 
     * @param qp Questão de pesquisa para o experimento a ser dodado. Ex.: QP1.
     * @param qtdAlg Quantidade total de <i>matchers</i> desenvolvidos para a(s)
     * base(s) de dados tratada.
     * @param tamBase1 Quantidade de elementos na base de dados.
     * @param vQtdAlg Blocos com quantidades variáveis de <i>matchers</i>.
     * @param qtdObs Quantidade de observações a serem geradas no experimento.
     */
    public QP(String gabarito, String base, String qp, int qtdAlg, int tamBase1, int[] vQtdAlg, int qtdObs) {
        gs = new File("./src/csv/datasets", gabarito);
        this.base = base;
        this.qp = qp;
        this.qtdObservacoes = qtdObs;
        this.qtdAlg = qtdAlg;
        this.tamBase1 = tamBase1;
        this.isDedup = true;
        this.vQtdAlg = vQtdAlg;
    }

    //Para record linkage
    /**
     *
     * @param gabarito Nome do arquivo que armazena o gabarito (<i>gold
     * standard</i>).
     * @param base Nome do diretório onde ficarão armazenados os arquivos
     * resultantes dos experimentos. Também nome do diretório onde ficam armazenados os <i>matchers</i> para cada base. Ex.: "cd". 
     * @param qp Questão de pesquisa para o experimento a ser dodado. Ex.: QP1.
     * @param qtdAlg Quantidade total de <i>matchers</i> desenvolvidos para a(s)
     * base(s) de dados tratada.
     * @param tamBase1 Quantidade de elementos na base de dados 1.
     * @param tamBase2 Quantidade de elementos na base de dados 2.
     * @param vQtdAlg Blocos com quantidades variáveis de <i>matchers</i>.
     * @param qtdObs Quantidade de observações a serem geradas no experimento.
     */
    public QP(String gabarito, String base, String qp, int qtdAlg, int tamBase1, int tamBase2, int[] vQtdAlg, int qtdObs) {
        gs = new File("./src/csv/datasets", gabarito);
        this.base = base;
        this.qp = qp;
        this.qtdObservacoes = qtdObs;
        this.qtdAlg = qtdAlg;
        this.tamBase1 = tamBase1;
        this.tamBase2 = tamBase2;
        this.isDedup = false;
        this.vQtdAlg = vQtdAlg;
    }

    /**
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void executa() throws IOException, InterruptedException {

        Map<ArrayList<Integer>, ArrayList<Integer>> mapAlgsGerados = new HashMap<ArrayList<Integer>, ArrayList<Integer>>();

//        AnnStd objAS = new AnnStd(gs);
//        DgStd1 objDS = new DgStd1(gs);
        AnnStd objAS = new AnnStd(gs, base, qp);
        DgStd1 objDS = new DgStd1(gs, base, qp);

//        String abordagemAA = "Dg"; //Pt - Peter Christen ou Dg - Diego Araújo
//
//        //Se for a abordagem de AA proposta nesse trabalho, não copia os arquivos de divergência convencionais,
//        //pois será necessário copiar os arquivos de divergência com as estatísticas (méd, mín, máx).
//        if (abordagemAA.equals("Dg")) {
//            objDS.setCopiaArqDiverg(false);
//        } else {
//            objDS.setCopiaArqDiverg(true);
//        }
        long seed = 500;

        //CONFIGURAÇÃO DOS DADOS REFERENTES AO EXPERIMENTO
        objAS.setGs(gs);
        objDS.setGs(gs);

        objDS.setDirDiverg(base, qp);

        objAS.setDedup(isDedup);
        objDS.setDedup(isDedup);

        objAS.setTamBaseOrig(tamBase1); //Necessário!
        objAS.setTamBaseOrig2(tamBase2); //Necessário!
        objDS.setTamBaseOrig(tamBase1); //Necessário!
        objAS.setTamBaseOrig2(tamBase2); //Necessário!

        //CONFIGURAÇÃO DOS DADOS REFERENTES AO EXPERIMENTO
        File[] resultados = new File[qtdAlg];
        for (int i = 0; i < resultados.length; ++i) {
            int index = i + 1;
            //O diretório que segue abaixo tem que ser setado de acordo com a base de dados utilizada
//            resultados[i] = new File("./src/csv/resultsDedup", "resultado" + index + ".csv"); 
            resultados[i] = new File("./src/csv/resultsDedup/" + base, "resultado" + index + ".csv");
            System.out.println(resultados[i].getAbsoluteFile());
        }

        //Padronização dos arquivos
        File[] resultadosPadr = new File[qtdAlg];

        ArrayList<Integer> listaAlg = null;

        for (int i = 0; i < resultadosPadr.length; ++i) {
            resultadosPadr[i] = objAS.padronizaCsvFile(resultados[i]);
//            resultadosPadr[i] = objDS.padronizaCsvFile(resultados[i]);
        }
        
        File dirAlgs = new File("./src/csv/listasAlgoritmos/" + base + "/" + qp);

            try {
                if (!dirAlgs.exists()) {
                    dirAlgs.mkdirs();
                    System.out.println("Diretório " + dirAlgs.getAbsoluteFile() + " criado!");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        

        for (int qtdAlgUt : vQtdAlg) { //Adicionado depois

            algSort = null;

            algSort = new File(dirAlgs, "algoritmos" + qtdAlgUt + ".csv");

            System.out.println("Quantidade de algoritmos: " + qtdAlgUt);

            //Gerando observações através de seleção aleatória de n algoritmos de deduplicação
            for (int i = 1; i <= qtdObservacoes; i++) {

                listaAlg = null;

                listaAlg = geraOrdAlg(qtdAlgUt, seed, qtdAlg);

                //Verifica se a sequência gerada não já foi utilizada antes
                if (!mapAlgsGerados.containsKey(listaAlg)) {

                    mapAlgsGerados.put(listaAlg, listaAlg);

//                if (!buscaAlgoritmos(algSort, listaAlg)) {
//                    gravaAlgoritmos(algSort, listaAlg);
                    objAS.setPermutacao(i);
                    objAS.setQtdAlg(qtdAlgUt);
                    objAS.limpaTudo();

                    objDS.setPermutacao(i);
                    objDS.setQtdAlg(qtdAlgUt);
                    objDS.limpaTudo();
                    System.out.println("Iteração " + i);

                    int alg = 0;

                    for (int index : listaAlg) {
//                        System.out.println("AQUI");||
//                        System.out.println(index + ",");
                        alg++;

                        objAS.comparaConjuntos(resultadosPadr[index]);

                        if (alg == listaAlg.size()) { //Gerar estatísticas só na última iteração
//                            System.out.println("último algoritmo: " + alg);
                            objDS.setGeraEst(true);
                        }
//                        System.out.println("resultadosPadr[index]: " + resultadosPadr[index].getName());
                        objDS.comparaConjuntos(resultadosPadr[index]);
                    }

                    //Isso aqui só executa depois da última iteração, então acho que dá pra ser chamado dentor de DgStd1
                    {
//                        objDS.contabilizaEstatDA2(objDS.getHistoricoDA());
//                        objDS.contabilizaEstatNAODA2(objDS.getHistoricoNAODA());
//
//                        objDS.filtraDivergencias_NEW2(objDS.getEstatDA(), objDS.getEstatNAODA());
//
//                        objDS.incrementaEstatNAO_DA();
//
//                        //Necessário, pois precisa dos dados gerados após a contabilização das estatísticas
//                        objDS.copiaArqDivergAA(); //Deve ser obrigatorieamente chamado quando se for aplicar a estratégia
                        //de AA proposta.
                    }

                } else {
//                    System.out.println("Entrou no else");
                    i--;
                }
                seed += 10;
            }
            gravaAlgoritmos(algSort, mapAlgsGerados);

            mapAlgsGerados.clear();//COLOQUEI ISSO AQUI, DIEGO!

            java.awt.Toolkit.getDefaultToolkit().beep();

        }

    }

    //Gera ordem aleatória de algoritmos sem repetição dessa
    /**
     *
     * @param qtdAlgUt
     * @param seed
     * @param maxAlgUt
     * @return
     */
    public ArrayList<Integer> geraOrdAlg(int qtdAlgUt, long seed, int maxAlgUt) { //Esse static precisa mesmo?

        ArrayList<Integer> aux = new ArrayList<Integer>();
        Random gerador = new Random(seed);

        while (aux.size() < qtdAlgUt) {

            int randomNum = gerador.nextInt(maxAlgUt);

            if (!aux.contains(randomNum)) {
                aux.add(randomNum);
            }

        }

        aux.sort(null);

        return aux;

    }

    //Grava uma lista de algoritmos em um arquivo dedicado a manter o histórico de algoritmos selecionados 
    //para compor a amostra para os experimentos
    /**
     *
     * @param arqSeqAlgs
     * @param mapAlgs
     * @throws IOException
     */
    public void gravaAlgoritmos(File arqSeqAlgs, Map<ArrayList<Integer>, ArrayList<Integer>> mapAlgs) throws IOException {

        FileWriter escreveAlgs;
        BufferedWriter bwAlg = null;

        try {

            escreveAlgs = new FileWriter(arqSeqAlgs); //Desta forma sobrescreve
            bwAlg = new BufferedWriter(escreveAlgs);

            for (Map.Entry<ArrayList<Integer>, ArrayList<Integer>> lista : mapAlgs.entrySet()) {

                bwAlg.write(lista.getValue() + "\n");

            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DgStd1.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(DgStd1.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            bwAlg.flush();
            bwAlg.close();
            escreveAlgs = null;
            bwAlg = null;
        }

    }

//    public static void main(String[] args) throws IOException, InterruptedException {
//        QP obj = new QP();
//        obj.executa();
//    }
}
