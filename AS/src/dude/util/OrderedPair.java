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

package dude.util;

import java.util.IdentityHashMap;
import java.util.Map;

import dude.util.data.DuDeObjectPair;

/**
 * <code>OrderedPair</code> extends {@link Pair} in this way that both elements has to have the same type. Additionally both elements should be
 * {@link Comparable} in order to set the smaller element at position 1.
 * 
 * @author Matthias Pohl
 * 
 * @param <T>
 *            The type of both elements. <code><T></code> has to implement <code>Comparable</code>.
 */
public class OrderedPair<T extends Comparable<T>> extends Pair<T, T> {

	private final Map<String, Object> properties = new IdentityHashMap<String, Object>();

	/**
	 * Initializes an empty pair.
	 */
	public OrderedPair() {
		super();
	}

	/**
	 * Creates a <code>OrderedPair</code> instance with the passed objects. The order of the elements does not matter since they are stored depending
	 * on which of the elements is the smaller one.
	 * 
	 * @param elem1
	 *            The first element.
	 * @param elem2
	 *            The second element.
	 */
	public OrderedPair(T elem1, T elem2) {
		this(elem1, elem2, elem1.compareTo(elem2));
	}

	private OrderedPair(T elem1, T elem2, int compareResult) {
		super(compareResult <= 0 ? elem1 : elem2, compareResult <= 0 ? elem2 : elem1);
	}

	/**
	 * Checks whether the passed property is set.
	 * 
	 * @param key
	 *            The name of the property.
	 * @return <code>true</code>, if the property is set; otherwise <code>false</code>.
	 */
	public boolean hasProperty(String key) {
		return this.properties.containsKey(key);
	}

	/**
	 * Returns the value of the passed property.
	 * 
	 * @param key
	 *            The name of the property.
	 * @return The value of the property or <code>null</code>, if the property is not set.
	 */
	public Object getProperty(String key) {
		return this.properties.get(key);
	}

	/**
	 * Sets the passed property.
	 * 
	 * @param key
	 *            The name of the property.
	 * @param value
	 *            The value of the property.
	 * @return The old value of the property or <code>null</code>, if it was not set, yet.
	 */
	public Object setProperty(String key, Object value) {
		return this.properties.put(key, value);
	}

	/**
	 * Copies the properties from the passed pair into the current instance. Properties that were already set are not removed unless another value is
	 * stored in the property list that shall be copied.
	 * 
	 * @param pair
	 *            The {@link DuDeObjectPair} whose properties shall be copied.
	 */
	public void copyPropertiesFrom(OrderedPair<T> pair) {
		for (Map.Entry<String, Object> entry : pair.properties.entrySet()) {
			this.properties.put(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Resets the pair's elements. The order of the passed element may vary depending on the compareTo result.
	 * 
	 * @param elem1
	 *            The new element no #1.
	 * @param elem2
	 *            The new element no #2.
	 */
	public void setElements(T elem1, T elem2) {
		if (elem1 != null && elem1.compareTo(elem2) > 0) {
			super.setFirstElement(elem2);
			super.setSecondElement(elem1);
		} else {
			super.setFirstElement(elem1);
			super.setSecondElement(elem2);
		}
	}

	@Override
	public void setFirstElement(T elem) {
		this.setElements(elem, this.getSecondElement());
	}

	@Override
	public void setSecondElement(T elem) {
		this.setElements(this.getFirstElement(), elem);
	}
}
