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

import java.util.HashSet;
import java.util.Set;

import dude.util.data.DuDeObject;
import dude.util.data.DuDeObjectId;
import dude.util.data.json.JsonArray;
import dude.util.data.json.JsonRecord;

/**
 * <code>AbstractMerger</code> splits the merge functionality into the merging of data,
 * which should be implemented in subclasses and the merging of identifiers, which is done in this class.
 * 
 * @author Johannes Dyck
 * 
 */
public abstract class AbstractMerger implements Merger{
	
	/**
	 * Merges two {@link DuDeObject}s into one new {@link DuDeObject}.
	 * Identifiers will be merged into an array containing all {@link DuDeObjectId}s
	 * that are part of the new object. Each identifier will only appear once in this array.
	 * The merging of the data is delegated to a subclass implementing the corresponding method.
	 * 
	 * @param leftElement
	 *            One of the <code>DuDeObjects</code> that shall be merged. 
	 * @param rightElement
	 *            The other <code>DuDeObject</code> that shall be merged.
	 * @return A new <code>DuDeObject</code> created by merging the input <code>DuDeObjects</code>.
	 */
	@Override
	public DuDeObject merge(DuDeObject leftElement, DuDeObject rightElement) {

		// the mergeData function should be overwritten in subclasses for different mergers
		JsonRecord data = mergeData(leftElement, rightElement);				
		DuDeObject result = null;
		
		// store the identifiers merged into the result so that there will be no duplicate entries
		Set<DuDeObjectId> ids = new HashSet<DuDeObjectId>();
		
		// add identifiers from the left object
		for (DuDeObjectId cId : leftElement.getIdentifiers()) {
			if (result == null) {
				// use the first identifier in the left object as constructor arguments ...
				JsonArray firstObjId = cId.getObjectId();
				String firstSrcId = cId.getSourceId();				
				result = new DuDeObject(data, firstSrcId, firstObjId);
				ids.add(cId);
			} else {
				// add all other identifiers except the first
				result.addIdentifier(cId);
				ids.add(cId);
			}
		}
		
		// add all new identifiers from the right object to the merged object
		for (DuDeObjectId cId : rightElement.getIdentifiers()) {			
			if (!ids.contains(cId)) {
				result.addIdentifier(cId);
			}
		}		
		return result;
	}
	
	/**
	 * Creates a new data set containing the merged data from the passed {@link DuDeObject}s.
	 * 
	 * @param leftElement
	 *            One of the <code>DuDeObjects</code> that shall be merged. 
	 * @param rightElement
	 *            The other <code>DuDeObject</code> that shall be merged.
	 * @return The new data set built from the passed {@link DuDeObject}s. 
	 */
	protected abstract JsonRecord mergeData(DuDeObject leftElement, DuDeObject rightElement);
	
}
