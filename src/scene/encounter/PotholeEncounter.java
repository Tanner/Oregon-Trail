package scene.encounter;

import model.Condition;
import model.Notification;
import model.Party;
import model.item.Vehicle;

/**
 * An encounter where the party's vehicle will become randomly damaged.
 */
public class PotholeEncounter extends Encounter {
	
	private final double MAX_DECREASE = .25;
	private String message;
	
	public PotholeEncounter(Party party, int value) {
		super(party, value, EncounterID.POTHOLE);
	}
	
	@Override
	public EncounterNotification doEncounter() {
		Vehicle vehicle = party.getVehicle();
		if (vehicle == null) {
			message = "You step around a huge hole on the trail.  You wonder " +
					"what would have happened to your wagon if you ran over " +
					"it.";
		}
		else {
			Condition condition = vehicle.getStatus();
			double max = condition.getMax();
			double damage = max * MAX_DECREASE * Math.random();
			party.damageVehicle((int) damage);
			message = "You weren't paying attention and ran your wagon over a " +
					"huge pothole.";
			if (party.getVehicle().getConditionPercentage() == 0) {
				message += "  Your vehicle has broken down.";
				if(party.repairVehicle() == 0) {
					message += "  However, you managed to fix it.";
				} else {
					if(party.getVehicle().getConditionPercentage() == 0) {
						message += "  You couldn't repair your wagon and had to abandon it.";
					} else {
						message += "  You managed to fix it enough to keep going.";
					}
				}
			}
			else
				message += "  Better check your wagon for damages.";
		}
		return makeNotification();
		
	}

	@Override
	protected EncounterNotification makeNotification() {
		return new EncounterNotification(new Notification(message), null);
	}
}
