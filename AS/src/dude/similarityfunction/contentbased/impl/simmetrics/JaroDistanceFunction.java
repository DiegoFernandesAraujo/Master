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

import uk.ac.shef.wit.simmetrics.similaritymetrics.Jaro;
import dude.util.data.DuDeObject;
import dude.util.data.Jsonable;

/**
 * <code>JaroDistanceFunction</code> compares two {@link DuDeObject}s based on the Jaro Distance of the given attribute.
 * 
 * @author Ziawasch Abedjan
 * @author Arvid Heise
 * @author Matthias Pohl
 */
public class JaroDistanceFunction extends SimmetricsFunction<JaroDistanceFunction, Jaro> {

	/**
	 * Internal constructor for {@link Jsonable} deserialization.
	 */
	protected JaroDistanceFunction() {
		super();
	}
	
	/**
	 * Initializes the <code>JaroDistanceFunction</code> with the default tokenizer.
	 * 
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public JaroDistanceFunction(String... defaultAttr) {
		super(new Jaro(), defaultAttr);
	}

	/**
	 * Initializes the <code>JaroDistanceFunction</code> with the default tokenizer.
	 * 
	 * @param attrIndex
	 *            The index of the default attribute. This parameter is used to select specific values of an array.
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public JaroDistanceFunction(int attrIndex, String... defaultAttr) {
		super(new Jaro(), attrIndex, defaultAttr);
	}
}
