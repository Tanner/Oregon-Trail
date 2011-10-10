package component;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

/**
 * {@code Panel} inherits from {@code Component} to draw a background color or image.
 */
public class Panel extends Component {
	protected Color backgroundColor;
	protected Image backgroundImage;
	
	/**
	 * Constructs a {@code Panel} with a width, height, and background color.
	 * @param context The GUI context
	 * @param width The width
	 * @param height The height
	 * @param backgroundColor A background color
	 */
	public Panel(GUIContext context, int width, int height, Color backgroundColor) {
		super(context, width, height);
		
		this.backgroundColor = backgroundColor;
	}
	
	/**
	 * Constructs a {@code Panel} with a and background color.
	 * @param context The GUI context
	 * @param backgroundColor A background color
	 */
	public Panel(GUIContext context, Color backgroundColor) {
		this(context, context.getWidth(), context.getHeight(), backgroundColor);
	}
	
	/**
	 * Constructs a {@code Panel} with a width, height, and background image.
	 * @param context The GUI context
	 * @param width The width
	 * @param height The height
	 * @param backgroundImage A background image
	 */
	public Panel(GUIContext context, int width, int height, Image backgroundImage) {
		super(context, width, height);
		
		this.backgroundImage = backgroundImage;
	}
	
	/**
	 * Constructs a {@code Panel} with a background image.
	 * @param context The GUI context
	 * @param backgroundImage A background image
	 */
	public Panel(GUIContext context, Image backgroundImage) {
		this(context, context.getWidth(), context.getHeight(), backgroundImage);
	}

	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		if (backgroundColor != null) {
			g.setColor(backgroundColor);
			g.fillRect(getX(), getY(), getWidth(), getHeight());
		}
		if (backgroundImage != null) {
			g.drawImage(backgroundImage, getX(), getY());
		}
		
		super.render(container, g);
	}
}