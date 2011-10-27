package model.worldMap;

import model.Condition;
import model.Conditioned;

public abstract class MapObject implements Conditioned {
	
	/**this object is visible to player*/
	protected boolean visible;
	/**the "quality" of the location or trail this object represents - either quality of town or distance left to travel of trail*/
	protected Condition quality;

	public MapObject(){
		
	}
	/**
	 * whether this trail is visible to the player or not
	 * @return this trail's visiblity
	 */
	public boolean getVisible(){
		return this.visible;
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
	
	/**
	 * for debuging purposes, this will return debug-only related information about the object
	 * @return the string of debug-related information
	 */
	public abstract String debugToString();

}
