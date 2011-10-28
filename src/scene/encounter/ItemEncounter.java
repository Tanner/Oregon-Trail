package scene.encounter;

import java.util.Random;

import model.Item;
import model.Notification;
import model.Party;
import model.Item.ITEM_TYPE;

/**
 * Generates a random number of an item, and gives them to the Party's wagon.
 * Displays a modal with results of the transaction.
 */
public class ItemEncounter extends Encounter {
	private enum ITEM_ENCOUNTER_TYPE {
		APPLE(ITEM_TYPE.APPLE, "You rest by an apple tree and pick up %d %s.");
		
		private ITEM_TYPE itemType;
		private String message;
		
		private ITEM_ENCOUNTER_TYPE(ITEM_TYPE itemType, String message) {
			this.itemType = itemType;
			this.message = message;
		}
		
		public ITEM_TYPE getItemType() {
			return itemType;
		}
		
		public String getMessage(int number) {
			String itemName;
			
			if (number > 1) {
				itemName = itemType.getPluralName();
			} else {
				itemName = itemType.getName();
			}
			
			itemName = itemName.toLowerCase();
			
			return String.format(message, number, itemName);
		}
	}
	
	private int numItems;
	private ITEM_ENCOUNTER_TYPE type;
	
	public ItemEncounter(Party party, int min, int max) {
		super(party, min, max);
		
		Random random = new Random();
		
		int index = random.nextInt(ITEM_ENCOUNTER_TYPE.values().length);
		type = ITEM_ENCOUNTER_TYPE.values()[index];
	}
	
	@Override
	public EncounterNotification doEncounter() {
		// Give the party 1 to 10 items
		numItems = (int) (Math.random() * 10) + 1;
		if (party.getVehicle() != null) {
			for (int i = 0; i < numItems; i++) {
				party.getVehicle().addItemToInventory(new Item(type.getItemType()));
			}
		}
		return makeNotification();
	}

	@Override
	protected EncounterNotification makeNotification() {
		Notification notification = new Notification(type.getMessage(numItems));
		return new EncounterNotification(notification, null);
	}

}
