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
import dude.util.data.json.JsonAtomic;

/**
 * {@link HonorificSimilarityFunction} compares two strings and treats them as honorifics, allowing for some special normalization and comparison techniques.
 * 
 * @author tobias.vogel
 */
public class HonorificSimilarityFunction extends ContentBasedSimilarityFunction<HonorificSimilarityFunction> {

	public HonorificSimilarityFunction(String... defaultAttribute) {
		super(defaultAttribute);
	}





	@Override
	protected double compareAtomicValues(JsonAtomic value1, JsonAtomic value2) {
		String honorific1 = value1.getStringValue();
		String honorific2 = value2.getStringValue();


		honorific1 = normalize(honorific1);
		honorific2 = normalize(honorific2);

		double similarity;
		if (
				(honorific1.equalsIgnoreCase("Herr") && honorific2.equalsIgnoreCase("Frau"))
				|| (honorific1.equalsIgnoreCase("Frau") && honorific2.equalsIgnoreCase("Herr"))
		)
			similarity = 0;
		else if (honorific1.equalsIgnoreCase(honorific2))
			similarity = 1;
		else
			// don't know...
			similarity = 0.5;

		return similarity;
	}





	private String normalize(String s) {
		return s.trim();
	}
}