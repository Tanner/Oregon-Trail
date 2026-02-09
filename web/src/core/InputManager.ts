export interface InputDelegate {
  mousePressed(button: number, x: number, y: number): void;
  mouseReleased(button: number, x: number, y: number): void;
  mouseMoved(x: number, y: number): void;
  keyPressed(key: string, code: string): void;
  keyReleased(key: string, code: string): void;
}

export class InputManager {
  private delegate: InputDelegate | null = null;
  private canvas: HTMLCanvasElement;
  private keysHeld: Set<string> = new Set();
  private boundHandlers: Map<string, (e: Event) => void> = new Map();

  constructor(canvas: HTMLCanvasElement) {
    this.canvas = canvas;
    this.setupListeners();
  }

  setDelegate(delegate: InputDelegate | null): void {
    this.delegate = delegate;
  }

  isKeyHeld(code: string): boolean {
    return this.keysHeld.has(code);
  }

  private setupListeners(): void {
    const mousedownHandler = (e: MouseEvent) => {
      if (this.delegate) {
        const rect = this.canvas.getBoundingClientRect();
        const x = e.clientX - rect.left;
        const y = e.clientY - rect.top;
        this.delegate.mousePressed(e.button, x, y);
      }
    };

    const mouseupHandler = (e: MouseEvent) => {
      if (this.delegate) {
        const rect = this.canvas.getBoundingClientRect();
        const x = e.clientX - rect.left;
        const y = e.clientY - rect.top;
        this.delegate.mouseReleased(e.button, x, y);
      }
    };

    const mousemoveHandler = (e: MouseEvent) => {
      if (this.delegate) {
        const rect = this.canvas.getBoundingClientRect();
        const x = e.clientX - rect.left;
        const y = e.clientY - rect.top;
        this.delegate.mouseMoved(x, y);
      }
    };

    const keydownHandler = (e: KeyboardEvent) => {
      if (!this.keysHeld.has(e.code)) {
        this.keysHeld.add(e.code);
        if (this.delegate) {
          this.delegate.keyPressed(e.key, e.code);
        }
      }
    };

    const keyupHandler = (e: KeyboardEvent) => {
      this.keysHeld.delete(e.code);
      if (this.delegate) {
        this.delegate.keyReleased(e.key, e.code);
      }
    };

    this.canvas.addEventListener('mousedown', mousedownHandler);
    this.canvas.addEventListener('mouseup', mouseupHandler);
    this.canvas.addEventListener('mousemove', mousemoveHandler);
    window.addEventListener('keydown', keydownHandler);
    window.addEventListener('keyup', keyupHandler);

    this.boundHandlers.set('mousedown', mousedownHandler as (e: Event) => void);
    this.boundHandlers.set('mouseup', mouseupHandler as (e: Event) => void);
    this.boundHandlers.set('mousemove', mousemoveHandler as (e: Event) => void);
    this.boundHandlers.set('keydown', keydownHandler as (e: Event) => void);
    this.boundHandlers.set('keyup', keyupHandler as (e: Event) => void);
  }

  destroy(): void {
    this.canvas.removeEventListener('mousedown', this.boundHandlers.get('mousedown')!);
    this.canvas.removeEventListener('mouseup', this.boundHandlers.get('mouseup')!);
    this.canvas.removeEventListener('mousemove', this.boundHandlers.get('mousemove')!);
    window.removeEventListener('keydown', this.boundHandlers.get('keydown')!);
    window.removeEventListener('keyup', this.boundHandlers.get('keyup')!);
    this.boundHandlers.clear();
  }
}
