package scene;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import component.Component;

public class SceneLayer extends Component {
	private Scene scene;
	
	public SceneLayer(GUIContext container, Scene scene) {
		super(container, container.getWidth(), container.getHeight());
		setLocation(0, 0);
		setVisible(true);
	}

	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		g.setWorldClip(getX(), getY(), container.getWidth(), getHeight());
		super.render(container, g);
		g.clearWorldClip();
		
		g.translate(getX(), getY());
	}
	
	public void add(Component component) { 
		components.add(component);
		component.setParentComponent(this);
	}
	
	public boolean isVisible() {
		return true;
	}
}
