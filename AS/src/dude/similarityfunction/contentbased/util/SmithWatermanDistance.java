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

package dude.similarityfunction.contentbased.util;

import java.io.Serializable;

import uk.ac.shef.wit.simmetrics.math.MathFuncs;
import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.costfunctions.AbstractSubstitutionCost;
import uk.ac.shef.wit.simmetrics.similaritymetrics.costfunctions.SubCost1_Minus2;

/**
 * <code>SmithWatermanDistance</code> implements the Smith-Waterman distance.
 * 
 * @author Ziawasch Abedjan
 */
public final class SmithWatermanDistance extends AbstractStringMetric implements Serializable {

	private static final long serialVersionUID = -8246363825037351820L;

	/**
	 * a constant for calculating the estimated timing cost.
	 */
	private final float ESTIMATEDTIMINGCONST = 1.61e-4f;

	/**
	 * the private cost function used in the levenstein distance.
	 */
	private AbstractSubstitutionCost dCostFunc;

	/**
	 * the cost of a gap.
	 */
	private float gapCost;

	/**
	 * constructor - default (empty).
	 */
	public SmithWatermanDistance() {
		// set the gapCost to a default value
		this.gapCost = 0.5f;
		// set the default cost func
		this.dCostFunc = new SubCost1_Minus2();
	}

	/**
	 * constructor.
	 * 
	 * @param costG
	 *            - the cost of a gap
	 */
	public SmithWatermanDistance(final float costG) {
		// set the gapCost to a given value
		this.gapCost = costG;
		// set the cost func to a default function
		this.dCostFunc = new SubCost1_Minus2();
	}

	/**
	 * constructor.
	 * 
	 * @param costG
	 *            - the cost of a gap
	 * @param costFunc
	 *            - the cost function to use
	 */
	public SmithWatermanDistance(final float costG, final AbstractSubstitutionCost costFunc) {
		// set the gapCost to the given value
		this.gapCost = costG;
		// set the cost func
		this.dCostFunc = costFunc;
	}

	/**
	 * constructor.
	 * 
	 * @param costFunc
	 *            - the cost function to use
	 */
	public SmithWatermanDistance(final AbstractSubstitutionCost costFunc) {
		// set the gapCost to a default value
		this.gapCost = 0.5f;
		// set the cost func
		this.dCostFunc = costFunc;
	}

	/**
	 * gets the gap cost for the distance function.
	 * 
	 * @return the gap cost for the distance function
	 */
	public float getGapCost() {
		return this.gapCost;
	}

	/**
	 * sets the gap cost for the distance function.
	 * 
	 * @param gapCost
	 *            the cost of a gap
	 */
	public void setGapCost(final float gapCost) {
		this.gapCost = gapCost;
	}

	/**
	 * get the d(i,j) cost function.
	 * 
	 * @return AbstractSubstitutionCost cost function used
	 */
	public AbstractSubstitutionCost getdCostFunc() {
		return this.dCostFunc;
	}

	/**
	 * sets the d(i,j) cost function used.
	 * 
	 * @param dCostFunc
	 *            - the cost function to use
	 */
	public void setdCostFunc(final AbstractSubstitutionCost dCostFunc) {
		this.dCostFunc = dCostFunc;
	}

	/**
	 * returns the string identifier for the metric .
	 * 
	 * @return the string identifier for the metric
	 */
	@Override
	public String getShortDescriptionString() {
		return "SmithWaterman";
	}

	/**
	 * returns the long string identifier for the metric.
	 * 
	 * @return the long string identifier for the metric
	 */
	@Override
	public String getLongDescriptionString() {
		return "Implements the Smith-Waterman algorithm providing a similarity measure between two string";
	}

	/**
	 * gets a div class xhtml similarity explaining the operation of the metric.
	 * 
	 * @param string1
	 *            string 1
	 * @param string2
	 *            string 2
	 * 
	 * @return a div class html section detailing the metric operation.
	 */
	@Override
	public String getSimilarityExplained(String string1, String string2) {
		// todo this should explain the operation of a given comparison
		return null; // To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * gets the estimated time in milliseconds it takes to perform a similarity timing.
	 * 
	 * @param string1
	 *            string 1
	 * @param string2
	 *            string 2
	 * 
	 * @return the estimated time in milliseconds taken to perform the similarity measure
	 */
	@Override
	public float getSimilarityTimingEstimated(final String string1, final String string2) {
		// timed millisecond times with string lengths from 1 + 50 each increment
		// 0 0.58 1.49 3.17 5.97 8.83 12.69 18.45 22.56 31.29 36.5 40.6 58.5 62.5 78.33 78 93.67 101.5 125 125 164 172 187.5 203 235 265 235 328 328
		// 344 343 391 391 453 453 484 547 578 594 625 641 672 718 766 797 812 860 890 907 953 984 1078 1047 1094 1156 1188 1250 1328 1344 1390
		final float str1Length = string1.length();
		final float str2Length = string2.length();
		return ((str1Length * str2Length) + str1Length + str2Length) * this.ESTIMATEDTIMINGCONST;
	}

	/**
	 * gets the similarity of the two strings using Needleman Wunch distance.
	 * 
	 * @param string1
	 * @param string2
	 * @return a value between 0-1 of the similarity
	 */
	@Override
	public float getSimilarity(final String string1, final String string2) {
		final float smithWaterman = getUnNormalisedSimilarity(string1, string2);

		// normalise into zero to one region from min max possible
		float maxValue = Math.max(string1.length(), string2.length());
		if (this.dCostFunc.getMaxCost() > -this.gapCost) {
			maxValue *= this.dCostFunc.getMaxCost();
		} else {
			maxValue *= -this.gapCost;
		}

		// check for 0 maxLen
		if (maxValue == 0) {
			return 1.0f; // as both strings identically zero length
		}

		// return actual / possible NeedlemanWunch distance to get 0-1 range
		return (smithWaterman / maxValue);
	}

	/**
	 * implements the Smith-Waterman distance function //see http://www.gen.tcd.ie/molevol/nwswat.html for details .
	 * 
	 * @param s
	 * @param t
	 * @return the Smith-Waterman distance for the given strings
	 */
	@Override
	public float getUnNormalisedSimilarity(final String s, final String t) {
		final float[][] d; // matrix
		final int n; // length of s
		final int m; // length of t
		int i; // iterates through s
		int j; // iterates through t
		float cost; // cost

		// check for zero length input
		n = s.length();
		m = t.length();

		if (m == 0 || n == 0) {
			return 0;
		}

		// create matrix (n)x(m)
		d = new float[n][m];

		// process first row and column first as no need to consider previous rows/columns
		float maxSoFar = 0.0f;
		for (i = 0; i < n; i++) {
			// get the substution cost
			cost = this.dCostFunc.getCost(s, i, t, 0);

			if (i == 0) {
				d[0][0] = MathFuncs.max3(0, -this.gapCost, cost);
			} else {
				d[i][0] = MathFuncs.max3(0, d[i - 1][0] - this.gapCost, cost);
			}
			// update max possible if available
			if (d[i][0] > maxSoFar) {
				maxSoFar = d[i][0];
			}
		}
		for (j = 0; j < m; j++) {
			// get the substution cost
			cost = this.dCostFunc.getCost(s, 0, t, j);

			if (j == 0) {
				d[0][0] = MathFuncs.max3(0, -this.gapCost, cost);
			} else {
				d[0][j] = MathFuncs.max3(0, d[0][j - 1] - this.gapCost, cost);
			}
			// update max possible if available
			if (d[0][j] > maxSoFar) {
				maxSoFar = d[0][j];
			}
		}

		// cycle through rest of table filling values from the lowest cost value of the three part cost function
		for (i = 1; i < n; i++) {
			for (j = 1; j < m; j++) {
				// get the substution cost
				cost = this.dCostFunc.getCost(s, i, t, j);

				// find lowest cost at point from three possible
				d[i][j] = MathFuncs.max4(0, d[i - 1][j] - this.gapCost, d[i][j - 1] - this.gapCost, d[i - 1][j - 1] + cost);
				// update max possible if available
				if (d[i][j] > maxSoFar) {
					maxSoFar = d[i][j];
				}
			}
		}

		// return max value within matrix as holds the maximum edit score
		return maxSoFar;
	}
}