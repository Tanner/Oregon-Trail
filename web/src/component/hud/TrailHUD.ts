import { HUD } from './HUD';
import { Panel } from '../Panel';
import { Button, ButtonClickListener } from '../Button';
import { Label, VerticalAlignment } from '../Label';
import { Sprite } from '../Sprite';
import { Component, BevelType, ReferencePoint } from '../Component';
import { Color } from '../../core/Color';
import { FontStore, FontID } from '../../core/FontStore';
import { ImageStore } from '../../core/ImageStore';
import { get as getLiteral } from '../../core/ConstantStore';

export enum TrailMode {
  TRAIL = 'TRAIL',
  CAMP = 'CAMP'
}

export class TrailHUD extends HUD {
  private static readonly MARGIN = 10;
  private static readonly BUTTON_HEIGHT = HUD.HEIGHT - 2 * TrailHUD.MARGIN;
  private static readonly INFO_WIDTH = 200;

  private currentMode: TrailMode;

  private menuButton!: Button;
  private inventoryButton!: Button;
  private mapButton!: Button;
  private huntButton!: Button;
  private leaveButton!: Button;

  private timeLabel!: Label;
  private dateLabel!: Label;
  private notificationLabel!: Label;

  private trailPanel!: Panel;
  private campPanel!: Panel;

  private notificationQueue: string[] = [];

  constructor(width: number, mode: TrailMode, listener: ButtonClickListener) {
    super(width, HUD.HEIGHT);

    this.currentMode = mode;

    const panelWidth = width - TrailHUD.INFO_WIDTH - TrailHUD.MARGIN * 2;

    this.campPanel = this.makeCampPanel(panelWidth, listener);
    this.trailPanel = this.makeTrailPanel(panelWidth, listener);

    this.add(this.campPanel, this.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
    this.add(this.trailPanel, this.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
    this.setMode(TrailMode.TRAIL);

    const fieldFont = FontStore.getFont(FontID.FIELD);

    this.timeLabel = new Label(
      TrailHUD.INFO_WIDTH,
      fieldFont.getLineHeight(),
      fieldFont,
      Color.white,
      ''
    );
    this.add(
      this.timeLabel,
      this.notificationLabel.getPosition(ReferencePoint.CENTERRIGHT),
      ReferencePoint.BOTTOMLEFT,
      TrailHUD.MARGIN,
      -TrailHUD.MARGIN / 2
    );

    this.dateLabel = new Label(
      TrailHUD.INFO_WIDTH,
      fieldFont.getLineHeight(),
      fieldFont,
      Color.white,
      ''
    );
    this.add(
      this.dateLabel,
      this.notificationLabel.getPosition(ReferencePoint.CENTERRIGHT),
      ReferencePoint.TOPLEFT,
      TrailHUD.MARGIN,
      TrailHUD.MARGIN / 2
    );

    this.setBackgroundColor(Color.gray);
    this.setBevelWidth(2);
    this.setBevel(BevelType.OUT);
    this.setBottomBorderWidth(2);
    this.setBorderColor(Color.black);
  }

  private makeTrailPanel(width: number, listener: ButtonClickListener): Panel {
    const panel = new Panel(width, HUD.HEIGHT);

    const fieldFont = FontStore.getFont(FontID.FIELD);

    const menuLabel = Label.withTextWidth(
      fieldFont,
      Color.white,
      getLiteral('TRAIL_SCENE', 'CAMP')
    );

    const fireSprite = new Sprite(48, ImageStore.getImage('CAMP_ICON'));

    this.menuButton = new Button(
      menuLabel.getWidth() + 2 * TrailHUD.MARGIN,
      TrailHUD.BUTTON_HEIGHT,
      menuLabel
    );
    this.menuButton.setSprite(fireSprite);
    this.menuButton.setShowLabel(false);
    this.menuButton.addClickListener(listener);
    panel.add(
      this.menuButton,
      this.getPosition(ReferencePoint.TOPLEFT),
      ReferencePoint.TOPLEFT,
      TrailHUD.MARGIN,
      TrailHUD.MARGIN
    );

    this.menuButton.setTooltipEnabled(true);
    this.menuButton.setTooltipMessage(getLiteral('TRAIL_SCENE', 'CAMP'));

    const notificationWidth = width - this.menuButton.getWidth() - TrailHUD.MARGIN * 2;

    this.notificationLabel = new Label(
      notificationWidth,
      TrailHUD.BUTTON_HEIGHT,
      fieldFont,
      Color.white,
      ''
    );
    this.notificationLabel.setVerticalAlignment(VerticalAlignment.CENTER);
    this.notificationLabel.setBackgroundColor(Color.black);
    panel.add(
      this.notificationLabel,
      this.menuButton.getPosition(ReferencePoint.TOPRIGHT),
      ReferencePoint.TOPLEFT,
      TrailHUD.MARGIN,
      0
    );

    return panel;
  }

  private makeCampPanel(width: number, listener: ButtonClickListener): Panel {
    const panel = new Panel(width, HUD.HEIGHT);

    const buttonCount = 4;
    const buttonWidth = Math.floor(
      (width - TrailHUD.MARGIN * 2 - TrailHUD.MARGIN * (buttonCount - 1)) / buttonCount
    );

    const fieldFont = FontStore.getFont(FontID.FIELD);

    const inventoryLabel = new Label(
      buttonWidth,
      TrailHUD.BUTTON_HEIGHT,
      fieldFont,
      Color.white,
      getLiteral('TRAIL_SCENE', 'INVENTORY')
    );
    this.inventoryButton = new Button(buttonWidth, TrailHUD.BUTTON_HEIGHT, inventoryLabel);
    this.inventoryButton.addClickListener(listener);

    const mapLabel = new Label(
      buttonWidth,
      TrailHUD.BUTTON_HEIGHT,
      fieldFont,
      Color.white,
      getLiteral('TRAIL_SCENE', 'MAP')
    );
    this.mapButton = new Button(buttonWidth, TrailHUD.BUTTON_HEIGHT, mapLabel);
    this.mapButton.addClickListener(listener);

    const huntLabel = new Label(
      buttonWidth,
      TrailHUD.BUTTON_HEIGHT,
      fieldFont,
      Color.white,
      getLiteral('TRAIL_SCENE', 'HUNT')
    );
    this.huntButton = new Button(buttonWidth, TrailHUD.BUTTON_HEIGHT, huntLabel);
    this.huntButton.addClickListener(listener);

    const leaveLabel = new Label(
      buttonWidth,
      TrailHUD.BUTTON_HEIGHT,
      fieldFont,
      Color.white,
      getLiteral('TRAIL_SCENE', 'LEAVE')
    );
    this.leaveButton = new Button(buttonWidth, TrailHUD.BUTTON_HEIGHT, leaveLabel);
    this.leaveButton.addClickListener(listener);

    const buttons = [this.leaveButton, this.inventoryButton, this.mapButton, this.huntButton];

    panel.addAsRow(
      buttons,
      panel.getPosition(ReferencePoint.TOPLEFT),
      TrailHUD.MARGIN,
      TrailHUD.MARGIN,
      TrailHUD.MARGIN
    );

    return panel;
  }

  updatePartyInformation(time: string, date: string): void {
    this.setTime(time);
    this.setDate(date);
  }

  updateNotifications(): void {
    if (this.notificationQueue.length > 0) {
      this.setNotification(this.notificationQueue.shift()!);
    }
  }

  setTime(time: string): void {
    this.timeLabel.setText(time);
  }

  setDate(date: string): void {
    this.dateLabel.setText(date);
  }

  addNotification(message: string): void {
    this.notificationQueue.push(message);
  }

  addNotifications(messages: string[]): void {
    for (const message of messages) {
      this.addNotification(message);
    }
  }

  isNotificationsEmpty(): boolean {
    return this.notificationQueue.length === 0;
  }

  private setNotification(message: string): void {
    this.notificationLabel.setText(message);
  }

  getMode(): TrailMode {
    return this.currentMode;
  }

  setMode(mode: TrailMode): void {
    this.currentMode = mode;

    if (this.currentMode === TrailMode.CAMP) {
      this.campPanel.setVisible(true);
      this.trailPanel.setVisible(false);
    } else if (this.currentMode === TrailMode.TRAIL) {
      this.campPanel.setVisible(false);
      this.trailPanel.setVisible(true);
    }
  }

  getMenuButton(): Button {
    return this.menuButton;
  }

  getInventoryButton(): Button {
    return this.inventoryButton;
  }

  getMapButton(): Button {
    return this.mapButton;
  }

  getHuntButton(): Button {
    return this.huntButton;
  }

  getLeaveButton(): Button {
    return this.leaveButton;
  }
}
