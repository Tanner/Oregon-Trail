package model.item;

import core.ConstantStore;
import model.Condition;
import model.Item;
/**
 * It's an apple, jim
 * @author John
 *
 */
public class Apple extends Item {
	public Apple() {
		super(ConstantStore.get("ITEMS", "APPLE_NAME"), 
			  ConstantStore.get("ITEMS", "APPLE_DESCRIPTION"), new Condition(100), 1.5, 2);
	}
}
