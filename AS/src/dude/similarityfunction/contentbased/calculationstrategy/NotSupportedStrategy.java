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
 * <code>NotSupportedStrategy</code> will throw an <code>IllegalArgumentException</code> no matter which values were passed.
 * 
 * @author Matthias Pohl
 * 
 * @param <T1>
 *            The type of the first value.
 * @param <T2>
 *            The type of the second value.
 */
public class NotSupportedStrategy<T1 extends JsonValue, T2 extends JsonValue> implements CalculationStrategy<T1, T2> {

	@Override
	public double calculateSimilarity(ContentBasedSimilarityFunction<?> similarityFunction, T1 value1, T2 value2) {
		throw new IllegalArgumentException("This similarity function does not allow comparing " + value1.getType() + " and " + value2.getType() + " (SimilarityFunction-Type: " + similarityFunction.getClass().getSimpleName() + "; value1: '" + value1 + "'; value2: '" + value2 + "').");
	}
}
