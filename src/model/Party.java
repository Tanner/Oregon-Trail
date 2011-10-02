package model;

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
	public Party(Person ... party) {
		this.members = party.clone();
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
	
	public enum Rations{
		FILLING,
		MEAGER,
		BAREBONES;
	}
	
	public Rations getRations(){
		return currentRations;
	}
}
