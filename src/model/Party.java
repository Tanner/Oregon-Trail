package model;

import java.util.ArrayList;
import java.util.List;

import model.datasource.HUDDataSource;
import model.item.Vehicle;

import core.Logger;
import core.Logger.Level;

/**
 * Party class that contains an array of persons that are members.
 * @author Null && Void
 */
public class Party implements HUDDataSource {
	
	private List<Person> members = new ArrayList<Person>();
	
	private int money;
	
	private Pace currentPace;
	
	private Rations currentRations;
	
	private Vehicle vehicle;
	
	private int location;
	
	/**
	 * 
	 * The possible values of the party's pace
	 */
	public enum Pace{
		STEADY ("Steady", 10),
		STRENUOUS ("Strenuous", 20),
		GRUELING ("Grueling", 30);
		
		private final String name;

		private final int speed;
		
		/**
		 * Constructs a new Pace.
		 * @param name The name of the pace
		 * @param speed The speed the pace makes you travel
		 */
		private Pace(String name, int speed) {
			this.name = name;
			this.speed = speed;
		}
		
		@Override
		public String toString() {
			return this.name;
		}
		
		/**
		 * Returns the current pace/speed.
		 * @return the current pace/speed
		 */
		public int getSpeed() {
			return speed;
		}
	}

	/**
	 * The possible settings of the party's consumption rate
	 */	
	public enum Rations {
		FILLING ("Filling", 100),
		MEAGER ("Meager", 75),
		BAREBONES ("Barebones", 50);
		
		private final String name;

		private final int breakpoint;
		
		/**
		 * Construct a new Ration
		 * @param name The name of the rations
		 * @param breakpoint The goal stable health for the party members
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
	 * If party is created before members, this constructor is used.
	 */
	public Party() {
		this.members = null;
	}
	
	/**
	 * If people are present before party is created, this constructor is used
	 * @param currentPace The current pace of the party
	 * @param currentRations The current rations of the party
	 * @param party Array of people to be initialized into party
	 */
	public Party(Pace currentPace, Rations currentRations, List<Person> party) {
		for (Person person : party) {
			if (person != null) {
				members.add(person);
			}
		}
		
		this.money = 0;
		this.currentPace = currentPace;
		this.currentRations = currentRations;
		this.location = 0;
		
		final StringBuffer partyCreationLog = new StringBuffer(members.size() + 
				" members were created successfully: ");
		for (Person person : party) {
			this.money += person.getProfession().getMoney();
			Logger.log(person.getName() + " as a " + 
					person.getProfession() + " brings $" + 
					person.getProfession().getMoney() + 
					" to the party.", Logger.Level.INFO);

			partyCreationLog.append(person.getName() + " ");
		}
		Logger.log(partyCreationLog.toString(), Logger.Level.INFO);
		Logger.log("Party starting money is: $" + money, Logger.Level.INFO);
		Logger.log("Current pace is: " + this.currentPace + 
				" and current rations is: " + this.currentRations, Level.INFO);
	}
	
	/**
	 * Buys the item (pays for it) and then gives it to the designated buyer.
	 * @param items The items to buy
	 * @param buyer The thing that wants the items in its inventory
	 */
	public void buyItemForInventory(List<Item> items, Inventoried buyer) {
		int cost = 0;
		final Item.ITEM_TYPE itemType = items.get(0).getType();
		final int numberOf = items.size();
		
		for (Item item : items) {
			cost += item.getCost();
		}
		
		if (money > cost && buyer.canGetItem(itemType, numberOf)) {
			buyer.addItemsToInventory(items);
			money -= cost;
			return;
		} else {
			return;
		}
	}
	
	/**
	 * Returns a list of inventoried things that can but the designated items.
	 * @param itemType The item type that is being tested
	 * @param numberOf The number of items to try with
	 * @return The list of people
	 */
	public List<Inventoried> canGetItem(Item.ITEM_TYPE itemType, int numberOf) {
		final List<Inventoried> ableList = new ArrayList<Inventoried>();
		
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
	 * Returns a list of Persons present in the party
	 * @return The members of the party
	 */
	public List<Person> getPartyMembers() {
		return this.members;
	}

	/**
	 * Returns a list of the Skills present in the party
	 * @return A list of skills in the party
	 */
	public List<Person.Skill> getSkills() {
		final List<Person.Skill> skillList = new ArrayList<Person.Skill>();
		
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
	 * Get the party's pace.
	 * @return the party's pace setting
	 */
	public Pace getPace() {
		return currentPace;
	}
	
	/**
	 * Sets a party's new pace.
	 * @param currentPace The party's new pace
	 */
	public void setPace(Pace currentPace) {
		Logger.log("Party pace changed to: " + currentPace, Logger.Level.INFO);
		this.currentPace = currentPace;
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
		final StringBuffer str = new StringBuffer("Members: ");
		for (Person person : members) {
			str.append(person.toString() + "; ");
		}
		str.append("Money remaining:" + money);
		return str.toString();
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
	 * @return the distance travelled with the walk
	 */
	public int walk() {
		location += 2 * getPace().getSpeed();
		
		final List<Person> deathList = new ArrayList<Person>();
		for (Person person : members) {
			healToBreakpoint(person);
			person.decreaseHealth(getPace().getSpeed());
			Logger.log(checkHungerStatus(person), Logger.Level.INFO);
			if(person.getHealth().getCurrent() == 0) {
				deathList.add(person);
			} else {
				healToBreakpoint(person);
			}
		}
		for (Person person : deathList) {
			members.remove(person);
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
			restoreNeeded = getRations().getBreakpoint() - 
			person.getHealth().getCurrent();
		
		}
		
		// Find out if the person has any food
		boolean personHasFood = false;
		boolean vehicleHasFood = false;
		
		for (Item.ITEM_TYPE itemType : person.getInventory().getPopulatedSlots()) {
			if (itemType.isFood()) {
				personHasFood = true;
			}
		}
		if(!personHasFood) {
			for (Item.ITEM_TYPE itemType : vehicle.getInventory().getPopulatedSlots()) {
				if (itemType.isFood()) {
					vehicleHasFood = true;
				}
			}
		}
		
		Inventoried donator = null;
		if (personHasFood) {
			donator = person;
		}
		else if (!personHasFood && vehicleHasFood){
			donator = vehicle;
		}
		
		if (restoreNeeded > 0 && donator != null) {
			//If we need restoration, and have food
			Item.ITEM_TYPE firstFood = null;
			final List<Item.ITEM_TYPE> typeList = 
				donator.getInventory().getPopulatedSlots();
			
			for (Item.ITEM_TYPE itemType : typeList) {
				if (itemType.isFood() && firstFood == null) {
					firstFood = itemType;
				}
			}
			
			final List<Item> foodList = 
				donator.removeItemFromInventory(firstFood, 1);
			
			final Item food = foodList.get(0);
			int foodFactor = food.getType().getFoodFactor();
			
			//Do some handling for party member skills, such as cooking
			if(food.getType().isPlant() && getSkills().contains(Person.Skill.BOTANY)) {
				foodFactor += 1;
			}
			if(getSkills().contains(Person.Skill.COOKING)) {
				foodFactor += 1;
			}

			final int foodToEat = (restoreNeeded / foodFactor) + 1; //+1 to ensure that we overshoot
			
			if (food.getStatus().getCurrent() > foodToEat) {
				//If there is enough condition in the food to feed the person completely, heal them and eat
				
				person.increaseHealth(foodToEat * foodFactor);
				food.decreaseStatus(foodToEat);
				donator.addItemToInventory(food); //puts the item back in inventory
				//Food status down, person health up
			} else {
				//we don't have enough status in the food to completely heal the person, so eat it all.
				person.increaseHealth(food.getStatus().getCurrent() * foodFactor);
				healToBreakpoint(person); //Recursively call the function to ensure we eat as much as possible.
			}
		}
	}
	
	/**
	 * Determines if a party member is near dying or dead, and alerts the player.
	 * @param person The person who's status we're checking.
	 * @return A string with a message about the health status of the person
	 */
	public String checkHungerStatus(Person person) {
		final int currentHealth = person.getHealth().getCurrent();
		if(currentHealth == 0) {
			return person.getName() + " has died of starvation!";
		}
		else if(person.getHealth().getCurrent() < 2 * getPace().getSpeed()) {
			return person.getName() + " is in danger of starvation.";
		}
		else {
			return null;
		}
	}

	@Override
	public Condition getPartyMembersHealth() {
		int currentSum = 0;
		int maxSum = 0;
		int minSum = 0;
		
		if (members != null && members.size() > 0) {
			for (Person person : members) {
				Condition health = person.getHealth();
				currentSum += health.getCurrent();
				maxSum += health.getMax();
				minSum += health.getMin();
			}
			
			final int size = members.size();
			return new Condition(minSum / size, maxSum / size, currentSum / size);
		}
		
		return null;
	}

	@Override
	public Condition getVehicleStatus() {
		if (vehicle != null) {
			return vehicle.getStatus();
		}
		
		return null;
	}
}
