import { Notification } from "../../model/Notification";
import { Encounter } from "./Encounter";
import { EncounterNotification } from "./EncounterNotification";
import { EncounterID } from "./EncounterID";
export class PotholeEncounter extends Encounter {
    constructor(party, value) {
        super(party, value, EncounterID.POTHOLE);
        this.message = "";
    }
    doEncounter() {
        const vehicle = this.party.getVehicle();
        if (!vehicle) {
            this.message =
                "You step around a huge hole on the trail.  You wonder what would have happened to your wagon if you ran over it.";
        }
        else {
            const condition = vehicle.getStatus();
            const max = condition.getMax();
            const damage = max * PotholeEncounter.MAX_DECREASE * Math.random();
            this.party.damageVehicle(Math.floor(damage));
            this.message =
                "You weren't paying attention and ran your wagon over a huge pothole.";
            if (this.party.getVehicle()?.getConditionPercentage() === 0) {
                this.message += "  Your vehicle has broken down.";
                if (this.party.repairVehicle() === 0) {
                    this.message += "  However, you managed to fix it.";
                }
                else {
                    if (this.party.getVehicle()) {
                        this.message += "  You managed to fix it enough to keep going.";
                    }
                    else {
                        this.message +=
                            "  You couldn't repair your wagon and had to abandon it.";
                    }
                }
            }
            else {
                this.message += "  Better check your wagon for damages.";
            }
        }
        return this.makeNotification();
    }
    makeNotification() {
        return new EncounterNotification(new Notification(this.message), null);
    }
}
PotholeEncounter.MAX_DECREASE = 0.25;
//# sourceMappingURL=PotholeEncounter.js.map