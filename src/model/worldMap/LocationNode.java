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
	
	
	public LocationNode(int Latitude, int Longitude, int trails){
		LOCATION_INDEX = LocationNode.indexCount++;
		//until we can get a nice source for lat and long data
		WORLD_LATITUDE = Latitude;
		WORLD_LONGITUDE = Longitude;
		

		locationName = "Location " + new Integer(LOCATION_INDEX).toString();
		outBoundTrails = new ArrayList<TrailEdge>(trails);
	}

	/**
	 * returns the string representation of this location
	 */
	public String toString(){
		
		return this.locationName;
	}
	
}
