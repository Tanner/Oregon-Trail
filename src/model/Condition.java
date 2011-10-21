package model;

import core.Logger;

/**
 * A condition with a min and max value and also a current value.
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
		this.current = current;
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
	 * Constructor for Condition. Sets the minimum value to 
	 * zero and the current value to the maximum value.
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
		return (double) (current - min) / (double) (max - min);
	}
	
	/**
	 * Increase the current value by a desired amount.
	 * @param amount Amount to increase the current value
	 */
	public void increase(int amount) {
		if(amount <= 0) {
			Logger.log("Not an increment", Logger.Level.ERROR);
			return;
		}
		else if(current + amount > max) {
			current = max;
			Logger.log("Increment exceeded max - set to max", Logger.Level.WARNING);
			return;
		}
		else {
			current += amount;
			return;
		}
	}
	
	/**
	 * Returns the min value.
	 * @return The min value
	 */
	public int getMin() {
		return this.min;
	}
	
	/**
	 * Returns the max value.
	 * @return The max value
	 */
	public int getMax() {
		return this.max;
	}
	
	/**
	 * Decrease the current value by a desired amount.
	 * @param amount Amount to decrease the current value
	 */
	public void decrease (int amount) {
		if(amount <= 0) {
			Logger.log("Not a decrement", Logger.Level.ERROR);
			return;
		}
		else if(current - amount < min) {
			current = min;
			Logger.log("Decrement exceeded min - set to min", Logger.Level.WARNING);
			return;
		}
		else {
			current -= amount;
			return;
		}
	}
	
	/***
	 * Returns a copy of the current condition for use in analysis.
	 * @return A copy of the condition
	 */
	public Condition copy() {
		return new Condition(this.min, this.max, this.current);
	}
	
	@Override
	public String toString() {
		return "Min: " + min + ", Max: " + max + ", Current: " + current;
	}
}
