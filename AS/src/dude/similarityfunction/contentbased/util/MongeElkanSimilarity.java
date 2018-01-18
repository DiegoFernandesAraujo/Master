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
import java.util.ArrayList;

import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.tokenisers.InterfaceTokeniser;
import uk.ac.shef.wit.simmetrics.tokenisers.TokeniserWhitespace;

/**
 * This class is a derived implementation of the Monge Elkan metric from the simmetric library. Internal String Comparison was changed to SmithWaterman.
 * Package: uk.ac.shef.wit.simmetrics.similaritymetrics.mongeelkan
 * Description: uk.ac.shef.wit.simmetrics.similaritymetrics.mongeelkan implements a
 * 
 * 
 */
public class MongeElkanSimilarity extends AbstractStringMetric implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * a constant for calculating the estimated timing cost.
     */
    private final float ESTIMATEDTIMINGCONST = 0.0344f;

    /**
     * private tokeniser for tokenisation of the query strings.
     */
    final InterfaceTokeniser tokeniser;

    /**
     * private string metric allowing internal metric to be composed.
     */
    private final AbstractStringMetric internalStringMetric;

    /**
     * gets a div class xhtml similarity explaining the operation of the metric.
     *
     * @param string1 string 1
     * @param string2 string 2
     *
     * @return a div class html section detailing the metric operation.
     */
    @Override
	public String getSimilarityExplained(String string1, String string2) {
        //todo this should explain the operation of a given comparison
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * constructor - default (empty).
     */
    public MongeElkanSimilarity() {
        this.tokeniser = new TokeniserWhitespace();
        this.internalStringMetric = new SmithWatermanDistance();
    }

    /**
     * constructor.
     *
     * @param tokeniserToUse - the tokeniser to use should a different tokeniser be required
     */
    public MongeElkanSimilarity(final InterfaceTokeniser tokeniserToUse) {
        this.tokeniser = tokeniserToUse;
        this.internalStringMetric = new SmithWatermanDistance();
    }

    /**
     * constructor.
     *
     * @param tokeniserToUse - the tokeniser to use should a different tokeniser be required
     * @param metricToUse    - the string metric to use
     */
    public MongeElkanSimilarity(final InterfaceTokeniser tokeniserToUse, final AbstractStringMetric metricToUse) {
        this.tokeniser = tokeniserToUse;
        this.internalStringMetric = metricToUse;
    }

    /**
     * constructor.
     *
     * @param metricToUse - the string metric to use
     */
    public MongeElkanSimilarity(final AbstractStringMetric metricToUse) {
        this.tokeniser = new TokeniserWhitespace();
        this.internalStringMetric = metricToUse;
    }

    /**
     * returns the string identifier for the metric.
     *
     * @return the string identifier for the metric
     */
    @Override
	public String getShortDescriptionString() {
        return "MongeElkan";
    }

    /**
     * returns the long string identifier for the metric.
     *
     * @return the long string identifier for the metric
     */
    @Override
	public String getLongDescriptionString() {
        return "Implements the Monge Elkan algorithm providing an matching style similarity measure between two strings";
    }

    /**
     * gets the estimated time in milliseconds it takes to perform a similarity timing.
     *
     * @param string1 string 1
     * @param string2 string 2
     *
     * @return the estimated time in milliseconds taken to perform the similarity measure
     */
    @Override
	public float getSimilarityTimingEstimated(final String string1, final String string2) {
        //timed millisecond times with string lengths from 1 + 50 each increment
        //0	5.97	11.94	27.38	50.75	73	109.5	148	195.5	250	297	375	437	500	594	672	781	875	969	1079	1218	1360	1469	1609	1750	1906	2063	2203	2375	2563	2734	2906	3110	3312	3500	3688	3906	4141	4375	4594	4844	5094	5328	5609	5860	6156	6422	6688	6984	7235	7547	7859	8157	8500	8813	9172	9484	9766	10125	10516
        final float str1Tokens = this.tokeniser.tokenizeToArrayList(string1).size();
        final float str2Tokens = this.tokeniser.tokenizeToArrayList(string2).size();
        return (((str1Tokens + str2Tokens) * str1Tokens) + ((str1Tokens + str2Tokens) * str2Tokens)) * this.ESTIMATEDTIMINGCONST;
    }

    /**
     * gets the similarity of the two strings using Monge Elkan.
     *
     * @param string1
     * @param string2
     * @return a value between 0-1 of the similarity
     */
    @Override
	public final float getSimilarity(final String string1, final String string2) {
        //split the strings into tokens for comparison
        final ArrayList<String> str1Tokens = this.tokeniser.tokenizeToArrayList(string1);
        final ArrayList<String> str2Tokens = this.tokeniser.tokenizeToArrayList(string2);
        
        if (str1Tokens.size() == 0){
        	return 0.0f;
        }

        float sumMatches = 0.0f;
        float maxFound;
        for (Object str1Token : str1Tokens) {
            maxFound = 0.0f;
            for (Object str2Token : str2Tokens) {
                final float found = this.internalStringMetric.getSimilarity((String) str1Token, (String) str2Token);
                if (found > maxFound) {
                    maxFound = found;
                }
            }
            sumMatches += maxFound;
        }
        return sumMatches / str1Tokens.size();
    }

    /**
     * gets the un-normalised similarity measure of the metric for the given strings.
     *
     * @param string1
     * @param string2
     * @return returns the score of the similarity measure (un-normalised)
     */
    @Override
	public float getUnNormalisedSimilarity(String string1, String string2) {
        //todo check this is valid before use mail sam@dcs.shef.ac.uk if problematic
        return getSimilarity(string1, string2);
    }
}