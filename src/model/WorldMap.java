package model;

import model.worldMap.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import core.ConstantStore;


/**
 * The world map: a collection of locations and paths between.
 */
public class WorldMap implements Serializable {
	
	/**number of locations on map if none passed to constructor - needs to be static to pass to constructor*/
	public final static int GEN_LOC = 80;  
	/**max quality for a location, which determines how big/populated/well known it is.*/
	public final int  MAX_LOC_QUAL = 100;
	/**weight of ranking assignment in random generator - 2 is 50 50 chance for same rank or next rank, higher numbers weight toward next rank*/
	public final int RANK_WEIGHT = 3;  
	/**maximum danger a trail may have - used as an int seed during construction of the map*/
	public final int MAX_DANGER = 100;  
	/**maximum number of exiting trails each location can have*/
	public final int MAX_TRAILS_OUT = 3;  
	/**minimum number of exiting trails each location can have*/
	public final int MIN_TRAILS_OUT = 1;  
	/**maximum number of "levels" of travel west - only oregon city has this as its rank.  edges can only go to edges with equal or higher rank than their origin node*/
	public final int MAX_RANK = 25;
	/**width of map in miles*/
	public final int MAX_X = 1200;
	/**height of map in miles*/
	public final int MAX_Y = 500;

	/**make a map to hold the game map data (yo dawg), indexed by locationnode.rank, with each value being an arraylist of locationnodes.*/
	private Map<Integer, List<LocationNode>> mapNodes;

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
	private Map<ConstantStore.StateIdx,List<Boolean>> townNamesUsed;

	/**
	 * Makes a {@code WorldMap} object that tells the game where the party is and what's ahead of them
	 * @param numNodes the number of possible locations on the map - not all of them will be reachable
	 * @param numTrails the number of possible directed trails - not all will be traveled.
	 */
	public WorldMap(int numNodes, String devMode){
		this.devMode = (devMode.length() == 0) ? false : true;
		this.numTrails = 0;
		this.numLocations = numNodes;
		//this.numLocations = 3010;
		if (this.devMode) {
			this.numLocations = 3000;
		}
		this.mapNodes = new HashMap<Integer, List<LocationNode>>();
		this.townNamesUsed = new HashMap <ConstantStore.StateIdx, List<Boolean>>();
		List<Boolean> townNamesBool;
		//build local structure that holds a true or false for every location node to see if the name has been used yet
		for (ConstantStore.StateIdx idx : ConstantStore.StateIdx.values()){
			townNamesBool = new ArrayList<Boolean>();
			for (int j = 0; j < ConstantStore.TOWN_NAMES.get(idx).size(); j++){
				townNamesBool.add(false);
			}
			townNamesUsed.put(idx, townNamesBool);
		}
		this.generateMap(this.numLocations);
		this.currLocationNode = this.mapHead;
		this.currTrail = null;
	}
	
	/**
	 * Makes a {@code WorldMap} object that tells the game where the party is and what's ahead of them
	 * @param numNodes the number of possible locations on the map - not all of them will be reachable
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
		if ((tmpX > 10) && (tmpX < 50)){
			tmpX -= mapRand.nextInt(20);
		}
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
		int tmpY = (mapRand.nextInt(MAX_Y / MAX_RANK) * mapRand.nextInt(rnkMult + 1) * tmpW  + tmpZ);  
		while(tmpY > MAX_Y / 2){
			tmpY -= mapRand.nextInt(MAX_Y / MAX_RANK);
			}
		while(tmpY <  -1 * (MAX_Y / 2)){
			tmpY += mapRand.nextInt(MAX_Y / MAX_RANK);
			}
		//number of exiting trails from this node - random between 1 and MAX_TRAILS_OUT
		numExitTrails = mapRand.nextInt(MAX_TRAILS_OUT + 1 - MIN_TRAILS_OUT) + MIN_TRAILS_OUT;
		LocationNode tempNode = new LocationNode(nameLocation(curRank, mapRand, tmpY), tmpX, tmpY, numExitTrails, curRank, mapRand.nextInt(MAX_LOC_QUAL));
		return tempNode;
	}
	
/*
	private String reNameLocation(LocationNode node, Random mapRand){
		
		ConstantStore.StateIdx stateIdx; 
		String retVal = "";
		//current name without the territory/state
		String curTownName = node.getName();
		int charLoc = curTownName.indexOf(',');
		//curTownName = curTownName.substring(indexOf());
		if (charLoc == -1){
			retVal = node.getName();
		}
		else{//comma exists, means this name has a territory or state name - need to recalcuate it based on the x,y position on the map
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			retVal = curTownName;// + ", " + ConstantStore.STATE_NAMES.get(stateIdx);
		}
		return node.getName();
	}
	
	*/
	
	/** 
	 * build the name of the location being generated by querying a list randomly and then
	 * determines the state we're in for a particular range of ranks and y locations
	 * 0 - .35 * MAX_RANK : Positive y : Nebraska Territory | Negative y : Kansas Territory
	 * .36-.60 * MAX_RANK : Positive y > ~70 : Dakota Territory | Middle y : Nebraska Territory | negative y < ~-50 : Colorado territory
	 * .61-.80 * MAX_RANK : Y > ~-50 : Washington Territory | negative y < ~-50 : utah territory
	 * .8 - 1 * MAX_RANK : Oregon
	 * 
	 * this method will limit the number of duplicate location names in a particular "zone"
	 * which is the equivalent to particular state or territory.  note names may be duplicated
	 * between different territories, but shouldn't be duped in the same territory unless all
	 * other names are taken.
	 * 
	 * 
	 * @param curRank the current rank of this city
	 * @param yVal the y position of the city on the map
	 * @return the name of this city
	 */
	
	private String nameLocation(int curRank, Random mapRand, int yVal){
		//index of town_names structure corresponding to the town names of this zone
		ConstantStore.StateIdx rankIndex; 
		String retVal = "";
		String locationVal;
		double maxRankDouble = (double) MAX_RANK;
		if (curRank == 0){//treat this as missouri
			rankIndex = ConstantStore.StateIdx.MISSOURI;
		} else if (curRank <= (0.05 * maxRankDouble)){
			rankIndex = (yVal > 10) ? ConstantStore.StateIdx.NEBRASKA_TERRITORY :  ConstantStore.StateIdx.KANSAS_TERRITORY ;
		} else if (curRank <= (0.25 * maxRankDouble)){
			rankIndex = (yVal > -20) ? ConstantStore.StateIdx.NEBRASKA_TERRITORY :  ConstantStore.StateIdx.KANSAS_TERRITORY ;
		} else if (curRank <= (0.35 * maxRankDouble)){
			rankIndex = (yVal > -20) ? ConstantStore.StateIdx.NEBRASKA_TERRITORY :  ConstantStore.StateIdx.KANSAS_TERRITORY ;
		} else if (curRank <= (0.45 * maxRankDouble)){
			rankIndex = (yVal > 80) ? ConstantStore.StateIdx.DAKOTA_TERRITORY : ConstantStore.StateIdx.NEBRASKA_TERRITORY;
			if (yVal <  -50){//use kansas names but colorado territory
				rankIndex = ConstantStore.StateIdx.COLORADO_TERRITORY;
			}
		} else if (curRank <= (0.5 * maxRankDouble)){
			rankIndex = (yVal > 80) ? ConstantStore.StateIdx.DAKOTA_TERRITORY : ConstantStore.StateIdx.NEBRASKA_TERRITORY;
			if (yVal <  -80){//use kansas names but colorado territory
				rankIndex = ConstantStore.StateIdx.COLORADO_TERRITORY;
			}
		} else if (curRank <= (0.6 * maxRankDouble)){
			rankIndex = (yVal > 55) ? ConstantStore.StateIdx.DAKOTA_TERRITORY : ConstantStore.StateIdx.NEBRASKA_TERRITORY;
			if (yVal <  -95){//use kansas names but colorado territory
				rankIndex = ConstantStore.StateIdx.COLORADO_TERRITORY;
			}
		} else if (curRank <= (0.7 * maxRankDouble)) {
			rankIndex = (yVal > -70) ? ConstantStore.StateIdx.WASHINGTON_TERRITORY : ConstantStore.StateIdx.UTAH_TERRITORY ;
			if (yVal > 100){//use Dakota names
				rankIndex = ConstantStore.StateIdx.DAKOTA_TERRITORY;
			}
			
		} else if (curRank <= (0.8 * maxRankDouble)) {
			rankIndex = (yVal > -70) ? ConstantStore.StateIdx.WASHINGTON_TERRITORY : ConstantStore.StateIdx.UTAH_TERRITORY ;
			if (yVal > 120){//use Dakota names
				rankIndex = ConstantStore.StateIdx.DAKOTA_TERRITORY;
			}
			
		} else if (curRank <= (0.83 * maxRankDouble)) {
			rankIndex = (yVal > -100) ? ConstantStore.StateIdx.WASHINGTON_TERRITORY : ConstantStore.StateIdx.UTAH_TERRITORY ;
			if (yVal > 150){//use Dakota names
				rankIndex = ConstantStore.StateIdx.DAKOTA_TERRITORY;
			}
		} else {
			rankIndex = ConstantStore.StateIdx.OREGON;
		}
		
		int maxRankNameAraSize = ConstantStore.TOWN_NAMES.get(rankIndex).size();
		int townNameIndex = mapRand.nextInt(maxRankNameAraSize);
		//if the town name at this index is already used, then increment forward until we hit one that hasn't been.  
		//this will only cycle through 1 time - if all are used then they will be reused with prefixStore set to "West"
		//maxRankNameAraSize is the size of the constantstore name array holding the literals for the names of the towns
		int loopIncr = 0;
		String prefixString = "";
		while ((townNamesUsed.get(rankIndex).get(townNameIndex)) && (loopIncr < maxRankNameAraSize)){
			townNameIndex = (townNameIndex + 1) % maxRankNameAraSize;
			loopIncr++;
		}
		if (loopIncr >= maxRankNameAraSize){
			int testVal = mapRand.nextInt(7);
			
			if ((testVal & 1) == 1) {
				prefixString += (mapRand.nextInt(2) == 0) ? "New " : "Old ";
			}
			else {
				prefixString += (mapRand.nextInt(2) == 0) ? "Little " : "Big ";
			}
			if ((testVal & 2) == 2) {
				prefixString += (mapRand.nextInt(2) == 0) ? "West " : "North ";
			}
			else {
				prefixString += (mapRand.nextInt(2) == 0) ? "East " : "South ";
			}
			if ((testVal & 4) == 4){
				prefixString += (mapRand.nextInt(2) == 0) ? "Low " : "Dusty ";	
			} else {
				prefixString += (mapRand.nextInt(2) == 0) ? "Crimson " : "Gray ";	
			}
		}
		locationVal = ConstantStore.TOWN_NAMES.get(rankIndex).get(townNameIndex);
		townNamesUsed.get(rankIndex).set(townNameIndex, true);
		//verify that town name hasn't been used already in same location/state
		
		
		retVal = prefixString + locationVal + ", " + ConstantStore.STATE_NAMES.get(rankIndex);
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
		
			//initialize arraylists at each rank location
		for (int i  = 0; i <= MAX_RANK; i++){
			this.mapNodes.put(i, new ArrayList<LocationNode>());
		}	
			//temp array holding number of locations at each rank, indexed by rank
		int[] numRankAra = new int[MAX_RANK];
			//current node's rank as we're building the node list
			//manufacture random object - make constant seeded now for testing purposes
		//Random mapRand = new Random(54321);
		Random mapRand = new Random();
			//num of edges from current location - will be between 1 and MAX_TRAILS_OUT
		int numExitTrails;
		
			//number of trails out of Independence : MaxTrailsOut constant
			//build beginning and final locations
		this.mapHead = new LocationNode("Independence, Missouri", MAX_X, 0, MAX_TRAILS_OUT);
		this.finalDestination = new LocationNode("Oregon City, Oregon", 0, 0, 0, MAX_RANK, 100);
		this.finalDestination.setVisible(true);
		//setting mapHead to be "on the trail" - don't want to loop back to home base as we initialize the map structure
		this.mapHead.setOnTheTrail(true);
		numRankAra[0] = 1;
		numRankAra[MAX_RANK - 1] = 1;
		numExitTrails = mapRand.nextInt(MAX_TRAILS_OUT + 1 - MIN_TRAILS_OUT) + MIN_TRAILS_OUT;
		
		this.mapNodes.get(mapHead.getRank()).add(mapHead);
		//need to build base set of nodes - must have at least 1 per rank to get from independence to portland
		for (int i = 1; i < MAX_RANK; i++){
			LocationNode tmp = generateLocationNode(mapRand, i, numExitTrails);
			this.mapNodes.get(i).add(tmp);
		}//for loop to build initial path

		//build rest of random map
		for(int i = MAX_RANK; i < numLocations - 1; i++){
			
			int curRankIter;
			int curRank ;
			curRankIter = i % (MAX_RANK - 1) + 1;
			curRank = (mapRand.nextInt(RANK_WEIGHT) == 0) ? curRankIter - 1 : curRankIter;
			//number of trails out of location : 1 to MaxTrailsOut constant
			LocationNode tmp = generateLocationNode(mapRand, curRank, numExitTrails);
			this.mapNodes.get(tmp.getRank()).add(tmp);
		}//for all locations make a node
		this.mapNodes.get(finalDestination.getRank()).add(finalDestination);

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
			for(LocationNode node : this.mapNodes.get(i)){
				//manufacture proper coords for player map from x/y pos
				convertToMapCoords(node);
				//node.setName(reNameLocation(node, mapRand));
				//node is of sufficient "quality" to show up on map regardless of having been visited or not
				if (node.getCondition().getCurrent() / this.MAX_LOC_QUAL > .90){
					node.setVisible(true);					
				}
				//define array holding all destinations that have been used.
				trailDest = new ArrayList<Integer>();
				trailForward = false;
				//add current node to list of used destination ID's
				//trailDest.add(node.getID());
				
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
						//System.out.println("i = " + i + " size = " + this.mapNodes.get(nextRank).size() + " random index : " + finalDestination.getRank() + " | Town name : " + finalDestination.getLocationName());
						node.setTrails(1);
						newTrail = new TrailEdge("Trail to " + finalDestination.getName(), finalDestination, node, dangerLevel );
					} else {
						if ((tNum == node.getTrails() - 1) && (!trailForward)) {
							nextRank = node.getRank() + 1;
						}
						//nexttown holds size of arraylist for locations - used as random source to determine where trails go
						int nextTown = mapRand.nextInt(this.mapNodes.get(nextRank).size());
						LocationNode randDestNode = this.mapNodes.get(nextRank).get(nextTown);
						//attempt to minimize going to a location that's already on the map and in the same zone as where we
						//currently are - minimize possibility of moving laterally repeatedly
						if ((mapRand.nextInt(100) < 90) || (randDestNode.getID() == (node.getID()))){
							while (trailDest.contains(randDestNode.getID())){
								if ((tNum == node.getTrails() - 1) && (!trailForward)) {
									//force rank to increase by 1 (i.e. go west 1 rank) if we're at the last trail in the list of trails and we haven't gone west yet
									nextRank = node.getRank() + 1;
								} else {
									nextRank = ((mapRand.nextInt(RANK_WEIGHT) == 0) ? (i + 1) : i);
								}
								nextTown = mapRand.nextInt(this.mapNodes.get(nextRank).size());
								randDestNode = this.mapNodes.get(nextRank).get(nextTown);
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
		//convert oregon city's coords to actual coords on our map
		convertToMapCoords(this.mapNodes.get(finalDestination.getRank()).get(0));
		
		
		
		if (this.devMode) {
			for (int i = 0; i <= MAX_RANK; i++){
				System.out.println("Locations at rank " +  i + " : " + this.mapNodes.get(i).size());
				for(LocationNode node : this.mapNodes.get(i)){
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
	
	/**
	 * returns the structure holding the world map
	 * @return the world map - nodes and edges
	 */
	
	public Map<Integer, List<LocationNode>> getMapNodes(){
		return this.mapNodes;
	}
	
	/**
	 * this method will convert the internal coords of the map to the physical representation used by the player map.
	 * 
	 */
	private void convertToMapCoords(LocationNode node){
		double newX;
		double newY;
		
		newX = (node.MAP_XPOS + 60) * (920.0/this.MAX_X) ; 
		newY = (((-1 * node.MAP_YPOS)/1.4) + 320) + ((node.MAP_XPOS -600)/2.4);
		if (newX > 1050){
			newX = 1050;
		}
		if (newY > 565){
			newY = 565;
		}
		node.setPlayerMapX(newX);
		node.setPlayerMapY(newY);
	}
	
	/** 
	 * returns the currently set maximum rank value for this map
	 * @return the constant max rank
	 */
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
	 * @return the numLocations
	 */
	public int getNumLocations() {
		return numLocations;
	}

	/**
	 * @param numLocations the numLocations to set
	 */
	public void setNumLocations(int numLocations) {
		this.numLocations = numLocations;
	}

	/**
	 * @return the numTrails
	 */
	public int getNumTrails() {
		return numTrails;
	}

	/**
	 * @param numTrails the numTrails to set
	 */
	public void setNumTrails(int numTrails) {
		this.numTrails = numTrails;
	}

	/**
	 * set current location or most recent location node of party
	 * @param currLocationNode Current location
	 */
	public void setCurrLocationNode(LocationNode currLocationNode){
		this.currLocationNode = currLocationNode;
		this.currLocationNode.setVisible(true);
		for(TrailEdge edge : currLocationNode.getOutboundTrails()){
			edge.setVisible(true);
			edge.getDestination().setVisible(true);
		}
	}
	
	/**
	 * resets the relevant max values in the locations and edges upon game reset
	 */
	public void resetMap(){
		LocationNode.resetCount();
		TrailEdge.resetCount();
		TrailEdge.resetTrails();

	}
	
	/**
	 * set current or most recent trail occupied by party
	 * @param currTrail Most current trail
	 */
	public void setCurrTrail(TrailEdge currTrail){
		this.currTrail = currTrail;
		if (this.currTrail != null){
			this.currTrail.setVisible(true);
		}
		currTrail.getDestination().setVisible(true);
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
