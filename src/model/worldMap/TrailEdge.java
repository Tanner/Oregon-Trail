package model.worldMap;


import model.Condition;
import model.Conditioned;

public class TrailEdge implements Conditioned {
	
	private Condition milesToGo;
	private LocationNode destination;
	private LocationNode origin;
	private String name;
	
	
	
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
