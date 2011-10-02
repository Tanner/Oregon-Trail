package component;

import org.newdawn.slick.Font;

public class Label {
	
	private String text;
	private Font font;
	private int x, y;
	
	public Label(int x, int y, Font font, String text) {
		this.x = x;
		this.y = y;
		this.font = font;
		this.text = text;
	}
	
	public Label(int x, int y, Font font) {
		this(x,y,font,"");
	}
	
	public void draw() {
		font.drawString(x, y, text);
	}

	public void setText(String text) {
		this.text = text;
	}
}
