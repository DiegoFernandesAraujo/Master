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

package dude.similarityfunction.contentbased.calculationstrategy;

import dude.similarityfunction.contentbased.ContentBasedSimilarityFunction;
import dude.util.data.json.JsonValue;

/**
 * <code>IgnoreStrategy</code> ignores the actual values and returns always the same default similarity. The number of times this strategy was used
 * can be reviewed using {@link #getCallCount()}.
 * 
 * @author Matthias Pohl
 * 
 * @param <T1>
 *            The type of the first value.
 * @param <T2>
 *            The type of the second value.
 */
public class IgnoreStrategy<T1 extends JsonValue, T2 extends JsonValue> implements CalculationStrategy<T1, T2> {

	private final double defaultSimilarity;
	private int callCount;

	/**
	 * Initializes a <code>IgnoreStrategy</code> that returns a default similarity of <code>0.0</code>.
	 */
	public IgnoreStrategy() {
		this(0.0);
	}

	/**
	 * Initializes a <code>IgnoreStrategy</code> that returns the passed default similarity.
	 * 
	 * @param defSimilarity
	 *            The similarity that shall be returned by any call of
	 *            {@link #calculateSimilarity(ContentBasedSimilarityFunction, JsonValue, JsonValue)}.
	 */
	public IgnoreStrategy(double defSimilarity) {
		if (defSimilarity < 0.0 || defSimilarity > 1.0) {
			throw new IllegalArgumentException("The passed default similarity has to be within the range of 0 and 1.");
		}

		this.defaultSimilarity = defSimilarity;
		this.resetCallCount();
	}

	@Override
	public double calculateSimilarity(ContentBasedSimilarityFunction<?> similarityFunction, T1 value1, T2 value2) {
		this.callCount++;
		return this.defaultSimilarity;
	}

	/**
	 * Returns the current call count.
	 * 
	 * @return The current call count.
	 */
	public int getCallCount() {
		return this.callCount;
	}

	/**
	 * Resets the call count.
	 */
	public void resetCallCount() {
		this.callCount = 0;
	}

}
