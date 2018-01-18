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
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dude.util.data.Jsonable;
import dude.util.data.json.DuDeJsonParser;
import dude.util.data.json.JsonArray;
import dude.util.data.json.JsonRecord;
import dude.util.data.json.JsonString;
import dude.util.data.json.JsonValue;

/**
 * The class <code>TextBasedSubkey</code> provides the functionality for generating sub-keys based on String values. By specifying the
 * {@link #ignoredCharactersRegEx} the considered character types can be set.
 * 
 * @author Matthias Pohl
 * @author Arvid Heise
 */
public class TextBasedSubkey extends AbstractSubkey {

	/**
	 * This regular expression can be used if vocals should be ignored.
	 */
	public static final String NO_VOWELS_REGEX = "[AaEeIiOoUu]";

	private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
	private Locale locale = TextBasedSubkey.DEFAULT_LOCALE;

	private boolean caseSensitivityEnabled = true;
	private String ignoredCharactersRegEx = "";

	/**
	 * Stores the position information.
	 */
	protected List<Integer> positions = new ArrayList<Integer>();

	/**
	 * The property stores the information whether the suffix shall is requested.
	 */
	protected boolean tillEnd = true;

	/**
	 * An empty constructor for supporting {@link Jsonable#fromJson(DuDeJsonParser)}.
	 */
	protected TextBasedSubkey() {
		super();
	}

	/**
	 * Initializes this subkey with the passed attribute name.
	 * 
	 * @param attrName
	 *            The name of the attribute that shall be used for generating the subkey.
	 */
	public TextBasedSubkey(String attrName) {
		this(attrName, "");
	}

	/**
	 * Initializes this subkey with the passed attribute name and the specified ignoredCharacters regular expression.
	 * 
	 * @param attrName
	 *            The name of the attribute that shall be used for generating the subkey.
	 * @param ignoreRegex
	 *            A regular expression that describes all the characters that shall be ignored in this subkey instance.
	 */
	public TextBasedSubkey(String attrName, String ignoreRegex) {
		super(attrName);

		this.setRange(0);
		this.setIgnoredCharactersRegEx(ignoreRegex);
	}

	/**
	 * Sets the positions that shall be taken into account explicitly.
	 * 
	 * @param positions
	 *            The positions that shall be taken into account.
	 * @throws NullPointerException
	 *             If <code>null</code> was passed.
	 */
	public void setPositions(Integer... positions) {
		this.tillEnd = false;
		this.positions.clear();
		for (Integer pos : positions) {
			if (pos == null) {
				throw new NullPointerException();
			}

			this.positions.add(pos);
		}
	}

	/**
	 * Sets the range that shall be taken into account. The range includes the whole substring beginning at the passed <code>beginIndex</code>.
	 * 
	 * @param beginIndex
	 *            The first position.
	 */
	public void setRange(int beginIndex) {
		this.setPositions(beginIndex);
		this.tillEnd = true;
	}

	/**
	 * Sets the range that shall be taken into account.
	 * 
	 * @param beginIndex
	 *            The first position.
	 * @param length
	 *            The range's length.
	 * @throws IllegalArgumentException
	 *             If a negative length was passed.
	 */
	public void setRange(int beginIndex, int length) {
		if (length < 0) {
			throw new IllegalArgumentException("The length parameter has to be a positive number.");
		}

		int realLength = length;

		// the realLength might vary if the passed length is longer than the reversed beginIndex (e.g. setRange(-2, 3) would generate [-2, -1, 0];
		// where 0 is an invalid position)
		if (beginIndex < 0 && -beginIndex < length) {
			realLength = -beginIndex;
		}

		Integer[] positions = new Integer[realLength];
		int pos = beginIndex;

		for (int i = 0; i < realLength; i++) {
			positions[i] = pos++;
		}

		this.setPositions(positions);
	}

	/**
	 * Returns an iterable instance that stores all specified character positions.
	 * 
	 * @return An iterable instance that stores all specified character positions.
	 */
	public Iterable<Integer> getPositions() {
		return this.positions;
	}

	/**
	 * Sets the regular expression which specifies the characters that will be ignored.
	 * 
	 * @param regEx
	 *            The regular expression for specifying which characters shall be ignored.
	 */
	public void setIgnoredCharactersRegEx(String regEx) {
		if (regEx == null) {
			this.ignoredCharactersRegEx = "";
		} else {
			this.ignoredCharactersRegEx = regEx;
		}
	}

	/**
	 * Returns the regular expression that specifies which character classes are ignored within the current instance.
	 * 
	 * @return The regular expression specifying all ignorable characters.
	 */
	public String getIgnoreRegex() {
		return this.ignoredCharactersRegEx;
	}

	/**
	 * Enables case-sensitivity for the comparisons. This means that 'a' and 'A' will be unequal.
	 */
	public void enableCaseSensitivity() {
		this.caseSensitivityEnabled = true;
	}

	/**
	 * Disables case-sensitivity for the comparisons. This means that 'A' will be converted into 'a' so that 'A' and 'a' are the same. In order to
	 * provide a valid transformation to lower case letters {@link Locale} can be specified. If <code>null</code> is passed, English will be used as
	 * the default language.
	 * 
	 * @param locale
	 *            Specifies the underlying language.
	 */
	public void disableCaseSensitivity(Locale locale) {
		this.caseSensitivityEnabled = false;
		this.locale = locale;
	}

	/**
	 * Disables case-sensitivity for the comparisons. This means that 'A' will be converted into 'a' so that 'A' and 'a' are the same. For
	 * transforming capital letters to lower case letters, the rules of {@link #DEFAULT_LOCALE} are used.
	 */
	public void disableCaseSensitivity() {
		this.disableCaseSensitivity(TextBasedSubkey.DEFAULT_LOCALE);
	}

	/**
	 * Checks if case-sensitivity is enabled.
	 * 
	 * @return <code>true</code>, if case-sensitivity is enabled; otherwise <code>false</code>.
	 */
	public boolean caseSensitivityEnabled() {
		return this.caseSensitivityEnabled;
	}

	@Override
	protected int compareJsonValues(JsonValue val1, JsonValue val2) throws ClassCastException {
		if (val1.getType().compareTo(val2.getType()) != 0) {
			return val1.getType().compareTo(val2.getType());
		}

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

			// if arr1 is a subset of arr2 it is "smaller" than arr2
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

			// the records can't be compared since they have no keys in common or these keys refer to the same values
			return 0;
		case Null:
			return 0;
		case Boolean:
		case Number:
		case String:
		default:
			String strVal1 = val1.toString();
			String strVal2 = val2.toString();

			// transform all capital letters into lower letters
			if (!this.caseSensitivityEnabled()) {
				strVal1 = strVal1.toLowerCase(this.locale);
				strVal2 = strVal2.toLowerCase(this.locale);
			}
			
			if (!"".equals(this.getIgnoreRegex())) {
				strVal1 = strVal1.replaceAll(this.getIgnoreRegex(), "");
				strVal2 = strVal2.replaceAll(this.getIgnoreRegex(), "");
			}

			// extract char arrays
			final char[] chArray1 = strVal1.toCharArray();
			final char[] chArray2 = strVal2.toCharArray();

			// no offset is set -> check the arrays till the end
			if (this.tillEnd) {
				final int startPos = this.getFirstPosition();

				int startPos1 = startPos;
				int startPos2 = startPos;

				if (startPos < 0) {
					startPos1 = chArray1.length + startPos;
					startPos2 = chArray2.length + startPos;
				}

				if (startPos1 < 0) {
					startPos1 = 0;
				}

				if (startPos2 < 0) {
					startPos2 = 0;
				}

				if (chArray1.length <= startPos1 && chArray2.length <= startPos2) {
					return 0;
				} else if (chArray1.length <= startPos1 || chArray2.length <= startPos2) {
					// empty arrays are in front
					return chArray1.length - chArray2.length;
				}

				for (; startPos1 < chArray1.length && startPos2 < chArray2.length; startPos1++, startPos2++) {
					if (chArray1[startPos1] != chArray2[startPos2]) {
						return chArray1[startPos1] - chArray2[startPos2];
					}
				}

				int rest1 = chArray1.length - startPos1;
				int rest2 = chArray2.length - startPos2;
				
				return rest1 - rest2;
			}

			for (Integer pos : this.positions) {
				int pos1 = pos;
				int pos2 = pos;

				if (pos < 0) {
					// negative values addresses characters in reverse order (last character: -1)
					pos1 = chArray1.length + pos;
					pos2 = chArray2.length + pos;
				}

				// positive values addresses characters in normal order (first character: 0)
				if (pos1 >= 0 && pos1 < chArray1.length && pos2 >= 0 && pos2 < chArray2.length) {
					if (chArray1[pos1] != chArray2[pos2]) {
						return chArray1[pos1] - chArray2[pos2];
					}

					// if both characters are the same continue with the next character
				} else if ((pos1 < chArray1.length && pos2 >= chArray2.length) || (pos1 >= 0 && pos2 < 0)) {
					// no character on this position in chArray2 -> the second value is smaller
					return 1;
				} else if ((pos1 >= chArray1.length && pos2 < chArray2.length) || (pos1 < 0 && pos2 >= 0)) {
					// no character on this position in chArray1 -> the first value is smaller
					return -1;
				} else {
					// pos1 >= chArray1.length && pos2 >= chArray2.length
					// both arrays have no character at this position -> continue iteration
				}
			}

			// all characters are the same
			return 0;
		}
	}

	@Override
	protected void collectRelevantValues(JsonArray array, JsonValue value) {
		switch (value.getType()) {
		case Array:
			for (JsonValue arrayElement : (JsonArray) value) {
				this.collectRelevantValues(array, arrayElement);
			}

			break;
		case Record:
			JsonRecord record = (JsonRecord) value;
			for (Map.Entry<String, JsonValue> entry : record.entrySet()) {
				this.collectRelevantValues(array, entry.getValue());
			}

			break;
		case Boolean:
		case Number:
		case Null:
		case String:
			String str = value.toString();

			if (str == null) {
				// do nothing
				return;
			}

			if (!"".equals(this.getIgnoreRegex())) {
				str = str.replaceAll(this.getIgnoreRegex(), "");
			}

			// empty string will be larger than any number
			if ("".equals(str)) {
				array.add(new JsonString());
				return;
			}

			// transform all capital letters into lower letters
			if (!this.caseSensitivityEnabled()) {
				str = str.toLowerCase(this.locale);
			}

			if (this.tillEnd) {
				int actualFirstPos = this.getFirstPosition();

				if (this.getFirstPosition() < 0) {
					actualFirstPos = str.length() + this.getFirstPosition();
				}

				if (actualFirstPos < 0) {
					array.add(new JsonString(str));
				} else if (actualFirstPos < str.length()) {
					array.add(new JsonString(str.substring(actualFirstPos)));
				} else {
					array.add(new JsonString());
				}

				return;
			}

			char[] chArray = str.toCharArray();

			StringBuilder strBuilder = new StringBuilder();
			for (Integer pos : this.positions) {
				int actualPos = pos;

				if (pos < 0) {
					// negative values addresses characters in reverse order (last character: -1)
					actualPos = chArray.length + pos;
				}

				// positive values addresses characters in normal order (first character: 0)
				if (actualPos >= 0 && actualPos < chArray.length) {
					strBuilder.append(chArray[actualPos]);
				} else {
					// TODO: currently all the invalid positions are ignored -> desired behavior?
				}
			}

			array.add(new JsonString(strBuilder.toString()));
		}
	}

	/**
	 * Returns the first set position or <code>0</code> if no position is set.
	 * 
	 * @return The first position that is set.
	 */
	protected int getFirstPosition() {
		return this.positions.size() > 0 ? this.positions.get(0) : 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (this.caseSensitivityEnabled ? 1231 : 1237);
		result = prime * result + ((this.ignoredCharactersRegEx == null) ? 0 : this.ignoredCharactersRegEx.hashCode());
		result = prime * result + ((this.locale == null) ? 0 : this.locale.hashCode());
		result = prime * result + ((this.positions == null) ? 0 : this.positions.hashCode());
		result = prime * result + (this.tillEnd ? 1231 : 1237);
		return result;
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

		TextBasedSubkey other = (TextBasedSubkey) obj;
		if (this.caseSensitivityEnabled != other.caseSensitivityEnabled) {
			return false;
		}

		if (this.ignoredCharactersRegEx == null) {
			if (other.ignoredCharactersRegEx != null) {
				return false;
			}
		} else if (!this.ignoredCharactersRegEx.equals(other.ignoredCharactersRegEx)) {
			return false;
		}

		if (this.locale == null) {
			if (other.locale != null) {
				return false;
			}
		} else if (!this.locale.equals(other.locale)) {
			return false;
		}

		if (this.positions == null) {
			if (other.positions != null) {
				return false;
			}
		} else if (!this.positions.equals(other.positions)) {
			return false;
		}

		if (this.tillEnd != other.tillEnd) {
			return false;
		}

		return true;
	}

}
