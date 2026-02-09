import { Component } from './Component';

export class Sprite extends Component {
  private image: HTMLImageElement | null = null;
  private rotation: number = 0;

  constructor(width: number, height?: number, image?: HTMLImageElement) {
    if (image && !height) {
      const aspectRatio = image.height / image.width;
      super(width, Math.floor(width * aspectRatio));
    } else if (height !== undefined) {
      super(width, height);
    } else {
      super(width, width);
    }

    if (image) {
      this.image = image;
    }
  }

  override render(ctx: CanvasRenderingContext2D): void {
    if (!this.isVisible()) {
      return;
    }

    if (this.image !== null) {
      ctx.save();
      ctx.imageSmoothingEnabled = false;

      if (this.rotation !== 0) {
        const centerX = this.getX() + this.getWidth() / 2;
        const centerY = this.getY() + this.getHeight() / 2;
        ctx.translate(centerX, centerY);
        ctx.rotate((this.rotation * Math.PI) / 180);
        ctx.translate(-centerX, -centerY);
      }

      ctx.drawImage(this.image, this.getX(), this.getY(), this.getWidth(), this.getHeight());

      ctx.restore();
    }

    super.render(ctx);
  }

  setImage(image: HTMLImageElement): void {
    this.image = image;
  }

  getImage(): HTMLImageElement | null {
    return this.image;
  }

  getRotation(): number {
    return this.rotation;
  }

  setRotation(rotation: number): void {
    this.rotation = rotation;
  }

  rotate(delta: number): void {
    this.rotation = (this.rotation + delta) % 360;
  }

  toString(): string {
    return this.image?.src || 'Sprite[no image]';
  }
}
