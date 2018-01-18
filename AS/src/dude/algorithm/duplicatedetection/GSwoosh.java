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
 * <code>GSwoosh</code> implements the GSwoosh duplicate detection (and merging) algorithm
 * as described in the paper Swoosh: a generic approach for entity resolution.
 * 
 * It is important to note that the {@link SimilarityFunction} to be used with GSwoosh needs to use the {@link CrossProductStrategy}.
 * The method <code>setCrossProductStrategy</code> can be used to set this strategy.
 * 
 * @author Johannes Dyck
 * 
 */
public class GSwoosh extends AbstractDuplicateDetection {

	private ComparisonResult notification = ComparisonResult.NON_DUPLICATE;
	
	private Merger merger;
	
	public enum ComparisonResult {		
		DUPLICATE,		
		NON_DUPLICATE
	}	
	
	/**
	 * Sets the strategy for comparing arrays of the passed {@link ContentBasedSimilarityFunction} to the CrossProductStrategy.
	 * 
	 * @param simFunction
	 *            The similarity function that will be used to compare {@link DuDeObject}s.
	 */
	public static void setCrossProductStrategy(ContentBasedSimilarityFunction<?> simFunction) {
		simFunction.setCompareArrayArrayStrategy(new CrossProductStrategy());
	}
	
	/**
	 * <code>GSwooshIterator</code> implements the comparison behavior of the GSwoosh algorithm.
	 * In order to correctly handle the duplicate notifications, it implements the {@link Iterator} interface.
	 * 
	 */
	private class GSwooshIterator implements Iterator<DuDeObjectPair>{
		private Iterator<DuDeObject> leftIter;
		private Iterator<DuDeObject> rightIter;
		
		// each element from the data on the left will be compared to		
		// each element on the right and will then be added to the data on the right 
		private List<DuDeObject> leftData;
		private List<DuDeObject> rightData;
		
		// Merged objects will be added to this list.
		// If the left list is empty, this list is added to the left data.
		private List<DuDeObject> mergedDuplicates;
		
		// current elements
		private DuDeObject leftElement;
		private DuDeObject rightElement;
		
		private boolean newLeft = true;
			
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
			
			// add the pair of the element and itself because GSwoosh does not assume idempotence
			if (this.newLeft) {
				this.newLeft = false;
				this.rightElement = this.leftElement;
				return new DuDeObjectPair(this.leftElement, this.leftElement);				
			}
			
			if (this.rightIter.hasNext()) {
				// add the current left element and the next element on the right
				this.rightElement = this.rightIter.next();
				return new DuDeObjectPair(this.leftElement, this.rightElement);
			} else {
				// all elements on the right have been compared,
				// so the current left element is added to the data on the right 
				this.rightData.add(this.leftElement);
				this.rightIter = this.rightData.iterator();
		
				// get the next left element, if possible
				if (this.leftIter.hasNext()) {
					this.leftElement = this.leftIter.next();
					this.rightElement = this.leftElement;
					
					// add a pair consisting of the new left element and itself
					return new DuDeObjectPair(this.leftElement, this.leftElement);
				} else {
					// there is no new element on the left, so the merged duplicates are added to the left data
					this.leftData.clear();
					this.leftData.addAll(mergedDuplicates);
					this.mergedDuplicates.clear();
					this.leftIter = this.leftData.iterator();
					
					// did the left data get any new objects from the merged duplicates?
					if (this.leftIter.hasNext()) {
						this.leftElement = this.leftIter.next();
						this.rightElement = this.leftElement;
						return new DuDeObjectPair(this.leftElement, this.leftElement);
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
		 * The merged object is inserted into the list of merged duplicates if not already contained in the data
		 * and will later be added to the left data set. 
		 */
		private void handleNotification() {
			if (GSwoosh.this.notification == ComparisonResult.DUPLICATE) {
				DuDeObject merged = GSwoosh.this.merge(this.leftElement, this.rightElement);
								
				// check whether merged object is already contained in any form				
				if (!(leftData.contains(merged) || mergedDuplicates.contains(merged) || rightData.contains(merged))) {
					if (!merged.equals(this.leftElement) && !merged.equals(this.rightElement)) {
						// leftData is empty at the beginning, so we specifically check both records merged into the result.
						// By doing this we prevent the insertion of non-merged records merged with itself into the list of merged duplicates. 
						this.mergedDuplicates.add(merged);
					}					
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
			if (this.rightIter.hasNext()) {
				return true;
			} else if (this.leftIter.hasNext()) {
				return true;			
			} else if (!this.mergedDuplicates.isEmpty()) {
				return true;
			} else if (this.leftElement != null && this.newLeft) {
				// if data set contains just one record
				return true;
			} else if (this.leftElement != null && GSwoosh.this.notification == ComparisonResult.DUPLICATE) {
				// If the last comparison produced a duplicate, we need to merge the records,
				// which can only be done by calling loadNextElement.
				// However, there can only be a next element if the result of the merge operation will indeed
				// be inserted into the set of merged duplicates.
				
				// check whether merged object is already contained in any form
				DuDeObject merged = GSwoosh.this.merge(this.leftElement, this.rightElement);
				if (!(leftData.contains(merged) || mergedDuplicates.contains(merged) || rightData.contains(merged))) {
					if (!merged.equals(this.leftElement) && !merged.equals(this.rightElement)) {
						// leftData is empty at the beginning, so we specifically check both records merged into the result.
						// By doing this we prevent the insertion of non-merged records merged with itself into the list of merged duplicates.
						return true;
					}					
				}				
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
		 * Throws an exception as the <code>GSwooshIterator</code> is not intended to remove objects.
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException("GSwooshIterator does not implement remove().");
		}

		/**
		 * Initializes the iterator instance by initializing the two data sets
		 * and their iterators used by the GSwoosh algorithm.
		 * 
		 * @param algo The GSwoosh algorithm instance
		 */
		public GSwooshIterator(GSwoosh algo) {
			this.leftData = new LinkedList<DuDeObject>();
			
			// start with all the data on the left side
			this.leftIter = algo.getData().iterator();

			// start with an empty right side
			this.rightData = new LinkedList<DuDeObject>();
			this.rightIter = rightData.iterator();
			
			this.mergedDuplicates = new LinkedList<DuDeObject>();
			this.leftElement = this.initializeFirstElement();			
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
	 * Initializes the <code>GSwoosh</code> algorithm with the {@link DefaultMerger}.
	 * 
	 */
	public GSwoosh() {
		super();
		this.merger = new DefaultMerger();
	}

	/**
	 * Initializes the <code>GSwoosh</code> algorithm with the passed {@link Merger}.
	 * 
	 * @param merger
	 *            The {@link Merger} to be used with this <code>GSwoosh</code> instance.
	 */
	public GSwoosh(Merger merger) {
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
		return this.merger.merge(leftElement, rightElement);
	}

	/**
	 * Notifies the <code>GSwoosh</code> algorithm of the result of the last comparison.
	 * This method needs to be called after each comparison.
	 * 
	 * @param c
	 *            The ComparisonResult (either DUPLICATE or NON_DUPLICATE) of the last comparison.
	 */
	public void setNotification(ComparisonResult c) {
		this.notification = c;
	}

	/**
	 * Creates a new instance of the <code>GSwooshIterator</code>.
	 * 
	 * @return The new iterator instance.
	 */
	@Override
	protected Iterator<DuDeObjectPair> createIteratorInstance() {
		return new GSwooshIterator(this);
	}	
	
}
