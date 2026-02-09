import { ConstantStore } from "../core/ConstantStore";

export enum ItemType {
  APPLE = "APPLE",
  BREAD = "BREAD",
  AMMO = "AMMO",
  GUN = "GUN",
  MEAT = "MEAT",
  SONIC = "SONIC",
  WAGON = "WAGON",
  WHEEL = "WHEEL",
  OX = "OX",
  TOOLS = "TOOLS",
  AXLE = "AXLE",
  HORSE = "HORSE",
  MULE = "MULE",
  STRANGEMEAT = "STRANGE_MEAT",
  MAP = "MAP",
  TRADEGOODS = "TRADEGOODS",
}

interface ItemTypeData {
  readonly name: string;
  readonly pluralName: string;
  readonly description: string;
  readonly cost: number;
  readonly weight: number;
  readonly isFood: boolean;
  readonly isPlant: boolean;
  readonly isAnimal: boolean;
  readonly isTool: boolean;
  readonly factor: number;
  readonly necessaryQuality: number;
}

const buildItemTypeData = (
  type: string,
  isFood: boolean,
  isPlant: boolean,
  isAnimal: boolean,
  isTool: boolean,
): ItemTypeData => {
  const name = ConstantStore.get("ITEMS", `${type}_NAME`);
  const pluralName = ConstantStore.get("ITEMS", `${type}_PLURAL_NAME`);
  const description = ConstantStore.get("ITEMS", `${type}_DESCRIPTION`);
  const cost = parseInt(ConstantStore.get("ITEMS", `${type}_COST`), 10);
  const weight = parseFloat(ConstantStore.get("ITEMS", `${type}_WEIGHT`));

  let factor = 0;
  if (isFood) {
    const factorStr = ConstantStore.get("ITEMS", `${type}_FOOD_FACTOR`);
    factor = factorStr ? parseInt(factorStr, 10) : 0;
  } else if (isTool) {
    const factorStr = ConstantStore.get("ITEMS", `${type}_REPAIR_FACTOR`);
    factor = factorStr ? parseInt(factorStr, 10) : 0;
  }

  const necessaryQualityStr = ConstantStore.get(
    "ITEMS",
    `${type}_NECESSARY_QUALITY`,
  );
  const necessaryQuality = necessaryQualityStr
    ? parseInt(necessaryQualityStr, 10)
    : 0;

  return {
    name,
    pluralName,
    description,
    cost,
    weight,
    isFood,
    isPlant,
    isAnimal,
    isTool,
    factor,
    necessaryQuality,
  };
};

const itemTypeDataMap: Record<ItemType, ItemTypeData> = {
  [ItemType.APPLE]: buildItemTypeData("APPLE", true, true, false, false),
  [ItemType.BREAD]: buildItemTypeData("BREAD", true, false, false, false),
  [ItemType.AMMO]: buildItemTypeData("AMMO", false, false, false, false),
  [ItemType.GUN]: buildItemTypeData("GUN", false, false, false, false),
  [ItemType.MEAT]: buildItemTypeData("MEAT", true, false, false, false),
  [ItemType.SONIC]: buildItemTypeData("SONIC", false, false, false, false),
  [ItemType.WAGON]: buildItemTypeData("WAGON", false, false, false, false),
  [ItemType.WHEEL]: buildItemTypeData("WHEEL", false, false, false, true),
  [ItemType.OX]: buildItemTypeData("OX", false, false, true, false),
  [ItemType.TOOLS]: buildItemTypeData("TOOLS", false, false, false, true),
  [ItemType.AXLE]: buildItemTypeData("AXLE", false, false, false, true),
  [ItemType.HORSE]: buildItemTypeData("HORSE", false, false, true, false),
  [ItemType.MULE]: buildItemTypeData("MULE", false, false, true, false),
  [ItemType.STRANGEMEAT]: buildItemTypeData(
    "STRANGE_MEAT",
    true,
    false,
    false,
    false,
  ),
  [ItemType.MAP]: buildItemTypeData("MAP", false, false, false, false),
  [ItemType.TRADEGOODS]: buildItemTypeData(
    "TRADEGOODS",
    false,
    false,
    false,
    false,
  ),
};

export function getItemTypeName(itemType: ItemType): string {
  return itemTypeDataMap[itemType].name;
}

export function getItemTypePluralName(itemType: ItemType): string {
  return itemTypeDataMap[itemType].pluralName;
}

export function getItemTypeDescription(itemType: ItemType): string {
  return itemTypeDataMap[itemType].description;
}

export function getItemTypeCost(itemType: ItemType): number {
  return itemTypeDataMap[itemType].cost;
}

export function getItemTypeWeight(itemType: ItemType): number {
  return itemTypeDataMap[itemType].weight;
}

export function isItemTypeFood(itemType: ItemType): boolean {
  return itemTypeDataMap[itemType].isFood;
}

export function isItemTypePlant(itemType: ItemType): boolean {
  return itemTypeDataMap[itemType].isPlant;
}

export function isItemTypeAnimal(itemType: ItemType): boolean {
  return itemTypeDataMap[itemType].isAnimal;
}

export function isItemTypeTool(itemType: ItemType): boolean {
  return itemTypeDataMap[itemType].isTool;
}

export function getItemTypeFactor(itemType: ItemType): number {
  return itemTypeDataMap[itemType].factor;
}

export function getItemTypeNecessaryQuality(itemType: ItemType): number {
  return itemTypeDataMap[itemType].necessaryQuality;
}
