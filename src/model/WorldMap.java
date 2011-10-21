package model;

import model.worldMap.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * The world map: a collection of locations and paths between.
 */
public class WorldMap {

	/**weight of ranking assignment in random generator - 2 is 50 50 chance for same rank or next rank, higher numbers weight toward next rank*/
	private final int RANK_WEIGHT = 3;  
	/**maximum number of exiting trails each location can have*/
	private final int MAX_TRAILS_OUT = 3;  
	/**maximum number of "levels" of travel west - only portland has this as its rank.  edges can only go to edges with equal or higher rank than their origin node*/
	private final int MAX_RANK = 10;
	/**width of map in miles*/
	private final int MAX_X = 1000;
	/**height of map in miles*/
	private final int MAX_Y = 800;
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
		this.currDestination = this.mapHead;
	}
	
	/**
	 * Makes a {@code WorldMap} object that tells the game where the party is and what's ahead of them, with predefined number of nodes and edges
	 */
	public WorldMap(){
		//make a generic-sized map for initial testing and development 
		this(60,200);
	}

	/**
	 * Makes the random map, using the given number of nodes and edges, with a fun and fancy algorithm that first
	 * makes all the nodes with a single edge linking them, and then adds connections until out of edges
	 * 
	 * @param numLocations number of locations this map will have - each can have between 1 and MAX_TRAILS_OUT trails leaving
	 * @param numTrails number of trails linking locations - will be forced to be enough to at least link all locations
	 */
	private void generateMap(int numLocations, int numTrails){
			//build a temporary list to hold the generated locations
		List<LocationNode> tempLocationStore = new ArrayList<LocationNode>(numLocations);
			//temp array holding number of locations at each rank, indexed by rank
		int[] numRankAra = new int[MAX_RANK];
			//current node's rank as we're building the node list
			//manufacture random object - make constant seeded now for testing purposes
		Random mapRand = new Random(12345);
			//num of edges from current location - will be between 1 and MAX_TRAILS_OUT
		int numExitTrails;
		
			//number of trails out of Independence : 1 to MaxTrailsOut constant
		numExitTrails = mapRand.nextInt(MAX_TRAILS_OUT) + 1;
			//build beginning and final locations
		this.mapHead = new LocationNode("Independence", MAX_X, MAX_Y/2, numExitTrails, 0);
		this.finalDestination = new LocationNode("Portland", 0,0,0, MAX_RANK);
		
		numRankAra[0] = 1;
		numRankAra[MAX_RANK - 1] = 1;
		
		tempLocationStore.add(mapHead);
		for(int i = 1; i < numLocations-1; i++){
			
			int curRankIter = i % (MAX_RANK - 1) + 1;
			int curRank = (mapRand.nextInt(RANK_WEIGHT) == 0) ? curRankIter-1 : curRankIter;
				//derive x coord of this location on map - should give range of MAX_X to 0 in "clumps" clustered around MAX_X/MAX_RANK
				int tmpZ =  mapRand.nextInt(MAX_X/MAX_RANK) - (MAX_X/(2 * MAX_RANK));
			int tmpX = MAX_X - (((MAX_X/MAX_RANK) * (curRank)) + tmpZ);
			while(tmpX < 10){
				tmpX += mapRand.nextInt(MAX_X/MAX_RANK);
				}
			while(tmpX > MAX_X){
				tmpX -= mapRand.nextInt(MAX_X/MAX_RANK);
				}
				//derive y coord of this location on map - should give some range of y between -MAX_Y/2 and MAX_Y/2
			int tmpY = (MAX_Y/MAX_RANK) * (mapRand.nextInt(MAX_RANK) - (MAX_RANK/2)) + (mapRand.nextInt(MAX_Y/(2 * MAX_RANK)) - (MAX_Y/MAX_RANK));			
	
			//number of trails out of location : 1 to MaxTrailsOut constant
			numExitTrails = mapRand.nextInt(MAX_TRAILS_OUT) + 1;
			LocationNode tempNode = new LocationNode(tmpX, tmpY, numExitTrails, curRank);
			tempLocationStore.add(tempNode);
			
			
		}//for all locations make a node
		tempLocationStore.add(finalDestination);
		
	}
	
	
}//class worldMap
