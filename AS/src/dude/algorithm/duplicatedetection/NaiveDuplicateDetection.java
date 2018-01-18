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

package dude.algorithm.duplicatedetection;

import java.util.Iterator;

import dude.algorithm.AbstractDuplicateDetection;
import dude.util.AbstractIterator;
import dude.util.data.DuDeObject;
import dude.util.data.DuDeObjectPair;

/**
 * <code>NaiveDuplicateDetection</code> implements the naive approach of checking all possible pairs. The relation that is described by the result of
 * this algorithm is neither reflexive (no [x,x] pair exists) nor symmetric (no [y,x] pair exists, if [x,y] was already produced).
 * 
 * @author Matthias Pohl
 */
public class NaiveDuplicateDetection extends AbstractDuplicateDetection {

	private class NaiveDuplicateDetectionIterator extends AbstractIterator<DuDeObjectPair> {

		private final Iterable<DuDeObject> data;

		private Iterator<DuDeObject> leftIterator;
		private Iterator<DuDeObject> rightIterator;

		private DuDeObject leftElement;
		private int leftElementCount = 0;

		public NaiveDuplicateDetectionIterator(Iterable<DuDeObject> data) {
			this.data = data;

			this.leftIterator = this.data.iterator();
			
			this.initializeRightIterator();
		}
		
		private void loadNextLeftElement() {
			if (!this.leftIterator.hasNext()) {
				this.leftElement = null;
				return;
			}
			
			this.leftElement = this.leftIterator.next();
			this.leftElementCount++;
		}

		private void initializeRightIterator() {
			// loads the next left (!) element
			this.loadNextLeftElement();
			
			if (this.endOfData()) {
				return;
			}
			
			this.rightIterator = this.data.iterator();

			for (int i = 0; i < this.leftElementCount && this.rightIterator.hasNext(); i++) {
				this.rightIterator.next();
			}
		}
		
		private boolean endOfData() {
			return this.leftElement == null;
		}

		@Override
		protected DuDeObjectPair loadNextElement() {
			if (this.endOfData()) {
				return null;
			}

			if (!this.rightIterator.hasNext()) {
				this.initializeRightIterator();
			}

			if (this.endOfData() || !this.rightIterator.hasNext()) {
				return null;
			}

			return new DuDeObjectPair(this.leftElement, this.rightIterator.next());
		}

	}

	/**
	 * Initializes a <code>NaiveDuplicateDetection</code> instance.
	 */
	public NaiveDuplicateDetection() {
		super();
	}

	@Override
	protected Iterator<DuDeObjectPair> createIteratorInstance() {
		return new NaiveDuplicateDetectionIterator(this.getData());
	}

}
