package scene.encounter;

import model.Party;

public enum EncounterID {
	THIEF ("Thief Encounter"),
	ITEM ("Item Encounter"),
	POTHOLE ("Pothole Encounter");
	
	private final String name;
	
	private EncounterID(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return getName();
	}
	
	public static Encounter getEncounter(Party party, EncounterID id, int min, int max) {
		if (id == EncounterID.THIEF)
			return new ItemEncounter(party, min, max);
		else if (id == EncounterID.ITEM)
			return new ItemEncounter(party, min, max);
		else if (id == EncounterID.POTHOLE)
			return new ItemEncounter(party, min, max);
		else
			return new ItemEncounter(party, min, max);

	}
}
