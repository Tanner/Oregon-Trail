package scene.encounter;

import model.Party;

/**
 * An abstract encounter.  Encounters have a certain chance of occurring,
 * and are meant to execute their own action, as well as return a message
 * or scene encapsulated in a EncounterNotification
 */
public abstract class Encounter {
	protected int value;
	protected Party party;
	protected EncounterID id;
	
	public Encounter(Party party, int value, EncounterID id) {
		this.party = party;
		this.value = value;
		this.id = id;
	}
	
	public boolean isInRange(int num, int timeIndex) {
		return num < value * id.getFrequencies()[timeIndex];
	}
	
	public int getValue(int timeIndex) {
		return value * id.getFrequencies()[timeIndex];
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
	
	public abstract EncounterID getEncounterID();

	public void increaseValue(int amount) {
		value += amount;
	}
}
