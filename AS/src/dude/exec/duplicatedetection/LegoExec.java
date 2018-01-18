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

import dude.algorithm.Algorithm;
import dude.algorithm.duplicatedetection.Lego;
import dude.algorithm.duplicatedetection.Lego.ComparisonResult;
import dude.algorithm.duplicatedetection.NaiveBlockingAlgorithm;
import dude.algorithm.duplicatedetection.NaiveDuplicateDetection;
import dude.datasource.CSVSource;
import dude.output.DuDeOutput;
import dude.output.JsonOutput;
import dude.similarityfunction.SimilarityFunction;
import dude.similarityfunction.aggregators.Average;
import dude.similarityfunction.aggregators.Maximum;
import dude.similarityfunction.contentbased.calculationstrategy.ArrayConversionStrategy;
import dude.similarityfunction.contentbased.calculationstrategy.AverageArrayArrayStrategy;
import dude.similarityfunction.contentbased.impl.simmetrics.LevenshteinDistanceFunction;
import dude.util.GoldStandard;
import dude.util.data.DuDeObjectPair;
import dude.util.merger.DefaultMerger;
import dude.util.sorting.sortingkey.SortingKey;
import dude.util.sorting.sortingkey.TextBasedSubkey;

/**
 * The execution class for the {@link Lego} algorithm.
 * 
 * @author Florian Thomas
 */
public class LegoExec {

	/**
	 * Sample main method in which <code>DuDe</code> extracts data from an XML file and runs the Sorted-Neighborhood-Method algorithm.
	 * 
	 * @param args
	 *            Won't be considered.
	 * @throws Exception
	 *             If any exception occurs.
	 */
	public static void main(String[] args) throws Exception {

		CSVSource dataSource = new CSVSource("restaurant", new File("./res", "restaurant.csv"));
		dataSource.enableHeader();
		dataSource.setSeparatorCharacter(',');

		// uses the id attribute for the object id - this call is optional, if no id attribute is set, DuDe will generate its own object ids
		dataSource.addIdAttributes("id");

		// Algorithm configuration

		// defines sub-keys that are used to generate the sorting key
		TextBasedSubkey citySubkey = new TextBasedSubkey("city");
		citySubkey.setRange(0, 10);
		SortingKey citySortingKey = new SortingKey(citySubkey);

		TextBasedSubkey phoneSubkey = new TextBasedSubkey("phone");
		phoneSubkey.setRange(0, 3);
		SortingKey phoneSortingKey = new SortingKey(phoneSubkey);
		
		TextBasedSubkey typeSubkey = new TextBasedSubkey("type");
		SortingKey typeSortingKey = new SortingKey(typeSubkey);

		Lego algorithm = new Lego(citySortingKey, phoneSortingKey, typeSortingKey);

		// enable in-memory storing
		algorithm.enableInMemoryProcessing();
		
		// set algorithm that should be used to process blocks
		algorithm.setCoreERAlgorithm(new NaiveDuplicateDetection());
		
		// set the merger, which will be responsible for merging DuDeObjects
		algorithm.setMerger(new DefaultMerger());

		// adds the "data" to the algorithm
		algorithm.addDataSource(dataSource);

		// instantiates similarity measure
		LevenshteinDistanceFunction addrSimilarity = new LevenshteinDistanceFunction("addr");
		addrSimilarity.setCompareArrayArrayStrategy(new AverageArrayArrayStrategy());
		addrSimilarity.setCompareArrayAtomicStrategy(new ArrayConversionStrategy());

		LevenshteinDistanceFunction citySimilarity = new LevenshteinDistanceFunction("city");
		citySimilarity.setCompareArrayArrayStrategy(new AverageArrayArrayStrategy());
		citySimilarity.setCompareArrayAtomicStrategy(new ArrayConversionStrategy());

		LevenshteinDistanceFunction phoneSimilarity = new LevenshteinDistanceFunction("phone");
		phoneSimilarity.setCompareArrayArrayStrategy(new AverageArrayArrayStrategy());
		phoneSimilarity.setCompareArrayAtomicStrategy(new ArrayConversionStrategy());

		SimilarityFunction addressFunction = new Average(addrSimilarity, citySimilarity);
		
		SimilarityFunction similarityFunction = new Maximum(addressFunction, phoneSimilarity);

		// instantiates output
		DuDeOutput output = new JsonOutput(System.out).withData();

		// Execution
		long start = System.currentTimeMillis();

		// counts the generated object pairs
		int cnt = 0;
		int dupCnt = 0;

		for (DuDeObjectPair pair : algorithm) {
			double similarity;
			try {
				similarity = similarityFunction.getSimilarity(pair);
			} catch (IllegalArgumentException e) {
				System.out.println("Invalid type: " + e.getMessage());
				continue;
			}
			
			if (similarity > 0.9) {
				output.write(pair);
				++dupCnt;
				algorithm.notifyOfLatestComparisonResult(ComparisonResult.DUPLICATE);
			} else {
				algorithm.notifyOfLatestComparisonResult(ComparisonResult.NON_DUPLICATE);
			}
			++cnt;
		}

		algorithm.cleanUp();

		System.out.println();
		System.out.println();
		System.out.println(dupCnt + " duplicates out of " + cnt + " pairs detected in " + (System.currentTimeMillis() - start) + " ms");
	}
}
