package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Item.ITEM_TYPE;
import core.Logger;

/**
 * Holds all game data.
 */
public class Game {
	private Player player;
	
	private WorldMap worldMap;
	
	private Inventory storeInventory = new Inventory(8, 10000);
	
	
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
		initializeStoreInventory();
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
	
	public void setStoreInventory(Inventory storeInventory) {
		this.storeInventory = storeInventory;
	}
	
	public WorldMap getWorldMap() {
		return worldMap;
	}

	public void reset() {
		storeInventory.clear();
		initializeStoreInventory();
		this.player =  new Player();
		//this.worldMap = new WorldMap();
	}

	private void initializeStoreInventory() {
		Random random = new Random();
		
		//Populate the list of item types to allow for ready population of store slots
		List<ITEM_TYPE> startItems = new ArrayList<ITEM_TYPE>();
		for(ITEM_TYPE itemType : ITEM_TYPE.values()) {
			startItems.add(itemType);
		}
		//Items that wont show up in starting inventory
		startItems.remove(ITEM_TYPE.STRANGEMEAT);
		startItems.remove(ITEM_TYPE.SONIC);
		
		//Now we fill in the stores inventory.
		int numberOf;
		for(ITEM_TYPE itemType : startItems) {
			if(itemType.isFood()) {
				numberOf = random.nextInt(50) + 25;
			} else {
				numberOf = random.nextInt(10) + 5;
			}
			
			for(int i = 0; i < numberOf; i++) {
				storeInventory.addItemToInventory(new Item(itemType));
			}
		}
		System.out.println(storeInventory);
	}

}