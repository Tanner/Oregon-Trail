import { Condition } from "./Condition";
import { getItemTypeName, getItemTypeDescription, getItemTypeCost, getItemTypeWeight, } from "./ItemType";
export class Item {
    constructor(type) {
        this.status = new Condition(100);
        this.type = type;
        this.isStackable = true;
    }
    getName() {
        return getItemTypeName(this.type);
    }
    getDescription() {
        return getItemTypeDescription(this.type);
    }
    getCost() {
        return Math.floor(getItemTypeCost(this.type) * this.status.getPercentage());
    }
    getStatus() {
        return this.status.copy();
    }
    getConditionPercentage() {
        return this.status.getPercentage();
    }
    increaseStatus(amount) {
        this.status.increase(amount);
        if (this.status.getCurrent() === this.status.getMax()) {
            this.isStackable = true;
        }
    }
    decreaseStatus(amount) {
        this.status.decrease(amount);
        if (this.status.getCurrent() < this.status.getMax()) {
            this.isStackable = false;
        }
    }
    getWeight() {
        return getItemTypeWeight(this.type);
    }
    isItemStackable() {
        return this.isStackable;
    }
    compareTo(other) {
        const diff = this.getConditionPercentage() - other.getConditionPercentage();
        if (Math.abs(diff) < 0.000001) {
            return 0;
        }
        return diff < 0 ? -1 : 1;
    }
    getType() {
        return this.type;
    }
    toString() {
        return getItemTypeName(this.type);
    }
    getCondition() {
        return this.status;
    }
}
//# sourceMappingURL=Item.js.map