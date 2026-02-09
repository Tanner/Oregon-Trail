import { Component } from './Component';
export class Sprite extends Component {
    constructor(width, height, image) {
        this.image = null;
        this.rotation = 0;
        if (image && !height) {
            const aspectRatio = image.height / image.width;
            super(width, Math.floor(width * aspectRatio));
        }
        else if (height !== undefined) {
            super(width, height);
        }
        else {
            super(width, width);
        }
        if (image) {
            this.image = image;
        }
    }
    render(ctx) {
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
    setImage(image) {
        this.image = image;
    }
    getImage() {
        return this.image;
    }
    getRotation() {
        return this.rotation;
    }
    setRotation(rotation) {
        this.rotation = rotation;
    }
    rotate(delta) {
        this.rotation = (this.rotation + delta) % 360;
    }
    toString() {
        return this.image?.src || 'Sprite[no image]';
    }
}
//# sourceMappingURL=Sprite.js.map