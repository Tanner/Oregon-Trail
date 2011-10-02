package model;

public class Player {
	
	private Party party;
	
	Player(){
		this.party = null;
	}
	
	Player(Party party){
		this.party = party;
	}
	
	public Party getParty(){
		return this.party;
	}
	
	public void setParty(Party party){
		this.party = party;
	}
	
}
