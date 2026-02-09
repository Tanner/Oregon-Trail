import { Party } from "../../model/Party";
import { EncounterNotification } from "./EncounterNotification";
import { EncounterID, getEncounterFrequencies } from "./EncounterID";

export abstract class Encounter {
  protected value: number;
  protected party: Party;
  protected id: EncounterID;

  constructor(party: Party, value: number, id: EncounterID) {
    this.party = party;
    this.value = value;
    this.id = id;
  }

  isInRange(num: number, timeIndex: number): boolean {
    return num < this.value * getEncounterFrequencies(this.id)[timeIndex];
  }

  getValue(timeIndex: number): number {
    return this.value * getEncounterFrequencies(this.id)[timeIndex];
  }

  abstract doEncounter(): EncounterNotification;

  protected abstract makeNotification(): EncounterNotification;

  getEncounterID(): EncounterID {
    return this.id;
  }

  increaseValue(amount: number): void {
    this.value += amount;
  }
}
