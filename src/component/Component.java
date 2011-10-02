package component;

import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

public abstract class Component extends AbstractComponent {
	public Component(GUIContext container) {
		super(container);
	}

	public void setCenter(int x, int y) {
		setLocation(x - getWidth()/2, y - getHeight()/2);
	}
}
