export class Player {
    constructor(party) {
        this.party = party || null;
        if (party) {
            console.info("Party successfully attached to player");
        }
    }
    getParty() {
        return this.party;
    }
    setParty(party) {
        this.party = party;
    }
    toString() {
        return this.party ? this.party.toString() : "No party";
    }
}
//# sourceMappingURL=Player.js.map