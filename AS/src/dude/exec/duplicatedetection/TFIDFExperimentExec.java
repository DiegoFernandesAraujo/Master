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
import dude.datasource.CSVSource;
import dude.preprocessor.DocumentFrequencyPreprocessor;
import dude.similarityfunction.SimilarityFunction;
import dude.similarityfunction.contentbased.impl.TFIDFSimilarityFunction;
import dude.util.GlobalConfig;
import dude.util.data.DuDeObjectPair;
import dude.util.sorting.sortingkey.SortingKey;
import dude.util.sorting.sortingkey.TextBasedSubkey;

/**
 * Executes a run using a large data file with the {@link SortedNeighborhoodMethod} and preprocessing for the tf-idf comparator.
 * 
 * @author Ziawasch Abedjan
 */
public class TFIDFExperimentExec {

	/**
	 * Runs the {@link SortedNeighborhoodMethod} on a huge data set.
	 * 
	 * @param args
	 *            Won't be considered.
	 * @throws Exception
	 *             If any exception occurs.
	 */
	public static void main(String[] args) throws Exception {

		// enables dynamic data-loading for file-based sorting
		GlobalConfig.getInstance().setInMemoryObjectThreshold(1000);

		// sets the CSV data source
		CSVSource dataSource = new CSVSource("cd", new File("./res/cd.csv"));
		dataSource.enableHeader();
		// defines sub-keys that are used to generate the sorting key
		TextBasedSubkey artistSubkey = new TextBasedSubkey("title");
		artistSubkey.setIgnoredCharactersRegEx(TextBasedSubkey.NO_VOWELS_REGEX);
		
		DocumentFrequencyPreprocessor dfPreprocessor = new DocumentFrequencyPreprocessor("title");
		// the key generator uses sub-key selectors to generate a key for each object
		SortingKey sortingKey = new SortingKey();
		sortingKey.addSubkey(artistSubkey);

		Algorithm algorithm = new SortedNeighborhoodMethod(sortingKey, 30);
		algorithm.addPreprocessor(dfPreprocessor);

		// enable in-memory storing
		algorithm.enableInMemoryProcessing();

		// adds the "data" to the algorithm
		algorithm.addDataSource(dataSource);

		// instantiates similarity measure
		SimilarityFunction similarityFunction = new TFIDFSimilarityFunction(dfPreprocessor, "title");

		long start = System.currentTimeMillis();

		// counts the generated object pairs
		int cnt = 0;
		int dupCnt = 0;
		int nondupCnt = 0;

		for (DuDeObjectPair pair : algorithm) {
			if (similarityFunction.getSimilarity(pair) > 0.5) {
				++dupCnt;
			}else{
				++nondupCnt;
			}
			++cnt;
		}

		algorithm.cleanUp();
		
		// print statistics
		System.out.println();
		System.out.println();
		System.out.println(dupCnt + " duplicates out of " + cnt + " pairs detected in " + (System.currentTimeMillis() - start) + " ms");
	}

}
