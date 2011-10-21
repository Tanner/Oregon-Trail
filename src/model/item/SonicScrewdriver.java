package model.item;

import model.Condition;
import model.Item;
import core.ConstantStore;
/**
 * To fix what must be fixed
 * @author NULL&void
 *
 */
public class SonicScrewdriver extends Item {
	
	/**
	 * Makes a sonic
	 */
	public SonicScrewdriver() {
			super(ConstantStore.get("ITEMS", "SONIC_NAME"), 
				  ConstantStore.get("ITEMS", "SONIC_DESCRIPTION"), new Condition(100),
				  Double.parseDouble(ConstantStore.get("ITEMS", "SONIC_WEIGHT")),
				  Integer.parseInt(ConstantStore.get("ITEMS", "SONIC_COST")),
				  Item.ITEM_TYPE.SONIC);
	}
}
