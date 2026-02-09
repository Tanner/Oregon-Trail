import { Color } from './Color';

export interface Transition {
  update(delta: number): boolean;
  preRender(ctx: CanvasRenderingContext2D): void;
  postRender(ctx: CanvasRenderingContext2D): void;
  isComplete(): boolean;
}

export class FadeOutTransition implements Transition {
  private elapsed: number = 0;
  private readonly duration: number;
  private readonly color: Color;
  private complete: boolean = false;

  constructor(color: Color = Color.black, duration: number = 500) {
    this.color = color;
    this.duration = duration;
  }

  update(delta: number): boolean {
    this.elapsed += delta;
    if (this.elapsed >= this.duration) {
      this.elapsed = this.duration;
      this.complete = true;
    }
    return this.complete;
  }

  preRender(_ctx: CanvasRenderingContext2D): void {
  }

  postRender(ctx: CanvasRenderingContext2D): void {
    const alpha = Math.min(1, this.elapsed / this.duration);
    ctx.globalAlpha = alpha;
    ctx.fillStyle = this.color.toCSS();
    ctx.fillRect(0, 0, ctx.canvas.width, ctx.canvas.height);
    ctx.globalAlpha = 1;
  }

  isComplete(): boolean {
    return this.complete;
  }
}

export class FadeInTransition implements Transition {
  private elapsed: number = 0;
  private readonly duration: number;
  private readonly color: Color;
  private complete: boolean = false;

  constructor(color: Color = Color.black, duration: number = 500) {
    this.color = color;
    this.duration = duration;
  }

  update(delta: number): boolean {
    this.elapsed += delta;
    if (this.elapsed >= this.duration) {
      this.elapsed = this.duration;
      this.complete = true;
    }
    return this.complete;
  }

  preRender(_ctx: CanvasRenderingContext2D): void {
  }

  postRender(ctx: CanvasRenderingContext2D): void {
    const alpha = Math.max(0, 1 - (this.elapsed / this.duration));
    ctx.globalAlpha = alpha;
    ctx.fillStyle = this.color.toCSS();
    ctx.fillRect(0, 0, ctx.canvas.width, ctx.canvas.height);
    ctx.globalAlpha = 1;
  }

  isComplete(): boolean {
    return this.complete;
  }
}

export class RotateTransition implements Transition {
  private elapsed: number = 0;
  private readonly duration: number;
  private readonly color: Color;
  private complete: boolean = false;
  private offscreenCanvas: HTMLCanvasElement | null = null;
  private offscreenCtx: CanvasRenderingContext2D | null = null;

  constructor(color: Color = Color.black, duration: number = 1000) {
    this.color = color;
    this.duration = duration;
  }

  update(delta: number): boolean {
    this.elapsed += delta;
    if (this.elapsed >= this.duration) {
      this.elapsed = this.duration;
      this.complete = true;
    }
    return this.complete;
  }

  preRender(ctx: CanvasRenderingContext2D): void {
    if (!this.offscreenCanvas) {
      this.offscreenCanvas = document.createElement('canvas');
      this.offscreenCanvas.width = ctx.canvas.width;
      this.offscreenCanvas.height = ctx.canvas.height;
      this.offscreenCtx = this.offscreenCanvas.getContext('2d')!;
    }

    this.offscreenCtx!.clearRect(0, 0, this.offscreenCanvas!.width, this.offscreenCanvas!.height);
    this.offscreenCtx!.fillStyle = this.color.toCSS();
    this.offscreenCtx!.fillRect(0, 0, this.offscreenCanvas!.width, this.offscreenCanvas!.height);
  }

  postRender(ctx: CanvasRenderingContext2D): void {
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
    ctx.drawImage(
      this.offscreenCanvas,
      -this.offscreenCanvas.width / 2,
      -this.offscreenCanvas.height / 2
    );
    ctx.restore();
  }

  isComplete(): boolean {
    return this.complete;
  }
}
