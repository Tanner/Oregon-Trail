package scene.encounter;

import java.util.Random;

import core.SoundStore;

import model.Item;
import model.Notification;
import model.Party;
import model.Item.ITEM_TYPE;
import model.item.Animal;

/**
 * Generates a random number of an item, and gives them to the Party's wagon.
 * Displays a modal with results of the transaction.
 */
public class ItemEncounter extends Encounter {
	private enum ITEM_ENCOUNTER_TYPE {
		APPLE(ITEM_TYPE.APPLE, "You rest by an apple tree. You pick up %d %3$s."),
		BREAD(ITEM_TYPE.BREAD, "You find a basket of bread which contains %d %s %s. You decide to take it."),
		GUN(ITEM_TYPE.GUN, "You find %d rusted %3$s on the trail. It seems like it still works."),
		OX(ITEM_TYPE.OX, "You find %d lonely %3$s. You let the %3$s join your party.");
		
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
			String itemName = number > 1 ? itemType.getPluralName() : itemType.getName();
			itemName = itemName.toLowerCase();
			
			String itemPrefix = "pieces of";
			
			if (itemType.getPluralName().equals(itemType.getName())) {
				// If the plural is the same as the singular
				if (number == 1) {
					itemPrefix = "piece of";
				}
			}
			
			return String.format(message, number, itemPrefix, itemName);
		}
	}
	
	private int numItems;
	private ITEM_ENCOUNTER_TYPE type;
	
	/**
	 * Constructs an ItemEncounter with a party, as well as its range of occurrence.
	 * @param party The party, so the encounter can affect the game
	 * @param min The beginning of the range of occurrence
	 * @param max The end of the range of occurrence
	 */
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
		if (type.getItemType().isAnimal()) {
			for (int i = 0; i < numItems; i++) {
				party.addAnimals(new Animal(type.getItemType()));
			}
		} else if (party.getVehicle() != null) {
			for (int i = 0; i < numItems; i++) {
				party.getVehicle().addItemToInventory(new Item(type.getItemType()));
			}
		}
		SoundStore.get().playSound("ItemGet");
		return makeNotification();
	}

	@Override
	protected EncounterNotification makeNotification() {
		String message = type.getMessage(numItems);
		
		Notification notification = new Notification(message);
		return new EncounterNotification(notification, null);
	}

}
