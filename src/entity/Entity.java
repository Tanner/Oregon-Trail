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

	private Vector2f position;
	private float scale;
	private float rotation;
	private int xDirection;

	private RenderComponent renderComponent;
	private List<Component> components;
	private Queue<AnimationComponent> animationComponents;
	
	Input input;

	public Entity(String id, GameContainer container) {
		this.id = id;

		components = new ArrayList<Component>();
		animationComponents = new LinkedList<AnimationComponent>();

		position = new Vector2f(0, 0);
		scale = 1;
		rotation = 0;
		xDirection = 0;
		
		input = container.getInput();
		input.addMouseListener(this);
	}

	public void setRenderComponent(RenderComponent component) {
		this.renderComponent = component;
		component.setOwnerEntity(this);
	}
	
	public void addComponent(Component component) {
		if (component instanceof RenderComponent) {
			setRenderComponent((RenderComponent)component);
			return;
		}
		
		if (component instanceof AnimationComponent) {
			addAnimationComponent((AnimationComponent)component);
			return;
		}
		
		components.add(component);
		component.setOwnerEntity(this);
	}
	
	public void addAnimationComponent(AnimationComponent component) {
		animationComponents.add(component);
		component.setOwnerEntity(this);
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
		renderComponent.update(container, game, delta);
		
		for (Component component : components) {
			component.update(container, game, delta);
		}
		
		if (!animationComponents.isEmpty()) {
			animationComponents.peek().update(container, game, delta);
		}
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		if (renderComponent != null) {
			renderComponent.render(container, game, g);
		}
	}
	
	public void animationDidStop(AnimationComponent animationComponent) {
		animationComponents.remove(animationComponent);
	}

	@Override
	public void inputEnded() { }

	@Override
	public void inputStarted() { }

	@Override
	public boolean isAcceptingInput() {
		return true;
	}

	@Override
	public void setInput(Input arg0) { }

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		if (getBounds().contains(x, y)) {
			addAnimationComponent(new PowerUpAnimation());
		}
	}

	@Override
	public void mouseDragged(int arg0, int arg1, int arg2, int arg3) { }

	@Override
	public void mouseMoved(int arg0, int arg1, int arg2, int arg3) { }

	@Override
	public void mousePressed(int arg0, int arg1, int arg2) { }

	@Override
	public void mouseReleased(int arg0, int arg1, int arg2) { 	}

	@Override
	public void mouseWheelMoved(int arg0) { }
}
