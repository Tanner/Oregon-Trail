import { Component, ReferencePoint } from './Component';
import { Color } from '../core/Color';

export interface Condition {
  getCurrent(): number;
  getPercentage(): number;
  getMin(): number;
  getMax(): number;
}

export class ConditionBar extends Component {
  private condition: Condition | null = null;

  private warningLevel: number = 0.5;
  private dangerLevel: number = 0.25;

  private normalColor: Color;
  private warningColor: Color;
  private dangerColor: Color;

  private disableText: boolean = true;
  private label: any | null = null;

  private conditionPanel: ConditionPanel;

  constructor(width: number, height: number, condition: Condition | null = null, font: any = null) {
    super(width, height);

    this.conditionPanel = new ConditionPanel(this.getWidth(), this.getHeight(), Color.gray, this);
    this.add(this.conditionPanel, this.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);

    if (condition !== null) {
      this.setCondition(condition);
    }
    this.setDisableText(true);

    this.normalColor = Color.fromHex(0x00a300);
    this.warningColor = Color.fromHex(0xe0e000);
    this.dangerColor = Color.red;

    if (font !== null) {
      this.setDisableText(false);
    }
  }

  override render(ctx: CanvasRenderingContext2D): void {
    if (!this.isVisible() || this.condition === null) {
      return;
    }

    super.render(ctx);
  }

  getBarColor(): Color {
    if (this.condition === null) {
      return this.normalColor;
    }

    let barColor = this.normalColor;
    if (this.condition.getPercentage() < this.dangerLevel) {
      barColor = this.dangerColor;
    } else if (this.condition.getPercentage() < this.warningLevel) {
      barColor = this.warningColor;
    }

    return barColor;
  }

  getCondition(): Condition | null {
    return this.condition;
  }

  setCondition(condition: Condition): void {
    this.condition = condition;
    this.updateBar();
  }

  private updateBar(): void {
    this.update(0);
  }

  override update(delta: number): void {
    if (this.condition !== null) {
      const percentage = this.condition.getPercentage();
      if (this.label !== null) {
        this.label.setText(Math.floor(percentage * 100) + '%');
      }

      this.conditionPanel.setPercentage(percentage);
    }
  }

  getDisableText(): boolean {
    return this.disableText;
  }

  setDisableText(disableText: boolean): void {
    this.disableText = disableText;

    if (this.label !== null) {
      this.label.setVisible(!disableText);
    }
  }

  setFont(font: any): void {
    if (this.label !== null) {
      this.label.setFont(font);
    }
  }

  setWarningLevel(level: number): void {
    this.warningLevel = level;
  }

  setDangerLevel(level: number): void {
    this.dangerLevel = level;
  }

  setNormalColor(color: Color): void {
    this.normalColor = color;
  }

  setWarningColor(color: Color): void {
    this.warningColor = color;
  }

  setDangerColor(color: Color): void {
    this.dangerColor = color;
  }
}

class ConditionPanel extends Component {
  private percentage: number = 0;
  private panelBackgroundColor: Color;
  private parentBar: ConditionBar;

  constructor(width: number, height: number, color: Color, parentBar: ConditionBar) {
    super(width, height);
    this.panelBackgroundColor = color;
    this.parentBar = parentBar;
    this.setBackgroundColor(color);
  }

  override render(ctx: CanvasRenderingContext2D): void {
    if (!this.isVisible()) {
      return;
    }

    super.render(ctx);

    ctx.fillStyle = this.parentBar.getBarColor().toCSS();
    ctx.fillRect(this.getX(), this.getY(), Math.floor(this.getWidth() * this.percentage), this.getHeight());
  }

  setPercentage(percentage: number): void {
    this.percentage = percentage;
  }
}
