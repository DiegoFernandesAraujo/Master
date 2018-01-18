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
import java.util.Iterator;

import dude.algorithm.duplicatedetection.DuplicateCountSNM;
import dude.algorithm.duplicatedetection.DuplicateCountSNM.AdaptionMode;
import dude.datasource.XMLSource;
import dude.output.DuDeOutput;
import dude.output.SimpleTextOutput;
import dude.postprocessor.NaiveTransitiveClosureGenerator;
import dude.similarityfunction.SimilarityFunction;
import dude.similarityfunction.contentbased.impl.EquationSimilarityFunction;
import dude.util.data.DuDeObjectPair;
import dude.util.sorting.sortingkey.SortingKey;
import dude.util.sorting.sortingkey.Subkey;
import dude.util.sorting.sortingkey.TextBasedSubkey;

/**
 * Executes the {@link DuplicateCountSNM}.
 * 
 * @author Fabian Lindenberg
 */
public class DuplicateCountSNMExec {

	/**
	 * Sample main method in which <code>DuDe</code> extracts data from an XML file and runs the {@link DuplicateCountSNM} algorithm.
	 * 
	 * @param args
	 *            Won't be considered.
	 * @throws Exception
	 *             If any exception occurs.
	 */
	public static void main(String[] args) throws Exception {

		// ///////////////////////////
		// ///////////////////////////
		// Data extraction

		// sets the XML data source
		XMLSource dataSource = new XMLSource("xml", new File("./res/adaptive_window_size_snm_example.xml"), "cddb");

		//
		// ///////////////////////////
		// ///////////////////////////

		// ///////////////////////////
		// ///////////////////////////
		// Algorithm configuration

		// instantiates the Sorted-Neighborhood-Method algorithm (in-file storing is enabled by default; in-memory storing is disabled by default)

		// defines sub-keys that are used to generate the sorting key
		Subkey artistSubkey = new TextBasedSubkey("artist");
		Subkey titleSubkey = new TextBasedSubkey("dtitle");

		// the key generator uses sub-key selectors to generate a key for each
		// object
		SortingKey sortingKey = new SortingKey();

		// ...and adds them to the key generator (the order matters!)
		sortingKey.addSubkey(artistSubkey);
		sortingKey.addSubkey(titleSubkey);

		// instantiates the actual algorithm by using the AdaptiveWindowSizeSNMBuilder and configures the adaption mode
		// AdaptiveWindowSizeSNM algorithm = new AdaptiveWindowSizeSNM
		// 											.AdaptiveWindowSizeSNMBuilder(sortingKey, AdaptionMode.BASIC)
		// 											.windowSize(5)
		// 											.increaseThreshold(0.25f)
		// 											.build();

		// alternatives:

		// Adaption Mode: MULTI_REC_INCREASE
		DuplicateCountSNM algorithm = new DuplicateCountSNM.AdaptiveWindowSizeSNMBuilder(sortingKey, AdaptionMode.MULTI_REC_INCREASE)
													.windowSize(5)
													.increaseThreshold(0.25f).build();

		// Adaption Mode: DISTANCE_BASED_INCREASE
		// AdaptiveWindowSizeSNM algorithm = new AdaptiveWindowSizeSNM
		// 											.AdaptiveWindowSizeSNMBuilder(sortingKey, AdaptionMode.DISTANCE_BASED_INCREASE)
		// 											.windowSize(5)
		// 											.increaseThreshold(0.25f)
		// 											.build();

		// Adaption Mode: PERCENTAGE_INCREASE
		// AdaptiveWindowSizeSNM algorithm = new AdaptiveWindowSizeSNM
		// 											.AdaptiveWindowSizeSNMBuilder(sortingKey, AdaptionMode.PERCENTAGE_INCREASE)
		//		 									.windowSize(5)
		// 											.increaseThreshold(0.25f)
		// 											.increaseFactor(0.8f)
		// 											.build();

		// Adaption Mode: PERCENTAGE with abortion of increase explicitly enabled. This may be enabled for all adaption modes.
		// AdaptiveWindowSizeSNM algorithm = new AdaptiveWindowSizeSNM
		// 											.AdaptiveWindowSizeSNMBuilder(sortingKey, AdaptionMode.PERCENTAGE)
		// 											.windowSize(5)
		// 											.increaseThreshold(0.25f)
		// 											.abortIncrease(true)
		// 											.abortThreshold(0.5f)
		// 											.build();

		// enable in-memory storing
		algorithm.enableInMemoryProcessing();

		// adds the "data" to the algorithm
		algorithm.addDataSource(dataSource);

		//
		// ///////////////////////////
		// ///////////////////////////

		// ///////////////////////////
		// ///////////////////////////
		// Similarity measurement

		// instantiates similarity measure for reasons of simplicity, in this example only a EquationSimilarityFunction is chosen
		SimilarityFunction similarityFunction = new EquationSimilarityFunction("dtitle");

		//
		// ///////////////////////////
		// ///////////////////////////

		// ///////////////////////////
		// ///////////////////////////
		// Output

		// instantiates output
		DuDeOutput output = new SimpleTextOutput(System.out);

		//
		// ///////////////////////////
		// ///////////////////////////

		// ///////////////////////////
		// ///////////////////////////
		// Execution

		long start = System.currentTimeMillis();

		// counts the generated object pairs
		int cnt = 0;
		int dupCnt = 0;

		NaiveTransitiveClosureGenerator transClosureGen = new NaiveTransitiveClosureGenerator();

		for (DuDeObjectPair pair : algorithm) {
			double similarity = similarityFunction.getSimilarity(pair);
			output.write(pair);

			// notifies the algorithm whether the pair is categorized as a duplicate or a non-duplicate
			// This step is crucial for adapting the window sizes continuously!
			// Thus, if no notification is received, the algorithm throws an exception.
			if (similarity > 0.9) {
				algorithm.notifyOfLatestComparisonResult(DuplicateCountSNM.ComparisonResult.DUPLICATE);
				transClosureGen.add(pair);
				++dupCnt;
			} else {
				algorithm.notifyOfLatestComparisonResult(DuplicateCountSNM.ComparisonResult.NON_DUPLICATE);
			}

			++cnt;
		}

		// Most of the adaption modes assume transitivity.
		int transDupCnt = 0;
		Iterator<DuDeObjectPair> iterator = transClosureGen.iterator();
		for (; iterator.hasNext(); iterator.next(), transDupCnt++) {
			// nothing to do
		}

		System.out.println(dupCnt + " (" + transDupCnt + " through transitive closure) duplicates out of " + cnt + " pairs detected in "
				+ (System.currentTimeMillis() - start) + " ms");
	}

}
