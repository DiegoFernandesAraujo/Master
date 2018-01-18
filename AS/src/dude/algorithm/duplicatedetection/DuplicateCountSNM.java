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

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import org.apache.log4j.Logger;

import dude.algorithm.SortingDuplicateDetection;
import dude.exceptions.DCSNMMissingNotificationException;
import dude.util.data.DuDeObject;
import dude.util.data.DuDeObjectPair;
import dude.util.data.DuDeObjectPair.GeneratedBy;
import dude.util.sorting.sortingkey.SortingKey;

/**
 * <code>AdaptiveWindowSizeSNM</code> implements the Adaptive-Window-Size Sorted-Neighborhood Method that was introduced by Oliver Wonneberg.
 * 
 * @author Fabian Lindenberg
 */
public class DuplicateCountSNM extends SortingDuplicateDetection {

	/**
	 * <code>AdaptiveWindowSizeSNMIterator</code> implements the behavior of the Adaptive-Window-Size SNM algorithm.
	 * 
	 * @author Fabian Lindenberg
	 */
	protected class AdaptiveWindowSizeSNMIterator implements Iterator<DuDeObjectPair> {

		// the AdaptiveWindowSizeSNM instance which is used for collecting the actual configuration of the algorithm
		private final DuplicateCountSNM algorithm;
		private final Iterator<DuDeObject> dataIterator;

		// the queue that collects the DuDeObjects within the window
		private final Queue<DuDeObject> windowQueue;
		// the iterator that is used for iterating over the window's DuDeObjects
		private Iterator<DuDeObject> windowIterator;
		// the adapted window size
		private int adaptedWindowSize;

		// the DuDeObject that will be the left element of the returned pair
		private DuDeObject currentLeftElement = null;
		// the DuDeObject that will be the right element of the returned pair
		private DuDeObject currentRightElement = null;

		private int currentPos = 1;
		private int numOfDuplicates = 0;
		private int numOfSucceedingNonDuplicates = 0;
		private int numOfComparisons = 0;
		private int posOfLastDuplicate = this.currentPos;
		private int maxDistanceBetweenDuplicates = 0;

		// storage for detected duplicates
		private Queue<DuDeObject> duplicatesQueue = new LinkedList<DuDeObject>();

		/**
		 * Initializes a <code>AdaptiveWindowSizeSNMIterator</code>.
		 * 
		 * @param algo
		 *            The algorithm of which the settings and data shall be used.
		 */
		protected AdaptiveWindowSizeSNMIterator(DuplicateCountSNM algo) {
			if (algo == null) {
				throw new NullPointerException("The algorithm instance is missing.");
			}

			this.algorithm = algo;
			this.algorithm.lockInstance();

			this.dataIterator = this.algorithm.getData().iterator();

			// initialization of the window
			this.resetAdaptedWindowSize();
			this.windowQueue = new LinkedList<DuDeObject>();
			this.windowIterator = null;
		}

		/**
		 * Resets the adapted size to the original window size.
		 */
		private void resetAdaptedWindowSize() {
			DuplicateCountSNM.logger.debug("Window size reset");

			this.currentPos = 1;
			this.numOfComparisons = 0;
			this.numOfDuplicates = 0;
			this.posOfLastDuplicate = this.currentPos;
			this.maxDistanceBetweenDuplicates = 0;
			this.numOfSucceedingNonDuplicates = 0;
			this.adaptedWindowSize = this.algorithm.getWindowSize();
		}

		/**
		 * Checks whether the previous {@link DuDeObjectPair} has been categorized as a duplicate or a non-duplicate and reacts accordingly. Its
		 * reaction (i.e. the adaption of the window size) further depends on the selected {@link AdaptionMode}.
		 * 
		 * @throws ASNMMissingNotificationException
		 *             in case no notification has been received
		 */
		private void handleNotification() {
			if (this.numOfComparisons > 0) { // if there hasn't been a comparison, a reaction is not yet necessary.
				DuplicateCountSNM.ComparisonResult notification = DuplicateCountSNM.this.getNotification();

				if (notification == null) { // throw exception in order to alert the user to the impossibility of adapting the window without a
					// notification
					throw new DCSNMMissingNotificationException(
							"Missing notification - the window size can not be adapted. Please call notifyOfLatestComparisonResult(ComparisonResult comparisonResult)!");
				} else if (notification.equals(DuplicateCountSNM.ComparisonResult.DUPLICATE)) { // previous pair has been categorized as a
					// duplicate
					this.numOfDuplicates++;
					this.numOfSucceedingNonDuplicates = 0;

					// take special actions according to the adaption mode
					handleDuplicate();

				} else { // non-duplicate
					this.numOfSucceedingNonDuplicates++;
					adaptWindowSizeIfNecessary(); // increase window size only if the average has not yet dropped below the threshold
				}

				DuplicateCountSNM.this.resetNotification();
			}
		}

		/**
		 * Handles the detection of a duplicate according to the selected adaption mode
		 */
		private void handleDuplicate() {
			if (!this.algorithm.getAdaptionMode().equals(AdaptionMode.BASIC)) {
				int increase = this.algorithm.getWindowSize() - 1; // increase suggested by AdaptionMode.LARGE_INCREASE
				if (this.algorithm.getAdaptionMode().equals(AdaptionMode.DISTANCE_BASED_INCREASE)) { // choose the maximum distance as the increase
																										// (if greater)
					this.maxDistanceBetweenDuplicates = Math.max(this.maxDistanceBetweenDuplicates, (this.currentPos - this.posOfLastDuplicate));
					this.posOfLastDuplicate = this.currentPos;
					increase = Math.max(increase, this.maxDistanceBetweenDuplicates);
				} else if (this.algorithm.getAdaptionMode().equals(AdaptionMode.PERCENTAGE_INCREASE)) { // choose the defined percentage of the
																										// current size as
					// the increase (if greater)
					increase = Math.max(increase, (int) (this.numOfComparisons * this.algorithm.getIncreaseFactor()));
				}
				// store the duplicate object in order to skip it when it is the first object of the window queue (transitivity assumed)
				this.duplicatesQueue.add(this.currentRightElement);
				updateAdaptedWindowSize(this.currentPos + increase);
			} else { // AdaptionMode.AVERAGE
				// increase window size if the average of duplicates is greater than or equals the threshold
				adaptWindowSizeIfNecessary();
			}
		}

		/**
		 * Chooses the maximum of the current adapted window size and the passed <code>alternativeSize</code> as the new adapted window size
		 * 
		 * @param alternativeSize
		 */
		private void updateAdaptedWindowSize(int alternativeSize) {
			int oldSize = this.adaptedWindowSize;
			this.adaptedWindowSize = Math.max(this.adaptedWindowSize, alternativeSize);
			logger.debug("Window size updated from " + oldSize + " to " + this.adaptedWindowSize);
		}

		/**
		 * If the current position has reached the end of the window, it is checked whether the average number of duplicates in this window exceeds
		 * the <code>increaseThreshold_iterator</code>. If that's the case, the window size is adapted accordingly.<br />
		 * However, this increase is aborted, if the <code>abortIncrease</code> flag is set and the average number of succeeding non-duplicates
		 * exceeds the <code>abort threshold</code>.
		 */
		private void adaptWindowSizeIfNecessary() {
			if (this.currentPos >= this.adaptedWindowSize) {
				if (this.algorithm.isAbortIncrease() == false
						|| (((float) this.numOfSucceedingNonDuplicates / (float) this.numOfComparisons) < this.algorithm.getAbortThreshold())) {
					if (((float) this.numOfDuplicates / (float) this.numOfComparisons) >= this.algorithm.getIncreaseThreshold()) {
						increaseAdaptedWindowSize();
					}
				} else {
					logger.debug("Increase of window size aborted.");
				}
			}
		}

		/**
		 * Selects the next object from the window queue. Is the {@link AdaptionMode} <code>LARGE_INCREASE</code> selected, duplicates that have
		 * already been detected are discarded.
		 * 
		 * @return the next suitable object from the window queue
		 */
		private DuDeObject selectNextObject() {
			if (!this.algorithm.getAdaptionMode().equals(AdaptionMode.BASIC)) {
				while (this.duplicatesQueue.contains(this.windowQueue.peek())) {
					this.duplicatesQueue.remove(this.windowQueue.poll());
				}
			}
			return this.windowQueue.poll();
		}

		/**
		 * Increases the currently used window size by one.
		 */
		private void increaseAdaptedWindowSize() {
			increaseAdaptedWindowSize(1);
		}

		/**
		 * Increases the currently used window size by the value of <code>addend</code>
		 * 
		 * @param addend
		 */
		private void increaseAdaptedWindowSize(int addend) {
			logger.debug("Window size increased by " + addend);
			this.adaptedWindowSize += addend;
		}

		/**
		 * Checks whether the window can be still extended.
		 * 
		 * @return <code>true</code>, if the window size has reached the threshold; otherwise <code>false</code>.
		 */
		private boolean windowIsFull() {
			return this.windowQueue.size() >= this.adaptedWindowSize - 1;
		}

		/**
		 * Checks whether the current object was already compared with each other object of the window queue.
		 * 
		 * @return <code>true</code>, if the end of the window queue was reached; otherwise <code>false</code>.
		 */
		protected boolean endOfWindowIsReached() {
			return this.windowIterator == null || !this.windowIterator.hasNext() || (this.numOfComparisons >= this.adaptedWindowSize - 1);
		}

		@Override
		public boolean hasNext() {
			boolean result = false;
			if (this.currentLeftElement != null) {
				result = !endOfWindowIsReached()
						|| this.dataIterator.hasNext()
						|| (this.algorithm.getAdaptionMode().equals(AdaptionMode.BASIC) && (this.windowQueue.size() > 1))
						|| (!this.algorithm.getAdaptionMode().equals(AdaptionMode.BASIC) && ((this.windowQueue.size() - this.duplicatesQueue.size()) > 1)); // objects
				// in duplicateQueue will be skipped anyway
			} else {
				result = this.algorithm.getDataSize() > 1;
			}

			return result;
		}

		@Override
		public DuDeObjectPair next() throws DCSNMMissingNotificationException {
			DuDeObjectPair currentElement = this.loadNextElement();

			// java specification - NoSuchElementException should be thrown if the end of the underlying data was reached
			if (currentElement == null) {
				throw new NoSuchElementException("End of data reached...");
			}

			currentElement.setLineage(GeneratedBy.Algorithm);

			return currentElement;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("AdaptiveWindowSizeSNMIterator does not implement remove().");
		}

		/**
		 * Returns the element in the iteration.
		 * 
		 * @return The next element.
		 * @throws DCSNMMissingNotificationException
		 *             If the lastly returned pair's type was not specified using
		 *             {@link DuplicateCountSNM#notifyOfLatestComparisonResult(ComparisonResult)}.
		 */
		protected DuDeObjectPair loadNextElement() throws DCSNMMissingNotificationException {
			// checks whether no pairs can be created anymore
			if (this.dataIterator == null) {
				this.algorithm.unlockInstance();
				return null;
			}
			// react to the received notification
			handleNotification();

			// loads the very first element as currentLeftElement
			if (this.currentLeftElement == null && this.dataIterator.hasNext()) {
				this.currentLeftElement = this.dataIterator.next();
			}

			this.currentRightElement = null;
			if (!endOfWindowIsReached()) { // process window queue
				this.currentRightElement = this.windowIterator.next();
			} else { // end of window queue has been reached
				if (!windowIsFull() && this.dataIterator.hasNext()) { // add a new element to the window
					this.currentRightElement = this.dataIterator.next();
					this.windowQueue.add(this.currentRightElement);
					this.windowIterator = null; // prevent concurrent modification
				} else { // reset window size, select the next object from the window queue and use it as currentLeftElement
					resetAdaptedWindowSize();
					this.currentLeftElement = selectNextObject();

					// if w=2 is selected, then it is possible that the window queue is empty after selecting the next current object
					// thus, if the data iterator has more elements, an additional element has to be added to the window queue
					if (this.algorithm.getWindowSize() == 2 && this.windowQueue.isEmpty() && this.dataIterator.hasNext()) {
						this.windowQueue.add(this.dataIterator.next());
					}

					if (!this.windowQueue.isEmpty()) { // process window queue
						this.windowIterator = this.windowQueue.iterator();
						this.currentRightElement = this.windowIterator.next();
					} else {
						this.algorithm.unlockInstance();
						return null;
					}
				}
			}

			this.currentPos++;
			this.numOfComparisons++;
			return new DuDeObjectPair(this.currentLeftElement, this.currentRightElement);

		}

	}

	/**
	 * This enumeration collects all the modes which can be used.
	 * 
	 * @author Fabian Lindenberg
	 */
	public enum AdaptionMode {
		/**
		 * As long as the number of duplicates divided by the number of comparison is higher than a given threshold, the window size will be increased
		 * step-wise.
		 */
		BASIC,
		/**
		 * If a duplicate is detected, the window will be increased by its original size minus one. Later on, the detected duplicate object will be
		 * skipped, as transitivity is assumed. Furthermore, the window size increases step-wise as long as the number of duplicates divided by the
		 * number of comparison is higher than a given threshold.
		 */
		MULTI_REC_INCREASE,
		/**
		 * If a duplicate is detected, the window will be increased by the maximum distance of two duplicates in the current window or by its original
		 * size minus one, whichever is higher. Later on, the detected duplicate object will be skipped, as transitivity is assumed. Furthermore, the
		 * window size increases step-wise as long as the number of duplicates divided by the number of comparison is higher than a given threshold.
		 */
		DISTANCE_BASED_INCREASE,
		/**
		 * If a duplicate is detected, the window will be increased by a defined percentage of its current size or by its original size minus one,
		 * whichever is higher. Later on, the detected duplicate object will be skipped, as transitivity is assumed. Furthermore, the window size
		 * increases step-wise as long as the number of duplicates divided by the number of comparison is higher than a given threshold.
		 */
		PERCENTAGE_INCREASE
	}

	/**
	 * The comparison of a {@link DuDeObjectPair} can either yield a DUPLICATE or a NON_DUPLICATE
	 * 
	 * @author Fabian Lindenberg
	 */
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

	/**
	 * The <code>AdaptiveWindowSizeSNM.AdaptiveWindowSizeSNMBuilder</code> maintains the adaptable window size of the
	 * <code>AdaptiveWindowSizeSNM</code>.
	 * 
	 * @author Fabian Lindenberg
	 */
	public static class AdaptiveWindowSizeSNMBuilder {
		// required parameters
		private final SortingKey sortingKey;
		private final AdaptionMode adaptionMode;

		// optional parameters - initialized with default values
		private int windowSize = 30;
		private float increaseThreshold = 0.5f;
		private float increaseFactor = 0.75f;
		private boolean abortIncrease = false;
		private float abortThreshold = 0.75f;

		/**
		 * Initializes the <code>AdaptiveWindowSizeSNM.AdaptiveWindowSizeSNMBuilder</code> with a {@link SortingKey} and the mode that shall be used.
		 * 
		 * @param sortingKey
		 *            The sorting key of this instance.
		 * @param adaptionMode
		 *            The mode.
		 */
		public AdaptiveWindowSizeSNMBuilder(SortingKey sortingKey, AdaptionMode adaptionMode) {
			this.sortingKey = sortingKey;
			this.adaptionMode = adaptionMode;
		}

		/**
		 * Sets the size of the window.
		 * 
		 * @param sz
		 *            The window size. This value needs to be larger than one.
		 * 
		 * @return Returns the current instance.
		 */
		public AdaptiveWindowSizeSNMBuilder windowSize(int sz) {
			if (sz < 2) {
				throw new IllegalArgumentException("The size of the window must be at least 2.");
			}
			this.windowSize = sz;
			return this;
		}

		/**
		 * Sets the threshold for increasing the window size.
		 * 
		 * @param threshold
		 *            The threshold. This value needs to lie in the range (0,1].
		 * 
		 * @return Returns the current instance.
		 */
		public AdaptiveWindowSizeSNMBuilder increaseThreshold(float threshold) {
			if (threshold <= 0 || threshold > 1) {
				throw new IllegalArgumentException("The threshold must lie in the range (0,1].");
			}

			// if (!this.adaptionMode.equals(AdaptionMode.AVERAGE) && (threshold > (1f / (this.windowSize - 1)))) {
			// throw new IllegalArgumentException(
			// "For all adaption modes other than AdaptionMode.AVERAGE the threshold must not be greater than 1/(windowSize-1).");
			// }

			this.increaseThreshold = threshold;
			return this;
		}

		/**
		 * Sets the factor by which the window size will be multiplied.
		 * 
		 * @param factor
		 *            The factor by which the window size will be multiplied.
		 * 
		 * @return Returns the current instance.
		 */
		public AdaptiveWindowSizeSNMBuilder increaseFactor(float factor) {
			if (factor <= 0) {
				throw new IllegalArgumentException("The factor must be greater than zero");
			}
			this.increaseFactor = factor;
			return this;
		}

		/**
		 * Determines whether the continuous increase of the window size may be aborted.
		 * 
		 * @param abortIncrease
		 *            <code>true</code>, if the increase shall be aborted; otherwise <code>false</code>.
		 * 
		 * @return Returns the current instance.
		 */
		public AdaptiveWindowSizeSNMBuilder abortIncrease(boolean abortIncrease) {
			this.abortIncrease = abortIncrease;
			return this;
		}

		/**
		 * Sets the threshold for aborting the continuous increase of the window size.
		 * 
		 * @param abortThreshold
		 *            The threshold. This value needs to lie in the range (0,1].
		 * 
		 * @return Returns the current instance.
		 */
		public AdaptiveWindowSizeSNMBuilder abortThreshold(float abortThreshold) {
			if (abortThreshold <= 0 || abortThreshold > 1) {
				throw new IllegalArgumentException("The threshold must lie in the range (0,1].");
			}
			this.abortThreshold = abortThreshold;
			return this;
		}

		/**
		 * Initializes a <code>AdaptiveWindowSizeSNM</code> instance
		 * 
		 * @return a <code>AdaptiveWindowSizeSNM</code> instance
		 */
		public DuplicateCountSNM build() {
			useDefaultIncreaseThresholdIfNecessary();
			return new DuplicateCountSNM(this);
		}

		/**
		 * If the current threshold is invalid, it is set to its default value, which depends on the window size
		 */
		private void useDefaultIncreaseThresholdIfNecessary() {
			if (this.increaseThreshold <= 0 || this.increaseThreshold > 1
					|| (!this.adaptionMode.equals(AdaptionMode.BASIC) && (this.increaseThreshold > (1f / (this.windowSize - 1))))) {
				this.increaseThreshold = (1f / (this.windowSize - 1));
			}
		}
	}

	private static final Logger logger = Logger.getLogger(DuplicateCountSNM.class.getPackage().getName());

	private AdaptionMode adaptionMode;

	private int windowSize;
	private float increaseThreshold;
	private float increaseFactor;
	private boolean abortIncrease;
	private float abortThreshold;

	private boolean instanceLocked = false;

	private transient ComparisonResult notification = null;

	/**
	 * Initializes a <code>AdaptiveWindowSizeSNM</code> instance with the help of the passed builder instance.
	 */
	private DuplicateCountSNM(AdaptiveWindowSizeSNMBuilder builder) {
		super(builder.sortingKey);
		this.adaptionMode = builder.adaptionMode;

		this.windowSize = builder.windowSize;
		this.increaseThreshold = builder.increaseThreshold;
		this.increaseFactor = builder.increaseFactor;
		this.abortIncrease = builder.abortIncrease;
		this.abortThreshold = builder.abortThreshold;
	}

	private void lockInstance() {
		this.instanceLocked = true;
	}

	/**
	 * Unlocks the instance. This instance is being locked, when an iteration process was initiated. After the iteration process is finished (i.e. the
	 * last element was returned), the instance will be automatically unlocked. Calling any setter methods while the instance is locked leads to a
	 * {@link ConcurrentModificationException}. If the iteration process is not finished, yet, but any parameter shall be changed, call this method
	 * beforehand.
	 */
	public void unlockInstance() {
		this.instanceLocked = false;
	}

	private boolean instanceLocked() {
		return this.instanceLocked;
	}

	/**
	 * For serialization
	 */
	protected DuplicateCountSNM() {
		super();
		// nothing to do
	}

	@Override
	protected Iterator<DuDeObjectPair> createIteratorInstance() {
		return new AdaptiveWindowSizeSNMIterator(this);
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

	/**
	 * Returns the set adaptation mode.
	 * 
	 * @return The adaptation mode.
	 */
	protected AdaptionMode getAdaptionMode() {
		return this.adaptionMode;
	}

	/**
	 * Returns the current window size.
	 * 
	 * @return The current window size.
	 */
	protected int getWindowSize() {
		return this.windowSize;
	}

	/**
	 * Returns the threshold for increasing the window size.
	 * 
	 * @return The threshold.
	 */
	protected float getIncreaseThreshold() {
		return this.increaseThreshold;
	}

	/**
	 * Returns the set increase factor.
	 * 
	 * @return The increase factor.
	 */
	protected float getIncreaseFactor() {
		return this.increaseFactor;
	}

	/**
	 * Checks whether aborting the increase is enabled.
	 * 
	 * @return <code>true</code>, if aborting is enabled; otherwise <code>false</code>.
	 */
	protected boolean isAbortIncrease() {
		return this.abortIncrease;
	}

	/**
	 * Returns the abort threshold.
	 * 
	 * @return The threshold for aborting the increase.
	 */
	protected float getAbortThreshold() {
		return this.abortThreshold;
	}

	/**
	 * Sets the window Size.
	 * 
	 * @param windowSize
	 *            The new window size.
	 * @throws ConcurrentModificationException
	 *             If this method is called while an iteration process was not finished, yet.
	 */
	public void setWindowSize(int windowSize) {
		if (this.instanceLocked()) {
			throw new ConcurrentModificationException("The instance is currently locked, since an iteration process is still in progress.");
		}

		this.windowSize = windowSize;
	}

	/**
	 * Sets the increase threshold.
	 * 
	 * @param increaseThreshold
	 *            The new increase threshold.
	 * @throws ConcurrentModificationException
	 *             If this method is called while an iteration process was not finished, yet.
	 */
	public void setIncreaseThreshold(float increaseThreshold) {
		if (this.instanceLocked()) {
			throw new ConcurrentModificationException("The instance is currently locked, since an iteration process is still in progress.");
		}

		this.increaseThreshold = increaseThreshold;
	}

	/**
	 * Sets the increase factor.
	 * 
	 * @param increaseFactor
	 *            The new increase factor.
	 * 
	 * @throws ConcurrentModificationException
	 *             If this method is called while an iteration process was not finished, yet.
	 */
	public void setIncreaseFactor(float increaseFactor) {
		if (this.instanceLocked()) {
			throw new ConcurrentModificationException("The instance is currently locked, since an iteration process is still in progress.");
		}

		this.increaseFactor = increaseFactor;
	}
}
