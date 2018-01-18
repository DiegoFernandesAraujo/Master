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

import dude.similarityfunction.SimilarityFunction;
import dude.similarityfunction.contentbased.ContentBasedSimilarityFunction;
import dude.util.data.DuDeObject;
import dude.util.data.Jsonable;
import dude.util.data.json.JsonAtomic;
import dude.util.data.json.JsonNumber;
import dude.util.data.json.JsonValue.JsonType;

/**
 * This {@link SimilarityFunction} implementation checks the relative variation of the numbers of two {@link DuDeObject} attributes. The maximum
 * allowed variation is defined by percentage (of the higher of the two value).
 * 
 * @author David Sonnabend
 * @author Arvid Heise
 */
public class RelativeNumberDiffFunction extends ContentBasedSimilarityFunction<RelativeNumberDiffFunction> {

	/**
	 * The default maximum tolerance factor that is used, if no other factor is set.
	 */
	public static final double DEFAULT_MAX_TOLERANCE_FACTOR = 0.001;

	/**
	 * The maximum percentage of allowed absolute variation.
	 */
	protected double maxToleranceFactor;

	/**
	 * The allowed variation of two doubles, so that the two values are regarded as equal.
	 */
	protected static final double DOUBLE_EQUALITY_EPSILON = 0.0000001;

	/**
	 * Internal constructor for {@link Jsonable} deserialization.
	 */
	protected RelativeNumberDiffFunction() {
		super();
	}
	
	/**
	 * The constructor initializes the <code>RelativeNumberDiffFunction</code>.
	 * 
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public RelativeNumberDiffFunction(String... defaultAttr) {
		this(RelativeNumberDiffFunction.DEFAULT_MAX_TOLERANCE_FACTOR, defaultAttr);
	}

	/**
	 * The constructor initializes the <code>RelativeNumberDiffFunction</code>.
	 * 
	 * @param attrIndex
	 *            The index of the default attribute. This parameter is used to select specific values of an array.
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public RelativeNumberDiffFunction(int attrIndex, String... defaultAttr) {
		this(RelativeNumberDiffFunction.DEFAULT_MAX_TOLERANCE_FACTOR, attrIndex, defaultAttr);
	}

	/**
	 * The constructor initializes the <code>RelativeNumberDiffFunction</code>.
	 * 
	 * @param maxToleranceFactor
	 *            The percentage of maximum allowed variation.
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public RelativeNumberDiffFunction(double maxToleranceFactor, String... defaultAttr) {
		super(defaultAttr);
		this.maxToleranceFactor = maxToleranceFactor;
	}

	/**
	 * The constructor initializes the <code>RelativeNumberDiffFunction</code>.
	 * 
	 * @param maxToleranceFactor
	 *            The percentage of maximum allowed variation.
	 * @param attrIndex
	 *            The index of the default attribute. This parameter is used to select specific values of an array.
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public RelativeNumberDiffFunction(double maxToleranceFactor, int attrIndex, String... defaultAttr) {
		super(attrIndex, defaultAttr);
		this.maxToleranceFactor = maxToleranceFactor;
	}

	@Override
	protected double compareAtomicValues(JsonAtomic value1, JsonAtomic value2) {
		if (value1.getType() != JsonType.Number || value2.getType() != JsonType.Number) {
			return 0.0;
		}

		final JsonNumber num1 = (JsonNumber) value1;
		final JsonNumber num2 = (JsonNumber) value2;

		// if the values are the same, no further calculation is needed
		if (num1.equals(num2)) {
			return 1.0;
		}

		final double val1 = num1.getValue().doubleValue();
		final double val2 = num2.getValue().doubleValue();

		final double maxAllowdVariation = this.getMaxAllowedVariation(val1, val2);
		double sim;
		final double absDiff = Math.abs(val1 - val2);
		if (maxAllowdVariation != 0) {
			sim = 1 - Math.min(absDiff / maxAllowdVariation, 1);
		} else {
			sim = absDiff < DOUBLE_EQUALITY_EPSILON ? 1.0 : 0.0;
		}

		return sim;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		final RelativeNumberDiffFunction other = (RelativeNumberDiffFunction) obj;
		if (Double.doubleToLongBits(this.maxToleranceFactor) != Double.doubleToLongBits(other.maxToleranceFactor))
			return false;
		return true;
	}

	/**
	 * Gets the maximum allowed variation based on the {@link RelativeNumberDiffFunction#maxToleranceFactor}.
	 * 
	 * @param value1
	 *            The first value.
	 * @param value2
	 *            The second value.
	 * @return The absolute value of maximum allowed variation.
	 */
	protected double getMaxAllowedVariation(double value1, double value2) {
		return Math.max(Math.abs(value1), Math.abs(value2)) * this.maxToleranceFactor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(this.maxToleranceFactor);
		result = prime * result + (int) (temp ^ temp >>> 32);
		return result;
	}

	@Override
	public String toString() {
		return "RelativeNumberDiffFunction [maxToleranceFactor=" + this.maxToleranceFactor + "]";
	}

}
