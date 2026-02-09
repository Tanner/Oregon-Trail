import { Party } from "./Party";

export class Player {
  private party: Party | null;

  constructor(party?: Party) {
    this.party = party || null;
    if (party) {
      console.info("Party successfully attached to player");
    }
  }

  getParty(): Party | null {
    return this.party;
  }

  setParty(party: Party): void {
    this.party = party;
  }

  toString(): string {
    return this.party ? this.party.toString() : "No party";
  }
}
