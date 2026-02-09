import { Component, ReferencePoint } from './Component';
import { Label, Alignment } from './Label';
import { Color } from '../core/Color';
import { COLORS } from '../core/ConstantStore';
export var AcceptedCharacters;
(function (AcceptedCharacters) {
    AcceptedCharacters["LETTERS"] = "LETTERS";
    AcceptedCharacters["LETTERS_NUMBERS"] = "LETTERS_NUMBERS";
    AcceptedCharacters["NUMBERS"] = "NUMBERS";
})(AcceptedCharacters || (AcceptedCharacters = {}));
export class TextField extends Component {
    constructor(width, height, font) {
        super(width, height);
        this.disabled = false;
        this.placeholderText = '';
        this.acceptedCharacters = AcceptedCharacters.LETTERS;
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
        this.add(this.label, this.getPosition(ReferencePoint.CENTERLEFT), ReferencePoint.CENTERLEFT, TextField.PADDING, 0);
    }
    render(ctx) {
        if (!this.isVisible()) {
            return;
        }
        super.render(ctx);
    }
    keyReleased(key, char) {
        if (this.hasFocus()) {
            if (this.isEmpty()) {
                this.label.setText('');
            }
            if (key === 'Enter') {
                this.setFocus(false);
            }
            else if (key === 'Backspace' && this.label.getText().length >= 1) {
                this.label.setText(this.label.getText().substring(0, this.label.getText().length - 1));
            }
            else if (this.isAcceptedCharacter(char) && this.label.getFontWidth() < this.label.getWidth() - 1) {
                this.label.setText(this.label.getText() + char);
            }
        }
    }
    mouseReleased(button, x, y) {
        if (!this.isVisible() || !this.isAcceptingInput()) {
            return;
        }
        super.mouseReleased(button, x, y);
        if (this.isMouseOver() && !this.disabled) {
            this.setFocus(true);
        }
    }
    setPlaceholderText(text) {
        this.placeholderText = text;
        if (this.isEmpty()) {
            this.label.setText(this.placeholderText);
        }
    }
    clear() {
        if (this.placeholderText) {
            this.label.setText(this.placeholderText);
        }
        else {
            this.label.setText('');
        }
    }
    isEmpty() {
        const text = this.label.getText().trim();
        return text.length === 0 || text === this.placeholderText;
    }
    isAcceptedCharacter(char) {
        if (char.length !== 1)
            return false;
        const code = char.charCodeAt(0);
        if (this.acceptedCharacters === AcceptedCharacters.LETTERS) {
            return (code >= 65 && code <= 90) || (code >= 97 && code <= 122) || char === ' ';
        }
        else if (this.acceptedCharacters === AcceptedCharacters.LETTERS_NUMBERS) {
            return ((code >= 48 && code <= 57) ||
                (code >= 65 && code <= 90) ||
                (code >= 97 && code <= 122) ||
                char === ' ');
        }
        else if (this.acceptedCharacters === AcceptedCharacters.NUMBERS) {
            return code >= 48 && code <= 57;
        }
        return false;
    }
    setFocus(focus) {
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
        }
        else {
            this.setBackgroundColor(this.fieldColor);
            this.setBorderColor(this.fieldBorderColor);
        }
    }
    setAcceptedCharacters(acceptedCharacters) {
        this.acceptedCharacters = acceptedCharacters;
    }
    setFieldColor(color) {
        this.fieldColor = color;
    }
    setFieldFocusColor(color) {
        this.fieldFocusColor = color;
    }
    setTextColor(color) {
        this.label.setColor(color);
    }
    getText() {
        return this.label.getText().trim();
    }
    setText(text) {
        this.label.setText(text);
    }
    isDisabled() {
        return this.disabled;
    }
    setDisabled(disabled) {
        this.disabled = disabled;
    }
}
TextField.PADDING = 10;
//# sourceMappingURL=TextField.js.map