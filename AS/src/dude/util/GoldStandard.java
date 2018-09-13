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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import dude.datasource.DataSource;
import dude.exceptions.ExtractionFailedException;
import dude.postprocessor.NaiveTransitiveClosureGenerator;
import dude.util.data.AutoJsonable;
import dude.util.data.DuDeObject;
import dude.util.data.DuDeObjectId;
import dude.util.data.DuDeObjectPair;
import dude.util.data.Jsonable;
import dude.util.data.json.JsonArray;
import dude.util.data.json.JsonValue;

/**
 * <code>GoldStandard</code> implements the functionality for extracting the gold standard out of a given {@link DataSource}. Therefore it has to be
 * specified, in which attributes the information is stored. The default attributes are "id1" and "id2". If no source id is set, an empty Strings are
 * added to the generated {@link DuDeObjectPair}s. These pairs contain only references. No actual data is added.
 * 
 * @author Matthias Pohl
 * @author Ziawasch Abedjan
 * @author Cindy Faehnrich
 */
public class GoldStandard implements AutoJsonable {

	/**
	 * The default attribute for extracting the first element's object id.
	 */
	public static final String FIRST_ELEMENTS_DEFAULT_OBJECT_ID_ATTRIBUTE = "id1";

	/**
	 * The default attribute for extracting the second element's object id.
	 */
	public static final String SECOND_ELEMENTS_DEFAULT_OBJECT_ID_ATTRIBUTE = "id2";

	private DataSource source;
	/**
	 * The gold standard in cluster format.
	 */
	private Vector<Vector<DuDeObject>> goldCluster;

	private List<String> objectIds1 = Arrays.asList(new String[] { GoldStandard.FIRST_ELEMENTS_DEFAULT_OBJECT_ID_ATTRIBUTE });
	private List<String> objectIds2 = Arrays.asList(new String[] { GoldStandard.SECOND_ELEMENTS_DEFAULT_OBJECT_ID_ATTRIBUTE });
	/**
	 * Whether the gold standard has to be read in from a separate file in cluster format.
	 */
	private boolean clustered = false; 
	/**
	 * The name of the file containing the gold standard in cluster format.
	 */
	private String clusterfile ="";
	private String sourceId1 = "";
	private String sourceId2 = "";

	private boolean sourceIdMappingEnabled = false;

	private transient final Collection<DuDeObjectPair> goldStandard = new HashSet<DuDeObjectPair>();

	private int extractedDataSize = 0;
	/**
	 * Internal constructor for {@link Jsonable} deserialization.
	 */
	protected GoldStandard() {
		// nothing to do
	}
	
	private Vector<DuDeObject> extractedData = new Vector<DuDeObject>();

	/**
	 * Initializes the <code>GoldStandard</code> with the passed {@link DataSource}.
	 * 
	 * @param source
	 *            The <code>DataSource</code> that is used for accessing the gold standard information.
	 */
	public GoldStandard(DataSource source) {
		this(source, "");
	}
	
	/**
	 * Initializes the <code>GoldStandard</code> with the passed {@link DataSource} and the filename
	 * of the gold standard in cluster format to read it in. Only one of these two sources should be
	 * specified.
	 * 
	 * @param source
	 *            The <code>DataSource</code> that is used for accessing the gold standard information.
	 * @param file
	 * 			The name of the file containing the gold standard in cluster format.
	 */
	public GoldStandard(DataSource source, String file) {
		if ((source == null) && file.equals("")) { //if no datasource and file is given, throw exception
			throw new NullPointerException("The DataSource is missing. Specify either a DataSource for the gold standard in duplicate pairs format or a filename for the goldstandard in cluster format.");
		}
		if (!file.equals("")){
			this.setFilename(file);
			this.clustered = true;
		} else {
			this.clustered = false;
		}
		this.source = source;
	}
	
	/**
	 * Initializes the <code>GoldStandard</code> with the passed filename
	 * of the gold standard in cluster format to read it in.
	 *
	 * @param file
	 * 			The name of the file containing the gold standard in cluster format.
	 */
	public GoldStandard(String file) {
		this(null, file);
	}

	/**
	 * Clears all gathered data. Accessing the gold standard the next time will cause a reload.
	 */
	public void clear() {
		this.goldStandard.clear();
	}

	/**
	 * Closes the stream of the {@link DataSource}.
	 * 
	 * @throws IOException
	 *             If an error occurs while closing the connection to the gold standard data source.
	 */
	public void close() throws IOException {
		this.source.close();
	}

	/**
	 * Checks whether the passed pair is in the gold standard.
	 * 
	 * @param pair
	 *            The {@link DuDeObjectPair} that shall be checked. It may also contain merged objects.
	 * @return <code>true</code>, if the passed pair is also contained in the gold standard; otherwise <code>false</code>.
	 */
	public boolean contains(DuDeObjectPair pair) {
//		System.out.println("contains");
		// Check all combinations of identifiers
		for (DuDeObjectId id1: pair.getFirstElement().getIdentifiers()) {
			for (DuDeObjectId id2: pair.getSecondElement().getIdentifiers()) {
				
				// If the gold standard contains the current combination return true
				DuDeObjectPair objPair = new DuDeObjectPair(id1.getSourceId(), id1.getObjectId(), id2.getSourceId(), id2.getObjectId());
				if (this.getData().contains(objPair)) {
					return true;
				}
			}
		}
		
		// None of the combinations were found in the gold standard
		return false;
	}

	/**
	 * Disables source-id mapping. If source-id mapping is disabled, the set source ids are used as the actual source ids of the generated pairs.
	 */
	public void disableSourceIdMapping() {
		this.sourceIdMappingEnabled = false;
	}

	/**
	 * Enables source-id mapping. If source-id mapping is enabled, the set source id is not used as a source id itself but handled as an attribute
	 * name. The actual source id is extracted using this attribute name.
	 */
	public void enableSourceIdMapping() {
		this.sourceIdMappingEnabled = true;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		final GoldStandard other = (GoldStandard) obj;
		if (this.source == null) {
			if (other.source != null)
				return false;
		} else if (!this.source.equals(other.source))
			return false;
		if (this.objectIds1 == null) {
			if (other.objectIds1 != null)
				return false;
		} else if (!this.objectIds1.equals(other.objectIds1))
			return false;
		if (this.objectIds2 == null) {
			if (other.objectIds2 != null)
				return false;
		} else if (!this.objectIds2.equals(other.objectIds2))
			return false;
		if (this.sourceId1 == null) {
			if (other.sourceId1 != null)
				return false;
		} else if (!this.sourceId1.equals(other.sourceId1))
			return false;
		if (this.sourceId2 == null) {
			if (other.sourceId2 != null)
				return false;
		} else if (!this.sourceId2.equals(other.sourceId2))
			return false;
		if (this.sourceIdMappingEnabled != other.sourceIdMappingEnabled)
			return false;
		return true;
	}

	/**
	 * Extracts the gold standard if necessary and returns it.
	 * 
	 * @return The gold standard.
	 */
	//changed to public since the slice algorithm needs the access on the duplicate pairs of the gold standard
	public Collection<DuDeObjectPair> getData() {
//                System.out.println("getData");
            
		if (this.goldStandard.isEmpty())
			this.loadData();

		return this.goldStandard;
	}

	/**
	 * Returns the names of the attributes that store the object id of the pair's first element.
	 * 
	 * @return The attribute names.
	 */
	protected Iterable<String> getFirstElementsObjectIdAttributes() {
		return this.objectIds1;
	}

	/**
	 * Returns the source id of the pair's first element. Depending on source-id mapping is enabled or not, the returned String is an attribute name
	 * or the actual source id.
	 * 
	 * @return The attribute name or the actual source id the pair's second element.
	 * 
	 * @see #enableSourceIdMapping()
	 * @see #disableSourceIdMapping()
	 * @see #sourceIdMappingEnabled()
	 */
//	protected String getFirstElementsSourceId() {
        public String getFirstElementsSourceId() {
		return this.sourceId1;
	}

	/**
	 * Returns the names of the attributes that store the object id of the pair's second element.
	 * 
	 * @return The attribute names.
	 */
	protected Iterable<String> getSecondElementsObjectIdAttributes() {
		return this.objectIds2;
	}

	/**
	 * Returns the source id of the pair's second element. Depending on source-id mapping is enabled or not, the returned String is an attribute name
	 * or the actual source id.
	 * 
	 * @return The attribute name or the actual source id for the pair's second element.
	 * 
	 * @see #enableSourceIdMapping()
	 * @see #disableSourceIdMapping()
	 * @see #sourceIdMappingEnabled()
	 */
//	protected String getSecondElementsSourceId() {
        public String getSecondElementsSourceId() {
		return this.sourceId2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.source == null ? 0 : this.source.hashCode());
		result = prime * result + (this.objectIds1 == null ? 0 : this.objectIds1.hashCode());
		result = prime * result + (this.objectIds2 == null ? 0 : this.objectIds2.hashCode());
		result = prime * result + (this.sourceId1 == null ? 0 : this.sourceId1.hashCode());
		result = prime * result + (this.sourceId2 == null ? 0 : this.sourceId2.hashCode());
		result = prime * result + (this.sourceIdMappingEnabled ? 1231 : 1237);
		return result;
	}

	/**
	 * Extracts the gold standard either in cluster or duplicate pair format.
	 * 
	 * @throws ExtractionFailedException
	 *             If any data could not be loaded since the set attributes are missing.
	 */
	protected void loadData() {
//		          System.out.println("loadData");
            
                this.goldStandard.clear();
                
		
		if (this.clustered){ //if a file in cluster format has been given as source
			loadClusters();
			generateDuplicatePairs();
		} else{//if a file in duplicate pairs format has been given as source
			loadDuplicatePairs();
			//generation of clusters only after setExtractedDataSize() has been invoked
		}
	}
	
	/**
	 * Reads in the gold standard in cluster format [objectId1 sourceId1];[objectId2 sourceId2] from the given file.
	 * Format as specified for writeClusterFile.
	 * @return goldStandard
	 * 					The read-in gold standard in cluster format
	 */
	protected void loadClusters(){
		this.goldCluster = new Vector<Vector<DuDeObject>>();
		FileInputStream fstream;
		try {
			fstream = new FileInputStream(this.clusterfile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String line;
			while ((line = br.readLine()) != null) {
				goldCluster.add(this.processLine(line));
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.err.println("Error: " + e.getMessage());
		} catch (IOException e){
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	/**
	 * Extracts the gold standard out of a file with duplicate pairs.
	 * 
	 * @throws ExtractionFailedException
	 *             If any data could not be loaded since the set attributes are missing.
	 */
	protected void loadDuplicatePairs(){
		//this is a hack! how can I make 2 dudeObjects out of one? DataSource class has to be adapted
                
//                System.out.println("loadDuplicatePairs");
                
		for (final DuDeObject data : this.source) {
			// extract first element's object id
			final JsonArray objectId1 = new JsonArray();
			for (final String objectIdAttribute : this.getFirstElementsObjectIdAttributes()) {
				final JsonValue value = data.getAttributeValues(objectIdAttribute);
//                                System.out.println("Dados do objectId1: " + data.getAttributeValues(objectIdAttribute));

				if (value == null)
					throw new ExtractionFailedException("Value for '" + objectIdAttribute + "' is missing.");

				objectId1.add(value);
			}

			// extract second element's object id
			final JsonArray objectId2 = new JsonArray();
			for (final String objectIdAttribute : this.getSecondElementsObjectIdAttributes()) {
				final JsonValue value = data.getAttributeValues(objectIdAttribute);
//                                System.out.println("Dados do objectId2: " + data.getAttributeValues(objectIdAttribute));
                                
				if (value == null)
					throw new ExtractionFailedException("Value for '" + objectIdAttribute + "' is missing.");

				objectId2.add(value);
			}

			// extract source ids
			String sourceId1 = this.getFirstElementsSourceId();
			String sourceId2 = this.getSecondElementsSourceId();

			// if source-id mapping is enabled, the set source ids are used as
			// attribute names
			if (this.sourceIdMappingEnabled()) {
				final JsonValue attrValue1 = data.getAttributeValue(this.getFirstElementsSourceId());
				final JsonValue attrValue2 = data.getAttributeValue(this.getSecondElementsSourceId());

				if (attrValue1 == null)
					throw new ExtractionFailedException("First source id '" + this.getFirstElementsSourceId() + "' is missing.");
				else if (attrValue2 == null)
					throw new ExtractionFailedException("Second source id '" + this.getSecondElementsSourceId() + "' is missing.");

				sourceId1 = attrValue1.toString();
				sourceId2 = attrValue2.toString();
			}

			this.goldStandard.add(new DuDeObjectPair(sourceId1, objectId1, sourceId2, objectId2));
		}
	}

	/**
	 * Sets the attributes that store the object id of the pair's first element.
	 * 
	 * @param attrNames
	 *            The names of the attributes that store the first element's object id.
	 */
	public void setFirstElementsObjectIdAttributes(String... attrNames) {
		this.objectIds1 = Arrays.asList(attrNames);
	}

	/**
	 * Sets the source id of the pair's first element. If source-id mapping is disabled, this source id is interpreted as the source id itself.
	 * 
	 * @param attrName
	 *            The attribute that store the source id of the first element.
	 * 
	 * @see #enableSourceIdMapping()
	 * @see #disableSourceIdMapping()
	 * @see #sourceIdMappingEnabled()
	 */
	public void setFirstElementsSourceId(String attrName) {
		this.sourceId1 = attrName;
	}

	/**
	 * Sets the actual source id of the pair's first element. If source-id mapping is enabled, this source id is interpreted as the attribute's name
	 * that stores the actual source id of the first element.
	 * 
	 * @param sourceId
	 *            The actual source id of the first element.
	 * 
	 * @see #enableSourceIdMapping()
	 * @see #disableSourceIdMapping()
	 * @see #sourceIdMappingEnabled()
	 */
	public void setFirstElementsSourceIdLiteral(String sourceId) {
		this.setFirstElementsSourceId(sourceId);
	}

	/**
	 * Sets the attributes that store the object id of the pair's second element.
	 * 
	 * @param attrNames
	 *            The names of the attributes that store the second element's object id.
	 */
	public void setSecondElementsObjectIdAttributes(String... attrNames) {
		this.objectIds2 = Arrays.asList(attrNames);
	}

	/**
	 * Sets the source id of the pair's second element. If source-id mapping is disabled, this source id is interpreted as the source id itself.
	 * 
	 * @param attrName
	 *            The attribute that store the source id of the second element.
	 * 
	 * @see #enableSourceIdMapping()
	 * @see #disableSourceIdMapping()
	 * @see #sourceIdMappingEnabled()
	 */
	public void setSecondElementsSourceId(String attrName) {
		this.sourceId2 = attrName;
	}

	/**
	 * Sets the actual source id of the pair's second element. If source-id mapping is enabled, this source id is interpreted as the attribute's name
	 * that stores the actual source id of the second element.
	 * 
	 * @param sourceId
	 *            The actual source id of the second element.
	 * 
	 * @see #enableSourceIdMapping()
	 * @see #disableSourceIdMapping()
	 * @see #sourceIdMappingEnabled()
	 */
	public void setSecondElementsSourceIdLiteral(String sourceId) {
		this.setSecondElementsSourceId(sourceId);
	}

	/**
	 * Sets the actual source id of both elements. If source-id mapping is enabled, this source id is interpreted as the attribute's name that stores
	 * the actual source id of the these elements.
	 * 
	 * @param sourceId
	 *            The actual source id of both elements.
	 * 
	 * @see #enableSourceIdMapping()
	 * @see #disableSourceIdMapping()
	 * @see #sourceIdMappingEnabled()
	 */
	public void setSourceIdLiteral(String sourceId) {
		this.setFirstElementsSourceIdLiteral(sourceId);
		this.setSecondElementsSourceIdLiteral(sourceId);
	}

	/**
	 * Returns the size of the gold standard.
	 * 
	 * @return The size of the gold standard.
	 */
	public int size() {
		return this.getData().size();
	}

	/**
	 * Checks whether source-id mapping is enabled.
	 * 
	 * @return <code>true</code>, if source-id mapping is enabled; otherwise <code>false</code>.
	 */
	public boolean sourceIdMappingEnabled() {
		return this.sourceIdMappingEnabled;
	}

	@Override
	public String toString() {
		return "GoldStandard [source=" + this.source + ", objectIds1=" + this.objectIds1 + ", objectIds2=" + this.objectIds2 + ", sourceId1="
				+ this.sourceId1 + ", sourceId2=" + this.sourceId2 + ", sourceIdMappingEnabled=" + this.sourceIdMappingEnabled + "]";
	}

	/**
	 * Fluent method for {@link #setFirstElementsSourceId(String)} and {@link #setFirstElementsObjectIdAttributes(String...)}.
	 * 
	 * @param sourceId
	 *            The source id of all first elements.
	 * @param attributes
	 *            The first element's id attributes.
	 * 
	 * @return The current instance.
	 */
	public GoldStandard withFirstElement(String sourceId, String... attributes) {
		this.setFirstElementsSourceId(sourceId);
		this.setFirstElementsObjectIdAttributes(attributes);
		return this;
	}

	/**
	 * Fluent method for {@link #setSecondElementsSourceId(String)} and {@link #setSecondElementsObjectIdAttributes(String...)}.
	 * 
	 * @param sourceId
	 *            The source id of all second elements.
	 * @param attributes
	 *            The second element's id attributes.
	 * 
	 * @return this
	 */
	public GoldStandard withSecondElement(String sourceId, String... attributes) {
		this.setSecondElementsSourceId(sourceId);
		this.setSecondElementsObjectIdAttributes(attributes);
		return this;
	}

	/**
	 * Triggers the generation of a gold standard in cluster format.
	 * @return goldCluster
	 * 				The gold standard in cluster format.
	 */
	public Vector<Vector<DuDeObject>> getCluster(){
		this.generateClusters();
		return this.goldCluster;
	}
	
	/**
	 * Sets the name of the file that contains the gold standard in cluster format.
	 * @param file
	 * 			Name of the file.
	 */
	public void setFilename(String file){
		this.clusterfile = file;
	}
	/**
	 * If not already existing, generates a list of clusters containing the duplicates.
	 * Writes the results into a csv file for later use.
	 * 
	 * @return The gold standard in cluster format
	 */
	public void generateClusters(){
		//check if already read in by goldStandard source file
		if (!this.clustered){//generate transitive closure = clusters from the duplicate pairs
			NaiveTransitiveClosureGenerator transClosGenerator = new NaiveTransitiveClosureGenerator();
			transClosGenerator.add(this.getData());
			Vector<Vector<DuDeObject>> newGoldCluster = new Vector<Vector<DuDeObject>>();
			Collection<Collection<DuDeObject>> goldCluster = (Collection<Collection<DuDeObject>>) transClosGenerator.getTransitiveClosures();
			for (Collection <DuDeObject> cluster : goldCluster){
				Vector<DuDeObject> newCluster = new Vector<DuDeObject>();
				for (DuDeObject record : cluster) newCluster.add(record);
				newGoldCluster.add(newCluster);
			}
			//generate single-record-clusters for unique records
			this.goldCluster = this.generateClustersForUnique(newGoldCluster);
			//write into file
			this.writeClusterFile("goldStandard_clustered.csv");
		}
	}
	
	/**
	 * Generates for each unique item (that is not listed in the pairwise gold standard) its own cluster
	 * and adds it to the given goldCluster.
	 * @param goldCluster
	 * 			The gold standard in cluster format, without the unique items.
	 * @return The gold standard in cluster format, with the unique items in their own cluster.
	 */
	public Vector<Vector<DuDeObject>> generateClustersForUnique(Vector<Vector<DuDeObject>> clusters){
		//take datasize, find out what id is missing and create a new DuDeObject form each left id
		/*Vector<Integer> ids = new Vector<Integer>();
		for (int i = 1; i <= this.getExtractedDataSize(); i++) {ids.add(i);}
		int j = 1879;
		for (Vector<DuDeObject> cluster : clusters){
			for (DuDeObject record : cluster){
				Integer id = Integer.valueOf(record.getObjectId().get(0).toString().trim());
				ids.remove(id);
				j -= 1;
			}
		}
		//from the ids that remain, generate a single cluster for each
		for (Integer id : ids){
			Vector<DuDeObject> cluster = new Vector<DuDeObject>();
			DuDeObject uniqueRecord = new DuDeObject(this.sourceId1, id.toString()); //add the corresponding ids
			cluster.add(uniqueRecord);
			clusters.add(cluster);
		}
		return clusters;*/
		for (Vector<DuDeObject> cluster : clusters){
			for (DuDeObject record : cluster){
				this.extractedData.remove(record);
			}
		}
		
		//from the objects that remain, generate a single cluster for each
		for (DuDeObject uniqueRecord : this.extractedData){
			Vector<DuDeObject> cluster = new Vector<DuDeObject>();
			cluster.add(uniqueRecord);
			clusters.add(cluster);
		}
		return clusters;
	}
	
	/**
	 * Writes the cluster form into a file in the following format:
	 * [objID1,sourceID1];[objID2,sourceID2];
	 * 
	 * Where each line represents a cluster of duplicates
	 * @param goldCluster
	 * 					The gold standard in cluster form to write to the file
	 */
	
	public void writeDuplicatePairsFile(String filename){
		//generate new csvWriter
		Writer output = null;
	    File file = new File(filename);
	    try {
			output = new BufferedWriter(new FileWriter(file));
			//write first line
			StringBuilder data = new StringBuilder();
			data.append("id1").append(";").append("id2");
			data.append("\n"); //mark as new line
    		output.write(data.toString());
    		output.flush();
    		
			//process each duplicate pair
			for (DuDeObjectPair pair : this.goldStandard){
				//write the line
				data = new StringBuilder();
				data.append(pair.getFirstElement().getObjectId().generateString()).append(";");
				data.append(pair.getSecondElement().getObjectId().generateString());
				data.append("\n"); //mark as new line
	    		output.write(data.toString());
	    		output.flush();
			}
			output.close();
	    } catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
		}	
	}
	
	/**
	 * Writes the cluster form into a file in the following format:
	 * [objID1,sourceID1];[objID2,sourceID2];
	 * 
	 * Where each line represents a cluster of duplicates
	 * @param goldCluster
	 * 					The gold standard in cluster form to write to the file
	 */
	
	public void writeClusterFile(String filename){
		//generate new csvWriter
		Writer output = null;
	    File file = new File(filename);
	    try {
			output = new BufferedWriter(new FileWriter(file));
	    
			//process each cluster
			for (Vector<DuDeObject> cluster : this.goldCluster){
				//write the line
				StringBuilder data = new StringBuilder();
				
				//make one line out of the cluster records
				for (DuDeObject record : cluster){ //write in the form [objectId,sourceId];[objectId,sourceId]
					data.append("[").append(record.getObjectId().generateString()).append(" ").append(record.getSourceId()).append("];");
				}
				data.append("\n"); //mark as new line
	    		output.write(data.toString());
	    		output.flush();
			}
			output.close();
	    } catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
		}	
	}
	
	/**
	 * Returns a cluster of duplicates read from the given line
	 * @param line
	 * 			A String containing the cluster records
	 * @return cluster
	 * 			A <code>LinkedList</code> cluster containing DuDeObjects
	 */
	public Vector<DuDeObject> processLine(String line){
		Vector<DuDeObject> cluster = new Vector<DuDeObject>();
		String[] lineArray = line.split(";");
		for (String str : lineArray){
			str = str.replace("[", "");
			str = str.replace("]", "");
			String[] ids = str.split(" ");
			DuDeObject record;
			if (ids.length == 1) 
				record = new DuDeObject("", ids[0]);
			else
				record = new DuDeObject(ids[1], ids[0]);
			cluster.add(record);
		}
		
		return cluster;
	}
	
	/**
	 * Sets the amount of records extracted (retrieved from {@link Algorithm}-Object).
	 * Necessary for formatting the gold standard to cluster size.
	 * @param size
	 * 			The amount of records extracted.
	 */
	public void setExtractedData(Vector<DuDeObject> extractedRecords){
		this.extractedData = extractedRecords;
	}
	
	/**
	 * Returns the amount of records extracted (formerly retrieved from {@link Algorithm}-Object).
	 * Necessary for formatting the gold standard to cluster size.
	 * @return size
	 * 			The amount of records extracted.
	 */
	public int getExtractedDataSize(){
		return this.extractedDataSize;
	}
	
	/**
	 * Generates a gold standard in duplicate pair format from a given one in cluster format.
	 * Write the result into a file called "goldStandard_duplicatePairs.csv".
	 */
	public void generateDuplicatePairs(){
		for (Vector<DuDeObject> cluster : this.goldCluster){ //take each cluster and generate its duplicate pairs
			for (int i = 0; i < cluster.size(); i++){
				for (int j = i + 1; j < cluster.size(); j++){
					//generate pair
					DuDeObjectPair pair = new DuDeObjectPair(cluster.get(i), cluster.get(j));
					this.goldStandard.add(pair);
				}
			}
		}
		this.writeDuplicatePairsFile("goldStandard_duplicatePairs.csv");
	}
}
