package component;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.Input;

import core.ConstantStore;
import core.FontManager;
import core.GameDirector;

/**
 * {@code Modal} inherits from {@code Component} to extend features that provide
 * functionality to act like a modal that gets input from the user.
 */
public class Modal extends Component {
	private static final int PADDING = 20;
	private static final Color OVERLAY_COLOR;
	
	static {
		OVERLAY_COLOR = new Color(0f, 0f, 0f, 0.5f);
	}
	
	private ModalListener listener;
	private Panel panel;
	private Label messageLabel;
	private Button resignButton;
	private Button dismissButton;
	private SegmentedControl segmentedControl;
	
	/**
	 * Constructs a {@code Modal} with a listener, message, and text for the dismiss button.
	 * @param context The GUI context
	 * @param listener The listener
	 * @param message The text for the message
	 * @param dismissButtonText The text for the dismiss button
	 */
	public Modal(GUIContext context, ModalListener listener, String message, String dismissButtonText) {
		super(context, context.getWidth(), context.getHeight());
		
		this.listener = listener;
				
		int messageWidth = 700;
		Font fieldFont = GameDirector.sharedSceneListener().getFontManager().getFont(FontManager.FontID.FIELD);		
		this.messageLabel = new Label(container, messageWidth, fieldFont, Color.white, message);
		messageLabel.setAlignment(Label.Alignment.Center);
		
		int buttonWidth = 200;
		int buttonHeight = 40;
		dismissButton = new Button(container, buttonWidth, buttonHeight, new Label(container, fieldFont, Color.white, dismissButtonText));
		dismissButton.addListener(new ButtonListener());
		
		int panelWidth = PADDING * 2 + messageLabel.getWidth();
		int panelHeight = PADDING * 3 + messageLabel.getHeight() + dismissButton.getHeight();
		panel = new Panel(container, panelWidth, panelHeight, ConstantStore.COLORS.get("MODAL"));
		panel.setBorderColor(ConstantStore.COLORS.get("MODAL_BORDER"));
		panel.setBorderWidth(2);
		panel.setLocation((getWidth() - panel.getWidth()) / 2, (getHeight() - panel.getHeight() / 2));
		
		panel.add(messageLabel, panel.getPosition(Positionable.ReferencePoint.TopCenter), Positionable.ReferencePoint.TopCenter, 0, PADDING);		
		panel.add(dismissButton, panel.getPosition(Positionable.ReferencePoint.BottomCenter), Positionable.ReferencePoint.BottomCenter, 0, -PADDING);
		
		add(panel, getPosition(Positionable.ReferencePoint.CenterCenter), Positionable.ReferencePoint.CenterCenter);
	}
	
	/**
	 * Constructs a {@code Modal} with a listener, message, segmented control, submit message text, and cancel button text.
	 * @param context The GUI context
	 * @param listener The listener
	 * @param message The text for the message
	 * @param segmentedControl A segmented control
	 * @param submitButtonString The text for the submit button
	 * @param cancelButtonString The text button for the cancel button
	 */
	public Modal(GUIContext context, ModalListener listener, String message, SegmentedControl segmentedControl, String submitButtonString, String cancelButtonString) {
		super(context, context.getWidth(), context.getHeight());
		
		this.listener = listener;
		this.segmentedControl = segmentedControl;
				
		int messageWidth = 700;
		Font fieldFont = GameDirector.sharedSceneListener().getFontManager().getFont(FontManager.FontID.FIELD);		
		messageLabel = new Label(container, messageWidth, fieldFont, Color.white, message);
		messageLabel.setAlignment(Label.Alignment.Center);
		
		ButtonListener buttonListener = new ButtonListener();
		int buttonWidth = 200;
		int buttonHeight = 40;
		resignButton = new Button(container, buttonWidth, buttonHeight, new Label(container, fieldFont, Color.white, submitButtonString));
		resignButton.addListener(buttonListener);
		
		dismissButton = new Button(container, buttonWidth, buttonHeight, new Label(container, fieldFont, Color.white, cancelButtonString));
		dismissButton.addListener(buttonListener);		
		
		int panelWidth = PADDING * 2 + segmentedControl.getWidth();
		int panelHeight = PADDING * 4 + messageLabel.getHeight() + segmentedControl.getHeight() + resignButton.getHeight();
		panel = new Panel(container, panelWidth, panelHeight, ConstantStore.COLORS.get("MODAL"));
		panel.setBorderColor(ConstantStore.COLORS.get("MODAL_BORDER"));
		panel.setBorderWidth(2);
		panel.setLocation((getWidth() - panel.getWidth()) / 2, (getHeight() - panel.getHeight() / 2));
		
		panel.add(messageLabel, panel.getPosition(Positionable.ReferencePoint.TopCenter), Positionable.ReferencePoint.TopCenter, 0, PADDING);
		panel.add(segmentedControl, messageLabel.getPosition(Positionable.ReferencePoint.BottomCenter), Positionable.ReferencePoint.TopCenter, 0, PADDING);
		panel.add(dismissButton, panel.getPosition(Positionable.ReferencePoint.BottomLeft), Positionable.ReferencePoint.BottomLeft, PADDING, -PADDING);
		panel.add(resignButton, panel.getPosition(Positionable.ReferencePoint.BottomRight), Positionable.ReferencePoint.BottomRight, -PADDING, -PADDING);
		
		add(panel, getPosition(Positionable.ReferencePoint.CenterCenter), Positionable.ReferencePoint.CenterCenter);
	}
	
	@Override
	public void render(GUIContext context, Graphics g) throws SlickException {
		g.setColor(OVERLAY_COLOR);
		g.fillRect(getX(), getY(), getWidth(), getHeight());
		
		super.render(context, g);
	}
	
	@Override
	public void keyReleased(int key, char c) {
		if (key == Input.KEY_ENTER) {
			listener.dismissModal(Modal.this, true);
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
			boolean cancelled = false;
			if (source == dismissButton) {
				cancelled = true;
			}
			listener.dismissModal(Modal.this, cancelled);
		} 
		
	}
}
