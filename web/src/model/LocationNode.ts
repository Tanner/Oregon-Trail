import { MapObject } from "./MapObject";
import { TrailEdge } from "./TrailEdge";
import { Condition } from "./Condition";

export class LocationNode extends MapObject {
  private readonly MAP_X_MAX: number;
  private readonly MAP_Y_MAX: number;
  public readonly WORLD_LATITUDE: number;
  public readonly WORLD_LONGITUDE: number;
  public readonly MAP_XPOS: number;
  public readonly MAP_YPOS: number;
  private playerMapX: number = 0;
  private playerMapY: number = 0;
  public readonly ID: number;
  private rank: number;
  private trails: number;
  private onTheTrail: boolean = false;
  private hasInTrail: boolean = false;
  private outboundTrails: TrailEdge[] = [];

  constructor(
    locationName: string,
    xPos: number,
    yPos: number,
    latitude: number,
    longitude: number,
    trails: number,
    rank: number,
    quality: number,
    MAP_X_MAX: number,
    MAP_Y_MAX: number
  ) {
    super();
    this.ID = MapObject.count++;
    this.MAP_XPOS = xPos;
    this.MAP_YPOS = yPos;
    this.WORLD_LATITUDE = latitude;
    this.WORLD_LONGITUDE = longitude;
    this.trails = trails;
    this.outboundTrails = [];
    this.name = locationName;
    this.rank = rank;
    this.quality = new Condition(0, 100, quality);
    this.visible = false;
    this.MAP_X_MAX = MAP_X_MAX;
    this.MAP_Y_MAX = MAP_Y_MAX;
    this.convertToMapCoords();
  }

  private convertToMapCoords(): void {
    let newX = (this.MAP_XPOS + 60) * (920.0 / this.MAP_X_MAX);
    let newY = ((-1 * this.MAP_YPOS) / 1.4 + 300) + ((this.MAP_XPOS - 600) / 2.4);

    if (newX > 1050) {
      newX = 1050;
    }
    if (newY > 565) {
      newY = 565;
    }

    this.setPlayerMapX(newX);
    this.setPlayerMapY(newY);
  }

  getID(): number {
    return this.ID;
  }

  getHasInTrail(): boolean {
    return this.hasInTrail;
  }

  setHasInTrail(hasInTrail: boolean): void {
    this.hasInTrail = hasInTrail;
  }

  setOnTheTrail(onTheTrail: boolean): void {
    this.onTheTrail = onTheTrail;
  }

  getOnTheTrail(): boolean {
    return this.onTheTrail;
  }

  getRank(): number {
    return this.rank;
  }

  setRank(rank: number): void {
    this.rank = rank;
  }

  getTrails(): number {
    return this.trails;
  }

  setTrails(trails: number): void {
    this.trails = trails;
  }

  addTrail(newTrail: TrailEdge): void {
    this.outboundTrails.push(newTrail);
    newTrail.getDestination().setOnTheTrail(true);
  }

  getOutboundTrails(): TrailEdge[] {
    return this.outboundTrails;
  }

  getPlayerMapX(): number {
    return this.playerMapX;
  }

  setPlayerMapX(playerMapX: number): void {
    this.playerMapX = playerMapX;
  }

  getPlayerMapY(): number {
    return this.playerMapY;
  }

  setPlayerMapY(playerMapY: number): void {
    this.playerMapY = playerMapY;
  }

  getOutBoundTrailByIndex(index: number): TrailEdge {
    return this.outboundTrails[index];
  }

  toString(): string {
    return `${this.name} Rank: ${this.rank} (which has ${this.trails} westward trails)`;
  }

  debugToString(): string {
    let retVal = `Name: \t${this.name}\t| X pos: \t${this.MAP_XPOS} \t| Y pos: \t${this.MAP_YPOS}\n`;
    retVal += `Map X: \t${this.playerMapX}\t| Map Y: ${this.playerMapY}\n`;
    retVal += `Max X: \t${this.MAP_X_MAX}\t| Max Y: ${this.MAP_Y_MAX}\n`;
    retVal += `Internal ID: \t${this.ID}\t| Total Nodes currently made: \t${MapObject.count}\n`;
    retVal += `Location Quality: \t${this.quality}\n`;
    retVal += `Rank: \t\t${this.rank}\t| Total Exit Trail Count: \t${this.trails}\n`;

    if (this.outboundTrails.length === 0) {
      retVal += `\tNo trails implemented from "${this.name}"\n`;
    }

    for (let i = 0; i < this.outboundTrails.length; i++) {
      retVal += `\tExit Trail ${i} from "${this.name}": ${this.outboundTrails[i].debugToString()}`;
    }

    return retVal;
  }
}
