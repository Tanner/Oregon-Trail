import { Notification } from "../../model/Notification";
import { Encounter } from "./Encounter";
import { EncounterNotification } from "./EncounterNotification";
import { EncounterID } from "./EncounterID";
export class RiverEncounter extends Encounter {
    constructor(party, value) {
        super(party, value, EncounterID.RIVER);
    }
    doEncounter() {
        return this.makeNotification();
    }
    makeNotification() {
        const message = "You've reached a river.  Make your decision wisely!";
        return new EncounterNotification(new Notification(message, true), "RIVER");
    }
}
//# sourceMappingURL=RiverEncounter.js.map