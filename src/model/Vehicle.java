package model;

public abstract class Vehicle {

	private Condition status;
	private Inventory cargo;
	
	public Vehicle(Condition status, Inventory cargo) {
		this.status = status;
		this.cargo = cargo;	
	}
	
	
}
