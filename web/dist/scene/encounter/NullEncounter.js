import { Notification } from "../../model/Notification";
import { Encounter } from "./Encounter";
import { EncounterNotification } from "./EncounterNotification";
import { EncounterID } from "./EncounterID";
export class NullEncounter extends Encounter {
    constructor(party, value) {
        super(party, value, EncounterID.NULL);
    }
    doEncounter() {
        this.value -= 1;
        return this.makeNotification();
    }
    makeNotification() {
        return new EncounterNotification(new Notification("", false), null);
    }
}
//# sourceMappingURL=NullEncounter.js.map