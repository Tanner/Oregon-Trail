package model;

import java.io.Serializable;

import core.Logger;

/**
 * A condition with a min and max value and also a current value.
 */
@SuppressWarnings("serial")
public class Condition implements Serializable{

	private double min, max, current;
	
	/**
	 * Constructor for Condition.
	 * @param min Minimum value for the condition
	 * @param max Maximum value for the condition
	 * @param current Desired current value for the condition
	 */
	public Condition(double min, double max, double current) {
		if (max < min
				|| current < min
				|| current > max) {
			throw new IllegalArgumentException();
		}
		
		this.min = min;
		this.max = max;
		this.current = current;
	}
	
	/**
	 * Constructor for Condition. Sets the current value to the maximum value.
	 * @param min Minimum value for the condition
	 * @param max Maximum value for the condition
	 */
	public Condition(double min, double max) {
		this(min, max, max);
	}
	
	/**
	 * Constructor for Condition. Sets the minimum value to 
	 * zero and the current value to the maximum value.
	 * @param max Maximum value for the condition
	 */
	public Condition(double max) {
		this(0, max, max);
	}
	
	/**
	 * Get the current value.
	 * @return Current value
	 */
	public double getCurrent() {
		return current;
	}
	
	/**
	 * Get the ratio of the current value to the maximum value.
	 * @return Ratio of the current value to the maximum value
	 */
	public double getPercentage() {
		return (current - min) / (max - min);
	}
	
	/**
	 * Increase the current value by a desired amount.
	 * @param amount Amount to increase the current value
	 */
	public void increase(double amount) {
		if(amount <= 0) {
			Logger.log("Not an increment", Logger.Level.ERROR);
		} else if(current + amount > max) {
			current = max;
			Logger.log("Increment exceeded max - set to max", Logger.Level.WARNING);
		} else {
			current += amount;
		}
	}
	
	/**
	 * Returns the min value.
	 * @return The min value
	 */
	public double getMin() {
		return this.min;
	}
	
	/**
	 * Returns the max value.
	 * @return The max value
	 */
	public double getMax() {
		return this.max;
	}
	
	/**
	 * Decrease the current value by a desired amount.
	 * @param amount Amount to decrease the current value
	 */
	public void decrease (double amount) {
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
