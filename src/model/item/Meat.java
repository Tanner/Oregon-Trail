package model.item;

import model.Item;
/**
 * It's what's for dinner
 */
public class Meat extends Item {
	/**
	 * Makes meat
	 */
	public Meat() {
		super(Item.ITEM_TYPE.MEAT);
	}
}