import { Sprite } from '../Sprite';

export enum Direction {
  LEFT = 'LEFT',
  RIGHT = 'RIGHT',
  FRONT = 'FRONT',
  BACK = 'BACK',
  UPPER_LEFT = 'UPPER_LEFT',
  UPPER_RIGHT = 'UPPER_RIGHT',
  LOWER_LEFT = 'LOWER_LEFT',
  LOWER_RIGHT = 'LOWER_RIGHT'
}

interface AnimationSet {
  frames: HTMLImageElement[];
  frameDuration: number;
}

/**
 * A sprite that supports frame-based animation with multiple animation sets.
 * Can switch between different animations (e.g., left-facing, right-facing).
 */
export class AnimatingSprite extends Sprite {
  protected animations: Record<string, AnimationSet> = {};
  protected currentAnimationName: string | null = null;
  protected currentFrame: number = 0;
  protected frameTimer: number = 0;
  protected isMoving: boolean = false;
  protected direction: Direction = Direction.LEFT;

  constructor(width: number, height: number) {
    super(width, height);
  }

  addAnimation(name: string, frames: HTMLImageElement[], frameDuration: number = 100): void {
    this.animations[name] = { frames, frameDuration };

    if (this.currentAnimationName === null) {
      this.setAnimation(name);
    }
  }

  setAnimation(name: string): void {
    if (this.animations[name]) {
      this.currentAnimationName = name;
      this.currentFrame = 0;
      this.frameTimer = 0;
    }
  }

  override update(delta: number): void {
    super.update(delta);

    if (this.currentAnimationName === null) {
      return;
    }

    const animation = this.animations[this.currentAnimationName];
    if (!animation || animation.frames.length === 0) {
      return;
    }

    this.frameTimer += delta;

    if (this.frameTimer >= animation.frameDuration) {
      this.currentFrame = (this.currentFrame + 1) % animation.frames.length;
      this.frameTimer = 0;
    }
  }

  override render(ctx: CanvasRenderingContext2D): void {
    if (!this.isVisible()) {
      return;
    }

    if (this.currentAnimationName === null) {
      super.render(ctx);
      return;
    }

    const animation = this.animations[this.currentAnimationName];
    if (!animation || animation.frames.length === 0) {
      super.render(ctx);
      return;
    }

    ctx.save();
    ctx.imageSmoothingEnabled = false;

    const frame = animation.frames[this.currentFrame];
    ctx.drawImage(frame, this.getX(), this.getY(), this.getWidth(), this.getHeight());

    ctx.restore();

    for (const component of this.components) {
      component.render(ctx);
    }
  }

  getDirection(): Direction {
    return this.direction;
  }

  setDirection(direction: Direction): void {
    this.direction = direction;

    if (direction === Direction.LEFT) {
      this.setAnimation('left');
    } else if (direction === Direction.RIGHT) {
      this.setAnimation('right');
    }
  }

  setIsMoving(moving: boolean): void {
    this.isMoving = moving;
  }

  getIsMoving(): boolean {
    return this.isMoving;
  }

  moveLeft(delta: number): void {
    const moveSpeed = delta / 6;
    this.setLocation(this.getX() - moveSpeed, this.getY());
    this.setDirection(Direction.LEFT);
  }

  moveRight(delta: number): void {
    const moveSpeed = delta / 6;
    this.setLocation(this.getX() + moveSpeed, this.getY());
    this.setDirection(Direction.RIGHT);
  }
}
