package model.worldMap;


import model.Condition;
import model.Conditioned;

public class TrailEdge implements Conditioned {
	
	private Condition milesToGo;
	
	
	public TrailEdge(){
		
	}

	@Override
	public double getConditionPercentage() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String toSring(){
		
		return "Trail to ";
	}
	
	
}
