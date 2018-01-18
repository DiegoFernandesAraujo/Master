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

import dude.algorithm.Algorithm;
import dude.algorithm.duplicatedetection.SortedNeighborhoodMethod;
import dude.datasource.XMLSource;
import dude.output.DuDeOutput;
import dude.output.JsonOutput;
import dude.similarityfunction.SimilarityFunction;
import dude.similarityfunction.aggregators.Average;
import dude.similarityfunction.contentbased.calculationstrategy.ArrayConversionStrategy;
import dude.similarityfunction.contentbased.calculationstrategy.AverageArrayArrayStrategy;
import dude.similarityfunction.contentbased.impl.SoundExFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.LevenshteinDistanceFunction;
import dude.util.GlobalConfig;
import dude.util.data.DuDeObjectPair;
import dude.util.sorting.sortingkey.SortingKey;
import dude.util.sorting.sortingkey.TextBasedSubkey;

/**
 * The execution class for the {@link SortedNeighborhoodMethod} algorithm.
 * 
 * @author Matthias Pohl
 */
public class SortedNeighborhoodMethodExec {

	/**
	 * Sample main method in which <code>DuDe</code> extracts data from an XML file and runs the Sorted-Neighborhood-Method algorithm.
	 * 
	 * @param args
	 *            Won't be considered.
	 * @throws Exception
	 *             If any exception occurs.
	 */
	public static void main(String[] args) throws Exception {

		// enables dynamic data-loading for file-based sorting
		GlobalConfig.getInstance().setInMemoryObjectThreshold(0);

		// instantiates the XML data source
		XMLSource dataSource = new XMLSource("xml", new File("./res/cddb_ID_nested_10000.xml"), "cddb");

		// Algorithm configuration

		// instantiates the Sorted-Neighborhood-Method algorithm (in-file
		// storing
		// is enabled by default; in-memory storing is disabled by default)

		// defines sub-keys that are used to generate the sorting key
		TextBasedSubkey artistSubkey = new TextBasedSubkey("artist");
		artistSubkey.setIgnoredCharactersRegEx(TextBasedSubkey.NO_VOWELS_REGEX);
		artistSubkey.setRange(2);
		TextBasedSubkey titleSubkey = new TextBasedSubkey("category");
		titleSubkey.setIgnoredCharactersRegEx(TextBasedSubkey.NO_VOWELS_REGEX);

		// the key generator uses sub-key selectors to generate a key for each
		// object
		SortingKey sortingKey = new SortingKey();

		// ...and add them to the key generator (the order matters!)
		sortingKey.addSubkey(artistSubkey);
		sortingKey.addSubkey(titleSubkey);

		Algorithm algorithm = new SortedNeighborhoodMethod(sortingKey, 50);

		// enable in-memory storing
		// algorithm.enableInMemoryProcessing();

		// adds the "data" to the algorithm
		algorithm.addDataSource(dataSource);

		// instantiates similarity measure
		LevenshteinDistanceFunction artistSimilarity = new LevenshteinDistanceFunction("artist");
		artistSimilarity.setCompareArrayArrayStrategy(new AverageArrayArrayStrategy());
		artistSimilarity.setCompareArrayAtomicStrategy(new ArrayConversionStrategy());

		SoundExFunction titleSimilarity = new SoundExFunction("dtitle");
		titleSimilarity.setCompareArrayArrayStrategy(new AverageArrayArrayStrategy());
		titleSimilarity.setCompareArrayAtomicStrategy(new ArrayConversionStrategy());
		
		SimilarityFunction similarityFunction = new Average(artistSimilarity, titleSimilarity);

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
			}
			++cnt;
		}

		algorithm.cleanUp();

		System.out.println();
		System.out.println();
		System.out.println(dupCnt + " duplicates out of " + cnt + " pairs detected in " + (System.currentTimeMillis() - start) + " ms");
	}
}
