package model.worldMap;

import java.io.Serializable;

import model.Condition;
import model.Conditioned;

public abstract class MapObject implements Conditioned, Serializable {
	
	/**this object is visible to player*/
	protected boolean visible;
	/**the "quality" of the location or trail this object represents - either quality of town or distance left to travel of trail*/
	protected Condition quality;
	/**the name of this object */
	protected String name;
	/** class-wide counter of nodes */
	protected static int count;
	/**the nature of the environment in/around this MapObject - format needed*/



	public MapObject(){
		
	}
	

	/**
	 * whether this map object is visible to the player or not
	 * @return this map object's visiblity
	 */
	public boolean isVisible(){
		return this.visible;
	}
	
	public void setName (String name){
		this.name = name;
	}
	public String getName(){
		return this.name;
	}
	
	/**
	 * this sets whether this trail is visible or not
	 * @param visible the visibility of this trail on the player's map
	 */
	public void setVisible(boolean visible){
		this.visible = visible;
	}

	public double getConditionPercentage() {
		return this.quality.getPercentage();
	}
	
	public abstract String debugToString();
		
	public static void resetCount(){
		MapObject.count = 0;
	}

	
		
	@Override
	public Condition getCondition() {
		return this.quality;
	}
}
