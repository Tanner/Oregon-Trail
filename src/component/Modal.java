package component;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;

import scene.Scene;

import core.FontManager;
import core.GameDirector;
import core.Logger;

public class Modal extends Component {
	private static final int MARGIN = 50;
	private static final int PADDING = 20;
	
	private Scene scene;
	private Vector2f location;
	private int width;
	private int height;
	private Label messageLabel;
	private Button resignButton;
	private Button dismissButton;
	private SegmentedControl segmentedControl;

	public Modal(GUIContext container) {
		super(container);
	}
	
	public Modal(GUIContext container, Scene scene, String message, String dismissButtonText) {
		this(container);
		
		this.scene = scene;
		
		this.width = container.getWidth() - MARGIN*2;
		
		location = new Vector2f(MARGIN, (width - height) / 2);
		
		UnicodeFont fieldFont = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.FIELD);		
		this.messageLabel = new Label(container, fieldFont, Color.white, message, width);
		messageLabel.setAlignment(Label.Alignment.Center);
		
		dismissButton = new Button(container, new Label(container, fieldFont, Color.white, dismissButtonText), 200, 40);
		dismissButton.setRoundedCorners(true);
		dismissButton.addListener(new ButtonListener());
		
		this.height = PADDING*3 + messageLabel.getHeight() + dismissButton.getHeight();
		location = new Vector2f(MARGIN, (container.getHeight() - height) / 2);

		add(messageLabel, getPosition(Positionable.ReferencePoint.TopLeft), Positionable.ReferencePoint.TopLeft, 0, PADDING);		
		add(dismissButton, getPosition(Positionable.ReferencePoint.BottomCenter), Positionable.ReferencePoint.BottomCenter, 0, -PADDING);
	}
	
	public Modal(GUIContext container, Scene scene, String message, SegmentedControl segmentedControl, String submitButtonString, String cancelButtonString) {
		this(container);
		
		this.scene = scene;
		this.segmentedControl = segmentedControl;
		this.width = container.getWidth() - MARGIN*2;
		
		location = new Vector2f(MARGIN, (width - height) / 2);
		
		UnicodeFont fieldFont = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.FIELD);		
		messageLabel = new Label(container, fieldFont, Color.white, message, width);
		messageLabel.setAlignment(Label.Alignment.Center);
		
		ButtonListener buttonListener = new ButtonListener();
		resignButton = new Button(container, new Label(container, fieldFont, Color.white, submitButtonString), (width - PADDING*2) / 2 - PADDING / 2, 40);
		resignButton.setRoundedCorners(true);
		resignButton.addListener(buttonListener);
		
		dismissButton = new Button(container, new Label(container, fieldFont, Color.white, cancelButtonString), (width - PADDING*2) / 2 - PADDING / 2, 40);
		dismissButton.setRoundedCorners(true);
		dismissButton.addListener(buttonListener);
		
		this.height = PADDING*4 + messageLabel.getHeight() + segmentedControl.getHeight() + resignButton.getHeight();
		location = new Vector2f(MARGIN, (container.getHeight() - height) / 2);
		
		segmentedControl.setWidth(width - PADDING*2);
		
		add(messageLabel, getPosition(Positionable.ReferencePoint.TopLeft), Positionable.ReferencePoint.TopLeft, 0, PADDING);
		add(segmentedControl, messageLabel.getPosition(Positionable.ReferencePoint.BottomCenter), Positionable.ReferencePoint.TopCenter, 0, PADDING);
		add(dismissButton, getPosition(Positionable.ReferencePoint.BottomLeft), Positionable.ReferencePoint.BottomLeft, PADDING, -PADDING);
		add(resignButton, getPosition(Positionable.ReferencePoint.BottomRight), Positionable.ReferencePoint.BottomRight, -PADDING, -PADDING);
	}
	
	public void render(GUIContext context, Graphics g) throws SlickException {
		g.setColor(Color.black);
		g.fillRect(getX(), getY(), getWidth(), getHeight());
		
		super.render(context, g);
	}
	
	@Override
	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public void setLocation(int x, int y) {
//		if (components != null) {
//			for (Component c : components) {
//				c.setLocation((int)(c.getX() + x - location.x), (int)(c.getY() + y - location.y));
//			}
//		}
//		
		if (location != null) {
			location.set(x, y);
		}
	}

	@Override
	public int getX() {
		return (int)location.x;
	}

	@Override
	public int getY() {
		return (int)location.y;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}
	
	private class ButtonListener implements ComponentListener {

		@Override
		public void componentActivated(AbstractComponent source) {
			if (source == resignButton) {
				scene.resignModal(Modal.this, segmentedControl.getSelection());
			} else {
				scene.dismissModal(Modal.this);
			}
		} 
		
	}
}
