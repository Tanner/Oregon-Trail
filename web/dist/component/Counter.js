import { Component, ReferencePoint } from './Component';
import { Button } from './Button';
import { Label, Alignment } from './Label';
import { Color } from '../core/Color';
import { FontStore, FontID } from '../core/FontStore';
export class Counter extends Component {
    constructor(width, height, label, sprite) {
        super(width, height);
        this.count = 0;
        this.min = 0;
        this.max = Number.MAX_SAFE_INTEGER;
        this.countUpOnLeftClick = true;
        this.disableAutoCount = false;
        this.hideCount = false;
        if (sprite) {
            this.button = new CountingButton(this, width, height, label, sprite);
        }
        else {
            this.button = new CountingButton(this, width, height, label);
        }
        this.add(this.button, this.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
        this.setCount(this.count);
    }
    render(ctx) {
        super.render(ctx);
    }
    isCountUpOnLeftClick() {
        return this.countUpOnLeftClick;
    }
    setCountUpOnLeftClick(countUpOnLeftClick) {
        this.countUpOnLeftClick = countUpOnLeftClick;
    }
    setMin(min) {
        this.min = min;
    }
    getMin() {
        return this.min;
    }
    setMax(max) {
        this.max = max;
    }
    getMax() {
        return this.max;
    }
    setCount(count) {
        this.count = count;
        if (this.countLabel) {
            this.remove(this.countLabel);
        }
        const fieldFont = FontStore.getFont(FontID.FIELD);
        const text = '' + count;
        this.countLabel = new Label(Math.floor(fieldFont.getWidth(text) + Counter.COUNT_LABEL_PADDING * 2), fieldFont.getLineHeight(), fieldFont, Color.white, text);
        this.countLabel.setBackgroundColor(Color.red);
        this.countLabel.setAlignment(Alignment.CENTER);
        this.add(this.countLabel, this.getPosition(ReferencePoint.TOPRIGHT), ReferencePoint.CENTERRIGHT, Counter.COUNT_LABEL_X_OFFSET, 0);
    }
    getCount() {
        return this.count;
    }
    getHideCount() {
        return this.hideCount;
    }
    setHideCount(hideCount) {
        this.hideCount = hideCount;
        this.countLabel.setVisible(!hideCount);
    }
    getDisableAutoCount() {
        return this.disableAutoCount;
    }
    setDisableAutoCount(disableAutoCount) {
        this.disableAutoCount = disableAutoCount;
    }
    isDisabled() {
        return this.button.isDisabled();
    }
    setDisabled(disabled) {
        this.button.setDisabled(disabled);
    }
    setText(text) {
        this.button.setText(text);
    }
    setSprite(sprite) {
        this.button.setSprite(sprite);
    }
    setShowLabel(showLabel) {
        this.button.setShowLabel(showLabel);
    }
    setShowSprite(showSprite) {
        this.button.setShowSprite(showSprite);
    }
}
Counter.COUNT_LABEL_PADDING = 5;
Counter.COUNT_LABEL_X_OFFSET = 10;
class CountingButton extends Button {
    constructor(counter, width, height, label, sprite) {
        if (sprite) {
            super(width, height, label, sprite);
        }
        else {
            super(width, height, label);
        }
        this.counter = counter;
    }
    mousePressed(button, mx, my) {
        if (!this.isVisible() || !this.isAcceptingInput()) {
            return;
        }
        if (this.isMouseOver() && !this.isDisabled()) {
            this.setActive(true);
        }
    }
    mouseReleased(button, mx, my) {
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
                if ((button === 0 && this.counter.isCountUpOnLeftClick()) ||
                    (button !== 0 && !this.counter.isCountUpOnLeftClick())) {
                    this.counter.setCount(Math.min(this.counter.getMax(), this.counter.getCount() + countChange));
                }
                else if ((button !== 0 && this.counter.isCountUpOnLeftClick()) ||
                    (button === 0 && !this.counter.isCountUpOnLeftClick())) {
                    this.counter.setCount(Math.max(this.counter.getMin(), this.counter.getCount() - countChange));
                }
            }
        }
    }
    isAltKeyPressed() {
        return false;
    }
}
//# sourceMappingURL=Counter.js.map