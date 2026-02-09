import { Component, ReferencePoint } from '../Component';
import { Panel } from '../Panel';
import { Label } from '../Label';
import { Button } from '../Button';
import { Color } from '../../core/Color';
import { COLORS, get as getLiteral } from '../../core/ConstantStore';
import { FontStore, FontID } from '../../core/FontStore';

export interface ModalListener {
  dismissModal(modal: Modal, buttonIndex: number): void;
}

export abstract class Modal extends Component {
  protected static readonly PADDING = 20;
  protected static DEFAULT_LABEL_WIDTH = 500;
  protected static readonly BUTTON_HEIGHT = 40;

  protected listener: ModalListener;
  protected panel!: Panel;
  protected messageLabel!: Label;
  protected buttons: Button[];
  private buttonListener: ButtonListener;

  private cancelButtonIndex: number = -1;

  constructor(
    width: number,
    height: number,
    listener: ModalListener,
    _message: string,
    buttonCount: number
  ) {
    super(width, height);

    this.listener = listener;
    this.buttons = new Array(buttonCount);
    this.buttonListener = new ButtonListener(this);

    const overlay = new Panel(width, height, COLORS.get('TRANSLUCENT_OVERLAY') || new Color(0, 0, 0, 0.25));
    this.add(overlay, this.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
  }

  protected createButtons(): void {
    const fieldFont = FontStore.getFont(FontID.FIELD);
    const buttonWidth = Math.floor(
      (this.panel.getWidth() - Modal.PADDING * (this.buttons.length + 1)) / this.buttons.length
    );

    for (let i = 0; i < this.buttons.length; i++) {
      const buttonLabel = new Label(
        buttonWidth,
        Modal.BUTTON_HEIGHT,
        fieldFont,
        Color.white,
        getLiteral('GENERAL', 'OK')
      );
      this.buttons[i] = new Button(buttonWidth, Modal.BUTTON_HEIGHT, buttonLabel);
      this.buttons[i].addClickListener(() => this.buttonListener.onClick(this.buttons[i]));
    }
  }

  protected getButtonListener(): ButtonListener {
    return this.buttonListener;
  }

  setButtonText(buttonIndex: number, text: string): void {
    this.buttons[buttonIndex].setText(text);
  }

  getCancelButtonIndex(): number {
    return this.cancelButtonIndex;
  }

  protected setCancelButtonIndex(i: number): void {
    this.cancelButtonIndex = i;
  }

  getButtons(): Button[] {
    return this.buttons;
  }

  getListener(): ModalListener {
    return this.listener;
  }

  keyReleased(key: string): void {
    if (!this.isVisible() || !this.isAcceptingInput()) {
      return;
    }

    if (key === 'Enter') {
      this.listener.dismissModal(this, -1);
    }
  }
}

class ButtonListener {
  private modal: Modal;

  constructor(modal: Modal) {
    this.modal = modal;
  }

  onClick(source: Button): void {
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
