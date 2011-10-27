package component;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.Input;

import core.ConstantStore;
import core.FontStore;
import core.SoundStore;

/**
 * {@code Modal} inherits from {@code Component} to extend features that provide
 * functionality to act like a modal that gets input from the user.
 */
public class Modal extends Component {
	private static final int PADDING = 20;
	private static int DEFAULT_LABEL_WIDTH = 500;
	
	private static final int BUTTON_WIDTH = 200;
	private static final int BUTTON_HEIGHT = 40;
	
	private ModalListener listener;
	private Panel panel;
	private Label messageLabel;
	private Button dismissButton;
	private Button cancelButton;
	private SegmentedControl segmentedControl;
	
	/**
	 * Constructs a {@code Modal} with a listener, message, and text for the dismiss button.
	 * @param context The GUI context
	 * @param listener The listener
	 * @param message The text for the message
	 * @param dismissButtonText The text for the dismiss button
	 */
	public Modal(GUIContext context, ModalListener listener, String message) {
		super(context, context.getWidth(), context.getHeight());
		
		add(new Panel(context, ConstantStore.COLORS.get("TRANSLUCENT_OVERLAY")), getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
		
		this.listener = listener;
				
		Font fieldFont = FontStore.get(FontStore.FontID.FIELD);
		this.messageLabel = new Label(container, DEFAULT_LABEL_WIDTH, fieldFont, Color.white, message);
		messageLabel.setAlignment(Label.Alignment.CENTER);
		
		ButtonListener buttonListener = new ButtonListener();
		dismissButton = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, new Label(container, fieldFont, Color.white, ConstantStore.get("GENERAL", "OK")));
		dismissButton.addListener(buttonListener);
		
		cancelButton = new Button(container, BUTTON_WIDTH, BUTTON_HEIGHT, new Label(container, fieldFont, Color.white, ConstantStore.get("GENERAL", "CANCEL")));
		cancelButton.addListener(new ButtonListener());
		
		int panelWidth = PADDING * 2 + messageLabel.getWidth();
		int panelHeight = PADDING * 3 + messageLabel.getHeight() + cancelButton.getHeight();
		panel = new Panel(container, panelWidth, panelHeight, ConstantStore.COLORS.get("MODAL"));
		panel.setBorderColor(ConstantStore.COLORS.get("MODAL_BORDER"));
		panel.setBorderWidth(2);
		panel.setLocation((getWidth() - panel.getWidth()) / 2, (getHeight() - panel.getHeight() / 2));
		
		panel.add(messageLabel, panel.getPosition(Positionable.ReferencePoint.TOPCENTER), Positionable.ReferencePoint.TOPCENTER, 0, PADDING);
		panel.add(cancelButton, panel.getPosition(Positionable.ReferencePoint.BOTTOMCENTER), Positionable.ReferencePoint.BOTTOMCENTER, 0, -PADDING);
		
		add(panel, getPosition(Positionable.ReferencePoint.CENTERCENTER), Positionable.ReferencePoint.CENTERCENTER);
	}
	
	/**
	 * Constructs a {@code Modal} with a listener, message, segmented control, submit message text, and cancel button text.
	 * @param context The GUI context
	 * @param listener The listener
	 * @param message The text for the message
	 * @param segmentedControl A segmented control
	 * @param dismissButtonText The text for the submit button
	 * @param cancelButtonText The text button for the cancel button
	 */
	public Modal(GUIContext context, ModalListener listener, String message, SegmentedControl segmentedControl) {
		super(context, context.getWidth(), context.getHeight());
		
		add(new Panel(context, ConstantStore.COLORS.get("TRANSLUCENT_OVERLAY")), getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
		
		this.listener = listener;
		this.segmentedControl = segmentedControl;
				
		Font fieldFont = FontStore.get(FontStore.FontID.FIELD);
		messageLabel = new Label(container, segmentedControl.getWidth(), fieldFont, Color.white, message);
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
	
	public void setDismissButtonText(String text) {
		dismissButton.setText(text);
	}
	
	public void setCancelButtonText(String text) {
		cancelButton.setText(text);
	}
	
	@Override
	public void keyReleased(int key, char c) {
		if (key == Input.KEY_ENTER) {
			listener.dismissModal(Modal.this, false);
		}
	}
	
	/**
	 * Returns the segmented control.
	 * @return The segmented control
	 */
	public SegmentedControl getSegmentedControl() {
		return segmentedControl;
	}

	/**
	 * Listener for the buttons of {@code Modal}.
	 */
	private class ButtonListener implements ComponentListener {
		@Override
		public void componentActivated(AbstractComponent source) {
			SoundStore.get().playSound("Click");
			boolean cancelled = false;
			if (source == cancelButton) {
				cancelled = true;
			}
			listener.dismissModal(Modal.this, cancelled);
		} 
		
	}
}
