package scene;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import component.Component;

public class SceneLayer extends Component {
		
	public SceneLayer(GUIContext container) {
		super(container, container.getWidth(), container.getHeight());
		setLocation(0, 0);
	}

	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		super.render(container, g);
		
		g.translate(getX(), getY());
	}
	
	public void add(Component component) {
		components.add(component);
	}
}
