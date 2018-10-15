/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//
package experimentos;

import AS.*;
import DS.DgStd1;
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
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Possui os métodos que organizam a geração de conjuntos de divergência, 
 * assim como a geração das estatísticas correspondentes.
 *
 * @author Diego
 */
public class Etapa1Experimento {

    File gs = null;
    String dirResult = null; //"cd", "cd1%"...
    String base = null; //"cd", "dblp-acm"...
    String qp = null; //Questão de pesquisa
    File algSort = null;
    int qtdMaxAlg; //Quantidade de algoritmos de resolução de entidades não supervisionados utilizados no processo
    int tamBase1, tamBase2, qtdObservacoes; //Quantidade de observações a serem geradas para os experimentos (ANTES ERAM 1000)
    boolean isDedup;
    int[] vQtdAlg = null; //{10, 15, 20};//, 25}; - Quantidades de algoritmos para geração das observações

    //Para deduplicação
    /**
     *
     * @param gabarito Nome do arquivo que armazena o gabarito (<i>gold
     * standard</i>).
     * @param base Nome do diretório onde ficarão armazenados os arquivos
     * resultantes dos experimentos. Também nome do diretório onde ficam
     * armazenados os <i>matchers</i> para cada base. Ex.: "cd".
     * @param qp Questão de pesquisa para o experimento a ser dodado. Ex.: QP1.
     * @param qtdMaxAlg Quantidade total de <i>matchers</i> desenvolvidos para a(s)
     * base(s) de dados tratada.
     * @param tamBase1 Quantidade de elementos na base de dados.
     * @param qtdObs Quantidade de observações a serem geradas no experimento.
     * @param vQtdAlg Blocos com quantidades variáveis de <i>matchers</i>.
     */
    public Etapa1Experimento(String gabarito, String base, String qp, int qtdMaxAlg, int tamBase1, int qtdObs, int[] vQtdAlg) {
        limpaEstatisticas(base, qp);        
        gs = new File("./src/csv/datasets", gabarito);
        this.base = base;
        this.qp = qp;
        this.qtdObservacoes = qtdObs;
        this.qtdMaxAlg = qtdMaxAlg;
        this.tamBase1 = tamBase1;
        this.isDedup = true;
        this.vQtdAlg = vQtdAlg;
        dirResult = base+"-"+qp;
    }

    //Para record linkage
    /**
     *
     * @param gabarito Nome do arquivo que armazena o gabarito (<i>gold
     * standard</i>).
     * @param base Nome do diretório onde ficarão armazenados os arquivos
     * resultantes dos experimentos. Também nome do diretório onde ficam
     * armazenados os <i>matchers</i> para cada base. Ex.: "cd".
     * @param qp Questão de pesquisa para o experimento a ser dodado. Ex.: QP1.
     * @param qtdMaxAlg Quantidade total de <i>matchers</i> desenvolvidos para a(s)
     * base(s) de dados tratada.
     * @param tamBase1 Quantidade de elementos na base de dados 1.
     * @param tamBase2 Quantidade de elementos na base de dados 2.
     * @param qtdObs Quantidade de observações a serem geradas no experimento.
     * @param vQtdAlg Blocos com quantidades variáveis de <i>matchers</i>.
     */
    public Etapa1Experimento(String gabarito, String base1, String base2, String qp, int qtdMaxAlg, int tamBase1, int tamBase2, int qtdObs, int... vQtdAlg) {
        limpaEstatisticas(base, qp);
        gs = new File("./src/csv/datasets", gabarito);
        this.base = base1+"-"+base2;
        this.qp = qp;
        this.qtdObservacoes = qtdObs;
        this.qtdMaxAlg = qtdMaxAlg;
        this.tamBase1 = tamBase1;
        this.tamBase2 = tamBase2;
        this.isDedup = false;
        this.vQtdAlg = vQtdAlg;
        dirResult = base+"-"+qp;
    }

    

    /**
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void executa() throws IOException, InterruptedException {

        Map<ArrayList<Integer>, ArrayList<Integer>> mapAlgsGerados = new HashMap<ArrayList<Integer>, ArrayList<Integer>>();

        AnnStd objAS = new AnnStd(gs, base, qp);
        DgStd1 objDS = new DgStd1(gs, base, qp);

        //CONFIGURAÇÃO DOS DADOS REFERENTES AO EXPERIMENTO
        long seed = 500;

        objAS.setGs(gs);
        objDS.setGs(gs);

//        objDS.setDirDiverg(base, qp);
        objAS.setDedup(isDedup);
        objDS.setDedup(isDedup);

        objAS.setTamBaseOrig(tamBase1); //Necessário!
        objAS.setTamBaseOrig2(tamBase2); //Necessário!
        objDS.setTamBaseOrig(tamBase1); //Necessário!
        objAS.setTamBaseOrig2(tamBase2); //Necessário!

        //CONFIGURAÇÃO DOS DADOS REFERENTES AO EXPERIMENTO
        File[] resultados = new File[qtdMaxAlg];
        for (int i = 0; i < resultados.length; ++i) {
            int index = i + 1;
            resultados[i] = new File("./src/csv/resultsDedup/" + dirResult, "resultado" + index + ".csv");
        }

        //Padronização dos arquivos
        File[] resultadosPadr = new File[qtdMaxAlg];

        ArrayList<Integer> listaAlg = null;

        for (int i = 0; i < resultadosPadr.length; ++i) {
            resultadosPadr[i] = objAS.padronizaCsvFile(resultados[i]);
//            resultadosPadr[i] = objDS.padronizaCsvFile(resultados[i]);
        }

        //Armazenando lista das sequências de algoritmos geradas para eventual futuro uso.
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
            for (int i = 1; i <= getQtdObservacoes(); i++) {

                listaAlg = null;

                listaAlg = geraOrdAlg(qtdAlgUt, seed, qtdMaxAlg);

                //Verifica se a sequência gerada não já foi utilizada antes
                if (!mapAlgsGerados.containsKey(listaAlg)) {

                    mapAlgsGerados.put(listaAlg, listaAlg);

                    objAS.setPermutacao(i);
                    objAS.setQtdAlg(qtdAlgUt);
                    objAS.limpaTudo();

                    objDS.setPermutacao(i);
                    objDS.setQtdAlg(qtdAlgUt);
                    objDS.limpaTudo();
                    System.out.println("Iteração " + i);

                    int alg = 0;

                    for (int index : listaAlg) {
                        alg++;

                        objAS.comparaConjuntos(resultadosPadr[index]);

                        if (alg == listaAlg.size()) { //Gerar estatísticas só na última iteração
                            objDS.setGeraEst(true);
                        }
                        objDS.comparaConjuntos(resultadosPadr[index]);
                    }

                    //Isso aqui só executa depois da última iteração, então acho que dá pra ser chamado dentor de DgStd1
                    //E foi!
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
                    i--;
                }
                seed += 10;
            }
            //Armazena as sequências geradas em um arquivo.
            gravaAlgoritmos(algSort, mapAlgsGerados);

            mapAlgsGerados.clear();

            java.awt.Toolkit.getDefaultToolkit().beep();

        }

    }

    public int getQtdObservacoes() {
        return qtdObservacoes;
    }

    public void setQtdObservacoes(int qtdObservacoes) {
        this.qtdObservacoes = qtdObservacoes;
    }

    /**
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void executa2() throws IOException, InterruptedException {

        Map<ArrayList<Integer>, ArrayList<Integer>> mapAlgsGerados = new HashMap<ArrayList<Integer>, ArrayList<Integer>>();

        AnnStd objAS = new AnnStd(gs, base, qp);
        DgStd1 objDS = new DgStd1(gs, base, qp);

        //CONFIGURAÇÃO DOS DADOS REFERENTES AO EXPERIMENTO
        long seed = 500;

        objAS.setGs(gs);
        objDS.setGs(gs);

//        objDS.setDirDiverg(base, qp);
        objAS.setDedup(isDedup);
        objDS.setDedup(isDedup);

        objAS.setTamBaseOrig(tamBase1); //Necessário!
        objAS.setTamBaseOrig2(tamBase2); //Necessário!
        objDS.setTamBaseOrig(tamBase1); //Necessário!
        objAS.setTamBaseOrig2(tamBase2); //Necessário!

        //CONFIGURAÇÃO DOS DADOS REFERENTES AO EXPERIMENTO
        File[] resultados = new File[qtdMaxAlg];
        for (int i = 0; i < resultados.length; ++i) {
            int index = i + 1;
            resultados[i] = new File("./src/csv/resultsDedup/" + base, "resultado" + index + ".csv");
        }

        //Padronização dos arquivos
        File[] resultadosPadr = new File[qtdMaxAlg];

        ArrayList<Integer> listaAlg = null;

        for (int i = 0; i < resultadosPadr.length; ++i) {
            resultadosPadr[i] = objAS.padronizaCsvFile(resultados[i]);
//            resultadosPadr[i] = objDS.padronizaCsvFile(resultados[i]);
        }

        //Armazenando lista das sequências de algoritmos geradas para eventual futuro uso.
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

            BufferedReader brArq = null;

            try {
                brArq = new BufferedReader(new FileReader(algSort.getPath()));

                String str = null;

                int i = 0;

                listaAlg = new ArrayList<Integer>();

                while ((str = brArq.readLine()) != null) {

                    str = str.replace("[", " ");
                    str = str.replace("]", " ");
                    str = str.replace(" ", "");
                    listaAlg.clear();

                    String elementos[] = str.split(",");
//                    

                    for (String num : elementos) {
                        if (!num.equals("[") & !num.equals("]") & !num.equals(",") & !num.equals(" ")) {

                            listaAlg.add(Integer.parseInt(num));
                        }
                    }

                    System.out.println(listaAlg);

                    i++;

                    if (true) {

                        objAS.setPermutacao(i);
                        objAS.setQtdAlg(qtdAlgUt);
                        objAS.limpaTudo();

                        objDS.setPermutacao(i);
                        objDS.setQtdAlg(qtdAlgUt);
                        objDS.limpaTudo();
                        System.out.println("Iteração " + i);

                        int alg = 0;

                        for (int index : listaAlg) {

                            alg++;

                            objAS.comparaConjuntos(resultadosPadr[index]);

                            if (alg == listaAlg.size()) { //Gerar estatísticas só na última iteração
                                objDS.setGeraEst(true);
                            }
                            objDS.comparaConjuntos(resultadosPadr[index]);
                        }

                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(AplicacaoASDS1.class.getName()).log(Level.SEVERE, null, ex);
            }

            java.awt.Toolkit.getDefaultToolkit().beep();

        }

    }

    /**
     * Gera ordem aleatória de algoritmos sem repetição dessa.
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

    /**
     * Grava uma lista de algoritmos em um arquivo dedicado a manter o histórico
     * de algoritmos selecionados para compor a amostra para os experimentos.
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
    
    /**
     * Elimina os possíveis arquivos com estatísticas de experimentos anteriores.
     * @param base
     * @param qp
     */
    public void limpaEstatisticas(String base, String qp) {

        File dir = new File("./src/csv/estatisticas/" + base + "/" + qp);

        if (dir.isDirectory()) {
            File[] sun = dir.listFiles();
            for (File toDelete : sun) {
                toDelete.delete();
            }
        }

    }
    
    public int[] getvQtdAlg() {
        return vQtdAlg;
    }

    public void setvQtdAlg(int[] vQtdAlg) {
        this.vQtdAlg = vQtdAlg;
    }

}
