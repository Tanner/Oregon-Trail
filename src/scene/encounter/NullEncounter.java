package scene.encounter;

import model.Notification;
import model.Party;

public class NullEncounter extends Encounter {

	public NullEncounter(Party party, int min, int max) {
		super(party, min, max);
	}
	
	@Override
	public EncounterNotification doEncounter() {
		return makeNotification();
	}

	@Override
	protected EncounterNotification makeNotification() {
		return new EncounterNotification(new Notification("", false), null);
	}

}
