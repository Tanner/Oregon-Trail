package model.item;

import model.Condition;
import model.Item;
import core.ConstantStore;

public class SonicScrewdriver extends Item {
	public SonicScrewdriver() {
			super(ConstantStore.get("ITEMS", "SONIC_SCREWDRIVER_NAME"), 
				  ConstantStore.get("ITEMS", "SONIC_SCREWDRIVER_DESCRIPTION"), new Condition(100), .5, 100);
	}
}
