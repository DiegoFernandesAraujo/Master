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

package dude.algorithm.duplicatedetection;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import dude.algorithm.SortingDuplicateDetection;
import dude.util.AbstractIterator;
import dude.util.data.DuDeObject;
import dude.util.data.DuDeObjectPair;
import dude.util.sorting.sortingkey.SortingKey;

/**
 * <code>SortedNeighborhoodMethod</code> is a simple Sorted-Neighborhood Method implementation without allowing multiple runs.
 * 
 * @author Matthias Pohl
 */
public class SortedNeighborhoodMethod extends SortingDuplicateDetection {

	/**
	 * <code>SortedNeighborhoodMethod.SortedNeighborhoodMethodIterator</code> implements the behavior of a simple Sorted-Neighborhood-Method
	 * implementation.
	 * 
	 * @author Matthias Pohl
	 */
	protected static class SortedNeighborhoodMethodIterator extends AbstractIterator<DuDeObjectPair> {

		private final int windowSize;

		private final Iterator<DuDeObject> dataIterator;

		private final Queue<DuDeObject> windowQueue = new LinkedList<DuDeObject>();
		private Iterator<DuDeObject> windowIterator = null;

		private DuDeObject currentObject = null;

		/**
		 * Initializes a SNM iterator with the given window size and data iterator.
		 * 
		 * @param wndSz
		 *            The window size.
		 * @param dataIter
		 *            The data, that shall be processed.
		 */
		protected SortedNeighborhoodMethodIterator(int wndSz, Iterator<DuDeObject> dataIter) {
			if (wndSz < 2) {
				throw new IllegalArgumentException("The window size needs to be larger than 1.");
			} else if (dataIter == null) {
				throw new NullPointerException();
			}

			this.windowSize = wndSz;
			this.dataIterator = dataIter;
		}

		private boolean fullWindow() {
			return this.windowQueue.size() >= this.windowSize - 1;
		}

		private boolean emptyWindow() {
			return this.windowQueue.isEmpty();
		}

		private boolean endOfWindowReached() {
			return this.windowIterator == null || !this.windowIterator.hasNext();
		}

		@Override
		protected DuDeObjectPair loadNextElement() {
			// checks whether no pairs can be created anymore
			if (!this.dataIterator.hasNext() && this.endOfWindowReached()) {
				return null;
			}

			// loads first element into window
			if (this.dataIterator.hasNext() && this.emptyWindow()) {
				this.windowQueue.add(this.dataIterator.next());
				this.windowIterator = this.windowQueue.iterator();

				if (this.dataIterator.hasNext()) {
					this.currentObject = this.dataIterator.next();
				} else {
					// indicates that the end of the window reached
					this.windowIterator = null;
					return null;
				}
			}

			// if the end of the window was reached - queue has to be modified
			if (this.endOfWindowReached()) {
				// remove first element from queue if the queue is full
				if (this.fullWindow()) {
					this.windowQueue.remove();
				}
				// add the current element to the queue
				this.windowQueue.add(this.currentObject);

				// load the new element
				this.currentObject = this.dataIterator.next();

				// re-initializes the queue iterator so that the iterator starts at position 0 again
				this.windowIterator = this.windowQueue.iterator();
			}

			return new DuDeObjectPair(this.windowIterator.next(), this.currentObject);
		}

	}

	/**
	 * The default window size that is used, if no window size was specified.
	 */
	public static final int DEFAULT_WINDOW_SIZE = 30;

	private int windowSize;

	/**
	 * For serialization
	 */
	protected SortedNeighborhoodMethod() {
		super();
		// nothing to do
	}
	
	/**
	 * Initializes a <code>SortedNeighborhoodMethod</code> instance with the passed {@link SortingKey} the {@link #DEFAULT_WINDOW_SIZE}.
	 * 
	 * @param key
	 *            The <code>SortingKey</code> that defines the sorting order during the preprocessing.
	 * 
	 * @throws NullPointerException
	 *             If <code>null</code> was passed instead of a <code>SortingKey</code>.
	 * @throws IllegalArgumentException
	 *             If a window size less than 2 was passed.
	 */
	public SortedNeighborhoodMethod(SortingKey key) {
		this(key, SortedNeighborhoodMethod.DEFAULT_WINDOW_SIZE);
	}

	/**
	 * Initializes a <code>SortedNeighborhoodMethod</code> instance with the passed {@link SortingKey} and a window size.
	 * 
	 * @param key
	 *            The <code>SortingKey</code> that defines the sorting order during the preprocessing.
	 * @param windowSz
	 *            The size of the underlying window.
	 * 
	 * @throws NullPointerException
	 *             If <code>null</code> was passed instead of a <code>SortingKey</code>.
	 * @throws IllegalArgumentException
	 *             If a window size less than 2 was passed.
	 */
	public SortedNeighborhoodMethod(SortingKey key, int windowSz) {
		super(key);
		
		this.setWindowSize(windowSz);
	}

	/**
	 * Returns the window size of this instance.
	 * 
	 * @return The window size of this instance.
	 */
	public int getWindowSize() {
		return this.windowSize;
	}

	/**
	 * Sets the window size.
	 * 
	 * @param windowSz
	 *            The window size.
	 * 
	 * @throws IllegalArgumentException
	 *             If a window size less than 2 was passed.
	 */
	public void setWindowSize(int windowSz) {
		if (windowSz < 2) {
			throw new IllegalArgumentException("The window size needs to be larger than 1.");
		}

		this.windowSize = windowSz;
	}

	@Override
	protected Iterator<DuDeObjectPair> createIteratorInstance() {
		return new SortedNeighborhoodMethodIterator(this.getWindowSize(), this.getData().iterator());
	}

}
