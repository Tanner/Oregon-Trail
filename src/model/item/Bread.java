package model.item;

import model.Item;

/**
 * The bread item.  For sandwiches

 */
public class Bread extends Item {
	/**
	 * Makes bread
	 */
	public Bread() {
		super(Item.ITEM_TYPE.BREAD);
	}
}