import { Component, ReferencePoint } from './Component';
import { Color } from '../core/Color';
export class ConditionBar extends Component {
    constructor(width, height, condition = null, font = null) {
        super(width, height);
        this.condition = null;
        this.warningLevel = 0.5;
        this.dangerLevel = 0.25;
        this.disableText = true;
        this.label = null;
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
    render(ctx) {
        if (!this.isVisible() || this.condition === null) {
            return;
        }
        super.render(ctx);
    }
    getBarColor() {
        if (this.condition === null) {
            return this.normalColor;
        }
        let barColor = this.normalColor;
        if (this.condition.getPercentage() < this.dangerLevel) {
            barColor = this.dangerColor;
        }
        else if (this.condition.getPercentage() < this.warningLevel) {
            barColor = this.warningColor;
        }
        return barColor;
    }
    getCondition() {
        return this.condition;
    }
    setCondition(condition) {
        this.condition = condition;
        this.updateBar();
    }
    updateBar() {
        this.update(0);
    }
    update(_delta) {
        if (this.condition !== null) {
            const percentage = this.condition.getPercentage();
            if (this.label !== null) {
                this.label.setText(Math.floor(percentage * 100) + '%');
            }
            this.conditionPanel.setPercentage(percentage);
        }
    }
    getDisableText() {
        return this.disableText;
    }
    setDisableText(disableText) {
        this.disableText = disableText;
        if (this.label !== null) {
            this.label.setVisible(!disableText);
        }
    }
    setFont(font) {
        if (this.label !== null) {
            this.label.setFont(font);
        }
    }
    setWarningLevel(level) {
        this.warningLevel = level;
    }
    setDangerLevel(level) {
        this.dangerLevel = level;
    }
    setNormalColor(color) {
        this.normalColor = color;
    }
    setWarningColor(color) {
        this.warningColor = color;
    }
    setDangerColor(color) {
        this.dangerColor = color;
    }
}
class ConditionPanel extends Component {
    constructor(width, height, color, parentBar) {
        super(width, height);
        this.percentage = 0;
        this.setBackgroundColor(color);
        this.parentBar = parentBar;
        this.setBackgroundColor(color);
    }
    render(ctx) {
        if (!this.isVisible()) {
            return;
        }
        super.render(ctx);
        ctx.fillStyle = this.parentBar.getBarColor().toCSS();
        ctx.fillRect(this.getX(), this.getY(), Math.floor(this.getWidth() * this.percentage), this.getHeight());
    }
    setPercentage(percentage) {
        this.percentage = percentage;
    }
}
//# sourceMappingURL=ConditionBar.js.map