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

package dude.similarityfunction.aggregators;

import java.util.ArrayList;
import java.util.Collection;

import dude.similarityfunction.AbstractSimilarityFunction;
import dude.similarityfunction.SimilarityFunction;
import dude.util.Pair;
import dude.util.data.DuDeObject;
import dude.util.data.DuDeObjectPair;
import dude.util.data.Jsonable;

/**
 * <code>Aggregator</code> aggregates the similarities returned by different {@link SimilarityFunction}s.
 * 
 * @author Matthias Pohl
 */
public abstract class Aggregator extends AbstractSimilarityFunction {

	private final Collection<Pair<SimilarityFunction, Integer>> similarityFunctions = new ArrayList<Pair<SimilarityFunction, Integer>>();

	/**
	 * Internal constructor for {@link Jsonable} deserialization.
	 */
	protected Aggregator() {
		super();
	}
	
	/**
	 * Initializes a <code>MultiDuDeObjectComparator</code> with a number of sub-comparators.
	 * 
	 * @param simFunctions
	 *            The {@link SimilarityFunction}s of which the aggregated similarity shall be calculated.
	 */
	public Aggregator(SimilarityFunction... simFunctions) {
		for (SimilarityFunction similarityFunction : simFunctions) {
			this.add(similarityFunction);
		}
	}

	/**
	 * Adds a {@link SimilarityFunction} to this <code>Aggregator</code> with no special multiplier.
	 * 
	 * @param simFunction
	 *            The <code>SimilarityFunction</code> to add.
	 */
	public void add(SimilarityFunction simFunction) {
		this.add(simFunction, 1);
	}

	/**
	 * Adds a {@link SimilarityFunction} to this <code>Aggregator</code> with the passed multiplier.
	 * 
	 * @param multiplier
	 *            The multiplier that corresponds to the passed <code>SimilarityFunction</code>.
	 * @param simFunction
	 *            The <code>SimilarityFunction</code> to add.
	 * 
	 * @throws NullPointerException
	 *             If no <code>SimilarityFunction</code> was passed.
	 * @throws IllegalArgumentException
	 *             If the passed multiplier is less than 0.
	 */
	public void add(SimilarityFunction simFunction, int multiplier) {
		if (simFunction == null) {
			throw new NullPointerException("No SimilarityFunction was passed.");
		} else if (multiplier < 0) {
			throw new IllegalArgumentException("Negative multipliers are not allowed.");
		}

		this.similarityFunctions.add(new Pair<SimilarityFunction, Integer>(simFunction, multiplier));
	}

	@Override
	protected double calculateSimilarity(DuDeObject obj1, DuDeObject obj2) {
		double[] similarities = new double[this.similarityFunctions.size()];
		int[] multipliers = new int[this.similarityFunctions.size()];

		SimilarityValidationState currentState = SimilarityValidationState.BothValid;

		int i = 0;
		for (Pair<SimilarityFunction, Integer> similarityFunctionMultiplierPair : this.similarityFunctions) {
			final SimilarityFunction similarityFunction = similarityFunctionMultiplierPair.getFirstElement();
			final Integer multiplier = similarityFunctionMultiplierPair.getSecondElement();

			similarities[i] = similarityFunction.getSimilarity(new DuDeObjectPair(obj1, obj2));
			multipliers[i] = multiplier;

			if (currentState == SimilarityValidationState.BothValid) {
				currentState = similarityFunction.getLastValidationState();
			} else if ((currentState == SimilarityValidationState.Value1ValidOnly && similarityFunction.getLastValidationState() == SimilarityValidationState.Value2ValidOnly)
					|| (currentState == SimilarityValidationState.Value2ValidOnly && similarityFunction.getLastValidationState() == SimilarityValidationState.Value1ValidOnly)
					|| similarityFunction.getLastValidationState() == SimilarityValidationState.BothInvalid) {
				currentState = SimilarityValidationState.BothInvalid;
			}

			i++;
		}

		this.setValidationState(currentState);
		
		return this.getAggregatedSimilarity(similarities, multipliers);
	}

	/**
	 * Aggregates the passed similarities and returns the aggregated similarity.
	 * 
	 * @param similarities
	 *            The similarities on which the aggregated similarity is based.
	 * @param multipliers
	 *            The multipliers.
	 * @return The aggregated similarity.
	 */
	protected abstract double getAggregatedSimilarity(double[] similarities, int[] multipliers);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.similarityFunctions == null) ? 0 : this.similarityFunctions.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null) {
			return false;
		}
		
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		
		Aggregator other = (Aggregator) obj;
		if (this.similarityFunctions == null) {
			if (other.similarityFunctions != null) {
				return false;
			}
		} else if (!this.similarityFunctions.equals(other.similarityFunctions)) {
			return false;
		}
		
		return true;
	}

}
