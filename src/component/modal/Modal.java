package component.modal;

import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.Input;

import component.Button;
import component.Component;
import component.Label;
import component.ModalListener;
import component.Panel;

import core.ConstantStore;

/**
 * {@code Modal} inherits from {@code Component} to extend features that provide
 * functionality to block other elements on the screen and present information
 * to the user.
 */
public abstract class Modal extends Component {
	protected static final int PADDING = 20;
	protected static int DEFAULT_LABEL_WIDTH = 500;
	protected static final int BUTTON_WIDTH = 200;
	protected static final int BUTTON_HEIGHT = 40;
	
	protected ModalListener listener;
	protected Panel panel;
	protected Label messageLabel;
	protected Button dismissButton;
	protected Button cancelButton;
	
	/**
	 * Constructs a {@code Modal} with a listener, message, and text for the dismiss button.
	 * @param context The GUI context
	 * @param listener The listener
	 * @param message The text for the message
	 */
	public Modal(GUIContext context, ModalListener listener, String message) {
		super(context, context.getWidth(), context.getHeight());
		
		add(new Panel(context, ConstantStore.COLORS.get("TRANSLUCENT_OVERLAY")), getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
		
		this.listener = listener;
	}
	
	public void setDismissButtonText(String text) {
		dismissButton.setText(text);
	}
	
	public void setCancelButtonText(String text) {
		cancelButton.setText(text);
	}
	
	@Override
	public void keyReleased(int key, char c) {
		if (!isVisible() || !isAcceptingInput()) {
			return;
		}
		
		if (key == Input.KEY_ENTER) {
			listener.dismissModal(Modal.this, false);
		}
	}
}
