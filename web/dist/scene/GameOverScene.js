import { Scene } from './Scene';
import { SceneID } from './SceneID';
import { Panel } from '../component/Panel';
import { Label } from '../component/Label';
import { ReferencePoint } from '../component/Component';
import { Color } from '../core/Color';
import { FontStore, FontID } from '../core/FontStore';
export class GameOverScene extends Scene {
    constructor(canvasWidth, canvasHeight) {
        super();
        this.init(canvasWidth, canvasHeight);
    }
    init(canvasWidth, canvasHeight) {
        const background = new Panel(canvasWidth, canvasHeight, Color.black);
        this.backgroundLayer.add(background);
        background.setPosition({ x: 0, y: 0 }, ReferencePoint.TOPLEFT);
        const h1Font = FontStore.getFont(FontID.H1);
        const gameOverLabel = new Label(canvasWidth, h1Font.getLineHeight(), h1Font, Color.red, "Game Over");
        this.mainLayer.add(gameOverLabel);
        gameOverLabel.setPosition({ x: canvasWidth / 2, y: canvasHeight / 2 }, ReferencePoint.CENTERCENTER);
    }
    getID() {
        return SceneID.GAMEOVER;
    }
}
//# sourceMappingURL=GameOverScene.js.map