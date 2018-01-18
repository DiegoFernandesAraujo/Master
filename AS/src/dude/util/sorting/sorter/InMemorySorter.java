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

package dude.util.sorting.sorter;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dude.util.data.DuDeObject;
import dude.util.data.storage.DuDeStorage;
import dude.util.data.storage.InMemoryStorage;
import dude.util.sorting.sortingkey.SortingKey;

/**
 * <code>InMemorySorter</code> implements a in-memory sort. It does not need any file access. Internally the JDK sorting algorithm is used.
 * 
 * @author Matthias Pohl
 * 
 * @see Collections#sort(List, Comparator)
 */
public class InMemorySorter extends AbstractDuDeObjectSorter {

	/**
	 * Initializes a <code>InMemorySorter</code> with no {@link SortingKey}.
	 */
	public InMemorySorter() {
		super();
	}

	/**
	 * Initializes a <code>InMemorySorter</code> with the passed {@link SortingKey}.
	 * 
	 * @param key
	 *            The sorting key that defines the sorting order.
	 */
	public InMemorySorter(SortingKey key) {
		super(key);
	}

	@Override
	public DuDeStorage<DuDeObject> getSortedCollection() throws IOException {
		Collections.sort(this.getInMemoryData(), this.getSortingKey());
		return new InMemoryStorage<DuDeObject>(this.getInMemoryData());
	}

}
