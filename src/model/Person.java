package model;

import java.util.ArrayList;
import java.util.List;

import core.Logger;

/**
 * Person consists of a list of skills the person is proficient with as well
 * as their name, and what their profession is called.
 */
public class Person implements Conditioned, Inventoried{
	
	private final Condition skillPoints;
	
	private final Condition health;
	
	private boolean isMale = true;
	
	private final List<Skill> skills = new ArrayList<Skill>();
	
	private String name;
	
	private Profession profession;
	
	private static final float BASE_MONEY = 1600f;
	
	private static final int BASE_SKILL_POINTS = 70;
	
	private final Inventory inventory;
	
	public static final int MAX_INVENTORY_SIZE = 5;
	
	private static final double MAX_INVENTORY_WEIGHT = 10;
	
	/**
	 * Person creation should be done this way always - 
	 * name first, then profession and skills are added.
	 * @param name 
	 */
	public Person(String name){
		this.name = name;
		this.skillPoints = new Condition(0, BASE_SKILL_POINTS, 0);
		this.health = new Condition(100);
		Logger.log(name + " was created", Logger.Level.INFO);
		this.inventory = new Inventory(MAX_INVENTORY_SIZE, MAX_INVENTORY_WEIGHT);
	}
	
	/**
	 * Establish this party member's profession
	 * @param profession the desired profession
	 * @return successful completion of operation
	 */
	public boolean changeProfession(Profession profession) {
		//This will only happen during party creation when the skills haven't been chosen yet.
		if (this.profession == null) {
			this.profession = profession;
			Logger.log(this.name + " became a " + this.profession, Logger.Level.INFO);
			addSkill(profession.getStartingSkill());
			return true;
		} else if (this.profession == profession) {
			Logger.log(this.name + " is already a " + profession, Logger.Level.INFO);
			return false;
		} else if (this.profession != profession) {
			this.skills.clear();
			Logger.log(this.name + " stopped being a " + 
					this.profession + " and lost all current skills", Logger.Level.INFO);
			this.profession = null;
			changeProfession(profession);
			return true;
		} else {
			Logger.log("Don't know what happened", Logger.Level.INFO);
			return false;
		}
	}

	/**
	 * Gets remaining skill points for consideration during skill adding
	 * @return Remaining skill points
	 */
	public int getSkillPoints() {
		return skillPoints.getCurrent();
	}
	
	/**
	 * Clears the persons skills
	 * @return True is successful, false otherwise
	 */
	public boolean clearSkills() {
		for (int i = skills.size() - 1; i >= 0; i--) {
			Skill skill = skills.get(i);
			if (!skill.equals(profession.getStartingSkill())) {
				skills.remove(skill);
			}
		}
		return true;
	}
	
	public boolean addSkill(Skill newSkill) {
		if(skills.contains(newSkill)) {
			Logger.log(this.name + " already has the skill " 
					+ newSkill, Logger.Level.INFO);
			return false;
		}
		else if(newSkill == this.profession.getStartingSkill()) {
			skills.add(newSkill);
			Logger.log("As a " + this.profession + " " + 
					this.name + " gains the skill " + newSkill, Logger.Level.INFO);
			return true;
		}
		else {
			skills.add(newSkill);
			Logger.log(this.name + " gained the skill " + newSkill, Logger.Level.INFO);
			return true;
		}
	}

	/**
	 * Adds a skill to this person's skill set
	 * @param newSkill skill to be added
	 * @return successful completion of this operation
	 */
	public boolean buySkill(Skill newSkill){
		if(skills.contains(newSkill)) {
			Logger.log(this.name + " already has the skill " + 
					newSkill, Logger.Level.INFO);
			return false;
		}
		else {
			if(skillPoints.getCurrent() < newSkill.getCost()) {
				Logger.log(this.name + " does not have enough skill points to obtain " + 
						newSkill + ".  Current skill points: " + 
						skillPoints.getCurrent() + " Cost of new skill: " + 
						newSkill.getCost(), Logger.Level.INFO);
			}
			else {
				skillPoints.decrease(newSkill.getCost());
				skills.add(newSkill);
				Logger.log(this.name + " gained the skill " +
						newSkill, Logger.Level.INFO);
				Logger.log(this.name + " has " + 
						skillPoints.getCurrent() + " skill points remaning.",
						Logger.Level.INFO);
				return true;
			}
		}
		return true;
	}
	
	/**
	 * Removes skill from person's skill set	
	 * @param oldSkill skill to be removed
	 * @return Successful completion of operation
	 */
	public boolean removeSkill(Skill oldSkill) {
		if (!this.skills.contains(oldSkill)) {
			Logger.log("Cannot remove a skill that isn't already known.",
					Logger.Level.INFO);
			return false;
		}
		else if (this.profession.getStartingSkill() == oldSkill) {
			Logger.log("Cannot remove profession's starting skill", Logger.Level.INFO);
			return false;
		}
		else {
			this.skills.remove(oldSkill);
			this.skillPoints.increase(oldSkill.getCost());
			return true;
		}
	}
	
	/**
	 * Gets the list of skills a person has.
	 * @return The list of skills
	 */
	public List<Skill> getSkills() {
		return skills;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
		Logger.log("Name changed to " + name, Logger.Level.INFO);
	}
	
	/**
	 * Profession of person in party
	 * @return Person's profession
	 */
	public Profession getProfession(){
		return profession;
	}

	@Override
	public String toString() {
		String str = "Name: " + name;
		if(profession != null) {
			 str += ", Profession: " + profession.getName();
		}
		for(Skill skill : skills) {
			str += ", Skill: " + skill.getName();
		}
		str+= (isMale ? ", Gender: Male" : ", Gender: Female");
		return str;
	}
	
	/**
	 * Skills named and with skill point cost.
	 * 
	 */
	public enum Skill {
		MEDICAL (50, "Medical"),
		RIVERWORK (50, "Riverwork"),
		SHARPSHOOTING (50, "Sharpshooting"),
		BLACKSMITHING (40, "Blacksmithing"),
		CARPENTRY (40, "Carpentry"),
		FARMING (40, "Farming"),
		TRACKING (30, "Tracking"),
		BOTANY (20, "Botany"),
		COMMERCE (20, "Commerce"),
		COOKING (20, "Cooking"),
		MUSICAL (10, "Musical"),
		SEWING (10, "Sewing"),
		SPANISH (10, "Spanish"),
		NONE (0, "");
		
		private final int cost;

		private final String name;
		
		/**
		 * Sets the cost of the skill as designated
		 * @param cost The cost of the skill (defined above)
		 */
		private Skill(int cost, String name) {
			this.cost = cost;
			this.name = name;
		}
		
		/**
		 * Returns the cost of a skill
		 * @return The cost
		 */
		public int getCost() {
			return cost;
		}
		
		/**
		 * Return the name of a skill
		 * @return The skill name
		 */
		public String getName(){
			return name;
		}
	}
	
	/**
	 * Professions with their gold/score multiplier and starting skill
	 * 
	 */
	public static enum Profession {
		BANKER (1, Skill.COMMERCE, "Banker"),
		DOCTOR (1.2f, Skill.MEDICAL, "Doctor"),
		MERCHANT (1.4f, Skill.COMMERCE, "Merchant"),
		PHARMACIST (1.6f, Skill.MEDICAL, "Pharmacist"),
		WAINWRIGHT (1.8f, Skill.CARPENTRY, "Wainwright"),
		GUNSMITH (2, Skill.SHARPSHOOTING, "Gunsmith"),
		BLACKSMITH (2.2f, Skill.BLACKSMITHING, "Blacksmith"),
		MASON (2.4f, Skill.CARPENTRY, "Mason"),
		WHEELWRIGHT (2.6f, Skill.CARPENTRY, "Wheelwright"),
		CARPENTER (2.8f, Skill.CARPENTRY, "Carpenter"),
		SADDLEMAKER (3, Skill.FARMING, "Saddlemaker"),
		BRICKMAKER (3.2f, Skill.CARPENTRY, "Brickmaker"),
		PROSPECTOR (3.4f, Skill.RIVERWORK, "Prospector"),
		TRAPPER (3.6f, Skill.TRACKING, "Trapper"),
		SURVEYOR (3.8f, Skill.RIVERWORK, "Surveyor"),
		SHOEMAKER (4, Skill.SEWING, "Shoemaker"),
		JOURNALIST (4.1f, Skill.NONE, "Journalist"),
		PRINTER (4.2f, Skill.COMMERCE, "Printer"),
		BUTCHER (4.3f, Skill.COOKING, "Butcher"),
		BAKER (4.4f, Skill.COOKING, "Baker"),
		TAILOR (4.5f, Skill.SEWING, "Tailor"),
		FARMER (4.5f, Skill.FARMING, "Farmer"),
		PASTOR (4.6f, Skill.NONE, "Pastor"),
		ARTIST (4.8f, Skill.MUSICAL, "Artist"),
		TEACHER (5, Skill.NONE, "Teacher");
		
		private final float moneyDivider;

		private final Skill startingSkill;
		
		private final String name;
		
		/**
		 * Assigns money multiplier and starting skill to each profession
		 * @param moneyDivider Gold divided by and score multiplied by this 
		 * @param startSkill Skill the profession starts with
		 */
		private Profession(float moneyDivider, Skill startingSkill, String name) {
			this.moneyDivider = moneyDivider;
			this.startingSkill = startingSkill;
			this.name = name;
		}
		
		/**
		 * Returns the multiplier
		 * @return Multiplier
		 */
		public int getMoney() {
			return (int) (BASE_MONEY / moneyDivider);
		}
		
		/**
		 * Returns starting skill
		 * @return Starting skill
		 */
		public Skill getStartingSkill() {
			return startingSkill;
		}
		
		/**
		 * Returns the profession's name
		 * @return Profession's name
		 */
		public String getName(){
			return name;
		}
	}
	
	/**
	 * Sets the gender of the person
	 * @param isMale Whether this Person is male or not
	 */
	
	public void setIsMale(boolean isMale) {
		this.isMale = isMale;
		
		Logger.log("Gender changed to " + 
				(isMale ? "Male" : "Female"), Logger.Level.INFO);
	}
	
	/**
	 * Returns this person's gender
	 * @return Whether or not this Person is male
	 */
	
	public boolean getIsMale() {
		return isMale;
	}
	
	@Override
	public Inventory getInventory() {
		return inventory;
	}
	
	@Override
	public boolean addItemsToInventory(List<Item> items) {
		return inventory.addItem(items);
	}
	
	@Override
	public boolean addItemToInventory(Item item) {
		final List<Item> itemToAdd = new ArrayList<Item>();
		itemToAdd.add(item);
		return inventory.addItem(itemToAdd);
	}

	@Override
	public List<Item> removeItemFromInventory(Item.ITEM_TYPE itemIndex, int quantity) {
		return inventory.removeItem(itemIndex, quantity);
	}
	
	/**
	 * Returns the health condition for status bar.
	 * @return The condition 'health'
	 */
	public Condition getHealth() {
		return health.copy();
	}
	
	/**
	 * Decreases health by amount
	 * @param amount The amount
	 * @return True if successful
	 */
	public boolean decreaseHealth(int amount) {
		return health.decrease(amount);
	}
	
	/**
	 * Increases health by amount
	 * @param amount The amount
	 * @return True if successful
	 */
	public boolean increaseHealth(int amount) {
		return health.increase(amount);
	}
	
	@Override
	public double getConditionPercentage() {
		return health.getPercentage();
	}
	
	@Override
	public boolean canGetItem(Item.ITEM_TYPE itemType, int numberOf) {
		return inventory.canAddItems(itemType, numberOf);
	}

	@Override
	public int getMaxSize() {
		return MAX_INVENTORY_SIZE;
	}
	
	@Override
	public double getMaxWeight() {
		return MAX_INVENTORY_WEIGHT;
	}
	
	@Override
	public double getWeight() {
		return inventory.getWeight();
	}
}
