package entity;

import java.util.*;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;


public class Entity implements MouseListener {
	private String id;
	private GameContainer container;

	private Vector2f position;
	private float scale;
	private float rotation;
	private int xDirection;

	private RenderComponent renderComponent;
	private List<Component> components;

	public Entity(String id, GameContainer container) {
		this.id = id;
		this.container = container;

		components = new ArrayList<Component>();

		position = new Vector2f(0, 0);
		scale = 1;
		rotation = 0;
		xDirection = 0;
		
		Input input = container.getInput();
		input.addMouseListener(this);
	}

	public void addComponent(Component component) {
		if (RenderComponent.class.isInstance(component)) {
			renderComponent = (RenderComponent) component;
		}

		component.setOwnerEntity(this);
		components.add(component);
	}

	public Component getComponent(String id) {
		for (Component comp : components) {
			if (comp.getId().equalsIgnoreCase(id)) {
				return comp;
			}
		}

		return null;
	}

	public Vector2f getPosition() {
		return position;
	}

	public float getScale() {
		return scale;
	}

	public float getRotation() {
		return rotation;
	}

	public String getId() {
		return id;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public void setRotation(float rotate) {
		rotation = rotate;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public float getWidth() {
		if (renderComponent != null) {
			return scale * renderComponent.getWidth();
		}
		
		return 0.0f;
	}
	
	public float getHeight() {
		if (renderComponent != null) {
			return scale * renderComponent.getHeight();
		}
		
		return 0.0f;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(position.x, position.y, getWidth(), getHeight());
	}

	public int getXDirection() {
		return xDirection;
	}
	
	public void setXDirection(int xDirection) {
		this.xDirection = xDirection;
	}
	
	public void update(GameContainer container, StateBasedGame game, int delta) {
		for (Component component : components) {
			component.update(container, game, delta);
		}
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		if (renderComponent != null) {
			renderComponent.render(container, game, g);
		}
	}

	@Override
	public void inputEnded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputStarted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isAcceptingInput() {
		return true;
	}

	@Override
	public void setInput(Input arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		if (getBounds().contains(x, y)) {
			scale = (scale >= 2) ? 1 : 2;
			position.y = container.getHeight() - this.getHeight();
		}
	}

	@Override
	public void mouseDragged(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseWheelMoved(int arg0) {
		// TODO Auto-generated method stub
		
	}
}
