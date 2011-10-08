package model;

public abstract class Vehicle {

	private Condition status;
	private Inventory cargo;
	private final int MAX_INVENTORY_SIZE = 10;
	private final double MAX_INVENTORY_WEIGHT;
	
	public Vehicle(Condition status, double maxWeight) {
		this.MAX_INVENTORY_WEIGHT = maxWeight;
		this.status = status;
		this.cargo = new Inventory(MAX_INVENTORY_SIZE, MAX_INVENTORY_WEIGHT);	
	}
	
	/**
	 * Returns the status of the vehicle
	 * @return The current status of the vehicle.
	 */
	public int getStatus() {
		return status.getCurrent();
	}
	
	/**
	 * Increases the vehicle's status by a specific amount. Returns false if the increase fails.
	 * @param amount The amount by which to increase the status
	 */
	public boolean increaseStatus(int amount) {
		return status.increase(amount);
	}
	
	/**
	 * Decreases the vehicle's status by a specific amount.  Returns false if the decrease fails.
	 * @param amount The amount by which to decrease the status
	 */
	public boolean decreaseStatus(int amount) {
		return status.decrease(amount);
	}
	
	/**
	 * Adds the specified item to the inventory if weight and size allows.
	 * @param item The item to be added.
	 * @return True if the method succeeded, false if the add isn't possible.
	 */
	public boolean addToInventory(Item item) {
		return cargo.addItem(item);
	}
	
	/**
	 * Removes the specified item from the inventory if it exists.
	 * @param item The item to be removed.
	 * @return True if the method succeeded, false if the add isn't possible.
	 */
	public boolean removeFromInventory(Item item) {
		return cargo.removeItem(item);
	}
}
