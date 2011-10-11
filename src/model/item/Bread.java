package model.item;

import core.ConstantStore;
import model.Condition;
import model.Item;

public class Bread extends Item {
	public Bread() {
		super(ConstantStore.get("ITEMS", "BREAD_NAME"), 
			  ConstantStore.get("ITEMS", "BREAD_DESCRIPTION"), new Condition(100), 
			  1.0, 4, ConstantStore.ITEM_TYPES.BREAD);
	}
}