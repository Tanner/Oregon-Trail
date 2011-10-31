package model.worldMap;


import core.Logger;
import model.Condition;

public class TrailEdge extends MapObject {
	
	/**end point of this edge - destination node*/
	private LocationNode destination;
	/**end point of this edge - where we started*/
	private LocationNode origin;
	/**describes how hard the transit on this trail will be*/
	private int dangerLevel;
	/**length of the trail.  calculated by the distance between the two location nodes.*/
	private float length;
	/**total number of edges built*/
	private static int edgeCount;
	/**length of the longest trail on map*/
	private static float longestTrail = 0;
	/**length of the shortest trail on map*/
	private static float shortestTrail = 9999;
	/**unique id corresponding to this edge.*/
	private final int ID;
	
	
	
	/**
	 * Make a trail edge that will connect two locationNodes
	 * @param name
	 * @param destination
	 * @param origin
	 * @param dangerLevel
	 */
	
	public TrailEdge(String name, LocationNode destination, LocationNode origin, int dangerLevel){
		this.destination = destination;
		this.origin = origin;
		this.ID = TrailEdge.edgeCount++;
		this.dangerLevel = dangerLevel;
		this.name ="Trail to " + destination.getName() ; // this.getRoughLength() + ", " + this.getDangerRating() + "," + this.getRoughDirection() + 
		this.length = calcDistance(destination.MAP_XPOS, origin.MAP_XPOS, destination.MAP_YPOS, origin.MAP_YPOS);
		if (this.length > TrailEdge.longestTrail){
			TrailEdge.longestTrail = this.length;
		}
		if (this.length < TrailEdge.shortestTrail){
			TrailEdge.shortestTrail = this.length;
		}
		this.quality = new Condition((int) this.length);
		}

	private float calcDistance(double destX, double origX, double destY, double origY){
		float result = 0;
		//capture any spurious math errors or overflow in coords
		try{
			result = (float) Math.sqrt((Math.pow((origX - destX), 2) + Math.pow((origY - destY), 2)));
		}
		catch (Exception e){
			Logger.log("Error calculating distance between two nodes in map generation - bad math in calcDistance", Logger.Level.ERROR);
			System.out.println("bad math in edge distance calculation for edge : " + this.ID + " " + this.toString());
		}
		
		return result;
	}

	@Override
	public String toString(){
		return this.name;
	}
	/**
	 * move along a trail a certain distance
	 * @param movement the amount to move along the trail
	 */
	public void advance(double movement) {
		this.quality.decrease(movement);
	}
	
	/**
	 * Returns the amount of distance left to go for this trail.
	 * @return Distance left to travel
	 */
	public double getDistanceToGo() {
		return quality.getCurrent();
	}
	
	/**
	 * Returns a verbal approximation of distance left to go on the trail
	 * @return An approximation of the distance left.
	 */
	public String getRoughDistanceToGo() {
		double current = 1 - quality.getPercentage();
		String str;
		if (current < .05) {
			str = "Just starting toward ";
		} else if (current < .25) {
			str = "Nowhere close to ";
		} else if (current < .5) {
			str = "Getting closer to ";
		} else if (current < .75) {
			str = "More than halfway to ";
		} else if (current < .95) {
			str = "Just a little further to ";
		} else {
			str = "On the outskirts of ";
		}
		return str;
	}

	/**
	 * Returns a verbal approximation of the direction of the trail, based on the y coords of the destination and the origin
	 * @return A string representation of the direction of the trail.
	 */
	public String getRoughDirection(){
		String resStr;
		
		if (this.destination.MAP_YPOS > this.origin.MAP_YPOS ) {//northwestern trail
			resStr =  "NW";
		} else if (this.destination.MAP_YPOS < this.origin.MAP_YPOS ) {
			resStr =  "SW";		
		} else {
			resStr =  "W";		
		}
		//if the rank between destinations is the same, make only s or n, so return only first char
		if (this.destination.getRank() == this.origin.getRank()){
			resStr = resStr.substring(0,0);
		}	
		return resStr;
		
	}
	/**
	 * return the destination for this trail
	 * @return the destination node
	 */
	public LocationNode getDestination(){
		return this.destination;
	}
	
	/**
	 * return the danger level of this trail
	 * @return The danger level of this trail
	 */
	public int getDangerLevel () {
		return this.dangerLevel;
	}
	
	/**
	 * Return the danger level of the trail in terms of how travelled it is
	 *  - established and well travelled trails are very easy, wilderness and indian lands trails are very difficult
	 */
	public String getDangerRating () {
		String str;
		if (dangerLevel < .05) {
			str = "Established";
		} else if (dangerLevel < .25) {
			str = "Well-Travelled";
		} else if (dangerLevel < .5) {
			str = "Untravelled";
		} else if (dangerLevel < .75) {
			str = "Wilderness";
		} else {
			str = "Indian Lands";
		}
		return str;
	}

	@Override
	public String debugToString() {
		String retVal = "";
		retVal += "Danger Level : " + this.dangerLevel + " Trail Edge ID : " + this.ID + " Trail Name : \"" + this.name + "\"\n";
		retVal += "\tTrail from " + this.origin.getName() + " to " + this.destination.toString() + "that is " + this.length + " miles long\n";
		return retVal;
	}

	/**
	 * Gives a verbal approximation of the trail length relative to the other trails on the map
	 * magic numbers are bad, m'kay?
	 * @return An approximation of the trail length.
	 */
	public String getRoughLength() {
		String str;
		if (length < (.05 * TrailEdge.longestTrail)) {
			str = "Very Short";
		} else if (length < (.25 * TrailEdge.longestTrail)) {
			str = "Short";
		} else if (length < (.5 * TrailEdge.longestTrail)) {
			str = "Average";
		} else if (length < (.75 * TrailEdge.longestTrail)) {
			str = "Long";
		} else if (length < (.95 * TrailEdge.longestTrail)) {
			str = "Very Long";
		} else {
			str = "Endless";
		}
		return str;
	}
	
}// class TrailEdge
