export var EncounterID;
(function (EncounterID) {
    EncounterID["THIEF"] = "THIEF";
    EncounterID["ITEM"] = "ITEM";
    EncounterID["POTHOLE"] = "POTHOLE";
    EncounterID["MESSAGE"] = "MESSAGE";
    EncounterID["RIVER"] = "RIVER";
    EncounterID["NULL"] = "NULL";
})(EncounterID || (EncounterID = {}));
const encounterData = {
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
export function getEncounterName(id) {
    return encounterData[id].name;
}
export function getEncounterFrequencies(id) {
    return encounterData[id].frequencies;
}
//# sourceMappingURL=EncounterID.js.map