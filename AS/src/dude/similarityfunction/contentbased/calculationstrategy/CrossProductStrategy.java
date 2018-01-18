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
import dude.util.data.json.JsonValue;

/**
 * <code>CrossProductStrategy</code> compares a each member of the first {@link JsonArray} with all elements of the second <code>JsonArray</code>.
 * Finally, the highest similarity of all combinations is returned.
 * 
 * @author Matthias Pohl
 */
public class CrossProductStrategy implements CalculationStrategy<JsonArray, JsonArray> {

	@Override
	public double calculateSimilarity(ContentBasedSimilarityFunction<?> similarityFunction, JsonArray arr1, JsonArray arr2) {
		double similarity = 0.0;

		for (JsonValue val1 : arr1) {
			for (JsonValue val2 : arr2) {
				similarity = Math.max(similarity, similarityFunction.calculateSimilarity(val1, val2));

				if (similarity >= 1.0) {
					return similarity;
				}
			}
		}

		return similarity;
	}

}
