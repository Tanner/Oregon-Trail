package model;

import model.Condition;

/**
 * 
 * An item contains it's own condition as well as a name, description, and weight.  The only modifiable aspect
 * is weight.
 */
public class Item {

	private String name;
	private String description;
	private Condition status;
	private double weight;
	
	public Item(String name, String description, Condition status, double weight) {
		this.name = name;
		this.description = description;
		this.status = status;
		this.weight = weight;
	}

	/**
	 * 
	 * @return The item's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return The item's description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @return The current condition of the item.
	 */
	public int getStatus() {
		return status.getCurrent();
	}

	/**
	 * 
	 * @param amount The amount by which to increase the status
	 */
	public void increaseStatus(int amount) {
		status.increase(amount);
	}
	
	/**
	 * 
	 * @param amount The amount by which to decrease the status
	 */
	public void decreaseStatus(int amount) {
		status.decrease(amount);
	}

	/**
	 * 
	 * @return The weight of the item
	 */
	public double getWeight() {
		return weight;
	}	
}
