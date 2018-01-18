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

import org.xml.sax.SAXException;

import dude.algorithm.Algorithm;
import dude.algorithm.duplicatedetection.NaiveDuplicateDetection;
import dude.datasource.CSVSource;
import dude.datasource.DataSource;
import dude.datasource.XMLSource;
import dude.output.DuDeOutput;
import dude.output.JsonOutput;
import dude.output.statisticoutput.SimpleStatisticOutput;
import dude.output.statisticoutput.StatisticOutput;
import dude.postprocessor.StatisticComponent;
import dude.similarityfunction.contentbased.calculationstrategy.ArrayConversionStrategy;
import dude.similarityfunction.contentbased.calculationstrategy.AverageArrayArrayStrategy;
import dude.similarityfunction.contentbased.impl.simmetrics.LevenshteinDistanceFunction;
import dude.util.GoldStandard;
import dude.util.data.DuDeObjectPair;

/**
 * This execution class runs the naive duplicate detection algorithm on the <code>CORA</code> data source. Two records are similar if their titles
 * match based on a relative Levenshtein distance of 0.8.
 * 
 * @author Matthias Pohl
 */
public class CoraExec {

	/**
	 * Executes the naive duplicate detection on the <code>CORA</code> data source. During the process all duplicates will be written onto the
	 * console.
	 * 
	 * @param args
	 *            No arguments will be processed.
	 * @throws SAXException
	 *             If an error would occur during the XML parsing process. This exception should not be thrown since the CORA.xml can be parsed
	 *             without any problems.
	 * @throws IOException
	 *             If an error occurs while reading from the file.
	 */
	public static void main(String[] args) throws IOException, SAXException {
		// enables dynamic data-loading for file-based sorting
		// GlobalConfig.getInstance().setMemoryCheckerStepSize(0);

		// instantiates the XML data source
		// "cora" is the source identifier
		// "CORA" is the root element of the data source - all child elements of the root will be transformed into DuDeObjects
		DataSource dataSource = new XMLSource("cora", new File("CORA.xml"), "CORA");

		// uses the id attribute for the object id - this call is optional, if no id attribute is set, DuDe will generate its own object ids
		dataSource.addIdAttributes("id");

		// instantiate a gold standard (the identifier of the CSVSource is not important)
		GoldStandard goldStandard = new GoldStandard(new CSVSource("goldstandard", new File("./", "cora_gold.csv")).withHeader());

		// since "id1" and "id2" are the default attribute names, no custom object id attributes need to be set
		// goldStandard.setFirstElementsObjectIdAttributes("id1");
		// goldStandard.setSecondElementsObjectIdAttributes("id2");

		// "cora" is the source identifier - this identifier needs to be equal to the identifier of the actual DataSource
		goldStandard.setSourceIdLiteral("cora");

		// instantiates the naive duplicate detection algorithm
		Algorithm algorithm = new NaiveDuplicateDetection();

		// enables in-memory execution for faster processing
		// this can be done since the whole data fits into memory
		algorithm.enableInMemoryProcessing();

		// adds the "data" to the algorithm
		algorithm.addDataSource(dataSource);

		// instantiates the similarity function
		// checks the Levenshtein distance of the papers' titles
		LevenshteinDistanceFunction similarityFunction = new LevenshteinDistanceFunction("title");
		similarityFunction.setCompareArrayArrayStrategy(new AverageArrayArrayStrategy());
		similarityFunction.setCompareArrayAtomicStrategy(new ArrayConversionStrategy());

		// writes the pairs onto the console by using the Json syntax
		DuDeOutput output = new JsonOutput(System.out);

		// instantiate statistic component to calculate key figures
		// like runtime, number of comparisons, precision and recall
		StatisticComponent statistic = new StatisticComponent(goldStandard, algorithm);

		// the actual computation starts
		// the algorithm returns each generated pair step-by-step
		statistic.setStartTime();
		for (DuDeObjectPair pair : algorithm) {
			double similarity;
			try {
				similarity = similarityFunction.getSimilarity(pair);
			} catch (IllegalArgumentException e) {
				// ignore invalid values
				System.out.println("Values will be ignored: " + e.getMessage());
				continue;
			}
			
			if (similarity > 0.8) {
				// if it is a duplicate - print it and add it to the
				// statistic component as duplicate
				output.write(pair);
				System.out.println();
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