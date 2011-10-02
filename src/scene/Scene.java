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
	
	public enum ReferencePoint {
		TopLeft, TopCenter, TopRight, CenterLeft, CenterCenter, CenterRight, BottomLeft, BottomCenter, BottomRight
	}
	
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
	
	public void add(Component component, int x, int y, ReferencePoint referencePoint) {
		switch (referencePoint) {
		case TopLeft:
			component.setLocation(x, y);
			break;
		case TopCenter:
			component.setLocation(x - component.getWidth()/2, y);
			break;
		case TopRight:
			component.setLocation(x - component.getWidth(), y);
			break;
		case CenterLeft:
			component.setLocation(x, y - component.getHeight()/2);
			break;
		case CenterCenter:
			component.setLocation(x - component.getWidth()/2, y - component.getHeight()/2);
			break;
		case CenterRight:
			component.setLocation(x - component.getWidth(), y - component.getHeight()/2);
			break;
		case BottomLeft:
			component.setLocation(x, y - component.getHeight());
			break;
		case BottomCenter:
			component.setLocation(x - component.getWidth()/2, y - component.getHeight());
			break;
		case BottomRight:
			component.setLocation(x - component.getWidth(), y - component.getHeight());
			break;
		}
		
		components.add(component);
	}
	
	@Override
	public int getID() {
		return ID.ordinal();
	}
}
