import { Panel } from '../Panel';
/**
 * Container that manages multiple ParallaxComponents, updating them in distance-sorted order.
 * Components are sorted from farthest to nearest, so distant objects render first.
 */
export class ParallaxPanel extends Panel {
    constructor(width, height) {
        super(width, height);
        this.parallaxComponents = [];
        this.maxDistance = 100;
        this.paused = false;
        this.speed = 1;
        this.setShouldUpdateComponents(true);
    }
    update(delta) {
        if (this.paused) {
            return;
        }
        for (let i = this.parallaxComponents.length - 1; i >= 0; i--) {
            const component = this.parallaxComponents[i];
            component.update(delta, this.speed);
            if (component.isExpired()) {
                this.removeParallaxComponent(component);
            }
        }
        super.update(delta);
    }
    addParallaxComponent(component) {
        this.parallaxComponents.push(component);
        this.sortComponents();
        this.components = [...this.parallaxComponents];
        for (const c of this.components) {
            c.setVisibleParent(this);
        }
    }
    removeParallaxComponent(component) {
        const index = this.parallaxComponents.indexOf(component);
        if (index > -1) {
            this.parallaxComponents.splice(index, 1);
            this.remove(component);
        }
    }
    sortComponents() {
        this.parallaxComponents.sort((a, b) => b.getDistance() - a.getDistance());
    }
    setMaxDistance(maxDistance) {
        this.maxDistance = maxDistance;
    }
    getMaxDistance() {
        return this.maxDistance;
    }
    setSpeed(speed) {
        this.speed = speed;
    }
    getSpeed() {
        return this.speed;
    }
    pause() {
        this.paused = true;
        for (const component of this.parallaxComponents) {
            component.setPaused(true);
        }
    }
    resume() {
        this.paused = false;
        for (const component of this.parallaxComponents) {
            component.setPaused(false);
        }
    }
    isPaused() {
        return this.paused;
    }
    getParallaxComponents() {
        return this.parallaxComponents;
    }
}
//# sourceMappingURL=ParallaxPanel.js.map