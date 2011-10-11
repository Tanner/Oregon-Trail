package model;

import core.Logger;
import model.Condition;

/**
 * 
 * An item contains it's own condition as well as a name, description, and weight.  The only modifiable aspect
 * is weight.
 */
public abstract class Item implements Conditioned{
	
	private String name;
	private String description;
	private Condition status;
	private double weight; //This is the individual unit weight
	private int numberOf;
	private boolean isStackable = true;
	private int baseCost;
	
	/**
	 * 
	 * Creates a new item with a name, description, status, and weight.
	 * @param name The item's name
	 * @param description The item's description
	 * @param status The item's status
	 * @param weight The item's weight
	 * @param baseCost The base cost of the item
	 */
	public Item(String name, String description, Condition status, double weight, int baseCost) {
		this(name, description, status, weight, 1, baseCost);
	}
	
	/**
	 * 
	 * Creates a new item with a name, description, status, and weight.
	 * @param name The item's name
	 * @param description The item's description
	 * @param status The item's status
	 * @param weight The item's weight
	 * @param numberOf The number of items in the stack
	 * @param baseCost The base cost of the item
	 */
	public Item(String name, String description, Condition status, double weight, int numberOf, int baseCost) {
		this.name = name;
		this.description = description;
		this.status = status;
		this.weight = weight;
		this.numberOf = numberOf;
		this.baseCost = baseCost;
	}

	/**
	 * Returns the item's name.
	 * @return The item's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the item's description.
	 * @return The item's description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the base cost of the item.
	 * @return The base cost of the item
	 */
	public int getCost() {
		return baseCost;
	}
	
	/**
	 * Returns the cost of the full item stack.
	 * @return The cost of the item stack
	 */
	public int getStackCost() {
		return baseCost*numberOf;
	}
	
	/**
	 * Returns the current condition of the item.
	 * @return The current condition of the item.
	 */
	public Condition getStatus() {
		return status.clone();
	}
	
	@Override
	public double getConditionPercentage() {
		return status.getPercentage();
	}

	/**
	 * Increases the item's status by a specific amount. Returns false if the increase fails.
	 * @param amount The amount by which to increase the status
	 */
	public boolean increaseStatus(int amount) {
		boolean returned = status.increase(amount);
		if (status.getCurrent() == status.getMax()) {
			this.isStackable = true;
		}
		return returned;
	}
	
	/**
	 * Decreases the item's status by a specific amount.  Returns false if the decrease fails.
	 * @param amount The amount by which to decrease the status
	 */
	public boolean decreaseStatus(int amount) {
		boolean returned = status.decrease(amount);
		if (status.getCurrent() < status.getMax()) {
			this.isStackable = false;
		}
		return returned;
	}
	
	/**
	 * Returns the weight of the item.
	 * @return The weight of the item
	 */
	public double getWeight() {
		return weight;
	}	
	
	/**
	 * Returns the weight of the item stack.
	 * @return The weight of the item stack
	 */
	public double getStackWeight() {
		return weight*(double)numberOf;
	}	
	
	/**
	 * Returns the number of the item.
	 * @return The number of the item
	 */
	public int getNumberOf() {
		return numberOf;
	}
	
	/**
	 * Increases the stack of the item by specified amount.
	 * @param amount The amount to add to the stack.
	 * @return True if the add succeeds, false otherwise.
	 */
	public boolean increaseStack(int amount) {
		if(amount <= 0) {
			Logger.log("Amount to increase by not positive", Logger.Level.INFO);
			return false;
		}
		else {
			numberOf += amount;
			Logger.log("Increment successful", Logger.Level.INFO);
			return true;
		}
	}
	
	/**
	 * Decreases the stack of the item by specified amount.
	 * @param amount The amount to subtract from the stack.
	 * @return True if the subtract succeeds, false otherwise.
	 */
	public boolean decreaseStack(int amount) {
		if(amount <= 0) {
			Logger.log("Amount to decrease by not positive", Logger.Level.INFO);
			return false;
		}
		else if(amount > numberOf) {
			Logger.log("Amount to decrease by more than exist", Logger.Level.INFO);
			return false;
		}
		else {
			numberOf -= amount;
			Logger.log("Decrement successful", Logger.Level.INFO);
			return true;
		}
	}
	
	/**
	 * Whether or not the item can be stacked.
	 * @return True if it can be stacked, false otherwise.
	 */
	public boolean isStackable() {
		return isStackable;
	}
}
