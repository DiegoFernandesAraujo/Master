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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import dude.algorithm.SortingDuplicateDetection;
import dude.util.AbstractIterator;
import dude.util.data.DuDeObject;
import dude.util.data.DuDeObjectPair;
import dude.util.data.storage.JsonableReader;
import dude.util.sorting.sortingkey.SortingKey;

/**
 * <code>NaiveBlockingAlgorithm</code> is the naive blocking approach. All {@link DuDeObject}s that have the same {@link SortingKey} value are located
 * in the same block, i.e. will be compared with each other.
 * 
 * @author Matthias Pohl
 * @author Uwe Draisbach
 */
public class NaiveBlockingAlgorithm extends SortingDuplicateDetection {
	
	// number of characters of the sorting key that are used as blocking criterion.
	private int nrCharForBlocking;

	private class NaiveBlockingAlgorithmIterator extends AbstractIterator<DuDeObjectPair> {

		private final SortingKey sortingKey;
		private final Iterator<DuDeObject> dataIterator;
		// number of characters of the sorting key that are used as blocking criterion.
		private int nrCharBlockCriterion;

		private final Collection<DuDeObject> currentBlock = new ArrayList<DuDeObject>();
		private Iterator<DuDeObject> currentBlockIterator;
		private boolean blockModified = true;

		private DuDeObject currentLeftObject;

		public NaiveBlockingAlgorithmIterator(SortingKey sortingKey, JsonableReader<DuDeObject> reader,
				int nrCharBlockCriterion) {
			this.sortingKey = sortingKey;
			this.dataIterator = reader.iterator();
			this.nrCharBlockCriterion = nrCharBlockCriterion;

			if (this.dataIterator.hasNext()) {
				this.loadNextLeftObject();
				this.initializeNextBlock();

				this.loadNextLeftObject();
			}
		}

		private void initializeNextBlock() {
			this.currentBlock.clear();
			this.currentBlock.add(this.currentLeftObject);

			this.currentBlockIterator = this.currentBlock.iterator();
		}

		private void extendCurrentBlock() {
			this.currentBlock.add(this.currentLeftObject);
			this.currentBlockIterator = this.currentBlock.iterator();

			this.blockModified = true;
		}

		private void loadNextLeftObject() {
			if (!this.dataIterator.hasNext()) {
				// no elements left -> clear block
				this.currentBlock.clear();
				return;
			}

			this.currentLeftObject = this.dataIterator.next();
		}

		private boolean isInSameBlock(DuDeObject currentBlockElement) {
			if (!this.blockModified) {
				return true;
			}

			//final JsonArray leftSortingKeyValue = this.sortingKey.getKeyValue(this.currentLeftObject);
			//final JsonArray rightSortingKeyValue = this.sortingKey.getKeyValue(currentBlockElement);
			//return leftSortingKeyValue.equals(rightSortingKeyValue);
			
			String leftSortingKeyValue = this.sortingKey.getKeyString(this.currentLeftObject);
			String rightSortingKeyValue = this.sortingKey.getKeyString(currentBlockElement);
			
			if (this.nrCharBlockCriterion > 0) {
				if (leftSortingKeyValue.length() > this.nrCharBlockCriterion) {
					leftSortingKeyValue = leftSortingKeyValue.substring(0, this.nrCharBlockCriterion);
				}
				if (rightSortingKeyValue.length() > this.nrCharBlockCriterion) {
					rightSortingKeyValue = rightSortingKeyValue.substring(0, this.nrCharBlockCriterion);
				}				
			}
			
			return leftSortingKeyValue.equals(rightSortingKeyValue);
			
		}

		private DuDeObjectPair generatePair(DuDeObject rightObject) {
			return new DuDeObjectPair(this.currentLeftObject, rightObject);
		}

		@Override
		protected DuDeObjectPair loadNextElement() {
			do {
				if (this.currentBlock.isEmpty()) {
					// end of data reached
					return null;
				}

				if (!this.currentBlockIterator.hasNext()) {
					if (!this.dataIterator.hasNext()) {
						// end of data reached
						this.currentBlock.clear();
						return null;
					}

					this.extendCurrentBlock();
					this.loadNextLeftObject();
				}

				DuDeObject currentRightObject = this.currentBlockIterator.next();
				if (this.isInSameBlock(currentRightObject)) {
					this.blockModified = false;
					return this.generatePair(currentRightObject);
				}

				this.initializeNextBlock();
				this.loadNextLeftObject();
			} while (true);
		}

	}

	/**
	 * For serialization.
	 */
	protected NaiveBlockingAlgorithm() {
		super();
		// nothing to do
	}

	/**
	 * Initializes a <code>NaiveBlockingAlgorithm</code> with the passed {@link SortingKey}.
	 * 
	 * @param sortingKey
	 *            The <code>SortingKey</code> that is used for defining the blocks. All {@link DuDeObject}s having the same generated
	 *            <code>SortingKey</code> will be include in one block.
	 */
	public NaiveBlockingAlgorithm(SortingKey sortingKey) {
		super(sortingKey);
		// nothing to do
	}
	
	/**
	 * Initializes a <code>NaiveBlockingAlgorithm</code> with the passed {@link SortingKey}.
	 * 
	 * @param sortingKey
	 *            The <code>SortingKey</code> that is used for defining the blocks. All {@link DuDeObject}s having the same generated
	 *            <code>SortingKey</code> will be include in one block.
	 * @param nrCharForBlocking           
	 *    		  The number of characters of the <code>SortingKey</code> that are used as blocking criterion.
	 */
	public NaiveBlockingAlgorithm(SortingKey sortingKey, int nrCharForBlocking) {
		super(sortingKey);
		this.nrCharForBlocking = nrCharForBlocking;
	}

	@Override
	protected Iterator<DuDeObjectPair> createIteratorInstance() {
		return new NaiveBlockingAlgorithmIterator(this.getSortingKey(), this.getData(), this.getNrCharForBlocking());
	}
	
	/**
	 * Set the number of characters of the sorting key that are used as blocking criterion.
	 * A value of 0 configures the usage of the whole sorting key.
	 * 
	 * @param nrCharForBlocking
	 * 			Number of characters of the sorting key that are used as blocking criterion
	 */
	public void setNrCharForBlocking(int nrCharForBlocking) {
		this.nrCharForBlocking = nrCharForBlocking;
	}
	
	/**
	 * Returns the number of characters of the sorting key that are used as blocking criterion.
	 * A value of 0 configures the usage of the whole sorting key.
	 * 
	 * @return
	 * 			Number of characters of the sorting key that are used as blocking criterion
	 */
	public int getNrCharForBlocking() {
		return this.nrCharForBlocking;
	}

}
