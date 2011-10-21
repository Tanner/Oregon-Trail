package model.item;

import core.ConstantStore;
import model.Condition;
import model.Item;

/**
 * The wagon object, by which we shall storm the trail
 * @author NULL&void
 *
 */

public class Wagon extends Vehicle {
	
	/**
	 * Makes a wagon
	 */
	public Wagon() {
		super(ConstantStore.get("ITEMS", "WAGON_NAME"),
				ConstantStore.get("ITEMS", "WAGON_DESCRIPTION"), new Condition(100), 2000,
				Double.parseDouble(ConstantStore.get("ITEMS", "WAGON_WEIGHT")),
				Integer.parseInt(ConstantStore.get("ITEMS", "WAGON_COST")),
				Item.ITEM_TYPE.WAGON);
	}
}
