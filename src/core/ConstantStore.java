package core;

import java.util.Collections;
import java.util.HashMap; 
import java.util.Map; 

/**
 * Class to hold constants used by the game
 * 
 * @author John Turner
 */
public final class ConstantStore {	
	/**
	 * Initialize and build literal map used to hold all display literals in game
	 */
	public static final Map<String, Map> LITERALS;
	static {
		Map<String, Map> literalMap = new HashMap<String, Map>();
		
		// Main Menu
		Map<String, String> mainMenu = new HashMap<String, String>();
		mainMenu.put("TITLE", "Oregon Trail");
		mainMenu.put("PRESS_ENTER", "Press Enter to Start");
		literalMap.put("MAIN_MENU", mainMenu);
		
		// Player Creation Scene
		Map<String, String> partyCreationScene = new HashMap<String, String>();
		partyCreationScene.put("NEW_PLAYER", "New Player");
		partyCreationScene.put("CHANGE_SKILL", "Change Skill");
		partyCreationScene.put("CHANGE_PROFESSION", "Change Profession");
		partyCreationScene.put("PACE_LABEL", "Pace:");
		partyCreationScene.put("RATIONS_LABEL", "Rations:");
		partyCreationScene.put("PROFESSION_MODAL", "Please select a profession.");
		partyCreationScene.put("SKILL_MODAL", "Please select three skills.");
		partyCreationScene.put("PARTY_CONFIRM", "Confirm");
		literalMap.put("PARTY_CREATION_SCENE", partyCreationScene);
		
		// General
		Map<String, String> general = new HashMap<String, String>();
		general.put("CONFIRM", "Confirm");
		general.put("CANCEL", "Cancel");
		general.put("PROFESSION", "Profession");

		LITERALS = Collections.unmodifiableMap(literalMap);
	}
	
	public static String get(String outer, String inner) {
		return (String)LITERALS.get(outer).get(inner);
	}
}
