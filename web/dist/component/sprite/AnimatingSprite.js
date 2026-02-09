import { Sprite } from '../Sprite';
export var Direction;
(function (Direction) {
    Direction["LEFT"] = "LEFT";
    Direction["RIGHT"] = "RIGHT";
    Direction["FRONT"] = "FRONT";
    Direction["BACK"] = "BACK";
    Direction["UPPER_LEFT"] = "UPPER_LEFT";
    Direction["UPPER_RIGHT"] = "UPPER_RIGHT";
    Direction["LOWER_LEFT"] = "LOWER_LEFT";
    Direction["LOWER_RIGHT"] = "LOWER_RIGHT";
})(Direction || (Direction = {}));
/**
 * A sprite that supports frame-based animation with multiple animation sets.
 * Can switch between different animations (e.g., left-facing, right-facing).
 */
export class AnimatingSprite extends Sprite {
    constructor(width, height) {
        super(width, height);
        this.animations = {};
        this.currentAnimationName = null;
        this.currentFrame = 0;
        this.frameTimer = 0;
        this.isMoving = false;
        this.direction = Direction.LEFT;
    }
    addAnimation(name, frames, frameDuration = 100) {
        this.animations[name] = { frames, frameDuration };
        if (this.currentAnimationName === null) {
            this.setAnimation(name);
        }
    }
    setAnimation(name) {
        if (this.animations[name]) {
            this.currentAnimationName = name;
            this.currentFrame = 0;
            this.frameTimer = 0;
        }
    }
    update(delta) {
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
    render(ctx) {
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
    getDirection() {
        return this.direction;
    }
    setDirection(direction) {
        this.direction = direction;
        if (direction === Direction.LEFT) {
            this.setAnimation('left');
        }
        else if (direction === Direction.RIGHT) {
            this.setAnimation('right');
        }
    }
    setIsMoving(moving) {
        this.isMoving = moving;
    }
    getIsMoving() {
        return this.isMoving;
    }
    moveLeft(delta) {
        const moveSpeed = delta / 6;
        this.setLocation(this.getX() - moveSpeed, this.getY());
        this.setDirection(Direction.LEFT);
    }
    moveRight(delta) {
        const moveSpeed = delta / 6;
        this.setLocation(this.getX() + moveSpeed, this.getY());
        this.setDirection(Direction.RIGHT);
    }
}
//# sourceMappingURL=AnimatingSprite.js.map