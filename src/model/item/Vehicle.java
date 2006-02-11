package model.item;

import java.util.ArrayList;

import model.Condition;
import model.Inventoried;
import model.Inventory;
import model.Item;

public abstract class Vehicle extends Item implements Inventoried{
/**
 * Abstract class to design a vehicle
 */
	private Condition status;
	private Inventory cargo;
	private final int MAX_INVENTORY_SIZE = 10;
	private final double MAX_INVENTORY_WEIGHT;

	/**
	 * Makes a vehicle to be used to get to oregon
	 * @param name what this vehicle is to be called
	 * @param description what kind of vehicle this is
	 * @param status what is the condition of this vehicle
	 * @param maxWeight how much can this vehicle hold
	 * @param weight how much this vehicle is currently carrying
	 * @param cost how much this vehicle costs purchase  
	 */
	public Vehicle(String name, String description, Condition status, 
			double maxWeight, double weight,
			int cost, Item.ITEM_TYPE type) {
	super(name, description, status, weight, cost, type);
		this.MAX_INVENTORY_WEIGHT = maxWeight;
		this.status = status;
		this.cargo = new Inventory(MAX_INVENTORY_SIZE, MAX_INVENTORY_WEIGHT);	
	}
	
	/**
	 * Returns the status of the vehicle
	 * @return The current status of the vehicle.
	 */
	public Condition getStatus() {
		return status.clone();
	}
	
	/**
	 * Increases the vehicle's status by a specific amount. Returns false if the increase fails.
	 * @param amount The amount by which to increase the status
	 * @return whether the increase worked or not
	 */
	public boolean increaseStatus(int amount) {
		return status.increase(amount);
	}
	
	/**
	 * Decreases the vehicle's status by a specific amount.  Returns false if the decrease fails.
	 * @param amount The amount by which to decrease the status
	 * @return whether the decrease worked or not
	 */
	public boolean decreaseStatus(int amount) {
		return status.decrease(amount);
	}
	
	/**
	 * Returns the inventory of the vehicle
	 * @return The inventory of the vehicle
	 */
	public Inventory getInventory() {
		return cargo;
	}
	
	/**
	 * Adds the specified item to the inventory if weight and size allows.
	 * @param item The item to be added.
	 * @return True if the method succeeded, false if the add isn't possible.
	 */
	public boolean addItemToInventory(ArrayList<Item> items) {
		return cargo.addItem(items);
	}
	
	/**
	 * Removes the specified item from the inventory if it exists.
	 * @param item The item to be removed.
	 * @return True if the method succeeded, false if the add isn't possible.
	 */
	public boolean removeItemFromInventory(Item.ITEM_TYPE itemIndex, int quantity) {
		return (cargo.removeItem(itemIndex, quantity) != null);
	}
	
	/**
	 * Returns whether or not the item addition is possible.
	 * @param The items to test the possibility of adding.
	 * @return True if possible.
	 */
	public boolean canGetItem(Item.ITEM_TYPE itemType, int numberOf) {
		return cargo.canAddItems(itemType, numberOf);
	}
	
	public int getMaxSize() {
		return MAX_INVENTORY_SIZE;
	}
}
