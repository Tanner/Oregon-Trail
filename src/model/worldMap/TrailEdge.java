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
		return current < .25 ? "Nowhere close to " : current < .5 ? "Getting closer to " : current < .75 ? "More than halfway to " : "Just a little further to ";
	}

	/**
	 * Returns a verbal approximation of the direction of the trail, based on the y coords of the destination and the origin
	 * @return A string representation of the direction of the trail.
	 */
	public String getRoughDirection(){
		if (this.destination.MAP_YPOS > this.origin.MAP_YPOS ) {//northwestern trail
			return "NW";
		} else if (this.destination.MAP_YPOS < this.origin.MAP_YPOS ) {
			return "SW";		
		} else {
			return "W";		
		}
		
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
	 * Return the danger level of the trail as either easy, moderate, challenging, or suicide.
	 */
	public String getDangerRating () {
		return dangerLevel < 25 ? "Easy" : dangerLevel < 50 ? "Moderately difficult" : dangerLevel < 75 ? "Challenging" : "Suicidal";
	}

	@Override
	public String debugToString() {
		String retVal = "";
		retVal += "Danger Level : " + this.dangerLevel + " Trail Edge ID : " + this.ID + " Trail Name : \"" + this.name + "\"\n";
		retVal += "Trail from " + this.origin.getName() + " to " + this.destination.toString() + "that is " + this.length + " miles long\n";
		return retVal;
	}

	/**
	 * Gives a verbal approximation of the trail length relative to the other trails on the map
	 * magic numbers are bad, m'kay?
	 * @return An approximation of the trail length.
	 */
	public String getRoughLength() {
		return (length < (.25 * TrailEdge.longestTrail) ? "Short" : (length < (.75 * TrailEdge.longestTrail) ? "Average" : ((length < TrailEdge.longestTrail) ? "Long" : "Endless")));
	}
	
}// class TrailEdge
