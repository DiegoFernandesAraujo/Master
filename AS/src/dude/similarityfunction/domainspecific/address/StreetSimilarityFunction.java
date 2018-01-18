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
 * {@link StreetSimilarityFunction} compares two strings and treats them as street names, allowing for some special normalization and comparison techniques.
 * 
 * @author tobias.vogel
 */
public class StreetSimilarityFunction extends ContentBasedSimilarityFunction<StreetSimilarityFunction> {

	public StreetSimilarityFunction(String... defaultAttribute) {
		super(defaultAttribute);
	}





	@Override
	protected double compareAtomicValues(JsonAtomic value1, JsonAtomic value2) {
		String streetName1 = value1.getStringValue();
		String streetName2 = value2.getStringValue();

		streetName1 = normalize(streetName1);
		streetName2 = normalize(streetName2);


		double similarity = new JaroWinklerFunction((String[]) null).getSimilarity(streetName1, streetName2);

		return similarity;
	}





	private String normalize(String s) {
		s = s.trim();
		s = s.replaceAll("str\\.", "straße");
		s = s.replaceAll("Str\\.", "Straße");
		s = s.replaceAll("-| |\\.", "");

		return s;
	}
}