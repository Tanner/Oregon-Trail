package scene.encounter;

import model.Item;
import model.Notification;
import model.Party;
import model.Item.ITEM_TYPE;

public class ItemEncounter extends Encounter {

	private int numItems;
	public ItemEncounter(Party party, int min, int max) {
		super(party, min, max);
	}
	
	@Override
	public EncounterNotification doEncounter() {
		numItems = (int) (Math.random() * 10) + 1;
		if (party.getVehicle() != null) {
			for (int i = 0; i < numItems; i++) {
				party.getVehicle().addItemToInventory(new Item(ITEM_TYPE.APPLE));	
			}
		}
		return makeNotification();	
	}

	@Override
	protected EncounterNotification makeNotification() {
		Notification notification = new Notification(String.format("You rest by an apple tree and pick up %d apples.", numItems));
		return new EncounterNotification(notification, null);
	}

}
