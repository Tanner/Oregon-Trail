package model;
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
	 * @param name The name of the skill
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
	
	public String toString() {
		return name;
	}
}