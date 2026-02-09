class SceneLayer {
    constructor() {
        this.components = [];
        this.visibleParent = null;
        this.visible = true;
        this.acceptingInput = true;
    }
    setVisibleParent(parent) {
        this.visibleParent = parent;
    }
    isVisible() {
        if (this.visibleParent !== null) {
            return this.visible && this.visibleParent.isVisible();
        }
        return this.visible;
    }
    setVisible(visible) {
        this.visible = visible;
    }
    add(component) {
        this.components.push(component);
        component.setVisibleParent(this);
    }
    remove(component) {
        component.setVisibleParent(null);
        const index = this.components.indexOf(component);
        if (index > -1) {
            this.components.splice(index, 1);
        }
    }
    render(ctx) {
        for (const component of this.components) {
            if (component.isVisible()) {
                component.render(ctx);
            }
        }
    }
    update(delta) {
        for (const component of this.components) {
            component.update(delta);
        }
    }
    setAcceptingInput(acceptingInput) {
        this.acceptingInput = acceptingInput;
        for (const component of this.components) {
            component.setAcceptingInput(acceptingInput);
        }
    }
    isAcceptingInput() {
        return this.acceptingInput;
    }
    getComponents() {
        return this.components;
    }
}
export class Scene {
    constructor() {
        this.active = false;
        this.paused = false;
        this.lastMouseX = 0;
        this.lastMouseY = 0;
        this.backgroundLayer = new SceneLayer();
        this.backgroundLayer.setVisibleParent(this);
        this.mainLayer = new SceneLayer();
        this.mainLayer.setVisibleParent(this);
        this.hudLayer = new SceneLayer();
        this.hudLayer.setVisibleParent(this);
        this.modalLayer = new SceneLayer();
        this.modalLayer.setVisibleParent(this);
    }
    render(ctx) {
        this.backgroundLayer.render(ctx);
        this.mainLayer.render(ctx);
        this.hudLayer.render(ctx);
        this.modalLayer.render(ctx);
    }
    update(delta) {
        this.backgroundLayer.update(delta);
        this.mainLayer.update(delta);
        this.hudLayer.update(delta);
        this.modalLayer.update(delta);
    }
    showModal(modal) {
        this.pause();
        this.modalLayer.add(modal);
        this.backgroundLayer.setAcceptingInput(false);
        this.mainLayer.setAcceptingInput(false);
        this.hudLayer.setAcceptingInput(false);
        this.modalLayer.setAcceptingInput(true);
        this.modalLayer.setVisible(true);
    }
    dismissModal(modal, button) {
        this.resume();
        this.modalLayer.setVisible(false);
        this.modalLayer.setAcceptingInput(false);
        this.modalLayer.remove(modal);
        this.backgroundLayer.setAcceptingInput(true);
        this.mainLayer.setAcceptingInput(true);
        this.hudLayer.setAcceptingInput(true);
    }
    prepareToEnter() {
        this.setActive(true);
    }
    enter() {
        this.mainLayer.setAcceptingInput(true);
        this.hudLayer.setAcceptingInput(true);
        this.modalLayer.setAcceptingInput(true);
    }
    leave() {
        this.mainLayer.setAcceptingInput(false);
        this.hudLayer.setAcceptingInput(false);
        this.modalLayer.setAcceptingInput(false);
    }
    disable() {
        this.mainLayer.setAcceptingInput(false);
        this.hudLayer.setAcceptingInput(false);
        this.modalLayer.setAcceptingInput(false);
    }
    mousePressed(button, x, y) {
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
    mouseReleased(button, x, y) {
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
    mouseMoved(x, y) {
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
    keyPressed(key, code) {
        // Subclasses can override for scene-specific key handling
    }
    keyReleased(key, code) {
        // Subclasses can override for scene-specific key handling
    }
    pause() {
        this.paused = true;
    }
    resume() {
        this.paused = false;
    }
    isPaused() {
        return this.paused;
    }
    isActive() {
        return this.active;
    }
    setActive(active) {
        this.active = active;
    }
    isVisible() {
        return this.active;
    }
    setVisible(visible) {
        const layers = [this.backgroundLayer, this.mainLayer, this.hudLayer, this.modalLayer];
        for (const layer of layers) {
            layer.setVisible(visible);
        }
    }
    getLayers() {
        return [this.backgroundLayer, this.mainLayer, this.hudLayer, this.modalLayer];
    }
}
//# sourceMappingURL=Scene.js.map