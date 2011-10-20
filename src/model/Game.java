package model;

import core.Logger;

/**
 * Holds all game data.
 */
public class Game {
	private Player player;
	private WorldMap worldMap;
	
	/**
	 * Know nothing, make nothing.
	 */
	public Game(WorldMap worldMap) {
		this.worldMap = worldMap;
		this.player = new Player();
		if(player != null){
			Logger.log("Player was created successfully", Logger.Level.INFO);
		}
	}
	
	/**
	 * For use by GameDirector. Returns player.
	 * @return the player game currently knows
	 */
	public Player getPlayer() {
		return this.player;
	}
}