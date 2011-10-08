package scene;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import scene.layout.Layout;

import component.Component;

public class SceneLayer extends Component {
	
	protected Layout layout;
	
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
		if (layout != null) {
			layout.setComponentLocation(component);
		}
		
		components.add(component);
	}

	public void setLayout(Layout layout) {
		this.layout = layout;
	}
}
