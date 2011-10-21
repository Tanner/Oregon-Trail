package model.worldMap;

import java.util.ArrayList;
import java.util.List;


public class LocationNode {
	
	public final double WORLD_LATITUDE;
	public final double WORLD_LONGITUDE;
	public final int LOCATION_INDEX;
	private static int indexCount;
	
	private String locationName;
	private List<TrailEdge> outBoundTrails;
	
	/**
	 * Constructor for making a {@code LocationNode}
	 * @param locationName
	 * @param Latitude
	 * @param Longitude
	 * @param trails
	 */
	public LocationNode(String locationName, int Latitude, int Longitude, int trails){
		LOCATION_INDEX = LocationNode.indexCount++;
		//until we can get a nice source for lat and long data
		WORLD_LATITUDE = Latitude;
		WORLD_LONGITUDE = Longitude;
		this.outBoundTrails = new ArrayList<TrailEdge>(trails);
		this.locationName = locationName;
	}
	
	public LocationNode(int Latitude, int Longitude, int trails){
		//makes unique name for location, temporarily, until we can manufacture them.
		this("Location " + new Integer(LocationNode.indexCount).toString(),Latitude, Longitude, trails);
		
	}

	/**
	 * returns the string representation of this location
	 */
	public String toString(){
		
		return this.locationName;
	}
	
}
