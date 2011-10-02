package component;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.GUIContext;

public class Button extends Component {
	private Label label;
	private Vector2f position;
	private int width;
	private int height;
	
	private final static int PADDING = 10;
	
	public Button(GUIContext container, Label label, int x, int y, int width, int height) {
		super(container);
		
		this.label = label;
		label.setLocation(x + PADDING, y + PADDING);
		
		position = new Vector2f(x, y);
		this.width = width + 2 * PADDING;
		this.height = height + 2 * PADDING;
	}
	
	public Button(GUIContext container, Label label, int x, int y) {
		this(container, label, x, y, label.getWidth(), label.getHeight());
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
	public void render(GUIContext container, Graphics g) throws SlickException {
		label.render(container, g);
	}

	@Override
	public void setLocation(int x, int y) {
		if (position == null) {
			position = new Vector2f(x, y);
		} else {
			position.set(x, y);
		}
	}
}
