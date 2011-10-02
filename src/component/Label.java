package component;

import org.newdawn.slick.*;
import org.newdawn.slick.gui.*;

public class Label extends Component {
	
	private String text;
	private Font font;
	private int x, y;
	private Color c;
	private int width, height;
	
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
	
	public Label(GUIContext context, int x, int y, Font font, Color c) {
		this(context,x,y,font,c,"");
	}

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
