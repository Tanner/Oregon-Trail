import { Scene } from './Scene';
import { SceneID } from './SceneID';
import { Panel } from '../component/Panel';
import { Sprite } from '../component/Sprite';
import { ReferencePoint } from '../component/Component';
import { AnimatingColor } from '../core/AnimatingColor';
import { Color } from '../core/Color';
import { ImageStore } from '../core/ImageStore';

export class SplashScene extends Scene {
  private static readonly WAIT_TIME = 3000;
  private static readonly DELAY = 100;

  private time: number = 0;
  private whitePanel!: Panel;
  private blackPanel!: Panel;
  private whitePanelAnimatingColor!: AnimatingColor;
  private blackPanelAnimatingColor!: AnimatingColor;
  private nullLogo!: Sprite;
  private voidLogo!: Sprite;
  private onComplete: (() => void) | null = null;

  constructor(canvasWidth: number, canvasHeight: number, onComplete: () => void) {
    super();
    this.onComplete = onComplete;
    this.init(canvasWidth, canvasHeight);
  }

  private init(canvasWidth: number, canvasHeight: number): void {
    this.whitePanel = new Panel(canvasWidth / 2, canvasHeight);
    this.whitePanelAnimatingColor = new AnimatingColor(
      Color.black,
      Color.white,
      SplashScene.WAIT_TIME / 4 - SplashScene.DELAY
    );
    this.whitePanel.setBackgroundColor(this.whitePanelAnimatingColor);
    this.backgroundLayer.add(this.whitePanel);
    this.whitePanel.setPosition({ x: 0, y: 0 }, ReferencePoint.TOPLEFT);

    this.blackPanel = new Panel(canvasWidth / 2, canvasHeight);
    this.blackPanelAnimatingColor = new AnimatingColor(
      Color.black,
      Color.white,
      SplashScene.WAIT_TIME / 4 - SplashScene.DELAY
    );
    this.blackPanel.setBackgroundColor(this.blackPanelAnimatingColor);
    this.backgroundLayer.add(this.blackPanel);
    this.blackPanel.setPosition({ x: canvasWidth / 2, y: 0 }, ReferencePoint.TOPLEFT);

    const nullImage = ImageStore.getImage('NULL');
    this.nullLogo = new Sprite(nullImage.width, nullImage.height, nullImage);
    this.whitePanel.add(
      this.nullLogo,
      this.whitePanel.getPosition(ReferencePoint.CENTERRIGHT),
      ReferencePoint.CENTERRIGHT,
      -10,
      0
    );
    this.nullLogo.setVisible(false);

    const voidImage = ImageStore.getImage('VOID');
    this.voidLogo = new Sprite(voidImage.width, voidImage.height, voidImage);
    this.blackPanel.add(
      this.voidLogo,
      this.blackPanel.getPosition(ReferencePoint.CENTERLEFT),
      ReferencePoint.CENTERLEFT,
      10,
      0
    );
    this.voidLogo.setVisible(false);
  }

  override update(delta: number): void {
    super.update(delta);

    this.time += delta;

    if (this.time >= SplashScene.WAIT_TIME) {
      if (this.onComplete) {
        this.onComplete();
        this.onComplete = null;
      }
    } else if (this.time >= (SplashScene.WAIT_TIME * 1) / 2 + SplashScene.DELAY) {
      this.voidLogo.setVisible(true);
      this.blackPanelAnimatingColor.update(delta);
    } else if (this.time >= SplashScene.WAIT_TIME / 4 + SplashScene.DELAY) {
      this.nullLogo.setVisible(true);
      this.whitePanelAnimatingColor.update(delta);

      this.blackPanelAnimatingColor = new AnimatingColor(
        Color.white,
        Color.black,
        SplashScene.WAIT_TIME / 4 - SplashScene.DELAY
      );
      this.blackPanel.setBackgroundColor(this.blackPanelAnimatingColor);
    } else if (this.time < SplashScene.WAIT_TIME / 4) {
      this.blackPanelAnimatingColor.update(delta);
    }
  }

  getID(): SceneID {
    return SceneID.SPLASH;
  }
}
