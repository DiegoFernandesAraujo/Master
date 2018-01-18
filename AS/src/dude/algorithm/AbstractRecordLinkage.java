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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonToken;

import dude.datasource.DataSource;
import dude.util.data.DuDeObject;
import dude.util.data.DuDeObjectPair;
import dude.util.data.Jsonable;
import dude.util.data.json.DuDeJsonGenerator;
import dude.util.data.json.DuDeJsonParser;
import dude.util.data.json.JsonUtil;
import dude.util.data.json.auto.AutoJsonSerialization;
import dude.util.data.json.auto.CompositeJsonSerialization;
import dude.util.data.json.auto.JsonTypeManager;
import dude.util.data.storage.DuDeStorage;
import dude.util.data.storage.JsonableReader;
import dude.util.data.storage.JsonableWriter;

/**
 * <code>AbstractRecordLinkage</code> provides the common functionality that is needed by every record-linkage algorithm. Any new record-linkage
 * algorithm implementation should extend this class.
 * 
 * @author Matthias Pohl
 */
public abstract class AbstractRecordLinkage extends AbstractAlgorithm implements Jsonable {

	private static final Logger logger = Logger.getLogger(AbstractRecordLinkage.class.getPackage().getName());

	// this is a transient field since only the DataSources need to be serialized
	private transient Map<DataSource, DuDeStorage<DuDeObject>> data = new LinkedHashMap<DataSource, DuDeStorage<DuDeObject>>();

	@Override
	public void fromJson(DuDeJsonParser<?> jsonParser) throws JsonParseException, IOException {
		jsonParser.skipToken(JsonToken.START_OBJECT);
		JsonUtil.readFields(jsonParser, this);
		jsonParser.skipFieldName("dataSources");
		jsonParser.skipToken(JsonToken.START_ARRAY);

		CompositeJsonSerialization<DataSource> serializer = (CompositeJsonSerialization<DataSource>) JsonTypeManager.getInstance().getTypeInfo(
				DataSource.class);
		while (jsonParser.currentToken() != JsonToken.END_ARRAY) {
			this.addSource(serializer.read(jsonParser));
		}

		jsonParser.skipToken(JsonToken.END_ARRAY);
		jsonParser.skipToken(JsonToken.END_OBJECT);
	}

	@Override
	public void toJson(DuDeJsonGenerator jsonGenerator) throws JsonGenerationException, IOException {
		jsonGenerator.writeRecordStart();
		JsonUtil.writeFields(jsonGenerator, this);
		jsonGenerator.writeRecordFieldName("dataSources");

		@SuppressWarnings("unchecked")
		AutoJsonSerialization<Set<DataSource>> serializer = (AutoJsonSerialization<Set<DataSource>>) JsonTypeManager.getInstance().getTypeInfo(
				this.data.keySet().getClass());
		serializer.write(jsonGenerator, this.data.keySet());

		jsonGenerator.writeRecordEnd();
	}

	@Override
	public void unregisterDataSources() {
		this.data.clear();
	}

	@Override
	protected void addSource(DataSource source) {
		this.initDataSource(source);
	}

	private void initDataSource(DataSource source) {
		if (this.dataSourceAttached(source)) {
			throw new IllegalStateException("The data source '" + source.getIdentifier() + "' does already exist.");
		}

		this.data.put(source, null);
	}

	private void initStorage(Map.Entry<DataSource, DuDeStorage<DuDeObject>> entry) {
		final DataSource source = entry.getKey();
		DuDeStorage<DuDeObject> storage = entry.getValue();

		if (source == null) {
			throw new NullPointerException();
		} else if (!this.dataSourceAttached(source)) {
			throw new IllegalArgumentException("The passed data source '" + source.getIdentifier() + "' does not exist.");
		}

		if (storage == null) {
			try {
				storage = this.createStorage(source.getIdentifier());
				entry.setValue(storage);
			} catch (IOException e) {
				throw new IllegalStateException("An IOException occurred while initializing the source's storage.", e);
			}
		}
	}

	@Override
	protected boolean dataSourceAttached(DataSource source) {
		if (source == null) {
			AbstractRecordLinkage.logger.debug("null was passed instead of a DataSource.");
			return false;
		}

		return this.data.containsKey(source);
	}

	@Override
	public int getDataSize() {
		int sum = 0;
		for (DataSource source : this.data.keySet()) {
			sum += this.getDataSize(source);
		}

		return sum;
	}

	private void extractData() {
		final int dataSourceCount = this.data.size();

		for (Map.Entry<DataSource, DuDeStorage<DuDeObject>> entry : this.data.entrySet()) {
			this.initStorage(entry);

			if (dataSourceCount > 1) {
				// do the extraction only if more than one DataSource was attached
				this.extractData(entry.getKey(), entry.getValue());
			}
		}
	}

	private void extractData(DataSource source, DuDeStorage<DuDeObject> storage) {
		try {
			JsonableWriter<DuDeObject> writer = storage.getWriter();

			for (DuDeObject object : source) {
				this.analyzeDuDeObject(object);
				writer.add(object);
			}

			writer.close();
			source.close();
		} catch (IOException e) {
			throw new IllegalStateException("An IOException occurred while extracting the data out of '" + source.getIdentifier() + "'.", e);
		}
	}

	@Override
	public Iterator<DuDeObjectPair> iterator() {
		if (this.data.size() < 2) {
			AbstractRecordLinkage.logger
					.warn("Not enough data sources added: A record-linkage run needs at least two different data sources in order to generate pairs.");
		}

		if (!this.dataExtracted()) {
			// 1st - extraction phase
			this.extractData();
			// closes all connections that were opened for extracting the data
			this.cleanUp();

			// 2nd - preprocessing phase
			final Map<DataSource, DuDeStorage<DuDeObject>> preprocessedData = this.preprocessData(this.data.keySet());
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
	 * Returns a {@link JsonableReader} that can be used to return the extracted data of the passed {@link DataSource}.
	 * 
	 * @param source
	 *            The <code>DataSource</code> of which the data is requested.
	 * @return The <code>JsonableReader</code> or <code>null</code>, if this <code>DataSource</code> was not added.
	 * 
	 * @throws NullPointerException
	 *             If <code>null</code> was passed as a <code>DataSource</code>.
	 */
	protected JsonableReader<DuDeObject> getData(DataSource source) {
		if (source == null) {
			throw new NullPointerException();
		}

		if (!this.dataSourceAttached(source)) {
			return null;
		}

		return this.data.get(source).getReader();
	}

	/**
	 * Returns the {@link DataSource}s and their extracted data.
	 * 
	 * @return The <code>DataSources</code> and their extracted data.
	 */
	protected Iterable<Map.Entry<DataSource, DuDeStorage<DuDeObject>>> getData() {
		return this.data.entrySet();
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
	 * @param dataSources
	 *            The data sources of which the data shall be preprocessed.
	 * @return The preprocessed data or <code>null</code>, if the preprocessing shall be ignored.
	 */
	protected Map<DataSource, DuDeStorage<DuDeObject>> preprocessData(Iterable<DataSource> dataSources) {
		// nothing to do
		return null;
	}

	@Override
	public long getMaximumPairCount() {
		if (!this.dataExtracted()) {
			return 0;
		}

		long maxPairCount = 0;
		final Iterable<Map.Entry<DataSource, DuDeStorage<DuDeObject>>> dataSources = this.getData();
		for (Map.Entry<DataSource, DuDeStorage<DuDeObject>> dataSource1Entry : dataSources) {
			final DataSource dataSource1 = dataSource1Entry.getKey();

			// this check has to be done to avoid considering a DataSource pair twice (anti-symmetry) or calculating the squared pair count for the
			// DataSource (anti-reflexivity) itself
			boolean dataSource1Reached = false;
			for (Map.Entry<DataSource, DuDeStorage<DuDeObject>> dataSource2Entry : dataSources) {
				final DataSource dataSource2 = dataSource2Entry.getKey();
				if (!dataSource1Reached) {
					if (dataSource1.equals(dataSource2)) {
						dataSource1Reached = true;
					}

					continue;
				}

				maxPairCount += dataSource1.getExtractedRecordCount() * dataSource2.getExtractedRecordCount();
			}
		}

		return maxPairCount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.data == null) ? 0 : this.data.hashCode());
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

		AbstractRecordLinkage other = (AbstractRecordLinkage) obj;
		if (this.data == null) {
			if (other.data != null) {
				return false;
			}
		} else if (!this.data.equals(other.data)) {
			return false;
		}

		return true;
	}

}
