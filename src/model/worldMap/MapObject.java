package model.worldMap;

import model.Condition;
import model.Conditioned;

public abstract class MapObject implements Conditioned {
	
	/**this object is visible to player*/
	protected boolean visible;
	/**the "quality" of the location or trail this object represents - either quality of town or distance left to travel of trail*/
	protected Condition quality;
	/**the name of this object */
	protected String name;


	public MapObject(){
		
	}
	/**
	 * whether this trail is visible to the player or not
	 * @return this trail's visiblity
	 */
	public boolean getVisible(){
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
		return quality.getPercentage();
	}
	
	public abstract String debugToString();
		

}
