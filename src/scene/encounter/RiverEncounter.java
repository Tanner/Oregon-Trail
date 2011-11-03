package scene.encounter;

import scene.SceneID;
import model.Notification;
import model.Party;

public class RiverEncounter extends Encounter {
	
	public RiverEncounter(Party party, int value) {
		super(party, value, EncounterID.RIVER);
	}
	
	@Override
	public EncounterNotification doEncounter() {
		return makeNotification();
	}

	@Override
	protected EncounterNotification makeNotification() {
		String message = "You've reached a river.  Make your decision wisely!";
		return new EncounterNotification(new Notification(message, true), SceneID.RIVER);
	}

	@Override
	public EncounterID getEncounterID() {
		return id;
	}

}
