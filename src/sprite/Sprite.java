package sprite;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.state.StateBasedGame;

public class Sprite extends AbstractComponent {
	private Animation leftAnimation;
	private Animation rightAnimation;
	private Animation currentAnimation;
	private Vector2f position;
	private float rotation;
	private float scale;
	
	private enum DirectionFacing { LEFT, RIGHT }
	private DirectionFacing xDirection = DirectionFacing.LEFT;
	
	public Sprite(GUIContext container, Animation rightAnimation, Animation leftAnimation) {
		super(container);
		
		this.rightAnimation = rightAnimation;
		this.leftAnimation = leftAnimation;
		
		xDirection = DirectionFacing.LEFT;
		
		position = new Vector2f(0, 0);
		scale = 1;
		rotation = 0;
	}

	public int getHeight() {
		return currentAnimation.getHeight();
	}

	public int getWidth() {
		return currentAnimation.getWidth();
	}

	public int getX() {
		return (int)position.getX();
	}

	public int getY() {
		return (int)position.getY();
	}

	public void render(GUIContext container, Graphics g) throws SlickException {
		currentAnimation.draw(position.getX(), position.getY());
	}
	
	public void update(GameContainer container, StateBasedGame game, int delta) {
		if (xDirection == DirectionFacing.LEFT) {
			currentAnimation = leftAnimation;
		} else {
			currentAnimation = rightAnimation;
		}
	}
	
	public void setLocation(int x, int y) {
		position.set(x, y);
	}
	
	public void setLocation(float x, float y) {
		position.set(x, y);
	}
}
