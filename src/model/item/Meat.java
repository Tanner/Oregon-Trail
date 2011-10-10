package model.item;

import core.ConstantStore;
import model.Condition;
import model.Item;

public class Meat extends Item {
	public Meat(int numberOf) {
		super(ConstantStore.get("ITEMS", "MEAT_NAME"), 
			  ConstantStore.get("ITEMS", "MEAT_DESRCIPTION"), new Condition(100), 25, numberOf);
	}
}