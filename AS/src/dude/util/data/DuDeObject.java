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

package dude.util.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonToken;

import dude.datasource.AbstractDataSource;
import dude.util.data.json.DuDeJsonGenerator;
import dude.util.data.json.DuDeJsonParser;
import dude.util.data.json.JsonArray;
import dude.util.data.json.JsonRecord;
import dude.util.data.json.JsonString;
import dude.util.data.json.JsonValue;
import dude.util.data.json.JsonValue.JsonType;

/**
 * <code>DuDeObject</code> encapsulates the data of the original object and two ids (for the source and a local one) for identifying each
 * <code>DuDeObject</code>.
 * 
 * @author Matthias Pohl
 * 
 * @see AbstractDataSource
 */
public class DuDeObject implements Comparable<DuDeObject>, Jsonable {

	/**
	 * The attribute name of the object data within the <code>DuDeObject's</code> Json representation.
	 */
	public final static String OBJECT_DATA_ATTRIBUTE_NAME = "data";

	/**
	 * The attribute name of the object identifier within the <code>DuDeObject's</code> Json representation.
	 */
	public static final String OBJECT_IDENTIFIER_ATTRIBUTE_NAME = "identifier";

	private List<DuDeObjectId> identifiers = new ArrayList<DuDeObjectId>();
	private JsonRecord data;

	@SuppressWarnings("unused")
	private DuDeObject() {
		// nothing to do
	}
	
	/**
	 * Initializes a <code>DuDeObject</code> with the passed ids and the given data.
	 * 
	 * @param data
	 *            The data that describes the real-world object of which this instance is the representation.
	 * @param srcId
	 *            The identifier of the source where this data comes from.
	 * @param objectId
	 *            The identifier of the object within the source where this data comes from.
	 * 
	 * @throws NullPointerException
	 *             If <code>null</code> was passed through <code>srcId</code> or <code>objId</code>.
	 */
	public DuDeObject(JsonRecord data, String srcId, JsonArray objectId) {
		if (srcId == null) {
			throw new NullPointerException("null was passed through the source id parameter.");
		} else if (objectId == null) {
			throw new NullPointerException("null was passed through the object id parameter.");
		}

		this.addIdentifier(new DuDeObjectId(srcId, objectId));
		this.data = data;
	}

	/**
	 * Creates a <code>DuDeObject</code> reference.
	 * 
	 * @param sourceId
	 *            The source identifier.
	 * @param objectId
	 *            The object identifier.
	 * 
	 * @throws NullPointerException
	 *             If <code>null</code> was passed through <code>sourceId</code> or <code>objectId</code>.
	 */
	public DuDeObject(String sourceId, JsonArray objectId) {
		this(null, sourceId, objectId);
	}

	/**
	 * Initializes a <code>DuDeObject</code> using the given ids. <code>objectId</code> will be embedded into a {@link JsonArray}.
	 * 
	 * @param data
	 *            The data that describes the real-world object of which this instance is the representation.
	 * @param objectId
	 *            The object identifier as String Value. It only consists of values of one column.
	 * @param sourceId
	 *            The source identifier.
	 */
	public DuDeObject(JsonRecord data, String sourceId, String objectId) {
		if (sourceId == null) {
			throw new NullPointerException("null was passed through the source id parameter.");
		}

		if (objectId == null) {
			throw new NullPointerException("null was passed through the object id parameter.");
		}

		JsonArray objectIdArray = new JsonArray();
		objectIdArray.add(new JsonString(objectId));

		this.addIdentifier(new DuDeObjectId(sourceId, objectIdArray));
		this.data = data;
	}

	/**
	 * Creates a <code>DuDeObject</code> reference.
	 * 
	 * @param sourceId
	 *            The source identifier.
	 * @param objectId
	 *            The object identifier as a <code> String </code> value.
	 * 
	 * @throws NullPointerException
	 *             If <code>null</code> was passed through <code>sourceId</code> or <code>objectId</code>.
	 */
	public DuDeObject(String sourceId, String objectId) {
		this(null, sourceId, objectId);
	}

	/**
	 * Two objects are equal, if both have the same ids. If both objects contain data, this data has to be equal as well.
	 * 
	 * @return <code>true</code>, if both objects have the same ids; otherwise <code>false</code>
	 * 
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(Object otherObj) {

		// check for reference
		if (this == otherObj) {
			return true;
		}

		// checks for equal type
		DuDeObject otherDuDeObject;
		if (otherObj != null && otherObj instanceof DuDeObject) {
			otherDuDeObject = (DuDeObject) otherObj;
		} else {
			return false;
		}

		return this.equalsID(otherDuDeObject);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifiers == null) ? 0 : identifiers.hashCode());
		return result;
	}

	/**
	 * Two objects are equal, if both have the same ids.
	 * 
	 * @param otherDuDeObject
	 *            The <code>DuDeObject</code> that shall be compared with the current instance.
	 * 
	 * @return <code>true</code>, if both objects have the same ids; otherwise <code>false</code>.
	 */
	public boolean equalsID(DuDeObject otherDuDeObject) {
		// check for reference
		if (this == otherDuDeObject) {
			return true;
		}

		// checks whether both object have the same identifier but different real data -> causes
		// IdentifierAlreadyInUseException
		return this.identifiers.equals(otherDuDeObject.identifiers);
	}

	/**
	 * Returns the first identifier of this <code>DuDeObject</code>.
	 * 
	 * @return The {@link DuDeObjectId} which contains both, the source id as well as the object id.
	 */
	public DuDeObjectId getIdentifier() {
		return this.identifiers.get(0);
	}
	
	/**
	 * Adds an identifier to this <code>DuDeObject</code>.
	 * 
	 * @param id The {@link DuDeObjectId}, which shall be added.
	 */
	public void addIdentifier(DuDeObjectId id) {
		this.identifiers.add(id);
	}
	
	/**
	 * Returns all identifiers of this <code>DuDeObject</code>.
	 * 
	 * @return An {@link Iterable} that contains all {@link DuDeObjectId}s of this <code>DuDeObject</code>.
	 */
	public Iterable<DuDeObjectId> getIdentifiers() {
		return this.identifiers;
	}
	
	/**
	 * Checks, if this <code>DuDeObject</code> is a merged object.
	 * 
	 * @return <code>true</code>, if it is a merged object, <code>false</code> otherwise
	 */
	public boolean isMerged() {
		return this.identifiers.size() > 1;
	}
	
	/**
	 * Returns the number of objects this <code>DuDeObject</code> was merged from.
	 * 
	 * @return The number of <code>DuDeObjects</code> that were the source for this merged object.
	 *         If this object is not a merged one, the return value is 1.
	 */
	public int getMergeCount() {
		if (this.isMerged()) {
			return this.identifiers.size();
		}
		
		return 0;
	}

	/**
	 * Returns the source identifier of this object.
	 * 
	 * @return The source identifier of this object..
	 */
	public String getSourceId() {
		return this.getIdentifier().getSourceId();
	}

	/**
	 * Returns the object identifier of this object.
	 * 
	 * @return The object identifier of this object.
	 */
	public JsonArray getObjectId() {
		return this.getIdentifier().getObjectId();
	}

	/**
	 * Returns the data that describes the real-world object of which this instance is the representation.
	 * 
	 * @return The object's data or <code>null</code>, if the <code>DuDeObject</code> has no data.
	 */
	public JsonRecord getData() {
		if (!this.hasData()) {
			return null;
		}

		return this.data;
	}

	/**
	 * Traverses the passed path and returns the corresponding attribute value or <code>null</code>, if the passed path is invalid.
	 * 
	 * @param path
	 *            The path which specifies the attribute whose value(s) shall be returned.
	 * @return The corresponding value(s) or <code>null</code>, if the path is invalid.
	 */
	public JsonValue getAttributeValuesByPath(String... path) {
		if (path == null || path.length == 0) {
			return null;
		}

		return this.data.getJsonValue(path);
	}

	/**
	 * Looks within the current <code>DuDeObject</code> for the given attribute. If this attribute is found, the corresponding value is returned. If
	 * this attribute does not exist, <code>null</code> will be returned.
	 * 
	 * @param attributeName
	 *            The name of the attribute whose value is requested.
	 * @return The corresponding {@link JsonArray} (since all attribute values are stored in arrays) or <code>null</code>, if this attribute does not
	 *         exist.
	 */
	public JsonValue getAttributeValues(String attributeName) {
		if (attributeName == null || !this.hasData()) {
			return null;
		}

		return this.data.searchFor(attributeName);
	}

	/**
	 * Returns the concrete value of the given attribute at the passed position.
	 * 
	 * @param name
	 *            The name of the attribute whose value shall be returned.
	 * @param index
	 *            The index of the value within the attribute value list. If this parameter is negative, the corresponding {@link JsonArray} will be
	 *            accessed in reverse order (e.g.: <code>-1</code> returns the last element).
	 * @return The concrete value or <code>null</code>, if the attribute does not exist, or no value was found at the given position.
	 */
	public JsonValue getAttributeValue(String name, int index) {
		JsonValue value = this.getAttributeValues(name);

		// invalid range
		if (value == null) {
			return null;
		} else if (value.getType() == JsonType.Array) {
			JsonArray arr = (JsonArray) value;
			if (arr.size() <= index || arr.size() <= -index) {
				return null;
			}
			
			// reverse index -> -1 represents the last value of the array
			if (index < 0) {
				return arr.get(arr.size() + index);
			}
			
			// normal index
			return arr.get(index);
		} else {
			// return concrete value if the passed index is 0
			if (index == 0) {
				return value;
			}
		}
		
		return null;
	}

	/**
	 * Returns the first value of the given attribute.
	 * 
	 * @param name
	 *            The attribute whose value shall be returned.
	 * @return The first value of the attribute or <code>null</code>, if the attribute does not exist or it has no value.
	 */
	public JsonValue getAttributeValue(String name) {
		return this.getAttributeValue(name, 0);
	}

	/**
	 * Converts the <code>DuDeObject</code> to its Json representation.
	 * 
	 * @return The Json representation of the <code>DuDeObject</code>.
	 */
	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		for (DuDeObjectId id : this.identifiers) {
			if (strBuilder.length() > 0) {
				strBuilder.append(", ");
			} else {
				strBuilder.append('[');
			}
			
			strBuilder.append(id);
		}
		strBuilder.append(']');
		return strBuilder.toString();
	}

	/**
	 * Checks whether the current <code>DuDeObject</code> contains real data.
	 * 
	 * @return <code>true</code>, if it contains data; otherwise <code>false</code>.
	 */
	public boolean hasData() {
		return this.data != null;
	}

	@Override
	public int compareTo(DuDeObject other) {
		Iterator<DuDeObjectId> thisIDIterator = this.identifiers.iterator();
		Iterator<DuDeObjectId> otherIDIterator = other.identifiers.iterator();
		
		while (thisIDIterator.hasNext() && otherIDIterator.hasNext()) {
			int compareValue = thisIDIterator.next().compareTo(otherIDIterator.next());
			if (compareValue != 0) {
				return compareValue;
			}
		}
		
		if (!thisIDIterator.hasNext()) {
			return -1;
		} else if (!otherIDIterator.hasNext()) {
			return 1;
		}
		
		return 0;
	}

	@Override
	public void toJson(DuDeJsonGenerator jsonGenerator) throws JsonGenerationException, IOException {
		jsonGenerator.writeRecordStart();

		jsonGenerator.writeRecordFieldName(DuDeObject.OBJECT_IDENTIFIER_ATTRIBUTE_NAME);
		jsonGenerator.writeArrayStart();
		for (DuDeObjectId id : this.identifiers) {
			id.toJson(jsonGenerator);
		}
		jsonGenerator.writeArrayEnd();

		jsonGenerator.writeRecordEntry(DuDeObject.OBJECT_DATA_ATTRIBUTE_NAME, this.data);

		jsonGenerator.writeRecordEnd();
	}
	
	@Override
	public void fromJson(DuDeJsonParser<?> jsonParser) throws JsonParseException, IOException {
		jsonParser.skipToken(); // skip '{'
		
		if (!DuDeObject.OBJECT_IDENTIFIER_ATTRIBUTE_NAME.equals(jsonParser.nextFieldName())) {
			throw new JsonParseException("Identifier is missing.", null);
		}
		
		if (this.identifiers == null) {
			this.identifiers = new ArrayList<DuDeObjectId>();
		}
		
		jsonParser.skipToken(JsonToken.START_ARRAY);
		
		while (jsonParser.currentToken() != JsonToken.END_ARRAY) {
			DuDeObjectId id = new DuDeObjectId();
			id.fromJson(jsonParser);
			this.identifiers.add(id);
		}
		
		jsonParser.skipToken(JsonToken.END_ARRAY);
		
		if (!DuDeObject.OBJECT_DATA_ATTRIBUTE_NAME.equals(jsonParser.nextFieldName())) {
			throw new JsonParseException("Data is missing.", null);
		}
		
		this.data = jsonParser.nextJsonRecord();
		
		jsonParser.skipToken(); // skip '}'
	}
}
