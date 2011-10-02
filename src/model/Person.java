package model;

import java.util.ArrayList;

/**
 * Person consists of a list of skills the person is proficient with as well
 * as their name, and what their profession is called. Potentially more, but not
 * right now
 * 
 * @author George Johnston
 */
public class Person {
	private int skillPoints = 120;
	private ArrayList<Skill> skills = new ArrayList<Skill>();
	private String name;
	private Profession profession;
	
	/**
	 * Constructor
	 * 
	 * @param name The name of the person
	 * @param profession The person's profession
	 * @param skills The list of skills selected during person creation
	 */
	public Person(String name, Profession profession, Skill ... skills) {
		
		this.name = name;
		this.profession = profession;
		
		for (Skill skill: skills) {
			if(!this.skills.contains(skill)) {
				this.skills.add(skill);
				this.skillPoints -= skill.getCost();
			}
		}
		
		if (!this.skills.contains(profession.getStartingSkill())) {
			this.skills.add(profession.getStartingSkill());
			this.skillPoints -= profession.getStartingSkill().getCost();
		}
	}
	
	/**
	 * Gets remaining skill points for consideration during skill adding
	 * @return Remaining skill points
	 */
	public int getSkillPoints() {
		return skillPoints;
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
		Profession(float moneyDivider, Skill startingSkill, String name) {
			this.moneyDivider = moneyDivider;
			this.startingSkill = startingSkill;
			this.name = name;
		}
		
		/**
		 * Returns the multiplier
		 * @return Multiplier
		 */
		public float getMoneyDivider() {
			return moneyDivider;
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
