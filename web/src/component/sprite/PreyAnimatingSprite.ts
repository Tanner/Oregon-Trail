import { AnimatingSprite, Direction } from './AnimatingSprite';
import { ImageStore } from '../../core/ImageStore';

export enum PreyType {
  COW = 'COW',
  PIG = 'PIG'
}

/**
 * Animated prey sprite with simple flee AI for the hunting minigame.
 */
export class PreyAnimatingSprite extends AnimatingSprite {
  private preyType: PreyType;
  private fleeSpeed: number = 2;
  private alive: boolean = true;

  constructor(width: number, type: PreyType) {
    super(width, width);
    this.preyType = type;
    this.setupAnimations();
  }

  private setupAnimations(): void {
    const prefix = this.preyType === PreyType.COW ? 'HUNT_COW' : 'HUNT_PIG';
    const frameCount = this.preyType === PreyType.COW ? 9 : 6;

    const leftFrames: HTMLImageElement[] = [];
    const rightFrames: HTMLImageElement[] = [];
    const frontFrames: HTMLImageElement[] = [];
    const backFrames: HTMLImageElement[] = [];

    for (let i = 1; i <= frameCount; i++) {
      leftFrames.push(ImageStore.getImage(`${prefix}LEFT${i}`));
      rightFrames.push(ImageStore.getImage(`${prefix}RIGHT${i}`));
      frontFrames.push(ImageStore.getImage(`${prefix}FRONT${i}`));
      backFrames.push(ImageStore.getImage(`${prefix}BACK${i}`));
    }

    this.addAnimation('left', leftFrames, 100);
    this.addAnimation('right', rightFrames, 100);
    this.addAnimation('front', frontFrames, 100);
    this.addAnimation('back', backFrames, 100);
  }

  fleeFrom(hunterX: number, hunterY: number, delta: number): void {
    if (!this.alive) return;

    const dx = this.getX() - hunterX;
    const dy = this.getY() - hunterY;
    const distance = Math.sqrt(dx * dx + dy * dy);

    if (distance < 200) {
      const moveX = (dx / distance) * this.fleeSpeed * (delta / 16);
      const moveY = (dy / distance) * this.fleeSpeed * (delta / 16);

      this.setLocation(this.getX() + moveX, this.getY() + moveY);

      if (Math.abs(dx) > Math.abs(dy)) {
        if (dx > 0) {
          this.setAnimation('right');
          this.setDirection(Direction.RIGHT);
        } else {
          this.setAnimation('left');
          this.setDirection(Direction.LEFT);
        }
      } else {
        if (dy > 0) {
          this.setAnimation('front');
          this.setDirection(Direction.FRONT);
        } else {
          this.setAnimation('back');
          this.setDirection(Direction.BACK);
        }
      }
    }
  }

  kill(): void {
    this.alive = false;
    this.setVisible(false);
  }

  isAlive(): boolean {
    return this.alive;
  }

  getMeatValue(): number {
    return this.preyType === PreyType.COW ? 100 : 50;
  }
}
