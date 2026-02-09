import { Notification } from "../../model/Notification";
import { Encounter } from "./Encounter";
import { EncounterNotification } from "./EncounterNotification";
import { EncounterID } from "./EncounterID";
export class MessageEncounter extends Encounter {
    constructor(party, value) {
        super(party, value, EncounterID.MESSAGE);
        this.messages = [
            "You notice something staring at you from behind the trees.  It might be best to keep moving.",
            "Your party admires the forest for a bit before moving ahead.",
            'Your party sings "Oh! Susanna" to increase morale.',
            "You pause to think about your bright future in Oregon.",
            "Them yonder hills sure are lookin' familiar.",
            "I heard our neighbors, the Donners, took this trail a few weeks ahead of us.\nSuch nice, quiet folks.",
        ];
    }
    doEncounter() {
        this.value = Math.floor(this.value / 2);
        return this.makeNotification();
    }
    makeNotification() {
        const message = this.messages[Math.floor(Math.random() * this.messages.length)];
        return new EncounterNotification(new Notification(message), null);
    }
}
//# sourceMappingURL=MessageEncounter.js.map