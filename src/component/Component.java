package component;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

import scene.Scene.ReferencePoint;

public abstract class Component extends AbstractComponent implements Positionable {
	public Component(GUIContext container) {
		super(container);
	}
	
	public void setPosition(int x, int y, ReferencePoint referencePoint) {
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
	
	public Vector2f getPosition(ReferencePoint referencePoint) {
		return null;
	}
}
