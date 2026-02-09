import { Sprite } from '../Sprite';
import { ReferencePoint } from '../Component';
import { ImageStore } from '../../core/ImageStore';

/**
 * Composite sprite representing a wagon with two rotating wheels.
 * The wagon body is drawn with a big wheel (back right) and small wheel (front left).
 */
export class WagonSprite extends Sprite {
  private static readonly WHEEL_ROTATION_TIME = 30;
  private static readonly BIG_WHEEL_ROTATION_AMOUNT = -1;
  private static readonly SMALL_WHEEL_ROTATION_AMOUNT = -2;

  private bigWheelSprite: Sprite;
  private smallWheelSprite: Sprite;
  private wheelRotationUpdateCounter: number = 0;

  constructor() {
    const wagonImage = ImageStore.getImage('TRAIL_WAGON');
    const wagonWidth = wagonImage.width * 2;
    super(wagonWidth, undefined, wagonImage);

    const bigWheelImage = ImageStore.getImage('BIG_WHEEL_1');
    const smallWheelImage = ImageStore.getImage('SMALL_WHEEL_1');

    this.bigWheelSprite = new Sprite(bigWheelImage.width * 2, undefined, bigWheelImage);
    this.smallWheelSprite = new Sprite(smallWheelImage.width * 2, undefined, smallWheelImage);

    this.add(
      this.bigWheelSprite,
      this.getPosition(ReferencePoint.BOTTOMRIGHT),
      ReferencePoint.BOTTOMRIGHT,
      -10,
      0
    );

    this.add(
      this.smallWheelSprite,
      this.getPosition(ReferencePoint.BOTTOMLEFT),
      ReferencePoint.BOTTOMLEFT,
      50,
      0
    );
  }

  override update(delta: number): void {
    super.update(delta);

    this.wheelRotationUpdateCounter += delta;

    if (this.wheelRotationUpdateCounter > WagonSprite.WHEEL_ROTATION_TIME) {
      this.bigWheelSprite.rotate(WagonSprite.BIG_WHEEL_ROTATION_AMOUNT);
      this.smallWheelSprite.rotate(WagonSprite.SMALL_WHEEL_ROTATION_AMOUNT);

      this.wheelRotationUpdateCounter = 0;
    }
  }
}
