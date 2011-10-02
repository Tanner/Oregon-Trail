package component;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

/**
 * An abstract class for a component.
 * 
 * @author Tanner Smith
 */
public abstract class Component extends AbstractComponent implements Positionable {
	/**
	 * Constructs a component.
	 * @param container Container the component resides in
	 */
	public Component(GUIContext container) {
		super(container);
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
}
