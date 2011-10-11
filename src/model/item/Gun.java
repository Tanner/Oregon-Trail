package model.item;

import core.ConstantStore;
import model.Condition;
import model.Item;

public class Gun extends Item {
	public Gun(int numberOf) {
		super(ConstantStore.get("ITEMS", "GUN_NAME"), 
			  ConstantStore.get("ITEMS", "GUN_DESCRIPTION"), new Condition(100), 5.0, numberOf, 50);
	}
	public Gun() {
		this(0);
	}
}