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

import dude.datasource.DataSource;
import dude.util.data.DuDeObject;

/**
 * <code>Preprocessor</code> is an <code>interface</code> that can be used for gathering statistics of the data
 * within the extraction phase.
 * 
 * @author Matthias Pohl
 */
public interface Preprocessor {
	//TODO: implement method for manipulating (i.e., normalizing) data (if it is done here, it is done only once instead of doing it over and over again in a similarity function)

	/**
	 * Passes the currently extracted {@link DuDeObject} to the <code>Preprocessor</code> for further analysis. This
	 * method is called by every {@link DataSource} per extracted data record.
	 * 
	 * @param data
	 *            The <code>DuDeObject</code> that shall be analyzed.
	 */
	public void analyzeDuDeObject(final DuDeObject data);

	/**
	 * This method is called after finishing the data extraction process. It can be used in order to created some
	 * further statistics.
	 */
	public void finish();

	/**
	 * Clears statistics that were already gathered.
	 */
	public void clearData();

}
