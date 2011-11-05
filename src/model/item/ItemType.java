package model.item;

import core.ConstantStore;

public enum ItemType {
	APPLE ("APPLE", true, true, false, false),
	BREAD ("BREAD", true, false, false, false),
	AMMO ("AMMO", false, false, false, false),
	GUN ("GUN", false, false, false, false),
	MEAT ("MEAT", true, false, false, false),
	SONIC ("SONIC", false, false, false, false),
	WAGON ("WAGON", false, false, false, false),
	WHEEL ("WHEEL", false, false, false, true),
	OX ("OX", false, false, true, false),
	TOOLS ("TOOLS", false, false, false, true),
	AXLE ("AXLE", false, false, false, true),
	HORSE ("HORSE", false, false, true, false),
	MULE ("MULE", false, false, true, false),
	STRANGEMEAT ("STRANGE_MEAT", true, false, false, false);
	
	private final String name;
	private final String pluralName;

	private final String description;
	
	private final int cost;
	
	private final double weight;
	
	private final boolean isFood, isPlant, isAnimal, isTool;
	
	private final int factor;
	private final int necessaryQuality;
			
	/**
	 * Makes the item type
	 * @param name Name of the item type
	 * @param description Description of the item type
	 * @param cost Cost of the item type
	 * @param weight Weight of the item type
	 * @param isFood If the item is food
	 * @param isPlant If the item is a plant
	 * @param foodFactor The multiplier for the food (if it is one, else 0)
	 */
	private ItemType (String type, boolean isFood, boolean isPlant, boolean isAnimal, boolean isTool) {
		this.name = ConstantStore.get("ITEMS", type + "_NAME");
		this.pluralName = ConstantStore.get("ITEMS", type + "_PLURAL_NAME");
		
		this.description = ConstantStore.get("ITEMS", type + "_DESCRIPTION");
		this.cost = Integer.parseInt(ConstantStore.get("ITEMS", type + "_COST"));
		this.weight = Double.parseDouble(ConstantStore.get("ITEMS", type + "_WEIGHT"));
		this.isFood = isFood;
		this.isPlant = isPlant;
		this.isAnimal = isAnimal;
		this.isTool = isTool;
		if(isFood) {
			this.factor = Integer.parseInt(ConstantStore.get("ITEMS", type + "_FOOD_FACTOR"));
		} else if (isTool){
			this.factor = Integer.parseInt(ConstantStore.get("ITEMS", type + "_REPAIR_FACTOR"));
		} else {
			this.factor = 0;
		}
		if (ConstantStore.get("ITEMS", type + "_NECESSARY_QUALITY") != null) {
			this.necessaryQuality = Integer.parseInt(ConstantStore.get("ITEMS", type + "_NECESSARY_QUALITY"));
		} else {
			this.necessaryQuality = 0;
		}
	}
	
	/**
	 * The name of the type
	 * @return The name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * The plural name of the type
	 * @return The plural name
	 */
	public String getPluralName() {
		return pluralName;
	}
	
	/**
	 * The description of the type
	 * @return The description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * The cost of the type
	 * @return The cost
	 */
	public int getCost() {
		return cost;
	}
	
	/**
	 * The weight of the type
	 * @return The weight
	 */
	public double getWeight() {
		return weight;
	}
	
	/**
	 * Whether or not the item is food
	 * @return Is the item food?
	 */
	public boolean isFood() {
		return isFood;
	}
	
	/**
	 * Whether or not the item is a tool
	 * @return Is the item a tool?
	 */
	public boolean isTool() {
		return isTool;
	}
	
	/**
	 * The multiplier for the food
	 * @return The multiplier
	 */
	public int getFactor() {
		return factor;
	}
	
	/**
	 * Is the item a plant
	 * @return Is it a plant?
	 */
	public boolean isPlant() {
		return isPlant;
	}

	/**
	 * Is the item an animal?
	 * @return Is it an animal
	 */
	public boolean isAnimal() {
		return isAnimal;
	}

	public double getNecessaryQuality() {
		return necessaryQuality;
	}
}
