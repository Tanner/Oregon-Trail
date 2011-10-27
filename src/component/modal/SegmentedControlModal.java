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
import component.SegmentedControl;
import core.ConstantStore;
import core.FontStore;

public class SegmentedControlModal extends Modal {
	private SegmentedControl segmentedControl;

	/**
	 * Constructs a {@code Modal} with a listener, message, segmented control, submit message text, and cancel button text.
	 * @param context The GUI context
	 * @param listener The listener
	 * @param message The text for the message
	 * @param segmentedControl A segmented control
	 * @param dismissButtonText The text for the submit button
	 * @param cancelButtonText The text button for the cancel button
	 */
	public SegmentedControlModal(GUIContext context, ModalListener listener, String message, SegmentedControl segmentedControl) {
		super(context, listener, message);
		
		this.segmentedControl = segmentedControl;
		
		Font fieldFont = FontStore.get(FontStore.FontID.FIELD);
		this.messageLabel = new Label(container, DEFAULT_LABEL_WIDTH, fieldFont, Color.white, message);
		messageLabel.setAlignment(Label.Alignment.CENTER);
		
		ButtonListener buttonListener = new ButtonListener();
		dismissButton = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, new Label(container, fieldFont, Color.white, ConstantStore.get("GENERAL", "OK")));
		dismissButton.addListener(buttonListener);
		
		cancelButton = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, new Label(container, fieldFont, Color.white, ConstantStore.get("GENERAL", "CANCEL")));
		cancelButton.addListener(buttonListener);
		
		int panelWidth = PADDING * 2 + Math.max(segmentedControl.getWidth(), messageLabel.getWidth());
		int panelHeight = PADDING * 4 + messageLabel.getHeight() + segmentedControl.getHeight() + cancelButton.getHeight();
		panel = new Panel(container, panelWidth, panelHeight, ConstantStore.COLORS.get("MODAL"));
		panel.setBorderColor(ConstantStore.COLORS.get("MODAL_BORDER"));
		panel.setBorderWidth(2);
		panel.setLocation((getWidth() - panel.getWidth()) / 2, (getHeight() - panel.getHeight() / 2));
		
		panel.add(messageLabel, panel.getPosition(Positionable.ReferencePoint.TOPCENTER), Positionable.ReferencePoint.TOPCENTER, 0, PADDING);
		panel.add(segmentedControl, messageLabel.getPosition(Positionable.ReferencePoint.BOTTOMCENTER), Positionable.ReferencePoint.TOPCENTER, 0, PADDING);
		panel.add(cancelButton, panel.getPosition(Positionable.ReferencePoint.BOTTOMLEFT), Positionable.ReferencePoint.BOTTOMLEFT, PADDING, -PADDING);
		panel.add(dismissButton, panel.getPosition(Positionable.ReferencePoint.BOTTOMRIGHT), Positionable.ReferencePoint.BOTTOMRIGHT, -PADDING, -PADDING);
		
		add(panel, getPosition(Positionable.ReferencePoint.CENTERCENTER), Positionable.ReferencePoint.CENTERCENTER);
	}
	
	
	/**
	 * Returns the segmented control.
	 * @return The segmented control
	 */
	public SegmentedControl getSegmentedControl() {
		return segmentedControl;
	}
	
	/**
	 * Listener for the buttons of {@code SegmentedControlModal}.
	 */
	private class ButtonListener implements ComponentListener {
		@Override
		public void componentActivated(AbstractComponent source) {
			boolean cancelled = false;
			listener.dismissModal(SegmentedControlModal.this, cancelled);
		} 
	}
}
