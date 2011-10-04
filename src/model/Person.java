package model;

/*
TO-DO List:
Skillpoints to Condition
Remove skill method
Gender availability
setName?
change profession wipes (clear skill tree, reset skill points)
add/remove single skill
set party with full party
*/

import java.util.ArrayList;

import core.Logger;

/**
 * Person consists of a list of skills the person is proficient with as well
 * as their name, and what their profession is called. Potentially more, but not
 * right now
 * 
 * @author George Johnston
 */
public class Person {
	private Condition skillPoints;
	private boolean isMale;
	private ArrayList<Skill> skills = new ArrayList<Skill>();
	private String name;
	private Profession profession;
	private final static float baseMoney = 1600f;
	
	/**
	 * Person creation should be done this way always - name first, then profession and skills are added.
	 * @param name
	 */
	public Person(String name){
		this.name = name;
		this.skillPoints = new Condition(0, 120, 120);
	}
	
	public boolean setProfession(Profession profession) {
		//This will only happen during party creation when the skills haven't been chosen yet.
		if(this.profession == null) {
			this.profession = profession;
			Logger.log(this.name + " became a " + this.profession, Logger.Level.INFO);
			addSkill(profession.getStartingSkill());
			return true;
		}
		else if(this.profession == profession) {
			Logger.log(this.name + " is already a " + profession, Logger.Level.INFO);
			return false;
		}
		else if (this.profession != profession) {
			this.skills.clear();
			Logger.log(this.name + " stopped being a " + this.profession + " and loses all current skills", Logger.Level.INFO);
			this.profession = null;
			setProfession(profession);
			return true;
		}
		else {
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
	
	public boolean addSkill(Skill newSkill){
		if(skills.contains(newSkill)) {
			Logger.log(this.name + " already has the skill " + newSkill, Logger.Level.INFO);
			return false;
		}
		else if(newSkill == this.profession.getStartingSkill()) {
			skills.add(newSkill);
			Logger.log("As a " + this.profession + " " + this.name + " gains the skill " + newSkill, Logger.Level.INFO);
			return true;
		}
		else {
			if(skillPoints.getCurrent() < newSkill.getCost()) {
				Logger.log(this.name + " does not have enough skill points.  Current skill points: " + skillPoints.getCurrent() + " Cost of new skill: " + newSkill.getCost(), Logger.Level.INFO);
			}
			else {
				skillPoints.decrease(newSkill.getCost());
				skills.add(newSkill);
				Logger.log(this.name + " gained the skill " + newSkill, Logger.Level.INFO);
				Logger.log(this.name + " has " + skillPoints.getCurrent() + " skill points remaning.", Logger.Level.INFO);
				return true;
			}
		}
		return true;
	}
	
		
	/**
	 * Gets the list of skills a person has.
	 * @return The list of skills
	 */
	public ArrayList<Skill> getSkills() {
		return skills;
	}
	
	/**
	 * I FORGOT MY NAME!
	 * @return Person's name
	 */
	public String getName() {
		return name;
	}
	
	public Profession getProfession(){
		return profession;
	}
	
	/**
	 * Skills named and with skill point cost.
	 * 
	 * @author George Johnston
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
		Skill(int cost, String name) {
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
		
		public String getName(){
			return name;
		}
	}
	
	/**
	 * Professions with their gold/score multiplier and starting skill
	 * 
	 * @author George Johnston
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
			return (int)(baseMoney / moneyDivider);
		}
		
		/**
		 * Returns starting skill
		 * @return Starting skill
		 */
		public Skill getStartingSkill() {
			return startingSkill;
		}
		
		public String getName(){
			return name;
		}
	}
}
