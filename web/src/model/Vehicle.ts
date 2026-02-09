import { Item } from "./Item";
import { Inventory } from "./Inventory";
import { ItemType } from "./ItemType";
import { Condition } from "./Condition";
import { ConstantStore } from "../core/ConstantStore";

export class Vehicle extends Item {
  private readonly cargo: Inventory;
  private readonly MAX_INVENTORY_SIZE: number;
  private readonly MAX_INVENTORY_WEIGHT: number;

  constructor(type: ItemType) {
    super(type);
    this.MAX_INVENTORY_SIZE = parseInt(
      ConstantStore.get("ITEMS", `${type}_MAX_INV_SIZE`),
      10,
    );
    this.MAX_INVENTORY_WEIGHT = parseFloat(
      ConstantStore.get("ITEMS", `${type}_MAX_INV_WEIGHT`),
    );
    this.cargo = new Inventory(
      this.MAX_INVENTORY_SIZE,
      this.MAX_INVENTORY_WEIGHT,
    );
  }

  getStatus(): Condition {
    return this.getCondition().copy();
  }

  increaseStatus(amount: number): void {
    this.getCondition().increase(amount);
  }

  decreaseStatus(amount: number): void {
    this.getCondition().decrease(amount);
  }

  getInventory(): Inventory {
    return this.cargo;
  }

  addItemsToInventory(items: Item[]): void {
    this.cargo.addItemsToInventory(items);
  }

  addItemToInventory(item: Item): void {
    this.cargo.addItemToInventory(item);
  }

  removeItemFromInventory(itemType: ItemType, quantity: number): Item[] | null {
    return this.cargo.removeItemFromInventory(itemType, quantity);
  }

  canGetItem(itemType: ItemType, numberOf: number): boolean {
    return this.cargo.canGetItems(itemType, numberOf);
  }

  getMaxSize(): number {
    return this.MAX_INVENTORY_SIZE;
  }

  getMaxWeight(): number {
    return this.MAX_INVENTORY_WEIGHT;
  }

  getWeight(): number {
    return this.cargo.getWeight();
  }

  repair(amount: number): void {
    this.getCondition().increase(amount);
  }
}
