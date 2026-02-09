export class Condition {
  private min: number;
  private max: number;
  private current: number;

  constructor(min: number, max: number, current?: number) {
    if (max < min) {
      throw new Error("Maximum value less than minimum value");
    }

    this.min = min;
    this.max = max;
    this.current = current !== undefined ? current : max;

    if (this.current < min || this.current > max) {
      throw new Error("Starting value not in min/max range");
    }
  }

  getCurrent(): number {
    return this.current;
  }

  getPercentage(): number {
    return (this.current - this.min) / (this.max - this.min);
  }

  getMin(): number {
    return this.min;
  }

  getMax(): number {
    return this.max;
  }

  increase(amount: number): void {
    if (amount <= 0) {
      console.error("Not an increment");
      return;
    }

    if (this.current + amount > this.max) {
      this.current = this.max;
      console.warn("Increment exceeded max - set to max");
    } else {
      this.current += amount;
    }
  }

  decrease(amount: number): void {
    if (amount <= 0) {
      console.error("Not a decrement");
      return;
    }

    if (this.current - amount < this.min) {
      this.current = this.min;
      console.warn("Decrement exceeded min - set to min");
    } else {
      this.current -= amount;
    }
  }

  copy(): Condition {
    return new Condition(this.min, this.max, this.current);
  }

  toString(): string {
    return `Min: ${this.min}, Max: ${this.max}, Current: ${this.current}`;
  }
}
