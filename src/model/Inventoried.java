package model;

import java.util.List;

import model.item.ItemType;

/**
 * Interface for objects with inventories
 */
public interface Inventoried {
	/**
	 * Adds the items to inventory.
	 * @param itemsToAdd The items to add to the inventory
	 */
	void addItemsToInventory(List<Item> itemsToAdd);
	
	/**
	 * Adds a single item to inventory.
	 * @param item The items to add to the inventory
	 */
	void addItemToInventory(Item item);
	
	/**
	 * Removes the item from inventory.
	 * @param itemType The type of item to remove
	 * @param quantity The number of items to remove
	 * @return True if successful
	 */
	List<Item> removeItemFromInventory(ItemType itemType, int quantity);
	
	/**
	 * Returns whether or not the number of a specific item type 
	 * can be added to the inventory.
	 * @param itemType The type of item to test with
	 * @param numberOf The number of items to test with
	 * @return True if successful.
	 */
	boolean canGetItem(ItemType itemType, int numberOf);
	
	/**
	 * The inventory of the inventoried person.
	 * @return The inventory
	 */
	Inventory getInventory();
	
	/**
	 * The max size of the inventory.
	 * @return The max size.
	 */
	int getMaxSize();
	
	/**
	 * Returns the current carried weight
	 * @return The current carried weight
	 */
	double getWeight();
	
	/**
	 * Returns the max weight of the inventory
	 * @return The max weight
	 */
	double getMaxWeight();
	
	/**
	 * Returns the name of the inventoried person
	 * @return the name
	 */
	String getName();
}
