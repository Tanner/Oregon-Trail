package component.modal;

import java.util.Arrays;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.gui.GUIContext;

import component.Label;
import component.Panel;
import component.Positionable;
import core.ConstantStore;
import core.FontStore;

public class ChoiceModal extends Modal {

	public ChoiceModal(GUIContext context, ModalListener listener, String message, int buttonCount) {
		super(context, listener, message, buttonCount);
		
		Font fieldFont = FontStore.get(FontStore.FontID.FIELD);
		this.messageLabel = new Label(container, DEFAULT_LABEL_WIDTH, fieldFont, Color.white, message);
		messageLabel.setAlignment(Label.Alignment.CENTER);
		
		int panelWidth = PADDING * 2 + messageLabel.getWidth();
		int panelHeight = PADDING * 3 + messageLabel.getHeight() + BUTTON_HEIGHT;
		panel = new Panel(container, panelWidth, panelHeight, ConstantStore.COLORS.get("MODAL"));
		panel.setBorderColor(ConstantStore.COLORS.get("MODAL_BORDER"));
		panel.setBorderWidth(2);
		panel.setLocation((getWidth() - panel.getWidth()) / 2, (getHeight() - panel.getHeight() / 2));
		
		setCancelButtonIndex(0);
		createButtons();
		
		panel.add(messageLabel, panel.getPosition(Positionable.ReferencePoint.TOPCENTER), Positionable.ReferencePoint.TOPCENTER, 0, PADDING);
		
		panel.addAsRow(Arrays.asList(buttons).iterator(),
				panel.getPosition(Positionable.ReferencePoint.BOTTOMLEFT),
				PADDING,
				-PADDING - BUTTON_HEIGHT,
				PADDING);
		
		add(panel, getPosition(Positionable.ReferencePoint.CENTERCENTER), Positionable.ReferencePoint.CENTERCENTER);
	}
}
