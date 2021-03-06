package model.worldMap;

import java.io.Serializable;

import core.ConstantStore;

import model.Condition;
import model.Conditioned;

@SuppressWarnings("serial")
public abstract class MapObject implements Conditioned, Serializable {
	
	/**this object is visible to player*/
	protected boolean visible;
	/**the "quality" of the location or trail this object represents - either quality of town or distance left to travel of trail*/
	protected Condition quality;
	/**the name of this object */
	protected String name;
	/** class-wide counter of nodes */
	protected static int count;
	/**the territory that this location resides in*/
	private ConstantStore.StateIdx territory;
	/**the nature of the environment in/around this MapObject - format needed*/
	/**protected static -something- environment;
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
	
	public ConstantStore.StateIdx getTerritory(){
		return this.territory;
	}
	
	public void setTerritory(ConstantStore.StateIdx territory){
		this.territory = territory;
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

	public void setQuality(Condition quality){		
		this.quality = quality;
	}
	
		
	@Override
	public Condition getCondition() {
		return this.quality;
	}
}
