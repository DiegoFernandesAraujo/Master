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

package dude.database.adapter;

import java.io.InputStream;
import java.util.Properties;

import dude.database.util.DBInfo;

/**
 * <code>DB2Database</code> encapsulates all the necessary information for establishing a connection to a DB2 database.
 * <p>
 * CAUTION! This <code>Database</code> implementation can be used with Oracle's Thin drivers. <code>DuDe</code> cannot offer this library due to
 * licensing issues. In order to connect <code>DuDe</code> with a DB2 database, you have to add the libraries containing the driver and its
 * license to your project's classpath manually.
 * 
 * @author Matthias Pohl
 */
public class DB2Database extends Database {

	/**
	 * Initializes the <code>DB2Database</code> instance members and loads the settings provided by the parameter <code>dbInfo</code>.
	 * 
	 * @param dbInfo
	 *            Information needed for establishing a connection to this database.
	 */
	public DB2Database(DBInfo dbInfo) {
		super(dbInfo);
	}

	/**
	 * Initializes the <code>DB2Database</code> using the passed {@link InputStream}. The information provided by this stream has to convertible into
	 * a {@link Properties} instance.
	 * 
	 * @param iStream
	 *            The <code>InputStream</code> that provides the connection information.
	 */
	public DB2Database(InputStream iStream) {
		super(iStream);
	}

	/**
	 * Initializes the <code>DB2Database</code> using the passed {@link Properties}.
	 * 
	 * @param prop
	 *            The <code>Properties</code> instance that provides the connection information.
	 */
	public DB2Database(Properties prop) {
		super(prop);
	}

	@Override
	public String getDatabaseDriverName() {
		return "com.ibm.db2.jcc.DB2Driver";
	}

	@Override
	public String getJDBCString() {
		String schemaInfo = "";
		if ("".equals(this.getSQLSchema())) {
			schemaInfo = ":currentSchema=" + this.getSQLSchema() + ";";
		}

		return "jdbc:db2://" + this.getHost() + ":" + this.getPort() + "/" + this.getDatabaseName() + schemaInfo;
	}

}
