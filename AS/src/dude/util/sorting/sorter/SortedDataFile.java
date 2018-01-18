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

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;

import dude.util.data.DuDeObject;
import dude.util.data.storage.FileBasedStorage;
import dude.util.data.storage.JsonableReader;

/**
 * <code>SortedDataFile</code> encapsulates the functionality that is needed for the {@link TwoPhaseMultiWayMergeSorter} in phase two. It provides a
 * method for removing the file and methods for iterating over the content of the file.
 * 
 * @author Matthias Pohl
 */
public class SortedDataFile implements Closeable {

	private DuDeObject currentObject;

	private FileBasedStorage<DuDeObject> dataFile;

	private JsonableReader<DuDeObject> dataReader;
	private Iterator<DuDeObject> dataIterator;

	/**
	 * Initializes a <code>SortedDataFile</code> and loads the first element. Therefore calling {@link #next()} after calling this constructor returns
	 * the second element.
	 * 
	 * @param file
	 *            The {@link FileBasedStorage} that is read.
	 */
	public SortedDataFile(FileBasedStorage<DuDeObject> file) {
		this.dataFile = file;

		this.dataReader = this.dataFile.getReader();
		this.dataIterator = this.dataReader.iterator();

		// loads the first element
		this.next();
	}

	/**
	 * Returns the element on which the iterating pointer points right now.
	 * 
	 * @return The current element or <code>null</code>, if the end of the file is reached.
	 */
	public DuDeObject current() {
		return this.currentObject;
	}

	/**
	 * Deletes the file from the file system and clears the member data of this instance.
	 */
	public void deleteFile() {
		this.dataFile.delete();

		this.dataFile = null;
		this.currentObject = null;
		this.dataIterator = null;
	}

	/**
	 * Checks whether the end of the file is reached.
	 * 
	 * @return <code>true</code>, if this file has still data to read; otherwise <code>false</code>.
	 */
	public boolean hasNext() {
		if (!this.isAccessible()) {
			return false;
		}

		return this.dataIterator.hasNext();
	}

	/**
	 * Sets the the iteration pointer to the next element and returns it.
	 * 
	 * @return The next {@link DuDeObject} instance in this collection or <code>null</code>, if the end of the file is reached.
	 */
	public DuDeObject next() {
		if (!this.isAccessible()) {
			this.currentObject = null;
			return null;
		}

		this.currentObject = this.dataIterator.next();

		return this.currentObject;
	}

	/**
	 * Checks whether the file can be accessed.
	 * 
	 * @return <code>true</code>, if the file can be accessed; otherwise <code>false</code>.
	 */
	protected boolean isAccessible() {
		return this.dataIterator != null;
	}

	/**
	 * Returns the {@link FileBasedStorage} that is encapsulated in this instance.
	 * 
	 * @return The <code>FileBasedStorage</code>.
	 */
	public FileBasedStorage<DuDeObject> getDataFile() {
		return this.dataFile;
	}

	@Override
	public void close() throws IOException {
		this.dataReader.close();
	}

}
