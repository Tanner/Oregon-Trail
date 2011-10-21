package model.item;

import model.Item;
/**
 * Ammo for a gun.
 */
public class Bullet extends Item {
	/**
	 * Makes a bullet
	 */
	public Bullet() {
		super(Item.ITEM_TYPE.BULLET);
	}
}
