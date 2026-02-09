import { Item } from "./Item";
import { ItemType, isItemTypeAnimal } from "./ItemType";
export class Inventory {
    constructor(maxSize, maxWeight) {
        this.MAX_SIZE = maxSize;
        this.MAX_WEIGHT = maxWeight;
        this.currentSize = 0;
        this.slots = [];
        const itemTypes = Object.values(ItemType);
        for (let i = 0; i < itemTypes.length; i++) {
            this.slots.push([]);
        }
    }
    getMaxSize() {
        return this.MAX_SIZE;
    }
    getPopulatedSlots() {
        const popSlots = [];
        const itemTypes = Object.values(ItemType);
        for (const itemType of itemTypes) {
            if (this.getNumberOf(itemType) !== 0) {
                popSlots.push(itemType);
            }
        }
        return popSlots;
    }
    getCurrentSize() {
        return this.currentSize;
    }
    getWeight() {
        let weight = 0;
        for (const slot of this.slots) {
            for (const item of slot) {
                weight += item.getWeight();
            }
        }
        return weight;
    }
    canGetItems(itemType, numberOf) {
        if (numberOf <= 0) {
            return false;
        }
        if (isItemTypeAnimal(itemType)) {
            return true;
        }
        const weight = itemType ? this.getItemTypeWeight(itemType) * numberOf : 0;
        if (this.getWeight() + weight > this.MAX_WEIGHT) {
            console.debug("Not enough weight capacity");
            return false;
        }
        else if (this.currentSize === this.MAX_SIZE &&
            this.getNumberOf(itemType) === 0) {
            console.debug("Not enough slots open");
            return false;
        }
        return true;
    }
    getItemTypeWeight(itemType) {
        const itemTypes = Object.values(ItemType);
        const ordinal = itemTypes.indexOf(itemType);
        if (ordinal === -1 || this.slots[ordinal].length === 0) {
            return 0;
        }
        return this.slots[ordinal][0].getWeight();
    }
    addItemsToInventory(itemsToAdd) {
        if (itemsToAdd.length === 0) {
            return;
        }
        const itemType = itemsToAdd[0].getType();
        if (!this.canGetItems(itemType, itemsToAdd.length)) {
            console.debug("Add item failed");
            return;
        }
        const itemTypes = Object.values(ItemType);
        const ordinal = itemTypes.indexOf(itemType);
        for (const item of itemsToAdd) {
            this.slots[ordinal].push(item);
            console.debug(`${item.getName()} added.`);
        }
        this.slots[ordinal].sort((a, b) => a.compareTo(b));
        this.recalculateCurrentSize();
    }
    addItemToInventory(item) {
        this.addItemsToInventory([item]);
    }
    removeItemFromInventory(itemType, quantity) {
        const itemTypes = Object.values(ItemType);
        const itemIndex = itemTypes.indexOf(itemType);
        if (this.slots[itemIndex].length < quantity) {
            console.debug("Not enough items to remove");
            return null;
        }
        const removedItems = [];
        for (let i = 0; i < quantity; i++) {
            const item = this.slots[itemIndex].shift();
            if (item) {
                removedItems.push(item);
            }
        }
        console.debug("Items removed successfully");
        this.recalculateCurrentSize();
        return removedItems;
    }
    recalculateCurrentSize() {
        this.currentSize = 0;
        for (const slot of this.slots) {
            if (slot.length > 0) {
                this.currentSize += 1;
            }
        }
    }
    isFull() {
        return this.slots.length === this.MAX_SIZE;
    }
    isEmpty() {
        return this.getCurrentSize() === 0;
    }
    getNumberOf(itemType) {
        const itemTypes = Object.values(ItemType);
        const ordinal = itemTypes.indexOf(itemType);
        return this.slots[ordinal].length;
    }
    getNumberOfItems() {
        let size = 0;
        for (const slot of this.slots) {
            size += slot.length;
        }
        return size;
    }
    getConditionOf(itemType) {
        const count = this.getNumberOf(itemType);
        if (count === 0) {
            return null;
        }
        const itemTypes = Object.values(ItemType);
        const ordinal = itemTypes.indexOf(itemType);
        return this.slots[ordinal][0].getStatus();
    }
    clear() {
        for (const slot of this.slots) {
            slot.length = 0;
        }
        this.currentSize = 0;
    }
    addRandomItems() {
        const numberOfItemsToAdd = Math.floor(Math.random() * this.getMaxSize()) + 2;
        const itemTypes = Object.values(ItemType);
        for (let i = 0; i < numberOfItemsToAdd; i++) {
            let item;
            let attempts = 0;
            do {
                const randomItem = Math.floor(Math.random() * itemTypes.length);
                item = new Item(itemTypes[randomItem]);
                attempts++;
            } while (!this.canGetItems(item.getType(), 1) && attempts < itemTypes.length);
            item.decreaseStatus(Math.floor(Math.random() * 101));
            const itemType = item.getType();
            if (!isItemTypeAnimal(itemType) &&
                itemType !== ItemType.STRANGEMEAT &&
                itemType !== ItemType.WAGON &&
                itemType !== ItemType.SONIC &&
                itemType !== ItemType.MAP) {
                this.addItemToInventory(item);
            }
        }
    }
    toString() {
        const popSlots = this.getPopulatedSlots();
        let str = `Size: ${popSlots.length}. `;
        for (const itemType of popSlots) {
            const itemTypes = Object.values(ItemType);
            const ordinal = itemTypes.indexOf(itemType);
            const name = this.slots[ordinal][0]?.getName() || "";
            str += ` # of ${name}s: ${this.getNumberOf(itemType)}`;
        }
        return str;
    }
    getMaxWeight() {
        return this.MAX_WEIGHT;
    }
}
//# sourceMappingURL=Inventory.js.map