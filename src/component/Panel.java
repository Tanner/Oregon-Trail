package component;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.GUIContext;

/**
 * This class holds basic graphic utilities
 * 
 * @author 
 *
 */

public class Panel extends Component {
	protected Color backgroundColor;
	protected Image backgroundImage;
	
	public Panel(GUIContext container, int width, int height, Color backgroundColor) {
		super(container, width, height);
		
		this.backgroundColor = backgroundColor;
	}
	
	public Panel(GUIContext container, Color backgroundColor) {
		this(container, container.getWidth(), container.getHeight(), backgroundColor);
	}
	
	public Panel(GUIContext container, int width, int height, Image backgroundImage) {
		super(container, width, height);
		
		this.backgroundImage = backgroundImage;
	}
	
	public Panel(GUIContext container, Image backgroundImage) {
		this(container, container.getWidth(), container.getHeight(), backgroundImage);
	}
	
	public Panel(GUIContext container, Color backgroundColor, int width, int height) {
		this(container, backgroundColor);
	}
	
	/**
	 * This method will draw the object
	 * 
	 * @param container
	 * @param g
	 * @throws SlickException
	 */

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