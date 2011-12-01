package model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import model.item.ItemType;

import org.junit.Test;

public class InventoryTest {
	@Test
	public void testAddOneItemstoInventory() {
		Inventory inventory = new Inventory(1, 50);
		List<Item> list = new ArrayList<Item>();
		
		list.add(new Item(ItemType.APPLE));
		inventory.addItemsToInventory(list);	
		assertTrue(inventory.getCurrentSize() == 1);
	}
	
	@Test
	public void testAddTooManyItemsToInventory() {
		Inventory inventory = new Inventory(1, 50);
		List<Item> list = new ArrayList<Item>();

		list.add(new Item(ItemType.APPLE));	
		list.add(new Item(ItemType.APPLE));
		inventory.addItemsToInventory(list);
		assertTrue(inventory.getCurrentSize() == 1);
	}
	
	@Test
	public void testAddNominalAmountOfItemsToInventory() {
		Inventory inventory = new Inventory(10, 50);
		List<Item> list = new ArrayList<Item>();
		
		list.add(new Item(ItemType.APPLE));	
		list.add(new Item(ItemType.APPLE));
		inventory.addItemsToInventory(list);
		assertTrue(inventory.getCurrentSize() == 1);
		
		list.clear();
		list.add(new Item(ItemType.AMMO));	
		list.add(new Item(ItemType.AMMO));
		inventory.addItemsToInventory(list);
		assertTrue(inventory.getCurrentSize() == 2);
	}
	
	@Test
	public void testAddDifferentItemTypesToInventory() {
		Inventory inventory = new Inventory(10, 50);
		List<Item> list = new ArrayList<Item>();
		
		list.add(new Item(ItemType.AMMO));
		list.add(new Item(ItemType.APPLE));
		inventory.addItemsToInventory(list);
		assertTrue(inventory.getCurrentSize() == 1);
	}
	
	@Test
	public void testAddTooMuchWeightItemsToInventory() {
		Inventory inventory = new Inventory(10, 1);
		List<Item> list = new ArrayList<Item>();
		
		list.add(new Item(ItemType.MULE));
		inventory.addItemsToInventory(list);
		assertTrue(inventory.getCurrentSize() == 0);
	}
}