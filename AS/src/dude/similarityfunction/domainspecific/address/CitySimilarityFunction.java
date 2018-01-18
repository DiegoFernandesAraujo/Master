/*
 * DuDe - The Duplicate Detection Toolkit
 * 
 * Copyright (C) 2011  Hasso-Plattner-Institut für Softwaresystemtechnik GmbH,
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

package dude.similarityfunction.domainspecific.address;

import dude.similarityfunction.contentbased.ContentBasedSimilarityFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.JaroWinklerFunction;
import dude.util.data.json.JsonAtomic;

/**
 * {@link CitySimilarityFunction} compares two strings and treats them as cities, allowing for some special normalization and comparison techniques.
 * 
 * @author tobias.vogel
 */
public class CitySimilarityFunction extends ContentBasedSimilarityFunction<CitySimilarityFunction> {

	public CitySimilarityFunction(String... defaultAttribute) {
		super(defaultAttribute);
	}





	@Override
	protected double compareAtomicValues(JsonAtomic value1, JsonAtomic value2) {
		String city1 = value1.getStringValue();
		String city2 = value2.getStringValue();

		city1 = normalize(city1);
		city2 = normalize(city2);

		/* 
		 * 2 possible errors:
		 * 
		 * 1. typographical errors -> edit distance
		 * 2. locality errors (districts instead of city name) -> geographical similarity
		 */

		double typograhicalSimilarity = new JaroWinklerFunction((String[]) null).getSimilarity(city1, city2);
		double geographicalSimilarity = 0; //Yahoo geo coding;
		//TODO implement (cached) (yahoo) geo coding
		//http://where.yahooapis.com/geocode?flags=X&appid=fRBqbG52&location=12345+germany

		double similarity = Math.max(typograhicalSimilarity, geographicalSimilarity);

		return similarity;
	}





	private String normalize(String s) {
		s = s.trim();
		return s;
	}
}