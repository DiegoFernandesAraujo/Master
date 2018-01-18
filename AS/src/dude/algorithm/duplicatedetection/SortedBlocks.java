/*
 * DuDe - The Duplicate Detection Toolkit
 * 
 * Copyright (C) 2010  Hasso-Plattner-Institut fur Softwaresystemtechnik GmbH,
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

import org.apache.log4j.Logger;

import dude.algorithm.SortingDuplicateDetection;
import dude.util.AbstractIterator;
import dude.util.data.DuDeObject;
import dude.util.data.DuDeObjectPair;
import dude.util.sorting.sortingkey.SortingKey;


/**
 * <code>SortedBlocks</code> combines blocking and the SNM method. This algorithm uses a
 * {@link SortedDataIterator}.
 * 
 * @author Uwe Draisbach
 * 
 * @see SortedDataIterator
 */
public class SortedBlocks extends SortingDuplicateDetection {
	
	private static final Logger logger = Logger.getLogger(SortedBlocks.class.getPackage().getName());

	private AlgorithmVariant   algorithmVariant;
	private int fixBlockSize = 0;
	private int maxSizeBlock = 0;
	private int sizeOverlap  = 0;
	private int charBlockKey = 0;


	private class SortedBlocksIterator extends AbstractIterator<DuDeObjectPair> {
    	
    	/**
		 * Iterator for the extracted records from the data sources
		 */
		protected Iterator<DuDeObject> dataIterator;
		
				
		/** 
		 * Algorithm variant
		 */
		protected AlgorithmVariant variant;
		
		/**
		 *  Sorting key for the records
		 */
		protected SortingKey sortingKey;
		
		private int nCharOfKey;
		private Queue<DuDeObject>    windowQueue    = new LinkedList<DuDeObject>();
	    private Iterator<DuDeObject> windowIterator = this.windowQueue.iterator();
	    private DuDeObject       currentObject      = null;
		private DuDeObjectPair   nextDuDeObjectPair = null;
		private int maxBlockSize;
		private int fixBlockSize;
		private int overlapSize;
	    private int windowNr;
	    
	    private String currentBlockKey; 
    	
    	/**
		 * Constructor
		 * 
		 * @param variant
		 * 			The used variant of SortedBlocks.
		 * @param sortingKey
		 * 			The sorting key used to sort the records.
		 * @param dataIterator
		 *          Iterator for the data, that shall be processed.
		 * @param partition
		 * 			The used predicate to decide when a new partition starts.
		 * @param num
		 * 			Depending on the partition predicate either the number of records
		 * 			within a partition or the number of characters of the sorting key
		 */
		public SortedBlocksIterator(AlgorithmVariant variant, SortingKey sortingKey,
				Iterator<DuDeObject> dataIterator, int overlap, int nCharOfKey, int num) {
			
			if (dataIterator == null) {
				throw new NullPointerException("DataIterator is null.");
			}
			
			this.variant = variant;
			this.sortingKey = sortingKey;
			this.dataIterator = dataIterator;
			this.overlapSize = overlap;
			this.nCharOfKey = nCharOfKey;
			
			if (this.variant == AlgorithmVariant.FixPartitionSize) {
				this.fixBlockSize = num;
			} else {
				this.maxBlockSize = num;
			}
			
			this.windowNr = overlap + 1;
		}
    	
    	
    	@Override
    	protected DuDeObjectPair loadNextElement() {
    		
    		
    		// checks whether no pairs can be created anymore
    		if (!this.dataIterator.hasNext() && !this.windowIterator.hasNext()) {
    			return null;
    		}
    		
    		
    		// check whether it is the first time that a  pair is requested
    		if (this.currentObject == null) {
    				this.currentObject = this.dataIterator.next();
    				if (this.variant != AlgorithmVariant.FixPartitionSize) {
    					this.currentBlockKey = this.getCurrentObjectSubKey();
    				}
    		}
    		
    		
    		// check whether windowIterator has a next element
    		// if yes, the next DuDeObjectPair is the next windowIterator element and the currentObject
    		// if no, add currentObject to windowQueue and read the next element from dataIterator
    		if (this.windowIterator.hasNext()) {
    			this.nextDuDeObjectPair = new DuDeObjectPair(this.windowIterator.next(), this.currentObject);
    		}
    		else {
    			this.windowQueue.add(this.currentObject);
    			this.currentObject = this.dataIterator.next();
    			
    			if (this.variant == AlgorithmVariant.MaxSizeSlideWindow && this.windowQueue.size() == this.maxBlockSize) {
    				this.windowQueue.remove();
    			}
    			
    			// Check if the current DuDeObject is the first element of a new block
    			// If yes, delete all elements from the queue, which are not relevant for the overlap
    			// If no, check if the object is within an overlap and if so, remove the first element of the overlap
    			if (newPartition()) {
    				while(this.windowQueue.size() > this.overlapSize) {
    					this.windowQueue.remove();
    			     }
    			     this.windowNr = 1;
    			}
    			else if(this.windowNr <= this.overlapSize) {
    		     		this.windowQueue.remove();
    		     		this.windowNr++;
    		     }
    			
    			// Compare current DuDe-object with all objects within the queue
    			this.windowIterator = this.windowQueue.iterator();
    			if (this.windowIterator.hasNext()) {
    				this.nextDuDeObjectPair = new DuDeObjectPair(this.windowIterator.next(), this.currentObject);
    			} else {
    				this.nextDuDeObjectPair = this.loadNextElement();
    			}
    		}
    		
    		return this.nextDuDeObjectPair;
    		
    	}
    	
    	
    	
    	/**
    	 * Checks whether the current object is the first element of a new partition
    	 */
    	private boolean newPartition() {
    		switch (this.variant) {
    	      case Basic:
    	      case MaxSizeSlideWindow:
    	    	  return !this.keyEqualsBlockKey(this.getCurrentObjectSubKey());
    	      case MaxSizeNewPartition:
    	          return !this.keyEqualsBlockKey(this.getCurrentObjectSubKey())
    	          			|| (this.windowQueue.size() >= this.maxBlockSize);  
    	      case FixPartitionSize:
    	    	  return (this.windowQueue.size() >= this.fixBlockSize);
    	    	  
    	     default :
    	       return false;
    		}
    	}
    	
    	
    	/**
    	 * Checks whether the key of the current block is equal to the blocking key of the parameter.
    	 * If not, the parameter value becomes the new blocking key.
    	 * 
    	 * @param key
    	 * 			The blocking key to be checked.
    	 * 
    	 * @return True, if the current block key and the parameter are equal, otherwise false.
    	 * 
    	 */
    	private boolean keyEqualsBlockKey(String key) {
 
    		if (key.equals(this.currentBlockKey)) {
    			return true;
    		}
    		this.currentBlockKey = key;
    		return false;
    	}
    	
    	
    	/**
    	 * Returns the string representation of the sorting key for the current object 
    	 */
    	private String getCurrentObjectSubKey() {
    		final String sortingKeyValue = this.sortingKey.getKeyString(this.currentObject);
    		if (sortingKeyValue.length() < this.nCharOfKey) {
    			return sortingKeyValue.
    				substring(sortingKeyValue.length());
    		}
    		
    		return sortingKeyValue.substring(0, this.nCharOfKey);
    	}
    }
    
	
    /**
	 * Initializes a <code>SortedBlocks</code> instance using fixed size blocks
	 * with the passed windows size.
	 * 
	 * @param variant
	 * 			The algorithm variant.
	 * @param sortingKey
	 *          The key specifies the sorting order.
	 * @param overlapSz
	 * 			Number of tuples from each block which are part of the overlap. 			  
	 */
	public SortedBlocks(AlgorithmVariant variant, SortingKey sortingKey, int overlapSz) {
		super(sortingKey);
		this.algorithmVariant = variant;
		this.sizeOverlap      = overlapSz;
	}
	
	
	/**
	 * Initializes a <code>SortedBlocks</code> instance using variable block sizes.
	 * 
	 * @param keyGen
	 *            The key specifies the sorting order.
	 * @param nrChar
	 *            Number of characters of the sorting key, which define the blocks.
	 * @param firstChar
	 * 			  First character of sorting key which is used for defining blocks.
	 *            Use 0 for first character of the sorting key.
	 * @param fixedBlockSize
	 * 			  
	 */
/*	public SortedBlocks(SortingKey keyGen, int nrChar, int firstChar, int overlapSz, int maxBlockSize) {
		if (maxBlockSize < 2) {
			throw new IllegalArgumentException("The maximum block size must be at least 2.");
		} else {
			this.sortKey      = keyGen;
			this.maxBlockSize    = maxBlockSize;
			this.nrChar          = nrChar;
			this.firstChar       = firstChar;
			this.overlapSize     = overlapSz;
			this.windowNr        = overlapSz + 1;
			this.fixedSizeBlocks = false;
		}
	}*/

	
	@Override
	protected Iterator<DuDeObjectPair> createIteratorInstance() {
		logger.debug("Iterator initialization starts...");
		
		if (this.algorithmVariant == AlgorithmVariant.Basic) {
			return new SortedBlocksIterator(this.algorithmVariant, this.getSortingKey(),
					this.getData().iterator(), this.sizeOverlap, this.charBlockKey, 0);
		}
		
		if (this.algorithmVariant == AlgorithmVariant.FixPartitionSize) {
			return new SortedBlocksIterator(this.algorithmVariant, this.getSortingKey(), this.getData().iterator(),
					this.sizeOverlap, 0, this.fixBlockSize);
		}
		
		return new SortedBlocksIterator(this.algorithmVariant, this.getSortingKey(), this.getData().iterator(),
				this.sizeOverlap, this.charBlockKey, this.maxSizeBlock);
		
	}

	/**
	 * Returns the maximum block size.
	 * 
	 * @return
	 * 			The maximum block size.
	 */
	public int getMaxBlockSize() {
		return this.maxSizeBlock;
	}

    /**
     * Set the new maxmimum block size.
     * 
     * @param maxBlockSize
     * 			The new maximum block size.
     */
	public void setMaxBlockSize(int maxBlockSize) {
		if (maxBlockSize < 2) {
			throw new IllegalArgumentException("The max. block size must be at least 2.");
		}
		this.maxSizeBlock = maxBlockSize;
	}

    /**
     * Returns the current overlap size.
     * 
     * @return
     * 			The current overlap size.
     */
	public int getOverlapSize() {
		return this.sizeOverlap;
	}

	/**
	 * Sets the new overlap size.
	 * 
	 * @param overlapSize
	 * 			The new overlap size.
	 */
	public void setSizeOverlap(int overlapSize) {
		this.sizeOverlap = overlapSize;
	}
	
	/**
	 * Returns the fix block size.
	 * 
	 * @return
	 * 			The fix block size.
	 */
	public int getFixBlockSize() {
		return this.fixBlockSize;
	}

    /**
     * Set the new fix block size.
     * 
     * @param fixBlockSize
     * 			The new fix block size.
     */
	public void setFixBlockSize(int fixBlockSize) {
		this.fixBlockSize = fixBlockSize;
	}
	
    
    /**
     * Returns the number of characters of the sorting key that are used for defining the blocks.
     * 
     * @return
     * 			The number of characters of the sorting key that are used for defining the blocks
     */
    public int getCharBlockKey() {
		return this.charBlockKey;
	}

    /**
     * Set the number of characters of the sorting key that are used for defining the blocks.
     * 
     * @param nrCharBlockKey
     * 			The number of characters of the sorting key that are used for defining the blocks.
     */
	public void setCharBlockKey(int nrCharBlockKey) {
		if (nrCharBlockKey < 1) {
			throw new IllegalArgumentException("The the number of characters of the sorting key that " +
					"are used for defining the blocks must be at least 2.");
		}
		this.charBlockKey = nrCharBlockKey;
	}
	
	
	/**
	 * This enumeration collects the possible SortedBlocks variants.
	 * 
	 * @author Uwe Draisbach
	 */
	public enum AlgorithmVariant {
		/**
		 * Basic
		 */
		Basic,
		
		/**
		 * Fix partition size
		 */
		FixPartitionSize,
		
		/**
		 * Create new partition when max. window size is reached.
		 */
		MaxSizeNewPartition,
		
		/**
		 * Slide window when max. window size is reached.
		 */
		MaxSizeSlideWindow
	}
	
}