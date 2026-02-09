import { SceneDirector } from './SceneDirector';
import { InputManager } from './InputManager';
import { SplashScene } from '../scene/SplashScene';
import { LoadingScene } from '../scene/LoadingScene';

export class GameDirector {
  private canvas: HTMLCanvasElement;
  private ctx: CanvasRenderingContext2D;
  private sceneDirector: SceneDirector;
  private inputManager: InputManager;
  private lastTimestamp: number = 0;
  private running: boolean = false;

  constructor(canvas: HTMLCanvasElement) {
    this.canvas = canvas;
    const ctx = canvas.getContext('2d');
    if (!ctx) {
      throw new Error('Failed to get 2D context from canvas');
    }
    this.ctx = ctx;
    this.ctx.imageSmoothingEnabled = false;

    this.sceneDirector = new SceneDirector();
    this.inputManager = new InputManager(canvas);
    this.inputManager.setDelegate(this.sceneDirector);
  }

  start(): void {
    if (this.running) return;
    this.running = true;

    const splashScene = new SplashScene(this.canvas.width, this.canvas.height, () => {
      this.sceneDirector.pushScene(
        new LoadingScene(this.canvas.width, this.canvas.height, () => {
          console.log('Loading complete - ready for MainMenuScene');
        }),
        true
      );
    });

    this.sceneDirector.pushScene(splashScene, false);
    this.lastTimestamp = performance.now();
    this.gameLoop(this.lastTimestamp);
  }

  private gameLoop(timestamp: number): void {
    if (!this.running) return;

    const delta = timestamp - this.lastTimestamp;
    this.lastTimestamp = timestamp;

    this.sceneDirector.update(delta);

    this.ctx.fillStyle = '#000000';
    this.ctx.fillRect(0, 0, this.canvas.width, this.canvas.height);

    this.sceneDirector.render(this.ctx);

    requestAnimationFrame((ts) => this.gameLoop(ts));
  }

  stop(): void {
    this.running = false;
  }
}
