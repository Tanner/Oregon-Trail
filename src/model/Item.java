package model;

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
	private double weight;
	
	/**
	 * 
	 * Creates a new item with a name, description, status, and weight.
	 * @param name The item's name
	 * @param description The item's description
	 * @param status The item's status
	 * @param weight The item's weight
	 */
	private Item(String name, String description, Condition status, double weight) {
		this.name = name;
		this.description = description;
		this.status = status;
		this.weight = weight;
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
}
