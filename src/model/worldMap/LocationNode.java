package model.worldMap;

import java.util.ArrayList;
import java.util.List;


public class LocationNode {
	
	/** real world latitude */	
	public final double WORLD_LATITUDE;
	/** real world longitude */
	public final double WORLD_LONGITUDE;
	/** ID for this node - used only internally for data store */ 
	public final int ID;
	/** class-wide counter of nodes */
	private static int nodeCount;
	
	private String locationName;
	private List<TrailEdge> outBoundTrails;
	
	/**
	 * Constructor for making a {@code LocationNode}
	 * @param locationName String representing name of location
	 * @param Latitude Real World(tm) latitude of location (for MapScene manufacture)
	 * @param Longitude Real World(tm) longitude of location (for MapScene Manufacture)
	 * @param trails number of trails exiting this location
	 */
	public LocationNode(String locationName, int Latitude, int Longitude, int trails){
		this.ID = LocationNode.nodeCount++;
		//until we can get a nice source for lat and long data
		WORLD_LATITUDE = Latitude;
		WORLD_LONGITUDE = Longitude;
		this.outBoundTrails = new ArrayList<TrailEdge>(trails);
		this.locationName = locationName;
	}
	
	public LocationNode(int Latitude, int Longitude, int trails){
		//makes unique name for location, temporarily, until we can make them prettier.
		this("Location " + LocationNode.nodeCount,Latitude, Longitude, trails);
		
	}

	/**
	 * adds a new, leaving, {@code TrailEdge} to this {@code LocationNode}
	 * @param newTrail
	 */
	
	public void addTrail(TrailEdge newTrail){
		this.outBoundTrails.add(newTrail);
	}
	
	/**
	 * returns the string representation of this location
	 */
	public String toString(){
		
		return this.locationName;
	}
	
	/**
	 * method to return all instance variables easily without having to string getters
	 * only dev mode
	 * @return string of all string representations of private variables
	 */
	public String debugToString(){
		String retVal;
		int numTrails = this.outBoundTrails.size();
		retVal = "Name : " + this.locationName + " World Lat : " + this.WORLD_LATITUDE + " World Long : " + this.WORLD_LONGITUDE + " \n";
		retVal += "Internal ID : " + this.ID + "Total Node count : " + this.nodeCount + " \n";
		
		if (numTrails == 0){
			retVal += "No trails implemented \n";
		}
		
		for (int i = 0; i < numTrails ;i++){
			retVal += "\tExit Trail " + i + " : " + this.outBoundTrails.get(i).toString() + " \n";
		}
		
		
		return retVal;
	}
	
}//class LocationNode
