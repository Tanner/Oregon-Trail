package model;

import model.worldMap.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import core.ConstantStore;


/**
 * The world map: a collection of locations and paths between.
 */
public class WorldMap {

	/**number of locations on map if none passed to constructor - needs to be static to pass to constructor*/
	private final static int GEN_LOC = 80;  
	/**weight of ranking assignment in random generator - 2 is 50 50 chance for same rank or next rank, higher numbers weight toward next rank*/
	private final int RANK_WEIGHT = 3;  
	/**maximum danger a trail may have - used as an int seed during construction of the map*/
	private final int MAX_DANGER = 100;  
	/**maximum number of exiting trails each location can have*/
	private final int MAX_TRAILS_OUT = 3;  
	/**minimum number of exiting trails each location can have*/
	private final int MIN_TRAILS_OUT = 1;  
	/**maximum number of "levels" of travel west - only oregon city has this as its rank.  edges can only go to edges with equal or higher rank than their origin node*/
	private final int MAX_RANK = 25;
	/**width of map in miles*/
	private final int MAX_X = 1200;
	/**height of map in miles*/
	private final int MAX_Y = 500;
	/**points to starting city - references entire map*/
	private LocationNode mapHead;
	/**points to trailEdge most recently occupied by party*/
	private TrailEdge currTrail;
	/**points to nearest locationNode ahead of party, or current location*/
	private LocationNode currLocationNode;
	/**final destination - Portland Oregon*/
	private LocationNode finalDestination;
	/**number of total locations on map*/
	private int numLocations;
	/**number of total trails on map*/
	private int numTrails;
	/**dev mode for this class, for testing */
	private boolean devMode;
	/**holds which location names have been used*/
	private Map<Integer,List<Boolean>> townNamesUsed;

	/**
	 * Makes a {@code WorldMap} object that tells the game where the party is and what's ahead of them
	 * @param numNodes the number of possible locations on the map - not all of them will be reachable
	 * @param numTrails the number of possible directed trails - not all will be traveled.
	 */
	public WorldMap(int numNodes, String devMode){
		this.devMode = (devMode.length() == 0) ? false : true;
		this.numTrails = 0;
		this.numLocations = numNodes;
		townNamesUsed = new HashMap <Integer, List<Boolean>>();
		List<Boolean> townNamesBool;
		//build local structure that holds a true or false for every location node to see if the name has been used yet
		for (int incr = 0; incr < ConstantStore.TOWN_NAMES.size(); incr++ ){
			townNamesBool = new ArrayList<Boolean>();
			for (int j = 0; j < ConstantStore.TOWN_NAMES.get(incr).size(); j++){
				townNamesBool.add(false);				
			}
			townNamesUsed.put(incr,townNamesBool);
		}
		this.generateMap(this.numLocations);
		this.currLocationNode = this.mapHead;
		this.currTrail = null;
	}
	
	/**
	 * Makes a {@code WorldMap} object that tells the game where the party is and what's ahead of them
	 * @param numNodes the number of possible locations on the map - not all of them will be reachable
	 * @param numTrails the number of possible directed trails - not all will be traveled.
	 */	
	public WorldMap(int numNodes){
		this(numNodes, "");
	}

	/**
	 * Makes a {@code WorldMap} object that tells the game where the party is and what's ahead of them, with predefined number of nodes and edges
	 */
	public WorldMap(){
		//make a generic-sized map for initial testing and development 
		this(GEN_LOC);
	}
	
	/**
	 * used for testing purposes - generates a list of the objects built into the map to the console
	 * @param devMode a non-zero length string used to set to devmode
	 */
	public WorldMap(String devMode){
		this(GEN_LOC, devMode);
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
		int tmpZ =  mapRand.nextInt(MAX_X / MAX_RANK) - (MAX_X / (2 * MAX_RANK));
		int tmpX = MAX_X - (((MAX_X / MAX_RANK) * curRank) + tmpZ);
		while(tmpX < 10){
			tmpX += mapRand.nextInt(MAX_X / MAX_RANK);
			}
		while(tmpX > MAX_X){
			tmpX -= mapRand.nextInt(MAX_X / MAX_RANK);
			}
			//derive y coord of this location on map - should give some range of y between -MAX_Y/2 and +MAX_Y/2
			//MAX_Y/MAX_RANK divies y up into maxrank partitions
			//mapRand of MAXRANK finds the correct general "rank zone"
			//final component tmpZ determines offset within zone
		tmpZ =  mapRand.nextInt(MAX_Y / (2 * MAX_RANK)) - (MAX_Y / MAX_RANK);  //range : +/- partition size/2
		//gives a multiplier that is greatest in the middle of the MAX_RANK # of partitions
		//allows the spread of possible y values of locations to be greatest at the middle of the 
		//path, with the least spread at the ends
		int rnkMult = (curRank <= (MAX_RANK / 2) ) ? (curRank) : (MAX_RANK - curRank);
		//multiplier to determine whether above or below the "equator"
		int tmpW = (mapRand.nextBoolean()) ? (1) : (-1);
		//actual y value passed to locationNode constructor
		int tmpY = ((MAX_Y / MAX_RANK) * rnkMult * tmpW)  + tmpZ;  
		while(tmpY > MAX_Y / 2){
			tmpY -= mapRand.nextInt(MAX_Y / MAX_RANK);
			}
		while(tmpY <  -1 * (MAX_Y / 2)){
			tmpY += mapRand.nextInt(MAX_Y / MAX_RANK);
			}
		//number of exiting trails from this node - random between 1 and MAX_TRAILS_OUT
		numExitTrails = mapRand.nextInt(MAX_TRAILS_OUT + 1 - MIN_TRAILS_OUT) + MIN_TRAILS_OUT;
		LocationNode tempNode = new LocationNode(nameLocation(curRank,mapRand, tmpY),tmpX, tmpY, numExitTrails, curRank, mapRand.nextInt(100));
		return tempNode;
	}
	

	/** 
	 * build the name of the location being generated by querying a list randomly and then
	 * determines the state we're in for a particular range of ranks and y locations
	 * 0 - .33 * MAX_RANK : Positive y : Nebraska Territory | Negative y : Kansas Territory
	 * .34-.66 * MAX_RANK : Positive y : Washington Territory | Negative y : Utah Territory
	 * .67 - 1 * MAX_RANK : Oregon
	 * 
	 * this method will limit the number of duplicate location names in a particular "zone"
	 * which is the equivalent to particular state or territory.  note names may be duplicated
	 * between different territories, but shouldn't be duped in the same territory unless all
	 * other names are taken.
	 * 
	 * @param curRank the current rank of this city
	 * @param yVal the y position of the city on the map
	 * @return the name of this city
	 */
	
	private String nameLocation(int curRank, Random mapRand, int yVal){
		//index of town_names structure corresponding to the town names of this zone
		int rankIndex; 
		String retVal = "";	
		String stateVal;
		String locationVal;
		double maxRankDouble = (double) MAX_RANK;
		if (curRank == 0){//treat this as missouri
			rankIndex = 0;
			stateVal = "Missouri";		
		} else if (curRank <= (0.6*maxRankDouble)){
			rankIndex = (yVal > 0) ? 1 : 2 ;
			stateVal = (yVal > 0) ? "Nebraska Territory" : "Kansas Territory";			
		} else if (curRank <= (0.9*maxRankDouble)) {
			rankIndex = (yVal > -50) ? 3 : 4 ;
			stateVal = (yVal > -50) ? "Washington Territory" : "Utah Territory";						
		} else {
			rankIndex = 5;
			stateVal = "Oregon";
		}
		
		int maxRankNameAraSize = ConstantStore.TOWN_NAMES.get(rankIndex).size();
		int townNameIndex = mapRand.nextInt(maxRankNameAraSize);
		//if the town name at this index is already used, then increment forward until we hit one that hasn't been.  
		//this will only cycle through 1 time - if all are used then they will be reused
		//maxRankNameAraSize is the size of the constantstore name array holding the literals for the names of the towns
		int loopIncr = 0;
		while ((townNamesUsed.get(rankIndex).get(townNameIndex)) && (loopIncr < maxRankNameAraSize)){
			townNameIndex = (townNameIndex + 1) % maxRankNameAraSize;
			loopIncr++;			
		}
		locationVal = ConstantStore.TOWN_NAMES.get(rankIndex).get(townNameIndex);
		townNamesUsed.get(rankIndex).set(townNameIndex, true);
		//verify that town name hasn't been used already in same location/state
		
		
		retVal = locationVal + ", " + stateVal;
		return retVal;
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
		//initialize arraylists at each rank location
		for (int i  = 0; i <= MAX_RANK; i++){
			mapNodes.put(i, new ArrayList<LocationNode>());
		}		
		//List<LocationNode> tempLocationStore = new ArrayList<LocationNode>(numLocations);
			//temp array holding number of locations at each rank, indexed by rank
		int[] numRankAra = new int[MAX_RANK];
			//current node's rank as we're building the node list
			//manufacture random object - make constant seeded now for testing purposes
		//Random mapRand = new Random(12345);
		Random mapRand = new Random();
			//num of edges from current location - will be between 1 and MAX_TRAILS_OUT
		int numExitTrails;
		
			//number of trails out of Independence : MaxTrailsOut constant
			//build beginning and final locations
		this.mapHead = new LocationNode("Independence, Missouri", MAX_X, 0, MAX_TRAILS_OUT);
		this.finalDestination = new LocationNode("Oregon City, Oregon", 0, 0, 0, MAX_RANK, 100);

		//setting mapHead to be "on the trail" - don't want to loop back to home base as we initialize the map structure
		this.mapHead.setOnTheTrail(true);
		numRankAra[0] = 1;
		numRankAra[MAX_RANK - 1] = 1;
		numExitTrails = mapRand.nextInt(MAX_TRAILS_OUT + 1 - MIN_TRAILS_OUT) + MIN_TRAILS_OUT;
		
		//tempLocationStore.add(mapHead);
		mapNodes.get(mapHead.getRank()).add(mapHead);
		//need to build base set of nodes - must have at least 1 per rank to get from independence to portland
		for (int i = 1; i < MAX_RANK; i++){
			LocationNode tmp = generateLocationNode(mapRand, i, numExitTrails);
	//		tempLocationStore.add(tmp);
			mapNodes.get(i).add(tmp);
		}//for loop to build initial path

		//build rest of random map
		for(int i = MAX_RANK; i < numLocations - 1; i++){
			
			int curRankIter;
			int curRank ;
			curRankIter = i % (MAX_RANK - 1) + 1;
			curRank = (mapRand.nextInt(RANK_WEIGHT) == 0) ? curRankIter - 1 : curRankIter;
			//number of trails out of location : 1 to MaxTrailsOut constant
			LocationNode tmp = generateLocationNode(mapRand, curRank, numExitTrails);
	//		tempLocationStore.add(tmp);
			mapNodes.get(tmp.getRank()).add(tmp);
		}//for all locations make a node
	//	tempLocationStore.add(finalDestination);
		mapNodes.get(finalDestination.getRank()).add(finalDestination);

		//as of here we have all the location nodes.  now need to build map.
		//by adding edges.  an edge can only go from a location to one with equal or higher 
		//rank.
		//no need to check MAX_RANK - never any location with exit trails at MAX_RANK
		
		//trailDest is array of used destinations;
		ArrayList<Integer> trailDest;
		
		//trailForward says there's a trail in this set of trails that moves to the next rank 
		//always want at least 1 trail to move forward
		boolean trailForward;
		for (int i = 0; i < MAX_RANK; i++){
			for(LocationNode node : mapNodes.get(i)){
				trailDest = new ArrayList<Integer>();
				trailForward = false;
				//add current node to list of used destination ID's
				trailDest.add(node.getID());
				for (int tNum = 0; tNum < node.getTrails(); tNum++){
					int nextRank = ((mapRand.nextInt(RANK_WEIGHT) == 0) ? (i + 1) : i);
					if (nextRank == MAX_RANK){
						nextRank = MAX_RANK - 1;
					}
					TrailEdge newTrail;
					this.numTrails++;
					int dangerLevel = randGenTrailDanger(mapRand, node.getRank());
					//if we're at final rank before finish, have all edges go to portland
					if (i == MAX_RANK - 1){
						//System.out.println("i = " + i + " size = " + mapNodes.get(nextRank).size() + " random index : " + finalDestination.getRank() + " | Town name : " + finalDestination.getLocationName());
						node.setTrails(1);
						newTrail = new TrailEdge("Trail to " + finalDestination.getName(), finalDestination, node, dangerLevel );
					} else {
						if ((tNum == node.getTrails() - 1) && (!trailForward)) {
							nextRank = node.getRank() + 1;
						}
						//nexttown holds size of arraylist for locations - used as random source to determine where trails go
						int nextTown = mapRand.nextInt(mapNodes.get(nextRank).size());
						LocationNode randDestNode = mapNodes.get(nextRank).get(nextTown);
						//attempt to minimize going to a location that's already on the map and in the same zone as where we
						//currently are - minimize possibility of moving laterally repeatedly
						if ((mapRand.nextInt(100) < 95) || (randDestNode.getID() == (node.getID()))){
							while (trailDest.contains(randDestNode.getID())){
								if ((tNum == node.getTrails() - 1) && (!trailForward)) {
									//force rank to increase by 1 (i.e. go west 1 rank) if we're at the last trail in the list of trails and we haven't gone west yet
									nextRank = node.getRank() + 1;
								} else {
									nextRank = ((mapRand.nextInt(RANK_WEIGHT) == 0) ? (i + 1) : i);
								}
								nextTown = mapRand.nextInt(mapNodes.get(nextRank).size());
								randDestNode = mapNodes.get(nextRank).get(nextTown);
								}
						}
						trailDest.add(randDestNode.getID());
						newTrail = new TrailEdge("Trail to " + randDestNode.getName(), randDestNode, node, dangerLevel );
						if (newTrail.getOrigin().getRank() != newTrail.getDestination().getRank()){
							trailForward = true;
						}
					}
					//add trail to this location's trail list
					node.addTrail(newTrail);
					
				}//for each trail at location
			}//for each location at rank
		}//for each rank
		if (this.devMode) {
			for (int i = 0; i <= MAX_RANK; i++){
				System.out.println("Locations at rank " +  i + " : " + mapNodes.get(i).size());
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
		int result = mapRand.nextInt(MAX_DANGER);
		return result;
	}
	
	public int getMaxRank(){
		return MAX_RANK;
	}
	
	/**
	 * returns head pointer of the map
	 * @return the map's head pointer
	 */
	public LocationNode getMapHead(){
		return this.mapHead;
	}
	
	/**
	 * return the current, or most recent, location of the party
	 * @return the location node most recently visited by the party
	 */
	public LocationNode getCurrLocationNode(){
		return this.currLocationNode;		
	}
	
	/**
	 * return the current, or most recent, trail traveled by the party
	 * @return the trail the party most recently traveled
	 */
	public TrailEdge getCurrTrail(){
		return this.currTrail;
	}
	
	/**
	 * set current location or most recent location node of party
	 * @param destination most recent/current trail
	 */
	public void setCurrLocationNode(LocationNode currLocationNode){
		this.currLocationNode = currLocationNode;
	}
	
	/**
	 * set current or most recent trail occupied by party
	 * @param trailEdge most recent/current trail
	 */
	public void setCurrTrail(TrailEdge currTrail){
		this.currTrail = currTrail;
	}
	
	/**
	 * returns a string representation of this map, by iterating through each node .
	 * @return the string representation
	 */
	public String toString(){
		String resString = "I'm a map, I'm a nappy map, and I dance dance dance on " + this.numTrails + " trails to " + this.numLocations + " locations";
		
		return resString;
	}
	
	
}//class worldMap
