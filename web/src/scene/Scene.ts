import { Component, Visible } from '../component/Component';
import { SceneID } from './SceneID';

export interface ModalListener {
  dismissModal(modal: Component, button: number): void;
}

class SceneLayer implements Visible {
  private components: Component[] = [];
  private visibleParent: Visible | null = null;
  private visible: boolean = true;
  private acceptingInput: boolean = true;

  setVisibleParent(parent: Visible | null): void {
    this.visibleParent = parent;
  }

  isVisible(): boolean {
    if (this.visibleParent !== null) {
      return this.visible && this.visibleParent.isVisible();
    }
    return this.visible;
  }

  setVisible(visible: boolean): void {
    this.visible = visible;
  }

  add(component: Component): void {
    this.components.push(component);
    component.setVisibleParent(this);
  }

  remove(component: Component): void {
    component.setVisibleParent(null);
    const index = this.components.indexOf(component);
    if (index > -1) {
      this.components.splice(index, 1);
    }
  }

  render(ctx: CanvasRenderingContext2D): void {
    for (const component of this.components) {
      if (component.isVisible()) {
        component.render(ctx);
      }
    }
  }

  update(delta: number): void {
    for (const component of this.components) {
      component.update(delta);
    }
  }

  setAcceptingInput(acceptingInput: boolean): void {
    this.acceptingInput = acceptingInput;
    for (const component of this.components) {
      component.setAcceptingInput(acceptingInput);
    }
  }

  isAcceptingInput(): boolean {
    return this.acceptingInput;
  }

  getComponents(): Component[] {
    return this.components;
  }
}

export interface SceneDelegate {
  requestScene(id: SceneID, lastScene: Scene | null, replace: boolean): void;
}

export abstract class Scene implements Visible, ModalListener {
  protected backgroundLayer: SceneLayer;
  protected mainLayer: SceneLayer;
  protected hudLayer: SceneLayer;
  protected modalLayer: SceneLayer;

  private active: boolean = false;
  private paused: boolean = false;
  private lastMouseX: number = 0;
  private lastMouseY: number = 0;
  private sceneDelegate: SceneDelegate | null = null;

  constructor() {
    this.backgroundLayer = new SceneLayer();
    this.backgroundLayer.setVisibleParent(this);
    this.mainLayer = new SceneLayer();
    this.mainLayer.setVisibleParent(this);
    this.hudLayer = new SceneLayer();
    this.hudLayer.setVisibleParent(this);
    this.modalLayer = new SceneLayer();
    this.modalLayer.setVisibleParent(this);
  }

  abstract getID(): SceneID;

  render(ctx: CanvasRenderingContext2D): void {
    this.backgroundLayer.render(ctx);
    this.mainLayer.render(ctx);
    this.hudLayer.render(ctx);
    this.modalLayer.render(ctx);
  }

  update(delta: number): void {
    this.backgroundLayer.update(delta);
    this.mainLayer.update(delta);
    this.hudLayer.update(delta);
    this.modalLayer.update(delta);
  }

  showModal(modal: Component): void {
    this.pause();
    this.modalLayer.add(modal);
    this.backgroundLayer.setAcceptingInput(false);
    this.mainLayer.setAcceptingInput(false);
    this.hudLayer.setAcceptingInput(false);
    this.modalLayer.setAcceptingInput(true);
    this.modalLayer.setVisible(true);
  }

  dismissModal(modal: Component, _button: number): void {
    this.resume();
    this.modalLayer.setVisible(false);
    this.modalLayer.setAcceptingInput(false);
    this.modalLayer.remove(modal);
    this.backgroundLayer.setAcceptingInput(true);
    this.mainLayer.setAcceptingInput(true);
    this.hudLayer.setAcceptingInput(true);
  }

  prepareToEnter(): void {
    this.setActive(true);
  }

  enter(): void {
    this.mainLayer.setAcceptingInput(true);
    this.hudLayer.setAcceptingInput(true);
    this.modalLayer.setAcceptingInput(true);
  }

  leave(): void {
    this.mainLayer.setAcceptingInput(false);
    this.hudLayer.setAcceptingInput(false);
    this.modalLayer.setAcceptingInput(false);
  }

  disable(): void {
    this.mainLayer.setAcceptingInput(false);
    this.hudLayer.setAcceptingInput(false);
    this.modalLayer.setAcceptingInput(false);
  }

  mousePressed(button: number, x: number, y: number): void {
    const layers = [this.modalLayer, this.hudLayer, this.mainLayer, this.backgroundLayer];
    for (const layer of layers) {
      if (layer.isAcceptingInput()) {
        const components = layer.getComponents();
        for (let i = components.length - 1; i >= 0; i--) {
          const component = components[i];
          if (component.isVisible() && component.isAcceptingInput()) {
            component.mousePressed(button, x, y);
          }
        }
      }
    }
  }

  mouseReleased(button: number, x: number, y: number): void {
    const layers = [this.modalLayer, this.hudLayer, this.mainLayer, this.backgroundLayer];
    for (const layer of layers) {
      if (layer.isAcceptingInput()) {
        const components = layer.getComponents();
        for (let i = components.length - 1; i >= 0; i--) {
          const component = components[i];
          if (component.isVisible() && component.isAcceptingInput()) {
            component.mouseReleased(button, x, y);
          }
        }
      }
    }
  }

  mouseMoved(x: number, y: number): void {
    const layers = [this.modalLayer, this.hudLayer, this.mainLayer, this.backgroundLayer];
    for (const layer of layers) {
      if (layer.isAcceptingInput()) {
        const components = layer.getComponents();
        for (let i = components.length - 1; i >= 0; i--) {
          const component = components[i];
          if (component.isVisible() && component.isAcceptingInput()) {
            component.mouseMoved(this.lastMouseX, this.lastMouseY, x, y);
          }
        }
      }
    }
    this.lastMouseX = x;
    this.lastMouseY = y;
  }

  keyPressed(_key: string, _code: string): void {
    // Subclasses can override for scene-specific key handling
  }

  keyReleased(_key: string, _code: string): void {
    // Subclasses can override for scene-specific key handling
  }

  private pause(): void {
    this.paused = true;
  }

  private resume(): void {
    this.paused = false;
  }

  isPaused(): boolean {
    return this.paused;
  }

  isActive(): boolean {
    return this.active;
  }

  setActive(active: boolean): void {
    this.active = active;
  }

  isVisible(): boolean {
    return this.active;
  }

  setVisible(visible: boolean): void {
    const layers = [this.backgroundLayer, this.mainLayer, this.hudLayer, this.modalLayer];
    for (const layer of layers) {
      layer.setVisible(visible);
    }
  }

  getLayers(): SceneLayer[] {
    return [this.backgroundLayer, this.mainLayer, this.hudLayer, this.modalLayer];
  }

  setSceneDelegate(delegate: SceneDelegate | null): void {
    this.sceneDelegate = delegate;
  }

  protected requestScene(id: SceneID, replace: boolean = false): void {
    if (this.sceneDelegate) {
      this.sceneDelegate.requestScene(id, this, replace);
    }
  }
}
