package model;

import core.ConstantStore;
import model.Condition;

/**
 * 
 * An item contains it's own condition as well as a name, description, and weight.  The only modifiable aspect
 * is weight.
 */
public abstract class Item implements Conditioned, Comparable<Item>{
	
	private String name;
	private String description;
	private Condition status;
	private double weight; //This is the individual unit weight
	private boolean isStackable = true;
	private int baseCost;
	private ITEM_TYPES type;
	/**
	 * 
	 * Creates a new item with a name, description, status, and weight.
	 * @param name The item's name
	 * @param description The item's description
	 * @param status The item's status
	 * @param weight The item's weight
	 * @param numberOf The number of items in the stack
	 * @param baseCost The base cost of the item
	 * @param type The type of the item
	 */
	public Item(String name, String description, Condition status, double weight, int baseCost, ITEM_TYPES type) {
		this.name = name;
		this.description = description;
		this.status = status;
		this.weight = weight;
		this.baseCost = baseCost;
		this.type = type;
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
	 * Returns the cost of the item.  Cost is the item multiplied
	 * by its current condition. 
	 * @return The base cost of the item
	 */
	public int getCost() {
		return (int) (baseCost * status.getPercentage());
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
	 * Whether or not the item can be stacked.
	 * @return True if it can be stacked, false otherwise.
	 */
	public boolean isStackable() {
		return isStackable;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Item i) {
		if ( Math.abs(getConditionPercentage() - i.getConditionPercentage()) < 0.000001 )
			return 0;
		if ( getConditionPercentage() < i.getConditionPercentage() )
			return -1;
		else 
			return 1;
	}
	
	public int getTypeIndex() {
		return type.ordinal();
	}
	
	public enum ITEM_TYPES {
		APPLE,
		BREAD,
		BULLET,
		GUN,
		MEAT,
		SONIC,
		WAGON,
		WHEEL,
	}
}