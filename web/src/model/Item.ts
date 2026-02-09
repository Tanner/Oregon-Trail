import { Condition } from "./Condition";
import {
  ItemType,
  getItemTypeName,
  getItemTypeDescription,
  getItemTypeCost,
  getItemTypeWeight,
} from "./ItemType";

export class Item {
  private readonly status: Condition;
  private isStackable: boolean;
  private readonly type: ItemType;

  constructor(type: ItemType) {
    this.status = new Condition(100);
    this.type = type;
    this.isStackable = true;
  }

  getName(): string {
    return getItemTypeName(this.type);
  }

  getDescription(): string {
    return getItemTypeDescription(this.type);
  }

  getCost(): number {
    return Math.floor(getItemTypeCost(this.type) * this.status.getPercentage());
  }

  getStatus(): Condition {
    return this.status.copy();
  }

  getConditionPercentage(): number {
    return this.status.getPercentage();
  }

  increaseStatus(amount: number): void {
    this.status.increase(amount);
    if (this.status.getCurrent() === this.status.getMax()) {
      this.isStackable = true;
    }
  }

  decreaseStatus(amount: number): void {
    this.status.decrease(amount);
    if (this.status.getCurrent() < this.status.getMax()) {
      this.isStackable = false;
    }
  }

  getWeight(): number {
    return getItemTypeWeight(this.type);
  }

  isItemStackable(): boolean {
    return this.isStackable;
  }

  compareTo(other: Item): number {
    const diff = this.getConditionPercentage() - other.getConditionPercentage();
    if (Math.abs(diff) < 0.000001) {
      return 0;
    }
    return diff < 0 ? -1 : 1;
  }

  getType(): ItemType {
    return this.type;
  }

  toString(): string {
    return getItemTypeName(this.type);
  }

  getCondition(): Condition {
    return this.status;
  }
}
