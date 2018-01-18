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

package dude.preprocessor;

import java.util.HashMap;

import dude.similarityfunction.contentbased.impl.TFIDFSimilarityFunction;
import dude.util.data.DuDeObject;
import dude.util.data.json.JsonArray;
import dude.util.data.json.JsonRecord;
import dude.util.data.json.JsonValue;
import dude.util.data.json.JsonValue.JsonType;

/**
 * The <code>DocumentFrequencyPreprocessor</code> collects frequencies of terms within an attribute value. Each value from the considered attribute is
 * regarded as a document.
 * 
 * @author Ziawasch Abedjan
 * 
 * @see TFIDFSimilarityFunction
 */
public class DocumentFrequencyPreprocessor implements Preprocessor {
	private String attributeName;
	private int documentCount = 0;
	private HashMap<String, Integer> documentFrequencies = new HashMap<String, Integer>();

	/**
	 * Initializes a <code>DocumentFrequencyPreprocessor</code> object for the passed attribute.
	 * 
	 * @param attrName
	 *            The attribute on which the document frequencies are calculated.
	 */
	public DocumentFrequencyPreprocessor(String attrName) {
		this.attributeName = attrName;
	}

	/**
	 * Retrieves the value frequencies within the considered attribute and ads them to the total document frequency of the terms
	 */
	@Override
	public void analyzeDuDeObject(DuDeObject data) {
		this.documentCount++;
		String value = getStringifiedAttributeValue(data);
		if (value != null) {
			String[] terms = value.split(" ");
			for (String term : terms) {
				if (this.documentFrequencies.containsKey(term)) {
					int freq = this.documentFrequencies.get(term);
					this.documentFrequencies.put(term, ++freq);
				} else {
					this.documentFrequencies.put(term, 1);
				}
			}
		}

	}

	@Override
	public void clearData() {
		// nothing to do
	}

	@Override
	public void finish() {
		// nothing to do
	}

	/**
	 * Retrieves the inverse document frequency of the passed term. The document frequency is the number of total occurences of the given term within
	 * values of the considered attribute.
	 * 
	 * @param term
	 *            The considered term
	 * @return Inverse document frequency log(N/df)
	 */
	public double getInverseDocumentFrequency(String term) {
		return Math.log10((double) this.documentCount / (double) this.documentFrequencies.get(term));
	}

	/**
	 * Returns a concatenation of all values stored in the object's attribute.
	 * 
	 * @param obj
	 *            The object whose values specified by the attribute name will be returned as a String.
	 * @return The concatenation of all values stored within the object's attribute.
	 */
	private String getStringifiedAttributeValue(DuDeObject obj) {
		if (obj == null) {
			return null;
		}

		JsonValue value = obj.getAttributeValues(this.attributeName);

		if (value == null) {
			return null;
		} else if (value.getType() == JsonType.Array) {
			return ((JsonArray) value).generateString();
		} else if (value.getType() == JsonType.Record) {
			return ((JsonRecord) value).generateString();
		}

		return value.toString();
	}

}
