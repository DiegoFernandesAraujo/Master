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
import dude.util.data.json.JsonArray;
import dude.util.data.json.JsonAtomic;

/**
 * <code>ArrayConversionStrategy</code> generates a one-element {@link JsonArray} with the passed {@link JsonAtomic} value and runs the passed
 * {@link ContentBasedSimilarityFunction} on both <code>JsonArrays</code>. Hence, this strategy let the <code>JsonAtomic</code> pretend to be a
 * <code>JsonArray</code>.
 * 
 * @author Matthias Pohl
 */
public class ArrayConversionStrategy implements CalculationStrategy<JsonArray, JsonAtomic> {

	@Override
	public double calculateSimilarity(ContentBasedSimilarityFunction<?> similarityFunction, JsonArray array, JsonAtomic value) {
		return similarityFunction.calculateSimilarity(array, new JsonArray(value));
	}

}
