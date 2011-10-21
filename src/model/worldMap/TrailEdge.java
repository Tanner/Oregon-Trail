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
	
	
	public TrailEdge(String name, LocationNode destination, LocationNode origin){
		this.destination = destination;
		this.origin = origin;
		this.name = name;
	}

	@Override
	public double getConditionPercentage() {
		return 0;
	}


	public String toSring(){
		
		return "Trail to ";
	}
	
	
}
