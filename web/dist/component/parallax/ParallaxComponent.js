import { Sprite } from '../Sprite';
/**
 * A sprite that scrolls horizontally based on its distance from the viewer.
 * Farther objects scroll slower, creating a parallax depth effect.
 */
export class ParallaxComponent extends Sprite {
    constructor(width, image, distance) {
        super(width, undefined, image);
        this.scrollX = 0;
        this.paused = false;
        this.expired = false;
        this.distance = distance;
    }
    update(delta, speed = 1) {
        if (this.paused) {
            return;
        }
        const scrollSpeed = speed / Math.max(this.distance, 1);
        this.scrollX += scrollSpeed * delta;
        if (this.scrollX > this.getWidth() * 2) {
            this.expired = true;
        }
    }
    render(ctx) {
        if (!this.isVisible() || this.getImage() === null) {
            return;
        }
        ctx.save();
        ctx.imageSmoothingEnabled = false;
        const image = this.getImage();
        ctx.drawImage(image, this.getX() + this.scrollX, this.getY(), this.getWidth(), this.getHeight());
        ctx.restore();
    }
    getDistance() {
        return this.distance;
    }
    getScrollX() {
        return this.scrollX;
    }
    setScrollX(x) {
        this.scrollX = x;
    }
    isPaused() {
        return this.paused;
    }
    setPaused(paused) {
        this.paused = paused;
    }
    isExpired() {
        return this.expired;
    }
}
//# sourceMappingURL=ParallaxComponent.js.map