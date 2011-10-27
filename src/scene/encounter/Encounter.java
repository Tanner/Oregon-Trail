package scene.encounter;

import model.Party;

/**
 * An abstract encounter.  Encounters have a certain chance of occurring,
 * and are meant to execute their own action, as well as return a message
 * or scene encapsulated in a EncounterNotification
 */
public abstract class Encounter {

	private int min;
	private int max;
	protected Party party;
	
	public Encounter() {
		party = null;
		min = -1;
		max = -1;
	}
	
	/**
	 * Give an Encounter the party, as well as its range of occurrence.
	 * @param party The party, so the encounter can affect the game
	 * @param min The beginning of the range of occurrence
	 * @param max The end of the range of occurrence
	 */
	public Encounter(Party party, int min, int max) {
		this.min = min;
		this.max = max;
		this.party = party;
	}
	
	/**
	 * Check if a certain number lies within an Encounter's range.  Meant to determine
	 * when an encounter occurs.
	 * @param num A randomly generated number
	 * @return True if num is in the range of this encounter's min and max
	 */
	public boolean isInRange(int num) {
		if ( num <= max && num >= min )
			return true;
		return false;
	}
	
	/**
	 * @return The maximum value for the Encounter's range
	 */
	public int getMax() {
		return max;
	}
	
	/**
	 * Executes the logic of the encounter.
	 * @return An EncounterNotification describing the encounter.
	 */
	public abstract EncounterNotification doEncounter();
	
	/**
	 * Creates the notification data for the Encounter.
	 * @return An EncounterNotification with the
	 */
	protected abstract EncounterNotification makeNotification();
}
