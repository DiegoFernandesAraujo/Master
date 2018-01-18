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

package dude.util.sorting.sortingkey;

import java.util.Comparator;

import dude.util.data.AutoJsonable;
import dude.util.data.DuDeObject;
import dude.util.data.json.JsonArray;

/**
 * <code>Subkey</code> is an <code>interface</code> that is used within the {@link SortingKey} implementation. Each <code>Subkey</code> defines a part
 * of this sorting key.
 * 
 * @author Matthias Pohl
 * @author Arvid Heise
 */
public interface Subkey extends Comparator<DuDeObject>, AutoJsonable {

	/**
	 * Returns a {@link JsonArray} that collects all relevant values for the subkey of the passed {@link DuDeObject}.
	 * 
	 * @param obj
	 *            The object from which the sub-key shall be returned.
	 * @return The <code>JsonArray</code> collecting all relevant values of the sub-key.
	 */
	public JsonArray getSubkeyValue(DuDeObject obj);
}
