export class Condition {
    constructor(min, max, current) {
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
    getCurrent() {
        return this.current;
    }
    getPercentage() {
        return (this.current - this.min) / (this.max - this.min);
    }
    getMin() {
        return this.min;
    }
    getMax() {
        return this.max;
    }
    increase(amount) {
        if (amount <= 0) {
            console.error("Not an increment");
            return;
        }
        if (this.current + amount > this.max) {
            this.current = this.max;
            console.warn("Increment exceeded max - set to max");
        }
        else {
            this.current += amount;
        }
    }
    decrease(amount) {
        if (amount <= 0) {
            console.error("Not a decrement");
            return;
        }
        if (this.current - amount < this.min) {
            this.current = this.min;
            console.warn("Decrement exceeded min - set to min");
        }
        else {
            this.current -= amount;
        }
    }
    copy() {
        return new Condition(this.min, this.max, this.current);
    }
    toString() {
        return `Min: ${this.min}, Max: ${this.max}, Current: ${this.current}`;
    }
}
//# sourceMappingURL=Condition.js.map