package scene;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import component.Component;

import scene.layout.Layout;


public abstract class Scene extends BasicGameState {
	public static SceneID ID = SceneID.Default;
	protected List<Component> components;
	protected Layout layout;
	
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		components = new ArrayList<Component>();
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		for (Component component : components) {
			component.render(container, g);
		}
	}

	public abstract void update(GameContainer container, StateBasedGame game, int delta) throws SlickException;
	
	public void add(Component component) {
		if (layout != null) {
			layout.setComponentLocation(component);
		}
		
		components.add(component);
	}
	
	@Override
	public int getID() {
		return ID.ordinal();
	}
}
