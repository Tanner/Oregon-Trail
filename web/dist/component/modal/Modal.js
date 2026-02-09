import { Component, ReferencePoint } from '../Component';
import { Panel } from '../Panel';
import { Label } from '../Label';
import { Button } from '../Button';
import { Color } from '../../core/Color';
import { COLORS, get as getLiteral } from '../../core/ConstantStore';
import { FontStore, FontID } from '../../core/FontStore';
export class Modal extends Component {
    constructor(width, height, listener, _message, buttonCount) {
        super(width, height);
        this.cancelButtonIndex = -1;
        this.listener = listener;
        this.buttons = new Array(buttonCount);
        this.buttonListener = new ButtonListener(this);
        const overlay = new Panel(width, height, COLORS.get('TRANSLUCENT_OVERLAY') || new Color(0, 0, 0, 0.25));
        this.add(overlay, this.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
    }
    createButtons() {
        const fieldFont = FontStore.getFont(FontID.FIELD);
        const buttonWidth = Math.floor((this.panel.getWidth() - Modal.PADDING * (this.buttons.length + 1)) / this.buttons.length);
        for (let i = 0; i < this.buttons.length; i++) {
            const buttonLabel = new Label(buttonWidth, Modal.BUTTON_HEIGHT, fieldFont, Color.white, getLiteral('GENERAL', 'OK'));
            this.buttons[i] = new Button(buttonWidth, Modal.BUTTON_HEIGHT, buttonLabel);
            this.buttons[i].addClickListener(() => this.buttonListener.onClick(this.buttons[i]));
        }
    }
    getButtonListener() {
        return this.buttonListener;
    }
    setButtonText(buttonIndex, text) {
        this.buttons[buttonIndex].setText(text);
    }
    getCancelButtonIndex() {
        return this.cancelButtonIndex;
    }
    setCancelButtonIndex(i) {
        this.cancelButtonIndex = i;
    }
    getButtons() {
        return this.buttons;
    }
    getListener() {
        return this.listener;
    }
    keyReleased(key) {
        if (!this.isVisible() || !this.isAcceptingInput()) {
            return;
        }
        if (key === 'Enter') {
            this.listener.dismissModal(this, -1);
        }
    }
}
Modal.PADDING = 20;
Modal.DEFAULT_LABEL_WIDTH = 500;
Modal.BUTTON_HEIGHT = 40;
class ButtonListener {
    constructor(modal) {
        this.modal = modal;
    }
    onClick(source) {
        let buttonIndex = -1;
        const buttons = this.modal.getButtons();
        for (let i = 0; i < buttons.length; i++) {
            if (buttons[i] === source) {
                buttonIndex = i;
                break;
            }
        }
        this.modal.getListener().dismissModal(this.modal, buttonIndex);
    }
}
//# sourceMappingURL=Modal.js.map