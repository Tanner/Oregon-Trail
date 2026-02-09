import { AnimatingSprite, Direction } from './AnimatingSprite';
import { ImageStore } from '../../core/ImageStore';
/**
 * 8-directional animated hunter sprite for the hunting minigame.
 */
export class HunterAnimatingSprite extends AnimatingSprite {
    constructor(width) {
        super(width, width);
        this.setupAnimations();
    }
    setupAnimations() {
        this.addAnimation('left', [ImageStore.getImage('HUNTER_LEFT')], 100);
        this.addAnimation('right', [ImageStore.getImage('HUNTER_RIGHT')], 100);
        this.addAnimation('front', [ImageStore.getImage('HUNTER_FRONT')], 100);
        this.addAnimation('back', [ImageStore.getImage('HUNTER_BACK')], 100);
        this.addAnimation('upperleft', [ImageStore.getImage('HUNTER_UPPERLEFT')], 100);
        this.addAnimation('upperright', [ImageStore.getImage('HUNTER_UPPERRIGHT')], 100);
        this.addAnimation('lowerleft', [ImageStore.getImage('HUNTER_LOWERLEFT')], 100);
        this.addAnimation('lowerright', [ImageStore.getImage('HUNTER_LOWERRIGHT')], 100);
    }
    setDirectionFromMovement(dx, dy) {
        if (dx === 0 && dy === 0)
            return;
        if (dy < 0 && dx === 0) {
            this.setAnimation('back');
            this.setDirection(Direction.BACK);
        }
        else if (dy > 0 && dx === 0) {
            this.setAnimation('front');
            this.setDirection(Direction.FRONT);
        }
        else if (dx < 0 && dy === 0) {
            this.setAnimation('left');
            this.setDirection(Direction.LEFT);
        }
        else if (dx > 0 && dy === 0) {
            this.setAnimation('right');
            this.setDirection(Direction.RIGHT);
        }
        else if (dx < 0 && dy < 0) {
            this.setAnimation('upperleft');
            this.setDirection(Direction.UPPER_LEFT);
        }
        else if (dx > 0 && dy < 0) {
            this.setAnimation('upperright');
            this.setDirection(Direction.UPPER_RIGHT);
        }
        else if (dx < 0 && dy > 0) {
            this.setAnimation('lowerleft');
            this.setDirection(Direction.LOWER_LEFT);
        }
        else if (dx > 0 && dy > 0) {
            this.setAnimation('lowerright');
            this.setDirection(Direction.LOWER_RIGHT);
        }
    }
}
//# sourceMappingURL=HunterAnimatingSprite.js.map