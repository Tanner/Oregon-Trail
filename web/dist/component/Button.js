import { Component, BevelType, ReferencePoint } from './Component';
import { Color } from '../core/Color';
import { COLORS } from '../core/ConstantStore';
import { SoundStore } from '../core/SoundStore';
export class Button extends Component {
    constructor(width, height, labelOrColor, sprite) {
        super(width, height);
        this.label = null;
        this.sprite = null;
        this.active = false;
        this.disabled = false;
        this.listeners = [];
        this.buttonColor = COLORS.get('INTERACTIVE_NORMAL') || Color.gray;
        this.buttonActiveColor = COLORS.get('INTERACTIVE_ACTIVE') || Color.darkGray;
        this.buttonDisabledColor = COLORS.get('INTERACTIVE_DISABLED') || Color.darkGray;
        if (labelOrColor instanceof Color) {
            this.buttonColor = labelOrColor;
            this.buttonActiveColor = labelOrColor;
            this.buttonDisabledColor = labelOrColor;
            this.setBackgroundColor(labelOrColor);
            this.setBorderWidth(1);
        }
        else {
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
    layout() {
        if (this.label !== null &&
            this.label.isVisible() &&
            this.sprite !== null &&
            this.sprite.isVisible()) {
            this.sprite.setPosition(this.getPosition(ReferencePoint.CENTERCENTER), ReferencePoint.BOTTOMCENTER);
            this.label.setPosition(this.getPosition(ReferencePoint.CENTERCENTER), ReferencePoint.TOPCENTER);
        }
        else {
            if (this.label !== null) {
                this.label.setPosition(this.getPosition(ReferencePoint.CENTERCENTER), ReferencePoint.CENTERCENTER);
            }
            if (this.sprite !== null) {
                this.sprite.setPosition(this.getPosition(ReferencePoint.CENTERCENTER), ReferencePoint.CENTERCENTER);
            }
        }
    }
    setLabel(label) {
        if (this.label !== null) {
            this.remove(this.label);
        }
        this.label = label;
        if (label !== null) {
            this.add(label, this.getPosition(ReferencePoint.CENTERCENTER), ReferencePoint.CENTERCENTER);
        }
        this.layout();
    }
    setSprite(sprite) {
        if (this.sprite !== null) {
            this.remove(this.sprite);
        }
        this.sprite = sprite;
        if (sprite !== null) {
            this.add(sprite, this.getPosition(ReferencePoint.CENTERCENTER), ReferencePoint.CENTERCENTER);
        }
        this.layout();
    }
    setShowLabel(showLabel) {
        if (this.label === null) {
            return;
        }
        this.label.setVisible(showLabel);
        this.layout();
    }
    setShowSprite(showSprite) {
        if (this.sprite === null) {
            return;
        }
        this.sprite.setVisible(showSprite);
        this.layout();
    }
    render(ctx) {
        if (!this.isVisible()) {
            return;
        }
        super.render(ctx);
    }
    mouseMoved(oldx, oldy, newx, newy) {
        if (!this.isVisible() || !this.isAcceptingInput()) {
            return;
        }
        super.mouseMoved(oldx, oldy, newx, newy);
    }
    mousePressed(button, mx, my) {
        if (!this.isVisible() || !this.isAcceptingInput()) {
            return;
        }
        super.mousePressed(button, mx, my);
        if (button === 0 && this.isMouseOver() && !this.disabled) {
            this.setActive(true);
        }
    }
    mouseReleased(button, mx, my) {
        if (!this.isVisible() || !this.isAcceptingInput()) {
            return;
        }
        super.mouseReleased(button, mx, my);
        if (button === 0 && this.isMouseOver() && !this.disabled && this.active) {
            this.notifyListeners();
        }
        this.setActive(false);
    }
    setButtonColor(color) {
        this.buttonColor = color;
        if (!this.disabled && !this.active) {
            this.setBackgroundColor(this.buttonColor);
        }
    }
    setButtonActiveColor(color) {
        this.buttonActiveColor = color;
    }
    setButtonDisabledColor(color) {
        this.buttonDisabledColor = color;
    }
    setFont(font) {
        if (this.label === null) {
            return;
        }
        this.label.setFont(font);
    }
    setLabelColor(color) {
        if (this.label === null) {
            return;
        }
        this.label.setColor(color);
    }
    setText(text) {
        if (this.label === null) {
            return;
        }
        this.label.setText(text);
    }
    getText() {
        if (this.label === null) {
            return '';
        }
        return this.label.getText();
    }
    setDisabled(disabled) {
        this.disabled = disabled;
        if (disabled) {
            this.active = false;
            this.setBackgroundColor(this.buttonDisabledColor);
            this.setBevel(BevelType.NONE);
            if (this.label !== null) {
                this.label.setColor(COLORS.get('INTERACTIVE_LABEL_DISABLED') || Color.gray);
            }
        }
        else {
            if (this.isActive()) {
                this.setBackgroundColor(this.buttonActiveColor);
                this.setBevel(BevelType.IN);
            }
            else {
                this.setBackgroundColor(this.buttonColor);
                this.setBevel(BevelType.OUT);
            }
            if (this.label !== null) {
                this.label.setColor(COLORS.get('INTERACTIVE_LABEL_NORMAL') || Color.white);
            }
        }
    }
    isDisabled() {
        return this.disabled;
    }
    isActive() {
        return this.active;
    }
    setActive(active) {
        if (this.isDisabled()) {
            return;
        }
        this.active = active;
        if (active) {
            this.setBackgroundColor(this.buttonActiveColor);
            this.setBevel(BevelType.IN);
        }
        else {
            this.setBackgroundColor(this.buttonColor);
            this.setBevel(BevelType.OUT);
        }
    }
    addClickListener(listener) {
        this.listeners.push(listener);
    }
    removeClickListener(listener) {
        const index = this.listeners.indexOf(listener);
        if (index > -1) {
            this.listeners.splice(index, 1);
        }
    }
    notifyListeners() {
        SoundStore.playSound('Click');
        for (const listener of this.listeners) {
            listener();
        }
    }
    toString() {
        return `Button[active: ${this.active}, label: ${this.label}]`;
    }
}
//# sourceMappingURL=Button.js.map