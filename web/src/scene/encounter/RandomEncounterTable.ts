import { Party } from "../../model/Party";
import { Encounter } from "./Encounter";
import { EncounterNotification } from "./EncounterNotification";
import { EncounterID } from "./EncounterID";
import { NullEncounter } from "./NullEncounter";
import { ThiefEncounter } from "./ThiefEncounter";
import { ItemEncounter } from "./ItemEncounter";
import { PotholeEncounter } from "./PotholeEncounter";
import { MessageEncounter } from "./MessageEncounter";
import { RiverEncounter } from "./RiverEncounter";

export class RandomEncounterTable {
  private encounters: Encounter[];
  private maxValue: number;

  constructor(encounters: Encounter[]) {
    this.encounters = encounters;
    this.maxValue = 0;
  }

  getRandomEncounter(timeIndex: number): EncounterNotification | null {
    this.maxValue = 0;
    for (const encounter of this.encounters) {
      this.maxValue += encounter.getValue(timeIndex);
    }

    let roll = Math.floor(Math.random() * this.maxValue);

    for (const encounter of this.encounters) {
      console.log(`${encounter} Value: ${encounter.getValue(timeIndex)}`);

      if (encounter instanceof NullEncounter) {
        for (const other of this.encounters) {
          other.increaseValue(1);
        }
        return encounter.doEncounter();
      } else if (encounter.isInRange(roll, timeIndex)) {
        return encounter.doEncounter();
      } else {
        roll -= encounter.getValue(timeIndex);
      }
    }

    return null;
  }

  static createEncounters(party: Party): Encounter[] {
    return [
      new ThiefEncounter(party, 1),
      new ItemEncounter(party, 1),
      new PotholeEncounter(party, 1),
      new MessageEncounter(party, 1),
      new RiverEncounter(party, 1),
      new NullEncounter(party, 1),
    ];
  }
}

export function createEncounter(
  party: Party,
  id: EncounterID,
  value: number,
): Encounter {
  switch (id) {
    case EncounterID.THIEF:
      return new ThiefEncounter(party, value);
    case EncounterID.ITEM:
      return new ItemEncounter(party, value);
    case EncounterID.POTHOLE:
      return new PotholeEncounter(party, value);
    case EncounterID.MESSAGE:
      return new MessageEncounter(party, value);
    case EncounterID.RIVER:
      return new RiverEncounter(party, value);
    case EncounterID.NULL:
    default:
      return new NullEncounter(party, value);
  }
}
