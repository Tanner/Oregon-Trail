import { Condition } from "./Condition";
import { StateIdx } from "../core/ConstantStore";

export abstract class MapObject {
  protected visible: boolean = false;
  protected quality: Condition;
  protected name: string = "";
  protected static count: number = 0;
  private territory?: StateIdx;

  constructor() {
    this.quality = new Condition(0, 100, 100);
  }

  isVisible(): boolean {
    return this.visible;
  }

  setVisible(visible: boolean): void {
    this.visible = visible;
  }

  getName(): string {
    return this.name;
  }

  setName(name: string): void {
    this.name = name;
  }

  getTerritory(): StateIdx | undefined {
    return this.territory;
  }

  setTerritory(territory: StateIdx): void {
    this.territory = territory;
  }

  getConditionPercentage(): number {
    return this.quality.getPercentage();
  }

  getCondition(): Condition {
    return this.quality;
  }

  setQuality(quality: Condition): void {
    this.quality = quality;
  }

  static resetCount(): void {
    MapObject.count = 0;
  }

  abstract debugToString(): string;
}
