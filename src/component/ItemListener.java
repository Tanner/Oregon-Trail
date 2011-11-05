package component;

import model.item.ItemType;

public interface ItemListener {
	public void itemButtonPressed(OwnerInventoryButtons ownerInventoryButtons, ItemType item);
	
	public void itemButtonPressed(OwnerInventoryButtons ownerInventoryButtons);
}
