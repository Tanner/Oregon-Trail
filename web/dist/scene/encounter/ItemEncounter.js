import { Item } from "../../model/Item";
import { Animal } from "../../model/Animal";
import { Notification } from "../../model/Notification";
import { ItemType, isItemTypeAnimal, getItemTypeName, getItemTypePluralName, } from "../../model/ItemType";
import { Encounter } from "./Encounter";
import { EncounterNotification } from "./EncounterNotification";
import { EncounterID } from "./EncounterID";
var ItemEncounterType;
(function (ItemEncounterType) {
    ItemEncounterType["APPLE"] = "APPLE";
    ItemEncounterType["BREAD"] = "BREAD";
    ItemEncounterType["GUN"] = "GUN";
    ItemEncounterType["OX"] = "OX";
})(ItemEncounterType || (ItemEncounterType = {}));
const itemEncounterData = {
    [ItemEncounterType.APPLE]: {
        itemType: ItemType.APPLE,
        message: "You rest by an apple tree. You pick up %d %s.",
    },
    [ItemEncounterType.BREAD]: {
        itemType: ItemType.BREAD,
        message: "You find a basket of bread which contains %d %s %s. You decide to take it.",
    },
    [ItemEncounterType.GUN]: {
        itemType: ItemType.GUN,
        message: "You find %d rusted %s on the trail. It seems like it still works.",
    },
    [ItemEncounterType.OX]: {
        itemType: ItemType.OX,
        message: "You find %d lonely %s. You let the %s join your party.",
    },
};
function getItemEncounterMessage(type, number) {
    const data = itemEncounterData[type];
    const itemName = number > 1
        ? getItemTypePluralName(data.itemType).toLowerCase()
        : getItemTypeName(data.itemType).toLowerCase();
    let itemPrefix = "pieces of";
    if (getItemTypePluralName(data.itemType) === getItemTypeName(data.itemType)) {
        if (number === 1) {
            itemPrefix = "piece of";
        }
    }
    return data.message
        .replace("%d", number.toString())
        .replace("%s", itemPrefix)
        .replace("%s", itemName);
}
export class ItemEncounter extends Encounter {
    constructor(party, value) {
        super(party, value, EncounterID.ITEM);
        const types = Object.values(ItemEncounterType);
        const index = Math.floor(Math.random() * types.length);
        this.type = types[index];
        this.numItems = 0;
    }
    doEncounter() {
        this.numItems = Math.floor(Math.random() * 3) + 1;
        const itemType = itemEncounterData[this.type].itemType;
        if (isItemTypeAnimal(itemType)) {
            for (let i = 0; i < this.numItems; i++) {
                this.party.addAnimals(new Animal(itemType));
            }
        }
        else if (this.party.getVehicle()) {
            for (let i = 0; i < this.numItems; i++) {
                this.party.getVehicle().addItemToInventory(new Item(itemType));
            }
        }
        this.value = Math.floor(this.value / 4);
        return this.makeNotification();
    }
    makeNotification() {
        const message = getItemEncounterMessage(this.type, this.numItems);
        return new EncounterNotification(new Notification(message), null);
    }
}
//# sourceMappingURL=ItemEncounter.js.map