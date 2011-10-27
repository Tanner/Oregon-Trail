package scene.encounter;

import model.Party;

public abstract class Encounter {

	private int min;
	private int max;
	protected Party party;
	
	public Encounter(Party party, int min, int max) {
		this.min = min;
		this.max = max;
		this.party = party;
	}
	
	public boolean isInRange(int num) {
		if ( num <= max && num >= min )
			return true;
		return false;
	}
	
	public int getMax() {
		return max;
	}
	
	public abstract EncounterNotification doEncounter();
	protected abstract EncounterNotification makeNotification();
}
