export class Color {
  constructor(
    public r: number,
    public g: number,
    public b: number,
    public a: number = 1
  ) {}

  toCSS(): string {
    return `rgba(${Math.round(this.r * 255)}, ${Math.round(this.g * 255)}, ${Math.round(this.b * 255)}, ${this.a})`;
  }

  darker(amount: number): Color {
    return new Color(
      this.r * (1 - amount),
      this.g * (1 - amount),
      this.b * (1 - amount),
      this.a
    );
  }

  brighter(amount: number): Color {
    return new Color(
      Math.min(1, this.r + amount),
      Math.min(1, this.g + amount),
      Math.min(1, this.b + amount),
      this.a
    );
  }

  withAlpha(a: number): Color {
    return new Color(this.r, this.g, this.b, a);
  }

  static fromHex(hex: number): Color {
    return new Color(
      ((hex >> 16) & 0xff) / 255,
      ((hex >> 8) & 0xff) / 255,
      (hex & 0xff) / 255
    );
  }

  static readonly white = new Color(1, 1, 1);
  static readonly black = new Color(0, 0, 0);
  static readonly gray = new Color(0.5, 0.5, 0.5);
  static readonly darkGray = new Color(0.25, 0.25, 0.25);
  static readonly red = new Color(1, 0, 0);
  static readonly green = new Color(0, 1, 0);
  static readonly yellow = new Color(1, 1, 0);
}
