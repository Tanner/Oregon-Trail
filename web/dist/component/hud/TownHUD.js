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
export class TownHUD extends HUD {
    constructor(width, listener) {
        super(width, HUD.HEIGHT);
        this.notificationQueue = [];
        const panelWidth = width - TownHUD.INFO_WIDTH - TownHUD.MARGIN * 2;
        const panel = new Panel(panelWidth, HUD.HEIGHT);
        const fieldFont = FontStore.getFont(FontID.FIELD);
        const menuLabel = Label.withTextWidth(fieldFont, Color.white, getLiteral('TOWN_SCENE', 'TRAIL'));
        const trailImage = ImageStore.getImage('TRAIL_ICON');
        const trailSprite = new Sprite(48);
        trailSprite.setImage(trailImage);
        this.trailButton = new Button(menuLabel.getWidth() + 2 * TownHUD.MARGIN, TownHUD.BUTTON_HEIGHT, menuLabel);
        this.trailButton.setSprite(trailSprite);
        this.trailButton.setShowLabel(false);
        this.trailButton.addClickListener(listener);
        panel.add(this.trailButton, this.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT, TownHUD.MARGIN, TownHUD.MARGIN);
        this.trailButton.setTooltipEnabled(true);
        this.trailButton.setTooltipMessage(getLiteral('TOWN_SCENE', 'CONTINUE_ON_TRAIL'));
        const notificationWidth = panelWidth - this.trailButton.getWidth() - TownHUD.MARGIN * 2;
        this.notificationLabel = new Label(notificationWidth, TownHUD.BUTTON_HEIGHT, fieldFont, Color.white, '');
        this.notificationLabel.setVerticalAlignment(VerticalAlignment.CENTER);
        this.notificationLabel.setBackgroundColor(Color.black);
        panel.add(this.notificationLabel, this.trailButton.getPosition(ReferencePoint.TOPRIGHT), ReferencePoint.TOPLEFT, TownHUD.MARGIN, 0);
        this.add(panel, this.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
        this.timeLabel = new Label(TownHUD.INFO_WIDTH, fieldFont.getLineHeight(), fieldFont, Color.white, '');
        this.add(this.timeLabel, this.notificationLabel.getPosition(ReferencePoint.CENTERRIGHT), ReferencePoint.BOTTOMLEFT, TownHUD.MARGIN, -TownHUD.MARGIN / 2);
        this.dateLabel = new Label(TownHUD.INFO_WIDTH, fieldFont.getLineHeight(), fieldFont, Color.white, '');
        this.add(this.dateLabel, this.notificationLabel.getPosition(ReferencePoint.CENTERRIGHT), ReferencePoint.TOPLEFT, TownHUD.MARGIN, TownHUD.MARGIN / 2);
        this.setBackgroundColor(Color.gray);
        this.setBevelWidth(2);
        this.setBevel(BevelType.OUT);
        this.setBottomBorderWidth(2);
        this.setBorderColor(Color.black);
    }
    updatePartyInformation(time, date) {
        this.setTime(time);
        this.setDate(date);
    }
    setTime(time) {
        this.timeLabel.setText(time);
    }
    setDate(date) {
        this.dateLabel.setText(date);
    }
    isNotificationsEmpty() {
        return this.notificationQueue.length === 0;
    }
    setNotification(message) {
        this.notificationLabel.setText(message);
    }
    getTrailButton() {
        return this.trailButton;
    }
}
TownHUD.MARGIN = 10;
TownHUD.BUTTON_HEIGHT = HUD.HEIGHT - 2 * TownHUD.MARGIN;
TownHUD.INFO_WIDTH = 200;
//# sourceMappingURL=TownHUD.js.map