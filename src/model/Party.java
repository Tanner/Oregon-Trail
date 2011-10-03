package model;

import core.Logger;
import core.Logger.Level;

/**
 * Party class that contains an array of persons that are members.
 * @author George Johnston
 */
public class Party {
	
	private Person[] members;
	private int money;
	private Pace currentPace;
	private Rations currentRations;
	
	
	/**
	 * If party is created before members, this constructor is used.
	 */
	public Party() {
		this.members = null;
	}
	
	/**
	 * If people are present before party is created, this constructor is used
	 * @param party Array of people to be initialized into party
	 */
	public Party(Pace pace, Rations rations, Person ... party) {
		this.members = party.clone();
		this.money = 0;
		this.currentPace = pace;
		this.currentRations = rations;
		String partyCreationLog = members.length + " members were created successfully: ";
		for (Person person: party){
			
			this.money += person.getProfession().getMoney();
			Logger.log(person.getName()+ " as a " + person.getProfession() + " brings " + 
					person.getProfession().getMoney() + " to the party.", Logger.Level.INFO);

			partyCreationLog += person.getName() + " ";
		}
		Logger.log(partyCreationLog, Logger.Level.INFO);
		Logger.log("Party starting money is: " + money, Logger.Level.INFO);
		Logger.log("Current pace is: " + currentPace + " and current rations is: " + currentRations, Level.INFO);
	}
	
	/**
	 * Returns an array of Persons present in the party
	 * @return
	 */
	public Person[] getPartyMembers() {
		return this.members;
	}
	
	public int getMoney(){
		return this.money;
	}
	
	public void setMoney(int money){
		this.money = money;
	}
	
	public enum Pace{
		STEADY,
		STRENUOUS,
		GRUELING;
	}
	
	public Pace getPace(){
		return currentPace;
	}
	
	public void setPace(Pace pace){
		this.currentPace = pace;
	}
	
	public enum Rations{
		FILLING,
		MEAGER,
		BAREBONES;
	}
	
	public Rations getRations(){
		return currentRations;
	}
	
	public void setRations(Rations rations){
		this.currentRations = rations;
	}
}
