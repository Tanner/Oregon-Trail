package component;

import java.util.Iterator;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.GUIContext;

import component.Positionable.ReferencePoint;

/**
 * A scene layer which holds components.
 */
public class SceneLayer extends Component {
	private Color overlayColor;
	
	/**
	 * Constructs a SceneLayer with a container.
	 * @param container Container
	 */
	public SceneLayer(GUIContext container) {
		super(container, container.getWidth(), container.getHeight());
		setLocation(0, 0);
		setVisible(true);
	}

	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		if (!isVisible()) {
			return;
		}
		
		g.setWorldClip(getX(), getY(), container.getWidth(), getHeight());
		super.render(container, g);
		
		if (overlayColor != null) {
			g.setColor(overlayColor);
			g.fillRect(getX(), getY(), getWidth(), getHeight());
		}
		
		g.clearWorldClip();
	}
	
	@Override
	public void update(int delta) {
		if (overlayColor != null && overlayColor instanceof AnimatingColor) {
			((AnimatingColor) overlayColor).update(delta);
		}
	}
	
	/**
	 * Add a {@code Component}.
	 * @param component New component to add
	 */
	public void add(Component component) { 
		components.add(component);
		component.setVisibleParent(this);
	}

	/**
	 * Remove a {@code Component}.
	 * @param component Component to remove
	 */
	public void remove(Component component) {
		components.remove(component);
		if (component.getVisibleParent() == this) {
			component.setVisibleParent(null);
		}
	}
	
	/**
	 * Sets the overlay {@code Color}.
	 * @param overlayColor Overlay Color
	 */
	public void setOverlayColor(Color overlayColor) {
		this.overlayColor = overlayColor;
	}
	
	/**
	 * Whether or not it is visible.
	 * @return Whether or not it is visible
	 */
	public boolean isVisible() {
		return super.isVisible();
	} 
}
