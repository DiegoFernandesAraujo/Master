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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonToken;

import dude.postprocessor.ExtendedStatisticComponent;
import dude.postprocessor.StatisticComponent;
import dude.postprocessor.ExtendedStatisticComponent.Config;
import dude.util.csv.CSVWriter;
import dude.util.data.Jsonable;
import dude.util.data.json.DuDeJsonGenerator;
import dude.util.data.json.DuDeJsonParser;
import dude.util.data.json.JsonUtil;

/**
 * <code>CSVStatisticOutput</code> writes the statistics provided by a {@link StatisticComponent} instance into a <code>CSV</code> file.
 * 
 * @author Ziawasch Abedjan
 * @author Matthias Pohl
 * @author Fabian Lindenberg
 * @author Uwe Draisbach
 * @author Cindy Faehnrich
 * 
 */
public class CSVStatisticOutput extends AbstractStatisticOutput implements Jsonable {

	private static final Logger logger = Logger.getLogger(CSVStatisticOutput.class.getPackage().getName());

	private transient CSVWriter csvWriter;

	private transient File csvFile;

	private boolean writeHeader = true;

	/**
	 * Initializes a <code>CSVStatisticOutput</code> with no statistics. The statistics need to be set later on.
	 * 
	 * @param out
	 *            The output stream that is used for the output.
	 * @param sep
	 *            The used separator character.
	 */
	public CSVStatisticOutput(OutputStream out, char sep) {
		this(out, null, sep);
	}

	/**
	 * Initializes the <code>CSVStatisticOutput</code>.
	 * 
	 * @param out
	 *            The output stream that is used for the output.
	 * @param statistics
	 *            The <code>StatisticComponent</code> instance that provides all the statistics.
	 * @param sep
	 *            The used separator character.
	 */
	public CSVStatisticOutput(OutputStream out, StatisticComponent statistics, char sep) {
		super(statistics);

		this.csvWriter = new CSVWriter(out);
		this.csvWriter.setSeparator(sep);
	}

	/**
	 * Initializes a <code>CSVStatisticOutput</code> with no statistics. The statistics need to be set later on.
	 * 
	 * @param csvFile
	 *            The file to write to.
	 * @param sep
	 *            The used separator character.
	 * @throws FileNotFoundException
	 */
	public CSVStatisticOutput(File csvFile, char sep) throws FileNotFoundException {
		this(csvFile, null, sep);
	}

	/**
	 * Initializes the <code>CSVStatisticOutput</code>.
	 * 
	 * @param csvFile
	 *            The file to write to.
	 * @param statistics
	 *            The <code>StatisticComponent</code> instance that provides all the statistics.
	 * @param sep
	 *            The used separator character.
	 * @throws FileNotFoundException
	 */
	public CSVStatisticOutput(File csvFile, StatisticComponent statistics, char sep) throws FileNotFoundException {
		this(new FileOutputStream(csvFile), statistics, sep);

		this.csvFile = csvFile;
	}

	/**
	 * Internal constructor for {@link Jsonable} deserialization.
	 */
	protected CSVStatisticOutput() {
		// nothing to do
	}

	/**
	 * Disables writing the header into the output.
	 */
	public void disableHeader() {
		this.writeHeader = false;
	}

	/**
	 * Enables writing the header into the output.
	 */
	public void enableHeader() {
		this.writeHeader = true;
	}

	/**
	 * Checks whether the header information will be written into the output.
	 * 
	 * @return <code>true</code>, if the header information will be written into the output; otherwise <code>false</code>.
	 */
	public boolean writeHeader() {
		return this.writeHeader;
	}

	/**
	 * Converts the data of the {@link StatisticComponent} into a <code>Map</code>.
	 * 
	 * @return A <code>Map</code> containing the converted statistics.
	 */
	protected Map<String, String> getConvertedStatistics() {
		Map<String, String> stats = new LinkedHashMap<String, String>();
		
		DecimalFormat dformat = new DecimalFormat("#.###############");

		// default columns
		stats.put(defaultLabels[0], String.valueOf(this.getStatistics().getStartDate()));                                    // "Start Time"
		stats.put(defaultLabels[1], String.valueOf(this.getStatistics().getEndDate()));                                      // "End Time"
		stats.put(defaultLabels[2], String.valueOf(this.getStatistics().getRuntime()));                                      // "Runtime in ms"
		stats.put(defaultLabels[3], String.valueOf(this.getStatistics().getMaximumMemoryUsed()));                            // "Max memory usage in kb"
		stats.put(defaultLabels[4], String.valueOf(this.getStatistics().getMinimumMemoryUsed()));                            // "Min memory usage in kb"
		stats.put(defaultLabels[5], String.valueOf(this.getStatistics().getAverageMemoryUsed()));                            // "Average memory usage in kb"
		stats.put(defaultLabels[6], String.valueOf(this.getStatistics().getObjectCount()));                                  // "Number of Data Records"
		stats.put(defaultLabels[7], String.valueOf(this.getStatistics().getNumberOfCandidateComparisons()));                 // "Number of Comparison Candidates"
		stats.put(defaultLabels[8], String.valueOf(this.getStatistics().getPairCount()));                                    // "Number of Created Pairs"
		stats.put(defaultLabels[9], String.valueOf(this.getStatistics().getComparisonCount()));                              // "Number of actual Comparisons"
		stats.put(defaultLabels[10], String.valueOf(dformat.format(this.getStatistics().getReductionRatio())));              // "Reduction Ratio"
		stats.put(defaultLabels[11], String.valueOf(this.getStatistics().getNumberOfRealDuplicates()));                      // "Number of real Duplicates"
		//only print these lines if NOT ExtendedStatisticComponent was used
		if (!this.getStatistics().hasGMD()){
			stats.put(defaultLabels[12], String.valueOf(dformat.format(this.getStatistics().getPrecision())));                   // "Precision"
			stats.put(defaultLabels[13], String.valueOf(dformat.format(this.getStatistics().getRecall())));                      // "Recall"
			stats.put(defaultLabels[14], String.valueOf(dformat.format(this.getStatistics().getFMeasure())));                    // "F-Measure"
		}
		stats.put(defaultLabels[15], String.valueOf(this.getStatistics().getTruePositives()));                               // "True Positives"
		stats.put(defaultLabels[16], String.valueOf(this.getStatistics().getFalsePositives()));                              // "False Positives"
		stats.put(defaultLabels[17], String.valueOf(this.getStatistics().getTrueNegatives()));                               // "True Negatives"
		stats.put(defaultLabels[18], String.valueOf(this.getStatistics().getFalseNegatives()));                              // "False Negatives"
		//only print these lines if NOT ExtendedStatisticComponent was used
		if (!this.getStatistics().hasGMD()){
			stats.put(defaultLabels[19], String.valueOf(dformat.format(this.getStatistics().getPrecisionByComparison())));      // "Precision  based on actual Comparisons"
			stats.put(defaultLabels[20], String.valueOf(dformat.format(this.getStatistics().getRecallByComparison())));         // "Recall based on actual Comparisons"
			stats.put(defaultLabels[21], String.valueOf(dformat.format(this.getStatistics().getFMeasureByComparison())));       // "F-Measure based on actual Comparisons"
		}
		stats.put(defaultLabels[22], String.valueOf(dformat.format(this.getStatistics().getReductionRatioByComparison()))); // "Reduction Ratio based on actual Comparisons"
		stats.put(defaultLabels[23], String.valueOf(this.getStatistics().getTruePositivesByComparison()));                   // "True Positives based on actual Comparisons"
		stats.put(defaultLabels[24], String.valueOf(this.getStatistics().getFalsePositivesByComparison()));                  // "False Positives based on actual Comparisons"
		stats.put(defaultLabels[25], String.valueOf(this.getStatistics().getTrueNegativesByComparison()));                  // "True Negatives based on actual Comparisons"
		stats.put(defaultLabels[26], String.valueOf(this.getStatistics().getFalseNegativesByComparison()));                 // "False Negatives based on actual Comparisons"

		//if the statistic component is an extended one, print for each activated config its GMD
		if (this.getStatistics().hasGMD()){
			for (Config c :Config.values()){
				if (c.isActivated()){
					((ExtendedStatisticComponent)this.getStatistics()).setConfig(c);
					stats.put(c.getLabel(), String.valueOf(((ExtendedStatisticComponent) this.getStatistics()).processGMD()));                 					// "Generalized Merge Distance"
				}			
			}
		}
		// optional columns
		stats.putAll(this.getOptionalEntries());

		return stats;
	}

	@Override
	public void writeStatistics() throws IOException {
		Map<String, String> stats = this.getConvertedStatistics();

		if (this.writeHeader()) {
			// write CSV header
			this.csvWriter.write(stats.keySet().toArray(new String[stats.keySet().size()]));

			// disable header information for the next call of this method
			this.disableHeader();
		}

		// write csv line
		this.csvWriter.write(stats.values().toArray(new String[stats.keySet().size()]));
	}

	/**
	 * Closes the {@link CSVWriter}.
	 */
	@Override
	public void close() {
		try {
			this.csvWriter.close();
		} catch (IOException e) {
			CSVStatisticOutput.logger.warn("Output writer is already closed.", e);
		}
	}

	@Override
	public void fromJson(DuDeJsonParser<?> jsonParser) throws JsonParseException, IOException {
		jsonParser.skipToken(JsonToken.START_OBJECT);
		JsonUtil.readFields(jsonParser, this);
		jsonParser.skipFieldName("file");
		this.csvFile = new File(jsonParser.nextString());
		jsonParser.skipToken(JsonToken.END_OBJECT);
	}

	@Override
	public void toJson(DuDeJsonGenerator jsonGenerator) throws JsonGenerationException, IOException {
		jsonGenerator.writeRecordStart();
		JsonUtil.writeFields(jsonGenerator, this);
		jsonGenerator.writeRecordFieldName("file");
		jsonGenerator.writeString(this.csvFile.toString());
		jsonGenerator.writeRecordEnd();
	}
}
