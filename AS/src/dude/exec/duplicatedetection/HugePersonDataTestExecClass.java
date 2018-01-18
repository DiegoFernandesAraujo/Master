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

import java.io.FileInputStream;

import dude.algorithm.Algorithm;
import dude.algorithm.duplicatedetection.SortedNeighborhoodMethod;
import dude.database.DatabaseSource;
import dude.database.adapter.MySQLDatabase;
import dude.database.util.DBInfo;
import dude.datasource.DataSource;
import dude.similarityfunction.SimilarityFunction;
import dude.similarityfunction.aggregators.Average;
import dude.similarityfunction.contentbased.impl.simmetrics.LevenshteinDistanceFunction;
import dude.util.GlobalConfig;
import dude.util.data.DuDeObjectPair;
import dude.util.sorting.sortingkey.SortingKey;
import dude.util.sorting.sortingkey.TextBasedSubkey;

/**
 * Executes a run using a large data file with the {@link SortedNeighborhoodMethod}. In order to run this executable, the data actual person data has
 * to be included in a MySQL database. The corresponding connection information should be stored in a *.properties './res/dbinfo.properties'.
 * 
 * @author Matthias Pohl
 */
public class HugePersonDataTestExecClass {

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

		// sets the data source
		DataSource dataSource = new DatabaseSource("persons", new MySQLDatabase(new DBInfo(new FileInputStream("./res/dbinfo.properties"))),
				"person_data_10000");

		// defines sub-keys that are used to generate the sorting key
		TextBasedSubkey artistSubkey = new TextBasedSubkey("lastName");
		artistSubkey.setIgnoredCharactersRegEx(TextBasedSubkey.NO_VOWELS_REGEX);

		// the key generator uses sub-key selectors to generate a key for each object
		SortingKey sortingKey = new SortingKey();
		sortingKey.addSubkey(artistSubkey);

		Algorithm algorithm = new SortedNeighborhoodMethod(sortingKey, 30);

		// enable in-memory storing
		algorithm.enableInMemoryProcessing();

		// adds the "data" to the algorithm
		algorithm.addDataSource(dataSource);

		// instantiates similarity measure
		SimilarityFunction comparator = new Average(new LevenshteinDistanceFunction("lastName"), new LevenshteinDistanceFunction("firstName"));

		long start = System.currentTimeMillis();

		// counts the generated object pairs
		int cnt = 0;
		int dupCnt = 0;

		for (DuDeObjectPair pair : algorithm) {
			if (comparator.getSimilarity(pair) > 0.9) {
				++dupCnt;
			}
			++cnt;
		}

		// print statistics
		System.out.println();
		System.out.println();
		System.out.println(dupCnt + " duplicates out of " + cnt + " pairs detected in " + (System.currentTimeMillis() - start) + " ms");
	}

}
