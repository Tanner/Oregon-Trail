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

public class Background extends Component {
	protected Vector2f location;
	protected int width, height;
	protected Color backgroundColor;
	protected Image backgroundImage;
	
	/**
	 * Constructor for Background object 
	 * @param container gui item held within this object
	 */
	
	public Background(GUIContext container) {
		super(container);
		this.location = new Vector2f(0, 0);
		this.width = container.getWidth();
		this.height = container.getHeight();
	}
	
	/**
	 * Constructor for Background object
	 * @param container gui item held within this object
	 * @param backgroundColor color of background
	 */
	
	public Background(GUIContext container, Color backgroundColor) {
		this(container);
		this.backgroundColor = backgroundColor;
	}
	
	public Background(GUIContext container, Image backgroundImage) {
		this(container);
		this.backgroundImage = backgroundImage;
	}
	
	public Background(GUIContext container, Color backgroundColor, int width, int height) {
		this(container, backgroundColor);
		
		this.width = width;
		this.height = height;
	}

	/**
	 * @return the height of this object
	 */

	@Override
	public int getHeight() {
		return height;
	}

	/**
	 * @return the width of this object
	 */
	
	@Override
	public int getWidth() {
		return width;
	}
	
	/**
	 * @return the x position of this object
	 */
	
	@Override
	public int getX() {
		return (int) location.x;
	}

	/**
	 * @return the y position of this object
	 */
	
	@Override
	public int getY() {
		return (int) location.y;
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
			g.fillRect(location.x, location.y, width, height);
		}
		if (backgroundImage != null) {
			g.drawImage(backgroundImage, getX(), getY());
		}
	}

	/**
	 * Sets the location of the object on the screen
	 * @param x
	 * @param y
	 */
	
	@Override
	public void setLocation(int x, int y) {
		if (location != null) {
			location.set(x, y);
		}
	}

	/**
	 * Sets the width of the object
	 * @param width the new width
	 */
	
	@Override
	public void setWidth(int width) {
		this.width = width;
	}
	
	/**
	 * Sets the height of the object
	 * @param height the new height
	 */

	@Override
	public void setHeight(int height) {
		this.height = height;
	}
}
