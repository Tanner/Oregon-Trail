package model.item;

import model.Condition;
import model.Item;
import core.ConstantStore;

public class Wheel extends Item {

	public Wheel(int numberOf) {
		super(ConstantStore.get("ITEMS", "WHEEL_NAME"), 
			  ConstantStore.get("ITEMS", "WHEEL_DESRCIPTION"), new Condition(100), 5, numberOf);
	}
}
