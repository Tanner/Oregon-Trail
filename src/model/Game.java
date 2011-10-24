package model;

import core.Logger;

/**
 * Holds all game data.
 */
public class Game {
	private final Player player;
	
	private final WorldMap worldMap;
	
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
		storeInventory.addRandomItems();
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
		storeInventory.addRandomItems();
		player.setParty(null);
	}

}