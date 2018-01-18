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

package dude.postprocessor;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import dude.util.AbstractIterator;
import dude.util.data.DuDeObject;
import dude.util.data.DuDeObjectPair;
import dude.util.data.DuDeObjectPair.GeneratedBy;

/**
 * <code>WarshallTransitiveClosureGenerator</code> implements the Warshall algorithm to calculate the transitive closure. The goal of the algorithm is
 * to create the transitive closure of a directed graph. It creates a n x n matrix in which each element (i,j) describes whether there is an edge from
 * element i to element j.
 * 
 * As we have an undirected graph, we only have to save the edges in one half of the matrix. The graph can be implemented as an adjacency matrix
 * (array of bitsets) or as an adjacency list (map of sets).
 * 
 * The original algorithm was published in Stephen Warshall: A Theorem on Boolean Matrices. In: Journal of the ACM 9, 1962, 1, ISSN 0004-5411, S.
 * 11–12.
 * 
 * @author Uwe Draisbach
 * 
 */
public class WarshallTransitiveClosureGenerator implements Iterable<DuDeObjectPair> {

	// each DuDe-object is assigned to an integer value
	private Map<DuDeObject, Integer> mappingDuDeObj2IntValue = new HashMap<DuDeObject, Integer>();
	private Map<Integer, DuDeObject> mappingIntValue2DuDeObj = new HashMap<Integer, DuDeObject>();
	private HashSet<IntPair> liste = new HashSet<IntPair>();

	// the last created matrix
	private GraphRepresentation currentGraph;
	private boolean graphAsAdjacencyList = true;
	// if a new DuDeObject was added, then a new matrix has to be created for the next iterator
	private boolean createNewGraph = true;

	// the number of added elements
	private int counter = 0;

	/**
	 * Adds a pair to the <code>WarshallClosureGenerator</code>.
	 * 
	 * @param pair
	 *            The pair that shall be added.
	 */
	public void add(DuDeObjectPair pair) {

		// check if the first object of the pair has already been assigned to an integer value
		// if not, assign a new value, add the object to the mappings and increase the counter
		Integer id1 = this.mappingDuDeObj2IntValue.get(pair.getFirstElement());
		if (id1 == null) {
			this.mappingDuDeObj2IntValue.put(pair.getFirstElement(), Integer.valueOf(this.counter));
			this.mappingIntValue2DuDeObj.put(Integer.valueOf(this.counter), pair.getFirstElement());
			id1 = this.counter;
			this.counter++;
		}

		// check if the second object of the pair has already been assigned to an integer value
		// if not, assign a new value, add the object to the mappings and increase the counter
		Integer id2 = this.mappingDuDeObj2IntValue.get(pair.getSecondElement());
		if (id2 == null) {
			this.mappingDuDeObj2IntValue.put(pair.getSecondElement(), Integer.valueOf(this.counter));
			this.mappingIntValue2DuDeObj.put(Integer.valueOf(this.counter), pair.getSecondElement());
			id2 = this.counter;
			this.counter++;
		}

		// save the connection between in the two objects
		this.liste.add(new IntPair(id1, id2));

		// a new pair was added, so the next iterator will have to create a new matrix
		this.createNewGraph = true;
	}

	@Override
	public Iterator<DuDeObjectPair> iterator() {
		if (this.createNewGraph) {
			if (this.graphAsAdjacencyList) {
				this.currentGraph = new AdjacencyList(this.counter);
			} else {
				this.currentGraph = new AdjacencyMatrix(this.counter);
			}
			this.currentGraph.populateGraph(this.liste);
			this.currentGraph.calculateTransitiveClosure();
			this.createNewGraph = false;
			// printMatrix();
			return new TransitiveClosureIterator(this.currentGraph);
		}
		return new TransitiveClosureIterator(this.currentGraph);
	}

	/**
	 * Uses a <code>WarshallTransitiveClosureGenerator.AdjacencyMatrix</code> to represent the graph.
	 */
	public void enableAdjacencyMatrixRepresentation() {
		this.graphAsAdjacencyList = false;
	}

	/**
	 * Uses a <code>WarshallTransitiveClosureGenerator.AdjacencyList</code> to represent the graph.
	 */
	public void enableAdjacencyListRepresentation() {
		this.graphAsAdjacencyList = true;
	}

	/**
	 * Resets the settings of the current <code>WarshallClosureGenerator</code>.
	 */
	public void reset() {
		this.mappingDuDeObj2IntValue = new HashMap<DuDeObject, Integer>();
		this.mappingIntValue2DuDeObj = new HashMap<Integer, DuDeObject>();
		this.liste = new HashSet<IntPair>();
		this.currentGraph = null;
		this.createNewGraph = true;
		this.counter = 0;
	}

	/**
	 * Prints the current graph as a matrix on the standard output
	 */
	public void printGraph() {
		int i, j;
		System.out.print("   ");
		for (j = 0; j < this.currentGraph.getSize(); j++) {
			System.out.print(j + " ");
		}
		System.out.println("");
		System.out.print("----");
		for (j = 0; j < this.currentGraph.getSize(); j++) {
			System.out.print("--");
		}
		System.out.println("");

		for (i = 0; i < this.currentGraph.getSize(); i++) {
			System.out.print(i + "| ");
			for (j = 0; j < this.currentGraph.getSize(); j++) {
				if (this.currentGraph.elementIsSet(i, j)) {
					System.out.print("1 ");
				} else {
					System.out.print("0 ");
				}
			}
			System.out.println("");
		}
		System.out.println("");
	}

	/**
	 * <code>WarshallTransitiveClosureGenerator.TransitiveClosureIterator</code> is used to iterate over all pairs collected or generated by the
	 * <code>WarshallTransitiveClosureGenerator</code>.
	 * 
	 * @author Uwe Draisbach
	 */
	protected class TransitiveClosureIterator extends AbstractIterator<DuDeObjectPair> {

		private int i = 0;
		private int j = 1;
		private GraphRepresentation iteratorMatrix;

		/**
		 * Constructor of <code>WarshallTransitiveClosureGenerator.TransitiveClosureIterator</code>
		 * 
		 * @param matrix
		 *            The matrix with the
		 */
		public TransitiveClosureIterator(GraphRepresentation matrix) {
			super();
			this.iteratorMatrix = matrix;
		}

		@Override
		protected DuDeObjectPair loadNextElement() {

			boolean pairFound = false;
			DuDeObjectPair result = null;

			// return null if matrix has no elements
			if (this.iteratorMatrix.getSize() == 0) {
				return null;
			}

			// iterate over all relevant edges in the matrix
			while (!pairFound) {
				if (this.iteratorMatrix.elementIsSet(this.i, this.j)) {
					DuDeObject dudeObj1 = WarshallTransitiveClosureGenerator.this.mappingIntValue2DuDeObj.get(this.i);
					DuDeObject dudeObj2 = WarshallTransitiveClosureGenerator.this.mappingIntValue2DuDeObj.get(this.j);
					result = new DuDeObjectPair(dudeObj1, dudeObj2);
					if (WarshallTransitiveClosureGenerator.this.liste.contains(new IntPair(this.i, this.j))) {
						result.setLineage(GeneratedBy.Algorithm);
					} else {
						result.setLineage(GeneratedBy.TransitiveClosure);
					}
					pairFound = true;
				}

				if (this.j + 1 < this.iteratorMatrix.getSize()) {
					this.j++;
				} else if (this.i + 1 < this.iteratorMatrix.getSize()) {
					this.i++;
					this.j = this.i + 1;
				} else {
					break;
				}
			}

			return result;
		}

	}

	/**
	 * <code>WarshallTransitiveClosureGenerator.GraphRepresentation</code> is an interface that should be implemented by all classes representing a
	 * graph of duplicates.
	 * 
	 * @author Uwe Draisbach
	 */
	protected abstract class GraphRepresentation {

		/**
		 * Returns the number of elements in the matrix.
		 * 
		 * @return number of elements in the matrix
		 */
		abstract int getSize();

		/**
		 * Sets elements (i, j) in the matrix to true. As the matrix handles undirected edges, also element (j, i) is set to true.
		 * 
		 * @param i
		 *            Coordinate i in the matrix.
		 * @param j
		 *            Coordinate j in the matrix.
		 */
		public abstract void set(int i, int j);

		/**
		 * Checks whether there is an edge between the two elements in the graph (element is already set in the matrix).
		 * 
		 * @param i
		 *            The number of the first element.
		 * @param j
		 *            The number of the second element.
		 * @return <code>true</code>, if there is an edge between the two elements; otherwise <code>false</code>.
		 */
		public abstract boolean elementIsSet(int i, int j);

		/**
		 * Populates the graph. For each passed pair of elements, an edge in the graph is created.
		 * 
		 * @param liste
		 *            A list of integer pairs that represent the connections between the elements.
		 */
		public void populateGraph(HashSet<IntPair> liste) {
			Iterator<IntPair> iterator = liste.iterator();
			while (iterator.hasNext()) {
				IntPair ip = iterator.next();
				this.set(ip.getFirst(), ip.getSecond());
			}
		}

		/**
		 * Iterating over the matrix and searching for new connections between elements. For each element it is set to which other elements there is a
		 * path in the graph.
		 */
		public abstract void calculateTransitiveClosure();

	}

	/**
	 * <code>WarshallTransitiveClosureGenerator.AdjacencyMatrix</code> is the matrix representation of the added pairs. It creates an array of
	 * bitsets. Each bit represents a possible edge between two vertices. If the bit is the, we have an edge, otherwise not. Although checking for
	 * edges or creating edges is very cheap, the matrix requires quadratic space concerning the number of vertices. As we are having an undirected
	 * graph, the matrix saves each edge only once, which cuts the required space in half.
	 * 
	 * @author Uwe Draisbach
	 */
	protected class AdjacencyMatrix extends GraphRepresentation {

		private int size;
		private BitSet[] matrix;

		/**
		 * Constructor of <code>WarshallTransitiveClosureGenerator.AdjacencyMatrix</code>
		 * 
		 * @param elements
		 *            Number of elements (size).
		 */
		public AdjacencyMatrix(int elements) {
			this.size = elements;
			this.matrix = new BitSet[elements];
			for (int i = 0; i < elements; i++) {
				this.matrix[i] = new BitSet(elements - i);
			}
		}

		@Override
		public int getSize() {
			return this.size;
		}

		@Override
		public void set(int i, int j) {
			if (i < j) {
				this.matrix[i].set(j - i);
			} else {
				this.matrix[j].set(i - j);
			}
		}

		@Override
		public boolean elementIsSet(int i, int j) {
			if (i < j) {
				return this.matrix[i].get(j - i);
			}
			return this.matrix[j].get(i - j);
		}

		@Override
		public void calculateTransitiveClosure() {
			for (int i = 0; i < this.getSize(); i++) {
				for (int j = 0; j < this.getSize(); j++) {
					if (this.elementIsSet(j, i)) {
						for (int k = 0; k < this.getSize(); k++) {
							if (this.elementIsSet(i, k)) {
								this.set(j, k);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * <code>WarshallTransitiveClosureGenerator.AdjacencyList</code> is the adjacency list representation of the added pairs. It created a map of
	 * vertices and their connections. The list requires less space than <code>WarshallTransitiveClosureGenerator.AdjacencyMatrix</code> if the graph
	 * is sparse. As we are having an undirected graph, the map saves each edge only once, which cuts the required space in half.
	 * 
	 * @author Uwe Draisbach
	 */
	protected class AdjacencyList extends GraphRepresentation {

		private int size;
		private ArrayList<Set<Integer>> list = new ArrayList<Set<Integer>>();

		/**
		 * Constructor of <code>WarshallTransitiveClosureGenerator.AdjacencyList</code>
		 * 
		 * @param elements
		 *            Number of elements (size).
		 */
		public AdjacencyList(int elements) {

			this.size = elements;
			// this.list = new HashMap<Integer, Set<Integer>>(this.size);
			for (int i = 0; i < elements; i++) {
				this.list.add(new HashSet<Integer>());
			}
		}

		@Override
		public int getSize() {
			return this.size;
		}

		@Override
		public void set(int i, int j) {
			this.list.get(i).add(j);
			this.list.get(j).add(i);
		}

		@Override
		public boolean elementIsSet(int i, int j) {
			return this.list.get(i).contains(j);
		}

		@Override
		public void calculateTransitiveClosure() {
			Iterator<Set<Integer>> iterator_i = this.list.iterator();
			Iterator<Integer> iterator_j;
			Iterator<Integer> iterator_k;
			int i = 0;
			int j = 0;
			while (iterator_i.hasNext()) {
				iterator_j = iterator_i.next().iterator();
				while (iterator_j.hasNext()) {
					j = iterator_j.next();
					iterator_k = this.list.get(i).iterator();
					while (iterator_k.hasNext()) {
						this.set(j, iterator_k.next());
					}
				}
				i++;
			}
		}
	}

	/**
	 * <code>WarshallTransitiveClosureGenerator.IntPair</code> is used to create a pair of integer values.
	 * 
	 * @author Uwe Draisbach
	 */
	public class IntPair {

		private int first;
		private int second;

		/**
		 * Constructor of class IntPair
		 * 
		 * @param i
		 *            The first integer value.
		 * @param j
		 *            The second integer value.
		 */
		public IntPair(int i, int j) {
			if (i < j) {
				this.first = i;
				this.second = j;
			} else {
				this.first = j;
				this.second = i;
			}
		}

		/**
		 * Returns the first integer value of the integer pair.
		 * 
		 * @return first integer value of the integer pair.
		 */
		public int getFirst() {
			return this.first;
		}

		/**
		 * Returns the second integer value of the integer pair.
		 * 
		 * @return second integer value of the integer pair.
		 */
		public int getSecond() {
			return this.second;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 17;
			result = prime * result + getFirst();
			result = prime * result + getSecond();
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj != null && obj instanceof IntPair) {
				IntPair other = (IntPair) obj;
				if (this.getFirst() == other.getFirst() && this.getSecond() == other.getSecond()) {
					return true;
				}
				if (this.getFirst() == other.getSecond() && this.getSecond() == other.getFirst()) {
					return true;
				}
			}
			return false;

		}

		@Override
		public String toString() {
			return getFirst() + " / " + getSecond();
		}

	}

}
