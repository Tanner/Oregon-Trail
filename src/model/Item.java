package model;

import core.ConstantStore;

/**
 * 
 * An item contains it's own condition as well as a 
 * name, description, and weight.  The only modifiable aspect
 * is weight.
 */
public class Item implements Conditioned, Comparable<Item>{
	
	private final Condition status;
	
	private boolean isStackable = true;
	
	private final ITEM_TYPE type;
	
	public enum ITEM_TYPE {
		APPLE ("APPLE", true, true, 1),
		BREAD ("BREAD", true, false, 2),
		BULLET ("BULLET", false, false, 0),
		GUN ("GUN", false, false, 0),
		MEAT ("MEAT", true, false, 4),
		SONIC ("SONIC", false, false, 0),
		WAGON ("WAGON", false, false, 0),
		WHEEL ("WHEEL", false, false, 0);
		
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
		//private ITEM_TYPE (String name, String description, String cost,
			//	String weight, boolean isFood, boolean isPlant, int foodFactor) {
		private ITEM_TYPE (String type, boolean isFood, boolean isPlant, int foodFactor) {
			String reference = type;
			this.name = ConstantStore.get("ITEMS", type + "_NAME");
			this.description = ConstantStore.get("ITEMS", type + "_DESCRIPTION");
			this.cost = Integer.parseInt(ConstantStore.get("ITEMS", type + "_COST"));
			this.weight = Double.parseDouble(ConstantStore.get("ITEMS", type + "_WEIGHT"));
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

	/**
	 * 
	 * Creates a new item with a name, description, status, and weight.
	 * @param type The type of the item
	 */
	public Item(ITEM_TYPE type) {
		this.status = new Condition(100);
		this.type = type;
	}

	/**
	 * Returns the item's name.
	 * @return The item's name
	 */
	public String getName() {
		return type.getName();
	}

	/**
	 * Returns the item's description.
	 * @return The item's description
	 */
	public String getDescription() {
		return type.getDescription();
	}

	/**
	 * Returns the cost of the item.  Cost is the item multiplied
	 * by its current condition. 
	 * @return The base cost of the item
	 */
	public int getCost() {
		return (int) (type.getCost() * status.getPercentage());
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
		return type.getWeight();
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
	
	
}