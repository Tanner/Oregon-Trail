import { Scene } from './Scene';
import { SceneID } from './SceneID';
import { MapComponent } from '../component/MapComponent';
import { Panel } from '../component/Panel';
import { Button } from '../component/Button';
import { Label, Alignment } from '../component/Label';
import { ReferencePoint } from '../component/Component';
import { AnimatingSprite } from '../component/sprite/AnimatingSprite';
import { Color } from '../core/Color';
import { ImageStore } from '../core/ImageStore';
import { FontStore, FontID } from '../core/FontStore';
import { get as constantGet } from '../core/ConstantStore';
/**
 * Displays the world map with location markers, trails, and current position indicator.
 * Shows a legend explaining location tiers and provides navigation back to camp.
 */
export class MapScene extends Scene {
    constructor(worldMap) {
        super();
        this.locationButtons = [];
        this.worldMap = worldMap;
        this.currNode = worldMap.getCurrLocationNode();
        this.currTrail = worldMap.getCurrTrail();
        this.currLocPtr = new AnimatingSprite(48, 48);
        this.currLocParty = new AnimatingSprite(24, 24);
        this.returnToCamp = new Button(0, 0);
        this.initialize();
    }
    initialize() {
        const canvas = document.querySelector('canvas');
        if (!canvas)
            return;
        const containerWidth = canvas.width;
        const containerHeight = canvas.height;
        const devMode = false;
        const playerMap = new MapComponent(containerWidth, containerHeight, this.worldMap, devMode);
        this.locationButtons = new Array(this.worldMap.getNumLocations()).fill(null);
        for (let i = 0; i <= this.worldMap.getMaxRank(); i++) {
            const nodes = this.worldMap.getMapNodes().get(i) || [];
            for (const location of nodes) {
                if (location.isVisible() || devMode) {
                    const buttonSize = 8 + Math.floor(location.getConditionPercentage() * 5);
                    const button = new Button(buttonSize, buttonSize, this.getLocColor(location));
                    button.setTooltipEnabled(true);
                    button.setTooltipMessage(location.getName());
                    playerMap.add(button);
                    button.setPosition({ x: location.getPlayerMapX(), y: location.getPlayerMapY() }, ReferencePoint.TOPLEFT);
                    button.addListener(() => {
                        console.log(`Clicked location: ${location.debugToString()}`);
                    });
                    this.locationButtons[location.getID()] = button;
                }
            }
        }
        this.setupAnimatedPointer();
        let curLocX;
        let curLocY;
        if (this.currTrail.getConditionPercentage() < 1.0) {
            curLocX = Math.floor(this.currTrail.getCurrTrailLocationX());
            curLocY = Math.floor(this.currTrail.getCurrTrailLocationY());
        }
        else {
            curLocX = Math.floor(this.currNode.getPlayerMapX());
            curLocY = Math.floor(this.currNode.getPlayerMapY());
        }
        playerMap.add(this.currLocPtr);
        this.currLocPtr.setPosition({ x: curLocX - 18, y: curLocY - 48 }, ReferencePoint.TOPLEFT);
        playerMap.add(this.currLocParty);
        this.currLocParty.setPosition({ x: curLocX - 6, y: curLocY - 75 }, ReferencePoint.TOPLEFT);
        const fieldFont = FontStore.getFont(FontID.FIELD);
        this.mainLayer.add(playerMap);
        this.setupLegend(fieldFont, containerWidth);
        const returnCampWidth = Math.floor((containerWidth - MapScene.PADDING * 4) / 4);
        this.returnToCamp = new Button(returnCampWidth, MapScene.REGULAR_BUTTON_HEIGHT, Label.withTextWidth(fieldFont, Color.white, constantGet('MAP_SCENE', 'RETURN_CAMP')));
        this.returnToCamp.addClickListener(() => {
            console.log('Returning to camp');
        });
        this.returnToCamp.layout();
        this.mainLayer.add(this.returnToCamp);
        this.returnToCamp.setPosition({ x: 20, y: containerHeight - 20 }, ReferencePoint.BOTTOMLEFT);
        const mapBackground = new Panel(containerWidth, containerHeight, ImageStore.getImage('TRAIL_MAP'));
        this.backgroundLayer.add(mapBackground);
    }
    setupAnimatedPointer() {
        const pointerFrames = [];
        for (let i = 6; i >= 1; i--) {
            pointerFrames.push(ImageStore.getImage(`MAP_POINTER${i}`));
        }
        this.currLocPtr.addAnimation('default', pointerFrames, 100);
        const partyFrames = [];
        for (let i = 1; i <= 4; i++) {
            partyFrames.push(ImageStore.getImage(`MAP_PARTY${i}`));
        }
        this.currLocParty.addAnimation('default', partyFrames, 200);
    }
    setupLegend(fieldFont, containerWidth) {
        const legend1 = new Label(MapScene.PADDING * 6, fieldFont.getCharHeight(), fieldFont, Color.white, 'Legend');
        legend1.setAlignment(Alignment.LEFT);
        this.mainLayer.add(legend1);
        legend1.setPosition({ x: containerWidth - 2 * legend1.getWidth(), y: legend1.getWidth() / 4 }, ReferencePoint.TOPRIGHT);
        const legendItems = [
            { size: 13, color: Color.white, text: 'Trailblaze to get here' },
            { size: 8, color: new Color(0, 0, 0), text: 'Ghost Town' },
            { size: 9, color: new Color(255, 255, 0), text: 'Outpost' },
            { size: 10, color: new Color(255, 200, 0), text: 'Village' },
            { size: 11, color: new Color(255, 150, 0), text: 'Town' },
            { size: 12, color: new Color(255, 100, 0), text: 'City' },
            { size: 13, color: new Color(255, 0, 0), text: 'Frontier Metropolis' }
        ];
        let previousButton = null;
        let referenceY = legend1.getY() + legend1.getHeight() + MapScene.PADDING / 2;
        for (const item of legendItems) {
            const button = new Button(item.size, item.size, item.color);
            const label = new Label(MapScene.PADDING * 15, fieldFont.getCharHeight(), fieldFont, Color.white, item.text);
            label.setAlignment(Alignment.LEFT);
            this.mainLayer.add(button);
            if (previousButton === null) {
                button.setPosition({ x: legend1.getX() - MapScene.PADDING / 2, y: referenceY }, ReferencePoint.TOPRIGHT);
            }
            else {
                button.setPosition({ x: previousButton.getX() + previousButton.getWidth(), y: previousButton.getY() + previousButton.getHeight() + MapScene.PADDING / 2 }, ReferencePoint.TOPRIGHT);
            }
            this.mainLayer.add(label);
            label.setPosition({ x: button.getX() + button.getWidth() + MapScene.PADDING, y: button.getY() + button.getHeight() / 2 }, ReferencePoint.CENTERLEFT);
            previousButton = button;
        }
    }
    getLocColor(node) {
        if (!node.getHasInTrail()) {
            return Color.white;
        }
        const percentage = node.getConditionPercentage();
        if (percentage < 0.10) {
            return new Color(0, 0, 0);
        }
        else if (percentage < 0.30) {
            return new Color(255, 255, 0);
        }
        else if (percentage < 0.50) {
            return new Color(255, 200, 0);
        }
        else if (percentage < 0.70) {
            return new Color(255, 150, 0);
        }
        else if (percentage < 0.90) {
            return new Color(255, 100, 0);
        }
        else {
            return new Color(255, 0, 0);
        }
    }
    update(delta) {
        super.update(delta);
        this.currLocPtr.update(delta);
        this.currLocParty.update(delta);
    }
    getID() {
        return SceneID.MAP;
    }
}
MapScene.PADDING = 20;
MapScene.REGULAR_BUTTON_HEIGHT = 30;
//# sourceMappingURL=MapScene.js.map