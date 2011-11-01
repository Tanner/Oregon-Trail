package scene.encounter;

import model.Notification;
import model.Party;

public class NullEncounter extends Encounter {

	public NullEncounter(Party party, int value) {
		super(party, value);
	}
	
	@Override
	public EncounterNotification doEncounter() {
		value -= 1;
		return makeNotification();
	}

	@Override
	protected EncounterNotification makeNotification() {
		return new EncounterNotification(new Notification("", false), null);
	}
}
