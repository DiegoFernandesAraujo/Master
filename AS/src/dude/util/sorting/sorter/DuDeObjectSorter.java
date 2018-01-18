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

import dude.util.data.DuDeObject;
import dude.util.data.storage.DuDeStorage;
import dude.util.sorting.sortingkey.SortingKey;

/**
 * The <code>interface</code> <code>DuDeObjectSorter</code> provides the method signatures for sorting a collection of {@link DuDeObject}s.
 * 
 * @author Matthias Pohl
 */
public interface DuDeObjectSorter {

	/**
	 * Adds a {@link DuDeObject} to the collection that will be sorted.
	 * 
	 * @param object
	 *            The <code>DuDeObject</code>.
	 */
	public void add(DuDeObject object);

	/**
	 * Adds the passed several {@link DuDeObject}s to this <code>DuDeObjectSorter</code>.
	 * 
	 * @param objects
	 *            The <code>DuDeObject</code>s.
	 */
	public void addAll(Iterable<DuDeObject> objects);

	/**
	 * Sets a new {@link SortingKey}.
	 * 
	 * @param sortingKey
	 *            The new <code>SortingKey</code>.
	 * @throws NullPointerException
	 *             If <code>null</code> was passed instead of a <code>SortingKey</code>.
	 */
	public void setSortingKey(SortingKey sortingKey);

	/**
	 * Clears the already added data.
	 */
	public void clear();

	/**
	 * Returns the sorted data.
	 * 
	 * @return A sorted collection of the data.
	 * 
	 * @throws IOException
	 *             If an error occurs while sorting the data file-based.
	 */
	public DuDeStorage<DuDeObject> getSortedCollection() throws IOException;
}
