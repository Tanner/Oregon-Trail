package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.item.ItemType;
import model.worldMap.LocationNode;
import core.Logger;

/**
 * Holds all game data.
 */
@SuppressWarnings("serial")
public class Game implements Serializable{
	private Player player;
	
	private WorldMap worldMap;
	
	private Inventory storeInventory = new Inventory(16, 10000);

	/**
	 * Constructs an new Game with a {@code WorldMap}.
	 * @param worldMap The games world map
	 */
	public Game(WorldMap worldMap) {
		this.worldMap = worldMap;
		this.player = new Player();
		if(player != null){
			Logger.log("Player was created successfully", Logger.Level.INFO);
		}
		resetStoreInventory(worldMap.getMapHead());
	}
	
	/**
	 * For use by GameDirector. Returns player.
	 * @return the player game currently knows
	 */
	public Player getPlayer() {
		return this.player;
	}
	
	/**
	 * String representation of the game
	 * @return the string
	 */
	public String toString() {
		return null;
	}

	public Inventory getStoreInventory() {
		return storeInventory;
	}
	
	public WorldMap getWorldMap() {
		return worldMap;
	}

	public void reset() {
		worldMap.resetMap();
		this.worldMap = new WorldMap();
		this.player =  new Player();
		resetStoreInventory(worldMap.getMapHead());
	}

	public void resetStoreInventory(LocationNode location) {
		Random random = new Random();
		storeInventory.clear();
		
		//Populate the list of item types to allow for ready population of store slots
		List<ItemType> startItems = new ArrayList<ItemType>();
		for(ItemType itemType : ItemType.values()) {
			if(itemType.getNecessaryQuality() <= location.getCondition().getCurrent()) {
				startItems.add(itemType);
			}
		}
		//Items that wont show up in starting inventory
		startItems.remove(ItemType.STRANGEMEAT);
		startItems.remove(ItemType.SONIC);
		startItems.remove(ItemType.TRADEGOODS);

		//Now we fill in the stores inventory.
		int numberOf;
		for(ItemType itemType : startItems) {
			if(itemType.isFood()) {
				numberOf = (random.nextInt(50) + 25)  /  ((location.getRank() / 4) + 1);
			} else if (itemType.getName().equals(ItemType.MAP.getName()) || 
					itemType.getName().equals(ItemType.WAGON.getName())) {
				numberOf = 1;
			} else {
				numberOf = (random.nextInt(10) + 5) / ((location.getRank() / 4) + 1);
			}
			
			for(int i = 0; i < numberOf; i++) { 
				storeInventory.addItemToInventory(new Item(itemType));
			}
		}
		if(location.getRank() == 0) {
			for(int i = 0; i < random.nextInt(50) + 50; i++) {
				storeInventory.addItemToInventory(new Item(ItemType.TRADEGOODS));
			}
		}
	}
}