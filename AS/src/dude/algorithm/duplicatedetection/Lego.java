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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;

import dude.algorithm.AbstractDuplicateDetection;
import dude.algorithm.Algorithm;
import dude.datasource.DataSource;
import dude.datasource.DuDeObjectSource;
import dude.exceptions.DCSNMMissingNotificationException;
import dude.util.data.DuDeObject;
import dude.util.data.DuDeObjectId;
import dude.util.data.DuDeObjectPair;
import dude.util.data.DuDeObjectPair.DuplicateType;
import dude.util.data.storage.JsonableReader;
import dude.util.merger.Merger;
import dude.util.sorting.sortingkey.SortingKey;

/**
 * <code>Lego</code> is an iterative blocking approach. In extension to the standard blocking algorithm
 * duplicates are distributed to other blocks to find more duplicates. Those blocks are processed
 * repeatedly until no new duplicates are found.
 * The implementation is based on the original description of the algorithm in the paper
 * "Entity Resolution with Iterative Blocking" by Steven Euijong Whang, David Menestrina, Georgia Koutrika,
 * Martin Theobald and Hector Garcia-Molina (see http://ilpubs.stanford.edu:8090/915/).
 * 
 * @author Florian Thomas
 */
public class Lego extends AbstractDuplicateDetection {
	
	private static final String BLOCK_DATA_SOURCE = "lego";
	
	public class LegoIterator implements Iterator<DuDeObjectPair> {
		
		// The core entity resolution (CER) algorithm that is used to process a single block.
		// Any duplicate detection algorithm can be used.
		private Algorithm coreERAlgorithm;
		
		// The merge that merges duplicate records together into one record
		private Merger merger;
		
		// The mapping between each base record and the blocks, that it was initially assigned to
		private Map<DuDeObjectId, Set<Set<DuDeObject>>> blockMapping;
		
		// A queue maintaining the list of blocks that have to be processed (again)
		private Queue<Set<DuDeObject>> blockQueue;
		
		// A mapping that maps each record to its maximal record
		private Map<DuDeObjectId, DuDeObject> maxRecords;
		
		private Iterator<DuDeObjectPair> currentBlockIterator;
		private Set<DuDeObject> currentBlock;
		private DuDeObjectPair currentPair;
		
		private DuDeObjectPair nextPair;
		
		// The local merge table and processed pairs set add support for processing merged objects
		// to the CER algorithm.
		
		// Contains every single merge that was done in the current block
		// Will be emptied before processing the next block
		private Map<DuDeObject, DuDeObject> localMergeTable;
		
		// Contains all pairs of the current block that were already compared 
		private Set<DuDeObjectPair> processedPairs;
		
		/**
		 * {@inheritDoc Iterator#hasNext()}
		 */
		@Override
		public final boolean hasNext() {
			if (this.nextPair == null) {
				this.nextPair = this.loadNextElement();
				return this.nextPair != null;
			}
			return true;
		}
		
		/**
		 * {@inheritDoc Iterator#next()}
		 */
		@Override
		public final DuDeObjectPair next() {
			
			// java specification - NoSuchElementException should be thrown if the end of the underlying data was reached
			if (!hasNext())
				throw new NoSuchElementException("End of data reached...");
			
			this.currentPair = this.nextPair;
			this.nextPair = null;
			
			return this.currentPair;
		}
		
		/**
		 * This method is not implemented and will throw an {@link UnsupportedOperationException}.
		 * 
		 * @see Iterator#remove()
		 */
		@Override
		public final void remove() {
			throw new UnsupportedOperationException("LegoIterator does not implement remove().");
		}

		/**
		 * Initializes a <code>LegoIterator</code>.
		 * 
		 * @param algorithm
		 *            The algorithm that is used for block processing.
		 * @param sortingKeys
		 *            The blocking criteria
		 * @param merger
		 *            The merger that puts duplicate objects together into one DuDeObject
		 * @param reader
		 *            The JsonableReader that contains the source for duplicate detection
		 */
		public LegoIterator(Algorithm algorithm, Set<SortingKey> sortingKeys, Merger merger,
				JsonableReader<DuDeObject> reader) {
			
			// Initialize local variables
			this.coreERAlgorithm = algorithm;
			
			this.merger = merger;
			
			this.blockMapping = new HashMap<DuDeObjectId,Set<Set<DuDeObject>>>();
			this.maxRecords = new HashMap<DuDeObjectId, DuDeObject>();
			
			this.blockQueue = new LinkedList<Set<DuDeObject>>();
			
			boolean firstIteration = true;
			
			// For every blocking criterion (sorting key) assign the objects
			// in the data source to the appropriate block
			for (SortingKey sortingKey: sortingKeys) {
				
				// The map contains all blocks (Set of DuDeObjects) of the current blocking criterion,
				// which are identified by their sorting key value
				Map<String, Set<DuDeObject>> blocks = new HashMap<String, Set<DuDeObject>>();
				
				for (DuDeObject object: reader) {
					
					// Get corresponding block for the object
					String sortingKeyValue = sortingKey.getKeyString(object);
					Set<DuDeObject> block = blocks.get(sortingKeyValue);
					
					// Create block if it does not yet exist
					if (block == null) {
						block = new HashSet<DuDeObject>();
						blocks.put(sortingKeyValue, block);
						this.blockQueue.add(block);
					}
					
					// Add the object to the block
					block.add(object);
					
					Set<Set<DuDeObject>> objectBlocks;
					if (firstIteration) {
						
						// At first iteration initialize maximal records and the objectId=>blocks mapping
						this.maxRecords.put(object.getIdentifier(), object);
						objectBlocks = new HashSet<Set<DuDeObject>>();
						this.blockMapping.put(object.getIdentifier(), objectBlocks);
						
					} else {
						
						objectBlocks = this.blockMapping.get(object.getIdentifier());
					}
					
					// Add the block to the objects block list 
					objectBlocks.add(block);
				}
				
				firstIteration = false;
			}
		}
		
		private void updateBlockQueue(DuDeObjectId objectId) {
			
			// Search for blocks containing the object and add them to the queue
			// According to the paper only those blocks are added, which initially contained
			// the object.
			for (Set<DuDeObject> block: this.blockMapping.get(objectId)) {
				if (!this.blockQueue.contains(block)) {
					this.blockQueue.add(block);
				}
			}
		}
		
		private DuDeObject mergeMaximalRecord(DuDeObject obj) {
			
			Iterator<DuDeObjectId> itr = obj.getIdentifiers().iterator();
			
			// For each base record in the merged object retrieve the maxmimal record
			// and merge them together
			DuDeObject mergedObject = maxRecords.get(itr.next());
			while (itr.hasNext()) {
				DuDeObjectId next = itr.next();
				mergedObject = this.merger.merge(mergedObject, maxRecords.get(next));
			}
			
			return mergedObject;
		}

		private Set<DuDeObject> mergeOverlappingRecords(Set<DuDeObject> block) {
			
			// Create a map that will contain the merged object for each base record in the block
			Map<DuDeObjectId, DuDeObject> objectAssignment = new HashMap<DuDeObjectId, DuDeObject>();
			for (DuDeObject mergedObject: block) {
				
				// Merge the current object with all the previous ones that contain a subset
				// of its base records
				DuDeObject newObject = mergedObject;
				for (DuDeObjectId id: mergedObject.getIdentifiers()) {
					DuDeObject next = objectAssignment.get(id);
					if (next != null)
						newObject = this.merger.merge(newObject, next);
				}
				
				// Update all corresponding base records of the new merged object
				for (DuDeObjectId id: newObject.getIdentifiers())
					objectAssignment.put(id, newObject);
			}
			
			// Assemble new block by putting all disjunct values of the map together into a new block
			Set<DuDeObject> newBlock = new HashSet<DuDeObject>();
			for (DuDeObject record: objectAssignment.values())
				newBlock.add(record);
			
			return newBlock;
			
		}
		
		private void initializeNextBlock() {
			
			// If a block was processed previously remove it from the block queue
			if (this.currentBlockIterator != null) {
				this.blockQueue.poll();
				this.currentBlockIterator = null;
			}
			
			// Get next block
			// Actually, do not remove the block from the queue. This ensures that it is not
			// added again to the queue while being processed.
			this.currentBlock = this.blockQueue.peek();
			if (this.currentBlock == null)
				return;
			
			Set<DuDeObject> maxBlock = new HashSet<DuDeObject>();
			
			// Merge maximal records
			// This is the second step of distributing duplicates to other blocks
			for (DuDeObject object: this.currentBlock)
				maxBlock.add(mergeMaximalRecord(object));
			
			// Merge overlapping records to avoid unneccessary comparisons
			// The result is a block with DuDeObjects that contain disjunct sets of base records
			this.currentBlock.clear();
			this.currentBlock.addAll(mergeOverlappingRecords(maxBlock));
			
			// Prepare the CER algorithm for the block
			this.coreERAlgorithm.unregisterDataSources();
			this.coreERAlgorithm.addDataSource(new DuDeObjectSource(BLOCK_DATA_SOURCE, this.currentBlock));
			this.currentBlockIterator = coreERAlgorithm.iterator();
			
			// Initialize local merge table and processed pairs set
			this.localMergeTable = new HashMap<DuDeObject, DuDeObject>();
			this.processedPairs = new HashSet<DuDeObjectPair>();
		}
		
		private DuDeObject updateWithLocalMergeTable(DuDeObject object) {
			
			DuDeObject result = object;
			DuDeObject mergedObject = null;
			
			// If the object was already merged with other ones create the maximal object
			// that contains it by iterating through the merges.
			do {
				mergedObject = this.localMergeTable.get(result);
				if (mergedObject != null) {
					result = mergedObject;
				}
			} while (mergedObject != null);
			
			return result;
		}
		
		private DuDeObjectPair getNextPair() {
			
			DuDeObjectPair result = null;
			
			// Search for the next pair that has to be processed
			do {
				
				result = this.currentBlockIterator.next();
				
				// Update pair with local merge table
				result.setFirstElement(updateWithLocalMergeTable(result.getFirstElement()));
				result.setSecondElement(updateWithLocalMergeTable(result.getSecondElement()));
				
			// Ensure that the pair was not compared before and that its elements are not equal
			} while (this.currentBlockIterator.hasNext() &&
					(result.getFirstElement().equals(result.getSecondElement()) || // Skip pairs containing equal objects (which can be the result of the local merge table update)
							this.processedPairs.contains(result))); // Skip pairs that were already compared in the block
			
			// No further pair was found in the current block
			if (result.getFirstElement().equals(result.getSecondElement()) ||
					this.processedPairs.contains(result)) {
				return null;
			}
			
			this.processedPairs.add(result);
			return result;
			
		}
		
		protected DuDeObjectPair loadNextElement() {
			
			// react to the received notification
			handleNotification();
			
			DuDeObjectPair pair = null;
			
			while (pair == null) {
				
				// Check if the current block has further pairs and initialize next block if neccessary
				while (!this.blockQueue.isEmpty() &&
						(this.currentBlockIterator == null || !this.currentBlockIterator.hasNext())) {
					
					initializeNextBlock();
					
				}
				
				// If the current block is empty and no further block has to be processed
				// the algorithm terminates
				if ((this.currentBlockIterator == null || !this.currentBlockIterator.hasNext()) &&
						this.blockQueue.isEmpty())
					return null;
				
				// Get a pair from the block
				// A null value means that no pair of the block needs to be processed
				pair = getNextPair();
			}
			
			return pair;
		}
		
		private void handleDuplicate() {
			
			this.currentPair.setDuplicateInfo(DuplicateType.Duplicate);
			
			// Merge both objects
			DuDeObject firstElement = this.currentPair.getFirstElement();
			DuDeObject secondElement = this.currentPair.getSecondElement();
			DuDeObject newObject = this.merger.merge(firstElement, secondElement);
			
			// Update local merge table
			this.localMergeTable.put(firstElement, newObject);
			this.localMergeTable.put(secondElement, newObject);
			
			// Update current block
			this.currentBlock.remove(firstElement);
			this.currentBlock.remove(secondElement);
			this.currentBlock.add(newObject);
			
			// Update maximal records and block queue
			for (DuDeObjectId id: newObject.getIdentifiers()) {
				this.maxRecords.put(id, this.merger.merge(newObject, this.maxRecords.get(id)));
				updateBlockQueue(id);
			}
		}
		
		private void handleNotification() {
			if (this.currentPair != null) { // if there hasn't been a comparison, a reaction is not yet necessary.
				Lego.ComparisonResult notification = Lego.this.getNotification();

				if (notification == null) { // throw exception in order to alert the user to the impossibility of adapting the window without a
					// notification
					throw new DCSNMMissingNotificationException(
							"Missing notification - the blocks cannot be adjusted. Please call notifyOfLatestComparisonResult(ComparisonResult comparisonResult)!");
				} else if (notification.equals(ComparisonResult.DUPLICATE)) { // previous pair has been categorized as a
					// duplicates
					
					// take special actions according to duplicate propagation
					handleDuplicate();
					
				} else { // non-duplicate
					// nothing to do
				}

				Lego.this.resetNotification();
			}
		}

	}
	
	public enum ComparisonResult {
		/**
		 * If the compared pair is a duplicate.
		 */
		DUPLICATE,
		/**
		 * If the compared pair is no duplicate.
		 */
		NON_DUPLICATE
	}
	
	// Core entity resolution algorithm used for processing blocks
	private Algorithm coreERAlgorithm;
	
	// Blocking criteria
	private Set<SortingKey> sortingKeys;
	
	private Merger merger;
	private transient ComparisonResult notification = null;
	
	/**
	 * For serialization.
	 */
	protected Lego() {
		super();
		// nothing to do
	}
	
	/**
	 * Initializes <code>Lego</code> with the passed {@link SortingKey}'s.
	 * 
	 * @param sortingKeys
	 *            The <code>SortingKeys</code> that are used for defining the blocks. All {@link DuDeObject}s having the same generated
	 *            <code>SortingKey</code> will be include in one block.
	 */
	public Lego(SortingKey... sortingKeys) {
		super();
		this.sortingKeys = new HashSet<SortingKey>(Arrays.asList(sortingKeys));
	}
	
	@Override
	protected Iterator<DuDeObjectPair> createIteratorInstance() {
		return new LegoIterator(this.getCoreERAlgorithm(), this.getSortingKeys(), this.merger,
				this.getData());
	}
	
	/**
	 * Adds a {@link DataSource} to the algorithm. Note: The data source must not contain merged objects.
	 * This will lead to malfunction of the Lego implementation.
	 * 
	 * @param source
	 *            The <code>DataSource</code> that shall be added.
	 * 
	 * @throws NullPointerException
	 *             If <code>null</code> was passed.
	 */
	@Override
	public void addDataSource(DataSource source) {
		super.addDataSource(source);
	}
	
	/**
	 * Sets the entity resolution algorithm that is used to process blocks internally
	 * 
	 * @param coreERAlgorithm
	 *            The algorithm for processing blocks. Using Lego as CER algorithm will not work!.
	 */
	public void setCoreERAlgorithm(Algorithm coreERAlgorithm) {
		this.coreERAlgorithm = coreERAlgorithm;
	}
	
	/**
	 * Returns the entity resolution algorithm that is used to process blocks internally
	 * 
	 * @return
	 *            The CER algorithm for processing blocks
	 */
	public Algorithm getCoreERAlgorithm() {
		return this.coreERAlgorithm;
	}
	
	/**
	 * Sets the merger that merges several DuDeObjects into one
	 * 
	 * @param merger
	 *            The merger that merges two objects into one. The Lego implementation relies
	 *            on the assumption that the merged object contains all identifiers of the merged
	 *            objects. Currently, the {@link DefaultMerger} is approved to work in the expected
	 *            way. Other implementations may operate differently.
	 */
	public void setMerger(Merger merger) {
		this.merger = merger;
	}
	
	/**
	 * Returns the merger that merges several DuDeObjects into one
	 * 
	 * @return
	 *            The merger
	 */
	public Merger getMerger() {
		return this.merger;
	}
	
	/**
	 * Adds a sorting key, which defines a blocking criterion
	 * 
	 * @param sortingKey
	 *            The sortingKey that shall be added
	 */
	public void addSortingKey(SortingKey sortingKey) {
		this.sortingKeys.add(sortingKey);
	}
	
	/**
	 * Defines the blocking criteria that shall be used for blocking
	 * 
	 * @param sortingKeys
	 *            A set of sorting keys that represent the blocking criteria
	 */
	public void setSortingKeys(Set<SortingKey> sortingKeys) {
		this.sortingKeys = sortingKeys;
	}
	
	/**
	 * Return the blocking criteria
	 * 
	 * @return
	 *            A set of sorting keys that represent the blocking criteria
	 */
	public Set<SortingKey> getSortingKeys() {
		return this.sortingKeys;
	}
	
	/**
	 * Notifies the algorithm, whether the latest object pair has been categorized as a duplicate or a non-duplicate
	 * 
	 * @param comparisonResult
	 *            The category.
	 */
	public void notifyOfLatestComparisonResult(ComparisonResult comparisonResult) {
		this.setNotification(comparisonResult);
	}
	
	/**
	 * Resets the last notification
	 */
	protected void resetNotification() {
		this.setNotification(null);
	}
	
	/**
	 * Returns the category that was set for the last processed pair.
	 * 
	 * @return The category of the last processed pair.
	 */
	protected ComparisonResult getNotification() {
		return this.notification;
	}
	
	/**
	 * Sets the category of the last processed pair.
	 * 
	 * @param notification
	 *            The category of the last processed pair.
	 */
	private void setNotification(ComparisonResult notification) {
		this.notification = notification;
	}
}
