package scene.encounter;

import java.util.Random;

/**
 * This provides a probability table for random encounters during a trail scene
 * as well as handling probability shifts.
 * @author Null && Void
 */
public class RandomEncounterTable {
	
	private Encounter[] encounters;
	Random random;
	
	/**
	 * Create the RandomEncounterTable so it may be polled for encounters.
	 * @param encounters An array of generated encounters
	 */
	public RandomEncounterTable(Encounter[] encounters){
		random = new Random();
		this.encounters = encounters;
	}
		
	/**
	 * Find which Encounter is going to be executed, execute that encounter,
	 * and return a Notification/Scene.
	 * @return The EncounterNotification relating to the just executed Encounter
	 */
	public EncounterNotification getRandomEncounter() {

		int roll = random.nextInt(encounters[encounters.length-1].getMax());
		
		for (int i = 0; i < encounters.length; i++) {
			if ( encounters[i].isInRange(roll))
				return encounters[i].doEncounter();
		}
		
		return null;
	}
}
