package model;

import core.Logger;

/**
 * A condition with a min and max value and also a current value.
 * 
 * @author George Johnston
 */
public class Condition {
	private int min, max, current;
	
	/**
	 * Constructor for Condition.
	 * @param min Minimum value for the condition
	 * @param max Maximum value for the condition
	 * @param current Desired current value for the condition
	 */
	public Condition(int min, int max, int current) {
		this.min = min;
		this.max = max;
		this.current = max;
	}
	
	/**
	 * Constructor for Condition. Sets the current value to the maximum value.
	 * @param min Minimum value for the condition
	 * @param max Maximum value for the condition
	 */
	public Condition(int min, int max) {
		this(min, max, max);
	}
	
	/**
	 * Constructor for Condition. Sets the minimum value to zero and the current value to the maximum value.
	 * @param max Maximum value for the condition
	 */
	public Condition(int max) {
		this(0, max, max);
	}
	
	/**
	 * Get the current value.
	 * @return Current value
	 */
	public int getCurrent() {
		return current;
	}
	
	/**
	 * Get the ratio of the current value to the maximum value.
	 * @return Ratio of the current value to the maximum value
	 */
	public double getPercentage() {
		return ((double) current) / max;
	}
	
	/**
	 * Increase the current value by a desired amount.
	 * @param amount Amount to increase the current value
	 * @return Success status (true for success, false for fail)
	 */
	public boolean increase(int amount) {
		if(amount <= 0) {
			Logger.log("Not an increment", Logger.Level.ERROR);
			return false;
		}
		else if(current + amount > max) {
			current = max;
			Logger.log("Increment exceeded max - set to max", Logger.Level.WARNING);
			return true;
		}
		else {
			current+= amount;
			return true;
		}
	}
	
	/**
	 * Decrease the current value by a desired amount.
	 * @param amount Amount to decrease the current value
	 * @return Success status (true for success, false for fail)
	 */
	public boolean decrease (int amount) {
		if(amount <= 0) {
			Logger.log("Not an decrement", Logger.Level.ERROR);
			return false;
		}
		else if(current - amount < min) {
			current = min;
			Logger.log("Decrement exceeded min - set to min", Logger.Level.WARNING);
			return true;
		}
		else {
			current-= amount;
			return true;
		}
	}
}