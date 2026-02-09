import { Button } from './Button';
export class ToggleButton extends Button {
    constructor(width, height, label) {
        super(width, height, label);
        this.disableAutoToggle = false;
    }
    mousePressed(button, mx, my) {
        if (!this.isVisible() || !this.isAcceptingInput()) {
            return;
        }
        if (button === 0 && this.isMouseOver() && !this.isDisabled()) {
            if (!this.disableAutoToggle) {
                this.setActive(!this.isActive());
            }
            else {
                this.setActive(true);
            }
        }
    }
    mouseReleased(button, mx, my) {
        if (!this.isVisible()) {
            return;
        }
        if (button === 0 && this.isMouseOver() && !this.isDisabled() && this.isActive()) {
            if (this.disableAutoToggle) {
                this.setActive(false);
            }
            this.notifyListeners();
        }
    }
    setDisableAutoToggle(disableAutoToggle) {
        this.disableAutoToggle = disableAutoToggle;
    }
    getDisableAutoToggle() {
        return this.disableAutoToggle;
    }
}
//# sourceMappingURL=ToggleButton.js.map