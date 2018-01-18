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

import dude.datasource.DataSource;
import dude.similarityfunction.StringSimilarity;
import dude.similarityfunction.contentbased.ContentBasedSimilarityFunction;
import dude.similarityfunction.contentbased.calculationstrategy.CalculationStrategy;
import dude.util.data.DuDeObject;
import dude.util.data.Jsonable;
import dude.util.data.json.JsonArray;
import dude.util.data.json.JsonAtomic;
import dude.util.data.json.JsonRecord;
import dude.util.data.json.JsonValue;

/**
 * <code>EquationSimilarityFunction</code> checks if two values are equal to each other.
 * 
 * @author Matthias Pohl
 */
public class EquationSimilarityFunction extends ContentBasedSimilarityFunction<EquationSimilarityFunction> implements StringSimilarity {

	private static class EquationStrategy<T1 extends JsonValue, T2 extends JsonValue> implements CalculationStrategy<T1, T2> {

		@Override
		public double calculateSimilarity(ContentBasedSimilarityFunction<?> similarityFunction, T1 value1, T2 value2) {
			return value1.equals(value2) ? 1.0 : 0.0;
		}

	}

	/**
	 * Internal constructor for {@link Jsonable} deserialization.
	 */
	protected EquationSimilarityFunction() {
		super();
		this.initializeCalculationStrategies();
	}

	/**
	 * Initializes a <code>EquationSimilarityFunction</code> with the passed default attribute. This attribute is used for any {@link DuDeObject}
	 * whose {@link DataSource} was not set with for a special attribute. If the requested attribute holds an array, the whole content will be
	 * compared.
	 * 
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public EquationSimilarityFunction(String... defaultAttr) {
		super(defaultAttr);
		this.initializeCalculationStrategies();
	}

	/**
	 * Initializes a <code>EquationSimilarityFunction</code> with the passed default attribute. This attribute is used for any {@link DuDeObject}
	 * whose {@link DataSource} was not set with for a special attribute.
	 * 
	 * @param attrIndex
	 *            The index of the default attribute. This parameter is used to select specific values of an array.
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public EquationSimilarityFunction(int attrIndex, String... defaultAttr) {
		super(attrIndex, defaultAttr);
		this.initializeCalculationStrategies();
	}

	/**
	 * Customizes all calculation strategies in order to check equality no matter which type the passed values are.
	 */
	private void initializeCalculationStrategies() {
		this.setCompareArrayArrayStrategy(new EquationStrategy<JsonArray, JsonArray>());
		this.setCompareArrayAtomicStrategy(new EquationStrategy<JsonArray, JsonAtomic>());
		this.setCompareArrayRecordStrategy(new EquationStrategy<JsonArray, JsonRecord>());
		this.setCompareRecordAtomicStrategy(new EquationStrategy<JsonRecord, JsonAtomic>());
		this.setCompareRecordRecordStrategy(new EquationStrategy<JsonRecord, JsonRecord>());
	}

	@Override
	protected double compareAtomicValues(JsonAtomic value1, JsonAtomic value2) {
		if (value1.equals(value2)) {
			return 1.0;
		}

		return 0.0;
	}

	@Override
	public double getSimilarity(String str1, String str2) {
		if (str1 == null || !str1.equals(str2)) {
			return 0.0;
		}

		return 1.0;
	}

}
