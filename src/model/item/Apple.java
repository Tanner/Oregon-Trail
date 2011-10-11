package model.item;

import core.ConstantStore;
import model.Condition;
import model.Item;

public class Apple extends Item {
	public Apple(int numberOf) {
		super(ConstantStore.get("ITEMS", "APPLE_NAME"), 
			  ConstantStore.get("ITEMS", "APPLE_DESCRIPTION"), new Condition(100), 1.5, numberOf, 2);
	}
	public Apple() {
		this(0);
	}
}
