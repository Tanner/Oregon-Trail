package model.worldMap;

import java.util.ArrayList;
import java.util.List;


public class LocationNode {
	
	public final double WORLD_LATITUDE;
	public final double WORLD_LONGITUDE;
	public final int LOCATION_INDEX;
	private static int indexCount;
	

	private String townName;
	private List<TrailEdge> outBoundTrails;
	
	
	public LocationNode(int Latitude, int Longitude){
		LOCATION_INDEX = LocationNode.indexCount++;
		//until we can get a nice source for lat and long data
		WORLD_LATITUDE = Latitude;
		WORLD_LONGITUDE = Longitude;
		
		townName = "Town " + LOCATION_INDEX;

		outBoundTrails = new ArrayList<TrailEdge>();
	}

	
}
