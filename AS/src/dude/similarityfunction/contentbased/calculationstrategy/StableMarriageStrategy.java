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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import dude.similarityfunction.contentbased.ContentBasedSimilarityFunction;
import dude.util.Pair;
import dude.util.data.json.JsonArray;
import dude.util.data.json.JsonValue;

/**
 * <code>StableMarriageStrategy</code> implements the <code>Stable-Marriage</code> algorithm. The returned similarity is the average of all found
 * value pairs.
 * 
 * @author Matthias Pohl
 */
public class StableMarriageStrategy implements CalculationStrategy<JsonArray, JsonArray> {

	@Override
	public double calculateSimilarity(ContentBasedSimilarityFunction<?> similarityFunction, JsonArray men, JsonArray women) {
		if (men.isEmpty() && women.isEmpty()) {
			return 1.0;
		} else if (men.isEmpty() || women.isEmpty()) {
			return 0.0;
		}

		Map<JsonValue, Pair<JsonValue, Double>> matchingPairs = new HashMap<JsonValue, Pair<JsonValue, Double>>(men.size());
		Collection<JsonValue> freeMen = new HashSet<JsonValue>(men);
		Collection<JsonValue> freeWomen = new HashSet<JsonValue>(women);

		while (!freeMen.isEmpty() && freeMen.size() > men.size() - women.size()) {
			Iterator<JsonValue> freeMenIterator = freeMen.iterator();
			Collection<JsonValue> newFreeMen = new ArrayList<JsonValue>();
			while (freeMenIterator.hasNext()) {
				JsonValue currentMan = freeMenIterator.next();

				// get best match for free women
				Pair<JsonValue, Double> favorite = this.getBestMatch(similarityFunction, currentMan, freeWomen);
				JsonValue favoriteWoman = favorite.getFirstElement();
				Double favoriteRank = favorite.getSecondElement();
				
				if (matchingPairs.containsKey(favoriteWoman)) {
					// favorite woman has already a matching man
					Pair<JsonValue, Double> old = matchingPairs.get(favoriteWoman);
					JsonValue oldMan = old.getFirstElement();
					Double oldMansRank = old.getSecondElement();
					
					// if the rank of the current man is higher than the old man's rank -> the woman will leave the old man
					if (favoriteRank > oldMansRank) {
						matchingPairs.put(favoriteWoman, new Pair<JsonValue, Double>(currentMan, favoriteRank));
						freeMenIterator.remove();
						newFreeMen.add(oldMan);
					}
				} else {
					// woman was not engaged, yet
					matchingPairs.put(favoriteWoman, new Pair<JsonValue, Double>(currentMan, favoriteRank));
					freeMenIterator.remove();
				}
			}
			
			freeMen.addAll(newFreeMen);
			newFreeMen = new HashSet<JsonValue>();
			
			freeWomen.removeAll(matchingPairs.keySet());
		}

		double sum = 0;
		for (Map.Entry<JsonValue, Pair<JsonValue, Double>> validPair : matchingPairs.entrySet()) {
			sum += validPair.getValue().getSecondElement();
		}

		return sum / matchingPairs.size();
	}

	private Pair<JsonValue, Double> getBestMatch(ContentBasedSimilarityFunction<?> similarityFunction, JsonValue man, Iterable<JsonValue> women) {
		JsonValue favoriteWoman = null;
		double favoriteRank = -1.0;

		for (JsonValue woman : women) {
			double rank = similarityFunction.calculateSimilarity(man, woman);

			if (favoriteRank < rank) {
				favoriteWoman = woman;
				favoriteRank = rank;
			}
		}

		return new Pair<JsonValue, Double>(favoriteWoman, favoriteRank);
	}

}
