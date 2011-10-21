package model.item;

import core.ConstantStore;
import model.Condition;
import model.Item;
/**
 * It's an apple, jim
 * @author NULL&void
 *
 */
public class Apple extends Item {
	
	/**
	 * Makes an apple
	 */
	public Apple() {
		super(ConstantStore.get("ITEMS", "APPLE_NAME"), 
			  ConstantStore.get("ITEMS", "APPLE_DESCRIPTION"), new Condition(100),
			  Double.parseDouble(ConstantStore.get("ITEMS", "APPLE_WEIGHT")),
			  Integer.parseInt(ConstantStore.get("ITEMS", "APPLE_COST")),
			  Item.ITEM_TYPE.APPLE);
	}
}