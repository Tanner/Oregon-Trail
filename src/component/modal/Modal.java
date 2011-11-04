package component.modal;

import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Input;

import component.Button;
import component.Component;
import component.Label;
import component.Panel;

import core.ConstantStore;
import core.FontStore;

/**
 * {@code Modal} inherits from {@code Component} to extend features that provide
 * functionality to block other elements on the screen and present information
 * to the user.
 */
public abstract class Modal extends Component {
	protected static final int PADDING = 20;
	protected static int DEFAULT_LABEL_WIDTH = 500;
	protected static final int BUTTON_HEIGHT = 40;
	
	protected ModalListener listener;
	protected Panel panel;
	protected Label messageLabel;
	protected Button[] buttons;
	private ButtonListener buttonListener;
	
	private int cancelButtonIndex;
	
	/**
	 * Constructs a {@code Modal} with a listener, message, and text for the dismiss button.
	 * @param context The GUI context
	 * @param listener The listener
	 * @param message The text for the message
	 */
	public Modal(GUIContext context, ModalListener listener, String message, int buttonCount) {
		super(context, context.getWidth(), context.getHeight());
		
		buttonListener = new ButtonListener(this);
		
		buttons = new Button[buttonCount];
		cancelButtonIndex = -1;
		
		add(new Panel(context, ConstantStore.COLORS.get("TRANSLUCENT_OVERLAY")), getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
		
		this.listener = listener;
	}
	
	protected void createButtons() {
		Font fieldFont = FontStore.get(FontStore.FontID.FIELD);
		int buttonWidth = (panel.getWidth() - PADDING * (buttons.length + 1)) / buttons.length;
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new Button(container, buttonWidth, BUTTON_HEIGHT, new Label(container, buttonWidth, BUTTON_HEIGHT, fieldFont, Color.white, ConstantStore.get("GENERAL", "OK")));
			buttons[i].addListener(buttonListener);
		}
	}
	
	protected ButtonListener getButtonListener() {
		return buttonListener;
	}
	
	public void setButtonText(int buttonIndex, String text) {
		buttons[buttonIndex].setText(text);
	}
	
	public int getCancelButtonIndex() {
		return cancelButtonIndex;
	}
	
	protected void setCancelButtonIndex(int i) {
		cancelButtonIndex = i;
	}
	
	@Override
	public void keyReleased(int key, char c) {
		if (!isVisible() || !isAcceptingInput()) {
			return;
		}
		
		if (key == Input.KEY_ENTER) {
			listener.dismissModal(Modal.this, -1);
		}
	}
	
	/**
	 * Listener for the buttons of {@code MessageModal}.
	 */
	private class ButtonListener implements ComponentListener {
		private Modal modal;
		
		protected ButtonListener(Modal modal) {
			this.modal = modal;
		}
		
		@Override
		public void componentActivated(AbstractComponent source) {
			int buttonIndex = -1;
			for (int i = 0; i < buttons.length; i++) {
				if (buttons[i] == source) {
					buttonIndex = i;
				}
			}
			
			listener.dismissModal(modal, buttonIndex);
		} 
	}
}
