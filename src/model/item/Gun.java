package model.item;

import core.ConstantStore;
import model.Condition;
import model.Item;
/**
 * consumer of bullets
 * @author NULL&void
 *
 */
public class Gun extends Item {
	public Gun() {
		super(ConstantStore.get("ITEMS", "GUN_NAME"), 
			  ConstantStore.get("ITEMS", "GUN_DESCRIPTION"), new Condition(100),
			  5.0, 50, ConstantStore.ITEM_TYPES.GUN);
	}

}