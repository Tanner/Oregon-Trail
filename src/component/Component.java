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

/**
 * {@code Component} provides basic features for GUI elements with an origin,
 * width, and height.
 */
public abstract class Component extends AbstractComponent implements Positionable {
	protected Vector2f origin;
	private int width;
	private int height;
	
	protected List<Component> components;
	private boolean mouseOver;
	private boolean visible;
		
	/**
	 * Constructs a {@code Component} with a width and height.
	 * @param container Container the component resides in
	 */
	public Component(GUIContext context, int width, int height) {
		super(context);
		
		origin = new Vector2f();
		this.width = width;
		this.height = height;
		
		components = new ArrayList<Component>();
		visible = true;
	}
	
	@Override
	public void render(GUIContext context, Graphics g) throws SlickException {
		for (Component component : components) {
			component.render(container, g);
		}
	}
	
	/**
	 * Set this component to be visible or invisible.
	 * @param visible Enables visibility if {@code true}, disables if {@code false}
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	/**
	 * Add this component at a location relative to a reference point.
	 * @param component {@code Component} to add
	 * @param location Location to add the component at
	 * @param referencePoint {@code ReferencePoint} in the component to set to the location
	 */
	public void add(Component component, Vector2f location, ReferencePoint referencePoint) {
		component.setPosition(location, referencePoint, 0, 0);
		
		components.add(component);
	}
	
	/**
	 * Add this component at a location relative to a reference point.
	 * @param component {@code Component} to add
	 * @param location Location to add the component at
	 * @param referencePoint ReferencePoint in the component to set to the location
	 * @param xOffset X Offset from the location
	 * @param yOffset Y Offset from the location
	 */
	public void add(Component component, Vector2f location, ReferencePoint referencePoint, int xOffset, int yOffset) {
		component.setPosition(location, referencePoint, xOffset, yOffset);
		
		components.add(component);
	}
	
	/**
	 * Add a set of components in a column with given spacing.
	 * @param components {@code Component}s to add
	 * @param location Location to add the components column at
	 * @param xOffset X Offset from the location
	 * @param yOffset Y Offset from the location
	 * @param spacing Spacing between components in the column
	 */
	public void addAsColumn(Component[] components, Vector2f location, int xOffset, int yOffset, int spacing) {
		for (int i = 0; i < components.length; i++) {
			add(components[i], location, ReferencePoint.TopLeft, xOffset, yOffset);
			location.set(location.x, location.y + components[i].getHeight() + spacing);
		}
	}
	
	/**
	 * Add a set of components in a row with given spacing.
	 * @param components {@code Component}s to add
	 * @param location Location to add the components row at
	 * @param xOffset X Offset from the location
	 * @param yOffset Y Offset from the location
	 * @param spacing Spacing between components in the row
	 */
	public void addAsRow(Component[] components, Vector2f location, int xOffset, int yOffset, int spacing) {
		for (int i = 0; i < components.length; i++) {
			if (components[i] != null) {
				add(components[i], location, ReferencePoint.TopLeft, xOffset, yOffset);
				location.set(location.x + components[i].getWidth() + spacing, location.y);
			}
		}
	}
	
	/**
	 * Add a set of components in a row with given spacing.
	 * @param components {@code Component}s to add
	 * @param location Location to add the components row at
	 * @param rows Number of rows to have
	 * @param cols Number of cols to have
	 * @param xOffset X Offset from the location
	 * @param yOffset Y Offset from the location
	 * @param xSpacing X Spacing between components in the row
	 * @param ySpacing Y Spacing between components in the row
	 */
	public void addAsGrid(Component[] components, Vector2f location, int rows, int cols, int xOffset, int yOffset, int xSpacing, int ySpacing) {
		int startIndex = 0;
		
		while (startIndex < components.length) {
			Component[] row = new Component[cols];
			for (int x = 0; x < row.length && startIndex + x < components.length; x++) {
				row[x] = components[startIndex + x];
			}
			
			addAsRow(row, location.copy(), xOffset, yOffset, xSpacing);
			
			location.set(location.x, location.y + components[startIndex].getHeight() + ySpacing);
			startIndex += cols;
		}
	}
	
	/**
	 * Remove a {@code Component}.
	 * @param component Component to remove
	 */
	public void remove(Component component) {
		components.remove(component);
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
	
	/**
	 * Return this area with origin, width, and height.
	 * @return This area's origin, width, and height
	 */
	public final Shape getArea() {
		return new Rectangle(origin.x, origin.y, this.width, this.height);
	}
	
	/**
	 * Return the width.
	 * @return The width
	 */
	public final int getWidth() {
		return width;
	}
	
	/**
	 * Sets the width.
	 * @param width The new width
	 */
	private final void setWidth(int width) {
		this.width = width;
	}
	
	/**
	 * Return the height.
	 * @param height the New
	 */
	public final int getHeight() {
		return height;
	}
	
	/**
	 * Set the height.
	 * @param height The new height
	 */
	private final void setHeight(int height) {
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
	
	@Override
	public void setLocation(int x, int y) {		
		if (origin == null) {
			origin = new Vector2f((int)x, (int)y);
		} else {
			if (components != null) {
				for (Component c : components) {
					c.setLocation(c.getX() + (x - getX()), c.getY() + (y - getY()));
				}
			}
			origin.set((int)x, (int)y);
		}
	}
	
	/**
	 * Set whether this component is accepting input.
	 * @param acceptingInput Enables input if {@code true}, disables if {@code false}
	 */
	public void setAcceptingInput(boolean acceptingInput) {
		super.setAcceptingInput(acceptingInput);
		
		for (Component c : components) {
			c.setAcceptingInput(acceptingInput);
		}
	}
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		super.mouseMoved(oldx, oldy, newx, newy);
		
		this.mouseOver = getArea().contains(newx, newy);
	}
	
	/**
	 * Return if mouse is over.
	 * @return {@code true} if mouse is over, {@code false} is not
	 */
	public boolean isMouseOver() {
		return mouseOver;
	}
	
	/**
	 * Return if visible.
	 * @return {@code true} if visible, {@code false} is not
	 */
	public boolean isVisible() {
		return visible;
	}
}
