package model;

import model.worldMap.*;
import java.util.Random;


/**
 * The world map: a collection of locations and paths between.
 */
public class WorldMap {

	/**maximum number of exiting trails each location can have*/
	private final int MAX_TRAILS_OUT = 3;  
	/**maximum number of "levels" of travel west - only portland has this as its rank.  edges can only go to edges with equal or higher rank than their origin node*/
	private final int MAX_RANK = 10;
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
	 * @param numTrails the number of possible directed trails - not all will be traveled.
	 */
	public WorldMap(int numNodes, int numTrails){
		this.generateMap(numNodes, numTrails);
	}
	
	public WorldMap(){
		//make a generic-sized map for initial testing and development 
		this(50,200);
	}

	
	/** 
	 * recursively build our node map - every location has a rank, denoting how far west it is.  we start at rank 0, we end at rank MAX_RANK.
	 * no edge can connect to a node with a lesser rank.
	 * 
	 * @param src the node we're going to build off.
	 * @return the last node we built
	 */
	private LocationNode generateMapLocations (LocationNode src, int num){
		if (num == 1){//if we're at num = 1 then we need to link up to portland
		
		}
		//iterate through arraylist of trails from this node, to build all next nodes

		for (int numEdge = 0; numEdge < src.getOutboundTrails().size(); numEdge++){
			
		}
		 
		return src;
		
	}
	/**
	 * Makes the random map, using the given number of nodes and edges, with a fun and fancy algorithm that first
	 * makes all the nodes with a single edge linking them, and then adds connections until out of edges
	 * 
	 * @param numLocations number of locations this map will have - each can have between 1 and MAX_TRAILS_OUT trails leaving
	 * @param numTrails number of trails linking locations - will be forced to be enough to at least link all locations
	 */
	private void generateMap(int numLocations, int numTrails){
		//manufacture random object - make constant seeded now for testing purposes
		Random mapRand = new Random(12345);
		//num of edges from current location - will be between 1 and MAX_TRAILS_OUT
		int numExitTrails;
		
		//number of trails out of Independence - 1 to MaxTrailsOut constant
		numExitTrails = mapRand.nextInt(MAX_TRAILS_OUT) + 1;
		//build beginning and final locations, and set up pointer to current location
		this.mapHead = new LocationNode("Independence", 50, 30, numExitTrails, 0);
		this.currDestination = this.mapHead;
		this.finalDestination = new LocationNode("Portland", 100,100,0, MAX_RANK);
		generateMapLocations(this.mapHead, numLocations - 2);
					
			
			
		
		
	}
	
	
}//class worldMap
