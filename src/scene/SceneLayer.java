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
	
	protected List<Component> components;
	protected Layout layout;
	
	public SceneLayer(GUIContext container) {
		super(container);
		components = new ArrayList<Component>();
		this.width = container.getWidth();
		this.height = container.getHeight();
	}
	
	public void render(GUIContext container, Graphics g) throws SlickException {
		for (Component component : components) {
			component.render(container, g);
		}
	}
	
	public void add(Component component) {
		if (layout != null) {
			layout.setComponentLocation(component);
		}
		
		components.add(component);
	}
	
	public void add(Component component, Vector2f location, ReferencePoint referencePoint) {
		component.setPosition(location, referencePoint, 0, 0);
		
		components.add(component);
	}
	
	public void add(Component component, Vector2f location, ReferencePoint referencePoint, int xOffset, int yOffset) {
		component.setPosition(location, referencePoint, xOffset, yOffset);
		
		components.add(component);
	}

	public void setLayout(Layout layout) {
		this.layout = layout;
	}
	
	public void setLocation(int x, int y) {
		this.location = new Vector2f(x, y);
	}
	
	public int getWidth() {
		return width;
	}
	
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
}
