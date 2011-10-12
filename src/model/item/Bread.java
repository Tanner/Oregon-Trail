package model.item;

import core.ConstantStore;
import model.Condition;
import model.Item;
/**
 * The bread item.  For sandwiches
 * @author NULL&void
 *
 */
public class Bread extends Item {
	public Bread() {
		super(ConstantStore.get("ITEMS", "BREAD_NAME"), 
			  ConstantStore.get("ITEMS", "BREAD_DESCRIPTION"), new Condition(100), 
			  Double.parseDouble(ConstantStore.get("ITEMS", "BREAD_WEIGHT")),
			  Integer.parseInt(ConstantStore.get("ITEMS", "BREAD_COST")),
			  Item.ITEM_TYPE.BREAD);
	}
}