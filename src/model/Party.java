package model;

public class Party {

	private Person[] members;
	
	public Party(){
		this.members = null;
	}
	
	public Party(Person ... party){
		this.members = party.clone();
	}
	
	public Person[] getPartyMembers(){
		return this.members;
	}
}
