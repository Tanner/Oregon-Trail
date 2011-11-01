package scene.encounter;

import model.Party;

/** 
 * A list of Encounters, as well as basic methods to deal with Encounters.
 * Get the name, frequency, as well as generate any requested Encounter.
 */
public enum EncounterID {
	THIEF ("Thief Encounter", 1),
	ITEM ("Item Encounter", 1),
	//POTHOLE ("Pothole Encounter", 1),
	MESSAGE("Message Encounter", 2),
	NULL ("Null Encounter", 20);
	
	private final String name;
	private final int frequency;
	
	private EncounterID(String name, int frequency) {
		this.name = name;
		this.frequency = frequency;
	}
	
	public String getName() {
		return name;
	}
	
	public int getFrequency() {
		return frequency;
	}
	
	public String toString() {
		return getName();
	}
	
	/**
	 * A method to generate Encounters based on EncounterID
	 * @param party The current game's party
	 * @param id The id of the desired encounter
	 * @param min The minimum in the Encounter's occurrence range
	 * @param max The maximum in the Encounter's occurrence range
	 * @return The created Encounter
	 */
	public static Encounter getEncounter(Party party, EncounterID id, int value) {
		if (id == EncounterID.THIEF) {
			return new ThiefEncounter(party, value);
		} else if (id == EncounterID.ITEM) {
			return new ItemEncounter(party, value);
		//} else if (id == EncounterID.POTHOLE) {
			//return new NullEncounter(party, value);
		} else if (id == EncounterID.MESSAGE) {
			return new MessageEncounter(party, value);
		} else {
			return new NullEncounter(party, value);
		}

	}
}
