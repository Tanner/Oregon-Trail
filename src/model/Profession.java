package model;

import model.item.ItemType;

/**
 * Professions with their gold/score multiplier and starting skill
 * 
 */
public enum Profession {
	BANKER (1, Skill.COMMERCE, "Banker", null),
	DOCTOR (1.2f, Skill.MEDICAL, "Doctor", null),
	MERCHANT (1.4f, Skill.COMMERCE, "Merchant", null),
	PHARMACIST (1.6f, Skill.MEDICAL, "Pharmacist", null),
	WAINWRIGHT (1.8f, Skill.CARPENTRY, "Wainwright", ItemType.WHEEL),
	GUNSMITH (2, Skill.SHARPSHOOTING, "Gunsmith", ItemType.GUN),
	BLACKSMITH (2.2f, Skill.BLACKSMITHING, "Blacksmith", null),
	MASON (2.4f, Skill.CARPENTRY, "Mason", null),
	WHEELWRIGHT (2.6f, Skill.CARPENTRY, "Wheelwright", ItemType.WHEEL),
	CARPENTER (2.8f, Skill.CARPENTRY, "Carpenter", ItemType.WHEEL),
	SADDLEMAKER (3, Skill.FARMING, "Saddlemaker", null),
	BRICKMAKER (3.2f, Skill.CARPENTRY, "Brickmaker", null),
	PROSPECTOR (3.4f, Skill.RIVERWORK, "Prospector", null),
	TRAPPER (3.6f, Skill.TRACKING, "Trapper", null),
	SURVEYOR (3.8f, Skill.RIVERWORK, "Surveyor", null),
	SHOEMAKER (4, Skill.SEWING, "Shoemaker", null),
	JOURNALIST (4.1f, Skill.NONE, "Journalist", null),
	PRINTER (4.2f, Skill.COMMERCE, "Printer", null),
	BUTCHER (4.3f, Skill.COOKING, "Butcher", ItemType.MEAT),
	BAKER (4.4f, Skill.COOKING, "Baker", ItemType.BREAD),
	TAILOR (4.5f, Skill.SEWING, "Tailor", null),
	FARMER (4.5f, Skill.FARMING, "Farmer", ItemType.APPLE),
	PASTOR (4.6f, Skill.NONE, "Pastor", null),
	ARTIST (4.8f, Skill.MUSICAL, "Artist", null),
	TEACHER (5, Skill.NONE, "Teacher", null);
	
	private final float moneyDivider;

	private final Skill startingSkill;
	
	private final String name;
	
	private final ItemType startingItem;
	
	private static final float BASE_MONEY = 1600f;
	
	/**
	 * Assigns money multiplier and starting skill to each profession
	 * @param moneyDivider Gold divided by and score multiplied by this 
	 * @param startingSkill Skill the profession starts with
	 * @param name The profession's name
	 */
	private Profession(float moneyDivider, Skill startingSkill, String name, ItemType itemType) {
		this.moneyDivider = moneyDivider;
		this.startingSkill = startingSkill;
		this.name = name;
		this.startingItem = itemType;
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
	
	public ItemType getStartingItem() {
		return startingItem;
	}
	
	/**
	 * Returns the profession's name
	 * @return Profession's name
	 */
	public String getName(){
		return name;
	}
		
	public String toString() {
		return name;
	}
}

