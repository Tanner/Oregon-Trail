package model.worldMap;

import java.util.ArrayList;
import java.util.List;


public class LocationNode {
	
	public final double WORLD_LATITUDE;
	public final double WORLD_LONGITUDE;
	public final int LOCATION_INDEX;
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
		LOCATION_INDEX = LocationNode.nodeCount++;
		//until we can get a nice source for lat and long data
		WORLD_LATITUDE = Latitude;
		WORLD_LONGITUDE = Longitude;
		this.outBoundTrails = new ArrayList<TrailEdge>(trails);
		this.locationName = locationName;
	}
	
	public LocationNode(int Latitude, int Longitude, int trails){
		//makes unique name for location, temporarily, until we can make them prettier.
		this("Location " + new Integer(LocationNode.nodeCount).toString(),Latitude, Longitude, trails);
		
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
	
}
