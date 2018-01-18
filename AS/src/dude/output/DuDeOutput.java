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

import java.io.IOException;

import dude.util.data.AutoJsonable;
import dude.util.data.DuDeObjectPair;

/**
 * <code>DuDeOutput</code> is an interface for writing {@link DuDeObjectPair}s onto an stream.
 * 
 * @author Matthias Pohl
 */
public interface DuDeOutput extends AutoJsonable {

	/**
	 * Writes the {@link DuDeObjectPair} onto an stream.
	 * 
	 * @param pair
	 *            A pair of two <code>DuDeObject</code>s that are written into an <code>OutputStream</code>.
	 * @throws IOException
	 *             If an error occurs while writing onto the stream.
	 */
	public void write(DuDeObjectPair pair) throws IOException;

	/**
	 * Writes the passed {@link DuDeObjectPair} onto the stream, if it is flagged as a duplicate.
	 * 
	 * @param pair
	 *            The pair that shall be written to the stream.
	 * @throws IOException
	 *             If an error occurs while writing onto the stream.
	 *             
	 * @see DuDeObjectPair#getDuplicateInfo()
	 */
	public void writeDuplicatesOnly(DuDeObjectPair pair) throws IOException;

	/**
	 * Closes the stream.
	 * 
	 * @throws IOException
	 *             If an error occurs while closing the stream.
	 */
	public void close() throws IOException;

	/**
	 * Enables printing the data. This means that the output does not only consist of the object ids but contains also the corresponding data.
	 * 
	 * @return The current instance.
	 */
	public DuDeOutput withData();

	/**
	 * Disables printing the data. This means that each printed duplicate pair is only specified by their source and object ids.
	 * 
	 * @return The current instance.
	 */
	public DuDeOutput withoutData();
}
