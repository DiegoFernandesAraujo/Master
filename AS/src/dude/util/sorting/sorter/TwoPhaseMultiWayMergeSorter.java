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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;

import dude.util.data.DuDeObject;
import dude.util.data.storage.DuDeStorage;
import dude.util.data.storage.FileBasedStorage;
import dude.util.data.storage.JsonableWriter;
import dude.util.sorting.sortingkey.SortingKey;

/**
 * <code>TwoPhaseMultiWayMergeSorter</code> implements a file-based sorting using the Two-Phase Multi-Way Merge-Sort algorithm (TPMMS).
 * 
 * @author Matthias Pohl
 */
public class TwoPhaseMultiWayMergeSorter extends AbstractDuDeObjectSorter {

	private static final Logger logger = Logger.getLogger(TwoPhaseMultiWayMergeSorter.class.getPackage().getName());

	private final static String TMP_FILENAME_PREFIX = "tmp";
	private final static String SORTED_DATA_FILENAME_PREFIX = "sortedData";

	private final static int TMP_FILE_MODULO = 128;

	private List<SortedDataFile> sortedDataFiles = new ArrayList<SortedDataFile>();

	private int tmpFileCounter;

	/**
	 * Initializes a <code>TwoPhaseMultiWayMergeSorter</code> with no {@link SortingKey}.
	 */
	public TwoPhaseMultiWayMergeSorter() {
		super();
		this.resetTmpFileCounter();
	}

	/**
	 * Initializes a <code>TwoPhaseMultiWayMergeSorter</code> with the passed {@link SortingKey}.
	 * 
	 * @param key
	 *            The sorting key that defines the sorting order.
	 */
	public TwoPhaseMultiWayMergeSorter(SortingKey key) {
		super(key);
		this.resetTmpFileCounter();
	}
	
	private void resetTmpFileCounter() {
		this.tmpFileCounter = 0;
	}

	@Override
	public void clear() {
		super.clear();
		this.removeSortedDataFiles();
		this.resetTmpFileCounter();
	}

	@Override
	public void add(DuDeObject record) {
		super.add(record);

		if (this.memoryLimitReached()) {
			this.generateSortedFile();
			// clears the in-memory collection
			super.clear();

			System.gc();
			System.gc();
			System.gc();
		}
	}

	/**
	 * Merges the the {@link #sortedDataFiles} within the passed range. The processed files will be deleted after the merging is finished.
	 * 
	 * @param startId
	 *            The index of the first {@link SortedDataFile} to merge.
	 * @param endId
	 *            The index of the last <code>SortedDataFile</code> to merge.
	 * @return The merge file.
	 * @throws IOException
	 *             If an error occurs while reading from the file or writing to the file.
	 */
	private FileBasedStorage<DuDeObject> merge(int startId, int endId) throws IOException {
		// the merged file
		final FileBasedStorage<DuDeObject> mergeResult = new FileBasedStorage<DuDeObject>(DuDeObject.class, this.getNextTemporaryFilename());

		final JsonableWriter<DuDeObject> mergeResultWriter = mergeResult.getWriter();

		// collects the indexes of all SortedDataFiles that are already completely processed
		final Collection<Integer> removedDataFiles = new ArrayList<Integer>();

		// stores the number of files that have to be merged
		final int filesToMergeCount = endId - startId + 1;

		// loop is repeated until all files are merged
		while (removedDataFiles.size() < filesToMergeCount) {
			// initialization
			DuDeObject objectToAdd = null;

			// index of the sorted data file from which the nextObject was extracted
			int dataFileIndex = -1;

			// iterates over all SortedDataFiles that shall be merged
			for (int i = startId; i <= endId; ++i) {
				// skip this step if the current data file is already merged completely
				if (removedDataFiles.contains(i)) {
					continue;
				}

				final SortedDataFile currentDataFile = this.sortedDataFiles.get(i);

				if (objectToAdd == null) {
					// initialization
					objectToAdd = currentDataFile.current();
					dataFileIndex = i;
				} else {
					// takes the smallest record in each iteration step depending on the sorting key
					if (this.getSortingKey().compare(objectToAdd, currentDataFile.current()) > 0) {
						// stores the record
						objectToAdd = currentDataFile.current();

						// stores the index of the data file where the smallest record was found
						dataFileIndex = i;
					}
				}
			}

			// checks whether the end of the temporary data file is reached
			if (this.sortedDataFiles.get(dataFileIndex).hasNext()) {
				// loads the next DuDeObject, if the end wasn't reached, yet
				this.sortedDataFiles.get(dataFileIndex).next();
			} else {
				// no elements to process within this file
				// adds the data file to the removedDataFiles collection, since it is merged completely
				removedDataFiles.add(dataFileIndex);
			}

			if (!mergeResultWriter.add(objectToAdd)) {
				TwoPhaseMultiWayMergeSorter.logger.error("DuDeObject couldn't be added to the sorted data file.");
			}
		}

		mergeResultWriter.close();

		this.removeSortedDataFiles();

		return mergeResult;
	}
	
	private void removeSortedDataFiles() {
		ListIterator<SortedDataFile> dataFileIterator = this.sortedDataFiles.listIterator(this.sortedDataFiles.size());
		while (dataFileIterator.hasPrevious()) {
			dataFileIterator.previous().deleteFile();
			dataFileIterator.remove();
		}
	}

	@Override
	public DuDeStorage<DuDeObject> getSortedCollection() throws IOException {
		// sort lastly added data
		if (!this.getInMemoryData().isEmpty()) {
			this.generateSortedFile();
			this.clear();
		}

		// start 2nd phase of TPMMS
		int sortedDataFileCount;
		// last-man-standing -> repeat the loop until only one file is left
		while (this.sortedDataFiles.size() > 1) {
			// a list of all merge results
			List<SortedDataFile> mergedSortedDataFiles = new ArrayList<SortedDataFile>();

			sortedDataFileCount = this.sortedDataFiles.size();
			for (int end = sortedDataFileCount - 1, start = sortedDataFileCount - TwoPhaseMultiWayMergeSorter.TMP_FILE_MODULO; end >= TwoPhaseMultiWayMergeSorter.TMP_FILE_MODULO - 1; end -= TwoPhaseMultiWayMergeSorter.TMP_FILE_MODULO, start -= TwoPhaseMultiWayMergeSorter.TMP_FILE_MODULO) {
				mergedSortedDataFiles.add(new SortedDataFile(this.merge(start, end)));
			}

			sortedDataFileCount = this.sortedDataFiles.size();
			if (sortedDataFileCount > 1) {
				// if more than one file are still available, merge them
				mergedSortedDataFiles.add(new SortedDataFile(this.merge(0, sortedDataFileCount - 1)));
			} else if (sortedDataFileCount == 1) {
				// if only one file is still available, add it to the next iteration
				mergedSortedDataFiles.add(this.sortedDataFiles.get(0));
			}

			// re-initialize the SortedDataFile collection
			this.sortedDataFiles = mergedSortedDataFiles;
		}

		// rename the final sorted file
		FileBasedStorage<DuDeObject> sortedData = this.sortedDataFiles.get(0).getDataFile();
		sortedData.renameTo(this.getSortedDataFilename());
		return sortedData;
	}

	/**
	 * Sorts the in-memory data and writes it into a file. This file is encapsulated in an {@link SortedDataFile} that will be added to
	 * {@link #sortedDataFiles}.
	 */
	protected void generateSortedFile() {
		// generate sorted file - 1st phase of TPMMS
		String filename = this.getNextTemporaryFilename();

		TwoPhaseMultiWayMergeSorter.logger.debug("Sorting starts for '" + filename + FileBasedStorage.JSON_FILE_EXTENSION + "'");
		Collections.sort(this.getInMemoryData(), this.getSortingKey());

		try {
			FileBasedStorage<DuDeObject> tmpStorage = new FileBasedStorage<DuDeObject>(DuDeObject.class, filename, this.getInMemoryData());

			this.sortedDataFiles.add(new SortedDataFile(tmpStorage));
		} catch (IOException e) {
			TwoPhaseMultiWayMergeSorter.logger.error("Unable to store sorted data in temporary file '" + filename + "'(IOException occurred).", e);
		}
	}

	/**
	 * Returns the name of the next temporary {@link DuDeObjectFile}.
	 * 
	 * @return The next file name that shall be used.
	 */
	protected String getNextTemporaryFilename() {
		return TwoPhaseMultiWayMergeSorter.TMP_FILENAME_PREFIX + this.tmpFileCounter++;
	}

	/**
	 * Returns the name of the {@link DuDeObjectFile} containing the sorted data. This file name is a concatenation of
	 * {@link #SORTED_DATA_FILENAME_PREFIX} and the current time in milliseconds.
	 * 
	 * @return The name of the file that contains the sorted data.
	 */
	protected String getSortedDataFilename() {
		return TwoPhaseMultiWayMergeSorter.SORTED_DATA_FILENAME_PREFIX + System.currentTimeMillis();
	}

}
