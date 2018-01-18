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

package dude.output.statisticoutput;

import java.io.IOException;

import dude.postprocessor.StatisticComponent;
import dude.util.data.AutoJsonable;

/**
 * <code>StatisticOutput</code> offers all methods needed to write out the statistics provided by a {@link StatisticComponent} instance.
 * 
 * @author Fabian Lindenberg
 */
public interface StatisticOutput extends AutoJsonable {

	/**
	 * Writes the stored statistics. May throw an IOException.
	 * 
	 * @throws IOException
	 *             If an error occurs while writing to the output.
	 */
	public void writeStatistics() throws IOException;

	/**
	 * Returns the labels for the measurements.
	 * 
	 * @return An array of Strings containing all labels that are used during the print-out.
	 */
	public String[] getLabels();

	/**
	 * Returns the current statistic component that is used by the output.
	 * 
	 * @return Current <code>StatisticComponent</code>.
	 */
	public StatisticComponent getStatistics();

	/**
	 * Sets the current statistic component that is used by the output.
	 * 
	 * @param statistics
	 *            <code>StatisticComponent</code> that is to be set.
	 */
	public void setStatistics(StatisticComponent statistics);

	/**
	 * Resets the values of all optional labels using empty Strings. The columns are not removed.
	 */
	public void resetOptionalStatisticEntries();

	/**
	 * Sets a new optional label with the passed value.
	 * 
	 * @param label
	 *            The entry's identifier.
	 * @param value
	 *            The entry's value.
	 * @return <code>true</code>, if a new statistic entry was added (no old value was overwritten); otherwise <code>false</code>.
	 */
	public boolean setOptionalStatisticEntry(String label, String value);

	/**
	 * Sets a new optional label with no value.
	 * 
	 * @param label
	 *            The entry's identifier.
	 * @return <code>true</code>, if a new statistic entry was added (no old value was overwritten); otherwise <code>false</code>.
	 */
	public boolean setOptionalStatisticEntry(String label);

	/**
	 * Closes the underlying stream.
	 * 
	 * @throws IOException
	 *             If an error occurs while closing the underlying stream.
	 */
	public void close() throws IOException;

}
