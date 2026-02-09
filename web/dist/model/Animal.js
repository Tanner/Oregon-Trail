import { Item } from "./Item";
import { ItemType, getItemTypeWeight } from "./ItemType";
import * as ConstantStore from "../core/ConstantStore";
export class Animal extends Item {
    constructor(type) {
        super(type);
        const moveFactorStr = ConstantStore.get("ITEMS", `${type}_MOVE_FACTOR`);
        this.moveFactor = moveFactorStr ? parseFloat(moveFactorStr) : 0;
        this.dead = false;
    }
    killForFood() {
        const meatWeight = getItemTypeWeight(ItemType.MEAT);
        const numberOf = Math.floor(this.getWeight() / meatWeight);
        const itemList = [];
        for (let i = 0; i < Math.floor(numberOf / 10); i++) {
            itemList.push(new Item(ItemType.MEAT));
        }
        return itemList;
    }
    getMoveFactor() {
        return this.moveFactor;
    }
    setDead(dead) {
        this.dead = dead;
    }
    isDead() {
        return this.dead;
    }
}
//# sourceMappingURL=Animal.js.map