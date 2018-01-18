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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dude.datasource.DataSource;
import dude.util.data.DuDeObject;
import dude.util.data.DuDeObjectPair;
import dude.util.data.storage.DuDeStorage;
import dude.util.data.storage.JsonableReader;
import dude.util.sorting.sorter.DuDeObjectSorter;
import dude.util.sorting.sorter.InMemorySorter;
import dude.util.sorting.sorter.TwoPhaseMultiWayMergeSorter;
import dude.util.sorting.sortingkey.SortingKey;

/**
 * <code>SortingRecordLinkage</code> implements the preprocessing phase were the data is sorted based on one or several {@link SortingKey}s.
 * 
 * @author Matthias Pohl
 */
public abstract class SortingRecordLinkage extends AbstractRecordLinkage {

	private SortingKey defaultSortingKey;

	private Map<DataSource, SortingKey> sortingKeys = new HashMap<DataSource, SortingKey>();

	/**
	 * Initializes the <code>SortingRecordLinkage</code> with no default {@link SortingKey}.
	 */
	public SortingRecordLinkage() {
		super();
	}

	/**
	 * Initializes the <code>SortingRecordLinkage</code> with the passed default {@link SortingKey}.
	 * 
	 * @param defaultSortingKey
	 */
	public SortingRecordLinkage(SortingKey defaultSortingKey) {
		super();

		this.setDefaultSortingKey(defaultSortingKey);
	}

	/**
	 * Sets the default {@link SortingKey}.
	 * 
	 * @param sortingKey
	 *            The default <code>SortingKey</code>. If <code>null</code> is passed, the previously set <code>SortingKey</code> will be unset.
	 */
	public void setDefaultSortingKey(SortingKey sortingKey) {
		this.defaultSortingKey = sortingKey;
	}

	/**
	 * Adds a {@link DataSource}-related {@link SortingKey}.
	 * 
	 * @param source
	 *            The <code>DataSource</code> for which a <code>SortingKey</code> shall be added.
	 * @param sortingKey
	 *            The <code>DataSource</code>-related <code>SortingKey</code>. Passing <code>null</code> instead of a <code>SortingKey</code> will
	 *            unset the previously set <code>SortingKey</code>.
	 * @throws NullPointerException
	 *             If no source is passed.
	 */
	public void addSortingKey(DataSource source, SortingKey sortingKey) {
		if (source == null) {
			throw new NullPointerException("The DataSource is missing.");
		}

		if (sortingKey == null) {
			// unsets the attribute of the passed DataSource
			this.sortingKeys.remove(source);
			return;
		}

		this.sortingKeys.put(source, sortingKey);
	}

	@Override
	protected abstract Iterator<DuDeObjectPair> createIteratorInstance();

	@Override
	protected Map<DataSource, DuDeStorage<DuDeObject>> preprocessData(Iterable<DataSource> dataSources) {
		Map<DataSource, DuDeStorage<DuDeObject>> preprocessedData = new HashMap<DataSource, DuDeStorage<DuDeObject>>();
		
		DuDeObjectSorter sorter = null;
		if (this.inMemoryProcessingEnabled()) {
			sorter = new InMemorySorter();
		} else {
			sorter = new TwoPhaseMultiWayMergeSorter();
		}
		
		for (DataSource source : dataSources) {
			SortingKey sourceSortingKey = this.defaultSortingKey;
			if (this.sortingKeys.containsKey(source)) {
				sourceSortingKey = this.sortingKeys.get(source);
			}

			if (sourceSortingKey == null) {
				throw new IllegalStateException("No default SortingKey was specified and no DataSource-related SortingKey was set for DataSource '"
						+ source.getIdentifier() + "'.");
			}

			try {
				sorter.clear();
				sorter.setSortingKey(sourceSortingKey);
				
				JsonableReader<DuDeObject> reader = this.getData(source);
				sorter.addAll(reader);
				reader.close();
				
				preprocessedData.put(source, sorter.getSortedCollection());
			} catch (IOException e) {
				throw new IllegalStateException("An IOException occurred while sorting the data.", e);
			}
		}

		return preprocessedData;
	}

}
