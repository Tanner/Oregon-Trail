package core;

import java.util.Collections;
import java.util.HashMap; 
import java.util.Map; 

import org.newdawn.slick.Color;

/**
 * Class to hold constants used by the game
 * 
 * @author John Turner
 */
public final class ConstantStore {	
	/**
	 * Initialize and build literal map used to hold all display literals in game
	 */
	public static final Map<String, Map<String, String>> LITERALS;
	public static final Map<String, Color> COLORS;
	
	static {
		Map<String, Map<String, String>> literalMap = new HashMap<String, Map<String, String>>();
		
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
		literalMap.put("GENERAL", general);

		LITERALS = Collections.unmodifiableMap(literalMap);
		
		// Colors
		Map<String, Color> colorMap = new HashMap<String, Color>(); 
		colorMap.put("INTERACTIVE_NORMAL", Color.gray);
		colorMap.put("INTERACTIVE_ACTIVE", Color.darkGray);
		
		COLORS = Collections.unmodifiableMap(colorMap);
	}
	
	public static String get(String outer, String inner) {
		return (String)LITERALS.get(outer).get(inner);
	}
}
