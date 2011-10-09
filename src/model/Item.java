package model;

import core.Logger;
import model.Condition;

/**
 * 
 * An item contains it's own condition as well as a name, description, and weight.  The only modifiable aspect
 * is weight.
 */
public abstract class Item {

	private String name;
	private String description;
	private Condition status;
	private double weight; //This is the individual unit weight
	private int numberOf;
	
	/**
	 * 
	 * Creates a new item with a name, description, status, and weight.
	 * @param name The item's name
	 * @param description The item's description
	 * @param status The item's status
	 * @param weight The item's weight
	 */
	public Item(String name, String description, Condition status, double weight) {
		this(name, description, status, weight, 1);
	}
	
	/**
	 * 
	 * Creates a new item with a name, description, status, and weight.
	 * @param name The item's name
	 * @param description The item's description
	 * @param status The item's status
	 * @param weight The item's weight
	 * @param numberOf The number of items in the stack
	 */
	public Item(String name, String description, Condition status, double weight, int numberOf) {
		this.name = name;
		this.description = description;
		this.status = status;
		this.weight = weight;
		this.numberOf = numberOf;
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
	 * Returns the current condition of the item.
	 * @return The current condition of the item.
	 */
	public int getStatus() {
		return status.getCurrent();
	}

	/**
	 * Increases the item's status by a specific amount. Returns false if the increase fails.
	 * @param amount The amount by which to increase the status
	 */
	public boolean increaseStatus(int amount) {
		return status.increase(amount);
	}
	
	/**
	 * Decreases the item's status by a specific amount.  Returns false if the decrease fails.
	 * @param amount The amount by which to decrease the status
	 */
	public boolean decreaseStatus(int amount) {
		return status.decrease(amount);
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
	
	public boolean increaseStack(int amount) {
		if(amount <= 0) {
			Logger.log("Amount to increase by not positive", Logger.Level.INFO);
			return false;
		}
		else {
			Logger.log("Increment successful", Logger.Level.INFO);
			return true;
		}
	}
	
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
			Logger.log("Decrement successful", Logger.Level.INFO);
			return true;
		}
	}
}
