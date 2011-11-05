package model.item;

import model.ITEM_TYPE;

/**
 * The wagon object, by which we shall storm the trail
 */
public class Wagon extends Vehicle {
	/**
	 * Makes a wagon
	 */
	public Wagon() {
		super(2000, ITEM_TYPE.WAGON);
	}
}
