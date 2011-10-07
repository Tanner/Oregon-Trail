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
		partyCreationScene.put("NAME_PLACEHOLDER", "Name");
		partyCreationScene.put("NO_PROFESSION_LABEL", "No Profession");
		partyCreationScene.put("CHANGE_SKILL", "Change Skills");
		partyCreationScene.put("CHANGE_PROFESSION", "Change Profession");
		partyCreationScene.put("DELETE_PERSON_LABEL", "X");
		partyCreationScene.put("PACE_LABEL", "Pace:");
		partyCreationScene.put("RATIONS_LABEL", "Rations:");
		partyCreationScene.put("PROFESSION_MODAL", "Please select a profession.");
		partyCreationScene.put("SKILL_MODAL_MESSAGE", "%s inherited the %s skill. Select two more skills.");
		partyCreationScene.put("SKILL_MODAL_MESSAGE_NO_SKILL", "Select 3 skills for %s.");
		partyCreationScene.put("EMPTY_SKILL_LABEL", "-");
		partyCreationScene.put("PARTY_CONFIRM", "Confirm");
		partyCreationScene.put("ERR_NO_MEMBERS", "Error - No party members.");
		partyCreationScene.put("ERR_INCOMPLETE_PROFESSIONS", "Error - Not all party members have a profession.");
		partyCreationScene.put("ERR_INCOMPLETE_SKILLS", "Error - Not all party members have 3 skills.");
		partyCreationScene.put("ERR_DUP_NAME", "Error - A person already has this name. Please choose another.");
		literalMap.put("PARTY_CREATION_SCENE", partyCreationScene);
		
		// General
		Map<String, String> general = new HashMap<String, String>();
		general.put("MONEY_SYMBOL", "$");
		general.put("CONFIRM", "Confirm");
		general.put("OK", "Ok");
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
