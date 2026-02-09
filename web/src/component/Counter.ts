import { Component, ReferencePoint } from './Component';
import { Button, Disableable } from './Button';
import { Label, Alignment } from './Label';
import { Sprite } from './Sprite';
import { Color } from '../core/Color';
import { FontStore, FontID } from '../core/FontStore';

export class Counter extends Component implements Disableable {
  private static readonly COUNT_LABEL_PADDING = 5;
  private static readonly COUNT_LABEL_X_OFFSET = 10;

  private count: number = 0;
  private min: number = 0;
  private max: number = Number.MAX_SAFE_INTEGER;
  private countUpOnLeftClick: boolean = true;
  private disableAutoCount: boolean = false;
  private hideCount: boolean = false;
  private countLabel!: Label;
  private button: CountingButton;

  constructor(width: number, height: number, label: Label, sprite?: Sprite) {
    super(width, height);

    if (sprite) {
      this.button = new CountingButton(this, width, height, label, sprite);
    } else {
      this.button = new CountingButton(this, width, height, label);
    }

    this.add(this.button, this.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
    this.setCount(this.count);
  }

  override render(ctx: CanvasRenderingContext2D): void {
    super.render(ctx);
  }

  isCountUpOnLeftClick(): boolean {
    return this.countUpOnLeftClick;
  }

  setCountUpOnLeftClick(countUpOnLeftClick: boolean): void {
    this.countUpOnLeftClick = countUpOnLeftClick;
  }

  setMin(min: number): void {
    this.min = min;
  }

  getMin(): number {
    return this.min;
  }

  setMax(max: number): void {
    this.max = max;
  }

  getMax(): number {
    return this.max;
  }

  setCount(count: number): void {
    this.count = count;

    if (this.countLabel) {
      this.remove(this.countLabel);
    }

    const fieldFont = FontStore.getFont(FontID.FIELD);
    const text = '' + count;
    this.countLabel = new Label(
      Math.floor(fieldFont.getWidth(text) + Counter.COUNT_LABEL_PADDING * 2),
      fieldFont.getLineHeight(),
      fieldFont,
      Color.white,
      text
    );
    this.countLabel.setBackgroundColor(Color.red);
    this.countLabel.setAlignment(Alignment.CENTER);

    this.add(
      this.countLabel,
      this.getPosition(ReferencePoint.TOPRIGHT),
      ReferencePoint.CENTERRIGHT,
      Counter.COUNT_LABEL_X_OFFSET,
      0
    );
  }

  getCount(): number {
    return this.count;
  }

  getHideCount(): boolean {
    return this.hideCount;
  }

  setHideCount(hideCount: boolean): void {
    this.hideCount = hideCount;
    this.countLabel.setVisible(!hideCount);
  }

  getDisableAutoCount(): boolean {
    return this.disableAutoCount;
  }

  setDisableAutoCount(disableAutoCount: boolean): void {
    this.disableAutoCount = disableAutoCount;
  }

  isDisabled(): boolean {
    return this.button.isDisabled();
  }

  setDisabled(disabled: boolean): void {
    this.button.setDisabled(disabled);
  }

  setText(text: string): void {
    this.button.setText(text);
  }

  setSprite(sprite: Sprite): void {
    this.button.setSprite(sprite);
  }

  setShowLabel(showLabel: boolean): void {
    this.button.setShowLabel(showLabel);
  }

  setShowSprite(showSprite: boolean): void {
    this.button.setShowSprite(showSprite);
  }
}

class CountingButton extends Button {
  private counter: Counter;

  constructor(counter: Counter, width: number, height: number, label: Label, sprite?: Sprite) {
    if (sprite) {
      super(width, height, label, sprite);
    } else {
      super(width, height, label);
    }
    this.counter = counter;
  }

  override mousePressed(_button: number, _mx: number, _my: number): void {
    if (!this.isVisible() || !this.isAcceptingInput()) {
      return;
    }

    if (this.isMouseOver() && !this.isDisabled()) {
      this.setActive(true);
    }
  }

  override mouseReleased(button: number, _mx: number, _my: number): void {
    if (!this.isVisible()) {
      return;
    }

    if (this.isMouseOver() && !this.isDisabled() && this.isActive()) {
      this.notifyListeners();
      this.setActive(false);

      let countChange = 1;
      if (this.isAltKeyPressed()) {
        countChange = 10;
      }

      if (!this.counter.getDisableAutoCount()) {
        if (
          (button === 0 && this.counter.isCountUpOnLeftClick()) ||
          (button !== 0 && !this.counter.isCountUpOnLeftClick())
        ) {
          this.counter.setCount(Math.min(this.counter.getMax(), this.counter.getCount() + countChange));
        } else if (
          (button !== 0 && this.counter.isCountUpOnLeftClick()) ||
          (button === 0 && !this.counter.isCountUpOnLeftClick())
        ) {
          this.counter.setCount(Math.max(this.counter.getMin(), this.counter.getCount() - countChange));
        }
      }
    }
  }

  private isAltKeyPressed(): boolean {
    return false;
  }
}
