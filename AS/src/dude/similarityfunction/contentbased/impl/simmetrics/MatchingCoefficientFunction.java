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

import uk.ac.shef.wit.simmetrics.similaritymetrics.MatchingCoefficient;
import uk.ac.shef.wit.simmetrics.tokenisers.InterfaceTokeniser;
import dude.util.data.DuDeObject;
import dude.util.data.Jsonable;

/**
 * <code>MatchingCoefficientFunction</code> compares two {@link DuDeObject}s based on the Matching Coefficient of the given attribute.
 * 
 * @author Ziawasch Abedjan
 * @author Arvid Heise
 * @author Matthias Pohl
 */
public class MatchingCoefficientFunction extends SimmetricsFunction<MatchingCoefficientFunction, MatchingCoefficient> {

	/**
	 * Internal constructor for {@link Jsonable} deserialization.
	 */
	protected MatchingCoefficientFunction() {
		super();
	}
	
	/**
	 * Initializes the <code>MatchingCoefficientFunction</code> with the default tokenizer.
	 * 
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public MatchingCoefficientFunction(String... defaultAttr) {
		super(new MatchingCoefficient(), defaultAttr);
	}

	/**
	 * Initializes the <code>MatchingCoefficientFunction</code> with the default tokenizer.
	 * 
	 * @param attrIndex
	 *            The index of the default attribute. This parameter is used to select specific values of an array.
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public MatchingCoefficientFunction(int attrIndex, String... defaultAttr) {
		super(new MatchingCoefficient(), attrIndex, defaultAttr);
	}

	/**
	 * Initializes the <code>MatchingCoefficientFunction</code> with the passed {@link InterfaceTokeniser}.
	 * 
	 * @param tokenizer
	 *            The internally used tokenizer.
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public MatchingCoefficientFunction(InterfaceTokeniser tokenizer, String... defaultAttr) {
		super(new MatchingCoefficient(tokenizer), defaultAttr);
	}

	/**
	 * Initializes the <code>MatchingCoefficientFunction</code> with the passed {@link InterfaceTokeniser}.
	 * 
	 * @param tokenizer
	 *            The internally used tokenizer.
	 * @param attrIndex
	 *            The index of the default attribute. This parameter is used to select specific values of an array.
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public MatchingCoefficientFunction(InterfaceTokeniser tokenizer, int attrIndex, String... defaultAttr) {
		super(new MatchingCoefficient(tokenizer), attrIndex, defaultAttr);
	}
}
