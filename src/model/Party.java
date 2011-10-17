package model;

import java.util.ArrayList;

import model.item.Vehicle;

import core.Logger;
import core.Logger.Level;

/**
 * Party class that contains an array of persons that are members.
 */
public class Party {
	private ArrayList<Person> members = new ArrayList<Person>();
	private int money;
	private Pace currentPace;
	private Rations currentRations;
	private Vehicle vehicle;
	private int location;
	
	/**
	 * If party is created before members, this constructor is used.
	 */
	public Party() {
		this.members = null;
	}
	
	/**
	 * If people are present before party is created, this constructor is used
	 * @param party Array of people to be initialized into party
	 */
	public Party(Pace pace, Rations rations, ArrayList<Person> party) {
		for (Person person : party) {
			if (person != null) {
				members.add(person);
			}
		}
		
		this.money = 0;
		this.currentPace = pace;
		this.currentRations = rations;
		this.location = 0;
		
		String partyCreationLog = members.size() + " members were created successfully: ";
		for (Person person : party) {
			this.money += person.getProfession().getMoney();
			Logger.log(person.getName()+ " as a " + person.getProfession() + " brings $" + 
					person.getProfession().getMoney() + " to the party.", Logger.Level.INFO);

			partyCreationLog += person.getName() + " ";
		}
		Logger.log(partyCreationLog, Logger.Level.INFO);
		Logger.log("Party starting money is: $" + money, Logger.Level.INFO);
		Logger.log("Current pace is: " + currentPace + " and current rations is: " + currentRations, Level.INFO);
	}
	
	/**
	 * Buys the item (pays for it) and then gives it to the designated buyer.
	 * @param items The items to buy
	 * @param buyer The thing that wants the items in its inventory
	 * @return True if successful
	 */
	public boolean buyItemForInventory(ArrayList<Item> items, Inventoried buyer) {
		int cost = 0;
		Item.ITEM_TYPE itemType = items.get(0).getType();
		int numberOf = items.size();
		
		for (Item item : items) {
			cost += item.getCost();
		}
		
		if (money > cost && buyer.canGetItem(itemType, numberOf)) {
			buyer.addItemToInventory(items);
			money -= cost;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Returns a list of inventoried things that can but the designated items.
	 * @param items The items to test buying
	 * @return The list of people
	 */
	public ArrayList<Inventoried> canGetItem(Item.ITEM_TYPE itemType, int numberOf) {
		ArrayList<Inventoried> ableList = new ArrayList<Inventoried>();
		
		if (itemType.getCost() * numberOf > money) {
			return ableList;
		}
		
		for (Person person : members) {
			if (person.canGetItem(itemType, numberOf)) {
				ableList.add(person);
			}
		}
		
		if (vehicle != null && vehicle.canGetItem(itemType, numberOf)) {
			ableList.add(vehicle);
		}
		
		return ableList;
	}
	/**
	 * Returns an array of Persons present in the party
	 * @return
	 */
	public ArrayList<Person> getPartyMembers() {
		return this.members;
	}

	/**
	 * Returns an array of the Skills present in the party
	 * @return
	 */
	public ArrayList<Person.Skill> getSkills() {
		ArrayList<Person.Skill> skillList = new ArrayList<Person.Skill>();
		
		for (Person person : members) {
			for (Person.Skill skill: person.getSkills()) {
				if (!skillList.contains(skill) && skill != Person.Skill.NONE) {
					skillList.add(skill);
				}
			}
		}
		
		return skillList;
	}
	/**
	 * Returns the money
	 * @return the party's money
	 */
	public int getMoney() {
		return this.money;
	}
	
	/**
	 * Sets the party's money
	 * @param money
	 */
	public void setMoney(int money) {
		Logger.log("Party money changed to: $" + money, Logger.Level.INFO);
		
		this.money = money;
	}
	
	/**
	 * Returns the vehicle of the party
	 * @return The vehicle of the party
	 */
	public Vehicle getVehicle() {
		return vehicle;
	}
	
	/**
	 * Sets a vehicle as the party vehicle.
	 * @param vehicle The party vehicle
	 */
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	
	/**
	 * 
	 * The possible values of the party's pace
	 */
	public enum Pace{
		STEADY ("Steady", 10),
		STRENUOUS ("Strenuous", 20),
		GRUELING ("Grueling", 30);
		
		private final String name;
		private int pace;
		
		/**
		 * Constructs a new Pace.
		 * @param name The name of the pace
		 */
		private Pace(String name, int pace) {
			this.name = name;
			this.pace = pace;
		}
		
		@Override
		public String toString() {
			return this.name;
		}
		
		/**
		 * Returns the current pace/speed.
		 * @return the current pace/speed
		 */
		public int getPace() {
			return pace;
		}
	}
	
	/**
	 * Get the party's pace.
	 * @return the party's pace setting
	 */
	public Pace getPace() {
		return currentPace;
	}
	
	/**
	 * Sets a party's new pace.
	 * @param pace The party's new pace
	 */
	public void setPace(Pace pace) {
		Logger.log("Party pace changed to: " + pace, Logger.Level.INFO);
		this.currentPace = pace;
	}
	
	/**
	 * The possible settings of the party's consumption rate
	 */	
	public enum Rations {
		FILLING ("Filling", 100),
		MEAGER ("Meager", 75),
		BAREBONES ("Barebones", 50);
		
		private final String name;
		private int breakpoint;
		
		/**
		 * Construct a new Ration
		 * @param name The name of the rations
		 */
		private Rations(String name, int breakpoint) {
			this.name = name;
			this.breakpoint = breakpoint;
		}
		
		@Override
		public String toString() {
			return this.name;
		}
		
		/**
		 * Returns the breakpoint.
		 * @return The breakpoint value
		 */
		public int getBreakpoint() {
			return breakpoint;
		}
	}
	
	/**
	 * Returns the party's current ration consumption rate.
	 * @return The party's current ration consumption rate
	 */
	public Rations getRations() {
		return currentRations;
	}
	
	/**
	 * Sets the party's ration to a new ration consumption rate
	 * @param rations The desired new ration consumption rate
	 */
	public void setRations(Rations rations) {
		Logger.log("Party rations changed to: " + rations, Logger.Level.INFO);
		
		this.currentRations = rations;
	}
	
	@Override
	public String toString() {
		String str = "Members: ";
		for (Person person : members) {
			str += person.toString() + "; ";
		}
		str += "Money remaining:" + money;
		return str;
	}
	
	/**
	 * Returns the current location.
	 * @return Party's current location
	 */
	public int getLocation() {
		return location;
	}
	
	/**
	 * Steps the player forward through the game.
	 */
	public int walk() {
		location += 2*getPace().getPace();
		
		for (Person person : members) {
			person.decreaseHealth(getPace().getPace());
			healToBreakpoint(person);
		}
		
		return location;
	}
	
	/**
	 * Heals the person until they are at their designated breakpoint.
	 * @param person The person to feed
	 */
	public void healToBreakpoint (Person person) {
		// Figure out how much restoration is needed.
		int restoreNeeded = 0;
		
		if (person.getHealth().getCurrent() < getRations().getBreakpoint()) {
			restoreNeeded = getRations().getBreakpoint() - person.getHealth().getCurrent();
		
		}
		
		// Find out if the person has any food
		boolean hasFood = false;
		
		for (Item.ITEM_TYPE itemType : person.getInventory().getPopulatedSlots()) {
			if (itemType.getIsFood()) {
				hasFood = true;
			}
		}
		
		if (restoreNeeded > 0 && hasFood) {
			//If we need restoration, and have food
			Item.ITEM_TYPE firstFood = null;
			
			for (Item.ITEM_TYPE itemType : person.getInventory().getPopulatedSlots()) {
				if (itemType.getIsFood() && firstFood == null) {
					firstFood = itemType;
				}
			}
			
			ArrayList<Item> foodList = person.removeItemFromInventory(firstFood, 1);
			Item food = foodList.get(0);
			int foodFactor = food.getType().getFoodFactor();

			int foodToEat = (restoreNeeded / foodFactor) + 1; //+1 to ensure that we overshoot
			
			if (food.getStatus().getCurrent() > foodToEat) {
				//If there is enough condition in the food to feed the person completely, heal them and eat
				
				person.increaseHealth(foodToEat * foodFactor);
				food.decreaseStatus(foodToEat);
				person.addItemToInventory(food); //puts the item back in inventory
				//Food status down, person health up
			} else {
				//we don't have enough status in the food to completely heal the person, so eat it all.
				person.increaseHealth(food.getStatus().getCurrent() * foodFactor);
				healToBreakpoint(person); //Recursively call the function to ensure we eat as much as possible.
			}
		}
	}
}
