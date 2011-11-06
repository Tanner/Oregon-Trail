package model.item;

import java.util.List;

import core.ConstantStore;

import model.Condition;
import model.Inventoried;
import model.Inventory;
import model.Item;

/**
 * A vehicle is anything that is classified as a vehicle
 */
public class Vehicle extends Item implements Inventoried{

	private final Inventory cargo;
	
	private final int MAX_INVENTORY_SIZE;
	
	private final double MAX_INVENTORY_WEIGHT;

	/**
	 * Makes a vehicle to be used to get to oregon
	 * @param type The type of vehicle
	 */
	public Vehicle(double maxWeight, ItemType type) {
		super(type);
		this.MAX_INVENTORY_SIZE = Integer.parseInt(ConstantStore.get("ITEMS", type + "_MAX_INV_SIZE"));
		this.MAX_INVENTORY_WEIGHT = Double.parseDouble(ConstantStore.get("ITEMS", type + "_MAX_INV_WEIGHT"));
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
		cargo.addItemsToInventory(items);
	}
	
	@Override
	public void addItemToInventory(Item item) {
		cargo.addItemToInventory(item);
	}
	
	@Override
	public List<Item> removeItemFromInventory(ItemType itemIndex, int quantity) {
		return cargo.removeItemFromInventory(itemIndex, quantity);
	}
	
	@Override
	public boolean canGetItem(ItemType itemType, int numberOf) {
		return cargo.canGetItems(itemType, numberOf);
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

	public void repair(int amount) {
		status.increase(amount);
	}
}
