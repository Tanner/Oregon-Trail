import { Condition } from "./Condition";
export class MapObject {
    constructor() {
        this.visible = false;
        this.name = "";
        this.quality = new Condition(0, 100, 100);
    }
    isVisible() {
        return this.visible;
    }
    setVisible(visible) {
        this.visible = visible;
    }
    getName() {
        return this.name;
    }
    setName(name) {
        this.name = name;
    }
    getTerritory() {
        return this.territory;
    }
    setTerritory(territory) {
        this.territory = territory;
    }
    getConditionPercentage() {
        return this.quality.getPercentage();
    }
    getCondition() {
        return this.quality;
    }
    setQuality(quality) {
        this.quality = quality;
    }
    static resetCount() {
        MapObject.count = 0;
    }
}
MapObject.count = 0;
//# sourceMappingURL=MapObject.js.map