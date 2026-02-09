import { Component, ReferencePoint } from './Component';
import { Color } from '../core/Color';
import { Sprite } from './Sprite';

export class Panel extends Component {
  protected backgroundImage: HTMLImageElement | null = null;

  constructor(width: number, height: number, backgroundColorOrImage?: Color | HTMLImageElement) {
    super(width, height);

    if (backgroundColorOrImage instanceof Color) {
      this.setBackgroundColor(backgroundColorOrImage);
    } else if (backgroundColorOrImage instanceof HTMLImageElement) {
      this.backgroundImage = backgroundColorOrImage;
      const sprite = new Sprite(width, height, backgroundColorOrImage);
      this.add(sprite, this.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
    }
  }

  override render(ctx: CanvasRenderingContext2D): void {
    if (!this.isVisible()) {
      return;
    }

    super.render(ctx);
  }
}
