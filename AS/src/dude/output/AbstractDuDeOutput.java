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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonToken;

import dude.util.data.DuDeObjectPair;
import dude.util.data.Jsonable;
import dude.util.data.json.DuDeJsonGenerator;
import dude.util.data.json.DuDeJsonParser;
import dude.util.data.json.JsonUtil;

/**
 * <code>AbstractDuDeOuput</code> is an <code>abstract</code> class which provides the common functionality of every class that implements
 * {@link DuDeOutput}. Every concrete <code>DuDeOutput</code> implementation might inherit from this class instead of implementing the
 * <code>DuDeOutput</code> interface directly.
 * 
 * @author Matthias Pohl
 */
public abstract class AbstractDuDeOutput implements DuDeOutput, Jsonable {

	/**
	 * The stream writer that is used for the output.
	 */
	protected transient OutputStreamWriter outputStream;

	private String header;
	private String footer;

	private boolean systemOutIsUsed = false;

	private boolean printData = false;

	private File file;

	/**
	 * Initializes a <code>DuDeOutput</code> with the given {@link OutputStream}. Since no specialized header and footer are set the default Strings
	 * will be printed. The data is not printed besides the source and object ids.
	 * 
	 * @param out
	 *            The <code>OutputStream</code> into which the information is written.
	 * @throws IOException
	 *             If an error occurs while printing the header.
	 */
	public AbstractDuDeOutput(OutputStream out) throws IOException {
		this(out, null, null);
	}

	/**
	 * Initializes a <code>DuDeOutput</code> with the given {@link OutputStream}. Since no specialized footer is set the default String will be
	 * printed. The data is not printed besides the source and object ids.
	 * 
	 * @param out
	 *            The <code>OutputStream</code> into which the information is written.
	 * @param hdr
	 *            Specifies the header that is written in front of the result list.
	 * @throws IOException
	 *             If an error occurs while printing the header.
	 */
	public AbstractDuDeOutput(OutputStream out, String hdr) throws IOException {
		this(out, hdr, null);
	}

	/**
	 * Initializes a <code>DuDeOutput</code> with the given {@link OutputStream}. The passed Strings will be used as header and footer.
	 * 
	 * @param out
	 *            The <code>OutputStream</code> into which the information is written.
	 * @param hdr
	 *            Specifies the header that is written in front of the result list.
	 * @param ftr
	 *            Specifies the footer that is written after the result list was printed.
	 * @throws IOException
	 *             If an error occurs while printing the header.
	 */
	public AbstractDuDeOutput(OutputStream out, String hdr, String ftr) throws IOException {
		this.outputStream = new OutputStreamWriter(out);
		this.systemOutIsUsed = out == System.out;

		this.setHeader(hdr);
		this.setFooter(ftr);

		this.writeHeaderToStream();
	}

	/**
	 * Initializes a <code>DuDeOutput</code> with the given {@link File}. Since no specialized header and footer are set the default Strings will be
	 * printed. The data is not printed besides the source and object ids.
	 * 
	 * @param out
	 *            The <code>File</code> into which the information is written.
	 * @throws IOException
	 *             If an error occurs while printing the header.
	 */
	public AbstractDuDeOutput(File out) throws IOException {
		this(out, null, null);
	}

	/**
	 * Initializes a <code>DuDeOutput</code> with the given {@link File}. Since no specialized footer is set the default String will be printed. The
	 * data is not printed besides the source and object ids.
	 * 
	 * @param out
	 *            The <code>File</code> into which the information is written.
	 * @param hdr
	 *            Specifies the header that is written in front of the result list.
	 * @throws IOException
	 *             If an error occurs while printing the header.
	 */
	public AbstractDuDeOutput(File out, String hdr) throws IOException {
		this(out, hdr, null);
	}

	/**
	 * Initializes a <code>DuDeOutput</code> with the given {@link File}. The passed Strings will be used as header and footer.
	 * 
	 * @param out
	 *            The <code>File</code> into which the information is written.
	 * @param hdr
	 *            Specifies the header that is written in front of the result list.
	 * @param ftr
	 *            Specifies the footer that is written after the result list was printed.
	 * @throws IOException
	 *             If an error occurs while printing the header.
	 */
	public AbstractDuDeOutput(File out, String hdr, String ftr) throws IOException {
		this.outputStream = new FileWriter(out);
		this.systemOutIsUsed = false;

		this.setHeader(hdr);
		this.setFooter(ftr);

		this.writeHeaderToStream();
	}

	/**
	 * Internal constructor for {@link Jsonable} deserialization.
	 */
	protected AbstractDuDeOutput() {
		// nothing to do
	}

	/**
	 * Checks whether printing the data is enable or not.
	 * 
	 * @return <code>true</code>, if printing the data is enabled; otherwise <code>false</code>.
	 */
	protected boolean printDataEnabled() {
		return this.printData;
	}

	@Override
	public AbstractDuDeOutput withData() {
		this.printData = true;

		return this;
	}

	@Override
	public AbstractDuDeOutput withoutData() {
		this.printData = false;

		return this;
	}

	/**
	 * Writes the passed string into the <code>OutputStream</code>.
	 * 
	 * @param str
	 *            The string that will be written into the <code>OutputStream</code>.
	 * @throws IOException
	 *             If an error occurs while writing to the <code>OutputStream</code>.
	 */
	protected void writeToStream(String str) throws IOException {
		this.outputStream.write(str);
		this.outputStream.flush();
	}

	/**
	 * Writes the passed string followed by a newline into the <code>OutputStream</code>.
	 * 
	 * @param str
	 *            The string that will be written into the <code>OutputStream</code>.
	 * @throws IOException
	 *             If an error occurs while writing to the <code>OutputStream</code>.
	 */
	protected void writelnToStream(String str) throws IOException {
		this.writeToStream(str + System.getProperty("line.separator"));
	}

	@Override
	public void close() throws IOException {
		this.writeFooterToStream();

		// System.out should not be closed
		if (!this.systemOutIsUsed) {
			this.outputStream.close();
		}
	}

	private void setHeader(String hdr) {
		if (hdr == null) {
			this.header = getDefaultHeader();
		} else {
			this.header = hdr;
		}
	}

	private void writeHeaderToStream() throws IOException {
		if (!this.header.equals("")) {
			this.writelnToStream(this.header);
		}
	}

	/**
	 * Returns the default header of the implementation. This method should be overwritten by a subclass if it shall provide another default header
	 * than an empty String.
	 * 
	 * @return Returns an empty String.
	 */
	protected String getDefaultHeader() {
		return "";
	}

	private void setFooter(String ftr) {
		if (ftr == null) {
			this.footer = getDefaultFooter();
		} else {
			this.footer = ftr;
		}
	}

	private void writeFooterToStream() throws IOException {
		if (!this.footer.equals("")) {
			this.writelnToStream("");
			this.writeToStream(this.footer);
		}
	}

	/**
	 * Returns the default footer of the implementation. This method should be overwritten by a subclass if it shall provide another default header
	 * than an empty String.
	 * 
	 * @return Returns an empty String.
	 */
	protected String getDefaultFooter() {
		return "";
	}

	@Override
	public abstract void write(DuDeObjectPair pair) throws IOException;

	/**
	 * DuDeOutput#writeDuplicatesOnly(DuDeObjectPair)
	 */
	@Override
	public void writeDuplicatesOnly(DuDeObjectPair pair) throws IOException {
		if (pair.hasDuplicateInfo() && pair.isDuplicate()) {
			this.write(pair);
		}
	}

	@Override
	public void fromJson(DuDeJsonParser<?> jsonParser) throws JsonParseException, IOException {
		jsonParser.skipToken(JsonToken.START_OBJECT);
		JsonUtil.readFields(jsonParser, this);
		jsonParser.skipFieldName("file");
		this.file = new File(jsonParser.nextString());
		jsonParser.skipToken(JsonToken.END_OBJECT);

		this.outputStream = new OutputStreamWriter(this.systemOutIsUsed ? System.out : new FileOutputStream(this.file));
		this.writeHeaderToStream();
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
}
