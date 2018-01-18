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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import dude.datasource.DataSource;
import dude.util.data.DuDeObject;
import dude.util.data.json.JsonArray;
import dude.util.data.json.JsonRecord;
import dude.util.data.json.JsonValue;
import dude.util.data.json.JsonValue.JsonType;

/**
 * <code>AbstractSubkey</code> is an <code>abstract</code> class that should be extended by each subkey class. It implements {@link Subkey}
 * particularly.
 * 
 * @author Matthias Pohl
 * @author Arvid Heise
 */
public abstract class AbstractSubkey implements Subkey {

	private String[] defaultAttribute;
	private Map<DataSource, String[]> attributes = new HashMap<DataSource, String[]>();

	/**
	 * Initializes a <code>AbstractSubkey</code> instance with the given default attribute.
	 * 
	 * @param defaultAttr
	 *            The default attribute that is used for generating a sub-key.
	 */
	public AbstractSubkey(String... defaultAttr) {
		if (defaultAttr.length > 0) {
			this.setDefaultAttribute(defaultAttr);
		}
	}

	/**
	 * Sets the default attribute. This attribute is used, if no specific attribute was set for a given {@link DataSource}. Passing an empty attribute
	 * or <code>null</code> will unset the current default attribute.
	 * 
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public void setDefaultAttribute(String... defaultAttr) {
		if (defaultAttr.length < 1) {
			this.defaultAttribute = null;
		}

		this.defaultAttribute = defaultAttr;
	}

	/**
	 * Sets the attribute for the passed {@link DataSource}. Passing <code>null</code> instead of an attribute will remove the attribute of the passed
	 * <code>DataSource</code> from this <code>Subkey</code>.
	 * 
	 * @param source
	 *            The <code>DataSource</code>.
	 * @param attribute
	 *            The corresponding attribute.
	 * @throws NullPointerException
	 *             If the <code>null</code> was passed instead of a <code>DataSource</code>.
	 */
	public void setAttribute(DataSource source, String... attribute) {
		if (source == null) {
			throw new NullPointerException("The DataSource is missing.");
		}

		if (attribute == null) {
			// unsets the attribute of the passed DataSource
			this.attributes.remove(source);
			return;
		}

		this.attributes.put(source, attribute);
	}

	/**
	 * Returns the attribute that corresponds to source of the passed {@link DuDeObject}.
	 * 
	 * @param obj
	 *            The <code>DuDeObject</code>.
	 * @return The corresponding attribute.
	 */
	protected String[] getAttribute(DuDeObject obj) {
		for (Map.Entry<DataSource, String[]> entry : this.attributes.entrySet()) {
			if (entry.getKey().getIdentifier().equals(obj.getSourceId())) {
				return entry.getValue();
			}
		}

		if (this.defaultAttribute == null) {
			throw new IllegalStateException("No default attribute was set.");
		}

		return this.defaultAttribute;
	}

	/**
	 * Collects all relevant values and put them into the passed {@link JsonArray}.
	 * 
	 * @param array
	 *            The <code>JsonArray</code> that shall be filled.
	 * @param value
	 *            The {@link JsonValue} that shall be traversed. All relevant values will be extracted out of this instance.
	 */
	protected abstract void collectRelevantValues(JsonArray array, JsonValue value);

	@Override
	public int compare(DuDeObject o1, DuDeObject o2) {
		final String[] attr1 = this.getAttribute(o1);
		final String[] attr2 = this.getAttribute(o2);

		final JsonValue value1 = o1.getAttributeValuesByPath(attr1);
		final JsonValue value2 = o2.getAttributeValuesByPath(attr2);

		// attribute does not exist
		if (value1 == null && value2 == null) {
			return 0;
		} else if (value1 == null) {
			return -1;
		} else if (value2 == null) {
			return 1;
		}

		if (value1.equals(value2)) {
			return 0;
		}

		return this.compareJsonValues(value1, value2);
	}

	/**
	 * Executes the text-based comparison for each Json type. Therefore all atomic {@link JsonValue}s are transformed into their String
	 * representation. The Json types storing multiple <code>JsonValues</code> ( {@link JsonRecord} and {@link JsonArray}) are traversed. If the
	 * passed {@link JsonValue}s are of different types, the order of the {@link JsonType}s is used.
	 * 
	 * @param val1
	 *            The first <code>JsonValue</code>.
	 * @param val2
	 *            The second <code>JsonValue</code>.
	 * @return <code>-1</code> if <code>val1</code> is less than <code>val2</code>; <code>1</code> if <code>val1</code> is larger than
	 *         <code>val2</code> and <code>0</code>, if they are equal.
	 * @throws ClassCastException
	 *             If at least one of the past values can't be converted.
	 */
	protected abstract int compareJsonValues(JsonValue val1, JsonValue val2) throws ClassCastException;

	/**
	 * Returns a {@link JsonArray} of all relevant Strings.
	 * 
	 * @param value
	 *            The value that shall be converted.
	 * @return A <code>JsonArray</code> of all relevant Strings.
	 */
	private JsonArray getRelevantValues(JsonValue value) {
		final JsonArray arr = new JsonArray();
		this.collectRelevantValues(arr, value);

		return arr;
	}

	@Override
	public JsonArray getSubkeyValue(DuDeObject obj) {
		final String[] attr = this.getAttribute(obj);
		final JsonValue value = obj.getAttributeValuesByPath(attr);

		if (value == null)
			return new JsonArray();

		return this.getRelevantValues(value);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.attributes == null) ? 0 : this.attributes.hashCode());
		result = prime * result + Arrays.hashCode(this.defaultAttribute);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AbstractSubkey other = (AbstractSubkey) obj;
		if (this.attributes == null) {
			if (other.attributes != null) {
				return false;
			}
		} else if (!this.attributes.equals(other.attributes)) {
			return false;
		}
		if (!Arrays.equals(this.defaultAttribute, other.defaultAttribute)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "AbstractSubkey [defaultAttribute=" + Arrays.toString(this.defaultAttribute) + "]";
	}

}
