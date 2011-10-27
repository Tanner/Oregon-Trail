package scene.encounter;

import model.Party;

public enum EncounterID {
	THIEF ("Thief Encounter", 1),
	ITEM ("Item Encounter", 1),
	POTHOLE ("Pothole Encounter", 1),
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
	
	public static Encounter getEncounter(Party party, EncounterID id, int min, int max) {
		if (id == EncounterID.THIEF)
			return new NullEncounter(party, min, max);
		else if (id == EncounterID.ITEM)
			return new ItemEncounter(party, min, max);
		else if (id == EncounterID.POTHOLE)
			return new NullEncounter(party, min, max);
		else if (id == EncounterID.MESSAGE)
			return new MessageEncounter(party, min, max);
		else
			return new NullEncounter(party, min, max);

	}
}
