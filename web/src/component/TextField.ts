import { Component, ReferencePoint } from './Component';
import { Label, Alignment } from './Label';
import { Disableable } from './Button';
import { Color } from '../core/Color';
import { COLORS } from '../core/ConstantStore';
import { BitmapFont } from '../core/BitmapFont';

export enum AcceptedCharacters {
  LETTERS = 'LETTERS',
  LETTERS_NUMBERS = 'LETTERS_NUMBERS',
  NUMBERS = 'NUMBERS'
}

export class TextField extends Component implements Disableable {
  private static readonly PADDING = 10;

  private label: Label;
  private fieldColor: Color;
  private fieldFocusColor: Color;
  private fieldBorderColor: Color;
  private fieldFocusBorderColor: Color;

  private disabled: boolean = false;
  private placeholderText: string = '';

  private acceptedCharacters: AcceptedCharacters = AcceptedCharacters.LETTERS;

  constructor(width: number, height: number, font: BitmapFont) {
    super(width, height);

    this.fieldColor = COLORS.get('INTERACTIVE_NORMAL') || Color.gray;
    this.fieldFocusColor = COLORS.get('INTERACTIVE_ACTIVE') || Color.darkGray;
    this.fieldBorderColor = COLORS.get('INTERACTIVE_BORDER_LIGHT') || Color.white.darker(0.25);
    this.fieldFocusBorderColor =
      COLORS.get('INTERACTIVE_BORDER_FOCUS_LIGHT') || Color.white.darker(0.5);

    this.setBackgroundColor(this.fieldColor);
    this.setBorderColor(Color.white);
    this.setBorderWidth(2);

    this.label = new Label(width - TextField.PADDING * 2, height, font, Color.white, '');
    this.label.setAlignment(Alignment.LEFT);
    this.add(
      this.label,
      this.getPosition(ReferencePoint.CENTERLEFT),
      ReferencePoint.CENTERLEFT,
      TextField.PADDING,
      0
    );
  }

  override render(ctx: CanvasRenderingContext2D): void {
    if (!this.isVisible()) {
      return;
    }

    super.render(ctx);
  }

  override keyReleased(key: string, _char: string): void {
    if (this.hasFocus()) {
      if (this.isEmpty()) {
        this.label.setText('');
      }

      if (key === 'Enter') {
        this.setFocus(false);
      } else if (key === 'Backspace' && this.label.getText().length >= 1) {
        this.label.setText(this.label.getText().substring(0, this.label.getText().length - 1));
      } else if (this.isAcceptedCharacter(key) && this.label.getFontWidth() < this.label.getWidth() - 1) {
        this.label.setText(this.label.getText() + key);
      }
    }
  }

  override mouseReleased(button: number, x: number, y: number): void {
    if (!this.isVisible() || !this.isAcceptingInput()) {
      return;
    }

    super.mouseReleased(button, x, y);

    if (this.isMouseOver() && !this.disabled) {
      this.setFocus(true);
    }
  }

  setPlaceholderText(text: string): void {
    this.placeholderText = text;

    if (this.isEmpty()) {
      this.label.setText(this.placeholderText);
    }
  }

  clear(): void {
    if (this.placeholderText) {
      this.label.setText(this.placeholderText);
    } else {
      this.label.setText('');
    }
  }

  isEmpty(): boolean {
    const text = this.label.getText().trim();
    return text.length === 0 || text === this.placeholderText;
  }

  isAcceptedCharacter(char: string): boolean {
    if (char.length !== 1) return false;

    const code = char.charCodeAt(0);

    if (this.acceptedCharacters === AcceptedCharacters.LETTERS) {
      return (code >= 65 && code <= 90) || (code >= 97 && code <= 122) || char === ' ';
    } else if (this.acceptedCharacters === AcceptedCharacters.LETTERS_NUMBERS) {
      return (
        (code >= 48 && code <= 57) ||
        (code >= 65 && code <= 90) ||
        (code >= 97 && code <= 122) ||
        char === ' '
      );
    } else if (this.acceptedCharacters === AcceptedCharacters.NUMBERS) {
      return code >= 48 && code <= 57;
    }

    return false;
  }

  override setFocus(focus: boolean): void {
    if (!focus && this.hasFocus()) {
      if (this.isEmpty()) {
        if (this.placeholderText) {
          this.label.setText(this.placeholderText);
        }
      }

      this.notifyListeners();
    }

    super.setFocus(focus);

    if (this.hasFocus()) {
      this.setBackgroundColor(this.fieldFocusColor);
      this.setBorderColor(this.fieldFocusBorderColor);
    } else {
      this.setBackgroundColor(this.fieldColor);
      this.setBorderColor(this.fieldBorderColor);
    }
  }

  setAcceptedCharacters(acceptedCharacters: AcceptedCharacters): void {
    this.acceptedCharacters = acceptedCharacters;
  }

  setFieldColor(color: Color): void {
    this.fieldColor = color;
  }

  setFieldFocusColor(color: Color): void {
    this.fieldFocusColor = color;
  }

  setTextColor(color: Color): void {
    this.label.setColor(color);
  }

  getText(): string {
    return this.label.getText().trim();
  }

  setText(text: string): void {
    this.label.setText(text);
  }

  isDisabled(): boolean {
    return this.disabled;
  }

  setDisabled(disabled: boolean): void {
    this.disabled = disabled;
  }
}
