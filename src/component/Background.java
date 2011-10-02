package component;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.GUIContext;

public class Background extends Component {
	protected Vector2f location;
	protected int width, height;
	protected Color backgroundColor;
	
	public Background(GUIContext container) {
		super(container);
		this.location = new Vector2f(0, 0);
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
		return (int) location.x;
	}

	@Override
	public int getY() {
		return (int) location.y;
	}

	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		g.setColor(backgroundColor);
		g.fillRect(location.x, location.y, width, height);
	}

	@Override
	public void setLocation(int x, int y) {
		if (location != null) {
			location.set(x, y);
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
}
