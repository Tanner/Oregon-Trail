package scene.encounter;

import java.util.List;
import java.util.Random;

/**
 * This provides a probability table for random encounters during a trail scene
 * as well as handling probability shifts.
 * @author Null && Void
 */
public class RandomEncounterTable {
	
	private List<Encounter> encounters;
	private Random random;
	private int maxValue;
	
	/**
	 * Create the RandomEncounterTable so it may be polled for encounters.
	 * @param encounters An array of generated encounters
	 */
	public RandomEncounterTable(List<Encounter> encounters){
		random = new Random();
		this.encounters = encounters;
	}
		
	/**
	 * Find which Encounter is going to be executed, execute that encounter,
	 * and return a Notification/Scene.
	 * @return The EncounterNotification relating to the just executed Encounter
	 */
	public EncounterNotification getRandomEncounter() {

		maxValue = 0;
		for(Encounter encounter : encounters) {
			maxValue += encounter.getValue();
		}
		
		int roll = random.nextInt(maxValue);
		for (Encounter encounter : encounters) {
			System.out.println(encounter + " Value: " + encounter.getValue());
			if (encounter instanceof NullEncounter) {
				for(Encounter other : encounters) {
					other.increaseValue(1);
				}
				return encounter.doEncounter();
			} else if(encounter.isInRange(roll)) {
				return encounter.doEncounter();
			} else 
				roll -= encounter.getValue();
		}
		return null;
	}
}
