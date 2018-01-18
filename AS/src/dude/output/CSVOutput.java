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

package dude.output;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonToken;

import dude.util.GlobalConfig;
import dude.util.csv.CSVWriter;
import dude.util.data.DuDeObjectPair;
import dude.util.data.Jsonable;
import dude.util.data.json.DuDeJsonGenerator;
import dude.util.data.json.DuDeJsonParser;
import dude.util.data.json.JsonUtil;

/**
 * Writes passed <code>DudeObjectPairs</code> , their similarity value and selected optional value in a CSV file row by row.
 * 
 * @author Ziawasch Abedjan
 */
public class CSVOutput implements DuDeOutput, Jsonable {

	private static final Logger logger = Logger.getLogger(CSVOutput.class.getPackage().getName());

	/**
	 * The default separator character.
	 */
	public static final char DEFAULT_SEPARATOR = ';';

	/**
	 * The default quote character.
	 */
	public static final char DEFAULT_QUOTE_CHARACTER = '"';

	/**
	 * The default escape character.
	 */
	public static final char DEFAULT_ESCAPE_CHARACTER = '\\';

	/**
	 * Returns the String representation of the object or <code>null</code>, if <code>null</code> was passed.
	 * 
	 * @param obj
	 *            The object whose String representation shall be returned.
	 * @return The String representation of the passed object or <code>null</code>, if <code>null</code> was passed.
	 */
	protected static String getString(Object obj) {
		if (obj == null)
			return null;

		return obj.toString();
	}

	private boolean writeHeader = true;

	/**
	 * The default header.
	 */
	protected final String[] defaultColumnNames = { "First Object", "Second Object", "Similarity Value", "First Object Data", "Second Object Data" };

	private boolean firstElementWritten = false;

	private final Map<String, String> columnExtension = new LinkedHashMap<String, String>();

	private transient CSVWriter csvWriter;

	private boolean printCompleteIdentifier = true;

	private boolean printData = false;

	private transient File csvFile;

	/**
	 * Initializes a new <code>CSVOutput</code>.
	 * 
	 * @param file
	 *            The file that is used for this output.
	 * @throws IOException
	 *             if the file exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any
	 *             other reason
	 */
	public CSVOutput(File file) throws IOException {
		this.csvFile = file;
		this.initializeCSVWriter(new FileWriter(file));
	}

	/**
	 * Initializes a new <code>CSVOutput</code> with the passed {@link OutputStream}.
	 * 
	 * @param stream
	 *            The stream that is used for printing the result.
	 */
	public CSVOutput(OutputStream stream) {
		try {
			this.initializeCSVWriter(new OutputStreamWriter(stream, GlobalConfig.getInstance().getDefaultEncoding()));
		} catch (final UnsupportedEncodingException e) {
			CSVOutput.logger.warn("The '" + GlobalConfig.getInstance().getDefaultEncoding()
					+ "' encoding could not be used. The system's default encoding was chosen.", e);
			this.initializeCSVWriter(new OutputStreamWriter(stream));
		}
	}

	/**
	 * Initializes a new <code>CSVOutput</code>.
	 * 
	 * @param writer
	 *            The writer that is used for this output.
	 * 
	 */
	public CSVOutput(Writer writer) {
		this.initializeCSVWriter(writer);
	}

	/**
	 * Internal constructor for {@link Jsonable} deserialization.
	 */
	protected CSVOutput() {
		// nothing to do
	}

	@Override
	public void close() throws IOException {
		this.csvWriter.close();
	}

	/**
	 * If this is disabled, the source id won't be printed.
	 */
	public void disablePrintingCompleteIdentifier() {
		this.printCompleteIdentifier = false;
	}

	/**
	 * If this is enabled, the complete identifier is printed.
	 */
	public void enablePrintingCompleteIdentifier() {
		this.printCompleteIdentifier = true;
	}

	@Override
	public void fromJson(DuDeJsonParser<?> jsonParser) throws JsonParseException, IOException {
		jsonParser.skipToken(JsonToken.START_OBJECT);
		JsonUtil.readFields(jsonParser, this);
		jsonParser.skipFieldName("file");
		this.csvFile = new File(jsonParser.nextString());
		this.initializeCSVWriter(new FileWriter(this.csvFile));
		jsonParser.skipToken(JsonToken.END_OBJECT);
	}

	/**
	 * Generates the data that shall be printed.
	 * 
	 * @param pair
	 *            The pair whose information shall be printed.
	 * @return The data that shall be written into the output.
	 */
	protected String[] getDataLine(DuDeObjectPair pair) {
		final Collection<String> data = new ArrayList<String>();

		if (this.printingCompleteIdentifierEnabled()) {
			// add object id only
			data.add(CSVOutput.getString(pair.getFirstElement().getIdentifier()));
			data.add(CSVOutput.getString(pair.getSecondElement().getIdentifier()));
		} else {
			// add complete identifier of each object
			data.add(CSVOutput.getString(pair.getFirstElement().getObjectId()));
			data.add(CSVOutput.getString(pair.getSecondElement().getObjectId()));
		}

		// add similarity
		data.add(String.valueOf(pair.getSimilarity()));

		// add data
		if (this.printingDataEnabled()) {
			data.add(CSVOutput.getString(pair.getFirstElement().getData()));
			data.add(CSVOutput.getString(pair.getSecondElement().getData()));
		}

		// add optional column values
		for (final Map.Entry<String, String> entry : this.columnExtension.entrySet())
			data.add(entry.getValue());

		return data.toArray(new String[data.size()]);
	}

	/**
	 * Returns the escape character.
	 * 
	 * @return The escape character.
	 */
	public char getEscapeCharacter() {
		return this.csvWriter.getEscapeCharacter();
	}

	/**
	 * Returns the header. The header contains all default column names and any column extensions.
	 * 
	 * @return The header of this output.
	 */
	protected String[] getHeader() {
		final Collection<String> header = new ArrayList<String>();

		header.addAll(Arrays.asList(this.defaultColumnNames));
		header.addAll(this.columnExtension.keySet());

		return header.toArray(new String[header.size()]);
	}

	/**
	 * Returns the quote character.
	 * 
	 * @return The quote character.
	 */
	public char getQuoteCharacter() {
		return this.csvWriter.getQuoteCharacter();
	}

	/**
	 * Returns the separator character.
	 * 
	 * @return The separator character.
	 */
	public char getSeparator() {
		return this.csvWriter.getSeparator();
	}

	/**
	 * Checks whether the header shall be written.
	 * 
	 * @return <code>true</code>, if the header will be written before the first pair is printed; otherwise <code>false</code>.
	 */
	public boolean headerIsEnabled() {
		return this.writeHeader;
	}

	private void initializeCSVWriter(Writer writer) {
		this.csvWriter = new CSVWriter(writer);

		this.csvWriter.setSeparator(CSVOutput.DEFAULT_SEPARATOR);
		this.csvWriter.setQuoteCharacter(CSVOutput.DEFAULT_QUOTE_CHARACTER);
		this.csvWriter.setEscapeCharacter(CSVOutput.DEFAULT_ESCAPE_CHARACTER);
	}

	/**
	 * Checks whether printing the complete identifier is enabled.
	 * 
	 * @return <code>true</code>, if it is enabled; otherwise <code>false</code>.
	 */
	public boolean printingCompleteIdentifierEnabled() {
		return this.printCompleteIdentifier;
	}

	/**
	 * Checks whether printing the data is enabled.
	 * 
	 * @return <code>true</code>, if it is enabled; otherwise <code>false</code>.
	 */
	public boolean printingDataEnabled() {
		return this.printData;
	}

	/**
	 * Resets the values of all optional columns using empty Strings.
	 */
	public void resetOptionalColumns() {
		for (final Map.Entry<String, String> entry : this.columnExtension.entrySet())
			entry.setValue("");
	}

	/**
	 * Sets the escape character.
	 * 
	 * @param escapeCharacter
	 *            The new escape character.
	 */
	public void setEscapeCharacter(char escapeCharacter) {
		this.csvWriter.setEscapeCharacter(escapeCharacter);
	}

	/**
	 * Sets a new optional column with no value.
	 * 
	 * @param identifier
	 *            The column's identifier.
	 * @return <code>true</code>, if a new column was added; otherwise <code>false</code>.
	 */
	public boolean setOptionalColumn(String identifier) {
		return this.setOptionalColumn(identifier, "");
	}

	/**
	 * Sets a new optional column with the passed value.
	 * 
	 * @param identifier
	 *            The column's identifier.
	 * @param value
	 *            The column's value.
	 * @return <code>true</code>, if a new column was added; otherwise <code>false</code>.
	 */
	public boolean setOptionalColumn(String identifier, String value) {
		return this.columnExtension.put(identifier, value) == null;
	}

	/**
	 * Sets the quote character.
	 * 
	 * @param quoteCharacter
	 *            The new quote character.
	 */
	public void setQuoteCharacter(char quoteCharacter) {
		this.csvWriter.setQuoteCharacter(quoteCharacter);
	}

	/**
	 * Sets the separator character.
	 * 
	 * @param sep
	 *            The new separator character.
	 */
	public void setSeparator(char sep) {
		this.csvWriter.setSeparator(sep);
	}

	@Override
	public void toJson(DuDeJsonGenerator jsonGenerator) throws JsonGenerationException, IOException {
		jsonGenerator.writeRecordStart();
		JsonUtil.writeFields(jsonGenerator, this);
		if (this.csvFile == null)
			throw new JsonGenerationException("can only serialize csv output if the file has been explicilty specified");
		jsonGenerator.writeStringRecordEntry("file", this.csvFile.toString());
		jsonGenerator.writeRecordEnd();
	}

	@Override
	public DuDeOutput withData() {
		this.printData = true;

		return this;
	}

	/**
	 * Writing the header before writing the first pair is enabled.
	 * 
	 * @return The current instance.
	 */
	public CSVOutput withHeader() {
		this.writeHeader = true;

		return this;
	}

	@Override
	public DuDeOutput withoutData() {
		this.printData = false;

		return this;
	}

	/**
	 * Writing the header before writing the first pair is disabled.
	 * 
	 * @return The current instance.
	 */
	public CSVOutput withoutHeader() {
		this.writeHeader = false;

		return this;
	}

	/**
	 * Writes the Ids of the DuDeObjects their similarity value and specified optional values into the file.
	 */
	@Override
	public void write(DuDeObjectPair pair) throws IOException {
		if (!this.firstElementWritten) {
			if (this.writeHeader)
				this.writeHeader();

			this.firstElementWritten = true;
		}

		// write data line
		this.csvWriter.write(this.getDataLine(pair));
	}

	@Override
	public void writeDuplicatesOnly(DuDeObjectPair pair) throws IOException {
		if (pair.hasDuplicateInfo() && pair.isDuplicate())
			this.write(pair);
	}

	/**
	 * Writes the header into the output.
	 * 
	 * @throws IOException
	 *             If an error occurs during the write process.
	 */
	protected void writeHeader() throws IOException {
		this.csvWriter.write(this.getHeader());
	}

}
