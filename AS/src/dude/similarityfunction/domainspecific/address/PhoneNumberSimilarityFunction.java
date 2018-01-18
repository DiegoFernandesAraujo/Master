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
 * {@link PhoneNumberSimilarityFunction} compares two strings and treats them as phone numbers, allowing for some special normalization and comparison techniques.
 * 
 * @author tobias.vogel
 */
public class PhoneNumberSimilarityFunction extends ContentBasedSimilarityFunction<PhoneNumberSimilarityFunction> {

	public PhoneNumberSimilarityFunction(String... defaultAttribute) {
		super(defaultAttribute);
	}





	@Override
	protected double compareAtomicValues(JsonAtomic value1, JsonAtomic value2) {
		String phoneNumber1 = value1.getStringValue();
		String phoneNumber2 = value2.getStringValue();

		phoneNumber1 = normalize(phoneNumber1);
		phoneNumber2 = normalize(phoneNumber2);

		phoneNumber1 = new StringBuffer(phoneNumber1).reverse().toString();
		phoneNumber2 = new StringBuffer(phoneNumber2).reverse().toString();

		int numberOfMatchingDigits = 0;
		for (int i = 0; i < Math.min(phoneNumber1.length(), phoneNumber2.length()); i++) {
			if (phoneNumber1.charAt(i) == phoneNumber2.charAt(i))
				numberOfMatchingDigits++;
			else
				break;
		}

		double similarity = numberOfMatchingDigits / (1.0 * Math.max(phoneNumber1.length(), phoneNumber2.length()));


		return similarity;
	}





	private String normalize(String s) {
		return s.replaceAll("[^0-9]", "");
	}
}