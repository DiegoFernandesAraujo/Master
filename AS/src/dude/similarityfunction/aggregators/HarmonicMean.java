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
 * <code>HarmonicMean</code> returns the harmonic mean of the added {@link SimilarityFunction}s.
 * 
 * @author Matthias Pohl
 */
public class HarmonicMean extends Aggregator {

	/**
	 * Internal constructor for {@link Jsonable} deserialization.
	 */
	protected HarmonicMean() {
		super();
	}
	
	/**
	 * Initializes a <code>HarmonicMean</code> instance.
	 * 
	 * @param simFunctions
	 *            The aggregated similarity functions.
	 */
	public HarmonicMean(SimilarityFunction... simFunctions) {
		super(simFunctions);
	}
	
	@Override
	protected double getAggregatedSimilarity(double[] similarities, int[] multipliers) {
		int multiplierSum = 0;
		double invertedSimilaritySum = 0.0;

		for (int i = 0; i < similarities.length; i++) {
			multiplierSum += multipliers[i];

			if (similarities[i] > 0.0) {
				invertedSimilaritySum += multipliers[i] / similarities[i];
			}
		}

		if (invertedSimilaritySum <= 0.0) {
			return 0.0;
		}

		final double harmonicMean = multiplierSum / invertedSimilaritySum;

		if (harmonicMean < 0.0) {
			return 0.0;
		} else if (harmonicMean > 1.0) {
			return 1.0;
		}

		return harmonicMean;
	}

}
