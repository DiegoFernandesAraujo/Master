/*
 * DuDe - The Duplicate Detection Toolkit
 * 
 * Copyright (C) 2010  Hasso-Plattner-Institut fr Softwaresystemtechnik GmbH,
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

package dude.similarityfunction.contentbased.impl.simmetrics;

import uk.ac.shef.wit.simmetrics.similaritymetrics.InterfaceStringMetric;
import dude.similarityfunction.StringSimilarity;
import dude.similarityfunction.contentbased.ContentBasedSimilarityFunction;
import dude.util.ReflectUtil;
import dude.util.data.Jsonable;
import dude.util.data.json.JsonAtomic;

/**
 * <code>SimmetricsFunction</code> is a skeleton class providing the common functionality of all <code>Simmetric</code> similarity functions.
 * 
 * @author Arvid Heise
 * @author Matthias Pohl
 * 
 * @param <T>
 *            The type that is returned by any fluent interface method.
 * @param <M>
 *            The internally used metric.
 */
public abstract class SimmetricsFunction<T extends SimmetricsFunction<T, M>, M extends InterfaceStringMetric> extends ContentBasedSimilarityFunction<T> implements StringSimilarity {

	private transient final M metric;

	/**
	 * Internal constructor for {@link Jsonable} deserialization.
	 */
	@SuppressWarnings("unchecked")
	protected SimmetricsFunction() {
		super();
		// extracts the 2nd generic type M and creates an instance of it
		this.metric = (M) ReflectUtil.newInstance(ReflectUtil.getStaticBoundTypes(this.getClass())[1].getType());
	}

	/**
	 * Initializes the <code>SimmetricsFunction</code> with the passed metric and the default values.
	 * 
	 * @param metric
	 *            The internally used metric function.
	 * @param defaultAttr
	 *            The default attributes.
	 */
	protected SimmetricsFunction(M metric, String... defaultAttr) {
		super(defaultAttr);

		if (metric == null) {
			throw new NullPointerException("The metric is missing.");
		}

		this.metric = metric;
	}

	/**
	 * Initializes the <code>SimmetricsFunction</code> with the passed metric and the default values.
	 * 
	 * @param metric
	 *            The internally used metric function.
	 * @param attrIndex
	 *            The index of the default attribute. This parameter might be used to select other values than the first one of an array.
	 * @param defaultAttr
	 *            The default attributes.
	 */
	protected SimmetricsFunction(M metric, int attrIndex, String... defaultAttr) {
		super(attrIndex, defaultAttr);
                
                this.metric = metric; //Adicionei aqui em cima

                try{//Eu que coloquei essa bagaça aqui, mas não adiantou de muita coisa.
                    
		if (this.metric == null) {
			throw new NullPointerException("The metric is missing.");
		}
                }catch(Exception e){
                    System.out.println("Fudeu!");
                }

		//this.metric = metric;
	}

	@Override
	protected double compareAtomicValues(JsonAtomic value1, JsonAtomic value2) {
		String str1 = value1.toString();
		String str2 = value2.toString();

		if (this.ignoringCapitalizationEnabled()) {
			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();
		}

		return this.metric.getSimilarity(str1, str2);
	}

	@Override
	public double getSimilarity(String str1, String str2) {
		if (str1 == null || str2 == null) {
			return 0.0;
		}
		
		return this.metric.getSimilarity(str1, str2);
	}

}
