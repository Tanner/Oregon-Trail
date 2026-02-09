export class Color {
    constructor(r, g, b, a = 1) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
    toCSS() {
        return `rgba(${Math.round(this.r * 255)}, ${Math.round(this.g * 255)}, ${Math.round(this.b * 255)}, ${this.a})`;
    }
    darker(amount) {
        return new Color(this.r * (1 - amount), this.g * (1 - amount), this.b * (1 - amount), this.a);
    }
    brighter(amount) {
        return new Color(Math.min(1, this.r + amount), Math.min(1, this.g + amount), Math.min(1, this.b + amount), this.a);
    }
    withAlpha(a) {
        return new Color(this.r, this.g, this.b, a);
    }
    static fromHex(hex) {
        return new Color(((hex >> 16) & 0xff) / 255, ((hex >> 8) & 0xff) / 255, (hex & 0xff) / 255);
    }
}
Color.white = new Color(1, 1, 1);
Color.black = new Color(0, 0, 0);
Color.gray = new Color(0.5, 0.5, 0.5);
Color.darkGray = new Color(0.25, 0.25, 0.25);
Color.red = new Color(1, 0, 0);
Color.green = new Color(0, 1, 0);
Color.yellow = new Color(1, 1, 0);
//# sourceMappingURL=Color.js.map