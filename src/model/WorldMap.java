package model;

import model.worldMap.*;

/**
 * The world map: a collection of locations and paths between.
 */
public class WorldMap {

	//points to starting city - references entire map
	private LocationNode mapHead;
	//points to nearest locationNode ahead of party, or current location
	private LocationNode currDestination;
	//final destination - Portland Oregon
	private LocationNode finalDestination;
	//points to trailEdge most recently occupied by party
	private TrailEdge currTrail;
	
	/**
	 * Makes a {@code WorldMap} object that tells the game where the party is and what's ahead of them
	 * @param numNodes the number of possible locations on the map - not all of them will be reachable
	 * @param numTrails the number of possible directed trails - not all will be travelled.
	 */
	public WorldMap(int numNodes, int numTrails){
		this.mapHead = new LocationNode("Independence", 50, 30, 3);
		this.currDestination = this.mapHead;
		this.finalDestination = new LocationNode("Portland", 100,100,0);
	}
	
	public WorldMap(){
		//make a generic-sized map for initial testing and development 
		this(50,200);
	}

}//class worldMap
