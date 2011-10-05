package scene;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import component.Background;

public class ModalSceneLayer extends SceneLayer {

	public ModalSceneLayer(GUIContext container) {
		super(container);
		
		add(new Background(container, new Color(0f, 0f, 0f, 0.25f)));
	}

	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		if (!visible) {
			return;
		}
		
		super.render(container, g);
	}
}
