package scene.encounter;

import model.Party;

/** 
 * A list of Encounters, as well as basic methods to deal with Encounters.
 * Get the name, frequency, as well as generate any requested Encounter.
 */
public enum EncounterID {
	THIEF ("Thief Encounter", 0, 0, 3, 5),
	ITEM ("Item Encounter", 1, 2, 1, 0),
	//POTHOLE ("Pothole Encounter", 2, 1, 3, 5),
	MESSAGE ("Message Encounter", 2, 2, 2, 2),
	RIVER ("River Encounter", 1, 1, 0, 0),
	NULL ("Null Encounter", 20, 20, 20, 20);	
	
	private final String name;
	private final int[] frequencies;
	
	private EncounterID(String name, int ... frequencies) {
		this.name = name;
		this.frequencies = frequencies;
	}
	
	public String getName() {
		return name;
	}
	
	public int[] getFrequencies() {
		return frequencies;
	}
	
	public String toString() {
		return getName();
	}
	
	/**
	 * A method to generate Encounters based on EncounterID
	 * @param party The current game's party
	 * @param id The id of the desired 
	 * @param value Multiplier for "extreme-ness" of the Encounter
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
		} else if (id == EncounterID.RIVER) {
			return new RiverEncounter(party, value);
		} else {
			return new NullEncounter(party, value);
		}
	}
}
