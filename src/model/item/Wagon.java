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
	public Wagon() {
		super("Wagon", "This is a wagon", new Condition(100), 2000, 200,
				2000, Item.ITEM_TYPES.WAGON);
	}
}
