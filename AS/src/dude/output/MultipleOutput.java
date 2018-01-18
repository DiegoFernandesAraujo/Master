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
import java.util.ArrayList;
import java.util.Collection;

import dude.util.data.DuDeObjectPair;

/**
 * The <code>MultipleOutput</code> to support more than one output formats. This class aggregates multiple outputs and delegates all
 * {@link DuDeOutput} operations to each of the aggregated <code>DuDeOutputs</code>.
 * 
 * @author David Sonnabend
 */
public class MultipleOutput implements DuDeOutput {

	/**
	 * The {@link DuDeOutput}s to use
	 */
	private Collection<DuDeOutput> outputs = new ArrayList<DuDeOutput>();

	/**
	 * The default constructor initialized the <code>MultipleOutput</code>.
	 * 
	 * @param outputs
	 *            The outputs that shall be added.
	 */
	public MultipleOutput(DuDeOutput... outputs) {
		for (DuDeOutput output : outputs) {
			this.addOutput(output);
		}
	}

	/**
	 * The default constructor initialized the <code>MultipleOutput</code>.
	 */
	public MultipleOutput() {
		//
	}

	/**
	 * Adds an output to the list of {@link DuDeOutput}s.
	 * 
	 * @param output
	 *            The output to add.
	 * @return <code>True</code> if output was added successfully, <code>false</code> otherwise.
	 */
	public boolean addOutput(DuDeOutput output) {
		if (output == null) {
			return false;
		}

		return this.outputs.add(output);
	}

	@Override
	public void close() throws IOException {
		for (DuDeOutput output : this.outputs) {
			output.close();
		}
	}

	@Override
	public void write(DuDeObjectPair pair) throws IOException {
		for (DuDeOutput output : this.outputs) {
			output.write(pair);
		}
	}

	@Override
	public void writeDuplicatesOnly(DuDeObjectPair pair) throws IOException {
		if (pair.hasDuplicateInfo() && pair.isDuplicate()) {
			this.write(pair);
		}
	}

	@Override
	public MultipleOutput withData() {
		for (DuDeOutput output : this.outputs) {
			output.withData();
		}

		return this;
	}

	@Override
	public MultipleOutput withoutData() {
		for (DuDeOutput output : this.outputs) {
			output.withoutData();
		}

		return this;
	}

}
