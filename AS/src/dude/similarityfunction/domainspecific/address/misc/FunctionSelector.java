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

package dude.similarityfunction.domainspecific.address.misc;

import dude.similarityfunction.contentbased.ContentBasedSimilarityFunction;
import dude.similarityfunction.contentbased.impl.simmetrics.JaccardSimilarityFunction;
import dude.similarityfunction.domainspecific.address.CitySimilarityFunction;
import dude.similarityfunction.domainspecific.address.DateSimilarityFunction;
import dude.similarityfunction.domainspecific.address.FamilyNameSimilarityFunction;
import dude.similarityfunction.domainspecific.address.GivenNameSimilarityFunction;
import dude.similarityfunction.domainspecific.address.HonorificSimilarityFunction;
import dude.similarityfunction.domainspecific.address.HouseNumberSimilarityFunction;
import dude.similarityfunction.domainspecific.address.PhoneNumberSimilarityFunction;
import dude.similarityfunction.domainspecific.address.StreetSimilarityFunction;
import dude.similarityfunction.domainspecific.address.TitleSimilarityFunction;
import dude.similarityfunction.domainspecific.address.ZIPSimilarityFunction;

/**
 * maps from a class/datatype/semantics name to similarity function
 * @author Tobias Vogel
 *
 */
public class FunctionSelector {

	/**
	 * returns the class object of the similarity function represented by the provided string
	 * @param classname the (informal) string representation of the similarity function
	 * @return the class object of the desired similarity function or null, if it is unknown
	 */
	public static Class<? extends ContentBasedSimilarityFunction<?>> getSimilarityFunctionForClassAsString(String classname) {
		Class<? extends ContentBasedSimilarityFunction<?>> result;
		
		if (classname.equals("GIVENNAME")) result = GivenNameSimilarityFunction.class;
		else if (classname.equals("ZIP")) result = ZIPSimilarityFunction.class;
		else if (classname.equals("HONORIFIC")) result = HonorificSimilarityFunction.class;
		else if (classname.equals("TITLE")) result = TitleSimilarityFunction.class;
		else if (classname.equals("FAMILYNAME")) result = FamilyNameSimilarityFunction.class;
		else if (classname.equals("DATE")) result = DateSimilarityFunction.class;
		else if (classname.equals("STREET_DE")) result = StreetSimilarityFunction.class;
		else if (classname.equals("HOUSENUMBER")) result = HouseNumberSimilarityFunction.class;
		else if (classname.equals("CITY_DE")) result = CitySimilarityFunction.class;
		else if (classname.equals("PHONE")) result = PhoneNumberSimilarityFunction.class;
		else if (classname.equals("OCCUPATION")) result = JaccardSimilarityFunction.class;
		else result = null;
		return result;
	}
}