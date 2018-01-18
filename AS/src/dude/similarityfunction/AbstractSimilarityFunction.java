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

package dude.similarityfunction;

import dude.util.data.DuDeObject;
import dude.util.data.DuDeObjectPair;

/**
 * <code>AbstractSimilarityFunction</code> is a skeleton implementation for providing the common functionality of a {@link SimilarityFunction}
 * implementation.
 * 
 * @author Matthias Pohl
 * @author Arvid Heise
 */
public abstract class AbstractSimilarityFunction implements SimilarityFunction {

	// Since the similarity function might be used in multi-threaded environments, the state of the SimilarityFunction has to be thread-local.
	private static class ThreadLocalValidationState extends ThreadLocal<SimilarityValidationState> {

		@Override
		protected SimilarityValidationState initialValue() {
			return SimilarityValidationState.BothValid;
		}

	}

	private final ThreadLocalValidationState lastValidationState = new ThreadLocalValidationState();
	
	/**
	 * Sets the validation state.
	 * 
	 * @param state
	 *            The validation state to be set.
	 */
	protected void setValidationState(SimilarityValidationState state) {
		this.lastValidationState.set(state);
	}

	@Override
	public SimilarityValidationState getLastValidationState() {
		return this.lastValidationState.get();
	}

	@Override
	public double getSimilarity(DuDeObjectPair pair) {
		if (pair == null) {
			throw new NullPointerException("No DuDeObjectPair instance was passed.");
		}

		// stores the calculated similarity of this pair
		pair.setSimilarity(this.calculateSimilarity(pair.getFirstElement(), pair.getSecondElement()));

		return pair.getSimilarity();
	}

	/**
	 * Calculates the similarity of the passed {@link DuDeObject}s. This similarity has to be within the range of [0; 1].
	 * 
	 * @param obj1
	 *            The first <code>DuDeObject</code>.
	 * @param obj2
	 *            The second <code>DuDeObject</code>.
	 * @return The similarity of the passed <code>DuDeObjects</code>.
	 */
	protected abstract double calculateSimilarity(DuDeObject obj1, DuDeObject obj2);

}
