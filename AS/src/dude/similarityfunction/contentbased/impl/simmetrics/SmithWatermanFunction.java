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

import uk.ac.shef.wit.simmetrics.similaritymetrics.SmithWaterman;
import uk.ac.shef.wit.simmetrics.similaritymetrics.costfunctions.AbstractSubstitutionCost;
import dude.util.data.DuDeObject;
import dude.util.data.Jsonable;

/**
 * <code>SmithWatermanFunction</code> compares two {@link DuDeObject}s based on the Smith Waterman Distance of the given attribute.
 * 
 * @author Ziawasch Abedjan
 * @author Arvid Heise
 * @author Matthias Pohl
 */
public class SmithWatermanFunction extends SimmetricsFunction<SmithWatermanFunction, SmithWaterman> {

	/**
	 * Internal constructor for {@link Jsonable} deserialization.
	 */
	protected SmithWatermanFunction() {
		super();
	}
	
	/**
	 * Initializes the <code>SmithWatermanFunction</code>.
	 * 
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public SmithWatermanFunction(String... defaultAttr) {
		super(new SmithWaterman(), defaultAttr);
	}

	/**
	 * Initializes the <code>SmithWatermanFunction</code>.
	 * 
	 * @param attrIndex
	 *            The index of the default attribute. This parameter is used to select specific values of an array.
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public SmithWatermanFunction(int attrIndex, String... defaultAttr) {
		super(new SmithWaterman(), attrIndex, defaultAttr);
	}

	/**
	 * Initializes the <code>SmithWatermanFunction</code>.
	 * 
	 * @param costGap
	 *            The cost of a gap.
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public SmithWatermanFunction(float costGap, String... defaultAttr) {
		super(new SmithWaterman(costGap), defaultAttr);
	}

	/**
	 * Initializes the <code>SmithWatermanFunction</code>.
	 * 
	 * @param costGap
	 *            The cost of a gap.
	 * @param attrIndex
	 *            The index of the default attribute. This parameter is used to select specific values of an array.
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public SmithWatermanFunction(float costGap, int attrIndex, String... defaultAttr) {
		super(new SmithWaterman(costGap), attrIndex, defaultAttr);
	}

	/**
	 * Initializes the <code>SmithWatermanFunction</code>.
	 * 
	 * @param costFunction
	 *            The cost function to use.
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public SmithWatermanFunction(AbstractSubstitutionCost costFunction, String... defaultAttr) {
		super(new SmithWaterman(costFunction), defaultAttr);
	}

	/**
	 * Initializes the <code>SmithWatermanFunction</code>.
	 * 
	 * @param costFunction
	 *            The cost function to use.
	 * @param attrIndex
	 *            The index of the default attribute. This parameter is used to select specific values of an array.
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public SmithWatermanFunction(AbstractSubstitutionCost costFunction, int attrIndex, String... defaultAttr) {
		super(new SmithWaterman(costFunction), attrIndex, defaultAttr);
	}

	/**
	 * Initializes the <code>SmithWatermanFunction</code>.
	 * 
	 * @param costGap
	 *            The cost of a gap.
	 * @param costFunction
	 *            The cost function to use.
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public SmithWatermanFunction(float costGap, AbstractSubstitutionCost costFunction, String... defaultAttr) {
		super(new SmithWaterman(costGap, costFunction), defaultAttr);
	}

	/**
	 * Initializes the <code>SmithWatermanFunction</code>.
	 * 
	 * @param costGap
	 *            The cost of a gap.
	 * @param costFunction
	 *            The cost function to use.
	 * @param attrIndex
	 *            The index of the default attribute. This parameter is used to select specific values of an array.
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public SmithWatermanFunction(float costGap, AbstractSubstitutionCost costFunction, int attrIndex, String... defaultAttr) {
		super(new SmithWaterman(costGap, costFunction), attrIndex, defaultAttr);
	}
}
