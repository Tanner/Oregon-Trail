import { Component } from '../component/Component';
import { FadeOutTransition, FadeInTransition } from './Transition';
import { Color } from './Color';
export class SceneDirector {
    constructor() {
        this.stack = [];
        this.transitionState = null;
    }
    pushScene(scene, popLast = false, animated = false, transitionOut = null, transitionIn = null) {
        if (this.isDuplicateScene(scene)) {
            return;
        }
        if (animated && this.stack.length > 0) {
            const outTrans = transitionOut || new FadeOutTransition(Color.black);
            const inTrans = transitionIn || new FadeInTransition(Color.black);
            this.transitionState = {
                outTransition: outTrans,
                inTransition: inTrans,
                oldScene: this.stack[this.stack.length - 1],
                newScene: scene,
                popLast: popLast,
                phase: 'out'
            };
            this.stack[this.stack.length - 1].leave();
        }
        else {
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
    }
    popScene(animated = false, transitionOut = null, transitionIn = null) {
        if (this.stack.length <= 1) {
            return;
        }
        if (animated) {
            const outTrans = transitionOut || new FadeOutTransition(Color.black);
            const inTrans = transitionIn || new FadeInTransition(Color.black);
            const poppedScene = this.stack[this.stack.length - 1];
            const nextScene = this.stack[this.stack.length - 2];
            this.transitionState = {
                outTransition: outTrans,
                inTransition: inTrans,
                oldScene: poppedScene,
                newScene: nextScene,
                popLast: false,
                phase: 'out'
            };
            poppedScene.leave();
        }
        else {
            const poppedScene = this.stack.pop();
            poppedScene.leave();
            poppedScene.setActive(false);
            if (this.stack.length > 0) {
                const currentScene = this.stack[this.stack.length - 1];
                currentScene.prepareToEnter();
                currentScene.enter();
            }
        }
    }
    replaceStackWithScene(scene) {
        if (this.isDuplicateScene(scene)) {
            this.popScene();
        }
        else {
            for (const s of this.stack) {
                s.leave();
                s.setActive(false);
            }
            this.stack = [];
            this.pushScene(scene, false);
        }
    }
    currentScene() {
        if (this.stack.length === 0) {
            return null;
        }
        return this.stack[this.stack.length - 1];
    }
    update(delta) {
        if (this.transitionState) {
            this.updateTransition(delta);
        }
        else {
            const scene = this.currentScene();
            if (scene) {
                scene.update(delta);
            }
        }
    }
    render(ctx) {
        if (this.transitionState) {
            this.renderTransition(ctx);
        }
        else {
            const scene = this.currentScene();
            if (scene) {
                scene.render(ctx);
            }
        }
    }
    updateTransition(delta) {
        if (!this.transitionState) {
            return;
        }
        const { outTransition, inTransition, phase } = this.transitionState;
        if (phase === 'out' && outTransition) {
            outTransition.update(delta);
            if (outTransition.isComplete()) {
                this.completeOutTransition();
            }
        }
        else if (phase === 'in' && inTransition) {
            inTransition.update(delta);
            if (inTransition.isComplete()) {
                this.completeInTransition();
            }
        }
    }
    renderTransition(ctx) {
        if (!this.transitionState) {
            return;
        }
        const { outTransition, inTransition, oldScene, newScene, phase } = this.transitionState;
        if (phase === 'out' && outTransition && oldScene) {
            outTransition.preRender(ctx);
            oldScene.render(ctx);
            outTransition.postRender(ctx);
        }
        else if (phase === 'in' && inTransition) {
            inTransition.preRender(ctx);
            newScene.render(ctx);
            inTransition.postRender(ctx);
        }
    }
    completeOutTransition() {
        if (!this.transitionState) {
            return;
        }
        const { oldScene, newScene, popLast } = this.transitionState;
        if (oldScene) {
            oldScene.setActive(false);
        }
        const oldSceneIndex = this.stack.indexOf(oldScene);
        if (oldSceneIndex !== -1 && oldScene !== newScene) {
            if (popLast && oldSceneIndex < this.stack.length - 1) {
                this.stack.splice(oldSceneIndex, 1);
            }
        }
        if (!this.stack.includes(newScene)) {
            this.stack.push(newScene);
        }
        newScene.prepareToEnter();
        newScene.enter();
        this.transitionState.phase = 'in';
    }
    completeInTransition() {
        this.transitionState = null;
    }
    mousePressed(button, x, y) {
        if (this.transitionState) {
            return;
        }
        const scene = this.currentScene();
        if (scene) {
            scene.mousePressed(button, x, y);
        }
    }
    mouseReleased(button, x, y) {
        if (this.transitionState) {
            return;
        }
        const scene = this.currentScene();
        if (scene) {
            scene.mouseReleased(button, x, y);
        }
    }
    mouseMoved(x, y) {
        if (this.transitionState) {
            return;
        }
        const scene = this.currentScene();
        if (scene) {
            scene.mouseMoved(x, y);
        }
    }
    keyPressed(key, code) {
        if (this.transitionState) {
            return;
        }
        const scene = this.currentScene();
        if (!scene) {
            return;
        }
        if (key === '+') {
            // TODO: GameDirector.sharedSceneListener().showSceneSelector();
            console.log('Scene selector requested');
        }
        else if (key === '-') {
            Component.changeDebugMode();
        }
        else if (code === 'Escape') {
            // TODO: pushScene(new OptionsScene(), false);
            console.log('Options scene requested');
        }
        else {
            scene.keyPressed(key, code);
        }
    }
    keyReleased(key, code) {
        if (this.transitionState) {
            return;
        }
        const scene = this.currentScene();
        if (scene) {
            scene.keyReleased(key, code);
        }
    }
    isDuplicateScene(scene) {
        const sceneID = scene.getID();
        for (const s of this.stack) {
            if (s.getID() === sceneID) {
                return true;
            }
        }
        return false;
    }
}
//# sourceMappingURL=SceneDirector.js.map