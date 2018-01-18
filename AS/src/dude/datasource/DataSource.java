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

package dude.datasource;

import dude.util.Cleanable;
import dude.util.data.AutoJsonable;
import dude.util.data.DuDeObject;
import dude.util.data.storage.JsonableReader;

/**
 * <code>DataSource</code> is used for extracting data out of different data sources.
 * 
 * @author Matthias Pohl
 */
public interface DataSource extends JsonableReader<DuDeObject>, Cleanable, AutoJsonable {

	/**
	 * Sets the attributes that shall be used for id generation. If no id attributes are set, the object ids are generated automatically.
	 * 
	 * @param idAttributes
	 *            The attributes that shall be used for id generation.
	 */
	public void addIdAttributes(String... idAttributes);

	/**
	 * Returns the identifier of this <code>DataSource</code>. This identifier is used as a source id for each {@link DuDeObject} that is extracted
	 * out of the current <code>DataSource</code> instance.
	 * 
	 * @return The <code>DataSource</code>'s identifier.
	 */
	public String getIdentifier();

	/**
	 * Returns the number of already extracted records.
	 * 
	 * @return The number of already extracted records.
	 */
	public int getExtractedRecordCount();
}
