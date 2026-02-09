import { Scene } from './Scene';
import { SceneID } from './SceneID';
import { Panel } from '../component/Panel';
import { Label } from '../component/Label';
import { ReferencePoint } from '../component/Component';
import { Color } from '../core/Color';
import { FontStore, FontID } from '../core/FontStore';

export class VictoryScene extends Scene {
  constructor(canvasWidth: number, canvasHeight: number) {
    super();
    this.init(canvasWidth, canvasHeight);
  }

  private init(canvasWidth: number, canvasHeight: number): void {
    const background = new Panel(canvasWidth, canvasHeight, Color.black);
    this.backgroundLayer.add(background);
    background.setPosition({ x: 0, y: 0 }, ReferencePoint.TOPLEFT);

    const h1Font = FontStore.getFont(FontID.H1);
    const victoryLabel = new Label("Victory!", h1Font, Color.green, canvasWidth);
    this.mainLayer.add(victoryLabel);
    victoryLabel.setPosition(
      { x: canvasWidth / 2, y: canvasHeight / 2 },
      ReferencePoint.CENTERCENTER
    );
  }

  getID(): SceneID {
    return SceneID.VICTORY;
  }
}
