package model.worldMap;

import java.util.ArrayList;
import java.util.List;

//import core.ConstantStore;
import model.Condition;


/**
 * class responsible for holding the information about a location the party can travel to.
 * @author NULL&&void
 *
 */

@SuppressWarnings("serial")
public class LocationNode extends MapObject {
	
	/**map object's max x dimension*/
	private final int MAP_X_MAX;
	/**map object's max y dimension*/
	private final int MAP_Y_MAX;	
	/** real world latitude */	
	public final double WORLD_LATITUDE;
	/** real world longitude */
	public final double WORLD_LONGITUDE;
	/** internal representation of x coord*/	
	public final double MAP_XPOS;
	/** internal representation of y coord */
	public final double MAP_YPOS;
	/** translated coord for x pos on player map*/
	private double playerMapX;
	/** translated coord for y pos on player map*/
	private double playerMapY;	
	/** ID for this node - used only internally for data store */ 
	public final int ID;
	/** how far west this location is - a location cannot have a trail leading to a location of lower rank, only equal or greater rank*/
	private int rank;
	/** how many outbound trails this location has */
	private int trails;
	/**already part of the trail or not */
	private boolean onTheTrail;
	/**has an incoming trail - might not due to random nature of map generation*/
	private boolean hasInTrail;
	/**all the outbound trails from this particular location*/
	
	private List<TrailEdge> outboundTrails;
	
	/**
	 * Constructor for making a {@code LocationNode}
	 * @param locationName String representing name of location
	 * @param xPos the x position for the location on the world map
	 * @param yPos the y position for the location on the world map
	 * @param latitude Real World(tm) latitude of location (for MapScene manufacture) - not used
	 * @param longitude Real World(tm) longitude of location (for MapScene Manufacture) - not used
	 * @param trails number of trails exiting this location
	 * @param quality quality of location - lower quality means smaller town or outpost
	 */
	public LocationNode(String locationName, int xPos, int yPos, double latitude, double longitude, int trails, int rank, int quality, int MAP_X_MAX, int MAP_Y_MAX){
		this.ID = LocationNode.count++;
		MAP_XPOS = xPos;
		MAP_YPOS = yPos;
		//eventually want to implement real world lat and long
		WORLD_LATITUDE = latitude;
		WORLD_LONGITUDE = longitude;
		this.trails = trails;
		this.onTheTrail = false;
		this.outboundTrails = new ArrayList<TrailEdge>(trails);
		this.name = locationName;
		this.rank = rank;
		this.quality = new Condition(0, 100, (int) quality);
		this.visible = false;
		this.hasInTrail = false;
		this.MAP_X_MAX = MAP_X_MAX;
		this.MAP_Y_MAX = MAP_Y_MAX;
		this.convertToMapCoords();
	}
	
	/**
	 * Constructor for making a {@code LocationNode}
	 * @param locatioName
	 * @param xPos
	 * @param yPos
	 * @param trails
	 * @param rank
	 */
	public LocationNode(String locationName, int xPos, int yPos, int trails, int rank, int quality, int MAP_X_MAX, int MAP_Y_MAX){
		this(locationName, xPos, yPos, 0, 90, trails, rank, quality, MAP_X_MAX, MAP_Y_MAX);
		
	}
	
	public LocationNode(int xPos, int yPos, int trails, int rank, int quality, int MAP_X_MAX, int MAP_Y_MAX){
		//makes unique name for location, temporarily, until we can make them prettier.
		this("Location " + LocationNode.count, xPos, yPos, 0, 90, trails, rank, quality,  MAP_X_MAX,  MAP_Y_MAX);
	}
	
	private void convertToMapCoords(){
		double newX;
		double newY;
		
		newX = (this.MAP_XPOS + 60) * (920.0/this.MAP_X_MAX) ; 
		newY = (((-1 * this.MAP_YPOS)/1.4) + 300) + ((this.MAP_XPOS -600)/2.4);
		if (newX > 1050){
			newX = 1050;
		}
		if (newY > 565){
			newY = 565;
		}
		this.setPlayerMapX(newX);
		this.setPlayerMapY(newY);
	}

	public LocationNode(String locationName, int xPos, int yPos, int trails, int MAP_X_MAX, int MAP_Y_MAX){
		//makes unique name for location, temporarily, until we can make them prettier.
		//this constructor makes Independence
		this(locationName, xPos, yPos, 0, 90, trails, 0, 100,  MAP_X_MAX,  MAP_Y_MAX);
		
	}
	
	public int getID(){
		return this.ID;
	}
	
	public boolean getHasInTrail(){
		return this.hasInTrail;
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
	 * gets the entire list of {@code outboundTrails}. Not preferred method of access - keep datastore hidden
	 * @return the list of trails for this particular node
	 */
	public List<TrailEdge> getOutboundTrails(){
		return this.outboundTrails;
	}

	public double getPlayerMapX() {
		return playerMapX;
	}

	public void setPlayerMapX(double playerMapX) {
		this.playerMapX = playerMapX;
	}

	public double getPlayerMapY() {
		return playerMapY;
	}

	public void setPlayerMapY(double playerMapY) {
		this.playerMapY = playerMapY;
	}

	
	/**
	 * get a particular trail from the {@code outboundTrail} list by index
	 * @param index the particular trail we want
	 * @return the TrailEdge object we want
	 */
	public TrailEdge getOutBoundTrailByIndex(int index){
		return this.outboundTrails.get(index);
	}
	
	/**
	 * returns the string representation of this location
	 */
	public String toString(){
		
		return this.name + " Rank : " + this.rank + " (which has " + this.trails + " westward trails) ";
	}

	/**
	 * @param hasInTrail the hasInTrail to set
	 */
	public void setHasInTrail(boolean hasInTrail) {
		this.hasInTrail = hasInTrail;
	}


	/**
	 * method to return all instance variables easily without having to string getters
	 * only dev mode
	 * @return string of all string representations of private variables
	 */
	public String debugToString(){
		String retVal;
		int numTrails = this.outboundTrails.size();
		retVal = "Name : \t" + this.name + "\t| X pos : \t" + this.MAP_XPOS + " \t| Y pos : \t" + this.MAP_YPOS + "\n";
//		retVal += "World Lat : \t" + this.WORLD_LATITUDE + "\t| World Long : " + this.WORLD_LONGITUDE + " \n";
		retVal += "Map X : \t" + this.playerMapX + "\t| Map Y : " + this.playerMapY + " \n";
		retVal += "Max X : \t" + this.MAP_X_MAX + "\t| Max Y : " + this.MAP_Y_MAX + " \n";
		retVal += "Internal ID : \t" + this.ID + "\t| Total Nodes currently made : \t" + LocationNode.count + " \n";
		retVal += "Location Quality : \t " +  this.quality + "\n";
		retVal += "Rank : \t\t" + this.rank + "\t| Total Exit Trail Count : \t" + trails + " \n";
		
		if (numTrails == 0){
			retVal += "\tNo trails implemented from \"" + this.name +  "\"\n";
		}
		
		for (int i = 0; i < numTrails ;i++){
			retVal += "\tExit Trail " + i + " from \"" + this.name + "\" : " + this.outboundTrails.get(i).debugToString();
		}
		return retVal;
	}

}//class LocationNode
