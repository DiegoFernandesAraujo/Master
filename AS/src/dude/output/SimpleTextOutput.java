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
import java.io.IOException;
import java.io.OutputStream;

import dude.util.data.DuDeObject;
import dude.util.data.DuDeObjectPair;
import dude.util.data.Jsonable;

/**
 * <code>SimpleTextOutput</code> writes the passed {@link DuDeObject} pair to an {@link OutputStream} line by line.
 * 
 * @author Matthias Pohl
 */
public class SimpleTextOutput extends AbstractDuDeOutput {

	/**
	 * The default separator string that is used for separating both <code>DuDeObjects</code>, if no separator string is passed.
	 */
	public static final String DEFAULT_SEPARATOR = "\t";

	private String separatorString;

	/**
	 * Initializes a new <code>SimpleTextOutput</code> with the passed parameters.
	 * 
	 * @param out
	 *            The {@link OutputStream} where the data is written to.
	 * @throws IOException
	 *             If an error occurs while accessing the <code>OutputStream</code>.
	 */
	public SimpleTextOutput(OutputStream out) throws IOException {
		super(out);
	}

	/**
	 * Initializes a new <code>SimpleTextOutput</code> with the passed parameters.
	 * 
	 * @param out
	 *            The {@link OutputStream} where the data is written to.
	 * @param hdr
	 *            The header that will be written in front of the actual data.
	 * @throws IOException
	 *             If an error occurs while accessing the <code>OutputStream</code>.
	 */
	public SimpleTextOutput(OutputStream out, String hdr) throws IOException {
		super(out, hdr);
	}

	/**
	 * Initializes a new <code>SimpleTextOutput</code> with the passed parameters.
	 * 
	 * @param out
	 *            The {@link OutputStream} where the data is written to.
	 * @param hdr
	 *            The header that will be written in front of the actual data.
	 * @param ftr
	 *            The footer that will be written after the actual data.
	 * @throws IOException
	 *             If an error occurs while accessing the <code>OutputStream</code>.
	 */
	public SimpleTextOutput(OutputStream out, String hdr, String ftr) throws IOException {
		super(out, hdr, ftr);
	}

	/**
	 * Initializes a new <code>SimpleTextOutput</code> with the passed parameters.
	 * 
	 * @param out
	 *            The {@link File} where the data is written to.
	 * @throws IOException
	 *             If an error occurs while accessing the <code>File</code>.
	 */
	public SimpleTextOutput(File out) throws IOException {
		super(out);
	}

	/**
	 * Initializes a new <code>SimpleTextOutput</code> with the passed parameters.
	 * 
	 * @param out
	 *            The {@link File} where the data is written to.
	 * @param hdr
	 *            The header that will be written in front of the actual data.
	 * @throws IOException
	 *             If an error occurs while accessing the <code>File</code>.
	 */
	public SimpleTextOutput(File out, String hdr) throws IOException {
		super(out, hdr);
	}

	/**
	 * Initializes a new <code>SimpleTextOutput</code> with the passed parameters.
	 * 
	 * @param out
	 *            The {@link File} where the data is written to.
	 * @param hdr
	 *            The header that will be written in front of the actual data.
	 * @param ftr
	 *            The footer that will be written after the actual data.
	 * @throws IOException
	 *             If an error occurs while accessing the <code>File</code>.
	 */
	public SimpleTextOutput(File out, String hdr, String ftr) throws IOException {
		super(out, hdr, ftr);
	}

	/**
	 * Internal constructor for {@link Jsonable} deserialization.
	 */
	protected SimpleTextOutput() {
		// nothing to do
	}

	/**
	 * Writes the <code>DuDeObject</code> pair line by line.
	 */
	@Override
	public void write(DuDeObjectPair pair) throws IOException {

		String addOn1 = "";
		String addOn2 = "";

		String similarity = "";

		if (this.printDataEnabled()) {
			addOn1 = " (" + pair.getFirstElementObjectData().toString() + ")";
			addOn2 = " (" + pair.getSecondElementObjectData().toString() + ")";
		}

		if (pair.hasSimilarity()) {
			similarity = "[" + pair.getSimilarity() + "]";
		}

		this.writelnToStream(pair.getFirstElement().getIdentifier() + addOn1 + this.separatorString + pair.getSecondElement().getIdentifier()
				+ addOn2 + this.separatorString + similarity);
	}

}
