import { Component, ReferencePoint } from './Component';
import { ToggleButton } from './ToggleButton';
import { Label } from './Label';
import { Color } from '../core/Color';
import { FontStore, FontID } from '../core/FontStore';
export class SegmentedControl extends Component {
    constructor(width, height, rows, cols, margin, requireSelection, maxSelectable, ...labels) {
        super(width, height);
        this.color = Color.white;
        this.singleSelection = -1;
        this.font = FontStore.getFont(FontID.FIELD);
        this.STATES = labels.length;
        this.maxSelected = maxSelectable;
        this.requireSelection = requireSelection;
        this.buttons = new Array(this.STATES);
        this.selection = new Array(this.STATES).fill(false);
        this.permanent = new Array(this.STATES).fill(false);
        this.singleSelection = -1;
        this.rowHeight = Math.floor((height - (rows + 1) * margin) / rows);
        this.colWidth = Math.floor((width - (cols + 1) * margin) / cols);
        for (let i = 0; i < this.STATES; i++) {
            const current = Label.withTextWidth(this.font, this.color, labels[i]);
            this.buttons[i] = new ToggleButton(this.colWidth, this.rowHeight, current);
            this.buttons[i].setDisableAutoToggle(true);
            this.buttons[i].addClickListener(() => this.handleButtonClick(i));
            if (margin <= 0) {
                if (i > 0 && rows === 1) {
                    this.buttons[i].setLeftBorderWidth(0);
                }
                if (i > Math.floor(this.STATES / rows) - 1) {
                    this.buttons[i].setTopBorderWidth(0);
                }
            }
        }
        const xOffset = Math.floor((width - cols * this.colWidth - margin * (cols - 1)) / 2);
        const yOffset = Math.floor((height - rows * this.rowHeight - margin * (rows - 1)) / 2);
        this.addAsGrid(this.buttons, this.getPosition(ReferencePoint.TOPLEFT), rows, cols, xOffset, yOffset, margin, margin);
        this.clear();
    }
    render(ctx) {
        if (!this.isVisible()) {
            return;
        }
        super.render(ctx);
    }
    handleButtonClick(ordinal) {
        if (this.maxSelected > 1 && !this.permanent[ordinal]) {
            if (this.selection[ordinal]) {
                this.selection[ordinal] = false;
            }
            else if (this.getNumSelected() < this.maxSelected) {
                this.selection[ordinal] = true;
            }
        }
        else {
            this.singleSelection = ordinal;
        }
        this.updateButtons();
        this.notifyListeners();
    }
    updateButtons() {
        for (let i = 0; i < this.buttons.length; i++) {
            if (this.permanent[i] || this.selection[i] || i === this.singleSelection) {
                this.buttons[i].setActive(true);
                if (i === this.singleSelection) {
                    this.buttons[i].setAcceptingInput(false);
                }
            }
            else {
                this.buttons[i].setActive(false);
                this.buttons[i].setAcceptingInput(true);
            }
        }
    }
    setSelection(selection) {
        if (selection.length === 0) {
            this.singleSelection = -1;
        }
        else if (selection.length === 1) {
            this.singleSelection = selection[0];
        }
        else {
            for (const i of selection) {
                this.selection[i] = !this.permanent[i];
            }
        }
        this.updateButtons();
    }
    setPermanent(permanent) {
        if (this.maxSelected > 1) {
            this.permanent.fill(false);
            for (const i of permanent) {
                this.permanent[i] = true;
            }
            this.setSelection(permanent);
        }
        this.updateButtons();
    }
    setDisabled(disabled) {
        for (const i of disabled) {
            this.buttons[i].setDisabled(true);
        }
        this.updateButtons();
    }
    clear() {
        this.selection.fill(false);
        this.permanent.fill(false);
        if (this.requireSelection) {
            this.singleSelection = 0;
            this.buttons[this.singleSelection].setActive(true);
        }
        else {
            this.singleSelection = -1;
        }
    }
    setFont(font) {
        this.font = font;
        for (const button of this.buttons) {
            button.setFont(font);
        }
    }
    setColor(color) {
        this.color = color;
        for (const button of this.buttons) {
            button.setLabelColor(color);
        }
    }
    getNumSelected() {
        let count = 0;
        for (const selected of this.selection) {
            if (selected)
                count++;
        }
        for (const perm of this.permanent) {
            if (perm)
                count++;
        }
        return count;
    }
    getSelection() {
        if (this.maxSelected > 1) {
            const returnStates = [];
            for (let i = 0; i < this.STATES; i++) {
                if (this.selection[i] || this.permanent[i]) {
                    returnStates.push(i);
                }
            }
            return returnStates;
        }
        else {
            return [this.singleSelection];
        }
    }
    setTooltips(tooltips) {
        if (tooltips.length !== this.STATES) {
            return;
        }
        for (let i = 0; i < this.STATES; i++) {
            this.buttons[i].setTooltipEnabled(true);
            this.buttons[i].setTooltipMessage(tooltips[i]);
        }
    }
}
//# sourceMappingURL=SegmentedControl.js.map