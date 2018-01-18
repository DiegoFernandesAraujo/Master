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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.pojava.datetime.DateTime;
import org.pojava.datetime.DateTimeConfig;

import dude.similarityfunction.contentbased.ContentBasedSimilarityFunction;
import dude.util.data.json.JsonAtomic;

/**
 * {@link DateSimilarityFunction} compares two strings and treats them as dates, allowing for some special normalization and comparison techniques.
 * 
 * @author tobias.vogel
 */
public class DateSimilarityFunction extends ContentBasedSimilarityFunction<DateSimilarityFunction> {

	public DateSimilarityFunction(String... defaultAttribute) {
		super(defaultAttribute);
	}





	@Override
	protected double compareAtomicValues(JsonAtomic value1, JsonAtomic value2) {
		String date1 = value1.getStringValue();
		String date2 = value2.getStringValue();

		date1 = normalize(date1);
		date2 = normalize(date2);


		/**
		 * contains all the similarities between both dates with different parsing configurations
		 */
		List<Double> similarities = new ArrayList<Double>();

		boolean[] date1DmyOrder = {true, false};
		boolean[] date2DmyOrder = {true, false};

		for (boolean date1DmyOrderValue : date1DmyOrder) {
			for (boolean date2DmyOrderValue : date2DmyOrder) {
				try {
					DateTimeConfig dtc1 = new DateTimeConfig();
					dtc1.setDmyOrder(date1DmyOrderValue);
					DateTime parsedDate1 = DateTime.parse(date1, dtc1);

					DateTimeConfig dtc2 = new DateTimeConfig();
					dtc2.setDmyOrder(date2DmyOrderValue);
					DateTime parsedDate2 = DateTime.parse(date2, dtc2);

					double similarity = 0;

					/*
					 * three cases:
					 * two dates could be the same
					 * two dates could be completely different
					 * at least one could be a "default date" such as 01.01.1911 or 09.09.1999, then: give some similarity in between
					 */

					if (parsedDate1.equals(parsedDate2))
						similarity = 1;
					else {
						List<DateTime> defaultDates = Arrays.asList(
								new DateTime("01.01.2011"),
								new DateTime("01.01.1911"),
								new DateTime("09.09.2099"),
								new DateTime("09.09.1999")
						);

						for (DateTime defaultDateTime : defaultDates) {
							if (parsedDate1.equals(defaultDateTime) || parsedDate2.equals(defaultDateTime)) {
								similarity = 0.5;
								break;
							}
						}
					}

					similarities.add(similarity);
				}
				catch (IllegalArgumentException iae) {
					// ignore this exception, this pair of configurations and this comparison and proceed with the next
				}
			}
		}


		double similarity;
		if (similarities.size() > 0)
			similarity = Collections.max(similarities);
		else
			similarity = 0;
		//TODO oder im notfall lieber levenshtein auf beiden daten?
		return similarity;
	}





	private String normalize(String s) {
		return s.trim();
	}
}