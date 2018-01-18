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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import dude.datasource.DataSource;
import dude.util.data.DuDeObject;
import dude.util.data.DuDeObjectPair;
import dude.util.data.storage.DuDeStorage;
import dude.util.data.storage.JsonableReader;
import dude.util.data.storage.JsonableWriter;

/**
 * <code>AbstractDuplicateDetection</code> provides the common functionality that is needed by every duplicate-detection algorithm. Any new
 * duplicate-detection algorithm implementation should extend this class.
 * 
 * @author Matthias Pohl
 */
public abstract class AbstractDuplicateDetection extends AbstractAlgorithm {

	private static final Logger logger = Logger.getLogger(AbstractDuplicateDetection.class.getPackage().getName());

	private DuDeStorage<DuDeObject> data;

	private final Collection<DataSource> dataSources = new ArrayList<DataSource>();

	/**
	 * Initializes a new <code>AbstractDuplicateDetection</code> instance.
	 */
	public AbstractDuplicateDetection() {
		super();
		// nothing to do
	}
	
	@Override
	public void unregisterDataSources() {
		this.dataSources.clear();
	}

	@Override
	protected void addSource(DataSource source) {
		this.dataSources.add(source);
	}

	@Override
	protected boolean dataSourceAttached(DataSource source) {
		if (source == null) {
			AbstractDuplicateDetection.logger.debug("null was passed instead of a DataSource.");
			return false;
		}

		return this.dataSources.contains(source);
	}

	@Override
	public int getDataSize() {
		if (this.data == null) {
			return 0;
		}

		return this.data.size();
	}

	/**
	 * Extracts the data out of the attached {@link DataSource}s.
	 */
	private void extractData() {
		try {
			this.data = this.createStorage("tmp");
		} catch (IOException e) {
			throw new IllegalStateException("IOException occurred while initializing the internal storage.", e);
		}

		JsonableWriter<DuDeObject> writer = null;
		try {
			writer = this.data.getWriter();
		} catch (IOException e) {
			throw new IllegalStateException("An IOException occurred while instantiating a writer on the storage.", e);
		}

		for (DataSource source : this.dataSources) {
			for (DuDeObject object : source) {
				try {
					this.analyzeDuDeObject(object);
					writer.add(object);
				} catch (IOException e) {
					throw new IllegalStateException("An IOException occurred while extracting a record out of '" + source.getIdentifier() + "'.", e);
				}
			}
		}

		try {
			writer.close();
		} catch (IOException e) {
			throw new IllegalStateException("An IOException occurred while closing the storage.", e);
		}

		// informs all preprocessor that the extraction process is finished
		this.finishPreprocessing();
	}

	/**
	 * Returns the extracted data.
	 * 
	 * @return The {@link JsonableReader} that can be used for reading the data.
	 */
	protected JsonableReader<DuDeObject> getData() {
		final JsonableReader<DuDeObject> reader = this.data.getReader();
		this.registerCloseable(reader);

		return reader;
	}

	@Override
	public Iterator<DuDeObjectPair> iterator() {
		if (!this.dataExtracted()) {
			// 1st - extraction phase
			this.extractData();
			// closes all connections that were opened for extracting the data
			this.cleanUp();

			// 2nd - preprocessing phase
			final DuDeStorage<DuDeObject> preprocessedData = this.preprocessData();
			if (preprocessedData != null) {
				// null indicates that no preprocessing was done -> the data was not changed at all
				this.data = preprocessedData;
			}

			this.finishExtraction();
		}

		// 3rd - returning the iterator
		return new AlgorithmIteratorWrapper(this.createIteratorInstance());
	}

	/**
	 * Returns a new {@link Iterator} instance.
	 * 
	 * @return The <code>Iterator</code> instance.
	 */
	protected abstract Iterator<DuDeObjectPair> createIteratorInstance();

	/**
	 * Preprocesses the data. This method needs to be overwritten, if the algorithm needs any preprocessing of the extracted data. By default, nothing
	 * is done when calling it.
	 * 
	 * @return Returns the preprocessed data or <code>null</code>, if the preprocessing shall be ignored.
	 */
	protected DuDeStorage<DuDeObject> preprocessData() {
		// nothing to do
		return null;
	}

	@Override
	public long getMaximumPairCount() {
		if (!this.dataExtracted()) {
			return 0;
		}
		
		final int dataSz = this.getDataSize();
		return (dataSz * (dataSz - 1)) / 2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.data == null) ? 0 : this.data.hashCode());
		result = prime * result + ((this.dataSources == null) ? 0 : this.dataSources.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!super.equals(obj)) {
			return false;
		}

		if (this.getClass() != obj.getClass()) {
			return false;
		}

		AbstractDuplicateDetection other = (AbstractDuplicateDetection) obj;
		if (this.data == null) {
			if (other.data != null) {
				return false;
			}
		} else if (!this.data.equals(other.data)) {
			return false;
		}

		if (this.dataSources == null) {
			if (other.dataSources != null) {
				return false;
			}
		} else if (!this.dataSources.equals(other.dataSources)) {
			return false;
		}

		return true;
	}
}
