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

package dude.output.statisticoutput;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import dude.postprocessor.StatisticComponent;
import dude.util.data.Jsonable;

/**
 * <code>AbstractStatisticOuput</code> is an <code>abstract</code> class that provides functionality common to most classes implementing
 * {@link StatisticOutput}. Every concrete <code>StatisticOutput</code> implementation may inherit from this class instead of implementing the
 * <code>StatisticOutput</code> interface itself.
 * 
 * @author Fabian Lindenberg
 * @author Uwe Draisbach
 */
public abstract class AbstractStatisticOutput implements StatisticOutput {

	/**
	 * Default labels.
	 * <ul>
	 * <li>0 - start time</li>
	 * <li>1 - end time</li>
	 * <li>2 - runtime in ms</li>
	 * <li>3 - max. memory in kb</li>
	 * <li>4 - min. memory in kb</li>
	 * <li>5 - avg. memory in kb</li>
	 * <li>6 - number of data records</li>
	 * <li>7 - number of comparison candidates</li>
	 * <li>8 - number of created pairs
	 * <li>9 - number of actual comparisons</li>
	 * <li>10 - reduction ratio</li>
	 * <li>11 - number of real duplicates</li>
	 * <li>12 - precision</li>
	 * <li>13 - recall</li>
	 * <li>14 - f-measure</li>
	 * <li>15 - true positives</li>
	 * <li>16 - false positives</li>
	 * <li>17 - true negatives</li>
	 * <li>18 - false negatives</li>
	 * <li>19 - precision based on actual comparisons</li>
	 * <li>20 - recall based on actual comparisons</li>
	 * <li>21 - f-measure based on actual comparisons</li>
	 * <li>22 - reduction ratio based on actual comparisons</li>
	 * <li>23 - true positives based on actual comparisons</li>
	 * <li>24 - false positives based on actual comparisons</li>
	 * <li>25 - true negatives based on actual comparisons</li>
	 * <li>26 - false negatives based on actual comparisons</li>
	 * </ul>
	 */
	protected static final String[] defaultLabels = { "Start Time", "End Time", "Runtime in ms", "Max. Memory in KB", "Min. Memory in KB",
			"Avg. Memory in KB", "Number of Data Records", "Number of Comparison Candidates", "Number of created pairs", "Number of actual comparisons",
			"Reduction Ratio", "Number of real Duplicates", "Precision", "Recall", "F-Measure", "True Positives", "False Positives", "True Negatives",
			"False Negatives", "Precision based on actual comparisons", "Recall based on actual comparisons", "F-Measure based on actual comparisons",
			"Reduction Ratio based on actual comparisons", "True Positives based on actual comparisons", "False Positives based on actual comparisons",
			"True Negatives based on actual comparisons", "False Negatives based on actual comparisons" };

	private transient StatisticComponent statistics;

	private transient Map<String, String> optionalExtension = new LinkedHashMap<String, String>();

	/**
	 * Initializes a <code>AbstractStatisticOutput</code> with the passed {@link StatisticComponent}.
	 * 
	 * @param statsComponent
	 *            The <code>StatisticComponent</code> whose data shall be printed.
	 */
	public AbstractStatisticOutput(StatisticComponent statsComponent) {
		this.setStatistics(statsComponent);
	}

	/**
	 * Internal constructor for {@link Jsonable} deserialization.
	 */
	protected AbstractStatisticOutput() {
		// nothing to do
	}

	@Override
	public abstract void writeStatistics() throws IOException;

	@Override
	public abstract void close() throws IOException;

	@Override
	public String[] getLabels() {
		return defaultLabels;
	}

	@Override
	public StatisticComponent getStatistics() {
		return this.statistics;
	}

	@Override
	public void setStatistics(StatisticComponent statistics) {
		this.statistics = statistics;
	}

	@Override
	public void resetOptionalStatisticEntries() {
		for (Map.Entry<String, String> entry : this.optionalExtension.entrySet()) {
			entry.setValue("");
		}
	}

	@Override
	public boolean setOptionalStatisticEntry(String label, String value) {
		return this.optionalExtension.put(label, value) == null;
	}

	@Override
	public boolean setOptionalStatisticEntry(String label) {
		return this.setOptionalStatisticEntry(label, "");
	}

	/**
	 * Returns all extension columns' label and value.
	 * 
	 * @return All entries that were added for extending the default output.
	 */
	protected Map<String, String> getOptionalEntries() {
		return this.optionalExtension;
	}

}
