import { Scene } from './Scene';
import { SceneID } from './SceneID';
import { Panel } from '../component/Panel';
import { Label } from '../component/Label';
import { ReferencePoint } from '../component/Component';
import { Color } from '../core/Color';
import { FontStore, FontID } from '../core/FontStore';
export class VictoryScene extends Scene {
    constructor(canvasWidth, canvasHeight) {
        super();
        this.init(canvasWidth, canvasHeight);
    }
    init(canvasWidth, canvasHeight) {
        const background = new Panel(canvasWidth, canvasHeight, Color.black);
        this.backgroundLayer.add(background);
        background.setPosition({ x: 0, y: 0 }, ReferencePoint.TOPLEFT);
        const h1Font = FontStore.getFont(FontID.H1);
        const victoryLabel = new Label(canvasWidth, h1Font.getLineHeight(), h1Font, Color.green, "Victory!");
        this.mainLayer.add(victoryLabel);
        victoryLabel.setPosition({ x: canvasWidth / 2, y: canvasHeight / 2 }, ReferencePoint.CENTERCENTER);
    }
    getID() {
        return SceneID.VICTORY;
    }
}
//# sourceMappingURL=VictoryScene.js.map