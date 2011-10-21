package model.item;

import model.Item;

/**
 * It's round, it's wood.  It goes on a vehicle.
 */
public class Wheel extends Item {
	/**
	 * Makes a wheel
	 */
	public Wheel() {
		super(Item.ITEM_TYPE.WHEEL);
	}
}
