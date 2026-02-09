import { Item } from "./Item";
import { ItemType, isItemTypeAnimal } from "./ItemType";
import { Condition } from "./Condition";

export class Inventory {
  private readonly slots: Item[][];
  private readonly MAX_SIZE: number;
  private readonly MAX_WEIGHT: number;
  private currentSize: number;

  constructor(maxSize: number, maxWeight: number) {
    this.MAX_SIZE = maxSize;
    this.MAX_WEIGHT = maxWeight;
    this.currentSize = 0;

    this.slots = [];
    const itemTypes = Object.values(ItemType);
    for (let i = 0; i < itemTypes.length; i++) {
      this.slots.push([]);
    }
  }

  getMaxSize(): number {
    return this.MAX_SIZE;
  }

  getPopulatedSlots(): ItemType[] {
    const popSlots: ItemType[] = [];
    const itemTypes = Object.values(ItemType);
    for (const itemType of itemTypes) {
      if (this.getNumberOf(itemType) !== 0) {
        popSlots.push(itemType);
      }
    }
    return popSlots;
  }

  getCurrentSize(): number {
    return this.currentSize;
  }

  getWeight(): number {
    let weight = 0;
    for (const slot of this.slots) {
      for (const item of slot) {
        weight += item.getWeight();
      }
    }
    return weight;
  }

  canGetItems(itemType: ItemType, numberOf: number): boolean {
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
    } else if (
      this.currentSize === this.MAX_SIZE &&
      this.getNumberOf(itemType) === 0
    ) {
      console.debug("Not enough slots open");
      return false;
    }

    return true;
  }

  private getItemTypeWeight(itemType: ItemType): number {
    const itemTypes = Object.values(ItemType);
    const ordinal = itemTypes.indexOf(itemType);
    if (ordinal === -1 || this.slots[ordinal].length === 0) {
      return 0;
    }
    return this.slots[ordinal][0].getWeight();
  }

  addItemsToInventory(itemsToAdd: Item[]): void {
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

  addItemToInventory(item: Item): void {
    this.addItemsToInventory([item]);
  }

  removeItemFromInventory(
    itemType: ItemType,
    quantity: number,
  ): Item[] | null {
    const itemTypes = Object.values(ItemType);
    const itemIndex = itemTypes.indexOf(itemType);

    if (this.slots[itemIndex].length < quantity) {
      console.debug("Not enough items to remove");
      return null;
    }

    const removedItems: Item[] = [];
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

  private recalculateCurrentSize(): void {
    this.currentSize = 0;
    for (const slot of this.slots) {
      if (slot.length > 0) {
        this.currentSize += 1;
      }
    }
  }

  isFull(): boolean {
    return this.slots.length === this.MAX_SIZE;
  }

  isEmpty(): boolean {
    return this.getCurrentSize() === 0;
  }

  getNumberOf(itemType: ItemType): number {
    const itemTypes = Object.values(ItemType);
    const ordinal = itemTypes.indexOf(itemType);
    return this.slots[ordinal].length;
  }

  getNumberOfItems(): number {
    let size = 0;
    for (const slot of this.slots) {
      size += slot.length;
    }
    return size;
  }

  getConditionOf(itemType: ItemType): Condition | null {
    const count = this.getNumberOf(itemType);
    if (count === 0) {
      return null;
    }

    const itemTypes = Object.values(ItemType);
    const ordinal = itemTypes.indexOf(itemType);
    return this.slots[ordinal][0].getStatus();
  }

  clear(): void {
    for (const slot of this.slots) {
      slot.length = 0;
    }
    this.currentSize = 0;
  }

  addRandomItems(): void {
    const numberOfItemsToAdd = Math.floor(Math.random() * this.getMaxSize()) + 2;

    const itemTypes = Object.values(ItemType);
    for (let i = 0; i < numberOfItemsToAdd; i++) {
      let item: Item;
      let attempts = 0;

      do {
        const randomItem = Math.floor(Math.random() * itemTypes.length);
        item = new Item(itemTypes[randomItem]);
        attempts++;
      } while (!this.canGetItems(item.getType(), 1) && attempts < itemTypes.length);

      item.decreaseStatus(Math.floor(Math.random() * 101));

      const itemType = item.getType();
      if (
        !isItemTypeAnimal(itemType) &&
        itemType !== ItemType.STRANGEMEAT &&
        itemType !== ItemType.WAGON &&
        itemType !== ItemType.SONIC &&
        itemType !== ItemType.MAP
      ) {
        this.addItemToInventory(item);
      }
    }
  }

  toString(): string {
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

  getMaxWeight(): number {
    return this.MAX_WEIGHT;
  }
}
