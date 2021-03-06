package scene.encounter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Inventoried;
import model.Notification;
import model.Party;
import model.item.ItemType;
import model.item.Vehicle;

/**
 * An encounter where the party will have a random item and item amount
 * removed from their inventory.
 */
public class ThiefEncounter extends Encounter {
	
	private String itemName;
	
	private Random random = new Random();
	
	public ThiefEncounter(Party party, int value) {
		super(party, value, EncounterID.THIEF);
		itemName = "";
	}
	
	@Override
	public EncounterNotification doEncounter() {
		
		ArrayList<Inventoried> inventories = new ArrayList<Inventoried>();
		Vehicle vehicle = party.getVehicle();
		if (vehicle != null)
			inventories.add(vehicle);
		inventories.addAll(party.getPartyMembers());
		
		boolean removed = false;
		
		while (!inventories.isEmpty() && !removed) {
			Inventoried inventory = inventories.remove(random.nextInt(inventories.size()));
			if ( !inventory.getInventory().isEmpty() ) {
				removed = true;
				List<ItemType> items = inventory.getInventory().getPopulatedSlots();
				ItemType type = items.get((int) (random.nextDouble() * items.size()));
				inventory.getInventory().removeItemFromInventory(type, random.nextInt(inventory.getInventory().getNumberOf(type))+1);
				itemName = type.getName();
			}
		}
		value /= 5;
		//SoundStore.get().playSound("Smooth");
		return makeNotification();

	}

	@Override
	protected EncounterNotification makeNotification() {
		
		String message;
		
		if (itemName.length() > 0)
			message = "Oh no, a thief took your " +
			itemName + ".  Hope you can survive without it.";
		else
			message = "A thief approached your party, but you didn't even" +
				"have anything to steal!  Hope you make it to the next town.";
		
		return new EncounterNotification(new Notification(message, true), null);
	}
	
}
