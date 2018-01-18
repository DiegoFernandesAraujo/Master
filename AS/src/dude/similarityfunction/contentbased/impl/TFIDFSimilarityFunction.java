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

package dude.similarityfunction.contentbased.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import dude.preprocessor.DocumentFrequencyPreprocessor;
import dude.similarityfunction.SimilarityFunction;
import dude.similarityfunction.StringSimilarity;
import dude.similarityfunction.contentbased.ContentBasedSimilarityFunction;
import dude.util.data.DuDeObject;
import dude.util.data.Jsonable;
import dude.util.data.json.JsonAtomic;

/**
 * <code>TFIDFSimilarityFunction</code> compares two {@link DuDeObject}s based on the classic tf-idf metric. For enabling the tf-idf the
 * {@link DocumentFrequencyPreprocessor} has to be set. Otherwise this {@link SimilarityFunction} compares the cosine similarity based on
 * term-frequency vectors.
 * 
 * @author Ziawasch Abedjan
 * @author Matthias Pohl
 * 
 * @see DocumentFrequencyPreprocessor
 */
public class TFIDFSimilarityFunction extends ContentBasedSimilarityFunction<TFIDFSimilarityFunction> implements StringSimilarity {

	private DocumentFrequencyPreprocessor idf;
	private String splitToken = " ";

	/**
	 * Internal constructor for {@link Jsonable} deserialization.
	 */
	protected TFIDFSimilarityFunction() {
		super();
	}
	
	/**
	 * Initializes a <code>TFIDFSimilarityFunction</code> object for the passed attribute. Note, using this constructor the document frequencies must
	 * still be added.
	 * 
	 * @param defaultAttr
	 *            The attribute for which the tf-based cosine similarity is calculated.
	 */
	public TFIDFSimilarityFunction(String... defaultAttr) {
		super(defaultAttr);
	}

	/**
	 * Initializes a <code>TFIDFSimilarityFunction</code> object for the passed attribute. Note, using this constructor the document frequencies must
	 * still be added.
	 * 
	 * @param attrIndex
	 *            The index of the default attribute. This parameter is used to select specific values of an array.
	 * @param defaultAttr
	 *            The attribute for which the tf-based cosine similarity is calculated.
	 */
	public TFIDFSimilarityFunction(int attrIndex, String... defaultAttr) {
		super(attrIndex, defaultAttr);
	}

	/**
	 * Initializes a <code>TFIDFSimilarityFunction</code> object for the passed attribute.
	 * 
	 * @param idfPreprocessor
	 *            The {@link DocumentFrequencyPreprocessor} that is needed for calculating the tf-idf similarity.
	 * @param defaultAttr
	 *            The attribute for which the tf-based cosine similarity is calculated.
	 */
	public TFIDFSimilarityFunction(DocumentFrequencyPreprocessor idfPreprocessor, String... defaultAttr) {
		super(defaultAttr);
		this.idf = idfPreprocessor;
	}

	/**
	 * Initializes a <code>TFIDFSimilarityFunction</code> object for the passed attribute.
	 * 
	 * @param idfPreprocessor
	 *            The {@link DocumentFrequencyPreprocessor} that is needed for calculating the tf-idf similarity.
	 * @param attrIndex
	 *            The index of the default attribute. This parameter is used to select specific values of an array.
	 * @param defaultAttr
	 *            The attribute for which the tf-based cosine similarity is calculated.
	 */
	public TFIDFSimilarityFunction(DocumentFrequencyPreprocessor idfPreprocessor, int attrIndex, String... defaultAttr) {
		super(attrIndex, defaultAttr);
		this.idf = idfPreprocessor;
	}

	/**
	 * Returns the split token.
	 * 
	 * @return The split token.
	 */
	public String getSplitToken() {
		return this.splitToken;
	}

	/**
	 * Sets the split token.
	 * 
	 * @param splitTk
	 *            The token that is used for splitting the String.
	 */
	public void setSplitToken(String splitTk) {
		if (splitTk == null) {
			throw new NullPointerException("No split token was passed.");
		}

		this.splitToken = splitTk;
	}

	/**
	 * Sets the split token and returns the current instance.
	 * 
	 * @param splitTk
	 *            The token that is used for splitting the String.
	 * @return The current instance.
	 */
	public TFIDFSimilarityFunction withSplitToken(String splitTk) {
		this.setSplitToken(splitTk);

		return this;
	}

	@Override
	protected double compareAtomicValues(JsonAtomic value1, JsonAtomic value2) {
		if (value1.equals(value2)) {
			return 1.0;
		}
		
		return this.getSimilarity(value1.getStringValue(), value2.getStringValue());
	}

	private double getWeight(Map<String, Integer> map, String term) {
		if (!map.containsKey(term)) {
			return 0;
		}

		return map.get(term);
	}

	@Override
	public String toString() {
		return "TFIDFSimilarityFunction []";
	}

	@Override
	public double getSimilarity(String str1, String str2) {
		if (str1 == null || str2 == null) {
			return 0.0;
		} else if (str1.equals(str2)) {
			return 1.0;
		}
		
		final String[] tokens1Strings = str1.split(this.getSplitToken());
		final String[] tokens2Strings = str2.split(this.getSplitToken());

		final Map<String, Integer> termFrequencies1 = new HashMap<String, Integer>();
		final Map<String, Integer> termFrequencies2 = new HashMap<String, Integer>();
		final Set<String> totalterms = new HashSet<String>();

		// retrieves term frequencies for terms of value1
		for (final String term : tokens1Strings) {
			if (termFrequencies1.containsKey(term)) {
				int frequency = termFrequencies1.get(term);
				frequency++;
				termFrequencies1.put(term, frequency);
			} else {
				termFrequencies1.put(term, 1);
				totalterms.add(term);
			}
		}

		// retrieves term frequencies for terms of value2
		for (final String term : tokens2Strings) {
			if (termFrequencies2.containsKey(term)) {
				int frequency = termFrequencies2.get(term);
				frequency++;
				termFrequencies2.put(term, frequency);
			} else {
				termFrequencies2.put(term, 1);
				totalterms.add(term);
			}
		}

		// computation starts
		double dotproduct = 0.0;
		double vectorlength1 = 0.0;
		double vectorlength2 = 0.0;

		// computes without inverse document frequency, if there are no document frequencies available
		if (this.idf == null) {
			// computes the numerator of the cosine similarity
			for (final String term : totalterms) {
				dotproduct = dotproduct + this.getWeight(termFrequencies1, term) * this.getWeight(termFrequencies2, term);
			}

			// computes the denominator factors
			for (final String term : termFrequencies1.keySet()) {
				final double freq = termFrequencies1.get(term);
				vectorlength1 = vectorlength1 + freq * freq;
			}

			for (final String term : termFrequencies2.keySet()) {
				final double freq = termFrequencies2.get(term);
				vectorlength2 = vectorlength2 + freq * freq;
			}
		} else {
			// computes the numerator of the cosine similarity
			for (final String term : totalterms) {
				dotproduct = dotproduct + this.getWeight(termFrequencies1, term) * this.getWeight(termFrequencies2, term)
						* this.idf.getInverseDocumentFrequency(term) * this.idf.getInverseDocumentFrequency(term);
			}

			// computes the denominator factors
			for (final String term : termFrequencies1.keySet()) {
				final double freq = termFrequencies1.get(term);
				vectorlength1 = vectorlength1 + freq * freq * this.idf.getInverseDocumentFrequency(term) * this.idf.getInverseDocumentFrequency(term);
			}

			for (final String term : termFrequencies2.keySet()) {
				final double freq = termFrequencies2.get(term);
				vectorlength2 = vectorlength2 + freq * freq * this.idf.getInverseDocumentFrequency(term) * this.idf.getInverseDocumentFrequency(term);
			}
		}

		final double denominator = Math.sqrt(vectorlength1 * vectorlength2);
		return dotproduct / denominator;
	}

}
