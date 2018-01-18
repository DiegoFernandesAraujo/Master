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

package dude.similarityfunction.contentbased;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import dude.datasource.DataSource;
import dude.similarityfunction.AbstractSimilarityFunction;
import dude.similarityfunction.SimilarityFunction;
import dude.similarityfunction.contentbased.calculationstrategy.CalculationStrategy;
import dude.similarityfunction.contentbased.calculationstrategy.NotSupportedStrategy;
import dude.util.data.DuDeObject;
import dude.util.data.Jsonable;
import dude.util.data.json.JsonArray;
import dude.util.data.json.JsonAtomic;
import dude.util.data.json.JsonRecord;
import dude.util.data.json.JsonString;
import dude.util.data.json.JsonValue;
import dude.util.data.json.JsonValue.JsonType;

/**
 * <code>ContentBasedSimilarityFunction</code> is a skeleton implementation with common functionality that is used by any content-based
 * {@link SimilarityFunction}. These functions are based on the concrete values of an attribute.
 * 
 * @author Matthias Pohl
 * @param <T>
 *            The type of similarity function. This type is used as a return type for any fluent interface method.
 */
public abstract class ContentBasedSimilarityFunction<T extends ContentBasedSimilarityFunction<T>> extends AbstractSimilarityFunction {

	private String[] defaultAttribute;
	private int attributeIndex;
	private Map<String, String[]> attributes = new HashMap<String, String[]>();

	private CalculationStrategy<JsonArray, JsonArray> compareArrayArrayStrategy = new NotSupportedStrategy<JsonArray, JsonArray>();
	private CalculationStrategy<JsonArray, JsonAtomic> compareArrayAtomicStrategy = new NotSupportedStrategy<JsonArray, JsonAtomic>();
	private CalculationStrategy<JsonArray, JsonRecord> compareArrayRecordStrategy = new NotSupportedStrategy<JsonArray, JsonRecord>();

	private CalculationStrategy<JsonRecord, JsonRecord> compareRecordRecordStrategy = new NotSupportedStrategy<JsonRecord, JsonRecord>();
	private CalculationStrategy<JsonRecord, JsonAtomic> compareRecordAtomicStrategy = new NotSupportedStrategy<JsonRecord, JsonAtomic>();

	private boolean capitalizationIgnored;

	/**
	 * Internal constructor for {@link Jsonable} deserialization.
	 */
	protected ContentBasedSimilarityFunction() {
		super();
	}

	/**
	 * Initializes a <code>ContentBasedSimilarityFunction</code> with the passed default attribute. This attribute is used for any {@link DuDeObject}
	 * whose {@link DataSource} was not set with for a special attribute. If the requested attribute holds an array, the whole content will be
	 * compared.
	 * 
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public ContentBasedSimilarityFunction(String... defaultAttr) {
		this(-1, defaultAttr);
	}

	/**
	 * Initializes a <code>ContentBasedSimilarityFunction</code> with the passed default attribute. This attribute is used for any {@link DuDeObject}
	 * whose {@link DataSource} was not set with for a special attribute.
	 * 
	 * @param attrIndex
	 *            The index of the default attribute. This parameter is used to select specific values of an array, iff the selected attribute is an
	 *            array containing multiple values. The first index will be <code>0</code>. The whole array will be checked, if a value smaller than
	 *            <code>0</code> is passed.
	 * @param defaultAttr
	 *            The default attribute.
	 */
	public ContentBasedSimilarityFunction(int attrIndex, String... defaultAttr) {
		this.defaultAttribute = defaultAttr;
		this.attributeIndex = attrIndex;
		this.capitalizationIgnored = false;
	}

	/**
	 * Enables ignoring capitalization. No distinction will be made between upper case and lower case during similarity calculations.
	 * 
	 * @return The current instance.
	 */
	@SuppressWarnings("unchecked")
	public T ignoreCapitalization() {
		this.capitalizationIgnored = true;

		return (T) this;
	}

	/**
	 * Checks whether this <code>ContentBasedSimilarityFunction</code> shall make a distinction between lower case and upper case or not.
	 * 
	 * @return <code>true</code>, if the cases shall be ignored; otherwise <code>false</code>.
	 */
	protected boolean ignoringCapitalizationEnabled() {
		return this.capitalizationIgnored;
	}

	/**
	 * Adds a {@link DataSource}-related attribute to this <code>ContentBasedSimilarityFunction</code>.
	 * 
	 * @param source
	 *            The <code>DataSource</code> to which the passed attribute path belongs.
	 * @param attributePath
	 *            The path of the attribute. This path describes the location of the requested attribute (e.g. addAttribute(..., "dateOfBirth",
	 *            "year") describes the "year" attribute within the "dateOfBirth" attribute).
	 */
	public void addAttribute(DataSource source, String... attributePath) {
		if (source == null) {
			throw new NullPointerException("No DataSource was passed.");
		} else if (attributePath == null || attributePath.length < 1) {
			// no attribute was passed -> use default attribute for the passed DataSource
			return;
		}

		this.attributes.put(source.getIdentifier(), attributePath);
	}

	/**
	 * Returns the attribute path that is valid for the passed {@link DuDeObject}.
	 * 
	 * @param obj
	 *            The <code>DuDeObject</code> of which the attribute path shall be returned. This path depends on the source identifier of the passed
	 *            object.
	 * @return The corresponding attribute path.
	 */
	protected String[] getAttribute(DuDeObject obj) {
		final String srcId = obj.getSourceId();

		if (this.attributes.containsKey(srcId)) {
			return this.attributes.get(srcId);
		}

		if (this.defaultAttribute == null || this.defaultAttribute.length < 1) {
			throw new IllegalStateException("The default attribute is missing. (No special attribute was specified for the '" + srcId
					+ "' data source.)");
		}

		return this.defaultAttribute;
	}

	@Override
	protected double calculateSimilarity(DuDeObject obj1, DuDeObject obj2) {
		final String[] attrPath1 = this.getAttribute(obj1);
		final String[] attrPath2 = this.getAttribute(obj2);

		JsonValue value1 = obj1.getAttributeValuesByPath(attrPath1);
		JsonValue value2 = obj2.getAttributeValuesByPath(attrPath2);

		if (this.attributeIndex >= 0) {
			if (value1.getType() == JsonType.Array) {
				JsonArray array = (JsonArray) value1;
				if (array.size() > this.attributeIndex) {
					value1 = array.get(this.attributeIndex);
				} else {
					value1 = null;
				}
			}

			if (value2.getType() == JsonType.Array) {
				JsonArray array = (JsonArray) value2;
				if (array.size() > this.attributeIndex) {
					value2 = array.get(this.attributeIndex);
				} else {
					value2 = null;
				}
			}
		}

		if (value1 == null && value2 == null) {
			this.setValidationState(SimilarityValidationState.BothInvalid);
			return 0.0;
		} else if (value1 == null) {
			this.setValidationState(SimilarityValidationState.Value2ValidOnly);
			return 0.0;
		} else if (value2 == null) {
			this.setValidationState(SimilarityValidationState.Value1ValidOnly);
			return 0.0;
		}

		this.setValidationState(SimilarityValidationState.BothValid);

		return this.calculateSimilarity(value1, value2);
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

		if (this.getClass() != obj.getClass()) {
			return false;
		}

		@SuppressWarnings("rawtypes")
		ContentBasedSimilarityFunction other = (ContentBasedSimilarityFunction) obj;
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

	/**
	 * Calculates the similarity of the two passed {@link JsonValue}s.
	 * 
	 * @param val1
	 *            The first value.
	 * @param val2
	 *            The second value.
	 * @return The similarity of the two passed <code>JsonValue</code>s.
	 */
	public double calculateSimilarity(JsonValue val1, JsonValue val2) {
		JsonValue value1 = val1;
		JsonValue value2 = val2;
		
		if (value1.size() < 1 && value2.size() < 1) {
			return 1.0;
		} else if (value1.size() < 1) {
			// no elements is equal to an empty String
			value1 = new JsonString();
		} else if (value2.size() < 1) {
			// no elements is equal to an empty String
			value2 = new JsonString();
		}
		
		if (value1.getType() == JsonType.Array && value2.getType() == JsonType.Array) {
			return this.compareArrayArrayStrategy.calculateSimilarity(this, (JsonArray) value1, (JsonArray) value2);
		} else if (value1.getType() == JsonType.Record && value2.getType() == JsonType.Record) {
			return this.compareRecordRecordStrategy.calculateSimilarity(this, (JsonRecord) value1, (JsonRecord) value2);
		} else if (value1.getType().isAtomic() && value2.getType().isAtomic()) {
			return this.compareAtomicValues((JsonAtomic) value1, (JsonAtomic) value2);
		} else if (value1.getType() == JsonType.Array && value2.getType() == JsonType.Record) {
			return this.compareArrayRecordStrategy.calculateSimilarity(this, (JsonArray) value1, (JsonRecord) value2);
		} else if (value1.getType() == JsonType.Record && value2.getType() == JsonType.Array) {
			return this.compareArrayRecordStrategy.calculateSimilarity(this, (JsonArray) value2, (JsonRecord) value1);
		} else if (value1.getType() == JsonType.Array) {
			return this.compareArrayAtomicStrategy.calculateSimilarity(this, (JsonArray) value1, (JsonAtomic) value2);
		} else if (value2.getType() == JsonType.Array) {
			return this.compareArrayAtomicStrategy.calculateSimilarity(this, (JsonArray) value2, (JsonAtomic) value1);
		} else if (value1.getType() == JsonType.Record) {
			return this.compareRecordAtomicStrategy.calculateSimilarity(this, (JsonRecord) value1, (JsonAtomic) value2);
		} else if (value2.getType() == JsonType.Record) {
			return this.compareRecordAtomicStrategy.calculateSimilarity(this, (JsonRecord) value2, (JsonAtomic) value1);
		}

		throw new IllegalStateException("The JsonType could not be determined.");
	}

	/**
	 * Calculates the similarity of the two passed {@link JsonAtomic}s.
	 * 
	 * @param value1
	 *            The first atomic value.
	 * @param value2
	 *            The second atomic value.
	 * @return The similarity of the two passed values.
	 */
	protected abstract double compareAtomicValues(JsonAtomic value1, JsonAtomic value2);

	/**
	 * Sets a new strategy for comparing {@link JsonArray}s.
	 * 
	 * @param strategy
	 *            The new strategy for comparing <code>JsonArray</code>s.
	 */
	public void setCompareArrayArrayStrategy(CalculationStrategy<JsonArray, JsonArray> strategy) {
		this.compareArrayArrayStrategy = strategy;
	}

	/**
	 * Sets a new strategy for comparing {@link JsonArray}s and atomic values.
	 * 
	 * @param strategy
	 *            The new strategy for comparing <code>JsonArray</code>s and atomic values.
	 */
	public void setCompareArrayAtomicStrategy(CalculationStrategy<JsonArray, JsonAtomic> strategy) {
		this.compareArrayAtomicStrategy = strategy;
	}

	/**
	 * Sets a new strategy for comparing {@link JsonArray}s and {@link JsonRecord}s.
	 * 
	 * @param strategy
	 *            The new strategy for comparing <code>JsonArray</code>s and <code>JsonRecord</code>s.
	 */
	public void setCompareArrayRecordStrategy(CalculationStrategy<JsonArray, JsonRecord> strategy) {
		this.compareArrayRecordStrategy = strategy;
	}

	/**
	 * Sets a new strategy for comparing {@link JsonRecord}s.
	 * 
	 * @param strategy
	 *            The new strategy for comparing <code>JsonRecord</code>s.
	 */
	public void setCompareRecordRecordStrategy(CalculationStrategy<JsonRecord, JsonRecord> strategy) {
		this.compareRecordRecordStrategy = strategy;
	}

	/**
	 * Sets a new strategy for comparing {@link JsonRecord}s and atomic values.
	 * 
	 * @param strategy
	 *            The new strategy for comparing <code>JsonRecord</code>s and atomic values.
	 */
	public void setCompareRecordAtomicStrategy(CalculationStrategy<JsonRecord, JsonAtomic> strategy) {
		this.compareRecordAtomicStrategy = strategy;
	}

	// /**
	// * Calculates the similarity of the two passed {@link JsonArray}s.
	// *
	// * @param arr1
	// * The first array.
	// * @param arr2
	// * The second array.
	// * @return The similarity of the two passed arrays.
	// */
	// protected double compareArrays(JsonArray arr1, JsonArray arr2) {
	// // double similaritySum = 0.0;
	// // int valueCount = 0;
	// //
	// // Iterator<JsonValue> iterator2 = arr2.iterator();
	// // for (JsonValue value : arr1) {
	// // if (iterator2.hasNext()) {
	// // similaritySum += this.calculateSimilarity(value, iterator2.next());
	// // }
	// // valueCount++;
	// // }
	// //
	// // while (iterator2.hasNext()) {
	// // iterator2.next();
	// // valueCount++;
	// // }
	// //
	// // return similaritySum / valueCount;
	// throw new IllegalStateException("Arrays cannot be compared by default.");
	// }
	//
	// /**
	// * Calculates the similarity of the single-valued {@link JsonValue} and the {@link JsonArray}. The first parameter is guaranteed to be a
	// * {@link JsonBoolean}, {@link JsonNull}, {@link JsonNumber}, or {@link JsonString}. This method has the same functionality like
	// * {@link #compareArrays(JsonArray, JsonArray)}
	// *
	// * @param value
	// * The value containing one of the atomic types.
	// * @param arr
	// * The array.
	// * @return The similarity of the single value and the array.
	// */
	// protected double compareSingleValueArray(JsonValue value, JsonArray arr) {
	// return this.compareArrays(new JsonArray(value), arr);
	// }
	//
	// /**
	// * Calculates the similarity of the passed {@link JsonArray} and the passed {@link JsonRecord}.
	// *
	// * @param arr
	// * The array to compare with.
	// * @param rec
	// * The record to compare with.
	// * @return The similarity of the array and the record.
	// */
	// protected double compareArrayRecord(JsonArray arr, JsonRecord rec) {
	// throw new IllegalArgumentException("Arrays and records cannot be compared by default.");
	// }
	//
	// /**
	// * Calculates the similarity of the single-valued {@link JsonValue} and the {@link JsonRecord}. The first parameter is guaranteed to be a
	// * {@link JsonBoolean}, {@link JsonNull}, {@link JsonNumber}, or {@link JsonString}.
	// *
	// * @param value
	// * The value containing one of the atomic types.
	// * @param rec
	// * The record.
	// * @return The similarity of the single value and the record.
	// */
	// protected double compareSingleValueRecord(JsonValue value, JsonRecord rec) {
	// throw new IllegalArgumentException("Atomic values cannot be compared with records by default.");
	// }
	//
	// /**
	// * Calculates the similarity of the two passed {@link JsonRecord}s.
	// *
	// * @param rec1
	// * The first record.
	// * @param rec2
	// * The second record.
	// * @return The similarity of the two passed records.
	// */
	// protected double compareRecords(JsonRecord rec1, JsonRecord rec2) {
	// throw new IllegalArgumentException("Records cannot be compared by default.");
	// }

}
