package model.item;

import core.ConstantStore;
import model.Condition;
import model.Item;
/**
 * ammo for a gun
 * @author NULL&void
 *
 */
public class Bullet extends Item {
	public Bullet() {
		super(ConstantStore.get("ITEMS", "BULLET_NAME"), 
			  ConstantStore.get("ITEMS", "BULLET_DESCRIPTION"), new Condition(100),
			  0.25, 2, ConstantStore.ITEM_TYPES.BULLET);
	}
}
