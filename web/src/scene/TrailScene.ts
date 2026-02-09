import { Scene } from './Scene';
import { SceneID } from './SceneID';
import { Party } from '../model/Party';
import { Panel } from '../component/Panel';
import { TrailHUD, TrailMode } from '../component/hud/TrailHUD';
import { ParallaxPanel } from '../component/parallax/ParallaxPanel';
import { ParallaxComponentLoop } from '../component/parallax/ParallaxComponentLoop';
import { WagonSprite } from '../component/sprite/WagonSprite';
import { AnimatingColor } from '../core/AnimatingColor';
import { Color } from '../core/Color';
import { ImageStore } from '../core/ImageStore';
import { ReferencePoint } from '../component/Component';

/**
 * TrailScene - The core gameplay scene where the party travels along the trail.
 * Features parallax scrolling, walking animations, day/night cycle, and camp mode.
 */
export class TrailScene extends Scene {
  private static readonly WALK_TICK_INTERVAL = 1000;
  private static readonly STEP_COUNT_TRIGGER = 2;

  private party: Party;
  private hud: TrailHUD;
  private parallaxPanel: ParallaxPanel;
  private sky: Panel;
  private skyColor: AnimatingColor;
  private wagon: WagonSprite | null = null;

  private walking: boolean = true;
  private timeElapsed: number = 0;
  private stepCounter: number = 0;

  constructor(canvasWidth: number, canvasHeight: number, party: Party) {
    super();
    this.party = party;
    this.hud = new TrailHUD(canvasWidth, TrailMode.TRAIL, () => {});
    this.parallaxPanel = new ParallaxPanel(canvasWidth, canvasHeight);
    this.sky = new Panel(canvasWidth, canvasHeight);
    this.skyColor = new AnimatingColor(
      new Color(135, 206, 235),
      new Color(25, 25, 112),
      10000
    );

    this.initialize(canvasWidth, canvasHeight);
  }

  private initialize(canvasWidth: number, canvasHeight: number): void {
    this.sky.setBackgroundColor(this.skyColor.getCurrentColor());
    this.backgroundLayer.add(this.sky);
    this.sky.setPosition({ x: 0, y: 0 }, ReferencePoint.TOPLEFT);

    this.setupParallax(canvasWidth, canvasHeight);

    if (this.party.getVehicle()) {
      this.wagon = new WagonSprite();
      this.mainLayer.add(this.wagon);
      this.wagon.setPosition(
        { x: canvasWidth / 2, y: canvasHeight - 150 },
        ReferencePoint.BOTTOMCENTER
      );
    }

    this.hudLayer.add(this.hud);
    this.hud.setPosition({ x: 0, y: 0 }, ReferencePoint.TOPLEFT);
  }

  private setupParallax(canvasWidth: number, canvasHeight: number): void {
    const groundImage = ImageStore.getImage('GRASS');
    const ground = new ParallaxComponentLoop(canvasWidth, groundImage, 1);
    this.parallaxPanel.addParallaxComponent(ground);
    ground.setPosition(
      { x: 0, y: canvasHeight - ground.getHeight() },
      ReferencePoint.TOPLEFT
    );

    const trailImage = ImageStore.getImage('TRAIL');
    const trail = new ParallaxComponentLoop(canvasWidth, trailImage, 2);
    this.parallaxPanel.addParallaxComponent(trail);
    trail.setPosition(
      { x: 0, y: canvasHeight - trail.getHeight() - 20 },
      ReferencePoint.TOPLEFT
    );

    const hillAImage = ImageStore.getImage('HILL_A');
    const hillA = new ParallaxComponentLoop(canvasWidth * 2, hillAImage, 10);
    this.parallaxPanel.addParallaxComponent(hillA);
    hillA.setPosition(
      { x: 0, y: canvasHeight - ground.getHeight() - hillA.getHeight() + 50 },
      ReferencePoint.TOPLEFT
    );

    const hillBImage = ImageStore.getImage('HILL_B');
    const hillB = new ParallaxComponentLoop(canvasWidth * 2, hillBImage, 15);
    this.parallaxPanel.addParallaxComponent(hillB);
    hillB.setPosition(
      { x: canvasWidth / 4, y: canvasHeight - ground.getHeight() - hillB.getHeight() + 30 },
      ReferencePoint.TOPLEFT
    );

    const cloudAImage = ImageStore.getImage('CLOUD_A');
    const cloudA = new ParallaxComponentLoop(canvasWidth * 3, cloudAImage, 50);
    this.parallaxPanel.addParallaxComponent(cloudA);
    cloudA.setPosition({ x: 0, y: 50 }, ReferencePoint.TOPLEFT);

    const cloudBImage = ImageStore.getImage('CLOUD_B');
    const cloudB = new ParallaxComponentLoop(canvasWidth * 3, cloudBImage, 60);
    this.parallaxPanel.addParallaxComponent(cloudB);
    cloudB.setPosition({ x: canvasWidth / 2, y: 100 }, ReferencePoint.TOPLEFT);

    this.parallaxPanel.setSpeed(20);
    this.backgroundLayer.add(this.parallaxPanel);
    this.parallaxPanel.setPosition({ x: 0, y: 0 }, ReferencePoint.TOPLEFT);
  }

  override update(delta: number): void {
    super.update(delta);

    if (this.walking) {
      this.timeElapsed += delta;

      if (this.timeElapsed >= TrailScene.WALK_TICK_INTERVAL) {
        this.timeElapsed = 0;
        this.stepCounter++;

        if (this.stepCounter >= TrailScene.STEP_COUNT_TRIGGER) {
          this.stepCounter = 0;
          this.walkStep();
        }
      }

      this.parallaxPanel.update(delta);
      if (this.wagon) {
        this.wagon.update(delta);
      }
    }

    this.skyColor.update(delta);
    this.sky.setBackgroundColor(this.skyColor.getCurrentColor());
  }

  private walkStep(): void {
    const trail = this.party.getTrail();
    if (!trail) {
      console.warn('No trail set for party');
      return;
    }

    if (trail.getConditionPercentage() <= 0) {
      console.log('Trail complete - arriving at destination');
      this.walking = false;
      return;
    }

    const members = this.party.getPartyMembers();
    if (members.length === 0 || members.every(m => m.getHealth().getCurrent() <= 0)) {
      console.log('All party members died - game over');
      this.walking = false;
      return;
    }
  }

  toggleCamp(): void {
    this.walking = !this.walking;

    if (this.walking) {
      this.parallaxPanel.resume();
      this.hud.setMode(TrailMode.TRAIL);
    } else {
      this.parallaxPanel.pause();
      this.hud.setMode(TrailMode.CAMP);
    }
  }

  override getID(): SceneID {
    return SceneID.TRAIL;
  }
}
