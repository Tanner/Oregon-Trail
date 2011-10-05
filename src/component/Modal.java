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

public class Modal extends Component {
	private static final int MARGIN = 50;
	private static final int PADDING = 20;
	
	private Scene scene;
	private Vector2f location;
	private int width;
	private int height;
	private Label messageLabel;

	public Modal(GUIContext container) {
		super(container);
	}
	
	public Modal(GUIContext container, Scene scene, String message, String submitButtonString) {
		this(container);
		
		this.scene = scene;
		
		this.width = container.getWidth() - MARGIN*2;
		
		location = new Vector2f(MARGIN, (width - height) / 2);
		
		UnicodeFont fieldFont = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.FIELD);		
		this.messageLabel = new Label(container, fieldFont, Color.white, message, width);
		messageLabel.setAlignment(Label.Alignment.Center);
		
		Button okButton = new Button(container, new Label(container, fieldFont, Color.white, submitButtonString), 200, 40);
		okButton.addListener(new ButtonListener());
		
		this.height = PADDING*3 + messageLabel.getHeight() + okButton.getHeight();
		location = new Vector2f(MARGIN, (container.getHeight() - height) / 2);

		add(okButton, getPosition(Positionable.ReferencePoint.BottomCenter), Positionable.ReferencePoint.BottomCenter, 0, -PADDING);
		add(messageLabel, getPosition(Positionable.ReferencePoint.TopLeft), Positionable.ReferencePoint.TopLeft, 0, PADDING);		
	}
	
	public Modal(GUIContext container, Scene scene, String message, Component component, String submitButtonString, String cancelButtonString) {
		this(container);
		
		this.scene = scene;
		
		this.width = container.getWidth() - MARGIN*2;
		
		location = new Vector2f(MARGIN, (width - height) / 2);
		
		UnicodeFont fieldFont = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.FIELD);		
		this.messageLabel = new Label(container, fieldFont, Color.white, message, width);
		messageLabel.setAlignment(Label.Alignment.Center);
		
		ButtonListener buttonListener = new ButtonListener();
		Button submitButton = new Button(container, new Label(container, fieldFont, Color.white, submitButtonString), (width - PADDING*2) / 2 - PADDING, 40);
		submitButton.addListener(buttonListener);
		
		Button cancelButton = new Button(container, new Label(container, fieldFont, Color.white, cancelButtonString), (width - PADDING*2) / 2 - PADDING, 40);
		cancelButton.addListener(buttonListener);
		
		this.height = PADDING*4 + messageLabel.getHeight() + component.getHeight() + submitButton.getHeight();
		location = new Vector2f(MARGIN, (container.getHeight() - height) / 2);
		
		component.setWidth(width - PADDING*2);
		
		add(messageLabel, getPosition(Positionable.ReferencePoint.TopLeft), Positionable.ReferencePoint.TopLeft, 0, PADDING);
		add(component, messageLabel.getPosition(Positionable.ReferencePoint.BottomCenter), Positionable.ReferencePoint.TopCenter, 0, PADDING);
		add(cancelButton, getPosition(Positionable.ReferencePoint.BottomLeft), Positionable.ReferencePoint.BottomLeft, PADDING, -PADDING);
		add(submitButton, getPosition(Positionable.ReferencePoint.BottomRight), Positionable.ReferencePoint.BottomRight, -PADDING, -PADDING);
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
			scene.closeModal(Modal.this);
		}
		
	}
}
