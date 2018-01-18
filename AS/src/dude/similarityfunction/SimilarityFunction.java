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

import dude.util.data.AutoJsonable;
import dude.util.data.DuDeObject;
import dude.util.data.DuDeObjectPair;

/**
 * <code>SimilarityFunction</code> is used to determine the similarity of two {@link DuDeObject}'s. The similarity is described by a floating-point
 * number within the range of 0 and 1.
 * 
 * @author Matthias Pohl
 * @author Arvid Heise
 * 
 * @see AbstractSimilarityFunction
 */
public interface SimilarityFunction extends AutoJsonable {

	/**
	 * <code>SimilarityValidationState</code> is a descriptor whether two values could be used for similarity calculation or not.
	 * 
	 * @author Matthias Pohl
	 */
	public enum SimilarityValidationState {
		/**
		 * <code>BothValid</code> means, that the similarity can be calculated. Both values are valid.
		 */
		BothValid,
		/**
		 * <code>Value1ValidOnly</code> means, that only the first value was valid (e.g. the second value does not exist).
		 */
		Value1ValidOnly,
		/**
		 * <code>Value2ValidOnly</code> means, that only the second value was valid (e.g. the first value does not exist).
		 */
		Value2ValidOnly,
		/**
		 * <code>BothInvalid</code> means, that both values are invalid (e.g. both values are missing).
		 */
		BothInvalid
	}

	/**
	 * Calculates the similarity of passed {@link DuDeObjectPair}'s members. Besides returning the similarity of the pair, this method stores the
	 * calculated similarity also in the pair using {@link DuDeObjectPair#setSimilarity(double)}. Any already calculated similarity will be
	 * overwritten.
	 * 
	 * @param pair
	 *            A pair of <code>DuDeObjects</code> that shall be compared.
	 * @return A value between <code>0.0</code> (not equal) and <code>1.0</code> (the same object).
	 * 
	 */
	public double getSimilarity(DuDeObjectPair pair);

	/**
	 * Returns the validation state of the last {@link #getSimilarity(DuDeObjectPair)} call. If it was not called, yet,
	 * {@link SimilarityValidationState#BothInvalid} is returned.
	 * 
	 * @return The validation state of the last similarity calculation or {@link SimilarityValidationState#BothInvalid}, if
	 *         {@link #getSimilarity(DuDeObjectPair)} was not called, yet.
	 */
	public SimilarityValidationState getLastValidationState();

}
