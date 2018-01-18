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

import dude.util.data.DuDeObject;
import dude.util.data.DuDeObjectPair;
import dude.util.data.Jsonable;
import dude.util.data.json.DuDeJsonGenerator;

/**
 * <code>JsonOutput</code> converts the passed {@link DuDeObject} pairs into Json syntax. There are two different modes for writing the information to
 * the stream. The default mode writes only the identifying information while the other one prints the whole data of each <code>DuDeObject</code.
 * 
 * @author Matthias Pohl
 */
public class JsonOutput extends AbstractDuDeOutput {

	/**
	 * The default header of the <code>JsonOutput</code>.
	 */
	protected static final String DEFAULT_HEADER = "[";

	/**
	 * The default footer of the <code>JsonOutput</code>.
	 */
	protected static final String DEFAULT_FOOTER = "]";

	/**
	 * The attribute name of the first object within each pair.
	 */
	public static final String FIRST_OBJECT_ATTRIBUTE = "firstObj";

	/**
	 * The attribute name of the second object within each pair.
	 */
	public static final String SECOND_OBJECT_ATTRIBUTE = "secondObj";

	/**
	 * The attribute name of the similarity that belongs to each pair
	 */
	public static final String SIMILARITY_ATTRIBUTE = "similarity";

	private DuDeJsonGenerator jsonGenerator;

	/**
	 * Initializes a <code>JsonOutput</code> formatter. This instance writes only the identifying information of each object to the stream.
	 * 
	 * @param out
	 *            The stream to which the data shall be written.
	 * @throws IOException
	 *             If an error occurs while writing to the <code>OutputStream</code>.
	 */
	public JsonOutput(OutputStream out) throws IOException {
		super(out, JsonOutput.DEFAULT_HEADER, JsonOutput.DEFAULT_FOOTER);
		this.jsonGenerator = new DuDeJsonGenerator(new OutputStreamWriter(out));
		this.jsonGenerator.writeArrayStart();
	}

	/**
	 * Initializes a <code>JsonOutput</code> formatter. This instance writes only the identifying information of each object to the stream.
	 * 
	 * @param out
	 *            The file to which the data shall be written.
	 * @throws IOException
	 *             If an error occurs while writing to the <code>OutputStream</code>.
	 */
	public JsonOutput(File out) throws IOException {
		super(out, JsonOutput.DEFAULT_HEADER, JsonOutput.DEFAULT_FOOTER);
		this.jsonGenerator = new DuDeJsonGenerator(new FileWriter(out));
		this.jsonGenerator.writeArrayStart();
	}

	/**
	 * Internal constructor for {@link Jsonable} deserialization.
	 */
	protected JsonOutput() {
		// nothing to do
	}

	/**
	 * Returns {@link #DEFAULT_HEADER}.
	 */
	@Override
	protected String getDefaultHeader() {
		return JsonOutput.DEFAULT_HEADER;
	}

	/**
	 * Returns {@link #DEFAULT_FOOTER}.
	 */
	@Override
	protected String getDefaultFooter() {
		return JsonOutput.DEFAULT_FOOTER;
	}

	@Override
	public void write(DuDeObjectPair pair) throws IOException {
		pair.toJson(this.jsonGenerator);
	}
}
