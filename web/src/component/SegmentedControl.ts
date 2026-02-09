import { Component, ReferencePoint } from './Component';
import { ToggleButton } from './ToggleButton';
import { Label } from './Label';
import { Color } from '../core/Color';
import { FontStore, FontID } from '../core/FontStore';
import { BitmapFont } from '../core/BitmapFont';

export class SegmentedControl extends Component {
  private font: BitmapFont;
  private color: Color = Color.white;

  private readonly STATES: number;
  private readonly requireSelection: boolean;
  private readonly maxSelected: number;

  private rowHeight: number;
  private colWidth: number;

  private selection: boolean[];
  private singleSelection: number = -1;
  private permanent: boolean[];
  private buttons: ToggleButton[];

  constructor(
    width: number,
    height: number,
    rows: number,
    cols: number,
    margin: number,
    requireSelection: boolean,
    maxSelectable: number,
    ...labels: string[]
  ) {
    super(width, height);

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
      const current = new Label(this.colWidth, this.rowHeight, this.font, this.color, labels[i]);

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

    this.addAsGrid(
      this.buttons,
      this.getPosition(ReferencePoint.TOPLEFT),
      rows,
      cols,
      xOffset,
      yOffset,
      margin,
      margin
    );

    this.clear();
  }

  override render(ctx: CanvasRenderingContext2D): void {
    if (!this.isVisible()) {
      return;
    }

    super.render(ctx);
  }

  private handleButtonClick(ordinal: number): void {
    if (this.maxSelected > 1 && !this.permanent[ordinal]) {
      if (this.selection[ordinal]) {
        this.selection[ordinal] = false;
      } else if (this.getNumSelected() < this.maxSelected) {
        this.selection[ordinal] = true;
      }
    } else {
      this.singleSelection = ordinal;
    }

    this.updateButtons();
    this.notifyListeners();
  }

  updateButtons(): void {
    for (let i = 0; i < this.buttons.length; i++) {
      if (this.permanent[i] || this.selection[i] || i === this.singleSelection) {
        this.buttons[i].setActive(true);

        if (i === this.singleSelection) {
          this.buttons[i].setAcceptingInput(false);
        }
      } else {
        this.buttons[i].setActive(false);
        this.buttons[i].setAcceptingInput(true);
      }
    }
  }

  setSelection(selection: number[]): void {
    if (selection.length === 0) {
      this.singleSelection = -1;
    } else if (selection.length === 1) {
      this.singleSelection = selection[0];
    } else {
      for (const i of selection) {
        this.selection[i] = !this.permanent[i];
      }
    }
    this.updateButtons();
  }

  setPermanent(permanent: number[]): void {
    if (this.maxSelected > 1) {
      this.permanent.fill(false);
      for (const i of permanent) {
        this.permanent[i] = true;
      }
      this.setSelection(permanent);
    }
    this.updateButtons();
  }

  setDisabled(disabled: number[]): void {
    for (const i of disabled) {
      this.buttons[i].setDisabled(true);
    }
    this.updateButtons();
  }

  clear(): void {
    this.selection.fill(false);
    this.permanent.fill(false);
    if (this.requireSelection) {
      this.singleSelection = 0;
      this.buttons[this.singleSelection].setActive(true);
    } else {
      this.singleSelection = -1;
    }
  }

  setFont(font: BitmapFont): void {
    this.font = font;
    for (const button of this.buttons) {
      button.setFont(font);
    }
  }

  setColor(color: Color): void {
    this.color = color;
    for (const button of this.buttons) {
      button.setLabelColor(color);
    }
  }

  private getNumSelected(): number {
    let count = 0;
    for (const selected of this.selection) {
      if (selected) count++;
    }
    for (const perm of this.permanent) {
      if (perm) count++;
    }
    return count;
  }

  getSelection(): number[] {
    if (this.maxSelected > 1) {
      const returnStates: number[] = [];
      for (let i = 0; i < this.STATES; i++) {
        if (this.selection[i] || this.permanent[i]) {
          returnStates.push(i);
        }
      }
      return returnStates;
    } else {
      return [this.singleSelection];
    }
  }

  setTooltips(tooltips: string[]): void {
    if (tooltips.length !== this.STATES) {
      return;
    }

    for (let i = 0; i < this.STATES; i++) {
      this.buttons[i].setTooltipEnabled(true);
      this.buttons[i].setTooltipMessage(tooltips[i]);
    }
  }
}
