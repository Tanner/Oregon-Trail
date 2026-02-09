import { Component, BevelType, ReferencePoint } from './Component';
import { Label } from './Label';
import { Sprite } from './Sprite';
import { Color } from '../core/Color';
import { COLORS } from '../core/ConstantStore';
import { SoundStore } from '../core/SoundStore';

export interface Disableable {
  isDisabled(): boolean;
  setDisabled(disabled: boolean): void;
}

export type ButtonClickListener = () => void;

export class Button extends Component implements Disableable {
  protected label: Label | null = null;
  protected sprite: Sprite | null = null;

  private buttonColor: Color;
  private buttonActiveColor: Color;
  private buttonDisabledColor: Color;

  private active: boolean = false;
  private disabled: boolean = false;

  private listeners: ButtonClickListener[] = [];

  constructor(width: number, height: number, labelOrColor?: Label | Color, sprite?: Sprite) {
    super(width, height);

    this.buttonColor = COLORS.get('INTERACTIVE_NORMAL') || Color.gray;
    this.buttonActiveColor = COLORS.get('INTERACTIVE_ACTIVE') || Color.darkGray;
    this.buttonDisabledColor = COLORS.get('INTERACTIVE_DISABLED') || Color.darkGray;

    if (labelOrColor instanceof Color) {
      this.buttonColor = labelOrColor;
      this.buttonActiveColor = labelOrColor;
      this.buttonDisabledColor = labelOrColor;
      this.setBackgroundColor(labelOrColor);
      this.setBorderWidth(1);
    } else {
      this.setBackgroundColor(this.buttonColor);
      this.setBevel(BevelType.OUT);
      this.setBevelWidth(2);
      this.setBorderColor(Color.black);
      this.setBorderWidth(2);

      if (labelOrColor) {
        this.setLabel(labelOrColor);
      }
    }

    if (sprite) {
      this.setSprite(sprite);
    }
  }

  layout(): void {
    if (
      this.label !== null &&
      this.label.isVisible() &&
      this.sprite !== null &&
      this.sprite.isVisible()
    ) {
      this.sprite.setPosition(
        this.getPosition(ReferencePoint.CENTERCENTER),
        ReferencePoint.BOTTOMCENTER
      );
      this.label.setPosition(
        this.getPosition(ReferencePoint.CENTERCENTER),
        ReferencePoint.TOPCENTER
      );
    } else {
      if (this.label !== null) {
        this.label.setPosition(
          this.getPosition(ReferencePoint.CENTERCENTER),
          ReferencePoint.CENTERCENTER
        );
      }

      if (this.sprite !== null) {
        this.sprite.setPosition(
          this.getPosition(ReferencePoint.CENTERCENTER),
          ReferencePoint.CENTERCENTER
        );
      }
    }
  }

  setLabel(label: Label | null): void {
    if (this.label !== null) {
      this.remove(this.label);
    }

    this.label = label;

    if (label !== null) {
      this.add(label, this.getPosition(ReferencePoint.CENTERCENTER), ReferencePoint.CENTERCENTER);
    }

    this.layout();
  }

  setSprite(sprite: Sprite | null): void {
    if (this.sprite !== null) {
      this.remove(this.sprite);
    }

    this.sprite = sprite;

    if (sprite !== null) {
      this.add(sprite, this.getPosition(ReferencePoint.CENTERCENTER), ReferencePoint.CENTERCENTER);
    }

    this.layout();
  }

  setShowLabel(showLabel: boolean): void {
    if (this.label === null) {
      return;
    }

    this.label.setVisible(showLabel);
    this.layout();
  }

  setShowSprite(showSprite: boolean): void {
    if (this.sprite === null) {
      return;
    }

    this.sprite.setVisible(showSprite);
    this.layout();
  }

  override render(ctx: CanvasRenderingContext2D): void {
    if (!this.isVisible()) {
      return;
    }

    super.render(ctx);
  }

  override mouseMoved(oldx: number, oldy: number, newx: number, newy: number): void {
    if (!this.isVisible() || !this.isAcceptingInput()) {
      return;
    }

    super.mouseMoved(oldx, oldy, newx, newy);
  }

  override mousePressed(button: number, mx: number, my: number): void {
    if (!this.isVisible() || !this.isAcceptingInput()) {
      return;
    }

    super.mousePressed(button, mx, my);

    if (button === 0 && this.isMouseOver() && !this.disabled) {
      this.setActive(true);
    }
  }

  override mouseReleased(button: number, mx: number, my: number): void {
    if (!this.isVisible() || !this.isAcceptingInput()) {
      return;
    }

    super.mouseReleased(button, mx, my);

    if (button === 0 && this.isMouseOver() && !this.disabled && this.active) {
      this.notifyListeners();
    }

    this.setActive(false);
  }

  setButtonColor(color: Color): void {
    this.buttonColor = color;

    if (!this.disabled && !this.active) {
      this.setBackgroundColor(this.buttonColor);
    }
  }

  setButtonActiveColor(color: Color): void {
    this.buttonActiveColor = color;
  }

  setButtonDisabledColor(color: Color): void {
    this.buttonDisabledColor = color;
  }

  setFont(font: any): void {
    if (this.label === null) {
      return;
    }

    this.label.setFont(font);
  }

  setLabelColor(color: Color): void {
    if (this.label === null) {
      return;
    }

    this.label.setColor(color);
  }

  setText(text: string): void {
    if (this.label === null) {
      return;
    }

    this.label.setText(text);
  }

  getText(): string {
    if (this.label === null) {
      return '';
    }

    return this.label.getText();
  }

  setDisabled(disabled: boolean): void {
    this.disabled = disabled;

    if (disabled) {
      this.active = false;

      this.setBackgroundColor(this.buttonDisabledColor);
      this.setBevel(BevelType.NONE);

      if (this.label !== null) {
        this.label.setColor(COLORS.get('INTERACTIVE_LABEL_DISABLED') || Color.gray);
      }
    } else {
      if (this.isActive()) {
        this.setBackgroundColor(this.buttonActiveColor);
        this.setBevel(BevelType.IN);
      } else {
        this.setBackgroundColor(this.buttonColor);
        this.setBevel(BevelType.OUT);
      }

      if (this.label !== null) {
        this.label.setColor(COLORS.get('INTERACTIVE_LABEL_NORMAL') || Color.white);
      }
    }
  }

  isDisabled(): boolean {
    return this.disabled;
  }

  isActive(): boolean {
    return this.active;
  }

  setActive(active: boolean): void {
    if (this.isDisabled()) {
      return;
    }

    this.active = active;

    if (active) {
      this.setBackgroundColor(this.buttonActiveColor);
      this.setBevel(BevelType.IN);
    } else {
      this.setBackgroundColor(this.buttonColor);
      this.setBevel(BevelType.OUT);
    }
  }

  addClickListener(listener: ButtonClickListener): void {
    this.listeners.push(listener);
  }

  removeClickListener(listener: ButtonClickListener): void {
    const index = this.listeners.indexOf(listener);
    if (index > -1) {
      this.listeners.splice(index, 1);
    }
  }

  protected notifyListeners(): void {
    SoundStore.playSound('Click');
    for (const listener of this.listeners) {
      listener();
    }
  }

  toString(): string {
    return `Button[active: ${this.active}, label: ${this.label}]`;
  }
}
