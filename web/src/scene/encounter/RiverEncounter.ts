import { Party } from "../../model/Party";
import { Notification } from "../../model/Notification";
import { Encounter } from "./Encounter";
import { EncounterNotification } from "./EncounterNotification";
import { EncounterID } from "./EncounterID";

export class RiverEncounter extends Encounter {
  constructor(party: Party, value: number) {
    super(party, value, EncounterID.RIVER);
  }

  doEncounter(): EncounterNotification {
    return this.makeNotification();
  }

  protected makeNotification(): EncounterNotification {
    const message = "You've reached a river.  Make your decision wisely!";
    return new EncounterNotification(new Notification(message, true), "RIVER");
  }
}
