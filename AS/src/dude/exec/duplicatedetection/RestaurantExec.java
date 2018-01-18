/*
 * DuDe - The Duplicate Detection Toolkit
 * 
 * Copyright (C) 2010  Hasso-Plattner-Institut fr Softwaresystemtechnik GmbH,
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
import dude.algorithm.duplicatedetection.SortedNeighborhoodMethod;
import dude.datasource.CSVSource;
import dude.output.DuDeOutput;
import dude.output.JsonOutput;
import dude.output.statisticoutput.SimpleStatisticOutput;
import dude.output.statisticoutput.StatisticOutput;
import dude.postprocessor.StatisticComponent;
import dude.similarityfunction.SimilarityFunction;
import dude.similarityfunction.aggregators.Average;
import dude.similarityfunction.contentbased.impl.EquationSimilarityFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.LevenshteinDistanceFunction;
import dude.util.GlobalConfig;
import dude.util.GoldStandard;
import dude.util.data.DuDeObjectPair;
import dude.util.sorting.sortingkey.SortingKey;
import dude.util.sorting.sortingkey.TextBasedSubkey;

/**
 * This execution class runs the naive duplicate detection algorithm on the <code>Restaurant</code> data source. Two records are similar if their
 * names match based on the Levenshtein distance and they are of the same "restaurant type".
 * 
 * @author Matthias Pohl, Uwe Draisbach
 */
public class RestaurantExec {

	/**
	 * Executes the naive duplicate detection on the <code>restaurant</code> data source. During the process all duplicates will be written onto the
	 * console.
	 * 
	 * @param args
	 *            No arguments will be processed.
	 * @throws IOException
	 *             If an error occurs while reading from the file.
	 */
	public static void main(String[] args) throws IOException {
		// enables dynamic data-loading for file-based sorting
		GlobalConfig.getInstance().setInMemoryObjectThreshold(100);

		// instantiates the CSVSource
		// "restaurant" is the source identifier
		CSVSource dataSource = new CSVSource("restaurant", new File("./res", "restaurant.csv"));
		dataSource.enableHeader();
		dataSource.setSeparatorCharacter(',');

		// uses the id attribute for the object id - this call is optional, if no id attribute is set, DuDe will generate its own object ids
		dataSource.addIdAttributes("id");

		// instantiates the CSVSource for reading the goldstandard
		// "goldstandard" is the goldstandard identifier
		CSVSource goldStandardSource = new CSVSource("goldstandard", new File("./res", "restaurant_gold.csv"));
		goldStandardSource.enableHeader();
		goldStandardSource.setSeparatorCharacter(',');

		// instantiate a loader for the real-duplicates
		// "restaurant" is the source identifier
		GoldStandard goldStandard = new GoldStandard(goldStandardSource);

		// define the attributes that contain the identifiers of the first and
		// second pair element
		goldStandard.setFirstElementsObjectIdAttributes("id_1");
		goldStandard.setSecondElementsObjectIdAttributes("id_2");

		// "restaurant" is the source identifier - this identifier needs to be equal to the identifier of the actual DataSource
		goldStandard.setSourceIdLiteral("restaurant");

		// instantiates the naive duplicate detection algorithm
//		Algorithm algorithm = new NaiveDuplicateDetection();
		Algorithm algorithm = new SortedNeighborhoodMethod(new SortingKey(new TextBasedSubkey("name")));

		// enables in-memory execution for faster processing
		// this can be done since the whole data fits into memory
		algorithm.enableInMemoryProcessing();

		// adds the "data" to the algorithm
		algorithm.addDataSource(dataSource);

		// instantiates the similarity function
		// checks the Levenshtein distance of the restaurant names
		// checks if the type of both restaurants is equal
		SimilarityFunction similarityFunction = new Average(new LevenshteinDistanceFunction("name"), new EquationSimilarityFunction("type"));

		// writes the pairs onto the console by using the Json syntax
		DuDeOutput output = new JsonOutput(System.out).withData();

		// instantiate statistic component to calculate key figures
		// like runtime, number of comparisons, precision and recall
		StatisticComponent statistic = new StatisticComponent(goldStandard, algorithm);

		// the actual computation starts
		// the algorithm returns each generated pair step-by-step
		statistic.setStartTime();
		for (DuDeObjectPair pair : algorithm) {
			final double similarity = similarityFunction.getSimilarity(pair);
			if (similarity > 0.9) {
				// if it is a duplicate - print it and add it to the
				// statistic component as duplicate
				output.write(pair);
				statistic.addDuplicate(pair);
			} else {
				// if it is not a duplicate, add it to the statistic
				// component as non-duplicate
				statistic.addNonDuplicate(pair);
			}
		}
		statistic.setEndTime();

		// Write statistics
		StatisticOutput statisticOutput = new SimpleStatisticOutput(System.out, statistic);
		statisticOutput.writeStatistics();
		System.out.println("Experiment finished.");

		// clean up
		dataSource.close();
		goldStandard.close();
	}

}