package model.worldMap;

import java.util.ArrayList;
import java.util.List;


public class LocationNode {
	
	private final double WORLD_LATITUDE;
	private final double WORLD_LONGITUDE;
	private final int LOCATION_INDEX;
	private static int indexCount;
	

	private String townName;
	private List<TrailEdge> outBoundTrails;
	
	
	public LocationNode(){
		LOCATION_INDEX = LocationNode.indexCount++;
		//until we can get a nice source for lat and long data
		WORLD_LATITUDE = this.LOCATION_INDEX;
		WORLD_LONGITUDE = this.LOCATION_INDEX;
		
		townName = "Town " + new Integer(LOCATION_INDEX).toString();

		outBoundTrails = new ArrayList<TrailEdge>();
		
		
	}

}
