/*
 * DuDe - The Duplicate Detection Toolkit
 * 
 * Copyright (C) 2011  Hasso-Plattner-Institut für Softwaresystemtechnik GmbH,
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
import java.util.List;
import java.util.NoSuchElementException;

import dude.algorithm.AbstractDuplicateDetection;
import dude.similarityfunction.contentbased.ContentBasedSimilarityFunction;
import dude.similarityfunction.contentbased.calculationstrategy.CrossProductStrategy;
import dude.util.data.DuDeObject;
import dude.util.data.DuDeObjectPair;
import dude.util.data.DuDeObjectPair.GeneratedBy;
import dude.util.merger.DefaultMerger;
import dude.util.merger.Merger;

/**
 * <code>RSwoosh</code> implements the RSwoosh duplicate detection (and merging) algorithm
 * as described in the paper Swoosh: a generic approach for entity resolution.
 * 
 * It is important to note that the {@link SimilarityFunction} to be used with RSwoosh needs to use the {@link CrossProductStrategy}.
 * The method <code>setCrossProductStrategy</code> can be used to set this strategy.
 * 
 * Furthermore, this algorithm assumes that the idempotence, commutativity, associativity
 * and representativity properties (as described in the paper) hold.
 * 
 * @author Johannes Dyck
 * 
 */
public class RSwoosh extends AbstractDuplicateDetection {

	private ComparisonResult notification = ComparisonResult.NON_DUPLICATE;
	
	private Merger merger;
	
	public enum ComparisonResult {		
		DUPLICATE,		
		NON_DUPLICATE
	}	
	
	/**
	 * Sets the strategy for comparing array of the passed {@link ContentBasedSimilarityFunction} to the CrossProductStrategy.
	 * 
	 * @param simFunction
	 *            The similarity function that will be used to compare {@link DuDeObject}s.
	 */
	public static void setCrossProductStrategy(ContentBasedSimilarityFunction<?> simFunction) {
		simFunction.setCompareArrayArrayStrategy(new CrossProductStrategy());
	}
	
	/**
	 * <code>RSwooshIterator</code> implements the comparison behavior of the RSwoosh algorithm.
	 * In order to correctly handle the duplicate notifications, it implements the {@link Iterator} interface.
	 */	
	private class RSwooshIterator implements Iterator<DuDeObjectPair>{

		private Iterator<DuDeObject> leftIter;
		private Iterator<DuDeObject> rightIter;
	
		// Each element from the data on the left will be compared to
		// each element on the right and will then be added to the data on the right.
		// This process will be interrupted when a duplicate is found.
		private List<DuDeObject> leftData;
		private List<DuDeObject> rightData;
		private List<DuDeObject> mergedDuplicates;
		
		// current elements
		private DuDeObject leftElement;
		private DuDeObject rightElement;
		
		// this flag signals whether a duplicate with the current left element has been found
		private boolean foundDuplicate;
				
		/**
		 * Determines the next two {@link DuDeObject}s to be compared with each other
		 * and returns them in a {@link DuDeObjectPair} or <code>null</code> if there are
		 * no more objects to compare.
		 * 
		 * @return The next pair of elements to compare.
		 */
		protected DuDeObjectPair loadNextElement() {
			if (this.leftElement == null) {
				return null;
			}
			
			// check the result of the last comparison and merge, if necessary
			handleNotification();
			
			if (this.rightIter.hasNext()) {
				this.rightElement = this.rightIter.next();
				return new DuDeObjectPair(this.leftElement, this.rightElement);
			} else {
				// each element on the right side has been compared with the current left element
				if (!this.foundDuplicate) {
					// add the left element only if it has not been merged
					this.rightData.add(this.leftElement);
				}
				this.foundDuplicate = false; // reset the flag
				
				// Try to reset the iterator for the data on the right.
				// Since the original objects of merged objects are removed,
				// it might be necessary to also add the merged duplicates to the left data
				// and add one element from the new left data to the right data. 
				while (rightData.isEmpty()) {
					if (leftIter.hasNext()) {
						this.leftElement = this.leftIter.next();
						this.rightData.add(leftElement);						
					} else {
						this.leftData.clear();
						this.leftData.addAll(mergedDuplicates);
						this.mergedDuplicates.clear();
						this.leftIter = this.leftData.iterator();
						if (this.leftIter.hasNext()) {														
							return null;
						}
					}
				}
				
				this.rightIter = this.rightData.iterator();
				this.rightElement = this.rightIter.next();
				
				// since the right iterator has finished, we fetch the next element from the left side
				if (this.leftIter.hasNext()) {
					this.leftElement = this.leftIter.next();
					return new DuDeObjectPair(this.leftElement, this.rightElement);
				} else {
					// there is no new left element, so try to add the merged duplicates to the left data
					this.leftData.clear();
					this.leftData.addAll(mergedDuplicates);
					this.mergedDuplicates.clear();
					this.leftIter = this.leftData.iterator();
					
					// did the left side get any new elements?
					if (this.leftIter.hasNext()) {
						this.leftElement = this.leftIter.next();						
						return new DuDeObjectPair(this.leftElement, this.rightElement);
					} else {
						// there is no next element on the right,
						// there is no new element on the left,
						// and there were no new merged duplicates,
						// so there is nothing left to compare
						return null;
					}					
				}
			}			
		}
		
		/**
		 * Checks the notification passed to the algorithm. If a duplicate is encountered, the records are merged.
		 * The merged object will eventually be inserted into the left data set.
		 */
		private void handleNotification() {
			if (RSwoosh.this.notification == ComparisonResult.DUPLICATE) {
				this.foundDuplicate = true;
				this.mergedDuplicates.add(RSwoosh.this.merge(this.leftElement, this.rightElement));
				this.rightIter.remove();
				
				// ensure that the next call of loadNextElement gets a new left element
				// by iterating through all data on the right side
				while (this.rightIter.hasNext()) {
					this.rightIter.next();					
				}
			}
		}

		/**
		 * Tells whether there are elements left to compare or whether
		 * the next call of <code>next()</code> will return <code>null</code>.
		 * 	 
		 */
		@Override
		public boolean hasNext() {
			if (this.leftIter.hasNext()) {
				return true;
			} else if (this.rightIter.hasNext()) {
				return true;
			} else if (!this.mergedDuplicates.isEmpty()) {
				return true;
			}
			return false;
		}
		
		/**
		 * Calls <code>loadNextElement()</code> to return the next {@link DuDeObjectPair} of objects to compare.
		 * 
		 * @return The next pair of objects.
		 */
		@Override
		public DuDeObjectPair next() {
			DuDeObjectPair currentElement = this.loadNextElement();

			// java specification - NoSuchElementException should be thrown if the end of the underlying data was reached
			if (currentElement == null) {
				throw new NoSuchElementException("End of data reached...");
			}
			currentElement.setLineage(GeneratedBy.Algorithm);

			return currentElement;
		}
		
		/**
		 * Throws an exception as the <code>RSwooshIterator</code> is not intended to remove objects.
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException("RSwooshIterator does not implement remove().");
		}
		
		/**
		 * Initializes the iterator instance by initializing the two data sets
		 * and their iterators used by the RSwoosh algorithm.
		 * 
		 * @param algo The <code>RSwoosh</code> algorithm instance
		 */
		public RSwooshIterator(RSwoosh algo) {
			this.leftData = new LinkedList<DuDeObject>();
			
			// start with all the data on the left side
			this.leftIter = algo.getData().iterator();

			// start with an empty right side
			this.rightData = new LinkedList<DuDeObject>();
			this.rightIter = rightData.iterator();
			
			this.mergedDuplicates = new LinkedList<DuDeObject>();
			this.leftElement = this.initializeFirstElement();
			
			this.foundDuplicate = false;			
		}
		
		/**
		 * Returns the first {@link DuDeObject} from the data set to be iterated over or <code>null</code> if there is none.
		 * This is only called once before the first pair is returned by the iterator.  
		 * 
		 * @return The first element from the data 
		 */
		private DuDeObject initializeFirstElement() {
			if (leftIter.hasNext()) {
				return this.leftIter.next();
			} else {
				return null;
			}
			
		}		
	}

	/**
	 * Initializes the <code>RSwoosh</code> algorithm with an instance of the {@link DefaultMerger}.
	 * 
	 */	
	public RSwoosh() {
		super();
		this.merger = new DefaultMerger();
	}
	
	/**
	 * Initializes the <code>RSwoosh</code> algorithm with the passed {@link Merger}.
	 * 
	 * @param merger
	 *            The {@link Merger} to be used with this <code>RSwoosh</code> instance.
	 */
	public RSwoosh(Merger merger) {		
		super();
		this.merger = merger;
	}
	
	/** 
	 * Merges the input {@link DuDeObject}s by calling the algorithm's merger.
	 * 
	 * @param leftElement One of the <code>DuDeObjects</code> that shall be merged.
	 * @param rightElement The other <code>DuDeObject</code> that shall be merged.
	 * @return A new <code>DuDeObject</code> created by merging the input <code>DuDeObjects</code>.
	 */
	private DuDeObject merge(DuDeObject leftElement, DuDeObject rightElement) {
		return merger.merge(leftElement, rightElement);
	}

	/**
	 * Notifies the <code>RSwoosh</code> algorithm of the result of the last comparison.
	 * This method needs to be called after each comparison.
	 * 
	 * @param c
	 *            The ComparisonResult (either DUPLICATE or NON_DUPLICATE) of the last comparison.
	 */
	public void setNotification(ComparisonResult c) {
		this.notification = c;
	}
	
	/**
	 * Creates a new instance of the <code>RSwooshIterator</code>.
	 * 
	 * @return The new iterator instance.
	 */
	@Override
	protected Iterator<DuDeObjectPair> createIteratorInstance() {
		return new RSwooshIterator(this);
	}
	
}
