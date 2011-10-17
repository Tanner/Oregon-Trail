package component;

import model.Item.ITEM_TYPE;

public interface ItemListener {
	public void itemButtonPressed(OwnerInventoryButtons ownerInventoryButtons, ITEM_TYPE item);
	
	public void itemButtonPressed(OwnerInventoryButtons ownerInventoryButtons);
}
