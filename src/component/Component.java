package component;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

import component.Positionable.ReferencePoint;

/**
 * An abstract class for a component.
 * 
 * @author Tanner Smith
 */
public abstract class Component extends AbstractComponent implements Positionable {
	protected Vector2f origin;
	private int width;
	private int height;
	
	protected List<Component> subComponents;
	protected boolean visible;
		
	/**
	 * Constructs a component.
	 * @param container Container the component resides in
	 */
	public Component(GUIContext container, int width, int height) {
		super(container);
		
		origin= new Vector2f();
		this.width = width;
		this.height = height;
		
		subComponents = new ArrayList<Component>();
		visible = true;
	}
	
	public void render(GUIContext context, Graphics g) throws SlickException {
		for (Component component : subComponents) {
			component.render(container, g);
		}
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public void add(Component component, Vector2f location, ReferencePoint referencePoint) {
		component.setPosition(location, referencePoint, 0, 0);
		
		subComponents.add(component);
	}
	
	public void add(Component component, Vector2f location, ReferencePoint referencePoint, int xOffset, int yOffset) {
		component.setPosition(location, referencePoint, xOffset, yOffset);
		
		subComponents.add(component);
	}
	
	public void remove(Component component) {
		subComponents.remove(component);
	}
	
	@Override
	public void setPosition(Vector2f location, ReferencePoint referencePoint) {
		int x = (int) location.x;
		int y = (int) location.y;
		
		switch (referencePoint) {
			case TopLeft:
				setLocation(x, y);
				break;
			case TopCenter:
				setLocation(x - getWidth() / 2, y);
				break;
			case TopRight:
				setLocation(x - getWidth(), y);
				break;
			case CenterLeft:
				setLocation(x, y - getHeight() / 2);
				break;
			case CenterCenter:
				setLocation(x - getWidth() / 2, y - getHeight() / 2);
				break;
			case CenterRight:
				setLocation(x - getWidth(), y - getHeight() / 2);
				break;
			case BottomLeft:
				setLocation(x, y - getHeight());
				break;
			case BottomCenter:
				setLocation(x - getWidth() / 2, y - getHeight());
				break;
			case BottomRight:
				setLocation(x - getWidth(), y - getHeight());
				break;
		}
	}
	
	@Override
	public void setPosition(Vector2f location, ReferencePoint referencePoint, int xOffset, int yOffset) {
		Vector2f offsetLocation = new Vector2f(location.x + xOffset, location.y + yOffset);
		
		setPosition(offsetLocation, referencePoint);
	}
	
	@Override
	public Vector2f getPosition(ReferencePoint referencePoint) {
		int x = getX();
		int y = getY();
		
		switch (referencePoint) {
			case TopLeft:
				return new Vector2f(x, y);
			case TopCenter:
				return new Vector2f(x + getWidth() / 2, y);
			case TopRight:
				return new Vector2f(x + getWidth(), y);
			case CenterLeft:
				return new Vector2f(x, y + getHeight() / 2);
			case CenterCenter:
				return new Vector2f(x + getWidth() / 2, y + getHeight() / 2);
			case CenterRight:
				return new Vector2f(x + getWidth(), y + getHeight() / 2);
			case BottomLeft:
				return new Vector2f(x, y + getHeight());
			case BottomCenter:
				return new Vector2f(x + getWidth() / 2, y + getHeight());
			case BottomRight:
				return new Vector2f(x + getWidth(), y + getHeight());
		}
		
		return null;
	}
	
	public final Shape getArea() {
		return new Rectangle(origin.x, origin.y, this.width, this.height);
	}
	
	public final int getWidth() {
		return width;
	}
	
	public final void setWidth(int width) {
		this.width = width;
	}
	
	public final int getHeight() {
		return height;
	}
	
	public final void setHeight(int height) {
		this.height = height;
	}
	
	@Override
	public final int getX() {
		return (int)origin.getX();
	}

	@Override
	public final int getY() {
		return (int)origin.getY();
	}
	
	public void setLocation(int x, int y) {
		if (subComponents != null) {
			for (Component c : subComponents) {
				c.setLocation(c.getX() + (x - getX()),
						c.getY() + y - getY());
			}
		}
		
		if (origin == null) {
			origin = new Vector2f((int)x, (int)y);
		} else {
			origin.set((int)x, (int)y);
		}
	}
	
	public void setAcceptingInput(boolean acceptingInput) {
		super.setAcceptingInput(acceptingInput);
		
		for (Component c : subComponents) {
			c.setAcceptingInput(acceptingInput);
		}
	}
}
