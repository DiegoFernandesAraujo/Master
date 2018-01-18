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

import java.util.ArrayList;
import java.util.Collection;

import dude.algorithm.duplicatedetection.SortedBlocks;
import dude.algorithm.duplicatedetection.SortedBlocks.AlgorithmVariant;
import dude.datasource.DataSource;
import dude.datasource.DuDeObjectSource;
import dude.output.DuDeOutput;
import dude.output.JsonOutput;
import dude.similarityfunction.SimilarityFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.LevenshteinDistanceFunction;
import dude.util.data.DuDeObject;
import dude.util.data.DuDeObjectPair;
import dude.util.data.json.JsonRecord;
import dude.util.data.json.JsonString;
import dude.util.sorting.sortingkey.SortingKey;
import dude.util.sorting.sortingkey.TextBasedSubkey;

/**
 * Example experiment for the SortedBlocks algorithm.
 * 
 * @author uwedraisbach
 */
public class SortedBlocksExec {
	
	private DataSource dataSource;
    private String sourceID = "src";

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		SortedBlocksExec exec = new SortedBlocksExec();
		exec.createData();
		exec.run();
	}
	
	/**
	 * Runs the example experiment.
	 * 
	 * @throws Exception
	 */
	public void run() throws Exception {
		
		// Sorting key configuration
		TextBasedSubkey subkey = new TextBasedSubkey("Name");
		//subkey.setRange(0, 1);
		SortingKey sortingKey = new SortingKey(subkey);

		// Algorithm configuration
		//AlgorithmVariant variant = AlgorithmVariant.Basic;
		//AlgorithmVariant variant = AlgorithmVariant.FixPartitionSize;
		AlgorithmVariant variant = AlgorithmVariant.MaxSizeNewPartition;
		//AlgorithmVariant variant = AlgorithmVariant.MaxSizeSlideWindow;
		int overlap = 1;
		SortedBlocks algorithm = new SortedBlocks(variant, sortingKey, overlap);
		algorithm.enableInMemoryProcessing();
		
		switch (variant) {
          case Basic:
        	  algorithm.setCharBlockKey(3);
        	  break;
          case FixPartitionSize:
        	  algorithm.setFixBlockSize(2);
        	  break;
          case MaxSizeNewPartition:
          case MaxSizeSlideWindow:
        	  algorithm.setCharBlockKey(3);
        	  algorithm.setMaxBlockSize(3);
        }
        

		// adds the "data" to the algorithm
		algorithm.addDataSource(this.dataSource);

		// instantiates similarity measure
		SimilarityFunction similarityFunction = new LevenshteinDistanceFunction("Name");

		// instantiates output
		DuDeOutput output = new JsonOutput(System.out).withData();

		// Execution
		long start = System.currentTimeMillis();

		// counts the generated object pairs
		int cnt = 0;
		int dupCnt = 0;

		for (DuDeObjectPair pair : algorithm) {
			System.out.println("Element 1: " + pair.getFirstElement().getAttributeValue("Name") +
					"   Element 2: " + pair.getSecondElement().getAttributeValue("Name"));
			
			double similarity;
			try {
				similarity = similarityFunction.getSimilarity(pair);
			} catch (IllegalArgumentException e) {
				System.out.println("Invalid type: " + e.getMessage());
				continue;
			}
			
			if (similarity > 0.7) {
				output.write(pair);
				++dupCnt;
			}
			++cnt;
		}

		algorithm.cleanUp();

		System.out.println();
		System.out.println();
		System.out.println(dupCnt + " duplicates out of " + cnt + " pairs detected in " + (System.currentTimeMillis() - start) + " ms.");
		
	}
	
	/**
	 * Populate the data source with data
	 */
	public void createData() {
		Collection<DuDeObject> coll = new ArrayList<DuDeObject>();
		JsonRecord data;
		
		data = new JsonRecord();
		data.put("Name", new JsonString("AAA1"));
		coll.add(new DuDeObject(data, this.sourceID, "0"));
		
		data = new JsonRecord();
		data.put("Name", new JsonString("AAA2"));
		coll.add(new DuDeObject(data, this.sourceID, "1"));
		
		data = new JsonRecord();
		data.put("Name", new JsonString("AAA3"));
		coll.add(new DuDeObject(data, this.sourceID, "2"));
		
		data = new JsonRecord();
		data.put("Name", new JsonString("CCC1"));
		coll.add(new DuDeObject(data, this.sourceID, "3"));
		
		data = new JsonRecord();
		data.put("Name", new JsonString("CCC2"));
		coll.add(new DuDeObject(data, this.sourceID, "4"));
		
		data = new JsonRecord();
		data.put("Name", new JsonString("CCC3"));
		coll.add(new DuDeObject(data, this.sourceID, "5"));
		
		data = new JsonRecord();
		data.put("Name", new JsonString("BBB1"));
		coll.add(new DuDeObject(data, this.sourceID, "6"));
		
		data = new JsonRecord();
		data.put("Name", new JsonString("BBB2"));
		coll.add(new DuDeObject(data, this.sourceID, "7"));
		
		data = new JsonRecord();
		data.put("Name", new JsonString("BBB3"));
		coll.add(new DuDeObject(data, this.sourceID, "8"));
		
		data = new JsonRecord();
		data.put("Name", new JsonString("BBB4"));
		coll.add(new DuDeObject(data, this.sourceID, "9"));
		
		data = new JsonRecord();
		data.put("Name", new JsonString("DDD1"));
		coll.add(new DuDeObject(data, this.sourceID, "10"));
		
		data = new JsonRecord();
		data.put("Name", new JsonString("DDD2"));
		coll.add(new DuDeObject(data, this.sourceID, "11"));

		this.dataSource = new DuDeObjectSource(this.sourceID, coll);		
	}

}
