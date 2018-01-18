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
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonToken;

import dude.postprocessor.ExtendedStatisticComponent;
import dude.postprocessor.ExtendedStatisticComponent.Config;
import dude.postprocessor.StatisticComponent;
import dude.util.data.Jsonable;
import dude.util.data.json.DuDeJsonGenerator;
import dude.util.data.json.DuDeJsonParser;
import dude.util.data.json.JsonUtil;

/**
 * <code>SimpleStatisticOutput</code> prints the statistics in a simple, formatted fashion. It is preferably used for console output.
 * 
 * @author Fabian Lindenberg
 * @author Uwe Draisbach
 * @author Cindy Faehnrich
 */
public class SimpleStatisticOutput extends AbstractStatisticOutput implements Jsonable {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Print out a number using the localized number, integer, currency,
		// and percent format for each locale

		final double myNumber = 0.46778;
		final NumberFormat form = new DecimalFormat("#,##0.####", new DecimalFormatSymbols(Locale.ENGLISH));
		final BigDecimal test = new BigDecimal(myNumber);
		System.out.println(form.format(test));
		// Locale[] locales = NumberFormat.getAvailableLocales();
		// double myNumber = -1234.56;
		// NumberFormat form;
		// for (int j=0; j<4; ++j) {
		// System.out.println("FORMAT");
		// for (int i = 0; i < locales.length; ++i) {
		// if (locales[i].getCountry().length() == 0) {
		// continue; // Skip language-only locales
		// }
		// System.out.print(locales[i].getDisplayName());
		// switch (j) {
		// case 0:
		// form = NumberFormat.getInstance(locales[i]); break;
		// case 1:
		// form = NumberFormat.getIntegerInstance(locales[i]); break;
		// case 2:
		// form = NumberFormat.getCurrencyInstance(locales[i]); break;
		// default:
		// form = NumberFormat.getPercentInstance(locales[i]); break;
		// }
		// if (form instanceof DecimalFormat) {
		// System.out.print(": " + ((DecimalFormat) form).toPattern());
		// }
		// System.out.print(" -> " + form.format(myNumber));
		// try {
		// System.out.println(" -> " + form.parse(form.format(myNumber)));
		// } catch (ParseException e) {}
		// }
		// }

	}

	private transient PrintWriter outputStreamWriter;
	private boolean systemOutIsUsed;

	private File file;

	/**
	 * Internal constructor for {@link Jsonable} deserialization.
	 */
	protected SimpleStatisticOutput() {
		// nothing to do
	}

	/**
	 * Initializes a <code>SimpleStatisticOutput</code> with no statistics. The statistics need to be set later on.
	 * 
	 * @param out
	 *            The output stream that is used for the output.
	 * @throws FileNotFoundException
	 *             If the given file object does not denote an existing, writable regular file and a new regular file of that name cannot be created,
	 *             or if some other error occurs while opening or creating the file
	 */
	public SimpleStatisticOutput(File out) throws FileNotFoundException {
		this(out, null);
	}

	/**
	 * Initializes the <code>SimpleStatisticOutput</code>.
	 * 
	 * @param out
	 *            The file that is used for the output.
	 * @param statistics
	 *            The <code>StatisticComponent</code> instance that provides all the statistics.
	 * @throws FileNotFoundException
	 *             If the given file object does not denote an existing, writable regular file and a new regular file of that name cannot be created,
	 *             or if some other error occurs while opening or creating the file
	 */
	public SimpleStatisticOutput(File out, StatisticComponent statistics) throws FileNotFoundException {
		super(statistics);

		this.outputStreamWriter = new PrintWriter(out);
		this.file = out;
	}

	/**
	 * Initializes a <code>SimpleStatisticOutput</code> with no statistics. The statistics need to be set later on.
	 * 
	 * @param out
	 *            The output stream that is used for the output.
	 */
	public SimpleStatisticOutput(OutputStream out) {
		this(out, null);
	}

	/**
	 * Initializes the <code>SimpleStatisticOutput</code>.
	 * 
	 * @param out
	 *            The file that is used for the output.
	 * @param statistics
	 *            The <code>StatisticComponent</code> instance that provides all the statistics.
	 */
	public SimpleStatisticOutput(OutputStream out, StatisticComponent statistics) {
		super(statistics);

		this.outputStreamWriter = new PrintWriter(out);
		this.systemOutIsUsed = out == System.out;
	}

	@Override
	public void close() throws IOException {
		// System.out should not be closed
		if (!this.systemOutIsUsed)
			this.outputStreamWriter.close();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		final SimpleStatisticOutput other = (SimpleStatisticOutput) obj;
		if (this.file == null) {
			if (other.file != null)
				return false;
		} else if (!this.file.equals(other.file))
			return false;
		if (this.systemOutIsUsed != other.systemOutIsUsed)
			return false;
		return true;
	}

	@Override
	public void fromJson(DuDeJsonParser<?> jsonParser) throws JsonParseException, IOException {
		jsonParser.skipToken(JsonToken.START_OBJECT);
		JsonUtil.readFields(jsonParser, this);
		jsonParser.skipFieldName("file");
		String fileName = jsonParser.nextString();
		jsonParser.skipToken(JsonToken.END_OBJECT);

		this.file = fileName == null ? null : new File(fileName);
		if (this.systemOutIsUsed)
			this.outputStreamWriter = new PrintWriter(System.out);
		else
			this.outputStreamWriter = new PrintWriter(this.file);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.file == null ? 0 : this.file.hashCode());
		result = prime * result + (this.systemOutIsUsed ? 1231 : 1237);
		return result;
	}

	/**
	 * Determines the length of the longest String among the passed values.
	 * 
	 * @param values
	 *            The values whose maximum length will be determined.
	 * @return The maximum length.
	 */
	private int maxLength(String... values) {
		int maxLength = 0;
		for (final String value : values)
			maxLength = Math.max(value.length(), maxLength);
		return maxLength;
	}

	@Override
	public void toJson(DuDeJsonGenerator jsonGenerator) throws JsonGenerationException, IOException {
		if (this.file == null && !this.systemOutIsUsed)
			throw new JsonGenerationException("can only serialize output if the file has been explicilty specified or System.out is used");

		jsonGenerator.writeRecordStart();
		JsonUtil.writeFields(jsonGenerator, this);
		jsonGenerator.writeStringRecordEntry("file", this.file == null ? null : this.file.toString());
		jsonGenerator.writeRecordEnd();
	}

	@Override
	public String toString() {
		return "SimpleStatisticOutput [file=" + this.file + ", systemOutIsUsed=" + this.systemOutIsUsed + "]";
	}

	/**
	 * Writes the label-value pairs line by line. The values will be right aligned.
	 * 
	 * @param labels
	 *            The labels that shall be printed.
	 * @param values
	 *            The corresponding values.
	 */
	private void writeFormattedGroup(String[] labels, String[] values) {
		final int maxLabelLength = this.maxLength(labels);
		final int maxValueLength = this.maxLength(values);

		for (int i = 0; i < labels.length; i++)
			this.outputStreamWriter.printf("%s: %" + (maxLabelLength - labels[i].length() + maxValueLength) + "s%n", labels[i], values[i]);

		this.outputStreamWriter.println();
	}

	@Override
	public void writeStatistics() {
		final NumberFormat form = new DecimalFormat("#,##0.####", new DecimalFormatSymbols(Locale.ENGLISH));

		// special treatment for the run time value: it is not right-aligned
		final String formattedRuntime = form.format(this.getStatistics().getRuntime());
		final String formattedMaxMem = this.getStatistics().getMaximumMemoryUsed();
		final String formattedMinMem = this.getStatistics().getMinimumMemoryUsed();
		final String formattedAvgMem = this.getStatistics().getAverageMemoryUsed();
		int maxLabelLength = this.maxLength(defaultLabels[0], defaultLabels[1], defaultLabels[2], defaultLabels[3], defaultLabels[4],
				defaultLabels[5]);
		final int maxValueLength = this.maxLength(String.valueOf(this.getStatistics().getStartDate()), String.valueOf(this.getStatistics()
				.getEndDate()), formattedRuntime);
		this.outputStreamWriter.printf("%s: %" + (maxLabelLength - defaultLabels[0].length() + maxValueLength) + "s%n", defaultLabels[0], this
				.getStatistics().getStartDate()); // "Start Date"
		this.outputStreamWriter.printf("%s: %" + (maxLabelLength - defaultLabels[1].length() + maxValueLength) + "s%n", defaultLabels[1], this
				.getStatistics().getEndDate());  // "End Date"
		this.outputStreamWriter.printf("%s: %" + (maxLabelLength - defaultLabels[2].length() + formattedRuntime.length()) + "s%n", defaultLabels[2],
				formattedRuntime); // "Runtime in ms"
		this.outputStreamWriter.printf("%s: %" + (maxLabelLength - defaultLabels[3].length() + formattedRuntime.length()) + "s%n", defaultLabels[3],
				formattedMaxMem); // "Max memory Usage in KB"
		this.outputStreamWriter.printf("%s: %" + (maxLabelLength - defaultLabels[4].length() + formattedRuntime.length()) + "s%n", defaultLabels[4],
				formattedMinMem); // "Min memory Usage in KB"
		this.outputStreamWriter.printf("%s: %" + (maxLabelLength - defaultLabels[5].length() + formattedRuntime.length()) + "s%n", defaultLabels[5],
				formattedAvgMem); // "Avg memory Usage in KB"
		this.outputStreamWriter.println();

		// right alignment
		this.writeFormattedGroup(new String[] {
				defaultLabels[6], // "Number of Data Records"
				defaultLabels[7], // "Number of Comparison Candidates"
				defaultLabels[8], // "Number of Created Pairs"
				defaultLabels[9], // "Number of actual Comparisons"
				defaultLabels[10], // "reduction ratio"
				defaultLabels[11] // "Number of real Duplicates"
				}, new String[] {
					form.format(this.getStatistics().getObjectCount()),
					form.format(this.getStatistics().getNumberOfCandidateComparisons()),
					form.format(this.getStatistics().getPairCount()),
					form.format(this.getStatistics().getComparisonCount()),
					form.format(this.getStatistics().getReductionRatio()),
					form.format(this.getStatistics().getNumberOfRealDuplicates()) });
		//only print these lines if NOT ExtendedStatisticComponent was used
		if (!this.getStatistics().hasGMD()){
			// right alignment
			this.writeFormattedGroup(new String[] {
				defaultLabels[12], // "Precision"
				defaultLabels[13], // "Recall"
				defaultLabels[14]  // "F-Measure"
				}, new String[] {
					form.format(this.getStatistics().getPrecision()),
					form.format(this.getStatistics().getRecall()),
					form.format(this.getStatistics().getFMeasure()) });
		}
		// right alignment
		this.writeFormattedGroup(new String[] {
				defaultLabels[15], // "True Positives"
				defaultLabels[16], // "False Positives"
				defaultLabels[17], // "True Negatives"
				defaultLabels[18]  // "False Negatives"
				}, new String[] {
					form.format(this.getStatistics().getTruePositives()),
					form.format(this.getStatistics().getFalsePositives()),
					form.format(this.getStatistics().getTrueNegatives()),
					form.format(this.getStatistics().getFalseNegatives()) });
		//only print these lines if NOT ExtendedStatisticComponent was used
		if (!this.getStatistics().hasGMD()){
			this.writeFormattedGroup(new String[] {
				defaultLabels[19], // "Precision based on actual Comparisons"
				defaultLabels[20], // "Recall based on actual Comparisons"
				defaultLabels[21], // "F-Measure based on actual Comparisons"
			}, new String[] {
				form.format(this.getStatistics().getPrecisionByComparison()),
				form.format(this.getStatistics().getRecallByComparison()),
				form.format(this.getStatistics().getFMeasureByComparison()) });
		}
		// right alignment
		this.writeFormattedGroup(new String[] {
				defaultLabels[22], // "Reduction Ratio based on actual Comparisons"
				defaultLabels[23], // "True Positives based on actual Comparisons"
				defaultLabels[24], // "False Positives based on actual Comparisons"
				defaultLabels[25], // "True Negatives based on actual Comparisons"
				defaultLabels[26]  // "False Negatives based on actual Comparisons"
				}, new String[] {
					form.format(this.getStatistics().getReductionRatioByComparison()),
					form.format(this.getStatistics().getTruePositivesByComparison()),
					form.format(this.getStatistics().getFalsePositivesByComparison()),
					form.format(this.getStatistics().getTrueNegativesByComparison()),
					form.format(this.getStatistics().getFalseNegativesByComparison()) });
		
		//if the statistic component is an extended one, print for each activated config its GMD
		if (this.getStatistics().hasGMD()) {
			for (Config c :Config.values()){
				if (c.isActivated()){
					((ExtendedStatisticComponent)this.getStatistics()).setConfig(c);
					this.writeFormattedGroup(new String[] {
							c.getLabel(), // "Generalized Merge Distance"
					}, new String[] {
						form.format(((ExtendedStatisticComponent) this.getStatistics()).processGMD()) });
				}
			}
		}
		// left alignment
		final Set<String> additionalLabels = this.getOptionalEntries().keySet();
		maxLabelLength = this.maxLength(additionalLabels.toArray(new String[additionalLabels.size()]));
		for (final Map.Entry<String, String> entry : this.getOptionalEntries().entrySet())
			this.outputStreamWriter.printf("%s: %" + (maxLabelLength - entry.getKey().length() + entry.getValue().length()) + "s%n", entry.getKey(),
					entry.getValue());

		this.outputStreamWriter.flush();
	}

}
