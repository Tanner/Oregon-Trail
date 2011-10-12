package model.item;

import model.Condition;
import model.Item;
import core.ConstantStore;

/**
 * It's round, it's wood.  It goes on a vehicle.
 * @author NULL&void
 *
 */
public class Wheel extends Item {

	public Wheel() {
		super(ConstantStore.get("ITEMS", "WHEEL_NAME"), 
			  ConstantStore.get("ITEMS", "WHEEL_DESCRIPTION"), new Condition(100),
			  Double.parseDouble(ConstantStore.get("ITEMS", "WHEEL_WEIGHT")),
			  Integer.parseInt(ConstantStore.get("ITEMS", "WHEEL_COST")),
			  Item.ITEM_TYPE.WHEEL);
	}
}
