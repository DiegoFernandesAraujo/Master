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

/**
 * 
 */
package dude.util.data.json.auto;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;

import dude.util.BoundType;
import dude.util.data.json.DuDeJsonGenerator;
import dude.util.data.json.DuDeJsonParser;

/**
 * Enum constant serialization.
 * 
 * @author Arvid.Heise
 * 
 * @param <E>
 *            the enum type
 */
class EnumJsonSerialization<E extends Enum<E>> extends AutoJsonSerialization<E> {

	/**
	 * Initializes the serialization for the given type.
	 * 
	 * @param enumType
	 *            the wrapped enum type
	 */
	public EnumJsonSerialization(BoundType enumType) {
		super(enumType);
	}

	@Override
	public void write(DuDeJsonGenerator generator, E jsonable) throws JsonGenerationException, IOException {
		generator.writeString(jsonable.name());
	}

	@Override
	public E read(DuDeJsonParser<?> parser, Object currentValue) throws JsonParseException, IOException {
		return Enum.valueOf(getRawType(), parser.nextString());
	}
}