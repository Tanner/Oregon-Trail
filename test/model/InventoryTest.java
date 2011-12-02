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

	@Test
	public void testCanGetItems() {
		
		
		Inventory inv = new Inventory(1,10);
		//Check for incorrect additions
		assertFalse("Can't get 0 items.", inv.canGetItems(ItemType.APPLE,0));
		assertFalse("Can't get negative items.", inv.canGetItems(ItemType.APPLE,-1));
		
		//Check if you can add with valid/invalid weights
		assertTrue("Should be able to add item, failed.", inv.canGetItems(ItemType.APPLE,1));
		assertTrue("Should be able to add item, failed.", inv.canGetItems(ItemType.APPLE,4));
		assertFalse("Shouldn't be able to add too much weight.", inv.canGetItems(ItemType.APPLE,7));
		
		//Test not being able to add more item types then you have slots, also weight
		inv = new Inventory(2,20);
		inv.addItemToInventory(new Item(ItemType.GUN));		//Inventory holding 5 lbs, 1 slot
		assertTrue("Should be able to add item, failed.", inv.canGetItems(ItemType.GUN, 1));
		assertFalse("Shouldn't be able to add too much weight", inv.canGetItems(ItemType.GUN, 4));
		inv.addItemToInventory(new Item(ItemType.APPLE));	//Inventory holding 6.5 lbs, 2 slots
		assertFalse("Can't add items of a new type if slots are already full", inv.canGetItems(ItemType.AMMO, 1));
		inv.addItemToInventory(new Item(ItemType.GUN));
		inv.addItemToInventory(new Item(ItemType.GUN));
		inv.addItemToInventory(new Item(ItemType.APPLE));
		inv.addItemToInventory(new Item(ItemType.APPLE)); //Inventory holding 19.5 lbs, 2 slots
		assertFalse("Shouldn't be able to hold too much weight", inv.canGetItems(ItemType.APPLE, 1));
		assertFalse("Shouldn't be able to hold too much weight", inv.canGetItems(ItemType.GUN, 1));
		assertFalse("Can't add items of a new type if slots are already full, also too much weight", inv.canGetItems(ItemType.AMMO, 1));
	}

}
