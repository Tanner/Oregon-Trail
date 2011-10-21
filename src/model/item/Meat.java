package model.item;

import core.ConstantStore;
import model.Condition;
import model.Item;
/**
 * It's what's for dinner
 * @author NULL&void
 *
 */
public class Meat extends Item {
	
	/**
	 * Makes meat
	 */
	public Meat() {
		super(ConstantStore.get("ITEMS", "MEAT_NAME"), 
			  ConstantStore.get("ITEMS", "MEAT_DESCRIPTION"), new Condition(100),
			  Double.parseDouble(ConstantStore.get("ITEMS", "MEAT_WEIGHT")),
			  Integer.parseInt(ConstantStore.get("ITEMS", "MEAT_COST")),
			  Item.ITEM_TYPE.MEAT);
	}
}