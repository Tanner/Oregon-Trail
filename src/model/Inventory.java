package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import model.Item.ITEM_TYPE;

import core.Logger;

/**
 * Inventory with an list that holds all the arrayLists of items in the inventory.
 */
public class Inventory implements Serializable {
	private final List<PriorityQueue<Item>> slots;
	
	private final int MAX_SIZE;
	
	private final double MAX_WEIGHT;
	
	private int currentSize;

	/**
	 * Inventory starts out empty.
	 * @param maxSize the max size of the inventory
	 * @param maxWeight the max weight of the inventory
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
	public List<Item.ITEM_TYPE> getPopulatedSlots() {
		final List<Item.ITEM_TYPE> popSlots = new ArrayList<Item.ITEM_TYPE>();
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
	 * @param itemType The item type
	 * @param numberOf The number of items to test with
	 * @return True if successful
	 */
	public boolean canGetItems(Item.ITEM_TYPE itemType, int numberOf) {
		if(numberOf == 0) {
			return false;
		}
		
		final double weight = itemType.getWeight() * numberOf;
		if(getWeight() + weight > MAX_WEIGHT) {
			Logger.log("Not enough weight capacity", Logger.Level.DEBUG);
			return false;
		} else if (currentSize == MAX_SIZE && getNumberOf(itemType) == 0) {
			Logger.log("Not enough slots open", Logger.Level.DEBUG);
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Adds the item to the inventory.
	 * @param itemsToAdd The items to add
	 */
	public void addItemsToInventory(List<Item> itemsToAdd) {
		if(itemsToAdd.size() == 0) {
			return;
		}
		final Item.ITEM_TYPE itemType = itemsToAdd.get(0).getType();
		if(canGetItems(itemType, itemsToAdd.size())) {
			for(Item item : itemsToAdd) {
				slots.get(itemType.ordinal()).add(item);
				Logger.log(item.getName() + " added.", Logger.Level.DEBUG);
			}
			currentSize = 0;
			for(PriorityQueue<Item> slot : slots) {
				if (slot.size() > 0) {
					currentSize += 1;
				}
			}
			return;
		} else {
			Logger.log("Add item failed", Logger.Level.DEBUG);
			return;
		}
	}
		
	public void addItemToInventory(Item item) {
		final List<Item> itemToAdd = new ArrayList<Item>();
		itemToAdd.add(item);
		addItemsToInventory(itemToAdd);
	}

	/**
	 * Removes the item from the inventory and gives it back as an arrayList
	 * @param itemType The type of item to remove
	 * @param quantity The number of the item to remvove
	 * @return The removed items.
	 */
	public List<Item> removeItemFromInventory(Item.ITEM_TYPE itemType, int quantity) {
		List<Item> removedItems = new ArrayList<Item>();
		final int itemIndex = itemType.ordinal();
		if(slots.get(itemIndex).size() < quantity) {
			Logger.log("Not enough items to remove", Logger.Level.DEBUG);
			removedItems = null;
		} else {
			for(int i = 0; i < quantity; i++) {
				removedItems.add(slots.get(itemIndex).poll());
			}
			Logger.log("Items removed successfully", Logger.Level.DEBUG);
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
	 * Checks to see if the current inventory is empty.
	 * @return True if the inventory is empty, false otherwise.
	 */
	public boolean isEmpty() {
		return (getCurrentSize() == 0);
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
	 * Checks the item type
	 * @param itemType The item to check
	 * @return The condition of the worst item in the list
	 */
	public Condition getConditionOf(Item.ITEM_TYPE itemType) {
		return getNumberOf(itemType) != 0 ? 
				slots.get(itemType.ordinal()).peek().getStatus() :
					null;
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
	 * Add random items to this inventory.
	 */
	public void addRandomItems() {
		Random random = new Random();
		
		int numberOfItemsToAdd = random.nextInt(getMaxSize()) + 2;
		
		for (int i = 0; i < numberOfItemsToAdd; i++) {
			Item item;
			int attempts = 0;
			do {
				int randomItem = random.nextInt(ITEM_TYPE.values().length);
				item = new Item(ITEM_TYPE.values()[randomItem]);
				
				attempts++;
			} while(!canGetItems(item.getType(), 1) && attempts < ITEM_TYPE.values().length);
			
			item.decreaseStatus(random.nextInt(101));
			if (!item.getType().isAnimal() && 
				item.getType() != ITEM_TYPE.STRANGEMEAT && 
				item.getType() != ITEM_TYPE.WAGON &&
				item.getType() != ITEM_TYPE.SONIC
				)
			addItemToInventory(item);
		}
	}
	
	/**
	 * ToString method for debugging and whatnot
	 * @return String representation of the inventory
	 */
	public String toString() {
		final List<Item.ITEM_TYPE> popSlots = getPopulatedSlots();
		final StringBuffer str = new StringBuffer("Size: " + popSlots.size() + ". ");
		for(Item.ITEM_TYPE itemType : popSlots) {
			str.append(" # of " + itemType.getName() + "s: " + getNumberOf(itemType));
		}
				
		return str.toString();
	}

	public double getMaxWeight() {
		return MAX_WEIGHT;
	}
}
