import { Color } from "./Color";

export class AnimatingColor extends Color {
  private oldColor: Color;
  private newColor: Color;
  private duration: number;
  private progress: number;
  private animating: boolean;

  constructor(oldColor: Color, newColor: Color, duration: number) {
    super(oldColor.r, oldColor.g, oldColor.b, oldColor.a);
    this.oldColor = oldColor;
    this.newColor = newColor;
    this.duration = duration;
    this.progress = 0;
    this.animating = true;
  }

  private setRGBA(color: Color): void {
    this.r = color.r;
    this.g = color.g;
    this.b = color.b;
    this.a = color.a;
  }

  update(delta: number): void {
    if (!this.animating) {
      this.setRGBA(this.newColor);
      return;
    }

    this.progress += delta;

    if (this.progress >= this.duration) {
      this.animating = false;
      this.setRGBA(this.newColor);
      return;
    }

    const t = this.progress / this.duration;
    const invT = 1 - t;

    const animatingColor = new Color(
      this.oldColor.r * invT + this.newColor.r * t,
      this.oldColor.g * invT + this.newColor.g * t,
      this.oldColor.b * invT + this.newColor.b * t,
      this.oldColor.a * invT + this.newColor.a * t
    );

    this.setRGBA(animatingColor);
  }

  getCurrentColor(): Color {
    return new Color(this.r, this.g, this.b, this.a);
  }

  setTarget(color: Color, durationMs: number): void {
    this.oldColor = new Color(this.r, this.g, this.b, this.a);
    this.newColor = color;
    this.duration = durationMs;
    this.progress = 0;
    this.animating = true;
  }

  isAnimating(): boolean {
    return this.animating;
  }
}
