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

package dude.util.data.json.auto;

import java.io.IOException;
import java.lang.reflect.Modifier;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonToken;

import dude.util.BoundType;
import dude.util.ReflectUtil;
import dude.util.data.AutoJsonable;
import dude.util.data.Jsonable;
import dude.util.data.json.DuDeJsonGenerator;
import dude.util.data.json.DuDeJsonParser;

/**
 * Provides json serialization for a specific {@link BoundType}.
 * 
 * @author Arvid.Heise
 * 
 * @param <T>
 *            the type to serialize
 */
public abstract class AutoJsonSerialization<T> {
	private final BoundType type;

	/**
	 * Initialization for the given type
	 * 
	 * @param type
	 *            the wrapped type
	 */
	protected AutoJsonSerialization(BoundType type) {
		this.type = type;
	}

	/**
	 * Returns the raw type for which this serialization class was created.
	 * 
	 * @return the raw type
	 */
	@SuppressWarnings("unchecked")
	public Class<T> getRawType() {
		return (Class<T>) this.type.getType();
	}

	/**
	 * Returns the {@link BoundType} for which this serialization class was created.
	 * 
	 * @return the {@link BoundType}
	 */
	public BoundType getType() {
		return this.type;
	}

	/**
	 * Determines whether the given class has an accessible default constructor and caches the result.
	 * 
	 * @param clazz
	 *            the class to check
	 * @return true if it is instantiable via {@link ReflectUtil#newInstance(Class)}
	 */
	protected boolean isInstantiable(Class<? extends Object> clazz) {
		return ReflectUtil.isInstantiable(clazz);
	}

	/**
	 * Creates an instance of the wrapped type and initializes the content with the json provided by the given {@link DuDeJsonParser}.
	 * 
	 * @param parser
	 *            the parser to read from
	 * @return an instance of <code>T</code>
	 * 
	 * @throws JsonParseException
	 *             If an error occurred while parsing the data.
	 * @throws IOException
	 *             If an error occurred while reading from the stream.
	 */
	public final T read(DuDeJsonParser<?> parser) throws JsonParseException, IOException {
		return this.read(parser, null);
	}

	/**
	 * Creates an instance of the wrapped type and initializes the content with the json provided by the given {@link DuDeJsonParser}. This method
	 * might reuse the currentValue but should in most cases use the actual type information of the currentValue to create a new instance.
	 * 
	 * @param parser
	 *            the parser to read from
	 * @param currentValue
	 *            the current value of the field which should be populated by the result of this method or null if this is the root object
	 * @return an instance of <code>T</code>
	 * 
	 * @throws JsonParseException
	 *             If an error occurred while parsing the data.
	 * @throws IOException
	 *             If an error occurred while reading from the stream.
	 */
	public abstract T read(DuDeJsonParser<?> parser, Object currentValue) throws JsonParseException, IOException;

	/**
	 * Reads all non-transient fields of the jsonable to the {@link DuDeJsonParser}. It expects the given jsonParser to currently have an opened
	 * record and it leaves it open.
	 * 
	 * @param parser
	 *            the {@link DuDeJsonParser} to read from
	 * @param jsonable
	 *            the {@link AutoJsonable} to initialize
	 * @throws JsonParseException
	 *             If an error occurred while parsing the data.
	 * @throws IOException
	 *             If an error occurred while reading the data.
	 */
	public void readFields(DuDeJsonParser<?> parser, T jsonable) throws JsonParseException, IOException {
		// should be implemented by subclasses where meaningful
	}

	/**
	 * Writes the given instance to the specified {@link DuDeJsonGenerator}.
	 * 
	 * @param generator
	 *            the generator to write to
	 * @param jsonable
	 *            the jsonable to write
	 * @throws JsonGenerationException
	 *             If an error occurs while generating the Json syntax.
	 * @throws IOException
	 *             If an error occurs while accessing the underlying stream.
	 */
	public abstract void write(DuDeJsonGenerator generator, T jsonable) throws JsonGenerationException, IOException;

	/**
	 * Writes all non-transient fields of the jsonable to the {@link DuDeJsonGenerator}. It expects the given jsonGenerator to currently have an
	 * opened record and it leaves it open.
	 * 
	 * @param generator
	 *            the {@link DuDeJsonGenerator} to write to
	 * @param jsonable
	 *            the object, the fields of which to write
	 * @throws JsonGenerationException
	 *             If an error occurs while generating the Json syntax.
	 * @throws IOException
	 *             If an error occurs while accessing the underlying stream.
	 */
	public void writeFields(DuDeJsonGenerator generator, Object jsonable) throws JsonGenerationException, IOException {
		// should be implemented by subclasses where meaningful
	}

	/**
	 * Writes the given value with additional type information.<br>
	 * This method should be used when the actual type of a value cannot be inferred from the declaration.<br>
	 * The typical scenario is the usage of polymorphism without secondary information which would allow to infer the type.
	 * 
	 * @param generator
	 *            the {@link DuDeJsonGenerator} to write to
	 * @param jsonable
	 *            the object to write
	 * @throws JsonGenerationException
	 *             If an error occurs while generating the Json syntax.
	 * @throws IOException
	 *             If an error occurs while accessing the underlying stream.
	 */
	protected void writeWithType(DuDeJsonGenerator generator, T jsonable) throws JsonGenerationException, IOException {
		generator.writeRecordStart();
		generator.writeStringRecordEntry("class", jsonable.getClass().getName());
		if (jsonable instanceof Jsonable) {
			generator.writeRecordFieldName("value");
			((Jsonable) jsonable).toJson(generator);
		} else
			this.writeFields(generator, jsonable);
		generator.writeRecordEnd();
	}

	/**
	 * Parsing equivalent of {@link #writeWithType(DuDeJsonGenerator, Object)}.<br>
	 * Since the type is not written in every case, this method also supports a non-destructive way for testing of type information.<br>
	 * If leaveRecordIntact is set and no type information has been detected, it appears as if the method has never been invoked.<br>
	 * However, if type information has been detected, it shall be removed from the stream and the parsed class shall be returned. In that case, the
	 * original record remains opened.<br>
	 * <br>
	 * In contrast, leaveRecordIntact is not set, an opened record will be returned in both cases.
	 * 
	 * @param parser
	 *            the {@link DuDeJsonParser} to read from
	 * @param leaveRecordIntact
	 *            true if the record should not be opened if no type information is encoded in the json
	 * @param declaredType
	 *            the declared type of the field
	 * 
	 * @return the encoded type information or null if none was detected
	 * @throws JsonParseException
	 *             If the class cannot be resolved or an error occurred while parsing the data.
	 * @throws IOException
	 *             If an error occurred while reading the data.
	 */
	@SuppressWarnings("unchecked")
	protected Class<T> readSerializedType(DuDeJsonParser<?> parser, boolean leaveRecordIntact, Class<?> declaredType) throws JsonParseException,
			IOException {
		if ((declaredType.getModifiers() & Modifier.FINAL) > 0)
			return null;

		Class<T> concreteType = null;
		if (parser.currentToken() == JsonToken.START_OBJECT) {
			parser.skipToken(JsonToken.START_OBJECT);
			if (parser.currentToken() == JsonToken.FIELD_NAME && parser.currentFieldName() == "class") {
				parser.skipToken();
				final String className = parser.nextString();
				try {
					concreteType = (Class<T>) Class.forName(className);
				} catch (final ClassNotFoundException e) {
					throw new JsonParseException("Could not resolve class " + className, null);
				}

				if (Jsonable.class.isAssignableFrom(concreteType))
					parser.skipFieldName("value");
			}
			if (leaveRecordIntact) {
				String currentFieldName = parser.currentFieldName();
				DuDeJsonGenerator pushBackGenerator = parser.createPushBackGenerator();
				pushBackGenerator.writeRecordStart();
				pushBackGenerator.writeRecordFieldName(currentFieldName);
				pushBackGenerator.close();
			}
		}

		return concreteType;
	}

	/**
	 * Cleanup method used in conjunction with {@link #readSerializedType(DuDeJsonParser, boolean)}.
	 * 
	 * @param parser
	 *            the {@link DuDeJsonParser} to read from
	 * @param serializedType
	 *            the return value of {@link #readSerializedType(DuDeJsonParser, boolean)}
	 * @throws JsonParseException
	 *             If the class cannot be resolved or an error occurred while parsing the data.
	 * @throws IOException
	 *             If an error occurred while reading the data.
	 */
	protected void finishTypeSerializationRead(DuDeJsonParser<?> parser, final Class<T> serializedType) throws JsonParseException, IOException {
		if (serializedType != null) {
			parser.skipToken(JsonToken.END_OBJECT);
			parser.consolidatePushBack();
		}
	}
}