import { ParallaxComponent } from './ParallaxComponent';

/**
 * A parallax component that tiles the image horizontally for seamless looping.
 * When one tile scrolls past the viewport, it wraps around to create an infinite effect.
 */
export class ParallaxComponentLoop extends ParallaxComponent {
  constructor(width: number, image: HTMLImageElement, distance: number) {
    super(width, image, distance);
  }

  override update(delta: number, speed: number = 1): void {
    if (this.isPaused()) {
      return;
    }

    const scrollSpeed = speed / Math.max(this.distance, 1);
    this.scrollX += scrollSpeed * delta;

    if (this.scrollX >= this.getWidth()) {
      this.scrollX = this.scrollX % this.getWidth();
    }
  }

  override render(ctx: CanvasRenderingContext2D): void {
    if (!this.isVisible() || this.getImage() === null) {
      return;
    }

    ctx.save();
    ctx.imageSmoothingEnabled = false;

    const image = this.getImage()!;
    const tileWidth = this.getWidth();
    const tileHeight = this.getHeight();

    const tilesNeeded = Math.ceil(ctx.canvas.width / tileWidth) + 2;
    const startX = this.getX() - this.scrollX;

    for (let i = 0; i < tilesNeeded; i++) {
      const x = startX + i * tileWidth;
      if (x + tileWidth >= 0 && x <= ctx.canvas.width) {
        ctx.drawImage(image, x, this.getY(), tileWidth, tileHeight);
      }
    }

    ctx.restore();
  }
}
