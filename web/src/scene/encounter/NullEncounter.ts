import { Party } from "../../model/Party";
import { Notification } from "../../model/Notification";
import { Encounter } from "./Encounter";
import { EncounterNotification } from "./EncounterNotification";
import { EncounterID } from "./EncounterID";

export class NullEncounter extends Encounter {
  constructor(party: Party, value: number) {
    super(party, value, EncounterID.NULL);
  }

  doEncounter(): EncounterNotification {
    this.value -= 1;
    return this.makeNotification();
  }

  protected makeNotification(): EncounterNotification {
    return new EncounterNotification(new Notification("", false), null);
  }
}
