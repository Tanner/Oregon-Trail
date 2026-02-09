import { Modal, ModalListener } from './Modal';
import { Panel } from '../Panel';
import { Label, Alignment } from '../Label';
import { ReferencePoint } from '../Component';
import { Color } from '../../core/Color';
import { COLORS } from '../../core/ConstantStore';
import { FontStore, FontID } from '../../core/FontStore';

export class MessageModal extends Modal {
  constructor(width: number, height: number, listener: ModalListener, message: string) {
    super(width, height, listener, message, 1);

    const fieldFont = FontStore.getFont(FontID.FIELD);
    this.messageLabel = Label.withAutoHeight(Modal.DEFAULT_LABEL_WIDTH, fieldFont, Color.white, message);
    this.messageLabel.setAlignment(Alignment.CENTER);

    const panelWidth = Modal.PADDING * 2 + this.messageLabel.getWidth();
    const panelHeight = Modal.PADDING * 3 + this.messageLabel.getHeight() + Modal.BUTTON_HEIGHT;

    this.panel = new Panel(panelWidth, panelHeight, COLORS.get('MODAL') || Color.darkGray);
    this.panel.setBorderColor(COLORS.get('MODAL_BORDER') || Color.white);
    this.panel.setBorderWidth(2);
    this.panel.setLocation(
      Math.floor((this.getWidth() - this.panel.getWidth()) / 2),
      Math.floor((this.getHeight() - this.panel.getHeight()) / 2)
    );

    this.createButtons();

    this.panel.add(
      this.messageLabel,
      this.panel.getPosition(ReferencePoint.TOPCENTER),
      ReferencePoint.TOPCENTER,
      0,
      Modal.PADDING
    );

    this.panel.addAsRow(
      this.buttons,
      this.panel.getPosition(ReferencePoint.BOTTOMLEFT),
      Modal.PADDING,
      -Modal.PADDING - Modal.BUTTON_HEIGHT,
      Modal.PADDING
    );

    this.add(
      this.panel,
      this.getPosition(ReferencePoint.CENTERCENTER),
      ReferencePoint.CENTERCENTER
    );
  }
}
