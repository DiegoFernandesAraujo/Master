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
import java.util.ArrayList;
import java.util.List;

import dude.util.MemoryChecker;
import dude.util.data.DuDeObject;
import dude.util.data.storage.DuDeStorage;
import dude.util.sorting.sortingkey.SortingKey;

/**
 * <code>AbstractDuDeObjectSorter</code> implements the {@link DuDeObjectSorter} interface partially. Sorting implementations only have to deal with
 * the <code>abstract</code> method {@link #getSortedCollection()}.
 * 
 * @author Matthias Pohl
 */
public abstract class AbstractDuDeObjectSorter implements DuDeObjectSorter {

	private MemoryChecker memoryChecker = MemoryChecker.getInstance();

	private List<DuDeObject> inMemoryData = new ArrayList<DuDeObject>();

	private SortingKey sortingKey;

	/**
	 * Initializes an <code>AbstractDuDeObjectSorter</code> with no {@link SortingKey}.
	 */
	public AbstractDuDeObjectSorter() {
		// nothing to do
	}

	/**
	 * Initializes an <code>AbstractDuDeObjectSorter</code> with the passed {@link SortingKey}.
	 * 
	 * @param key
	 *            The key that defines the sorting order.
	 */
	// DO NOT IMPLEMENT A CONSTRUCTOR THAT ACCEPTS INITIAL DATA -> THIS WON'T WORK SINCE THE SORTER INITIALIZATION IS NOT FINISHED, YET
	public AbstractDuDeObjectSorter(SortingKey key) {
		if (key == null) {
			throw new NullPointerException("A sorting key needs to be passed.");
		}

		this.sortingKey = key;
	}

	@Override
	public void add(DuDeObject record) {
		this.memoryChecker.makeMemoryUsageSnapshot();
		this.inMemoryData.add(record);
	}

	/**
	 * Adds all elements of the passed {@link Iterable} to the <code>AbstractDuDeObjectSorter</code>. It simply calls {@link #add(DuDeObject)} on each
	 * element.
	 * 
	 * @param objects
	 *            The <code>DuDeObject</code>s that shall be added.
	 */
	@Override
	public void addAll(Iterable<DuDeObject> objects) {
		if (objects == null) {
			return;
		}

		for (DuDeObject object : objects) {
			this.add(object);
		}
	}

	@Override
	public void clear() {
		this.inMemoryData.clear();
	}

	/**
	 * {@inheritDoc DuDeObjectSorter#getSortedCollection()}
	 * 
	 * This method needs to be implemented by each sub-class. It should sort the data in some way and return it.
	 */
	@Override
	public abstract DuDeStorage<DuDeObject> getSortedCollection() throws IOException;

	/**
	 * Checks whether the memory limit was reached.
	 * 
	 * @return <code>true</code>, if no more elements should be added; otherwise <code>false</code>.
	 */
	protected boolean memoryLimitReached() {
		return !this.memoryChecker.enoughMemoryAvailable();
	}

	/**
	 * Returns the in-memory data.
	 * 
	 * @return The in-memory data.
	 */
	protected List<DuDeObject> getInMemoryData() {
		return this.inMemoryData;
	}

	/**
	 * Returns the {@link SortingKey} that defines the sorting order.
	 * 
	 * @return The <code>SortingKey</code>.
	 */
	protected SortingKey getSortingKey() {
		return this.sortingKey;
	}

	@Override
	public void setSortingKey(SortingKey sortingKey) {
		this.sortingKey = sortingKey;
	}

}
