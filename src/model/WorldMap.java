package model;

import model.worldMap.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * The world map: a collection of locations and paths between.
 */
public class WorldMap {

	//private final int TESTRANK = 1;  

	/**weight of ranking assignment in random generator - 2 is 50 50 chance for same rank or next rank, higher numbers weight toward next rank*/
	private final int RANK_WEIGHT = 3;  
	/**maximum number of exiting trails each location can have*/
	private final int MAX_TRAILS_OUT = 3;  
	/**maximum number of "levels" of travel west - only portland has this as its rank.  edges can only go to edges with equal or higher rank than their origin node*/
	private final int MAX_RANK = 20;
	/**width of map in miles*/
	private final int MAX_X = 2000;
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
	/**dev mode for this class, for testing */
	private boolean devMode;
	
	
	/**
	 * Makes a {@code WorldMap} object that tells the game where the party is and what's ahead of them
	 * @param numNodes the number of possible locations on the map - not all of them will be reachable
	 * @param numTrails the number of possible directed trails - not all will be traveled.
	 */
	public WorldMap(int numNodes, String devMode){
		this.devMode = (devMode.length() == 0) ? false : true;
		this.numTrails = 0;
		this.numLocations = numNodes;
		this.generateMap(numNodes);
		this.currDestination = this.mapHead;
	}
	
	/**
	 * Makes a {@code WorldMap} object that tells the game where the party is and what's ahead of them
	 * @param numNodes the number of possible locations on the map - not all of them will be reachable
	 * @param numTrails the number of possible directed trails - not all will be traveled.
	 */	
	public WorldMap(int numNodes){
		this(numNodes,"");
	}

	/**
	 * Makes a {@code WorldMap} object that tells the game where the party is and what's ahead of them, with predefined number of nodes and edges
	 */
	public WorldMap(){
		//make a generic-sized map for initial testing and development 
		this(120);
	}
	
	public WorldMap(String devMode){
		this(120,"devMode");
	}

	/**
	 * generates the actual {@LocationNode} based on the random value
	 * @param mapRand
	 * @param curRank
	 * @param numExitTrails
	 * @return
	 */
	private LocationNode generateLocationNode(Random mapRand, int curRank, int numExitTrails){
		
		//curRank = TESTRANK;
		int tmpZ =  mapRand.nextInt(MAX_X/MAX_RANK) - (MAX_X/(2 * MAX_RANK));
		int tmpX = MAX_X - (((MAX_X/MAX_RANK) * curRank) + tmpZ);
		while(tmpX < 10){
			tmpX += mapRand.nextInt(MAX_X/MAX_RANK);
			}
		while(tmpX > MAX_X){
			tmpX -= mapRand.nextInt(MAX_X/MAX_RANK);
			}
			//derive y coord of this location on map - should give some range of y between -MAX_Y/2 and +MAX_Y/2
			//MAX_Y/MAX_RANK divies y up into maxrank partitions
			//mapRand of MAXRANK finds the correct general "rank zone"
			//final component tmpZ determines offset within zone
		tmpZ =  mapRand.nextInt(MAX_Y/(2 * MAX_RANK)) - (MAX_Y/ MAX_RANK);  //range : +/- partition size/2
		//gives a multiplier that is greatest in the middle of the MAX_RANK # of partitions
		//allows the spread of possible y values of locations to be greatest at the middle of the 
		//path, with the least spread at the ends
		int rnkMult = (curRank <= (MAX_RANK/2) ) ? (curRank) : (MAX_RANK - curRank);
		//multiplier to determine whether above or below the "equator"
		int tmpW = (mapRand.nextBoolean()) ? (1) : (-1);
		//actual y value passed to locationNode constructor
		int tmpY = ((MAX_Y/MAX_RANK) * rnkMult * tmpW)  + tmpZ;  
		while(tmpY > MAX_Y/2){
			tmpY -= mapRand.nextInt(MAX_Y/MAX_RANK);
			}
		while(tmpY <  -1 * (MAX_Y/2)){
			tmpY += mapRand.nextInt(MAX_Y/MAX_RANK);
			}
		//number of exiting trails from this node - random between 1 and MAX_TRAILS_OUT
		numExitTrails = mapRand.nextInt(MAX_TRAILS_OUT) + 1;
		LocationNode tempNode = new LocationNode(tmpX, tmpY, numExitTrails, curRank);
		return tempNode;	
	}
	
	/**
	 * Makes the random map, using the given number of nodes and edges, with a fun and fancy algorithm that first
	 * makes all the nodes with a single edge linking them, and then adds connections until out of edges
	 * 
	 * @param numLocations number of locations this map will have - each can have between 1 and MAX_TRAILS_OUT trails leaving
	 * @param numTrails number of trails linking locations - will be forced to be enough to at least link all locations
	 */
	private void generateMap(int numLocations){
		//build a temporary list to hold the generated locations, from which to build the map by adding edges
		
		//make a map instead of a list, indexed by locationnode.rank, with value being arraylist of locationnodes.
		
		Map<Integer, List<LocationNode>> mapNodes = new HashMap<Integer, List<LocationNode>>();
		
		for (int i  = 0; i <= MAX_RANK; i++){
			mapNodes.put(i, new ArrayList<LocationNode>());
		}
		
		List<LocationNode> tempLocationStore = new ArrayList<LocationNode>(numLocations);
			//temp array holding number of locations at each rank, indexed by rank
		int[] numRankAra = new int[MAX_RANK];
			//current node's rank as we're building the node list
			//manufacture random object - make constant seeded now for testing purposes
		Random mapRand = new Random();
			//num of edges from current location - will be between 1 and MAX_TRAILS_OUT
		int numExitTrails;
		
			//number of trails out of Independence : 1 to MaxTrailsOut constant
		numExitTrails = mapRand.nextInt(MAX_TRAILS_OUT) + 1;
			//build beginning and final locations
		this.mapHead = new LocationNode("Independence", MAX_X, 0, numExitTrails, 0);
		this.finalDestination = new LocationNode("Portland", 0,0,0, MAX_RANK);

		//setting mapHead to be "on the trail" - don't want to loop back to home base as we initialize the map structure
		this.mapHead.setOnTheTrail(true);
		numRankAra[0] = 1;
		numRankAra[MAX_RANK - 1] = 1;
		
		tempLocationStore.add(mapHead);
		mapNodes.get(mapHead.getRank()).add(mapHead);
		//need to build base set of nodes - must have at least 1 per rank to get from independence to portland
		for (int i = 1; i < MAX_RANK; i++){
			numExitTrails = mapRand.nextInt(MAX_TRAILS_OUT) + 1;
			LocationNode tmp = generateLocationNode(mapRand,i,numExitTrails);
			tempLocationStore.add(tmp);
			mapNodes.get(i).add(tmp);
		}//for loop to build initial path

		//build rest of random map
		for(int i = MAX_RANK; i < numLocations-1; i++){
			
			int curRankIter;
			int curRank ;
			curRankIter = i % (MAX_RANK - 1) + 1;
			curRank = (mapRand.nextInt(RANK_WEIGHT) == 0) ? curRankIter-1 : curRankIter;
			//number of trails out of location : 1 to MaxTrailsOut constant
			numExitTrails = mapRand.nextInt(MAX_TRAILS_OUT) + 1;
			LocationNode tmp = generateLocationNode(mapRand,curRank,numExitTrails);
			tempLocationStore.add(tmp);
			mapNodes.get(tmp.getRank()).add(tmp);
		}//for all locations make a node
		tempLocationStore.add(finalDestination);
		mapNodes.get(finalDestination.getRank()).add(finalDestination);

		//as of here we have all the location nodes.  now need to build map.
		//by adding edges.  an edge can only go from a location to one with equal or higher 
		//rank.
/*		if (this.devMode) {
			for (int i = 0; i <= MAX_RANK; i++){
				System.out.println("Locations at rank "+  i + " : " + mapNodes.get(i).size());
				for(LocationNode node : mapNodes.get(i)){
					System.out.println(node.debugToString());
				}//for locationnodes
			}//for each rank
		}//devmode stub
*/		
		//no need to check MAX_RANK - never any location with exit trails at MAX_RANK
		for (int i = 0; i < MAX_RANK; i++){
			for(LocationNode node : mapNodes.get(i)){
				for (int tNum = 0; tNum < node.getTrails(); tNum++){
					int nextRank = ((mapRand.nextInt(RANK_WEIGHT) == 0) ? (i+1) : i);
					if (nextRank == MAX_RANK){
						nextRank = MAX_RANK-1;
					}
					TrailEdge newTrail;
					this.numTrails++;
					//if we're at final rank before finish, have all edges go to portland
					if (i == MAX_RANK-1){
						//System.out.println("i = " + i + " size = " + mapNodes.get(nextRank).size() + " random index : " + finalDestination.getRank() + " | Town name : " + finalDestination.getLocationName());
						node.setTrails(1);
						newTrail = new TrailEdge("Trail from " + node.getLocationName() + " to " + finalDestination.getLocationName(), finalDestination ,node, randGenTrailDanger (mapRand, node.getRank()) );
					} else {
						//nexttown holds size of arraylist for locations - used as random source to determine where trails go
						int nextTown = mapRand.nextInt(mapNodes.get(nextRank).size());
						LocationNode randDestNode = mapNodes.get(nextRank).get(nextTown);
						while ((randDestNode.getOnTheTrail()) && (randDestNode.getRank() == node.getRank())){	
							nextRank = ((mapRand.nextInt(RANK_WEIGHT) == 0) ? (i+1) : i);
							nextTown = mapRand.nextInt(mapNodes.get(nextRank).size());
							randDestNode = mapNodes.get(nextRank).get(nextTown);
							}
						randDestNode.setOnTheTrail(true);
						newTrail = new TrailEdge("Trail from " + node.getLocationName() + " to " + randDestNode.getLocationName(), randDestNode ,node, randGenTrailDanger(mapRand, node.getRank()) );
					}
					//add trail to this location's trail list
					node.addTrail(newTrail);
					
				}//for each trail at location
			}//for each location at rank
		}//for each rank
		if (this.devMode) {
			for (int i = 0; i <= MAX_RANK; i++){
				System.out.println("Locations at rank "+  i + " : " + mapNodes.get(i).size());
				for(LocationNode node : mapNodes.get(i)){
					System.out.println(node.debugToString());
				}//for locationnodes
			}//for each rank
		}//devmode stub
	
	}//map generator method
	
	/**
	 * generates a random value for the trail danger based on the trail's origin's rank with some random element tossed in
	 * @param mapRand the random generator being used to build the map
	 * @return the random danger level of the trail
	 */
	private int randGenTrailDanger(Random mapRand, int trailRank){
		int result = mapRand.nextInt(100);			
		return result;
	}
	
	public int getMaxRank(){
		return MAX_RANK;
	}
	
	/**
	 * returns a string representation of this map, by iterating through each node .
	 * @return the string representation
	 */
	
	public String toString(){
		String resString = "";
		
		return resString;
	}
	
	
}//class worldMap
