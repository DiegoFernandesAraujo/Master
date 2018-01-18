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

import dude.similarityfunction.SimilarityFunction;
import dude.similarityfunction.contentbased.ContentBasedSimilarityFunction;
import dude.util.data.json.JsonValue;

/**
 * <code>CalculationStrategy</code> is an interface for different strategies, that can be used within {@link ContentBasedSimilarityFunction}s for defining
 * the behavior of the similarity calculation, if at least one value is not an atomic one.
 * 
 * @author Matthias Pohl
 * 
 * @param <T1>
 *            The type of the first value.
 * @param <T2>
 *            The type of the second value.
 */
public interface CalculationStrategy<T1 extends JsonValue, T2 extends JsonValue> {

	/**
	 * Calculates the similarity of the passed values.
	 * 
	 * @param similarityFunction
	 *            The {@link SimilarityFunction} that is used for atomic values within this strategy.
	 * @param value1
	 *            The first value.
	 * @param value2
	 *            The second value.
	 * @return Returns the similarity of the passed values.
	 */
	public double calculateSimilarity(ContentBasedSimilarityFunction<?> similarityFunction, T1 value1, T2 value2);
}
