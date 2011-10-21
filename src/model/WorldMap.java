package model;

import model.worldMap.*;
import java.util.Random;


/**
 * The world map: a collection of locations and paths between.
 */
public class WorldMap {

	/**maximum number of exiting trails each location can have*/
	private final int MAX_TRAILS_OUT = 3;  
	/**points to starting city - references entire map*/
	private LocationNode mapHead;
	/**points to nearest locationNode ahead of party, or current location*/
	private LocationNode currDestination;
	/**final destination - Portland Oregon*/
	private LocationNode finalDestination;
	/**points to trailEdge most recently occupied by party*/
	private TrailEdge currTrail;
	/**number of total locations on map*/
	private int numLocations;
	/**number of total trails on map*/
	private int numTrails;
	
	
	/**
	 * Makes a {@code WorldMap} object that tells the game where the party is and what's ahead of them
	 * @param numNodes the number of possible locations on the map - not all of them will be reachable
	 * @param numTrails the number of possible directed trails - not all will be travelled.
	 */
	public WorldMap(int numNodes, int numTrails){
		this.mapHead = new LocationNode("Independence", 50, 30, 3);
		this.currDestination = this.mapHead;
		this.finalDestination = new LocationNode("Portland", 100,100,0);
		this.generateMap(numNodes, numTrails);
	}
	
	public WorldMap(){
		//make a generic-sized map for initial testing and development 
		this(50,200);
	}

	/**
	 * Makes the random map, using the given number of nodes and edges, with a fun and fancy algorithm that first
	 * makes all the nodes with a single edge linking them, and then adds connections until out of edges
	 * @param numNodes number of locations this map will have - each can have between 1 and MAX_TRAILS_OUT trails leaving
	 * @param numTrails number of trails linking locations - will be forced to be enough to at least link all locations
	 */
	private void generateMap(int numNodes, int numTrails){
		//manufacture random object - make constant seeded now for testing purposes
		Random mapRand = new Random(12345);
		
		
	}
	
	
}//class worldMap
