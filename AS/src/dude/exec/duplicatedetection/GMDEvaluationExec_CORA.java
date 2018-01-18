/*
 * DuDe - The Duplicate Detection Toolkit
 * 
 * Copyright (C) 2011  Hasso-Plattner-Institut fr Softwaresystemtechnik GmbH,
 *                     Potsdam, Germany 
 *
 * This file is part of DuDe.
 * 
 * DuDe is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DuDe is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DuDe.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package dude.exec.duplicatedetection;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import dude.algorithm.Algorithm;
import dude.algorithm.duplicatedetection.NaiveDuplicateDetection;
import dude.datasource.CSVSource;
import dude.datasource.DataSource;
import dude.datasource.XMLSource;
import dude.output.statisticoutput.SimpleStatisticOutput;
import dude.output.statisticoutput.StatisticOutput;
import dude.postprocessor.ExtendedStatisticComponent;
import dude.postprocessor.NaiveTransitiveClosureGenerator;
import dude.postprocessor.ExtendedStatisticComponent.Config;
import dude.similarityfunction.contentbased.calculationstrategy.ArrayConversionStrategy;
import dude.similarityfunction.contentbased.calculationstrategy.AverageArrayArrayStrategy;
import dude.similarityfunction.contentbased.impl.simmetrics.LevenshteinDistanceFunction;
import dude.util.GoldStandard;
import dude.util.data.DuDeObject;
import dude.util.data.DuDeObjectPair;

/**
 * <code>GMDEvaluationExec_CORA</code> is an example experiment for the usage of the <code>ExtendedStatisticComponent</code>.
 * It shows how to read in the GoldStandard (two ways possible: As duplicate pairs or in cluster format) with the CORA data set.
 * 
 * @author Cindy Faehnrich
 */
public class GMDEvaluationExec_CORA {

/**
 * @param args
 */
public static void main(String[] args) {
		
	try {
		/************* (1) DataSource Configuration ***********
		 * Instantiates the DataSources for gold standard and the one to compare
		 */
		//read in CORA sources
		DataSource dataSource = new XMLSource("cora", new File("./CORA.xml"), "CORA");

		//you have to use the id-attribute for id creation!
		dataSource.addIdAttributes("id");

		//instantiate a gold standard, here with a file in _duplicate_pairs_format_
		GoldStandard goldStandard = new GoldStandard(new CSVSource("cora", new File("./", "cora_gold.csv")).withHeader());
		
		//instantiates the gold standard in cluster format - remember to adapt the filename
		//GoldStandard goldStandard = new GoldStandard("goldstandard_clustered.csv");
		
		// the attributes that store the object ids for clusterfile
		//goldStandard.setFirstElementsObjectIdAttributes("id1");
		//goldStandard.setSecondElementsObjectIdAttributes("id2");
		
		// "cora" is the source identifier - this identifier needs to be equal to the identifier of the actual DataSource
		goldStandard.setSourceIdLiteral("cora");

		/************* (2) Algorithm Configuration ***********
		 * instantiates the naive duplicate detection algorithm
		 */
		Algorithm algorithm = new NaiveDuplicateDetection();

		// adds the "data" to the algorithm
		algorithm.addDataSource(dataSource);

		/************* (3) SimilarityFunction Configuration ***********
		 * instantiates the similarity function
		 * checks the Levenshtein distance of the papers' titles 
		 */
		LevenshteinDistanceFunction similarityFunc = new LevenshteinDistanceFunction("title");
		similarityFunc.setCompareArrayArrayStrategy(new AverageArrayArrayStrategy());
		similarityFunc.setCompareArrayAtomicStrategy(new ArrayConversionStrategy());

		/************* (4) Statistics ***********
		 * instantiates the ExtendedStatisticComponent to calculate key figures
		 * like runtime, number of comparisons and measures like pF1, precision, and recall
		 */
		ExtendedStatisticComponent statistic = new ExtendedStatisticComponent(goldStandard, algorithm, Config.DEFAULT);
		//activate the measures here
		statistic.activateF1(); //note: this also activates PRECISION and RECALL
		statistic.activateHybrid();
		statistic.activateDefault();
		statistic.activateVI();
		//statistic.deactivateF1();
		//statistic.deactivateHybrid();
		//statistic.deactivateDefault();
		//statistic.deactivateVI();
		    
		// instantiates the output formatter for the statistics
		StatisticOutput statisticOutput = new SimpleStatisticOutput(System.out, statistic);
		/************* (5) Execution ***********
		 * the algorithm returns each generated pair step-by-step
		 */
		statistic.setStartTime();
		Vector<DuDeObjectPair> pairs = new Vector<DuDeObjectPair>();
		for (DuDeObjectPair pair : algorithm) {
			 double similarity;
			try{
			   similarity = similarityFunc.getSimilarity(pair);
		   } catch (IllegalArgumentException e) {
				// ignore invalid values
				//System.out.println("Values will be ignored: " + e.getMessage());
				continue;
			}
		   if (similarity > 0.8) {
		      // if it is a duplicate add it to a collection to generate the transitive closure later 
			   pairs.add(pair);
			   
		   } else {
		      // if it is no duplicate, add it to the statistic component as non-duplicate
		      statistic.addNonDuplicate(pair);
		   }
		}
		
		//generate transitive closure from pairs to achieve the same result as with comparison-based precision/recall
		NaiveTransitiveClosureGenerator transClosGenerator = new NaiveTransitiveClosureGenerator();
		transClosGenerator.add(pairs);
		Collection<Collection<DuDeObject>> duplicates = (Collection<Collection<DuDeObject>>) transClosGenerator.getTransitiveClosures();
		//iterate over each cluster to generate duplicate pairs
		Iterator<Collection<DuDeObject>> it1 = duplicates.iterator();
		while (it1.hasNext()){
			Collection<DuDeObject> cluster = it1.next(); //take cluster
			Iterator<DuDeObject> it2 = cluster.iterator();
			List<DuDeObject> li = new LinkedList<DuDeObject>();
			while (it2.hasNext()){
				li.add(it2.next());	
			}
			//generate Duplicate pairs from transitive Closure to add to statisticComponent
			for (int i = 0; i < li.size(); i++){
				for (int j = i+1; j < li.size(); j++){
					DuDeObjectPair pair = new DuDeObjectPair(li.get(i), li.get(j));
					statistic.addDuplicate(pair);
				}
			}
		}
			
		statistic.setEndTime();
		// prints statistics
		statisticOutput.writeStatistics();
		System.out.println("Experiment finished.");

		// clean up
		algorithm.cleanUp();
		goldStandard.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
 }

}