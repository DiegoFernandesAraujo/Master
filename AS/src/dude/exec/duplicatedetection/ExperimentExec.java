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
import dude.datasource.JSONSource;
import dude.output.statisticoutput.SimpleStatisticOutput;
import dude.similarityfunction.SimilarityFunction;
import dude.similarityfunction.aggregators.Average;
import dude.similarityfunction.contentbased.impl.simmetrics.LevenshteinDistanceFunction;
import dude.util.Experiment;
import dude.util.GlobalConfig;
import dude.util.sorting.sortingkey.SortingKey;
import dude.util.sorting.sortingkey.TextBasedSubkey;

/**
 * The execution class for the {@link Experiment} class.
 * 
 * @author Matthias Pohl
 */
public class ExperimentExec {

	/**
	 * Sample main method in which <code>DuDe</code> extracts data from a Json file.
	 * 
	 * @param args
	 *            Won't be considered.
	 * @throws Exception
	 *             If any exception occurs.
	 */
	public static void main(String[] args) throws Exception {
		GlobalConfig.getInstance().setInMemoryObjectThreshold(1000);

		JSONSource dataSource = new JSONSource("json", new File("./res/data/", "persons_10000.json"));

		TextBasedSubkey subkey = new TextBasedSubkey("lastName");
		subkey.setIgnoredCharactersRegEx(TextBasedSubkey.NO_VOWELS_REGEX);

		SortingKey sortingKey = new SortingKey(subkey);
		Algorithm algorithm = new SortedNeighborhoodMethod(sortingKey, 30);
		// Algorithm algorithm = new NaiveDuplicateDetection();

		SimilarityFunction similarityFunction = new Average(new LevenshteinDistanceFunction("lastName"), new LevenshteinDistanceFunction("firstName"));

		Experiment experiment = new Experiment();
		experiment.enableStatistics();
		experiment.enableInMemoryProcessing();

		experiment.addDataSource(dataSource);
		experiment.setAlgorithm(algorithm);
		experiment.setSimilarityFunction(similarityFunction);
		experiment.addStatisticOutput(new SimpleStatisticOutput(System.out));

		experiment.run(0.9);
	}
}
