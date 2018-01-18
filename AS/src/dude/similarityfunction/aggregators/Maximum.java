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

package dude.similarityfunction.aggregators;

import dude.similarityfunction.SimilarityFunction;
import dude.util.data.Jsonable;

/**
 * <code>Maximum</code> returns the maximal similarity of the added {@link SimilarityFunction}s.
 * 
 * @author Matthias Pohl
 */
public class Maximum extends Aggregator {

	/**
	 * Internal constructor for {@link Jsonable} deserialization.
	 */
	protected Maximum() {
		super();
	}
	
	/**
	 * Initializes a <code>Maximum</code> instance.
	 * 
	 * @param simFunctions
	 *            The aggregated similarity functions.
	 */
	public Maximum(SimilarityFunction... simFunctions) {
		super(simFunctions);
	}
	
	@Override
	protected double getAggregatedSimilarity(double[] similarities, int[] multipliers) {
		double max = 0.0;

		for (double similarity : similarities) {
			max = Math.max(max, similarity);
		}

		return max;
	}

}
