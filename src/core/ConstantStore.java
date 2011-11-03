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
	public static final Map<StateIdx,List<String>> TOWN_NAMES;
	public static final Map<StateIdx,String> STATE_NAMES;
	
	public static enum StateIdx{
		MISSOURI,
		KANSAS_TERRITORY,
		NEBRASKA_TERRITORY,
		COLORADO_TERRITORY,
		DAKOTA_TERRITORY,
		WASHINGTON_TERRITORY,
		UTAH_TERRITORY,
		OREGON
	}
	
	public static enum Environments{
		FORREST,
		SNOWY_FORREST,
		HILLS,
		SNOWY_HILLS,
		MOUNTAINS,
		SNOWY_MOUNTAINS,
		PLAINS,
		SNOWY_PLAINS,
		DESERT
	}
	
	static {
		Map<String, Map<String, String>> literalMap = new HashMap<String, Map<String, String>>();
		TOWN_NAMES = new HashMap <StateIdx, List<String>>();
		STATE_NAMES = new HashMap <StateIdx,String>();
		
		// Main Menu
		Map<String, String> mainMenu = new HashMap<String, String>();
		mainMenu.put("TITLE", "Oregon Trail");
		mainMenu.put("NEW_GAME", "New Game");
		literalMap.put("MAIN_MENU", mainMenu);
		
		// Scene Selector Scene
		Map<String, String> sceneSelectorScene = new HashMap<String, String>();
		sceneSelectorScene.put("RESET_PARTY", "Reset Party");
		sceneSelectorScene.put("RESET_GAME", "Reset Game");
		sceneSelectorScene.put("SAVE_GAME", "Save Game");
		sceneSelectorScene.put("LOAD_GAME", "Load Game");
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
		trailScene.put("INVENTORY", "Inventory");
		trailScene.put("MAP", "Map");
		trailScene.put("HUNT", "Hunt");
		trailScene.put("MISC", "TBD");
		trailScene.put("LEAVE", "Continue");
		literalMap.put("TRAIL_SCENE", trailScene);
		
		// Town Scene
		Map<String, String> townScene = new HashMap<String, String>();
		townScene.put("TRAIL_CHOICE", "Where do you wish to travel to next?");
		literalMap.put("TOWN_SCENE", townScene);
		
		// Map Scene
		Map<String, String> mapScene = new HashMap<String, String>();
		mapScene.put("TITLE", "Your Map to Oregon");
		mapScene.put("RETURN_CAMP", "Return to Camp");
		literalMap.put("MAP_SCENE", mapScene);
		
		// Items
		Map<String, String> itemMap = new HashMap<String, String>();
		itemMap.put("WHEEL_NAME", "Wheel");
		itemMap.put("WHEEL_PLURAL_NAME", "Wheels");
		itemMap.put("WHEEL_DESCRIPTION", "This is a wheeel");
		itemMap.put("WHEEL_WEIGHT", "5");
		itemMap.put("WHEEL_COST", "75");
		itemMap.put("WHEEL_REPAIR_FACTOR", "2");
		itemMap.put("WHEEL_NECESSARY_QUALITY", "50");
		
		itemMap.put("SONIC_NAME", "Sonic");
		itemMap.put("SONIC_PLURAL_NAME", "Sonics");
		itemMap.put("SONIC_DESCRIPTION", "Weeeweeeveeeeweee");
		itemMap.put("SONIC_WEIGHT", "0.5");
		itemMap.put("SONIC_COST", "100");
		itemMap.put("SONIC_NECESSARY_QUALITY", "100");
		
		itemMap.put("APPLE_NAME", "Apple");
		itemMap.put("APPLE_PLURAL_NAME", "Apples");
		itemMap.put("APPLE_DESCRIPTION", "Its an Apple.");
		itemMap.put("APPLE_WEIGHT", "1.5");
		itemMap.put("APPLE_COST", "2");
		itemMap.put("APPLE_FOOD_FACTOR", "1");
		
		itemMap.put("BREAD_NAME", "Bread");
		itemMap.put("BREAD_PLURAL_NAME", "Bread");
		itemMap.put("BREAD_DESCRIPTION", "Its the finest bread ever.");
		itemMap.put("BREAD_WEIGHT", "1.0");
		itemMap.put("BREAD_COST", "4");
		itemMap.put("BREAD_FOOD_FACTOR", "2");
		itemMap.put("BREAD_NECESSARY_QUALITY", "10");
		
		itemMap.put("GUN_NAME", "Gun");
		itemMap.put("GUN_PLURAL_NAME", "Guns");
		itemMap.put("GUN_DESCRIPTION", "Bang! Bang bang bang!");
		itemMap.put("GUN_WEIGHT", "5.0");
		itemMap.put("GUN_COST", "50");
		itemMap.put("GUN_NECESSARY_QUALITY", "25");
		
		itemMap.put("AMMO_NAME", "Ammo");
		itemMap.put("AMMO_PLURAL_NAME", "Ammo");
		itemMap.put("AMMO_DESCRIPTION", "A box of 100 bullets.");
		itemMap.put("AMMO_WEIGHT", "2");
		itemMap.put("AMMO_COST", "100");
		itemMap.put("AMMO_NECESSARY_QUALITY", "20");
		
		itemMap.put("MEAT_NAME", "Meat");
		itemMap.put("MEAT_PLURAL_NAME", "Meat");
		itemMap.put("MEAT_DESCRIPTION", "Food noms - ohm nom nom.");
		itemMap.put("MEAT_WEIGHT", "1");
		itemMap.put("MEAT_COST", "15");
		itemMap.put("MEAT_FOOD_FACTOR", "4");
		
		itemMap.put("WAGON_NAME", "Wagon");
		itemMap.put("WAGON_PLURAL_NAME", "Wagons");
		itemMap.put("WAGON_DESCRIPTION", "This is a trusty wooden wagon.");
		itemMap.put("WAGON_WEIGHT", "200");
		itemMap.put("WAGON_COST", "2000");
		itemMap.put("WAGON_MAX_INV_SIZE", "10");
		itemMap.put("WAGON_MAX_INV_WEIGHT", "2000");
		itemMap.put("WAGON_NECESSARY_QUALITY", "95");
		
		itemMap.put("OX_NAME", "Ox");
		itemMap.put("OX_PLURAL_NAME", "Oxen");
		itemMap.put("OX_DESCRIPTION", "MOOO!");
		itemMap.put("OX_WEIGHT", "200");
		itemMap.put("OX_COST", "100");
		itemMap.put("OX_MOVE_FACTOR", "2.0");
		
		itemMap.put("STRANGE_MEAT_NAME", "Strange Meat");
		itemMap.put("STRANGE_MEAT_PLURAL_NAME", "Strange Meat");
		itemMap.put("STRANGE_MEAT_DESCRIPTION", "I don't want to know where this came from...");
		itemMap.put("STRANGE_MEAT_WEIGHT", "25");
		itemMap.put("STRANGE_MEAT_COST", "15");
		itemMap.put("STRANGE_MEAT_FOOD_FACTOR", "3");
		
		itemMap.put("HORSE_NAME", "Horse");
		itemMap.put("HORSE_PLURAL_NAME", "Horses");
		itemMap.put("HORSE_DESCRIPTION", "NEIGH!");
		itemMap.put("HORSE_WEIGHT", "200");
		itemMap.put("HORSE_COST", "400");
		itemMap.put("HORSE_MOVE_FACTOR", "4.0");
		itemMap.put("HORSE_NECESSARY_QUALITY", "75");
		
		itemMap.put("MULE_NAME", "Mule");
		itemMap.put("MULE_PLURAL_NAME", "Mule");
		itemMap.put("MULE_DESCRIPTION", "HEEHAW!");
		itemMap.put("MULE_WEIGHT", "200");
		itemMap.put("MULE_COST", "250");
		itemMap.put("MULE_MOVE_FACTOR", "3.0");
		itemMap.put("MULE_NECESSARY_QUALITY", "30");
		
		itemMap.put("HAMMER_NAME", "Hammer");
		itemMap.put("HAMMER_PLURAL_NAME", "Hammers");
		itemMap.put("HAMMER_DESCRIPTION", "A hammer for hammering.");
		itemMap.put("HAMMER_WEIGHT", ".5");
		itemMap.put("HAMMER_COST", "20");
		itemMap.put("HAMMER_REPAIR_FACTOR", "1");
		
		itemMap.put("AXLE_NAME", "Axle");
		itemMap.put("AXLE_PLURAL_NAME", "Axles");
		itemMap.put("AXLE_DESCRIPTION", "An axle for your wagon.");
		itemMap.put("AXLE_WEIGHT", "10");
		itemMap.put("AXLE_COST", "200");
		itemMap.put("AXLE_REPAIR_FACTOR", "4");
		itemMap.put("AXLE_NECESSARY_QUALITY", "60");
		
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
		general.put("PACE_LABEL", "Pace:");
		general.put("RATIONS_LABEL", "Rations:");
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
		townNames0.add("Arrow Rock");
		townNames0.add("Boonville");
		townNames0.add("Cave Spring");
		townNames0.add("Collumbia");
		townNames0.add("Danville");
		townNames0.add("Kanesville"); 
		townNames0.add("Kansas City");
		townNames0.add("St. Joseph"); 
		townNames0.add("Wentzville");
		townNames0.add("Westport");
		townNames0.add("Weston");

		
		//town names - nebraska territory
		List<String> townNames1 = new ArrayList<String>();
		townNames1.add("Archer");
		townNames1.add("Ash Hollow");
		townNames1.add("Brule");
		townNames1.add("California Hill");		
		townNames1.add("Fairbury");
		townNames1.add("Fort Bridger");
		townNames1.add("Fort Kearny");
		townNames1.add("Fort Laramie");
		townNames1.add("Fort Lisa");
		townNames1.add("Fort McPherson");
		townNames1.add("Fort Omaha"); 
		townNames1.add("Franklin");
		townNames1.add("Gothenburg");
		townNames1.add("Guernsey");
		townNames1.add("Hastings");
		townNames1.add("Homestead");
		townNames1.add("Lexington");
		townNames1.add("Lincoln");
		townNames1.add("Minden");
		townNames1.add("Nebraska City");
		townNames1.add("North Platte");
		townNames1.add("Ogallala");
		townNames1.add("Odell");
		townNames1.add("O'Fallon's Bluff");
		townNames1.add("Pawneeville");
		townNames1.add("Ponca Fort");
		townNames1.add("Red Cloud");
		townNames1.add("Rock Creek Station");
		townNames1.add("Saline"); 
		townNames1.add("Scottsbluff");
		townNames1.add("Southerland");
		townNames1.add("Tecumseh");
		townNames1.add("Windlass Hill");
		townNames1.add("York");
		
		
		townNames1.add("Cokeville");
		townNames1.add("Tombstone");
	
	
		//town names - kansas territory
		List<String> townNames2 = new ArrayList<String>();
		townNames2.add("Alcove Spring");
		townNames2.add("Ash Hollow");
		townNames2.add("Atchison");
		townNames2.add("Barnes");
		townNames2.add("Blue Rapids");
		townNames2.add("Casper");
		townNames2.add("FairlWay");
		townNames2.add("Fort Bridger");
		townNames2.add("Fort Coon");
		townNames2.add("Fort Laramie");
		townNames2.add("Fort Leavenworth");
		townNames2.add("Fort Riley");
		townNames2.add("Gardner");
		townNames2.add("Hollenburg Ranch");
		townNames2.add("Manhattan");
		townNames2.add("Marysville"); 
		townNames2.add("Mount Oread");
		townNames2.add("Red Vermillion Crossing");
		townNames2.add("Scott Spring");
		townNames2.add("St Mary's");
		townNames2.add("Topeka");
		townNames2.add("Wamego");
		townNames2.add("Washington");

		townNames2.add("Julesburg");
		townNames2.add("Goodnight");
		townNames2.add("Deadwood");
		townNames2.add("Latham");
	
		//town names - washington territory
		List<String> townNames3 = new ArrayList<String>();
		townNames3.add("Alkali");
		townNames3.add("Boise");
		townNames3.add("Boardman");
		townNames3.add("Bridger Outpost");
		townNames3.add("Casper");
		townNames3.add("Castle Rock");
		townNames3.add("Dry Sands");
		townNames3.add("Fort Bridger");
		townNames3.add("Fort Hall");
		townNames3.add("Glenn's Ferry");
		townNames3.add("Heppner Junction");
		townNames3.add("Irrigon");
		townNames3.add("Pacific Springs");
		townNames3.add("Shoshone");
		townNames3.add("South Pass");	
		townNames3.add("Split Rock");
		townNames3.add("Simpson's Hollow");
		townNames3.add("Umatilla");
		
		//town names - utah territory
		List<String> townNames4 = new ArrayList<String>();
		townNames4.add("Big Mountain Pass");
		townNames4.add("Gold Hill");
		townNames4.add("Hogsback");
		townNames4.add("Promontory Point");
		townNames4.add("Southbend");
		townNames4.add("Tombstone");
		townNames4.add("Yellow Creek");
		
		//town names - colorado territory
		List<String> townNames5 = new ArrayList<String>();
		townNames5.add("Alcove Spring");
		townNames5.add("Ash Hollow");
		townNames5.add("Atchison");
		townNames5.add("Barnes");
		townNames5.add("Blue Rapids");
		townNames5.add("Boulder");
		townNames5.add("Casper");
		townNames5.add("Denver");
		townNames5.add("FairlWay");
		townNames5.add("Fort Bridger");
		townNames5.add("Fort Coon");
		townNames5.add("Fort Laramie");
		townNames5.add("Fort Leavenworth");
		townNames5.add("Fort Riley");
		townNames5.add("Gardner");
		townNames5.add("Hollenburg Ranch");
		townNames5.add("Manhattan");
		townNames5.add("Marysville"); 
		townNames5.add("Mount Oread");
		townNames5.add("Red Vermillion Crossing");
		townNames5.add("Scott Spring");
		townNames5.add("St Mary's");
		townNames5.add("Topeka");
		townNames5.add("Wamego");
		townNames5.add("Washington");

		townNames5.add("Julesburg");
		townNames5.add("Goodnight");
		townNames5.add("Deadwood");
		townNames5.add("Latham");

		//town names - Dakota territory
		List<String> townNames6 = new ArrayList<String>();
		townNames6.add("Archer");
		townNames6.add("Ash Hollow");
		townNames6.add("Black Hills");
		townNames6.add("Brule");
		townNames6.add("California Hill");		
		townNames6.add("Fairbury");
		townNames6.add("Fort Bridger");
		townNames6.add("Fort Kearny");
		townNames6.add("Fort Laramie");
		townNames6.add("Fort Lisa");
		townNames6.add("Fort McPherson");
		townNames6.add("Franklin");
		townNames6.add("Gothenburg");
		townNames6.add("Guernsey");
		townNames6.add("Hastings");
		townNames6.add("Homestead");
		townNames6.add("Lexington");
		townNames6.add("Minden");
		townNames6.add("North Platte");
		townNames6.add("Ogallala");
		townNames6.add("Odell");
		townNames6.add("O'Fallon's Bluff");
		townNames6.add("Pawneeville");
		townNames6.add("Ponca Fort");
		townNames6.add("Red Cloud");
		townNames6.add("Rock Creek Station");
		townNames6.add("Saline"); 
		townNames6.add("Scottsbluff");
		townNames6.add("Southerland");
		townNames6.add("Sioux City");
		townNames6.add("Tecumseh");
		townNames6.add("Windlass Hill");
		townNames6.add("York");
		
		
		townNames6.add("Cokeville");
		townNames6.add("Tombstone");

		//town names - oregon
		List<String> townNames7 = new ArrayList<String>();

		townNames7.add("Antelope");
		townNames7.add("Boyd");
		townNames7.add("Baker City");
		townNames7.add("Barlow Gate");
		townNames7.add("Bourne");
		townNames7.add("Cornucopia");
		townNames7.add("The Dalles");
		townNames7.add("Fort Vancouver");
		townNames7.add("Fort Walla Walla");
		townNames7.add("Grande Ronde Outpost");
		townNames7.add("Granite");
		townNames7.add("Granite");
		townNames7.add("Greenback");
		townNames7.add("HalfWay");
		townNames7.add("Horse Heaven");
		townNames7.add("Kent");
		townNames7.add("Pendleton");
		townNames7.add("Shaniko");
		townNames7.add("Sumpter");
		townNames7.add("Vale");
		townNames7.add("Whitman Mission");
		townNames7.add("Walla Walla Lake");
		
		//missouri zone names
		TOWN_NAMES.put(StateIdx.MISSOURI, townNames0);
		//nebraska territory zone names
		TOWN_NAMES.put(StateIdx.NEBRASKA_TERRITORY, townNames1);		
		//kansas territory zone names
		TOWN_NAMES.put(StateIdx.KANSAS_TERRITORY, townNames2);		
		//washington territory zone names
		TOWN_NAMES.put(StateIdx.WASHINGTON_TERRITORY, townNames3);		
		//Utah territory zone names
		TOWN_NAMES.put(StateIdx.UTAH_TERRITORY, townNames4);
		//Colorado territory zone names		
		TOWN_NAMES.put(StateIdx.COLORADO_TERRITORY, townNames5);
		//Dakota territory zone names		
		TOWN_NAMES.put(StateIdx.DAKOTA_TERRITORY, townNames6);
		//oregon zone names		
		TOWN_NAMES.put(StateIdx.OREGON, townNames7);
		
		STATE_NAMES.put(StateIdx.MISSOURI, "Missouri");
		STATE_NAMES.put(StateIdx.NEBRASKA_TERRITORY, "Nebraska Territory");
		STATE_NAMES.put(StateIdx.KANSAS_TERRITORY, "Kansas Territory");
		STATE_NAMES.put(StateIdx.WASHINGTON_TERRITORY, "Washington Territory");
		STATE_NAMES.put(StateIdx.UTAH_TERRITORY, "Utah Territory");
		STATE_NAMES.put(StateIdx.COLORADO_TERRITORY, "Colorado");
		STATE_NAMES.put(StateIdx.DAKOTA_TERRITORY, "Dakota Territory");
		STATE_NAMES.put(StateIdx.OREGON, "Oregon");
		
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
