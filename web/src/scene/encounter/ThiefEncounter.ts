import { Party } from "../../model/Party";
import { Notification } from "../../model/Notification";
import { Person } from "../../model/Person";
import { Vehicle } from "../../model/Vehicle";
import { ItemType, getItemTypeName } from "../../model/ItemType";
import { Encounter } from "./Encounter";
import { EncounterNotification } from "./EncounterNotification";
import { EncounterID } from "./EncounterID";

interface Inventoried {
  getInventory(): any;
}

export class ThiefEncounter extends Encounter {
  private itemName: string;

  constructor(party: Party, value: number) {
    super(party, value, EncounterID.THIEF);
    this.itemName = "";
  }

  doEncounter(): EncounterNotification {
    const inventories: Inventoried[] = [];
    const vehicle = this.party.getVehicle();
    if (vehicle) {
      inventories.push(vehicle as unknown as Inventoried);
    }
    inventories.push(...(this.party.getPartyMembers() as unknown as Inventoried[]));

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

  protected makeNotification(): EncounterNotification {
    let message: string;

    if (this.itemName.length > 0) {
      message = `Oh no, a thief took your ${this.itemName}.  Hope you can survive without it.`;
    } else {
      message =
        "A thief approached your party, but you didn't even have anything to steal!  Hope you make it to the next town.";
    }

    return new EncounterNotification(new Notification(message, true), null);
  }
}
