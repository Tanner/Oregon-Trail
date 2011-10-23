package model;

import java.util.Map;
import java.util.Random;

import scene.SceneID;

/**
 * This provides a probability table for random encounters during a trail scene
 * as well as handling probability shifts.
 * @author Null && Void
 */
public class RandomEncounterTable {
	
	Map<SceneID, Integer> encounterProb;
	
	Random random;
	
	public RandomEncounterTable(Map<SceneID, Integer> encounterMap){
		random = new Random();
		encounterProb = encounterMap;
	}
		
	SceneID getRandomEncounter() {
		int total = 0;
		SceneID nextScene = null;
		boolean sceneFound = false;
		for(SceneID scene : encounterProb.keySet()) {
			total+= encounterProb.get(scene);
		}
		random.nextInt(total);
		for(SceneID scene : encounterProb.keySet()) {
			if (!sceneFound) {
				if(encounterProb.get(scene) < total && scene != SceneID.TRAIL) {
					nextScene = scene;
					sceneFound = true;
				} else if (encounterProb.get(scene) < total && scene == SceneID.TRAIL) {
					nextScene = null;
					sceneFound = true;
				} else {
					total -= encounterProb.get(scene);			
				}
			}
		}
		return nextScene;
	}
}
