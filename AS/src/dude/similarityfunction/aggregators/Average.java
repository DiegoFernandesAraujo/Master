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
 * <code>Average</code> returns the average similarity of all added {@link SimilarityFunction}s.
 * 
 * @author Matthias Pohl
 */
public class Average extends Aggregator {

	/**
	 * Internal constructor for {@link Jsonable} deserialization.
	 */
	protected Average() {
		super();
	}

	/**
	 * Initializes a <code>Average</code> instance.
	 * 
	 * @param simFunctions
	 *            The aggregated similarity functions.
	 */
	public Average(SimilarityFunction... simFunctions) {
		super(simFunctions);
	}

	@Override
	protected double getAggregatedSimilarity(double[] similarities, int[] multipliers) {
		int multiplierSum = 0;
		double similaritySum = 0;

		for (int i = 0; i < similarities.length; i++) {
			multiplierSum += multipliers[i];
			similaritySum += multipliers[i] * similarities[i];
		}

		if (multiplierSum == 0) {
			return 0.0;
		}

		return similaritySum / multiplierSum;
	}

}
