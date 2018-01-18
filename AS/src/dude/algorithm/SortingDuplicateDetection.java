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

package dude.algorithm;

import java.io.IOException;
import java.util.Iterator;

import dude.util.data.DuDeObject;
import dude.util.data.DuDeObjectPair;
import dude.util.data.storage.DuDeStorage;
import dude.util.data.storage.JsonableReader;
import dude.util.sorting.sorter.DuDeObjectSorter;
import dude.util.sorting.sorter.InMemorySorter;
import dude.util.sorting.sorter.TwoPhaseMultiWayMergeSorter;
import dude.util.sorting.sortingkey.SortingKey;

/**
 * <code>SortingDuplicateDetection</code> implements the preprocessing phase were the data is sorted based on a given {@link SortingKey}.
 * 
 * @author Matthias Pohl
 */
public abstract class SortingDuplicateDetection extends AbstractDuplicateDetection {

	private SortingKey sortingKey;

	/**
	 * For serialization
	 */
	protected SortingDuplicateDetection() {
		super();
		// nothing to do
	}

	/**
	 * Initializes the <code>SortingDuplicateDetection</code> with the passed {@link SortingKey}.
	 * 
	 * @param sortingKey
	 *            The {@link SortingKey} that is used for sorting the extracted data.
	 */
	public SortingDuplicateDetection(SortingKey sortingKey) {
		super();
		this.setSortingKey(sortingKey);
	}

	/**
	 * Returns the set {@link SortingKey}.
	 * 
	 * @return The set <code>SortingKey</code>.
	 */
	public SortingKey getSortingKey() {
		return this.sortingKey;
	}

	/**
	 * Sets the {@link SortingKey}.
	 * 
	 * @param sortingKey
	 *            The new <code>SortingKey</code>.
	 */
	public void setSortingKey(SortingKey sortingKey) {
		if (sortingKey == null) {
			throw new NullPointerException("The SortingKey is missing.");
		}

		this.sortingKey = sortingKey;
	}

	@Override
	protected abstract Iterator<DuDeObjectPair> createIteratorInstance();

	@Override
	protected DuDeStorage<DuDeObject> preprocessData() {
		if (this.sortingKey.isEmpty()) {
			// no subkeys are set -> no sorting needs to be done
			return null;
		}

		try {
			DuDeObjectSorter sorter = null;
			JsonableReader<DuDeObject> reader = this.getData();
			if (this.inMemoryProcessingEnabled()) {
				sorter = new InMemorySorter(this.sortingKey);
			} else {
				sorter = new TwoPhaseMultiWayMergeSorter(this.sortingKey);
			}
			sorter.addAll(reader);
			reader.close();

			return sorter.getSortedCollection();
		} catch (IOException e) {
			throw new IllegalStateException("An IOException occurred while sorting the data.", e);
		}
	}

}
