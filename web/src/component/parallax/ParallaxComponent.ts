import { Sprite } from '../Sprite';

/**
 * A sprite that scrolls horizontally based on its distance from the viewer.
 * Farther objects scroll slower, creating a parallax depth effect.
 */
export class ParallaxComponent extends Sprite {
  protected distance: number;
  protected scrollX: number = 0;
  protected paused: boolean = false;
  protected expired: boolean = false;

  constructor(width: number, image: HTMLImageElement, distance: number) {
    super(width, undefined, image);
    this.distance = distance;
  }

  override update(delta: number, speed: number = 1): void {
    if (this.paused) {
      return;
    }

    const scrollSpeed = speed / Math.max(this.distance, 1);
    this.scrollX += scrollSpeed * delta;

    if (this.scrollX > this.getWidth() * 2) {
      this.expired = true;
    }
  }

  override render(ctx: CanvasRenderingContext2D): void {
    if (!this.isVisible() || this.getImage() === null) {
      return;
    }

    ctx.save();
    ctx.imageSmoothingEnabled = false;

    const image = this.getImage()!;
    ctx.drawImage(
      image,
      this.getX() + this.scrollX,
      this.getY(),
      this.getWidth(),
      this.getHeight()
    );

    ctx.restore();
  }

  getDistance(): number {
    return this.distance;
  }

  getScrollX(): number {
    return this.scrollX;
  }

  setScrollX(x: number): void {
    this.scrollX = x;
  }

  isPaused(): boolean {
    return this.paused;
  }

  setPaused(paused: boolean): void {
    this.paused = paused;
  }

  isExpired(): boolean {
    return this.expired;
  }
}
