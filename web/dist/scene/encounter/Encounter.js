import { getEncounterFrequencies } from "./EncounterID";
export class Encounter {
    constructor(party, value, id) {
        this.party = party;
        this.value = value;
        this.id = id;
    }
    isInRange(num, timeIndex) {
        return num < this.value * getEncounterFrequencies(this.id)[timeIndex];
    }
    getValue(timeIndex) {
        return this.value * getEncounterFrequencies(this.id)[timeIndex];
    }
    getEncounterID() {
        return this.id;
    }
    increaseValue(amount) {
        this.value += amount;
    }
}
//# sourceMappingURL=Encounter.js.map