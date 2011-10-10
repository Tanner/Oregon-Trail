package model.item;

import core.ConstantStore;
import model.Condition;
import model.Item;

public class Bullet extends Item {
	public Bullet(int numberOf) {
		super(ConstantStore.get("ITEMS", "BULLET_NAME"), 
			  ConstantStore.get("ITEMS", "BULLET_DESRCIPTION"), new Condition(100), 0.25, numberOf);
	}
}
