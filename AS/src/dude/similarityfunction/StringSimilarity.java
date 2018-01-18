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

package dude.similarityfunction;

/**
 * <code>StringSimilarity</code> is an interface for comparing Strings.
 * 
 * @author Matthias Pohl
 * 
 */
public interface StringSimilarity {

	/**
	 * Returns the similarity of the passed Strings, where <code>0.0</code> means that Strings are completely different, and <code>1.0</code>
	 * indicates that the passed Strings are the same.
	 * 
	 * @param str1
	 *            The first String.
	 * @param str2
	 *            The second String.
	 * @return Returns the similarity of the passed Strings or 0.0, if <code>null</code> was passed for at least one String.
	 */
	public double getSimilarity(String str1, String str2);
}
