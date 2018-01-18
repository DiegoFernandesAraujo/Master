/* DuDe - The Duplicate Detection Toolkit
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

package dude.postprocessor;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;

import org.apache.log4j.Logger;

import dude.algorithm.Algorithm;
import dude.util.GoldStandard;
import dude.util.MemoryCheckerTask;
import dude.util.data.AutoJsonable;
import dude.util.data.DuDeObjectPair;
import dude.util.data.Jsonable;

/**
 * <code>StatisticComponent</code> provides functionality for gathering statistics concerning the recall, precision and f-measure. Therefore a
 * collection real duplicates has to be added.
 * 
 * @author Matthias Pohl
 */
public class StatisticComponent implements AutoJsonable {

	private static final Logger logger = Logger.getLogger(StatisticComponent.class.getPackage().getName());

	protected static final int NO_STATISTIC_VALUE = -1;

	//changed from private to protected to enable ExtendedStatisticComponent access
	protected GoldStandard goldStandard;

	protected long truePositives = 0; // number of detected True-Positives
	protected long falsePositives = 0; // number of detected False-Positives
	protected long truePositivesByComparison = 0; // number of detected True-Positive pairs that were explicitly compared and not implicitly calculated
	protected long falsePositivesByComparison = 0; // number of detected False-Positive pairs that were explicitly compared and not implicitly
													// calculated
	protected long trueNegativesByComparison = 0; // number of True-Negative pairs that were explicitly compared and not implicitly calculated
	protected long falseNegativesByComparison = 0; // number of False-Negative pairs that were explicitly compared and not implicitly calculated
	protected long pairCount = 0; // number of pairs that were processed by the statistic component
	protected long actualComparisonCount = 0; // number of pairs, for which a comparison function was executed

	protected long startTime; // start time of the experiment
	protected Date startDate; // start date of the experiment
	protected long endTime; // end time of the experiment
	protected Date endDate; // end date of the experiment

	//changed from private to protected to enable ExtendedStatisticComponent access
	protected Algorithm algorithm; // the algorithm that is used in the experiment

	// variables used for checking the the used memory size
	protected boolean checkMemory = true;
	protected transient MemoryCheckerTask task;
	protected transient Timer timer;
	protected long memoryCheckFrequency = 5000;
        
       //Diego nas paradas
        protected boolean status = false;

	/**
	 * Initializes a <code>StatisticComponent</code> with no gold standard.
	 * 
	 * @param algorithm
	 *            The used algorithm.
	 */
	public StatisticComponent(Algorithm algorithm) {
		this(null, algorithm);
	}

	/**
	 * Initializes a <code>StatisticComponent</code> using the passed {@link DuDeObjectPair}s as real duplicates.
	 * 
	 * @param goldStandard
	 *            The gold standard which these statistics are based on.
	 * @param algorithm
	 *            Used algorithm.
	 */
	public StatisticComponent(GoldStandard goldStandard, Algorithm algorithm) {
		this.goldStandard = goldStandard;
		this.algorithm = algorithm;
	}

	/**
	 * Internal constructor for {@link Jsonable} deserialization.
	 */
	protected StatisticComponent() {
		// nothing to do
	}

	/**
	 * Checks whether this StatisticComponent calculates the Generalized Merge Distance. Important for the output components.
	 * 
	 * @return false since this is the original component without GMD computation
	 */
	public boolean hasGMD(){
		return false;
	}
	/**
	 * Checks whether a gold standard was passed. If no gold standard was set, some of the statistics cannot be calculated.
	 * 
	 * @return <code>true</code>, if a gold standard was set; otherwise <code>false</code>.
	 */
	public boolean goldStandardSet() {
		return this.goldStandard != null;
	}

	/**
	 * Returns the <code>true positives</code> count.
	 * 
	 * @return The <code>true positives</code> count.
	 */
	public long getTruePositives() {
		if (!this.goldStandardSet()) {
			return StatisticComponent.NO_STATISTIC_VALUE;
		}

		return this.truePositives;
	}

	/**
	 * Returns the <code>false positives</code> count.
	 * 
	 * @return The <code>false positives</code> count.
	 */
	public long getFalsePositives() {
		if (!this.goldStandardSet()) {
			return StatisticComponent.NO_STATISTIC_VALUE;
		}

		return this.falsePositives;
	}

	/**
	 * Returns the <code>true negatives</code> count.
	 * 
	 * @return The <code>true negatives</code> count.
	 */
	public long getTrueNegatives() {
		if (!this.goldStandardSet()) {
			return StatisticComponent.NO_STATISTIC_VALUE;
		}

		return this.getNumberOfCandidateComparisons() - this.getTruePositives() - this.getFalsePositives() - this.getFalseNegatives();
	}

	/**
	 * Returns the <code>false negatives</code> count.
	 * 
	 * @return The <code>false negatives</code> count.
	 */
	public long getFalseNegatives() {
		if (!this.goldStandardSet()) {
			return StatisticComponent.NO_STATISTIC_VALUE;
		}

		return this.getNumberOfRealDuplicates() - this.getTruePositives();
	}

	/**
	 * Returns the <code>true positives</code> count that are explicitly classified by the comparator..
	 * 
	 * @return The <code>true positives</code> count that are explicitly classified by the comparator..
	 */
	public long getTruePositivesByComparison() {
		if (!this.goldStandardSet()) {
			return StatisticComponent.NO_STATISTIC_VALUE;
		}

		return this.truePositivesByComparison;
	}

	/**
	 * Returns the <code>false positives</code> count that are explicitly classified by the comparator..
	 * 
	 * @return The <code>false positives</code> count that are explicitly classified by the comparator..
	 */
	public long getFalsePositivesByComparison() {
		if (!this.goldStandardSet()) {
			return StatisticComponent.NO_STATISTIC_VALUE;
		}

		return this.falsePositivesByComparison;
	}

	/**
	 * Returns the <code>true negatives</code> count that are explicitly classified by the comparator.
	 * 
	 * @return The <code>true negatives</code> count that are explicitly classified by the comparator.
	 */
	public long getTrueNegativesByComparison() {
		if (!this.goldStandardSet()) {
			return StatisticComponent.NO_STATISTIC_VALUE;
		}

		return this.trueNegativesByComparison;
	}

	/**
	 * Returns the <code>false negatives</code> count that are explicitly classified by the comparator.
	 * 
	 * @return The <code>false negatives</code> count that are explicitly classified by the comparator.
	 */
	public long getFalseNegativesByComparison() {
		if (!this.goldStandardSet()) {
			return StatisticComponent.NO_STATISTIC_VALUE;
		}

		return this.falseNegativesByComparison;
	}

	/**
	 * Returns the number of pairs that were already considered. These are all pairs, including those that should not be counted as comparison (e.g.
	 * those created by building the transitive closure).
	 * 
	 * @return The number of pairs that were already considered.
	 */
	public long getPairCount() {
		return this.pairCount;
	}

	/**
	 * Returns the number of pairs that were already compared. These are only those pairs that should be counted as comparison (e.g. NOT those created
	 * by building the transitive closure).
	 * 
	 * @return The number of pairs that were already compared.
	 */
	public long getComparisonCount() {
		return this.actualComparisonCount;
	}

	/**
	 * Adds a {@link DuDeObjectPair} to the knowledge base. The pair is counted as comparison.
	 * 
	 * @param pair
	 *            The pair that shall be considered in the statistics.
	 * @param positive
	 *            <code>true</code>, if the passed pair was detected as an duplicate; otherwise <code>false</code>.
	 */
	public void addPair(DuDeObjectPair pair, boolean positive) {
		if (positive) {
			this.addDuplicate(pair);
		} else {
			this.addNonDuplicate(pair);
		}
	}

	/**
	 * Adds several {@link DuDeObjectPair}s to the knowledge base. The pairs are counted as comparisons.
	 * 
	 * @param pairs
	 *            The pairs that shall be considered in the statistics.
	 * @param positive
	 *            <code>true</code>, if the passed pair was detected as an duplicate; otherwise <code>false</code>.
	 */
	public void addPair(Iterable<DuDeObjectPair> pairs, boolean positive) {
		if (positive) {
			this.addDuplicate(pairs);
		} else {
			this.addNonDuplicate(pairs);
		}
	}

	/**
	 * Adds a {@link DuDeObjectPair} to the knowledge base that is labeled as a detected duplicate. The pair is counted as comparison.
	 * 
	 * @param pair
	 *            A detected duplicate.
	 */
	public void addDuplicate(DuDeObjectPair pair) {
		this.addDuplicate(pair, true);
	}
        
        /**
	 * Adds a {@link DuDeObjectPair} to the knowledge base that is labeled as a detected duplicate.
	 * 
	 * @param pair
	 *            A detected duplicate.
	 * @param actualComparison
	 *            <code>true</code>, if the pair should be counted as comparison; otherwise <code>false</code>.
	 */
	public void addDuplicate(DuDeObjectPair pair, boolean actualComparison) {
		if (this.goldStandardSet()) {
			if (this.goldStandard.contains(pair)) {
				this.truePositives++;
                                System.out.println("DUPLICATE TRUE");
				if (actualComparison) {
					this.truePositivesByComparison++;
				}
			} else {
				this.falsePositives++;
                                System.out.println("DUPLICATE FALSE");
				if (actualComparison) {
					this.falsePositivesByComparison++;
				}
			}
		}

		this.pairCount++;
		if (actualComparison) {
			this.actualComparisonCount++;
		}
	}
        
        
        public boolean getStatusDuplicate(DuDeObjectPair pair) {
		boolean esteStatus = false;
                if (this.goldStandardSet()) {
			if (this.goldStandard.contains(pair)) {
				esteStatus = true;
                                
				
			} else {
				esteStatus = false;
				}
			}
                return esteStatus;
	}

		

	/**
	 * Adds several {@link DuDeObjectPair}s to the knowledge base that are labeled as detected duplicates. The pairs are counted as comparisons.
	 * 
	 * @param pairs
	 *            The pairs that shall be considered as detected duplicates in the statistics.
	 */
	public void addDuplicate(Iterable<DuDeObjectPair> pairs) {
		for (DuDeObjectPair pair : pairs) {
			this.addDuplicate(pair);
		}
	}

	/**
	 * Adds a {@link DuDeObjectPair} to the knowledge base that is labeled as a detected non-duplicate. The pair is counted as comparison.
	 * 
	 * @param pair
	 *            The pair that shall be considered as a detected non-duplicate in the statistics.
	 */
	public void addNonDuplicate(DuDeObjectPair pair) {
		this.addNonDuplicate(pair, true);
	}

	/**
	 * Adds a {@link DuDeObjectPair} to the knowledge base that is labeled as a detected non-duplicate.
	 * 
	 * @param pair
	 *            The pair that shall be considered as a detected non-duplicate in the statistics.
	 * @param actualComparison
	 *            <code>true</code>, if the pair is an actual comparison; otherwise <code>false</code>.
	 */
	public void addNonDuplicate(DuDeObjectPair pair, boolean actualComparison) {
		if (actualComparison) {
			this.actualComparisonCount++;
			if (this.goldStandardSet()) {
				if (!this.goldStandard.contains(pair)) {
                                        System.out.println("NONDUPLICATE TRUE");
					this.trueNegativesByComparison++;
				} else {
                                        System.out.println("NONDUPLICATE FALSE");
					this.falseNegativesByComparison++;
				}
			}
		}

		this.pairCount++;
	}

	/**
	 * Adds several {@link DuDeObjectPair}s to the knowledge base that are labeled as a detected non-duplicates. The pairs are counted as comparisons.
	 * 
	 * @param pairs
	 *            The pairs that shall be considered as detected non-duplicates in the statistics.
	 */
	public void addNonDuplicate(Iterable<DuDeObjectPair> pairs) {
		for (DuDeObjectPair pair : pairs) {
			this.addNonDuplicate(pair);
		}
	}

	/**
	 * Sets the current time as starting time for the runtime and initiates memory monitoring.
	 * 
	 * @deprecated Replaced by {@link #setStartTime()}
	 */
	@Deprecated
	public void setBeginningTime() {
		this.setStartTime();
	}

	/**
	 * Sets the current time as starting time for the runtime and initiates memory monitoring.
	 */
	public void setStartTime() {
		this.startTime = System.currentTimeMillis();
		this.startDate = new Date(this.startTime);
		if (this.checkMemory) {
			checkMemoryUsage();
		}

		StatisticComponent.logger.info("StartingTime:" + this.startDate.toString());
	}

	/**
	 * Gets the date of the specified start time of an algorithm.
	 * 
	 * @return <code>Date</code> object
	 */
	public Date getStartDate() {
		return this.startDate;
	}

	/**
	 * Sets current time as finishing time for the runtime. Memory monitoring ends if it is activated.
	 * 
	 * @deprecated Replaced by {@link #setEndTime()}
	 */
	@Deprecated
	public void setFinishingTime() {
		this.setEndTime();
	}

	/**
	 * Sets current time as finishing time for the runtime. Memory monitoring ends if it is activated.
	 */
	public void setEndTime() {
		this.endTime = System.currentTimeMillis();
		this.endDate = new Date(this.endTime);
		if (this.checkMemory) {
			this.timer.cancel();
		}

		StatisticComponent.logger.info("EndTime:" + this.endDate.toString());
	}

	/**
	 * Gets the date of the specified end time of an algorithm.
	 * 
	 * @return <code>Date</code> object
	 */
	public Date getEndDate() {
		return this.endDate;
	}

	/**
	 * Gets the time difference between beginning time and finishing time.
	 * 
	 * @return Runtime in milliseconds
	 */
	public long getRuntime() {
		return this.endTime - this.startTime;
	}

	/**
	 * Returns the precision based on the current knowledge base.
	 * 
	 * @return The precision value.
	 */
	public double getPrecision() {
		if (!this.goldStandardSet()) {
			return StatisticComponent.NO_STATISTIC_VALUE;
		}

		if (this.getTruePositives() == 0 && this.getFalsePositives() == 0) {
			return 1.0;
		}

		if (this.getTruePositives() == 0) {
			return 0.0;
		}

		return (double) this.getTruePositives() / (this.getTruePositives() + this.getFalsePositives());
	}

	/**
	 * Returns the recall based on the current knowledge base. In the context of blocking algorithms the returned value can be considered as pair
	 * completeness ratio.
	 * 
	 * @return The recall value.
	 */
	public double getRecall() {
		if (!this.goldStandardSet()) {
			return StatisticComponent.NO_STATISTIC_VALUE;
		}

		if (this.goldStandard.size() == 0) {
			return 1.0;
		}

		if (this.getTruePositives() == 0) {
			return 0.0;
		}

		return (double) this.getTruePositives() / this.goldStandard.size();
	}

	/**
	 * Returns the f-measure based on the current knowledge base.
	 * 
	 * @return The f-measure.
	 */
	public double getFMeasure() {
		if (!this.goldStandardSet()) {
			return StatisticComponent.NO_STATISTIC_VALUE;
		}

		if (this.getRecall() == 0.0 && this.getPrecision() == 0.0) {
			return 0.0;
		}

		return 2 * this.getRecall() * this.getPrecision() / (this.getRecall() + this.getPrecision());
	}

	/**
	 * Returns the reduction ratio based on the current knowledge base.
	 * 
	 * @return The reduction ratio.
	 */
	public double getReductionRatio() {
		if (!this.goldStandardSet()) {
			return StatisticComponent.NO_STATISTIC_VALUE;
		}

		if (this.getObjectCount() == 0) {
			return 1.0;
		}

		return 1 - ((double) this.getPairCount() / this.getNumberOfCandidateComparisons());
	}

	/**
	 * Returns the precision based on the current knowledge base and the actual comparisons.
	 * 
	 * @return The precision value.
	 */
	public double getPrecisionByComparison() {
		if (!this.goldStandardSet()) {
			return StatisticComponent.NO_STATISTIC_VALUE;
		}

		if (this.getTruePositivesByComparison() == 0 && this.getFalsePositivesByComparison() == 0) {
			return 1.0;
		}

		if (this.getTruePositivesByComparison() == 0) {
			return 0.0;
		}
		System.out.println("PRECISION:" + this.getTruePositivesByComparison() + "/" + (this.getTruePositivesByComparison()  + "+" + this.getFalsePositivesByComparison()));
		return (double) this.getTruePositivesByComparison() / (this.getTruePositivesByComparison() + this.getFalsePositivesByComparison());
	}

	/**
	 * Returns the recall based on the current knowledge base and the actual comparisons. In the context of blocking algorithms the returned value can
	 * be considered as pair completeness ratio.
	 * 
	 * @return The recall value.
	 */
	public double getRecallByComparison() {
		if (!this.goldStandardSet()) {
			return StatisticComponent.NO_STATISTIC_VALUE;
		}

		if (this.goldStandard.size() == 0) {
			return 1.0;
		}

		if (this.getTruePositivesByComparison() == 0) {
			return 0.0;
		}
		System.out.println("RECALL: " + this.getTruePositivesByComparison() + "/" +  this.goldStandard.size());
		return (double) this.getTruePositivesByComparison() / this.goldStandard.size();
	}

	/**
	 * Returns the f-measure based on the current knowledge base and the actual comparisons.
	 * 
	 * @return The f-measure.
	 */
	public double getFMeasureByComparison() {
		if (!this.goldStandardSet()) {
			return StatisticComponent.NO_STATISTIC_VALUE;
		}

		if (this.getRecallByComparison() == 0.0 && this.getPrecisionByComparison() == 0.0) {
			return 0.0;
		}

		return 2 * this.getRecallByComparison() * this.getPrecisionByComparison() / (this.getRecallByComparison() + this.getPrecisionByComparison());
	}

	/**
	 * Returns the reduction ratio based on the current knowledge base and the actual comparisons.
	 * 
	 * @return The reduction ratio.
	 */
	public double getReductionRatioByComparison() {
		if (!this.goldStandardSet()) {
			return StatisticComponent.NO_STATISTIC_VALUE;
		}

		if (this.getObjectCount() == 0) {
			return 1.0;
		}

		return 1 - ((double) this.getComparisonCount() / this.getNumberOfCandidateComparisons());
	}

	/**
	 * Returns the size of the gold standard.
	 * 
	 * @return The gold standard's size.
	 */
	public long getNumberOfRealDuplicates() {
		if (!this.goldStandardSet()) {
			return StatisticComponent.NO_STATISTIC_VALUE;
		}

		return this.goldStandard.size();
	}

	/**
	 * Returns the number of records that were processed by the algorithm.
	 * 
	 * @return The record count of the source data set.
	 */
	public long getObjectCount() {
		return this.algorithm.getDataSize();
	}

	/**
	 * Returns the maximum number of pairs that would be generated by the naive approach.
	 * 
	 * @return The number of pairs that would be generated by the naive approach.
	 */
	public long getNumberOfCandidateComparisons() {
		return this.algorithm.getMaximumPairCount();
	}

	/**
	 * Returns <code>true</code> if the {@link DuDeObjectPair} exists in the set of real duplicate pairs.
	 * 
	 * @param pair
	 *            The duplicate pair that is to be checked.
	 * @return <code>true</code>, if the duplicate pair exists in the set of real duplicate pairs.
	 */
	public boolean isDuplicate(DuDeObjectPair pair) {
//                System.out.println("isDuplicate");
		return this.goldStandard.contains(pair);
	}
        
        //Criando um método para retornar as duplicatas aqui. Com licença!

	/**
	 * Checks whether a specific pair exists in the set of real duplicate pairs.
	 * 
	 * @param pair
	 *            The duplicate pair that is to be checked.
	 * @return <code>false</code>, if the duplicate pair exists in the set of real duplicate pairs.
	 */
	public boolean isNonDuplicate(DuDeObjectPair pair) {
		return !this.goldStandard.contains(pair);
	}

	/**
	 * Gets the frequency of memory checks.
	 * 
	 * @return Frequency of memory checks in ms.
	 */
	public long getMemoryCheckFrequency() {
		return this.memoryCheckFrequency;
	}

	/**
	 * Gets the frequency of memory checks.
	 * 
	 * @param memoryCheckFrequency
	 *            Frequency of memory checks in ms. Default value is 5000 ms.
	 */
	public void setMemoryCheckFrequency(long memoryCheckFrequency) {
		this.memoryCheckFrequency = memoryCheckFrequency;
	}

	/**
	 * Gets the boolean flag that indicates the activation status of memory checking.
	 * 
	 * @return Boolean flag is TRUE if memory checking is performed.
	 */
	public boolean isCheckMemory() {
		return this.checkMemory;
	}

	/**
	 * Sets the boolean flag that indicates the activation status of memory checking.
	 * 
	 * @param checkMemory
	 *            Is set to False if memory checking should not be performed. Default value is True.
	 */
	public void setCheckMemory(boolean checkMemory) {
		this.checkMemory = checkMemory;
	}

	/**
	 * Starts Memoryusage task
	 */
	protected void checkMemoryUsage() {

		this.timer = new Timer();
		// Start in 2 seconds then run every 5 seconds
		this.task = new MemoryCheckerTask();
		this.timer.schedule(this.task, 2000, this.memoryCheckFrequency);

	}

	/**
	 * Gets the registered maximum amount of memory during the experiment.
	 * 
	 * @return Maximum amount of memory in KB.
	 */
	public String getMaximumMemoryUsed() {
		if (this.timer == null) {
			return "n.a.";
		}
		NumberFormat form = new DecimalFormat("#,##0.####", new DecimalFormatSymbols(Locale.ENGLISH));

		return form.format(this.task.getMaxMemoryUsed()) + "";
	}

	/**
	 * Gets the registered minimum amount of memory during the experiment.
	 * 
	 * @return Minimum amount of memory in KB.
	 */
	public String getMinimumMemoryUsed() {
		if (this.timer == null) {
			return "n.a.";
		}
		NumberFormat form = new DecimalFormat("#,##0.####", new DecimalFormatSymbols(Locale.ENGLISH));

		return form.format(this.task.getMinMemoryUsed()) + "";
	}

	/**
	 * Gets the registered average amount of memory used during the experiment.
	 * 
	 * @return Average amount of memory in KB.
	 */
	public String getAverageMemoryUsed() {
		if (this.timer == null) {
			return "n.a.";
		}
		NumberFormat form = new DecimalFormat("#,##0.####", new DecimalFormatSymbols(Locale.ENGLISH));

		return form.format(this.task.getAverageMemoryUsed()) + "";
	}

	/**
	 * Sets the attributes for TruePositives, FalsePositives, TruePositivesByComparison, FalsePositivesByComparison, TrueNegativesByComparison,
	 * FalseNegativesByComparison, PairCount and ComparisonCount to 0. The algorithm and the gold standard are not changed. This allows to reuse the
	 * statistic component for several experiments. Please note that the start and end time for the next experiment has to be set again by using the
	 * corresponding methods.
	 */
	public void reset() {
		this.truePositives = 0;
		this.falsePositives = 0;
		this.truePositivesByComparison = 0;
		this.falsePositivesByComparison = 0;
		this.trueNegativesByComparison = 0;
		this.falseNegativesByComparison = 0;
		this.pairCount = 0;
		this.actualComparisonCount = 0;
	}
}
