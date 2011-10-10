package model.item;

import model.Condition;
import model.Item;
import core.ConstantStore;

public class SonicScrewdriver extends Item {

	public SonicScrewdriver(int numberOf) {
			super(ConstantStore.get("ITEMS", "SONIC_SCREWDRIVER_NAME"), 
				  ConstantStore.get("ITEMS", "SONIC_SCREWDRIVER_DESRCIPTION"), new Condition(100), .5, numberOf);
	}
}
