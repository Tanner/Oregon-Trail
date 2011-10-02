package sprite;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.state.StateBasedGame;

import component.Component;

/**
 * This class represents a sprite with an left and right animation.
 * 
 * @author Tanner Smith
 */
public class Sprite extends Component {
	private Animation leftAnimation;
	private Animation rightAnimation;
	private Animation currentAnimation;
	private Vector2f position;
	private float rotation;
	private float scale;
	
	public static enum DirectionFacing { LEFT, RIGHT }
	private DirectionFacing xDirection = DirectionFacing.LEFT;
	
	/**
	 * Constructs a Sprite.
	 * @param container Container displaying the component
	 * @param rightAnimation Animation for when facing right
	 * @param leftAnimation Animation for when facing left
	 */
	public Sprite(GUIContext container, Animation rightAnimation, Animation leftAnimation) {
		super(container);
		
		this.rightAnimation = rightAnimation;
		this.leftAnimation = leftAnimation;
		
		xDirection = DirectionFacing.LEFT;
		currentAnimation = leftAnimation;
		
		position = new Vector2f(0, 0);
		scale = 1;
		rotation = 0;
	}
	
	/**
	 * Constructs a Sprite.
	 * @param container Container displaying the component
	 * @param rightImage Image for when facing right
	 * @param leftImage Image for when facing left
	 */
	public Sprite(GUIContext container, Image rightImage, Image leftImage) {
		this(container, new Animation(new Image[]{rightImage}, 1), new Animation(new Image[]{leftImage}, 1));
	}
	
	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		currentAnimation.draw(position.getX(), position.getY());
	}
	
	/**
	 * Updates the Sprite on a clock cycle.
	 * @param container Container displaying the component
	 * @param game Game containing the Entity
	 * @param delta Time since last update
	 */
	public void update(GameContainer container, StateBasedGame game, int delta) {
		if (xDirection == DirectionFacing.LEFT) {
			currentAnimation = leftAnimation;
		} else {
			currentAnimation = rightAnimation;
		}
		
		currentAnimation.update(delta);
	}

	@Override
	public int getHeight() {
		return (int)(scale * currentAnimation.getHeight());
	}

	@Override
	public int getWidth() {
		return (int)(scale *currentAnimation.getWidth());
	}

	@Override
	public int getX() {
		return (int)position.getX();
	}

	@Override
	public int getY() {
		return (int)position.getY();
	}
	
	/**
	 * Get the rotation.
	 * @return Rotation of the Sprite
	 */
	public float getRotation() {
		return rotation;
	}
	
	/**
	 * Get which direction the Sprite is facing.
	 * @return Direction the Sprite is facing
	 */
	public DirectionFacing getDirectionFacing() {
		return xDirection;
	}
	
	/**
	 * Get the scale.
	 * @return Scale of the Sprite
	 */
	public float getScale() {
		return scale;
	}
	
	/**
	 * Get the bounding box of this Sprite.
	 * @return Rectangle bounding box of the Sprite
	 */
	public Rectangle getBounds() {
		return new Rectangle(position.x, position.y, getWidth(), getHeight());
	}
	
	@Override
	public void setLocation(int x, int y) {
		if (position == null) {
			position = new Vector2f(x, y);
		} else {
			position.set(x, y);
		}
	}
	
	/**
	 * Set the location of the Sprite
	 * @param x New X location
	 * @param y New Y location
	 */
	public void setLocation(float x, float y) {
		position.set(x, y);
	}
	
	/**
	 * Set the scale of the Sprite
	 * @param scale New scale
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	/**
	 * Set the rotation of the Sprite
	 * @param rotation New rotation in degrees
	 */
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	
	/**
	 * Sets the direction the Sprite is facing
	 * @param xDirection New direction the Sprite is facing
	 */
	public void setDirectionFacing(DirectionFacing xDirection) {
		this.xDirection = xDirection;
	}
}
