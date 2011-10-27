package model.worldMap;


import core.Logger;
import model.Condition;
import model.Conditioned;

public class TrailEdge implements Conditioned {
	
	/**how far along the trail the party is*/
	private Condition milesToGo;
	/**end point of this edge - destination node*/
	private LocationNode destination;
	/**end point of this edge - where we started*/
	private LocationNode origin;
	/**name of this edge - currently holds name of destination - "Trail to <location name>"*/
	private String name;
	/**describes how hard the transit on this trail will be*/
	private int dangerLevel;
	/**length of the trail.  calculated by the distance between the two location nodes.*/
	private float length;
	private static int edgeCount;

	//unique id corresponding to this edge.
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
		this.milesToGo = new Condition((int) this.length);
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
	public double getConditionPercentage() {
		return 0;
	}

	@Override
	public String toString(){
		return "Danger Level : " + this.dangerLevel +  " trail from " + this.origin.getLocationName() + " to " + this.destination.toString() + "that is " + this.length + " miles long";
	}
	
	
}// class TrailEdge
