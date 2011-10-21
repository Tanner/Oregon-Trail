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
	
	/**
	 * Makes a gun
	 */
	public Gun() {
		super(ConstantStore.get("ITEMS", "GUN_NAME"), 
			  ConstantStore.get("ITEMS", "GUN_DESCRIPTION"), new Condition(100),
			  Double.parseDouble(ConstantStore.get("ITEMS", "GUN_WEIGHT")),
			  Integer.parseInt(ConstantStore.get("ITEMS", "GUN_COST")),
			  Item.ITEM_TYPE.GUN);
	}

}