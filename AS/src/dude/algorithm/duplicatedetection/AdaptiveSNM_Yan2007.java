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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.log4j.Logger;

import uk.ac.shef.wit.simmetrics.similaritymetrics.InterfaceStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;
import dude.algorithm.SortingDuplicateDetection;
import dude.util.AbstractIterator;
import dude.util.data.DuDeObject;
import dude.util.data.DuDeObjectPair;
import dude.util.sorting.sortingkey.SortingKey;


/**
 * Implementation of the adative Sorted Neighborhood Methods presented
 * by Yan et.al. in JCDL'07. 
 * The paper presents 2 algorithms:
 * - Incrementally-Adaptive SNM (IA-SNM)
 * - Accumulatively-Adadptive SNM (AA-SNM)
 * 
 * @author Uwe Draisbach
 *
 */
public class AdaptiveSNM_Yan2007 extends SortingDuplicateDetection {
	
	private static final Logger logger = Logger.getLogger(AdaptiveSNM_Yan2007.class.getPackage().getName());
	
	private AlgorithmVariant variant; // IA-SNM or AA-SNM
	private float threshold;
	private int sortingKeyComparisons = 0;
	private int numberOfCreatedBlocks = 0;
	private int sumAssignedRecords = 0;
	
	/**
	 * Initializes a <code>AdaptiveSNM_Yan2007</code> instance with the passed windows size.
	 * 
	 * @param variant
	 *            The variant specifies if IA-SNM or AA-SNM is used for creating record pairs.
	 * @param sortingKey
	 *            The sorting key specifies the sorting order.
	 * @param threshold
	 * 			  Threshold for the similarity of the sorting keys. Used for defining the window borders.  
	 */
	public AdaptiveSNM_Yan2007(AlgorithmVariant variant, SortingKey sortingKey, float threshold) {
		super(sortingKey);
		
		this.variant = variant;
		this.threshold = threshold;
	}
	
	/**
	 * Returns the sorting key.
	 * 
	 * @return The sorting key.
	 */
	public float getThreshold() {
		return this.threshold;
	}
	
	/**
	 * Set the threshold.
	 * 
	 * @param threshold
	 * 			The new threshold.
	 */
	public void setThreshold(float threshold) {
		this.threshold = threshold;
	}
	
	/**
	 * Returns the number of distance comparisons of two sorting key values.
	 * 
	 * @return The number of distance comparisons of two sorting key values.
	 */
	public int getSortingKeyComparisons() {
		return this.sortingKeyComparisons;
	}
	
	/**
	 * Returns the number of created blocks
	 * 
	 * @return The number of created blocks
	 */
	public int getNumberCreatedBlocks() {
		return this.numberOfCreatedBlocks;
	}
	
	/**
	 * Returns the sum of records that are already assigned to a block.
	 * 
	 * @return The number of distance comparisons of two sorting key values.
	 */
	public int getNumberAssignedRecords() {
		return this.sumAssignedRecords;
	}
	
	
	/**
	 * Resets the algorithm. The number of sorting key comparisons, the number of created blocks
	 * and the number of records already in blocks are set to 0.
	 */
	public void reset() {
		this.sortingKeyComparisons = 0;
		this.numberOfCreatedBlocks = 0;
		this.sumAssignedRecords = 0;
	}
	

	@Override
	protected Iterator<DuDeObjectPair> createIteratorInstance() {
		switch (this.variant) {
			case IA_SNM:
				return new IA_SNM_Iterator(this.getSortingKey(), this.threshold, this.getData().iterator());
			case AA_SNM:
				return new AA_SNM_Iterator(this.getSortingKey(), this.threshold, this.getData().iterator());
		}
		return null;
	}
	
	/**
	 * This enumeration collects the possible SNM variants.
	 * 
	 * @author Uwe Draisbach
	 */
	public enum AlgorithmVariant {
		/**
		 * Incrementally-Adaptive Sorted Neighborhood Method
		 */
		IA_SNM,
		/**
		 * Accumulatively-Adaptive Sorted Neighborhood Method
		 */
		AA_SNM
	}
	
	/**
	 * Abstract <code>Iterator</code> implementation that is used by the different
	 * adaptive Sorted Neighborhood methods.
	 * 
	 * @author Uwe Draisbach
	 */
	protected abstract class YanIterator extends AbstractIterator<DuDeObjectPair> {
		
		/** 
		 * the distance threshold
		 */
		protected float phi;

		/**
		 * Iterator for the extracted records from the data sources
		 */
		protected Iterator<DuDeObject> dataIterator;
		
		/**
		 * Queue with already extracted records from the dataIterator that are
		 * not yet finally assigned to a block.
		 */
		protected Queue<DuDeObject> recordQueue = new LinkedList<DuDeObject>();
		/**
		 * Iterator for the recordQueue
		 */
		protected Iterator<DuDeObject> recordQueueIterator = this.recordQueue.iterator();
		
		/**
		 * Queue that contains the records of the current block
		 */
		protected Queue<DuDeObject> blockQueue;
		/**
		 * Iterator for the records of the current block
		 */
		protected Iterator<DuDeObject> blockQueueIterator;
		
		/** current record that is used to create record pairs
		 * with all other records in the block
		 */
		protected DuDeObject currentRec;
		
		/**
		 *  Sorting key for the records
		 */
		protected SortingKey sortingKey_iterator;
		
		/**
		 *  comparator used to calculate the distance between two sorting keys
		 */
		protected InterfaceStringMetric comparator = new Levenshtein();
		
		/**
		 * Constructor
		 * 
		 * @param sortingKey
		 * 			The sorting key used to sort the records.
		 * @param phi
		 * 			The threshold used to determine the boundary pairs.
		 * @param dataIterator
		 *          Iterator for the data, that shall be processed.
		 */
		public YanIterator(SortingKey sortingKey, float phi, Iterator<DuDeObject> dataIterator) {
			if (phi < 0.0f || phi > 1.0f) {
				throw new IllegalArgumentException("Threshold phi has to be 0 <= phi <= 1.");
			}
			
			if (dataIterator == null) {
				throw new NullPointerException("DataIterator is null.");
			}
			
			this.phi = phi;
			this.sortingKey_iterator = sortingKey;
			
			if (dataIterator.hasNext()) {
				this.dataIterator = dataIterator;	
			} else {
				// if the dataIterator has no records, set this.dataIterator = null
				this.dataIterator = null;
			}
		}
		
		@Override
		protected DuDeObjectPair loadNextElement() {
			
			// checks whether no pairs can be created anymore
			if (this.dataIterator == null) {
				return null;
			}
			
			// check if the method is called the first time
			if (this.blockQueueIterator == null) {
				this.getNextBlock();
			}
			
			if (this.blockQueueIterator.hasNext()) {
				return new DuDeObjectPair(this.currentRec, this.blockQueueIterator.next());
			} else if (this.blockQueue.size() >= 2) {
				this.currentRec = this.blockQueue.poll();
				this.blockQueueIterator = this.blockQueue.iterator();
				return new DuDeObjectPair(this.currentRec, this.blockQueueIterator.next());
			} else if (this.nextBlockExists()) {
				this.getNextBlock();
				if (this.blockQueueIterator.hasNext()) {
					return new DuDeObjectPair(this.currentRec, this.blockQueueIterator.next());
				}
			}
			
			return null;
		}
		
		/**
		 * Checks whether the record queue or the data source has a next element.
		 * 
		 * @return True, if a next record exists, otherwise false.
		 */
		protected boolean nextRecordExists() {
			if (this.recordQueueIterator == null) {
				return this.dataIterator.hasNext();
			}
			return this.recordQueueIterator.hasNext() || this.dataIterator.hasNext();
		}
		
		/**
		 * Checks whether a next block exists.
		 * 
		 * @return
		 * 		True, if a next block exists, otherwise false.
		 */
		protected boolean nextBlockExists() {
			
			// a new block exists, if at least 2 records have not yet been assigned to a block
			
			// case 1: the record queue contains at least 2 records => next block exists
			if (this.recordQueue.size() > 1) {
				return true;
			}
			
			// case 2: record queue has 1 element and data iterator has atleast 1 element => next block exists
			if (this.recordQueue.size() == 1 && this.dataIterator.hasNext()) {
				return true;
			}
			
			// case 3: no records in record queue, but at least 1 record in data iterator
			if (this.dataIterator.hasNext()) {
				this.recordQueue.add(this.dataIterator.next());
				this.recordQueueIterator = this.recordQueue.iterator();
				return this.dataIterator.hasNext();
			}
			
			// otherwise no new block exists
			return false;
		}
		
		
		/**
		 * Calculates the elements of the next block.
		 */
		protected void getNextBlock() {
			this.blockQueue = new LinkedList<DuDeObject>();
			int numRecordsNextBlock = getNumRecordsOfBlock();
			for (int i = 0; i<numRecordsNextBlock; i++) {
				this.blockQueue.add(this.recordQueue.poll());
			}
			this.recordQueueIterator = this.recordQueue.iterator();
			this.currentRec = this.blockQueue.poll();
			this.blockQueueIterator = this.blockQueue.iterator();
			
			AdaptiveSNM_Yan2007.this.numberOfCreatedBlocks++;
			AdaptiveSNM_Yan2007.this.sumAssignedRecords =
				AdaptiveSNM_Yan2007.this.sumAssignedRecords + numRecordsNextBlock;
			
			if (logger.isDebugEnabled()) {
				if (AdaptiveSNM_Yan2007.this.getNumberCreatedBlocks()%10000==0 ||
						AdaptiveSNM_Yan2007.this.getNumberCreatedBlocks() >= 510000) {
					logger.debug("Created blocks:   " + AdaptiveSNM_Yan2007.this.getNumberCreatedBlocks());
					logger.debug("Assigned records: " + AdaptiveSNM_Yan2007.this.getNumberAssignedRecords());
				}
			}
		}
		
		/**
		 * Returns the next object from the record queue. If this queue does not contain new
		 * elements, then will the next element from the data source be extracted.
		 * 
		 * @return The next DuDeObject in the sorting order.
		 */
		protected DuDeObject getNextRecord() {
			if (this.recordQueueIterator != null && this.recordQueueIterator.hasNext()) {
				return this.recordQueueIterator.next();
			} else if (this.dataIterator.hasNext()) {
				this.recordQueueIterator = null;
				DuDeObject next = this.dataIterator.next();
				this.recordQueue.add(next);
				return next;
			} else {
				return null;
			}
		}
		
		/**
		 * Calculates the sorting key distance of two <code>DuDeObject<code>s.
		 * 
		 * @param first
		 * 			The first <code>DuDeObject<code>.
		 * @param second
		 * 			The second <code>DuDeObject<code>.
		 * @return the sorting key distance
		 */
		protected float getKeyDistance(DuDeObject first, DuDeObject second) {
			
			AdaptiveSNM_Yan2007.this.sortingKeyComparisons++;
			
			//String key1 = this.sortingKey_iterator.getKeyString(first).toLowerCase();
			//key1 = key1.replace("[null]", "[]");
			//key1 = key1.replace("][", "");
			//if (key1.startsWith("[", 0)) key1 = key1.substring(1);  
			//if (key1.endsWith("]"))      key1 = key1.substring(0, key1.length()-1);
				
			//String key2 = this.sortingKey_iterator.getKeyString(second).toLowerCase();
			//key2 = key2.replace("[null]", "[]");
			//key2 = key2.replace("][", "");
			//if (key2.startsWith("[", 0)) key2 = key2.substring(1);
			//if (key2.endsWith("]"))      key2 = key2.substring(0, key2.length()-1);
			
			String key1 = this.sortingKey_iterator.getKeyString(first);
			String key2 = this.sortingKey_iterator.getKeyString(second);
			
			if (logger.isTraceEnabled()) {
				logger.trace("Key1: " + key1);
				logger.trace("Key2: " + key2);
				logger.trace("Distance: " + String.valueOf(1- this.comparator.getSimilarity(key1, key2)));
			}
			
			return 1- this.comparator.getSimilarity(key1, key2);
		}
		
		/**
		 * Calculates the number of records within the next block.
		 * 
		 * @return number of records in the next block
		 */
		protected abstract int getNumRecordsOfBlock();
		
	}
	
	/**
	 * <code>Iterator</code> implementation that implements the behavior of the
	 * <code>Incrementally-Adaptive Sorted-Neighborhood Method</code>.
	 * 
	 * @author Uwe Draisbach
	 */
	protected class IA_SNM_Iterator extends YanIterator {

		/**
		 * {@inheritDoc YanIterator#YanIterator(SortingKey, float, Iterator)}
		 */
		public IA_SNM_Iterator(SortingKey sortingKey, float phi, Iterator<DuDeObject> dataIterator) {
			super(sortingKey, phi, dataIterator);
		}
		
        @Override
		protected int getNumRecordsOfBlock() {
			// the current elements in block
			ArrayList<DuDeObject> blockRecords = new ArrayList<DuDeObject>();
			
			// the current block size
			int currentBlockSize = 2;
			
			while (blockRecords.size() < currentBlockSize && this.nextRecordExists()) {
				blockRecords.add(this.getNextRecord());
			}
			
			// in the enlargement phase, the first record is always the used for the comparison
			DuDeObject firstRec = blockRecords.get(0);
			
			// distance between the first and the compare-record
			float distance = getKeyDistance(firstRec, blockRecords.get(blockRecords.size()-1));
			
			
			// Enlargement phase
			boolean endOfRecords = false;
			while ( !endOfRecords && distance < this.phi) {
				
				
				// set the next block size
				int nextBlockSize = this.calculateNextBlockSize(distance, currentBlockSize);
				
				// if the next block size equals the current block size (because the current distance is close to the threshold),
				// then increase the block size by 1
				if (nextBlockSize == currentBlockSize) {
					nextBlockSize++;
				}
				
				blockRecords.ensureCapacity(nextBlockSize);
				while (blockRecords.size() < nextBlockSize && this.nextRecordExists()) {
					blockRecords.add(this.getNextRecord());
					currentBlockSize++;
				}
				
				// calculate the distance between the first and the last record in the block
				distance = getKeyDistance(firstRec, blockRecords.get(blockRecords.size()-1));
				
				if (blockRecords.size() < nextBlockSize) {
					// no more records available
					endOfRecords = true;
				}
			}
			
			logger.trace("Enlargment phase finished. Current block size: " + blockRecords.size());
			
			
			// Retrenchment phase
			int result = blockRecords.size();
			if (distance > this.phi && blockRecords.size() > 2) {
				
				int minIndex = 0;
				int maxIndex = blockRecords.size()-1;
				
				int compareIndex = Math.round((this.phi * maxIndex) / distance);
				
				boolean boundaryPairFound = false;
				while ( !boundaryPairFound ) {
					
					if (getKeyDistance(blockRecords.get(minIndex), blockRecords.get(compareIndex)) > this.phi) {
						// boundary pair is "left" from the compare-record
						if (compareIndex - minIndex == 1) {
							// boundary pair is found
							boundaryPairFound = true;
							result = minIndex + 1;
						} else {
							// boundary pair is not found
							maxIndex = compareIndex;
						}
						
					} else {
						// boundary pair is "right" from the compare-record
						if (maxIndex - compareIndex == 1) {
							// boundary pair is found
							boundaryPairFound = true;
							result = compareIndex + 1;
						} else {
							minIndex = compareIndex;
						}
					}
					compareIndex = ((maxIndex - minIndex) / 2) + minIndex;
				}
				
			}
			
			logger.trace("Retrenchment phase finished. Block size: " + result);
			return result;
		}
		
		private int calculateNextBlockSize(float distance, int blockSize) {
			// the average distance between the records in the block
			float average = distance / blockSize;
			
			if (average == 0) {
				// the records in the block have the distance 0, so just double the blocksize
				return 2 * blockSize;
			}
			
			// the records in the block have a distance greater 0, so calculate the "perfect" block size
			// (if the distance between the records is equidistant)
			return Math.round(this.phi / average);
		}
	}
	
	/**
	 * <code>Iterator</code> implementation that implements the behavior of the
	 * <code>Accumulatively-Adaptive Sorted-Neighborhood Method</code>.
	 * 
	 * @author Uwe Draisbach
	 */
	protected class AA_SNM_Iterator extends YanIterator {
		
		/**
		 * {@inheritDoc YanIterator#YanIterator(SortingKey sortingKey, float phi)}
		 */
		public AA_SNM_Iterator (SortingKey sortingKey, float phi, Iterator<DuDeObject> dataIterator) {
			super(sortingKey, phi, dataIterator);
		}
		
		@Override
		protected int getNumRecordsOfBlock() {
			// the current elements in block
			ArrayList<DuDeObject> blockRecords = new ArrayList<DuDeObject>();
			
			// the current block size
			int currentBlockSizeAccumulated = 2;
			
			while (blockRecords.size() < currentBlockSizeAccumulated && this.nextRecordExists()) {
				blockRecords.add(this.getNextRecord());
			}
			
			// in the enlargement phase, the first record is always the used for the comparison
			int firstRecIndex = 0;
			DuDeObject firstRec = blockRecords.get(firstRecIndex);
			
			// distance between the first and the compare-record
			float distance = getKeyDistance(firstRec, blockRecords.get(blockRecords.size()-1));

			
			// Enlargement phase
			boolean endOfRecords = false;
			while ( !endOfRecords && distance <= this.phi) {
				
				// get current block size
				currentBlockSizeAccumulated = blockRecords.size();
				
				// set the next block size
				int nextBlockSizeAccumulated = (2 * currentBlockSizeAccumulated) - 1;
				
				// the last record of the current record is the first record of the next record
				firstRecIndex = blockRecords.size()-1;
				firstRec = blockRecords.get(firstRecIndex);
				
				// add new records to the block
				blockRecords.ensureCapacity(nextBlockSizeAccumulated);
				while (blockRecords.size() < nextBlockSizeAccumulated && this.nextRecordExists()) {
					blockRecords.add(this.getNextRecord());
				}
				
				// calculate the distance between the first and the last record in the block
				distance = getKeyDistance(firstRec, blockRecords.get(blockRecords.size()-1));
				
				if (blockRecords.size() < nextBlockSizeAccumulated) {
					// no more records available
					endOfRecords = true;
				}
			}
			
			logger.trace("Enlargment phase finished. Current block size: " + blockRecords.size());
			
			
			// Retrenchment phase
			int result = blockRecords.size();
			if (distance > this.phi && blockRecords.size() > 2) {
				
				int minIndex = firstRecIndex;
				int maxIndex = blockRecords.size()-1;
				
				boolean boundaryPairFound = false;
				while ( !boundaryPairFound ) {
					
					int compareIndex = minIndex + ((maxIndex - minIndex) / 2);
					
					if (getKeyDistance(blockRecords.get(minIndex), blockRecords.get(compareIndex)) > this.phi) {
						// boundary pair is "left" from the compare-record
						if (compareIndex - minIndex == 1) {
							// boundary pair is found
							boundaryPairFound = true;
							result = minIndex + 1;
						} else {
							// boundary pair is not found
							maxIndex = compareIndex;
						}
						
					} else {
						// boundary pair is "right" from the compare-record
						if (maxIndex - compareIndex == 1) {
							// boundary pair is found
							boundaryPairFound = true;
							result = compareIndex + 1;
						} else {
							minIndex = compareIndex;
						}
					}
					
				}
				
			}
			
			logger.trace("Retrenchment phase finished. Block size: " + result);
			return result;
		}
		
	}

}
