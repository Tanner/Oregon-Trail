import { Item } from "./Item";
import { Inventory } from "./Inventory";
import * as ConstantStore from "../core/ConstantStore";
export class Vehicle extends Item {
    constructor(type) {
        super(type);
        this.MAX_INVENTORY_SIZE = parseInt(ConstantStore.get("ITEMS", `${type}_MAX_INV_SIZE`), 10);
        this.MAX_INVENTORY_WEIGHT = parseFloat(ConstantStore.get("ITEMS", `${type}_MAX_INV_WEIGHT`));
        this.cargo = new Inventory(this.MAX_INVENTORY_SIZE, this.MAX_INVENTORY_WEIGHT);
    }
    getStatus() {
        return this.getCondition().copy();
    }
    increaseStatus(amount) {
        this.getCondition().increase(amount);
    }
    decreaseStatus(amount) {
        this.getCondition().decrease(amount);
    }
    getInventory() {
        return this.cargo;
    }
    addItemsToInventory(items) {
        this.cargo.addItemsToInventory(items);
    }
    addItemToInventory(item) {
        this.cargo.addItemToInventory(item);
    }
    removeItemFromInventory(itemType, quantity) {
        return this.cargo.removeItemFromInventory(itemType, quantity);
    }
    canGetItem(itemType, numberOf) {
        return this.cargo.canGetItems(itemType, numberOf);
    }
    getMaxSize() {
        return this.MAX_INVENTORY_SIZE;
    }
    getMaxWeight() {
        return this.MAX_INVENTORY_WEIGHT;
    }
    getWeight() {
        return this.cargo.getWeight();
    }
    repair(amount) {
        this.getCondition().increase(amount);
    }
}
//# sourceMappingURL=Vehicle.js.map