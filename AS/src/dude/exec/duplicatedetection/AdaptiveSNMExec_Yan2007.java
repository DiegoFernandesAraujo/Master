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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import dude.algorithm.duplicatedetection.AdaptiveSNM_Yan2007;
import dude.algorithm.duplicatedetection.AdaptiveSNM_Yan2007.AlgorithmVariant;
import dude.datasource.DuDeObjectSource;
import dude.util.data.DuDeObject;
import dude.util.data.DuDeObjectPair;
import dude.util.data.json.JsonArray;
import dude.util.data.json.JsonRecord;
import dude.util.data.json.JsonString;
import dude.util.sorting.sortingkey.SortingKey;
import dude.util.sorting.sortingkey.Subkey;
import dude.util.sorting.sortingkey.TextBasedSubkey;

/**
* This execution class runs the <code>AdaptiveSNM_Yan2007</code> algorithm.
 * 
 * @author Uwe Draisbach
 */
public class AdaptiveSNMExec_Yan2007 {
	
	/**
	 * Executes a <code>AdaptiveSNM_Yan2007</code> example experiment.
	 * 
	 * @param args
	 *            No arguments will be processed.
	 */
	public static void main(String[] args) {
		try {
			new AdaptiveSNMExec_Yan2007().run();
			System.out.println("Verarbeitung erfolgreich beendet");
		} catch (Exception e) {
			System.out.println("Exception");
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Runs the example experiment for the algorithm <code>AdaptiveSNME_Yan2007</code>
	 * 
	 * @throws IOException
	 */
	public void run() throws IOException {
		
		System.out.println("Start");
		
		Collection<DuDeObject> data = new ArrayList<DuDeObject>();
		JsonRecord record = new JsonRecord();
		record.put("Name1", new JsonArray(new JsonString("AAA1")));
		record.put("Name2", new JsonArray(new JsonString("X")));
		DuDeObject a1_Object = new DuDeObject(record, "", "0");
		
		record = new JsonRecord();
		record.put("Name1", new JsonArray(new JsonString("AAA2")));
		record.put("Name2", new JsonArray(new JsonString("X")));
		DuDeObject a2_Object = new DuDeObject(record, "", "1");
		
		record = new JsonRecord();
		record.put("Name1", new JsonArray(new JsonString("AAA3")));
		record.put("Name2", new JsonArray(new JsonString("X")));
		DuDeObject a3_Object = new DuDeObject(record, "", "2");
		
		record = new JsonRecord();
		record.put("Name1", new JsonArray(new JsonString("BBB1")));
		record.put("Name2", new JsonArray(new JsonString("X")));
		DuDeObject b1_Object = new DuDeObject(record, "", "3");
		
		record = new JsonRecord();
		record.put("Name1", new JsonArray(new JsonString("BBB2")));
		record.put("Name2", new JsonArray(new JsonString("X")));
		DuDeObject b2_Object = new DuDeObject(record, "", "4");
		
		record = new JsonRecord();
		record.put("Name1", new JsonArray(new JsonString("BBB3")));
		record.put("Name2", new JsonArray(new JsonString("X")));
		DuDeObject b3_Object = new DuDeObject(record, "", "5");
		
		record = new JsonRecord();
		record.put("Name1", new JsonArray(new JsonString("BBB4")));
		record.put("Name2", new JsonArray(new JsonString("X")));
		DuDeObject b4_Object = new DuDeObject(record, "", "6");
		
		record = new JsonRecord();
		record.put("Name1", new JsonArray(new JsonString("CCC1")));
		record.put("Name2", new JsonArray(new JsonString("X")));
		DuDeObject c1_Object = new DuDeObject(record, "", "7");
		
		record = new JsonRecord();
		record.put("Name1", new JsonArray(new JsonString("CCC2")));
		record.put("Name2", new JsonArray(new JsonString("X")));
		DuDeObject c2_Object = new DuDeObject(record, "", "8");
		
		record = new JsonRecord();
		record.put("Name1", new JsonArray(new JsonString("CCC3")));
		record.put("Name2", new JsonArray(new JsonString("X")));
		DuDeObject c3_Object = new DuDeObject(record, "", "9");
		
		record = new JsonRecord();
		record.put("Name1", new JsonArray(new JsonString("DDD1")));
		record.put("Name2", new JsonArray(new JsonString("X")));
		DuDeObject d1_Object = new DuDeObject(record, "", "10");
		
		record = new JsonRecord();
		record.put("Name1", new JsonArray(new JsonString("DDD2")));
		record.put("Name2", new JsonArray(new JsonString("X")));
		DuDeObject d2_Object = new DuDeObject(record, "", "11");
		
		data.add(a1_Object);
		data.add(b1_Object);
		data.add(c1_Object);
		data.add(d1_Object);
		data.add(a2_Object);
		data.add(b2_Object);
		data.add(c2_Object);
		data.add(d2_Object);
		data.add(a3_Object);
		data.add(b3_Object);
		data.add(c3_Object);
		data.add(b4_Object);
	

		Subkey subkey1 = new TextBasedSubkey("Name1");
		Subkey subkey2 = new TextBasedSubkey("Name2");
		SortingKey sortingKey = new SortingKey(subkey1);
		sortingKey.addSubkey(subkey2);
		
		AdaptiveSNM_Yan2007 algorithm = new AdaptiveSNM_Yan2007(AlgorithmVariant.IA_SNM, sortingKey, 0.5f);
		algorithm.addDataSource(new DuDeObjectSource("source", data));
		algorithm.enableInMemoryProcessing();
		
		int i = 0;
		for (DuDeObjectPair pair : algorithm) {
			i++;
			System.out.println("Created pair: "+
								pair.getFirstElement().getAttributeValue("Name1")
								+ " - "
								+ pair.getSecondElement().getAttributeValue("Name1"));
			System.out.println("-----");
		}
		
		System.out.println("Anzahl Distanz-Vergleiche: " + algorithm.getSortingKeyComparisons());
		
	}
}


