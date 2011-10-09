package component;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;

import core.FontManager;
import core.GameDirector;

public class Modal extends Component {
	private static final int PADDING = 20;
	
	private ModalListener listener;
	private Panel panel;
	private Label messageLabel;
	private Button resignButton;
	private Button dismissButton;
	private SegmentedControl segmentedControl;
	
	public Modal(GUIContext container, ModalListener listener, String message, String dismissButtonText) {
		super(container, container.getWidth(), container.getHeight());
		
		this.listener = listener;
		
		int messageWidth = 700;
		UnicodeFont fieldFont = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.FIELD);		
		this.messageLabel = new Label(container, messageWidth, fieldFont, Color.white, message);
		messageLabel.setAlignment(Label.Alignment.Center);
		
		int buttonWidth = 200;
		int buttonHeight = 40;
		dismissButton = new Button(container, buttonWidth, buttonHeight, new Label(container, fieldFont, Color.white, dismissButtonText));
		dismissButton.setRoundedCorners(true);
		dismissButton.addListener(new ButtonListener());
		
		int panelWidth = PADDING * 2 + messageLabel.getWidth();
		int panelHeight = PADDING * 3 + messageLabel.getHeight() + dismissButton.getHeight();
		panel = new Panel(container, panelWidth, panelHeight, Color.black);
		panel.setLocation((getWidth() - panel.getWidth()) / 2, (getHeight() - panel.getHeight() / 2));
		
		panel.add(messageLabel, panel.getPosition(Positionable.ReferencePoint.TopCenter), Positionable.ReferencePoint.TopCenter, 0, PADDING);		
		panel.add(dismissButton, panel.getPosition(Positionable.ReferencePoint.BottomCenter), Positionable.ReferencePoint.BottomCenter, 0, -PADDING);
		
		add(panel, getPosition(Positionable.ReferencePoint.CenterCenter), Positionable.ReferencePoint.CenterCenter);
	}
	
	public Modal(GUIContext container, ModalListener listener, String message, SegmentedControl segmentedControl, String submitButtonString, String cancelButtonString) {
		super(container, container.getWidth(), container.getHeight());
		
		this.listener = listener;
		this.segmentedControl = segmentedControl;
				
		int messageWidth = 700;
		UnicodeFont fieldFont = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.FIELD);		
		messageLabel = new Label(container, messageWidth, fieldFont, Color.white, message);
		messageLabel.setAlignment(Label.Alignment.Center);
		
		ButtonListener buttonListener = new ButtonListener();
		int buttonWidth = 200;
		int buttonHeight = 40;
		resignButton = new Button(container, buttonWidth, buttonHeight, new Label(container, fieldFont, Color.white, submitButtonString));
		resignButton.setRoundedCorners(true);
		resignButton.addListener(buttonListener);
		
		dismissButton = new Button(container, buttonWidth, buttonHeight, new Label(container, fieldFont, Color.white, cancelButtonString));
		dismissButton.setRoundedCorners(true);
		dismissButton.addListener(buttonListener);		
		
		int panelWidth = PADDING * 2 + segmentedControl.getWidth();
		int panelHeight = PADDING * 4 + messageLabel.getHeight() + segmentedControl.getHeight() + resignButton.getHeight();
		panel = new Panel(container, panelWidth, panelHeight, Color.black);
		panel.setLocation((getWidth() - panel.getWidth()) / 2, (getHeight() - panel.getHeight() / 2));
		
		panel.add(messageLabel, panel.getPosition(Positionable.ReferencePoint.TopCenter), Positionable.ReferencePoint.TopCenter, 0, PADDING);
		panel.add(segmentedControl, messageLabel.getPosition(Positionable.ReferencePoint.BottomCenter), Positionable.ReferencePoint.TopCenter, 0, PADDING);
		panel.add(dismissButton, panel.getPosition(Positionable.ReferencePoint.BottomLeft), Positionable.ReferencePoint.BottomLeft, PADDING, -PADDING);
		panel.add(resignButton, panel.getPosition(Positionable.ReferencePoint.BottomRight), Positionable.ReferencePoint.BottomRight, -PADDING, -PADDING);
		
		add(panel, getPosition(Positionable.ReferencePoint.CenterCenter), Positionable.ReferencePoint.CenterCenter);
	}
	
	public SegmentedControl getSegmentedControl() {
		return segmentedControl;
	}
	
	public void render(GUIContext context, Graphics g) throws SlickException {		
		super.render(context, g);
	}

	private class ButtonListener implements ComponentListener {

		@Override
		public void componentActivated(AbstractComponent source) {
			listener.dismissModal(Modal.this);
		} 
		
	}
}
