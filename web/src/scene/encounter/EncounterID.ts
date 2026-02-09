export enum EncounterID {
  THIEF = "THIEF",
  ITEM = "ITEM",
  POTHOLE = "POTHOLE",
  MESSAGE = "MESSAGE",
  RIVER = "RIVER",
  NULL = "NULL",
}

const encounterData: Record<
  EncounterID,
  { name: string; frequencies: number[] }
> = {
  [EncounterID.THIEF]: { name: "Thief Encounter", frequencies: [0, 0, 3, 5] },
  [EncounterID.ITEM]: { name: "Item Encounter", frequencies: [1, 2, 1, 0] },
  [EncounterID.POTHOLE]: { name: "Pothole Encounter", frequencies: [1, 1, 1, 2] },
  [EncounterID.MESSAGE]: {
    name: "Message Encounter",
    frequencies: [2, 2, 2, 2],
  },
  [EncounterID.RIVER]: { name: "River Encounter", frequencies: [1, 1, 0, 0] },
  [EncounterID.NULL]: {
    name: "Null Encounter",
    frequencies: [100, 100, 100, 100],
  },
};

export function getEncounterName(id: EncounterID): string {
  return encounterData[id].name;
}

export function getEncounterFrequencies(id: EncounterID): number[] {
  return encounterData[id].frequencies;
}
