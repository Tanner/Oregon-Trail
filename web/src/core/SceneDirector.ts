import { InputDelegate } from './InputManager';
import { Scene } from '../scene/Scene';
import { Component } from '../component/Component';

export class SceneDirector implements InputDelegate {
  private stack: Scene[] = [];

  pushScene(scene: Scene, popLast: boolean = false): void {
    if (this.isDuplicateScene(scene)) {
      return;
    }

    if (this.stack.length > 0) {
      this.stack[this.stack.length - 1].leave();
    }

    this.stack.push(scene);
    scene.prepareToEnter();
    scene.enter();

    if (popLast && this.stack.length > 1) {
      this.stack.splice(this.stack.length - 2, 1);
    }
  }

  popScene(): void {
    if (this.stack.length <= 1) {
      return;
    }

    const poppedScene = this.stack.pop()!;
    poppedScene.leave();
    poppedScene.setActive(false);

    if (this.stack.length > 0) {
      const currentScene = this.stack[this.stack.length - 1];
      currentScene.prepareToEnter();
      currentScene.enter();
    }
  }

  replaceStackWithScene(scene: Scene): void {
    if (this.isDuplicateScene(scene)) {
      this.popScene();
    } else {
      for (const s of this.stack) {
        s.leave();
        s.setActive(false);
      }
      this.stack = [];
      this.pushScene(scene, false);
    }
  }

  currentScene(): Scene | null {
    if (this.stack.length === 0) {
      return null;
    }
    return this.stack[this.stack.length - 1];
  }

  update(delta: number): void {
    const scene = this.currentScene();
    if (scene) {
      scene.update(delta);
    }
  }

  render(ctx: CanvasRenderingContext2D): void {
    const scene = this.currentScene();
    if (scene) {
      scene.render(ctx);
    }
  }

  mousePressed(button: number, x: number, y: number): void {
    const scene = this.currentScene();
    if (scene) {
      scene.mousePressed(button, x, y);
    }
  }

  mouseReleased(button: number, x: number, y: number): void {
    const scene = this.currentScene();
    if (scene) {
      scene.mouseReleased(button, x, y);
    }
  }

  mouseMoved(x: number, y: number): void {
    const scene = this.currentScene();
    if (scene) {
      scene.mouseMoved(x, y);
    }
  }

  keyPressed(key: string, code: string): void {
    const scene = this.currentScene();
    if (!scene) {
      return;
    }

    if (key === '+') {
      // TODO: GameDirector.sharedSceneListener().showSceneSelector();
      console.log('Scene selector requested');
    } else if (key === '-') {
      Component.changeDebugMode();
    } else if (code === 'Escape') {
      // TODO: pushScene(new OptionsScene(), false);
      console.log('Options scene requested');
    } else {
      scene.keyPressed(key, code);
    }
  }

  keyReleased(key: string, code: string): void {
    const scene = this.currentScene();
    if (scene) {
      scene.keyReleased(key, code);
    }
  }

  private isDuplicateScene(scene: Scene): boolean {
    const sceneID = scene.getID();
    for (const s of this.stack) {
      if (s.getID() === sceneID) {
        return true;
      }
    }
    return false;
  }
}
