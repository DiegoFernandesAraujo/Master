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
import dude.util.data.DuDeObject;
import dude.util.data.Jsonable;

/**
 * This {@link SimilarityFunction} implementation checks the absolute variation of the numbers of two {@link DuDeObject} attributes. The maximum
 * allowed variation is defined by an absolute value.
 * 
 * @author David Sonnabend
 * @author Arvid Heise
 */
public class AbsoluteNumberDiffFunction extends RelativeNumberDiffFunction {

	/**
	 * The absolute value of maximum allowed variation.
	 */
	protected double maxAbsoluteVariation;

	/**
	 * Internal constructor for {@link Jsonable} deserialization.
	 */
	protected AbsoluteNumberDiffFunction() {
		super();
	}

	/**
	 * The constructor initializes the <code>RelativeNumberDiffFunction</code>.
	 * 
	 * @param maxAbsVariation
	 *            The absolute value of maximum allowed variation.
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public AbsoluteNumberDiffFunction(int maxAbsVariation, String... defaultAttr) {
		super(defaultAttr);
		this.maxAbsoluteVariation = maxAbsVariation;
	}

	/**
	 * The constructor initializes the <code>RelativeNumberDiffFunction</code>.
	 * 
	 * @param maxAbsVariation
	 *            The absolute value of maximum allowed variation.
	 * @param attrIndex
	 *            The index of the default attribute. This parameter is used to select specific values of an array.
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public AbsoluteNumberDiffFunction(int maxAbsVariation, int attrIndex, String... defaultAttr) {
		super(attrIndex, defaultAttr);
		this.maxAbsoluteVariation = maxAbsVariation;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!super.equals(obj)) {
			return false;
		}

		if (this.getClass() != obj.getClass()) {
			return false;
		}

		final AbsoluteNumberDiffFunction other = (AbsoluteNumberDiffFunction) obj;
		if (Double.doubleToLongBits(this.maxAbsoluteVariation) != Double.doubleToLongBits(other.maxAbsoluteVariation)) {
			return false;
		}

		return true;
	}

	/**
	 * Just returns the {@link AbsoluteNumberDiffFunction#maxAbsoluteVariation} set in constructor.
	 */
	@Override
	protected double getMaxAllowedVariation(double value1, double value2) {
		return this.maxAbsoluteVariation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(this.maxAbsoluteVariation);
		result = prime * result + (int) (temp ^ temp >>> 32);
		return result;
	}

	@Override
	public String toString() {
		return "AbsoluteNumberDiffFunction [maxAbsoluteVariation=" + this.maxAbsoluteVariation + "]";
	}

}
