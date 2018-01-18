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

import uk.ac.shef.wit.simmetrics.similaritymetrics.SmithWatermanGotoh;
import uk.ac.shef.wit.simmetrics.similaritymetrics.costfunctions.AbstractAffineGapCost;
import uk.ac.shef.wit.simmetrics.similaritymetrics.costfunctions.AbstractSubstitutionCost;
import dude.util.data.DuDeObject;
import dude.util.data.Jsonable;

/**
 * <code>SmithWatermanGotohFunction</code> compares two {@link DuDeObject}s based on the Smith Waterman Gotoh Distance of the given attribute.
 * 
 * @author Ziawasch Abedjan
 * @author Arvid Heise
 * @author Matthias Pohl
 */
public class SmithWatermanGotohFunction extends SimmetricsFunction<SmithWatermanGotohFunction, SmithWatermanGotoh> {

	/**
	 * Internal constructor for {@link Jsonable} deserialization.
	 */
	protected SmithWatermanGotohFunction() {
		super();
	}
	
	/**
	 * Initializes the <code>SmithWatermanGotohFunction</code>.
	 * 
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public SmithWatermanGotohFunction(String... defaultAttr) {
		super(new SmithWatermanGotoh(), defaultAttr);
	}

	/**
	 * Initializes the <code>SmithWatermanGotohFunction</code>.
	 * 
	 * @param attrIndex
	 *            The index of the default attribute. This parameter is used to select specific values of an array.
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public SmithWatermanGotohFunction(int attrIndex, String... defaultAttr) {
		super(new SmithWatermanGotoh(), attrIndex, defaultAttr);
	}

	/**
	 * Initializes the <code>SmithWatermanGotohFunction</code>.
	 * 
	 * @param gapCostFunction
	 *            The cost function for the gap.
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public SmithWatermanGotohFunction(AbstractAffineGapCost gapCostFunction, String... defaultAttr) {
		super(new SmithWatermanGotoh(gapCostFunction), defaultAttr);
	}

	/**
	 * Initializes the <code>SmithWatermanGotohFunction</code>.
	 * 
	 * @param gapCostFunction
	 *            The cost function for the gap.
	 * @param attrIndex
	 *            The index of the default attribute. This parameter is used to select specific values of an array.
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public SmithWatermanGotohFunction(AbstractAffineGapCost gapCostFunction, int attrIndex, String... defaultAttr) {
		super(new SmithWatermanGotoh(gapCostFunction), attrIndex, defaultAttr);
	}

	/**
	 * Initializes the <code>SmithWatermanGotohFunction</code>.
	 * 
	 * @param costFunction
	 *            The cost function to use.
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public SmithWatermanGotohFunction(AbstractSubstitutionCost costFunction, String... defaultAttr) {
		super(new SmithWatermanGotoh(costFunction), defaultAttr);
	}

	/**
	 * Initializes the <code>SmithWatermanGotohFunction</code>.
	 * 
	 * @param costFunction
	 *            The cost function to use.
	 * @param attrIndex
	 *            The index of the default attribute. This parameter is used to select specific values of an array.
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public SmithWatermanGotohFunction(AbstractSubstitutionCost costFunction, int attrIndex, String... defaultAttr) {
		super(new SmithWatermanGotoh(costFunction), attrIndex, defaultAttr);
	}

	/**
	 * Initializes the <code>SmithWatermanGotohFunction</code>.
	 * 
	 * @param gapCostFunction
	 *            The cost function for the gap.
	 * @param costFunction
	 *            The cost function to use.
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public SmithWatermanGotohFunction(AbstractAffineGapCost gapCostFunction, AbstractSubstitutionCost costFunction, String... defaultAttr) {
		super(new SmithWatermanGotoh(gapCostFunction, costFunction), defaultAttr);
	}

	/**
	 * Initializes the <code>SmithWatermanGotohFunction</code>.
	 * 
	 * @param gapCostFunction
	 *            The cost function for the gap.
	 * @param costFunction
	 *            The cost function to use.
	 * @param attrIndex
	 *            The index of the default attribute. This parameter is used to select specific values of an array.
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public SmithWatermanGotohFunction(AbstractAffineGapCost gapCostFunction, AbstractSubstitutionCost costFunction, int attrIndex,
			String... defaultAttr) {
		super(new SmithWatermanGotoh(gapCostFunction, costFunction), attrIndex, defaultAttr);
	}
}
