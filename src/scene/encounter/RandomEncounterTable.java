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
	
	public RandomEncounterTable(Encounter[] encounters){
		random = new Random();
		this.encounters = encounters;
	}
		
	public EncounterNotification getRandomEncounter() {

		int roll = random.nextInt(encounters[encounters.length-1].getMax());
		
		for (int i = 0; i < encounters.length; i++) {
			if ( encounters[i].isInRange(roll))
				return encounters[i].doEncounter();
		}
		return null;
	}
}
