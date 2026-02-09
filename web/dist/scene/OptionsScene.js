import { Scene } from './Scene';
import { SceneID } from './SceneID';
import { Panel } from '../component/Panel';
import { Label, Alignment } from '../component/Label';
import { Button } from '../component/Button';
import { Counter } from '../component/Counter';
import { SegmentedControl } from '../component/SegmentedControl';
import { ComponentModal } from '../component/modal/ComponentModal';
import { Color } from '../core/Color';
import { FontStore, FontID } from '../core/FontStore';
import { SoundStore } from '../core/SoundStore';
import { ReferencePoint } from '../component/Component';
/**
 * Options scene with volume controls and save/load functionality
 */
export class OptionsScene extends Scene {
    constructor(canvasWidth, canvasHeight) {
        super();
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
    }
    setOnMainMenu(callback) {
        this.onMainMenu = callback;
    }
    setOnBack(callback) {
        this.onBack = callback;
    }
    init() {
        // Black background
        const background = new Panel(this.canvasWidth, this.canvasHeight, Color.black);
        this.backgroundLayer.add(background);
        const h1Font = FontStore.getFont(FontID.H1);
        const fieldFont = FontStore.getFont(FontID.FIELD);
        // Title
        const titleLabel = new Label(OptionsScene.BUTTON_WIDTH, h1Font.getLineHeight(), h1Font, Color.white, 'Options');
        titleLabel.setAlignment(Alignment.CENTER);
        // Volume control
        const volumeLabel = new Label(OptionsScene.BUTTON_WIDTH, fieldFont.getLineHeight(), fieldFont, Color.white, 'Volume');
        volumeLabel.setAlignment(Alignment.CENTER);
        const volumeButtonLabel = new Label(OptionsScene.BUTTON_WIDTH, fieldFont.getLineHeight(), fieldFont, Color.white, '');
        this.volumeCounter = new Counter(OptionsScene.BUTTON_WIDTH, OptionsScene.BUTTON_HEIGHT, volumeButtonLabel);
        this.volumeCounter.setMin(0);
        this.volumeCounter.setMax(10);
        this.volumeCounter.setCount(Math.round(SoundStore.getVolume() * 10));
        this.volumeCounter.setCountUpOnLeftClick(true);
        this.volumeCounter.addListener(() => {
            const volume = this.volumeCounter.getCount() / 10;
            SoundStore.setVolume(volume);
        });
        // Volume panel combining label and counter
        const volumePanel = new Panel(OptionsScene.BUTTON_WIDTH, volumeLabel.getHeight() + this.volumeCounter.getHeight());
        volumePanel.add(volumeLabel, { x: 0, y: 0 }, ReferencePoint.TOPLEFT, 0, 0);
        volumePanel.add(this.volumeCounter, { x: 0, y: volumeLabel.getHeight() }, ReferencePoint.TOPLEFT, 0, 0);
        // Save button
        const saveLabel = new Label(OptionsScene.BUTTON_WIDTH, fieldFont.getLineHeight(), fieldFont, Color.white, 'Save');
        this.saveButton = new Button(OptionsScene.BUTTON_WIDTH, OptionsScene.BUTTON_HEIGHT, saveLabel);
        this.saveButton.addListener(() => this.handleSaveButton());
        // Main Menu button
        const mainMenuLabel = new Label(OptionsScene.BUTTON_WIDTH, fieldFont.getLineHeight(), fieldFont, Color.white, 'Main Menu');
        this.mainMenuButton = new Button(OptionsScene.BUTTON_WIDTH, OptionsScene.BUTTON_HEIGHT, mainMenuLabel);
        this.mainMenuButton.addListener(() => this.handleMainMenuButton());
        // Back button
        const backLabel = new Label(OptionsScene.BUTTON_WIDTH, fieldFont.getLineHeight(), fieldFont, Color.white, 'Back');
        this.backButton = new Button(OptionsScene.BUTTON_WIDTH, OptionsScene.BUTTON_HEIGHT, backLabel);
        this.backButton.addListener(() => this.handleBackButton());
        // Layout components in column
        const components = [
            titleLabel,
            volumePanel,
            this.saveButton,
            this.mainMenuButton,
            this.backButton
        ];
        let totalHeight = 0;
        for (const component of components) {
            totalHeight += component.getHeight() + OptionsScene.PADDING;
        }
        totalHeight -= OptionsScene.PADDING; // Remove last padding
        let currentY = -totalHeight / 2;
        for (const component of components) {
            this.mainLayer.add(component);
            component.setPosition({ x: this.canvasWidth / 2, y: this.canvasHeight / 2 }, ReferencePoint.CENTERCENTER, -OptionsScene.BUTTON_WIDTH / 2, currentY);
            currentY += component.getHeight() + OptionsScene.PADDING;
        }
        // Create save modal (5 save slots)
        const saveControl = new SegmentedControl(600, 150, 2, 3, 20, true, 1, 'Game 1', 'Game 2', 'Game 3', 'Game 4', 'Game 5');
        this.saveModal = new ComponentModal(this.canvasWidth, this.canvasHeight, this, 'Select a save slot:', 1, saveControl);
    }
    dismissModal(modal, button) {
        super.dismissModal(modal, button);
        const currentModal = modal;
        if (currentModal === this.saveModal && button === 0) {
            const selection = this.saveModal.getComponent().getSelection();
            if (selection.length > 0) {
                const slot = selection[0];
                this.saveGame(slot);
            }
        }
    }
    getID() {
        return SceneID.OPTIONS;
    }
    handleSaveButton() {
        this.showModal(this.saveModal);
    }
    handleMainMenuButton() {
        if (this.onMainMenu) {
            this.onMainMenu();
        }
    }
    handleBackButton() {
        if (this.onBack) {
            this.onBack();
        }
    }
    /**
     * Save game to localStorage
     */
    saveGame(slot) {
        // Placeholder implementation
        // In full implementation, this would serialize the Game state
        const saveData = {
            slot: slot,
            timestamp: Date.now(),
            // Game state would be serialized here
        };
        const key = `oregon-trail-save-${slot}`;
        localStorage.setItem(key, JSON.stringify(saveData));
        console.log(`Game saved to slot ${slot + 1}`);
    }
    /**
     * Load game from localStorage
     */
    static loadGame(slot) {
        const key = `oregon-trail-save-${slot}`;
        const data = localStorage.getItem(key);
        if (!data) {
            return null;
        }
        try {
            return JSON.parse(data);
        }
        catch (e) {
            console.error('Failed to parse save data:', e);
            return null;
        }
    }
}
OptionsScene.BUTTON_WIDTH = 250;
OptionsScene.BUTTON_HEIGHT = 50;
OptionsScene.PADDING = 20;
//# sourceMappingURL=OptionsScene.js.map