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
import dude.similarityfunction.contentbased.util.SoundEx;
import dude.util.data.DuDeObject;
import dude.util.data.Jsonable;
import dude.util.data.json.JsonAtomic;

/**
 * <code>SoundExFunction</code> compares two {@link DuDeObject}s based on the SoundEx values of the given attribute.
 * 
 * @author Matthias Pohl
 */
public class SoundExFunction extends ContentBasedSimilarityFunction<SoundExFunction> implements StringSimilarity {

	private final SoundEx soundExGenerator = new SoundEx();

	/**
	 * Internal constructor for {@link Jsonable} deserialization.
	 */
	protected SoundExFunction() {
		super();
	}
	
	/**
	 * Initializes a <code>SoundExFunction</code> with the passed default attribute.
	 * 
	 * @param defaultAttr
	 *            The default attribute that is used if no {@link DataSource} specific attribute was set.
	 */
	public SoundExFunction(String... defaultAttr) {
		super(defaultAttr);
	}

	/**
	 * Initializes a <code>SoundExFunction</code> with the passed default attribute.
	 * 
	 * @param attrIndex
	 *            The index of the default attribute. This parameter is used to select specific values of an array.
	 * @param defaultAttr
	 *            The default attribute that is used if no {@link DataSource} specific attribute was set.
	 */
	public SoundExFunction(int attrIndex, String... defaultAttr) {
		super(attrIndex, defaultAttr);
	}

	@Override
	protected double compareAtomicValues(JsonAtomic value1, JsonAtomic value2) {
		return this.getSimilarity(value1.toString(), value2.toString());
	}

	@Override
	public double getSimilarity(String str1, String str2) {
		final String soundEx1 = this.soundExGenerator.getSoundEx(str1);
		final String soundEx2 = this.soundExGenerator.getSoundEx(str2);
		
		if (soundEx1 == null) {
			return 0.0;
		}
		
		return soundEx1.equals(soundEx2) ? 1.0 : 0.0;
	}

}
