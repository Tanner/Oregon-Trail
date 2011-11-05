package model;

import java.io.Serializable;

import model.item.ItemType;

import core.ConstantStore;

/**
 * 
 * An item contains it's own condition as well as a 
 * name, description, and weight.  The only modifiable aspect
 * is weight.
 */
public class Item implements Conditioned, Comparable<Item>, Serializable{
	
	private final Condition status;
	
	private boolean isStackable = true;
	
	private final ItemType type;
	
	/**
	 * 
	 * Creates a new item with a name, description, status, and weight.
	 * @param type The type of the item
	 */
	public Item(ItemType type) {
		this.status = new Condition(100);
		this.type = type;
	}

	/**
	 * Returns the item's name.
	 * @return The item's name
	 */
	public String getName() {
		return type.getName();
	}

	/**
	 * Returns the item's description.
	 * @return The item's description
	 */
	public String getDescription() {
		return type.getDescription();
	}

	/**
	 * Returns the cost of the item.  Cost is the item multiplied
	 * by its current condition. 
	 * @return The base cost of the item
	 */
	public int getCost() {
		return (int) (type.getCost() * status.getPercentage());
	}
	
	/**
	 * Returns the current condition of the item.
	 * @return The current condition of the item.
	 */
	public Condition getStatus() {
		return status.copy();
	}
	
	@Override
	public double getConditionPercentage() {
		return status.getPercentage();
	}

	/**
	 * Increases the item's status by a specific amount. 
	 * Returns false if the increase fails.
	 * @param amount The amount by which to increase the status
	 */
	public void increaseStatus(int amount) {
		status.increase(amount);
		if (status.getCurrent() == status.getMax()) {
			this.isStackable = true;
		}
	}
	
	/**
	 * Decreases the item's status by a specific amount.  
	 * Returns false if the decrease fails.
	 * @param amount The amount by which to decrease the status
	 */
	public void decreaseStatus(int amount) {
		status.decrease(amount);
		if (status.getCurrent() < status.getMax()) {
			this.isStackable = false;
		}
	}
	
	/**
	 * Returns the weight of the item.
	 * @return The weight of the item
	 */
	public double getWeight() {
		return type.getWeight();
	}
	
	/**
	 * Whether or not the item can be stacked.
	 * @return True if it can be stacked, false otherwise.
	 */
	public boolean isStackable() {
		return isStackable;
	}
	
	@Override
	public int compareTo(Item i) {
		if (Math.abs(getConditionPercentage() - i.getConditionPercentage()) < 0.000001) {
			return 0;
		} else if (getConditionPercentage() < i.getConditionPercentage()) {
			return -1;
		} else {
			return 1;
		}
	}
	
	/**
	 * Returns the item type
	 * @return The item type
	 */
	public ItemType getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return type.getName();
	}

	@Override
	public Condition getCondition() {
		return status;
	}
}