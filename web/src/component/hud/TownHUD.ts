import { HUD } from './HUD';
import { Panel } from '../Panel';
import { Button, ButtonClickListener } from '../Button';
import { Label, VerticalAlignment } from '../Label';
import { Sprite } from '../Sprite';
import { BevelType, ReferencePoint } from '../Component';
import { Color } from '../../core/Color';
import { FontStore, FontID } from '../../core/FontStore';
import { ImageStore } from '../../core/ImageStore';
import { get as getLiteral } from '../../core/ConstantStore';

export class TownHUD extends HUD {
  private static readonly MARGIN = 10;
  private static readonly BUTTON_HEIGHT = HUD.HEIGHT - 2 * TownHUD.MARGIN;
  private static readonly INFO_WIDTH = 200;

  private trailButton!: Button;
  private timeLabel!: Label;
  private dateLabel!: Label;
  private notificationLabel!: Label;
  private notificationQueue: string[] = [];

  constructor(width: number, listener: ButtonClickListener) {
    super(width, HUD.HEIGHT);

    const panelWidth = width - TownHUD.INFO_WIDTH - TownHUD.MARGIN * 2;
    const panel = new Panel(panelWidth, HUD.HEIGHT);

    const fieldFont = FontStore.getFont(FontID.FIELD);

    const menuLabel = Label.withTextWidth(fieldFont, Color.white, getLiteral('TOWN_SCENE', 'TRAIL'));

    const trailImage = ImageStore.getImage('TRAIL_ICON');
    const trailSprite = new Sprite(48);
    trailSprite.setImage(trailImage);

    this.trailButton = new Button(
      menuLabel.getWidth() + 2 * TownHUD.MARGIN,
      TownHUD.BUTTON_HEIGHT,
      menuLabel
    );
    this.trailButton.setSprite(trailSprite);
    this.trailButton.setShowLabel(false);
    this.trailButton.addClickListener(listener);
    panel.add(
      this.trailButton,
      this.getPosition(ReferencePoint.TOPLEFT),
      ReferencePoint.TOPLEFT,
      TownHUD.MARGIN,
      TownHUD.MARGIN
    );

    this.trailButton.setTooltipEnabled(true);
    this.trailButton.setTooltipMessage(getLiteral('TOWN_SCENE', 'CONTINUE_ON_TRAIL'));

    const notificationWidth = panelWidth - this.trailButton.getWidth() - TownHUD.MARGIN * 2;

    this.notificationLabel = new Label(
      notificationWidth,
      TownHUD.BUTTON_HEIGHT,
      fieldFont,
      Color.white,
      ''
    );
    this.notificationLabel.setVerticalAlignment(VerticalAlignment.CENTER);
    this.notificationLabel.setBackgroundColor(Color.black);
    panel.add(
      this.notificationLabel,
      this.trailButton.getPosition(ReferencePoint.TOPRIGHT),
      ReferencePoint.TOPLEFT,
      TownHUD.MARGIN,
      0
    );

    this.add(panel, this.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);

    this.timeLabel = new Label(
      TownHUD.INFO_WIDTH,
      fieldFont.getLineHeight(),
      fieldFont,
      Color.white,
      ''
    );
    this.add(
      this.timeLabel,
      this.notificationLabel.getPosition(ReferencePoint.CENTERRIGHT),
      ReferencePoint.BOTTOMLEFT,
      TownHUD.MARGIN,
      -TownHUD.MARGIN / 2
    );

    this.dateLabel = new Label(
      TownHUD.INFO_WIDTH,
      fieldFont.getLineHeight(),
      fieldFont,
      Color.white,
      ''
    );
    this.add(
      this.dateLabel,
      this.notificationLabel.getPosition(ReferencePoint.CENTERRIGHT),
      ReferencePoint.TOPLEFT,
      TownHUD.MARGIN,
      TownHUD.MARGIN / 2
    );

    this.setBackgroundColor(Color.gray);
    this.setBevelWidth(2);
    this.setBevel(BevelType.OUT);
    this.setBottomBorderWidth(2);
    this.setBorderColor(Color.black);
  }

  updatePartyInformation(time: string, date: string): void {
    this.setTime(time);
    this.setDate(date);
  }

  setTime(time: string): void {
    this.timeLabel.setText(time);
  }

  setDate(date: string): void {
    this.dateLabel.setText(date);
  }

  isNotificationsEmpty(): boolean {
    return this.notificationQueue.length === 0;
  }

  setNotification(message: string): void {
    this.notificationLabel.setText(message);
  }

  getTrailButton(): Button {
    return this.trailButton;
  }
}
