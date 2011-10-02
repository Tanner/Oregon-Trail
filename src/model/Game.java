package model;

/**
 * Holds all game data
 * 
 * @author George Johnston
 */
public class Game {
	private Player player;
	
	/**
	 * Know nothing, make nothing.
	 */
	public Game() {
		this.player = new Player();
	}
	
	/**
	 * For use by GameDirector.  Returns player
	 * @return The player game currently knows.
	 */
	public Player getPlayer() {
		return this.player;
	}
}