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

package dude.algorithm.recordlinkage;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import dude.algorithm.AbstractRecordLinkage;
import dude.datasource.DataSource;
import dude.util.AbstractIterator;
import dude.util.data.DuDeObject;
import dude.util.data.DuDeObjectPair;
import dude.util.data.storage.DuDeStorage;
import dude.util.data.storage.JsonableReader;

/**
 * <p>
 * <code>NaiveRecordLinkage</code> implements the naive approach for record-linkage. Each element of a {@link DataSource} is combined with each
 * element of all other <code>DataSources</code>.
 * 
 * <p>
 * Example:
 * <table style="text-align: center">
 * <tr>
 * <td>A</td>
 * <td>B</td>
 * <td>C</td>
 * </tr>
 * <tr>
 * <td>a<sub>1</sub></td>
 * <td>b<sub>1</sub></td>
 * <td>c<sub>1</sub></td>
 * </tr>
 * <tr>
 * <td>a<sub>2</sub></td>
 * <td>b<sub>2</sub></td>
 * <td>&nbsp;</td>
 * </tr>
 * <tr>
 * <td>a<sub>3</sub></td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * </tr>
 * </table>
 * 
 * The example above will generate the following result:
 * <ol>
 * <li>(a<sub>1</sub>, b<sub>1</sub>)</li>
 * <li>(a<sub>1</sub>, b<sub>2</sub>)</li>
 * <li>(a<sub>1</sub>, c<sub>1</sub>)</li>
 * <li>(a<sub>2</sub>, b<sub>1</sub>)</li>
 * <li>(a<sub>2</sub>, b<sub>2</sub>)</li>
 * <li>(a<sub>2</sub>, c<sub>1</sub>)</li>
 * <li>(a<sub>3</sub>, b<sub>1</sub>)</li>
 * <li>(a<sub>3</sub>, b<sub>2</sub>)</li>
 * <li>(a<sub>3</sub>, c<sub>1</sub>)</li>
 * <li>(b<sub>1</sub>, c<sub>1</sub>)</li>
 * <li>(b<sub>2</sub>, c<sub>1</sub>)</li>
 * </ol>
 * 
 * @author Matthias Pohl
 */
public class NaiveRecordLinkage extends AbstractRecordLinkage {

	/**
	 * <code>NaiveRecordLinkageIterator</code> implements the actual functionality of the naive record-linkage approach.
	 * 
	 * @author Matthias Pohl
	 */
	protected static class NaiveRecordLinkageIterator extends AbstractIterator<DuDeObjectPair> {

		// the actual data
		private final Iterable<Map.Entry<DataSource, DuDeStorage<DuDeObject>>> data;

		// the iterator holding all DataSources that should be used for generating the pair's left element
		private final Iterator<Map.Entry<DataSource, DuDeStorage<DuDeObject>>> leftDataSourceIterator;
		// stores the index of the current left DataSource -> the right DataSource starts at currentLeftDataSourceIndex + 1 in order to avoid
		// symmetric pairs
		private int currentLeftDataSourceIndex = 0;
		// the iterator holding all DataSources that should be used for generating the pair's right element
		private Iterator<Map.Entry<DataSource, DuDeStorage<DuDeObject>>> rightDataSourceIterator;

		// the reader that is used for generating the pair's left element
		private JsonableReader<DuDeObject> leftDataReader;
		// the reader that is used for generating the pair's right element
		private JsonableReader<DuDeObject> rightDataReader;

		// the iterator that corresponds to the leftDataReader
		private Iterator<DuDeObject> leftDataIterator;
		// the iterator that corresponds to the rightDataReader
		private Iterator<DuDeObject> rightDataIterator;

		// the current left element that is used for generating the pairs
		private DuDeObject currentLeftElement;

		/**
		 * Initializes the <code>NaiveRecordLinkageIterator</code> with the passed data.
		 * 
		 * @param data
		 *            The underlying data.
		 */
		protected NaiveRecordLinkageIterator(Iterable<Map.Entry<DataSource, DuDeStorage<DuDeObject>>> data) {
			this.data = data;

			this.leftDataSourceIterator = this.data.iterator();
			this.loadNextLeftDataSource();

			this.rightDataSourceIterator = this.data.iterator();
			this.initializeRightDataSourceIterator();
			this.loadNextRightDataSource();

			if (!this.endOfDataSources()) {
				this.initializeDataIterators();
				this.loadNextLeftElement();
			}
		}

		private void loadNextLeftElement() {
			if (this.leftDataIterator.hasNext()) {
				this.currentLeftElement = this.leftDataIterator.next();
			}

			this.rightDataIterator = this.rightDataReader.iterator();
		}

		private void loadNextLeftDataSource() {
			this.closeLeftDataReader();

			if (this.leftDataSourceIterator.hasNext()) {
				final DuDeStorage<DuDeObject> storage = this.leftDataSourceIterator.next().getValue();
				this.currentLeftDataSourceIndex++;

				if (storage.size() > 0) {
					this.leftDataReader = storage.getReader();
				} else {
					this.loadNextLeftDataSource();
				}
			}
		}

		private void initializeRightDataSourceIterator() {
			for (int i = 0; i < this.currentLeftDataSourceIndex; i++) {
				if (!this.rightDataSourceIterator.hasNext()) {
					throw new IllegalStateException("Different number of data sources loaded.");
				}

				this.rightDataSourceIterator.next();
			}
		}

		private boolean loadNextRightDataSource() {
			this.closeRightDataReader();

			if (this.rightDataSourceIterator.hasNext()) {
				final DuDeStorage<DuDeObject> storage = this.rightDataSourceIterator.next().getValue();

				if (storage.size() > 0) {
					this.rightDataReader = storage.getReader();
					return true;
				}

				return this.loadNextRightDataSource();
			}

			return false;
		}

		private boolean loadNextDataSources() {
			if (!this.leftDataSourceIterator.hasNext() && !this.rightDataSourceIterator.hasNext()) {
				return false;
			}

			if (this.leftDataSourceIterator.hasNext() && !this.rightDataSourceIterator.hasNext()) {
				this.loadNextLeftDataSource();

				this.rightDataSourceIterator = this.data.iterator();
				this.initializeRightDataSourceIterator();
			}

			if (!this.loadNextRightDataSource()) {
				return false;
			}

			this.initializeDataIterators();

			return true;
		}

		private void initializeDataIterators() {
			if (this.leftDataReader == null) {
				throw new IllegalStateException("The left data reader is not initialized.");
			} else if (this.rightDataReader == null) {
				throw new IllegalStateException("The right data reader is not initialized.");
			}

			this.leftDataIterator = this.leftDataReader.iterator();
			this.rightDataIterator = this.rightDataReader.iterator();
		}

		private void closeJsonableReader(JsonableReader<DuDeObject> reader) {
			if (reader == null) {
				return;
			}

			try {
				reader.close();
			} catch (IOException e) {
				NaiveRecordLinkage.logger.warn("IOException occurred while closing the JsonReader.", e);
			}
		}

		private void closeLeftDataReader() {
			this.closeJsonableReader(this.leftDataReader);
			this.leftDataReader = null;
		}

		private void closeRightDataReader() {
			this.closeJsonableReader(this.rightDataReader);
			this.rightDataReader = null;
		}

		private boolean endOfDataSources() {
			return !this.leftDataSourceIterator.hasNext() && !this.rightDataSourceIterator.hasNext();
		}

		private boolean endOfData() {
			return this.endOfLeftDataIterator() && this.endOfRightDataIterator() && this.endOfDataSources();
		}

		private boolean endOfDataIterator(Iterator<?> iterator) {
			return iterator == null || !iterator.hasNext();
		}

		private boolean endOfLeftDataIterator() {
			return this.endOfDataIterator(this.leftDataIterator);
		}

		private boolean endOfRightDataIterator() {
			return this.endOfDataIterator(this.rightDataIterator);
		}

		@Override
		protected DuDeObjectPair loadNextElement() {
			if (this.endOfData()) {
				this.closeLeftDataReader();
				this.closeRightDataReader();

				return null;
			}

			if (!this.rightDataIterator.hasNext()) {
				if (!this.leftDataIterator.hasNext()) {
					if (!this.loadNextDataSources()) {
						return null;
					}
				}

				this.loadNextLeftElement();
			}

			return new DuDeObjectPair(this.currentLeftElement, this.rightDataIterator.next());
		}

	}

	private static final Logger logger = Logger.getLogger(NaiveRecordLinkage.class.getPackage().getName());

	@Override
	protected Iterator<DuDeObjectPair> createIteratorInstance() {
		return new NaiveRecordLinkageIterator(this.getData());
	}

}
