package model.item;

import java.util.ArrayList;
import java.util.List;

import model.Condition;
import model.Inventoried;
import model.Inventory;
import model.Item;

/**
 * A vehicle is anything that is classified as a vehicle
 * @author Null && Void
 *
 */
public abstract class Vehicle extends Item implements Inventoried{
/**
 * Abstract class to design a vehicle
 */
	private final Condition status;

	private final Inventory cargo;
	
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
	 * @param type The type of vehicle
	 */
	public Vehicle(String name, String description, Condition status, 
			double maxWeight, double weight,
			int cost, Item.ITEM_TYPE type) {
	super(name, description, status, weight, cost, type);
		this.MAX_INVENTORY_WEIGHT = maxWeight;
		this.status = status;
		this.cargo = new Inventory(MAX_INVENTORY_SIZE, MAX_INVENTORY_WEIGHT);
	}
	
	@Override
	public Condition getStatus() {
		return status.copy();
	}
	
	@Override
	public void increaseStatus(int amount) {
		status.increase(amount);
	}
	
	@Override
	public void decreaseStatus(int amount) {
		status.decrease(amount);
	}
	
	@Override
	public Inventory getInventory() {
		return cargo;
	}
	
	@Override
	public void addItemsToInventory(List<Item> items) {
		cargo.addItem(items);
	}
	
	@Override
	public void addItemToInventory(Item item) {
		final List<Item> itemToAdd = new ArrayList<Item>();
		itemToAdd.add(item);
		cargo.addItem(itemToAdd);
	}
	
	@Override
	public List<Item> removeItemFromInventory(Item.ITEM_TYPE itemIndex, int quantity) {
		return cargo.removeItem(itemIndex, quantity);
	}
	
	@Override
	public boolean canGetItem(Item.ITEM_TYPE itemType, int numberOf) {
		return cargo.canAddItems(itemType, numberOf);
	}
	
	@Override
	public int getMaxSize() {
		return MAX_INVENTORY_SIZE;
	}
	
	@Override
	public double getMaxWeight() {
		return MAX_INVENTORY_WEIGHT;
	}
	
	@Override
	public double getWeight() {
		return cargo.getWeight();
	}
}
