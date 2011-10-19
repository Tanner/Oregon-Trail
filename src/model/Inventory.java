package model;

import java.util.ArrayList;
import java.util.PriorityQueue;

import core.Logger;

/**
 * Inventory with an ArrayList that holds all the arrayLists of items in the inventory.
 */
public class Inventory {
	private ArrayList<PriorityQueue<Item>> slots;
	private final int MAX_SIZE;
	private final double MAX_WEIGHT;
	private int currentSize;

	/**
	 * Inventory starts out empty.
	 */
	public Inventory(int maxSize, double maxWeight) {
		this.MAX_SIZE = maxSize;
		this.MAX_WEIGHT = maxWeight;
		this.slots = new ArrayList<PriorityQueue<Item>>(Item.ITEM_TYPE.values().length);
		for(int i = 0; i < Item.ITEM_TYPE.values().length; i++) {
			slots.add(new PriorityQueue<Item>());
		}
		this.currentSize = 0;
	}
	
	/**
	 * Returns the max number of items we can hold.
	 * @return Max number of items to hold
	 */
	public int getMaxSize() {
		return MAX_SIZE;
	}
	
	/**
	 * Returns the slots in inventory with items.
	 * @return The slots in the inventory.
	 */
	public ArrayList<Item.ITEM_TYPE> getPopulatedSlots() {
		ArrayList<Item.ITEM_TYPE> popSlots = new ArrayList<Item.ITEM_TYPE>();
		for(Item.ITEM_TYPE itemType : Item.ITEM_TYPE.values()) {
			if (getNumberOf(itemType) != 0) {
				popSlots.add(itemType);
			}
		}
		return popSlots;
	}
	
	/**
	 * Returns the current size of the inventory (number of item queues with contents)
	 * @return The size of the inventory
	 */
	public int getCurrentSize() {
		return currentSize;
	}
	
	/**
	 * Calculates and returns the weight of the inventory.
	 * @return The weight of the inventory.
	 */
	public double getWeight() {
		double weight = 0;
		for(PriorityQueue<Item> slot : slots) {
			for(Item item : slot) {
				weight += item.getWeight();
			}
		}
		return weight;
	}

	/**
	 * Determines if the inventory can handle the addition
	 * @param itemsToAdd The list of items to add.
	 * @return True if successful
	 */
	public boolean canAddItems(Item.ITEM_TYPE itemType, int numberOf) {
		if(numberOf == 0) {
			return false;
		}
		
		double weight = itemType.getWeight() * numberOf;
		if(getWeight() + weight > MAX_WEIGHT) {
			Logger.log("Not enough weight capacity", Logger.Level.INFO);
			return false;
		} else if (currentSize == MAX_SIZE && getNumberOf(itemType) == 0) {
			Logger.log("Not enough slots open", Logger.Level.INFO);
			return false;
		} else {
			return true;
		}
	}
	/**
	 * Adds the item to the inventory.
	 * @param item The item to add
	 * @return True if successful, false otherwise
	 */
	public boolean addItem(ArrayList<Item> itemsToAdd) {
		if(itemsToAdd.size() == 0) {
			return false;
		}
		Item.ITEM_TYPE itemType = itemsToAdd.get(0).getType();		
		if(canAddItems(itemType, itemsToAdd.size())) {
			for(Item item : itemsToAdd) {
				slots.get(itemType.ordinal()).add(item);
				Logger.log(item.getName() + " added.", Logger.Level.INFO);
			}
			currentSize = 0;
			for(PriorityQueue<Item> slot : slots) {
				if (slot.size() > 0) {
					currentSize += 1;
				}
			}
			return true;
		} else {
			Logger.log("Add item failed", Logger.Level.INFO);
			return false;
		}
	}

	/**
	 * Removes the item from the inventory.
	 * @param item The item to remove
	 * @return True if successful, false otherwise
	 * */
	public ArrayList<Item> removeItem(int itemType, int quantity) {
		ArrayList<Item> removedItems = new ArrayList<Item>();
		if(slots.get(itemType).size() < quantity) {
			Logger.log("Not enough items to remove", Logger.Level.INFO);
			removedItems = null;
		} else {
			for(int i = 0; i < quantity; i++) {
				removedItems.add(slots.get(itemType).poll());
			}
			Logger.log("Items removed successfully", Logger.Level.INFO);
		}
		
		currentSize = 0;
		for(PriorityQueue<Item> slot : slots) {
			if (slot.size() > 0) {
				currentSize += 1;
			}
		}
		
		return removedItems;
	}
	
	/**
	 * Removes the item from the inventory and gives it back as an arrayList
	 * @param itemType The type of item to remove
	 * @param quantity The number of the item to remvove
	 * @return The removed items.
	 */
	public ArrayList<Item> removeItem(Item.ITEM_TYPE itemType, int quantity) {
		ArrayList<Item> removedItems = new ArrayList<Item>();
		int itemIndex = itemType.ordinal();
		if(slots.get(itemIndex).size() < quantity) {
			Logger.log("Not enough items to remove", Logger.Level.INFO);
			removedItems = null;
		} else {
			for(int i = 0; i < quantity; i++) {
				removedItems.add(slots.get(itemIndex).poll());
			}
			Logger.log("Items removed successfully", Logger.Level.INFO);
		}
		
		currentSize = 0;
		for(PriorityQueue<Item> slot : slots) {
			if (slot.size() > 0) {
				currentSize += 1;
			}
		}
		
		return removedItems;
	}
	/**
	 * Checks to see if the current inventory is full.
	 * @return True if the inventory is full, false otherwise.
	 */
	public boolean isFull() {
		return (slots.size() == MAX_SIZE);
	}
	
	/**
	 * Returns the number of a specific type of item in the inventory
	 * @param itemType The item type queried
	 * @return The number of that item type.
	 */
	public int getNumberOf(Item.ITEM_TYPE itemType) {
		return slots.get(itemType.ordinal()).size();
	}
	
	/**
	 * Returns the number of any item type in this inventory.
	 * @return The number of any item types that is in this inventory
	 */
	public int getNumberOfItems() {
		int size = 0;
		
		for(PriorityQueue<Item> slot : slots) {
			size += slot.size();
		}
		
		return size;
	}
	
	/**
	 * 
	 * @param itemType
	 * @return
	 */
	public Condition getConditionOf(Item.ITEM_TYPE itemType) {
		return getNumberOf(itemType) != 0 ? slots.get(itemType.ordinal()).peek().getStatus() : null;
	}
	
	/**
	 * Clear all the items in each slot.
	 */
	public void clear() {
		for(PriorityQueue<Item> slot : slots) {
			slot.clear();
		}
		
		currentSize = 0;
	}
	
	/**
	 * ToString method for debugging and whatnot
	 * @return String representation of the inventory
	 */
	public String toString() {
		ArrayList<Item.ITEM_TYPE> popSlots = getPopulatedSlots();
		String str = "Size: " + popSlots.size() + ". ";
		for(Item.ITEM_TYPE itemType : popSlots) {
			str += " # of " + itemType.getName() + "s: " + getNumberOf(itemType);
		}
				
		return str;
	}
}
