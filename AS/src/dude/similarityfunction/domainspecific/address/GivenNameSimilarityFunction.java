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
import dude.similarityfunction.contentbased.impl.simmetrics.LevenshteinDistanceFunction;
import dude.util.data.json.JsonAtomic;

/**
 * {@link GivenNameSimilarityFunction} compares two strings and treats them as given names, allowing for some special normalization and comparison techniques.
 * 
 * @author tobias.vogel
 */
public class GivenNameSimilarityFunction extends ContentBasedSimilarityFunction<GivenNameSimilarityFunction> {

	public GivenNameSimilarityFunction(String... defaultAttribute) {
		super(defaultAttribute);
	}





	@Override
	protected double compareAtomicValues(JsonAtomic value1, JsonAtomic value2) {
		String givenName1 = value1.getStringValue();
		String givenName2 = value2.getStringValue();

		givenName1 = normalize(givenName1);
		givenName2 = normalize(givenName2);


		// rule-based similarity classification
		double similarity;
		if (seemsToBeAbbreviated(givenName1) || seemsToBeAbbreviated(givenName2))
			similarity = new JaroWinklerFunction((String[]) null).getSimilarity(givenName1, givenName2);
		else
			similarity = new LevenshteinDistanceFunction((String[]) null).getSimilarity(givenName1, givenName2);


		// TODO other fancy similarity functions
		/*
		 * ideen für eine tolle funktion:
		 * 
		 * im allgemeinen levenshtein
		 * Michael vs. M. sollte einigermaßen hoch sein => für diese fälle jarowinkler nehmen
		 * Hans-Georg oder Hans Georg vs. Hans sollte hoch sein
		 * Hans-Georg vs. Karl Hans sollte nicht so hoch sein
		 */

		return similarity;
	}





	private boolean seemsToBeAbbreviated(String s) {
		return s.endsWith(".");
	}





	private String normalize(String s) {
		// TODO implement fancy normalization 
		/*
		 * ideen für eine tolle normalisierung:
		 * bill -> william
		 * karl-heinz -> karl heinz
		 */

		return s.trim();
	}
}