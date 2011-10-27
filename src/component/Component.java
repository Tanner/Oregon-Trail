package component;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

import scene.Scene;

/**
 * {@code Component} provides basic features for GUI elements with an origin,
 * width, and height.
 */
public abstract class Component extends AbstractComponent implements Positionable {
	public static enum DebugMode {
		NONE,
		VISIBLE,
		ALL
	}
	private static DebugMode debugMode;
	
	public static enum BevelType {
		NONE,
		IN,
		OUT
	}
	
	private Component parentComponent;
	private Vector2f origin;
	private int width;
	private int height;
	
	private Color backgroundColor;
	private BevelType beveled;
	private int bevelWidth;
	
	private Color borderColor;
	private int topBorderWidth;
	private int rightBorderWidth;
	private int bottomBorderWidth;
	private int leftBorderWidth;
	
	protected List<Component> components;
	private boolean mouseOver;
	private boolean visible;
	
	private boolean tooltipEnabled;
	private String tooltipMessage;
	
	/**
	 * Constructs a {@code Component} with a width and height.
	 * @param context The Gui Context
	 * @param width the width of the component
	 * @param heigh the height of the component
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
		g.setClip((Rectangle) getArea());
		
		if (backgroundColor != null) {
			g.setColor(backgroundColor);
			if (beveled == BevelType.NONE) {
				g.fillRect(getX(), getY(), getWidth(), getHeight());
			} else {
				Color brightColor = backgroundColor.brighter(0.2f);
				Color darkColor = backgroundColor.darker(0.2f);
				
				g.setColor(backgroundColor);
				// inner rect
				g.fillRect(getX() + bevelWidth,
						getY() + bevelWidth,
						getWidth() - bevelWidth * 2,
						getHeight() - bevelWidth * 2);
				
				// top bar
				if (beveled == BevelType.OUT) {
					g.setColor(brightColor);
				} else if (beveled == BevelType.IN) {
					g.setColor(darkColor);
				}
				g.fillRect(getX() + leftBorderWidth,
						getY() + topBorderWidth,
						getWidth() - leftBorderWidth - rightBorderWidth,
						bevelWidth);
						
				// bottom bar
				if (beveled == BevelType.OUT) {
					g.setColor(darkColor);
				} else if (beveled == BevelType.IN) {
					g.setColor(brightColor);
				}
				g.fillRect(getX() + leftBorderWidth,
						getY() + getHeight() - bevelWidth - bottomBorderWidth,
						getWidth() - leftBorderWidth - rightBorderWidth,
						bevelWidth);
						
				// left bar
				if (beveled == BevelType.OUT) {
					g.setColor(brightColor);
				} else if (beveled == BevelType.IN) {
					g.setColor(darkColor);
				}
				g.fillRect(getX() + leftBorderWidth,
						getY() + topBorderWidth,
						bevelWidth,
						getHeight() - topBorderWidth - bottomBorderWidth);
				
				// right bar
				if (beveled == BevelType.OUT) {
					g.setColor(darkColor);
				} else if (beveled == BevelType.IN) {
					g.setColor(brightColor);
				}
				g.fillRect(getX() + getWidth() - bevelWidth - rightBorderWidth,
						getY() + topBorderWidth,
						bevelWidth,
						getHeight() - topBorderWidth - bottomBorderWidth);
			}
		}
		
		for (Component component : components) {
			component.render(container, g);
			
			if (debugMode == DebugMode.ALL || debugMode == DebugMode.VISIBLE && component.isVisible()) {
				if (component.isVisible()) {
					g.setColor(Color.red);
				} else {
					g.setColor(Color.yellow);
				}
				g.drawRect(component.getX(), component.getY(), component.getWidth() - 1, component.getHeight() - 1);
			}
		}
		
		g.setClip((Rectangle) getArea());
				
		// border
		g.setColor(borderColor);
		// top bar
		g.fillRect(getX(),
				getY(),
				getWidth(),
				topBorderWidth);
		
		// right bar
		g.fillRect(getX() + getWidth() - rightBorderWidth,
				getY(),
				rightBorderWidth,
				getHeight());
		
		// bottom bar
		g.fillRect(getX(),
				getY() + getHeight() - bottomBorderWidth,
				getWidth(),
				bottomBorderWidth);
				
		// left bar
		g.fillRect(getX(),
				getY(),
				leftBorderWidth,
				getHeight());
		
		g.clearClip();
	}
	
	public void setParentComponent(Component component) {
		this.parentComponent = component;
	}
	
	/**
	 * Return if visible and parent component(s) are visible.
	 * @return {@code true} if visible, {@code false} is not
	 */
	public boolean isVisible() {
		if (parentComponent != null) {
			return visible && parentComponent.isVisible();
		}
		
		return false;
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
	 * @param referencePoint ReferencePoint in the component to set to the location
	 * @param xOffset X Offset from the location
	 * @param yOffset Y Offset from the location
	 */
	public void add(Component component, Vector2f location, ReferencePoint referencePoint, int xOffset, int yOffset) {
		component.setPosition(location, referencePoint, xOffset, yOffset);
		
		components.add(component);
		component.setParentComponent(this);
	}
	
	/**
	 * Add this component at a location relative to a reference point.
	 * @param component {@code Component} to add
	 * @param location Location to add the component at
	 * @param referencePoint {@code ReferencePoint} in the component to set to the location
	 */
	public void add(Component component, Vector2f location, ReferencePoint referencePoint) {
		add(component, location, referencePoint, 0, 0);
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
			add(components[i], location, ReferencePoint.TOPLEFT, xOffset, yOffset);
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
				add(components[i], location, ReferencePoint.TOPLEFT, xOffset, yOffset);
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
		component.setParentComponent(null);
		components.remove(component);
	}
	
	@Override
	public void setPosition(Vector2f location, ReferencePoint referencePoint) {
		int x = (int) location.x;
		int y = (int) location.y;
		
		switch (referencePoint) {
			case TOPLEFT:
				setLocation(x, y);
				break;
			case TOPCENTER:
				setLocation(x - getWidth() / 2, y);
				break;
			case TOPRIGHT:
				setLocation(x - getWidth(), y);
				break;
			case CENTERLEFT:
				setLocation(x, y - getHeight() / 2);
				break;
			case CENTERCENTER:
				setLocation(x - getWidth() / 2, y - getHeight() / 2);
				break;
			case CENTERRIGHT:
				setLocation(x - getWidth(), y - getHeight() / 2);
				break;
			case BOTTOMLEFT:
				setLocation(x, y - getHeight());
				break;
			case BOTTOMCENTER:
				setLocation(x - getWidth() / 2, y - getHeight());
				break;
			case BOTTOMRIGHT:
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
			case TOPLEFT:
				return new Vector2f(x, y);
			case TOPCENTER:
				return new Vector2f(x + getWidth() / 2, y);
			case TOPRIGHT:
				return new Vector2f(x + getWidth(), y);
			case CENTERLEFT:
				return new Vector2f(x, y + getHeight() / 2);
			case CENTERCENTER:
				return new Vector2f(x + getWidth() / 2, y + getHeight() / 2);
			case CENTERRIGHT:
				return new Vector2f(x + getWidth(), y + getHeight() / 2);
			case BOTTOMLEFT:
				return new Vector2f(x, y + getHeight());
			case BOTTOMCENTER:
				return new Vector2f(x + getWidth() / 2, y + getHeight());
			case BOTTOMRIGHT:
				return new Vector2f(x + getWidth(), y + getHeight());
		}
		
		return null;
	}
	
	public Color getBackgroundColor() {
		return backgroundColor;
	}
	
	/**
	 * Set the background color.
	 * @param color The new background color
	 */
	public void setBackgroundColor(Color color) {
		this.backgroundColor = color;
	}
	
	/**
	 * Enable/disable a beveled appearance.
	 * @param beveled Enables beveled appearance if {@code true}, disables if {@code false}
	 */
	public void setBevel(BevelType bevelType) {
		this.beveled = bevelType;
	}
	
	/**
	 * Set the bevel width.
	 * @param width The new bevel width
	 */
	public void setBevelWidth(int width) {
		this.bevelWidth = width;
	}
	
	/**
	 * Set the border width of all sides.
	 * @param width The new border width of all sides
	 */
	public void setBorderWidth(int width) {
		setTopBorderWidth(width);
		setRightBorderWidth(width);
		setBottomBorderWidth(width);
		setLeftBorderWidth(width);
	}
	
	/**
	 * Set the bottom border width.
	 * @param width The new top border width
	 */
	public void setTopBorderWidth(int width) {
		this.topBorderWidth = width;
	}
	
	/**
	 * Set the right border width.
	 * @param width The new right border width
	 */
	public void setRightBorderWidth(int width) {
		this.rightBorderWidth = width;
	}
	
	/**
	 * Set the bottom border width.
	 * @param width The new bottom border width
	 */
	public void setBottomBorderWidth(int width) {
		this.bottomBorderWidth = width;
	}
	
	/**
	 * Set the left border width.
	 * @param width The new left border width
	 */
	public void setLeftBorderWidth(int width) {
		this.leftBorderWidth = width;
	}
	
	/**
	 * Set the border color.
	 * @param color The new color
	 */
	public void setBorderColor(Color color) {
		this.borderColor = color;
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
	 * Return the height.
	 * @param height the New
	 */
	public final int getHeight() {
		return height;
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
			origin = new Vector2f(x, y);
		} else {
			if (components != null) {
				for (Component c : components) {
					c.setLocation(c.getX() + (x - getX()), c.getY() + (y - getY()));
				}
			}
			origin.set(x, y);
		}
	}
	
	/**
	 * Set whether this component is accepting input. Also sets all subcomponents
	 * of this component.
	 * @param acceptingInput Enables input if {@code true}, disables if {@code false}
	 */
	public void setAcceptingInput(boolean acceptingInput) {
		super.setAcceptingInput(acceptingInput);
		
		if (!acceptingInput) {
			mouseOver = false;
		}
		
		for (Component c : components) {
			c.setAcceptingInput(acceptingInput);
		}
	}
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		if (!isVisible() || !isAcceptingInput()) {
			return;
		}
		
		super.mouseMoved(oldx, oldy, newx, newy);
		
		this.mouseOver = getArea().contains(newx, newy);
		
		if (isMouseOver() && tooltipEnabled) {
			Scene.showTooltip(newx, newy, this, tooltipMessage);
		}
	}
	
	/**
	 * Return if mouse is over.
	 * @return {@code true} if mouse is over, {@code false} is not
	 */
	public boolean isMouseOver() {
		return mouseOver;
	}

	public static void changeDebugMode() {
		if (debugMode == DebugMode.NONE) {
			debugMode = DebugMode.VISIBLE;
		} else if (debugMode == DebugMode.VISIBLE) {
			debugMode = DebugMode.ALL;
		} else {
			debugMode = DebugMode.NONE;
		}
	}

	public boolean isTooltipEnabled() {
		return tooltipEnabled;
	}

	public void setTooltipEnabled(boolean tooltipEnabled) {
		this.tooltipEnabled = tooltipEnabled;
		
		if (tooltipMessage == null) {
			setTooltipMessage("");
		}
	}

	public String getTooltipMessage() {
		return tooltipMessage;
	}

	public void setTooltipMessage(String tooltipMessage) {
		this.tooltipMessage = tooltipMessage;
	}
}
