import { Scene } from './Scene';
import { SceneID } from './SceneID';
import { Panel } from '../component/Panel';
import { Button } from '../component/Button';
import { Label } from '../component/Label';
import { Counter } from '../component/Counter';
import { ConditionBar } from '../component/ConditionBar';
import { Sprite } from '../component/Sprite';
import { ReferencePoint } from '../component/Component';
import { Color } from '../core/Color';
import { FontStore, FontID } from '../core/FontStore';
import { ImageStore } from '../core/ImageStore';
var Mode;
(function (Mode) {
    Mode["NORMAL"] = "NORMAL";
    Mode["TRANSFER"] = "TRANSFER";
})(Mode || (Mode = {}));
/**
 * PartyInventoryScene - Manage party inventory with transfer, sell, and drop actions.
 */
export class PartyInventoryScene extends Scene {
    constructor(canvasWidth, canvasHeight, party) {
        super();
        this.mode = Mode.NORMAL;
        this.binItems = new Map();
        this.inventoryPanels = [];
        this.party = party;
        const fieldFont = FontStore.getFont(FontID.FIELD);
        const background = new Panel(canvasWidth, canvasHeight, new Color(59, 45, 89));
        this.backgroundLayer.add(background);
        background.setPosition({ x: 0, y: 0 }, ReferencePoint.TOPLEFT);
        this.closeButton = new Button(150, PartyInventoryScene.BUTTON_HEIGHT, Label.withTextWidth(fieldFont, Color.white, 'Close'));
        this.closeButton.addClickListener(() => this.onClose());
        this.closeButton.layout();
        this.transferButton = new Button(150, PartyInventoryScene.BUTTON_HEIGHT, Label.withTextWidth(fieldFont, Color.white, 'Transfer'));
        this.transferButton.addClickListener(() => this.onTransfer());
        this.transferButton.layout();
        this.dropButton = new Button(150, PartyInventoryScene.BUTTON_HEIGHT, Label.withTextWidth(fieldFont, Color.white, 'Drop'));
        this.dropButton.addClickListener(() => this.onDrop());
        this.dropButton.layout();
        const binLabel = Label.withTextWidth(fieldFont, Color.white, 'Bin');
        this.binCounter = new Counter(200, 40, binLabel);
        this.layoutInventory(canvasWidth, canvasHeight);
    }
    layoutInventory(canvasWidth, canvasHeight) {
        const fieldFont = FontStore.getFont(FontID.FIELD);
        let yOffset = PartyInventoryScene.PADDING;
        const members = this.party.getPartyMembers();
        for (const member of members) {
            const panel = this.createPersonPanel(member, canvasWidth - PartyInventoryScene.PADDING * 2);
            this.mainLayer.add(panel);
            panel.setPosition({ x: PartyInventoryScene.PADDING, y: yOffset }, ReferencePoint.TOPLEFT, 0, 0);
            this.inventoryPanels.push(panel);
            yOffset += panel.getHeight() + PartyInventoryScene.PADDING;
        }
        const vehicle = this.party.getVehicle();
        if (vehicle) {
            const panel = this.createVehiclePanel(vehicle, canvasWidth - PartyInventoryScene.PADDING * 2);
            this.mainLayer.add(panel);
            panel.setPosition({ x: PartyInventoryScene.PADDING, y: yOffset }, ReferencePoint.TOPLEFT);
            this.inventoryPanels.push(panel);
            yOffset += panel.getHeight() + PartyInventoryScene.PADDING;
        }
        const animals = this.party.getAnimals();
        if (animals.length > 0) {
            const animalPanel = new Panel(canvasWidth - PartyInventoryScene.PADDING * 2, 60);
            const animalLabel = new Label(200, fieldFont.getLineHeight(), fieldFont, Color.white, 'Animals:');
            animalPanel.add(animalLabel, { x: 10, y: 10 }, ReferencePoint.TOPLEFT);
            let animalX = 100;
            const horseCount = animals.filter(a => a.getType().toString() === 'HORSE').length;
            const oxCount = animals.filter(a => a.getType().toString() === 'OX').length;
            const muleCount = animals.filter(a => a.getType().toString() === 'MULE').length;
            if (horseCount > 0) {
                const horseSprite = new Sprite(24, undefined, ImageStore.getImage('HORSE_ICON'));
                const horseLabel = new Label(50, fieldFont.getLineHeight(), fieldFont, Color.white, `x${horseCount}`);
                animalPanel.add(horseSprite, { x: animalX, y: 20 }, ReferencePoint.TOPLEFT, 0, 0);
                animalPanel.add(horseLabel, { x: animalX + 30, y: 25 }, ReferencePoint.TOPLEFT, 0, 0);
                animalX += 80;
            }
            if (oxCount > 0) {
                const oxSprite = new Sprite(24, undefined, ImageStore.getImage('OX_ICON'));
                const oxLabel = new Label(50, fieldFont.getLineHeight(), fieldFont, Color.white, `x${oxCount}`);
                animalPanel.add(oxSprite, { x: animalX, y: 20 }, ReferencePoint.TOPLEFT, 0, 0);
                animalPanel.add(oxLabel, { x: animalX + 30, y: 25 }, ReferencePoint.TOPLEFT, 0, 0);
                animalX += 80;
            }
            if (muleCount > 0) {
                const muleSprite = new Sprite(24, undefined, ImageStore.getImage('MULE_ICON'));
                const muleLabel = new Label(50, fieldFont.getLineHeight(), fieldFont, Color.white, `x${muleCount}`);
                animalPanel.add(muleSprite, { x: animalX, y: 20 }, ReferencePoint.TOPLEFT, 0, 0);
                animalPanel.add(muleLabel, { x: animalX + 30, y: 25 }, ReferencePoint.TOPLEFT, 0, 0);
            }
            this.mainLayer.add(animalPanel);
            animalPanel.setPosition({ x: PartyInventoryScene.PADDING, y: yOffset }, ReferencePoint.TOPLEFT);
        }
        this.mainLayer.add(this.binCounter);
        this.binCounter.setPosition({ x: canvasWidth - PartyInventoryScene.PADDING, y: canvasHeight - PartyInventoryScene.PADDING }, ReferencePoint.BOTTOMRIGHT);
        this.mainLayer.add(this.closeButton);
        this.closeButton.setPosition({ x: PartyInventoryScene.PADDING, y: canvasHeight - PartyInventoryScene.PADDING }, ReferencePoint.BOTTOMLEFT);
        this.mainLayer.add(this.transferButton);
        this.transferButton.setPosition({ x: this.closeButton.getX() + this.closeButton.getWidth() + PartyInventoryScene.PADDING, y: canvasHeight - PartyInventoryScene.PADDING }, ReferencePoint.BOTTOMLEFT);
        this.mainLayer.add(this.dropButton);
        this.dropButton.setPosition({ x: this.transferButton.getX() + this.transferButton.getWidth() + PartyInventoryScene.PADDING, y: canvasHeight - PartyInventoryScene.PADDING }, ReferencePoint.BOTTOMLEFT);
    }
    createPersonPanel(person, width) {
        const panel = new Panel(width, 100);
        const fieldFont = FontStore.getFont(FontID.FIELD);
        const nameLabel = new Label(200, fieldFont.getLineHeight(), fieldFont, Color.white, person.getName());
        panel.add(nameLabel, { x: 10, y: 10 }, ReferencePoint.TOPLEFT);
        const healthBar = new ConditionBar(150, 20, person.getHealth());
        panel.add(healthBar, { x: 10, y: 35 }, ReferencePoint.TOPLEFT);
        const weight = person.getInventory().getWeight();
        const capacity = person.getInventory().getMaxWeight();
        const weightLabel = new Label(200, fieldFont.getLineHeight(), fieldFont, Color.white, `${weight}/${capacity} lbs`);
        panel.add(weightLabel, { x: 10, y: 60 }, ReferencePoint.TOPLEFT);
        return panel;
    }
    createVehiclePanel(vehicle, width) {
        const panel = new Panel(width, 100);
        const fieldFont = FontStore.getFont(FontID.FIELD);
        const nameLabel = new Label(200, fieldFont.getLineHeight(), fieldFont, Color.white, 'Wagon');
        panel.add(nameLabel, { x: 10, y: 10 }, ReferencePoint.TOPLEFT);
        const condition = vehicle.getCondition();
        const conditionBar = new ConditionBar(150, 20, condition);
        panel.add(conditionBar, { x: 10, y: 35 }, ReferencePoint.TOPLEFT);
        const weight = vehicle.getInventory().getWeight();
        const capacity = vehicle.getInventory().getMaxWeight();
        const weightLabel = new Label(200, fieldFont.getLineHeight(), fieldFont, Color.white, `${weight}/${capacity} lbs`);
        panel.add(weightLabel, { x: 10, y: 60 }, ReferencePoint.TOPLEFT);
        return panel;
    }
    onClose() {
        console.log('Closing inventory');
    }
    onTransfer() {
        if (this.mode === Mode.NORMAL) {
            this.mode = Mode.TRANSFER;
            console.log('Transfer mode activated - click person to transfer bin items');
        }
    }
    onDrop() {
        console.log(`Dropped ${this.binItems.size} item types from bin`);
        this.binItems.clear();
        this.updateBinDisplay();
    }
    updateBinDisplay() {
        const totalItems = Array.from(this.binItems.values()).reduce((sum, count) => sum + count, 0);
        this.binCounter.setCount(totalItems);
    }
    getID() {
        return SceneID.PARTYINVENTORY;
    }
}
PartyInventoryScene.PADDING = 20;
PartyInventoryScene.BUTTON_HEIGHT = 30;
//# sourceMappingURL=PartyInventoryScene.js.map