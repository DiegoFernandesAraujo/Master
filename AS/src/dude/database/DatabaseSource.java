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

package dude.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import dude.database.adapter.Database;
import dude.database.util.Schema;
import dude.datasource.AbstractDataSource;
import dude.datasource.DataSource;
import dude.exceptions.ExtractionFailedException;
import dude.exceptions.InvalidSchemaException;
import dude.util.data.DuDeObject;
import dude.util.data.Jsonable;
import dude.util.data.json.JsonBoolean;
import dude.util.data.json.JsonNull;
import dude.util.data.json.JsonNumber;
import dude.util.data.json.JsonRecord;
import dude.util.data.json.JsonString;
import dude.util.data.json.JsonValue;

/**
 * <code>DatabaseSource</code> represents databases.
 * 
 * @author Matthias Pohl
 * 
 * @see Database
 * @see DBConnector
 */
public class DatabaseSource extends AbstractDataSource<DatabaseSource> {

	/**
	 * <code>DatabaseSourceIterator</code> is used for generating {@link DuDeObject}s out of <code>DatabaseSource</code>s.
	 * 
	 * @author Matthias Pohl
	 */
	protected class DatabaseSourceIterator extends AbstractDataSourceIterator<DatabaseSource> {

		private Connection connection;
		
		private Schema tableSchema;
		private ResultSet result;

		/**
		 * Initializes a <code>DatabaseSourceIterator</code> using the passed <code>DatabaseSource</code>.
		 * 
		 * @param source
		 *            The source of which the data shall be extracted.
		 * @throws SQLException
		 *             If an error occurred while requesting the data.
		 */
		protected DatabaseSourceIterator(DatabaseSource source) throws SQLException {
			super(source);

			try {
				this.connection = this.dataSource.getDatabase().createConnection();
			} catch (ClassNotFoundException e) {
				throw new IllegalStateException("The database driver could not be loaded.", e);
			}
			Statement stmt = this.connection.createStatement();

			try {
				this.tableSchema = new Schema(this.connection, this.dataSource.getDatabase().getSQLSchema(), this.dataSource.getTableName());
			} catch (InvalidSchemaException e) {
				throw new IllegalStateException("The schema could not be extracted.", e);
			}
			
			this.result = stmt.executeQuery(this.dataSource.getQuery());

			this.dataSource.registerStatement(stmt);
		}

		private void closeResultSet() {
			if (!this.resultSetClosed()) {
				try {
					this.result.close();
				} catch (final SQLException e) {
					DatabaseSource.logger.warn("Error occurred while closing the underlying resultSet.", e);
				} finally {
					this.result = null;
				}
			}
		}

		private boolean resultSetClosed() {
			return this.result == null;
		}

		@Override
		protected JsonRecord loadNextRecord() throws ExtractionFailedException {
			if (this.resultSetClosed()) {
				return null;
			}

			JsonRecord retVal = null;

			// get next object
			try {
				if (this.result.next()) {
					retVal = new JsonRecord();

					for (int columnIndex = 0; columnIndex < this.tableSchema.size(); ++columnIndex) {
						JsonValue value;

						switch (this.tableSchema.getColumnSQLType(columnIndex)) {
						case java.sql.Types.BIT: // JsonBoolean
						case java.sql.Types.BOOLEAN:
							// JDBC iteration starts with 1!!!
							if (this.result.getBoolean(columnIndex + 1)) {
								value = JsonBoolean.TRUE;
							} else {
								value = JsonBoolean.FALSE;
							}

							break;
						case java.sql.Types.DECIMAL: // JsonNumber
						case java.sql.Types.NUMERIC:
						case java.sql.Types.DOUBLE:
						case java.sql.Types.FLOAT:
						case java.sql.Types.REAL:
						case java.sql.Types.BIGINT:
						case java.sql.Types.INTEGER:
						case java.sql.Types.SMALLINT:
						case java.sql.Types.TINYINT:
							// JDBC iteration starts with 1!!!
							// dynamic type selection -> the smallest type that is possible will be chosen as generic type (see DuDeJsonParser)
							final String strValue = this.result.getString(columnIndex + 1);

							if (!this.result.wasNull()) {
								value = JsonNumber.createJsonNumber(strValue);
							} else {
								value = JsonNull.NULL;
							}

							break;
						case java.sql.Types.ARRAY: // JsonString
						case java.sql.Types.BINARY:
						case java.sql.Types.BLOB:
						case java.sql.Types.CHAR:
						case java.sql.Types.CLOB:
						case java.sql.Types.DATALINK:
						case java.sql.Types.DATE:
						case java.sql.Types.LONGVARBINARY:
						case java.sql.Types.LONGVARCHAR:
						case java.sql.Types.JAVA_OBJECT:
						case java.sql.Types.OTHER:
						case java.sql.Types.REF:
						case java.sql.Types.STRUCT:
						case java.sql.Types.TIME:
						case java.sql.Types.TIMESTAMP:
						case java.sql.Types.VARBINARY:
						case java.sql.Types.VARCHAR:
						default:
							// JDBC iteration starts with 1!!!
							value = new JsonString(this.result.getString(columnIndex + 1));
							break;
						}

						if (this.result.wasNull()) {
							value = JsonNull.NULL;
						}

						this.addAttributeValue(retVal, this.tableSchema.getColumnName(columnIndex).intern(), value);
					}

				} else {
					this.closeResultSet();
				}
			} catch (final SQLException e) {
				DatabaseSource.logger.fatal("An SQLException was raised while extracting the data with the following connection:\n\t"
						+ this.dataSource.getJDBCString() + "\nQuery:\n" + this.dataSource.getQuery(), e);
				throw new ExtractionFailedException("An SQLException occurred - connection: " + this.dataSource.getJDBCString() + "; Query: "
						+ this.dataSource.getQuery(), e);
			} catch (final ParseException e) {
				DatabaseSource.logger.fatal("A ParseException was raised while tokenizing a number.", e);
			}

			return retVal;
		}

	}

	private static final Logger logger = Logger.getLogger(DatabaseSource.class.getPackage().getName());

	private Database database;

	private String tableName;
	private String queryExtension;

	private transient final Collection<Statement> openedStatements = new ArrayList<Statement>();

	/**
	 * Internal constructor for {@link Jsonable} deserialization.
	 */
	protected DatabaseSource() {
		// nothing to do
	}

	/**
	 * Initializes <code>DatabaseSource</code> for the passed {@link Database} and table.
	 * 
	 * @param identifier
	 *            The identifier of this {@link DataSource}.
	 * @param db
	 *            The underlying <code>Database</code>.
	 * @param tbName
	 *            The table name.
	 */
	public DatabaseSource(String identifier, Database db, String tbName) {
		super(identifier);

		if (db == null) {
			throw new NullPointerException("DB connection is missing.");
		} else if (tbName == null) {
			throw new NullPointerException("Table name is missing.");
		}

		this.database = db;
		this.tableName = tbName;
	}

	/**
	 * Returns the underlying {@link Database}.
	 * 
	 * @return The underlying <code>Database</code>.
	 */
	protected Database getDatabase() {
		return this.database;
	}

	/**
	 * Returns the table name.
	 * 
	 * @return The table name.
	 */
	protected String getTableName() {
		return this.tableName;
	}

	/**
	 * Enables the where clause with the passed query extension. This method can be used for extracting only specified records. CAUTION: No query
	 * validation is done.
	 * 
	 * @param whereExt
	 *            The where clause.
	 */
	public void setWhereFilter(String whereExt) {
		// TODO: make validation check
		this.queryExtension = whereExt;
	}

	/**
	 * Sets the where clause for the data extraction.
	 * 
	 * @param whereExt
	 *            The where clause.
	 * @return The current instance.
	 * 
	 * @see #setWhereFilter(String)
	 */
	public DatabaseSource withWhereFilter(String whereExt) {
		this.setWhereFilter(whereExt);

		return this;
	}

	/**
	 * Returns the complete query that is used for querying the result.
	 * 
	 * @return The SQL query.
	 */
	protected String getQuery() {
		StringBuilder strBuilder = new StringBuilder();

		strBuilder.append("SELECT * FROM ").append(this.database.getSQLSchema()).append('.').append(this.tableName);

		if (this.queryExtension != null) {
			strBuilder.append(" WHERE (").append(this.queryExtension.trim()).append(')');
		}

		return strBuilder.toString();
	}

	/**
	 * Returns the JDBC String of the underlying database.
	 * 
	 * @return The JDBC String of the underlying database.
	 */
	protected String getJDBCString() {
		return this.database.getJDBCString();
	}

	/**
	 * Registers a {@link Statement}. This statement will be closed during the next call of {@link #cleanUp()}.
	 * 
	 * @param stmt
	 *            The statement that shall be closed later on.
	 */
	protected void registerStatement(Statement stmt) {
		if (stmt == null) {
			return;
		}

		this.openedStatements.add(stmt);
	}

	@Override
	public void cleanUp() {
		super.cleanUp();

		for (Statement stmt : this.openedStatements) {
			try {
				stmt.close();
			} catch (SQLException e) {
				DatabaseSource.logger.warn("An SQLException occurred while closing an opened statement.", e);
			}
		}

		this.openedStatements.clear();
	}

	@Override
	public Iterator<DuDeObject> iterator() {
		try {
			return new DatabaseSourceIterator(this);
		} catch (SQLException e) {
			throw new IllegalStateException("A SQLException occurred while instantiating the DatabaseSource iterator.", e);
		}
	}

}
