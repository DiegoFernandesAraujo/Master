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

import org.apache.log4j.Logger;

import dude.util.data.DuDeObject;
import dude.util.data.Jsonable;
import dude.util.data.json.JsonArray;
import dude.util.data.json.JsonBoolean;
import dude.util.data.json.JsonNumber;
import dude.util.data.json.JsonRecord;
import dude.util.data.json.JsonValue;

/**
 * <code>NumberBasedSubkey</code> can be used for number-based sub-keys. All {@link JsonNumber} values are supported. In
 * order to provide additional support for all other atomic values they are transformed in some way into numbers.
 * <ul>
 * <li><code>JsonNull</code>: A null value will be transformed into <code>0</code></li>
 * <li><code>JsonBoolean</code>:
 * <ul>
 * <li><code>true</code> are transformed into <code>1</code></li>
 * <li><code>false</code> are transformed into <code>0</code></li>
 * </ul>
 * </li>
 * <li><code>JsonString</code>: All non-number characters are removed. The String that is left will be interpreted
 * as the number.</li>
 * </ul>
 * All non-atomic values (<code>JsonArray</code> and <code>JsonRecord</code>) are recursively traversed.
 * 
 * @author Matthias Pohl
 * @author Arvid Heise
 */
public class NumberBasedSubkey extends TextBasedSubkey {

	private static final Logger logger = Logger.getLogger(NumberBasedSubkey.class.getPackage().getName());

	private static final String ALL_BUT_DIGITS_REGEX = "[^\\d]";

	/**
	 * Internal constructor for {@link Jsonable} deserialization.
	 */
	protected NumberBasedSubkey() {
		// nothing to do
	}
	
	/**
	 * Initializes a <code>NumberBasedSubkey</code> instance that takes all digits within the value.
	 * 
	 * @param attrName
	 *            The name of the attribute that shall be used for comparing the {@link DuDeObject}s.
	 */
	public NumberBasedSubkey(String attrName) {
		super(attrName);
		
		this.setIgnoredCharactersRegEx(NumberBasedSubkey.ALL_BUT_DIGITS_REGEX);
	}

	/**
	 * Initializes a <code>NumberBasedSubkey</code> instance that takes all digits within the value.
	 * <code>firstCharacter</code> specifies the index (starting at 0) of the first digit that will be considered.
	 * 
	 * @param attrName
	 *            The name of the attribute that shall be used for comparing the {@link DuDeObject}s.
	 * @param firstDigit
	 *            The index of the first digit that shall be considered. If this value is a negative one, the counting
	 *            starts from the end of the value (-1 means the last digit).
	 */
	public NumberBasedSubkey(String attrName, int firstDigit) {
		super(attrName);
		
		this.setIgnoredCharactersRegEx(NumberBasedSubkey.ALL_BUT_DIGITS_REGEX);
		this.setRange(firstDigit);
	}

	/**
	 * Initializes a <code>NumberBasedSubkey</code> instance that takes all digits within the value.
	 * <code>firstCharacter</code> specifies the index (starting at 0) of the first digit that will be considered.
	 * <code>length</code> specifies the considered offset.
	 * 
	 * @param attrName
	 *            The name of the attribute that shall be used for comparing the {@link DuDeObject}s.
	 * @param firstDigit
	 *            The index of the first digit that shall be considered. If this value is a negative one, the counting
	 *            starts from the end of the value (-1 means the last digit).
	 * @param length
	 *            The considered offset.
	 */
	public NumberBasedSubkey(String attrName, int firstDigit, int length) {
		super(attrName);

		this.setIgnoredCharactersRegEx(NumberBasedSubkey.ALL_BUT_DIGITS_REGEX);
		this.setRange(firstDigit, length);
	}

	/**
	 * Initializes a <code>NumberBasedSubkey</code> instance that takes all digits within the value. <code>pos</code>
	 * can be used to specify which concrete digit shall be considered.
	 * 
	 * @param attrName
	 *            The name of the attribute that shall be used for comparing the {@link DuDeObject}s.
	 * @param pos
	 *            An Integer array that is used for specifying the digits that shall be considered.
	 */
	public NumberBasedSubkey(String attrName, Integer[] pos) {
		super(attrName);
		
		this.setIgnoredCharactersRegEx(NumberBasedSubkey.ALL_BUT_DIGITS_REGEX);
		this.setPositions(pos);
	}

	@Override
	protected int compareJsonValues(JsonValue val1, JsonValue val2) {
		if (val1.getType().compareTo(val2.getType()) != 0) {
			return val1.getType().compareTo(val2.getType());
		}

		// are used for the atomic type conversion
		String strVal1, strVal2;

		switch (val1.getType()) {
		case Array:
			JsonArray arr1 = (JsonArray) val1;
			JsonArray arr2 = (JsonArray) val2;
			if (arr1.size() < arr2.size()) {
				for (int i = 0; i < arr1.size(); ++i) {
					int compareValue = this.compareJsonValues(arr1.get(i), arr2.get(i));

					if (compareValue != 0) {
						return compareValue;
					}
				}
			} else {
				for (int i = 0; i < arr2.size(); ++i) {
					int compareValue = this.compareJsonValues(arr1.get(i), arr2.get(i));

					if (compareValue != 0) {
						return compareValue;
					}
				}
			}

			// if arr1 is a subset of arr2 it is "smaller" that arr2
			return arr1.size() - arr2.size();
		case Record:
			JsonRecord record1 = (JsonRecord) val1;
			JsonRecord record2 = (JsonRecord) val2;
			if (record1.keySet().size() < record2.keySet().size()) {
				for (String key : record1.keySet()) {
					if (record2.containsKey(key)) {
						int compareValue = this.compareJsonValues(record1.get(key), record2.get(key));

						if (compareValue != 0) {
							return compareValue;
						}
					}
				}
			} else {
				for (String key : record2.keySet()) {
					if (record1.containsKey(key)) {
						int compareValue = this.compareJsonValues(record1.get(key), record2.get(key));

						if (compareValue != 0) {
							return compareValue;
						}
					}
				}
			}

			// records can't be compared since they have no keys in common or these keys refer to the same values
			return 0;
		case Null:
			// null is represented by 0
			return 0;
		case Boolean:
			// for false (converted to 0) < true (converted to 1)
			strVal1 = ((JsonBoolean) val1).getValue() ? "1" : "0";
			strVal2 = ((JsonBoolean) val2).getValue() ? "1" : "0";
			break;
		case Number:
		case String:
		default:
			strVal1 = val1.toString();
			strVal2 = val2.toString();
		}

		Long longVal1 = this.convertString(strVal1);
		Long longVal2 = this.convertString(strVal2);

		if (longVal1 == null && longVal2 != null) {
			return -1;
		} else if (longVal1 != null && longVal2 == null) {
			return 1;
		} else if (longVal1 == null && longVal2 == null) {
			return 0;
		}

		if (longVal1 < longVal2) {
			return -1;
		} else if (longVal1 > longVal2) {
			return 1;
		} else {
			return 0;
		}
	}

	private Long convertString(String val) {
		if (val == null) {
			return null;
		}
		
		String strVal = val.replaceAll(this.getIgnoreRegex(), "");
		
		if ("".equals(strVal)) {
			return null;
		}

		if (this.tillEnd) {
			if (strVal.length() <= this.getFirstPosition()) {
				return null;
			}
			
			int actualFirstPos = this.getFirstPosition();
			
			if (actualFirstPos < 0) {
				// add firstPosition since it is negative
				actualFirstPos = strVal.length() + this.getFirstPosition();
				
				if (actualFirstPos < 0) {
					return new Long(strVal);
				}
			}
			
			return new Long(strVal.substring(actualFirstPos));
		}

		char[] chArray = strVal.toCharArray();

		StringBuilder strBuilder = new StringBuilder();
		for (Integer pos : this.positions) {
			int actualPos = pos;

			if (pos < 0) {
				// negative values addresses characters in reverse order (last character: -1)
				actualPos = chArray.length + pos;
			}

			// positive values addresses characters in normal order (first character: 0)
			if (actualPos < chArray.length) {
				strBuilder.append(chArray[actualPos]);
			} else {
				// TODO: currently all the invalid positions are ignored -> desired behavior?
			}
		}

		try {
			return Long.parseLong(strBuilder.toString());
		} catch (NumberFormatException e) {
			NumberBasedSubkey.logger.warn("String could not be converted into a Long value: "
					+ strBuilder.toString());
			return null;
		}
	}

	@Override
	protected void collectRelevantValues(JsonArray collectedValues, JsonValue value) {
		String strVal;
		switch (value.getType()) {
		case Array:
			for (JsonValue arrayElement : (JsonArray) value) {
				this.collectRelevantValues(collectedValues, arrayElement);
			}

			return;
		case Record:
			JsonRecord record = (JsonRecord) value;
			for (String key : record.keySet()) {
				this.collectRelevantValues(collectedValues, record.get(key));
			}

			return;
		case Null:
			// null is represented by 0
			strVal = "0";
			break;
		case Boolean:
			// for false (converted to 0) < true (converted to 1)
			strVal = ((JsonBoolean) value).getValue() ? "1" : "0";
			break;
		case Number:
		case String:
		default:
			strVal = value.toString();
		}

		Long longVal = this.convertString(strVal);

		if (longVal != null) {
			collectedValues.add(new JsonNumber(longVal));
		}
	}
	

	
}
