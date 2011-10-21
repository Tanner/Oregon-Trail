package model.item;

import model.Item;
/**
 * Consumer of bullets
 */
public class Gun extends Item {
	/**
	 * Makes a gun
	 */
	public Gun() {
		super(Item.ITEM_TYPE.GUN);
	}

}