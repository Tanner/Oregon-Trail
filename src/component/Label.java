package component;

import org.newdawn.slick.*;
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
	private int x, y;
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
	public Label(GUIContext context, int x, int y, Font font, Color c, String text) {
		super(context);
		this.x = x;
		this.y = y;
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
	public Label(GUIContext context, int x, int y, Font font, Color c) {
		this(context,x,y,font,c,"");
	}
	
	
	/**
	 * Updates the label with new text, and calculates new width
	 * and height based on that text.
	 * 
	 * @param text The text you want drawn on the label
	 */
	public void setText(String text) {
		this.text = text;
		this.width = font.getWidth(text);
		this.height = font.getLineHeight();
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
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public void render(GUIContext context, Graphics g) throws SlickException {
		font.drawString(x, y, text, c);
		
	}

	@Override
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
