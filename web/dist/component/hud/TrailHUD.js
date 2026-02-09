import { HUD } from './HUD';
import { Panel } from '../Panel';
import { Button } from '../Button';
import { Label, VerticalAlignment } from '../Label';
import { Sprite } from '../Sprite';
import { BevelType, ReferencePoint } from '../Component';
import { Color } from '../../core/Color';
import { FontStore, FontID } from '../../core/FontStore';
import { ImageStore } from '../../core/ImageStore';
import { get as getLiteral } from '../../core/ConstantStore';
export var TrailMode;
(function (TrailMode) {
    TrailMode["TRAIL"] = "TRAIL";
    TrailMode["CAMP"] = "CAMP";
})(TrailMode || (TrailMode = {}));
export class TrailHUD extends HUD {
    constructor(width, mode, listener) {
        super(width, HUD.HEIGHT);
        this.notificationQueue = [];
        this.currentMode = mode;
        const panelWidth = width - TrailHUD.INFO_WIDTH - TrailHUD.MARGIN * 2;
        this.campPanel = this.makeCampPanel(panelWidth, listener);
        this.trailPanel = this.makeTrailPanel(panelWidth, listener);
        this.add(this.campPanel, this.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
        this.add(this.trailPanel, this.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
        this.setMode(TrailMode.TRAIL);
        const fieldFont = FontStore.getFont(FontID.FIELD);
        this.timeLabel = new Label(TrailHUD.INFO_WIDTH, fieldFont.getLineHeight(), fieldFont, Color.white, '');
        this.add(this.timeLabel, this.notificationLabel.getPosition(ReferencePoint.CENTERRIGHT), ReferencePoint.BOTTOMLEFT, TrailHUD.MARGIN, -TrailHUD.MARGIN / 2);
        this.dateLabel = new Label(TrailHUD.INFO_WIDTH, fieldFont.getLineHeight(), fieldFont, Color.white, '');
        this.add(this.dateLabel, this.notificationLabel.getPosition(ReferencePoint.CENTERRIGHT), ReferencePoint.TOPLEFT, TrailHUD.MARGIN, TrailHUD.MARGIN / 2);
        this.setBackgroundColor(Color.gray);
        this.setBevelWidth(2);
        this.setBevel(BevelType.OUT);
        this.setBottomBorderWidth(2);
        this.setBorderColor(Color.black);
    }
    makeTrailPanel(width, listener) {
        const panel = new Panel(width, HUD.HEIGHT);
        const fieldFont = FontStore.getFont(FontID.FIELD);
        const menuLabel = Label.withTextWidth(fieldFont, Color.white, getLiteral('TRAIL_SCENE', 'CAMP'));
        const fireSprite = new Sprite(48, ImageStore.getImage('CAMP_ICON'));
        this.menuButton = new Button(menuLabel.getWidth() + 2 * TrailHUD.MARGIN, TrailHUD.BUTTON_HEIGHT, menuLabel);
        this.menuButton.setSprite(fireSprite);
        this.menuButton.setShowLabel(false);
        this.menuButton.addClickListener(listener);
        panel.add(this.menuButton, this.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT, TrailHUD.MARGIN, TrailHUD.MARGIN);
        this.menuButton.setTooltipEnabled(true);
        this.menuButton.setTooltipMessage(getLiteral('TRAIL_SCENE', 'CAMP'));
        const notificationWidth = width - this.menuButton.getWidth() - TrailHUD.MARGIN * 2;
        this.notificationLabel = new Label(notificationWidth, TrailHUD.BUTTON_HEIGHT, fieldFont, Color.white, '');
        this.notificationLabel.setVerticalAlignment(VerticalAlignment.CENTER);
        this.notificationLabel.setBackgroundColor(Color.black);
        panel.add(this.notificationLabel, this.menuButton.getPosition(ReferencePoint.TOPRIGHT), ReferencePoint.TOPLEFT, TrailHUD.MARGIN, 0);
        return panel;
    }
    makeCampPanel(width, listener) {
        const panel = new Panel(width, HUD.HEIGHT);
        const buttonCount = 4;
        const buttonWidth = Math.floor((width - TrailHUD.MARGIN * 2 - TrailHUD.MARGIN * (buttonCount - 1)) / buttonCount);
        const fieldFont = FontStore.getFont(FontID.FIELD);
        const inventoryLabel = new Label(buttonWidth, TrailHUD.BUTTON_HEIGHT, fieldFont, Color.white, getLiteral('TRAIL_SCENE', 'INVENTORY'));
        this.inventoryButton = new Button(buttonWidth, TrailHUD.BUTTON_HEIGHT, inventoryLabel);
        this.inventoryButton.addClickListener(listener);
        const mapLabel = new Label(buttonWidth, TrailHUD.BUTTON_HEIGHT, fieldFont, Color.white, getLiteral('TRAIL_SCENE', 'MAP'));
        this.mapButton = new Button(buttonWidth, TrailHUD.BUTTON_HEIGHT, mapLabel);
        this.mapButton.addClickListener(listener);
        const huntLabel = new Label(buttonWidth, TrailHUD.BUTTON_HEIGHT, fieldFont, Color.white, getLiteral('TRAIL_SCENE', 'HUNT'));
        this.huntButton = new Button(buttonWidth, TrailHUD.BUTTON_HEIGHT, huntLabel);
        this.huntButton.addClickListener(listener);
        const leaveLabel = new Label(buttonWidth, TrailHUD.BUTTON_HEIGHT, fieldFont, Color.white, getLiteral('TRAIL_SCENE', 'LEAVE'));
        this.leaveButton = new Button(buttonWidth, TrailHUD.BUTTON_HEIGHT, leaveLabel);
        this.leaveButton.addClickListener(listener);
        const buttons = [this.leaveButton, this.inventoryButton, this.mapButton, this.huntButton];
        panel.addAsRow(buttons, panel.getPosition(ReferencePoint.TOPLEFT), TrailHUD.MARGIN, TrailHUD.MARGIN, TrailHUD.MARGIN);
        return panel;
    }
    updatePartyInformation(time, date) {
        this.setTime(time);
        this.setDate(date);
    }
    updateNotifications() {
        if (this.notificationQueue.length > 0) {
            this.setNotification(this.notificationQueue.shift());
        }
    }
    setTime(time) {
        this.timeLabel.setText(time);
    }
    setDate(date) {
        this.dateLabel.setText(date);
    }
    addNotification(message) {
        this.notificationQueue.push(message);
    }
    addNotifications(messages) {
        for (const message of messages) {
            this.addNotification(message);
        }
    }
    isNotificationsEmpty() {
        return this.notificationQueue.length === 0;
    }
    setNotification(message) {
        this.notificationLabel.setText(message);
    }
    getMode() {
        return this.currentMode;
    }
    setMode(mode) {
        this.currentMode = mode;
        if (this.currentMode === TrailMode.CAMP) {
            this.campPanel.setVisible(true);
            this.trailPanel.setVisible(false);
        }
        else if (this.currentMode === TrailMode.TRAIL) {
            this.campPanel.setVisible(false);
            this.trailPanel.setVisible(true);
        }
    }
    getMenuButton() {
        return this.menuButton;
    }
    getInventoryButton() {
        return this.inventoryButton;
    }
    getMapButton() {
        return this.mapButton;
    }
    getHuntButton() {
        return this.huntButton;
    }
    getLeaveButton() {
        return this.leaveButton;
    }
}
TrailHUD.MARGIN = 10;
TrailHUD.BUTTON_HEIGHT = HUD.HEIGHT - 2 * TrailHUD.MARGIN;
TrailHUD.INFO_WIDTH = 200;
//# sourceMappingURL=TrailHUD.js.map