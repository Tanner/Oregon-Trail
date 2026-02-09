import { Panel } from '../Panel';
import { ParallaxComponent } from './ParallaxComponent';

/**
 * Container that manages multiple ParallaxComponents, updating them in distance-sorted order.
 * Components are sorted from farthest to nearest, so distant objects render first.
 */
export class ParallaxPanel extends Panel {
  private parallaxComponents: ParallaxComponent[] = [];
  private maxDistance: number = 100;
  private paused: boolean = false;
  private speed: number = 1;

  constructor(width: number, height: number) {
    super(width, height);
    this.setShouldUpdateComponents(true);
  }

  override update(delta: number): void {
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

  addParallaxComponent(component: ParallaxComponent): void {
    this.parallaxComponents.push(component);
    this.sortComponents();

    this.components = [...this.parallaxComponents];
    for (const c of this.components) {
      c.setVisibleParent(this);
    }
  }

  removeParallaxComponent(component: ParallaxComponent): void {
    const index = this.parallaxComponents.indexOf(component);
    if (index > -1) {
      this.parallaxComponents.splice(index, 1);
      this.remove(component);
    }
  }

  private sortComponents(): void {
    this.parallaxComponents.sort((a, b) => b.getDistance() - a.getDistance());
  }

  setMaxDistance(maxDistance: number): void {
    this.maxDistance = maxDistance;
  }

  getMaxDistance(): number {
    return this.maxDistance;
  }

  setSpeed(speed: number): void {
    this.speed = speed;
  }

  getSpeed(): number {
    return this.speed;
  }

  pause(): void {
    this.paused = true;
    for (const component of this.parallaxComponents) {
      component.setPaused(true);
    }
  }

  resume(): void {
    this.paused = false;
    for (const component of this.parallaxComponents) {
      component.setPaused(false);
    }
  }

  isPaused(): boolean {
    return this.paused;
  }

  getParallaxComponents(): ParallaxComponent[] {
    return this.parallaxComponents;
  }
}
