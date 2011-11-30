package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.item.ItemType;

import component.PartyComponentDataSource;

import core.Logger;

/**
 * Person consists of a list of skills the person is proficient with as well
 * as their name, and what their profession is called.
 */
@SuppressWarnings("serial")
public class Person implements Conditioned, Inventoried, PartyComponentDataSource, Serializable {
	private final Condition skillPoints;
	
	private final Condition health;

	private boolean dead;
	
	private boolean isMale = true;
	
	private final List<Skill> skills = new ArrayList<Skill>();
	
	private String name;
	
	private Profession profession;
		
	private static final int BASE_SKILL_POINTS = 70;
	
	private final Inventory inventory;

	private double weight;
	
	public static final int MAX_INVENTORY_SIZE = 5;
	
	private static final double MAX_INVENTORY_WEIGHT = 100;
	
	/**
	 * Person creation should be done this way always - 
	 * name first, then profession and skills are added.
	 * @param name 
	 */
	public Person(String name){
		this.name = name;
		this.skillPoints = new Condition(0, BASE_SKILL_POINTS, 0);
		this.health = new Condition(100);
		Logger.log(name + " was created", Logger.Level.DEBUG);
		this.inventory = new Inventory(MAX_INVENTORY_SIZE, MAX_INVENTORY_WEIGHT);
		Random random = new Random();
		this.weight = random.nextInt(100) + 90;
	}
	
	/**
	 * Establish this party member's profession
	 * @param profession the desired profession
	 */
	public void setProfession(Profession profession) {
		//This will only happen during party creation when the skills haven't been chosen yet.
		if (this.profession == null) {
			this.profession = profession;
			Logger.log(this.name + " became a " + this.profession, Logger.Level.DEBUG);
			addSkill(profession.getStartingSkill());
			inventory.clear();
			if(profession.getStartingItem() != null) {
				addItemToInventory(new Item(profession.getStartingItem()));
			}
			return;
		} else if (this.profession == profession) {
			Logger.log(this.name + " is already a " + profession, Logger.Level.DEBUG);
			return;
		} else if (this.profession != profession) {
			this.skills.clear();
			Logger.log(this.name + " stopped being a " + 
					this.profession + " and lost all current skills", Logger.Level.DEBUG);
			this.profession = null;
			setProfession(profession);
			inventory.clear();
			if(profession.getStartingItem() != null) {
				addItemToInventory(new Item(profession.getStartingItem()));
			}
			return;
		} else {
			Logger.log("Don't know what happened", Logger.Level.DEBUG);
			return;
		}
	}

	/**
	 * Gets remaining skill points for consideration during skill adding
	 * @return Remaining skill points
	 */
	public int getSkillPoints() {
		return (int) skillPoints.getCurrent();
	}
	
	/**
	 * Clears the persons skills
	 */
	public void clearSkills() {
		for (int i = skills.size() - 1; i >= 0; i--) {
			Skill skill = skills.get(i);
			if (!skill.equals(profession.getStartingSkill())) {
				skills.remove(skill);
			}
		}
	}
	
	/**
	 * Adds a new skill to the person
	 * @param newSkill The skill to add
	 */
	public void addSkill(Skill newSkill) {
		if(skills.contains(newSkill)) {
			Logger.log(this.name + " already has the skill " 
					+ newSkill, Logger.Level.DEBUG);
			return;
		} else if(newSkill == this.profession.getStartingSkill()) {
			skills.add(newSkill);
			Logger.log("As a " + this.profession + " " + 
					this.name + " gains the skill " + newSkill, Logger.Level.DEBUG);
			return;
		} else {
			skills.add(newSkill);
			Logger.log(this.name + " gained the skill " + newSkill, Logger.Level.DEBUG);
			return;
		}
	}

	/**
	 * Adds a skill to this person's skill set
	 * @param newSkill skill to be added
	 */
	public void buySkill(Skill newSkill){
		if(skills.contains(newSkill)) {
			Logger.log(this.name + " already has the skill " + 
					newSkill, Logger.Level.DEBUG);
			return;
		}
		else {
			if(skillPoints.getCurrent() < newSkill.getCost()) {
				Logger.log(this.name + " does not have enough skill points to obtain " + 
						newSkill + ".  Current skill points: " + 
						skillPoints.getCurrent() + " Cost of new skill: " + 
						newSkill.getCost(), Logger.Level.DEBUG);
			}
			else {
				skillPoints.decrease(newSkill.getCost());
				skills.add(newSkill);
				Logger.log(this.name + " gained the skill " +
						newSkill, Logger.Level.DEBUG);
				Logger.log(this.name + " has " + 
						skillPoints.getCurrent() + " skill points remaning.",
						Logger.Level.DEBUG);
				return;
			}
		}
		return;
	}
	
	/**
	 * Removes skill from person's skill set	
	 * @param oldSkill skill to be removed
	 */
	public void removeSkill(Skill oldSkill) {
		if (!this.skills.contains(oldSkill)) {
			Logger.log("Cannot remove a skill that isn't already known.",
					Logger.Level.DEBUG);
			return;
		}
		else if (this.profession.getStartingSkill() == oldSkill) {
			Logger.log("Cannot remove profession's starting skill", Logger.Level.DEBUG);
			return;
		}
		else {
			this.skills.remove(oldSkill);
			this.skillPoints.increase(oldSkill.getCost());
			return;
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
	
	/**
	 * Sets the name of the person
	 * @param name The name of the person
	 */
	public void setName(String name) {
		this.name = name;
		Logger.log("Name changed to " + name, Logger.Level.DEBUG);
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
		final StringBuffer str = new StringBuffer("Name: " + name);
		if(profession != null) {
			 str.append(", Profession: " + profession.getName());
		}
		for(Skill skill : skills) {
			str.append(", Skill: " + skill.getName());
		}
		str.append((isMale ? ", Gender: Male" : ", Gender: Female"));
		return str.toString();
	}
	
	/**
	 * Sets the gender of the person
	 * @param isMale Whether this Person is male or not
	 */
	
	public void setIsMale(boolean isMale) {
		this.isMale = isMale;
		
		Logger.log("Gender changed to " + 
				(isMale ? "Male" : "Female"), Logger.Level.DEBUG);
	}
	
	/**
	 * Returns this person's gender
	 * @return Whether or not this Person is male
	 */
	
	public boolean isMale() {
		return isMale;
	}
	
	@Override
	public Inventory getInventory() {
		return inventory;
	}
	
	@Override
	public void addItemsToInventory(List<Item> items) {
		inventory.addItemsToInventory(items);
	}
	
	@Override
	public void addItemToInventory(Item item) {
		inventory.addItemToInventory(item);
	}

	@Override
	public List<Item> removeItemFromInventory(ItemType itemIndex, int quantity) {
		return inventory.removeItemFromInventory(itemIndex, quantity);
	}
	
	/**
	 * Returns the health condition for status bar.
	 * @return The condition 'health'
	 */
	public Condition getHealth() {
		return health;
	}
	
	/**
	 * Decreases health by amount
	 * @param amount The amount
	 */
	public void decreaseHealth(int amount) {
		health.decrease(amount);
	}
	
	/**
	 * Increases health by amount
	 * @param amount The amount
	 */
	public void increaseHealth(int amount) {
		health.increase(amount);
	}
	
	@Override
	public double getConditionPercentage() {
		return health.getPercentage();
	}
	
	@Override
	public boolean canGetItem(ItemType itemType, int numberOf) {
		return inventory.canGetItems(itemType, numberOf);
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

	public void increaseSkillPoints(int amount) {
		skillPoints.increase(amount);
		
	}

	public List<Item> killForFood() {
		int numberOf = (int) (this.weight / ItemType.STRANGEMEAT.getWeight());
		List<Item> itemList = new ArrayList<Item>();
		for(int i = 0; i < (numberOf / 2); i++) {
			itemList.add(new Item(ItemType.STRANGEMEAT));
		}
		return itemList;
	}

	@Override
	public Condition getCondition() {
		return health;
	}

	@Override
	public boolean isDead() {
		return dead;
	}
	
	public void setDead(boolean dead) {
		this.dead = dead;
	}
}
