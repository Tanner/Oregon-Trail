package model;

import java.util.ArrayList;

import core.Logger;

/**
 * 
 * Inventory with an ArrayList that holds all of the items in the inventory.
 */
//TODO: Fix inventory so that it'll actually work with subclassed items.
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
		if(getWeight() + (item.getStackWeight()) > MAX_WEIGHT) {
			Logger.log("The inventory has max weight: " + MAX_WEIGHT + 
					" and the addition of " + item.getName() + " would increase the weight to " + 
					(getWeight() + item.getWeight()), Logger.Level.INFO);
			return false;
		}
		else if(items.size() == MAX_SIZE) {
			Logger.log("The inventory has max capacity: " + MAX_SIZE + 
					" and currently has " + items.size() + " items.", Logger.Level.INFO);
			return false;
		}
		else {
			if(items.contains(item)) {
				if(!items.get(items.indexOf(item)).increaseStack(item.getNumberOf())) {
					Logger.log("Could not add item", Logger.Level.INFO);
					return false;
				}
			}
			else {
				items.add(item);
			}
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
		if(items.contains(item)) {
			if(!items.get(items.indexOf(item)).decreaseStack(item.getNumberOf())) {
				Logger.log("Could not remove item", Logger.Level.INFO);
				return false;
			}
			else if(items.get(items.indexOf(item)).getNumberOf() == 0) {
				items.remove(item);
			}
		}
		else {
			items.add(item);
		}
		Logger.log(item.getName() + " was added successfully.", Logger.Level.INFO);
		return true;
	}
}