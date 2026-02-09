import { Item } from "./Item";
import { ItemType, getItemTypeWeight } from "./ItemType";
import { ConstantStore } from "../core/ConstantStore";

export class Animal extends Item {
  private moveFactor: number;
  private dead: boolean;

  constructor(type: ItemType) {
    super(type);
    const moveFactorStr = ConstantStore.get("ITEMS", `${type}_MOVE_FACTOR`);
    this.moveFactor = moveFactorStr ? parseFloat(moveFactorStr) : 0;
    this.dead = false;
  }

  killForFood(): Item[] {
    const meatWeight = getItemTypeWeight(ItemType.MEAT);
    const numberOf = Math.floor(this.getWeight() / meatWeight);
    const itemList: Item[] = [];
    for (let i = 0; i < Math.floor(numberOf / 10); i++) {
      itemList.push(new Item(ItemType.MEAT));
    }
    return itemList;
  }

  getMoveFactor(): number {
    return this.moveFactor;
  }

  setDead(dead: boolean): void {
    this.dead = dead;
  }

  isDead(): boolean {
    return this.dead;
  }
}
