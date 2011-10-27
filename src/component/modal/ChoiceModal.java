package component.modal;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;

import component.Button;
import component.Label;
import component.ModalListener;
import component.Panel;
import component.Positionable;
import core.ConstantStore;
import core.FontStore;

public class ChoiceModal extends Modal {

	public ChoiceModal(GUIContext context, ModalListener listener, String message) {
		super(context, listener, message);
		
		Font fieldFont = FontStore.get(FontStore.FontID.FIELD);
		this.messageLabel = new Label(container, DEFAULT_LABEL_WIDTH, fieldFont, Color.white, message);
		messageLabel.setAlignment(Label.Alignment.CENTER);
		
		ButtonListener buttonListener = new ButtonListener();
		cancelButton = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, new Label(container, fieldFont, Color.white, ConstantStore.get("GENERAL", "CANCEL")));
		cancelButton.addListener(buttonListener);
		dismissButton = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, new Label(container, fieldFont, Color.white, ConstantStore.get("GENERAL", "OK")));
		dismissButton.addListener(buttonListener);
		
		int panelWidth = PADDING * 2 + messageLabel.getWidth();
		int panelHeight = PADDING * 3 + messageLabel.getHeight() + dismissButton.getHeight();
		panel = new Panel(container, panelWidth, panelHeight, ConstantStore.COLORS.get("MODAL"));
		panel.setBorderColor(ConstantStore.COLORS.get("MODAL_BORDER"));
		panel.setBorderWidth(2);
		panel.setLocation((getWidth() - panel.getWidth()) / 2, (getHeight() - panel.getHeight() / 2));
		
		panel.add(messageLabel, panel.getPosition(Positionable.ReferencePoint.TOPCENTER), Positionable.ReferencePoint.TOPCENTER, 0, PADDING);
		
		panel.add(cancelButton, panel.getPosition(Positionable.ReferencePoint.BOTTOMLEFT), Positionable.ReferencePoint.BOTTOMLEFT, PADDING, -PADDING);
		panel.add(dismissButton, panel.getPosition(Positionable.ReferencePoint.BOTTOMRIGHT), Positionable.ReferencePoint.BOTTOMRIGHT, -PADDING, -PADDING);
		
		add(panel, getPosition(Positionable.ReferencePoint.CENTERCENTER), Positionable.ReferencePoint.CENTERCENTER);
	}
	
	/**
	 * Listener for the buttons of {@code MessageModal}.
	 */
	private class ButtonListener implements ComponentListener {
		@Override
		public void componentActivated(AbstractComponent source) {
			boolean cancelled = false;
			if (source == cancelButton) {
				cancelled = true;
			}
			listener.dismissModal(ChoiceModal.this, cancelled);
		} 
	}
}
