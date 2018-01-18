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

package dude.similarityfunction.contentbased.impl.simmetrics;

import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;
import dude.util.data.DuDeObject;
import dude.util.data.Jsonable;

/**
 * <code>LevenshteinDistanceFunction</code> compares two {@link DuDeObject}s based on the Levenshtein Distance of the given attribute.
 * 
 * @author Ziawasch Abedjan
 * @author Arvid Heise
 * @author Matthias Pohl
 */
public class LevenshteinDistanceFunction extends SimmetricsFunction<LevenshteinDistanceFunction, Levenshtein> {

	/**
	 * Internal constructor for {@link Jsonable} deserialization.
	 */
	protected LevenshteinDistanceFunction() {
		super();
	}
	
	/**
	 * Initializes the <code>LevenshteinDistanceFunction</code> with the default tokenizer.
	 * 
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public LevenshteinDistanceFunction(String... defaultAttr) {
		super(new Levenshtein(), defaultAttr);
	}

	/**
	 * Initializes the <code>LevenshteinDistanceFunction</code> with the default tokenizer.
	 * 
	 * @param attrIndex
	 *            The index of the default attribute. This parameter is used to select specific values of an array.
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public LevenshteinDistanceFunction(int attrIndex, String... defaultAttr) {
		super(new Levenshtein(), attrIndex, defaultAttr);
	}
}
