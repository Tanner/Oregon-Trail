package model;

import core.ConstantStore;

/**
 * 
 * An item contains it's own condition as well as a 
 * name, description, and weight.  The only modifiable aspect
 * is weight.
 */
public abstract class Item implements Conditioned, Comparable<Item>{
	
	private final String name;

	private final String description;
	
	private final Condition status;
	
	private final double weight; //This is the individual unit weight
	
	private boolean isStackable = true;
	
	private final int baseCost;
	
	private final ITEM_TYPE type;
	
	/**
	 * 
	 * Creates a new item with a name, description, status, and weight.
	 * @param name The item's name
	 * @param description The item's description
	 * @param status The item's status
	 * @param weight The item's weight
	 * @param baseCost The base cost of the item
	 * @param type The type of the item
	 */
	public Item(String name, String description, Condition status, 
			double weight, int baseCost, ITEM_TYPE type) {
		this.name = name;
		this.description = description;
		this.status = status;
		this.weight = weight;
		this.baseCost = baseCost;
		this.type = type;
	}

	/**
	 * Returns the item's name.
	 * @return The item's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the item's description.
	 * @return The item's description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the cost of the item.  Cost is the item multiplied
	 * by its current condition. 
	 * @return The base cost of the item
	 */
	public int getCost() {
		return (int) (baseCost * status.getPercentage());
	}
	
	/**
	 * Returns the current condition of the item.
	 * @return The current condition of the item.
	 */
	public Condition getStatus() {
		return status.copy();
	}
	
	@Override
	public double getConditionPercentage() {
		return status.getPercentage();
	}

	/**
	 * Increases the item's status by a specific amount. 
	 * Returns false if the increase fails.
	 * @param amount The amount by which to increase the status
	 * @return True if successful, false otherwise
	 */
	public void increaseStatus(int amount) {
		status.increase(amount);
		if (status.getCurrent() == status.getMax()) {
			this.isStackable = true;
		}
	}
	
	/**
	 * Decreases the item's status by a specific amount.  
	 * Returns false if the decrease fails.
	 * @param amount The amount by which to decrease the status
	 * @return True if successful, false otherwise
	 */
	public void decreaseStatus(int amount) {
		status.decrease(amount);
		if (status.getCurrent() < status.getMax()) {
			this.isStackable = false;
		}
	}
	
	/**
	 * Returns the weight of the item.
	 * @return The weight of the item
	 */
	public double getWeight() {
		return weight;
	}
	
	/**
	 * Whether or not the item can be stacked.
	 * @return True if it can be stacked, false otherwise.
	 */
	public boolean isStackable() {
		return isStackable;
	}
	
	@Override
	public int compareTo(Item i) {
		if (Math.abs(getConditionPercentage() - i.getConditionPercentage()) < 0.000001) {
			return 0;
		} else if (getConditionPercentage() < i.getConditionPercentage()) {
			return -1;
		} else {
			return 1;
		}
	}
	
	/**
	 * Returns the item type
	 * @return The item type
	 */
	public ITEM_TYPE getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return type.getName();
	}
	
	public enum ITEM_TYPE {
		APPLE (ConstantStore.get("ITEMS", "APPLE_NAME"), 
			  ConstantStore.get("ITEMS", "APPLE_DESCRIPTION"), 
			  ConstantStore.get("ITEMS", "APPLE_COST"), 
			  ConstantStore.get("ITEMS", "APPLE_WEIGHT"), true, true, 1),
		BREAD (ConstantStore.get("ITEMS", "BREAD_NAME"), 
			  ConstantStore.get("ITEMS", "BREAD_DESCRIPTION"),
			  ConstantStore.get("ITEMS", "BREAD_COST"),
			  ConstantStore.get("ITEMS", "BREAD_WEIGHT"), true, false, 2),
		BULLET (ConstantStore.get("ITEMS", "BULLET_NAME"), 
			  ConstantStore.get("ITEMS", "BULLET_DESCRIPTION"),
			  ConstantStore.get("ITEMS", "BULLET_COST"),
			  ConstantStore.get("ITEMS", "BULLET_WEIGHT"), false, false, 0),
		GUN (ConstantStore.get("ITEMS", "GUN_NAME"), 
			ConstantStore.get("ITEMS", "GUN_DESCRIPTION"),
			ConstantStore.get("ITEMS", "GUN_COST"),
			ConstantStore.get("ITEMS", "GUN_WEIGHT"), false, false, 0),
		MEAT (ConstantStore.get("ITEMS", "MEAT_NAME"), 
			  ConstantStore.get("ITEMS", "MEAT_DESCRIPTION"),
			  ConstantStore.get("ITEMS", "MEAT_COST"),
			  ConstantStore.get("ITEMS", "MEAT_WEIGHT"), true, false, 4),
		SONIC (ConstantStore.get("ITEMS", "SONIC_NAME"), 
			  ConstantStore.get("ITEMS", "SONIC_DESCRIPTION"),
			  ConstantStore.get("ITEMS", "SONIC_COST"),
			  ConstantStore.get("ITEMS", "SONIC_WEIGHT"), false, false, 0),
		WAGON (ConstantStore.get("ITEMS", "WAGON_NAME"), 
			  ConstantStore.get("ITEMS", "WAGON_DESCRIPTION"),
			  ConstantStore.get("ITEMS", "WAGON_COST"),
			  ConstantStore.get("ITEMS", "WAGON_WEIGHT"), false, false, 0),
		WHEEL (ConstantStore.get("ITEMS", "WHEEL_NAME"), 
			  ConstantStore.get("ITEMS", "WHEEL_DESCRIPTION"),
			  ConstantStore.get("ITEMS", "WHEEL_COST"),
			  ConstantStore.get("ITEMS", "WHEEL_WEIGHT"), false, false, 0);
		
		private final String name;

		private final String description;
		
		private final int cost;
		
		private final double weight;
		
		private final boolean isFood, isPlant;
		
		private final int foodFactor;
		
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
		private ITEM_TYPE (String name, String description, String cost,
				String weight, boolean isFood, boolean isPlant, int foodFactor) {
			this.name = name;
			this.description = description;
			this.cost = Integer.parseInt(cost);
			this.weight = Double.parseDouble(weight);
			this.isFood = isFood;
			this.isPlant = isPlant;
			this.foodFactor = foodFactor;
		}
		
		/**
		 * The name of the type
		 * @return The name
		 */
		public String getName() {
			return name;
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
		 * The multiplier for the food
		 * @return The multiplier
		 */
		public int getFoodFactor() {
			return foodFactor;
		}
		
		/**
		 * Is the item a plant
		 * @return Is it a plant?
		 */
		public boolean isPlant() {
			return isPlant;
		}
	}
	
	
}