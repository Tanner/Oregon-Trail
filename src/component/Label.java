package component;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.*;

/**
 * A label class to draw text on screen.  Uses
 * Font's drawString method to accomplish this.
 * 
 * @author Jeremy Grebner
 */
public class Label extends Component {
	private String text;
	private Font font;
	private Vector2f position;
	private Color c;
	private int width, height;
	
	/**
	 * Creates a label to be drawn on the screen.
	 * 
	 * @param context The game container
	 * @param x	far left x coordinate
	 * @param y top y component
	 * @param font Font the text will be drawn in
	 * @param c	Color of the text
	 * @param text The text to draw
	 */
	public Label(GUIContext context, Vector2f position, Font font, Color c, String text) {
		super(context);
		
		this.position = position;
		this.font = font;
		this.c = c;
		this.text = text;
		this.width = font.getWidth(text);
		this.height = font.getLineHeight();
	}
	
	/**
	 * Creates a label to be drawn on the screen.
	 * 
	 * @param context The game container
	 * @param font Font the text will be drawn in
	 * @param c Color of the text
	 * @param text The text to draw
	 */
	public Label(GUIContext context, Font font, Color c, String text) {
		super(context);
		
		this.position = new Vector2f(0, 0);
		this.font = font;
		this.c = c;
		this.text = text;
		this.width = font.getWidth(text);
		this.height = font.getLineHeight();
	}
	
	
	/**
	 * Creates a label that is initially empty.
	 * 
	 * @param context The game container
	 * @param x	far left x coordinate
	 * @param y top y component
	 * @param font Font the text will be drawn in
	 * @param c	Color of the text
	 */
	public Label(GUIContext context, Vector2f position, Font font, Color c) {
		this(context, position, font, c, "");
	}
	
	@Override
	public void render(GUIContext context, Graphics g) throws SlickException {
//		g.setColor(Color.orange);
//		g.fillRect(getX(), getY(), getWidth(), getHeight());
		
		String renderText = text;
		while (font.getWidth(renderText) > width) {
			renderText = text.substring(0, renderText.length()-1);
		}
		font.drawString((width - font.getWidth(renderText)) / 2 + position.getX(), position.getY(), renderText, c);
	}
	
	/**
	 * Updates the label with new text, and calculates new width
	 * and height based on that text.
	 * 
	 * @param text The text you want drawn on the label
	 */
	public void setText(String text) {
		this.text = text;
		setWidth(font.getWidth(text));
	}
	
	/**
	 * Get the text of this label.
	 * @return Label's text
	 */
	public String getText() {
		return text;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getX() {
		return (int)position.getX();
	}

	@Override
	public int getY() {
		return (int)position.getY();
	}

	@Override
	public void setLocation(int x, int y) {
		if (font != null) {
			position = new Vector2f(x, y);
		}
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public void setHeight(int height) {
		this.height = height;
	}
	
	/**
	 * Changes the color of the label.
	 * @param color New color
	 */
	public void setColor(Color color) {
		c = color;
	}
}
