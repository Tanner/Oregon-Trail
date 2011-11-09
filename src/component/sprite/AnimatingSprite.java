package component.sprite;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

public class AnimatingSprite extends Sprite {
	private static final int MOVE_SPEED_REDUCTION = 6;
	
	private Animation leftAnimation;
	private Animation rightAnimation;
	private Animation currentAnimation;
	
	public static enum Direction { LEFT, RIGHT }
	private Direction xDirection = Direction.LEFT;
	
	/**
	 * Constructs an {@code AnimatingSprite} with a right and left animation.
	 * @param context The GUI context
	 * @param width The width
	 * @param animation Animation for when facing left
	 * @param direction Direction Direction facing
	 */
	public AnimatingSprite(GUIContext context, int width, Animation animation, Direction direction) {
		super(context, width, width * animation.getHeight() / animation.getWidth());
		
		animation.setAutoUpdate(false);
		
		Animation reflectedAnimation = new Animation();
		for (int i = 0; i < animation.getFrameCount(); i++) {
			reflectedAnimation.addFrame(animation.getImage(i).getFlippedCopy(true, false), animation.getDuration(i));
		}
		reflectedAnimation.setAutoUpdate(false);
		
		if (direction == Direction.LEFT) {
			this.leftAnimation = animation;
			this.rightAnimation = reflectedAnimation;
		} else {
			this.leftAnimation = reflectedAnimation;
			this.rightAnimation = animation;
		}
		
		xDirection = Direction.LEFT;
		currentAnimation = leftAnimation;
	}
	
	/**
	 * Constructs an {@code AnimatingSprite} with a right and left animation.
	 * @param context The GUI context
	 * @param width The width
	 * @param animation Animation for when facing left
	 * @param direction Direction Direction facing
	 */
	public AnimatingSprite(GUIContext context, int width, Animation leftAnimation, Animation rightAnimation, Direction direction) {
		super(context, width, width * leftAnimation.getHeight() / leftAnimation.getWidth());
		
		this.leftAnimation = leftAnimation;
		this.rightAnimation = rightAnimation;
		
		leftAnimation.setAutoUpdate(false);
		rightAnimation.setAutoUpdate(false);
		
		xDirection = Direction.LEFT;
		currentAnimation = leftAnimation;
	}
	
	/**
	 * Updates the Sprite on a clock cycle.
	 * @param container Container displaying the component
	 * @param delta Time since last update
	 */
	public void update(GameContainer container, int delta) {
		if (xDirection == Direction.LEFT) {
			currentAnimation = leftAnimation;
		} else {
			currentAnimation = rightAnimation;
		}
		
		currentAnimation.update(delta);
	}
	
	@Override
	public void render(GUIContext context, Graphics g) throws SlickException {
		if (!isVisible()) {
			return;
		}
		
		super.render(context, g);
		
		if (currentAnimation != null) {
			currentAnimation.draw(getX(), getY(), getWidth(), getHeight());
		}
	}
	
	/**
	 * Get which direction the Sprite is facing.
	 * @return Direction the Sprite is facing
	 */
	public Direction getDirectionFacing() {
		return xDirection;
	}
	
	/**
	 * Sets the direction the Sprite is facing
	 * @param xDirection New direction the Sprite is facing
	 */
	public void setDirectionFacing(Direction xDirection) {
		this.xDirection = xDirection;
	}

	public void moveLeft(int delta) {
		delta /= MOVE_SPEED_REDUCTION;
		setLocation(getX() - delta, getY());
		
		setDirectionFacing(Direction.LEFT);
	}

	public void moveRight(int delta) {
		delta /= MOVE_SPEED_REDUCTION;
		setLocation(getX() + delta, getY());
		
		setDirectionFacing(Direction.RIGHT);
	}
}
