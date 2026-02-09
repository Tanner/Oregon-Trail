import { MapObject } from "./MapObject";
import { LocationNode } from "./LocationNode";
import { Condition } from "./Condition";

export class TrailEdge extends MapObject {
  private destination: LocationNode;
  private origin: LocationNode;
  private dangerLevel: number;
  private length: number;
  private static longestTrail: number = 0;
  private static shortestTrail: number = 9999;
  private static totalTrailLength: number = 0;
  private readonly ID: number;
  private taken: boolean = false;

  constructor(destination: LocationNode, origin: LocationNode, dangerLevel: number) {
    super();
    this.destination = destination;
    this.origin = origin;
    this.ID = MapObject.count++;
    this.dangerLevel = dangerLevel;
    this.name = "Trail to " + destination.getName();
    this.length = this.calcDistance(
      destination.MAP_XPOS,
      origin.MAP_XPOS,
      destination.MAP_YPOS,
      origin.MAP_YPOS
    );

    if (this.length > TrailEdge.longestTrail) {
      TrailEdge.longestTrail = this.length;
    }
    if (this.length < TrailEdge.shortestTrail) {
      TrailEdge.shortestTrail = this.length;
    }

    this.quality = new Condition(0, this.length);
    TrailEdge.totalTrailLength += this.length;
    this.visible = false;
  }

  private calcDistance(destX: number, origX: number, destY: number, origY: number): number {
    try {
      return Math.sqrt(Math.pow(origX - destX, 2) + Math.pow(origY - destY, 2));
    } catch (e) {
      console.error("Error calculating distance between two nodes in map generation", e);
      return 0;
    }
  }

  advance(movement: number): void {
    this.quality.decrease(movement);
  }

  getDistanceToGo(): number {
    return this.quality.getCurrent();
  }

  getRoughDistanceToGo(): string {
    const current = 1 - this.quality.getPercentage();
    if (current < 0.05) {
      return "Just starting toward ";
    } else if (current < 0.25) {
      return "Nowhere close to ";
    } else if (current < 0.5) {
      return "Getting closer to ";
    } else if (current < 0.75) {
      return "More than halfway to ";
    } else if (current < 0.95) {
      return "Just a little further to ";
    } else {
      return "On the outskirts of ";
    }
  }

  getRoughDirection(): string {
    let resStr: string;

    if (this.destination.MAP_YPOS > this.origin.MAP_YPOS) {
      resStr = "North West";
    } else if (this.destination.MAP_YPOS < this.origin.MAP_YPOS) {
      resStr = "South West";
    } else {
      resStr = "West";
    }

    if (this.destination.getRank() === this.origin.getRank()) {
      const spaceIndex = resStr.indexOf(' ');
      if (spaceIndex > 0) {
        resStr = resStr.substring(0, spaceIndex);
      }
    }

    return resStr;
  }

  getDestination(): LocationNode {
    return this.destination;
  }

  getOrigin(): LocationNode {
    return this.origin;
  }

  getDangerLevel(): number {
    return this.dangerLevel;
  }

  getDangerRating(): string {
    if (this.dangerLevel < 10) {
      return "Established";
    } else if (this.dangerLevel < 40) {
      return "Well-Travelled";
    } else if (this.dangerLevel < 55) {
      return "Rarely Travelled";
    } else if (this.dangerLevel < 90) {
      return "Wilderness";
    } else {
      return "Indian Lands";
    }
  }

  getRoughLength(): string {
    const avgLength = TrailEdge.totalTrailLength / MapObject.count;
    if (this.length < 0.1 * avgLength) {
      return "Very Short";
    } else if (this.length < 0.5 * avgLength) {
      return "Short";
    } else if (this.length < avgLength) {
      return "Average";
    } else if (this.length < 1.5 * avgLength) {
      return "Long";
    } else if (this.length < 2 * avgLength) {
      return "Very Long";
    } else {
      return "Endless";
    }
  }

  getCurrTrailLocationX(): number {
    const percentComplete = 1 - this.quality.getPercentage();
    return this.origin.getPlayerMapX() + 
           (this.destination.getPlayerMapX() - this.origin.getPlayerMapX()) * percentComplete;
  }

  getCurrTrailLocationY(): number {
    const percentComplete = 1 - this.quality.getPercentage();
    return this.origin.getPlayerMapY() + 
           (this.destination.getPlayerMapY() - this.origin.getPlayerMapY()) * percentComplete;
  }

  isTaken(): boolean {
    return this.taken;
  }

  setTaken(taken: boolean): void {
    this.taken = taken;
  }

  static resetTrails(): void {
    TrailEdge.longestTrail = 0;
    TrailEdge.shortestTrail = 9999;
    TrailEdge.totalTrailLength = 0;
  }

  debugToString(): string {
    return `\n\tLength: ${this.length} = ${this.getRoughLength()} Danger: ${this.dangerLevel} = "${this.getDangerRating()}" Trail ID: ${this.ID} Trail Name: "${this.name}" Rank: ${this.origin.getRank()} to ${this.destination.getRank()}\n`;
  }

  toString(): string {
    return `${this.getRoughDirection()} ${this.name} from ${this.origin}`;
  }
}
