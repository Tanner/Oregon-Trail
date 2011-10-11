package model.item;

import core.ConstantStore;
import model.Condition;
import model.Item;

public class Meat extends Item {
	public Meat() {
		super(ConstantStore.get("ITEMS", "MEAT_NAME"), 
			  ConstantStore.get("ITEMS", "MEAT_DESCRIPTION"), new Condition(100), 25, 75);
	}
}