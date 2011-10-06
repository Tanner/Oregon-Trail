package scene;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.GUIContext;

import scene.layout.Layout;

import component.Component;
import component.Positionable.ReferencePoint;

public class SceneLayer extends Component {
	private Vector2f location;
	private int width;
	private int height;
	
	protected Layout layout;
	
	public SceneLayer(GUIContext container) {
		super(container);
		this.width = container.getWidth();
		this.height = container.getHeight();
	}

	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		super.render(container, g);
		
		g.translate(location.x, location.y);
	}
	
	public void add(Component component) {
		if (layout != null) {
			layout.setComponentLocation(component);
		}
		
		components.add(component);
	}

	public void setLayout(Layout layout) {
		this.layout = layout;
	}

	@Override
	public void setLocation(int x, int y) {
		this.location = new Vector2f(x, y);
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
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
	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public void setHeight(int height) {
		this.height = height;
	}
}
