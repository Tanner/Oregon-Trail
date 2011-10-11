package model;

import java.util.ArrayList;

public interface Inventoried {
	//TODO: get items as list
	
	public boolean addItemToInventory(ArrayList<Item> itemsToAdd);
	public boolean removeItemFromInventory(int itemIndex, int quantity);
	public boolean canGetItem(Item item, int numberOf);
	public String getName();
}
