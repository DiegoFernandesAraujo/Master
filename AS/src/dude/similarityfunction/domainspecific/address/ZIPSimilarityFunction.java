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
 * {@link ZIPSimilarityFunction} compares two strings and treats them as ZIP codes, allowing for some special normalization and comparison techniques.
 * 
 * @author tobias.vogel
 */
public class ZIPSimilarityFunction extends ContentBasedSimilarityFunction<ZIPSimilarityFunction> {

	public ZIPSimilarityFunction(String... defaultAttribute) {
		super(defaultAttribute);
	}





	@Override
	protected double compareAtomicValues(JsonAtomic value1, JsonAtomic value2) {
		String zipCode1 = value1.getStringValue();
		String zipCode2 = value2.getStringValue();

		zipCode1 = normalize(zipCode1);
		zipCode2 = normalize(zipCode2);

		/* 
		 * 2 possible errors:
		 * 
		 * 1. typographical errors: truncation, leading 0 is removed -> take string similarity
		 * 2. assignment errors: changing/assigning addresses within larger cities might cause a larger change in zip codes -> do some geo lookup wether 2 zip codes are in near distance
		 */

		double typograhicalSimilarity = new JaroWinklerFunction((String[]) null).getSimilarity(zipCode1, zipCode2);
		double moversSimilarity = 0; //Yahoo geo coding;
		//TODO implement (cached) (yahoo) geo coding
		//http://where.yahooapis.com/geocode?flags=X&appid=fRBqbG52&location=12345+germany

		double similarity = Math.max(typograhicalSimilarity, moversSimilarity);

		return similarity;
	}





	private String normalize(String s) {
		s = s.trim();
		s = s.replaceAll("[^0-9]", "");

		return s;
	}
}