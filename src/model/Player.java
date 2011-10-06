package model;

import core.Logger;

/**
 * Player class contains party.  Can be passed or set manually.
 * 
 * @author George Johnston
 */
public class Player {
	private Party party;
	
	/**
	 * Constructor for unknown current party. Should be used at creation of 
	 * game before party create scene
	 */
	public Player() {
		this.party = null;
	}
	
	/**
	 * Party already created. Don't know when we'll use this, but maybe for load/save
	 * @param party An already existant party
	 */
	public Player(Party party) {
		this.party = party;
		if(party != null){
			Logger.log("Party successfully attached to player", Logger.Level.INFO);
		}
	}
	
	/**
	 * Returns the player's party. GameDirector calls this a bunch
	 * @return The party for scenes to mess with
	 */
	public Party getParty() {
		return this.party;
	}
	
	/**
	 * Enables scenes like partyCreationScene to give the player a party.
	 * @param party Party for the player to own.
	 */
	public void setParty(Party party) {
		this.party = party;
	}
}
