import { Scene } from './Scene';
import { SceneID } from './SceneID';
import { Panel } from '../component/Panel';
import { Sprite } from '../component/Sprite';
import { Button } from '../component/Button';
import { Label } from '../component/Label';
import { ReferencePoint } from '../component/Component';
import { Color } from '../core/Color';
import { FontStore, FontID } from '../core/FontStore';
import { ImageStore } from '../core/ImageStore';
import { SoundStore } from '../core/SoundStore';
export class MainMenuScene extends Scene {
    constructor(canvasWidth, canvasHeight) {
        super();
        this.init(canvasWidth, canvasHeight);
    }
    init(canvasWidth, canvasHeight) {
        const trailMapImage = ImageStore.getImage('TRAIL_MAP');
        const backgroundPanel = new Panel(canvasWidth, canvasHeight, trailMapImage);
        this.backgroundLayer.add(backgroundPanel);
        backgroundPanel.setPosition({ x: 0, y: 0 }, ReferencePoint.TOPLEFT);
        const logoImage = ImageStore.getImage('LOGO');
        const logoSprite = new Sprite(480, undefined, logoImage);
        this.mainLayer.add(logoSprite);
        logoSprite.setPosition({ x: canvasWidth / 2, y: 0 }, ReferencePoint.TOPCENTER, 0, MainMenuScene.LOGO_PADDING);
        const fieldFont = FontStore.getFont(FontID.FIELD);
        this.newGameButton = new Button(MainMenuScene.BUTTON_WIDTH, MainMenuScene.BUTTON_HEIGHT, Label.withTextWidth(fieldFont, Color.white, 'New Game'));
        this.newGameButton.addClickListener(() => this.onNewGame());
        this.newGameButton.layout();
        this.loadButton = new Button(MainMenuScene.BUTTON_WIDTH, MainMenuScene.BUTTON_HEIGHT, Label.withTextWidth(fieldFont, Color.white, 'Load'));
        this.loadButton.addClickListener(() => this.onLoad());
        this.loadButton.setDisabled(true);
        this.loadButton.layout();
        this.optionsButton = new Button(MainMenuScene.BUTTON_WIDTH, MainMenuScene.BUTTON_HEIGHT, Label.withTextWidth(fieldFont, Color.white, 'Options'));
        this.optionsButton.addClickListener(() => this.onOptions());
        this.optionsButton.layout();
        const buttonY = canvasHeight - MainMenuScene.BUTTON_HEIGHT * 2;
        let currentX = MainMenuScene.PADDING;
        const buttons = [this.newGameButton, this.loadButton, this.optionsButton];
        for (const button of buttons) {
            this.mainLayer.add(button);
            button.setPosition({ x: currentX, y: buttonY }, ReferencePoint.TOPLEFT);
            currentX += button.getWidth() + MainMenuScene.PADDING;
        }
    }
    onNewGame() {
        console.log('New Game clicked - PartyCreationScene not yet implemented');
    }
    onLoad() {
        console.log('Load clicked');
    }
    onOptions() {
        console.log('Options clicked');
    }
    enter() {
        super.enter();
        SoundStore.setVolume(0.5);
        SoundStore.loopMusic('GBU');
    }
    leave() {
        super.leave();
        SoundStore.stopMusic();
    }
    getID() {
        return SceneID.MAINMENU;
    }
}
MainMenuScene.BUTTON_WIDTH = 230;
MainMenuScene.BUTTON_HEIGHT = 60;
MainMenuScene.PADDING = 20;
MainMenuScene.LOGO_PADDING = 75;
//# sourceMappingURL=MainMenuScene.js.map