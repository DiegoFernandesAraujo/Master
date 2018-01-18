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

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;

import dude.util.data.json.DuDeJsonGenerator;
import dude.util.data.json.DuDeJsonParser;
import dude.util.data.json.JsonArray;
import dude.util.data.json.JsonString;

/**
 * <code>DuDeObjectId</code> encapsulates the identifying information of each {@link DuDeObject}.
 * 
 * @author Matthias Pohl
 */
public class DuDeObjectId implements Jsonable, Comparable<DuDeObjectId> {

	/**
	 * The attribute name of the object id within the Json representation of this <code>DuDeObject</code>.
	 */
	public final static String OBJECT_ID_ATTRIBUTE_NAME = "objectId";

	/**
	 * The attribute name of the source id within the Json representation of this <code>DuDeObject</code>.
	 */
	public final static String SOURCE_ID_ATTRIBUTE_NAME = "sourceId";

	private String sourceId = null;
	private JsonArray objectId = null;

	/**
	 * Initializes an invalid <code>DuDeObjectId</code>.
	 */
	DuDeObjectId() {
		this(null, null);
	}

	/**
	 * Initializes a <code>DuDeObjectId</code> with the passed identifiers. If one of the parameters is
	 * <code>null</code>, the id will be <code>invalid</code>.
	 * 
	 * @param srcId
	 *            The identifier of the corresponding source.
	 * @param objId
	 *            The actual identifier of the object.
	 */
	DuDeObjectId(String srcId, JsonArray objId) {
		this.sourceId = srcId;
		this.objectId = objId;
	}

	/**
	 * Returns the source identifier.
	 * 
	 * @return The source identifier or <code>null</code>, if the id is <code>invalid</code>.
	 */
	public String getSourceId() {
		if (!this.isValid()) {
			return null;
		}

		return this.sourceId;
	}

	/**
	 * Returns the object identifier.
	 * 
	 * @return The object identifier or <code>null</code>, if the id is <code>invalid</code>.
	 */
	public JsonArray getObjectId() {
		if (!this.isValid()) {
			return null;
		}

		return this.objectId;
	}

	/**
	 * Checks if the id is valid.
	 * 
	 * @return <code>true</code>, if the id is valid; otherwise <code>false</code>.
	 */
	public boolean isValid() {
		return this.sourceId != null && this.objectId != null;
	}

	/**
	 * Checks if the passed id has the same source information.
	 * 
	 * @param other
	 *            Another <code>DuDeObjectId</code>.
	 * @return <code>true</code>, if both ids contain the same source information; otherwise <code>false</code>.
	 */
	public boolean fromSameSource(DuDeObjectId other) {
		return this.sourceId.equals(other.sourceId);
	}

	@Override
	public String toString() {
		return this.getSourceId() + "." + this.getObjectId();
	}

	@Override
	public int compareTo(DuDeObjectId other) {
		if (this.sourceId.compareTo(other.sourceId) != 0) {
			// compares the source identifier
			return this.sourceId.compareTo(other.sourceId);
		}

		// compares the object identifier if the source identifiers are equal
		return this.objectId.compareTo(other.objectId);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.objectId == null) ? 0 : this.objectId.hashCode());
		result = prime * result + ((this.sourceId == null) ? 0 : this.sourceId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (this.getClass() != obj.getClass()) {
			return false;
		}
		
		DuDeObjectId other = (DuDeObjectId) obj;
		if (this.objectId == null) {
			if (other.objectId != null) {
				return false;
			}
		} else if (!this.objectId.equals(other.objectId)) {
			return false;
		}
		if (this.sourceId == null) {
			if (other.sourceId != null) {
				return false;
			}
		} else if (!this.sourceId.equals(other.sourceId)) {
			return false;
		}
		return true;
	}
	
	@Override
	public void toJson(DuDeJsonGenerator jsonGenerator) throws JsonGenerationException, IOException {
		jsonGenerator.writeRecordStart();
		
		jsonGenerator.writeRecordEntry(DuDeObjectId.SOURCE_ID_ATTRIBUTE_NAME, new JsonString(this.getSourceId()));
		jsonGenerator.writeRecordEntry(DuDeObjectId.OBJECT_ID_ATTRIBUTE_NAME, this.objectId);
		
		jsonGenerator.writeRecordEnd();
	}

	@Override
	public void fromJson(DuDeJsonParser<?> jsonParser) throws JsonParseException, IOException {
		jsonParser.skipToken(); // skip '{'
		
		if (!DuDeObjectId.SOURCE_ID_ATTRIBUTE_NAME.equals(jsonParser.nextFieldName())) {
			throw new JsonParseException("Source identifier is missing.", null);
		}
		
		this.sourceId = jsonParser.nextJsonString().getStringValue();
		
		if (!DuDeObjectId.OBJECT_ID_ATTRIBUTE_NAME.equals(jsonParser.nextFieldName())) {
			throw new JsonParseException("Object identifier is missing.", null);
		}
		
		this.objectId = jsonParser.nextJsonArray();
		
		jsonParser.skipToken(); // skip '}'
	}
}
