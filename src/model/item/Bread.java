package model.item;

import core.ConstantStore;
import model.Condition;
import model.Item;

public class Bread extends Item {
	public Bread(int numberOf) {
		super(ConstantStore.get("ITEMS", "BREAD_NAME"), 
			  ConstantStore.get("ITEMS", "BREAD_DESCRIPTION"), new Condition(100), 1.0, numberOf, 4);
	}
	public Bread() {
		this(0);
	}
}