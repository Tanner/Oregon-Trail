import { Color } from "./Color";

export enum StateIdx {
  MISSOURI = "MISSOURI",
  KANSAS_TERRITORY = "KANSAS_TERRITORY",
  NEBRASKA_TERRITORY = "NEBRASKA_TERRITORY",
  COLORADO_TERRITORY = "COLORADO_TERRITORY",
  DAKOTA_TERRITORY = "DAKOTA_TERRITORY",
  WASHINGTON_TERRITORY = "WASHINGTON_TERRITORY",
  UTAH_TERRITORY = "UTAH_TERRITORY",
  OREGON = "OREGON",
}

export enum BackgroundType {
  GRASS = "GRASS",
  SNOW = "SNOW",
  MOUNTAIN = "MOUNTAIN",
  DESERT = "DESERT",
}

export enum Environments {
  FOREST = "FOREST",
  SNOWY_FOREST = "SNOWY_FOREST",
  HILLS = "HILLS",
  SNOWY_HILLS = "SNOWY_HILLS",
  MOUNTAINS = "MOUNTAINS",
  SNOWY_MOUNTAINS = "SNOWY_MOUNTAINS",
  PLAINS = "PLAINS",
  SNOWY_PLAINS = "SNOWY_PLAINS",
  DESERT = "DESERT",
}

export const PATH_GRAPHICS = "graphics/";
export const PATH_ANIMALS = PATH_GRAPHICS + "animals/";
export const PATH_BKGRND = PATH_GRAPHICS + "backgrounds/";
export const PATH_BUILDINGS = PATH_GRAPHICS + "buildings/";
export const PATH_FONTS = "fonts/";
export const PATH_GROUND = PATH_GRAPHICS + "ground/";
export const PATH_HUNT = PATH_GRAPHICS + "hunt/";
export const PATH_ICONS = PATH_GRAPHICS + "icons/";
export const PATH_ITEMS = PATH_GRAPHICS + "icons/items/";
export const PATH_LOGO = PATH_GRAPHICS + "logo/";
export const PATH_PEOPLE = PATH_GRAPHICS + "people/";
export const PATH_RIVER = PATH_GRAPHICS + "river/";
export const PATH_SOUND = "sounds/";
export const PATH_HUNTTERRAIN = PATH_GRAPHICS + "hunt/terrain/";
export const PATH_HUNTBKG = PATH_GRAPHICS + "hunt/backgrounds/";
export const PATH_HUNTPREY = PATH_GRAPHICS + "hunt/prey/";
export const PATH_TEST = PATH_GRAPHICS + "test/";
export const PATH_TRAIL = PATH_GRAPHICS + "trail/";

const buildLiterals = (): Map<string, Map<string, string>> => {
  const literalMap = new Map<string, Map<string, string>>();

  const mainMenu = new Map<string, string>();
  mainMenu.set("TITLE", "Oregon Trail");
  mainMenu.set("NEW_GAME", "New Game");
  literalMap.set("MAIN_MENU", mainMenu);

  const sceneSelectorScene = new Map<string, string>();
  sceneSelectorScene.set("RESET_PARTY", "Reset Party");
  sceneSelectorScene.set("RESET_GAME", "Reset Game");
  sceneSelectorScene.set("SAVE_GAME", "Save Game");
  sceneSelectorScene.set("LOAD_GAME", "Load Game");
  sceneSelectorScene.set("ERR_NO_PARTY_FOR_SCENE", "Error - A party is required for this scene.");
  literalMap.set("SCENE_SELECTOR_SCENE", sceneSelectorScene);

  const partyCreationScene = new Map<string, string>();
  partyCreationScene.set("NEW_PERSON", "New Person");
  partyCreationScene.set("NAME_PLACEHOLDER", "Name");
  partyCreationScene.set("NO_PROFESSION_LABEL", "No Profession");
  partyCreationScene.set("CHANGE_SKILL", "Change Skills");
  partyCreationScene.set("CHANGE_PROFESSION", "Change Profession");
  partyCreationScene.set("DELETE_PERSON_LABEL", "X");
  partyCreationScene.set("PROFESSION_MODAL", "Please select a profession.");
  partyCreationScene.set("PROFESSIONS_TOOLTIP_MONEY", "Money: ");
  partyCreationScene.set("PROFESSIONS_TOOLTIP_SKILL", "Starting Skill: ");
  partyCreationScene.set("SKILL_MODAL_MESSAGE", "%s inherited the %s skill. Select two more skills.");
  partyCreationScene.set("SKILL_MODAL_MESSAGE_NO_SKILL", "Select 3 skills for %s.");
  partyCreationScene.set("EMPTY_SKILL_LABEL", "-");
  partyCreationScene.set("PARTY_CONFIRM", "Confirm");
  partyCreationScene.set("ERR_NO_MEMBERS", "Error - No party members.");
  partyCreationScene.set("ERR_INCOMPLETE_PROFESSIONS", "Error - Not all party members have a profession.");
  partyCreationScene.set("ERR_INCOMPLETE_SKILLS", "Error - Not all party members have 3 skills.");
  partyCreationScene.set("ERR_DUP_NAME", "Error - A person already has this name. Please choose another.");
  literalMap.set("PARTY_CREATION_SCENE", partyCreationScene);

  const partyInventoryScene = new Map<string, string>();
  partyInventoryScene.set("TRANSFER", "Transfer");
  partyInventoryScene.set("DROP", "Drop");
  partyInventoryScene.set("SELL", "Sell");
  partyInventoryScene.set("FREE", "Free");
  partyInventoryScene.set("ERR_EMPTY_BIN", "The bin is empty. Please add an item to the bin to proceed.");
  partyInventoryScene.set("ERR_INV_FAIL", "This inventory cannot hold all the bin's items. Please select another inventory.");
  literalMap.set("PARTY_INVENTORY_SCENE", partyInventoryScene);

  const storeScene = new Map<string, string>();
  storeScene.set("WEIGHT", "Weight: ");
  storeScene.set("COST", "Cost: ");
  storeScene.set("QUANTITY", "Quantity: ");
  storeScene.set("TOTAL_WEIGHT", "Total Weight: ");
  storeScene.set("TOTAL_COST", "Total Cost: ");
  storeScene.set("PARTY_MONEY", "Party's Money: ");
  storeScene.set("INVENTORY", "Inventory");
  storeScene.set("CLEAR", "Clear");
  storeScene.set("BUY", "Buy");
  storeScene.set("PICK_RECEIVER", "Choose who will buy this item.");
  storeScene.set("ERR_TOO_MANY_ANIMALS", "You can only purchase up to 6 animals.");
  storeScene.set("ERR_TOO_MANY_WAGON", "Please buy a single wagon first.");
  storeScene.set("ERR_NOT_ENOUGH_MONEY_FOR_WAGON", "You don't have enough money to buy a wagon.\nBetter prepare to make it on foot.");
  storeScene.set("ERR_NOT_ENOUGH_MONEY", "You don't have enough money for this purchase.");
  storeScene.set("ERR_CANT_CARRY", "No one can carry that weight.");
  literalMap.set("STORE_SCENE", storeScene);

  const trailScene = new Map<string, string>();
  trailScene.set("CAMP", "Camp");
  trailScene.set("INVENTORY", "Inventory");
  trailScene.set("MAP", "Map");
  trailScene.set("HUNT", "Hunt");
  trailScene.set("MISC", "TBD");
  trailScene.set("LEAVE", "Continue");
  literalMap.set("TRAIL_SCENE", trailScene);

  const townScene = new Map<string, string>();
  townScene.set("TRAIL_CHOICE", "Where do you wish to travel to next?");
  townScene.set("TRAIL", "Trail");
  townScene.set("ENTER_STORE_INSTRUCTION", "Press Enter to Go Inside Store");
  townScene.set("ENTER_TAVERN_INSTRUCTION", "Press Enter to Go Inside Saloon");
  townScene.set("CONTINUE_ON_TRAIL", "Continue on Trail");
  townScene.set("TRAILBLAZE", "Trailblaze");
  townScene.set("NO_TRAIL", "You can't trailblaze without a trapper in your party.");
  literalMap.set("TOWN_SCENE", townScene);

  const huntScene = new Map<string, string>();
  huntScene.set("CAMP", "Return to Camp");
  huntScene.set("INVENTORY", "Inventory");
  literalMap.set("HUNT_SCENE", huntScene);

  const mapScene = new Map<string, string>();
  mapScene.set("TITLE", "Your Map to Oregon");
  mapScene.set("RETURN_CAMP", "Return to Camp");
  literalMap.set("MAP_SCENE", mapScene);

  const itemMap = new Map<string, string>();
  itemMap.set("WHEEL_NAME", "Wheel");
  itemMap.set("WHEEL_PLURAL_NAME", "Wheels");
  itemMap.set("WHEEL_DESCRIPTION", "This is a wheeel");
  itemMap.set("WHEEL_WEIGHT", "75");
  itemMap.set("WHEEL_COST", "10");
  itemMap.set("WHEEL_REPAIR_FACTOR", "2");
  itemMap.set("WHEEL_NECESSARY_QUALITY", "50");

  itemMap.set("SONIC_NAME", "Sonic");
  itemMap.set("SONIC_PLURAL_NAME", "Sonics");
  itemMap.set("SONIC_DESCRIPTION", "Weeeweeeveeeeweee");
  itemMap.set("SONIC_WEIGHT", "0.5");
  itemMap.set("SONIC_COST", "100");
  itemMap.set("SONIC_NECESSARY_QUALITY", "100");

  itemMap.set("APPLE_NAME", "Apple");
  itemMap.set("APPLE_PLURAL_NAME", "Apples");
  itemMap.set("APPLE_DESCRIPTION", "Its an Apple.");
  itemMap.set("APPLE_WEIGHT", "1.5");
  itemMap.set("APPLE_COST", "10");
  itemMap.set("APPLE_FOOD_FACTOR", "5");

  itemMap.set("BREAD_NAME", "Bread");
  itemMap.set("BREAD_PLURAL_NAME", "Bread");
  itemMap.set("BREAD_DESCRIPTION", "Its the finest bread ever.");
  itemMap.set("BREAD_WEIGHT", "1.0");
  itemMap.set("BREAD_COST", "25");
  itemMap.set("BREAD_FOOD_FACTOR", "2");
  itemMap.set("BREAD_NECESSARY_QUALITY", "10");

  itemMap.set("GUN_NAME", "Gun");
  itemMap.set("GUN_PLURAL_NAME", "Guns");
  itemMap.set("GUN_DESCRIPTION", "Bang! Bang bang bang!");
  itemMap.set("GUN_WEIGHT", "5.0");
  itemMap.set("GUN_COST", "50");
  itemMap.set("GUN_NECESSARY_QUALITY", "25");

  itemMap.set("AMMO_NAME", "Ammo");
  itemMap.set("AMMO_PLURAL_NAME", "Ammo");
  itemMap.set("AMMO_DESCRIPTION", "A box of 100 bullets.");
  itemMap.set("AMMO_WEIGHT", "2");
  itemMap.set("AMMO_COST", "100");
  itemMap.set("AMMO_NECESSARY_QUALITY", "20");

  itemMap.set("MEAT_NAME", "Meat");
  itemMap.set("MEAT_PLURAL_NAME", "Meat");
  itemMap.set("MEAT_DESCRIPTION", "Food noms - ohm nom nom.");
  itemMap.set("MEAT_WEIGHT", "1");
  itemMap.set("MEAT_COST", "15");
  itemMap.set("MEAT_FOOD_FACTOR", "25");

  itemMap.set("WAGON_NAME", "Wagon");
  itemMap.set("WAGON_PLURAL_NAME", "Wagons");
  itemMap.set("WAGON_DESCRIPTION", "This is a trusty wooden wagon.");
  itemMap.set("WAGON_WEIGHT", "200");
  itemMap.set("WAGON_COST", "800");
  itemMap.set("WAGON_MAX_INV_SIZE", "10");
  itemMap.set("WAGON_MAX_INV_WEIGHT", "3500");
  itemMap.set("WAGON_NECESSARY_QUALITY", "95");

  itemMap.set("OX_NAME", "Ox");
  itemMap.set("OX_PLURAL_NAME", "Oxen");
  itemMap.set("OX_DESCRIPTION", "MOOO!");
  itemMap.set("OX_WEIGHT", "200");
  itemMap.set("OX_COST", "100");
  itemMap.set("OX_MOVE_FACTOR", "2.0");

  itemMap.set("STRANGE_MEAT_NAME", "Strange Meat");
  itemMap.set("STRANGE_MEAT_PLURAL_NAME", "Strange Meat");
  itemMap.set("STRANGE_MEAT_DESCRIPTION", "I don't want to know where this came from...");
  itemMap.set("STRANGE_MEAT_WEIGHT", "5");
  itemMap.set("STRANGE_MEAT_COST", "15");
  itemMap.set("STRANGE_MEAT_FOOD_FACTOR", "20");

  itemMap.set("HORSE_NAME", "Horse");
  itemMap.set("HORSE_PLURAL_NAME", "Horses");
  itemMap.set("HORSE_DESCRIPTION", "NEIGH!");
  itemMap.set("HORSE_WEIGHT", "200");
  itemMap.set("HORSE_COST", "400");
  itemMap.set("HORSE_MOVE_FACTOR", "4.0");
  itemMap.set("HORSE_NECESSARY_QUALITY", "75");

  itemMap.set("MULE_NAME", "Mule");
  itemMap.set("MULE_PLURAL_NAME", "Mule");
  itemMap.set("MULE_DESCRIPTION", "HEEHAW!");
  itemMap.set("MULE_WEIGHT", "200");
  itemMap.set("MULE_COST", "250");
  itemMap.set("MULE_MOVE_FACTOR", "3.0");
  itemMap.set("MULE_NECESSARY_QUALITY", "30");

  itemMap.set("TOOLS_NAME", "Tools");
  itemMap.set("TOOLS_PLURAL_NAME", "Tools");
  itemMap.set("TOOLS_DESCRIPTION", "Some tools to fix stuff.");
  itemMap.set("TOOLS_WEIGHT", "5");
  itemMap.set("TOOLS_COST", "10");
  itemMap.set("TOOLS_REPAIR_FACTOR", "1");

  itemMap.set("AXLE_NAME", "Axle");
  itemMap.set("AXLE_PLURAL_NAME", "Axles");
  itemMap.set("AXLE_DESCRIPTION", "An axle for your wagon.");
  itemMap.set("AXLE_WEIGHT", "125");
  itemMap.set("AXLE_COST", "10");
  itemMap.set("AXLE_REPAIR_FACTOR", "4");
  itemMap.set("AXLE_NECESSARY_QUALITY", "60");

  itemMap.set("MAP_NAME", "Scout's Notes");
  itemMap.set("MAP_PLURAL_NAME", "Scout's Notes");
  itemMap.set("MAP_DESCRIPTION", "Some scouts notes.  May or may not reveal portions of the map.");
  itemMap.set("MAP_WEIGHT", "0");
  itemMap.set("MAP_COST", "200");
  itemMap.set("MAP_NECESSARY_QUALITY", "50");

  itemMap.set("TRADEGOODS_NAME", "Trade Goods");
  itemMap.set("TRADEGOODS_PLURAL_NAME", "Trade Goods");
  itemMap.set("TRADEGOODS_DESCRIPTION", "Some goods to trade further ahead for a tidy profit");
  itemMap.set("TRADEGOODS_WEIGHT", "5");
  itemMap.set("TRADEGOODS_COST", "100");
  itemMap.set("TRADEGOODS_NECESSARY_QUALITY", "60");

  literalMap.set("ITEMS", itemMap);

  const general = new Map<string, string>();
  general.set("MONEY_SYMBOL", "$");
  general.set("CONFIRM", "Confirm");
  general.set("OK", "Ok");
  general.set("CANCEL", "Cancel");
  general.set("CONTINUE", "Continue");
  general.set("CLOSE", "Close");
  general.set("LEAVE", "Leave");
  general.set("PROFESSION", "Profession");
  general.set("WEIGHT_UNIT", "lbs");
  general.set("PACE_LABEL", "Pace:");
  general.set("RATIONS_LABEL", "Rations:");
  literalMap.set("GENERAL", general);

  return literalMap;
};

const buildColors = (): Map<string, Color> => {
  const colorMap = new Map<string, Color>();
  colorMap.set("INTERACTIVE_NORMAL", Color.gray);
  colorMap.set("INTERACTIVE_ACTIVE", Color.darkGray);
  colorMap.set("INTERACTIVE_DISABLED", Color.darkGray);
  colorMap.set("INTERACTIVE_BORDER_DARK", Color.black);
  colorMap.set("INTERACTIVE_BORDER_LIGHT", Color.white.darker(0.25));
  colorMap.set("INTERACTIVE_BORDER_FOCUS_LIGHT", Color.white.darker(0.5));
  colorMap.set("INTERACTIVE_LABEL_NORMAL", Color.white);
  colorMap.set("INTERACTIVE_LABEL_DISABLED", Color.gray);
  colorMap.set("MODAL", Color.darkGray);
  colorMap.set("MODAL_BORDER", Color.white);
  colorMap.set("TRANSLUCENT_OVERLAY", new Color(0, 0, 0, 0.25));
  return colorMap;
};

const buildTownNames = (): Map<StateIdx, string[]> => {
  const townNames = new Map<StateIdx, string[]>();

  townNames.set(StateIdx.MISSOURI, [
    "Arrow Rock", "Boonville", "Cave Spring", "Collumbia", "Danville",
    "Kanesville", "Kansas City", "St. Joseph", "Wentzville", "Westport", "Weston"
  ]);

  townNames.set(StateIdx.NEBRASKA_TERRITORY, [
    "Archer", "Ash Hollow", "Brule", "California Hill", "Fairbury",
    "Fort Bridger", "Fort Kearny", "Fort Laramie", "Fort Lisa", "Fort McPherson",
    "Fort Omaha", "Franklin", "Gothenburg", "Guernsey", "Hastings",
    "Homestead", "Lexington", "Lincoln", "Minden", "Nebraska City",
    "North Platte", "Ogallala", "Odell", "O'Fallon's Bluff", "Pawneeville",
    "Ponca Fort", "Red Cloud", "Rock Creek Station", "Saline", "Scottsbluff",
    "Southerland", "Tecumseh", "Windlass Hill", "York", "Cokeville", "Tombstone"
  ]);

  townNames.set(StateIdx.KANSAS_TERRITORY, [
    "Alcove Spring", "Ash Hollow", "Atchison", "Barnes", "Blue Rapids",
    "Casper", "Fair Way", "Fort Bridger", "Fort Coon", "Fort Laramie",
    "Fort Leavenworth", "Fort Riley", "Gardner", "Hollenburg Ranch", "Manhattan",
    "Marysville", "Mount Oread", "Red Vermillion Crossing", "Scott Spring", "St Mary's",
    "Topeka", "Wamego", "Washington", "Julesburg", "Goodnight", "Deadwood", "Latham"
  ]);

  townNames.set(StateIdx.WASHINGTON_TERRITORY, [
    "Alkali", "Boise", "Boardman", "Bridger Outpost", "Casper",
    "Castle Rock", "Dry Sands", "Fort Bridger", "Fort Hall", "Glenn's Ferry",
    "Heppner Junction", "Irrigon", "Pacific Springs", "Shoshone", "South Pass",
    "Split Rock", "Simpson's Hollow", "Umatilla"
  ]);

  townNames.set(StateIdx.UTAH_TERRITORY, [
    "Big Mountain Pass", "Gold Hill", "Hogsback", "Promontory Point", 
    "Southbend", "Tombstone", "Yellow Creek"
  ]);

  townNames.set(StateIdx.COLORADO_TERRITORY, [
    "Alcove Spring", "Ash Hollow", "Atchison", "Barnes", "Blue Rapids",
    "Boulder", "Casper", "Denver", "FairlWay", "Fort Bridger",
    "Fort Coon", "Fort Laramie", "Fort Leavenworth", "Fort Riley", "Gardner",
    "Hollenburg Ranch", "Manhattan", "Marysville", "Mount Oread", "Red Vermillion Crossing",
    "Scott Spring", "St Mary's", "Topeka", "Wamego", "Washington",
    "Julesburg", "Goodnight", "Deadwood", "Latham"
  ]);

  townNames.set(StateIdx.DAKOTA_TERRITORY, [
    "Archer", "Ash Hollow", "Black Hills", "Brule", "California Hill",
    "Fairbury", "Fort Bridger", "Fort Kearny", "Fort Laramie", "Fort Lisa",
    "Fort McPherson", "Franklin", "Gothenburg", "Guernsey", "Hastings",
    "Homestead", "Lexington", "Minden", "North Platte", "Ogallala",
    "Odell", "O'Fallon's Bluff", "Pawneeville", "Ponca Fort", "Red Cloud",
    "Rock Creek Station", "Saline", "Scottsbluff", "Southerland", "Sioux City",
    "Tecumseh", "Windlass Hill", "York", "Cokeville", "Tombstone"
  ]);

  townNames.set(StateIdx.OREGON, [
    "Antelope", "Boyd", "Baker City", "Barlow Gate", "Bourne",
    "Cornucopia", "The Dalles", "Fort Vancouver", "Fort Walla Walla", "Grande Ronde Outpost",
    "Granite", "Granite", "Greenback", "HalfWay", "Horse Heaven",
    "Kent", "Pendleton", "Shaniko", "Sumpter", "Vale",
    "Whitman Mission", "Walla Walla Lake"
  ]);

  return townNames;
};

const buildStateNames = (): Map<StateIdx, string> => {
  const stateNames = new Map<StateIdx, string>();
  stateNames.set(StateIdx.MISSOURI, "Missouri");
  stateNames.set(StateIdx.NEBRASKA_TERRITORY, "Nebraska Territory");
  stateNames.set(StateIdx.KANSAS_TERRITORY, "Kansas Territory");
  stateNames.set(StateIdx.WASHINGTON_TERRITORY, "Washington Territory");
  stateNames.set(StateIdx.UTAH_TERRITORY, "Utah Territory");
  stateNames.set(StateIdx.COLORADO_TERRITORY, "Colorado");
  stateNames.set(StateIdx.DAKOTA_TERRITORY, "Dakota Territory");
  stateNames.set(StateIdx.OREGON, "Oregon");
  return stateNames;
};

export const LITERALS = buildLiterals();
export const COLORS = buildColors();
export const TOWN_NAMES = buildTownNames();
export const STATE_NAMES = buildStateNames();

export function get(outer: string, inner: string): string {
  return LITERALS.get(outer)?.get(inner) ?? "";
}
