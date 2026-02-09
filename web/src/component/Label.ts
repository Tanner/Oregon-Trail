import { Component } from './Component';
import { Color } from '../core/Color';
import { BitmapFont } from '../core/BitmapFont';

export enum Alignment {
  CENTER = 'CENTER',
  LEFT = 'LEFT'
}

export enum VerticalAlignment {
  CENTER = 'CENTER',
  TOP = 'TOP'
}

export class Label extends Component {
  private static readonly Y_OFFSET = 1;

  private lines: string[] = [];
  private text: string;
  private font: BitmapFont;
  private color: Color;
  private alignment: Alignment = Alignment.CENTER;
  private verticalAlignment: VerticalAlignment = VerticalAlignment.CENTER;
  private clip: boolean = true;

  constructor(width: number, height: number, font: BitmapFont, color: Color, text: string = '') {
    super(width, height);
    this.font = font;
    this.color = color;
    this.text = text;
    this.parseLines();
  }

  static withAutoHeight(width: number, font: BitmapFont, color: Color, text: string): Label {
    const height = Label.calculateHeight(width, text, font);
    const label = new Label(width, height, font, color, text);
    return label;
  }

  static withTextWidth(font: BitmapFont, color: Color, text: string): Label {
    const width = font.getWidth(text);
    const label = new Label(width, font.getLineHeight(), font, color, text);
    label.clip = false;
    return label;
  }

  override render(ctx: CanvasRenderingContext2D): void {
    if (!this.isVisible()) {
      return;
    }

    super.render(ctx);

    ctx.save();

    if (this.clip) {
      ctx.beginPath();
      ctx.rect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
      ctx.clip();
    }

    let startY: number;

    if (this.verticalAlignment === VerticalAlignment.CENTER) {
      startY = this.getY() + (this.getHeight() - this.lines.length * this.font.getLineHeight()) / 2;
    } else {
      startY = this.getY();
    }

    startY += Label.Y_OFFSET;

    ctx.fillStyle = this.color.toCSS();

    for (let i = 0; i < this.lines.length; i++) {
      const line = this.lines[i];
      const lineY = startY + this.font.getLineHeight() * i;

      if (this.alignment === Alignment.CENTER) {
        const lineWidth = this.font.getWidth(line);
        const lineX = this.getX() + 1 + (this.getWidth() - lineWidth) / 2;
        this.font.drawText(ctx, line, lineX, lineY);
      } else if (this.alignment === Alignment.LEFT) {
        this.font.drawText(ctx, line, this.getX(), lineY);
      }
    }

    ctx.restore();
  }

  private static calculateHeight(width: number, text: string, font: BitmapFont): number {
    const textLines = text.split('\n');
    let lines = 0;

    for (const textLine of textLines) {
      const words = textLine.split(/\s+/);
      let currentText = '';

      for (const word of words) {
        const testText = currentText ? currentText + ' ' + word : word;
        if (font.getWidth(testText) > width) {
          if (currentText) {
            currentText = '';
            lines++;
          }
        }
        currentText = currentText ? currentText + ' ' + word : word;
      }

      if (currentText) {
        lines++;
      }
    }

    return lines * font.getLineHeight();
  }

  private parseLines(): void {
    this.lines = [];
    const textLines = this.text.split('\n');

    const maxLines = Math.floor(this.getHeight() / this.getFontHeight()) - 1;
    let currentLine = 0;

    for (const textLine of textLines) {
      const words = textLine.split(/\s+/);
      let currentText = '';

      for (const word of words) {
        const testText = currentText ? currentText + ' ' + word : word;

        if (currentLine !== maxLines && this.font.getWidth(testText) > this.getWidth()) {
          if (currentText) {
            this.lines.push(currentText.trim());
            currentText = '';
            currentLine++;
          }
        }
        currentText = currentText ? currentText + ' ' + word : word;
      }

      if (currentText) {
        this.lines.push(currentText.trim());
        currentLine++;
      }
    }
  }

  static getNumberOfNewlines(text: string): number {
    return text.split('\n').length;
  }

  setFont(font: BitmapFont): void {
    this.font = font;
    this.parseLines();
  }

  getFontWidth(): number {
    return this.font.getWidth(this.text);
  }

  getFontHeight(): number {
    return this.font.getLineHeight();
  }

  setText(text: string): void {
    this.text = text;
    this.parseLines();
  }

  getText(): string {
    return this.text;
  }

  setColor(color: Color): void {
    this.color = color;
  }

  setAlignment(alignment: Alignment): void {
    this.alignment = alignment;
  }

  setVerticalAlignment(verticalAlignment: VerticalAlignment): void {
    this.verticalAlignment = verticalAlignment;
  }

  toString(): string {
    return `Label[text: "${this.text}"]`;
  }
}
