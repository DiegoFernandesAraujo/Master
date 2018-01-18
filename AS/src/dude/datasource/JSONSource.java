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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonToken;

import dude.exceptions.ExtractionFailedException;
import dude.util.data.DuDeObject;
import dude.util.data.Jsonable;
import dude.util.data.json.DuDeJsonGenerator;
import dude.util.data.json.DuDeJsonParser;
import dude.util.data.json.JsonRecord;
import dude.util.data.json.JsonString;
import dude.util.data.json.JsonUtil;

/**
 * <code>JSONSource</code> represents files containing <code>Json</code> syntax. The underlying file should contain a Json array of Json records.
 * Each record collects the data of one {@link DuDeObject}.
 * 
 * @author Matthias Pohl
 */
public class JSONSource extends AbstractDataSource<JSONSource> implements Jsonable {

	/**
	 * <code>JSONSourceIterator</code> is used for generating {@link DuDeObject}s out of <code>JSONSource</code>s.
	 * 
	 * @author Matthias Pohl
	 */
	protected class JSONSourceIterator extends AbstractDataSourceIterator<JSONSource> {

		private final DuDeJsonParser<JsonRecord> jsonParser;

		/**
		 * Initializes a <code>JSONSourceIterator</code> using the passed <code>JSONSource</code>.
		 * 
		 * @param source
		 *            The source of which the data shall be extracted.
		 * @throws JsonParseException
		 *             If the underlying Json syntax is not valid.
		 * @throws IOException
		 *             If an IO error occurred while the iterator was initialized.
		 */
		protected JSONSourceIterator(JSONSource source) throws JsonParseException, IOException {
			super(source);

			FileInputStream fileIStream = new FileInputStream(source.jsonFile);
			source.registerCloseable(fileIStream);

			this.jsonParser = new DuDeJsonParser<JsonRecord>(JsonRecord.class, fileIStream);
		}

		@Override
		protected JsonRecord loadNextRecord() throws ExtractionFailedException {
			if (!this.jsonParser.hasNext()) {
				return null;
			}

			return this.jsonParser.next();
		}

	}

	private transient File jsonFile;

	/**
	 * Internal constructor for {@link Jsonable} deserialization.
	 */
	protected JSONSource() {
		super();
		// nothing to do
	}

	/**
	 * Initializes a <code>JSONSource</code>.
	 * 
	 * @param identifier
	 *            The identifier of the {@link DataSource} instance.
	 * @param file
	 *            The file of which the data will be extracted.
	 * @throws FileNotFoundException
	 *             If the passed file does not exist.
	 */
	public JSONSource(String identifier, File file) throws FileNotFoundException {
		super(identifier);

		if (file == null) {
			throw new NullPointerException("No file was passed.");
		}

		if (!file.exists()) {
			throw new FileNotFoundException("'" + file.getName() + "' does not exist.");
		}

		if (!file.isFile()) {
			throw new IllegalArgumentException("The passed File instance does not refer to a file in the file system.");
		}

		this.jsonFile = file;
	}

	@Override
	public Iterator<DuDeObject> iterator() {
		try {
			return new JSONSourceIterator(this);
		} catch (JsonParseException e) {
			throw new IllegalStateException("The underlying Json file contains invalid Json syntax.", e);
		} catch (IOException e) {
			throw new IllegalStateException("An IOException occurred while initializing the Json parser.", e);
		}
	}

	@Override
	public void toJson(DuDeJsonGenerator jsonGenerator) throws JsonGenerationException, IOException {
		jsonGenerator.writeRecordStart();
		JsonUtil.writeFields(jsonGenerator, this);
		jsonGenerator.writeRecordEntry("file", new JsonString(this.jsonFile.getCanonicalPath()));
		jsonGenerator.writeRecordEnd();
	}

	@Override
	public void fromJson(DuDeJsonParser<?> jsonParser) throws JsonParseException, IOException {
		jsonParser.skipToken(JsonToken.START_OBJECT);
		JsonUtil.readFields(jsonParser, this);
		jsonParser.skipFieldName("file");
		this.jsonFile = new File(jsonParser.nextString());
		jsonParser.skipToken(JsonToken.END_OBJECT);
	}

}
