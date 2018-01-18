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

package dude.similarityfunction.structurebased;

import dude.similarityfunction.SimilarityFunction;
import dude.util.data.DuDeObjectPair;
import dude.util.data.Jsonable;

/**
 * <code>ConstantSimilarityFunction</code> returns a similarity that is independent from the passed {@link DuDeObjectPair} and can be specified by the
 * user.
 * 
 * @author Matthias Pohl
 */
public class ConstantSimilarityFunction implements SimilarityFunction {

	private double constantSimilarity;
	private boolean firstCallIsDone = false;

	/**
	 * Internal constructor for {@link Jsonable} deserialization.
	 */
	protected ConstantSimilarityFunction() {
		super();
	}
	
	/**
	 * Initializes the <code>ConstantSimilarityFunction</code> with the passed similarity. Any call of {@link #getSimilarity(DuDeObjectPair)} will set
	 * this similarity for the pair.
	 * 
	 * @param simValue
	 *            The similarity that will be returned by {@link #getSimilarity(DuDeObjectPair)}.
	 */
	public ConstantSimilarityFunction(double simValue) {
		if (simValue < 0.0 || simValue > 1.0) {
			throw new IllegalArgumentException("The passed similarity has to be within a range of 0 and 1.");
		}

		this.constantSimilarity = simValue;
	}

	@Override
	public double getSimilarity(DuDeObjectPair pair) {
		// for specification support
		this.firstCallIsDone = true;

		pair.setSimilarity(this.constantSimilarity);

		return pair.getSimilarity();
	}

	/**
	 * Since <code>ConstantSimilarityFunction</code> is not based on actual values, it returns <code>SimilarityValidationState.BothValid</code> for each
	 * calculated pair.
	 */
	@Override
	public SimilarityValidationState getLastValidationState() {
		if (!this.firstCallIsDone) {
			return SimilarityValidationState.BothInvalid;
		}

		return SimilarityValidationState.BothValid;
	}
}
