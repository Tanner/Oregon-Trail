package model;

import core.Logger;

public class Condition {

	private int min, max, current;
	
	Condition(int min, int max, int current) {
		this.min = min;
		this.max = max;
		this.current = max;
	}
	
	Condition(int min, int max) {
		this(min, max, max);
	}
	
	Condition(int max) {
		this(0, max, max);
	}
	
	public int getCurrent() {
		return current;
	}
	
	public double getPercentage() {
		return (double)current / max;
	}
	
	public boolean increase(int amount) {
		if(amount <= 0) {
			Logger.log("Not an increment", Logger.Level.INFO);
			return false;
		}
		else if(current + amount > max) {
			current = max;
			Logger.log("Increment exceeded max - set to max", Logger.Level.INFO);
			return true;
		}
		else {
			current+= amount;
			return true;
		}
	}
	
	public boolean decrease (int amount) {
		if(amount <= 0) {
			Logger.log("Not an decrement", Logger.Level.INFO);
			return false;
		}
		else if(current + amount > max) {
			current = min;
			Logger.log("Decrement exceeded min - set to min", Logger.Level.INFO);
			return true;
		}
		else {
			current-= amount;
			return true;
		}
	}
}
