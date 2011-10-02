package component;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

public class Background extends Component {
	protected int x, y, width, height;
	protected Color backgroundColor;
	
	public Background(GUIContext container) {
		super(container);
		this.width = container.getWidth();
		this.height = container.getHeight();
	}
	
	public Background(GUIContext container, Color backgroundColor) {
		this(container);
		this.backgroundColor = backgroundColor;
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
	public void render(GUIContext container, Graphics g) throws SlickException {
		g.setColor(backgroundColor);
		g.fillRect(0, 0, width, height);
	}

	@Override
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
