package core;

import java.util.Collections;
import java.util.HashMap; 
import java.util.Map; 

import org.newdawn.slick.Color;

/**
 * Class to hold constants used by the game
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
		
		// Scene Selector Scene
		Map<String, String> sceneSelectorScene = new HashMap<String, String>();
		sceneSelectorScene.put("ADD_PARTY", "Add Party");
		sceneSelectorScene.put("REMOVE_PARTY", "Remove Party");
		sceneSelectorScene.put("ERR_NO_PARTY_FOR_SCENE", "Error - A party is required for this scene.");
		literalMap.put("SCENE_SELECTOR_SCENE", sceneSelectorScene);
		
		// Player Creation Scene
		Map<String, String> partyCreationScene = new HashMap<String, String>();
		partyCreationScene.put("NEW_PERSON", "New Person");
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
		
		// Party Inventory Scene
		Map<String, String> partyInventoryScene = new HashMap<String, String>();
		partyInventoryScene.put("TRANSFER", "Transfer");
		partyInventoryScene.put("DROP", "Drop");
		partyInventoryScene.put("SELL", "Sell");
		partyInventoryScene.put("ERR_EMPTY_BIN", "The bin is empty. Please add an item to the bin to proceed.");
		partyInventoryScene.put("ERR_INV_FAIL", "This inventory cannot hold all the bin's items. Please select another inventory.");
		literalMap.put("PARTY_INVENTORY_SCENE", partyInventoryScene);
		
		// HUD
		Map<String, String> hudScene = new HashMap<String, String>();
		hudScene.put("MEMBERS_HEALTH", "Members");
		hudScene.put("VEHICLE_HEALTH", "Vehicle");
		literalMap.put("HUD_SCENE", hudScene);
		
		// Items
		Map<String, String> itemMap = new HashMap<String, String>();
		itemMap.put("WHEEL_NAME", "Wheel");
		itemMap.put("WHEEL_DESCRIPTION", "This is a wheeel");
		itemMap.put("WHEEL_WEIGHT", "5");
		itemMap.put("WHEEL_COST", "75");
		itemMap.put("WHEEL_FF", "0");
		itemMap.put("SONIC_NAME", "Sonic");
		itemMap.put("SONIC_DESCRIPTION", "Weeeweeeveeeeweee");
		itemMap.put("SONIC_WEIGHT", "0.5");
		itemMap.put("SONIC_COST", "100");
		itemMap.put("SONIC_FF", "0");
		itemMap.put("APPLE_NAME", "Apple");
		itemMap.put("APPLE_DESCRIPTION", "Its an Apple.");
		itemMap.put("APPLE_WEIGHT", "1.5");
		itemMap.put("APPLE_COST", "2");
		itemMap.put("APPLE_FF", "1");
		itemMap.put("BREAD_NAME", "Bread");
		itemMap.put("BREAD_DESCRIPTION", "Its the finest bread ever.");
		itemMap.put("BREAD_WEIGHT", "1.0");
		itemMap.put("BREAD_COST", "4");
		itemMap.put("BREAD_FF", "2");
		itemMap.put("GUN_NAME", "Gun");
		itemMap.put("GUN_DESCRIPTION", "Bang! Bang bang bang!");
		itemMap.put("GUN_WEIGHT", "5.0");
		itemMap.put("GUN_COST", "50");
		itemMap.put("GUN_FF", "0");
		itemMap.put("AMMO_NAME", "Bullet");
		itemMap.put("AMMO_DESCRIPTION", "For a gun.");
		itemMap.put("AMMO_WEIGHT", "0.25");
		itemMap.put("AMMO_COST", "2");
		itemMap.put("AMMO_FF", "0");
		itemMap.put("MEAT_NAME", "Meat");
		itemMap.put("MEAT_DESCRIPTION", "Food noms - ohm nom nom.");
		itemMap.put("MEAT_WEIGHT", "25");
		itemMap.put("MEAT_COST", "15");
		itemMap.put("MEAT_FF", "4");
		itemMap.put("WAGON_NAME", "Wagon");
		itemMap.put("WAGON_DESCRIPTION", "This is a trusty wooden wagon.");
		itemMap.put("WAGON_WEIGHT", "200");
		itemMap.put("WAGON_COST", "2000");
		itemMap.put("WAGON_FF", "0");
		itemMap.put("OX_NAME", "Ox");
		itemMap.put("OX_DESCRIPTION", "MOOO!");
		itemMap.put("OX_WEIGHT", "200");
		itemMap.put("OX_COST", "100");
		itemMap.put("OX_FF", "0");
		literalMap.put("ITEMS", itemMap);
		
		// General
		Map<String, String> general = new HashMap<String, String>();
		general.put("MONEY_SYMBOL", "$");
		general.put("CONFIRM", "Confirm");
		general.put("OK", "Ok");
		general.put("CANCEL", "Cancel");
		general.put("CLOSE", "Close");
		general.put("PROFESSION", "Profession");
		literalMap.put("GENERAL", general);

		LITERALS = Collections.unmodifiableMap(literalMap);
		
		// Colors
		Map<String, Color> colorMap = new HashMap<String, Color>(); 
		colorMap.put("INTERACTIVE_NORMAL", Color.gray);
		colorMap.put("INTERACTIVE_ACTIVE", Color.darkGray);
		colorMap.put("INTERACTIVE_DISABLED", Color.darkGray);
		colorMap.put("INTERACTIVE_BORDER_DARK", Color.black);
		colorMap.put("INTERACTIVE_BORDER_LIGHT", Color.white.darker(0.25f));
		colorMap.put("INTERACTIVE_BORDER_FOCUS_LIGHT", Color.white.darker(0.5f));
		colorMap.put("INTERACTIVE_LABEL_NORMAL", Color.white);
		colorMap.put("INTERACTIVE_LABEL_DISABLED", Color.gray);
		colorMap.put("MODAL", Color.darkGray);
		colorMap.put("MODAL_BORDER", Color.white);

		COLORS = Collections.unmodifiableMap(colorMap);
	}
	
	/**
	 * Returns string held in hash map of constant literal hashmaps
	 * @param outer key to find appropriate inner map
	 * @param inner key to item item within proper map
	 * @return the string held in the map
	 */
	public static String get(String outer, String inner) {
		return LITERALS.get(outer).get(inner);
	}
}
