package model;

import java.util.ArrayList;

import core.Logger;

/**
 * Inventory with an ArrayList that holds all of the items in the inventory.
 */
public class Inventory {

	private ArrayList<Item> items;
	private final int MAX_SIZE;
	private final double MAX_WEIGHT;
	
	/**
	 * Inventory starts out empty.
	 */
	public Inventory(int maxSize, double maxWeight) {
		this.MAX_SIZE = maxSize;
		this.MAX_WEIGHT = maxWeight;
		this.items = new ArrayList<Item>();		
	}
	
	/**
	 * Returns the max number of items we can hold.
	 * @return Max number of items to hold
	 */
	public int getMaxSize() {
		return MAX_SIZE;
	}
	
	/**
	 * Returns the items in inventory.
	 * @return The items in the inventory.
	 */
	public ArrayList<Item> getItems() {
		return items;
	}
	
	/**
	 * Calculates and returns the weight of the inventory.
	 * @return The weight of the inventory.
	 */
	public double getWeight() {
		double weight = 0;
		for(Item item : items) {
			weight += item.getWeight();
		}
		return weight;
	}
	
	/**
	 * Adds the specified item to the inventory if weight and size allows.
	 * @param item The item to be added.
	 * @return True if the method succeeded, false if the add isn't possible.
	 */
	public boolean addItem(Item item) {
		boolean contains = false;
		int indexOf = -1;
		boolean allContainedStackable = true;
		for(Item containedItem : items) {
			if (containedItem.getClass() == item.getClass()) {
				if(!containedItem.isStackable()) {
					allContainedStackable = false;
				}
				contains = true;
				indexOf = items.indexOf(containedItem);
			}
		}
		if(getWeight() + (item.getStackWeight()) > MAX_WEIGHT) {
			//Item addition would exceeded max weight
			Logger.log("The inventory has max weight: " + MAX_WEIGHT + 
					" and the addition of " + item.getName() + " would increase the weight to " + 
					(getWeight() + item.getStackWeight()), Logger.Level.INFO);
			return false;
		} else if((!contains && items.size() == MAX_SIZE) || (contains && !allContainedStackable && items.size() == MAX_SIZE)) {
			// Item doesn't offend max weight and isn't already contained but would overflow inventory bins
			Logger.log("The inventory has max capacity: " + MAX_SIZE + 
					" and currently has " + items.size() + " items.", Logger.Level.INFO);
			return false;
		} else if(contains && (items.get(indexOf).isStackable() && item.isStackable()) ){
			// Item doesn't offend max weight and is already present in inventory and both the item added and current item are stackable
			if(!items.get(indexOf).increaseStack(item.getNumberOf())) {
				// IncreaseStack failed
				Logger.log("Could not add item", Logger.Level.INFO);
				return false;
			} else {
				// Successfully added item
				Logger.log(items.get(indexOf).getName() + " was added successfully.  Stack is now " + items.get(indexOf).getNumberOf(), Logger.Level.INFO);
				return true;
			}
		} else {
			//One or the other is not stackable, but there is room available, or the item doesn't already exist and there is room
			items.add(item);
			Logger.log(item.getName() + " was added successfully.", Logger.Level.INFO);
			return true;
		}
	}

	/**
	 * Removes the specified item from the inventory if it exists.
	 * @param item The item to be removed.
	 * @return True if the method succeeded, false if the add isn't possible.
	 */
	public boolean removeItem(Item item) {
		boolean contains = false;
		int indexOf = -1;
		for(Item containedItem : items) {
			if (containedItem.getClass() == item.getClass()) {
				indexOf = items.indexOf(containedItem);
				contains = true;
			}
		}
		if(contains) {
			if(!items.get(indexOf).decreaseStack(item.getNumberOf())) {
				Logger.log("Could not remove item", Logger.Level.INFO);
				return false;
			}
			else if(items.get(indexOf).getNumberOf() == 0) {
				items.remove(item);
			}
		}
		else {
			items.add(item);
		}
		Logger.log(item.getName() + " was added successfully.", Logger.Level.INFO);
		return true;
	}
	
	/**
	 * Checks to see if the current inventory is full.
	 * @return True if the inventory is full, false otherwise.
	 */
	public boolean isFull() {
		return (items.size() == MAX_SIZE);
	}
	
	//TODO: Inventory knows when statuses change
}
