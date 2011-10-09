package model;

import core.ConstantStore;

public class Wheel extends Item {

	public Wheel(int numberOf) {
		super(ConstantStore.get("ITEMS", "WHEEL_NAME"), 
			  ConstantStore.get("ITEMS", "WHEEL_DESRCIPTION"), new Condition(100), 5, 1);
	}
}
