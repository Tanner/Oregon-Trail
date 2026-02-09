import { Notification } from "../../model/Notification";
import { getItemTypeName } from "../../model/ItemType";
import { Encounter } from "./Encounter";
import { EncounterNotification } from "./EncounterNotification";
import { EncounterID } from "./EncounterID";
export class ThiefEncounter extends Encounter {
    constructor(party, value) {
        super(party, value, EncounterID.THIEF);
        this.itemName = "";
    }
    doEncounter() {
        const inventories = [];
        const vehicle = this.party.getVehicle();
        if (vehicle) {
            inventories.push(vehicle);
        }
        inventories.push(...this.party.getPartyMembers());
        let removed = false;
        while (inventories.length > 0 && !removed) {
            const randomIndex = Math.floor(Math.random() * inventories.length);
            const inventory = inventories.splice(randomIndex, 1)[0];
            if (!inventory.getInventory().isEmpty()) {
                removed = true;
                const items = inventory.getInventory().getPopulatedSlots();
                const type = items[Math.floor(Math.random() * items.length)];
                const count = inventory.getInventory().getNumberOf(type);
                inventory
                    .getInventory()
                    .removeItemFromInventory(type, Math.floor(Math.random() * count) + 1);
                this.itemName = getItemTypeName(type);
            }
        }
        this.value = Math.floor(this.value / 5);
        return this.makeNotification();
    }
    makeNotification() {
        let message;
        if (this.itemName.length > 0) {
            message = `Oh no, a thief took your ${this.itemName}.  Hope you can survive without it.`;
        }
        else {
            message =
                "A thief approached your party, but you didn't even have anything to steal!  Hope you make it to the next town.";
        }
        return new EncounterNotification(new Notification(message, true), null);
    }
}
//# sourceMappingURL=ThiefEncounter.js.map