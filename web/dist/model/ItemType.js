import { get } from "../core/ConstantStore";
export var ItemType;
(function (ItemType) {
    ItemType["APPLE"] = "APPLE";
    ItemType["BREAD"] = "BREAD";
    ItemType["AMMO"] = "AMMO";
    ItemType["GUN"] = "GUN";
    ItemType["MEAT"] = "MEAT";
    ItemType["SONIC"] = "SONIC";
    ItemType["WAGON"] = "WAGON";
    ItemType["WHEEL"] = "WHEEL";
    ItemType["OX"] = "OX";
    ItemType["TOOLS"] = "TOOLS";
    ItemType["AXLE"] = "AXLE";
    ItemType["HORSE"] = "HORSE";
    ItemType["MULE"] = "MULE";
    ItemType["STRANGEMEAT"] = "STRANGE_MEAT";
    ItemType["MAP"] = "MAP";
    ItemType["TRADEGOODS"] = "TRADEGOODS";
})(ItemType || (ItemType = {}));
const buildItemTypeData = (type, isFood, isPlant, isAnimal, isTool) => {
    const name = get("ITEMS", `${type}_NAME`);
    const pluralName = get("ITEMS", `${type}_PLURAL_NAME`);
    const description = get("ITEMS", `${type}_DESCRIPTION`);
    const cost = parseInt(get("ITEMS", `${type}_COST`), 10);
    const weight = parseFloat(get("ITEMS", `${type}_WEIGHT`));
    let factor = 0;
    if (isFood) {
        const factorStr = get("ITEMS", `${type}_FOOD_FACTOR`);
        factor = factorStr ? parseInt(factorStr, 10) : 0;
    }
    else if (isTool) {
        const factorStr = get("ITEMS", `${type}_REPAIR_FACTOR`);
        factor = factorStr ? parseInt(factorStr, 10) : 0;
    }
    const necessaryQualityStr = get("ITEMS", `${type}_NECESSARY_QUALITY`);
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
const itemTypeDataMap = {
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
    [ItemType.STRANGEMEAT]: buildItemTypeData("STRANGE_MEAT", true, false, false, false),
    [ItemType.MAP]: buildItemTypeData("MAP", false, false, false, false),
    [ItemType.TRADEGOODS]: buildItemTypeData("TRADEGOODS", false, false, false, false),
};
export function getItemTypeName(itemType) {
    return itemTypeDataMap[itemType].name;
}
export function getItemTypePluralName(itemType) {
    return itemTypeDataMap[itemType].pluralName;
}
export function getItemTypeDescription(itemType) {
    return itemTypeDataMap[itemType].description;
}
export function getItemTypeCost(itemType) {
    return itemTypeDataMap[itemType].cost;
}
export function getItemTypeWeight(itemType) {
    return itemTypeDataMap[itemType].weight;
}
export function isItemTypeFood(itemType) {
    return itemTypeDataMap[itemType].isFood;
}
export function isItemTypePlant(itemType) {
    return itemTypeDataMap[itemType].isPlant;
}
export function isItemTypeAnimal(itemType) {
    return itemTypeDataMap[itemType].isAnimal;
}
export function isItemTypeTool(itemType) {
    return itemTypeDataMap[itemType].isTool;
}
export function getItemTypeFactor(itemType) {
    return itemTypeDataMap[itemType].factor;
}
export function getItemTypeNecessaryQuality(itemType) {
    return itemTypeDataMap[itemType].necessaryQuality;
}
//# sourceMappingURL=ItemType.js.map