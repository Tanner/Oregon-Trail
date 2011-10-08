package component;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

import component.Positionable.ReferencePoint;

/**
 * An abstract class for a component.
 */
public abstract class Component extends AbstractComponent implements Positionable {
	protected boolean visible;
	
	protected List<Component> components;
	
	/**
	 * Constructs a component.
	 * @param container Container the component resides in
	 */
	public Component(GUIContext container) {
		super(container);
		
		components = new ArrayList<Component>();
		
		visible = true;
	}
	
	/**
	 * Render the component and its subcomponents to the screen.
	 * @param context Container the component resides in
	 */
	public void render(GUIContext context, Graphics g) throws SlickException {
		for (Component component : components) {
			component.render(container, g);
		}
	}
	
	/**
	 * Set this component to be visible or invisible.
	 * @param visible Whether it should be visible (true) or not
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	/**
	 * Add this component at a location relative to a reference point.
	 * @param component Component to add
	 * @param location Location to add the component at
	 * @param referencePoint ReferencePoint in the component to set to the location
	 */
	public void add(Component component, Vector2f location, ReferencePoint referencePoint) {
		component.setPosition(location, referencePoint, 0, 0);
		
		components.add(component);
	}
	
	/**
	 * Add this component at a location relative to a reference point.
	 * @param component Component to add
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
	 * @param components Components to add
	 * @param location Location to add the components column at
	 * @param xOffset X Offset from the location
	 * @param yOffset Y Offset from the location
	 * @param spacing Spacing between components in the column
	 */
	public void addAsColumn(Component[] components, Vector2f location, int xOffset, int yOffset, int spacing) {
		for (int i = 0; i < components.length; i++) {
			add(components[i], location, ReferencePoint.TopLeft, xOffset, yOffset);
			location.set(location.x, location.y + components[i].getHeight() + spacing);
			
			this.components.add(components[i]);
		}
	}
	
	/**
	 * Add a set of components in a row with given spacing.
	 * @param components Components to add
	 * @param location Location to add the components row at
	 * @param xOffset X Offset from the location
	 * @param yOffset Y Offset from the location
	 * @param spacing Spacing between components in the row
	 */
	public void addAsRow(Component[] components, Vector2f location, int xOffset, int yOffset, int spacing) {
		for (int i = 0; i < components.length; i++) {
			add(components[i], location, ReferencePoint.TopLeft, xOffset, yOffset);
			location.set(location.x + components[i].getWidth() + spacing, location.y);
			
			this.components.add(components[i]);
		}
	}
	
	/**
	 * Remove a component from this component.
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
	 * Change the width.
	 * @param width New width
	 */
	public abstract void setWidth(int width);
	
	/**
	 * Change the height.
	 * @param height New height
	 */
	public abstract void setHeight(int height);
	
	/**
	 * Set whether this component is accepting input.
	 * @param acceptingInput Whether the component is accepting input (true = yes)
	 */
	public void setAcceptingInput(boolean acceptingInput) {
		super.setAcceptingInput(acceptingInput);
		
		for (Component c : components) {
			c.setAcceptingInput(acceptingInput);
		}
	}
}
