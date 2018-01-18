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

package dude.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonToken;

import dude.algorithm.Algorithm;
import dude.datasource.DataSource;
import dude.output.DuDeOutput;
import dude.output.statisticoutput.StatisticOutput;
import dude.postprocessor.NaiveTransitiveClosureGenerator;
import dude.postprocessor.StatisticComponent;
import dude.similarityfunction.SimilarityFunction;
import dude.util.data.DuDeObjectPair;
import dude.util.data.DuDeObjectPair.DuplicateType;
import dude.util.data.Jsonable;
import dude.util.data.json.DuDeJsonGenerator;
import dude.util.data.json.DuDeJsonParser;
import dude.util.data.json.JsonUtil;

/**
 * <code>Experiment</code> is a Wrapper for hiding the actual process of checking each pair of records.
 * 
 * @author Matthias Pohl
 */
public class Experiment implements Jsonable {

	private static final Logger logger = Logger.getLogger(Experiment.class.getPackage().getName());

	private final Collection<DataSource> dataSources = new ArrayList<DataSource>();
	private Algorithm algorithm;

	private SimilarityFunction similarityFunction;

	private final Collection<DuDeOutput> outputs = new ArrayList<DuDeOutput>();
	private final Collection<DuDeOutput> fuzzyOutputs = new ArrayList<DuDeOutput>();
	private final Collection<StatisticOutput> statisticsOutputs = new ArrayList<StatisticOutput>();

	private GoldStandard goldStandard;

	private boolean inMemoryProcessingEnabled = false;
	private boolean statisticsEnabled = false;
	private boolean transitiveClosuresEnabled = false;

	private double lowerThreshold, upperThreshold = 1;

	/**
	 * Initializes an <code>Experiment</code>. All required components need to be initialized, yet.
	 */
	public Experiment() {
		// nothing to do
	}

	/**
	 * Adds a {@link DataSource} to this <code>Experiment</code>.
	 * 
	 * @param source
	 *            The <code>DataSource</code> that shall be added.
	 */
	public void addDataSource(DataSource source) {
		this.dataSources.add(source);
	}

	/**
	 * Adds several {@link DataSource}s to this <code>Experiment</code>.
	 * 
	 * @param sources
	 *            The <code>DataSource</code> that shall be added.
	 */
	public void addDataSources(DataSource... sources) {
		this.dataSources.addAll(Arrays.asList(sources));
	}

	/**
	 * Adds a new {@link DuDeOutput} to this <code>Experiment</code>.
	 * 
	 * @param output
	 *            A <code>DuDeOutput</code> onto which the result shall be written.
	 */
	public void addDuDeOutput(DuDeOutput output) {
		this.outputs.add(output);
	}

	/**
	 * Adds several {@link DuDeOutput}s to this <code>Experiment</code>.
	 * 
	 * @param outputs
	 *            The <code>DuDeOutputs</code> that shall be used.
	 */
	public void addDuDeOutputs(DuDeOutput... outputs) {
		this.outputs.addAll(Arrays.asList(outputs));
	}

	/**
	 * Adds a new {@link DuDeOutput} for fuzzy duplicates to this <code>Experiment</code>.
	 * 
	 * @param fuzzyOutput
	 *            A <code>DuDeOutput</code> onto which fuzzy duplicates shall be written.
	 */
	public void addFuzzyDuDeOutput(DuDeOutput fuzzyOutput) {
		this.fuzzyOutputs.add(fuzzyOutput);
	}

	/**
	 * Adds several {@link DuDeOutput}s for fuzzy duplicates to this <code>Experiment</code>.
	 * 
	 * @param fuzzyOutputs
	 *            The <code>DuDeOutputs</code> that shall be used for fuzzy duplicates.
	 */
	public void addFuzzyDuDeOutputs(DuDeOutput... fuzzyOutputs) {
		this.fuzzyOutputs.addAll(Arrays.asList(fuzzyOutputs));
	}

	/**
	 * Adds a {@link StatisticOutput} instance to this <code>Experiment</code>.
	 * 
	 * @param statsOutput
	 *            An {@link StatisticOutput} that will be used internally.
	 */
	public void addStatisticOutput(StatisticOutput statsOutput) {
		this.statisticsOutputs.add(statsOutput);
	}

	/**
	 * Adds several {@link StatisticOutput} instances to this <code>Experiment</code>.
	 * 
	 * @param statsOutputs
	 *            <code>StatisticOutputs</code> that will be used within the <code>Experiment</code>.
	 */
	public void addStatisticOutputs(StatisticOutput... statsOutputs) {
		this.statisticsOutputs.addAll(Arrays.asList(statsOutputs));
	}

	/**
	 * Checks whether a {@link Algorithm} was set.
	 * 
	 * @return <code>true</code>, if the <code>Algorithm</code> was set; otherwise <code>false</code>.
	 */
	protected boolean algorithmSet() {
		return this.algorithm != null;
	}

	/**
	 * Performs a clean-up. Opened connections will be closed.
	 */
	public void cleanUp() {
		this.closeDataSources();

		try {
			this.closeOutputs();
		} catch (final IOException e) {
			Experiment.logger.warn("Error while closing an output.", e);
		}

		try {
			this.closeStatisticOutputs();
		} catch (final IOException e) {
			Experiment.logger.warn("Error while closing an statistic output.", e);
		}
	}

	/**
	 * Closes all added {@link DataSources}s.
	 */
	protected void closeDataSources() {
		for (final DataSource dataSource : this.getDataSources()) {
			dataSource.cleanUp();
		}
	}

	/**
	 * Closes all added fuzzy {@link DuDeOutput}s.
	 * 
	 * @throws IOException
	 *             If an error occurs while closing the output.
	 */
	protected void closeFuzzyOutputs() throws IOException {
		for (final DuDeOutput fuzzyOutput : this.fuzzyOutputs) {
			fuzzyOutput.close();
		}
	}

	/**
	 * Closes all added {@link DuDeOutput}s.
	 * 
	 * @throws IOException
	 *             If an error occurs while closing the output.
	 */
	protected void closeOutputs() throws IOException {
		for (final DuDeOutput output : this.outputs) {
			output.close();
		}
	}

	/**
	 * Closes all added {@link StatisticOutput}s.
	 * 
	 * @throws IOException
	 *             If an error occurs while closing the output.
	 */
	protected void closeStatisticOutputs() throws IOException {
		for (final StatisticOutput statisticOutput : this.statisticsOutputs) {
			statisticOutput.close();
		}
	}

	/**
	 * Checks whether a {@link SimilarityFunction} was set.
	 * 
	 * @return <code>true</code>, if a comparator was set; otherwise <code>false</code>.
	 */
	protected boolean similarityFunctionSet() {
		return this.similarityFunction != null;
	}

	/**
	 * Checks whether any {@link DataSource} is added.
	 * 
	 * @return <code>true</code>, if at least one <code>DataSource</code> was added; otherwise <code>false</code>.
	 */
	protected boolean dataSourcesSet() {
		return !this.dataSources.isEmpty();
	}

	/**
	 * Disables in-memory processing.
	 */
	public void disableInMemoryProcessing() {
		this.inMemoryProcessingEnabled = false;
	}

	/**
	 * Disables gathering statistics.
	 */
	public void disableStatistics() {
		this.statisticsEnabled = false;
	}

	/**
	 * Disables transitive closure processing.
	 */
	public void disableTransitiveClosures() {
		this.transitiveClosuresEnabled = false;
	}

	/**
	 * Enables in-memory processing.
	 */
	public void enableInMemoryProcessing() {
		this.inMemoryProcessingEnabled = true;
	}

	/**
	 * Enables gathering statistics.
	 */
	public void enableStatistics() {
		this.statisticsEnabled = true;
	}

	/**
	 * Enables transitive closure processing.
	 */
	public void enableTransitiveClosures() {
		this.transitiveClosuresEnabled = true;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		final Experiment other = (Experiment) obj;
		if (this.algorithm == null) {
			if (other.algorithm != null)
				return false;
		} else if (!this.algorithm.equals(other.algorithm))
			return false;
		if (this.similarityFunction == null) {
			if (other.similarityFunction != null)
				return false;
		} else if (!this.similarityFunction.equals(other.similarityFunction))
			return false;
		if (this.dataSources == null) {
			if (other.dataSources != null)
				return false;
		} else if (!this.dataSources.equals(other.dataSources))
			return false;
		if (this.fuzzyOutputs == null) {
			if (other.fuzzyOutputs != null)
				return false;
		} else if (!this.fuzzyOutputs.equals(other.fuzzyOutputs))
			return false;
		if (this.goldStandard == null) {
			if (other.goldStandard != null)
				return false;
		} else if (!this.goldStandard.equals(other.goldStandard))
			return false;
		if (this.inMemoryProcessingEnabled != other.inMemoryProcessingEnabled)
			return false;
		if (this.outputs == null) {
			if (other.outputs != null)
				return false;
		} else if (!this.outputs.equals(other.outputs))
			return false;
		if (this.statisticsEnabled != other.statisticsEnabled)
			return false;
		if (this.statisticsOutputs == null) {
			if (other.statisticsOutputs != null)
				return false;
		} else if (!this.statisticsOutputs.equals(other.statisticsOutputs))
			return false;
		if (this.transitiveClosuresEnabled != other.transitiveClosuresEnabled)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.algorithm == null ? 0 : this.algorithm.hashCode());
		result = prime * result + (this.similarityFunction == null ? 0 : this.similarityFunction.hashCode());
		result = prime * result + (this.dataSources == null ? 0 : this.dataSources.hashCode());
		result = prime * result + (this.fuzzyOutputs == null ? 0 : this.fuzzyOutputs.hashCode());
		result = prime * result + (this.goldStandard == null ? 0 : this.goldStandard.hashCode());
		result = prime * result + (this.inMemoryProcessingEnabled ? 1231 : 1237);
		result = prime * result + (this.outputs == null ? 0 : this.outputs.hashCode());
		result = prime * result + (this.statisticsEnabled ? 1231 : 1237);
		result = prime * result + (this.statisticsOutputs == null ? 0 : this.statisticsOutputs.hashCode());
		result = prime * result + (this.transitiveClosuresEnabled ? 1231 : 1237);
		return result;
	}

	/**
	 * Checks whether any output is set.
	 * 
	 * @return <code>true</code>, if any {@link DuDeOutput} for fuzzy duplicates was set; otherwise <code>false</code>.
	 */
	protected boolean fuzzyOutputSet() {
		return !this.fuzzyOutputs.isEmpty();
	}

	/**
	 * Returns the {@link Algorithm}.
	 * 
	 * @return The <code>Algorithm</code>.
	 */
	protected Algorithm getAlgorithm() {
		return this.algorithm;
	}

	/**
	 * Returns the {@link SimilarityFunction}.
	 * 
	 * @return The internally used <code>SimilarityFunction</code> instance.
	 */
	protected SimilarityFunction getSimilarityFunction() {
		return this.similarityFunction;
	}

	/**
	 * Returns all added {@link DataSource}s.
	 * 
	 * @return All added <code>DataSources</code>.
	 */
	protected Iterable<DataSource> getDataSources() {
		return this.dataSources;
	}

	/**
	 * Returns the gold standard.
	 * 
	 * @return The gold standard of this <code>Experiment</code>.
	 */
	protected GoldStandard getGoldStandard() {
		if (!this.goldStandardSet()) {
			return null;
		}

		return this.goldStandard;
	}

	/**
	 * Gets the lower threshold for this experiment. All pairs with <code>lowerThreshold &lt;= sim && sim &lt; upperThreshold</code> are considered
	 * fuzzy duplicates.<br>
	 * The threshold is in [0;1] and <code>lowerThreshold &lt;= upperThreshold</code>.
	 * 
	 * @return the lower threshold
	 */
	public double getLowerThreshold() {
		return this.lowerThreshold;
	}

	/**
	 * Returns the added {@link StatisticOutput}s.
	 * 
	 * @return The <code>StatisticOutputs</code> that shall be used.
	 */
	protected Iterable<StatisticOutput> getStatisticOutputs() {
		return this.statisticsOutputs;
	}

	/**
	 * Gets the threshold for this experiment. All pairs with <code>upperThreshold &lt;= sim</code> are duplicates.<br>
	 * The threshold is in [0;1].<br>
	 * Note: this method only succeeds if <code>{@link #getLowerThreshold()} == {@link #getUpperThreshold()}</code>
	 * 
	 * @return the threshold
	 */
	public double getThreshold() {
		if (this.lowerThreshold != this.upperThreshold) {
			throw new IllegalStateException();
		}

		return this.upperThreshold;
	}

	/**
	 * Gets the thresholds for this experiment. All pairs with <code>upperThreshold &lt;= sim</code> are duplicates.<br>
	 * The threshold is in [0;1] and <code>lowerThreshold &lt;= upperThreshold</code>.
	 * 
	 * @return the upper threshold
	 */
	public double getUpperThreshold() {
		return this.upperThreshold;
	}

	/**
	 * Checks whether a {@link GoldStandard} was set.
	 * 
	 * @return <code>true</code>, if an <code>GoldStandard</code> was set; otherwise <code>false</code>.
	 */
	protected boolean goldStandardSet() {
		return this.goldStandard != null;
	}

	/**
	 * Initializes the algorithm instance.
	 */
	protected void initializeAlgorithm() {
		for (final DataSource dataSource : this.getDataSources()) {
			this.algorithm.addDataSource(dataSource);
		}

		if (this.inMemoryProcessingEnabled()) {
			this.algorithm.enableInMemoryProcessing();
		} else {
			this.algorithm.disableInMemoryProcessing();
		}
	}

	/**
	 * Checks whether in-memory processing is enabled.
	 * 
	 * @return <code>true</code>, if in-memory processing is enabled; otherwise <code>false</code>.
	 */
	protected boolean inMemoryProcessingEnabled() {
		return this.inMemoryProcessingEnabled;
	}

	/**
	 * Checks whether any output is set.
	 * 
	 * @return <code>true</code>, if any {@link DuDeOutput} was set; otherwise <code>false</code>.
	 */
	protected boolean outputSet() {
		return !this.outputs.isEmpty();
	}

	/**
	 * Writes the passed fuzzy {@link DuDeObjectPair} onto all added fuzzy {@link DuDeOutput}s.
	 * 
	 * @param fuzzyPair
	 *            The fuzzy pair that shall be printed.
	 * @throws IOException
	 *             If an error occurs during the writing.
	 */
	protected void printFuzzyPair(DuDeObjectPair fuzzyPair) throws IOException {
		for (final DuDeOutput output : this.fuzzyOutputs) {
			output.write(fuzzyPair);
		}
	}

	/**
	 * Writes the passed {@link DuDeObjectPair} onto all added {@link DuDeOutput}s.
	 * 
	 * @param pair
	 *            The pair that shall be printed.
	 * @throws IOException
	 *             If an error occurs during the writing.
	 */
	protected void printPair(DuDeObjectPair pair) throws IOException {
		for (final DuDeOutput output : this.outputs) {
			output.write(pair);
		}
	}

	/**
	 * Starts a run based on the previously configured thresholds.
	 * 
	 * @throws IOException
	 *             If an error occurs while printing the result.
	 * 
	 * @throws IllegalStateException
	 *             If one essential component was not set.
	 */
	public void run() throws IOException {
		this.run(this.lowerThreshold, this.upperThreshold);
	}

	/**
	 * Starts a run based on the passed thresholds. All pairs with a similarity smaller than <code>threshold</code> are marked as non-duplicates.
	 * Pairs having a similarity larger than or equal to <code>threshold</code> are definite duplicates. The <code>threshold</code> has to be within
	 * the range of [0,1].
	 * 
	 * @param threshold
	 *            The lower threshold of this run.
	 * @throws IOException
	 *             If an error occurs while printing the result.
	 * 
	 * @throws IllegalStateException
	 *             If one essential component was not set.
	 * @throws IllegalArgumentException
	 *             If the passed threshold is invalid.
	 */
	public void run(final double threshold) throws IOException {
		this.run(threshold, threshold);
	}

	/**
	 * Starts a run based on the passed thresholds. All pairs with a similarity smaller than <code>lowerThreshold</code> are marked as non-duplicates.
	 * All pairs with a similarity larger than or equal to the <code>upperThreshold</code> are definite duplicates. Pairs having a similarity larger
	 * than or equal to <code>lowerThreshold</code> but smaller than <code>upperThreshold</code> are marked as <code>fuzzy</code> duplicates.
	 * 
	 * Both thresholds has to be within the range of [0,1] and <code>lowerThreshold</code> <= <code>upperThreshold</code>.
	 * 
	 * @param lowerThreshold
	 *            The lower threshold of this run.
	 * @param upperThreshold
	 *            The upper threshold of this run.
	 * @throws IOException
	 *             If an error occurs while printing the result.
	 * 
	 * @throws IllegalStateException
	 *             If one essential component was not set.
	 * @throws IllegalArgumentException
	 *             If the passed thresholds are invalid in some way.
	 */
	public void run(final double lowerThreshold, final double upperThreshold) throws IOException {

		// check integrity
		if (!this.dataSourcesSet()) {
			throw new IllegalStateException("No DataSource was added.");
		} else if (!this.algorithmSet()) {
			throw new IllegalStateException("No algorithm was set.");
		} else if (!this.similarityFunctionSet()) {
			throw new IllegalStateException("No comparator was set.");
			// } else if (!this.outputSet()) {
			// throw new IllegalStateException("No output was added.");
			// } else if (!this.fuzzyOutputSet()) {
			// throw new IllegalStateException("No fuzzy output was added.");
		} else if (this.statisticsEnabled()) {
			if (!this.statisticOutputSet()) {
				throw new IllegalStateException("No statistic output was added although gathering statistics is enabled.");
			}
			// } else if (!this.goldStandardLoaderSet()) {
			// throw new IllegalStateException("No GoldStandardLoader was set although gathering statistics is enabled.");
		}

		if (lowerThreshold < 0.0) {
			throw new IllegalArgumentException("The passed lower threshold have to be larger than 0.0.");
		} else if (upperThreshold > 1.0) {
			throw new IllegalArgumentException("The passed upper threshold have to be smaller than 1.0.");
		} else if (lowerThreshold > upperThreshold) {
			throw new IllegalArgumentException("The passed lower threshold must not be larger than the upper threshold.");
		}

		this.initializeAlgorithm();

		final NaiveTransitiveClosureGenerator transitiveClosuresGenerator = new NaiveTransitiveClosureGenerator();

		StatisticComponent statisticComponent = null;
		if (this.statisticsEnabled()) {
			statisticComponent = new StatisticComponent(this.getGoldStandard(), this.getAlgorithm());

			statisticComponent.setStartTime();
		}

		for (final DuDeObjectPair pair : this.getAlgorithm()) {
			final double similarity = this.getSimilarityFunction().getSimilarity(pair);
			if (similarity >= upperThreshold) {
				pair.setDuplicateInfo(DuplicateType.Duplicate);
				if (this.transitiveClosuresEnabled()) {
					transitiveClosuresGenerator.add(pair);
				} else {
					this.printPair(pair);

					if (this.statisticsEnabled()) {
						statisticComponent.addDuplicate(pair);
					}
				}
			} else if (similarity >= lowerThreshold) {
				pair.setDuplicateInfo(DuplicateType.Fuzzy);
				this.printFuzzyPair(pair);

				if (this.statisticsEnabled()) {
					statisticComponent.addNonDuplicate(pair);
				}
			} else {
				pair.setDuplicateInfo(DuplicateType.NonDuplicate);
				if (this.statisticsEnabled()) {
					statisticComponent.addNonDuplicate(pair);
				}
			}
		}

		if (this.transitiveClosuresEnabled()) {
			for (final DuDeObjectPair pair : transitiveClosuresGenerator) {
				pair.setDuplicateInfo(DuplicateType.Duplicate);
				this.printPair(pair);

				if (this.statisticsEnabled()) {
					statisticComponent.addDuplicate(pair);
				}
			}
		}

		if (this.statisticsEnabled()) {
			statisticComponent.setEndTime();
		}

		for (final StatisticOutput statisticOutput : this.statisticsOutputs) {
			statisticOutput.setStatistics(statisticComponent);
			statisticOutput.writeStatistics();
		}

		this.algorithm.cleanUp();
	}

	/**
	 * Sets the algorithm of this <code>Experiment</code>.
	 * 
	 * @param algorithm
	 *            The {@link Algorithm} instance that is used internally.
	 */
	public void setAlgorithm(Algorithm algorithm) {
		this.algorithm = algorithm;
	}

	/**
	 * Sets the internally used {@link SimilarityFunction}.
	 * 
	 * @param similarityFunction
	 *            The <code>SimilarityFunction</code> instance.
	 */
	public void setSimilarityFunction(SimilarityFunction similarityFunction) {
		this.similarityFunction = similarityFunction;
	}

	/**
	 * Sets the gold standard loader of this <code>Experiment</code>.
	 * 
	 * @param goldStandard
	 *            The loader of the <code>Experiment's</code> gold standard.
	 */
	public void setGoldStandard(GoldStandard goldStandard) {
		this.goldStandard = goldStandard;
	}

	/**
	 * Sets the lower threshold for this experiment. All pairs with <code>lowerThreshold &lt;= sim && sim &lt; upperThreshold</code> are considered
	 * fuzzy duplicates. <br>
	 * The threshold must be in [0;1] and <code>lowerThreshold &lt;= upperThreshold</code>.
	 * 
	 * 
	 * @param lowerThreshold
	 *            the lower threshold
	 */
	public void setLowerThreshold(double lowerThreshold) {
		this.setThresholds(lowerThreshold, this.upperThreshold);
	}

	/**
	 * Sets the threshold for this experiment. All pairs with <code>upperThreshold &lt;= sim</code> are duplicates.<br>
	 * The threshold must be in [0;1].<br>
	 * Note: the methods sets the lower and upper threshold to the specified value.
	 * 
	 * @param threshold
	 *            the threshold
	 */
	public void setThreshold(double threshold) {
		this.lowerThreshold = this.upperThreshold = threshold;
	}

	/**
	 * Sets the thresholds for this experiment. All pairs with <code>upperThreshold &lt;= sim</code> are duplicates while pairs with
	 * <code>lowerThreshold &lt;= sim && sim &lt; upperThreshold</code> are considered fuzzy duplicates.<br>
	 * Both thresholds must be in [0;1] and <code>lowerThreshold &lt;= upperThreshold</code>.
	 * 
	 * @param lowerThreshold
	 *            the lower threshold
	 * @param upperThreshold
	 *            the upper threshold
	 */
	public void setThresholds(double lowerThreshold, double upperThreshold) {
		if (lowerThreshold < 0.0) {
			throw new IllegalArgumentException("The passed lower threshold have to be larger than 0.0.");
		} else if (upperThreshold > 1.0) {
			throw new IllegalArgumentException("The passed upper threshold have to be smaller than 1.0.");
		} else if (lowerThreshold > upperThreshold) {
			throw new IllegalArgumentException("The passed lower threshold must not be larger than the upper threshold.");
		}

		this.lowerThreshold = lowerThreshold;
		this.upperThreshold = upperThreshold;
	}

	/**
	 * Sets the thresholds for this experiment. All pairs with <code>upperThreshold &lt;= sim</code> are duplicates.<br>
	 * The threshold must be in [0;1] and <code>lowerThreshold &lt;= upperThreshold</code>.
	 * 
	 * @param upperThreshold
	 *            the upper threshold
	 */
	public void setUpperThreshold(double upperThreshold) {
		this.upperThreshold = upperThreshold;
	}

	/**
	 * Checks whether any {@link StatisticOutput} instance is set.
	 * 
	 * @return <code>true</code>, if any instance is set; otherwise <code>false</code>.
	 */
	protected boolean statisticOutputSet() {
		return !this.statisticsOutputs.isEmpty();
	}

	/**
	 * Checks whether gathering statistics is enabled.
	 * 
	 * @return <code>true</code>, if statistics shall be gathered; otherwise <code>false</code>.
	 */
	protected boolean statisticsEnabled() {
		return this.statisticsEnabled;
	}

	@Override
	public void fromJson(DuDeJsonParser<?> jsonParser) throws JsonParseException, IOException {
		jsonParser.skipToken(JsonToken.START_OBJECT);
		JsonUtil.readFields(jsonParser, this);
		jsonParser.skipToken(JsonToken.END_OBJECT);
	}

	@Override
	public void toJson(DuDeJsonGenerator jsonGenerator) throws JsonGenerationException, IOException {
		jsonGenerator.writeRecordStart();
		JsonUtil.writeFields(jsonGenerator, this);
		jsonGenerator.writeRecordEnd();
	}

	@Override
	public String toString() {
		return "Experiment [algorithm=" + this.algorithm + ", similarityFunction=" + this.similarityFunction + ", dataSources=" + this.dataSources
				+ ", fuzzyOutputs=" + this.fuzzyOutputs + ", goldStandard=" + this.goldStandard + ", inMemoryProcessingEnabled="
				+ this.inMemoryProcessingEnabled + ", outputs=" + this.outputs + ", statisticsEnabled=" + this.statisticsEnabled
				+ ", statisticsOutputs=" + this.statisticsOutputs + ", transitiveClosuresEnabled=" + this.transitiveClosuresEnabled + "]";
	}

	/**
	 * Checks whether the usage of a transitive closure is enabled.
	 * 
	 * @return <code>true</code>, if the algorithm's result shall be processed in a second step using transitive closures; otherwise
	 *         <code>false</code>.
	 */
	protected boolean transitiveClosuresEnabled() {
		return this.transitiveClosuresEnabled;
	}

}
