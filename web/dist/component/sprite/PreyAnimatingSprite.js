import { AnimatingSprite, Direction } from './AnimatingSprite';
import { ImageStore } from '../../core/ImageStore';
export var PreyType;
(function (PreyType) {
    PreyType["COW"] = "COW";
    PreyType["PIG"] = "PIG";
})(PreyType || (PreyType = {}));
/**
 * Animated prey sprite with simple flee AI for the hunting minigame.
 */
export class PreyAnimatingSprite extends AnimatingSprite {
    constructor(width, type) {
        super(width, width);
        this.fleeSpeed = 2;
        this.alive = true;
        this.preyType = type;
        this.setupAnimations();
    }
    setupAnimations() {
        const prefix = this.preyType === PreyType.COW ? 'HUNT_COW' : 'HUNT_PIG';
        const frameCount = this.preyType === PreyType.COW ? 9 : 6;
        const leftFrames = [];
        const rightFrames = [];
        const frontFrames = [];
        const backFrames = [];
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
    fleeFrom(hunterX, hunterY, delta) {
        if (!this.alive)
            return;
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
                }
                else {
                    this.setAnimation('left');
                    this.setDirection(Direction.LEFT);
                }
            }
            else {
                if (dy > 0) {
                    this.setAnimation('front');
                    this.setDirection(Direction.FRONT);
                }
                else {
                    this.setAnimation('back');
                    this.setDirection(Direction.BACK);
                }
            }
        }
    }
    kill() {
        this.alive = false;
        this.setVisible(false);
    }
    isAlive() {
        return this.alive;
    }
    getMeatValue() {
        return this.preyType === PreyType.COW ? 100 : 50;
    }
}
//# sourceMappingURL=PreyAnimatingSprite.js.map