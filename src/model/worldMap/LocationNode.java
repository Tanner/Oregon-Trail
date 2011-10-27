package model.worldMap;

import java.util.ArrayList;
import java.util.List;
import model.Condition;


/**
 * class responsible for holding the information about a location the party can travel to.
 * @author NULL&&void
 *
 */

public class LocationNode extends MapObject {
	
	/** real world latitude */	
	public final double WORLD_LATITUDE;
	/** real world longitude */
	public final double WORLD_LONGITUDE;
	/** map position */	
	public final double MAP_XPOS;
	/** real world longitude */
	public final double MAP_YPOS;
	/** ID for this node - used only internally for data store */ 
	public final int ID;
	/** class-wide counter of nodes */
	private static int nodeCount;
	/** how far west this location is - a location cannot have a trail leading to a location of lower rank, only equal or greater rank*/
	private int rank;
	/** how many outbound trails this location has */
	private int trails;
	/**already part of the trail or not */
	private boolean onTheTrail;
	
	private String locationName;
	private List<TrailEdge> outboundTrails;
	
	/**
	 * Constructor for making a {@code LocationNode}
	 * @param locationName String representing name of location
	 * @param xPos the x position for the location on the world map
	 * @param yPos the y position for the location on the world map
	 * @param latitude Real World(tm) latitude of location (for MapScene manufacture)
	 * @param longitude Real World(tm) longitude of location (for MapScene Manufacture)
	 * @param trails number of trails exiting this location
	 * @param quality quality of location - lower quality means smaller town or outpost
	 */
	public LocationNode(String locationName, int xPos, int yPos, double latitude, double longitude, int trails, int rank, int quality){
		this.ID = LocationNode.nodeCount++;
		//until we can get a nice source for lat and long data
		MAP_XPOS = xPos;
		MAP_YPOS = yPos;
		//eventually want to implement real world lat and long
		WORLD_LATITUDE = latitude;
		WORLD_LONGITUDE = longitude;
		this.trails = trails;
		this.onTheTrail = false;
		this.outboundTrails = new ArrayList<TrailEdge>(trails);
		this.locationName = locationName;
		this.rank = rank;
		this.quality = new Condition((int) quality);
	}
	
	/**
	 * Constructor for making a {@code LocationNode}
	 * @param name
	 * @param xPos
	 * @param yPos
	 * @param trails
	 * @param rank
	 */
	public LocationNode(String locationName, int xPos, int yPos, int trails, int rank, int quality){
		//makes unique name for location, temporarily, until we can make them prettier.
		this(locationName, xPos, yPos, 0, 90, trails, rank, quality);
		
	}
	
	public LocationNode(int xPos, int yPos, int trails, int rank, int quality){
		//makes unique name for location, temporarily, until we can make them prettier.
		this("Location " + LocationNode.nodeCount, xPos, yPos, 0, 90, trails, rank, quality);
	}
	
	
	public LocationNode(String locationName, int xPos, int yPos, int trails){
		//makes unique name for location, temporarily, until we can make them prettier.
		//this constructor makes Independence
		this(locationName, xPos, yPos, 0, 90, trails, 0, 100);
		
	}
	
	public int getID(){
		return this.ID;
	}
	
	public void setOnTheTrail(boolean onTheTrail){
		this.onTheTrail = onTheTrail;
	}
	
	public boolean getOnTheTrail(){
		return this.onTheTrail;
	}
	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getTrails() {
		return trails;
	}

	public void setTrails(int trails) {
		this.trails = trails;
	}

	/**
	 * adds a new, leaving, {@code TrailEdge} to this {@code LocationNode}
	 * @param newTrail
	 */
	
	public void addTrail(TrailEdge newTrail){
		this.outboundTrails.add(newTrail);
	}
	/**
	 * gets the entire list of {@code outboundTrails}. Not preferred method of acces - keep datastore hidden
	 * @return the list of trails for this particular node
	 */
	public List<TrailEdge> getOutboundTrails(){
		return this.outboundTrails;
	}
	
	/**
	 * get a particular trail from the {@code outboundTrail} list by index
	 * @param index the particular trail we want
	 * @return the TrailEdge object we want
	 */
	public TrailEdge getOutBoundTrailByIndex(int index){
		return this.outboundTrails.get(index);
	}
	
	public String getLocationName(){
		return this.locationName;
	}
	/**
	 * returns the string representation of this location
	 */
	public String toString(){
		
		return this.locationName + " (which has " + this.trails + " westward trails) ";
	}
	
	/**
	 * method to return all instance variables easily without having to string getters
	 * only dev mode
	 * @return string of all string representations of private variables
	 */
	public String debugToString(){
		String retVal;
		int numTrails = this.outboundTrails.size();
		retVal = "Name : \t" + this.locationName + "\t| X pos : \t" + this.MAP_XPOS + " \t| Y pos : \t" + this.MAP_YPOS + "\n";
//		retVal += "World Lat : \t" + this.WORLD_LATITUDE + "\t| World Long : " + this.WORLD_LONGITUDE + " \n";
		retVal += "Internal ID : \t" + this.ID + "\t| Total Nodes currently made : \t" + LocationNode.nodeCount + " \n";
		retVal += "";
		retVal += "Rank : \t\t" + this.rank + "\t| Total Exit Trail Count : \t" + trails + " \n";
		
		if (numTrails == 0){
			retVal += "\tNo trails implemented from \"" + this.locationName +  "\"\n";
		}
		
		for (int i = 0; i < numTrails ;i++){
			retVal += "\tExit Trail " + i + " from \"" + this.locationName + "\" : " + this.outboundTrails.get(i).debugToString();
		}
		return retVal;
	}

}//class LocationNode
