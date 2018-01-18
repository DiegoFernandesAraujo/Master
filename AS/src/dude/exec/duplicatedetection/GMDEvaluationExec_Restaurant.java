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

import dude.algorithm.Algorithm;
import dude.algorithm.duplicatedetection.NaiveDuplicateDetection;
import dude.datasource.CSVSource;
import dude.output.statisticoutput.SimpleStatisticOutput;
import dude.output.statisticoutput.StatisticOutput;
import dude.postprocessor.ExtendedStatisticComponent;
import dude.postprocessor.ExtendedStatisticComponent.Config;
import dude.similarityfunction.contentbased.calculationstrategy.ArrayConversionStrategy;
import dude.similarityfunction.contentbased.calculationstrategy.AverageArrayArrayStrategy;
import dude.similarityfunction.contentbased.impl.simmetrics.LevenshteinDistanceFunction;
import dude.util.GoldStandard;
import dude.util.data.DuDeObjectPair;

/**
 * <code>GMDEvaluationExec_Restaurant</code> is an example experiment for the usage of the <code>ExtendedStatisticComponent</code>.
 * It shows how to read in the GoldStandard (two ways possible: As duplicate pairs or in cluster format) with the restaurant data set.
 * 
 * @author Cindy Faehnrich
 */
public class GMDEvaluationExec_Restaurant {

/**
 * @param args
 */
public static void main(String[] args) {
		
	try {
		/************* (1) DataSource Configuration ***********
		 * Instantiates the DataSources for gold standard and the one to compare
		 */
		//read in restaurant sources	
		CSVSource dataSource = new CSVSource("restaurant", new File("./", "test_set.csv"));
		dataSource.enableHeader();
		dataSource.withIdAttributes("id");
		                
		// instantiates the CSVSource for reading the gold standard
		CSVSource goldStandardSource = new CSVSource("restaurant", new File("./", "test_gold.csv"));
		goldStandardSource.enableHeader();
		
		// instantiates the gold standard as duplicate pairs
		GoldStandard goldStandard = new GoldStandard(goldStandardSource);
		   
		// instantiates the gold standard in cluster format
		//GoldStandard goldStandard = new GoldStandard("goldstandard_clustered.csv");
		
		// the attributes that store the object ids in DataSource
		goldStandard.setFirstElementsObjectIdAttributes("id_1"); //for the clusterfile, it is "id1"
		goldStandard.setSecondElementsObjectIdAttributes("id_2");//for the clusterfile, it is "id2"

		// "cddb" is the source identifier
		goldStandard.setSourceIdLiteral("restaurant");

		/************* (2) Algorithm Configuration ***********
		 * instantiates the naive duplicate detection algorithm
		 */
		Algorithm algorithm = new NaiveDuplicateDetection();

		// adds the "data" to the algorithm
		algorithm.addDataSource(dataSource);

		/************* (3) SimilarityFunction Configuration ***********
		 * instantiates the similarity function
		 * checks the Levenshtein distance of the restaurant names
		 */
		//checks the Levenshtein distance of the restaurants' names
		LevenshteinDistanceFunction similarityFunc = new LevenshteinDistanceFunction("name");
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
		for (DuDeObjectPair pair : algorithm) {
			 double similarity;
			   similarity = similarityFunc.getSimilarity(pair);
		   if (similarity > 0.8) {
		      // if it is a duplicate - print it and add it to the statistic component as duplicate 
		      //output.write(pair);
		      statistic.addDuplicate(pair);
		   } else {
		      // if it is no duplicate, add it to the statistic component as non-duplicate
		      statistic.addNonDuplicate(pair);
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
