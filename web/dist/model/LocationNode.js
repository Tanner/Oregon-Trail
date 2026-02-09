import { MapObject } from "./MapObject";
import { Condition } from "./Condition";
export class LocationNode extends MapObject {
    constructor(locationName, xPos, yPos, latitude, longitude, trails, rank, quality, MAP_X_MAX, MAP_Y_MAX) {
        super();
        this.playerMapX = 0;
        this.playerMapY = 0;
        this.onTheTrail = false;
        this.hasInTrail = false;
        this.outboundTrails = [];
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
    convertToMapCoords() {
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
    getID() {
        return this.ID;
    }
    getHasInTrail() {
        return this.hasInTrail;
    }
    setHasInTrail(hasInTrail) {
        this.hasInTrail = hasInTrail;
    }
    setOnTheTrail(onTheTrail) {
        this.onTheTrail = onTheTrail;
    }
    getOnTheTrail() {
        return this.onTheTrail;
    }
    getRank() {
        return this.rank;
    }
    setRank(rank) {
        this.rank = rank;
    }
    getTrails() {
        return this.trails;
    }
    setTrails(trails) {
        this.trails = trails;
    }
    addTrail(newTrail) {
        this.outboundTrails.push(newTrail);
        newTrail.getDestination().setOnTheTrail(true);
    }
    getOutboundTrails() {
        return this.outboundTrails;
    }
    getPlayerMapX() {
        return this.playerMapX;
    }
    setPlayerMapX(playerMapX) {
        this.playerMapX = playerMapX;
    }
    getPlayerMapY() {
        return this.playerMapY;
    }
    setPlayerMapY(playerMapY) {
        this.playerMapY = playerMapY;
    }
    getOutBoundTrailByIndex(index) {
        return this.outboundTrails[index];
    }
    toString() {
        return `${this.name} Rank: ${this.rank} (which has ${this.trails} westward trails)`;
    }
    debugToString() {
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
//# sourceMappingURL=LocationNode.js.map