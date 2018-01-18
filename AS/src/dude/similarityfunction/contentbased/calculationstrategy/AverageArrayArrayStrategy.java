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

import java.util.Iterator;

import dude.similarityfunction.contentbased.ContentBasedSimilarityFunction;
import dude.util.data.json.JsonArray;
import dude.util.data.json.JsonValue;

/**
 * Calculates the average similarity of the arrays' elements (e.g. the first element of array #1 is compared with the first element of array #2, the
 * second elements are compared with each other etc.). The similarities are added to each other and the over-all sum is divided by the size of the
 * larger array.
 * 
 * @author Matthias Pohl
 */
public class AverageArrayArrayStrategy implements CalculationStrategy<JsonArray, JsonArray> {

	@Override
	public double calculateSimilarity(ContentBasedSimilarityFunction<?> similarityFunction, JsonArray arr1, JsonArray arr2) {
		double similaritySum = 0.0;
		int valueCount = 0;

		Iterator<JsonValue> iterator2 = arr2.iterator();
		for (JsonValue value : arr1) {
			if (iterator2.hasNext()) {
				similaritySum += similarityFunction.calculateSimilarity(value, iterator2.next());
			}
			valueCount++;
		}

		while (iterator2.hasNext()) {
			iterator2.next();
			valueCount++;
		}

		if (valueCount == 0) {
			// both arrays are empty -> hence, they are equal
			return 1.0;
		}

		return similaritySum / valueCount;
	}

}
