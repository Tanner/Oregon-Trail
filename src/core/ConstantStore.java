package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap; 
import java.util.List;
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
	public static final Map<Integer,List<String>> TOWN_NAMES;
	
	static {
		Map<String, Map<String, String>> literalMap = new HashMap<String, Map<String, String>>();
		TOWN_NAMES = new HashMap <Integer, List<String>>();
		
		// Main Menu
		Map<String, String> mainMenu = new HashMap<String, String>();
		mainMenu.put("TITLE", "Oregon Trail");
		mainMenu.put("NEW_GAME", "New Game");
		literalMap.put("MAIN_MENU", mainMenu);
		
		// Scene Selector Scene
		Map<String, String> sceneSelectorScene = new HashMap<String, String>();
		sceneSelectorScene.put("RESET_PARTY", "Reset Party");
		sceneSelectorScene.put("RESET_GAME", "Reset Game");
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
		partyCreationScene.put("PROFESSIONS_TOOLTIP_MONEY", "Money: ");
		partyCreationScene.put("PROFESSIONS_TOOLTIP_SKILL", "Starting Skill: ");
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
		partyInventoryScene.put("FREE", "Free");
		partyInventoryScene.put("ERR_EMPTY_BIN", "The bin is empty. Please add an item to the bin to proceed.");
		partyInventoryScene.put("ERR_INV_FAIL", "This inventory cannot hold all the bin's items. Please select another inventory.");
		literalMap.put("PARTY_INVENTORY_SCENE", partyInventoryScene);
		
		// Store Scene
		Map<String, String> storeScene = new HashMap<String, String>();
		storeScene.put("WEIGHT", "Weight: ");
		storeScene.put("COST", "Cost: ");
		storeScene.put("QUANTITY", "Quantity: ");
		storeScene.put("TOTAL_WEIGHT", "Total Weight: ");
		storeScene.put("TOTAL_COST", "Total Cost: ");
		storeScene.put("PARTY_MONEY", "Party's Money: ");
		storeScene.put("INVENTORY", "Inventory");
		storeScene.put("CLEAR", "Clear");
		storeScene.put("BUY", "Buy");
		storeScene.put("PICK_RECEIVER", "Choose who will buy this item.");
		storeScene.put("ERR_TOO_MANY_WAGON", "Please buy a single wagon first.");
		storeScene.put("ERR_NOT_ENOUGH_MONEY_FOR_WAGON", "You don't have enough money to buy a wagon.\nBetter prepare to make it on foot.");
		storeScene.put("ERR_NOT_ENOUGH_MONEY", "You don't have enough money for this purchase.");
		storeScene.put("ERR_CANT_CARRY", "No one can carry that weight.");
		literalMap.put("STORE_SCENE", storeScene);
		
		// Trail Scene
		Map<String, String> trailScene = new HashMap<String, String>();
		trailScene.put("CAMP", "Camp");
		literalMap.put("TRAIL_SCENE", trailScene);
		
		// Town Scene
		Map<String, String> townScene = new HashMap<String, String>();
		townScene.put("TRAIL_CHOICE", "Where do you wish to travel to next?");
		literalMap.put("TOWN_SCENE", townScene);
		
		// Items
		Map<String, String> itemMap = new HashMap<String, String>();
		itemMap.put("WHEEL_NAME", "Wheel");
		itemMap.put("WHEEL_DESCRIPTION", "This is a wheeel");
		itemMap.put("WHEEL_WEIGHT", "5");
		itemMap.put("WHEEL_COST", "75");
		itemMap.put("WHEEL_FOOD_FACTOR", "0");
		itemMap.put("SONIC_NAME", "Sonic");
		itemMap.put("SONIC_DESCRIPTION", "Weeeweeeveeeeweee");
		itemMap.put("SONIC_WEIGHT", "0.5");
		itemMap.put("SONIC_COST", "100");
		itemMap.put("SONIC_FOOD_FACTOR", "0");
		itemMap.put("APPLE_NAME", "Apple");
		itemMap.put("APPLE_DESCRIPTION", "Its an Apple.");
		itemMap.put("APPLE_WEIGHT", "1.5");
		itemMap.put("APPLE_COST", "2");
		itemMap.put("APPLE_FOOD_FACTOR", "1");
		itemMap.put("BREAD_NAME", "Bread");
		itemMap.put("BREAD_DESCRIPTION", "Its the finest bread ever.");
		itemMap.put("BREAD_WEIGHT", "1.0");
		itemMap.put("BREAD_COST", "4");
		itemMap.put("BREAD_FOOD_FACTOR", "2");
		itemMap.put("GUN_NAME", "Gun");
		itemMap.put("GUN_DESCRIPTION", "Bang! Bang bang bang!");
		itemMap.put("GUN_WEIGHT", "5.0");
		itemMap.put("GUN_COST", "50");
		itemMap.put("GUN_FOOD_FACTOR", "0");
		itemMap.put("AMMO_NAME", "Bullet");
		itemMap.put("AMMO_DESCRIPTION", "For a gun.");
		itemMap.put("AMMO_WEIGHT", "0.25");
		itemMap.put("AMMO_COST", "2");
		itemMap.put("AMMO_FOOD_FACTOR", "0");
		itemMap.put("MEAT_NAME", "Meat");
		itemMap.put("MEAT_DESCRIPTION", "Food noms - ohm nom nom.");
		itemMap.put("MEAT_WEIGHT", "1");
		itemMap.put("MEAT_COST", "15");
		itemMap.put("MEAT_FOOD_FACTOR", "4");
		itemMap.put("WAGON_NAME", "Wagon");
		itemMap.put("WAGON_DESCRIPTION", "This is a trusty wooden wagon.");
		itemMap.put("WAGON_WEIGHT", "200");
		itemMap.put("WAGON_COST", "2000");
		itemMap.put("WAGON_FOOD_FACTOR", "0");
		itemMap.put("OX_NAME", "Ox");
		itemMap.put("OX_DESCRIPTION", "MOOO!");
		itemMap.put("OX_WEIGHT", "200");
		itemMap.put("OX_COST", "100");
		itemMap.put("OX_FOOD_FACTOR", "0");
		itemMap.put("STRANGE_MEAT_NAME", "Strange Meat");
		itemMap.put("STRANGE_MEAT_DESCRIPTION", "I don't want to know where this came from...");
		itemMap.put("STRANGE_MEAT_WEIGHT", "25");
		itemMap.put("STRANGE_MEAT_COST", "15");
		itemMap.put("STRANGE_MEAT_FOOD_FACTOR", "3");
		literalMap.put("ITEMS", itemMap);
		
		// General
		Map<String, String> general = new HashMap<String, String>();
		general.put("MONEY_SYMBOL", "$");
		general.put("CONFIRM", "Confirm");
		general.put("OK", "Ok");
		general.put("CANCEL", "Cancel");
		general.put("CONTINUE", "Continue");
		general.put("CLOSE", "Close");
		general.put("LEAVE", "Leave");
		general.put("PROFESSION", "Profession");
		general.put("WEIGHT_UNIT", "lbs");
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
		colorMap.put("TRANSLUCENT_OVERLAY", new Color(0, 0, 0, .25f));

		COLORS = Collections.unmodifiableMap(colorMap);
		
		//town names - missouri 
		List<String> townNames0 = new ArrayList<String>();
		townNames0.add("St. Joseph"); 
		townNames0.add("Kansas City");
		townNames0.add("Westport");

		
		//town names - nebraska territory
		List<String> townNames1 = new ArrayList<String>();
		townNames1.add("Archer");
		townNames1.add("Nebraska City");
		townNames1.add("Pawneeville");
		townNames1.add("Tecumseh");
		townNames1.add("Saline"); 
		townNames1.add("Fort Omaha"); 
		townNames1.add("Ponca Fort");
		townNames1.add("Fort Kearny");
		townNames1.add("Fort Lisa");
		townNames1.add("Ash Hollow");
		townNames1.add("Scottsbluff");
		townNames1.add("Lincoln");
		townNames1.add("Ogallala");
		townNames1.add("Windlass Hill");
		townNames1.add("Ash Hollow");
		townNames1.add("Fort Laramie");
		townNames1.add("Guernsey");
		townNames1.add("Fort Bridger");
		
		
		townNames1.add("Julesburg");
		townNames1.add("Cokeville");
		townNames1.add("Goodnight");
		townNames1.add("Deadwood");
		townNames1.add("Tombstone");
		townNames1.add("Julesburg");
		townNames1.add("Fort Coon");
		townNames1.add("Latham");

	
		//town names - kansas territory
		List<String> townNames2 = new ArrayList<String>();
		townNames2.add("Ash Hollow");
		townNames2.add("Atchison");
		townNames2.add("Blue Rapids");
		townNames2.add("Casper");
		townNames2.add("Fort Bridger");
		townNames2.add("Fort Coon");
		townNames2.add("Fort Laramie");
		townNames2.add("Fort Leavenworth");
		townNames2.add("Marysville"); 
		townNames2.add("Mount Oread");
		townNames2.add("Santa Fe");
		townNames2.add("Topeka");

		townNames2.add("Julesburg");
		townNames2.add("Latham");
		townNames2.add("Scott Spring");
		townNames2.add("Weston");

		//town names - washington territory
		List<String> townNames3 = new ArrayList<String>();
		townNames3.add("Boise");
		townNames3.add("Fort Bridger");
		townNames3.add("Fort Hall");
		townNames3.add("Glenn's Ferry");
		townNames3.add("Shoshone");
		
		

		//town names - utah territory
		List<String> townNames4 = new ArrayList<String>();
		townNames4.add("Lincoln");
			
		//town names - oregon
		List<String> townNames5 = new ArrayList<String>();

		townNames5.add("Baker City");
		townNames5.add("Barlow Gate");
		townNames5.add("The Dalles");
		townNames5.add("Fort Vancouver");
		townNames5.add("Fort Walla Walla");
		townNames5.add("Grande Ronde Outpost");
		townNames5.add("Pendleton");
		townNames5.add("Vale");
		townNames5.add("Whitman Mission");
		

		
		//missouri territory zone names
		TOWN_NAMES.put(0, townNames0);
		//nebraska territory zone names
		TOWN_NAMES.put(1, townNames1);		
		//kansas territory zone names
		TOWN_NAMES.put(2, townNames2);		
		//washington territory zone names
		TOWN_NAMES.put(3, townNames3);		
		//Utah territory zone names
		TOWN_NAMES.put(4, townNames4);
		//oregon zone names		
		TOWN_NAMES.put(5, townNames5);
		
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
