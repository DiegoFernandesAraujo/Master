/*
 * DuDe - The Duplicate Detection Toolkit
 * 
 * Copyright (C) 2010  Hasso-Plattner-Institut für Softwaresystemtechnik GmbH,
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

package dude.exec.recordlinkage;

import java.util.ArrayList;
import java.util.Collection;

import dude.algorithm.Algorithm;
import dude.algorithm.recordlinkage.NaiveRecordLinkage;
import dude.datasource.DataSource;
import dude.datasource.DuDeObjectSource;
import dude.output.CSVOutput;
import dude.similarityfunction.contentbased.calculationstrategy.ArrayConversionStrategy;
import dude.similarityfunction.contentbased.calculationstrategy.StableMarriageStrategy;
import dude.similarityfunction.contentbased.impl.AbsoluteNumberDiffFunction;
import dude.util.data.DuDeObject;
import dude.util.data.DuDeObjectPair;
import dude.util.data.json.JsonArray;
import dude.util.data.json.JsonRecord;

/**
 * <code>NaiveRecordLinkageExec</code> contains a code-snippet that illustrates, how to use the {@link NaiveRecordLinkage} implementation.
 * 
 * @author Matthias Pohl
 */
public class NaiveRecordLinkageExec {

	private static JsonRecord generateData0(int value) {
		JsonRecord data = new JsonRecord();
		data.put("value", value);
		
		return data;
	}
	
	private static JsonRecord generateData1(int... values) {
		JsonRecord data = new JsonRecord();
		
		JsonArray arr = new JsonArray();
		for (int value : values) {
			arr.add(value);
		}
		
		data.put("value", arr);
		
		return data;
	}
	
	/**
	 * Creates a first {@link DataSource}.
	 * 
	 * @return The <code>DataSource</code>.
	 */
	private static DataSource createDataSource0() {
		Collection<DuDeObject> coll = new ArrayList<DuDeObject>();
		
		coll.add(new DuDeObject(NaiveRecordLinkageExec.generateData0(0), "src0", "0"));
		coll.add(new DuDeObject(NaiveRecordLinkageExec.generateData0(1), "src0", "1"));
		coll.add(new DuDeObject(NaiveRecordLinkageExec.generateData0(2), "src0", "2"));
		coll.add(new DuDeObject(NaiveRecordLinkageExec.generateData0(3), "src0", "3"));
		coll.add(new DuDeObject(NaiveRecordLinkageExec.generateData0(4), "src0", "4"));
		coll.add(new DuDeObject(NaiveRecordLinkageExec.generateData0(5), "src0", "5"));
		
		return new DuDeObjectSource("src0", coll);
	}

	/**
	 * Creates a second {@link DataSource}.
	 * 
	 * @return The <code>DataSource</code>.
	 */
	private static DataSource createDataSource1() {
		Collection<DuDeObject> coll = new ArrayList<DuDeObject>();
		
		coll.add(new DuDeObject(NaiveRecordLinkageExec.generateData1(0, 1, 2, 3), "src1", "0"));
		coll.add(new DuDeObject(NaiveRecordLinkageExec.generateData1(1, 2, 3), "src1", "1"));
		coll.add(new DuDeObject(NaiveRecordLinkageExec.generateData1(2, 3, 4, 5), "src1", "2"));
		coll.add(new DuDeObject(NaiveRecordLinkageExec.generateData1(3, 4, 5, 6, 7), "src1", "3"));
		coll.add(new DuDeObject(NaiveRecordLinkageExec.generateData1(4), "src1", "4"));
		coll.add(new DuDeObject(NaiveRecordLinkageExec.generateData1(5, 6, 7), "src1", "5"));
		
		return new DuDeObjectSource("src1", coll);
	}

	/**
	 * Sample main method.
	 * 
	 * @param args
	 *            Won't be considered.
	 * @throws Exception
	 *             If any exception occurs.
	 */
	public static void main(String[] args) throws Exception {
		DataSource source0 = NaiveRecordLinkageExec.createDataSource0();
		DataSource source1 = NaiveRecordLinkageExec.createDataSource1();

		Algorithm algorithm = new NaiveRecordLinkage();
		algorithm.enableInMemoryProcessing();

		algorithm.addDataSource(source0);
		algorithm.addDataSource(source1);

		AbsoluteNumberDiffFunction similarityFunction = new AbsoluteNumberDiffFunction(10, "value");
		similarityFunction.setCompareArrayArrayStrategy(new StableMarriageStrategy());
		similarityFunction.setCompareArrayAtomicStrategy(new ArrayConversionStrategy());

		CSVOutput out = new CSVOutput(System.out);
		out.setQuoteCharacter(' ');
		out.setSeparator('\t');

		for (DuDeObjectPair pair : algorithm) {
                    System.out.println(similarityFunction.getSimilarity(pair));
//			if (similarityFunction.getSimilarity(pair) > 0.9) {
//				out.write(pair);
//			}
		}
	}

}
