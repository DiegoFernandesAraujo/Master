/*
 * DuDe - The Duplicate Detection Toolkit
 * 
 * Copyright (C) 2011  Hasso-Plattner-Institut für Softwaresystemtechnik GmbH,
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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Vector;

import dude.algorithm.Algorithm;
import dude.util.GoldStandard;
import dude.util.data.AutoJsonable;
import dude.util.data.DuDeObject;
import dude.util.data.DuDeObjectPair;
import dude.util.data.Jsonable;

/**
 * <code>ExtendedStatisticComponent</code> provides functionality for gathering statistics concerning different 
 * measures that can be realized with the Generalized Merge Distance (GMD). Up to this day, included are Precision, 
 * Recall, F1, Variation of Information, Basic Merge Distance, and a hybrid measure. Therefore, a collection 
 * of real duplicates has to be added. 
 * The GMD clusters the duplicate pairs for the computation and thus generates the transitive closure of the 
 * duplicates, which implies that the set of duplicate pairs detected is COMPLETE, otherwise the results of 
 * the GMD concerning Precision/Recall/F1 will DIFFER from the ones of the <code>StatisticComponent</code> 
 * (there the results are based on the actual amount of duplicate pairs detected)!
 * 
 * @author Cindy Faehnrich
 */

public class ExtendedStatisticComponent extends StatisticComponent implements
		AutoJsonable {
	
	public enum Config {
	     DEFAULT() {
	    	 
	    	@Override 
	    	public double fSplit(double x, double y){
	    		return 1; //default config of 1		
	    	}
	    	
	    	@Override
	    	public double fMerge(double x, double y){
	    		return 1; //default config of 1
	    	}
	    	
	    	@Override
			public String getLabel(){
				return "GMD Basic";
			}
	    	
	     },
	     
	     RECALL() {
	    	 
		    @Override 
		    public double fSplit(double x, double y){
		    	return 0;	
		    }
		    
		    @Override
		    public double fMerge(double x, double y){
		    	return x * y; 
		    }
		    
		    @Override
			public String getLabel(){
				return "GMD Recall";
			}
		 },
		 PRECISION() {
		   	 
		    @Override 
		    public double fSplit(double x, double y){
		    	return x * y;	
		    }
		    
		    @Override
		    public double fMerge(double x, double y){
		    	return 0;
		    }
		    
		    @Override
			public String getLabel(){
				return "GMD Precision";
			}
		  },
		  VARIATION_INFORMATION() {
		     
		    @Override 
		    public double fSplit(double x, double y){
		    	return h(x+y) - h(x) - h(y); 	
		    }
		    	
		    @Override
		    public double fMerge(double x, double y){
		    	return h(x+y) - h(x) - h(y);
		    }
		    
		    public double h(double z){
		    	return ((double)z/this.getNumBaseRecords() *  Math.log((double) z/this.getNumBaseRecords()));
		    }
		    
		    @Override
			public String getLabel(){
				return "GMD Variation of Information";
			}
		  },
		  HYBRID() {
		    	 
			@Override 
			public double fSplit(double x, double y){
				return (x * y) + 1;		
			}
			   	
			@Override
			public double fMerge(double x, double y){
				return (x * y) + 1; 
			}
			
			@Override
			public String getLabel(){
				return "GMD Hybrid";
			}	
		  },
		 
		  PAIRWISE_F1() {
			  
			  @Override 
				public double fSplit(double x, double y){
					return 0; //not used for pairwise	
				}
				   	
				@Override
				public double fMerge(double x, double y){
					return 0; //not used for pairwise
				}
			  
			  @Override
				public String getLabel(){
					return "GMD Pairwise F1";
				}
		  };
	     /**
 		 * Computes the costs for a split operation
 		 * @param x
 		 * 			the size of cluster x_i that was split off from the original cluster
 		 * @param y
 		 * 			The size of cluster y_i that was split off as other half of the original cluster
 		 * @return The costs for a split operation
 		 */
 		public abstract double fSplit(double x, double y);
 		
 		/**
		 * Computes the costs for a merge operation
		 * @param x
		 * 			The size of cluster x_i that is merged with y_i
		 * @param y
		 * 			The size of cluster y_i that is merged with x_i
		 * @return The costs for a merge operation
		 */	
		public abstract double fMerge(double x, double y);
		
		/**
		 * Returns the label of the configuration for the output components
		 * @return The label of the configuration
		 */
		public abstract String getLabel();
		
		/**
		 * To check if the current config measure should be used for evaluation.
		 */
		public boolean activated = false;
		
		/**
		 * Activates usage of the current config measure.
		 */
		public void activate(){
			this.activated = true;
		 }
		
		/**
		 * Deactivates usage of the current config measure.
		 */
		public void deactivate(){
			this.activated = false;
		}
		
		/**
		 * Checks if current config measure is activated for usage.
		 */
		public boolean isActivated(){
			  return this.activated;
		}
		
		/**
		 * Total number of base records, needed for computation of VI measure. 
		 */
		public int n = 1;
		
		/**
		 * Setter for total number of base records, needed for computation of VI.
		 * @param n
		 * 			Total number of base records to set.
		 */
		public void setNumBaseRecords(int n){
			this.n = n;
		}
		/**
		 * Getter for total number of base records, needed for computation of VI.
		 * @return n
		 * 			Total number of base records currently set.
		 */
		public int getNumBaseRecords(){
			return this.n;
		}
	}
	/**
	 * Final ER result in cluster format.
	 */
	public Vector<Vector<DuDeObject>> resultCluster = new Vector<Vector<DuDeObject>>(); 
	private Config config;
	/**
	 * Cluster of each dudeobject in its own cluster - needed for pF1 calculation.
	 */
	private Vector<Vector<DuDeObject>> singleClusters = new Vector<Vector<DuDeObject>>(); 
	/**
	 * Collection of all duplicate pairs of the comparisons.
	 */
	public transient final Collection<DuDeObjectPair> duplicates = new HashSet<DuDeObjectPair>();
	/**
	 * Initializes an <code>ExtendedStatisticComponent</code> with no gold standard and 
	 * default configuration for GMD.
	 * 
	 * @param algorithm
	 *            The used algorithm.
	 */
	public ExtendedStatisticComponent(Algorithm algorithm) {
		this(null, algorithm, Config.DEFAULT);
	}
	
	/**
	 * Initializes an <code>ExtendedStatisticComponent</code> using the passed {@link DuDeObjectPair}s as real duplicates.
	 * 
	 * @param goldStandard
	 *            The gold standard which these statistics are based on.
	 * @param algorithm
	 *            Used algorithm.
	 * @param config
	 *            Used configuration of GMD.
	 */
	public ExtendedStatisticComponent(GoldStandard goldStandard, Algorithm algorithm, Config config) {
		this.goldStandard = goldStandard;
		this.algorithm = algorithm;
		if (this.goldStandard != null) this.setExtractedData();
		this.config = config;
	}

	/**
	 * Internal constructor for {@link Jsonable} deserialization.
	 */
	protected ExtendedStatisticComponent() {
		// nothing to do
		this.config = Config.DEFAULT;
		this.activateDefault();
	}
	/**
	 * Activates the usage of PairwiseF1 (and by that Precision and Recall) as GMD evaluation measure.
	 */
	public void activateF1(){
		Config.PAIRWISE_F1.activate();
		Config.RECALL.activate();
		Config.PRECISION.activate();
	}
	/**
	 * Deactivates the usage of PairwiseF1 as GMD evaluation measure.
	 */
	public void deactivateF1(){
		Config.PAIRWISE_F1.deactivate();
	}
	/**
	 * Activates the usage of Recall as GMD evaluation measure.
	 */
	public void activateRECALL(){
		Config.RECALL.activate();
	}
	/**
	 * Deactivates the usage of Recall as GMD evaluation measure.
	 */
	public void deactivateRECALL(){
		Config.RECALL.deactivate();
	}
	/**
	 * Activates the usage of Precision as GMD evaluation measure.
	 */
	public void activatePRECISION(){
		Config.PRECISION.activate();
	}
	/**
	 * Deactivates the usage of Precision as GMD evaluation measure.
	 */
	public void deactivatePRECISION(){
		Config.PRECISION.deactivate();
	}
	/**
	 * Activates the usage of Hybrid GMD as evaluation measure.
	 */
	public void activateHybrid(){
		Config.HYBRID.activate();
	}
	/**
	 * Deactivates the usage of Hybrid GMD as evaluation measure.
	 */
	public void deactivateHybrid(){
		Config.HYBRID.deactivate();
	}
	/**
	 * Activates the usage of the Default GMD as evaluation measure.
	 */
	public void activateDefault(){
		Config.DEFAULT.activate();
	}
	/**
	 * Deactivates the usage of the Default GMD as evaluation measure.
	 */
	public void deactivateDefault(){
		Config.DEFAULT.deactivate();
	}
	/**
	 * Activates the usage of the Variation of Information as GMD evaluation measure.
	 */
	public void activateVI(){
		Config.VARIATION_INFORMATION.activate();
	}
	/**
	 * Deactivates the usage of the Variation of Information as GMD evaluation measure.
	 */
	public void deactivateVI(){
		Config.VARIATION_INFORMATION.deactivate();
	}
	/**
	 * Sets the cost configuration to use for GMD processing
	 * @param config
	 * 			{@link Config} attribute. 
	 */
	public void setConfig(Config config){
		this.config = config;
	}
	
	/**
	 * Returns the current cost configuration set for GMD processing
	 * @return
	 * 		The current configuration
	 */
	public Config getConfig(){
		return this.config;
	}
	/**
	 * Checks whether this StatisticComponent calculates the Generalized Merge Distance. Important for the output components.
	 * 
	 * @return true since this is the extended component with GMD computation
	 */
	public boolean hasGMD(){
		return true;
	}
	
	/**
	 * Sets the size of the extracted data. Necessary for generating the unique clusters for the
	 * gold standard.
	 */
	public void setExtractedData(){
		this.goldStandard.setExtractedData(this.algorithm.getExtractedData());
	}
	/**
	 * Returns the Generalized Merge Distance based on the current cost configurations.
	 * @param goldCluster
	 *            The Gold Standard in cluster format
	 * @param erCluster
	 * 			  The questionable ER result in cluster format 
	 * @return The GMD for the given cluster sets and configurations.
	 */
	public double computeGMD(Collection<? extends Collection<DuDeObject>> erCluster, Collection<? extends Collection<DuDeObject>> goldCluster){
		//m is a hashmap that assigns the cluster Id to each DuDeObject
		HashMap<DuDeObject, Integer> m = new HashMap<DuDeObject, Integer>();
		//assigns to each cluster the amount of entities contained: rSizes[Cluster1] -> 3 Entities
		HashMap<Integer, Integer> rSizes = new HashMap<Integer, Integer>();
		
		//build up m and rSizes
		Iterator<? extends Collection<DuDeObject>> it1 = erCluster.iterator();
		int i = 0;
		while (it1.hasNext()){
			Collection<DuDeObject> ri = it1.next();
			Iterator<DuDeObject> it2 = ri.iterator();
			
			while (it2.hasNext()){
				m.put(it2.next(), new Integer(i));
			}
			rSizes.put(i, ri.size());
			i++;
		}
		//begin computing costs
		double cost = 0;		
		Iterator<? extends Collection<DuDeObject>> it3 = goldCluster.iterator();
		//iterate over the clusters in the gold standard
		while (it3.hasNext()){
			Collection<DuDeObject> si = it3.next();
			//assign for every cluster of R an occurence
			HashMap<Integer, Integer> pMap = new HashMap<Integer, Integer>();
			
			Iterator<DuDeObject> it4 = si.iterator();
			//for each entity in the cluster
			while (it4.hasNext()){
				//check to what cluster in R this entity belongs
				int clusterIndex = findCluster(m, it4.next());
				//increment the counter for this cluster of R 
				if (!pMap.keySet().contains(clusterIndex))
					pMap.put(clusterIndex, 0);
				int y = pMap.get(clusterIndex) + 1;
				pMap.put(clusterIndex, y); //if there are 3 items in R_i also contained in S_j, then pMap[i]--> 3
			}
			double siCost = 0;
			int totalRecs = 0;
			//iterate over the filled pMap
			Iterator<Entry<Integer,Integer>> iterator = pMap.entrySet().iterator();
			while (iterator.hasNext()){
				Entry<Integer,Integer> entry = iterator.next();
				//if the cluster of R contains more entities than counted in pMap, split off the difference
				if (rSizes.get(entry.getKey()) > entry.getValue()){
					siCost += this.getConfig().fSplit((float)entry.getValue(), (float)(rSizes.get(entry.getKey()) - entry.getValue()));
				}
				//update rSizes for split off clusters
				int y = rSizes.get(entry.getKey()) - entry.getValue();
				rSizes.put(entry.getKey(), y);
				//add costs for merging
				if (totalRecs > 0){
					siCost += this.getConfig().fMerge(entry.getValue(), totalRecs);
				}
				//add amount of entities split off
				totalRecs += entry.getValue();
			}
			cost += siCost;
			
		}
		return cost;
	}
	
	/**
	 * Searches for the value of a given key in a hashmap by comparing their 
	 * ids (from source and object)
	 * @param m
	 * 			The hashmap that assigns a cluster id to each record
	 * @param item
	 * 			The DuDeObject whose cluster id in R is searched for 
	 * @return The cluster id the given record has in R.
	 */
	public int findCluster(HashMap<DuDeObject, Integer> m, DuDeObject item){
		Iterator<DuDeObject> keys = m.keySet().iterator();
		while (keys.hasNext()){
			DuDeObject clusterItem = keys.next();
			
			if (clusterItem.equalsID(item)) 
					return m.get(clusterItem);
			//if (clusterItem.equalsID(item)) return m.get(clusterItem);
		}
		return 0;
	}

	/**
	 * Returns a value normalized to 1 of the first given value, where the second value is the maximum.
	 * Needed for the computation of precision and recall.
	 * @param val1
	 * 			Float value to normalize.
	 * @param val2
	 * 			Maximal possible value val1 could reach.
	 * @return
	 * 		The to 1 normalized value of val1.
	 */
	public double normalize(double val1, double val2){
		return 1 - (val1/val2);
	}
	
	/**
	 * Computation of the f1 measure by calculating the harmonic mean of the given
	 * precision and recall.
	 * @param precision
	 * 			Precision value (normalized to 1).
	 * @param recall
	 * 			Recall value (normalized to 1).
	 * @return
	 * 		The pairwise f1 measure value (between 0 and 1).
	 */
	public double computeF1(double precision, double recall){
		double f1 = (2 * precision * recall) / (precision + recall);
		return f1;
	}
	
	/**
	 * Returns a list of clusters with each record alone in its cluster. Built from
	 * the current gold standard
	 */
	public Vector<Vector<DuDeObject>> computeSingleClusters(Vector<Vector<DuDeObject>> clusteredRecords){
		this.singleClusters.clear();
		Iterator<Vector<DuDeObject>> it = clusteredRecords.iterator();
		while (it.hasNext()){
			Iterator<DuDeObject> it2 = it.next().iterator();
			while (it2.hasNext()){
				Vector<DuDeObject> cluster = new Vector<DuDeObject>();
				cluster.add(it2.next());
				this.singleClusters.add(cluster);
			}
		}
		return this.singleClusters;
	}
	
	
	/**
	 * Processes the Generalized Merge Distance regarding the current configuration if it is
	 * activated.
	 * @return
	 * 		The final costs needed to transform the ER result to the gold standard.
	 */
	public double processGMD(){
		double costs = 0;
		this.setExtractedData();
		this.createResultCluster(); //creates the ER result in cluster form
		Vector<Vector<DuDeObject>> goldCluster = this.goldStandard.getCluster();
		switch(this.config){
			case PRECISION: {
				if (this.singleClusters.size() == 0){
					this.singleClusters = computeSingleClusters(this.resultCluster);
				}
			
				double precision1 = computeGMD(this.resultCluster, goldCluster);
				double precision2 = computeGMD(this.resultCluster, this.singleClusters);
				costs = normalize(precision1, precision2);
				break;
			}
			case RECALL: {
				if (this.singleClusters.size() == 0){
					this.singleClusters = computeSingleClusters(this.resultCluster);
				}
				
				Iterator<Vector<DuDeObject>> it1 = this.resultCluster.iterator();
				int count = 0;
				while (it1.hasNext()){
					int size = it1.next().size();
					count += (size * (size-1))/2;
				}
				double recall1 = computeGMD(this.resultCluster, goldCluster);
				double recall2 = computeGMD(this.singleClusters, goldCluster);
				costs = normalize(recall1, recall2);
				break;
			}
			case PAIRWISE_F1: {
				this.setConfig(Config.PRECISION);
				double precision = this. processGMD();
				this.setConfig(Config.RECALL);
				double recall = this.processGMD();
		
				this.setConfig(Config.PAIRWISE_F1);
				costs = computeF1(precision, recall);
				break;
			}
			case VARIATION_INFORMATION: {
				this.config.setNumBaseRecords(this.algorithm.getDataSize());
				costs = computeGMD(this.resultCluster, goldCluster); 
				break;
			}
			default: {
				costs = computeGMD(this.resultCluster, goldCluster); 
				break;
			}	
		}
		return costs;
	}
	
	/**
	 * Creates a Vector of Vector of DuDeObjects from the given data structure (Transform gold standard
	 * from duplicate pairs to cluster format).
	 */
	public void createResultCluster(){
		//generate transitive closure = clusters from the duplicate pairs
		NaiveTransitiveClosureGenerator transClosGenerator = new NaiveTransitiveClosureGenerator();
		transClosGenerator.add(this.duplicates);
		Vector<Vector<DuDeObject>> newClusters = new Vector<Vector<DuDeObject>>();
		Collection<Collection<DuDeObject>> resultCluster = (Collection<Collection<DuDeObject>>) transClosGenerator.getTransitiveClosures();
		for (Collection <DuDeObject> cluster : resultCluster){
			Vector<DuDeObject> newCluster = new Vector<DuDeObject>();
			for (DuDeObject record : cluster) newCluster.add(record);
			newClusters.add(newCluster);
		}
		
		//generate single-record-clusters for unique records
		this.resultCluster = this.generateClustersForUnique(newClusters);
	}
	/**
	 * Generates for each unique item (that is not listed in the pairwise er result) its own cluster
	 * and adds it to the given clusters.
	 * @param clusters
	 * 			The ER result in cluster format, without the unique items.
	 * @return The ER standard in cluster format, with the unique items in their own cluster.
	 */
	public Vector<Vector<DuDeObject>> generateClustersForUnique(Vector<Vector<DuDeObject>> clusters){
		//take datasize, find out what id is missing and create a new DuDeObject form each left id
		/*Vector<Integer> ids = new Vector<Integer>();
		for (int i = 1; i <= this.algorithm.getDataSize(); i++) ids.add(i);
		for (Vector<DuDeObject> cluster : clusters){
			for (DuDeObject record : cluster){
				Integer id = Integer.valueOf(record.getObjectId().get(0).toString().trim());
				ids.remove(id);
			}
		}*/
		
		Vector<DuDeObject> extractedRecords = this.algorithm.getExtractedData();
		for (Vector<DuDeObject> cluster : clusters){
			for (DuDeObject record : cluster){
				extractedRecords.remove(record);
			}
		}
		
		//from the objects that remain, generate a single cluster for each
		for (DuDeObject uniqueRecord : extractedRecords){
			Vector<DuDeObject> cluster = new Vector<DuDeObject>();
			cluster.add(uniqueRecord);
			clusters.add(cluster);
		}
		return clusters;
	}
	/**
	 * Adds a {@link DuDeObjectPair} to the knowledge base that is labeled as a detected duplicate and
	 * the gold standard's duplicate pairs.
	 * 
	 * @param pair
	 *            A detected duplicate.
	 * @param actualComparison
	 *            <code>true</code>, if the pair should be counted as comparison; otherwise <code>false</code>.
	 */
	public void addDuplicate(DuDeObjectPair pair, boolean actualComparison) {
		
		super.addDuplicate(pair, actualComparison);
		this.duplicates.add(pair);
	}	
}
