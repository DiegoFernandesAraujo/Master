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
import dude.util.data.json.JsonValue;

/**
 * <code>BestMatchCalculationStrategy</code> compares a {@link JsonArray} with a {@link JsonAtomic} by selecting the best match. The member of the
 * array that is most similar to the atomic value will be used as a representative of the <code>JsonArray</code> for the comparison.
 * 
 * @author Matthias Pohl
 */
public class BestMatchCalculationStrategy implements CalculationStrategy<JsonArray, JsonAtomic> {

	@Override
	public double calculateSimilarity(ContentBasedSimilarityFunction<?> similarityFunction, JsonArray array, JsonAtomic value) {
		double maxSimilarity = 0.0;
		for (JsonValue arrValue : array) {
			maxSimilarity = Math.max(maxSimilarity, similarityFunction.calculateSimilarity(arrValue, value));
		}

		return maxSimilarity;
	}

}
