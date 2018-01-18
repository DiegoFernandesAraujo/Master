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

package dude.merger;

import dude.util.data.DuDeObject;
import dude.util.data.json.JsonArray;
import dude.util.data.json.JsonRecord;
import dude.util.data.json.JsonValue;
import dude.util.data.json.JsonValue.JsonType;

/**
 * <code>DefaultMerger</code> implements merge functionality.
 * 
 * @author Johannes Dyck
 * 
 */
public class DefaultMerger extends AbstractMerger {
	
	/**
	 * Merges the data from the {@link DuDeObject}s into a new {@link JsonRecord}. Attribute values should be arrays and multiple values of the same 
	 * attribute are merged into a single array. Array entries will be unique unless one of the input objects already contained duplicate entries for the attribute.
	 * 
	 * The result object contains one identifier for each object that was merged into the result.
	 * Each identifier consists of a source id and an array of object ids. If an object was merged into the result object more
	 * than once, the corresponding identifier will still be contained only once.	 * 
	 * 
	 * @param leftElement
	 *            One of the <code>DuDeObjects</code> that shall be merged. 
	 * @param rightElement
	 *            The other <code>DuDeObject</code> that shall be merged.
	 * @return The merged data.
	 */
	@Override
	protected JsonRecord mergeData(DuDeObject leftElement, DuDeObject rightElement) {
		JsonRecord newData = new JsonRecord();
		JsonRecord leftData = leftElement.getData();
		JsonRecord rightData = rightElement.getData();
		
		// add left data
		if (leftData != null) {
			for (String key : leftData.keySet()) {
				Object d = leftData.get(key);
				if (d instanceof JsonArray) {
					newData.put(key, (JsonArray) ((JsonArray) d).clone());
				} else {
					JsonArray newArray = new JsonArray();
					newArray.add((JsonValue) d);
					newData.put(key, newArray);
				}				
			}			
		} else {
			if (rightData == null) {
				// ensure that two null values merge into a null value, not an empty record 
				newData = null;
			}
		}
		
		// add right data
		if (rightData != null) {
			// iterate over the attributes on the right
			for (String key : rightData.keySet()) {
				// check whether (left) data already contains the current attribute from the right data
				if (newData.containsKey(key)) {
					// right data contains the attribute, so merge the values
					if (rightData.get(key) instanceof JsonArray) {						
						JsonArray newArray = (JsonArray) newData.get(key);
						JsonArray rightArray = (JsonArray) rightData.get(key);
						for (JsonValue v : rightArray) {
							// avoid creating duplicates when merging the arrays
							if (!newArray.contains(v)) {
								newArray.add(v);								
							}
						}
					} else {
						// rightData.get(key) should at least be an instance of JsonValue
						JsonArray newArray = (JsonArray) newData.get(key);
						if (!newArray.contains(rightData.get(key))) {
							newArray.add(rightData.get(key));
						}
					}
				} else {
					// left data does not contain the attribute, so just add it
					Object d = rightData.get(key);
					if (d instanceof JsonArray) {
						newData.put(key, (JsonArray) ((JsonArray) d).clone());
					} else {
						JsonArray newArray = new JsonArray();
						newArray.add((JsonValue) d);
						newData.put(key, newArray);
					}					
				}
			}			
		}
		return newData;
	}
}
