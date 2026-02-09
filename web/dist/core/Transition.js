import { Color } from './Color';
export class FadeOutTransition {
    constructor(color = Color.black, duration = 500) {
        this.elapsed = 0;
        this.complete = false;
        this.color = color;
        this.duration = duration;
    }
    update(delta) {
        this.elapsed += delta;
        if (this.elapsed >= this.duration) {
            this.elapsed = this.duration;
            this.complete = true;
        }
        return this.complete;
    }
    preRender(_ctx) {
    }
    postRender(ctx) {
        const alpha = Math.min(1, this.elapsed / this.duration);
        ctx.globalAlpha = alpha;
        ctx.fillStyle = this.color.toCSS();
        ctx.fillRect(0, 0, ctx.canvas.width, ctx.canvas.height);
        ctx.globalAlpha = 1;
    }
    isComplete() {
        return this.complete;
    }
}
export class FadeInTransition {
    constructor(color = Color.black, duration = 500) {
        this.elapsed = 0;
        this.complete = false;
        this.color = color;
        this.duration = duration;
    }
    update(delta) {
        this.elapsed += delta;
        if (this.elapsed >= this.duration) {
            this.elapsed = this.duration;
            this.complete = true;
        }
        return this.complete;
    }
    preRender(_ctx) {
    }
    postRender(ctx) {
        const alpha = Math.max(0, 1 - (this.elapsed / this.duration));
        ctx.globalAlpha = alpha;
        ctx.fillStyle = this.color.toCSS();
        ctx.fillRect(0, 0, ctx.canvas.width, ctx.canvas.height);
        ctx.globalAlpha = 1;
    }
    isComplete() {
        return this.complete;
    }
}
export class RotateTransition {
    constructor(color = Color.black, duration = 1000) {
        this.elapsed = 0;
        this.complete = false;
        this.offscreenCanvas = null;
        this.offscreenCtx = null;
        this.color = color;
        this.duration = duration;
    }
    update(delta) {
        this.elapsed += delta;
        if (this.elapsed >= this.duration) {
            this.elapsed = this.duration;
            this.complete = true;
        }
        return this.complete;
    }
    preRender(ctx) {
        if (!this.offscreenCanvas) {
            this.offscreenCanvas = document.createElement('canvas');
            this.offscreenCanvas.width = ctx.canvas.width;
            this.offscreenCanvas.height = ctx.canvas.height;
            this.offscreenCtx = this.offscreenCanvas.getContext('2d');
        }
        this.offscreenCtx.clearRect(0, 0, this.offscreenCanvas.width, this.offscreenCanvas.height);
        this.offscreenCtx.fillStyle = this.color.toCSS();
        this.offscreenCtx.fillRect(0, 0, this.offscreenCanvas.width, this.offscreenCanvas.height);
    }
    postRender(ctx) {
        if (!this.offscreenCanvas || !this.offscreenCtx) {
            return;
        }
        this.offscreenCtx.drawImage(ctx.canvas, 0, 0);
        ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height);
        ctx.fillStyle = this.color.toCSS();
        ctx.fillRect(0, 0, ctx.canvas.width, ctx.canvas.height);
        const progress = this.elapsed / this.duration;
        const centerX = ctx.canvas.width / 2;
        const centerY = ctx.canvas.height / 2;
        const rotation = progress * Math.PI * 2;
        const scale = 1 - progress;
        ctx.save();
        ctx.translate(centerX, centerY);
        ctx.rotate(rotation);
        ctx.scale(scale, scale);
        ctx.globalAlpha = 1 - progress;
        ctx.drawImage(this.offscreenCanvas, -this.offscreenCanvas.width / 2, -this.offscreenCanvas.height / 2);
        ctx.restore();
    }
    isComplete() {
        return this.complete;
    }
}
//# sourceMappingURL=Transition.js.map