package model;

public class Wagon extends Vehicle {
	public Wagon() {
		super("Wagon", "This is a wagon", new Condition(100), 2000, 200);
	}
}
