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

import java.util.Set;

import uk.ac.shef.wit.simmetrics.tokenisers.TokeniserWhitespace;
import dude.similarityfunction.contentbased.ContentBasedSimilarityFunction;
import dude.util.data.json.JsonAtomic;

/**
 * {@link TitleSimilarityFunction} compares two strings and treats them as person's titles, allowing for some special normalization and comparison techniques.
 * 
 * @author tobias.vogel
 */
public class TitleSimilarityFunction extends ContentBasedSimilarityFunction<TitleSimilarityFunction> {

	public TitleSimilarityFunction(String... defaultAttribute) {
		super(defaultAttribute);
	}





	@Override
	protected double compareAtomicValues(JsonAtomic value1, JsonAtomic value2) {
		String title1 = value1.getStringValue().trim();
		String title2 = value2.getStringValue().trim();



		title1 = "Prof. PhD Dr. Dr Dr Dr.";
		title2 = "Dipl      Ing";

		double similarity;
		if (title1.equalsIgnoreCase(title2))
			similarity = 1;
		else {
			/*
			 * do something token-based
			 * 
			 * "x" vs. "x" -> 1.0
			 * Prof. Dr. vs. Dr. -> ~0.7
			 * Dr. vs. "" -> ~0.5
			 * Dr. vs. Dipl.-Ing. -> ~0.3
			 * 
			 */

			Set<String> title1Tokens = normalize(title1);
			Set<String> title2Tokens = normalize(title2);

			// sim = 1, wenn gleich
			// sim = 0.5, wenn eins eine teilmenge des anderen ist
			// sim = 0 sonst

			if (title1Tokens.equals(title2Tokens))
				// this allows for random order of the title parts
				similarity = 1;
			else if (title1Tokens.containsAll(title2Tokens) || title2Tokens.containsAll(title1Tokens))
				similarity = 0.5;
			else
				similarity = 0;

			//TODO andere idee: jaccard nehmen, alle sonderzeichen und leerzeichen und so entfernen -> ProfDrPhDDiplInf und mit 2-Grammen tokenisieren
		}

		return similarity;
	}





	private Set<String> normalize(String s) {
		// break up Dr.-Ing and Dipl-Ing into their parts
		// could be spelled Dipl-Ing, Dipl.-Ing., DiplIng, ...
		// therefore, replace all possible dividers (-, .) by blanks
		s = s.replaceAll("\\.", " ");
		s = s.replaceAll("-", " ");
		// use camelCase to break the title
		// however, this does affect PhD (we have to live with it)
		s = s.replaceAll("(?<=[a-z])(?=[A-Z])", " ");


		// tokenize title
		// multiple Dr.'s are reduced to one
		Set<String> tokenSet = new TokeniserWhitespace().tokenizeToSet(s);

		return tokenSet;
	}
}