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
		this.name = name;
		this.ID = TrailEdge.edgeCount++;
		this.dangerLevel = dangerLevel;
		this.length = calcDistance(destination.MAP_XPOS, origin.MAP_XPOS, destination.MAP_YPOS, origin.MAP_YPOS);
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
	 * @param distance the amount to move along the trail
	 */
	public void advance(int distance) {
		this.quality.decrease(distance);
	}
	
	/**
	 * Returns the amount of distance left to go for this trail.
	 * @return Distance left to travel
	 */
	public int getDistanceToGo() {
		return quality.getCurrent();
	}
	
	/**
	 * Returns a verbal approximation of distance left to go on the trail
	 * @return An approximation of the distance left.
	 */
	public Object getRoughDistanceToGo() {
		double current = 1 - quality.getPercentage();
		return current < .25 ? "Nowhere close to " : current < .5 ? "Getting closer to " : current < .75 ? "More than halfway to " : "Just a little further to ";
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
		return dangerLevel < 25 ? "Easy" : dangerLevel < 50 ? "Moderate" : dangerLevel < 75 ? "Challenging" : "Suicidal";
	}

	@Override
	public String debugToString() {
		String retVal = "";
		retVal += "Danger Level : " + this.dangerLevel + " Trail Edge ID : " + this.ID + " Trail Name : \"" + this.name + "\"\n";
		retVal += "Trail from " + this.origin.getName() + " to " + this.destination.toString() + "that is " + this.length + " miles long\n";
		return retVal;
	}

	/**
	 * Gives a verbal approximation of the trail length
	 * @return An approximation of the trail length.
	 */
	public String getRoughDistance() {
		return length < 50 ? "Short" : length < 75 ? "Average" : length < 100 ? "Long" : "Endless";
	}
	
}// class TrailEdge
