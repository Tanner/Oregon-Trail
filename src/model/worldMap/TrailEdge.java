package model.worldMap;


import model.Condition;
import model.Conditioned;

public class TrailEdge implements Conditioned {
	
	//how far along the trail the party is
	private Condition milesToGo;
	private LocationNode destination;
	private LocationNode origin;
	private String name;
	//describes how hard the transit on this trail will be
	private int dangerLevel;	
	//length of the trail.  calculated by the distance between the two location nodes.
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
	}

	@Override
	public double getConditionPercentage() {
		return 0;
	}


	public String toString(){
		
		return "Trail to "+ this.destination.toString();
	}
	
	
}// class TrailEdge
