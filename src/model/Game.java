package model;

import model.Party.Pace;
import model.Party.Rations;
import core.Logger;

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
		Person person1 = new Person("Frank", Person.Profession.BAKER);
		Person person2 = new Person("Bob", Person.Profession.GUNSMITH, Person.Skill.BLACKSMITHING);
		this.player = new Player(new Party(Pace.GRUELING, Rations.BAREBONES, person1, person2));
		Logger.log("Game was created successfully", Logger.Level.INFO);
	}
	
	/**
	 * For use by GameDirector.  Returns player
	 * @return The player game currently knows.
	 */
	public Player getPlayer() {
		return this.player;
	}
}