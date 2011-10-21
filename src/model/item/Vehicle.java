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
	 * @param type The type of vehicle
	 */
	public Vehicle(double maxWeight, Item.ITEM_TYPE type) {
		super(type);
		this.status = new Condition(100);
		this.MAX_INVENTORY_WEIGHT = maxWeight;
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
