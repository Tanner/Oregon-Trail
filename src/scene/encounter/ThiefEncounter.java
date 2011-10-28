package scene.encounter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import core.SoundStore;

import model.Inventoried;
import model.Item.ITEM_TYPE;
import model.Notification;
import model.Party;
import model.item.Vehicle;

public class ThiefEncounter extends Encounter {
	
	private String itemName;
	
	private Random random = new Random();
	
	public ThiefEncounter(Party party, int min, int max) {
		super(party, min, max);
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
			Inventoried inventory = inventories.remove((int) (Math.random() * inventories.size()));
			if ( !inventory.getInventory().isEmpty() ) {
				removed = true;
				List<ITEM_TYPE> items = inventory.getInventory().getPopulatedSlots();
				ITEM_TYPE type = items.get((int) (random.nextDouble() * items.size()));
				inventory.getInventory().removeItemFromInventory(type, random.nextInt(inventory.getInventory().getNumberOf(type)));
				itemName = type.getName();
			}
		}
		//SoundStore.get().playMusic("Smooth");
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
