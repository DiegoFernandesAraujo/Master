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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import dude.util.data.AutoJsonable;
import dude.util.data.DuDeObject;
import dude.util.data.json.JsonArray;
import dude.util.data.json.JsonAtomic;
import dude.util.data.json.JsonRecord;
import dude.util.data.json.JsonValue;

/**
 * The <code>SortingKey</code> collects different sub-keys and compares different {@link DuDeObject} based on these sub-keys. Each sub-key has to
 * implement {@link Comparator} based on <code>DuDeObjects</code>.
 * 
 * @author Matthias Pohl
 * @author Arvid Heise
 */
public class SortingKey implements Comparator<DuDeObject>, AutoJsonable {

	/**
	 * A collection of all sub-keys which are used within the sorting key.
	 */
	private final List<Subkey> subkeyList = new ArrayList<Subkey>();

	/**
	 * Initializes a <code>SortingKey</code> with no sub-key(s). Additional sub-keys can be added using the {@link #addSubkey(Subkey)} method. If no
	 * sub-key will be added all {@link DuDeObject}s that are compared based on that sorting key will be equal.
	 */
	// this constructor is not obsolete -> it is used for serialization/deserialization
	public SortingKey() {
		// nothing to do
	}

	/**
	 * Initializes a <code>SortingKey</code> instance. Calling this constructor may cause a compiler warning. The warning can be ignored.
	 * 
	 * @param subkeys
	 *            A list of sub-keys that shall be used within the sorting key. (Caution: the order within the parameter list matters!)
	 */
	public SortingKey(Subkey... subkeys) {
		this.subkeyList.addAll(Arrays.asList(subkeys));
	}

	/**
	 * Adds a sub-key to the <code>SortingKey</code>. (Caution: the order of calling addSubkey() matters!)
	 * 
	 * @param newSubkey
	 *            An additional sub-key.
	 * @return A reference to this object.
	 */
	public SortingKey addSubkey(Subkey newSubkey) {
		this.subkeyList.add(newSubkey);

		return this;
	}

	/**
	 * Compares two {@link DuDeObject}s.
	 * 
	 * @param record1
	 *            The first <code>DuDeObject</code> instance.
	 * @param record2
	 *            The second <code>DuDeObject</code> instance.
	 * @return The value <code>0</code>, if the first <code>DuDeObject</code> instance is equal to the second one; a value less than <code>0</code>,
	 *         if the first <code>DuDeObject</code> instance is lexicographically less than the second one depending on the sorting key; and a value
	 *         greater than <code>0</code>, if the first <code>DuDeObject</code> instance is lexicographically greater than the second one depending
	 *         on the sorting key.
	 */
	@Override
	public int compare(DuDeObject record1, DuDeObject record2) {
		for (final Subkey subkey : this.subkeyList) {
			final int compareValue = subkey.compare(record1, record2);
			if (compareValue != 0)
				return compareValue;
		}

		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		final SortingKey other = (SortingKey) obj;
		if (this.subkeyList == null) {
			if (other.subkeyList != null)
				return false;
		} else if (!this.subkeyList.equals(other.subkeyList))
			return false;
		return true;
	}

	/**
	 * Returns the sorting key value of the passed {@link DuDeObject}.
	 * 
	 * @param obj
	 *            The <code>DuDeObject</code> from which the sorting key shall be returned.
	 * @return The String representation of the sorting key that belongs to the passed <code>DuDeObject</code>. If multiple values are generating the
	 *         sorting key, they are concatenated without any split token (i.e. the split token is the empty String).
	 * 
	 * @see SortingKey#getKeyString(DuDeObject, String)
	 */
	public String getKeyString(DuDeObject obj) {
		return this.getKeyString(obj, "");
	}

	/**
	 * Returns the sorting key value of the passed {@link DuDeObject}. The returned String represents the key value that is used for positioning this
	 * object within a sorted collection.<br>
	 * Note: this method does not work well with numerical values
	 * 
	 * @param obj
	 *            The <code>DuDeObject</code> from which the sorting key shall be returned.
	 * @param splitToken
	 *            The token that shall be used for splitting all values.
	 * @return The String representation of the sorting key that belongs to the passed <code>DuDeObject</code>. *
	 * @see #getKeyValue(DuDeObject)
	 */
	public String getKeyString(DuDeObject obj, String splitToken) {
		JsonArray arr = this.getKeyValue(obj);

		StringBuilder strBuilder = new StringBuilder();
		this.getKeyString(strBuilder, arr, splitToken != null ? splitToken : "");

		return strBuilder.toString();
	}

	private void getKeyString(StringBuilder strBuilder, JsonValue value, String splitToken) {
		switch (value.getType()) {
		case Array:
			this.getKeyString(strBuilder, (JsonArray) value, splitToken);
			return;
		case Record:
			this.getKeyString(strBuilder, (JsonRecord) value, splitToken);
			return;
		default:
			this.getKeyString(strBuilder, (JsonAtomic) value);
			return;
		}
	}

	private void getKeyString(StringBuilder strBuilder, JsonAtomic value) {
		if (value.size() > 0) {
			strBuilder.append(value.getStringValue());
		}
	}

	private void getKeyString(StringBuilder strBuilder, JsonArray arr, String splitToken) {
		boolean first = true;
		for (JsonValue value : arr) {
			if (!first) {
				strBuilder.append(splitToken);
				first = false;
			}

			this.getKeyString(strBuilder, value, splitToken);
		}
	}

	private void getKeyString(StringBuilder strBuilder, JsonRecord rec, String splitToken) {
		boolean first = true;
		for (JsonValue value : rec.values()) {
			if (!first) {
				strBuilder.append(splitToken);
				first = false;
			}

			this.getKeyString(strBuilder, value, splitToken);
		}
	}

	/**
	 * Returns the sorting key value of the passed {@link DuDeObject}. The returned {@link JsonArray} is used for positioning this object within a
	 * sorted collection.
	 * 
	 * @param obj
	 *            The <code>DuDeObject</code> from which the sorting key shall be returned.
	 * @return The JsonArray representation of the sorting key that belongs to the passed <code>DuDeObject</code>. *
	 */
	public JsonArray getKeyValue(DuDeObject obj) {
		final JsonArray values = new JsonArray(this.subkeyList.size());
		for (final Subkey subkey : this.subkeyList)
			values.add(subkey.getSubkeyValue(obj));
		return values;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.subkeyList == null ? 0 : this.subkeyList.hashCode());
		return result;
	}

	/**
	 * Checks whether any {@link Subkey} was added.
	 * 
	 * @return <code>true</code>, if at least one <code>Subkey</code> was added; otherwise <code>false</code>.
	 */
	public boolean isEmpty() {
		return this.subkeyList.isEmpty();
	}

	@Override
	public String toString() {
		return "SortingKey [subkeys=" + this.subkeyList + "]";
	}

	// @Override
	// public void fromJson(DuDeJsonParser<?> jsonParser) throws JsonParseException, IOException {
	// this.subkeyList.clear();
	//
	// jsonParser.skipToken(); // Record start
	// jsonParser.skipToken(); // "subkeys"
	// jsonParser.skipToken(); // Array start
	// while (jsonParser.currentToken() != JsonToken.END_ARRAY) {
	// try {
	// jsonParser.skipToken(); // Record start
	// jsonParser.skipToken(); // "type"
	// Subkey subkey = (Subkey) ReflectUtil.newInstance(Class.forName(jsonParser.nextJsonString().toString()));
	// jsonParser.skipToken(); // "properties"
	// jsonParser.skipToken(); // Record start
	// subkey.fromJson(jsonParser);
	// jsonParser.skipToken(); // Record end
	// jsonParser.skipToken(); // Record end
	// this.subkeyList.add(subkey);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// jsonParser.skipToken(); // Record end
	// }

	// @Override
	// public void toJson(DuDeJsonGenerator jsonGenerator) throws JsonGenerationException, IOException {
	// jsonGenerator.writeRecordStart();
	// jsonGenerator.writeRecordFieldName("subkeys");
	// jsonGenerator.writeArrayStart();
	// for (Subkey subkey : this.subkeyList) {
	// jsonGenerator.writeRecordStart();
	// jsonGenerator.writeStringRecordEntry("type", subkey.getClass().getName());
	// jsonGenerator.writeRecordFieldName("properties");
	// jsonGenerator.writeRecordStart();
	// subkey.toJson(jsonGenerator);
	// jsonGenerator.writeRecordEnd();
	// jsonGenerator.writeRecordEnd();
	// }
	// jsonGenerator.writeArrayEnd();
	// jsonGenerator.writeRecordEnd();
	// }
}
